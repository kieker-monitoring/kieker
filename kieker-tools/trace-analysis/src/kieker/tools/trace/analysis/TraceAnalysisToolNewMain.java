/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.trace.analysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.beust.jcommander.JCommander;

import kieker.analysis.architecture.trace.InvalidEventRecordTraceCounter;
import kieker.analysis.architecture.trace.ValidEventRecordTraceCounter;
import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.model.repository.SystemModelRepository;
import kieker.tools.common.AbstractService;

/**
 * This is the main class to start the Kieker TraceAnalysisTool - the model synthesis and analysis
 * tool to process the monitoring data that comes from the instrumented system, or from a file that
 * contains Kieker monitoring data. The Kieker TraceAnalysisTool can produce output such as
 * sequence diagrams, dependency graphs on demand. Alternatively it can be used continuously for
 * online performance analysis, anomaly detection or live visualization of system behavior.
 *
 * This is the trace analysis main class built upon TeeTime.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class TraceAnalysisToolNewMain extends AbstractService<TraceAnalysisConfiguration, TraceAnalysisParameters> {

	private final SystemModelRepository systemRepository = new SystemModelRepository();

	private TraceAnalysisConfiguration teetimeConfiguration;

	public TraceAnalysisToolNewMain() {
		// Nothing to do here
	}

	/**
	 * Configure and execute the TCP Kieker data collector.
	 *
	 * @param args
	 *            arguments are ignored
	 */
	public static void main(final String[] args) {
		final TraceAnalysisToolNewMain tool = new TraceAnalysisToolNewMain();
		final int result = tool.run("Trace Analysis Tool", "trace-analysis", args, new TraceAnalysisParameters());

		if (tool.settings.isPrintSystemModel()) {
			final Path systemModelPath;
			try {
				systemModelPath = Paths.get(tool.settings.getOutputDir().getCanonicalPath(), "system-entities.html");

				try {
					tool.getSystemRepository().saveSystemToHTMLFile(systemModelPath);
					if (tool.teetimeConfiguration.getTraceReconstructionStage() != null) {
						tool.teetimeConfiguration.getTraceReconstructionStage().printStatusMessage();
					}
					final ValidEventRecordTraceCounter validTraceCounter = tool.teetimeConfiguration.getValidEventRecordTraceCounter();
					final InvalidEventRecordTraceCounter invalidTraceCounter = tool.teetimeConfiguration.getInvalidEventRecordTraceCounter();
					if (validTraceCounter != null) {
						tool.logger.debug("");
						tool.logger.debug("#");
						tool.logger.debug("# Plugin: {}", validTraceCounter.getClass().getName());
						final int total = validTraceCounter.getTotalCount() + invalidTraceCounter.getTotalCount();
						tool.logger.debug("Trace processing summary: {} total; {} succeeded; {} failed.",
								total, validTraceCounter.getSuccessCount(), invalidTraceCounter.getErrorCount());
					}
					if (tool.teetimeConfiguration.getTraceEventRecords2ExecutionAndMessageTraceStage() != null) {
						tool.teetimeConfiguration.getTraceEventRecords2ExecutionAndMessageTraceStage().printStatusMessage();
					}
				} catch (final IOException e) {
					tool.logger.error("Cannot save system model in {}: {}", systemModelPath.toString(), e.getLocalizedMessage());
				}
			} catch (final IOException e1) {
				tool.logger.error("Cannot compose path: {}", e1.getLocalizedMessage());
			}

		}
		System.exit(result);
	}

	protected SystemModelRepository getSystemRepository() {
		return this.systemRepository;
	}

	@Override
	protected TraceAnalysisConfiguration createTeetimeConfiguration() throws ConfigurationException {
		this.teetimeConfiguration = new TraceAnalysisConfiguration(this.settings, this.systemRepository);
		return this.teetimeConfiguration;
	}

	@Override
	protected Path getConfigurationPath() {
		return null;
	}

	@Override
	protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
		return true;
	}

	@Override
	protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
		for (final File inputFile : this.settings.getInputDirs()) {
			if (!inputFile.isDirectory()) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void shutdownService() {
		// nothing special to do here
	}
}
