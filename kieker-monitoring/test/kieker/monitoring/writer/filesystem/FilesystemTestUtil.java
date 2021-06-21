/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

	public static boolean deleteContent(final Path writerPath) {
		if (writerPath != null) {
			boolean result = true;
			final File directory = writerPath.toFile();
			final File[] contents = directory.listFiles();
			if (contents != null) {
				for (final File content : contents) {
					result &= FilesystemTestUtil.deleteObject(content);
				}
			}

			return result;
		} else {
			return false;
		}
	}

	private static boolean deleteObject(final File fsObject) { // NOFB directory is always a directory
		if (fsObject.isDirectory()) {
			boolean result = true;
			final File[] contents = fsObject.listFiles();
			if (contents != null) {
				for (final File content : contents) { // NOFB directory is always a directory
					result &= FilesystemTestUtil.deleteObject(content);
				}
			}

			return fsObject.delete() && result;
		} else {
			return fsObject.delete();
		}
	}

}
