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

public class Dbconnector {

    private static Connection conn = null;
    private static PreparedStatement psInsertMonitoringData;
    private static boolean init = false;
    private static int experimentId = TpmonController.getExperimentId();
    private static String vmname = TpmonController.getVmname();

    private Dbconnector() {
    }

    /**
     * Returns false if an error occurs.
     * Errors are printed to stdout, even if debug=false
     * in dbconnector.properties.
     *
     */
    public static synchronized boolean init() {
        if (!init) {
            if (TpmonController.debug) {
                System.out.println("Tpmon dbconnector init");
            }
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (Exception ex) {
                TpmonController.formatAndOutputError("MySQL driver registration failed. Perhaps the mysql-connector-....jar missing? Exception: " + ex.getMessage(), false, false);
                ex.printStackTrace();
                return false;
            }

            try {
                conn = DriverManager.getConnection(TpmonController.dbConnectionAddress);
                System.out.println("Tpmon: Connected to database at " + TpmonController.getDateString());
                init = true;

                if (TpmonController.setInitialExperimentIdBasedOnLastId) {
                    // set initial experiment id based on last id (increased by 1)
                    Statement stm = conn.createStatement();
                    ResultSet res = stm.executeQuery("SELECT max(experimentID) FROM " + TpmonController.dbTableName);
                    if (res.next()) {
                        experimentId = res.getInt(1) + 1;
                    }
                    TpmonController.setExperimentId(experimentId);
                    System.out.println("Tpmon: Setting initial experiment id based on last id (=" + (experimentId - 1) + " + 1 = " + experimentId + ")");
                }

            } catch (SQLException ex) {
                System.out.println("Tpmon: SQLException: " + ex.getMessage());
                System.out.println("Tpmon: SQLState: " + ex.getSQLState());
                System.out.println("Tpmon: VendorError: " + ex.getErrorCode());
                return false;
            }
        }
        return true;
    }

    /**
     * This method to store monitoring data into the database or
     * file system. The storage mode is configured in the file
     * dbconnector.properties.
     */
    static boolean insertMonitoringDataNow(String componentname, String methodname, String traceid, long tin, long tout,int executionOrderIndex, int executionStackSize) {
        return insertMonitoringDataNow(componentname, methodname, "nosession", traceid, tin, tout,executionOrderIndex, executionStackSize);
    }

    /**
     * This method to store monitoring data into the database or
     * file system. The storage mode is configured in the file
     * dbconnector.properties.
     */
    static synchronized boolean insertMonitoringDataNow(String componentname, String methodname, String sessionid, String traceid, long tin, long tout,int executionOrderIndex, int executionStackSize) {

        if (Dbconnector.init == false) {
            init();
            if (init == false) {
                System.out.println("Tpmon: Error: Theres something wrong with the database connection of tpmon!" +
                        "- Database Connection Could Not Be Initiated");
                return false;
            }
        }


        try {
            // INSERT INTO `newSchema` ( `experimentid` , `operation` , `traceid` , `tin` , `tout` ) VALUES ( '0', '1231', '1231', '12312', '1221233' );
            if (psInsertMonitoringData == null || experimentId != TpmonController.getExperimentId() || !vmname.equals(TpmonController.getVmname())) { // Vmname and ExperimentId may be changed
                experimentId = TpmonController.getExperimentId();
                vmname = TpmonController.getVmname();
                psInsertMonitoringData = conn.prepareStatement("INSERT INTO " + TpmonController.dbTableName +
                        " (experimentid,operation,sessionid,traceid,tin,tout,vmname,executionOrderIndex,executionStackSize)" +
                        "VALUES (" + experimentId + ",?,?,?,?,?," + vmname + ",?,?)");
            }

            psInsertMonitoringData.setString(1, componentname + methodname);
            psInsertMonitoringData.setString(2, sessionid);
            psInsertMonitoringData.setString(3, traceid);
            psInsertMonitoringData.setLong(4, tin);
            psInsertMonitoringData.setLong(5, tout);
            psInsertMonitoringData.setLong(6, executionOrderIndex);
            psInsertMonitoringData.setLong(7, executionStackSize);
            psInsertMonitoringData.execute();
        } catch (SQLException ex) {
            System.out.println("Tpmon Error: " + System.currentTimeMillis() + " insertMonitoringData() failed: SQLException: " + ex.getMessage());
            return false;
        } finally {
            try {
                psInsertMonitoringData.clearParameters();
            } catch (Exception ex) {
            }
        }
        return true;
    }
}
