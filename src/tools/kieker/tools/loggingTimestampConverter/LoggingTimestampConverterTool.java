/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.tools.util.LoggingTimestampConverter;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public final class LoggingTimestampConverterTool {

	private static final Log LOG = LogFactory.getLog(LoggingTimestampConverterTool.class);
	private static final CommandLineParser CMDL_PARSER = new BasicParser();
	private static final HelpFormatter CMD_HELP_FORMATTER = new HelpFormatter();
	private static final Options CMDL_OPTS = new Options();
	private static final List<Option> OPTIONS = new CopyOnWriteArrayList<Option>();
	private static final String CMD_OPT_NAME_TIMESTAMPS = "timestamps";
	private static CommandLine cmdl = null;
	private static String[] timestampsStr;
	private static long[] timestampsLong;

	static {
		LoggingTimestampConverterTool.initializeOptions();
	}

	private LoggingTimestampConverterTool() {}

	@SuppressWarnings("static-access")
	private static final void initializeOptions() {
		LoggingTimestampConverterTool.OPTIONS.add(OptionBuilder.withLongOpt(LoggingTimestampConverterTool.CMD_OPT_NAME_TIMESTAMPS)
				.withArgName("timestamp1 ... timestampN").hasArgs().isRequired(true).withDescription("List of timestamps (UTC timezone) to convert").create("t"));
		for (final Option o : LoggingTimestampConverterTool.OPTIONS) {
			LoggingTimestampConverterTool.CMDL_OPTS.addOption(o);
		}
		LoggingTimestampConverterTool.CMD_HELP_FORMATTER.setOptionComparator(new Comparator<Object>() {

			@Override
			public int compare(final Object o1, final Object o2) {
				if (o1 == o2) { // NOPMD
					return 0;
				}
				final int posO1 = LoggingTimestampConverterTool.OPTIONS.indexOf(o1);
				final int posO2 = LoggingTimestampConverterTool.OPTIONS.indexOf(o2);
				if (posO1 < posO2) {
					return -1;
				}
				if (posO1 > posO2) {
					return 1;
				}
				return 0;
			}
		});
	}

	public static void main(final String[] args) {
		if (!LoggingTimestampConverterTool.parseArgs(args) || !LoggingTimestampConverterTool.initFromArgs()) {
			System.exit(1);
		}

		for (final long tstamp : LoggingTimestampConverterTool.timestampsLong) {
			final StringBuilder strB = new StringBuilder(); // NOPMD (new in loop)
			strB.append(tstamp).append(": ").append(LoggingTimestampConverter.convertLoggingTimestampToUTCString(tstamp)).append(" (")
					.append(LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(tstamp)).append(")");
			System.out.println(strB.toString());
		}
	}

	private static boolean parseArgs(final String[] args) {
		try {
			LoggingTimestampConverterTool.cmdl = LoggingTimestampConverterTool.CMDL_PARSER.parse(LoggingTimestampConverterTool.CMDL_OPTS, args);
		} catch (final ParseException e) {
			LoggingTimestampConverterTool.printUsage();
			System.err.println("\nError parsing arguments: " + e.getMessage());
			return false;
		}
		return true;
	}

	private static void printUsage() {
		LoggingTimestampConverterTool.CMD_HELP_FORMATTER.printHelp(80, LoggingTimestampConverterTool.class.getName(), "", // NOCS (MagicNumberCheck)
				LoggingTimestampConverterTool.CMDL_OPTS,
				"", true);
	}

	private static boolean initFromArgs() {
		LoggingTimestampConverterTool.timestampsStr = LoggingTimestampConverterTool.cmdl.getOptionValues(LoggingTimestampConverterTool.CMD_OPT_NAME_TIMESTAMPS);
		if (LoggingTimestampConverterTool.timestampsStr == null) { // should not happen since marked as required opt
			LoggingTimestampConverterTool.LOG.error("Missing value for option '" + LoggingTimestampConverterTool.CMD_OPT_NAME_TIMESTAMPS + "'");
			return false;
		}

		LoggingTimestampConverterTool.timestampsLong = new long[LoggingTimestampConverterTool.timestampsStr.length];
		for (int curIdx = 0; curIdx < LoggingTimestampConverterTool.timestampsStr.length; curIdx++) {
			try {
				LoggingTimestampConverterTool.timestampsLong[curIdx] = Long.parseLong(LoggingTimestampConverterTool.timestampsStr[curIdx]);
			} catch (final NumberFormatException ex) {
				LoggingTimestampConverterTool.LOG.error("Failed to parse timestamp:" + LoggingTimestampConverterTool.timestampsStr[curIdx], ex);
				System.err.println("Failed to parse timestamp:" + LoggingTimestampConverterTool.timestampsStr[curIdx]);
				return false;
			}
		}

		return true;
	}
}
