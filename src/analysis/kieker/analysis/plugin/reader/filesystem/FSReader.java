/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

import java.io.File;
import java.util.PriorityQueue;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.common.util.filesystem.FSConstants;

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
			@Property(name = FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, defaultValue = ".",
					description = "The name of the input dirs used to read data (multiple dirs are separated by |)."),
			@Property(name = FSReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, defaultValue = "false",
					description = "Ignore unknown records? Aborts if encountered and value is false.")
		})
public class FSReader extends AbstractReaderPlugin implements IMonitoringRecordReceiver, FSConstants {

	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	public static final String CONFIG_PROPERTY_NAME_INPUTDIRS = "inputDirs";
	public static final String CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES = "ignoreUnknownRecordTypes";

	public static final IMonitoringRecord EOF = new EmptyRecord();

	private static final Log LOG = LogFactory.getLog(FSReader.class);

	private final boolean ignoreUnknownRecordTypes;

	private final String[] inputDirs;
	private final PriorityQueue<IMonitoringRecord> recordQueue;

	private volatile boolean running = true;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public FSReader(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.inputDirs = this.configuration.getStringArrayProperty(CONFIG_PROPERTY_NAME_INPUTDIRS);
		int nDirs = this.inputDirs.length;
		for (int i = 0; i < nDirs; i++) {
			this.inputDirs[i] = Configuration.convertToPath(this.inputDirs[i]);
		}
		if (nDirs == 0) {
			LOG.warn("The list of input dirs passed to the " + FSReader.class.getSimpleName() + " is empty");
			nDirs = 1;
		}
		this.recordQueue = new PriorityQueue<IMonitoringRecord>(nDirs);
		this.ignoreUnknownRecordTypes = this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * 
	 * @deprecated To be removed in Kieker 1.8.
	 */
	@Deprecated
	public FSReader(final Configuration configuration) {
		this(configuration, null);
	}

	/**
	 * {@inheritDoc}
	 */
	public void terminate(final boolean error) {
		LOG.info("Shutting down reader.");
		this.running = false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean read() {
		// start all reader
		for (final String inputDirFn : this.inputDirs) {
			final File inputDir = new File(inputDirFn);
			final Thread readerThread;
			if (inputDir.isDirectory()) {
				readerThread = new Thread(new FSDirectoryReader(inputDir, this, this.ignoreUnknownRecordTypes));
			} else if (inputDir.isFile() && inputDirFn.endsWith(ZIP_FILE_EXTENSION)) {
				readerThread = new Thread(new FSZipReader(inputDir, this, this.ignoreUnknownRecordTypes));
			} else {
				LOG.warn("Invalid Directory or filename (no Kieker log): " + inputDirFn);
				continue;
			}
			readerThread.setDaemon(true);
			readerThread.start();
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
	 * {@inheritDoc}
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(this.inputDirs));
		configuration.setProperty(CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, Boolean.toString(this.ignoreUnknownRecordTypes));
		return configuration;
	}
}

/**
 * This is a simple interface showing that the {@link FSReader} can receive records. This is mostly a relict from an older version.
 * 
 * @author Andre van Hoorn, Jan Waller
 */
interface IMonitoringRecordReceiver {

	/**
	 * This method is called for each new record by each ReaderThread.
	 * 
	 * @param record
	 *            The record to be processed.
	 * @return true if and only if the record has been handled correctly.
	 */
	public abstract boolean newMonitoringRecord(IMonitoringRecord record);
}
