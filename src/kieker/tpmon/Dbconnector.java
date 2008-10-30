package kieker.tpmon;

/**
 * kieker.tpmon.Dbconnector.java
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
import java.sql.*;

import java.util.Vector;
import kieker.tpmon.annotations.TpmonInternal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Dbconnector extends AbstractMonitoringDataWriter {

    private static final Log log = LogFactory.getLog(Dbconnector.class);
    private Connection conn = null;
    private PreparedStatement psInsertMonitoringData;
    private String dbConnectionAddress = "jdbc:mysql://jupiter.informatik.uni-oldenburg.de/0610turbomon?user=root&password=xxxxxx";
    private String dbTableName = "turbomon10";
    private boolean setInitialExperimentIdBasedOnLastId = false;
    // only used if setInitialExperimentIdBasedOnLastId==true
    private int experimentId = -1;

   private final static String defaultConstructionErrorMsg = 
            "Do not select this writer using the full-qualified classname. " +
            "Use the the constant " + TpmonController.WRITER_SYNCDB +
                " and the file system specific configuration properties.";
    
   public Dbconnector() {
        throw new UnsupportedOperationException(defaultConstructionErrorMsg);
   }
   
    @TpmonInternal
   public boolean init(String initString) {
        throw new UnsupportedOperationException(defaultConstructionErrorMsg);
   }
    
    public Dbconnector(String dbConnectionAddress, String dbTableName,
            boolean setInitialExperimentIdBasedOnLastId) {
        this.dbConnectionAddress = dbConnectionAddress;
        this.dbTableName = dbTableName;
        this.setInitialExperimentIdBasedOnLastId = setInitialExperimentIdBasedOnLastId;
        this.init();
    }

    /**
     * Returns false if an error occurs.
     * Errors are printed to stdout, even if debug=false
     * in dbconnector.properties.
     *
     */
    @TpmonInternal
    private boolean init() {
        if (this.isDebug()) {
            log.debug("Tpmon dbconnector init");
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            log.error("MySQL driver registration failed. Perhaps the mysql-connector-....jar missing?", ex);
            ex.printStackTrace();
            return false;
        }

        try {
            conn = DriverManager.getConnection(this.dbConnectionAddress);
            log.info("Tpmon: Connected to database");

            if (this.setInitialExperimentIdBasedOnLastId) {
                // set initial experiment id based on last id (increased by 1)
                Statement stm = conn.createStatement();
                ResultSet res = stm.executeQuery("SELECT max(experimentID) FROM " + this.dbTableName);
                if (res.next()) {
                    this.experimentId = res.getInt(1) + 1;
                }
                // this.experimentId keeps the old value else
                log.info("Tpmon: Setting initial experiment id based on last id (=" + (experimentId - 1) + " + 1 = " + experimentId + ")");
            }

            psInsertMonitoringData = conn.prepareStatement("INSERT INTO " + dbTableName +
                    " (experimentid,operation,sessionid,traceid,tin,tout,vmname,executionOrderIndex,executionStackSize)" +
                    "VALUES (?,?,?,?,?,?,?,?,?)");
        } catch (SQLException ex) {
            log.error("Tpmon: SQLException: " + ex.getMessage());
            log.error("Tpmon: SQLState: " + ex.getSQLState());
            log.error("Tpmon: VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }

    /**
     * This method to store monitoring data into the database or
     * file system. The storage mode is configured in the file
     * dbconnector.properties.
     */
    @TpmonInternal
    public synchronized boolean insertMonitoringDataNow(KiekerExecutionRecord execData) {
        try {
            psInsertMonitoringData.setInt(1,
                    (this.setInitialExperimentIdBasedOnLastId && this.experimentId >= 0) ? this.experimentId : experimentId);
            psInsertMonitoringData.setString(2, execData.componentName + "." + execData.opname);
            psInsertMonitoringData.setString(3, execData.sessionId);
            psInsertMonitoringData.setString(4, String.valueOf(execData.traceId));
            psInsertMonitoringData.setLong(5, execData.tin);
            psInsertMonitoringData.setLong(6, execData.tout);
            psInsertMonitoringData.setString(7, execData.vmName);
            psInsertMonitoringData.setLong(8, execData.eoi);
            psInsertMonitoringData.setLong(9, execData.ess);
            psInsertMonitoringData.execute();
        } catch (Exception ex) {
            log.error("Tpmon Error: " + System.currentTimeMillis() + " insertMonitoringData() failed:" + ex.getMessage());
            return false;
        } finally {
            try {
                psInsertMonitoringData.clearParameters();
            } catch (Exception ex) {
                log.error(ex);
                return false;
            }
        }
        return true;
    }

    @TpmonInternal
    public Vector<Worker> getWorkers() {
        return null;
    }
}
