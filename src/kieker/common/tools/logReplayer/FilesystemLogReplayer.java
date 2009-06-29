package kieker.common.tools.logReplayer;

import kieker.common.logReader.IMonitoringRecordConsumer;
import kieker.common.logReader.filesystemReader.FilesystemReader;
import kieker.tpmon.core.TpmonController;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class FilesystemLogReplayer {

    private static final Log log = LogFactory.getLog(FilesystemLogReplayer.class);
    private static final TpmonController ctrlInst = TpmonController.getInstance();
    private static String inputDir = null;

    private static boolean realtimeMode = false;

    public static void main(String[] args) {

        inputDir = System.getProperty("inputDir");
        if (inputDir == null || inputDir.length() == 0 || inputDir.equals("${inputDir}")) {
            log.error("No input dir found!");
            log.error("Provide an input dir as system property.");
            log.error("Example to read all tpmon-* files from /tmp:\n" +
                    "                    ant -DinputDir=/tmp/ -DrealtimeMode=[true|false] run-reader    ");
            System.exit(1);
        } else {
            log.info("Reading all tpmon-* files from " + inputDir);
        }

        /**
         * Force the controller to keep the original logging timestamps
         * of the monitoring records.
         */
        ctrlInst.setReplayMode(true);

        String realTimeModeStr = System.getProperty("realtimeMode");
        if (realTimeModeStr != null  && realTimeModeStr.equalsIgnoreCase("true") ){
            log.info("Replaying log data in real time");
            realtimeMode = true;
        }else{
            log.info("Replaying log data in non-real time");
        }

        FilesystemReader fsReader = new FilesystemReader(inputDir);
        if (realtimeMode) {
            fsReader.addConsumer(
                    new ReplayDistributor(7),
                    null); // consume records of all types
        } else {
            fsReader.addConsumer(new IMonitoringRecordConsumer() {

                /** Anonymous consumer class that simply passes all records to the
                 *  controller */
                public String[] getRecordTypeSubscriptionList() {
                    return null; // consume all types
                }

                public void consumeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) {
                    ctrlInst.logMonitoringRecord(monitoringRecord);
                }

                public void execute() {
                    // do nothing, we are synchronous
                }
            }, null); // consume records of all types
        }
        
        fsReader.run();

        log.info("Finished to read files");
        System.exit(0);
    }
}
