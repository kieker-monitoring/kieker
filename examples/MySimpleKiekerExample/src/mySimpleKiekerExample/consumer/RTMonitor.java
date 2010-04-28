package mySimpleKiekerExample.consumer;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */

import java.util.ArrayList;
import java.util.Collection;
import kieker.common.record.IMonitoringRecord;
import kieker.tools.logReplayer.FSReaderRealtime;
import kieker.tpan.TpanInstance;
import kieker.tpan.consumer.IMonitoringRecordConsumerPlugin;
import kieker.tpan.consumer.MonitoringRecordConsumerException;
import kieker.tpan.reader.MonitoringLogReaderException;
import mySimpleKiekerExample.record.MyRTMonitoringRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class RTMonitor implements IMonitoringRecordConsumerPlugin {

    private static final Log log = LogFactory.getLog(RTMonitor.class);
    private final long rtSloMs;

    public RTMonitor(long rtSloMs) {
        this.rtSloMs = rtSloMs;
    }

   private static final Collection<Class<? extends IMonitoringRecord>> recordTypeSubscriptionList =
            new ArrayList<Class<? extends IMonitoringRecord>>();
    static {
        recordTypeSubscriptionList.add(MyRTMonitoringRecord.class);
    }

    public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
        return recordTypeSubscriptionList;
    }

    public boolean newMonitoringRecord(IMonitoringRecord r) {
        MyRTMonitoringRecord rtRec = (MyRTMonitoringRecord) r;
        long rtMs = rtRec.rt/(1000*1000);
        if (rtMs > this.rtSloMs) {
            log.error("rtRec.rt (ms) > this.rtSloMs: " + rtMs + ">" + this.rtSloMs);
        } else {
            log.info("rtRec.rt (ms) <=this.rtSloMs: " + rtMs + "<=" + this.rtSloMs);
        }
        return true;
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
        analysisInstance.registerPlugin(rtMonitor);

        try {
            analysisInstance.run();
        } catch (MonitoringLogReaderException e) {
            log.error("LogReaderExecutionException:", e);
        } catch (MonitoringRecordConsumerException e) {
            log.error("RecordConsumerExecutionException:", e);
        }
    }
}
