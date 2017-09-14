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

	public static void writeMonitoringRecords(final AbstractMonitoringWriter writer, final int numRecords) {
		for (int i = 0; i < numRecords; i++) {
			writer.writeMonitoringRecord(new EmptyRecord());
		}
	}

	public static <W extends AbstractMonitoringWriter & IFileWriter> File executeFileWriterTest(final int numRecordsToWrite, final W writer) {
		writer.onStarting();
		FilesystemTestUtil.writeMonitoringRecords(writer, numRecordsToWrite);
		writer.onTerminating();

		return writer.getLogFolder().toFile();
	}

}
