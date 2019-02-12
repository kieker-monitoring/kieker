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

import kieker.common.exception.ConfigurationException;

import teetime.framework.Configuration;
import teetime.framework.Execution;

/**
 * Abstract class to create runnable teetime services.
 *
 * @param <T>
 *            a Teetime configuration
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public abstract class AbstractEmbeddableService<T extends Configuration> implements Runnable {

	private Execution<T> execution;

	/**
	 * Default constructor.
	 */
	public AbstractEmbeddableService() {
		// nothing to be done here
	}

	@Override
	public final void run() {
		try {
			this.execution = new Execution<>(this.createTeetimeConfiguration());
			this.execution.executeBlocking();
			this.shutdownService();
		} catch (final ConfigurationException e) {
			this.logError(e);
		}
	}

	/**
	 * Terminate a running service.
	 */
	public final void terminate() {
		if (this.execution != null) {
			this.execution.abortEventually();
		}
	}

	/**
	 * Log exceptions to the UI.
	 *
	 * @param e
	 *            configuration exception
	 */
	protected abstract void logError(ConfigurationException e);

	/**
	 * Trigger cleanup features of the service.
	 */
	protected abstract void shutdownService();

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
