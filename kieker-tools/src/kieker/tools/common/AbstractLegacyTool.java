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
package kieker.tools.common;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import kieker.common.exception.ConfigurationException;
import kieker.monitoring.core.configuration.ConfigurationFactory;

/**
 * Generic legacy tool framework class.
 *
 * @param <T>
 *            type of the configuration object to be used
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public abstract class AbstractLegacyTool<T extends Object> {

	/** Exit code for successful operation. */
	public static final int SUCCESS_EXIT_CODE = 0;
	/** An runtime error happened. */
	public static final int RUNTIME_ERROR = 1;
	/** There was an configuration error. */
	public static final int CONFIGURATION_ERROR = 2;
	/** There was a parameter error. */
	public static final int PARAMETER_ERROR = 3;
	/** Displayed the usage message. */
	public static final int USAGE_EXIT_CODE = 4;

	/** logger for all tools. */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName()); // NOPMD logging must not be static, confuses user

	/** true if help should be displayed. */
	protected boolean help = false; // NOPMD this is set to false for documentation purposes
	/** configuration specified as parameters. */
	protected T settings;
	/** configuration provided as kieker configuration file. */
	protected kieker.common.configuration.Configuration kiekerConfiguration;

	/**
	 * Default constructor.
	 */
	public AbstractLegacyTool() {
		// nothing to do
	}

	/**
	 * Configure and execute the evaluation tool utilizing an external configuration.
	 *
	 * @param title
	 *            start up label for debug messages
	 * @param label
	 *            label used during execution to indicate the running service
	 * @param args
	 *            arguments are ignored
	 * @param configuration
	 *            configuration object
	 *
	 * @return returns exit code
	 */
	public int run(final String title, final String label, final String[] args, final T configuration) {
		this.settings = configuration;
		this.logger.debug(title);

		final JCommander commander = new JCommander(configuration);
		try {
			commander.parse(args);
			if (this.checkParameters(commander)) {
				if (this.help) {
					commander.usage();
					return USAGE_EXIT_CODE;
				} else {
					this.kiekerConfiguration = this.readConfiguration();

					if (this.checkConfiguration(this.kiekerConfiguration, commander)) {
						return this.execute(commander, label);
					} else {
						return CONFIGURATION_ERROR;
					}
				}
			} else {
				this.logger.error("Configuration Error"); // NOPMD
				return CONFIGURATION_ERROR;
			}
		} catch (final ParameterException e) {
			this.logger.error(e.getLocalizedMessage()); // NOPMD
			commander.usage();
			return PARAMETER_ERROR;
		} catch (final ConfigurationException e) {
			this.logger.error(e.getLocalizedMessage()); // NOPMD
			commander.usage();
			return CONFIGURATION_ERROR;
		}
	}

	/**
	 * Execute the core part of a tool or service.
	 *
	 * @param commander
	 *            JCommander instance used to display usage information in case of errors
	 * @param label
	 *            label used during execution to indicate the running service
	 *
	 * @return returns exit code
	 * @throws ConfigurationException
	 *             on configuration errors occuring at runtime
	 */
	protected abstract int execute(final JCommander commander, final String label) throws ConfigurationException;

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
	 * Trigger cleanup features of the service.
	 */
	protected abstract void shutdownService();

}
