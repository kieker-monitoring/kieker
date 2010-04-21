/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mySimpleKiekerJMSExample.recordConsumer;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.OperationExecutionRecord;
import kieker.tpan.TpanInstance;
import kieker.tpan.consumer.IMonitoringRecordConsumer;
import kieker.tpan.consumer.MonitoringRecordConsumerExecutionException;
import kieker.tpan.reader.IMonitoringLogReader;
import kieker.tpan.reader.JMSReader;
import kieker.tpan.reader.LogReaderExecutionException;
import mySimpleKiekerJMSExample.monitoringRecord.MyRTMonitoringRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class OnlineMonitor implements IMonitoringRecordConsumer {

    private static final Log log = LogFactory.getLog(OnlineMonitor.class);
    private final long rtSloMs;

    public OnlineMonitor(long rtSloMs) {
        this.rtSloMs = rtSloMs;
    }

    public String[] getRecordTypeSubscriptionList() {
        return new String[]{
            MyRTMonitoringRecord.class.getName(),
            OperationExecutionRecord.class.getName()
        };
    }

    public void consumeMonitoringRecord(IMonitoringRecord r) throws MonitoringRecordConsumerExecutionException {
        if (r instanceof OperationExecutionRecord) {
            OperationExecutionRecord execRec = (OperationExecutionRecord) r;
            log.info("Received execution record:" + execRec);
        } else if (r instanceof MyRTMonitoringRecord) {
            MyRTMonitoringRecord rtRec = (MyRTMonitoringRecord) r;
            long rtMs = rtRec.rt / (1000 * 1000);
            if (rtMs > this.rtSloMs) {
                log.error("rtRec.rt (ms) > this.rtSloMs: " + rtMs + ">" + this.rtSloMs);
            } else {
                log.info("rtRec.rt (ms) <=this.rtSloMs: " + rtMs + "<=" + this.rtSloMs);
            }
        }
    }

    public boolean execute() {
        return true;
    }

    public void terminate(final boolean error) {
    }

    public static void main(String[] args) {
        log.info("Hi, this is " + OnlineMonitor.class.getName());

        OnlineMonitor rtMonitor = new OnlineMonitor(1800);

        IMonitoringLogReader logReader;
        logReader = new JMSReader("tcp://127.0.0.1:3035/", "queue1");

        TpanInstance analysisInstance = new TpanInstance();
        analysisInstance.setLogReader(logReader);
        analysisInstance.addRecordConsumer(rtMonitor);

        try {
            analysisInstance.run();
        } catch (LogReaderExecutionException e) {
            log.error("LogReaderExecutionException:", e);
        } catch (MonitoringRecordConsumerExecutionException e) {
            log.error("RecordConsumerExecutionException:", e);
        }
    }
}
