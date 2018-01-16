/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
 * If this file changes, the default config file has to be adjusted!
 * Ideally it would be created using this file! (#151)
 * 
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.3
 */
interface Keys { // NOPMD NOCS (static list)
	/** prefix used for all kieker.monitoring components. */
	public static final String PREFIX = "kieker.monitoring.";

	/** Location of the custom properties file (in classpath). */
	public static final String CUSTOM_PROPERTIES_LOCATION_CLASSPATH = "META-INF/" + PREFIX + "properties";
	/** Location of the default properties file (in classpath). */
	public static final String DEFAULT_PROPERTIES_LOCATION_CLASSPATH = "META-INF/" + PREFIX + "default.properties";

	/** JVM-parameter to specify a custom properties file. */
	public static final String CUSTOM_PROPERTIES_LOCATION_JVM = PREFIX + "configuration";

	// these MUST be declared in the file DEFAULT_PROPERTIES_LOCATION_CLASSPATH

	// Monitoring Controller
	public static final String MONITORING_ENABLED = PREFIX + "enabled";
	public static final String CONTROLLER_NAME = PREFIX + "name";
	public static final String HOST_NAME = PREFIX + "hostname";
	public static final String EXPERIMENT_ID = PREFIX + "initialExperimentId";
	public static final String USE_SHUTDOWN_HOOK = PREFIX + "useShutdownHook";
	public static final String DEBUG = PREFIX + "debug";
	public static final String METADATA = PREFIX + "metadata";

	// JMX Controller
	public static final String ACTIVATE_JMX = PREFIX + "jmx";
	public static final String ACTIVATE_JMX_DOMAIN = PREFIX + "jmx.domain";
	public static final String ACTIVATE_JMX_CONTROLLER = PREFIX + "jmx.MonitoringController";
	public static final String ACTIVATE_JMX_CONTROLLER_NAME = PREFIX + "jmx.MonitoringController.name";
	public static final String ACTIVATE_JMX_REMOTE = PREFIX + "jmx.remote";
	public static final String ACTIVATE_JMX_REMOTE_PORT = PREFIX + "jmx.remote.port";
	public static final String ACTIVATE_JMX_REMOTE_NAME = PREFIX + "jmx.remote.name";
	public static final String ACTIVATE_JMX_REMOTE_FALLBACK = PREFIX + "jmx.remote.fallback";

	// Writer Controller
	public static final String AUTO_SET_LOGGINGTSTAMP = PREFIX + "setLoggingTimestamp";
	public static final String WRITER_CLASSNAME = PREFIX + "writer";

	// TimeSource Controller
	public static final String TIMER_CLASSNAME = PREFIX + "timer";

	// Sampling Controller
	public static final String PERIODIC_SENSORS_EXECUTOR_POOL_SIZE = PREFIX + "periodicSensorsExecutorPoolSize";

	// Probe Controller
	public static final String ADAPTIVE_MONITORING_ENABLED = PREFIX + "adaptiveMonitoring.enabled";
	public static final String ADAPTIVE_MONITORING_CONFIG_FILE = PREFIX + "adaptiveMonitoring.configFile";
	public static final String ADAPTIVE_MONITORING_CONFIG_FILE_UPDATE = PREFIX + "adaptiveMonitoring.updateConfigFile";
	public static final String ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL = PREFIX + "adaptiveMonitoring.readInterval";
	public static final String ADAPTIVE_MONITORING_MAX_CACHE_SIZE = PREFIX + "adaptiveMonitoring.maxCacheSize";
	public static final String ADAPTIVE_MONITORING_BOUNDED_CACHE_BEHAVIOUR = PREFIX + "adaptiveMonitoring.boundedCacheBehaviour";

}
