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

package kieker.analysis.plugin.reader.tcp;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.CountingFilter;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.tcp.TCPWriter;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 */
public class TestTCPReader {

	private static final String RECORD_PORT = "11111";
	private static final String REGISTRY_PORT = "11112";

	@Test
	public void test() throws IllegalStateException, AnalysisConfigurationException, InterruptedException {
		final IAnalysisController analysisController = new AnalysisController();

		final Configuration configuration = new Configuration();
		configuration.setProperty(TCPReader.CONFIG_PROPERTY_NAME_PORT1, RECORD_PORT);
		configuration.setProperty(TCPReader.CONFIG_PROPERTY_NAME_PORT2, REGISTRY_PORT);
		final TCPReader tcpReader = new TCPReader(configuration, analysisController);
		final CountingFilter counter = new CountingFilter(new Configuration(), analysisController);

		analysisController.connect(tcpReader, TCPReader.OUTPUT_PORT_NAME_RECORDS, counter, CountingFilter.INPUT_PORT_NAME_EVENTS);

		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					analysisController.run();
				} catch (final AnalysisConfigurationException e) {
					new IllegalStateException("An exception occurred", e);
				}

			}
		});

		thread.start();

		this.createAndRunLoadDriver();

		thread.join();

		Assert.assertEquals(AnalysisController.STATE.TERMINATED, analysisController.getState());
		final long EXPECTED_NUM_RECORDS = 10;
		Assert.assertEquals(EXPECTED_NUM_RECORDS, counter.getMessageCount());
	}

	void createAndRunLoadDriver() throws IllegalStateException, AnalysisConfigurationException {
		final IAnalysisController analysisController = new AnalysisController();

		final Configuration configurationFSReader = new Configuration();
		final String INPUT_DIR = "test/analysis/" + TestTCPReader.class.getPackage().getName().replaceAll("\\.", "/") + "/log-data";
		configurationFSReader.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, INPUT_DIR);
		final FSReader reader = new FSReader(configurationFSReader, analysisController);

		final ListCollectionFilter<IMonitoringRecord> collectionFilter = new ListCollectionFilter<IMonitoringRecord>(new Configuration(), analysisController);

		analysisController.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, collectionFilter, ListCollectionFilter.INPUT_PORT_NAME);

		analysisController.run();

		final Configuration configuration = new Configuration();
		configuration.setProperty(TCPWriter.CONFIG_HOSTNAME, "localhost");
		configuration.setProperty(TCPWriter.CONFIG_PORT1, RECORD_PORT);
		configuration.setProperty(TCPWriter.CONFIG_PORT2, REGISTRY_PORT);
		configuration.setProperty(TCPWriter.CONFIG_BUFFERSIZE, "1024");
		configuration.setProperty(TCPWriter.CONFIG_FLUSH, "false");
		final TCPWriter tcpWriter = new TCPWriter(configuration);

		for (final IMonitoringRecord record : collectionFilter.getList()) {
			tcpWriter.newMonitoringRecord(record);
		}
	}

}
