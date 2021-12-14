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
package kieker.extension.cassandra.reader;

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

	/** Fields **/
	private Cluster cluster;
	private List<InetSocketAddress> contactPoints;
	private String defaultKeyspace;
	private boolean init;
	private Session session;
	private final String cassandraDefaultHost = "127.0.0.1";
	private final String cassandraDefaultPort = "9042";
	private List<PreparedStatement> preparedStatements;

	private Map<Class<?>, String> databaseTypes;

	/**
	 * Creates a new instance of this class using the given parameter.
	 *
	 * @param keyspace
	 * @param contactpoints
	 * @throws Exception
	 */
	public CassandraDb(final String keyspace, final String[] contactpoints) {
		this.initDefault();
		this.initDatabase(keyspace, contactpoints);
	}

	/**
	 * Establishes a connection to the database
	 *
	 * @return
	 */
	public void connect() {
		try {
			this.closeSession();
			this.close();

			this.cluster = Cluster.builder().addContactPointsWithPorts(this.contactPoints)
					.withRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE)
					.withMaxSchemaAgreementWaitSeconds(60)
					.build();

			this.session = this.cluster.connect(this.getDefaultKeyspace());
			this.init = true;
		} catch (final NoHostAvailableException | AuthenticationException | InvalidQueryException | IllegalStateException exc) {
			LOGGER.error("Opening Connection to Database failed. {}", exc.getLocalizedMessage());
			this.init = false;
		}
	}
	
	public void setDefaultKeyspace(final String defaultKeyspace) {
		this.defaultKeyspace = defaultKeyspace;
	}

	public String getDefaultKeyspace() {
		return this.defaultKeyspace;
	}

	public boolean isInit() {
		return this.init;
	}

	/**
	 * Sets the default values for all fields
	 */
	private void initDefault() {
		this.contactPoints = new ArrayList<>();
		final InetSocketAddress iA = new InetSocketAddress("127.0.0.1", 9042);
		this.contactPoints.add(iA);
		this.defaultKeyspace = "kieker";
		this.setDatabasetypes();
	}
	
	/**
	 * initializes the database connection
	 *
	 * @param keyspace
	 * @param contactpoints
	 * @param tablePrefix
	 * @param dropTables
	 */
	private void initDatabase(final String keyspace, final String[] contactpoints) {
		if ((keyspace != null) && !keyspace.isEmpty()) {
			this.defaultKeyspace = keyspace;
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
	 * @throws ConfigurationException
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
	 * @throws ConfigurationException
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
	 * Executes the given statement. Returns a ResultSet if the call was successful.
	 *
	 * @param statement
	 * @param session2
	 * @return
	 * @throws ConfigurationException
	 */
	private ResultSet execute(final BoundStatement statement, final Session session2) throws ConfigurationException {
		if (!this.init) {
			throw new ConfigurationException("Connection not established.");
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

		final BoundStatement bs = new BoundStatement(ps);
		bs.setFetchSize(10000);
		return bs;
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
	 * Closes all open connections to the database
	 */
	public void disconnect() {
		this.closeSession();
		this.close();
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
	public void insertAsync(final String statement, final Collection<Object> values, final boolean ignoreFuture) throws ConfigurationException {
		final BoundStatement bs = this.getBoundStatement(statement);

		int i = 0;
		for (final Object value : values) {
			this.setParam(bs, value, i);
			i++;
		}

		if ((this.executeAsync(bs, ignoreFuture) == null) && !ignoreFuture) {
			throw new ConfigurationException("Error inserting monitoring data");
		}
	}

	/**
	 * Select data form the Cassandra cluster
	 *
	 * @param fields
	 * @param table
	 * @param whereClause
	 * @return
	 * @throws Exception
	 */
	public ResultSet select(final String fields, final String table, final String whereClause) throws Exception {
		return this.select(fields, ";", table, whereClause);
	}

	/**
	 * Select data form the Cassandra cluster
	 *
	 * @param fields
	 * @param delimiter
	 * @param table
	 * @param whereClause
	 * @return
	 * @throws ConfigurationException
	 */
	public ResultSet select(final String fields, final String delimiter, final String table, final String whereClause) throws ConfigurationException {
		final String[] array = fields.split(delimiter);
		final List<String> list = Arrays.asList(array);

		return this.select(list, table, whereClause);
	}

	/**
	 * Select data form the Cassandra cluster
	 *
	 * @param fields
	 * @param table
	 * @param whereClause
	 * @return
	 * @throws Exception
	 */
	public ResultSet select(final List<String> fields, final String table, final String whereClause) throws ConfigurationException {
		String statement = "select ";

		if ((fields != null) && !fields.isEmpty()) {
			for (final String s : fields) {
				statement += s + ",";
			}

			statement = statement.substring(0, statement.length() - 1);
		} else {
			statement += "*";
		}

		statement += " from " + table;

		if ((whereClause != null) && !whereClause.isEmpty()) {
			statement += " where " + whereClause;
		}

		final BoundStatement bs = this.getBoundStatement(statement);

		return this.execute(bs);
	}
}
