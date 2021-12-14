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
import java.util.Arrays;
import java.util.Collection;
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
 * @author Armin Moebius, Sven Ulrich
 *
 */
public class CassandraDb {

	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraDb.class);

	private Cluster cluster;
	private List<InetSocketAddress> contactPoints;
	private String defaultKeyspace;
	private String tablePrefix;
	private boolean dropTables;
	private boolean init = false;
	private Session session;
	private final String cassandraDefaultHost = "127.0.0.1";
	private final String cassandraDefaultPort = "9042";
	private List<PreparedStatement> preparedStatements;

	private Map<Class<?>, String> databaseTypes;

	/** Constructor **/

	/**
	 * Creates a new instance of this class
	 */
	public CassandraDb() {
		this.initDefault();
	}

	/**
	 * Creates a new instance of this class using the given parameter
	 *
	 * @param keyspace
	 * @param contactpoints
	 * @param tablePrefix
	 * @param dropTables
	 * @param defaultId
	 * @param idType
	 * @throws Exception
	 */
	public CassandraDb(final String keyspace, final String[] contactpoints, final String tablePrefix, final boolean dropTables) throws Exception {
		this();
		this.initDatabase(keyspace, contactpoints, tablePrefix, dropTables);
	}

	/** Setter **/

	public void setDefaultKeyspace(final String defaultKeyspace) {
		this.defaultKeyspace = defaultKeyspace;
	}

	public void setTablePrefix(final String prefix) {
		this.tablePrefix = prefix;
	}

	public void setDropTables(final boolean drop) {
		this.dropTables = drop;
	}

	/** Getter **/

	public String getDefaultKeyspace() {
		return this.defaultKeyspace;
	}

	public String getTablePrefix() {
		return this.tablePrefix;
	}

	public boolean getDropTables() {
		return this.dropTables;
	}

	public boolean isInit() {
		return this.init;
	}

	/** private **/
	/**
	 * Sets the default values for all fields
	 */
	private void initDefault() {
		this.contactPoints = new ArrayList<>();
		final InetSocketAddress iA = new InetSocketAddress("127.0.0.1", 9042);
		this.contactPoints.add(iA);
		this.setDefaultKeyspace("kieker");
		this.setTablePrefix("kieker");
		this.setDatabasetypes();
	}

	/**
	 * Prepares the mapping from Java Types to Cassandra Types
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
	 * Establishes a connection to the database
	 *
	 * @return
	 */
	private boolean connect() {
		boolean result = false;

		try {
			/**
			 * PoolingOptions poolingOpts = new PoolingOptions();
			 * poolingOpts.setCoreConnectionsPerHost(HostDistance.REMOTE, 2);
			 * poolingOpts.setMaxConnectionsPerHost(HostDistance.REMOTE, 200);
			 **/
			this.closeSession();
			this.close();

			this.cluster = Cluster.builder().addContactPointsWithPorts(this.contactPoints)
					.withRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE)
					.withMaxSchemaAgreementWaitSeconds(60)
					.build();

			this.session = this.cluster.connect(this.getDefaultKeyspace());
			result = true;
		} catch (final NoHostAvailableException | AuthenticationException | InvalidQueryException | IllegalStateException exc) {
			result = false;
			LOGGER.error("Opening connection to database failed: {}", exc.getLocalizedMessage());
		}

		return result;
	}

	/**
	 * Closes the cluster
	 */
	private void close() {
		if ((this.cluster != null) && !this.cluster.isClosed()) {
			this.cluster.close();
		}
	}

	/**
	 * Closes the Session
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
	 * @throws Exception
	 */
	private ResultSet execute(final BoundStatement statement) throws ConfigurationException {
		return this.execute(statement, this.session);
	}

	/**
	 * Executes the given statement asynchronous. Returns a ResultSetFuture if the call was successful.
	 *
	 * @param statement
	 * @param ignoreFuture
	 *            ignore Future if return is not important
	 * @return
	 * @throws Exception
	 */
	private ResultSetFuture executeAsync(final BoundStatement statement, final boolean ignoreFuture) throws ConfigurationException {
		return this.executeAsync(statement, this.session, ignoreFuture);
	}

	/**
	 * Executes the given statement asynchronous. Returns a ResultSetFuture if the call was succesfull
	 *
	 * @param statement
	 * @param session2
	 * @param ignoreFuture
	 *            ignore Future if return is not important
	 * @return
	 * @throws ConfigurationException
	 */
	private ResultSetFuture executeAsync(final BoundStatement statement, final Session session2, final boolean ignoreFuture) throws ConfigurationException {
		if (!this.init) {
			throw new ConfigurationException("Connection not established");
		}

		ResultSetFuture rs = null;
		try {
			rs = session2.executeAsync(statement);
			if (ignoreFuture) {
				rs.cancel(true);
			}
		} catch (final UnsupportedFeatureException exc) {
			LOGGER.error("Error executing statement: {}", exc.getLocalizedMessage());
		}

		return rs;
	}

	/**
	 * Executes the given statement. Returns a ResultSet if the call was successfull
	 *
	 * @param statement
	 * @param session2
	 * @return
	 * @throws ConfigurationException
	 */
	private ResultSet execute(final BoundStatement statement, final Session session2) throws ConfigurationException {
		if (!this.init) {
			throw new ConfigurationException("Connection not established; please establish a Connection first");
		}

		ResultSet rs = null;
		try {
			rs = session2.execute(statement);
		} catch (final NoHostAvailableException | QueryExecutionException | QueryValidationException | UnsupportedFeatureException exc) {
			LOGGER.error("Error executing statement: {}", exc.getLocalizedMessage());
		}

		return rs;
	}

	/**
	 * Returns a BoundStatement from the given String. Uses the default Session to the keyspace.
	 *
	 * @param statement
	 * @return
	 * @throws ConfigurationException
	 */
	private BoundStatement getBoundStatement(final String statement) throws ConfigurationException {
		return this.getBoundStatement(statement, this.session);
	}

	/**
	 * Returns a BoundStatement from the given String. Uses the given Session.
	 *
	 * @param statement
	 * @param session2
	 * @return
	 * @throws ConfigurationException
	 */
	private BoundStatement getBoundStatement(final String statement, final Session session2) throws ConfigurationException {
		LOGGER.debug("Statement: {}", statement);

		if (!this.init) {
			throw new ConfigurationException("Connection not established");
		}

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
			ps = session2.prepare(statement);
			this.preparedStatements.add(ps);
		}

		return new BoundStatement(ps);
	}

	/**
	 * Sets the param in the BoundStatement
	 *
	 * @param bs
	 * @param value
	 * @param index
	 */
	private void setParam(final BoundStatement bs, final Object value, final int index) {
		if (value instanceof String) {
			bs.setString(index, (String) value);
		} else if (value instanceof Integer) {
			bs.setInt(index, (Integer) value);
		} else if (value instanceof Long) {
			bs.setLong(index, (Long) value);
		} else if (value instanceof Float) {
			bs.setFloat(index, (Float) value);
		} else if (value instanceof Double) {
			bs.setDouble(index, (Double) value);
		} else if (value instanceof Short) {
			bs.setShort(index, (Short) value);
		} else if (value instanceof Boolean) {
			bs.setBool(index, (Boolean) value);
		}
	}

	/** public **/

	/**
	 * initializes the database connection
	 *
	 * @param keyspace
	 * @param contactpoints
	 * @param tablePrefix2
	 * @param dropTables2
	 * @throws Exception
	 */
	public void initDatabase(final String keyspace, final String[] contactpoints, final String tablePrefix2, final boolean dropTables2) {
		if ((keyspace != null) && !keyspace.isEmpty()) {
			this.setDefaultKeyspace(keyspace);
		}

		if ((contactpoints != null) && (contactpoints.length > 0)) {
			this.contactPoints.clear();

			for (final String contactpoint : contactpoints) {
				final String[] array = contactpoint.split(":");
				final List<String> list = Arrays.asList(array);

				String host = this.cassandraDefaultHost;
				String port = this.cassandraDefaultPort;

				int index = 1;
				for (final String s : list) {
					if (index == 1) {
						host = s;
					} else if (index == 2) {
						port = s;
					}
					index++;
				}

				final InetSocketAddress iA = new InetSocketAddress(host, Integer.parseInt(port));
				this.contactPoints.add(iA);
			}
		}

		if ((tablePrefix2 != null) && !tablePrefix2.isEmpty()) {
			this.setTablePrefix(tablePrefix2);
		}

		this.setDropTables(dropTables2);

		this.init = this.connect();
	}

	/**
	 * Creates the index Table
	 *
	 * @throws Exception
	 */
	public void createIndexTable() throws Exception {
		boolean tableExists = false;

		if (!this.init) {
			throw new ConfigurationException("Connection not established.");
		}

		if (this.getDropTables()) {
			final String dropStatement = "DROP TABLE " + this.getTablePrefix();

			final BoundStatement bs = this.getBoundStatement(dropStatement);
			if (this.execute(bs) == null) {
				LOGGER.warn("Dropping table " + this.getTablePrefix() + " failed; Maybe table does not exist");
			}
		} else {
			try {
				final String selectStatement = "SELECT * FROM " + this.getTablePrefix();
				final BoundStatement bs = this.getBoundStatement(selectStatement);
				final ResultSet rs = this.execute(bs);
				if (rs != null) {
					tableExists = true;
				}
			} catch (final ConfigurationException exc) {
				LOGGER.warn("Looking for table " + this.getTablePrefix() + " failed; Maybe table does not exist");
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
				LOGGER.error("Creating index table " + this.getTablePrefix() + " failed!");
				throw new ConfigurationException("Creating index table " + this.getTablePrefix() + " failed!");
			}
		}
	}

	/**
	 * Closes all open connections to the database
	 */
	public void disconnect() {
		this.closeSession();
		this.close();
	}

	/**
	 * Creates a table in the keyspace with the given parameters
	 *
	 * @param className
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public String createTable(final String className, final Class<?>... columns) throws ConfigurationException {
		boolean tableExists = false;
		final String tablename = this.getTablePrefix() + "_" + className;

		if (!this.init) {
			throw new ConfigurationException("Connection not established.");
		}

		if (this.getDropTables()) {
			final String dropStatement = "DROP TABLE " + tablename;

			final BoundStatement bs = this.getBoundStatement(dropStatement);
			if (this.execute(bs) == null) {
				LOGGER.warn("Dropping table {} failed.", tablename);
			}
		} else {
			try {
				final String selectStatement = "SELECT * FROM " + tablename;
				final BoundStatement bs = this.getBoundStatement(selectStatement);
				final ResultSet rs = this.execute(bs);
				if (rs != null) {
					tableExists = true;
				}
			} catch (final ConfigurationException exc) {
				LOGGER.warn("Looking for table {} failed.", tablename);
			}

		}

		if (!tableExists) {
			final StringBuilder createTable = new StringBuilder();
			// currentString CREATE TABLE $classname (
			createTable.append("CREATE TABLE ").append(tablename).append(" (");
			// currentString CREATE TABLE $classname ( id bigint,
			createTable.append("benchmark_id ").append(this.databaseTypes.get(String.class));
			// currentString CREATE TABLE $classname ( id bigint, timestamp bigint
			createTable.append(", ").append("timestamp ").append(this.databaseTypes.get(long.class));

			int i = 1;
			for (final Class<?> c : columns) {
				createTable.append(", c").append(i++).append(" ");
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
				LOGGER.error("Creating table " + this.getTablePrefix() + " failed!");
				throw new ConfigurationException("Creating table " + this.getTablePrefix() + " failed!");
			} else {
				final String addIndex = "INSERT INTO " + this.getTablePrefix() + " (tablename, classname) VALUES('" + tablename + "','" + className + "')";
				final BoundStatement index = this.getBoundStatement(addIndex);
				if (this.execute(index) == null) {
					LOGGER.error("Adding table " + this.getTablePrefix() + " to index failed!");
					throw new ConfigurationException("Adding table " + this.getTablePrefix() + " to index failed!");
				}
			}
		}

		return tablename;
	}

	/**
	 * Inserts the given statement into the database
	 *
	 * @param statement
	 * @param values
	 * @throws Exception
	 */
	public void insert(final String statement, final Collection<Object> values) throws MonitoringRecordException, ConfigurationException {
		final BoundStatement bs = this.getBoundStatement(statement);

		int i = 0;
		for (final Object value : values) {
			this.setParam(bs, value, i);
			i++;
		}

		if (this.execute(bs) == null) {
			throw new MonitoringRecordException("Error inserting monitoring data");
		}
	}

	/**
	 * Inserts the given statement into the database asynchronous
	 *
	 * @param statement
	 * @param values
	 * @param ignoreFuture
	 * @throws Exception
	 */
	public void insertAsync(final String statement, final Collection<Object> values, final boolean ignoreFuture) throws Exception {
		final BoundStatement bs = this.getBoundStatement(statement);

		int i = 0;
		for (final Object value : values) {
			this.setParam(bs, value, i);
			i++;
		}

		if ((this.executeAsync(bs, ignoreFuture) == null) && !ignoreFuture) {
			throw new Exception("Error inserting monitoring data");
		}
	}

	/**
	 * Returns the highest found RecordId. If no record or matching table is found; 0 is returned.
	 *
	 * @return
	 * @throws Exception
	 */
	public int getLastRecordId() throws Exception {
		if (!this.init) {
			throw new Exception("Connection not established; please establish a Connection first");
		}

		final List<String> tables = new ArrayList<>();
		int highestId = 0;

		// get all tables who belong to the current prefix
		final Session sysSession = this.cluster.connect("system_schema");
		final String statement = "select table_name from tables where keyspace_name ='" + this.getDefaultKeyspace() + "'";
		final String searchKey = this.getTablePrefix() + "_";
		final BoundStatement bs = this.getBoundStatement(statement, sysSession);
		final ResultSet rs = this.execute(bs, sysSession);
		if (rs != null) {
			for (final Row row : rs) {
				final String table = row.getString("table_name");

				if (table.contains(searchKey)) {
					tables.add(table);
				}
			}
		}
		sysSession.close();

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
