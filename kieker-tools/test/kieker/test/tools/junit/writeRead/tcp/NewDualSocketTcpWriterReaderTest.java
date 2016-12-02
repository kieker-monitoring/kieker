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

package kieker.test.tools.junit.writeRead.tcp;

import java.util.List;

import org.junit.Assert;

import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.tcp.DualSocketTcpReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writernew.tcp.TCPWriter;

import kieker.test.tools.junit.writeRead.AbstractWriterReaderTest;

/**
 * @author Jan Waller
 *
 * @since 1.5
 */
public class NewDualSocketTcpWriterReaderTest extends AbstractWriterReaderTest { // NOPMD NOCS (TestClassWithoutTestCases)

	private static final String PORT1 = "10333";
	private static final String PORT2 = "10334";

	private volatile ListCollectionFilter<IMonitoringRecord> sinkFilter = null; // NOPMD (init for findbugs)
	private volatile AnalysisController analysisController = null; // NOPMD (init for findbugs)
	private volatile AnalysisControllerThread analysisThread = null; // NOPMD (init for findbugs)

	@Override
	protected IMonitoringController createController(final int numRecordsWritten) throws IllegalStateException, AnalysisConfigurationException,
			InterruptedException {
		this.analysisController = new AnalysisController();

		final Configuration readerConfig = new Configuration();
		readerConfig.setProperty(DualSocketTcpReader.CONFIG_PROPERTY_NAME_PORT1, NewDualSocketTcpWriterReaderTest.PORT1);
		readerConfig.setProperty(DualSocketTcpReader.CONFIG_PROPERTY_NAME_PORT2, NewDualSocketTcpWriterReaderTest.PORT2);
		final DualSocketTcpReader tcpReader = new DualSocketTcpReader(readerConfig, this.analysisController);
		this.sinkFilter = new ListCollectionFilter<IMonitoringRecord>(new Configuration(), this.analysisController);
		this.analysisController.connect(tcpReader, DualSocketTcpReader.OUTPUT_PORT_NAME_RECORDS, this.sinkFilter, ListCollectionFilter.INPUT_PORT_NAME);
		this.analysisThread = new AnalysisControllerThread(this.analysisController);
		this.analysisThread.start();

		Thread.sleep(1000);

		final Configuration monitoringConfig = ConfigurationFactory.createDefaultConfiguration();
		monitoringConfig.setProperty(ConfigurationFactory.WRITER_CLASSNAME, TCPWriter.class.getName());
		monitoringConfig.setProperty(TCPWriter.CONFIG_PORT1, NewDualSocketTcpWriterReaderTest.PORT1);
		monitoringConfig.setProperty(TCPWriter.CONFIG_PORT2, NewDualSocketTcpWriterReaderTest.PORT2);
		return MonitoringController.createInstance(monitoringConfig);
	}

	@Override
	protected void checkControllerStateBeforeRecordsPassedToController(final IMonitoringController monitoringController) throws Exception {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		Assert.assertEquals(AnalysisController.STATE.RUNNING, this.analysisController.getState());
	}

	@Override
	protected void checkControllerStateAfterRecordsPassedToController(final IMonitoringController monitoringController) throws Exception {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		monitoringController.terminateMonitoring();
		this.analysisThread.awaitTermination();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, this.analysisController.getState());
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
