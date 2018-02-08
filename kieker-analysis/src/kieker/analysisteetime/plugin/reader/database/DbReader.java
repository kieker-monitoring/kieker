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

package kieker.analysisteetime.plugin.reader.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractProducerStage;

/**
 * A very simple database reader that probably only works for small data sets.
 *
 * @author Jan Waller, Nils Christian Ehmke
 *
 * @since 1.10
 */
// @Description("A reader which reads records from a database")
public class DbReader extends AbstractProducerStage<IMonitoringRecord> {

	/**
	 * The classname of the driver used for the connection.
	 */
	private String driverClassname = "org.apache.derby.jdbc.EmbeddedDrive";

	/**
	 * The connection string used to establish the connection.
	 */
	private String connectionString = "jdbc:derby:tmp/KIEKER;user=DBUSER;password=DBPASS";

	/**
	 * The prefix of the used table within the database.
	 */
	private String tablePrefix = "kieker";

	private volatile boolean running = true;

	public DbReader() {
		// create reader
	}

	@Override
	public void onStarting() throws Exception {
		try {
			Class.forName(this.driverClassname).newInstance();
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			this.logger.error("DB driver registration failed. Perhaps the driver jar is missing?", ex);
			throw ex;
		}
	}

	// @Override // TODO implement onStop
	// public void onPipelineStops() {
	// super.logger.info("Shutdown of DBReader requested.");
	// this.running = false;
	// super.onPipelineStops();
	// }

	@Override
	protected void execute() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(this.connectionString);
			PreparedStatement getIndexTable = null;
			try {
				getIndexTable = connection.prepareStatement("SELECT * from ?");
				getIndexTable.setString(1, this.tablePrefix);
				ResultSet indexTable = null;
				try { // NOCS (nested try)
					indexTable = getIndexTable.executeQuery();
					while (this.running && indexTable.next()) {
						final String tablename = indexTable.getString(1);
						final String classname = indexTable.getString(2);
						try { // NOCS (nested try)
							this.table2record(connection, tablename, AbstractMonitoringRecord.classForName(classname));
						} catch (final MonitoringRecordException ex) {
							// log error but continue with next table
							super.logger.error("Failed to load records of type " + classname + " from table " + tablename, ex);
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
			super.logger.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (final SQLException ex) {
					super.logger.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
				}
			}
		}
	}

	public final String getDriverClassname() {
		return this.driverClassname;
	}

	public final void setDriverClassname(final String driverClassname) {
		this.driverClassname = driverClassname;
	}

	public final String getConnectionString() {
		return this.connectionString;
	}

	public final void setConnectionString(final String connectionString) {
		this.connectionString = connectionString;
	}

	public final String getTablePrefix() {
		return this.tablePrefix;
	}

	public final void setTablePrefix(final String tablePrefix) {
		this.tablePrefix = tablePrefix;
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
	private void table2record(final Connection connection, final String tablename, final Class<? extends IMonitoringRecord> clazz)
			throws SQLException, MonitoringRecordException {
		PreparedStatement selectRecord = null;
		try {
			selectRecord = connection.prepareStatement("SELECT * from ?");
			selectRecord.setString(1, tablename);
			ResultSet records = null;
			try {
				records = selectRecord.executeQuery();
				final int size = records.getMetaData().getColumnCount() - 2; // remove index column
				while (this.running && records.next()) {
					final Object[] recordValues = new Object[size];
					for (int i = 0; i < size; i++) {
						recordValues[i] = records.getObject(i + 3);
					}
					final IMonitoringRecord record = AbstractMonitoringRecord.createFromArray(clazz, recordValues);
					record.setLoggingTimestamp(records.getLong(2));
					this.outputPort.send(record);
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

}
