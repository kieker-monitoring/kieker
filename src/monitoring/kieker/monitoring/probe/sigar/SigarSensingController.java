package kieker.monitoring.probe.sigar;

import java.util.Hashtable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.probe.sigar.loggerJobs.AbstractSigarLogger;
import kieker.monitoring.probe.sigar.loggerJobs.CPUsCombinedPercLogger;
import kieker.monitoring.probe.sigar.loggerJobs.CPUsDetailedPercLogger;
import kieker.monitoring.probe.sigar.loggerJobs.MemSwapUsageLogger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.Sigar;

/**
 * TODO: A configuration concept like that of the {@link MonitoringController}
 * could be added.
 * 
 * TODO: should be moved to Kieker later on.
 * 
 * TODO: Create JUnit tests!
 * 
 * @author Andre van Hoorn
 */
public class SigarSensingController {

	private static final Log log = LogFactory
			.getLog(SigarSensingController.class);

	/**
	 * Default number of threads used to serve the jobs by the
	 * {@link #poolExecutor}.
	 * 
	 * TODO: Select a meaningful default value.
	 */
	public final static int DEFAULT_EXECUTOR_THREAD_POOL_SIZE = 5;

	/**
	 * Executes the {@link AbstractSigarLogger}s.
	 */
	private final ScheduledThreadPoolExecutor poolExecutor;

	/** Controller responsible for persisting the created records. */
	private final MonitoringController monitoringController;

	/**
	 * http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
	 * 
	 * @author Andre van Hoorn
	 * 
	 */
	private static class LazyHolder {
		/**
		 * The singleton instance of the {@link SigarSensingController}
		 */
		private static final SigarSensingController SINGLETON_INSTANCE =
				new SigarSensingController();
	}

	/**
	 * Returns the singleton instance of the {@link SigarSensingController}
	 * which uses the singleton instance of the {@link MonitoringController
	 * retrieved via {@link MonitoringController#getInstance()}.
	 * 
	 * The size of the internal thread pool used to serve the sensing and
	 * logging jobs is set to
	 * {@link SigarSensingController#DEFAULT_EXECUTOR_THREAD_POOL_SIZE}.
	 */
	public static SigarSensingController getInstance() {
		return LazyHolder.SINGLETON_INSTANCE;
	}

	/**
	 * The name of this controller instance
	 */
	private final String instanceName;

	/**
	 * Returns the instance name.
	 * 
	 * @return
	 */
	public final String getInstanceName() {
		return this.instanceName;
	}

	/**
	 * Name of the singleton instance
	 */
	private final static String SINGLETON_INSTANCE_NAME = "SINGLETON";

	/**
	 * {@link Sigar} instance used to retrieve the data to be logged.
	 */
	private final Sigar sigar;

	/**
	 * Used by {@link #getInstance()} to construct the singleton instance.
	 */
	private SigarSensingController() {
		this(SigarSensingController.SINGLETON_INSTANCE_NAME, new Sigar(),
				MonitoringController.getInstance(),
				SigarSensingController.DEFAULT_EXECUTOR_THREAD_POOL_SIZE);
	}

	/**
	 * Constructs a {@link SigarSensingController} with the given name,
	 * {@link MonitoringController} and pool size used to sense and pool the
	 * {@link AbstractSigarLogger} jobs provided via
	 * {@link #sensePeriodic(AbstractSigarLogger, long, long, TimeUnit)} or
	 * other convenience methods such as
	 * {@link #senseCPUsCombinedPercPeriodic(long, long, TimeUnit)}.
	 * 
	 * @param name
	 * @param monitoringController
	 */
	public SigarSensingController(final String name, final Sigar sigar,
			final MonitoringController monitoringController, final int poolSize) {
		this.instanceName = name;
		this.sigar = sigar;
		this.monitoringController = monitoringController;
		this.poolExecutor = new ScheduledThreadPoolExecutor(poolSize);
		this.poolExecutor
				.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
		this.poolExecutor
				.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
	}

	final Hashtable<Long, AbstractSigarLogger> jobTable =
			new Hashtable<Long, AbstractSigarLogger>();
	long nextJobId = 1;

	/**
	 * Convenience function for
	 * {@link #sensePeriodic(AbstractSigarLogger, long, long, TimeUnit)} which
	 * registers a {@link CPUsCombinedPercLogger} as a periodic
	 * {@link AbstractSigarLogger} with the given parameters.
	 * 
	 * @param initialDelay
	 * @param period
	 * @param timeUnit
	 * @return
	 */
	public synchronized long senseCPUsCombinedPercPeriodic(
			final long initialDelay, final long period, final TimeUnit timeUnit) {
		final CPUsCombinedPercLogger l =
				new CPUsCombinedPercLogger(this.sigar,
						this.monitoringController);
		return this.sensePeriodic(l, initialDelay, period, timeUnit);
	}

	/**
	 * Convenience function for
	 * {@link #sensePeriodic(AbstractSigarLogger, long, long, TimeUnit)} which
	 * registers a {@link CPUsDetailedPercLogger} as a periodic
	 * {@link AbstractSigarLogger} with the given parameters.
	 * 
	 * @param initialDelay
	 * @param period
	 * @param timeUnit
	 * @return
	 */
	public synchronized long senseCPUsDetailedPercPeriodic(
			final long initialDelay, final long period, final TimeUnit timeUnit) {
		final CPUsDetailedPercLogger l =
				new CPUsDetailedPercLogger(this.sigar,
						this.monitoringController);
		return this.sensePeriodic(l, initialDelay, period, timeUnit);
	}

	/**
	 * Convenience function for
	 * {@link #sensePeriodic(AbstractSigarLogger, long, long, TimeUnit)} which
	 * registers a {@link MemSwapUsageLogger} as a periodic
	 * {@link AbstractSigarLogger} with the given parameters.
	 * 
	 * @param initialDelay
	 * @param period
	 * @param timeUnit
	 * @return
	 */
	public synchronized long senseMemSwapUsagePeriodic(final long initialDelay,
			final long period, final TimeUnit timeUnit) {
		final MemSwapUsageLogger l =
				new MemSwapUsageLogger(this.sigar, this.monitoringController);
		return this.sensePeriodic(l, initialDelay, period, timeUnit);
	}

	/**
	 * Schedules the given {@link AbstractSigarLogger} to be scheduled with the
	 * given initial delay, and period.
	 * 
	 * @param sigarLogger
	 * @param initialDelay
	 * @param period
	 * @param timeUnit
	 * @return
	 */
	public synchronized long sensePeriodic(
			final AbstractSigarLogger sigarLogger, final long initialDelay,
			final long period, final TimeUnit timeUnit) {
		final long currentJobId = this.nextJobId++;
		sigarLogger.setLoggerId(currentJobId);
		this.poolExecutor.scheduleAtFixedRate(sigarLogger, initialDelay,
				period, timeUnit);
		this.jobTable.put(this.nextJobId, sigarLogger);
		return currentJobId;
	}

	/**
	 * Stops future executions of the periodic sensor job with the given id.
	 * 
	 * @param loggerId
	 * @return the loggerId if the job has been stopped successfully; -1 if no
	 *         such job exists.
	 */
	public synchronized long stopPeriodicSensor(final long loggerId) {
		final AbstractSigarLogger l = this.jobTable.get(loggerId);
		if (l == null) {
			SigarSensingController.log
					.warn("You tried to stop a non-existing peridioc logger with id '"
							+ loggerId + "'");
			return -1;
		}
		this.poolExecutor.remove(l);
		return loggerId;
	}

	/**
	 * Terminates this {@link SigarSensingController}.
	 */
	public synchronized void shutdown() {
		this.poolExecutor.shutdown();
	}
}