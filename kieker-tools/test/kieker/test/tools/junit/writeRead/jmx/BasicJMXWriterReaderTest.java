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

package kieker.test.tools.junit.writeRead.jmx;

import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.junit.Assert;

import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.jmx.JMXReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writernew.jmx.JmxWriter;

import kieker.test.tools.junit.writeRead.AbstractWriterReaderTest;

/**
 * @author Jan Waller
 *
 * @since 1.8
 */
public class BasicJMXWriterReaderTest extends AbstractWriterReaderTest { // NOPMD NOCS (TestClassWithoutTestCases)

	private static final String DOMAIN = "kieker.monitoring";
	private static final String CONTROLLER = "MonitoringController";
	private static final String PORT = "59999";
	private static final String LOGNAME = "MonitoringLog";

	private volatile ListCollectionFilter<IMonitoringRecord> sinkFilter = null; // NOPMD (init for findbugs)

	@Override
	protected IMonitoringController createController(final int numRecordsWritten) throws IllegalStateException, AnalysisConfigurationException,
			InterruptedException {
		final AnalysisController analysisController = new AnalysisController();

		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX, "true");
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_CONTROLLER, "true");
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_DOMAIN, BasicJMXWriterReaderTest.DOMAIN);
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_CONTROLLER_NAME, BasicJMXWriterReaderTest.CONTROLLER);
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_REMOTE, "true");
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_REMOTE_FALLBACK, "false");
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_REMOTE_NAME, "JMXServer");
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_REMOTE_PORT, BasicJMXWriterReaderTest.PORT);
		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, JmxWriter.class.getName());
		config.setProperty(JmxWriter.CONFIG_DOMAIN, "");
		config.setProperty(JmxWriter.CONFIG_LOGNAME, BasicJMXWriterReaderTest.LOGNAME);
		final IMonitoringController ctrl = MonitoringController.createInstance(config);
		Thread.sleep(1000);
		final Configuration jmxReaderConfig = new Configuration();
		jmxReaderConfig.setProperty(JMXReader.CONFIG_PROPERTY_NAME_DOMAIN, BasicJMXWriterReaderTest.DOMAIN);
		jmxReaderConfig.setProperty(JMXReader.CONFIG_PROPERTY_NAME_LOGNAME, BasicJMXWriterReaderTest.LOGNAME);
		jmxReaderConfig.setProperty(JMXReader.CONFIG_PROPERTY_NAME_SERVER, "localhost");
		jmxReaderConfig.setProperty(JMXReader.CONFIG_PROPERTY_NAME_PORT, BasicJMXWriterReaderTest.PORT);
		jmxReaderConfig.setProperty(JMXReader.CONFIG_PROPERTY_NAME_SERVICEURL, "");
		jmxReaderConfig.setProperty(JMXReader.CONFIG_PROPERTY_NAME_SILENT, "false");

		final JMXReader jmxReader = new JMXReader(jmxReaderConfig, analysisController);
		this.sinkFilter = new ListCollectionFilter<IMonitoringRecord>(new Configuration(), analysisController);

		analysisController.connect(jmxReader, JMXReader.OUTPUT_PORT_NAME_RECORDS, this.sinkFilter, ListCollectionFilter.INPUT_PORT_NAME);
		final AnalysisControllerThread analysisThread = new AnalysisControllerThread(analysisController);
		analysisThread.start();
		Thread.sleep(1000);
		return ctrl;
	}

	@Override
	protected void checkControllerStateBeforeRecordsPassedToController(final IMonitoringController monitoringController) throws Exception {
		// Test the JMX Controller
		final JMXServiceURL serviceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + BasicJMXWriterReaderTest.PORT + "/jmxrmi");
		final ObjectName controllerObjectName = new ObjectName(BasicJMXWriterReaderTest.DOMAIN, "type", BasicJMXWriterReaderTest.CONTROLLER);

		final JMXConnector jmx = JMXConnectorFactory.connect(serviceURL);
		final MBeanServerConnection mbServer = jmx.getMBeanServerConnection();

		final Object tmpObj = MBeanServerInvocationHandler.newProxyInstance(mbServer, controllerObjectName, IMonitoringController.class, false);
		final IMonitoringController ctrlJMX = (IMonitoringController) tmpObj; // NOCS // NOPMD (required for the cast not being removed by Java 1.6 editors)

		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		Assert.assertTrue(ctrlJMX.isMonitoringEnabled());

		jmx.close();
	}

	@Override
	protected void checkControllerStateAfterRecordsPassedToController(final IMonitoringController monitoringController) throws Exception {
		// Test the JMX Controller
		final JMXServiceURL serviceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + BasicJMXWriterReaderTest.PORT + "/jmxrmi");
		final ObjectName controllerObjectName = new ObjectName(BasicJMXWriterReaderTest.DOMAIN, "type", BasicJMXWriterReaderTest.CONTROLLER);

		final JMXConnector jmx = JMXConnectorFactory.connect(serviceURL);
		final MBeanServerConnection mbServer = jmx.getMBeanServerConnection();

		final Object tmpObj = MBeanServerInvocationHandler.newProxyInstance(mbServer, controllerObjectName, IMonitoringController.class, false);
		final IMonitoringController ctrlJMX = (IMonitoringController) tmpObj; // NOCS // NOPMD (required for the cast not being removed by Java 1.6 editors)

		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		Assert.assertTrue(ctrlJMX.isMonitoringEnabled());

		Assert.assertTrue(ctrlJMX.disableMonitoring());

		Assert.assertFalse(monitoringController.isMonitoringEnabled());
		Assert.assertFalse(ctrlJMX.isMonitoringEnabled());

		jmx.close();
	}

	@Override
	protected void inspectRecords(final List<IMonitoringRecord> eventsPassedToController, final List<IMonitoringRecord> eventFromMonitoringLog) throws Exception {
		Assert.assertEquals("Unexpected set of records", eventsPassedToController, eventFromMonitoringLog);
	}

	@Override
	protected boolean terminateBeforeLogInspection() {
		return false;
	}

	@Override
	protected List<IMonitoringRecord> readEvents() throws AnalysisConfigurationException {
		return this.sinkFilter.getList();
	}
}
