package kieker.common.tools.logReplayer;

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

import kieker.tpmon.core.TpmonController;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.OptionBuilder;
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
        cmdlOpts.addOption(OptionBuilder.withArgName("dir1 ... dirN").hasArgs().withLongOpt(CMD_OPT_NAME_INPUTDIRS).isRequired(true).withDescription("Log directories to read data from").withValueSeparator('=').create("i"));
        cmdlOpts.addOption(OptionBuilder.withArgName("true|false").hasArg().withLongOpt(CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS).isRequired(false).withDescription("Replay the original logging timestamps (defaults to true)?)").withValueSeparator('=').create("k"));
        cmdlOpts.addOption(OptionBuilder.withArgName("true|false").hasArg().withLongOpt(CMD_OPT_NAME_REALTIME).isRequired(true).withDescription("Replay log data in realtime?").withValueSeparator('=').create("r"));
        cmdlOpts.addOption(OptionBuilder.withArgName("num").hasArg().withLongOpt(CMD_OPT_NAME_NUM_REALTIME_WORKERS).isRequired(false).withDescription("Number of worker threads used in realtime mode (defaults to 1).").withValueSeparator('=').create("n"));
    }
    private static final Log log = LogFactory.getLog(FilesystemLogReplayerStarter.class);
    private static TpmonController ctrlInst = null;
    private static String[] inputDirs = null;
    private static boolean keepOriginalLoggingTimestamps;
    private static boolean realtimeMode = false;
    private static int numRealtimeWorkerThreads = -1;

    private static boolean parseArgs(String[] args) {
        try {
            cmdl = cmdlParser.parse(cmdlOpts, args);
        } catch (ParseException e) {
            System.err.println("Error parsing arguments: " + e.getMessage());
            printUsage();
            return false;
        }
        return true;
    }

    private static void printUsage() {
        cmdHelpFormatter.printHelp(FilesystemLogReplayerStarter.class.getName(), cmdlOpts);
    }

    private static boolean initFromArgs() {
        boolean retVal = true;

        /* 1.) init inputDirs */
        inputDirs = cmdl.getOptionValues(CMD_OPT_NAME_INPUTDIRS);

        /* 2.) init keepOriginalLoggingTimestamps */
       String keepOriginalLoggingTimestampsOptValStr = cmdl.getOptionValue(CMD_OPT_NAME_REALTIME, "true");
        if (!(keepOriginalLoggingTimestampsOptValStr.equals("true") || keepOriginalLoggingTimestampsOptValStr.equals("false"))) {
            System.out.println("Invalid value for option " + CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS + ": '" + keepOriginalLoggingTimestampsOptValStr + "'");
            retVal = false;
        }
        keepOriginalLoggingTimestamps = keepOriginalLoggingTimestampsOptValStr.equals("true");

        /* 3.) init realtimeMode */
        String realtimeOptValStr = cmdl.getOptionValue(CMD_OPT_NAME_REALTIME, "false");
        if (!(realtimeOptValStr.equals("true") || realtimeOptValStr.equals("false"))) {
            System.out.println("Invalid value for option " + CMD_OPT_NAME_REALTIME + ": '" + realtimeOptValStr + "'");
            retVal = false;
        }
        realtimeMode = realtimeOptValStr.equals("true");

        /* 4.) init numRealtimeWorkerThreads */
        String numRealtimeWorkerThreadsStr = cmdl.getOptionValue(CMD_OPT_NAME_NUM_REALTIME_WORKERS, "1");
        try {
            numRealtimeWorkerThreads = Integer.parseInt(numRealtimeWorkerThreadsStr);
        } catch (NumberFormatException exc) {
            System.out.println("Invalid value for option " + CMD_OPT_NAME_NUM_REALTIME_WORKERS + ": '" + numRealtimeWorkerThreadsStr + "'");
            log.error("NumberFormatException: ", exc);
            retVal = false;
        }
        if (numRealtimeWorkerThreads < 1) {
            System.out.println("Option value for " + CMD_OPT_NAME_NUM_REALTIME_WORKERS + " must be >= 1; found " + numRealtimeWorkerThreads);
            log.error("Invalid specification of " + CMD_OPT_NAME_NUM_REALTIME_WORKERS + ":" + numRealtimeWorkerThreads);
            retVal = false;
        }

        /* log configuration */
        if (retVal == true) {
            log.info("inputDirs: " + StringArrayToDeliminedString(inputDirs, ';'));
            log.info("Replaying in " + (realtimeMode ? "" : "non-") + "realtime mode");
            if (realtimeMode) {
                log.info("Using " + numRealtimeWorkerThreads + " realtime worker thread" + (numRealtimeWorkerThreads > 1 ? "s" : ""));
            }
        }

        return retVal;
    }

    private static String StringArrayToDeliminedString(String[] array, char delimiter) {
        StringBuilder arTostr = new StringBuilder();
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
        if (!parseArgs(args) || !initFromArgs()) {
            System.exit(1);
        }

        /* Parsed args and initialized variables */

        if (realtimeMode) {
            log.info("Replaying log data in real time");
        } else {
            log.info("Replaying log data in non-real time");
        }

        FilesystemLogReplayer player = new FilesystemLogReplayer(inputDirs, keepOriginalLoggingTimestamps, realtimeMode, numRealtimeWorkerThreads);

        if (!player.execute()) {
            System.err.println("An error occured");
            System.err.println("");
            System.err.println("See 'kieker.log' for details");
            System.exit(1);
        }
    }
}
