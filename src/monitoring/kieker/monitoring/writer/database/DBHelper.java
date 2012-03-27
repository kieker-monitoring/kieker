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
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.IMonitoringRecordReceiver;

/**
 * @author Jan Waller
 */
public final class DBHelper implements IMonitoringRecordReceiver {
	private static final Log LOG = LogFactory.getLog(DBHelper.class);

	private final Connection connection;
	private final String tablePrefix;

	private final Map<Class<? extends IMonitoringRecord>, PreparedStatement> recordTypeInformation = new ConcurrentHashMap<Class<? extends IMonitoringRecord>, PreparedStatement>();

	private final String createString;
	private final String createLong;
	private final String createInteger;

	private final PreparedStatement preparedStatementTotal;
	private final AtomicLong recordId = new AtomicLong();

	public DBHelper(final Connection connection, final String tablePrefix) throws SQLException {
		this.connection = connection;
		this.tablePrefix = tablePrefix;
		// get initial info on the used database
		String createString = "VARCHAR";
		String createLong = "BIGINT";
		String createInteger = "INTEGER";
		// check database for types
		final DatabaseMetaData dbmd = connection.getMetaData();
		final ResultSet rs = dbmd.getTypeInfo();
		while (rs.next()) {
			final int id = rs.getInt("DATA_TYPE");
			final String dbmsName = rs.getString("TYPE_NAME");
			// final String createParams = rs.getString("CREATE_PARAMS");
			// DBHelper.LOG.info(dbmsName + " : " + createParams);
			switch (id) {
			case Types.VARCHAR:
				createString = dbmsName + " (255)";
				break;
			case Types.BIGINT:
				createLong = dbmsName;
				break;
			case Types.INTEGER:
				createInteger = dbmsName;
				break;
			}
		}
		this.createString = createString;
		this.createLong = createLong;
		this.createInteger = createInteger;
		rs.close();
		// create initial table
		this.createTable(tablePrefix, String.class);
		this.preparedStatementTotal = connection.prepareStatement("INSERT INTO " + tablePrefix + " VALUES (?, ?)");
	}

	public void createTable(final String tableName, final Class<?>... columns) throws SQLException {
		final StringBuilder stmt = new StringBuilder("CREATE TABLE ");
		stmt.append(tableName).append(" (id ").append(this.createLong);
		int i = 0;
		for (final Class<?> c : columns) {
			stmt.append(", ").append("c").append(i++).append(' ');
			if (c == String.class) {
				stmt.append(this.createString);
			} else if ((c == int.class) || (c == Integer.class)) {
				stmt.append(this.createInteger);
			} else if ((c == long.class) || (c == Long.class)) {
				stmt.append(this.createLong);
				// } else if ((c.type == float.class) || (c.type == Float.class)) {
				// } else if ((c.type == double.class) || (c.type == Double.class)) {
				// } else if ((c.type == byte.class) || (c.type == Byte.class)) {
				// } else if ((c.type == short.class) || (c.type == Short.class)) { // NOPMD (short)
				// } else if ((c.type == boolean.class) || (c.type == Boolean.class)) {
			} else {
				DBHelper.LOG.error("Type " + c.getSimpleName() + " not supported");
			}
		}
		stmt.append(")");
		final String s = stmt.toString();
		final Statement statement = this.connection.createStatement();
		DBHelper.LOG.info("Creating table: " + s);
		statement.execute(s);
	}

	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		final Class<? extends IMonitoringRecord> recordClass = record.getClass();
		final String recordClassName = recordClass.getSimpleName();
		if (!this.recordTypeInformation.containsKey(recordClass)) { // not yet seen record
			DBHelper.LOG.info("New record type found: " + recordClassName);
			final String tableName = this.tablePrefix + "_" + recordClassName;
			final Class<?>[] typeArray;
			try {
				typeArray = AbstractMonitoringRecord.typesForClass(recordClass);
			} catch (final MonitoringRecordException ex) {
				DBHelper.LOG.error("Failed to get types of record", ex);
				return false;
			}
			try {
				this.createTable(tableName, typeArray);
				final StringBuilder s = new StringBuilder("?");
				for (@SuppressWarnings("unused")
				final Class<?> element : typeArray) {
					s.append(",?");
				}
				final PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO " + tableName + " VALUES (" + s.toString() + ")");
				this.recordTypeInformation.put(recordClass, preparedStatement);
			} catch (final SQLException ex) {
				DBHelper.LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
				return false;
			}
		}
		try {
			final long recordId = this.recordId.getAndIncrement();
			final PreparedStatement preparedStatement = this.recordTypeInformation.get(recordClass);
			preparedStatement.setLong(1, recordId);
			final Object[] recordFields = record.toArray();
			for (int i = 0; i < recordFields.length; i++) {
				if (recordFields[i] instanceof String) {
					preparedStatement.setString(i + 2, (String) recordFields[i]);
				} else if (recordFields[i] instanceof Integer) {
					preparedStatement.setInt(i + 2, (Integer) recordFields[i]);
				} else if (recordFields[i] instanceof Long) {
					preparedStatement.setLong(i + 2, (Long) recordFields[i]);
					// } else if (recordFields[i] instanceof Float) {
					// } else if (recordFields[i] instanceof Double) {
					// } else if (recordFields[i] instanceof Byte) {
					// } else if (recordFields[i] instanceof Short) {
					// } else if (recordFields[i] instanceof Boolean) {
				} else if (recordFields[i] == null) {
					DBHelper.LOG.error("null value in record detected");
					return false;
				} else {
					DBHelper.LOG.error("Type " + recordFields[i].getClass().getSimpleName() + " not supported");
					return false;
				}
			}
			preparedStatement.executeUpdate();

			this.preparedStatementTotal.setLong(1, recordId);
			this.preparedStatementTotal.setString(2, recordClassName);
			this.preparedStatementTotal.executeUpdate();
		} catch (final SQLException ex) {
			DBHelper.LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
			return false;
		}
		return true;
	}
}
