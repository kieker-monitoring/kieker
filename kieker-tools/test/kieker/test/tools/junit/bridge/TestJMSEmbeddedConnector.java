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
package kieker.test.tools.junit.bridge;

import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.jms.JMSClientConnector;
import kieker.tools.bridge.connector.jms.JMSEmbeddedConnector;

/**
 *
 * @author Reiner Jung
 *
 * @since 1.9
 */
public class TestJMSEmbeddedConnector extends AbstractConnectorTest {

	/**
	 * Empty constructor to satisfy coding style.
	 */
	public TestJMSEmbeddedConnector() {
		// Nothing to be done.
	}

	/**
	 * Test the JMS embedded connector.
	 *
	 * @throws ConnectorDataTransmissionException
	 *             on lookup failure for the test record
	 */
	@Test
	public void testJMSEmbeddedConnector() throws ConnectorDataTransmissionException { // NOPMD
		final Thread messageGenerator = new Thread(new JMSMessageGenerator(ConfigurationParameters.JMS_EMBEDDED_URI,
				ConfigurationParameters.JMS_EMBEDDED_FACTORY_LOOKUP_NAME), "Generator");

		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		configuration.setProperty(JMSEmbeddedConnector.PORT, String.valueOf(ConfigurationParameters.JMS_EMBEDDED_PORT));
		configuration.setProperty(JMSClientConnector.USERNAME, String.valueOf(ConfigurationParameters.JMS_USERNAME));
		configuration.setProperty(JMSClientConnector.PASSWORD, String.valueOf(ConfigurationParameters.JMS_PASSWORD));
		configuration.setProperty(JMSClientConnector.URI, ConfigurationParameters.JMS_EMBEDDED_URI);
		configuration.setProperty(JMSClientConnector.FACTORY_LOOKUP_NAME, ConfigurationParameters.JMS_EMBEDDED_FACTORY_LOOKUP_NAME);

		// test the connector
		this.setConnector(new JMSEmbeddedConnector(configuration, this.createLookupEntityMap()));
		this.initialize();
		messageGenerator.start();
		this.deserialize(ConfigurationParameters.SEND_NUMBER_OF_RECORDS, true);
		this.close(ConfigurationParameters.SEND_NUMBER_OF_RECORDS);

		try {
			messageGenerator.join(2000);
		} catch (final InterruptedException e) {
			LOG.error("Message generator was illegaly interrupted. ", e);
		}
	}
}
