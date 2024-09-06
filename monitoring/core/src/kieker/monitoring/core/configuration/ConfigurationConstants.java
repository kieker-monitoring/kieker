/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.monitoring.core.configuration;

/**
 * If this file changes, the default config file has to be adjusted! Ideally it
 * would be created using this file! (#151)
 *
 * @author Andre van Hoorn, Jan Waller
 * @author Reiner Jung -- changed to ConfigurationConstants
 *
 * @since 1.3
 */
public final class ConfigurationConstants {
	/** prefix used for all kieker.monitoring components. */
	public static final String PREFIX = "kieker.monitoring.";

	/** Location of the custom properties file (in classpath). */
	public static final String CUSTOM_PROPERTIES_LOCATION_CLASSPATH = "META-INF/" + ConfigurationConstants.PREFIX
			+ "properties";
	/** Location of the default properties file (in classpath). */
	public static final String DEFAULT_PROPERTIES_LOCATION_CLASSPATH = "META-INF/" + ConfigurationConstants.PREFIX
			+ "default.properties";

	/** JVM-parameter to specify a custom properties file. */
	public static final String CUSTOM_PROPERTIES_LOCATION_JVM = ConfigurationConstants.PREFIX + "configuration";

	// these MUST be declared in the file DEFAULT_PROPERTIES_LOCATION_CLASSPATH

	// Monitoring Controller
	public static final String MONITORING_ENABLED = ConfigurationConstants.PREFIX + "enabled";
	public static final String CONTROLLER_NAME = ConfigurationConstants.PREFIX + "name";
	public static final String HOST_NAME = ConfigurationConstants.PREFIX + "hostname";
	public static final String EXPERIMENT_ID = ConfigurationConstants.PREFIX + "initialExperimentId";
	public static final String APPLICATION_NAME = ConfigurationConstants.PREFIX + "applicationName";
	public static final String USE_SHUTDOWN_HOOK = ConfigurationConstants.PREFIX + "useShutdownHook";
	public static final String DEBUG = ConfigurationConstants.PREFIX + "debug";
	public static final String META_DATA = ConfigurationConstants.PREFIX + "metadata";

	// JMX Controller
	public static final String ACTIVATE_JMX = ConfigurationConstants.PREFIX + "jmx";
	public static final String ACTIVATE_JMX_DOMAIN = ConfigurationConstants.PREFIX + "jmx.domain";
	public static final String ACTIVATE_JMX_CONTROLLER = ConfigurationConstants.PREFIX + "jmx.MonitoringController";
	public static final String ACTIVATE_JMX_CONTROLLER_NAME = ConfigurationConstants.PREFIX
			+ "jmx.MonitoringController.name";
	public static final String ACTIVATE_JMX_REMOTE = ConfigurationConstants.PREFIX + "jmx.remote";
	public static final String ACTIVATE_JMX_REMOTE_PORT = ConfigurationConstants.PREFIX + "jmx.remote.port";
	public static final String ACTIVATE_JMX_REMOTE_NAME = ConfigurationConstants.PREFIX + "jmx.remote.name";
	public static final String ACTIVATE_JMX_REMOTE_FALLBACK = ConfigurationConstants.PREFIX + "jmx.remote.fallback";

	// TCP Controller
	public static final String ACTIVATE_TCP = ConfigurationConstants.PREFIX + "tcp";
	public static final String ACTIVATE_TCP_DOMAIN = ConfigurationConstants.PREFIX + "tcp.domain";
	public static final String ACTIVATE_TCP_REMOTE = ConfigurationConstants.PREFIX + "tcp.remote";
	public static final String ACTIVATE_TCP_REMOTE_PORT = ConfigurationConstants.PREFIX + "tcp.remote.port";

	// Writer Controller
	public static final String AUTO_SET_LOGGINGTSTAMP = ConfigurationConstants.PREFIX + "setLoggingTimestamp";
	public static final String WRITER_CLASSNAME = ConfigurationConstants.PREFIX + "writer";

	// TimeSource Controller
	public static final String TIMER_CLASSNAME = ConfigurationConstants.PREFIX + "timer";

	// Sampling Controller
	public static final String PERIODIC_SENSORS_EXECUTOR_POOL_SIZE = ConfigurationConstants.PREFIX
			+ "periodicSensorsExecutorPoolSize";

	// Probe Controller
	public static final String ADAPTIVE_MONITORING_ENABLED = ConfigurationConstants.PREFIX
			+ "adaptiveMonitoring.enabled";
	public static final String ADAPTIVE_MONITORING_CONFIG_FILE = ConfigurationConstants.PREFIX
			+ "adaptiveMonitoring.configFile";
	public static final String ADAPTIVE_MONITORING_CONFIG_FILE_UPDATE = ConfigurationConstants.PREFIX
			+ "adaptiveMonitoring.updateConfigFile";
	public static final String ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL = ConfigurationConstants.PREFIX
			+ "adaptiveMonitoring.readInterval";
	public static final String ADAPTIVE_MONITORING_MAX_CACHE_SIZE = ConfigurationConstants.PREFIX
			+ "adaptiveMonitoring.maxCacheSize";
	public static final String ADAPTIVE_MONITORING_BOUNDED_CACHE_BEHAVIOUR = ConfigurationConstants.PREFIX
			+ "adaptiveMonitoring.boundedCacheBehaviour";

	/**
	 * Factory class. Avoid instantiation.
	 */
	private ConfigurationConstants() {}
}
