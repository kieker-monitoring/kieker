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

import kieker.common.record.IMonitoringRecord;
import kieker.tpan.reader.AbstractMonitoringLogReader;
import kieker.tpan.consumer.IMonitoringRecordConsumer;
import kieker.tpan.reader.LogReaderExecutionException;
import kieker.tpan.reader.filesystem.FSReader;
import kieker.tpmon.core.TpmonController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class FilesystemLogReplayer {
    private static final Log log = LogFactory.getLog(FilesystemLogReplayer.class);
    private static final TpmonController ctrlInst = TpmonController.getInstance();
    private String[] inputDirs = null;
    private boolean realtimeMode = false;
    private boolean keepOriginalLoggingTimestamps = true;
    private int numRealtimeWorkerThreads = -1;

    private FilesystemLogReplayer() {}

    /** Normal replay mode (i.e., non-real-time). */
    public FilesystemLogReplayer(final String[] inputDirs){
        this.inputDirs = inputDirs;
    }

    public FilesystemLogReplayer(final String[] inputDirs, final boolean keepOriginalLoggingTimestamps, final boolean realtimeMode, final int numRealtimeWorkerThreads){
        this.inputDirs = inputDirs;
        this.realtimeMode = realtimeMode;
        this.numRealtimeWorkerThreads = numRealtimeWorkerThreads;
        this.keepOriginalLoggingTimestamps = keepOriginalLoggingTimestamps;
    }

    /**
     * @return true on success; false otherwise */
    public boolean execute(){
        boolean success = true;

        /**
         * Force the controller to keep the original logging timestamps
         * of the monitoring records.
         */
        ctrlInst.setReplayMode(this.keepOriginalLoggingTimestamps);

        IMonitoringRecordConsumer logCons = new IMonitoringRecordConsumer() {

            /** Anonymous consumer class that simply passes all records to the
             *  controller */
            public String[] getRecordTypeSubscriptionList() {
                return null; // consume all types
            }

            public void consumeMonitoringRecord(final IMonitoringRecord monitoringRecord) {
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
        AbstractMonitoringLogReader fsReader;
        if (realtimeMode) {
          fsReader = new FSReaderRealtime(inputDirs, numRealtimeWorkerThreads);

        } else {
                fsReader = new FSReader(inputDirs);
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
