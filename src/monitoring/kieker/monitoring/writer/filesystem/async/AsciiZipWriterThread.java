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

package kieker.monitoring.writer.filesystem.async;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.filesystem.map.StringMappingFileWriter;

/**
 * @author Jan Waller
 */
public class AsciiZipWriterThread extends AbstractZipWriterThread {

	private final PrintWriter out;

	public AsciiZipWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final StringMappingFileWriter mappingFileWriter, final String path, final int maxEntriesInFile, final int bufferSize, final int level)
			throws IOException {
		super(monitoringController, writeQueue, mappingFileWriter, path, maxEntriesInFile, level);
		this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(super.zipOutputStream, ENCODING), bufferSize), false);
	}

	@Override
	protected void write(final IMonitoringRecord monitoringRecord) throws IOException {
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
