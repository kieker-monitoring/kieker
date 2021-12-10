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

package kieker.examples.userguide.appendixAMQP;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.TeeFilter;
import kieker.analysis.plugin.reader.amqp.AmqpReader;
import kieker.common.configuration.Configuration;

/**
 * @author Holger Knoche
 */
public final class AMQPAnalysisStarter {

	private static final String URI = "amqp://username:password@127.0.0.1:1234/virtualhost";
	private static final String QUEUENAME = "queue1";

	private static String uri;
	private static String queueName;

	private AMQPAnalysisStarter() {}

	public static void main(final String[] args) throws Exception {
		if (!AMQPAnalysisStarter.parseArguments(args)) {
			// invalid parameters
			AMQPAnalysisStarter.printUsage();
			System.exit(1);
		}

		final IAnalysisController analysisInstance = new AnalysisController();

		final Configuration logReaderConfiguration = new Configuration();
		logReaderConfiguration.setProperty(AmqpReader.CONFIG_PROPERTY_URI, uri);
		logReaderConfiguration.setProperty(AmqpReader.CONFIG_PROPERTY_QUEUENAME, queueName);

		final AmqpReader logReader = new AmqpReader(logReaderConfiguration, analysisInstance);

		// Create and register a simple output writer.
		final TeeFilter teeFilter = new TeeFilter(new Configuration(), analysisInstance);

		try {
			analysisInstance.connect(logReader, AmqpReader.OUTPUT_PORT_NAME_RECORDS, teeFilter, TeeFilter.INPUT_PORT_NAME_EVENTS);
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
		if (args.length != 2) {
			System.err.println("Invalid number of arguments: " + args.length);
			return false;
		}

		uri = args[0];
		System.out.println("amqp-uri:" + uri);
		queueName = args[1];
		System.out.println("amqp-queue-name:" + queueName);
		System.out.println();
		return true;
	}

	private static void printUsage() {
		System.out.println("Usage: " + AMQPAnalysisStarter.class.getName() + " <amqp-uri> <amqp-queue-name>");
		System.out.println();
		System.out.println("Example:");
		System.out.println(AMQPAnalysisStarter.class.getName() + " " + URI + " " + QUEUENAME);
	}
}
