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

package kieker.tools.loggingTimestampConverter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
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

	private static final Log LOG = LogFactory.getLog(LoggingTimestampConverterTool.class);

	private long[] timestampsLong;

	private LoggingTimestampConverterTool() {
		super(true);
	}

	public static void main(final String[] args) {
		new LoggingTimestampConverterTool().start(args);
	}

	@Override
	@SuppressWarnings("static-access")
	protected void addAdditionalOptions(final Options options) {
		options.addOption(OptionBuilder.withLongOpt("timestamps").withArgName("timestamp1 ... timestampN").hasArgs().isRequired(true)
				.withDescription("List of timestamps (UTC timezone) to convert").create("t"));
	}

	@Override
	protected boolean readPropertiesFromCommandLine(final CommandLine commandLine) {
		final String[] timestampsStr = commandLine.getOptionValues("t");
		this.timestampsLong = new long[timestampsStr.length];

		for (int curIdx = 0; curIdx < timestampsStr.length; curIdx++) {
			try {
				this.timestampsLong[curIdx] = Long.parseLong(timestampsStr[curIdx]);
			} catch (final NumberFormatException ex) {
				LOG.error("Failed to parse timestamp:" + timestampsStr[curIdx], ex);
				return false;
			}
		}

		return true;
	}

	@Override
	protected boolean performTask() {
		for (final long tstamp : this.timestampsLong) {
			final StringBuilder strB = new StringBuilder();
			strB.append(tstamp).append(": ").append(LoggingTimestampConverter.convertLoggingTimestampToUTCString(tstamp)).append(" (")
					.append(LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(tstamp)).append(')');
			System.out.println(strB.toString()); // NOPMD (System.out)
		}

		return true;
	}

}
