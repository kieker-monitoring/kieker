package kieker.tools.logReplayer;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
 * ==================================================
 *
 */

import kieker.monitoring.core.MonitoringController;

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
 * 
 * @author Andre van Hoorn
 */
public class FilesystemLogReplayerStarter {

	private static CommandLine cmdl = null;
	private static final CommandLineParser cmdlParser = new BasicParser();
	private static final HelpFormatter cmdHelpFormatter = new HelpFormatter();
	private static final Options cmdlOpts = new Options();
	private static final String CMD_OPT_NAME_INPUTDIRS = "inputdirs";
	private static final String CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS = "keep-logging-timestamps";
	private static final String CMD_OPT_NAME_REALTIME = "realtime";
	private static final String CMD_OPT_NAME_NUM_REALTIME_WORKERS = "realtime-worker-threads";

	static {
		FilesystemLogReplayerStarter.cmdlOpts.addOption(OptionBuilder.withArgName("dir1 ... dirN").hasArgs()
				.withLongOpt(FilesystemLogReplayerStarter.CMD_OPT_NAME_INPUTDIRS).isRequired(true)
				.withDescription("Log directories to read data from")
				.withValueSeparator('=').create("i"));
		FilesystemLogReplayerStarter.cmdlOpts.addOption(OptionBuilder
				.withArgName("true|false")
				.hasArg()
				.withLongOpt(FilesystemLogReplayerStarter.CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS)
				.isRequired(false)
				.withDescription(
						"Replay the original logging timestamps (defaults to true)?)")
				.withValueSeparator('=').create("k"));
		FilesystemLogReplayerStarter.cmdlOpts.addOption(OptionBuilder.withArgName("true|false").hasArg()
				.withLongOpt(FilesystemLogReplayerStarter.CMD_OPT_NAME_REALTIME).isRequired(true)
				.withDescription("Replay log data in realtime?")
				.withValueSeparator('=').create("r"));
		FilesystemLogReplayerStarter.cmdlOpts.addOption(OptionBuilder
				.withArgName("num")
				.hasArg()
				.withLongOpt(FilesystemLogReplayerStarter.CMD_OPT_NAME_NUM_REALTIME_WORKERS)
				.isRequired(false)
				.withDescription(
						"Number of worker threads used in realtime mode (defaults to 1).")
				.withValueSeparator('=').create("n"));
	}
	private static final Log log = LogFactory
			.getLog(FilesystemLogReplayerStarter.class);
	private static String[] inputDirs = null;
	private static boolean keepOriginalLoggingTimestamps;
	private static boolean realtimeMode = false;
	private static int numRealtimeWorkerThreads = -1;

	private static boolean parseArgs(final String[] args) {
		try {
			FilesystemLogReplayerStarter.cmdl = FilesystemLogReplayerStarter.cmdlParser.parse(FilesystemLogReplayerStarter.cmdlOpts, args);
		} catch (final ParseException e) {
			System.err.println("Error parsing arguments: " + e.getMessage());
			FilesystemLogReplayerStarter.printUsage();
			return false;
		}
		return true;
	}

	private static void printUsage() {
		FilesystemLogReplayerStarter.cmdHelpFormatter.printHelp(
				FilesystemLogReplayerStarter.class.getName(), FilesystemLogReplayerStarter.cmdlOpts);
	}

	private static boolean initFromArgs() {
		boolean retVal = true;

		/* 1.) init inputDirs */
		FilesystemLogReplayerStarter.inputDirs = FilesystemLogReplayerStarter.cmdl.getOptionValues(FilesystemLogReplayerStarter.CMD_OPT_NAME_INPUTDIRS);

		/* 2.) init keepOriginalLoggingTimestamps */
		final String keepOriginalLoggingTimestampsOptValStr = FilesystemLogReplayerStarter.cmdl.getOptionValue(
				FilesystemLogReplayerStarter.CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS, "true");
		if (!(keepOriginalLoggingTimestampsOptValStr.equals("true") || keepOriginalLoggingTimestampsOptValStr
				.equals("false"))) {
			System.out.println("Invalid value for option "
					+ FilesystemLogReplayerStarter.CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS + ": '"
					+ keepOriginalLoggingTimestampsOptValStr + "'");
			retVal = false;
		}
		FilesystemLogReplayerStarter.keepOriginalLoggingTimestamps = keepOriginalLoggingTimestampsOptValStr
				.equals("true");
		FilesystemLogReplayerStarter.log.info("Keeping original logging timestamps: "
				+ (FilesystemLogReplayerStarter.keepOriginalLoggingTimestamps ? "true" : "false"));

		/* 3.) init realtimeMode */
		final String realtimeOptValStr = FilesystemLogReplayerStarter.cmdl.getOptionValue(FilesystemLogReplayerStarter.CMD_OPT_NAME_REALTIME,
				"false");
		if (!(realtimeOptValStr.equals("true") || realtimeOptValStr
				.equals("false"))) {
			System.out.println("Invalid value for option "
					+ FilesystemLogReplayerStarter.CMD_OPT_NAME_REALTIME + ": '" + realtimeOptValStr + "'");
			retVal = false;
		}
		FilesystemLogReplayerStarter.realtimeMode = realtimeOptValStr.equals("true");

		/* 4.) init numRealtimeWorkerThreads */
		final String numRealtimeWorkerThreadsStr = FilesystemLogReplayerStarter.cmdl.getOptionValue(
				FilesystemLogReplayerStarter.CMD_OPT_NAME_NUM_REALTIME_WORKERS, "1");
		try {
			FilesystemLogReplayerStarter.numRealtimeWorkerThreads = Integer
					.parseInt(numRealtimeWorkerThreadsStr);
		} catch (final NumberFormatException exc) {
			System.out.println("Invalid value for option "
					+ FilesystemLogReplayerStarter.CMD_OPT_NAME_NUM_REALTIME_WORKERS + ": '"
					+ numRealtimeWorkerThreadsStr + "'");
			FilesystemLogReplayerStarter.log.error("NumberFormatException: ", exc);
			retVal = false;
		}
		if (FilesystemLogReplayerStarter.numRealtimeWorkerThreads < 1) {
			System.out.println("Option value for "
					+ FilesystemLogReplayerStarter.CMD_OPT_NAME_NUM_REALTIME_WORKERS
					+ " must be >= 1; found " + FilesystemLogReplayerStarter.numRealtimeWorkerThreads);
			FilesystemLogReplayerStarter.log.error("Invalid specification of "
					+ FilesystemLogReplayerStarter.CMD_OPT_NAME_NUM_REALTIME_WORKERS + ":"
					+ FilesystemLogReplayerStarter.numRealtimeWorkerThreads);
			retVal = false;
		}

		/* log configuration */
		if (retVal == true) {
			FilesystemLogReplayerStarter.log.info("inputDirs: "
					+ FilesystemLogReplayerStarter.StringArrayToDeliminedString(FilesystemLogReplayerStarter.inputDirs, ';'));
			FilesystemLogReplayerStarter.log.info("Replaying in " + (FilesystemLogReplayerStarter.realtimeMode ? "" : "non-")
					+ "realtime mode");
			if (FilesystemLogReplayerStarter.realtimeMode) {
				FilesystemLogReplayerStarter.log.info("Using " + FilesystemLogReplayerStarter.numRealtimeWorkerThreads
						+ " realtime worker thread"
						+ (FilesystemLogReplayerStarter.numRealtimeWorkerThreads > 1 ? "s" : ""));
			}
		}

		return retVal;
	}

	private static String StringArrayToDeliminedString(final String[] array,
			final char delimiter) {
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
			FilesystemLogReplayerStarter.log.info("Replaying log data in real time");
		} else {
			FilesystemLogReplayerStarter.log.info("Replaying log data in non-real time");
		}

		System.out.println(MonitoringController.getInstance()
				.getConnectorInfo());

		/**
		 * Force the controller to keep the original logging timestamps of the
		 * monitoring records.
		 */
		final MonitoringController monitoringController = MonitoringController.getInstance(); // use the singleton instance
		
		if (FilesystemLogReplayerStarter.keepOriginalLoggingTimestamps) {
			monitoringController.enableReplayMode();
		} else  {
			monitoringController.enableRealtimeMode();
		}
				
		final FilesystemLogReplayer player = new FilesystemLogReplayer(
				monitoringController,
				FilesystemLogReplayerStarter.inputDirs,
				FilesystemLogReplayerStarter.realtimeMode,
				FilesystemLogReplayerStarter.numRealtimeWorkerThreads);

		if (!player.replay()) {
			System.err.println("An error occured");
			System.err.println("");
			System.err.println("See 'kieker.log' for details");
			System.exit(1);
		}
	}
}
