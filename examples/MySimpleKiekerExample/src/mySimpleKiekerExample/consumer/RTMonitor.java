/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src.mySimpleKiekerExample.consumer;

import kieker.common.record.IMonitoringRecord;
import kieker.tools.logReplayer.FSReaderRealtime;
import kieker.tpan.TpanInstance;
import kieker.tpan.consumer.IMonitoringRecordConsumer;
import kieker.tpan.consumer.MonitoringRecordConsumerExecutionException;
import kieker.tpan.reader.LogReaderExecutionException;
import src.mySimpleKiekerExample.record.MyRTMonitoringRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class RTMonitor implements IMonitoringRecordConsumer {

    private static final Log log = LogFactory.getLog(RTMonitor.class);
    private final long rtSloMs;

    public RTMonitor(long rtSloMs) {
        this.rtSloMs = rtSloMs;
    }

    public Class<? extends IMonitoringRecord>[] getRecordTypeSubscriptionList() {
        return new Class[]{MyRTMonitoringRecord.class};
    }

    public void consumeMonitoringRecord(IMonitoringRecord r) throws MonitoringRecordConsumerExecutionException {
        MyRTMonitoringRecord rtRec = (MyRTMonitoringRecord) r;
        long rtMs = rtRec.rt/(1000*1000);
        if (rtMs > this.rtSloMs) {
            log.error("rtRec.rt (ms) > this.rtSloMs: " + rtMs + ">" + this.rtSloMs);
        } else {
            log.info("rtRec.rt (ms) <=this.rtSloMs: " + rtMs + "<=" + this.rtSloMs);
        }
    }

    public boolean execute() {
        return true;
    }

    public void terminate(final boolean error) {
    }

    public static void main(String[] args) {
        log.info("Hi, this is " + RTMonitor.class.getName());

        String inputDir = System.getProperty("inputDir");
        if (inputDir == null || inputDir.length() == 0 || inputDir.equals("${inputDir}")) {
            log.error("No input dir found!");
            log.error("Provide an input dir as system property.");
            log.error("Example to read all tpmon-* files from /tmp:\n" +
                    "                    ant -DinputDir=/tmp/ run-RTMonitor    ");
            System.exit(1);
        } else {
            log.info("Reading all tpmon-* files from " + inputDir);
        }

        RTMonitor rtMonitor = new RTMonitor(1800);

        FSReaderRealtime fsReaderRealtime =
                new FSReaderRealtime(new String[]{inputDir}, 7);
        TpanInstance analysisInstance = new TpanInstance();
        analysisInstance.setLogReader(fsReaderRealtime);
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
