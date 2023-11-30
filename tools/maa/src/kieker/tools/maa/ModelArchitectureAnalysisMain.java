/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.maa;

import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;

/**
 * @author Reiner Jung
 *
 */
public class ModelArchitectureAnalysisMain extends AbstractService<TeetimeConfiguration, Settings> {

	public static void main(final String[] args) {
		final ModelArchitectureAnalysisMain main = new ModelArchitectureAnalysisMain();
		try {
			final int exitCode = main.run("model architecture analysis", "maa", args, new Settings());
			System.exit(exitCode);
		} catch (final IllegalArgumentException e) {
			LoggerFactory.getLogger(ModelArchitectureAnalysisMain.class).error("Configuration error: {}",
					e.getLocalizedMessage());
			System.exit(1);
		}
	}

	@Override
	protected TeetimeConfiguration createTeetimeConfiguration() throws ConfigurationException {
		return new TeetimeConfiguration(this.settings);
	}

	@Override
	protected Path getConfigurationPath() {
		// we do not use a configuration file
		return null;
	}

	@Override
	protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
		return true;
	}

	@Override
	protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
		if (!Files.isDirectory(this.settings.getInputModelPath())) {
			this.logger.error("Input path '{}' cannot be found.", this.settings.getInputModelPath().toString());
			return false;
		}
		if (!Files.isDirectory(this.settings.getOutputModelPath())) {
			this.logger.error("Output path '{}' cannot be found.", this.settings.getOutputModelPath().toString());
			return false;
		}
		if ((this.settings.getMapFiles() != null) && (this.settings.getSeparator() == null)) {
			this.logger.error("Missing separator string for grouping CSV file.");
			return false;
		}

		return true;
	}

	@Override
	protected void shutdownService() {
		// nothing to be done here
	}

}
