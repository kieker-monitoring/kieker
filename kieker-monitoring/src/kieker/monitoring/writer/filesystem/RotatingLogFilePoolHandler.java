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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.util.filesystem.FSUtil;

/**
 * Rotating log file pool handler, there is a maximum limit on files.
 *
 * @author Reiner Jung
 *
 * @since 1.14
 */
public class RotatingLogFilePoolHandler implements ILogFilePoolHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RotatingLogFilePoolHandler.class);

	private static final String TIME_ZONE = "UTC";
	private static final Locale LOCALE = Locale.US;

	private final List<Path> logFiles = new ArrayList<>();

	private final SimpleDateFormat dateFormatter;

	private final Path location;
	private int counter;
	private final String fileExtensionWithDot;
	private final int maxAmountOfFiles;

	public RotatingLogFilePoolHandler(final Path location, final String extension, final Integer maxAmountOfFiles) {
		this.dateFormatter = new SimpleDateFormat("yyyyMMdd'-'HHmmssSSS", LOCALE);
		this.dateFormatter.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));

		this.maxAmountOfFiles = maxAmountOfFiles;
		this.location = location;
		this.fileExtensionWithDot = extension;
	}

	@Override
	public Path requestFile() {
		this.counter++;

		if (this.counter > this.maxAmountOfFiles) {
			final Path oldestFile = this.logFiles.remove(0);
			try {
				Files.delete(oldestFile);
			} catch (final IOException e) {
				LOGGER.warn("Cannot delete oldest file.", e);
			}
		}

		final Date now = new Date();

		// "%1$s-%2$tY%2$tm%2$td-%2$tH%2$tM%2$tS%2$tL-UTC-%3$03d-%4$s.%5$s"
		final String fileName = String.format(LOCALE, "%s-%s-%s-%03d%s",
				FSUtil.FILE_PREFIX, this.dateFormatter.format(now), TIME_ZONE, this.counter, this.fileExtensionWithDot);

		final Path logFile = this.location.resolve(fileName);
		this.logFiles.add(logFile);

		return logFile;
	}

}
