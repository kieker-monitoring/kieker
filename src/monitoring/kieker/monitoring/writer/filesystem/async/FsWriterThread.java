/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.writer.filesystem.async;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.filesystem.map.MappingFileWriter;

/**
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 * 
 * @since 1.5
 */
public final class FsWriterThread extends AbstractFsWriterThread {

	private static final String ENCODING = "UTF-8";

	private PrintWriter pos = null; // NOPMD (init for findbugs)
	private final boolean autoflush;
	private final int bufferSize;

	/**
	 * Create a new FsWriterThread.
	 * 
	 * @param monitoringController
	 *            the monitoring controller accessed by this thread
	 * @param writeQueue
	 *            the queue where the writer fetches its records from
	 * @param mappingFileWriter
	 *            writer for the mapping file (the file where class names are mapped to record ids)
	 * @param path
	 *            location where to files should go to (the path must point to a directory)
	 * @param maxEntriesInFile
	 *            limit for the number of records per log file
	 * @param maxLogSize
	 *            limit of the log file size
	 * @param maxLogFiles
	 *            limit of the number of log files
	 * @param autoflush
	 *            if true do not use an output buffer while writing
	 * @param bufferSize
	 *            size of the output buffer
	 */
	public FsWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final MappingFileWriter mappingFileWriter, final String path, final int maxEntriesInFile, final int maxLogSize, final int maxLogFiles,
			final boolean autoflush, final int bufferSize) {
		super(monitoringController, writeQueue, mappingFileWriter, path, maxEntriesInFile, maxLogSize, maxLogFiles);
		this.autoflush = autoflush;
		this.bufferSize = bufferSize;
	}

	@Override
	protected final void write(final IMonitoringRecord monitoringRecord) throws IOException {
		final Object[] recordFields = monitoringRecord.toArray();
		final StringBuilder sb = new StringBuilder(256);
		sb.append('$');
		sb.append(this.monitoringController.getUniqueIdForString(monitoringRecord.getClass().getName()));
		sb.append(';');
		sb.append(monitoringRecord.getLoggingTimestamp());
		for (final Object recordField : recordFields) {
			sb.append(';');
			sb.append(String.valueOf(recordField));
		}
		this.pos.println(sb.toString());
	}

	/**
	 * Prepares a new file if needed.
	 * 
	 * @param filename
	 *            The name of the file to be prepared.
	 * 
	 * @throws FileNotFoundException
	 *             If the given file is somehow invalid.
	 * @throws UnsupportedEncodingException
	 *             If the used default encoding is not supported.
	 */
	@Override
	protected final void prepareFile(final String filename) throws FileNotFoundException, UnsupportedEncodingException {
		if (this.pos != null) {
			this.pos.close();
		}
		if (this.autoflush) {
			this.pos = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename), ENCODING), true);
		} else {
			this.pos = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), ENCODING), this.bufferSize), false);
		}
		this.pos.flush();
	}

	@Override
	protected final void cleanup() {
		if (this.pos != null) {
			this.pos.close();
		}
	}
}
