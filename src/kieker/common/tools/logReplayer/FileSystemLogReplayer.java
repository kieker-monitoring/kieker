package kieker.common.tools.logReplayer;

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

import kieker.common.logReader.AbstractKiekerMonitoringLogReader;
import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.common.logReader.LogReaderExecutionException;
import kieker.common.logReader.filesystemReader.FSReader;
import kieker.common.logReader.filesystemReader.realtime.FSReaderRealtime;
import kieker.tpmon.core.TpmonController;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class FileSystemLogReplayer {
    private static final Log log = LogFactory.getLog(FileSystemLogReplayer.class);
    private static final TpmonController ctrlInst = TpmonController.getInstance();
    private String inputDir = null;
    private boolean realtimeMode = false;
    private int numRealtimeWorkerThreads = -1;

    private FileSystemLogReplayer() {}

    /** Normal replay mode (i.e., non-real-time). */
    public FileSystemLogReplayer(final String inputDir){
        this.inputDir = inputDir;
    }

    public FileSystemLogReplayer(final String inputDir, final boolean realtimeMode, final int numRealtimeWorkerThreads){
        this.inputDir = inputDir;
        this.realtimeMode = realtimeMode;
        this.numRealtimeWorkerThreads = numRealtimeWorkerThreads;
    }

    /**
     * @return true on success; false otherwise */
    public boolean execute(){
        boolean success = true;

        /**
         * Force the controller to keep the original logging timestamps
         * of the monitoring records.
         */
        ctrlInst.setReplayMode(realtimeMode);

        IKiekerRecordConsumer logCons = new IKiekerRecordConsumer() {

            /** Anonymous consumer class that simply passes all records to the
             *  controller */
            public String[] getRecordTypeSubscriptionList() {
                return null; // consume all types
            }

            public void consumeMonitoringRecord(final AbstractKiekerMonitoringRecord monitoringRecord) {
                ctrlInst.logMonitoringRecord(monitoringRecord);
            }

            public boolean execute() {
                // do nothing, we are synchronous
                return true;
            }

            public void terminate() {
                ctrlInst.terminateMonitoring();
            }
        };
        AbstractKiekerMonitoringLogReader fsReader;
        if (realtimeMode) {
//            IKiekerRecordConsumer rtDistributorCons = new ReplayDistributor(numRealtimeWorkerThreads, logCons);
//            fsReader.addConsumer(
//                    rtDistributorCons,
//                    null); // consume records of all types
            fsReader = new FSReaderRealtime(inputDir, numRealtimeWorkerThreads);

        } else {
            fsReader = new FSReader(inputDir);
        }
        fsReader.addConsumer(logCons, null); // consume records of all types
        try {
            if (!fsReader.execute()) {
                // here, we do not start consumers since they don't do anything in execute()
                log.error("Log Replay failed");
                success = false;
            }
        } catch (LogReaderExecutionException ex) {
            log.error("LogReaderExecutioException", ex);
            success = false;
        }
        return success;
    }
}
