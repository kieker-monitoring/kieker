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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.tools.AbstractCommandLineTool;
import kieker.tools.util.LoggingTimestampConverter;

/**
 * This tool can be used to convert timestamps.
 *
 * @author Andre van Hoorn, Nils Christian Ehmke
 *
 * @since 1.1
 */
public final class LoggingTimestampConverterTool extends AbstractCommandLineTool {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingTimestampConverterTool.class);
	private static final String FLAG_TIMESTAMPS_PARAMETER = "t";

	private long[] timestampsLong;

	private LoggingTimestampConverterTool() {
		super(true);
	}

	public static void main(final String[] args) {
		new LoggingTimestampConverterTool().start(args);
	}

	@Override
	protected void addAdditionalOptions(final Options options) {
		final Option option = new Option(LoggingTimestampConverterTool.FLAG_TIMESTAMPS_PARAMETER, "timestamps", true,
				"List of timestamps (UTC timezone) to convert");
		option.setArgName("timestamp1 ... timestampN");
		option.setRequired(false);
		option.setArgs(Option.UNLIMITED_VALUES);

		options.addOption(option);
	}

	@Override
	protected boolean readPropertiesFromCommandLine(final CommandLine commandLine) {
		final String[] timestampsStr = commandLine
				.getOptionValues(LoggingTimestampConverterTool.FLAG_TIMESTAMPS_PARAMETER);
		if ((timestampsStr == null) || (timestampsStr.length == 0)) {
			LoggingTimestampConverterTool.LOGGER.error("No timestamp passed as argument.");
			return false;
		}

		this.timestampsLong = new long[timestampsStr.length];

		for (int curIdx = 0; curIdx < timestampsStr.length; curIdx++) {
			try {
				this.timestampsLong[curIdx] = Long.parseLong(timestampsStr[curIdx]);
			} catch (final NumberFormatException ex) {
				LoggingTimestampConverterTool.LOGGER.error("Failed to parse timestamp: {}", timestampsStr[curIdx], ex);
				return false;
			}
		}

		return true;
	}

	@Override
	protected boolean performTask() {
		final String lineSeperator = System.getProperty("line.separator");
		final int estimatedNumberOfChars = this.timestampsLong.length * 85;
		final StringBuilder stringBuilder = new StringBuilder(estimatedNumberOfChars);

		for (final long timestampLong : this.timestampsLong) {
			stringBuilder.append(timestampLong).append(": ")
					.append(LoggingTimestampConverter.convertLoggingTimestampToUTCString(timestampLong));
			stringBuilder.append(" (")
					.append(LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(timestampLong))
					.append(')').append(lineSeperator);
		}

		System.out.print(stringBuilder.toString()); // NOPMD (System.out)

		return true;
	}

}
