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

package kieker.analysis.configuration;

/**
 * @author Jan Waller
 */
interface Keys { // NOPMD NOCS
	/** prefix used for all kieker.monitoring components */
	public static final String PREFIX = "kieker.analysis.";

	/** Location of the custom properties file (in classpath) */
	public static final String CUSTOM_PROPERTIES_LOCATION_CLASSPATH = "META-INF/" + Keys.PREFIX + "properties";
	/** Location of the default properties file (in classpath) */
	public static final String DEFAULT_PROPERTIES_LOCATION_CLASSPATH = Keys.CUSTOM_PROPERTIES_LOCATION_CLASSPATH + ".default";

	/** JVM-parameter to specify a custom properties file */
	public static final String CUSTOM_PROPERTIES_LOCATION_JVM = Keys.PREFIX + "configuration";

	// these MUST be declared in the file DEFAULT_PROPERTIES_LOCATION_CLASSPATH
	// TODO: add keys (see kieker.monitoring.core.configuration.Keys for examples)
}
