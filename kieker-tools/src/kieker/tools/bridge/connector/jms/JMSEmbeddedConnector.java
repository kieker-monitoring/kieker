/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.bridge.connector.jms;

import java.util.concurrent.ConcurrentMap;

import org.apache.activemq.broker.BrokerService;

import kieker.common.configuration.Configuration;
import kieker.tools.bridge.LookupEntity;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorProperty;

/**
 * JMSClient with an embedded JMS server.
 * 
 * @author Reiner Jung
 * @since 1.8
 */
@ConnectorProperty(cmdName = "jms-embedded", name = "JMS Embedded Connector", description = "JMS Client to receive records from a built-in JMS queue.")
public class JMSEmbeddedConnector extends JMSClientConnector {

	/** Property name for the configuration property for the port of the embedded JMS server. */
	public static final String PORT = JMSEmbeddedConnector.class.getCanonicalName() + ".port";

	private BrokerService broker;
	private final int port;

	/**
	 * Construct a new JMS service consumer and an embedded JMS service.
	 * 
	 * @param configuration
	 *            Kieker configuration including setup for connectors
	 * 
	 * @param lookupEntityMap
	 *            IMonitoringRecord constructor and TYPES-array to id map
	 * @throws ConnectorDataTransmissionException
	 */
	public JMSEmbeddedConnector(final Configuration configuration, final ConcurrentMap<Integer, LookupEntity> lookupEntityMap) {
		super(configuration, lookupEntityMap);
		this.port = this.configuration.getIntProperty(JMSEmbeddedConnector.PORT);
		this.configuration.setProperty(JMSClientConnector.class.getCanonicalName() + ".uri", "tcp://localhost:" + this.port);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.tools.bridge.connector.jms.JMSClientConnector#initialize()
	 */
	@Override
	public void initialize() throws ConnectorDataTransmissionException {
		this.broker = new BrokerService();
		this.broker.setUseJmx(true);
		try {
			this.broker.addConnector("tcp://localhost:" + this.port);
			this.broker.start();
			super.initialize();
			// Cannot be avoided as the broker throws Exception.
		} catch (final Exception e) { // NOCS, NOPMD
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.tools.bridge.connector.jms.JMSClientConnector#close()
	 */
	@Override
	public void close() throws ConnectorDataTransmissionException {
		try {
			super.close();
			this.broker.stop();
			// Cannot be avoided as the broker throws Exception.
		} catch (final Exception e) { // NOCS, NOPMD
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}
	}
}
