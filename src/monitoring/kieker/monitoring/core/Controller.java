package kieker.monitoring.core;

import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.monitoring.core.configuration.Configuration;

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
 * @author Jan Waller
 */
abstract class Controller implements IController {
	private static final Log log = LogFactory.getLog(Controller.class);
	
	/** Whether monitoring is terminated */
	private final AtomicBoolean monitoringTerminated = new AtomicBoolean(false);
	/** The name of this controller instance */
	private final String name;
	/** The hostname of this controller instance */
	private final String hostname;
	/** The experimentId of this controller instance */
	private final AtomicInteger experimentId = new AtomicInteger(0);
	/** DebugMode */
	private volatile boolean debug = false;

	protected Controller(final Configuration configuration) {
		name = configuration.getStringProperty(Configuration.CONTROLLER_NAME);
		String hostname = configuration.getStringProperty(Configuration.HOST_NAME);
		if (hostname.isEmpty()) {
			hostname = "<UNKNOWN>";
			try {
				hostname = java.net.InetAddress.getLocalHost().getHostName();
			} catch (final UnknownHostException ex) {
				Controller.log.warn("Failed to retrieve hostname");
			}
		}
		this.hostname = hostname; 
		this.experimentId.set(configuration.getIntProperty(Configuration.EXPERIMENT_ID));
		this.debug = configuration.getBooleanProperty(Configuration.DEBUG);
		try {
			// Dangerous! escaping "this" in constructor!
			Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));
		} catch (final Exception ex) {
			Controller.log.warn("Failed to add shutdownHook", ex);
		}
	}
	
	@Override
	public boolean terminateMonitoring() {
		if (!monitoringTerminated.getAndSet(true)) {
			Controller.log.info("Controller (" + this.name + ") shutting down");
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isMonitoringTerminated() {
		return monitoringTerminated.get();
	}
	
	@Override
	public final void enableDebug() {
		debug = true;
	}
	
	@Override
	public final void disableDebug() {
		debug = false;
	}

	@Override
	public final boolean isDebug() {
		return debug;
	}

	@Override
	public final String getName() {
		return name;
	}
	
	@Override
	public final String getHostName() {
		return hostname;
	}
	
	@Override
	public final int incExperimentId() {
		return experimentId.incrementAndGet();
	}

	@Override
	public final void setExperimentId(final int newExperimentID) {
		experimentId.set(newExperimentID);
	}
	
	@Override
	public final int getExperimentId() {
		return experimentId.get();
	}
	
	@Override
	public String getState() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: '");
		sb.append(name);
		sb.append("'; Hostname: '");
		sb.append(hostname);
		sb.append("'; Terminated: '");
		sb.append(isMonitoringTerminated());
		sb.append("'; Debug Mode: '");
		sb.append(debug);
		sb.append("'; experimentID: '");
		sb.append(getExperimentId());
		sb.append("'");
		return sb.toString();
	}
}

/**
 * This class ensures that virtual machine shutdown (e.g., cause by a
 * System.exit(int)) is delayed until all monitoring data is written. This is
 * important for the asynchronous writers for the files system and database,
 * since these store data with a small delay and data would be lost when
 * System.exit is not delayed.
 * 
 * When the system shutdown is initiated, the termination of the Virtual Machine
 * is delayed until all registered worker queues are empty.
 * 
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 */
final class ShutdownHook extends Thread {
	private static final Log log = LogFactory.getLog(ShutdownHook.class);

	private final IController ctrl;

	public ShutdownHook(final IController ctrl) {
		this.ctrl = ctrl;
	}

	@Override
	public void run() {
		// is called when VM shutdown (e.g., strg+c) is initiated or when system.exit is called
		if (!ctrl.isMonitoringTerminated()) {
			//TODO: We can't use a logger in shutdown hooks, logger may already be down!
			ShutdownHook.log.info("ShutdownHook notifies controller to initiate shutdown");
			this.ctrl.terminateMonitoring();
		}
	}
}
