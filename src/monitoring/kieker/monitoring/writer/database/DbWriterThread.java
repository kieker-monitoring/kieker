package kieker.monitoring.writer.database;

import kieker.monitoring.writer.util.async.AbstractWorkerThread;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.MonitoringController;

import kieker.common.record.OperationExecutionRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 *
 * ==================LICENCE=========================
 * Copyright 2006-2008 Matthias Rohr and the Kieker Project
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

/**
 * @author Matthias Rohr
 */
public class DbWriterThread extends AbstractWorkerThread {

    private static final Log log = LogFactory.getLog(DbWriterThread.class);
    private Connection conn;
    private BlockingQueue<IMonitoringRecord> writeQueue;
    private PreparedStatement psInsertMonitoringData;
    private static boolean shutdown = false;
    private boolean finished = false;
    boolean statementChanged = true;
    String nextStatementText;

    public DbWriterThread(Connection initializedConnection, BlockingQueue<IMonitoringRecord> writeQueue, String statementtext) {
        this.conn = initializedConnection;
        this.writeQueue = writeQueue;
        this.nextStatementText = statementtext;
        log.info("New DbWriter thread created");
    }

    
    synchronized void changeStatement(String statementtext) {
        nextStatementText = statementtext;
        statementChanged = true;
    }

    /**
     * May be called more often than required... but that doesn't harm
     */
    
    public void initShutdown() {
        DbWriterThread.shutdown = true;
    }

    
    public void run() {
        log.info("Dbwriter thread running");
        try {
            while (!finished) {
                IMonitoringRecord data = writeQueue.take();
                if (data == MonitoringController.END_OF_MONITORING_MARKER){
                    log.info("Found END_OF_MONITORING_MARKER. Will terminate");
                    // need to put the marker back into the queue to notify other threads
                    writeQueue.add(MonitoringController.END_OF_MONITORING_MARKER);
                    finished = true;
                    break;
                }
                if (data != null) {
                    consume(data); // throws SQLException
                } else {
                    // timeout ... 
                    if (shutdown && writeQueue.isEmpty()) {
                        finished = true;
                    }
                }
            }
            log.info("Dbwriter finished");
        } catch (Exception ex) {
            // e.g. Interrupted Exception or SQLException
            log.error("DB Writer will halt " + ex.getMessage());
            // TODO: This is a dirty hack!
            // What we need is a listener interface!
            log.error("Will terminate monitoring!");
            MonitoringController.getInstance().terminate();
        } finally {
            this.finished = true;
        }
    }

    /**
     * writes next item into database
     */
    
    private void consume(IMonitoringRecord monitoringRecord) throws SQLException {
        //if (TpmonController.debug) System.out.println("DbWriterThread "+this+" Consuming "+monitoringRecord);
        try {
            if (statementChanged || psInsertMonitoringData == null) {
                psInsertMonitoringData = conn.prepareStatement(nextStatementText);
                statementChanged = false;
            }

            OperationExecutionRecord execData = (OperationExecutionRecord) monitoringRecord;
            psInsertMonitoringData.setString(1, execData.className + "." + execData.operationName);
            psInsertMonitoringData.setString(2, execData.sessionId);
            psInsertMonitoringData.setString(3, String.valueOf(execData.traceId));
            psInsertMonitoringData.setLong(4, execData.tin);
            psInsertMonitoringData.setLong(5, execData.tout);
            psInsertMonitoringData.setString(6, execData.hostName);
            psInsertMonitoringData.setLong(7, execData.eoi);
            psInsertMonitoringData.setLong(8, execData.ess);
            psInsertMonitoringData.execute();

        } catch (SQLException ex) {
            log.error("DbWriter Error during database statement preparation: ", ex);
            throw ex;
        }
    }

    
    public boolean isFinished() {
        return finished;
    }
}
