package kieker.monitoring.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kieker.common.util.Version;
import kieker.monitoring.core.configuration.Configuration;

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
public class MonitoringController extends SamplingController implements IMonitoringController {
	private static final Log log = LogFactory.getLog(MonitoringController.class);

	/**
	 * @return the singleton instance of Kieker
	 */
	public final static MonitoringController getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * Returns an additional Kieker Object
	 * 
	 * @param name
	 * @param configuration
	 * @return Kieker
	 */
	public final static MonitoringController createInstance(final Configuration configuration) {
		return new MonitoringController(configuration);
	}
	
	private MonitoringController(final Configuration configuration) {
		super(configuration);
		if (isMonitoringTerminated()) {
			MonitoringController.log.error("Controller (" + this.getName() +") initializsation failed\n" + getState());
			MonitoringController.log.error(configuration.toString());
			return;
		}
		MonitoringController.log.info("Controller (" + this.getName() +") initializsation finished\n" + getState());
	}
	
	@Override
	public final boolean terminateMonitoring() {
		if (super.terminateMonitoring()) {
			MonitoringController.log.info("Controller (" + this.getName() +") shutdown completed");
			return true;
		}
		return false;
	}
	
	@Override
	public final String getState() {
		StringBuilder sb = new StringBuilder();
		sb.append("Current State of kieker.monitoring (");
		sb.append(getVersion());
		sb.append("):\n");
		sb.append(super.getState());
		return sb.toString();
	}
	
	/**
	 * Return the version name of this controller instance.
	 * 
	 * @return the version name
	 */
	public final static String getVersion() {
		return Version.getVERSION();
	}
	
	/**
	 * SINGLETON
	 */
	private final static class LazyHolder {
		static {
			MonitoringController.log.info("Initialization started");
		}
		private static final MonitoringController INSTANCE = new MonitoringController(Configuration.createSingletonConfiguration());
	}
}
