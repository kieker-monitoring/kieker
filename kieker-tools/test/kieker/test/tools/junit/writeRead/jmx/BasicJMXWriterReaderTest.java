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

package kieker.test.tools.junit.writeRead.jmx;

import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.plugin.reader.jmx.JmxReader;
import kieker.analysis.tt.writeRead.TestDataRepository;
import kieker.analysis.tt.writeRead.TestProbe;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.jmx.JmxWriter;

import kieker.test.tools.junit.writeRead.TestAnalysis;

/**
 * @author Jan Waller, Christian Wulf
 *
 * @since 1.8
 */
public class BasicJMXWriterReaderTest {

	private static final String DOMAIN = "kieker.monitoring";
	private static final String CONTROLLER = "MonitoringController";
	private static final String PORT = "59999";
	private static final String LOGNAME = "MonitoringLog";
	private static final TestDataRepository TEST_DATA_REPOSITORY = new TestDataRepository();
	private static final long MONITORING_TIMEOUT_IN_MS = 0;
	private static final long ANALYSIS_TIMEOUT_IN_MS = 1000;

	public BasicJMXWriterReaderTest() {
		super();
	}

	// @Test
	public void testCommunication() throws Exception {
		final MonitoringController monitoringController = this.createMonitoringController();
		final TestAnalysis analysis = this.createAnalysis();
		analysis.startInNewThread();
		// TO_DO wait until jmx reader has registered its notification handler
		// via: MBeanServerConnection.isRegistered(monitoringLog)
		// from JmxReader: this.monitoringLog = new ObjectName(this.domain, "type", this.logname);
		Thread.sleep(1000);

		// 3. define analysis config
		final List<IMonitoringRecord> records = TEST_DATA_REPOSITORY.newTestEventRecords();

		// 4. trigger records
		final TestProbe testProbe = new TestProbe(monitoringController);
		this.checkControllerStateBeforeRecordsPassedToController(monitoringController);
		testProbe.triggerRecords(records);
		this.checkControllerStateAfterRecordsPassedToController(monitoringController);

		// 5. terminate monitoring
		monitoringController.terminateMonitoring();

		// 6. wait for termination
		monitoringController.waitForTermination(MONITORING_TIMEOUT_IN_MS);
		// analysis.startAndWaitForTermination();
		analysis.waitForTermination(ANALYSIS_TIMEOUT_IN_MS);

		// 7. read actual records
		final List<IMonitoringRecord> analyzedRecords = analysis.getList();

		// 8. compare actual and expected records
		Assert.assertThat(analyzedRecords, CoreMatchers.is(CoreMatchers.equalTo(records)));
	}

	private MonitoringController createMonitoringController() {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationConstants.ACTIVATE_JMX, "true");
		config.setProperty(ConfigurationConstants.ACTIVATE_JMX_CONTROLLER, "true");
		config.setProperty(ConfigurationConstants.ACTIVATE_JMX_DOMAIN, BasicJMXWriterReaderTest.DOMAIN);
		config.setProperty(ConfigurationConstants.ACTIVATE_JMX_CONTROLLER_NAME, BasicJMXWriterReaderTest.CONTROLLER);
		config.setProperty(ConfigurationConstants.ACTIVATE_JMX_REMOTE, "true");
		config.setProperty(ConfigurationConstants.ACTIVATE_JMX_REMOTE_FALLBACK, "false");
		config.setProperty(ConfigurationConstants.ACTIVATE_JMX_REMOTE_NAME, "JMXServer");
		config.setProperty(ConfigurationConstants.ACTIVATE_JMX_REMOTE_PORT, BasicJMXWriterReaderTest.PORT);
		config.setProperty(ConfigurationConstants.WRITER_CLASSNAME, JmxWriter.class.getName());
		config.setProperty(JmxWriter.CONFIG_DOMAIN, "");
		config.setProperty(JmxWriter.CONFIG_LOGNAME, BasicJMXWriterReaderTest.LOGNAME);
		return MonitoringController.createInstance(config);
	}

	private TestAnalysis createAnalysis() throws Exception {
		final Configuration jmxReaderConfig = new Configuration();

		jmxReaderConfig.setProperty(JmxReader.CONFIG_PROPERTY_NAME_DOMAIN, BasicJMXWriterReaderTest.DOMAIN);
		jmxReaderConfig.setProperty(JmxReader.CONFIG_PROPERTY_NAME_LOGNAME, BasicJMXWriterReaderTest.LOGNAME);
		jmxReaderConfig.setProperty(JmxReader.CONFIG_PROPERTY_NAME_SERVER, "localhost");
		jmxReaderConfig.setProperty(JmxReader.CONFIG_PROPERTY_NAME_PORT, BasicJMXWriterReaderTest.PORT);
		jmxReaderConfig.setProperty(JmxReader.CONFIG_PROPERTY_NAME_SERVICEURL, "");
		jmxReaderConfig.setProperty(JmxReader.CONFIG_PROPERTY_NAME_SILENT, "false");

		return new TestAnalysis(jmxReaderConfig, JmxReader.class);
	}

	private void checkControllerStateBeforeRecordsPassedToController(final IMonitoringController monitoringController)
			throws Exception {
		// Test the JMX Controller
		final JMXServiceURL serviceURL = new JMXServiceURL(
				"service:jmx:rmi:///jndi/rmi://localhost:" + BasicJMXWriterReaderTest.PORT + "/jmxrmi");
		final ObjectName controllerObjectName = new ObjectName(BasicJMXWriterReaderTest.DOMAIN, "type",
				BasicJMXWriterReaderTest.CONTROLLER);

		final JMXConnector jmx = JMXConnectorFactory.connect(serviceURL);
		final MBeanServerConnection mbServer = jmx.getMBeanServerConnection();

		final Object tmpObj = MBeanServerInvocationHandler.newProxyInstance(mbServer, controllerObjectName,
				IMonitoringController.class, false);
		final IMonitoringController ctrlJMX = (IMonitoringController) tmpObj;

		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		Assert.assertTrue(ctrlJMX.isMonitoringEnabled());

		jmx.close();
	}

	private void checkControllerStateAfterRecordsPassedToController(final IMonitoringController monitoringController)
			throws Exception {
		// Test the JMX Controller
		final JMXServiceURL serviceURL = new JMXServiceURL(
				"service:jmx:rmi:///jndi/rmi://localhost:" + BasicJMXWriterReaderTest.PORT + "/jmxrmi");
		final ObjectName controllerObjectName = new ObjectName(BasicJMXWriterReaderTest.DOMAIN, "type",
				BasicJMXWriterReaderTest.CONTROLLER);

		final JMXConnector jmx = JMXConnectorFactory.connect(serviceURL);
		final MBeanServerConnection mbServer = jmx.getMBeanServerConnection();

		final Object tmpObj = MBeanServerInvocationHandler.newProxyInstance(mbServer, controllerObjectName,
				IMonitoringController.class, false);
		final IMonitoringController ctrlJMX = (IMonitoringController) tmpObj;

		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		Assert.assertTrue(ctrlJMX.isMonitoringEnabled());

		Assert.assertTrue(ctrlJMX.disableMonitoring());

		Assert.assertFalse(monitoringController.isMonitoringEnabled());
		Assert.assertFalse(ctrlJMX.isMonitoringEnabled());

		jmx.close();
	}

}
