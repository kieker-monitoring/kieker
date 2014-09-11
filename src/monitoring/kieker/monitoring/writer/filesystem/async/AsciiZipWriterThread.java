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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

import kieker.common.record.IMonitoringRecord;
import kieker.common.util.filesystem.FSUtil;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.filesystem.map.StringMappingFileWriter;

/**
 * @author Jan Waller
 * 
 * @since 1.7
 */
public class AsciiZipWriterThread extends AbstractZipWriterThread {

	private final PrintWriter out;

	/**
	 * Create a new AsciiZipWriterThread.
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
	 * @param bufferSize
	 *            size of the output buffer
	 * @param level
	 *            compression level
	 * 
	 * @throws IOException
	 *             when file operation fails
	 */
	public AsciiZipWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final StringMappingFileWriter mappingFileWriter, final String path, final int maxEntriesInFile, final int bufferSize, final int level)
			throws IOException {
		super(monitoringController, writeQueue, mappingFileWriter, path, maxEntriesInFile, level);
		this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(super.zipOutputStream, FSUtil.ENCODING), bufferSize), false);
	}

	@Override
	protected void write(final IMonitoringRecord monitoringRecord) throws IOException {
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
		this.out.println(sb.toString());
	}

	@Override
	protected void cleanupForNextEntry() throws IOException {
		this.out.flush();
	}

	@Override
	protected void cleanupFinal() throws IOException {
		this.out.close();
	}
}
