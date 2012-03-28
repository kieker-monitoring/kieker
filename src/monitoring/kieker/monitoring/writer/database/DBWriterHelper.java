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
 * This class should be instantiated and used once per {@link Connection}.
 * 
 * @author Jan Waller
 */
public final class DBWriterHelper implements IMonitoringRecordReceiver {
	private static final Log LOG = LogFactory.getLog(DBWriterHelper.class);

	private final Connection connection;
	private final String tablePrefix;

	private final Map<Class<? extends IMonitoringRecord>, PreparedStatement> recordTypeInformation = new ConcurrentHashMap<Class<? extends IMonitoringRecord>, PreparedStatement>();

	private final PreparedStatement preparedStatementTotal;
	private final AtomicLong recordId = new AtomicLong();

	private final Map<Class<?>, String> createTypeMap = new ConcurrentHashMap<Class<?>, String>();

	public DBWriterHelper(final Connection connection, final String tablePrefix) throws SQLException {
		this.connection = connection;
		this.tablePrefix = tablePrefix;
		// get initial info on the used database
		this.initializeTypeMap();
		// create initial table
		this.createTable(tablePrefix, String.class);
		this.preparedStatementTotal = connection.prepareStatement("INSERT INTO " + tablePrefix + " VALUES (?, ?)");
	}

	private final void initializeTypeMap() throws SQLException {
		final DatabaseMetaData dbmd = this.connection.getMetaData();
		final ResultSet rs = dbmd.getTypeInfo();
		while (rs.next()) {
			final int id = rs.getInt("DATA_TYPE");
			final String typeName = rs.getString("TYPE_NAME");
			final String typeParams = rs.getString("CREATE_PARAMS");
			switch (id) {
			case Types.VARCHAR: // String
				if (typeParams != null) {
					this.createTypeMap.put(String.class, typeName + " (255)");
				} else {
					this.createTypeMap.put(String.class, typeName);
				}
				break;
			case Types.INTEGER: // Integer
				this.createTypeMap.put(int.class, typeName);
				this.createTypeMap.put(Integer.class, typeName);
				break;
			case Types.BIGINT: // Long
				this.createTypeMap.put(long.class, typeName);
				this.createTypeMap.put(Long.class, typeName);
				break;
			case Types.REAL: // Float
				this.createTypeMap.put(float.class, typeName);
				this.createTypeMap.put(Float.class, typeName);
				break;
			case Types.DOUBLE: // Double
				this.createTypeMap.put(double.class, typeName);
				this.createTypeMap.put(Double.class, typeName);
				break;
			case Types.TINYINT: // Byte
				this.createTypeMap.put(byte.class, typeName);
				this.createTypeMap.put(Byte.class, typeName);
				break;
			case Types.SMALLINT: // Short
				this.createTypeMap.put(short.class, typeName);
				this.createTypeMap.put(Short.class, typeName);
				break;
			case Types.BIT: // Boolean
				this.createTypeMap.put(boolean.class, typeName);
				this.createTypeMap.put(Boolean.class, typeName);
				break;
			default: // unneeded
				break;
			}
		}
		rs.close();
	}

	public void createTable(final String tableName, final Class<?>... columns) throws SQLException {
		final StringBuilder stmt = new StringBuilder();
		// stmt.append("DROP TABLE ").append(tableName).append(';');
		stmt.append("CREATE TABLE ").append(tableName).append(" (id ");
		final String createLong = this.createTypeMap.get(long.class);
		if (createLong != null) {
			stmt.append(createLong);
		} else {
			throw new SQLException("Type 'long' not supported.");
		}
		int i = 1;
		for (final Class<?> c : columns) {
			stmt.append(", c").append(i++).append(' ');
			final String createType = this.createTypeMap.get(c);
			if (createType != null) {
				stmt.append(createType);
			} else {
				throw new SQLException("Type '" + c.getSimpleName() + "' not supported.");
			}
		}
		stmt.append(")");
		final String s = stmt.toString();
		final Statement statement = this.connection.createStatement();
		DBWriterHelper.LOG.info("Creating table: " + s);
		statement.execute(s);
	}

	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		final Class<? extends IMonitoringRecord> recordClass = record.getClass();
		final String recordClassName = recordClass.getSimpleName();
		if (!this.recordTypeInformation.containsKey(recordClass)) { // not yet seen record
			DBWriterHelper.LOG.info("New record type found: " + recordClassName);
			final String tableName = this.tablePrefix + "_" + recordClassName;
			final Class<?>[] typeArray;
			try {
				typeArray = AbstractMonitoringRecord.typesForClass(recordClass);
			} catch (final MonitoringRecordException ex) {
				DBWriterHelper.LOG.error("Failed to get types of record", ex);
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
				DBWriterHelper.LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
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
				} else if (recordFields[i] instanceof Float) {
					preparedStatement.setFloat(i + 2, (Float) recordFields[i]);
				} else if (recordFields[i] instanceof Double) {
					preparedStatement.setDouble(i + 2, (Double) recordFields[i]);
				} else if (recordFields[i] instanceof Byte) {
					preparedStatement.setByte(i + 2, (Byte) recordFields[i]);
				} else if (recordFields[i] instanceof Short) {
					preparedStatement.setShort(i + 2, (Short) recordFields[i]);
				} else if (recordFields[i] instanceof Boolean) {
					preparedStatement.setBoolean(i + 2, (Boolean) recordFields[i]);
				} else if (recordFields[i] == null) {
					DBWriterHelper.LOG.error("Null value in record not supported!");
					return false;
				} else {
					DBWriterHelper.LOG.error("Type '" + recordFields[i].getClass().getSimpleName() + "' not supported");
					return false;
				}
			}
			preparedStatement.executeUpdate();

			// send to overviewtable
			this.preparedStatementTotal.setLong(1, recordId);
			this.preparedStatementTotal.setString(2, recordClassName);
			this.preparedStatementTotal.executeUpdate();
		} catch (final SQLException ex) {
			DBWriterHelper.LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
			return false;
		}
		return true;
	}
}
