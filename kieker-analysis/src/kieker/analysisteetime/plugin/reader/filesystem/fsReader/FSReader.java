/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.plugin.reader.filesystem.fsReader;

import java.io.File;
import java.util.PriorityQueue;

import kieker.analysis.plugin.reader.filesystem.FSZipReader;
import kieker.analysis.plugin.reader.util.IMonitoringRecordReceiver;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.common.util.filesystem.FSUtil;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import teetime.framework.AbstractProducerStage;

/**
 * Filesystem reader which reads from multiple directories simultaneously ordered by the logging timestamp.
 *
 * @author Andre van Hoorn, Jan Waller, Lars Bluemke
 *
 * @since 0.95a
 *
 * @deprecated 1.15
 */
@Deprecated
public class FSReader extends AbstractProducerStage<IMonitoringRecord> implements IMonitoringRecordReceiver {

	/** This dummy record can be send to the reader's record queue to mark the end of the current file. */
	private static final IMonitoringRecord EOF = new EmptyRecord();

	private final boolean ignoreUnknownRecordTypes;

	private final String[] inputDirs;
	private final PriorityQueue<IMonitoringRecord> recordQueue;

	private volatile boolean running = true;

	/**
	 * Creates an instance of this class using the given parameters.
	 *
	 * @param inputDirs
	 *            Determines the input directories for this plugin.
	 * @param ignoreUnknownRecordTypes
	 *            Determines whether the reader ignores unknown record types or not.
	 */
	public FSReader(final String[] inputDirs, final boolean ignoreUnknownRecordTypes) {
		this.inputDirs = inputDirs.clone();

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
		this.ignoreUnknownRecordTypes = ignoreUnknownRecordTypes;
	}

	@Override
	public void terminateStage() {
		this.logger.info("Shutting down reader.");
		this.running = false;
		super.terminateStage();
	}

	@Override
	@SuppressFBWarnings("NN_NAKED_NOTIFY")
	protected void execute() {
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
				record.notifyAll();
			}
			if (record == EOF) { // NOPMD (CompareObjectsWithEquals)
				readingReaders--;
			} else {
				this.outputPort.send(record);
			}
		}
	}

	@Override
	@SuppressFBWarnings("WA_NOT_IN_LOOP")
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
	public void newEndOfFileRecord() {
		this.newMonitoringRecord(EOF);
	}

	public boolean isIgnoreUnknownRecordTypes() {
		return this.ignoreUnknownRecordTypes;
	}

}
