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
import java.util.concurrent.ConcurrentHashMap;
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
 */
public final class SyncDbWriter extends AbstractMonitoringWriter {
	private static final String PREFIX = SyncDbWriter.class.getName() + ".";
	public static final String CONFIG_DRIVERCLASSNAME = SyncDbWriter.PREFIX + "DriverClassname"; // NOCS (AfterPREFIX)
	public static final String CONFIG_CONNECTIONSTRING = SyncDbWriter.PREFIX + "ConnectionString"; // NOCS (AfterPREFIX)
	public static final String CONFIG_TABLEPREFIX = SyncDbWriter.PREFIX + "TablePrefix"; // NOCS (AfterPREFIX)

	private static final Log LOG = LogFactory.getLog(SyncDbWriter.class);

	private final String tablePrefix;
	private final Connection connection;
	private final DBWriterHelper helper;

	private final Map<Class<? extends IMonitoringRecord>, PreparedStatement> recordTypeInformation = new ConcurrentHashMap<Class<? extends IMonitoringRecord>, PreparedStatement>();

	private final PreparedStatement preparedStatementInsertOverview;
	private final AtomicLong recordId = new AtomicLong();

	public SyncDbWriter(final Configuration configuration) throws Exception {
		super(configuration);
		try {
			Class.forName(configuration.getStringProperty(SyncDbWriter.CONFIG_DRIVERCLASSNAME)).newInstance();
		} catch (final Throwable ex) { // NOPMD NOCS (IllegalCatchCheck)
			throw new Exception("DB driver registration failed. Perhaps the driver jar is missing?", ex);
		}
		try {
			this.connection = DriverManager.getConnection(configuration.getStringProperty(SyncDbWriter.CONFIG_CONNECTIONSTRING));
			this.tablePrefix = configuration.getStringProperty(SyncDbWriter.CONFIG_TABLEPREFIX);
			this.helper = new DBWriterHelper(this.connection);
			// create overview table
			this.helper.createTable(this.tablePrefix, String.class);
			this.preparedStatementInsertOverview = this.connection.prepareStatement("INSERT INTO " + this.tablePrefix + " VALUES (?, ?)");
		} catch (final SQLException ex) {
			throw new Exception("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex); // NOPMD
		}
	}

	@Override
	public final void init() throws Exception {
		// nothing to do
	}

	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		final Class<? extends IMonitoringRecord> recordClass = record.getClass();
		final String recordClassName = recordClass.getSimpleName();
		if (!this.recordTypeInformation.containsKey(recordClass)) { // not yet seen record
			SyncDbWriter.LOG.info("New record type found: " + recordClassName);
			final String tableName = this.tablePrefix + "_" + recordClassName;
			final Class<?>[] typeArray;
			try {
				typeArray = AbstractMonitoringRecord.typesForClass(recordClass);
			} catch (final MonitoringRecordException ex) {
				SyncDbWriter.LOG.error("Failed to get types of record", ex);
				return false;
			}
			try {
				this.helper.createTable(tableName, typeArray);
				final StringBuilder sb = new StringBuilder("?");
				for (@SuppressWarnings("unused")
				final Class<?> element : typeArray) {
					sb.append(",?");
				}
				final PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO " + tableName + " VALUES (" + sb.toString() + ")");
				this.recordTypeInformation.put(recordClass, preparedStatement);
			} catch (final SQLException ex) {
				SyncDbWriter.LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
				return false;
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
					return false;
				}
			}
			preparedStatement.executeUpdate();

			// send to overview table
			this.preparedStatementInsertOverview.setLong(1, id);
			this.preparedStatementInsertOverview.setString(2, recordClassName);
			this.preparedStatementInsertOverview.executeUpdate();
		} catch (final SQLException ex) {
			SyncDbWriter.LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
			return false;
		}
		return true;
	}

	public void terminate() {
		try {
			// close all prepared statements
			if (this.preparedStatementInsertOverview != null) {
				this.preparedStatementInsertOverview.close();
			}
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
			SyncDbWriter.LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
		}
		SyncDbWriter.LOG.info("Writer: SyncDbWriter shutdown complete");
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\n\tConnection: '");
		sb.append(this.connection.toString());
		sb.append("'");
		return sb.toString();
	}
}
