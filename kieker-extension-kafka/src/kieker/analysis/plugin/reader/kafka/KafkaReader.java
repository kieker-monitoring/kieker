/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.plugin.reader.kafka;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.newio.AbstractRawDataReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * Reader plugin that reads monitoring records from a Kafka topic.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
@Plugin(
		description = "A plugin that reads monitoring records from a Kafka topic",
		outputPorts = {
			@OutputPort(name = KafkaReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class }, description = "Output port of the Kafka reader")
		},
		configuration = {
				@Property(name = KafkaReader.CONFIG_PROPERTY_TOPIC_NAME, defaultValue = "kiekerRecords",
						description = "Name of the Kafka topic to read the records from"),
				@Property(name = KafkaReader.CONFIG_PROPERTY_BOOTSTRAP_SERVERS, defaultValue = "localhost:9092",
						description = "Bootstrap servers for the Kafka cluster"),
				@Property(name = KafkaReader.CONFIG_PROPERTY_GROUP_ID, defaultValue = "kieker", description = "Group ID for the Kafka consumer group"),
				@Property(name = KafkaReader.CONFIG_PROPERTY_AUTO_COMMIT, defaultValue = "true", description = "Auto-commit the current position?"),
				@Property(name = KafkaReader.CONFIG_PROPERTY_AUTO_COMMIT_INTERVAL_MS, defaultValue = "1000", description = "Auto commit interval in milliseconds"),
				@Property(name = KafkaReader.CONFIG_PROPERTY_SESSION_TIMEOUT_MS, defaultValue = "30000", description = "Session timeout interval in milliseconds")
		}
		)
public class KafkaReader extends AbstractRawDataReaderPlugin {
	/** The name of the output port delivering the received records. */
	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	/** The name of the configuration property for the deserializer */
	public static final String CONFIG_PROPERTY_DESERIALIZER = "deserializer";
	/** The name of the configuration property for the topic name. */
	public static final String CONFIG_PROPERTY_TOPIC_NAME = "topicName";
	/** The name of the configuration property for the bootstrap servers. */
	public static final String CONFIG_PROPERTY_BOOTSTRAP_SERVERS = "bootstrapServers";
	/** The name of the configuration property for the group ID. */
	public static final String CONFIG_PROPERTY_GROUP_ID = "groupId";
	/** The name of the configuration property for the auto-commit flag. */
	public static final String CONFIG_PROPERTY_AUTO_COMMIT = "autoCommit";
	/** The name of the configuration property for the auto-commit interval. */
	public static final String CONFIG_PROPERTY_AUTO_COMMIT_INTERVAL_MS = "autoCommitIntervalMs";
	/** The name of the configuration property for the session timeout interval. */
	public static final String CONFIG_PROPERTY_SESSION_TIMEOUT_MS = "sessionTimeoutMs";

	private final String topicName;
	private final String bootstrapServers;
	private final String groupId;
	private final boolean enableAutoCommit;
	private final int autoCommitIntervalMs;
	private final int sessionTimeoutMs;

	private KafkaConsumer<String, byte[]> consumer;

	private volatile boolean terminated = false; // NOPMD

	/**
	 * Creates a new Kafka reader using the givend data.
	 * 
	 * @param configuration
	 *            The configuration to use
	 * @param projectContext
	 *            The project context the plugin runs in
	 */
	public KafkaReader(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext, configuration.getStringProperty(CONFIG_PROPERTY_DESERIALIZER));

		this.topicName = configuration.getStringProperty(CONFIG_PROPERTY_TOPIC_NAME);
		this.bootstrapServers = configuration.getStringProperty(CONFIG_PROPERTY_BOOTSTRAP_SERVERS);
		this.groupId = configuration.getStringProperty(CONFIG_PROPERTY_GROUP_ID);
		this.enableAutoCommit = configuration.getBooleanProperty(CONFIG_PROPERTY_AUTO_COMMIT);
		this.autoCommitIntervalMs = configuration.getIntProperty(CONFIG_PROPERTY_AUTO_COMMIT_INTERVAL_MS);
		this.sessionTimeoutMs = configuration.getIntProperty(CONFIG_PROPERTY_SESSION_TIMEOUT_MS);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_TOPIC_NAME, this.topicName);
		configuration.setProperty(CONFIG_PROPERTY_BOOTSTRAP_SERVERS, this.bootstrapServers);

		if (this.groupId != null) {
			configuration.setProperty(CONFIG_PROPERTY_GROUP_ID, this.groupId);
		}

		configuration.setProperty(CONFIG_PROPERTY_AUTO_COMMIT, this.enableAutoCommit);
		configuration.setProperty(CONFIG_PROPERTY_AUTO_COMMIT_INTERVAL_MS, this.autoCommitIntervalMs);
		configuration.setProperty(CONFIG_PROPERTY_SESSION_TIMEOUT_MS, this.sessionTimeoutMs);

		return configuration;
	}

	@Override
	public boolean init() {
		final boolean superInitSuccessful = super.init();

		if (!superInitSuccessful) {
			return false;
		}

		final Properties properties = new Properties();

		properties.put("bootstrap.servers", this.bootstrapServers);
		properties.put("group.id", this.groupId);
		properties.put("enable.auto.commit", this.enableAutoCommit);
		properties.put("auto.commit.interval.ms", this.autoCommitIntervalMs);
		properties.put("session.timeout.ms", this.sessionTimeoutMs);

		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");

		this.consumer = new KafkaConsumer<String, byte[]>(properties);

		return true;
	}

	@Override
	public boolean read() {
		this.consumer.subscribe(Arrays.asList(this.topicName));

		try {
			while (!this.terminated) {
				final ConsumerRecords<String, byte[]> records = this.consumer.poll(100);

				this.processRecords(records);
			}
		} finally {
			this.consumer.close();
		}

		return true;
	}

	// PMD thinks this is an unused private method (see: https://github.com/pmd/pmd/issues/521)
	private void processRecords(final ConsumerRecords<String, byte[]> records) { // NOPMD (false positive, see above)
		for (final ConsumerRecord<String, byte[]> record : records) {
			final byte[] valueAsBytes = record.value();
			this.decodeAndDeliverRecords(valueAsBytes, OUTPUT_PORT_NAME_RECORDS);
		}
	}

	@Override
	public void terminate(final boolean error) {
		this.terminated = true;
	}

}
