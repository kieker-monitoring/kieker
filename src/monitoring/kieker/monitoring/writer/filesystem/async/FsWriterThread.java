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
import kieker.monitoring.writer.filesystem.MappingFileWriter;

/**
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 */
public final class FsWriterThread extends AbstractFsWriterThread {

	private static final String ENCODING = "UTF-8";

	private PrintWriter pos = null; // NOPMD (init for findbugs)
	private final boolean autoflush;

	public FsWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final MappingFileWriter mappingFileWriter, final String path, final int maxEntiresInFile, final boolean autoflush) {
		super(monitoringController, writeQueue, mappingFileWriter, path, maxEntiresInFile);
		this.autoflush = autoflush;
	}

	@Override
	protected final void write(final IMonitoringRecord monitoringRecord) throws IOException {
		final Object[] recordFields = monitoringRecord.toArray();
		final StringBuilder sb = new StringBuilder(256);
		sb.append('$');
		sb.append(this.monitoringController.getIdForString(monitoringRecord.getClass().getName()));
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
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */

	@Override
	protected final void prepareFile() throws FileNotFoundException, UnsupportedEncodingException {
		if (this.pos != null) {
			this.pos.close();
		}
		if (this.autoflush) {
			this.pos = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.getFilename()), ENCODING), true);
		} else {
			this.pos = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.getFilename()), ENCODING)), false);
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
