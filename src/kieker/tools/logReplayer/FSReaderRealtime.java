package kieker.tools.logReplayer;

import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.PropertyMap;
import kieker.tpan.TpanInstance;
import kieker.tpan.reader.AbstractMonitoringLogReader;
import kieker.tpan.consumer.IMonitoringRecordConsumer;
import kieker.tpan.reader.LogReaderExecutionException;
import kieker.tpan.consumer.MonitoringRecordConsumerExecutionException;

import kieker.tpan.reader.filesystem.FSReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class FSReaderRealtime extends AbstractMonitoringLogReader {

    private static final Log log = LogFactory.getLog(FSReaderRealtime.class);

    /* manages the lifecycle of the reader and consumers */
    private final TpanInstance tpanInstance = new TpanInstance();
    private RealtimeReplayDistributor rtDistributor = null;
    private static final String PROP_NAME_NUM_WORKERS = "numWorkers";
    private static final String PROP_NAME_INPUTDIRNAMES = "inputDirs";
    /** Reader will wait for this latch before read() returns */
    private final CountDownLatch terminationLatch = new CountDownLatch(1);

    /**
     * Acts as a consumer to the rtDistributor and delegates incoming records
     * to the FSReaderRealtime instance.
     */
    private class FSReaderRealtimeCons implements IMonitoringRecordConsumer {

        private final FSReaderRealtime master;

        public FSReaderRealtimeCons(FSReaderRealtime master) {
            this.master = master;
        }

        public String[] getRecordTypeSubscriptionList() {
            return null;
        }

        public void consumeMonitoringRecord(IMonitoringRecord monitoringRecord) throws MonitoringRecordConsumerExecutionException {
            try {
                this.master.deliverRecordToConsumers(monitoringRecord);
            } catch (LogReaderExecutionException ex) {
                log.error("LogReaderExecutionException", ex);
                throw new MonitoringRecordConsumerExecutionException("LogReaderExecutionException", ex);
            }
        }

        public boolean execute() throws MonitoringRecordConsumerExecutionException {
            /* do nothing */
            return true;
        }

        public void terminate(final boolean error) {
        }
    }

    /** Constructor for FSReaderRealtime. Requires a subsequent call to the init
     *  method in order to specify the input directory and number of workers
     *  using the parameter @a inputDirName. */
    public FSReaderRealtime() {
    }

    /** Valid key/value pair: inputDirNames=INPUTDIRECTORY1;...;INPUTDIRECTORYN | numWorkers=XX */
    public void init(String initString) throws IllegalArgumentException {
        PropertyMap propertyMap = new PropertyMap(initString, "|", "="); // throws IllegalArgumentException
        String numWorkersString = propertyMap.getProperty(PROP_NAME_NUM_WORKERS);
        int numWorkers = -1;
        if (numWorkersString == null) {
            throw new IllegalArgumentException("Missing init parameter '" + PROP_NAME_NUM_WORKERS + "'");
        }
        try {
            numWorkers = Integer.parseInt(numWorkersString);
        } catch (NumberFormatException ex) { /* value of numWorkers remains -1 */ }

        initInstanceFromArgs(inputDirNameListToArray(propertyMap.getProperty(PROP_NAME_INPUTDIRNAMES)), numWorkers);
    }

    public FSReaderRealtime(final String[] inputDirNames, int numWorkers) {
        initInstanceFromArgs(inputDirNames, numWorkers);
    }

    private String[] inputDirNameListToArray(final String inputDirNameList) throws IllegalArgumentException {
        String[] dirNameArray;

        // parse inputDir property value
        if (inputDirNameList == null || inputDirNameList.trim().length() == 0) {
            log.error("Invalid argument value for inputDirNameList:" + inputDirNameList);
            throw new IllegalArgumentException("Invalid argument value for inputDirNameList:" + inputDirNameList);
        }
        try {
            StringTokenizer dirNameTokenizer = new StringTokenizer(inputDirNameList, ";");
            dirNameArray = new String[dirNameTokenizer.countTokens()];
            for (int i = 0; dirNameTokenizer.hasMoreTokens(); i++) {
                dirNameArray[i] = dirNameTokenizer.nextToken().trim();
            }
        } catch (Exception exc) {
            throw new IllegalArgumentException("Error parsing list of input directories'" + inputDirNameList + "'", exc);
        }
        return dirNameArray;
    }

    private void initInstanceFromArgs(final String[] inputDirNames, int numWorkers) throws IllegalArgumentException {
        if (inputDirNames == null || inputDirNames.length <= 0) {
            throw new IllegalArgumentException("Invalid property value for " + PROP_NAME_INPUTDIRNAMES + ":" + inputDirNames);
        }

        if (numWorkers <= 0) {
            throw new IllegalArgumentException("Invalid proprty value for " + PROP_NAME_NUM_WORKERS + ": " + numWorkers);
        }

        final AbstractMonitoringLogReader fsReader = new FSReader(inputDirNames);
        final IMonitoringRecordConsumer rtCons = new FSReaderRealtimeCons(this);
        rtDistributor = new RealtimeReplayDistributor(numWorkers, rtCons, terminationLatch);
        //fsReader.addConsumer(rtDistributor, null);
        this.tpanInstance.setLogReader(fsReader);
        this.tpanInstance.addRecordConsumer(rtDistributor);
    }

    /**
     * Replays the monitoring log in real-time and returns after the complete
     * log was being replayed.
     */
    public boolean read() throws LogReaderExecutionException {
        boolean success = true;
        try {
            this.tpanInstance.run();
            this.terminationLatch.await();
        } catch (Exception ex) {
            log.error("An error occured while reading", ex);
            throw new LogReaderExecutionException("An error occured while reading", ex);
        }
        return success;
    }
}
