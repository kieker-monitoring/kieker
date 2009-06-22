package kieker.tpmon.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import kieker.tpmon.core.TpmonController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Use this listener in webapps where TpmonShutdownHook is not called
 * 
 * @author Dennis Kieselhorst
 *
 */
public class KiekerServletContextListener implements ServletContextListener {
	private static final Log LOG = LogFactory.getLog(KiekerServletContextListener.class); 

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent evt) {
		TpmonController.getInstance().terminateMonitoring();
		LOG.info("context destroyed");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent evt) {
		LOG.info("context initialized");
	}

}
