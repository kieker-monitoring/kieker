/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.monitoring.core.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import kicker.common.logging.Log;
import kicker.common.logging.LogFactory;
import kicker.monitoring.core.controller.IStateController;
import kicker.monitoring.core.controller.MonitoringController;

/**
 * Use this listener in webapps where ShutdownHook is not called.
 * 
 * @author Dennis Kieselhorst
 * 
 * @since 1.3
 */
public class TerminationServletContextListener implements ServletContextListener {
	private static final Log LOG = LogFactory.getLog(TerminationServletContextListener.class);

	private final IStateController ctrl;

	/**
	 * Creates a new instance of this class using the singleton instance of the {@link MonitoringController} as a controller.
	 */
	public TerminationServletContextListener() {
		this.ctrl = MonitoringController.getInstance();
	}

	public TerminationServletContextListener(final IStateController ctrl) {
		this.ctrl = ctrl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contextDestroyed(final ServletContextEvent evt) {
		this.ctrl.terminateMonitoring();
		LOG.info("context destroyed");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contextInitialized(final ServletContextEvent evt) {
		LOG.info("context initialized");
	}
}
