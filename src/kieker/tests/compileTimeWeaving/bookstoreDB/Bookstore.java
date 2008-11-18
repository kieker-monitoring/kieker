package kieker.tests.compileTimeWeaving.bookstoreDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import kieker.tpmon.annotations.TpmonMonitoringProbe;
import kieker.tpmon.aspects.*;
import java.util.Vector;

/**
 * kieker.tests.compileTimeWeaving.bookstore.Bookstore.java
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
 *
 * A simple test and demonstration scenario for Kieker's 
 * monitoring component tpmon. See the kieker tutorial 
 * for more information 
 * (http://www.matthias-rohr.com/kieker/tutorial.html)
 *
 * @author Matthias Rohr
 * History:
 * 2008/01/09: Refactoring for the first release of
 *             Kieker and publication under an open source licence
 * 2007-04-18: Initial version
 *
 */
public class Bookstore extends Thread {

    static int numberOfRequests = 100;
    static int interRequestTime = 5;

    /**
     *
     * main is the load driver for the Bookstore. It creates 
     * request which all request a search from the bookstore. 
     * A fixed time delay is between two request. Requests 
     * are likely to overlap, which leads to request processing
     * in more than one thread. 
     *
     * Both the number of requests and arrival rate are defined 
     * by the local variables above the method.
     * (default: 100 requests; interRequestTime 5 (millisecs))
     * 
     * This will be monitored by Tpmon, since it has the
     * TpmonMonitoringProbe() annotation.
     */
    @TpmonMonitoringProbe()
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

        Vector<Bookstore> bookstoreScenarios = new Vector<Bookstore>();

        for (int i = 0; i < numberOfRequests; i++) {
            System.out.println("Bookstore.main: Starting request " + i);
            Bookstore newBookstore = new Bookstore();
            bookstoreScenarios.add(newBookstore);
            newBookstore.start();
            Bookstore.waitabit(interRequestTime);
        }
        System.out.println("Bookstore.main: Finished with starting all requests.");
        System.out.println("Bookstore.main: Waiting 5 secs before calling system.exit");
        waitabit(5000);
        System.exit(0);
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

    public void run() {
        Bookstore.searchBook();
    }

    @TpmonMonitoringProbe()
    public static void searchBook() {
        Catalog.getBook(false);
        CRM.getOffers();
    }

    /**
     * Only encapsulates Thread.sleep()
     */
    public static void waitabit(long waittime) {
        if (waittime > 0) {
            try {
                Thread.sleep(waittime);
            } catch (Exception e) {
            }
        }
    }
} 
