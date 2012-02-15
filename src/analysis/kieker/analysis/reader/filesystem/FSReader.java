/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.analysis.reader.filesystem;

import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;

import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.internal.NullRecord;

/**
 * Filesystem reader which reads from multiple directories simultaneously ordered by the logging timestamp.
 * TODO: check correct handling of errors!
 * 
 * @author Andre van Hoorn, Jan Waller
 */
@Plugin(outputPorts = @OutputPort(name = FSReader.OUTPUT_PORT_NAME, eventTypes = { IMonitoringRecord.class }, description = "Output Port of the FSReader"))
public class FSReader extends AbstractReaderPlugin implements IMonitoringRecordReceiver {

	public static final String OUTPUT_PORT_NAME = "defaultOutput";
	public static final String CONFIG_INPUTDIRS = FSReader.class.getName() + ".inputDirs";
	public static final String CONFIG_ONLYRECORDS = FSReader.class.getName() + ".readOnlyRecordsOfType";

	public static final IMonitoringRecord EOF = new NullRecord();

	private static final Log LOG = LogFactory.getLog(FSReader.class);

	private final Set<Class<? extends IMonitoringRecord>> readOnlyRecordsOfType;
	private final String[] inputDirs;
	private final PriorityQueue<IMonitoringRecord> recordQueue;

	private volatile boolean running = true;

	public FSReader(final Configuration configuration) {
		super(configuration);
		this.inputDirs = this.configuration.getStringArrayProperty(FSReader.CONFIG_INPUTDIRS);
		this.recordQueue = new PriorityQueue<IMonitoringRecord>(this.inputDirs.length);
		final String[] onlyrecords = this.configuration.getStringArrayProperty(FSReader.CONFIG_ONLYRECORDS);
		if (onlyrecords.length == 0) {
			this.readOnlyRecordsOfType = null;
		} else {
			this.readOnlyRecordsOfType = new HashSet<Class<? extends IMonitoringRecord>>(onlyrecords.length);
			for (final String classname : onlyrecords) {
				try {
					final Class<? extends IMonitoringRecord> recClass = AbstractMonitoringRecord.classForName(classname);
					this.readOnlyRecordsOfType.add(recClass);
				} catch (final MonitoringRecordException ex) {
					FSReader.LOG.warn(ex.getMessage(), ex.getCause());
				}
			}
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration defaultConfiguration = new Configuration();
		defaultConfiguration.setProperty(FSReader.CONFIG_INPUTDIRS, "");
		defaultConfiguration.setProperty(FSReader.CONFIG_ONLYRECORDS, "");
		return defaultConfiguration;
	}

	@Override
	public void terminate() {
		FSReader.LOG.info("Shutting down reader.");
		this.running = false;
	}

	@Override
	public boolean read() {
		// start all reader
		for (final String inputDir : this.inputDirs) {
			new Thread(new FSDirectoryReader(inputDir, this, this.readOnlyRecordsOfType)).start(); // NOPMD (new in loop)
		}
		// consume incoming records
		int readingReaders = this.inputDirs.length;
		while (readingReaders > 0) {
			synchronized (this.recordQueue) { // with newMonitoringRecord()
				while (this.recordQueue.size() < readingReaders) {
					try {
						this.recordQueue.wait();
					} catch (final InterruptedException ex) {
						// ignore InterruptedException
					}
				}
			}
			final IMonitoringRecord record = this.recordQueue.remove();
			synchronized (record) { // with newMonitoringRecord()
				record.notifyAll();
			}
			if (record == FSReader.EOF) {
				readingReaders--;
			} else {
				super.deliver(FSReader.OUTPUT_PORT_NAME, record);
			}
		}
		return true;
	}

	/**
	 * This method is called for each new record by each ReaderThread.
	 */
	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		synchronized (record) { // with read()
			synchronized (this.recordQueue) { // with read()
				this.recordQueue.add(record);
				this.recordQueue.notifyAll();
			}
			try {
				record.wait();
			} catch (final InterruptedException ex) {
				// ignore InterruptedException
			}
		}
		return this.running;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(FSReader.CONFIG_INPUTDIRS, Configuration.toProperty(this.inputDirs));
		/* Extract the names of the record-classes again. */
		if (this.readOnlyRecordsOfType == null) {
			configuration.setProperty(FSReader.CONFIG_ONLYRECORDS, Configuration.toProperty(new String[] {}));
		} else {
			final int len = this.readOnlyRecordsOfType.size();
			final String[] onlyRecords = new String[len];
			final Iterator<Class<? extends IMonitoringRecord>> iter = this.readOnlyRecordsOfType.iterator();
			for (int i = 0; i < len; i++) {
				onlyRecords[i] = iter.next().getName();
			}
			configuration.setProperty(FSReader.CONFIG_ONLYRECORDS, Configuration.toProperty(onlyRecords));
		}
		return configuration;
	}
}

interface IMonitoringRecordReceiver {
	public abstract boolean newMonitoringRecord(IMonitoringRecord record);
}
