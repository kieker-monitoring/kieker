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
import org.apache.commons.cli.Option;
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

	private String[] timestampsStr;
	private long[] timestampsLong;

	private LoggingTimestampConverterTool() {}

	public static void main(final String[] args) {
		new LoggingTimestampConverterTool().start(args);
	}

	@Override
	protected void addAdditionalOptions(final Options options) {
		final Option timestampsOption = new Option("t", "timestamps", true, "List of timestamps (UTC timezone) to convert");
		timestampsOption.setRequired(true);
		timestampsOption.setArgName("timestamp1 ... timestampN");

		options.addOption(timestampsOption);
	}

	@Override
	protected boolean readPropertiesFromCommandLine(final CommandLine commandLine) {
		this.timestampsStr = commandLine.getOptionValues("t");
		this.timestampsLong = new long[this.timestampsStr.length];

		for (int curIdx = 0; curIdx < this.timestampsStr.length; curIdx++) {
			try {
				this.timestampsLong[curIdx] = Long.parseLong(this.timestampsStr[curIdx]);
			} catch (final NumberFormatException ex) {
				LOG.error("Failed to parse timestamp:" + this.timestampsStr[curIdx], ex);
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
