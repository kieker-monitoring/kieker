/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.core.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.monitoring.core.controller.IStateController;
import kieker.monitoring.core.controller.MonitoringController;

/**
 * Use this listener in webapps where ShutdownHook is not called.
 *
 * @author Dennis Kieselhorst
 *
 * @since 1.3
 */
public class TerminationServletContextListener implements ServletContextListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(TerminationServletContextListener.class);

	private final IStateController ctrl;

	/**
	 * Creates a new instance of this class using the singleton instance of the {@link MonitoringController} as a controller.
	 */
	public TerminationServletContextListener() {
		this.ctrl = MonitoringController.getInstance();
	}

	/**
	 * Create a new instance utilizing an external state controller.
	 *
	 * @param ctrl
	 *            state controller
	 */
	public TerminationServletContextListener(final IStateController ctrl) {
		this.ctrl = ctrl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contextDestroyed(final ServletContextEvent evt) {
		this.ctrl.terminateMonitoring();
		LOGGER.info("context destroyed");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contextInitialized(final ServletContextEvent evt) {
		LOGGER.info("context initialized");
	}
}
