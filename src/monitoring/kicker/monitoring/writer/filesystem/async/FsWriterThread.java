/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.monitoring.writer.filesystem.async;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;

import kicker.common.record.IMonitoringRecord;
import kicker.monitoring.core.controller.IMonitoringController;
import kicker.monitoring.writer.filesystem.map.MappingFileWriter;

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

	public FsWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final MappingFileWriter mappingFileWriter, final String path, final int maxEntiresInFile, final int maxLogSize, final int maxLogFiles,
			final boolean autoflush, final int bufferSize) {
		super(monitoringController, writeQueue, mappingFileWriter, path, maxEntiresInFile, maxLogSize, maxLogFiles);
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
