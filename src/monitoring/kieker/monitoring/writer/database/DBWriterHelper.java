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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class DBWriterHelper {
	private static final Log LOG = LogFactory.getLog(DBWriterHelper.class);

	private final Connection connection;
	private final String indexTablename;
	private final AtomicInteger tableCounter;
	private final boolean overwrite;

	private final Map<Class<?>, String> createTypeMap = new ConcurrentHashMap<Class<?>, String>(); // NOPMD (Map)

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param connection
	 *            The connection to the database.
	 * @param indexTablename
	 *            The index table name (The created tables will have this name as a prefix).
	 * @param overwrite
	 *            If set to true, existing tables will be dropped and newly created.
	 * 
	 * @throws SQLException
	 *             If something went wrong during the preparation of the connection.
	 */
	public DBWriterHelper(final Connection connection, final String indexTablename, final boolean overwrite) throws SQLException {
		this(connection, indexTablename, new AtomicInteger(), overwrite);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param connection
	 *            The connection to the database.
	 * @param indexTablename
	 *            The index table name (The created tables will have this name as a prefix).
	 * @param tableCounter
	 *            The counter containing the number of tables within the current system.
	 * @param overwrite
	 *            If set to true, existing tables will be dropped and newly created.
	 * 
	 * @throws SQLException
	 *             If something went wrong during the preparation of the connection.
	 */
	public DBWriterHelper(final Connection connection, final String indexTablename, final AtomicInteger tableCounter, final boolean overwrite) throws SQLException {
		this.connection = connection;
		ResultSet databaseTypeInfo = null;
		try {
			databaseTypeInfo = connection.getMetaData().getTypeInfo();
			while (databaseTypeInfo.next()) {
				final int id = databaseTypeInfo.getInt("DATA_TYPE");
				final String typeName = databaseTypeInfo.getString("TYPE_NAME");
				final String typeParams = databaseTypeInfo.getString("CREATE_PARAMS");
				switch (id) {
				case Types.VARCHAR: // String
					if (typeParams != null) {
						this.createTypeMap.put(String.class, typeName + " (1024)");
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
					this.createTypeMap.put(short.class, typeName); // NOPMD (short)
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
		} finally {
			if (null != databaseTypeInfo) {
				databaseTypeInfo.close();
			}
		}
		this.indexTablename = indexTablename;
		this.tableCounter = tableCounter;
		this.overwrite = overwrite;
	}

	/**
	 * Creates a table using the given parameters.
	 * 
	 * @param classname
	 *            The name of the class which corresponds to the newly created table.
	 * @param columns
	 *            The array of classes determining the columns of this table.
	 * @return The name of the newly created table.
	 * 
	 * @throws SQLException
	 *             If something went wrong during the creation.
	 */
	public String createTable(final String classname, final Class<?>... columns) throws SQLException {
		// automatically determine the tablename
		final String tablename = this.indexTablename + "_" + this.tableCounter.getAndIncrement();
		// check whether table exists
		if (this.overwrite) {
			final String statementDropTableString = "DROP TABLE " + tablename;
			Statement statementDropTable = null;
			try {
				statementDropTable = this.connection.createStatement();
				if (LOG.isDebugEnabled()) {
					LOG.debug("Dropping table: " + statementDropTableString);
				}
				statementDropTable.execute(statementDropTableString);
			} catch (final SQLException ignore) { // NOPMD (empty catch block)
			} finally {
				if (statementDropTable != null) {
					statementDropTable.close();
				}
			}
		}
		// create the table
		final StringBuilder statementCreateTable = new StringBuilder(128);
		statementCreateTable.append("CREATE TABLE ").append(tablename).append(" (id ");
		final String createLong = this.createTypeMap.get(long.class);
		if (createLong != null) {
			statementCreateTable.append(createLong);
		} else {
			throw new SQLException("Type 'long' not supported.");
		}
		statementCreateTable.append(", timestamp ").append(createLong);
		int i = 1;
		for (final Class<?> c : columns) {
			statementCreateTable.append(", c").append(i++).append(' ');
			final String createType = this.createTypeMap.get(c);
			if (createType != null) {
				statementCreateTable.append(createType);
			} else {
				throw new SQLException("Type '" + c.getSimpleName() + "' not supported.");
			}
		}
		statementCreateTable.append(')');
		final String statementCreateTableString = statementCreateTable.toString();
		Statement statement = null;
		try {
			statement = this.connection.createStatement();
			if (LOG.isDebugEnabled()) {
				LOG.debug("Creating table: " + statementCreateTableString);
			}
			statement.execute(statementCreateTableString);
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		// insert this new table into the index table
		try {
			statement = this.connection.createStatement();
			statement.executeUpdate("INSERT INTO " + this.indexTablename + " VALUES ('" + tablename + "','" + classname + "')");
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		return tablename;
	}

	public void createIndexTable() throws SQLException {
		final String createString = this.createTypeMap.get(String.class);
		if (createString == null) {
			throw new SQLException("Type 'String' not supported.");
		}
		// check whether table exists
		if (this.overwrite) {
			final String statementDropTableString = "DROP TABLE " + this.indexTablename;
			Statement statementDropTable = null;
			try {
				statementDropTable = this.connection.createStatement();
				if (LOG.isDebugEnabled()) {
					LOG.debug("Dropping table: " + statementDropTableString);
				}
				statementDropTable.execute(statementDropTableString);
			} catch (final SQLException ignore) { // NOPMD (empty catch block)
			} finally {
				if (statementDropTable != null) {
					statementDropTable.close();
				}
			}
		}
		final StringBuilder statementCreateTable = new StringBuilder(128);
		statementCreateTable.append("CREATE TABLE ").append(this.indexTablename);
		statementCreateTable.append(" (tablename ").append(createString).append(", classname ").append(createString).append(')');
		final String statementCreateTableString = statementCreateTable.toString();
		Statement statement = null;
		try {
			statement = this.connection.createStatement();
			if (LOG.isDebugEnabled()) {
				LOG.debug("Creating table: " + statementCreateTableString);
			}
			statement.execute(statementCreateTableString);
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	/**
	 * This is a simple helper method using automatically the correct setter method for the given value to set a value within the prepared statement.
	 * 
	 * @param preparedStatement
	 *            The prepared statement to fill.
	 * @param parameterIndex
	 *            The index of the parameter to fill.
	 * @param value
	 *            The value of the parameter to fill.
	 * 
	 * @return true iff the setting was successful.
	 * 
	 * @throws SQLException
	 *             If, for example, the connection has already been closed or the given index is invalid.
	 */
	public boolean set(final PreparedStatement preparedStatement, final int parameterIndex, final Object value) throws SQLException {
		if (value instanceof String) {
			preparedStatement.setString(parameterIndex, (String) value);
		} else if (value instanceof Integer) {
			preparedStatement.setInt(parameterIndex, (Integer) value);
		} else if (value instanceof Long) {
			preparedStatement.setLong(parameterIndex, (Long) value);
		} else if (value instanceof Float) {
			preparedStatement.setFloat(parameterIndex, (Float) value);
		} else if (value instanceof Double) {
			preparedStatement.setDouble(parameterIndex, (Double) value);
		} else if (value instanceof Byte) {
			preparedStatement.setByte(parameterIndex, (Byte) value);
		} else if (value instanceof Short) {
			preparedStatement.setShort(parameterIndex, (Short) value);
		} else if (value instanceof Boolean) {
			preparedStatement.setBoolean(parameterIndex, (Boolean) value);
		} else if (value == null) {
			LOG.error("Null value in record not supported!");
			return false;
		} else {
			LOG.error("Type '" + value.getClass().getSimpleName() + "' not supported");
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(128);
		sb.append("Prefix: '");
		sb.append(this.indexTablename);
		sb.append("'; Drop Tables: '");
		sb.append(this.overwrite);
		sb.append("'; Created Tables: '");
		sb.append(this.tableCounter.get());
		sb.append('\'');
		return sb.toString();
	}
}
