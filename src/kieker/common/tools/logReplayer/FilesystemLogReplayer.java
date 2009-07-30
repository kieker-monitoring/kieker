package kieker.common.tools.logReplayer;

import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.common.logReader.LogReaderExecutionException;
import kieker.common.logReader.filesystemReader.FSReader;
import kieker.tpmon.core.TpmonController;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

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
public class FilesystemLogReplayer {

    private static CommandLine cmdl = null;
    private static final CommandLineParser cmdlParser = new BasicParser();
    private static final HelpFormatter cmdHelpFormatter = new HelpFormatter();
    private static final Options cmdlOpts = new Options();


    static {
        cmdlOpts.addOption(OptionBuilder.withArgName("dir").hasArg().withLongOpt("inputdir").isRequired(true).withDescription("Log directory to read data from").withValueSeparator('=').create("i"));
        cmdlOpts.addOption(OptionBuilder.withArgName("true|false").hasArg().withLongOpt("realtime").isRequired(true).withDescription("Replay log data in realtime?").withValueSeparator('=').create("r"));
        cmdlOpts.addOption(OptionBuilder.withArgName("num").hasArg().withLongOpt("realtime-worker-threads").isRequired(false).withDescription("Number of worker threads used in realtime mode (defaults to 1).").withValueSeparator('=').create("n"));
    }
    private static final Log log = LogFactory.getLog(FilesystemLogReplayer.class);
    private static TpmonController ctrlInst = null;
    private static String inputDir = null;
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
        cmdHelpFormatter.printHelp(FilesystemLogReplayer.class.getName(), cmdlOpts);
    }

    private static boolean initFromArgs() {
        boolean retVal = true;

        /* 1.) init inputDir */
        inputDir = cmdl.getOptionValue("inputdir");

        /* 2.) init realtimeMode */
        String realtimeOptValStr = cmdl.getOptionValue("realtime", "false");
        if (!(realtimeOptValStr.equals("true") || realtimeOptValStr.equals("false"))) {
            System.out.println("Invalid value for option realtime: '" + realtimeOptValStr + "'");
            retVal = false;
        }
        realtimeMode = realtimeOptValStr.equals("true");

        /* 3.) init numRealtimeWorkerThreads */
       String numRealtimeWorkerThreadsStr = cmdl.getOptionValue("realtime-worker-threads", "1");
        try {
            numRealtimeWorkerThreads = Integer.parseInt(numRealtimeWorkerThreadsStr);
        } catch (NumberFormatException exc) {
            System.out.println("Invalid value for option realtime-worker-threads: '" + numRealtimeWorkerThreadsStr + "'");
            log.error("NumberFormatException: ", exc);
            retVal = false;
        }
       if (numRealtimeWorkerThreads < 1) {
           System.out.println("Option value for realtime-worker-threads must be >= 1; found " + numRealtimeWorkerThreads);
           log.error("Invalid specification of numRealtimeWorkerThreads:" + numRealtimeWorkerThreads);
           retVal = false;
       }

        /* log configuration */
        if (retVal == true) {
            log.info("inputDir: " + inputDir);
            log.info("Replaying in " + (realtimeMode ? "" : "non-") + "realtime mode");
            if (realtimeMode){
                log.info("Using " + numRealtimeWorkerThreads + " realtime worker thread" + (numRealtimeWorkerThreads>1?"s":""));
            }
        }

        return retVal;
    }

    public static void main(final String[] args) {
        int retVal = 0;
        if (!parseArgs(args) || !initFromArgs()) {
            System.exit(1);
        }

        /* Parsed args and initialized variables */

        ctrlInst = TpmonController.getInstance();

        if (realtimeMode) {
            log.info("Replaying log data in real time");
        } else {
            log.info("Replaying log data in non-real time");
        }

        /**
         * Force the controller to keep the original logging timestamps
         * of the monitoring records.
         */
        ctrlInst.setReplayMode(realtimeMode);

        FSReader fsReader = new FSReader(inputDir);

        IKiekerRecordConsumer logCons = new IKiekerRecordConsumer() {

            /** Anonymous consumer class that simply passes all records to the
             *  controller */
            public String[] getRecordTypeSubscriptionList() {
                return null; // consume all types
            }

            public void consumeMonitoringRecord(final AbstractKiekerMonitoringRecord monitoringRecord) {
                ctrlInst.logMonitoringRecord(monitoringRecord);
            }

            public boolean execute() {
                // do nothing, we are synchronous
                return true;
            }

            public void terminate() {
                ctrlInst.terminateMonitoring();
            }
        };
        if (realtimeMode) {
            IKiekerRecordConsumer rtDistributorCons = new ReplayDistributor(numRealtimeWorkerThreads, logCons);
            fsReader.addConsumer(
                    rtDistributorCons,
                    null); // consume records of all types
        } else {
            fsReader.addConsumer(logCons, null); // consume records of all types
        }
        try {
            if (!fsReader.execute()) {
                // here, we do not start consumers since they don't do anything in execute()
                log.error("Log Replay failed");
                retVal = 1;
            }
        } catch (LogReaderExecutionException ex) {
            log.error("LogReaderExecutioException", ex);
            retVal = 1;
        }
        if (retVal != 0) {
            System.err.println("An error occured");
            System.err.println("");
            System.err.println("See 'kieker.log' for details");
            System.exit(retVal);
        }
    }
}
