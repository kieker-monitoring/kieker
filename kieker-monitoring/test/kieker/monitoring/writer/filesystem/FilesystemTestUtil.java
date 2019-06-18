/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.writer.filesystem;

import java.io.File;
import java.nio.file.Path;

import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
final class FilesystemTestUtil {

	private FilesystemTestUtil() {
		// utility class
	}

	public static void writeMonitoringRecords(final AbstractMonitoringWriter writer, final int numRecords, final EmptyRecord record) {
		for (int i = 0; i < numRecords; i++) {
			writer.writeMonitoringRecord(record);
		}
	}

	public static <W extends AbstractMonitoringWriter> void executeFileWriterTest(final int numRecordsToWrite, final W writer,
			final EmptyRecord record) {
		writer.onStarting();
		FilesystemTestUtil.writeMonitoringRecords(writer, numRecordsToWrite, record);
		writer.onTerminating();
	}

	public static void deleteContent(final Path writerPath) {
		final File directory = writerPath.toFile();
		for (final File content : directory.listFiles()) {
			if (content.isDirectory()) {
				FilesystemTestUtil.deleteDirectory(content);
			} else {
				FilesystemTestUtil.deleteFile(content);
			}
		}
	}

	private static void deleteDirectory(final File directory) {
		for (final File content : directory.listFiles()) {
			if (content.isDirectory()) {
				FilesystemTestUtil.deleteDirectory(content);
			} else {
				FilesystemTestUtil.deleteFile(content);
			}
		}
		directory.delete();
	}

	private static void deleteFile(final File file) {
		file.delete();
	}

}
