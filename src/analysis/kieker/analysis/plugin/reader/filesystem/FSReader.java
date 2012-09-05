/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.reader.filesystem;

import java.util.PriorityQueue;

import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;

// TODO: check correct handling of errors by creating suitable tests in kieker.test.tools.junit.writeRead.filesystem!
/**
 * Filesystem reader which reads from multiple directories simultaneously ordered by the logging timestamp.
 * 
 * @author Andre van Hoorn, Jan Waller
 */
@Plugin(description = "A file system reader which reads records from multiple directories",
		outputPorts = {
			@OutputPort(name = FSReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class }, description = "Output Port of the FSReader") },
		configuration = {
			@Property(name = FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, defaultValue = "."),
			@Property(name = FSReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, defaultValue = FSReader.CONFIG_PROPERTY_VALUE_IGNORE_UNKNOWN_RECORD_TYPES_DEFAULT)
		})
public class FSReader extends AbstractReaderPlugin implements IMonitoringRecordReceiver {

	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	public static final String CONFIG_PROPERTY_NAME_INPUTDIRS = "inputDirs";
	public static final String CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES = "ignoreUnknownRecordTypes";

	public static final String CONFIG_PROPERTY_VALUE_IGNORE_UNKNOWN_RECORD_TYPES_DEFAULT = "false";

	public static final IMonitoringRecord EOF = new EmptyRecord();

	private static final Log LOG = LogFactory.getLog(FSReader.class);

	private final boolean ignoreUnknownRecordTypes;

	private final String[] inputDirs;
	private final PriorityQueue<IMonitoringRecord> recordQueue;

	private volatile boolean running = true;

	public FSReader(final Configuration configuration) {
		super(configuration);
		this.inputDirs = this.configuration.getStringArrayProperty(CONFIG_PROPERTY_NAME_INPUTDIRS);
		for (int i = 0; i < this.inputDirs.length; i++) {
			this.inputDirs[i] = Configuration.convertToPath(this.inputDirs[i]);
		}
		if (this.inputDirs.length == 0) {
			LOG.warn("The list of input dirs passed to the " + FSReader.class.getSimpleName() + " is empty");
		}
		this.recordQueue = new PriorityQueue<IMonitoringRecord>(this.inputDirs.length);
		this.ignoreUnknownRecordTypes = this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES);
	}

	public void terminate(final boolean error) {
		LOG.info("Shutting down reader.");
		this.running = false;
	}

	public boolean read() {
		// start all reader
		for (final String inputDir : this.inputDirs) {
			new Thread(new FSDirectoryReader(inputDir, this, this.ignoreUnknownRecordTypes)).start();
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
			if (record == EOF) { // NOPMD (CompareObjectsWithEquals)
				readingReaders--;
			} else {
				super.deliver(OUTPUT_PORT_NAME_RECORDS, record);
			}
		}
		return true;
	}

	/**
	 * This method is called for each new record by each ReaderThread.
	 */

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

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(this.inputDirs));
		configuration.setProperty(CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, Boolean.toString(this.ignoreUnknownRecordTypes));
		return configuration;
	}
}

/**
 * @author Andre van Hoorn, Jan Waller
 */
interface IMonitoringRecordReceiver {
	public abstract boolean newMonitoringRecord(IMonitoringRecord record);
}
