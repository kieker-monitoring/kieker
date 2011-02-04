package kieker.monitoring.core;

import java.util.concurrent.atomic.AtomicBoolean;

import kieker.monitoring.core.ShutdownHook;
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
	
	private final AtomicBoolean monitoringTerminated = new AtomicBoolean(false);
	/** The name of this controller instance */
	private final String name;

	protected Controller(final Configuration configuration) {
		name = configuration.getStringProperty(Configuration.CONTROLLER_NAME);
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
	public String getState() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: '");
		sb.append(name);
		sb.append("'; Terminated: '");
		sb.append(isMonitoringTerminated());
		sb.append("'");
		return sb.toString();
	}
}
