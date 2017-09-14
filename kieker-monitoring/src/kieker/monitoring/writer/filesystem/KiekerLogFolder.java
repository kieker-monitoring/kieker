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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import kieker.common.configuration.Configuration;
import kieker.common.util.filesystem.FSUtil;
import kieker.monitoring.core.configuration.ConfigurationFactory;

/**
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 */
final class KiekerLogFolder {

	private KiekerLogFolder() {
		// utility class
	}

	public static Path buildKiekerLogFolder(final String customStoragePath, final Configuration configuration) {
		final DateFormat date = new SimpleDateFormat("yyyyMMdd'-'HHmmss", Locale.US);
		date.setTimeZone(TimeZone.getTimeZone("UTC"));
		final String currentDateStr = date.format(new java.util.Date())
				+ "-" + System.nanoTime(); // 'SSS' in SimpleDateFormat is not accurate enough for fast unit tests

		final String hostName = configuration.getStringProperty(ConfigurationFactory.HOST_NAME);
		final String controllerName = configuration.getStringProperty(ConfigurationFactory.CONTROLLER_NAME);

		final String filename = String.format("%s-%s-UTC-%s-%s", FSUtil.FILE_PREFIX, currentDateStr, hostName, controllerName);

		return Paths.get(customStoragePath, filename);
	}
}
