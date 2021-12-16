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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.IMonitoringRecord;
import kieker.extension.cassandra.CassandraUtils;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 *
 * @author Armin Moebius, Sven Ulrich, Reiner Jung
 * @since 1.16
 */
public class CassandraDbWriter extends AbstractMonitoringWriter { // NOPMD DataClass
	private static final String PREFIX = CassandraDbWriter.class.getName() + ".";

	public static final String CONFIG_KEYSPACE = PREFIX + "keyspace";
	public static final String CONFIG_CONTACTPOINTS = PREFIX + "contactpoints";
	public static final String CONFIG_TABLE_PREFIX = PREFIX + "tablePrefix";
	public static final String CONFIG_OVERWRITE = PREFIX + "dropTables";
	public static final String CONFIG_BENCHMARK_ID = PREFIX + "benchmarkId";
	
	private static final String DEFAULT_KEYSPACE = "kieker";

	private static final String DEFAULT_TABLE_PREFIX = "kieker";

	
	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraDbWriter.class);

	private final CassandraDb database;
	private final String benchmarkId;

	/**
	 * Creates a new instance of this class using the given parameter.
	 *
	 * @param configuration
	 * @throws ConfigurationException
	 * @throws Exception
	 */
	public CassandraDbWriter(final Configuration configuration) {
		super(configuration);
		final String keyspace = configuration.getStringProperty(CONFIG_KEYSPACE, DEFAULT_KEYSPACE);
		final String[] contactPointParameters = configuration.getStringArrayProperty(CONFIG_CONTACTPOINTS, ",");
		final String tablePrefix = configuration.getStringProperty(CONFIG_TABLE_PREFIX, DEFAULT_TABLE_PREFIX);
		final boolean dropTables = configuration.getBooleanProperty(CONFIG_OVERWRITE);
		this.benchmarkId = configuration.getStringProperty(CONFIG_BENCHMARK_ID);
		
		this.database = new CassandraDb(keyspace, CassandraUtils.computeDatabaseConnections(contactPointParameters), tablePrefix, dropTables);
	}

	@Override
	public void onStarting() {
		this.database.connect();
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord record) {
		try {
			this.database.insert(record, benchmarkId);
		} catch (MonitoringRecordException e) {
			LOGGER.error("Error inserting monitoring record: {}", e.getLocalizedMessage());
		}
	}

	@Override
	public void onTerminating() {
		this.database.disconnect();
	}


}
