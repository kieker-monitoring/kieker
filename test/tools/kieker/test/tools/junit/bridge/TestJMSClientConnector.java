/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
package kieker.test.tools.junit.bridge;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.jms.JMSClientConnector;

/**
 * 
 * @author Reiner Jung
 * 
 * @since 1.8
 */
public class TestJMSClientConnector extends AbstractConnectorTest {

	/**
	 * Empty constructor to satisfy coding style.
	 */
	public TestJMSClientConnector() {
		// Nothing to be done.
	}

	/**
	 * Test the JMS client connector.
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             on lookup failure for the test record
	 */
	@Test
	public void testJMSClientConnector() throws ConnectorDataTransmissionException { // NOPMD

		final JMSBroker broker = new JMSBroker();
		final Thread brokerThread = new Thread(broker, "Broker");
		try {
			final Thread messageGenerator = new Thread(new JMSMessageGenerator(new URI(ConfigurationParameters.JMS_URI)), "Generator");

			brokerThread.start();
			while (!broker.isRunning()) {
				try {
					Thread.sleep(10);
				} catch (final InterruptedException e) {
					throw new ConnectorDataTransmissionException("Wait for broker failed.");
				}
			}
			messageGenerator.start();

			final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
			configuration.setProperty(JMSClientConnector.USERNAME, String.valueOf(ConfigurationParameters.JMS_USERNAME));
			configuration.setProperty(JMSClientConnector.PASSWORD, String.valueOf(ConfigurationParameters.JMS_PASSWORD));
			configuration.setProperty(JMSClientConnector.URI, String.valueOf(ConfigurationParameters.JMS_URI));
			configuration.setProperty(JMSClientConnector.FACTORY_LOOKUP_NAME, ConfigurationParameters.JMS_FACTORY_LOOKUP_NAME);
			// test the connector
			this.setConnector(new JMSClientConnector(configuration, this.createLookupEntityMap()));
			this.initialize();
			this.deserialize(ConfigurationParameters.SEND_NUMBER_OF_RECORDS);
			this.close(ConfigurationParameters.SEND_NUMBER_OF_RECORDS);
		} catch (final URISyntaxException e) {
			throw new ConnectorDataTransmissionException(e.getMessage());
		}

	}
}
