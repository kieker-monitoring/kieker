package kieker.monitoring.probe.sigar.loggerJobs;

import java.util.concurrent.atomic.AtomicLong;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.ResourceUtilizationRecord;
import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.probe.sigar.SigarSensingController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.Sigar;

/**
 * Eases the implementation of loggers for the {@link SigarSensingController}
 * that retrieve system-level sensor data via {@link Sigar} and store this data
 * as {@link ResourceUtilizationRecord}s via
 * {@link MonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}
 * . The {@link #run()} method throws a {@link RuntimeException} if an error
 * occurs.
 * 
 * 
 * @author Andre van Hoorn
 * 
 */
public abstract class AbstractSigarLogger implements Runnable {

	private static final Log log = LogFactory.getLog(AbstractSigarLogger.class);

	private final Sigar sigar;

	/**
	 * @return the sigar
	 */
	protected final Sigar getSigar() {
		return this.sigar;
	}

	private final MonitoringController monitoringController;

	/**
	 * Caches the hostname of this machine.
	 */
	private final String hostname;

	/**
	 * @return the hostname
	 */
	protected final String getHostname() {
		return this.hostname;
	}

	/**
	 * The externally ID of this logger which can be used to map logger messages
	 * to registered loggers. The value is set by the
	 * {@link SigarSensingController} using
	 * 
	 */
	private final AtomicLong loggerId = new AtomicLong(-1);

	/**
	 * @param loggerId
	 *            the loggerId to set
	 */
	public final void setLoggerId(final long loggerId) {
		this.loggerId.set(loggerId);
	}

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private AbstractSigarLogger() {
		this(null, null);
	}

	/**
	 * Constructs a new {@link AbstractSigarLogger} with given loggerId,
	 * {@link Sigar} instance used to retrieve the sensor data and
	 * {@link MonitoringController} used to log the {@link IMonitoringRecord}s.
	 * 
	 * @param sigar
	 * @param monitoringController
	 */
	public AbstractSigarLogger(final Sigar sigar,
			final MonitoringController monitoringController) {
		this.sigar = sigar;
		this.monitoringController = monitoringController;
		this.hostname = this.monitoringController.getHostName();
	}

	/**
	 * Throws a {@link RuntimeException} if an error occurred.
	 */
	@Override
	public final void run() throws RuntimeException {
		try {
			this.concreteRun();
		} catch (final Exception e) {
			final String errorMsg =
					"Logger " + this.loggerId.get() + ": Exception occurred: "
							+ e.getMessage();
			AbstractSigarLogger.log.error(errorMsg, e);
			/* Re-throw exception */
			throw new RuntimeException(errorMsg, e);
		}
	}

	/**
	 * Handler to be executed be implementing classes.
	 * 
	 * @throws Exception
	 *             thrown to indicate an error.
	 */
	protected abstract void concreteRun() throws Exception;

	/**
	 * Logs the {@link IMonitoringRecord} using the {@link MonitoringController}
	 * passed at construction time.
	 * 
	 * @param record
	 * @return true on success; false if an error occurred.
	 */
	protected final boolean logRecord(final IMonitoringRecord record) {
		return this.monitoringController.newMonitoringRecord(record);
	}
}