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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.BoundStatement;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueSerializer;
import kieker.extension.cassandra.CassandraValueSerializer;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 *
 * @author Armin Moebius, Sven Ulrich, Reiner Jung
 * @since 1.16
 */
public class CassandraDbWriter extends AbstractMonitoringWriter { // NOPMD DataClass
	private static final String PREFIX = CassandraDbWriter.class.getName() + ".";

	public static final String CONFIG_KEYSPACE = PREFIX + "Keyspace";
	public static final String CONFIG_CONTACTPOINTS = PREFIX + "Contactpoints";
	public static final String CONFIG_TABLEPREFIX = PREFIX + "TablePrefix";
	public static final String CONFIG_OVERWRITE = PREFIX + "DropTables";
	public static final String CONFIG_BENCHMARKID = PREFIX + "BenchmarkId";

	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraDbWriter.class);

	private final ConcurrentHashMap<String, String> classes = new ConcurrentHashMap<>();

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
		final String keyspace = configuration.getStringProperty(CONFIG_KEYSPACE);
		final String[] contactpoints = configuration.getStringArrayProperty(CONFIG_CONTACTPOINTS, ";");
		final String tableprefix = configuration.getStringProperty(CONFIG_TABLEPREFIX);
		final boolean dropTables = configuration.getBooleanProperty(CONFIG_OVERWRITE);
		this.benchmarkId = configuration.getStringProperty(CONFIG_BENCHMARKID);

		this.database = new CassandraDb(keyspace, contactpoints, tableprefix, dropTables);
	}

	@Override
	public void onStarting() {
		if (this.database.connect()) {
			try {
				this.database.createIndexTable();
			} catch (ConfigurationException ex) {
				// This is a temporary measure. There should be no exception 
			}
		}
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord record) {
		final Class<? extends IMonitoringRecord> recordClass = record.getClass();
		final String className = recordClass.getSimpleName();

		if (!this.classes.containsKey(className)) {
			Class<?>[] typeArray = null;
			try {
				typeArray = AbstractMonitoringRecord.typesForClass(recordClass);
			} catch (final MonitoringRecordException exc) {
				LOGGER.error("Failed to get types of record: {}", exc.getLocalizedMessage());
			}

			try {
				final String tableName = this.database.createTable(className, typeArray);
				final StringBuilder values = new StringBuilder();
				values.append("'" + this.benchmarkId + "',?");

				final StringBuilder fields = new StringBuilder("benchmark_id,timestamp");

				for (int i = 1; i <= typeArray.length; i++) {
					values.append(",?");
					fields.append(",c");
					fields.append(i);
				}

				final String statement = "INSERT INTO " + tableName + " ( " + fields.toString() + " )  VALUES (" + values.toString() + ")";
				this.classes.put(className, statement);
			} catch (final ConfigurationException exc) {
				LOGGER.error("Error creating table: {}", exc.getLocalizedMessage());
			}
		}

		
		final BoundStatement boundStatement = this.database.createBoundStatement(this.classes.get(className));
		// The while section must be reworked
		final IValueSerializer cassandraSerializer = new CassandraValueSerializer(boundStatement);
		record.serialize(cassandraSerializer);
		
		final List<Object> values = new ArrayList<>();
		values.add(record.getLoggingTimestamp());

		try {
			this.database.insert(this.classes.get(className), boundStatement);
		} catch (final ConfigurationException | MonitoringRecordException exc) {
			LOGGER.error("Error inserting monitoring record: {}", exc.getLocalizedMessage());
		}
	}

	@Override
	public void onTerminating() {
		this.database.disconnect();
	}

}
