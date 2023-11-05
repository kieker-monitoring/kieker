/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
import kieker.analysis.plugin.reader.util.IMonitoringRecordReceiver;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.common.util.filesystem.FSUtil;

/**
 * Filesystem reader which reads from multiple directories simultaneously ordered by the logging timestamp.
 *
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 0.95a
 * @deprecated 1.15 replaced by teetime log reading facilities
 */
@Deprecated
@Plugin(description = "A file system reader which reads records from multiple directories", outputPorts = {
	@OutputPort(name = FSReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = IMonitoringRecord.class,
			description = "Output Port of the FSReader") },
		configuration = {
			@Property(name = FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, defaultValue = ".",
					description = "The name of the input dirs used to read data (multiple dirs are separated by |)."),
			@Property(name = FSReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES,
					defaultValue = "false", description = "Ignore unknown records? Aborts if encountered and value is false.")
		})
public class FSReader extends AbstractReaderPlugin implements IMonitoringRecordReceiver {

	/** The name of the output port delivering the record read by this plugin. */
	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	/** The name of the configuration determining the input directories for this plugin. */
	public static final String CONFIG_PROPERTY_NAME_INPUTDIRS = "inputDirs";
	/** The name of the configuration determining whether the reader ignores unknown record types or not. */
	public static final String CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES = "ignoreUnknownRecordTypes";

	/** This dummy record can be send to the reader's record queue to mark the end of the current file. */
	private static final IMonitoringRecord EOF = new EmptyRecord();

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
			// Workaround for #1323
			if (!".".equals(this.inputDirs[i])) {
				this.inputDirs[i] = Configuration.convertToPath(this.inputDirs[i]);
			}
		}
		if (nDirs == 0) {
			this.logger.warn("The list of input dirs passed to the {} is empty", FSReader.class.getSimpleName());
			nDirs = 1;
		}
		this.recordQueue = new PriorityQueue<>(nDirs);
		this.ignoreUnknownRecordTypes = this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate(final boolean error) {
		this.logger.info("Shutting down reader.");
		this.running = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean read() {
		// start all reader
		int notInitializesReaders = 0;
		for (final String inputDirFn : this.inputDirs) {
			// Make sure that white spaces in paths are handled correctly
			final File inputDir = new File(inputDirFn);

			final Thread readerThread;
			if (inputDir.isDirectory()) {
				readerThread = new Thread(new FSDirectoryReader(inputDir, this, this.ignoreUnknownRecordTypes));
			} else if (inputDir.isFile() && inputDirFn.endsWith(FSUtil.ZIP_FILE_EXTENSION)) {
				readerThread = new Thread(new FSZipReader(inputDir, this, this.ignoreUnknownRecordTypes));
			} else {
				this.logger.warn("Invalid Directory or filename (no Kieker log): {}", inputDirFn);
				notInitializesReaders++;
				continue;
			}
			readerThread.setDaemon(true);
			readerThread.start();
		}
		// consume incoming records
		int readingReaders = this.inputDirs.length - notInitializesReaders;
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
				record.notifyAll(); // NOFB this is an issue, but the code is legacy
			}
			if (record != EOF) { // NOPMD (CompareObjectsWithEquals)
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
	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		synchronized (record) { // with read()
			synchronized (this.recordQueue) { // with read()
				this.recordQueue.add(record);
				this.recordQueue.notifyAll();
			}
			try {
				record.wait(); // NOFB this is an issue, but the code is legacy
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

	@Override
	public void newEndOfFileRecord() {
		this.newMonitoringRecord(EOF);
	}
}
