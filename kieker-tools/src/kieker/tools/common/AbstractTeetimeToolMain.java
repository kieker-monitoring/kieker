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
 *            type of the teetime Configuration to be used
 * @param <R>
 *            type of the parameter configuration object
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public abstract class AbstractTeetimeToolMain<T extends Configuration, R extends Object> extends AbstractToolMain<R> {

	@Override
	protected void execute(final JCommander commander, final String label) throws ConfigurationException {
		final kieker.common.configuration.Configuration configuration = this.readConfiguration();

		if (this.checkConfiguration(configuration, commander)) {
			final Execution<T> execution = new Execution<>(this.createTeetimeConfiguration(configuration));

			this.shutdownHook(execution);

			AbstractToolMain.LOGGER.debug("Running {}", label); // NOPMD LoD sucks

			execution.executeBlocking();
			this.shutdownService();

			AbstractToolMain.LOGGER.debug("Done"); // NOPMD LoD sucks
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
						AbstractTeetimeToolMain.this.shutdownService(); // NOPMD
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
	 * @param configuration
	 *            kieker configuration object, can be null if now configuration
	 * @return return the newly created service
	 *
	 * @throws ConfigurationException
	 *             in case the creation fails
	 */
	protected abstract T createTeetimeConfiguration(kieker.common.configuration.Configuration configuration)
			throws ConfigurationException;

}
