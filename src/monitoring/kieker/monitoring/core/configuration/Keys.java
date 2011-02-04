package kieker.monitoring.core.configuration;

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
 * @author Andre van Hoorn, Jan Waller
 */
interface Keys {
	//TODO: if this changes, the default config file has to be adjusted!
	//      Ideally it would be created using this file!
	
	/** prefix used for all kieker.monitoring components */
	public final static String PREFIX = "kieker.monitoring.";
	
	/** Location of the custom properties file (in classpath) */
	public final static String CUSTOM_PROPERTIES_LOCATION_CLASSPATH = "META-INF/" + PREFIX + "properties";
	/** Location of the default properties file (in classpath) */
	public final static String DEFAULT_PROPERTIES_LOCATION_CLASSPATH = CUSTOM_PROPERTIES_LOCATION_CLASSPATH + ".default";

	/** JVM-parameter to specify a custom properties file */
	public final static String CUSTOM_PROPERTIES_LOCATION_JVM = PREFIX + "configuration";

	// these MUST be declared in the file DEFAULT_PROPERTIES_LOCATION_CLASSPATH

	// Monitoring Controller
	public final static String CONTROLLER_NAME = PREFIX + "name";
	public final static String MONITORING_ENABLED = PREFIX + "enabled";
	public final static String WRITER_CLASSNAME = PREFIX + "writer";

	//public final static String DEBUG_ENABLED = PREFIX + "debug";
	//public final static String PERIODIC_SENSORS_EXECUTOR_POOL_SIZE = PREFIX + "periodicSensorsExecutorPoolSize";
}
