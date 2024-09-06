/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.collector;

import java.nio.file.Path;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;
import kieker.tools.common.CommonConfigurationKeys;
import kieker.tools.common.ParameterEvaluationUtils;

/**
 * The collector allows to collect input from different input sources, including TCP and Kieker
 * files. In future, we may add a nice mechanism to add other
 *
 * @author Reiner Jung
 *
 * @since 1.16
 */
public final class CollectorMain extends AbstractService<CollectorConfiguration, CollectorMain> {

	@Parameter(names = { "-c",
		"--configuration" }, required = true, description = "Configuration file.", converter = PathConverter.class)
	private Path configurationPath;

	/**
	 * This is a simple main class which does not need to be instantiated.
	 */
	private CollectorMain() {

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
	protected CollectorConfiguration createTeetimeConfiguration() throws ConfigurationException {
		return new CollectorConfiguration(this.kiekerConfiguration);
	}

	@Override
	protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
		return ParameterEvaluationUtils.isFileReadable(this.configurationPath.toFile(), "Configuration File", commander);
	}

	@Override
	protected Path getConfigurationPath() {
		return this.configurationPath;
	}

	@Override
	protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
		configuration.getStringProperty(CommonConfigurationKeys.SOURCE_STAGE);
		return true;
	}

	@Override
	protected void shutdownService() {
		// nothing special to do here
	}

}
