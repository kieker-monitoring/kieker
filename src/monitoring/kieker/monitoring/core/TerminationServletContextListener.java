package kieker.monitoring.core;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Use this listener in webapps where TpmonShutdownHook is not called
 * 
 * @author Dennis Kieselhorst
 *
 */
public class TerminationServletContextListener implements ServletContextListener {
	private static final Log LOG = LogFactory.getLog(TerminationServletContextListener.class);

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent evt) {
		MonitoringController.getInstance().terminate();
		LOG.info("context destroyed");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent evt) {
		LOG.info("context initialized");
	}

}
