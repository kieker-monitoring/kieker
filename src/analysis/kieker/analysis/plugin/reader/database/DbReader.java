/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

/**
 * A very simple database reader that probably only works for small data sets.
 * 
 * @author Jan Waller
 */
@Plugin(description = "A reader which reads records from a database",
		outputPorts = {
			@OutputPort(name = DbReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class }, description = "Output Port of the DBReader")
		},
		configuration = {
			@Property(name = DbReader.CONFIG_PROPERTY_NAME_DRIVERCLASSNAME, defaultValue = "org.apache.derby.jdbc.EmbeddedDriver"),
			@Property(name = DbReader.CONFIG_PROPERTY_NAME_CONNECTIONSTRING, defaultValue = "jdbc:derby:tmp/KIEKER;user=DBUSER;password=DBPASS"),
			@Property(name = DbReader.CONFIG_PROPERTY_NAME_TABLEPREFIX, defaultValue = "kieker")
		})
public class DbReader extends AbstractReaderPlugin {

	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	public static final String CONFIG_PROPERTY_NAME_DRIVERCLASSNAME = "DriverClassname";
	public static final String CONFIG_PROPERTY_NAME_CONNECTIONSTRING = "ConnectionString";
	public static final String CONFIG_PROPERTY_NAME_TABLEPREFIX = "TablePrefix";

	private static final Log LOG = LogFactory.getLog(DbReader.class);

	private final String driverClassname;
	private final String connectionString;
	private final String tablePrefix;

	private volatile boolean running = true;

	public DbReader(final Configuration configuration) throws Exception {
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
			Statement getIndexTable = null;
			try {
				getIndexTable = connection.createStatement();
				ResultSet indexTable = null;
				try { // NOCS (nested try)
					indexTable = getIndexTable.executeQuery("SELECT * from " + this.tablePrefix);
					while (this.running && indexTable.next()) {
						final String tablename = indexTable.getString(1);
						final String classname = indexTable.getString(2);
						try { // NOCS (nested try)
							this.table2record(connection, tablename, AbstractMonitoringRecord.classForName(classname));
						} catch (final MonitoringRecordException ex) {
							// log error but continue with next table
							LOG.error("Failed to load records of type " + classname + " from table " + tablename, ex);
							continue;
						}
					}
				} finally {
					if (indexTable != null) {
						indexTable.close();
					}
				}
			} finally {
				if (getIndexTable != null) {
					getIndexTable.close();
				}
			}
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

	private void table2record(final Connection connection, final String tablename, final Class<? extends IMonitoringRecord> clazz)
			throws SQLException, MonitoringRecordException {
		Statement selectRecord = null;
		try {
			selectRecord = connection.createStatement();
			ResultSet records = null;
			try {
				records = selectRecord.executeQuery("SELECT * from " + tablename);
				final int size = records.getMetaData().getColumnCount() - 2; // remove index column
				while (this.running && records.next()) {
					final Object[] recordValues = new Object[size];
					for (int i = 0; i < size; i++) {
						recordValues[i] = records.getObject(i + 3);
					}
					final IMonitoringRecord record = AbstractMonitoringRecord.createFromArray(clazz, recordValues);
					record.setLoggingTimestamp(records.getLong(2));
					super.deliver(OUTPUT_PORT_NAME_RECORDS, record);
				}
			} finally {
				if (records != null) {
					records.close();
				}
			}
		} finally {
			if (selectRecord != null) {
				selectRecord.close();
			}
		}
	}

	public void terminate(final boolean error) {
		LOG.info("Shutdown of DBReader requested.");
		this.running = false;
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_DRIVERCLASSNAME, this.driverClassname);
		configuration.setProperty(CONFIG_PROPERTY_NAME_CONNECTIONSTRING, this.connectionString);
		configuration.setProperty(CONFIG_PROPERTY_NAME_TABLEPREFIX, this.tablePrefix);
		return configuration;
	}

}
