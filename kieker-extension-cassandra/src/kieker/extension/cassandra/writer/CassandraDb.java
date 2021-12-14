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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
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
import kieker.common.exception.MonitoringRecordException;

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

	private Cluster cluster;
	private final List<InetSocketAddress> contactPoints;
	private final String defaultKeyspace;
	private final String tablePrefix;
	private final boolean dropTables;
	private Session session;

	private List<PreparedStatement> preparedStatements;

	private Map<Class<?>, String> databaseTypes;

	/**
	 * Creates a new instance of this class using the given parameter.
	 *
	 * @param keyspace
	 * @param contactPointSpecs
	 * @param tablePrefix
	 * @param dropTables
	 * @param defaultId
	 * @param idType
	 * @throws Exception
	 */
	public CassandraDb(final String keyspace, final String[] contactPointSpecs, final String tablePrefix, final boolean dropTables) {
		this.contactPoints = new ArrayList<>();
		this.setDatabasetypes();
		this.defaultKeyspace = (keyspace == null) ? DEFAULT_KEY_SPACE : keyspace; // NOCS avoid inline conditionals
		this.tablePrefix = (tablePrefix == null) ? DEFAULT_TABLE_PREFIX : tablePrefix; // NOCS avoid inline conditionals
		this.dropTables = dropTables;
		this.initDatabase(contactPointSpecs);
	}

	/**
	 * Establishes a connection to the database.
	 *
	 * @return
	 */
	public boolean connect() {
		try {
			this.cluster = Cluster.builder().addContactPointsWithPorts(this.contactPoints)
					.withRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE)
					.withMaxSchemaAgreementWaitSeconds(60)
					.build();

			this.session = this.cluster.connect(this.getDefaultKeyspace());
			return true;
		} catch (final NoHostAvailableException | AuthenticationException | InvalidQueryException | IllegalStateException exc) {
			LOGGER.error("Opening Connection to Database failed. {}", exc.getLocalizedMessage());
			return false;
		}
	}

	public String getDefaultKeyspace() {
		return this.defaultKeyspace;
	}

	public String getTablePrefix() {
		return this.tablePrefix;
	}

	public boolean getDropTables() {
		return this.dropTables;
	}

	/**
	 * initializes the database connection.
	 *
	 * @param contactPointSpecs
	 */
	private void initDatabase(final String[] contactPointSpecs) {
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
	private void setDatabasetypes() {
		this.databaseTypes = new HashMap<>();

		// String
		this.databaseTypes.put(String.class, "text");

		// int
		this.databaseTypes.put(int.class, "int");
		this.databaseTypes.put(Integer.class, "int");

		// long
		this.databaseTypes.put(long.class, "bigint");
		this.databaseTypes.put(Long.class, "bigint");

		// float
		this.databaseTypes.put(float.class, "float");
		this.databaseTypes.put(Float.class, "float");

		// double
		this.databaseTypes.put(double.class, "double");
		this.databaseTypes.put(Double.class, "double");

		// short
		this.databaseTypes.put(short.class, "int");
		this.databaseTypes.put(Short.class, "int");

		// boolean
		this.databaseTypes.put(boolean.class, "boolean");
		this.databaseTypes.put(Boolean.class, "boolean");

		// char
		this.databaseTypes.put(char.class, "varchar");
	}

	/**
	 * Closes the cluster.
	 */
	private void close() {
		if ((this.cluster != null) && !this.cluster.isClosed()) {
			this.cluster.close();
		}
	}

	/**
	 * Closes the Session.
	 */
	private void closeSession() {
		if ((this.session != null) && !this.session.isClosed()) {
			this.session.close();
		}
	}

	/**
	 * Executes the given statement. Returns a ResultSet if the call was successful.
	 *
	 * @param statement
	 * @return
	 */
	private ResultSet execute(final BoundStatement statement) {
		ResultSet rs = null;
		try {
			rs = session.execute(statement);
		} catch (final NoHostAvailableException | QueryExecutionException | QueryValidationException | UnsupportedFeatureException exc) {
			LOGGER.error("Error executing statement: {}", exc.getLocalizedMessage());
		}

		return rs;
	}

	/**
	 * Executes the given statement asynchronous. Returns a ResultSetFuture if the call was successful.
	 *
	 * @param statement
	 * @param ignoreFuture
	 *            ignore Future if return is not important
	 * @return
	 */
	private ResultSetFuture executeAsync(final BoundStatement statement, final boolean ignoreFuture) {
		ResultSetFuture rs = null;
		try {
			rs = session.executeAsync(statement);
			if (ignoreFuture) {
				rs.cancel(true);
			}
		} catch (final UnsupportedFeatureException exc) {
			LOGGER.error("Error executing statement: {}", exc.getLocalizedMessage());
		}

		return rs;
	}

	public BoundStatement createBoundStatement(final String classname) {
		final PreparedStatement preparedStatement = this.session.prepare("STRING-QUERY");
		return new BoundStatement(preparedStatement);
	}
	
	/**
	 * Returns a BoundStatement from the given String. Uses the default Session to the keyspace.
	 *
	 * @param statement
	 * @return
	 */
	private BoundStatement getBoundStatement(final String statement) {
		LOGGER.debug("Statement: {}", statement);

		if (this.preparedStatements == null) {
			this.preparedStatements = new ArrayList<>();
		}

		PreparedStatement ps = null;

		for (final PreparedStatement cached : this.preparedStatements) {
			if (cached.getQueryString().equals(statement)) {
				ps = cached;
			}
		}

		if (ps == null) {
			ps = session.prepare(statement);
			this.preparedStatements.add(ps);
		}

		return new BoundStatement(ps);
	}

	/**
	 * Creates the index Table.
	 *
	 * @throws ConfigurationException
	 */
	public void createIndexTable() throws ConfigurationException {
		boolean tableExists = false;

		if (this.getDropTables()) {
			final String dropStatement = "DROP TABLE " + this.getTablePrefix();

			final BoundStatement bs = this.getBoundStatement(dropStatement);
			if (this.execute(bs) == null) {
				LOGGER.warn("Dropping table {} failed.", this.getTablePrefix());
			}
		} else {
			final String selectStatement = "SELECT * FROM " + this.getTablePrefix();
			final BoundStatement bs = this.getBoundStatement(selectStatement);
			final ResultSet rs = this.execute(bs);
			if (rs != null) {
				tableExists = true;
			}
		}

		if (!tableExists) {
			final String createStatement = "CREATE TABLE "
					+ this.getTablePrefix()
					+ "  ( "
					+ "tablename text, "
					+ "classname text, "
					+ "PRIMARY KEY (tablename) "
					+ ")";
			final BoundStatement bs = this.getBoundStatement(createStatement);
			if (this.execute(bs) == null) {
				LOGGER.error("Creating index table {} failed!", this.getTablePrefix());
				throw new ConfigurationException("Creating index table " + this.getTablePrefix() + " failed!");
			}
		}
	}

	/**
	 * Closes all open connections to the database.
	 */
	public void disconnect() {
		this.closeSession();
		this.close();
	}

	/**
	 * Creates a table in the keyspace with the given parameters.
	 *
	 * @param className
	 * @param columns
	 * @return a string
	 * @throws ConfigurationException on errors
	 */
	public String createTable(final String className, final Class<?>... columns) throws ConfigurationException {
		boolean tableExists = false;
		final String tablename = this.getTablePrefix() + "_" + className;

		if (this.getDropTables()) {
			final String dropStatement = "DROP TABLE " + tablename;

			final BoundStatement bs = this.getBoundStatement(dropStatement);
			if (this.execute(bs) == null) {
				LOGGER.warn("Dropping table {} failed.", tablename);
			}
		} else {
			final String selectStatement = "SELECT * FROM " + tablename;
			final BoundStatement bs = this.getBoundStatement(selectStatement);
			final ResultSet rs = this.execute(bs);
			if (rs != null) {
				tableExists = true;
			}
		}

		if (!tableExists) {
			final StringBuilder createTable = new StringBuilder(100);
			// currentString CREATE TABLE $classname (
			createTable.append("CREATE TABLE ").append(tablename).append(" (");
			// currentString CREATE TABLE $classname ( id bigint,
			createTable.append("benchmark_id ").append(this.databaseTypes.get(String.class));
			// currentString CREATE TABLE $classname ( id bigint, timestamp bigint
			createTable.append(", ").append("timestamp ").append(this.databaseTypes.get(long.class));

			int i = 1;
			for (final Class<?> c : columns) {
				createTable.append(", c").append(i++).append(' ');
				final String databaseType = this.databaseTypes.get(c);

				if (databaseType != null) {
					createTable.append(databaseType);
				} else {
					throw new ConfigurationException(String.format("Type '%s' not supported.", c.getSimpleName()));
				}

			}
			createTable.append(", PRIMARY KEY (benchmark_id, timestamp)) ");

			final BoundStatement bs = this.getBoundStatement(createTable.toString());
			if (this.execute(bs) == null) {
				LOGGER.error("Creating table {} failed!", this.getTablePrefix());
				throw new ConfigurationException("Creating table " + this.getTablePrefix() + " failed!");
			} else {
				final String addIndex = "INSERT INTO " + this.getTablePrefix() + " (tablename, classname) VALUES('"
						+ tablename + "','" + className + "')";
				final BoundStatement index = this.getBoundStatement(addIndex);
				if (this.execute(index) == null) {
					LOGGER.error("Adding table {} to index failed!", this.getTablePrefix());
					throw new ConfigurationException("Adding table " + this.getTablePrefix() + " to index failed!");
				}
			}
		}

		return tablename;
	}

	/**
	 * Inserts the given statement into the database.
	 *
	 * @param statement
	 * @param boundStatement
	 * @throws Exception
	 */
	public void insert(final String statement, final BoundStatement boundStatement) throws MonitoringRecordException, ConfigurationException {
		final BoundStatement bs = this.getBoundStatement(statement);

		if (this.execute(bs) == null) {
			throw new MonitoringRecordException("Error inserting monitoring data.");
		}
	}

	/**
	 * Returns the highest found RecordId. If no record or matching table is found; 0 is returned.
	 *
	 * @return
	 * @throws Exception
	 */
	public int getLastRecordId() throws ConfigurationException {

		final List<String> tables = new ArrayList<>();
		int highestId = 0;

		// get all tables who belong to the current prefix
		final String statement = "select table_name from tables where keyspace_name ='" + this.getDefaultKeyspace() + "'";
		final String searchKey = this.getTablePrefix() + "_";
		final BoundStatement bs = this.getBoundStatement(statement);
		final ResultSet rs = this.execute(bs);
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
				final ResultSet result = this.execute(tableStmt);
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

}
