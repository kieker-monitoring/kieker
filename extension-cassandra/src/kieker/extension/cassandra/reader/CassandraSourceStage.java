/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
import java.util.List;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import kieker.common.exception.ConfigurationException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.extension.cassandra.CassandraUtils;
import kieker.extension.cassandra.CassandraValueDeserializer;

import teetime.framework.AbstractProducerStage;

/**
 * Reader from Cassandra DB.
 *
 * @author Armin Moebius, Sven Ulrich, Reiner Jung
 * @since 1.16
 */
public class CassandraSourceStage extends AbstractProducerStage<IMonitoringRecord> {

	private final CachedRecordFactoryCatalog recordFactories = CachedRecordFactoryCatalog.getInstance();

	private final String keyspace;
	private final List<InetSocketAddress> contactPoints;
	private final String tablePrefix;

	public CassandraSourceStage(final String keyspace, final String[] contactPoints, final String tablePrefix) {
		this.keyspace = keyspace;
		this.contactPoints = CassandraUtils.computeDatabaseConnections(contactPoints);

		this.tablePrefix = tablePrefix;
	}

	@Override
	protected void execute() throws Exception {
		CassandraDb database = null;
		try {
			database = new CassandraDb(this.keyspace, this.contactPoints);
			database.connect();

			// index table
			final ResultSet rs = database.select(new ArrayList<>(), this.tablePrefix, null);
			for (Row r : rs) {
				final String tablename = r.getString(1);
				final String classname = r.getString(2);

				this.table2record(database, tablename, classname);
			}
		} catch (final ConfigurationException exc) {
			this.logger.error(exc.getMessage());
		} finally {
			if (database != null) {
				database.disconnect();
			}
			this.logger.info("{} shutdown", this.getClass().getSimpleName());
			this.workCompleted();
		}
	}

	/**
	 * This method uses the given table to read records and sends them to the output port.
	 *
	 * @param database
	 *            Connection to the database
	 * @param tablename
	 *            Table which contains the records
	 * @param clazz
	 *            Class of the monitoring records
	 * @throws Exception
	 */
	private void table2record(final CassandraDb database, final String tablename, final String eventTypeName) throws ConfigurationException {
		final ResultSet rs = database.select(new ArrayList<>(), tablename, null);

		for (final Row row : rs) {
			if ((rs.getAvailableWithoutFetching() == 10000) && !rs.isFullyFetched()) {
				rs.fetchMoreResults();
			}

			if (row != null) {
				final CassandraValueDeserializer deserializer = new CassandraValueDeserializer(row);
				final int loggingTimestamp = deserializer.getInt();

				final IRecordFactory<? extends IMonitoringRecord> eventTypeFactory = this.recordFactories.get(eventTypeName);
				if (eventTypeFactory == null) {
					this.logger.error("Class type {} was not found. Cannot instantiate event type.", eventTypeName);
				} else {
					final IMonitoringRecord record = eventTypeFactory.create(deserializer);
					record.setLoggingTimestamp(loggingTimestamp);
					this.outputPort.send(record);
				}
			}
		}
	}

}
