/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
import kieker.analysis.reader.jms.JMSReader;
import kieker.common.configuration.Configuration;

/**
 * 
 * @author Andre van Hoorn
 */
public class JMSAnalysisStarter {

	private static final long MAX_RT_NANOS = 1700;

	private final static String CONNECTION_FACTORY_TYPE__ACTIVEMQ = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";
	private final static String CONNECTION_FACTORY_TYPE__HORNETQ = "org.jnp.interfaces.NamingContextFactory";
	private final static String CONNECTION_FACTORY_TYPE__OPENJMS = "org.exolab.jms.jndi.InitialContextFactory";

	/** Default provider URL used by ActiveMQ */
	private final static String PROVIDER_URL__ACTIVEMQ = "tcp://127.0.0.1:61616/";
	/** Default provider URL used by HornetQ */
	private final static String PROVIDER_URL__HORNETQ = "jnp://localhost:1099/";
	/** Default provider URL used by OpenJMS */
	private final static String PROVIDER_URL__OPENJMS = "tcp://127.0.0.1:3035/";

	private final static String QUEUE_ACTIVEMQ = "queue1";
	private final static String QUEUE_HORNETQ = "queue1";
	private final static String QUEUE_OPENJMS = "queue1";

	private static String connectionFactory;
	private static String providerUrl;
	private static String queue;

	public static void main(final String[] args) {
		if (!JMSAnalysisStarter.parseArguments(args)) {
			// invalid parameters
			JMSAnalysisStarter.printUsage();
			System.exit(1);
		}

		final AnalysisController analysisInstance = new AnalysisController();

		final Configuration logReaderConfiguration = new Configuration();
		logReaderConfiguration.setProperty(JMSReader.CONFIG_PROVIDERURL, JMSAnalysisStarter.providerUrl);
		logReaderConfiguration.setProperty(JMSReader.CONFIG_FACTORYLOOKUP, JMSAnalysisStarter.connectionFactory);
		logReaderConfiguration.setProperty(JMSReader.CONFIG_DESTINATION, JMSAnalysisStarter.queue);

		final JMSReader logReader = new JMSReader(logReaderConfiguration);
		analysisInstance.registerReader(logReader);

		final Configuration consumerConfiguration = new Configuration();
		consumerConfiguration.setProperty(Consumer.CONFIG_MAX_RESPONSE_TIME, Long.toString(JMSAnalysisStarter.MAX_RT_NANOS));

		final Consumer consumer = new Consumer(consumerConfiguration);
		analysisInstance.registerFilter(consumer);

		analysisInstance.connect(logReader, JMSReader.OUTPUT_PORT_NAME, consumer, Consumer.INPUT_PORT_NAME);

		analysisInstance.run();
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

		JMSAnalysisStarter.connectionFactory = args[0];
		System.out.println("jms-connection-factory:" + JMSAnalysisStarter.connectionFactory);
		JMSAnalysisStarter.providerUrl = args[1];
		System.out.println("jms-provider-url:      " + JMSAnalysisStarter.providerUrl);
		JMSAnalysisStarter.queue = args[2];
		System.out.println("jms-queue:             " + JMSAnalysisStarter.queue);
		System.out.println();
		return true;
	}

	private static void printUsage() {
		System.out.println("Usage: " + JMSAnalysisStarter.class.getName() + " <jms-connection-factory> <jms-provider-url> <jms-queue>");
		System.out.println();
		System.out.println("Examples:");
		System.out.println(" - ActiveMQ: " + JMSAnalysisStarter.CONNECTION_FACTORY_TYPE__ACTIVEMQ + " " + JMSAnalysisStarter.PROVIDER_URL__ACTIVEMQ + " "
				+ JMSAnalysisStarter.QUEUE_ACTIVEMQ);
		System.out.println(" - HornetQ:  " + JMSAnalysisStarter.CONNECTION_FACTORY_TYPE__HORNETQ + " " + JMSAnalysisStarter.PROVIDER_URL__HORNETQ + " "
				+ JMSAnalysisStarter.QUEUE_HORNETQ);
		System.out.println(" - OpenJMS:  " + JMSAnalysisStarter.CONNECTION_FACTORY_TYPE__OPENJMS + " " + JMSAnalysisStarter.PROVIDER_URL__OPENJMS + " "
				+ JMSAnalysisStarter.QUEUE_OPENJMS);
	}
}
