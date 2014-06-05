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

package kieker.monitoring.writer.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import kieker.common.configuration.Configuration;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * Stores monitoring data into a database.
 * 
 * Warning! This class is an academic prototype and not intended for usage in any critical system.
 * 
 * @author Jan Waller
 * 
 * @since < 0.9
 */
public final class SyncDbWriter extends AbstractMonitoringWriter {
	private static final String PREFIX = SyncDbWriter.class.getName() + ".";
	public static final String CONFIG_DRIVERCLASSNAME = PREFIX + "DriverClassname"; // NOCS (AfterPREFIX)
	public static final String CONFIG_CONNECTIONSTRING = PREFIX + "ConnectionString"; // NOCS (AfterPREFIX)
	public static final String CONFIG_TABLEPREFIX = PREFIX + "TablePrefix"; // NOCS (AfterPREFIX)
	public static final String CONFIG_OVERWRITE = PREFIX + "DropTables"; // NOCS (AfterPREFIX)

	private static final Log LOG = LogFactory.getLog(SyncDbWriter.class);

	private final Connection connection;
	private final DBWriterHelper helper;

	private final ConcurrentMap<Class<? extends IMonitoringRecord>, PreparedStatement> recordTypeInformation =
			new ConcurrentHashMap<Class<? extends IMonitoringRecord>, PreparedStatement>();

	private final AtomicLong recordId = new AtomicLong();

	/**
	 * 
	 * Creates a new instance of this class using the given parameter.
	 * 
	 * @param configuration
	 *            The configuration which will be used to initialize this writer.
	 * 
	 * @throws Exception
	 *             If the constructor failed to establish a connection to the database.
	 */
	public SyncDbWriter(final Configuration configuration) throws Exception {
		super(configuration);
		try {
			Class.forName(configuration.getStringProperty(CONFIG_DRIVERCLASSNAME)).newInstance();
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			throw new Exception("DB driver registration failed. Perhaps the driver jar is missing?", ex);
		}
		try {
			this.connection = DriverManager.getConnection(configuration.getStringProperty(CONFIG_CONNECTIONSTRING));
			this.helper = new DBWriterHelper(this.connection,
					configuration.getStringProperty(CONFIG_TABLEPREFIX),
					configuration.getBooleanProperty(CONFIG_OVERWRITE));
			this.helper.createIndexTable();
		} catch (final SQLException ex) {
			throw new Exception("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex); // NOPMD (exception)
		}
	}

	@Override
	public final void init() throws Exception {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		final Class<? extends IMonitoringRecord> recordClass = record.getClass();
		final String recordClassName = recordClass.getSimpleName();
		if (!this.recordTypeInformation.containsKey(recordClass)) { // not yet seen record
			if (LOG.isDebugEnabled()) {
				LOG.debug("New record type found: " + recordClassName);
			}
			final Class<?>[] typeArray;
			try {
				typeArray = AbstractMonitoringRecord.typesForClass(recordClass);
			} catch (final MonitoringRecordException ex) {
				LOG.error("Failed to get types of record", ex);
				return false;
			}
			try {
				final String tableName = this.helper.createTable(recordClass.getName(), typeArray);
				final StringBuilder sb = new StringBuilder("?,?"); // id and loggingTimestamp
				for (int count = typeArray.length; count > 0; count--) {
					sb.append(",?");
				}
				final PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO " + tableName + " VALUES (" + sb.toString() + ")");
				this.recordTypeInformation.put(recordClass, preparedStatement);
			} catch (final SQLException ex) {
				if (null == ex.getSQLState()) { // probably an exception by Kieker
					LOG.error("Unable to log records of type " + recordClass.getName() + ": " + ex.getMessage());
					return true; // we ignore this kind of error
				} else {
					LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
					return false;
				}
			}
		}
		try {
			final long id = this.recordId.getAndIncrement();
			// send to actual table
			final PreparedStatement preparedStatement = this.recordTypeInformation.get(recordClass);
			preparedStatement.setLong(1, id);
			preparedStatement.setLong(2, record.getLoggingTimestamp());
			final Object[] recordFields = record.toArray();
			for (int i = 0; i < recordFields.length; i++) {
				if (!this.helper.set(preparedStatement, i + 3, recordFields[i])) {
					return false;
				}
			}
			preparedStatement.executeUpdate();
		} catch (final SQLException ex) {
			LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate() {
		try {
			// close all prepared statements
			for (final Class<? extends IMonitoringRecord> recordType : this.recordTypeInformation.keySet()) {
				final PreparedStatement preparedStatement = this.recordTypeInformation.remove(recordType);
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			}
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (final SQLException ex) {
			LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
		}
		LOG.info("Writer: SyncDbWriter shutdown complete");
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(128);
		sb.append(super.toString());
		sb.append("\n\tConnection: '");
		sb.append(this.connection.toString());
		sb.append("'\n\t");
		sb.append(this.helper.toString());
		return sb.toString();
	}
}
