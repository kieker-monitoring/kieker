package kieker.monitoring.core;

import java.util.concurrent.atomic.AtomicLong;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.writer.IMonitoringWriter;

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
 * @author Andre van Hoorn, Matthias Rohr, Jan Waller
 */
abstract class MonitoringController extends ReplayController implements IMonitoringController {
	private static final Log log = LogFactory.getLog(MonitoringController.class);

	/** Used to track the total number of monitoring records received while the controller has been enabled */
	private final AtomicLong numberOfInserts = new AtomicLong(0);
	/** Monitoring Writer */
	private final IMonitoringWriter monitoringWriter;
	/** State of monitoring */
	private volatile boolean monitoringEnabled = false;
	
	protected MonitoringController(final Configuration configuration) {
		super(configuration);
		// set Writer
		final String writerClassname = configuration.getStringProperty(Configuration.WRITER_CLASSNAME);
		IMonitoringWriter monitoringWriter = null;
		try {
			// search for correct Constructor -> 1 parameter of type Properties
			monitoringWriter = IMonitoringWriter.class.cast(Class.forName(writerClassname).getConstructor(Configuration.class).newInstance(configuration.getPropertiesStartingWith(writerClassname)));
		} catch (final NoSuchMethodException ex) {
			MonitoringController.log.error("Writer Class '" + writerClassname + "' has to implement a constructor that accepts a single Configuration");
		} catch (final NoClassDefFoundError ex) {
			MonitoringController.log.error("Writer Class '" + writerClassname + "' not found");
		} catch (final ClassNotFoundException ex) {
			MonitoringController.log.error("Writer Class '" + writerClassname + "' not found");
		} catch (final Throwable ex) {
			MonitoringController.log.error("Failed to load writer class for name " + writerClassname, ex);
		}
		if ((this.monitoringWriter = monitoringWriter) == null) {
			terminateMonitoring();
			return;
		}
		// set State
		monitoringEnabled = configuration.getBooleanProperty(Configuration.MONITORING_ENABLED);
	}

	/**
	 * Permanently terminates monitoring (e.g., due to a failure). Subsequent
	 * tries to enable monitoring via {@link #setMonitoringEnabled(boolean)} will
	 * be refused. 
	 */
	@Override
	public boolean terminateMonitoring() {
		if (super.terminateMonitoring()) {
			// TODO: Logger may be problematic, may already have shutdown!
			MonitoringController.log.info("Shutting down Monitoring Controller");
			if (this.monitoringWriter != null) {
				monitoringWriter.terminate();
			}
			return true;
		}
		return false;
	}
	
	@Override
	public String getState() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.getState());
		sb.append("; Enabled: '");
		sb.append(monitoringEnabled);
		sb.append("'; Writer: '");
		if (monitoringWriter != null) {
			sb.append(monitoringWriter.getClass().getName());
			sb.append("'\n");
			sb.append(monitoringWriter.getInfoString());
		} else {
			sb.append("null'");
		}
		return sb.toString();
	}
	
	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		try {
			if (!isMonitoringEnabled()) { //enabled and not terminated
				return false;
			}
			numberOfInserts.incrementAndGet();
			if (this.isRealtimeMode()) {
				record.setLoggingTimestamp(MonitoringController.currentTimeNanos());
			}
			if (!monitoringWriter.newMonitoringRecord(record)) {
				MonitoringController.log.fatal("Error writing the monitoring data. Will terminate monitoring!");
				terminateMonitoring();
				return false;
			}
			return true;
	} catch (final Throwable ex) {
			MonitoringController.log.fatal("Exception detected. Will terminate monitoring", ex);
			terminateMonitoring();
			return false;
		}
	}

	@Override
	public final boolean enableMonitoring() {
		if (isMonitoringTerminated()) {
			MonitoringController.log.error("Refused to enable monitoring because monitoring has been permanently terminated before");
			return false;
		}
		MonitoringController.log.info("Enabling monitoring");
		monitoringEnabled = true;
		return true;
	}
	
	/**
	 * Careful!
	 * isMonitoringEnabled() != !isMonitoringDisabled()
	 */
	@Override
	public final boolean isMonitoringEnabled() {
		return !isMonitoringTerminated() && monitoringEnabled;
	}
	
	@Override
	public final boolean disableMonitoring() {
		if (isMonitoringTerminated()) {
			MonitoringController.log.error("Refused to disable monitoring because monitoring has been permanently terminated before");
			return false;
		}
		MonitoringController.log.info("Disabling monitoring");
		monitoringEnabled = false;
		return true;
	}

	/**
	 * Careful!
	 * isMonitoringDisabled() != !isMonitoringEnabled()
	 */
	@Override
	public final boolean isMonitoringDisabled() {
		return !isMonitoringTerminated() && !monitoringEnabled;
	}

	@Override
	public final IMonitoringWriter getMonitoringWriter() {
		return monitoringWriter;
	}
}
