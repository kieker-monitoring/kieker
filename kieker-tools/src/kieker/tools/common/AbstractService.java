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

import com.beust.jcommander.JCommander;

import kieker.analysis.common.ConfigurationException;

import teetime.framework.Configuration;
import teetime.framework.Execution;

/**
 * Generic tool and service framework class. A specific tool or service must implement the following functions:
 * <ul>
 * <li><code>protected T createTeetimeConfiguration()</code></li>
 * <li><code>protected boolean checkParameters(final JCommander commander)</code></li>
 * <li><code>protected File getConfigurationFile()</code></li>
 * <li><code>protected boolean checkConfiguration(final Configuration kiekerConfiguration, final JCommander commander)</code></li>
 * <li><code>protected void shutdownService()</code></li>
 * </ul>
 *
 * Furthermore, you have to define a main method:
 * <code>
 * public static void main(final String[] args) {
 *       final ExampleMain main = new ExampleMain();
 *       System.exit(main.run("Example Tool", "Log label", args, main));
 * }
 * </code>
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
public abstract class AbstractService<T extends Configuration, R extends Object> extends AbstractLegacyTool<R> {

	/**
	 * Default constructor.
	 */
	public AbstractService() {
		super();
	}

	/**
	 * Execute the tool.
	 *
	 * @param commander
	 *            command line parter JCommander
	 * @param label
	 *            printed to the debug log about what application is running.
	 */
	@Override
	protected int execute(final JCommander commander, final String label) throws ConfigurationException {
		this.kiekerConfiguration = this.readConfiguration();

		if (this.checkConfiguration(this.kiekerConfiguration, commander)) {
			final Execution<T> execution = new Execution<>(this.createTeetimeConfiguration());

			final Thread shutdownThread = this.shutdownHook(execution);

			AbstractLegacyTool.LOGGER.debug("Running {}", label);

			execution.executeBlocking();

			Runtime.getRuntime().removeShutdownHook(shutdownThread);
			this.shutdownService();

			AbstractLegacyTool.LOGGER.debug("Done");

			return AbstractLegacyTool.SUCCESS_EXIT_CODE;
		} else {
			return AbstractLegacyTool.CONFIGURATION_ERROR;
		}
	}

	/**
	 * General shutdown hook for services and tools.
	 *
	 * @param execution
	 *            teetime execution
	 */
	private Thread shutdownHook(final Execution<T> execution) {
		final Thread shutdownThread = new Thread(new Runnable() { // NOPMD is not a web app
			/**
			 * Thread to gracefully terminate service.
			 */
			@Override
			public void run() {
				synchronized (execution) {
					execution.abortEventually();
					AbstractService.this.shutdownService();
				}
			}
		});
		Runtime.getRuntime().addShutdownHook(shutdownThread);
		return shutdownThread;
	}

	/**
	 * Create and initialize teetime configuration for a service.
	 *
	 * @return return the newly created service
	 *
	 * @throws ConfigurationException
	 *             in case the creation fails
	 */
	protected abstract T createTeetimeConfiguration() throws ConfigurationException;

}
