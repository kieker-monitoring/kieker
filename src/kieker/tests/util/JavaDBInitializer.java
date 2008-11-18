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

/**
 *
 * @author Andre van Hoorn
 */
public class JavaDBInitializer {

    public static void main(String[] args) {
        Connection dbConnection = null;
        System.setProperty("derby.system.home", "/home/voorn/svn_work/sw_kieker/trunk/tmp/");

        String strUrl = "jdbc:derby:DBNAME;user=DBUSER;password=DBPASS;create=true";

        try {
            dbConnection = DriverManager.getConnection(strUrl);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        createTables(dbConnection);
    }
    private static String strCreateAddressTable =
            "CREATE TABLE tpmondata(" +
            "`autoid` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
            "`experimentid` SMALLINT NOT NULL DEFAULT '0'," +
            "`operation` VARCHAR( 160 ) NOT NULL ," +
            "`sessionid` VARCHAR( 34 ) NOT NULL ," +
            "`traceid` VARCHAR( 34 ) NOT NULL ," +
            "`tin` BIGINT( 19 ) UNSIGNED NOT NULL ," +
            "`tout` BIGINT( 19 ) UNSIGNED NOT NULL ," +
            "`vmname` VARCHAR( 40 ) NOT NULL DEFAULT ''," +
            "`executionOrderIndex` INT( 10 ) NOT NULL DEFAULT '-1'," +
            "`executionStackSize` INT( 10 ) NOT NULL DEFAULT '-1'," +
            "INDEX (operation(16)), INDEX (traceid), INDEX (tin)" +
            ") ENGINE = MYISAM;" +
            "";

    private static boolean createTables(Connection dbConnection) {
        boolean bCreatedTables = false;
        Statement statement = null;
        try {
            statement = dbConnection.createStatement();
            statement.execute(strCreateAddressTable);
            bCreatedTables = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return bCreatedTables;
    }
}
