/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.tt.reader.filesystem.fsReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import kieker.analysis.plugin.reader.util.IMonitoringRecordReceiver;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;

import teetime.framework.AbstractProducerStage;

/**
 * Filesystem reader which reads from multiple directories simultaneously ordered by the logging timestamp.
 *
 * @author Andre van Hoorn, Jan Waller, Lars Bluemke
 *
 * @since 0.95a
 *
 * @deprecated since 1.15 removed 1.16
 */
@Deprecated
public class BinaryLogReader extends AbstractProducerStage<IMonitoringRecord> implements IMonitoringRecordReceiver {

	/** This dummy record can be send to the reader's record queue to mark the end of the current file. */
	private static final IMonitoringRecord EOF = new EmptyRecord();

	private final String[] inputDirs;
	private final PriorityQueue<IMonitoringRecord> recordQueue;

	private final boolean shouldDecompress;

	private final List<AbstractLogReaderThread> readerThreads = new ArrayList<>();

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public BinaryLogReader(final String[] inputDirs, final boolean shouldDecompress) {

		this.inputDirs = inputDirs.clone();
		int nDirs = this.inputDirs.length;
		for (int i = 0; i < nDirs; i++) {
			// Workaround for #1323
			if (!".".equals(this.inputDirs[i])) {
				this.inputDirs[i] = Configuration.convertToPath(this.inputDirs[i]);
			}
		}
		if (nDirs == 0) {
			this.logger.warn("The list of input dirs passed to the " + BinaryLogReader.class.getSimpleName() + " is empty");
			nDirs = 1;
		}
		this.recordQueue = new PriorityQueue<>(nDirs);

		this.shouldDecompress = shouldDecompress;
	}

	@Override
	public void terminateStage() {
		this.logger.info("Shutting down reader.");
		for (final AbstractLogReaderThread readerThread : this.readerThreads) {
			readerThread.terminate();
		}
		super.terminateStage();
	}

	@Override
	// @SuppressFBWarnings("NN_NAKED_NOTIFY")
	protected void execute() {
		// start all reader
		int notInitializesReaders = 0;
		for (final String inputDirFn : this.inputDirs) {
			// Make sure that white spaces in paths are handled correctly
			final File inputDir = new File(inputDirFn);

			if (inputDir.isDirectory()) {
				final AbstractLogReaderThread readerThread = new BinaryLogReaderThread(inputDir, this, this.shouldDecompress);
				readerThread.setDaemon(true);
				this.readerThreads.add(readerThread);
				readerThread.start();
			} else {
				this.logger.warn("Invalid Directory or filename (no Kieker log): " + inputDirFn);
				notInitializesReaders++;
				continue;
			}
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
		this.terminateStage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	// @SuppressFBWarnings("WA_NOT_IN_LOOP")
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
		return true;
	}

	@Override
	public void newEndOfFileRecord() {
		this.newMonitoringRecord(EOF);
	}
}
