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
package kieker.test.monitoring.junit.core;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import junit.framework.Assert;
import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jan Waller
 */
public class TestJMXInterface {

	private static final String DOMAIN = "kieker.monitoring";
	private static final String CONTROLLER = "MonitoringController";
	private static final String PORT = "59999";

	private volatile IMonitoringController ctrl = null;

	@Before
	public void setUp() throws Exception {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX, "true");
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_CONTROLLER, "true");
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_DOMAIN, TestJMXInterface.DOMAIN);
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_CONTROLLER_NAME, TestJMXInterface.CONTROLLER);
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_REMOTE, "true");
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_REMOTE_FALLBACK, "false");
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_REMOTE_NAME, "JMXServer");
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_REMOTE_PORT, TestJMXInterface.PORT);
		this.ctrl = MonitoringController.createInstance(config);
		// Thread.sleep(1000); // seems to work without delay
	}

	@After
	public void tearDown() {
		if (this.ctrl != null) {
			this.ctrl.terminateMonitoring();
			this.ctrl = null; // help the GC
		}
	}

	@Test
	public void testJMXInterface() throws Exception {
		final JMXServiceURL serviceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + TestJMXInterface.PORT + "/jmxrmi");
		final ObjectName controllerObjectName = new ObjectName(TestJMXInterface.DOMAIN, "type", TestJMXInterface.CONTROLLER);

		final JMXConnector jmx = JMXConnectorFactory.connect(serviceURL);
		final MBeanServerConnection mbServer = jmx.getMBeanServerConnection();

		final IMonitoringController ctrlJMX = JMX.newMBeanProxy(mbServer, controllerObjectName, IMonitoringController.class, true);

		Assert.assertTrue(this.ctrl.isMonitoringEnabled());
		Assert.assertTrue(ctrlJMX.isMonitoringEnabled());

		Assert.assertTrue(ctrlJMX.disableMonitoring());

		Assert.assertFalse(this.ctrl.isMonitoringEnabled());
		Assert.assertFalse(ctrlJMX.isMonitoringEnabled());

		jmx.close();
	}
}
