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

package kieker.test.tools.junit.writeRead.jms;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.jms.JMSReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writernew.jms.JmsWriter;

import kieker.test.tools.junit.writeRead.AbstractWriterReaderTest;

/**
 * @author Nils Christian Ehmke
 *
 * @since 1.8
 */
public class BasicJMSWriterReaderTest extends AbstractWriterReaderTest { // NOPMD NOCS (TestClassWithoutTestCases)

	private volatile ListCollectionFilter<IMonitoringRecord> sinkFilter = null; // NOPMD (init for findbugs)

	@Override
	protected MonitoringController createController(final int numRecordsWritten) throws IllegalStateException, AnalysisConfigurationException,
			InterruptedException {
		final AnalysisController analysisController = new AnalysisController();

		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, JmsWriter.class.getName());
		config.setProperty(JmsWriter.CONFIG_CONTEXTFACTORYTYPE, FakeInitialContextFactory.class.getName());
		config.setProperty(JmsWriter.CONFIG_FACTORYLOOKUPNAME, "ConnectionFactory");
		final MonitoringController ctrl = MonitoringController.createInstance(config);
		Thread.sleep(1000);
		final Configuration jmsReaderConfig = new Configuration();
		jmsReaderConfig.setProperty(JMSReader.CONFIG_PROPERTY_NAME_FACTORYLOOKUP, FakeInitialContextFactory.class.getName());

		final JMSReader jmsReader = new JMSReader(jmsReaderConfig, analysisController);
		this.sinkFilter = new ListCollectionFilter<IMonitoringRecord>(new Configuration(), analysisController);

		analysisController.connect(jmsReader, JMSReader.OUTPUT_PORT_NAME_RECORDS, this.sinkFilter, ListCollectionFilter.INPUT_PORT_NAME);
		final AnalysisControllerThread analysisThread = new AnalysisControllerThread(analysisController);
		analysisThread.start();
		Thread.sleep(1000);
		return ctrl;
	}

	@Override
	protected void checkControllerStateBeforeRecordsPassedToController(final IMonitoringController monitoringController) {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
	}

	@Override
	protected void checkControllerStateAfterRecordsPassedToController(final IMonitoringController monitoringController) {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		monitoringController.disableMonitoring();
		Assert.assertFalse(monitoringController.isMonitoringEnabled());
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

	/**
	 * This test makes sure that the read method of the JMSReader returns true (this was a bug, reported in #758).
	 */
	@Test
	public void testReadReturnsTrue() {
		final IAnalysisController ac = new AnalysisController();

		final Configuration jmsReaderConfig = new Configuration();
		jmsReaderConfig.setProperty(JMSReader.CONFIG_PROPERTY_NAME_FACTORYLOOKUP, FakeInitialContextFactory.class.getName());
		final JMSReader jmsReader = new JMSReader(jmsReaderConfig, ac);

		jmsReader.terminate(false);

		Assert.assertTrue(jmsReader.read());
	}
}
