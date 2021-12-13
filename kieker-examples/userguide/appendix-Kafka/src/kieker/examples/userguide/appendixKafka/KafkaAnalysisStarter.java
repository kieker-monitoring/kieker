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

package kieker.examples.userguide.appendixKafka;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.TeeFilter;
import kieker.analysis.plugin.reader.newio.deserializer.BinaryDeserializer;
import kieker.analysis.plugin.reader.kafka.KafkaReader;
import kieker.common.configuration.Configuration;

/**
 * @author Holger Knoche
 */
public final class KafkaAnalysisStarter {

	private static final String BOOTSTRAP_SERVER = "127.0.0.1:9092";
	private static final String TOPIC_NAME = "kiekerRecords";
	private static final String GROUP_ID = "kieker";
	
	private static final String DESERIALIZER_NAME = BinaryDeserializer.class.getName();

	private static String bootstrapServer;
	private static String topicName;
	private static String groupId;
	
	private static String deserializerName;

	private KafkaAnalysisStarter() {}

	public static void main(final String[] args) throws Exception {
		if (!KafkaAnalysisStarter.parseArguments(args)) {
			// invalid parameters
			KafkaAnalysisStarter.printUsage();
			System.exit(1);
		}

		final IAnalysisController analysisInstance = new AnalysisController();

		final Configuration logReaderConfiguration = new Configuration();
		logReaderConfiguration.setProperty(KafkaReader.CONFIG_PROPERTY_BOOTSTRAP_SERVERS, bootstrapServer);
		logReaderConfiguration.setProperty(KafkaReader.CONFIG_PROPERTY_TOPIC_NAME, topicName);
		logReaderConfiguration.setProperty(KafkaReader.CONFIG_PROPERTY_GROUP_ID, groupId);
		logReaderConfiguration.setProperty(KafkaReader.CONFIG_PROPERTY_DESERIALIZER, deserializerName);

		final KafkaReader logReader = new KafkaReader(logReaderConfiguration, analysisInstance);

		// Create and register a simple output writer.
		final TeeFilter teeFilter = new TeeFilter(new Configuration(), analysisInstance);

		try {
			analysisInstance.connect(logReader, KafkaReader.OUTPUT_PORT_NAME_RECORDS, teeFilter, TeeFilter.INPUT_PORT_NAME_EVENTS);
			analysisInstance.run();
		} catch (final AnalysisConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parses the given arguments and initializes the variables {@link #connectionFactory} and {@link #providerUrl}.
	 *
	 * @return true iff valid parameters were passed
	 */
	private static boolean parseArguments(final String[] args) {
		if (args.length != 3 && args.length != 4) {
			System.err.println("Invalid number of arguments: " + args.length);
			return false;
		}

		bootstrapServer = args[0];
		System.out.println("kafka-bootstrap-server:" + bootstrapServer);
		topicName = args[1];
		System.out.println("kafka-topic-name:" + topicName);
		groupId = args[2];
		System.out.println("kafka-group-id:" + groupId);
		
		deserializerName = (args.length == 3) ? DESERIALIZER_NAME : args[3];
		System.out.println("kafka-deserializer-name:" + deserializerName);
		
		System.out.println();
		return true;
	}

	private static void printUsage() {
		System.out.println("Usage: " + KafkaAnalysisStarter.class.getName() + " <kafka-bootstrap-server> <kafka-topic-name> <kafka-group-id> [kafka-deserializer-name]");
		System.out.println();
		System.out.println("Example:");
		System.out.println(KafkaAnalysisStarter.class.getName() + " " + BOOTSTRAP_SERVER + " " + TOPIC_NAME + " " + GROUP_ID + " " + DESERIALIZER_NAME);
	}
}
