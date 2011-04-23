package kieker.monitoring.core.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import kieker.monitoring.core.controller.IStateController;
import kieker.monitoring.core.controller.MonitoringController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Use this listener in webapps where ShutdownHook is not called.
 * 
 * @author Dennis Kieselhorst
 * 
 */
public class TerminationServletContextListener implements ServletContextListener {
	private static final Log log = LogFactory.getLog(TerminationServletContextListener.class);

	private final IStateController ctrl;

	public TerminationServletContextListener() {
		this.ctrl = MonitoringController.getInstance();
	}

	public TerminationServletContextListener(final IStateController ctrl) {
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
