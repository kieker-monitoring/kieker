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

package kieker.tools.logReplayer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Command-line tool for replaying a filesystem monitoring log using the {@link FilesystemLogReplayer}.
 * 
 * @author Andre van Hoorn
 */
@SuppressWarnings("static-access")
public class FilesystemLogReplayerStarter {

	private static CommandLine cmdl = null;
	private static final CommandLineParser CMDL_PARSER = new BasicParser();
	private static final HelpFormatter CMD_HELP_FORMATTER = new HelpFormatter();
	private static final Options CMDL_OPTS = new Options();
	private static final String CMD_OPT_NAME_INPUTDIRS = "inputdirs";
	private static final String CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS = "keep-logging-timestamps";
	private static final String CMD_OPT_NAME_REALTIME = "realtime";
	private static final String CMD_OPT_NAME_NUM_REALTIME_WORKERS = "realtime-worker-threads";
	private static final String CMD_OPT_NAME_IGNORERECORDSBEFOREDATE = "ignore-records-before-date";
	private static final String CMD_OPT_NAME_IGNORERECORDSAFTERDATE = "ignore-records-after-date";
	private static final String DATE_FORMAT_PATTERN = "yyyyMMdd'-'HHmmss";
	private static final String DATE_FORMAT_PATTERN_CMD_USAGE_HELP = FilesystemLogReplayerStarter.DATE_FORMAT_PATTERN.replaceAll("'", ""); // only for usage
																																			// info

	static {
		FilesystemLogReplayerStarter.CMDL_OPTS.addOption(OptionBuilder.withArgName("dir1 ... dirN").hasArgs()
				.withLongOpt(FilesystemLogReplayerStarter.CMD_OPT_NAME_INPUTDIRS).isRequired(true).withDescription("Log directories to read data from")
				.withValueSeparator('=').create("i"));
		FilesystemLogReplayerStarter.CMDL_OPTS.addOption(OptionBuilder.withArgName("true|false").hasArg()
				.withLongOpt(FilesystemLogReplayerStarter.CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS).isRequired(false)
				.withDescription("Replay the original logging timestamps (defaults to true)?)").withValueSeparator('=').create("k"));
		FilesystemLogReplayerStarter.CMDL_OPTS.addOption(OptionBuilder.withArgName("true|false").hasArg()
				.withLongOpt(FilesystemLogReplayerStarter.CMD_OPT_NAME_REALTIME).isRequired(true).withDescription("Replay log data in realtime?")
				.withValueSeparator('=').create("r"));
		FilesystemLogReplayerStarter.CMDL_OPTS.addOption(OptionBuilder.withArgName("num").hasArg()
				.withLongOpt(FilesystemLogReplayerStarter.CMD_OPT_NAME_NUM_REALTIME_WORKERS).isRequired(false)
				.withDescription("Number of worker threads used in realtime mode (defaults to 1).").withValueSeparator('=').create("n"));
		FilesystemLogReplayerStarter.CMDL_OPTS.addOption(OptionBuilder.withLongOpt(FilesystemLogReplayerStarter.CMD_OPT_NAME_IGNORERECORDSBEFOREDATE)
				.withArgName(FilesystemLogReplayerStarter.DATE_FORMAT_PATTERN_CMD_USAGE_HELP).hasArg().isRequired(false)
				.withDescription("Records logged before this date (UTC timezone) are ignored (disabled by default).").create());
		FilesystemLogReplayerStarter.CMDL_OPTS.addOption(OptionBuilder.withLongOpt(FilesystemLogReplayerStarter.CMD_OPT_NAME_IGNORERECORDSAFTERDATE)
				.withArgName(FilesystemLogReplayerStarter.DATE_FORMAT_PATTERN_CMD_USAGE_HELP).hasArg().isRequired(false)
				.withDescription("Records logged after this date (UTC timezone) are ignored (disabled by default).").create());
	}
	private static final Log LOG = LogFactory.getLog(FilesystemLogReplayerStarter.class);
	private static String[] inputDirs = null;
	private static boolean keepOriginalLoggingTimestamps;
	private static boolean realtimeMode = false;
	private static int numRealtimeWorkerThreads = -1;
	private static long ignoreRecordsBeforeTimestamp = FilesystemLogReplayer.MIN_TIMESTAMP;
	private static long ignoreRecordsAfterTimestamp = FilesystemLogReplayer.MAX_TIMESTAMP;

	private FilesystemLogReplayerStarter() {}

	private static boolean parseArgs(final String[] args) {
		try {
			FilesystemLogReplayerStarter.cmdl = FilesystemLogReplayerStarter.CMDL_PARSER.parse(FilesystemLogReplayerStarter.CMDL_OPTS, args);
		} catch (final ParseException e) {
			System.err.println("Error parsing arguments: " + e.getMessage());
			FilesystemLogReplayerStarter.printUsage();
			return false;
		}
		return true;
	}

	private static void printUsage() {
		FilesystemLogReplayerStarter.CMD_HELP_FORMATTER.printHelp(FilesystemLogReplayerStarter.class.getName(), FilesystemLogReplayerStarter.CMDL_OPTS);
	}

	private static boolean initFromArgs() {
		boolean retVal = true;

		/* 1.) init inputDirs */
		FilesystemLogReplayerStarter.inputDirs = FilesystemLogReplayerStarter.cmdl.getOptionValues(FilesystemLogReplayerStarter.CMD_OPT_NAME_INPUTDIRS);

		/* 2.) init keepOriginalLoggingTimestamps */
		final String keepOriginalLoggingTimestampsOptValStr = FilesystemLogReplayerStarter.cmdl.getOptionValue(
				FilesystemLogReplayerStarter.CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS, "true");
		if (!(keepOriginalLoggingTimestampsOptValStr.equals("true") || keepOriginalLoggingTimestampsOptValStr.equals("false"))) { // NOCS (EqualsAvoidNullCheck)
			System.out.println("Invalid value for option " + FilesystemLogReplayerStarter.CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS + ": '"
					+ keepOriginalLoggingTimestampsOptValStr + "'");
			retVal = false;
		}
		FilesystemLogReplayerStarter.keepOriginalLoggingTimestamps = keepOriginalLoggingTimestampsOptValStr.equals("true"); // NOCS (EqualsAvoidNullCheck)
		FilesystemLogReplayerStarter.LOG.info("Keeping original logging timestamps: "
				+ (FilesystemLogReplayerStarter.keepOriginalLoggingTimestamps ? "true" : "false")); // NOCS

		/* 3.) init realtimeMode */
		final String realtimeOptValStr = FilesystemLogReplayerStarter.cmdl.getOptionValue(FilesystemLogReplayerStarter.CMD_OPT_NAME_REALTIME, "false");
		if (!(realtimeOptValStr.equals("true") || realtimeOptValStr.equals("false"))) { // NOCS (EqualsAvoidNullCheck)
			System.out.println("Invalid value for option " + FilesystemLogReplayerStarter.CMD_OPT_NAME_REALTIME + ": '" + realtimeOptValStr + "'");
			retVal = false;
		}
		FilesystemLogReplayerStarter.realtimeMode = realtimeOptValStr.equals("true"); // NOCS (EqualsAvoidNullCheck)

		/* 4.) init numRealtimeWorkerThreads */
		final String numRealtimeWorkerThreadsStr = FilesystemLogReplayerStarter.cmdl.getOptionValue(FilesystemLogReplayerStarter.CMD_OPT_NAME_NUM_REALTIME_WORKERS,
				"1");
		try {
			FilesystemLogReplayerStarter.numRealtimeWorkerThreads = Integer.parseInt(numRealtimeWorkerThreadsStr);
		} catch (final NumberFormatException exc) {
			System.out.println("Invalid value for option " + FilesystemLogReplayerStarter.CMD_OPT_NAME_NUM_REALTIME_WORKERS + ": '" + numRealtimeWorkerThreadsStr
					+ "'");
			FilesystemLogReplayerStarter.LOG.error("NumberFormatException: ", exc);
			retVal = false;
		}
		if (FilesystemLogReplayerStarter.numRealtimeWorkerThreads < 1) {
			System.out.println("Option value for " + FilesystemLogReplayerStarter.CMD_OPT_NAME_NUM_REALTIME_WORKERS + " must be >= 1; found "
					+ FilesystemLogReplayerStarter.numRealtimeWorkerThreads);
			FilesystemLogReplayerStarter.LOG.error("Invalid specification of " + FilesystemLogReplayerStarter.CMD_OPT_NAME_NUM_REALTIME_WORKERS + ":"
					+ FilesystemLogReplayerStarter.numRealtimeWorkerThreads);
			retVal = false;
		}

		/* 5.) init ignoreRecordsBefore/After */
		final DateFormat dateFormat_ISO8601UTC = new SimpleDateFormat(FilesystemLogReplayerStarter.DATE_FORMAT_PATTERN); // NOCS
		dateFormat_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));

		try {
			final String ignoreRecordsBeforeTimestampString = FilesystemLogReplayerStarter.cmdl.getOptionValue(
					FilesystemLogReplayerStarter.CMD_OPT_NAME_IGNORERECORDSBEFOREDATE, null);
			final String ignoreRecordsAfterTimestampString = FilesystemLogReplayerStarter.cmdl.getOptionValue(
					FilesystemLogReplayerStarter.CMD_OPT_NAME_IGNORERECORDSAFTERDATE, null);
			if (ignoreRecordsBeforeTimestampString != null) {
				final Date ignoreBeforeDate = dateFormat_ISO8601UTC.parse(ignoreRecordsBeforeTimestampString);
				FilesystemLogReplayerStarter.ignoreRecordsBeforeTimestamp = ignoreBeforeDate.getTime() * (1000 * 1000); // NOCS (MagicNumberCheck)
				FilesystemLogReplayerStarter.LOG.info("Ignoring records before " + dateFormat_ISO8601UTC.format(ignoreBeforeDate) + " ("
						+ FilesystemLogReplayerStarter.ignoreRecordsBeforeTimestamp + ")");
			}
			if (ignoreRecordsAfterTimestampString != null) {
				final Date ignoreAfterDate = dateFormat_ISO8601UTC.parse(ignoreRecordsAfterTimestampString);
				FilesystemLogReplayerStarter.ignoreRecordsAfterTimestamp = ignoreAfterDate.getTime() * (1000 * 1000); // NOCS (MagicNumberCheck)
				FilesystemLogReplayerStarter.LOG.info("Ignoring records after " + dateFormat_ISO8601UTC.format(ignoreAfterDate) + " ("
						+ FilesystemLogReplayerStarter.ignoreRecordsAfterTimestamp + ")");
			}
		} catch (final java.text.ParseException ex) {
			final String erorMsg = "Error parsing date/time string. Please use the following pattern: "
					+ FilesystemLogReplayerStarter.DATE_FORMAT_PATTERN_CMD_USAGE_HELP;
			System.err.println(erorMsg);
			FilesystemLogReplayerStarter.LOG.error(erorMsg, ex);
			return false;
		}

		/* log configuration */
		if (retVal) {
			FilesystemLogReplayerStarter.LOG.info("inputDirs: "
					+ FilesystemLogReplayerStarter.fromStringArrayToDeliminedString(FilesystemLogReplayerStarter.inputDirs, ';'));
			FilesystemLogReplayerStarter.LOG.info("Replaying in " + (FilesystemLogReplayerStarter.realtimeMode ? "" : "non-") + "realtime mode"); // NOCS
			if (FilesystemLogReplayerStarter.realtimeMode) {
				FilesystemLogReplayerStarter.LOG.info("Using " + FilesystemLogReplayerStarter.numRealtimeWorkerThreads + " realtime worker thread"
						+ (FilesystemLogReplayerStarter.numRealtimeWorkerThreads > 1 ? "s" : "")); // NOCS
			}
		}

		return retVal;
	}

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
			FilesystemLogReplayerStarter.LOG.info("Replaying log data in real time");
		} else {
			FilesystemLogReplayerStarter.LOG.info("Replaying log data in non-real time");
		}

		/**
		 * Force the controller to keep the original logging timestamps of the
		 * monitoring records.
		 */
		final Configuration configuration = Configuration.createDefaultConfiguration();

		if (FilesystemLogReplayerStarter.keepOriginalLoggingTimestamps) {
			configuration.setProperty(Configuration.AUTO_SET_LOGGINGTSTAMP, Boolean.toString(false));
		} else {
			configuration.setProperty(Configuration.AUTO_SET_LOGGINGTSTAMP, Boolean.toString(true));
		}

		final IMonitoringController monitoringController = MonitoringController.createInstance(configuration);

		final FilesystemLogReplayer player = new FilesystemLogReplayer(monitoringController, FilesystemLogReplayerStarter.inputDirs,
				FilesystemLogReplayerStarter.realtimeMode, FilesystemLogReplayerStarter.numRealtimeWorkerThreads,
				FilesystemLogReplayerStarter.ignoreRecordsBeforeTimestamp, FilesystemLogReplayerStarter.ignoreRecordsAfterTimestamp);

		if (!player.replay()) {
			System.err.println("An error occured");
			System.err.println("");
			System.err.println("See 'kieker.log' for details");
			System.exit(1);
		}
	}
}
