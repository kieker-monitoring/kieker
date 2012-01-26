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

package kieker.examples.userguide.appendixSigar;

import java.util.HashMap;
import java.util.Map;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.record.CPUUtilizationRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.MemSwapUsageRecord;
import kieker.tools.util.LoggingTimestampConverter;

public class AnalysisStarter {

	public static void main(final String[] args) {

		if (args.length == 0) {
			return;
		}

		/* Create Kieker.Analysis instance */
		final AnalysisController analysisInstance = new AnalysisController();
		/* Create a register our own consumer */
		final StdOutDumpConsumer consumer = new StdOutDumpConsumer(new Configuration(), new HashMap<String, AbstractRepository>());
		analysisInstance.registerPlugin(consumer);

		/* Set filesystem monitoring log input directory for our analysis */
		final Configuration readerConfiguration = new Configuration();
		final String inputDirs[] = { args[0] };
		readerConfiguration.setProperty(FSReader.CONFIG_INPUTDIRS, Configuration.toProperty(inputDirs));
		final FSReader fsReader = new FSReader(readerConfiguration, new HashMap<String, AbstractRepository>());
		analysisInstance.setReader(fsReader);

		/* Connect both components. */
		AbstractPlugin.connect(fsReader, FSReader.OUTPUT_PORT_NAME, consumer, StdOutDumpConsumer.INPUT_PORT_NAME);

		/* Start the analysis */
		analysisInstance.run();
	}
}

class StdOutDumpConsumer extends AbstractAnalysisPlugin {

	public static final String INPUT_PORT_NAME = "newMonitoringRecord";

	public StdOutDumpConsumer(final Configuration configuration, final Map<String, AbstractRepository> repositories) {
		super(configuration, repositories);
	}

	@InputPort(eventTypes = { IMonitoringRecord.class })
	public void newMonitoringRecord(final Object record) {
		if (record instanceof CPUUtilizationRecord) {
			final CPUUtilizationRecord cpuUtilizationRecord =
					(CPUUtilizationRecord) record;

			final String hostName = cpuUtilizationRecord.getHostName();
			final String cpuId = cpuUtilizationRecord.getCpuID();
			final double utilizationPercent = cpuUtilizationRecord.getTotalUtilization() * 100;

			System.out
					.println(String.format(
							"%s: [CPU] host: %s ; cpu-id: %s ; utilization: %3.2f %%",
							LoggingTimestampConverter
									.convertLoggingTimestampToUTCString(cpuUtilizationRecord
											.getTimestamp()),
							hostName, cpuId, utilizationPercent));
		} else if (record instanceof MemSwapUsageRecord) {
			final MemSwapUsageRecord memSwapUsageRecord =
					(MemSwapUsageRecord) record;

			final String hostName = memSwapUsageRecord.getHostName();
			final double memUsageMB = memSwapUsageRecord.getMemUsed() / (1024 * 1024);
			final double swapUsageMB = memSwapUsageRecord.getSwapUsed() / (1024 * 1024);

			System.out
					.println(String.format(
							"%s: [Mem/Swap] host: %s ; mem usage: %s MB ; swap usage: %s MB",
							LoggingTimestampConverter
									.convertLoggingTimestampToUTCString(memSwapUsageRecord
											.getTimestamp()),
							hostName, memUsageMB, swapUsageMB));
		} else {
			/* Unexpected record type */
		}
	}

	@Override
	public boolean execute() {
		// Nothing to do
		return true;
	}

	@Override
	public void terminate(final boolean error) {
		// Nothing to do
	}

	@Override
	public Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@Override
	public Map<String, AbstractRepository> getCurrentRepositories() {
		return new HashMap<String, AbstractRepository>();
	}

	@Override
	public Map<String, AbstractRepository> getDefaultRepositories() {
		return new HashMap<String, AbstractRepository>();
	}

}
