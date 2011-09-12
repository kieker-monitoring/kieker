/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
 */
interface Keys {
	/** prefix used for all kieker.monitoring components */
	public final static String PREFIX = "kieker.monitoring.";

	/** Location of the custom properties file (in classpath) */
	public final static String CUSTOM_PROPERTIES_LOCATION_CLASSPATH = "META-INF/" + Keys.PREFIX + "properties";
	/** Location of the default properties file (in classpath) */
	public final static String DEFAULT_PROPERTIES_LOCATION_CLASSPATH = Keys.CUSTOM_PROPERTIES_LOCATION_CLASSPATH + ".default";

	/** JVM-parameter to specify a custom properties file */
	public final static String CUSTOM_PROPERTIES_LOCATION_JVM = Keys.PREFIX + "configuration";

	// these MUST be declared in the file DEFAULT_PROPERTIES_LOCATION_CLASSPATH

	// Monitoring Controller
	public final static String MONITORING_ENABLED = Keys.PREFIX + "enabled";
	public final static String CONTROLLER_NAME = Keys.PREFIX + "name";
	public final static String HOST_NAME = Keys.PREFIX + "hostname";
	public final static String EXPERIMENT_ID = Keys.PREFIX + "initialExperimentId";

	// JMX
	public final static String ACTIVATE_JMX = Keys.PREFIX + "jmx";
	public final static String ACTIVATE_JMX_DOMAIN = Keys.PREFIX + "jmx.domain";
	public final static String ACTIVATE_JMX_CONTROLLER = Keys.PREFIX + "jmx.MonitoringController";
	public final static String ACTIVATE_JMX_CONTROLLER_NAME = Keys.PREFIX + "jmx.MonitoringController.name";
	public final static String ACTIVATE_JMX_REMOTE = Keys.PREFIX + "jmx.remote";
	public final static String ACTIVATE_JMX_REMOTE_PORT = Keys.PREFIX + "jmx.remote.port";
	public final static String ACTIVATE_JMX_REMOTE_NAME = Keys.PREFIX + "jmx.remote.name";
	public final static String ACTIVATE_JMX_REMOTE_FALLBACK = Keys.PREFIX + "jmx.remote.fallback";

	// Writer Controller
	public final static String AUTO_SET_LOGGINGTSTAMP = Keys.PREFIX + "setLoggingTimestamp";
	public final static String WRITER_CLASSNAME = Keys.PREFIX + "writer";
	public final static String TIMER_CLASSNAME = Keys.PREFIX + "timer";
	// Sampling Controller
	public final static String PERIODIC_SENSORS_EXECUTOR_POOL_SIZE = Keys.PREFIX + "periodicSensorsExecutorPoolSize";
}
