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

package kieker.examples.userguide.appendixSigar;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.system.CPUUtilizationRecord;
import kieker.common.record.system.MemSwapUsageRecord;
import kieker.tools.util.LoggingTimestampConverter;

public final class AnalysisStarter {

	private AnalysisStarter() {}

	public static void main(final String[] args) {

		if (args.length == 0) {
			System.err.println("No directory given as argument");
			System.exit(1);
		}

		// Create Kieker.Analysis instance
		final IAnalysisController analysisInstance = new AnalysisController();
		// Create and register our own consumer
		final StdOutDumpConsumer consumer = new StdOutDumpConsumer(new Configuration(), analysisInstance);

		// Set filesystem monitoring log input directory for our analysis
		final Configuration readerConfiguration = new Configuration();
		final String[] inputDirs = { args[0] };
		readerConfiguration.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(inputDirs));
		final FSReader fsReader = new FSReader(readerConfiguration, analysisInstance);

		try {
			// Connect both components.
			analysisInstance.connect(fsReader, FSReader.OUTPUT_PORT_NAME_RECORDS, consumer, StdOutDumpConsumer.INPUT_PORT_NAME);
			// Start the analysis
			analysisInstance.run();
		} catch (final AnalysisConfigurationException e) {
			e.printStackTrace();
		}
	}
}

@Plugin
class StdOutDumpConsumer extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME = "newMonitoringRecord";

	public StdOutDumpConsumer(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@InputPort(
			name = StdOutDumpConsumer.INPUT_PORT_NAME,
			eventTypes = { IMonitoringRecord.class })
	public void newMonitoringRecord(final Object record) {
		if (record instanceof CPUUtilizationRecord) {
			final CPUUtilizationRecord cpuUtilizationRecord =
					(CPUUtilizationRecord) record;

			final String hostname = cpuUtilizationRecord.getHostname();
			final String cpuId = cpuUtilizationRecord.getCpuID();
			final double utilizationPercent = cpuUtilizationRecord.getTotalUtilization() * 100;

			System.out
					.println(String.format(
							"%s: [CPU] host: %s ; cpu-id: %s ; utilization: %3.2f %%",
							LoggingTimestampConverter
									.convertLoggingTimestampToUTCString(cpuUtilizationRecord
											.getTimestamp()),
							hostname, cpuId, utilizationPercent));
		} else if (record instanceof MemSwapUsageRecord) {
			final MemSwapUsageRecord memSwapUsageRecord =
					(MemSwapUsageRecord) record;

			final String hostname = memSwapUsageRecord.getHostname();
			final double memUsageMB = memSwapUsageRecord.getMemUsed() / (1024d * 1024d);
			final double swapUsageMB = memSwapUsageRecord.getSwapUsed() / (1024d * 1024d);

			System.out
					.println(String.format(
							"%s: [Mem/Swap] host: %s ; mem usage: %s MB ; swap usage: %s MB",
							LoggingTimestampConverter
									.convertLoggingTimestampToUTCString(memSwapUsageRecord
											.getTimestamp()),
							hostname, memUsageMB, swapUsageMB));
		} // else Unexpected record type
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
