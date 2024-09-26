/***************************************************************************
 * Copyright 2018 Armin Moebius, Sven Ulrich (https://www.rbee.io)
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
package kieker.extension.cassandra.writer;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.internal.core.cql.DefaultBoundStatement;

import kieker.common.exception.ConfigurationException;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueSerializer;
import kieker.extension.cassandra.CassandraValueSerializer;

/**
 *
 * @author Armin Moebius, Sven Ulrich, Reiner Jung
 * @since 1.16
 */
public class CassandraDb {

	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraDb.class);

	private static final String DB_TEXT = "text";

	private static final String DB_INT = "int";

	private static final String DB_BIGINT = "bigint";

	private static final String DB_FLOAT = "float";

	private static final String DB_DOUBLE = "double";

	private static final String DB_BOOLEAN = "boolean";

	private static final String DB_VARCHAR = "varchar";

	private final List<InetSocketAddress> contactPoints;
	private final String keyspace;
	private final String tablePrefix;
	private final boolean dropTables;

	private final Map<Class<?>, String> databaseTypeMap = new ConcurrentHashMap<>();

	private final ConcurrentHashMap<Class<? extends IMonitoringRecord>, PreparedStatement> classes = new ConcurrentHashMap<>();

	private CqlSession session;

	/**
	 * Creates a new instance of this class using the given parameter.
	 *
	 * @param keyspace
	 * @param contactPoints
	 * @param tablePrefix
	 * @param dropTables
	 */
	public CassandraDb(final String keyspace, final List<InetSocketAddress> contactPoints, final String tablePrefix, final boolean dropTables) {
		this.initializeDatabaseTypeMapping();
		this.keyspace = keyspace;
		this.tablePrefix = tablePrefix;
		this.dropTables = dropTables;
		this.contactPoints = contactPoints;
	}

	/**
	 * Establishes a connection to the database.
	 *
	 * @return true when the connection could be established
	 */
	public boolean connect() {
		try {
			this.session = CqlSession.builder().build();
			this.createIndexTable();
			return true;
		} catch (final Exception exc) {
			LOGGER.error("Opening Connection to Database failed. {}", exc.getLocalizedMessage());
			return false;
		}
	}

	/**
	 * Closes all open connections to the database.
	 */
	public void disconnect() {
		this.session.close();
	}

	/**
	 * Insert a record into the database.
	 *
	 * @param record
	 *            the record
	 * @param benchmarkId
	 *            the current benchmarkId
	 * @throws MonitoringRecordException
	 *             failed to insert the record or creating and registring the record table in the first place.
	 */
	public void insert(final IMonitoringRecord record, final String benchmarkId) throws MonitoringRecordException {
		final Class<? extends IMonitoringRecord> recordClass = record.getClass();
		final BoundStatement boundStatement = this.getBoundStatement(this.getPreparedStatement(recordClass, benchmarkId));

		final IValueSerializer cassandraSerializer = new CassandraValueSerializer(boundStatement);
		cassandraSerializer.putLong(record.getLoggingTimestamp());
		record.serialize(cassandraSerializer);

		this.session.execute(boundStatement);
	}

	/**
	 * Create new prepared statement.
	 *
	 * @param recordClass
	 *            record class type
	 * @param benchmarkId
	 *            benchmark id
	 * @return returns prepared statement
	 * @throws MonitoringRecordException
	 *             when the creation of a new entry or the table for the record itself failed.
	 */
	private PreparedStatement getPreparedStatement(final Class<? extends IMonitoringRecord> recordClass, final String benchmarkId)
			throws MonitoringRecordException {
		PreparedStatement statement = this.classes.get(recordClass);
		if (statement == null) {
			statement = this.createRecordInsertStatement(recordClass.getSimpleName(), benchmarkId, recordClass);
			this.classes.put(recordClass, statement);
		}
		return statement;
	}

	/**
	 * Create a new prepared statement to insert a new record.
	 *
	 * @param className
	 *            class name for the table
	 * @param benchmarkId
	 *            benchmark id
	 * @param recordClass
	 *            the record class
	 * @return a prepared statement
	 */
	private PreparedStatement createRecordInsertStatement(final String className, final String benchmarkId,
			final Class<? extends IMonitoringRecord> recordClass) throws MonitoringRecordException {
		Class<?>[] typeArray = null;
		try {
			typeArray = AbstractMonitoringRecord.typesForClass(recordClass);
		} catch (final MonitoringRecordException exc) {
			LOGGER.error("Failed to get types of record: {}", exc.getLocalizedMessage());
		}

		final String tableName = this.createTable(className, typeArray);
		final StringBuilder values = new StringBuilder();
		values.append("'" + benchmarkId + "',?");

		final StringBuilder fields = new StringBuilder("benchmark_id,timestamp");

		for (int i = 1; i <= typeArray.length; i++) {
			values.append(",?");
			fields.append(",c");
			fields.append(i);
		}

		return this.session.prepare(String.format("INSERT INTO %s ( %s )  VALUES (%s)", tableName, fields.toString(), values.toString()));
	}

	/**
	 * Prepares the mapping from Java Types to Cassandra types.
	 */
	private void initializeDatabaseTypeMapping() {
		final Class<?>[] primitiveTypes = { String.class, int.class, Integer.class, long.class, Long.class, float.class, Float.class, double.class,
			Double.class, boolean.class, Boolean.class, char.class, Character.class };
		final String[] databaseTypes = { DB_TEXT, DB_INT, DB_INT, DB_BIGINT, DB_BIGINT, DB_FLOAT, DB_FLOAT, DB_DOUBLE, DB_DOUBLE, DB_INT, DB_INT, DB_BOOLEAN,
			DB_BOOLEAN,
			DB_VARCHAR, DB_VARCHAR };
		for (int i = 0; i < primitiveTypes.length; i++) {
			this.databaseTypeMap.put(primitiveTypes[i], databaseTypes[i]);
		}
	}

	/**
	 * Returns a BoundStatement from the given String. Uses the default Session to the keyspace.
	 *
	 * @param statement
	 * @return
	 */
	private BoundStatement getBoundStatement(final String statement) {
		return new DefaultBoundStatement(this.session.prepare(statement), null, null, statement, null, null, null, null, null, this.dropTables, this.dropTables, 0,
				null, 0, null, null, null, null, null, null);
	}

	/**
	 * Returns a BoundStatement from the given String. Uses the default Session to the keyspace.
	 *
	 * @param statement
	 * @return bound statement
	 */
	public BoundStatement getBoundStatement(final PreparedStatement statement) {
		return new DefaultBoundStatement(statement, null, null, this.keyspace, null, null, null, null, null, this.dropTables, this.dropTables, 0, null, 0, null,
				null, null, null, null, null);
	}

	/**
	 * Creates the index Table.
	 *
	 * @throws ConfigurationException
	 */
	private void createIndexTable() {
		if (this.dropTables) {
			this.dropTable(this.tablePrefix);
			this.createTableClassLookupTable();
		} else if (!this.doesTableExist(this.tablePrefix)) {
			this.createTableClassLookupTable();
		}
	}

	private void dropTable(final String tableName) {
		final String dropStatement = "DROP TABLE " + tableName;

		final BoundStatement boundStatement = this.getBoundStatement(dropStatement);
		try {
			this.session.execute(boundStatement);
		} catch (final Exception exc) {
			LOGGER.warn("Dropping table {} failed.", tableName);
		}
	}

	private void createTableClassLookupTable() {
		final String createStatement = String.format("CREATE TABLE %s ( tablename text, classname text, PRIMARY KEY (tablename) )",
				this.tablePrefix);
		final BoundStatement boundStatement = this.getBoundStatement(createStatement);

		try {
			this.session.execute(boundStatement);
		} catch (final Exception exc) {
			LOGGER.error("Creating index table {} failed!", this.tablePrefix);
		}
	}

	private boolean doesTableExist(final String tableName) {
		final String selectStatement = "SELECT * FROM " + tableName;
		final BoundStatement boundStatement = this.getBoundStatement(selectStatement);
		try {
			return this.session.execute(boundStatement) != null;
		} catch (final Exception exc) {
			return false;
		}
	}

	/**
	 * Creates a table in the keyspace with the given parameters.
	 *
	 * @param className
	 * @param columns
	 * @return returns the name of the table
	 * @throws ConfigurationException
	 *             on errors
	 */
	private String createTable(final String className, final Class<?>... columns) throws MonitoringRecordException {
		final String tableName = this.createTableName(className);

		if (this.dropTables) {
			this.dropTable(tableName);
			this.createClassTable(tableName, className, columns);
		} else if (!this.doesTableExist(tableName)) {
			this.createClassTable(tableName, className, columns);
		}

		return tableName;
	}

	private String createTableName(final String className) {
		return this.tablePrefix + "_" + className;
	}

	private void createClassTable(final String tableName, final String className, final Class<?>[] attributeTypes) throws MonitoringRecordException {
		final BoundStatement boundStatement = this.getBoundStatement(this.createClassTableString(tableName, attributeTypes));

		try {
			this.session.execute(boundStatement);
		} catch (final Exception exc) {
			throw new MonitoringRecordException(String.format("Creating table %s failed!", tableName), exc);
		}

		final String addIndex = String.format("INSERT INTO %s (tablename, classname) VALUES('%s','%s')", this.tablePrefix, tableName, className);
		final BoundStatement index = this.getBoundStatement(addIndex);

		try {
			this.session.execute(index);
		} catch (final Exception exc) {
			throw new MonitoringRecordException(String.format("Adding table %s to index failed!", tableName), exc);
		}
	}

	private String createClassTableString(final String tableName, final Class<?>[] attributeTypes) throws MonitoringRecordException {
		final StringBuilder createTable = new StringBuilder(100);
		createTable.append(String.format("CREATE TABLE %s (benchmark_id %s, timestamp %s",
				tableName, this.databaseTypeMap.get(String.class), this.databaseTypeMap.get(long.class)));

		int i = 0;
		for (final Class<?> c : attributeTypes) {
			createTable.append(", c").append(i++).append(' ');
			final String databaseType = this.databaseTypeMap.get(c);

			if (databaseType != null) {
				createTable.append(databaseType);
			} else {
				throw new MonitoringRecordException(String.format("Type '%s' not supported.", c.getSimpleName()));
			}
		}
		createTable.append(", PRIMARY KEY (benchmark_id, timestamp)) ");
		return createTable.toString();
	}

}
