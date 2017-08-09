/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.userguide.appendixJMS;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.TeeFilter;
import kieker.analysis.plugin.reader.jms.JMSReader;
import kieker.common.configuration.Configuration;

/**
 * 
 * @author Andre van Hoorn
 */
public final class JMSAnalysisStarter {

	private static final String CONNECTION_FACTORY_TYPE__ACTIVEMQ = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";

	/** Default provider URL used by ActiveMQ */
	private static final String PROVIDER_URL__ACTIVEMQ = "tcp://127.0.0.1:61616/";

	private static final String QUEUE_ACTIVEMQ = "queue1";

	private static String connectionFactory;
	private static String providerUrl;
	private static String queue;

	private JMSAnalysisStarter() {}

	public static void main(final String[] args) {
		if (!JMSAnalysisStarter.parseArguments(args)) {
			// invalid parameters
			JMSAnalysisStarter.printUsage();
			System.exit(1);
		}

		final IAnalysisController analysisInstance = new AnalysisController();

		final Configuration logReaderConfiguration = new Configuration();
		logReaderConfiguration.setProperty(JMSReader.CONFIG_PROPERTY_NAME_PROVIDERURL, providerUrl);
		logReaderConfiguration.setProperty(JMSReader.CONFIG_PROPERTY_NAME_FACTORYLOOKUP, connectionFactory);
		logReaderConfiguration.setProperty(JMSReader.CONFIG_PROPERTY_NAME_DESTINATION, queue);

		final JMSReader logReader = new JMSReader(logReaderConfiguration, analysisInstance);

		// Create and register a simple output writer.
		final TeeFilter teeFilter = new TeeFilter(new Configuration(), analysisInstance);

		try {
			analysisInstance.connect(logReader, JMSReader.OUTPUT_PORT_NAME_RECORDS, teeFilter, TeeFilter.INPUT_PORT_NAME_EVENTS);
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
		if (args.length != 3) {
			System.err.println("Invalid number of arguments: " + args.length);
			return false;
		}

		connectionFactory = args[0];
		System.out.println("jms-connection-factory:" + connectionFactory);
		providerUrl = args[1];
		System.out.println("jms-provider-url:      " + providerUrl);
		queue = args[2];
		System.out.println("jms-queue:             " + queue);
		System.out.println();
		return true;
	}

	private static void printUsage() {
		System.out.println("Usage: " + JMSAnalysisStarter.class.getName() + " <jms-connection-factory> <jms-provider-url> <jms-queue>");
		System.out.println();
		System.out.println("Examples:");
		System.out.println(" - ActiveMQ: " + CONNECTION_FACTORY_TYPE__ACTIVEMQ + " " + PROVIDER_URL__ACTIVEMQ + " "
				+ QUEUE_ACTIVEMQ);
	}
}
