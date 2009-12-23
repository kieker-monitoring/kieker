/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mySimpleKiekerJMSExample.recordConsumer;

import kieker.common.logReader.IKiekerMonitoringLogReader;
import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.common.logReader.LogReaderExecutionException;
import kieker.common.logReader.RecordConsumerExecutionException;
import kieker.common.logReader.filesystemReader.realtime.FSReaderRealtime;
import kieker.tpan.TpanInstance;
import kieker.tpan.logReader.JMSReader;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;
import mySimpleKiekerJMSExample.monitoringRecord.MyRTMonitoringRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author avanhoorn
 */
public class OnlineMonitor implements IKiekerRecordConsumer {

    private static final Log log = LogFactory.getLog(OnlineMonitor.class);
    private final long rtSloMs;

    public OnlineMonitor(long rtSloMs) {
        this.rtSloMs = rtSloMs;
    }

    public String[] getRecordTypeSubscriptionList() {
        return new String[]{MyRTMonitoringRecord.class.getName()};
    }

    public void consumeMonitoringRecord(AbstractKiekerMonitoringRecord r) throws RecordConsumerExecutionException {
        if (r instanceof KiekerExecutionRecord) {
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

    public void terminate() {
    }

    public static void main(String[] args) {
        log.info("Hi, this is " + OnlineMonitor.class.getName());

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

        OnlineMonitor rtMonitor = new OnlineMonitor(1800);

        IKiekerMonitoringLogReader logReader;
        logReader = new FSReaderRealtime(inputDir, 7);
        logReader = new JMSReader("tcp://127.0.0.1:3035/", "queue1");

        TpanInstance analysisInstance = new TpanInstance();
        analysisInstance.setLogReader(logReader);
        analysisInstance.addRecordConsumer(rtMonitor);

        try {
            analysisInstance.run();
        } catch (LogReaderExecutionException e) {
            log.error("LogReaderExecutionException:", e);
        } catch (RecordConsumerExecutionException e) {
            log.error("RecordConsumerExecutionException:", e);
        }
    }
}
