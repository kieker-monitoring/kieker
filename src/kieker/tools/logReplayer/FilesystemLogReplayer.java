package kieker.tools.logReplayer;

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
import java.util.Collection;
import kieker.common.record.IMonitoringRecord;
import kieker.analysis.AnalysisInstance;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.reader.AbstractMonitoringLogReader;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.monitoring.core.MonitoringController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class FilesystemLogReplayer {

    private static final Log log = LogFactory.getLog(FilesystemLogReplayer.class);
    private static final MonitoringController ctrlInst = MonitoringController.getInstance();
    private String[] inputDirs = null;
    private volatile boolean realtimeMode = false;
    private boolean keepOriginalLoggingTimestamps = true;
    private int numRealtimeWorkerThreads = -1;

    private FilesystemLogReplayer() {
    }

    /** Normal replay mode (i.e., non-real-time). */
    public FilesystemLogReplayer(final String[] inputDirs) {
        this.inputDirs = inputDirs;
    }

    public FilesystemLogReplayer(final String[] inputDirs, final boolean keepOriginalLoggingTimestamps, final boolean realtimeMode, final int numRealtimeWorkerThreads) {
        this.inputDirs = inputDirs;
        this.realtimeMode = realtimeMode;
        this.numRealtimeWorkerThreads = numRealtimeWorkerThreads;
        this.keepOriginalLoggingTimestamps = keepOriginalLoggingTimestamps;
    }

    /**
     * @return true on success; false otherwise */
    public boolean replay() {
        boolean success = true;

        /**
         * Force the controller to keep the original logging timestamps
         * of the monitoring records.
         */
        ctrlInst.setControllerMode(this.keepOriginalLoggingTimestamps?MonitoringController.ControllerMode.REPLAY:MonitoringController.ControllerMode.REALTIME);

        IMonitoringRecordConsumerPlugin logCons = new IMonitoringRecordConsumerPlugin() {

            /** Anonymous consumer class that simply passes all records to the
             *  controller */
            public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
                return null; // consume all types
            }

            public boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
                return ctrlInst.newMonitoringRecord(monitoringRecord);
            }

            public boolean execute() {
                return true; // no need to do anything
            }

            public void terminate(boolean error) {
                // no need to do anything
            }
        };
        AbstractMonitoringLogReader fsReader;
        if (realtimeMode) {
            fsReader = new FSReaderRealtime(inputDirs, numRealtimeWorkerThreads);
        } else {
            fsReader = new FSReader(inputDirs);
        }
        AnalysisInstance tpanInstance = new AnalysisInstance();
        tpanInstance.setLogReader(fsReader);
        tpanInstance.registerPlugin(logCons);
        try {
            tpanInstance.run();
            success = true;
        } catch (Exception ex) {
            log.error("Exception", ex);
            success = false;
        }
        return success;
    }
}
