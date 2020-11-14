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
package kieker.examples.analysis.traceanalysis;

import java.io.File;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;
import kieker.tools.common.CommonConfigurationKeys;
import kieker.tools.common.ParameterEvaluationUtils;
import kieker.tools.source.LogsReaderCompositeStage;

/**
 * The collector allows to collect input from different input sources, including TCP and Kieker
 * files. In future, we may add a nice mechanism to add other
 *
 * @author Reiner Jung
 *
 * @since 1.16
 */
public final class TraceAnalysisExampleMain extends AbstractService<TeeTimeConfiguration, TraceAnalysisExampleMain> {

	// Depending on the input, you have to change the input path and the reader configuration (commented with ALTERNATIVE)
	private static final String INPUT_MONITORING_LOG_OER = "./input-data/operation-execution-log/";
	// ALTERNATIVE
	// private static final String INPUT_MONITORING_LOG_TE = "./input-data/trace-event-log/";

	/**
	 * This is a simple main class which does not need to be instantiated.
	 */
	private TraceAnalysisExampleMain() {

	}

	/**
	 * Configure and execute the TCP Kieker data collector.
	 *
	 * @param args
	 *            arguments are ignored
	 */
	public static void main(final String[] args) {
		final CollectorMain collector = new CollectorMain();
		System.exit(collector.run("Collector", "collector", args, collector));
	}

	@Override
	protected TeeTimeConfiguration createTeetimeConfiguration() throws ConfigurationException {
		return new TeeTimeConfiguration(this.kiekerConfiguration);
	}

	@Override
	protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
		return true;
	}

	@Override
	protected File getConfigurationFile() {
		return null;
	}

	@Override
	protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
		configuration.setProperty(LogsReaderCompositeStage.LOG_DIRECTORIES, INPUT_MONITORING_LOG_OER);
		
		return true;
	}

	@Override
	protected void shutdownService() {
		// nothing special to shutdown
	}

}
