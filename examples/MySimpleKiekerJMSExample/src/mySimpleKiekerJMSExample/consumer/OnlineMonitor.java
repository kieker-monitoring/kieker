/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src.mySimpleKiekerJMSExample.consumer;

import java.util.ArrayList;
import java.util.Collection;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.OperationExecutionRecord;
import kieker.tpan.TpanInstance;
import kieker.tpan.consumer.IMonitoringRecordConsumer;
import kieker.tpan.consumer.MonitoringRecordConsumerException;
import kieker.tpan.reader.IMonitoringLogReader;
import kieker.tpan.reader.JMSReader;
import kieker.tpan.reader.MonitoringLogReaderException;
import src.mySimpleKiekerJMSExample.record.MyRTMonitoringRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class OnlineMonitor implements IMonitoringRecordConsumer {

    private static final Log log = LogFactory.getLog(OnlineMonitor.class);
    private final long rtSloMs;

    private static final Collection<Class<? extends IMonitoringRecord>> recordTypeSubscriptionList =
            new ArrayList<Class<? extends IMonitoringRecord>>();
    static {
        recordTypeSubscriptionList.add(MyRTMonitoringRecord.class);
        recordTypeSubscriptionList.add(OperationExecutionRecord.class);
    }

    public OnlineMonitor(long rtSloMs) {
        this.rtSloMs = rtSloMs;
    }

    public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
        return recordTypeSubscriptionList;
    }

    public boolean newMonitoringRecord(IMonitoringRecord r) {
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
        return true;
    }

    public boolean invoke() {
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
        } catch (MonitoringLogReaderException e) {
            log.error("LogReaderExecutionException:", e);
        } catch (MonitoringRecordConsumerException e) {
            log.error("RecordConsumerExecutionException:", e);
        }
    }
}
