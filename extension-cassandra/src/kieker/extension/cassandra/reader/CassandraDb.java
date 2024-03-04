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

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.internal.core.cql.DefaultBoundStatement;

import kieker.common.exception.ConfigurationException;

/**
 *
 * @author Armin Moebius, Sven Ulrich
 *
 */
public class CassandraDb {

	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraDb.class);

	private final List<InetSocketAddress> contactPoints;
	private final String keyspace;
	private CqlSession session;
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
			this.session = CqlSession.builder().build();
		} catch (final Exception exc) {
			LOGGER.error("Opening Connection to Database failed. {}", exc.getLocalizedMessage());
		}
	}

	/**
	 * Closes all open sessions and connections to the database.
	 */
	public void disconnect() {
		this.session.close();
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

		if ((fields != null) && !fields.isEmpty()) {
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

		if ((whereClause != null) && !whereClause.isEmpty()) {
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

		final BoundStatement boundStatement = new DefaultBoundStatement(preparedStatement, null, null, statement, null, null, null, null, null, null, false, 0, null,
				0, null, null, null, null, null, null);

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
		} catch (final Exception exc) {
			LOGGER.error("Error executing statement: {}", exc.getLocalizedMessage());
		}

		return resultSet;
	}
}
