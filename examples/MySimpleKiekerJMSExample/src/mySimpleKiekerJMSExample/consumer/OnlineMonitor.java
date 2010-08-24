package mySimpleKiekerJMSExample.consumer;

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
 *
 */

import java.util.ArrayList;
import java.util.Collection;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.OperationExecutionRecord;
import kieker.analysis.AnalysisController;
import kieker.analysis.reader.IMonitoringLogReader;
import kieker.analysis.reader.JMSReader;
import kieker.analysis.reader.MonitoringLogReaderException;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.plugin.MonitoringRecordConsumerException;
import mySimpleKiekerJMSExample.record.MyRTMonitoringRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class OnlineMonitor implements IMonitoringRecordConsumerPlugin {

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

        AnalysisController analysisInstance = new AnalysisController();
        analysisInstance.setLogReader(logReader);
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
