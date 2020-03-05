/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.writer.kafka;

import java.nio.ByteBuffer;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import kieker.common.configuration.Configuration;
import kieker.common.exception.InvalidConfigurationException;
import kieker.monitoring.writer.raw.IRawDataWriter;

/**
 * Raw data writer which sends monitoring records to a Kafka topic.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class KafkaWriter implements IRawDataWriter {

	private static final String DEFAULT_ACKS = "all";
	private static final int DEFAULT_BATCH_SIZE = 16384;
	private static final int DEFAULT_LINGER_MS = 1;
	private static final int DEFAULT_BUFFER_MEMORY = 32 << 20;

	private static final String PREFIX = KafkaWriter.class.getName() + ".";

	/** The name of the configuration property for the acks parameter. */
	public static final String CONFIG_PROPERTY_ACKS = PREFIX + "acks"; // NOCS (afterPREFIX)
	/** The name of the configuration property for the batch size. */
	public static final String CONFIG_PROPERTY_BATCH_SIZE = PREFIX + "batchSize"; // NOCS (afterPREFIX)
	/** The name of the configuration property for the bootstrap servers. */
	public static final String CONFIG_PROPERTY_BOOTSTRAP_SERVERS = PREFIX + "bootstrapServers"; // NOCS (afterPREFIX)
	/** The name of the configuration property for the buffer memory size. */
	public static final String CONFIG_PROPERTY_BUFFER_MEMORY = PREFIX + "bufferMemory"; // NOCS (afterPREFIX)
	/** The name of the configuration property for the linger interval. */
	public static final String CONFIG_PROPERTY_LINGER_MS = PREFIX + "lingerMs"; // NOCS (afterPREFIX)
	/** The name of the configuration property for the topic name. */
	public static final String CONFIG_PROPERTY_TOPIC_NAME = PREFIX + "topicName"; // NOCS (afterPREFIX)

	private final String bootstrapServers;
	private final String topicName;
	private final String acknowledges;
	private final int lingerMs;
	private final int batchSize;
	private final int bufferMemory;

	private Producer<String, byte[]> producer;

	/**
	 * Creates a new Kafka writer using the given configuration.
	 *
	 * @param configuration
	 *            The configuration to use
	 */
	public KafkaWriter(final Configuration configuration) {
		this.bootstrapServers = configuration.getStringProperty(CONFIG_PROPERTY_BOOTSTRAP_SERVERS);
		this.topicName = configuration.getStringProperty(CONFIG_PROPERTY_TOPIC_NAME);
		this.acknowledges = configuration.getStringProperty(CONFIG_PROPERTY_ACKS, DEFAULT_ACKS);
		this.lingerMs = configuration.getIntProperty(CONFIG_PROPERTY_LINGER_MS, DEFAULT_LINGER_MS);
		this.batchSize = configuration.getIntProperty(CONFIG_PROPERTY_BATCH_SIZE, DEFAULT_BATCH_SIZE);
		this.bufferMemory = configuration.getIntProperty(CONFIG_PROPERTY_BUFFER_MEMORY, DEFAULT_BUFFER_MEMORY);

		this.checkConfiguration();
	}

	private void checkConfiguration() {
		// Check bootstrap servers field
		if (this.bootstrapServers.isEmpty()) {
			throw new InvalidConfigurationException("At least one bootstrap server must be provided.");
		}

		// Check whether a topic name was given
		if (this.topicName.isEmpty()) {
			throw new InvalidConfigurationException("A topic name must be provided.");
		}
	}

	@Override
	public void onInitialization() {
		final Properties properties = new Properties();

		properties.put("bootstrap.servers", this.bootstrapServers);
		properties.put("acks", this.acknowledges);
		properties.put("batch.size", this.batchSize);
		properties.put("linger.ms", this.lingerMs);
		properties.put("buffer.memory", this.bufferMemory);

		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

		this.producer = new KafkaProducer<String, byte[]>(properties);
	}

	@Override
	public void onTermination() {
		if (this.producer != null) {
			this.producer.close();
		}
	}

	@Override
	public void writeData(final ByteBuffer buffer, final int offset, final int length) {
		buffer.position(offset);
		final byte[] rawDataAsBytes = new byte[length];
		buffer.get(rawDataAsBytes);

		final ProducerRecord<String, byte[]> record = new ProducerRecord<String, byte[]>(this.topicName, rawDataAsBytes);
		this.producer.send(record);
	}

}
