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

import com.beust.jcommander.JCommander;

import teetime.framework.Configuration;
import teetime.framework.Execution;

/**
 * Generic service main class.
 *
 * @param <T>
 *            type of the teetime configuration to be used
 * @param <R>
 *            type of the parameter configuration object
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public abstract class AbstractTeetimeTool<T extends Configuration, R extends Object> extends AbstractTool<R> {

	@Override
	protected int execute(final JCommander commander, final String label) throws ConfigurationException {
		this.kiekerConfiguration = this.readConfiguration();

		if (this.checkConfiguration(this.kiekerConfiguration, commander)) {
			final Execution<T> execution = new Execution<>(this.createTeetimeConfiguration());

			this.shutdownHook(execution);

			AbstractTool.LOGGER.debug("Running {}", label); // NOPMD LoD sucks

			execution.executeBlocking();
			this.shutdownService();

			AbstractTool.LOGGER.debug("Done"); // NOPMD LoD sucks

			return SUCCESS_EXIT_CODE;
		} else {
			return CONFIGURATION_ERROR;
		}
	}

	/**
	 * General shutdown hook for services and tools.
	 *
	 * @param execution
	 *            teetime execution
	 */
	private void shutdownHook(final Execution<T> execution) {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() { // NOPMD is not a web app
			/**
			 * Thread to gracefully terminate service.
			 */
			@Override
			public void run() {
				try {
					synchronized (execution) {
						execution.abortEventually(); // TODO replace by different termination logic
						AbstractTeetimeTool.this.shutdownService(); // NOPMD
					}
				} catch (final Exception e) { // NOCS NOPMD

				}
			}
		}));

	}

	/**
	 * Method returning the configuration file handle.
	 *
	 * @return returns a file handle in case a configuration file is used, else null
	 */
	@Override
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
	@Override
	protected abstract boolean checkConfiguration(kieker.common.configuration.Configuration configuration,
			JCommander commander);

	/**
	 * Create and initialize teetime configuration for a service.
	 *
	 * @return return the newly created service
	 *
	 * @throws ConfigurationException
	 *             in case the creation fails
	 */
	protected abstract T createTeetimeConfiguration()
			throws ConfigurationException;

}
