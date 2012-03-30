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

package kieker.monitoring.writer.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import kieker.common.configuration.Configuration;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.AbstractAsyncThread;
import kieker.monitoring.writer.AbstractAsyncWriter;

/**
 * Stores monitoring data into a database.
 * 
 * Warning! This class is an academic prototype and not intended for usage in any critical system.
 * 
 * @author Jan Waller
 */
public final class AsyncDbWriter extends AbstractAsyncWriter {
	private static final String PREFIX = AsyncDbWriter.class.getName() + ".";
	public static final String CONFIG_DRIVERCLASSNAME = PREFIX + "DriverClassname"; // NOCS (AfterPREFIX)
	public static final String CONFIG_CONNECTIONSTRING = PREFIX + "ConnectionString"; // NOCS (AfterPREFIX)
	public static final String CONFIG_TABLEPREFIX = PREFIX + "TablePrefix"; // NOCS (AfterPREFIX)
	public static final String CONFIG_NRCONN = PREFIX + "numberOfConnections"; // NOCS (AfterPREFIX)

	private final String tablePrefix;
	private final String connectionString;
	private final AtomicLong recordId = new AtomicLong();

	public AsyncDbWriter(final Configuration configuration) throws Exception {
		super(configuration);
		try {
			Class.forName(this.configuration.getStringProperty(CONFIG_DRIVERCLASSNAME)).newInstance();
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			throw new Exception("DB driver registration failed. Perhaps the driver jar is missing?", ex);
		}
		this.connectionString = configuration.getStringProperty(CONFIG_CONNECTIONSTRING);
		this.tablePrefix = configuration.getStringProperty(CONFIG_TABLEPREFIX);
	}

	@Override
	public void init() throws Exception {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(this.configuration.getStringProperty(CONFIG_CONNECTIONSTRING));
			new DBWriterHelper(connection, this.tablePrefix).createIndexTable();
		} catch (final SQLException ex) {
			throw new Exception("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		try {
			for (int i = 0; i < this.configuration.getIntProperty(CONFIG_NRCONN); i++) {
				this.addWorker(new DbWriterThread(super.monitoringController, this.blockingQueue, i, this.connectionString, this.tablePrefix, this.recordId));
			}
		} catch (final SQLException ex) {
			throw new Exception("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
		}
	}
}

/**
 * @author Jan Waller
 */
final class DbWriterThread extends AbstractAsyncThread {
	private static final Log LOG = LogFactory.getLog(DbWriterThread.class);

	private final String tablePrefix;
	private final Connection connection;
	private final DBWriterHelper helper;

	private final Map<Class<? extends IMonitoringRecord>, PreparedStatement> recordTypeInformation = new ConcurrentHashMap<Class<? extends IMonitoringRecord>, PreparedStatement>(); // NOPMD
	private final AtomicLong recordId;

	public DbWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> blockingQueue, final int threadId,
			final String connectionString, final String tablePrefix, final AtomicLong recordId) throws SQLException {
		super(monitoringController, blockingQueue);
		this.recordId = recordId;
		this.connection = DriverManager.getConnection(connectionString);
		this.tablePrefix = tablePrefix + "_" + threadId;
		this.helper = new DBWriterHelper(this.connection, tablePrefix);
	}

	@Override
	protected final void consume(final IMonitoringRecord record) throws Exception {
		final Class<? extends IMonitoringRecord> recordClass = record.getClass();
		final String recordClassName = recordClass.getSimpleName();
		if (!this.recordTypeInformation.containsKey(recordClass)) { // not yet seen record
			DbWriterThread.LOG.info("New record type found: " + recordClassName);
			final String tableName = this.tablePrefix + "_" + recordClassName;
			final Class<?>[] typeArray;
			try {
				typeArray = AbstractMonitoringRecord.typesForClass(recordClass);
			} catch (final MonitoringRecordException ex) {
				throw new Exception("Failed to get types of record", ex);
			}
			try {
				this.helper.createTable(tableName, recordClass.getName(), typeArray);
				final StringBuilder sb = new StringBuilder("?");
				for (int count = typeArray.length; count > 0; count--) {
					sb.append(",?");
				}
				final PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO " + tableName + " VALUES (" + sb.toString() + ")");
				this.recordTypeInformation.put(recordClass, preparedStatement);
			} catch (final SQLException ex) {
				throw new Exception("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
			}
		}
		try {
			final long id = this.recordId.getAndIncrement();
			// send to actual table
			final PreparedStatement preparedStatement = this.recordTypeInformation.get(recordClass);
			preparedStatement.setLong(1, id);
			final Object[] recordFields = record.toArray();
			for (int i = 0; i < recordFields.length; i++) {
				if (!this.helper.set(preparedStatement, i + 2, recordFields[i])) {
					throw new Exception("Failed to add record to database.");
				}
			}
			preparedStatement.executeUpdate();
		} catch (final SQLException ex) {
			throw new Exception("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
		}
	}

	@Override
	protected void cleanup() {
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
			DbWriterThread.LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("; Connection: '");
		sb.append(this.connection.toString());
		sb.append("'");
		return sb.toString();
	}
}
