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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
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
 * @author Armin Moebius, Sven Ulrich
 *
 */
public class CassandraDb {

	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraDb.class);

	private Cluster cluster;
	private final List<InetSocketAddress> contactPoints;
	private final String keyspace;
	private Session session;
	private final Map<String, PreparedStatement> preparedStatements = new ConcurrentHashMap<>();

	/**
	 * Creates a new instance of this class using the given parameter.
	 *
	 * @param keyspace
	 * @param contactPoints
	 */
	public CassandraDb(final String keyspace, final List<InetSocketAddress> contactPoints) {
		this.contactPoints = contactPoints;
		this.keyspace = keyspace;
	}

	/**
	 * Establishes a connection to the database.
	 *
	 * @return
	 */
	public void connect() {
		try {
			this.cluster = Cluster.builder().addContactPointsWithPorts(this.contactPoints)
					.withRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE)
					.withMaxSchemaAgreementWaitSeconds(60)
					.build();

			this.session = this.cluster.connect(this.keyspace);
		} catch (final NoHostAvailableException | AuthenticationException | InvalidQueryException | IllegalStateException exc) {
			LOGGER.error("Opening Connection to Database failed. {}", exc.getLocalizedMessage());
		}
	}

	/**
	 * Closes all open sessions and connections to the database.
	 */
	public void disconnect() {
		this.session.close();
		this.cluster.close();
	}

	/**
	 * Select data form the Cassandra cluster.
	 *
	 * @param fields
	 * @param table
	 * @param whereClause
	 * @return returns a result set
	 */
	public ResultSet select(final List<String> fields, final String table, final String whereClause) {
		final StringBuilder statement = new StringBuilder(200);
		statement.append("SELECT ");

		if (fields != null && !fields.isEmpty()) {
			for (final String s : fields) {
				statement.append(s);
				statement.append(',');
			}
			statement.deleteCharAt(statement.length() - 1);
		} else {
			statement.append('*');
		}

		statement.append(" FROM ");
		statement.append(table);

		if (whereClause != null && !whereClause.isEmpty()) {
			statement.append(" WHERE ");
			statement.append(whereClause);
		}

		return this.execute(this.makeBoundStatement(statement.toString()));
	}

	/**
	 * Returns a BoundStatement from the given String. Uses the given Session.
	 *
	 * @param statement
	 * @return
	 */
	private BoundStatement makeBoundStatement(final String statement) {
		PreparedStatement preparedStatement = this.preparedStatements.get(statement);

		if (preparedStatement == null) {
			preparedStatement = this.session.prepare(statement);
			this.preparedStatements.put(statement, preparedStatement);
		}

		final BoundStatement boundStatement = new BoundStatement(preparedStatement);
		boundStatement.setFetchSize(10000);

		return boundStatement;
	}

	/**
	 * Executes the given statement. Returns a ResultSet if the call was successful.
	 *
	 * @param statement
	 * @return a result set
	 * @throws ConfigurationException
	 *             on error
	 */
	private ResultSet execute(final BoundStatement statement) {
		ResultSet resultSet = null;
		try {
			resultSet = this.session.execute(statement);
		} catch (final NoHostAvailableException | QueryExecutionException | QueryValidationException | UnsupportedFeatureException exc) {
			LOGGER.error("Error executing statement: {}", exc.getLocalizedMessage());
		}

		return resultSet;
	}
}
