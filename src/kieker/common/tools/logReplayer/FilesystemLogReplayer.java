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

    static{
        cmdlOpts.addOption("h", "help", false, "Show help");
    }

    private static final Log log = LogFactory.getLog(FilesystemLogReplayer.class);
    private static final TpmonController ctrlInst = TpmonController.getInstance();
    private static String inputDir = null;
    private static boolean realtimeMode = false;

    private static boolean parseArgs(String[] args){
        try {
            cmdl = cmdlParser.parse(cmdlOpts, args);
        } catch (ParseException e) {
            log.error("Parse Exception", e);
            cmdHelpFormatter.printHelp(FilesystemLogReplayer.class.getName(), cmdlOpts);
            return false;
        }
        return true;
    }

    public static void main(final String[] args) {

        parseArgs(args);

        inputDir = "tmp//tpmon-20090629-154255/";//System.getProperty("inputDir");
        if (inputDir == null || inputDir.length() == 0 || inputDir.equals("${inputDir}")) {
            log.error("No input dir found!");
            log.error("Provide an input dir as system property.");
            log.error("Example to read all tpmon-* files from /tmp:\n" +
                    "                    ant -DinputDir=/tmp/ -DrealtimeMode=[true|false] run-reader    ");
            System.exit(1);
        } else {
            log.info("Reading all tpmon-* files from " + inputDir);
        }

        String realTimeModeStr = "true";//System.getProperty("realtimeMode");
        if (realTimeModeStr != null && realTimeModeStr.equalsIgnoreCase("true")) {
            log.info("Replaying log data in real time");
            realtimeMode = true;
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

                public void execute() {
                    // do nothing, we are synchronous
                }

				@Override
				public void terminate() {
				}
            }, null); // consume records of all types
        }
        cons.execute();
        fsReader.run();

        log.info("Finished to read files");
    //System.exit(0);
    }
}
