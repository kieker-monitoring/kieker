package kieker.monitoring.core;

import java.lang.management.ManagementFactory;
import java.util.Set;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

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
public class MonitoringController extends SamplingController implements IMonitoringController, MonitoringControllerMBean {
	private static final Log log = LogFactory.getLog(MonitoringController.class);

	private final ObjectName objectname;
	
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
			this.objectname = null;
			MonitoringController.log.error("Controller (" + this.getName() +") initializsation failed\n" + getState());
			final StringBuilder sb = new StringBuilder("Configuration:");
			final Set<String> keys = configuration.stringPropertyNames();
			if (keys.isEmpty()) {
				sb.append("\n\tNo Configuration found");
			} else {
				for (String property : keys) {
					sb.append("\n\t");
					sb.append(property);
					sb.append("='");
					sb.append(configuration.getProperty(property));
					sb.append("'");
				}
			}
			MonitoringController.log.error(sb.toString());
			return;
		}
		// register MBean
		ObjectName objectname = null;
		if (configuration.getBooleanProperty(Configuration.ACTIVATE_MBEAN)) {
			try {
				objectname = new ObjectName(
						configuration.getStringProperty(Configuration.ACTIVATE_MBEAN_DOMAIN), 
						"type", 
						configuration.getStringProperty(Configuration.ACTIVATE_MBEAN_TYPE));
				ManagementFactory.getPlatformMBeanServer().registerMBean(this, objectname);
			} catch (final MalformedObjectNameException ex) {
				log.warn("Unable to register MBean (constructed ObjectName malformated)! Check the following configuration values '" + 
						Configuration.ACTIVATE_MBEAN_DOMAIN + "=" + configuration.getStringProperty(Configuration.ACTIVATE_MBEAN_DOMAIN) + "' and '" + 
						Configuration.ACTIVATE_MBEAN_TYPE + "=" + configuration.getStringProperty(Configuration.ACTIVATE_MBEAN_TYPE) + "'");
				objectname = null;
			} catch (final Exception ex) {
				log.warn("Unable to register MBean", ex);
				objectname = null;
			}
		}
		this.objectname = objectname;
		// finished initialization
		MonitoringController.log.info("Controller (" + this.getName() +") initializsation finished\n" + getState());
	}
	
	@Override
	public final boolean terminateMonitoring() {
		if (super.terminateMonitoring()) {
			if (objectname != null) {
				try {
					ManagementFactory.getPlatformMBeanServer().unregisterMBean(objectname);
				} catch (final Exception ex) {
					log.error("Failed to terminate MBean", ex);
				}
			}
			MonitoringController.log.info("Controller (" + this.getName() +") shutdown completed");
			return true;
		}
		return false;
	}
	
	@Override
	public final String getState() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Current State of kieker.monitoring (");
		sb.append(getVersion());
		sb.append(")");
		if (objectname != null) {
			sb.append("(MBean available: ");
			sb.append(objectname.getCanonicalName());
			sb.append(")");
		}
		sb.append(":\n");
		sb.append(super.getState());
		return sb.toString().trim();
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
