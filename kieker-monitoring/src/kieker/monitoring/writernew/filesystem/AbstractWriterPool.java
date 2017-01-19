/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.writernew.filesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import kieker.common.logging.Log;
import kieker.common.util.filesystem.FSUtil;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
abstract class AbstractWriterPool {

	protected static final String TIME_ZONE = "UTC";
	protected static final Locale LOCALE = Locale.US;

	protected final List<Path> logFiles = new ArrayList<>();
	protected final Log writerLog;
	protected final Path folder;

	private final SimpleDateFormat dateFormatter;

	public AbstractWriterPool(final Log writerLog, final Path folder) {
		this.writerLog = writerLog;
		this.folder = folder;
		this.dateFormatter = new SimpleDateFormat("yyyyMMdd'-'HHmmssSSS", LOCALE);
		this.dateFormatter.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
	}

	public void onMaxLogFilesExceeded() {
		final Path oldestFile = this.logFiles.remove(0);
		try {
			Files.delete(oldestFile);
		} catch (final IOException e) {
			this.writerLog.warn("Cannot delete oldest file.", e);
		}
	}

	public Path getNextFileName(final String fileExtensionWithDot) {
		final int counter = this.logFiles.size();
		final Date now = new Date();

		// "%1$s-%2$tY%2$tm%2$td-%2$tH%2$tM%2$tS%2$tL-UTC-%3$03d-%4$s.%5$s"
		final String fileName = String.format(LOCALE, "%s-%s-%s-%03d%s",
				FSUtil.FILE_PREFIX, this.dateFormatter.format(now), TIME_ZONE, counter, fileExtensionWithDot);

		final Path logFile = this.folder.resolve(fileName);
		this.logFiles.add(logFile);

		return logFile;
	}
}
