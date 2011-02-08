package kieker.monitoring.core.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import kieker.monitoring.core.IController;
import kieker.monitoring.core.Kieker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
/**
 * Use this listener in webapps where ShutdownHook is not called.
 * 
 * @author Dennis Kieselhorst
 * 
 */
public class TerminationServletContextListener implements ServletContextListener {
	private static final Log log = LogFactory.getLog(TerminationServletContextListener.class);

	private final IController ctrl;

	public TerminationServletContextListener() {
		this.ctrl = Kieker.getInstance();
	}

	public TerminationServletContextListener(final IController ctrl) {
		this.ctrl = ctrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(final ServletContextEvent evt) {
		this.ctrl.terminateMonitoring();
		TerminationServletContextListener.log.info("context destroyed");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
	 * .ServletContextEvent)
	 */
	@Override
	public void contextInitialized(final ServletContextEvent evt) {
		TerminationServletContextListener.log.info("context initialized");
	}
}
