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
public class LogReplayer {
    private static final Log log = LogFactory.getLog(LogReplayer.class);

    private static String inputDir = null;

    public static void main(String[] args) {

        inputDir = System.getProperty("inputDir");
        if (inputDir == null || inputDir.length() == 0 || inputDir.equals("${inputDir}")) {
            log.error("No input dir found!");
            log.error("Provide an input dir as system property.");
            log.error("Example to read all tpmon-* files from /tmp:\n" +
                    "                    ant -DinputDir=/tmp/ run-reader    ");
            System.exit(1);
        } else {
            log.info("Reading all tpmon-* files from " + inputDir);
        }

        FilesystemReader fsReader = new FilesystemReader(inputDir);
        fsReader.registerConsumer(new IMonitoringRecordConsumer() {
            /** Anonymous consumer class that simply passes all records to the
             *  controller */
            public String[] getRecordTypeSubscriptionList() {
                return null; // consume all types
            }

            public void consumeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) {
                TpmonController.getInstance().logMonitoringRecord(monitoringRecord);
            }

            public void run() {
                // do nothing, we are synchronous
            }
        });
        fsReader.run();

        log.info("Finished to read files");
        System.exit(0);
    }
}
