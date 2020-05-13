/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
 * @author Reiner Jung -- changed to ConfigurationKeys
 *
 * @since 1.3
 *
 * @deprecated since 1.14 remove in 1.16 replaced by the class {@link ConfigurationKeys} to support Kieker checkstyle policy
 */
@Deprecated
public interface Keys { // NOCS
	/** prefix used for all kieker.monitoring components. */
	String PREFIX = "kieker.monitoring.";

	/** Location of the custom properties file (in classpath). */
	String CUSTOM_PROPERTIES_LOCATION_CLASSPATH = "META-INF/" + PREFIX + "properties";
	/** Location of the default properties file (in classpath). */
	String DEFAULT_PROPERTIES_LOCATION_CLASSPATH = "META-INF/" + PREFIX + "default.properties";

	/** JVM-parameter to specify a custom properties file. */
	String CUSTOM_PROPERTIES_LOCATION_JVM = PREFIX + "configuration";

	// these MUST be declared in the file DEFAULT_PROPERTIES_LOCATION_CLASSPATH

	// Monitoring Controller
	String MONITORING_ENABLED = PREFIX + "enabled";
	String CONTROLLER_NAME = PREFIX + "name";
	String HOST_NAME = PREFIX + "hostname";
	String EXPERIMENT_ID = PREFIX + "initialExperimentId";
	String APPLICATION_NAME = PREFIX + "applicationName";
	String USE_SHUTDOWN_HOOK = PREFIX + "useShutdownHook";
	String DEBUG = PREFIX + "debug";
	String META_DATA = PREFIX + "metadata";

	// JMX Controller
	String ACTIVATE_JMX = PREFIX + "jmx";
	String ACTIVATE_JMX_DOMAIN = PREFIX + "jmx.domain";
	String ACTIVATE_JMX_CONTROLLER = PREFIX + "jmx.MonitoringController";
	String ACTIVATE_JMX_CONTROLLER_NAME = PREFIX + "jmx.MonitoringController.name";
	String ACTIVATE_JMX_REMOTE = PREFIX + "jmx.remote";
	String ACTIVATE_JMX_REMOTE_PORT = PREFIX + "jmx.remote.port";
	String ACTIVATE_JMX_REMOTE_NAME = PREFIX + "jmx.remote.name";
	String ACTIVATE_JMX_REMOTE_FALLBACK = PREFIX + "jmx.remote.fallback";

	// TCP Controller
	String ACTIVATE_TCP = PREFIX + "tcp";
	String ACTIVATE_TCP_DOMAIN = PREFIX + "tcp.domain";
	String ACTIVATE_TCP_REMOTE = PREFIX + "tcp.remote";
	String ACTIVATE_TCP_REMOTE_PORT = PREFIX + "tcp.remote.port";

	// Writer Controller
	String AUTO_SET_LOGGINGTSTAMP = PREFIX + "setLoggingTimestamp";
	String WRITER_CLASSNAME = PREFIX + "writer";

	// TimeSource Controller
	String TIMER_CLASSNAME = PREFIX + "timer";

	// Sampling Controller
	String PERIODIC_SENSORS_EXECUTOR_POOL_SIZE = PREFIX + "periodicSensorsExecutorPoolSize";

	// Probe Controller
	String ADAPTIVE_MONITORING_ENABLED = PREFIX + "adaptiveMonitoring.enabled";
	String ADAPTIVE_MONITORING_CONFIG_FILE = PREFIX + "adaptiveMonitoring.configFile";
	String ADAPTIVE_MONITORING_CONFIG_FILE_UPDATE = PREFIX + "adaptiveMonitoring.updateConfigFile";
	String ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL = PREFIX + "adaptiveMonitoring.readInterval";
	String ADAPTIVE_MONITORING_MAX_CACHE_SIZE = PREFIX + "adaptiveMonitoring.maxCacheSize";
	String ADAPTIVE_MONITORING_BOUNDED_CACHE_BEHAVIOUR = PREFIX + "adaptiveMonitoring.boundedCacheBehaviour";

	/**
	 * Method used to fool checkstyle in believing in a proper interface.
	 *
	 * @since 1.14
	 */
	void dummy();
}
