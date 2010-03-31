/**
 * kieker.tests.util.JavaDBInitializer
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
package kieker.tests.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class JavaDBInitializer {
    private static final Log log = LogFactory.getLog(JavaDBInitializer.class);
    
    private static String dbDriverClassname = "";
    private static String dbConnectionAddress = "jdbc:derby:tmp/KIEKER;user=DBUSER;password=DBPASS";
    private static String dbTableName = "APP.tpmondata";
    
    // TODO: needs to be read from file
    private static String strCreateAddressTable =
            "CREATE table APP.tpmondata (" +
            "autoid      INTEGER NOT NULL " +
            "   PRIMARY KEY GENERATED ALWAYS AS IDENTITY " +
            "   (START WITH 0, INCREMENT BY 1)," +
            "experimentid SMALLINT NOT NULL DEFAULT 0," +
            "operation VARCHAR(160) NOT NULL," +
            "sessionid VARCHAR(34)," +
            "traceid VARCHAR(34) NOT NULL," +
            "tin BIGINT NOT NULL," +
            "tout BIGINT NOT NULL," +
            "vmname VARCHAR(40) NOT NULL DEFAULT ''," +
            "executionOrderIndex SMALLINT NOT NULL DEFAULT -1," +
            "executionStackSize SMALLINT NOT NULL DEFAULT -1" +
//            "INDEX (operation(16)), INDEX (traceid), INDEX (tin)" +
            ")";

    public static void main(String[] args) {
        if (!readProperties()){
            log.error("readProperties returned false.");
            System.exit(1);
        }
        
        Connection dbConnection = null;
        try {
            dbConnection = DriverManager.getConnection(dbConnectionAddress+";create=true");
            createTables(dbConnection);
            dbConnection.close();            
        } catch (SQLException ex) {
            log.error(ex);
            System.exit(1);
        }
        log.info(JavaDBInitializer.class.getName()+".main(..) done");
    }

    private static boolean readProperties(){
       dbConnectionAddress = System.getProperty("tpmon.dbConnectionAddress");
       dbDriverClassname = System.getProperty("tpmon.dbDriverClassname");
       dbTableName = System.getProperty("tpmon.dbTableName");
       return dbConnectionAddress != null && dbDriverClassname != null 
               && dbTableName != null;
    }
    
    private static boolean createTables(Connection dbConnection) {
        boolean bCreatedTables = false;
        Statement statement = null;
        try {
            statement = dbConnection.createStatement();
            statement.execute(strCreateAddressTable);
            bCreatedTables = true;
            
            // TODO: remove:
//            statement = dbConnection.createStatement();
//            statement.execute("INSERT INTO APP.tpmondata (experimentid) VALUES (5)");
//            statement.execute("INSERT INTO APP.tpmondata (experimentid) VALUES (7)");
//            
//                statement = dbConnection.createStatement();     // TODO: FindBugs says this method may fail to close the database resource
//                ResultSet res = statement.executeQuery("SELECT max(experimentid) FROM APP.tpmondata");
//                if (res.next()) {
//                    System.out.println(res.getInt(1));
//                }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return bCreatedTables;
    }
}
