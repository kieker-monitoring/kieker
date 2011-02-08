package kieker.monitoring.probe.servlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import kieker.monitoring.core.Kieker;
import kieker.monitoring.core.sampler.ScheduledSamplerJob;
import kieker.monitoring.probe.sigar.ISigarSamplerFactory;
import kieker.monitoring.probe.sigar.SigarSamplerFactory;
import kieker.monitoring.probe.sigar.samplers.CPUsDetailedPercSampler;
import kieker.monitoring.probe.sigar.samplers.MemSwapUsageSampler;

/**
 * <p>
 * Starts and stops the periodic logging of CPU utilization employing the {@link SigarSamplerFactory} as the Servlet is
 * initialized and destroyed respectively. The statistics are logged with a period of {@value #SENSOR_INTERVAL_SECONDS}
 * seconds.
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
public class CPUMemUsageServletContextListener implements ServletContextListener {

	private final Kieker monitoringController = Kieker.getInstance();

	/**
	 * Stores the {@link ScheduledSamplerJob}s which are scheduled in {@link #contextInitialized(ServletContextEvent)} and
	 * removed from the
	 * scheduler in {@link #contextDestroyed(ServletContextEvent)}.
	 */
	private final Collection<ScheduledSamplerJob> samplerJobs = new ArrayList<ScheduledSamplerJob>();

	private static final long SENSOR_INTERVAL_SECONDS = 15;
	private static final long SENSOR_INITIAL_DELAY_SECONDS = 0;

	@Override
	public void contextDestroyed(final ServletContextEvent arg0) {
		for (final ScheduledSamplerJob s : this.samplerJobs) {
			this.monitoringController.removeScheduledSampler(s);
		}
	}

	@Override
	public void contextInitialized(final ServletContextEvent arg0) {
		this.initSensors();
	}

	/**
	 * Creates and schedules the {@link ScheduledSamplerJob}s and stores them for
	 * later removal in the {@link Collection} {@link #samplerJobs}.
	 */
	private void initSensors() {
		final ISigarSamplerFactory sigarFactory = SigarSamplerFactory.getInstance();

		// Log utilization of each CPU every 30 seconds
		final CPUsDetailedPercSampler cpuSensor = sigarFactory.createSensorCPUsDetailedPerc();
		final ScheduledSamplerJob cpuSensorJob = this.monitoringController.schedulePeriodicSampler(cpuSensor,
				CPUMemUsageServletContextListener.SENSOR_INITIAL_DELAY_SECONDS,
				CPUMemUsageServletContextListener.SENSOR_INTERVAL_SECONDS, TimeUnit.SECONDS);
		this.samplerJobs.add(cpuSensorJob);

		// Log memory and swap statistics every 30 seconds
		final MemSwapUsageSampler memSensor = sigarFactory.createSensorMemSwapUsage();
		final ScheduledSamplerJob memSensorJob = this.monitoringController.schedulePeriodicSampler(memSensor,
				CPUMemUsageServletContextListener.SENSOR_INITIAL_DELAY_SECONDS,
				CPUMemUsageServletContextListener.SENSOR_INTERVAL_SECONDS, TimeUnit.SECONDS);
		this.samplerJobs.add(memSensorJob);
	}
}
