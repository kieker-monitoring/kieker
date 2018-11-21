/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.common;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import kieker.monitoring.core.configuration.ConfigurationFactory;

/**
 * Generic service main class.
 *
 * @param <T>
 *            type of the teetime Configuration to be used
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public abstract class AbstractToolMain<T extends Object> {

	/** logger for all tools. */
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractToolMain.class);
	protected boolean help = false; // NOPMD this is set to false for documentation purposes
	protected T settings;

	/**
	 * Configure and execute the evaluation tool utilizing an external configuration.
	 *
	 * @param <R>
	 *            configuration object type
	 *
	 * @param title
	 *            start up label for debug messages
	 * @param label
	 *            label used during execution
	 * @param args
	 *            arguments are ignored
	 * @param configuration
	 *            configuration object
	 */
	public void run(final String title, final String label, final String[] args, final T configuration) {
		this.settings = configuration;
		AbstractToolMain.LOGGER.debug(title); // NOPMD

		final JCommander commander = new JCommander(configuration);
		try {
			commander.parse(args);
			if (this.checkParameters(commander)) {
				if (this.help) {
					commander.usage();
					System.exit(1); // NOPMD not a J2EE app
				} else {
					final kieker.common.configuration.Configuration kiekerConfiguration = this.readConfiguration();

					if (this.checkConfiguration(kiekerConfiguration, commander)) {
						this.execute(commander, label);
					}
				}
			} else {
				AbstractToolMain.LOGGER.error("Configuration Error"); // NOPMD
			}
		} catch (final ParameterException e) {
			AbstractToolMain.LOGGER.error(e.getLocalizedMessage()); // NOPMD
			commander.usage();
		} catch (final ConfigurationException e) {
			AbstractToolMain.LOGGER.error(e.getLocalizedMessage()); // NOPMD
			commander.usage();
		}
	}

	/**
	 * Execute the core part of a tool or service.
	 *
	 * @param commander
	 *            JCommander instance used to display usage information in case of errors
	 * @param label
	 *            additional label
	 * @throws ConfigurationException
	 *             on configuration errors occuring at runtime
	 */
	protected abstract void execute(final JCommander commander, final String label) throws ConfigurationException;

	/**
	 * Read a configuration form a file.
	 *
	 * @return returns a complete Kieker configuration
	 */
	protected kieker.common.configuration.Configuration readConfiguration() {
		if (this.getConfigurationFile() != null) { // NOPMD
			return ConfigurationFactory.createConfigurationFromFile(this.getConfigurationFile().getAbsolutePath()); // NOPMD
		} else {
			return null;
		}
	}

	/**
	 * Method returning the configuration file handle.
	 *
	 * @return returns a file handle in case a configuration file is used, else null
	 */
	protected abstract File getConfigurationFile();

	/**
	 * Check a given configuration for validity.
	 *
	 * @param configuration
	 *            the configuration object with all configuration parameter. Can be null.
	 * @param commander
	 *            JCommander used to generate usage information.
	 * @return true if the configuration is valid.
	 */
	protected abstract boolean checkConfiguration(kieker.common.configuration.Configuration configuration,
			JCommander commander);

	/**
	 * Check all given parameters for correct directory and files path, as well as, all other values
	 * for fitness.
	 *
	 * @param commander
	 *            the command line interface
	 * @return true if all parameter check out, else false
	 *
	 * @throws ConfigurationException
	 *             on error
	 */
	protected abstract boolean checkParameters(JCommander commander) throws ConfigurationException;

	/**
	 * Shutdown cleanup features of the application.
	 */
	protected abstract void shutdownService();

}
