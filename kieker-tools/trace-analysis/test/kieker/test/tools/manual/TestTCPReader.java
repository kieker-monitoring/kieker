/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.manual;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.AnalysisThroughputFilter;
import kieker.analysis.plugin.filter.forward.TeeFilter;
import kieker.analysis.plugin.reader.tcp.TCPReader;
import kieker.analysis.plugin.reader.timer.TimeReader;
import kieker.common.configuration.Configuration;

// Command-Line:
// java -javaagent:lib/kieker-1.15-SNAPSHOT-aspectj.jar -Dkieker.monitoring.writer=kieker.monitoring.writer.tcp.TCPWriter
// -Dkieker.monitoring.writer.tcp.TCPWriter.QueueFullBehavior=1 -jar MooBench.jar --recursiondepth 10 --totalthreads 1 --methodtime 0 --output-filename raw.csv
// --totalcalls 10000000
/**
 *
 * @author Jan Waller
 *
 * @since 1.8
 */
public final class TestTCPReader {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestTCPReader.class);

	private static final String KAX_FILENAME = "tmp/testproject.kax";

	private TestTCPReader() {}

	public static void main(final String[] args) {
		final IAnalysisController analysisController = new AnalysisController("TCPThroughput");
		TestTCPReader.createAndConnectPlugins(analysisController);
		try {
			analysisController.run();
		} catch (final AnalysisConfigurationException ex) {
			TestTCPReader.LOGGER.error("Failed to start the example project.", ex);
		}
	}

	private static void createAndConnectPlugins(final IAnalysisController analysisController) {
		final Configuration readerConfig = new Configuration();
		// readerConfig.setProperty(TCPReader.CONFIG_PROPERTY_NAME_PORT1, 10333);
		// readerConfig.setProperty(TCPReader.CONFIG_PROPERTY_NAME_PORT2, 10334);
		final TCPReader reader = new TCPReader(readerConfig, analysisController);

		final Configuration timerConfig = new Configuration();
		final TimeReader timer = new TimeReader(timerConfig, analysisController);

		// TeeFilter
		final Configuration confTeeFilter1 = new Configuration();
		confTeeFilter1.setProperty(TeeFilter.CONFIG_PROPERTY_NAME_STREAM, TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_STDOUT);
		confTeeFilter1.setProperty(TeeFilter.CONFIG_PROPERTY_NAME_STREAM, TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_NULL);
		final TeeFilter teeFilter1 = new TeeFilter(confTeeFilter1, analysisController);

		// CountingFilter
		final AnalysisThroughputFilter countingFilter1 = new AnalysisThroughputFilter(new Configuration(), analysisController);

		// TeeFilter
		final Configuration confTeeFilter2 = new Configuration();
		confTeeFilter1.setProperty(TeeFilter.CONFIG_PROPERTY_NAME_STREAM, TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_STDOUT);
		// confTeeFilter1.setProperty(TeeFilter.CONFIG_PROPERTY_NAME_STREAM, TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_NULL);
		final TeeFilter teeFilter2 = new TeeFilter(confTeeFilter2, analysisController);

		try { // connect everything
			analysisController.connect(reader, TCPReader.OUTPUT_PORT_NAME_RECORDS, teeFilter1, TeeFilter.INPUT_PORT_NAME_EVENTS);
			analysisController.connect(timer, TimeReader.OUTPUT_PORT_NAME_TIMESTAMPS, countingFilter1, AnalysisThroughputFilter.INPUT_PORT_NAME_TIME);
			analysisController.connect(teeFilter1, TeeFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, countingFilter1, AnalysisThroughputFilter.INPUT_PORT_NAME_OBJECTS);
			analysisController.connect(countingFilter1, AnalysisThroughputFilter.OUTPUT_PORT_NAME_THROUGHPUT, teeFilter2, TeeFilter.INPUT_PORT_NAME_EVENTS);
		} catch (final AnalysisConfigurationException ex) {
			TestTCPReader.LOGGER.error("Failed to wire the example project.", ex);
		}
		try {
			analysisController.saveToFile(new File(TestTCPReader.KAX_FILENAME));
		} catch (final IOException ex) {
			TestTCPReader.LOGGER.error("Failed to save configuration to {}", TestTCPReader.KAX_FILENAME, ex);
		} catch (final AnalysisConfigurationException ex) {
			TestTCPReader.LOGGER.error("Failed to save configuration to {}", TestTCPReader.KAX_FILENAME, ex);
		}
	}
}
