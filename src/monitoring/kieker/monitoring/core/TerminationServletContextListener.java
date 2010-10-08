package kieker.monitoring.core;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Use this listener in webapps where TpmonShutdownHook is not called.
 * 
 * @author Dennis Kieselhorst
 * 
 */
public class TerminationServletContextListener implements
		ServletContextListener {
	private static final Log LOG = LogFactory
			.getLog(TerminationServletContextListener.class);

	private final MonitoringController ctrl;

	public TerminationServletContextListener() {
		this.ctrl = MonitoringController.getInstance();
	}

	public TerminationServletContextListener(final MonitoringController ctrl) {
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
		TerminationServletContextListener.LOG.info("context destroyed");
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
		TerminationServletContextListener.LOG.info("context initialized");
	}

}
