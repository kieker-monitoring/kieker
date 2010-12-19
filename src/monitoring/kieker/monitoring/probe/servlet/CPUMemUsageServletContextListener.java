package kieker.monitoring.probe.servlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.core.ScheduledSensorJob;
import kieker.monitoring.probe.sigar.ISigarTriggeredSensorFactory;
import kieker.monitoring.probe.sigar.SigarTriggeredSensorFactory;
import kieker.monitoring.probe.sigar.sensors.CPUsDetailedPercSensor;
import kieker.monitoring.probe.sigar.sensors.MemSwapUsageSensor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Starts and stops the periodic logging of CPU utilization employing the
 * {@link SigarTriggeredSensorFactory} as the Servlet is initialized and
 * destroyed respectively. The statistics are logged with a period of
 * {@value #SENSOR_INTERVAL_SECONDS} seconds.
 * </p>
 * 
 * <p>
 * It can be integrated into a web.xml as follows:<br/>
 * 
 * {@code
 * <listener>
 *   <listener-class>
 *    kieker.monitoring.probe.servlet.CPUMemUsageServletContextListener
 *   </listener-class>
 * </listener>}
 * </p>
 * 
 * @author Andre van Hoorn
 */
public class CPUMemUsageServletContextListener implements
		ServletContextListener {

	private static final Log log = LogFactory
			.getLog(CPUMemUsageServletContextListener.class);

	private final MonitoringController monitoringController =
			MonitoringController.getInstance();

	/**
	 * Stores the {@link ScheduledSensorJob}s which are scheduled in
	 * {@link #contextInitialized(ServletContextEvent)} and removed from the
	 * scheduler in {@link #contextDestroyed(ServletContextEvent)}.
	 */
	private final Collection<ScheduledSensorJob> sensorJobs =
			new ArrayList<ScheduledSensorJob>();

	private static final long SENSOR_INTERVAL_SECONDS = 30;
	private static final long SENSOR_INITIAL_DELAY_SECONDS = 0;

	@Override
	public void contextDestroyed(final ServletContextEvent arg0) {
		for (final ScheduledSensorJob s : this.sensorJobs) {
			this.monitoringController.removePeriodicSensor(s);
		}
	}

	@Override
	public void contextInitialized(final ServletContextEvent arg0) {
		this.initSensors();
	}

	/**
	 * Creates and schedules the {@link ScheduledSensorJob}s and stores them for
	 * later removal in the {@link Collection} {@link #sensorJobs}.
	 */
	private void initSensors() {
		final ISigarTriggeredSensorFactory sigarFactory =
				SigarTriggeredSensorFactory.getInstance();

		// Log utilization of each CPU every 30 seconds
		final CPUsDetailedPercSensor cpuSensor =
				sigarFactory.createSensorCPUsDetailedPerc();
		final ScheduledSensorJob cpuSensorJob =
				this.monitoringController
						.schedulePeriodicSensor(
								cpuSensor,
								CPUMemUsageServletContextListener.SENSOR_INITIAL_DELAY_SECONDS,
								CPUMemUsageServletContextListener.SENSOR_INTERVAL_SECONDS,
								TimeUnit.SECONDS);
		this.sensorJobs.add(cpuSensorJob);

		// Log memory and swap statistics every 30 seconds
		final MemSwapUsageSensor memSensor =
				sigarFactory.createSensorMemSwapUsage();
		final ScheduledSensorJob memSensorJob =
				this.monitoringController
						.schedulePeriodicSensor(
								memSensor,
								CPUMemUsageServletContextListener.SENSOR_INITIAL_DELAY_SECONDS,
								CPUMemUsageServletContextListener.SENSOR_INTERVAL_SECONDS,
								TimeUnit.SECONDS);
		this.sensorJobs.add(memSensorJob);
	}
}
