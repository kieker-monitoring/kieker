/***************************************************************************
 * Copyright 2012 by
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

package kieker.tools.logReplayer;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * Command-line tool for replaying a filesystem monitoring log using the {@link FilesystemLogReplayer}.
 * 
 * @author Andre van Hoorn
 */
@SuppressWarnings("static-access")
public final class FilesystemLogReplayerStarter {
	private static final Log LOG = LogFactory.getLog(FilesystemLogReplayerStarter.class);

	private static CommandLine cmdl = null;
	private static final CommandLineParser CMDL_PARSER = new BasicParser();
	private static final HelpFormatter CMD_HELP_FORMATTER = new HelpFormatter();
	private static final Options CMDL_OPTS = new Options();
	private static final String CMD_OPT_NAME_MONITORING_CONFIGURATION = "monitoring.configuration";
	private static final String CMD_OPT_NAME_INPUTDIRS = "inputdirs";
	private static final String CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS = "keep-logging-timestamps";
	private static final String CMD_OPT_NAME_REALTIME = "realtime";
	private static final String CMD_OPT_NAME_NUM_REALTIME_WORKERS = "realtime-worker-threads";
	private static final String CMD_OPT_NAME_IGNORERECORDSBEFOREDATE = "ignore-records-before-date";
	private static final String CMD_OPT_NAME_IGNORERECORDSAFTERDATE = "ignore-records-after-date";
	private static final String DATE_FORMAT_PATTERN = "yyyyMMdd'-'HHmmss";
	private static final String DATE_FORMAT_PATTERN_CMD_USAGE_HELP = DATE_FORMAT_PATTERN.replaceAll("'", ""); // only for usage info

	private static final String OPTION_EXAMPLE_FILE_MONITORING_PROPERTIES =
			File.separator + "path" + File.separator + "to" + File.separator + "monitoring.properties";

	static {
		CMDL_OPTS.addOption(OptionBuilder.withArgName(OPTION_EXAMPLE_FILE_MONITORING_PROPERTIES).hasArg()
				.withLongOpt(CMD_OPT_NAME_MONITORING_CONFIGURATION).isRequired(false)
				.withDescription("Configuration to use for the Kieker monitoring instance").withValueSeparator('=').create("c"));
		CMDL_OPTS.addOption(OptionBuilder.withArgName("dir1 ... dirN").hasArgs()
				.withLongOpt(CMD_OPT_NAME_INPUTDIRS).isRequired(true).withDescription("Log directories to read data from")
				.withValueSeparator('=').create("i"));
		CMDL_OPTS.addOption(OptionBuilder.withArgName("true|false").hasArg()
				.withLongOpt(CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS).isRequired(false)
				.withDescription("Replay the original logging timestamps (defaults to true)?)").withValueSeparator('=').create("k"));
		CMDL_OPTS.addOption(OptionBuilder.withArgName("true|false").hasArg()
				.withLongOpt(CMD_OPT_NAME_REALTIME).isRequired(true).withDescription("Replay log data in realtime?")
				.withValueSeparator('=').create("r"));
		CMDL_OPTS.addOption(OptionBuilder.withArgName("num").hasArg()
				.withLongOpt(CMD_OPT_NAME_NUM_REALTIME_WORKERS).isRequired(false)
				.withDescription("Number of worker threads used in realtime mode (defaults to 1).").withValueSeparator('=').create("n"));
		CMDL_OPTS.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_IGNORERECORDSBEFOREDATE)
				.withArgName(DATE_FORMAT_PATTERN_CMD_USAGE_HELP).hasArg().isRequired(false)
				.withDescription("Records logged before this date (UTC timezone) are ignored (disabled by default).").create());
		CMDL_OPTS.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_IGNORERECORDSAFTERDATE)
				.withArgName(DATE_FORMAT_PATTERN_CMD_USAGE_HELP).hasArg().isRequired(false)
				.withDescription("Records logged after this date (UTC timezone) are ignored (disabled by default).").create());
	}

	private static String monitoringConfigurationFile = null;
	private static String[] inputDirs = null;
	private static boolean keepOriginalLoggingTimestamps;
	private static boolean realtimeMode = false;
	private static int numRealtimeWorkerThreads = -1;
	private static long ignoreRecordsBeforeTimestamp = FilesystemLogReplayer.MIN_TIMESTAMP;
	private static long ignoreRecordsAfterTimestamp = FilesystemLogReplayer.MAX_TIMESTAMP;

	// Avoid instantiation by setting the constructor's visibility to private
	private FilesystemLogReplayerStarter() {}

	private static boolean parseArgs(final String[] args) {
		try {
			FilesystemLogReplayerStarter.cmdl = CMDL_PARSER.parse(CMDL_OPTS, args);
		} catch (final ParseException e) {
			System.err.println("Error parsing arguments: " + e.getMessage());
			FilesystemLogReplayerStarter.printUsage();
			return false;
		}
		return true;
	}

	private static void printUsage() {
		CMD_HELP_FORMATTER.printHelp(FilesystemLogReplayerStarter.class.getName(), CMDL_OPTS);
	}

	private static boolean initFromArgs() {
		boolean retVal = true;

		/* 0.) monitoring properties */
		monitoringConfigurationFile = cmdl.getOptionValue(CMD_OPT_NAME_MONITORING_CONFIGURATION);

		/* 1.) init inputDirs */
		FilesystemLogReplayerStarter.inputDirs = FilesystemLogReplayerStarter.cmdl.getOptionValues(CMD_OPT_NAME_INPUTDIRS);

		/* 2.) init keepOriginalLoggingTimestamps */
		final String keepOriginalLoggingTimestampsOptValStr = FilesystemLogReplayerStarter.cmdl.getOptionValue(
				CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS, "true");
		if (!("true".equals(keepOriginalLoggingTimestampsOptValStr) || "false".equals(keepOriginalLoggingTimestampsOptValStr))) {
			System.out.println("Invalid value for option " + CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS + ": '"
					+ keepOriginalLoggingTimestampsOptValStr + "'");
			retVal = false;
		}
		FilesystemLogReplayerStarter.keepOriginalLoggingTimestamps = "true".equals(keepOriginalLoggingTimestampsOptValStr);
		LOG.info("Keeping original logging timestamps: "
				+ (FilesystemLogReplayerStarter.keepOriginalLoggingTimestamps ? "true" : "false")); // NOCS

		/* 3.) init realtimeMode */
		final String realtimeOptValStr = FilesystemLogReplayerStarter.cmdl.getOptionValue(CMD_OPT_NAME_REALTIME, "false");
		if (!("true".equals(realtimeOptValStr) || "false".equals(realtimeOptValStr))) {
			System.out.println("Invalid value for option " + CMD_OPT_NAME_REALTIME + ": '" + realtimeOptValStr + "'");
			retVal = false;
		}
		FilesystemLogReplayerStarter.realtimeMode = "true".equals(realtimeOptValStr);

		/* 4.) init numRealtimeWorkerThreads */
		final String numRealtimeWorkerThreadsStr = FilesystemLogReplayerStarter.cmdl.getOptionValue(CMD_OPT_NAME_NUM_REALTIME_WORKERS,
				"1");
		try {
			FilesystemLogReplayerStarter.numRealtimeWorkerThreads = Integer.parseInt(numRealtimeWorkerThreadsStr);
		} catch (final NumberFormatException ex) {
			System.out.println("Invalid value for option " + CMD_OPT_NAME_NUM_REALTIME_WORKERS + ": '" + numRealtimeWorkerThreadsStr
					+ "'");
			LOG.error("NumberFormatException: ", ex);
			retVal = false;
		}
		if (FilesystemLogReplayerStarter.numRealtimeWorkerThreads < 1) {
			System.out.println("Option value for " + CMD_OPT_NAME_NUM_REALTIME_WORKERS + " must be >= 1; found "
					+ FilesystemLogReplayerStarter.numRealtimeWorkerThreads);
			LOG.error("Invalid specification of " + CMD_OPT_NAME_NUM_REALTIME_WORKERS + ":"
					+ FilesystemLogReplayerStarter.numRealtimeWorkerThreads);
			retVal = false;
		}

		/* 5.) init ignoreRecordsBefore/After */
		final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		try {
			final String ignoreRecordsBeforeTimestampString = FilesystemLogReplayerStarter.cmdl.getOptionValue(
					CMD_OPT_NAME_IGNORERECORDSBEFOREDATE, null);
			final String ignoreRecordsAfterTimestampString = FilesystemLogReplayerStarter.cmdl.getOptionValue(
					CMD_OPT_NAME_IGNORERECORDSAFTERDATE, null);
			if (ignoreRecordsBeforeTimestampString != null) {
				final Date ignoreBeforeDate = dateFormat.parse(ignoreRecordsBeforeTimestampString);
				FilesystemLogReplayerStarter.ignoreRecordsBeforeTimestamp = ignoreBeforeDate.getTime() * (1000 * 1000);
				LOG.info("Ignoring records before " + dateFormat.format(ignoreBeforeDate) + " ("
						+ FilesystemLogReplayerStarter.ignoreRecordsBeforeTimestamp + ")");
			}
			if (ignoreRecordsAfterTimestampString != null) {
				final Date ignoreAfterDate = dateFormat.parse(ignoreRecordsAfterTimestampString);
				FilesystemLogReplayerStarter.ignoreRecordsAfterTimestamp = ignoreAfterDate.getTime() * (1000 * 1000);
				LOG.info("Ignoring records after " + dateFormat.format(ignoreAfterDate) + " ("
						+ FilesystemLogReplayerStarter.ignoreRecordsAfterTimestamp + ")");
			}
		} catch (final java.text.ParseException ex) {
			final String erorMsg = "Error parsing date/time string. Please use the following pattern: "
					+ DATE_FORMAT_PATTERN_CMD_USAGE_HELP;
			System.err.println(erorMsg);
			LOG.error(erorMsg, ex);
			return false;
		}

		/* log configuration */
		if (retVal) {
			LOG.info("inputDirs: "
					+ FilesystemLogReplayerStarter.fromStringArrayToDeliminedString(FilesystemLogReplayerStarter.inputDirs, ';'));
			LOG.info("Replaying in " + (FilesystemLogReplayerStarter.realtimeMode ? "" : "non-") + "realtime mode"); // NOCS
			if (FilesystemLogReplayerStarter.realtimeMode) {
				LOG.info("Using " + FilesystemLogReplayerStarter.numRealtimeWorkerThreads + " realtime worker thread"
						+ (FilesystemLogReplayerStarter.numRealtimeWorkerThreads > 1 ? "s" : "")); // NOCS
			}
		}

		return retVal;
	}

	// TODO: difference to Arrays.toString(..array.)?
	private static String fromStringArrayToDeliminedString(final String[] array, final char delimiter) {
		final StringBuilder arTostr = new StringBuilder();
		if (array.length > 0) {
			arTostr.append(array[0]);
			for (int i = 1; i < array.length; i++) {
				arTostr.append(delimiter);
				arTostr.append(array[i]);
			}
		}
		return arTostr.toString();

	}

	public static void main(final String[] args) {
		if (!FilesystemLogReplayerStarter.parseArgs(args) || !FilesystemLogReplayerStarter.initFromArgs()) {
			System.exit(1);
		}

		/* Parsed args and initialized variables */

		if (FilesystemLogReplayerStarter.realtimeMode) {
			LOG.info("Replaying log data in real time");
		} else {
			LOG.info("Replaying log data in non-real time");
		}

		final FilesystemLogReplayer player = new FilesystemLogReplayer(monitoringConfigurationFile, realtimeMode, keepOriginalLoggingTimestamps, numRealtimeWorkerThreads,
				ignoreRecordsBeforeTimestamp, ignoreRecordsAfterTimestamp, inputDirs);

		if (!player.replay()) {
			System.err.println("An error occured");
			System.err.println("");
			System.err.println("See 'kieker.log' for details");
			System.exit(1);
		}

	}
}
