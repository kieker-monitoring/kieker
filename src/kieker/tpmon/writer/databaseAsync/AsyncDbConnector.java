package kieker.tpmon.writer.databaseAsync;

import java.sql.*;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import kieker.tpmon.writer.AbstractKiekerMonitoringLogWriter;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.tpmon.core.TpmonController;
import kieker.tpmon.writer.util.async.AbstractWorkerThread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.tpmon.asyncDbconnector.AsyncDbconnector
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
 * 
 * Stores monitoring data into a database.
 *
 * Warning !
 * This class is an academic prototype and not intended
 * for reliability or availability critical systems.
 *
 * The insertMonitoringData methods are thread-save (also in combination with experimentId changes),
 * so that they may be used by multiple threads at the same time. We have tested this in various
 * applications, in combination with the standard mysql-Jconnector database driver.
 *
 * Our experience shows that it is not a major bottleneck if not too many
 * measurement points are used (e.g., 30/second). However, there are much
 * performance tuning possible in this class. For instance, performance
 * optimization should be possible by using a connection pool
 * instead of a single database connection. The current version uses
 * prepared statements. Alternatively, it could be tuned by collecting
 * multiple database commands before sending it to the database.
 *
 * @author Matthias Rohr
 *
 * History (Build) (change the String BUILD when this file is changed):
 * 2008/05/29: Changed vmid to vmname (defaults to hostname), 
 *             which may be changed during runtime
 * 2007/07/30: Initial Prototype
 */
public final class AsyncDbConnector extends AbstractKiekerMonitoringLogWriter {

    private final static String defaultConstructionErrorMsg =
            "Do not select this writer using the full-qualified classname. " +
            "Use the the constant " + TpmonController.WRITER_ASYNCDB +
            " and the file system specific configuration properties.";

    public AsyncDbConnector() {
        throw new UnsupportedOperationException(defaultConstructionErrorMsg);
    }

    
    public boolean init(String initString) {
        throw new UnsupportedOperationException(defaultConstructionErrorMsg);
    }
    
    private static final Log log = LogFactory.getLog(AsyncDbConnector.class);
    private Connection conn = null;
    private BlockingQueue<AbstractMonitoringRecord> blockingQueue;
    private String dbDriverClassname = "com.mysql.jdbc.Driver";
    private String dbConnectionAddress = "jdbc:mysql://jupiter.informatik.uni-oldenburg.de/0610turbomon?user=root&password=xxxxxx";
    private String dbTableName = "turbomon10";
    private boolean setInitialExperimentIdBasedOnLastId = false;
    // only used if setInitialExperimentIdBasedOnLastId==true
    private int experimentId = -1;
    private int asyncRecordQueueSize = 8000;

    public AsyncDbConnector(String dbDriverClassname, String dbConnectionAddress, String dbTableName,
            boolean setInitialExperimentIdBasedOnLastId, int asyncRecordQueueSize) {
        this.dbDriverClassname = dbDriverClassname;
        this.dbConnectionAddress = dbConnectionAddress;
        this.dbTableName = dbTableName;
        this.setInitialExperimentIdBasedOnLastId = setInitialExperimentIdBasedOnLastId;
        this.asyncRecordQueueSize = asyncRecordQueueSize;
        this.init();
    }
    private Vector<AbstractWorkerThread> workers = new Vector<AbstractWorkerThread>();

    
    public Vector<AbstractWorkerThread> getWorkers() {
        return workers;
    }

    /**
     * Returns false if an error occurs. Errors are printed to stdout (e.g., App-server logfiles), even if debug = false.
     */
    
    public boolean init() {
        if (this.isDebug()) {
            log.info("Tpmon asyncDbconnector init");
        }
        try {
            if (this.dbDriverClassname != null && this.dbDriverClassname.length() != 0) {
                // NOTE: It's absolutely ok to have no class loaded at this point!
                //       For example Java 6 and higher have an embedded DB driver
                Class.forName(this.dbDriverClassname).newInstance();
            }
        } catch (Exception ex) {
            log.error("DB driver registration failed. Perhaps the driver jar missing? Exception: ", ex);
            return false;
        }
        try {
            conn = DriverManager.getConnection(this.dbConnectionAddress);
            int numberOfConnections = 4;
            blockingQueue = new ArrayBlockingQueue<AbstractMonitoringRecord>(asyncRecordQueueSize);

//                DbWriterThread dbw = new DbWriterThread(DriverManager.getConnection(TpmonController.dbConnectionAddress),blockingQueue);
//                 new Thread(dbw).start();  
            if (this.setInitialExperimentIdBasedOnLastId) {
                // set initial experiment id based on last id (increased by 1)
                Statement stm = conn.createStatement();     // TODO: FindBugs says this method may fail to close the database resource
                ResultSet res = stm.executeQuery("SELECT max(experimentID) FROM " + this.dbTableName);
                if (res.next()) {
                    this.experimentId = res.getInt(1) + 1;
                }
                log.info(" set initial experiment id based on last id (=" + (experimentId - 1) + " + 1 = " + experimentId + ")");
            }

            String preparedQuery = "INSERT INTO " + this.dbTableName +
                    " (experimentid,operation,sessionid,traceid,tin,tout,vmname,executionOrderIndex,executionStackSize)" +
                    "VALUES (" + experimentId + ",?,?,?,?,?,?,?,?)";
            for (int i = 0; i < numberOfConnections; i++) {
                DbWriterThread dbw = new DbWriterThread(DriverManager.getConnection(this.dbConnectionAddress), blockingQueue, preparedQuery);
                dbw.setDaemon(true); //might lead to inconsistent data due to harsh shutdown
                dbw.start();
                //TODO: Fix this (there shouldn't be a dependency to the TpmonCtrl)
                //TpmonController.getInstance().registerWorker(dbw);
            }
            log.info("Tpmon (" + numberOfConnections + " threads) connected to database");
        } catch (SQLException ex) {
            log.error("SQLException: " + ex.getMessage());
            log.error("SQLState: " + ex.getSQLState());
            log.error("VendorError: " + ex.getErrorCode());
            // TODO: This is a dirty hack!
            // What we need is a listener interface!
            //log.error("Will disable monitoring!");
            return false;
        }
        return true;
    }

//    /**
//     * TODO: Is this method ever used??
//     * Use this method to insert data into the database.
//     */
//    
//    public boolean insertMonitoringDataNow(int experimentId, String vmName, String opname, String traceid, long tin, long tout, int executionOrderIndex, int executionStackSize) {
//        return this.insertMonitoringDataNow(experimentId, vmName, opname, "nosession", traceid, tin, tout, executionOrderIndex, executionStackSize);
//    }

    /**
     * This method is not synchronized, in contrast to the insert method of the Dbconnector.java.
     * It uses several dbconnections in parallel using the consumer, producer pattern.
     */
    
    public boolean writeMonitoringRecord(AbstractMonitoringRecord monitoringRecord) {
        if (this.isDebug()) {
            log.debug("Async.insertMonitoringDataNow");
        }
        try {
            // INSERT INTO `newSchema` ( `experimentid` , `operation` , `traceid` , `tin` , `tout` ) VALUES ( '0', '1231', '1231', '12312', '1221233' );
            /*
             * BY ANDRE: I disabled this for the moment since we don't seem to use the db anyhow
             * 
            if (experimentId != TpmonController.getExperimentId() || !vmname.equals(TpmonController.getVmname())) { // ExperimentId and vmname may be changed
            experimentId = TpmonController.getExperimentId();
            vmname = TpmonController.getVmname();
            String preparedQuery = "INSERT INTO " + TpmonController.dbTableName +
            " (experimentid,operation,sessionid,traceid,tin,tout,vmname,executionOrderIndex,executionStackSize)" +
            "VALUES (" + experimentId + ",?,?,?,?,?," + vmname + ",?,?)";
            for (DbWriterThread wr : workers) {
            wr.changeStatement(preparedQuery);
            }
            }*/

            blockingQueue.add(monitoringRecord); // tries to add immediately!
            //System.out.println("Queue is "+blockingQueue.size());

        } catch (Exception ex) {
            log.error("" + System.currentTimeMillis() + " insertMonitoringData() failed: SQLException: ", ex);
            return false;
        }
        return true;
    }

    
    public String getInfoString() {
        StringBuilder strB = new StringBuilder();

        //only show the password if debug is on
        String dbConnectionAddress2 = dbConnectionAddress;
        if (!this.isDebug()) {
            if (dbConnectionAddress.toLowerCase().contains("password")) {
                int posPassw = dbConnectionAddress.toLowerCase().lastIndexOf("password");
                dbConnectionAddress2 =
                        dbConnectionAddress.substring(0, posPassw) + "-PASSWORD-HIDDEN";
            }
        }
        strB.append("dbDriverClassname :" + dbDriverClassname);
        strB.append(", dbConnectionAddress : " + dbConnectionAddress2);
        strB.append(", dbTableName : " + dbTableName);
        strB.append(", setInitialExperimentIdBasedOnLastId : " + setInitialExperimentIdBasedOnLastId);

        return strB.toString();
    }

    
    public void registerMonitoringRecordType(int id, String className) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setWriteRecordTypeIds(final boolean writeRecordTypeIds) {
        super.setWriteRecordTypeIds(writeRecordTypeIds);
        for (AbstractWorkerThread t : workers) {
            t.setWriteRecordTypeIds(writeRecordTypeIds);
        }
    }
}
