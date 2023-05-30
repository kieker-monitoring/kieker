/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.logging.timestamp.converter;

import java.nio.file.Path;

import com.beust.jcommander.JCommander;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.common.util.dataformat.LoggingTimestampConverter;
import kieker.tools.common.AbstractLegacyTool;

/**
 * This tool can be used to convert timestamps.
 *
 * @author Andre van Hoorn, Nils Christian Ehmke
 *
 * @since 1.1
 */
public final class LoggingTimestampConverterTool extends AbstractLegacyTool<Settings> {

	public static void main(final String[] args) {
		final LoggingTimestampConverterTool tool = new LoggingTimestampConverterTool();
		System.exit(tool.run("ltct", "Logging timestamp converter tool", args, new Settings()));
	}

	@Override
	protected int execute(final JCommander commander, final String label) throws ConfigurationException {
		final String lineSeperator = System.getProperty("line.separator");
		final int estimatedNumberOfChars = this.settings.getTimestamps().size() * 85;
		final StringBuilder stringBuilder = new StringBuilder(estimatedNumberOfChars);

		for (final long timestampLong : this.settings.getTimestamps()) {
			stringBuilder.append(timestampLong).append(": ")
					.append(LoggingTimestampConverter.convertLoggingTimestampToUTCString(timestampLong));
			stringBuilder.append(" (")
					.append(LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(timestampLong))
					.append(')').append(lineSeperator);
		}

		System.out.print(stringBuilder.toString()); // NOPMD (System.out)

		return 0;
	}

	@Override
	protected Path getConfigurationPath() {
		return null;
	}

	@Override
	protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
		return false;
	}

	@Override
	protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
		if (this.settings.getTimestamps().size() == 0) {
			this.logger.error("No timestamps specified");
			return false;
		} else {
			return false;
		}
	}

	@Override
	protected void shutdownService() {
		// nothing to be done here
	}

}
