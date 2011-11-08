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

import java.util.Collection;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.analysis.configuration.Configuration;
import kieker.analysis.plugin.AbstractMonitoringReader;
import kieker.analysis.plugin.configuration.OutputPort;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.DummyMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;

/**
 * Filesystem reader which reads from multiple directories simultaneously ordered by the logging timestamp.
 * TODO: check correct handling of errors!
 * 
 * @author Andre van Hoorn, Jan Waller
 */
public class FSReader extends AbstractMonitoringReader implements IMonitoringRecordReceiver {

	private static final String PREFIX = AbstractMonitoringReader.class.getName() + ".";
	public static final String CONFIG_INPUTDIRS = FSReader.PREFIX + "inputDirs";
	public static final String CONFIG_ONLYRECORDS = FSReader.PREFIX + "readOnlyRecordsOfType";

	public static final IMonitoringRecord EOF = new DummyMonitoringRecord();

	private static final Log LOG = LogFactory.getLog(FSReader.class);

	private static final Collection<Class<?>> OUT_CLASSES = Collections
			.unmodifiableCollection(new CopyOnWriteArrayList<>(new Class<?>[] { IMonitoringRecord.class }));

	private final String[] readOnlyRecordsOfType;
	private final String[] inputDirs;
	private final PriorityQueue<IMonitoringRecord> recordQueue;
	private final OutputPort outputPort;

	private volatile boolean running = true;

	public FSReader(final Configuration configuration) {
		super(configuration);
		this.inputDirs = this.configuration.getStringArrayProperty(FSReader.CONFIG_INPUTDIRS);
		this.recordQueue = new PriorityQueue<IMonitoringRecord>(this.inputDirs.length);
		final String[] onlyrecords = this.configuration.getStringArrayProperty(FSReader.CONFIG_ONLYRECORDS);
		if (onlyrecords.length == 0) {
			this.readOnlyRecordsOfType = null;
		} else {
			this.readOnlyRecordsOfType = onlyrecords;
		}
		// TODO: improve description string
		this.outputPort = new OutputPort("Output Port of the FSReader", FSReader.OUT_CLASSES);
		super.registerOutputPort("out", this.outputPort);
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
				this.outputPort.deliver(record);
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
}
