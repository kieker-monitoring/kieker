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

package kieker.analysis.plugin.reader.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Jan Waller
 */
@Plugin(outputPorts = @OutputPort(name = DBReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class }, description = "Output Port of the DBReader"))
public class DBReader extends AbstractReaderPlugin {

	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	public static final String CONFIG_PROPERTY_NAME_DRIVERCLASSNAME = "DriverClassname";
	public static final String CONFIG_PROPERTY_NAME_CONNECTIONSTRING = "ConnectionString";
	public static final String CONFIG_PROPERTY_NAME_TABLEPREFIX = "TablePrefix";

	private static final Log LOG = LogFactory.getLog(DBReader.class);

	private final String driverClassname;
	private final String connectionString;
	private final String tablePrefix;

	public DBReader(final Configuration configuration) throws Exception {
		super(configuration);
		this.driverClassname = configuration.getStringProperty(CONFIG_PROPERTY_NAME_DRIVERCLASSNAME);
		this.connectionString = configuration.getStringProperty(CONFIG_PROPERTY_NAME_CONNECTIONSTRING);
		this.tablePrefix = configuration.getStringProperty(CONFIG_PROPERTY_NAME_TABLEPREFIX);
		try {
			Class.forName(this.driverClassname).newInstance();
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			throw new Exception("DB driver registration failed. Perhaps the driver jar is missing?", ex);
		}
	}

	public boolean read() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(this.connectionString);
			final Statement getIndexTable = connection.createStatement();
			final ResultSet indexTable = getIndexTable.executeQuery("SELECT * from " + this.tablePrefix);
			while (indexTable.next()) {
				LOG.info("TABLE: " + indexTable.getString(1) + " CLASSNAME: " + indexTable.getString(2));
			}
			indexTable.close();
			getIndexTable.close();
		} catch (final SQLException ex) {
			LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
			return false;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (final SQLException ex) {
					LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
				}
			}
		}
		return true;
	}

	public void terminate(final boolean error) {
		LOG.info("Shutdown of DBReader requested.");
		// TODO: what to do here?
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_DRIVERCLASSNAME, this.driverClassname);
		configuration.setProperty(CONFIG_PROPERTY_NAME_CONNECTIONSTRING, this.connectionString);
		configuration.setProperty(CONFIG_PROPERTY_NAME_TABLEPREFIX, this.tablePrefix);
		return configuration;
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_DRIVERCLASSNAME, "org.apache.derby.jdbc.EmbeddedDriver");
		configuration.setProperty(CONFIG_PROPERTY_NAME_CONNECTIONSTRING, "jdbc:derby:tmp/KIEKER;user=DBUSER;password=DBPASS");
		configuration.setProperty(CONFIG_PROPERTY_NAME_TABLEPREFIX, "kieker");
		return configuration;
	}
}
