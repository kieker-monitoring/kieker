/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.util.LoggingTimestampConverter;

/**
 * This tool can be used to convert timestamps.
 * 
 * @author Andre van Hoorn
 */
public final class LoggingTimestampConverterTool {

	static final List<Option> OPTIONS = new CopyOnWriteArrayList<Option>(); // NOPMD package for inner class
	private static final Log LOG = LogFactory.getLog(LoggingTimestampConverterTool.class);
	private static final CommandLineParser CMDL_PARSER = new BasicParser();
	private static final HelpFormatter CMD_HELP_FORMATTER = new HelpFormatter();
	private static final Options CMDL_OPTS = new Options();
	private static final String CMD_OPT_NAME_TIMESTAMPS = "timestamps";
	private static CommandLine cmdl;
	private static String[] timestampsStr;
	private static long[] timestampsLong;

	static {
		LoggingTimestampConverterTool.initializeOptions();
	}

	/**
	 * Private constructor to avoid instantiation.
	 */
	private LoggingTimestampConverterTool() {}

	/**
	 * This method initializes some static fields.
	 */
	@SuppressWarnings({ "static-access", "static" })
	private static final void initializeOptions() {
		OPTIONS.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TIMESTAMPS)
				.withArgName("timestamp1 ... timestampN").hasArgs().isRequired(true).withDescription("List of timestamps (UTC timezone) to convert").create("t"));
		for (final Option o : OPTIONS) {
			CMDL_OPTS.addOption(o);
		}
		CMD_HELP_FORMATTER.setOptionComparator(new Comparator<Object>() {

			public int compare(final Object o1, final Object o2) {
				if (o1 == o2) { // NOPMD (not equals)
					return 0;
				}
				final int posO1 = OPTIONS.indexOf(o1);
				final int posO2 = OPTIONS.indexOf(o2);
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

	/**
	 * This is the main method of the tool.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
	public static void main(final String[] args) {
		if (!LoggingTimestampConverterTool.parseArgs(args) || !LoggingTimestampConverterTool.initFromArgs()) {
			System.exit(1);
		}

		for (final long tstamp : LoggingTimestampConverterTool.timestampsLong) {
			final StringBuilder strB = new StringBuilder();
			strB.append(tstamp).append(": ").append(LoggingTimestampConverter.convertLoggingTimestampToUTCString(tstamp)).append(" (")
					.append(LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(tstamp)).append(')');
			System.out.println(strB.toString()); // NOPMD (System.out)
		}
	}

	/**
	 * This method tries to parse the given command line arguments.
	 * 
	 * @param args
	 *            The command line arguments.
	 * @return true if and only if the arguments have been parsed successfully.
	 */
	private static boolean parseArgs(final String[] args) {
		try {
			LoggingTimestampConverterTool.cmdl = CMDL_PARSER.parse(CMDL_OPTS, args);
		} catch (final ParseException e) {
			LoggingTimestampConverterTool.printUsage();
			System.err.println("\nError parsing arguments: " + e.getMessage()); // NOPMD (System.out)
			return false;
		}
		return true;
	}

	/**
	 * This method prints a help text on the screen to show the usage of the tool.
	 */
	private static void printUsage() {
		CMD_HELP_FORMATTER.printHelp(80, LoggingTimestampConverterTool.class.getName(), "",
				CMDL_OPTS,
				"", true);
	}

	/**
	 * This method tries to initialize the tool by using the (already parsed and stored) command line arguments.
	 * 
	 * @return true if and only if the tool has been initialized successfully.
	 */
	private static boolean initFromArgs() {
		LoggingTimestampConverterTool.timestampsStr = LoggingTimestampConverterTool.cmdl.getOptionValues(CMD_OPT_NAME_TIMESTAMPS);
		if (LoggingTimestampConverterTool.timestampsStr == null) { // should not happen since marked as required opt
			LOG.error("Missing value for option '" + CMD_OPT_NAME_TIMESTAMPS + "'");
			return false;
		}

		LoggingTimestampConverterTool.timestampsLong = new long[LoggingTimestampConverterTool.timestampsStr.length];
		for (int curIdx = 0; curIdx < LoggingTimestampConverterTool.timestampsStr.length; curIdx++) {
			try {
				LoggingTimestampConverterTool.timestampsLong[curIdx] = Long.parseLong(LoggingTimestampConverterTool.timestampsStr[curIdx]);
			} catch (final NumberFormatException ex) {
				LOG.error("Failed to parse timestamp:" + LoggingTimestampConverterTool.timestampsStr[curIdx], ex);
				System.err.println("Failed to parse timestamp:" + LoggingTimestampConverterTool.timestampsStr[curIdx]); // NOPMD (System.out)
				return false;
			}
		}

		return true;
	}
}
