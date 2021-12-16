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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AuthenticationException;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.QueryExecutionException;
import com.datastax.driver.core.exceptions.QueryValidationException;
import com.datastax.driver.core.exceptions.UnsupportedFeatureException;
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;

import kieker.common.exception.ConfigurationException;

/**
 *
 * @author Armin Moebius, Sven Ulrich, Reiner Jung
 * @since 1.16
 */
public class CassandraDb {

	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraDb.class);

	private static final String DEFAULT_KEY_SPACE = "kieker";

	private static final String DEFAULT_TABLE_PREFIX = "kieker";

	private static final String DEFAULT_HOST = "localhost";
	private static final int DEFAULT_PORT = 9042;

	private final List<InetSocketAddress> contactPoints = new ArrayList<>();
	private final String defaultKeyspace;
	private final String tablePrefix;
	private final boolean dropTables;

	private final Map<String, PreparedStatement> preparedStatements = new ConcurrentHashMap<>();

	private final Map<Class<?>, String> databaseTypeMap = new ConcurrentHashMap<>();

	private Cluster cluster;
	private Session session;
	
	/**
	 * Creates a new instance of this class using the given parameter.
	 *
	 * @param keyspace
	 * @param contactPointSpecs
	 * @param tablePrefix
	 * @param dropTables
	 * @throws Exception
	 */
	public CassandraDb(final String keyspace, final String[] contactPointSpecs, final String tablePrefix, final boolean dropTables) {
		this.initializeDatabaseTypeMapping();
		this.defaultKeyspace = (keyspace == null) ? DEFAULT_KEY_SPACE : keyspace; // NOCS avoid inline conditionals
		this.tablePrefix = (tablePrefix == null) ? DEFAULT_TABLE_PREFIX : tablePrefix; // NOCS avoid inline conditionals
		this.dropTables = dropTables;
		this.computeDatabaseConnections(contactPointSpecs);
	}

	/**
	 * Establishes a connection to the database.
	 *
	 * @return true when the connection could be established
	 */
	public boolean connect() {
		try {
			this.cluster = Cluster.builder().addContactPointsWithPorts(this.contactPoints)
					.withRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE)
					.withMaxSchemaAgreementWaitSeconds(60)
					.build();

			this.session = this.cluster.connect(this.defaultKeyspace);
			return true;
		} catch (final NoHostAvailableException | AuthenticationException | InvalidQueryException | IllegalStateException exc) {
			LOGGER.error("Opening Connection to Database failed. {}", exc.getLocalizedMessage());
			return false;
		}
	}

	/**
	 * Closes all open connections to the database.
	 */
	public void disconnect() {
		this.session.close();
		this.cluster.close();
	}

	/**
	 * Create a bounded statement for a class name.
	 *
	 * @param classname
	 *            name of the class
	 * @return returns a bound statement
	 */
	public BoundStatement createBoundStatement(final String classname) {
		final PreparedStatement preparedStatement = this.session.prepare("STRING-QUERY");
		return new BoundStatement(preparedStatement);
	}

	/**
	 * Compute the database connections.
	 *
	 * @param contactPointSpecs
	 */
	private void computeDatabaseConnections(final String[] contactPointSpecs) {
		for (final String contactpoint : contactPointSpecs) {
			final String[] array = contactpoint.split(":");
			if (array.length == 2) {
				final InetSocketAddress socket = new InetSocketAddress(array[0], Integer.parseInt(array[1]));
				this.contactPoints.add(socket);
			}
		}

		if (contactPointSpecs.length == 0) {
			final InetSocketAddress socket = new InetSocketAddress(DEFAULT_HOST, DEFAULT_PORT);
			this.contactPoints.add(socket);
		}
	}

	/**
	 * Prepares the mapping from Java Types to Cassandra types.
	 */
	private void initializeDatabaseTypeMapping() {
		final Class<?>[] primitiveTypes = { String.class, int.class, Integer.class, long.class, Long.class, float.class, Float.class, double.class, 
			Double.class, boolean.class, Boolean.class, char.class, Character.class };
		final String[] databaseTypes = { "text", "int", "int", "bigint", "bigint", "float", "float", "double", "double", "int", "int", "boolean", "boolean",
			"varchar", "varchar" };
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
		PreparedStatement preparedStatement = this.preparedStatements.get(statement);
		
		if (preparedStatement == null) {
			preparedStatement = this.session.prepare(statement);
			this.preparedStatements.put(statement, preparedStatement);
		}

		return new BoundStatement(preparedStatement);
	}

	/**
	 * Creates the index Table.
	 *
	 * @throws ConfigurationException
	 */
	public void createIndexTable() throws ConfigurationException {
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
		} catch (final NoHostAvailableException | QueryExecutionException | QueryValidationException | UnsupportedFeatureException exc) {
			LOGGER.warn("Dropping table {} failed.", tableName);
		}
	}

	private void createTableClassLookupTable() {
		final String createStatement = String.format("CREATE TABLE %s ( tablename text, classname text, PRIMARY KEY (tablename) )",
				this.tablePrefix);
		final BoundStatement boundStatement = this.getBoundStatement(createStatement);
		
		try {
			this.session.execute(boundStatement);
		} catch (final NoHostAvailableException | QueryExecutionException | QueryValidationException | UnsupportedFeatureException exc) {
			LOGGER.error("Creating index table {} failed!", this.tablePrefix);
		}
	}

	private boolean doesTableExist(final String tableName) {
		final String selectStatement = "SELECT * FROM " + tableName;
		final BoundStatement boundStatement = this.getBoundStatement(selectStatement);
		try {
			return this.session.execute(boundStatement) != null;
		} catch (final NoHostAvailableException | QueryExecutionException | QueryValidationException | UnsupportedFeatureException exc) {
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
	public String createTable(final String className, final Class<?>... columns) throws ConfigurationException {
		final String tableName = this.tablePrefix + "_" + className;

		if (this.dropTables) {
			dropTable(tableName);
			createClassTable(tableName, className, columns);
		} else if (!doesTableExist(tableName)) {
			createClassTable(tableName, className, columns);
		}

		return tableName;
	}

	private void createClassTable(final String tableName, final String className, final Class<?>[] columns) throws ConfigurationException {
		final StringBuilder createTable = new StringBuilder(100);
		createTable.append(String.format("CREATE TABLE %s (benchmark_id %s, timestamp %s",
				tableName, this.databaseTypeMap.get(String.class), this.databaseTypeMap.get(long.class)));

		int i = 1;
		for (final Class<?> c : columns) {
			createTable.append(", c").append(i++).append(' ');
			final String databaseType = this.databaseTypeMap.get(c);

			if (databaseType != null) {
				createTable.append(databaseType);
			} else {
				throw new ConfigurationException(String.format("Type '%s' not supported.", c.getSimpleName()));
			}
		}
		createTable.append(", PRIMARY KEY (benchmark_id, timestamp)) ");

		final BoundStatement boundStatement = this.getBoundStatement(createTable.toString());
		
		try {
			this.session.execute(boundStatement);
		} catch (final NoHostAvailableException | QueryExecutionException | QueryValidationException | UnsupportedFeatureException exc) {
			LOGGER.error("Creating table {} failed! Cause: {}", tableName, exc.getLocalizedMessage());
		}

		try {
			final String addIndex = "INSERT INTO " + this.tablePrefix + " (tablename, classname) VALUES('"
					+ tableName + "','" + className + "')";
			final BoundStatement index = this.getBoundStatement(addIndex);
			this.session.execute(index);
		} catch (final NoHostAvailableException | QueryExecutionException | QueryValidationException | UnsupportedFeatureException exc) {
				LOGGER.error("Adding table {} to index failed!", tableName);
		}
	}

	/**
	 * Returns the highest found RecordId. If no record or matching table is found; 0 is returned.
	 *
	 * @return returns the highest known record id.
	 * @throws ConfigurationException on errors
	 */
	public int getLastRecordId() throws ConfigurationException {

		final List<String> tables = new ArrayList<>();
		int highestId = 0;

		// get all tables who belong to the current prefix
		final String statement = "select table_name from tables where keyspace_name ='" + this.defaultKeyspace + "'";
		final String searchKey = this.tablePrefix + "_";
		final BoundStatement boundedStatement = this.getBoundStatement(statement);
		final ResultSet rs = this.session.execute(boundedStatement);
		if (rs != null) {
			for (final Row row : rs) {
				final String table = row.getString("table_name");

				if (table.contains(searchKey)) {
					tables.add(table);
				}
			}
		}

		if (!tables.isEmpty()) {
			final String stmt = "select max(id) as id from ";

			for (final String s : tables) {
				final String tableLookup = stmt + s;
				final BoundStatement tableStmt = this.getBoundStatement(tableLookup);
				final ResultSet result = this.session.execute(tableStmt);
				for (final Row row : result) {
					final int id = row.getInt("id");

					if (id > highestId) {
						highestId = id;
					}
				}
			}
		}

		return highestId;
	}
	
	/**
	 * Insert one record.
	 * 
	 * @param boundStatement record contained in a boundStatement
	 * @throws NoHostAvailableException
	 * @throws QueryExecutionException
	 * @throws QueryValidationException
	 * @throws UnsupportedFeatureException
	 */
	public void insert(final BoundStatement boundStatement) 
			throws NoHostAvailableException, QueryExecutionException, QueryValidationException, UnsupportedFeatureException {
		this.session.execute(boundStatement);		
	}

}
