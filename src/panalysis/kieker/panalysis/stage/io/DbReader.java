/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.panalysis.stage.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.Description;
import kieker.panalysis.framework.core.IOutputPort;

/**
 * A very simple database reader that probably only works for small data sets.
 * 
 * @author Jan Waller, Nils Christian Ehmke
 * 
 * @since 1.10
 */
@Description("A reader which reads records from a database")
public class DbReader extends AbstractFilter<DbReader> {

	private final IOutputPort<DbReader, IMonitoringRecord> outputPort = super.createOutputPort();

	@Description("The classname of the driver used for the connection.")
	private String driverClassname = "org.apache.derby.jdbc.EmbeddedDrive";
	@Description("The connection string used to establish the connection.")
	private String connectionString = "jdbc:derby:tmp/KIEKER;user=DBUSER;password=DBPASS";
	@Description("The prefix of the used table within the database.")
	private String tablePrefix = "kieker";

	private volatile boolean running = true;

	@Override
	public void onPipelineStarts() throws Exception {
		super.onPipelineStarts();
		try {
			Class.forName(this.driverClassname).newInstance();
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			throw new Exception("DB driver registration failed. Perhaps the driver jar is missing?", ex);
		}
	}

	@Override
	public void onPipelineStops() {
		super.logger.info("Shutdown of DBReader requested.");
		this.running = false;
		super.onPipelineStops();
	}

	@Override
	protected boolean execute(final Context<DbReader> context) {
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
							this.table2record(context, connection, tablename, AbstractMonitoringRecord.classForName(classname));
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
			return false;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (final SQLException ex) {
					super.logger.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
				}
			}
		}
		return true;
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
	private void table2record(final Context<DbReader> context, final Connection connection, final String tablename, final Class<? extends IMonitoringRecord> clazz)
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
					context.put(this.outputPort, record);
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
