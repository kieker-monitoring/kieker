/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

/**
 * A very simple database reader that probably only works for small data sets.
 *
 * @author Jan Waller
 *
 * @since 1.5
 */
@Plugin(description = "A reader which reads records from a database", outputPorts = {
	@OutputPort(name = DbReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class }, description = "Output Port of the DBReader")
}, configuration = {
	@Property(name = DbReader.CONFIG_PROPERTY_NAME_DRIVERCLASSNAME, defaultValue = "org.apache.derby.jdbc.EmbeddedDriver", description = "The classname of the driver used for the connection."),
	@Property(name = DbReader.CONFIG_PROPERTY_NAME_CONNECTIONSTRING, defaultValue = "jdbc:derby:tmp/KIEKER;user=DBUSER;password=DBPASS", description = "The connection string used to establish the connection."),
	@Property(name = DbReader.CONFIG_PROPERTY_NAME_TABLEPREFIX, defaultValue = "kieker", description = "The prefix of the used table within the database.")
})
public class DbReader extends AbstractReaderPlugin {

	/** This is the name of the outport port delivering the records from the database. */
	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	/** The name of the property containing the class name of the driver. */
	public static final String CONFIG_PROPERTY_NAME_DRIVERCLASSNAME = "DriverClassname";
	/** The name of the property containing the string to connect to the database. */
	public static final String CONFIG_PROPERTY_NAME_CONNECTIONSTRING = "ConnectionString";
	/** The name of the property containing the prefix for the tables to read. */
	public static final String CONFIG_PROPERTY_NAME_TABLEPREFIX = "TablePrefix";

	private final String driverClassname;
	private final String connectionString;
	private final String tablePrefix;

	private volatile boolean running = true;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 *
	 * @throws Exception
	 *             If the driver for the database could not be found.
	 */
	public DbReader(final Configuration configuration, final IProjectContext projectContext) throws Exception {
		super(configuration, projectContext);

		this.driverClassname = configuration.getStringProperty(CONFIG_PROPERTY_NAME_DRIVERCLASSNAME);
		this.connectionString = configuration.getStringProperty(CONFIG_PROPERTY_NAME_CONNECTIONSTRING);
		this.tablePrefix = configuration.getStringProperty(CONFIG_PROPERTY_NAME_TABLEPREFIX);
		try {
			Class.forName(this.driverClassname).newInstance();
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			throw new Exception("DB driver registration failed. Perhaps the driver jar is missing?", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
							this.logger.error("Failed to load records of type {} from table {}", classname, tablename, ex);
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
			this.logger.error("SQLException with SQLState: '{}' and VendorError: '{}'", ex.getSQLState(), ex.getErrorCode(), ex);
			return false;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (final SQLException ex) {
					this.logger.error("SQLException with SQLState: '{}' and VendorError: '{}'", ex.getSQLState(), ex.getErrorCode(), ex);
				}
			}
		}
		return true;
	}

	/**
	 * This method uses the given table to read records and sends them to the output port.
	 *
	 * @param connection
	 *            The connection to the database which will be used.
	 * @param tablename
	 *            The name of the table containing records.
	 * @param clazz
	 *            The class of the monitoring records. This will be used to convert the array into the record.
	 * @throws SQLException
	 *             If something went wrong during the database access.
	 * @throws MonitoringRecordException
	 *             If the data within the table could not be converted into a valid record.
	 */
	private void table2record(final Connection connection, final String tablename, final Class<? extends IMonitoringRecord> clazz) throws SQLException,
			MonitoringRecordException {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate(final boolean error) {
		this.logger.info("Shutdown of DBReader requested.");
		this.running = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_DRIVERCLASSNAME, this.driverClassname);
		configuration.setProperty(CONFIG_PROPERTY_NAME_CONNECTIONSTRING, this.connectionString);
		configuration.setProperty(CONFIG_PROPERTY_NAME_TABLEPREFIX, this.tablePrefix);
		return configuration;
	}

}
