/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
 ***************************************************************************/

package kieker.test.monitoring.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * @author Andre van Hoorn
 */
public final class JavaDBInitializer {
	private static final Log LOG = LogFactory.getLog(JavaDBInitializer.class);

	private static final String dbDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String dbConnectionAddress = "jdbc:derby:tmp/KIEKER;user=DBUSER;password=DBPASS";
	private static final String dbTableName = "APP.kiekerdata";

	// TODO: needs to be read from file
	// http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/158
	private static final String STR_CREATE_ADDRESS_TABLE = "CREATE table " + JavaDBInitializer.dbTableName + "(autoid INTEGER NOT NULL "
			+ "   PRIMARY KEY GENERATED ALWAYS AS IDENTITY " + "   (START WITH 0, INCREMENT BY 1)," + "experimentid SMALLINT NOT NULL DEFAULT 0,"
			+ "operation VARCHAR(160) NOT NULL," + "sessionid VARCHAR(34)," + "traceid BIGINT NOT NULL," + "tin BIGINT NOT NULL," + "tout BIGINT NOT NULL,"
			+ "vmname VARCHAR(40) NOT NULL DEFAULT ''," + "executionOrderIndex SMALLINT NOT NULL DEFAULT -1," + "executionStackSize SMALLINT NOT NULL DEFAULT -1"
			+ ")";

	private JavaDBInitializer() {}

	public static void main(final String[] args) {
		try {
			Class.forName(JavaDBInitializer.dbDriverClassname).newInstance();
		} catch (final Exception ex) { // NOPMD NOCS (Exception)
			ex.printStackTrace();
		}
		Connection dbConnection = null;
		try {
			dbConnection = DriverManager.getConnection(JavaDBInitializer.dbConnectionAddress + ";create=true");
			JavaDBInitializer.createTables(dbConnection);
		} catch (final SQLException ex) {
			JavaDBInitializer.LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
			System.exit(1);
		} finally {
			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (final SQLException ex) {
					JavaDBInitializer.LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
					System.exit(1);
				}
			}
		}
		JavaDBInitializer.LOG.info(JavaDBInitializer.class.getName() + ".main(..) done");
	}

	private static boolean createTables(final Connection dbConnection) {
		boolean bCreatedTables = false;
		Statement statement = null;
		try {
			statement = dbConnection.createStatement();
			statement.execute(JavaDBInitializer.STR_CREATE_ADDRESS_TABLE);
			bCreatedTables = true;
		} catch (final SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return bCreatedTables;
	}
}
