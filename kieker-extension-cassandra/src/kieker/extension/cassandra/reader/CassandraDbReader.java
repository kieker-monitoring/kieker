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

import java.util.ArrayList;
import java.util.Iterator;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.extension.cassandra.CassandraValueDeserializer;

/**
 * A cassandra database reader
 *
 * @author Armin Moebius, Sven Ulrich
 *
 * @deprecated 1.15 implementation moved to TeeTime stage.
 */
@Plugin(
		description = "A reader which reads records from a cassandra database",
		outputPorts = {
			@OutputPort(
					name = CassandraDbReader.OUTPUT_PORT_NAME_RECORDS,
					eventTypes = IMonitoringRecord.class,
					description = "Output Port of the Cassandra Reader")
		},
		configuration = {
			@Property(
					name = CassandraDbReader.CONFIG_PROPERTY_NAME_KEYSPACE,
					defaultValue = "kieker",
					description = "Name of the keyspace of the cassandra database"),
			@Property(
					name = CassandraDbReader.CONFIG_PROPERTY_NAME_CONTACTPOINTS,
					defaultValue = "127.0.0.1:9042",
					description = "Contactpoints (Host:Port), where the Database is reachable (List separated by ;)"),
			@Property(
					name = CassandraDbReader.CONFIG_PROPERTY_NAME_TABLEPREFIX,
					defaultValue = "kieker",
					description = "The prefix of the used table within the database")
		})
@Deprecated
public class CassandraDbReader extends AbstractReaderPlugin {
	/**
	 * Name of the outputport delivering the records from the database
	 */
	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	public static final String CONFIG_PROPERTY_NAME_KEYSPACE = "Keyspace";
	public static final String CONFIG_PROPERTY_NAME_CONTACTPOINTS = "Contactpoints";
	public static final String CONFIG_PROPERTY_NAME_TABLEPREFIX = "TablePrefix";

	private final String keyspace;
	private final String[] contactPoints;
	private final String tablePrefix;

	private volatile boolean running = true;
	
	private final CachedRecordFactoryCatalog recordFactories = CachedRecordFactoryCatalog.getInstance();


	/**
	 * Creates a new instance of this class using the given parameters
	 *
	 * @param configuration
	 * @param projectContext
	 */
	public CassandraDbReader(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.keyspace = configuration.getStringProperty(CONFIG_PROPERTY_NAME_KEYSPACE);
		this.contactPoints = configuration.getStringArrayProperty(CONFIG_PROPERTY_NAME_CONTACTPOINTS, ";");
		this.tablePrefix = configuration.getStringProperty(CONFIG_PROPERTY_NAME_TABLEPREFIX);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean read() {
		CassandraDb database = null;
		try {
			database = new CassandraDb(this.keyspace, this.contactPoints);

			// index table
			final ResultSet rs = database.select(new ArrayList<String>(), this.tablePrefix, null);
			final Iterator<Row> iterator = rs.iterator();

			while (iterator.hasNext() && this.running) {
				final Row r = iterator.next();

				final String tablename = r.getString(1);
				final String classname = r.getString(2);

				this.table2record(database, tablename, classname);
			}

		} catch (ConfigurationException exc) {
			this.logger.error(exc.getMessage());
		} finally {
			if (database != null) {
				database.disconnect();
			}
		}

		return true;
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
		final ResultSet rs = database.select(new ArrayList<String>(), tablename, null);

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
					super.deliver(OUTPUT_PORT_NAME_RECORDS, record);
				}
			}

			if (!this.running) {
				break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate(final boolean terminate) {
		this.logger.info("Shutdown of CassandraDbReader");
		this.running = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_KEYSPACE, this.keyspace);
		configuration.setStringArrayProperty(CONFIG_PROPERTY_NAME_CONTACTPOINTS, this.contactPoints);
		configuration.setProperty(CONFIG_PROPERTY_NAME_TABLEPREFIX, this.tablePrefix);
		return configuration;
	}

}
