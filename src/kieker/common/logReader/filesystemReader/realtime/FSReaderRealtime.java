package kieker.common.logReader.filesystemReader.realtime;

import kieker.common.logReader.AbstractKiekerMonitoringLogReader;
import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.common.logReader.LogReaderExecutionException;
import kieker.common.logReader.RecordConsumerExecutionException;
import kieker.common.logReader.filesystemReader.FSReader;

import kieker.common.logReader.RealtimeReplayDistributor;
import kieker.tpmon.annotation.TpmonInternal;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class FSReaderRealtime extends AbstractKiekerMonitoringLogReader {

    private static final Log log = LogFactory.getLog(FSReaderRealtime.class);

    /* delegate */
    private FSReader fsReader;
    private RealtimeReplayDistributor rtDistributor = null;

    /**
     * Acts as a consumer to the rtDistributor and delegates incoming records
     * to the FSReaderRealtime instance.
     */
    private class FSReaderRealtimeCons implements IKiekerRecordConsumer {

        private final FSReaderRealtime master;

        public FSReaderRealtimeCons(FSReaderRealtime master) {
            this.master = master;
        }

        public String[] getRecordTypeSubscriptionList() {
            return null;
        }

        public void consumeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) throws RecordConsumerExecutionException {
            try {
                this.master.deliverRecordToConsumers(monitoringRecord);
            } catch (LogReaderExecutionException ex) {
                log.info("LogReaderExecutionException", ex);
                throw new RecordConsumerExecutionException("LogReaderExecutionException", ex);
            }
        }

        public boolean execute() throws RecordConsumerExecutionException {
            /* do nothing */
            return true;
        }

        public void terminate() {
            this.master.terminate();
        }
    }

    /** Constructor for FSReaderRealtime. Requires a subsequent call to the init
     *  method in order to specify the input directory and number of workers
     *  using the parameter @a inputDirName. */
    public FSReaderRealtime() {
    }

    /** Valid key/value pair: inputDirName=INPUTDIRECTORY | numWorkers=XX */
    @TpmonInternal()
    public void init(String initString) throws IllegalArgumentException {
        super.initVarsFromInitString(initString);

        String numWorkersString = this.getInitProperty("numWorkers");
        int numWorkers = -1;
        if (numWorkersString == null) {
            throw new IllegalArgumentException("Missing init parameter 'numWorkers'");
        }
        try {
            numWorkers = Integer.parseInt(numWorkersString);
        } catch (NumberFormatException ex) { /* value of numWorkers remains -1 */ }

        initInstanceFromArgs(this.getInitProperty("inputDirName"), numWorkers);
    }

    public FSReaderRealtime(final String inputDirName, int numWorkers) {
        initInstanceFromArgs(inputDirName, numWorkers);
    }

    private void initInstanceFromArgs(final String inputDirName, int numWorkers) throws IllegalArgumentException {
        if (inputDirName == null || inputDirName.equals("")) {
            throw new IllegalArgumentException("Invalid proprty value for inputDirName:" + inputDirName);
        }

        if (numWorkers <= 0) {
            throw new IllegalArgumentException("Invalid proprty value for numWorkers: " + numWorkers);
        }

        fsReader = new FSReader(inputDirName);
        IKiekerRecordConsumer rtCons = new FSReaderRealtimeCons(this);
        rtDistributor = new RealtimeReplayDistributor(numWorkers, rtCons);
        fsReader.addConsumer(rtDistributor, null);
    }

    public boolean execute() throws LogReaderExecutionException {
        return this.fsReader.execute();
    }
}
