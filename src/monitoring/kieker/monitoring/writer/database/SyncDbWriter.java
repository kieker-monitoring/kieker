package kieker.monitoring.writer.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.configuration.ConfigurationFileConstants;
import kieker.monitoring.writer.IMonitoringLogWriter;
import kieker.monitoring.writer.util.async.AbstractWorkerThread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 *
 * ==================LICENCE=========================
 * Copyright 2009 Kieker Project
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
 * Warning! This class is an academic prototype and not intended
 * for usage in any critical system.
 *
 * The insertMonitoringData methods should be thread-save
 * (also in combination with experimentId changes), so that they
 * may be used by multiple threads at the same time.
 *
 * We have tested this in various applications, in combination with
 * the standard MySQL Connector/J database driver.
 *
 * We experienced that the monitoring it is not a major bottleneck
 * if not too many measurement points are used (e.g., 30/second).
 * However, there is additional performance tuning possible. For instance,
 * by collecting multiple database commands before sending
 * it to the database, or by implementing "statistical profiling", which
 * stores only samples instead of all executions.
 *
 * @author Matthias Rohr, Andre van Hoorn
 *
 * History:
 * 2008/05/29: Changed vmid to vmname (defaults to hostname), which may be changed during runtime
 * 2008/01/04: Refactoring for the first release of
 *             Kieker and publication under an open source licence
 * 2007/03/13: Refactoring
 * 2006/12/20: Initial Prototype
 *
 */
public final class SyncDbWriter implements IMonitoringLogWriter {

    private static final Log log = LogFactory.getLog(SyncDbWriter.class);
    private Connection conn = null;
    private PreparedStatement psInsertMonitoringData;
    private String dbDriverClassname;
    private String dbConnectionAddress;
    private String dbTableName;
    private boolean setInitialExperimentIdBasedOnLastId = false;
    // only used if setInitialExperimentIdBasedOnLastId==true
    private int experimentId = -1;
    private final static String defaultConstructionErrorMsg =
            "Do not select this writer using the fully qualified classname. "
            + "Use the the constant " + ConfigurationFileConstants.WRITER_SYNCDB
            + " and the file system specific configuration properties.";

    public SyncDbWriter() {
        throw new UnsupportedOperationException(SyncDbWriter.defaultConstructionErrorMsg);
    }

    @Override
	public boolean init(final String initString) {
        throw new UnsupportedOperationException(SyncDbWriter.defaultConstructionErrorMsg);
    }

    public SyncDbWriter(final String dbDriverClassname, final String dbConnectionAddress, final String dbTableName,
            final boolean setInitialExperimentIdBasedOnLastId) {
        this.dbDriverClassname = dbDriverClassname;
        this.dbConnectionAddress = dbConnectionAddress;
        this.dbTableName = dbTableName;
        this.setInitialExperimentIdBasedOnLastId = setInitialExperimentIdBasedOnLastId;
        this.init();
    }

    /**
     * Returns false if an error occurs.
     */
    private boolean init() {
        SyncDbWriter.log.debug("Tpmon dbconnector init");
        try {
            if ((this.dbDriverClassname != null) && (this.dbDriverClassname.length() != 0)) {
                // NOTE: It's absolutely ok to have no class loaded at this point!
                //       For example Java 6 and higher have an embedded DB driver
                Class.forName(this.dbDriverClassname).newInstance();
            }
        } catch (final Exception ex) {
            SyncDbWriter.log.error("DB driver registration failed. Perhaps the driver jar missing?", ex);
            return false;
        }

        try {
            this.conn = DriverManager.getConnection(this.dbConnectionAddress);
            SyncDbWriter.log.info("Tpmon: Connected to database");

            if (this.setInitialExperimentIdBasedOnLastId) {
                // set initial experiment id based on last id (increased by 1)
                SyncDbWriter.log.info("Tpmon: Setting initial experiment id based on last id (=" + (this.experimentId - 1) + " + 1 = " + this.experimentId + ")");
                final Statement stm = this.conn.createStatement();     // TODO: FindBugs says this method may fail to close the database resource
                final ResultSet res = stm.executeQuery("SELECT max(experimentid) FROM " + this.dbTableName);
                if (res.next()) {
                    this.experimentId = res.getInt(1) + 1;
                }
                // this.experimentId keeps the old value else
            }

            this.psInsertMonitoringData = this.conn.prepareStatement("INSERT INTO " + this.dbTableName
                    + " (experimentid,operation,sessionid,traceid,tin,tout,vmname,executionOrderIndex,executionStackSize)"
                    + "VALUES (?,?,?,?,?,?,?,?,?)");
        } catch (final SQLException ex) {
            SyncDbWriter.log.error("Tpmon: SQLException: " + ex.getMessage());
            SyncDbWriter.log.error("Tpmon: SQLState: " + ex.getSQLState());
            SyncDbWriter.log.error("Tpmon: VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }

    /**
     * This method is used to store monitoring data into the database or
     * file system. The storage mode is configured in the file
     * dbconnector.properties.
     */
    @Override
	public synchronized boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
        try {
            // connector only supports execution records so far
            final OperationExecutionRecord execRecord = (OperationExecutionRecord) monitoringRecord;

            this.psInsertMonitoringData.setInt(1,
                    (this.setInitialExperimentIdBasedOnLastId && (this.experimentId >= 0)) ? this.experimentId : execRecord.experimentId);
            this.psInsertMonitoringData.setString(2, execRecord.className + "." + execRecord.operationName);
            this.psInsertMonitoringData.setString(3, execRecord.sessionId);
            this.psInsertMonitoringData.setString(4, String.valueOf(execRecord.traceId));
            this.psInsertMonitoringData.setLong(5, execRecord.tin);
            this.psInsertMonitoringData.setLong(6, execRecord.tout);
            this.psInsertMonitoringData.setString(7, execRecord.hostName);
            this.psInsertMonitoringData.setLong(8, execRecord.eoi);
            this.psInsertMonitoringData.setLong(9, execRecord.ess);
            this.psInsertMonitoringData.execute();
        } catch (final Exception ex) {
            SyncDbWriter.log.error("Tpmon Error: " + System.currentTimeMillis() + " insertMonitoringData() failed:" + ex.getMessage());
            return false;
        } finally {
            try {
                this.psInsertMonitoringData.clearParameters();
            } catch (final Exception ex) {
                SyncDbWriter.log.error(ex);
                return false;
            }
        }
        return true;
    }

    @Override
	public Vector<AbstractWorkerThread> getWorkers() {
        return null;
    }

    @Override
	public String getInfoString() {
        final StringBuilder strB = new StringBuilder();

        //only show the password if debug is on
        String dbConnectionAddress2 = this.dbConnectionAddress;
        if (this.dbConnectionAddress.toLowerCase().contains("password")) {
            final int posPassw = this.dbConnectionAddress.toLowerCase().lastIndexOf("password");
            dbConnectionAddress2 = this.dbConnectionAddress.substring(0, posPassw) + "-PASSWORD-HIDDEN";
        }
        strB.append("dbDriverClassname :" + this.dbDriverClassname);
        strB.append(", dbConnectionAddress : " + dbConnectionAddress2);
        strB.append(", dbTableName : " + this.dbTableName);
        strB.append(", setInitialExperimentIdBasedOnLastId : " + this.setInitialExperimentIdBasedOnLastId);
        if (this.setInitialExperimentIdBasedOnLastId && (this.experimentId >= 0)) {
            strB.append(", ACTUAL EXPERIMENT_ID : " + this.experimentId);
        }
        return strB.toString();
    }
}
