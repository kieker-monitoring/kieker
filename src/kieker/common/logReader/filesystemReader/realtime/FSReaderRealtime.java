/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kieker.common.logReader.filesystemReader.realtime;

import kieker.common.logReader.AbstractKiekerMonitoringLogReader;
import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.common.logReader.LogReaderExecutionException;
import kieker.common.logReader.RecordConsumerExecutionException;
import kieker.common.logReader.filesystemReader.FSReader;

import kieker.common.logReader.RealtimeReplayDistributor;
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
    private final FSReader fsReader;
    private RealtimeReplayDistributor rtDistributor = null;

    /**
     * Acts as a consumer to the rtDistributor and delegates incoming records
     * to the FSReaderRealtime instance.
     */
    private class FSReaderRealtimeCons implements IKiekerRecordConsumer {
        FSReaderRealtime master;

        public FSReaderRealtimeCons (FSReaderRealtime master){
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

    public FSReaderRealtime(final String inputDirName, int numWorkers) {
        fsReader = new FSReader(inputDirName);
        IKiekerRecordConsumer rtCons = new FSReaderRealtimeCons(this);
        rtDistributor = new RealtimeReplayDistributor(numWorkers, rtCons);
        fsReader.addConsumer(rtDistributor, null);
    }

    public boolean execute() throws LogReaderExecutionException {
        return this.fsReader.execute();
    }
}
