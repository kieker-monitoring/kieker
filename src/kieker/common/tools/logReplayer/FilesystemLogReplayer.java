package kieker.common.tools.logReplayer;

import kieker.common.logReader.IMonitoringRecordConsumer;
import kieker.common.logReader.filesystemReader.FilesystemReader;
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
    }
    private static final Log log = LogFactory.getLog(FilesystemLogReplayer.class);
    private static TpmonController ctrlInst = null;
    private static String inputDir = null;
    private static boolean realtimeMode = false;

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
        inputDir = cmdl.getOptionValue("inputdir");
        log.info("inputDir: " + inputDir);
        realtimeMode = cmdl.getOptionValue("realtime","false").equals("true");
        return true;
    }

    public static void main(final String[] args) {
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

        FilesystemReader fsReader = new FilesystemReader(inputDir);

        IMonitoringRecordConsumer cons = null;
        if (realtimeMode) {
            cons = new ReplayDistributor(7);
            fsReader.addConsumer(
                    cons,
                    null); // consume records of all types
        } else {
            fsReader.addConsumer(cons = new IMonitoringRecordConsumer() {

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

                @Override
                public void terminate() {
                    ctrlInst.terminateMonitoring();
                }
            }, null); // consume records of all types
        }
        if (!cons.execute() || !fsReader.execute()){
            log.error("Log Replay failed");
            System.exit(0);
        }
    }
}
