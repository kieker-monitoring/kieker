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

package kieker.monitoring.core.controller;

import java.util.concurrent.atomic.AtomicBoolean;

import kieker.common.configuration.Configuration;

/**
 * @author Jan Waller
 *
 * @since 1.3
 */
public abstract class AbstractController {
	protected volatile MonitoringController monitoringController;
	private final AtomicBoolean terminated = new AtomicBoolean(false);

	/**
	 * Default constructor.
	 *
	 * @param configuration
	 *            The configuration for this controller.
	 */
	protected AbstractController(final Configuration configuration) { // NOPMD (unused parameter)
		// do nothing but enforce constructor
	}

	/**
	 * Sets and initializes the monitoring controller, if it has not been set yet.
	 *
	 * @param monitoringController
	 *            The monitoring controller.
	 */
	protected final void setMonitoringController(final MonitoringController monitoringController) {
		synchronized (this) {
			if (this.monitoringController == null) {
				this.monitoringController = monitoringController;
				if (!this.monitoringController.isTerminated()) {
					this.init();
				}
			}
		}
	}

	/**
	 * Permanently terminates this controller.
	 *
	 * @return true iff the controller was terminated.
	 *
	 * @see #isTerminated()
	 */
	protected final boolean terminate() {
		if (!this.terminated.getAndSet(true)) {
			this.cleanup();
			if (this.monitoringController != null) {
				this.monitoringController.terminate();
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns whether this controller is terminated.
	 *
	 * @see #terminate()
	 * @return true if terminated
	 */
	protected final boolean isTerminated() {
		return this.terminated.get();
	}

	/**
	 * This method should to the initialization work.
	 */
	protected abstract void init();

	/**
	 * This method should clean up.
	 */
	protected abstract void cleanup();

	@Override
	public abstract String toString();

	/**
	 * This is a helper method trying to find, create and initialize the given class, using its public constructor which accepts a single {@link Configuration}.
	 *
	 * @param c
	 *            This class defines the expected result of the method call.
	 * @param classname
	 *            The name of the class to be created.
	 * @param configuration
	 *            The configuration which will be used to initialize the class in question.
	 *
	 * @return A new and initializes class instance if everything went well.
	 *
	 * @param <C>
	 *            The type of the returned class.
	 */
	protected static final <C> C createAndInitialize(final Class<C> c, final String classname, final Configuration configuration) {
		return ControllerFactory.getInstance(configuration).createAndInitialize(c, classname, configuration);
	}
}
