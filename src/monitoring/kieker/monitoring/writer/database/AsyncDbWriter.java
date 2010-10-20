package kieker.monitoring.writer.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFileConstants;
import kieker.monitoring.writer.IMonitoringLogWriter;
import kieker.monitoring.writer.util.async.AbstractWorkerThread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 *
 * ==================LICENCE=========================
 * Copyright 2006-2010 the Kieker Project
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
public final class AsyncDbWriter implements IMonitoringLogWriter {

    private final static String defaultConstructionErrorMsg =
            "Do not select this writer using the full-qualified classname. "
            + "Use the the constant " + ConfigurationFileConstants.WRITER_ASYNCDB
            + " and the file system specific configuration properties.";

    public AsyncDbWriter() {
        throw new UnsupportedOperationException(AsyncDbWriter.defaultConstructionErrorMsg);
    }

    @Override
	public boolean init(final String initString) {
        throw new UnsupportedOperationException(AsyncDbWriter.defaultConstructionErrorMsg);
    }
    private static final Log log = LogFactory.getLog(AsyncDbWriter.class);
    private Connection conn = null;
    private BlockingQueue<IMonitoringRecord> blockingQueue;
    private String dbDriverClassname = "com.mysql.jdbc.Driver";
    private String dbConnectionAddress = "jdbc:mysql://jupiter.informatik.uni-oldenburg.de/0610turbomon?user=root&password=xxxxxx";
    private String dbTableName = "turbomon10";
    private boolean setInitialExperimentIdBasedOnLastId = false;
    // only used if setInitialExperimentIdBasedOnLastId==true
    private int experimentId = -1;
    private final int asyncRecordQueueSize; // = 8000;
    private final boolean blockOnFullQueue;

    public AsyncDbWriter(final String dbDriverClassname, final String dbConnectionAddress, final String dbTableName,
            final boolean setInitialExperimentIdBasedOnLastId, final int asyncRecordQueueSize, final boolean blockOnFullQueue) {
        this.dbDriverClassname = dbDriverClassname;
        this.dbConnectionAddress = dbConnectionAddress;
        this.dbTableName = dbTableName;
        this.setInitialExperimentIdBasedOnLastId = setInitialExperimentIdBasedOnLastId;
        this.asyncRecordQueueSize = asyncRecordQueueSize;
        this.blockOnFullQueue = blockOnFullQueue;
        this.init();
    }
    private final Vector<AbstractWorkerThread> workers = new Vector<AbstractWorkerThread>();

    @Override
	public Vector<AbstractWorkerThread> getWorkers() {
        return this.workers;
    }

    /**
     * Returns false if an error occurs. Errors are printed to stdout (e.g., App-server logfiles), even if debug = false.
     */
    public boolean init() {
        AsyncDbWriter.log.info("Tpmon asyncDbconnector init");
        try {
            if ((this.dbDriverClassname != null) && (this.dbDriverClassname.length() != 0)) {
                // NOTE: It's absolutely ok to have no class loaded at this point!
                //       For example Java 6 and higher have an embedded DB driver
                Class.forName(this.dbDriverClassname).newInstance();
            }
        } catch (final Exception ex) {
            AsyncDbWriter.log.error("DB driver registration failed. Perhaps the driver jar missing? Exception: ", ex);
            return false;
        }
        try {
            this.conn = DriverManager.getConnection(this.dbConnectionAddress);
            final int numberOfConnections = 4;
            this.blockingQueue = new ArrayBlockingQueue<IMonitoringRecord>(this.asyncRecordQueueSize);

//                DbWriterThread dbw = new DbWriterThread(DriverManager.getConnection(TpmonController.dbConnectionAddress),blockingQueue);
//                 new Thread(dbw).start();
            if (this.setInitialExperimentIdBasedOnLastId) {
                // set initial experiment id based on last id (increased by 1)
                final Statement stm = this.conn.createStatement();     // TODO: FindBugs says this method may fail to close the database resource
                final ResultSet res = stm.executeQuery("SELECT max(experimentID) FROM " + this.dbTableName);
                if (res.next()) {
                    this.experimentId = res.getInt(1) + 1;
                }
                AsyncDbWriter.log.info(" set initial experiment id based on last id (=" + (this.experimentId - 1) + " + 1 = " + this.experimentId + ")");
            }

            final String preparedQuery = "INSERT INTO " + this.dbTableName
                    + " (experimentid,operation,sessionid,traceid,tin,tout,vmname,executionOrderIndex,executionStackSize)"
                    + "VALUES (" + this.experimentId + ",?,?,?,?,?,?,?,?)";
            for (int i = 0; i < numberOfConnections; i++) {
                final DbWriterThread dbw = new DbWriterThread(DriverManager.getConnection(this.dbConnectionAddress), this.blockingQueue, preparedQuery);
                dbw.setDaemon(true); //might lead to inconsistent data due to harsh shutdown
                dbw.start();
                //TODO: Fix this (there shouldn't be a dependency to the TpmonCtrl)
                //TpmonController.getInstance().registerWorker(dbw);
            }
            AsyncDbWriter.log.info("Tpmon (" + numberOfConnections + " threads) connected to database");
        } catch (final SQLException ex) {
            AsyncDbWriter.log.error("SQLException: " + ex.getMessage());
            AsyncDbWriter.log.error("SQLState: " + ex.getSQLState());
            AsyncDbWriter.log.error("VendorError: " + ex.getErrorCode());
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
    @Override
	public boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
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
            if (this.blockOnFullQueue) {
                this.blockingQueue.offer(monitoringRecord); // blocks when queue full
            } else {
                this.blockingQueue.add(monitoringRecord); // tries to add immediately!
            }
            //System.out.println("Queue is "+blockingQueue.size());

        } catch (final Exception ex) {
            AsyncDbWriter.log.error("" + System.currentTimeMillis() + " insertMonitoringData() failed: SQLException: ", ex);
            return false;
        }
        return true;
    }

    @Override
	public String getInfoString() {
        final StringBuilder strB = new StringBuilder();

        //only show the password if debug is on
        String dbConnectionAddress2 = this.dbConnectionAddress;
        if (this.dbConnectionAddress.toLowerCase().contains("password")) {
            final int posPassw = this.dbConnectionAddress.toLowerCase().lastIndexOf("password");
            dbConnectionAddress2 =
                    this.dbConnectionAddress.substring(0, posPassw) + "-PASSWORD-HIDDEN";
        }
        strB.append("dbDriverClassname :" + this.dbDriverClassname);
        strB.append(", dbConnectionAddress : " + dbConnectionAddress2);
        strB.append(", dbTableName : " + this.dbTableName);
        strB.append(", setInitialExperimentIdBasedOnLastId : " + this.setInitialExperimentIdBasedOnLastId);
        strB.append(", asyncRecordQueueSize :" + this.asyncRecordQueueSize);
        strB.append(", blockOnFullQueue :" + this.blockOnFullQueue);

        return strB.toString();
    }
}
