package kieker.monitoring.probe.servlet;

import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import kieker.monitoring.probe.sigar.SigarSensingController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Starts and stops the logging of CPU utilization employing the
 * {@link SigarSensingController} as the Servlet is initialized and destroyed
 * respectively.
 * 
 * TODO: should be moved to Kieker as an example later on.
 * 
 * @author Andre van Hoorn
 * 
 */
public class CPUMemUsageServletContextListener implements
		ServletContextListener {

	private static final Log log = LogFactory
			.getLog(CPUMemUsageServletContextListener.class);

	private volatile SigarSensingController sigarCtrl = null;

	@Override
	public void contextDestroyed(final ServletContextEvent arg0) {
		if (this.sigarCtrl != null) {
			this.sigarCtrl.shutdown();
		}
	}

	@Override
	public void contextInitialized(final ServletContextEvent arg0) {
		this.sigarCtrl = SigarSensingController.getInstance();

		if (this.sigarCtrl == null) {
			CPUMemUsageServletContextListener.log
					.error("Failed to acquire sigar controller instance");
			// will not exit from method but force a NullpointerException
		}

		// Log utilization of each CPU every 30 seconds
		this.sigarCtrl.senseCPUsDetailedPercPeriodic(0, 30, TimeUnit.SECONDS);

		// Log memory and swap statistics every 30 seconds
		this.sigarCtrl.senseMemSwapUsagePeriodic(0, 30, TimeUnit.SECONDS);
	}
}
