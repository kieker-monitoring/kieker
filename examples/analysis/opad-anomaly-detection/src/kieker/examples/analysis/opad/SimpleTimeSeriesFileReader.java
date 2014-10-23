/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.analysis.opad;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.filter.forward.TeeFilter;
import kieker.common.configuration.Configuration;

/**
 * @author Thomas Duellmann
 *
 * @since 1.11
 */

@Plugin(description = "A filter to print the object to a configured stream",
outputPorts = @OutputPort(name = TeeFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, description = "Provides each incoming object", eventTypes = { Object.class }),
configuration = {
	@Property(name = TeeFilter.CONFIG_PROPERTY_NAME_STREAM, defaultValue = TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_STDOUT,
			description = "The name of the stream used to print the incoming data (special values are STDOUT, STDERR, STDlog, and NULL; "
					+ "other values are interpreted as filenames)."),
					@Property(name = TeeFilter.CONFIG_PROPERTY_NAME_ENCODING, defaultValue = TeeFilter.CONFIG_PROPERTY_VALUE_DEFAULT_ENCODING,
					description = "The used encoding for the selected stream."),
					@Property(name = TeeFilter.CONFIG_PROPERTY_NAME_APPEND, defaultValue = TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_APPEND,
					description = "Decides whether the filter appends to the stream in the case of a file or not.")
})
public class SimpleTimeSeriesFileReader extends AbstractFilterPlugin {

	/** The name of the property determining whether or not the stream appends or overwrites to files. */
	public static final String CONFIG_PROPERTY_NAME_INPUT_FILE = "input_file";

	private final String inputFile;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public SimpleTimeSeriesFileReader(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
		this.inputFile = this.configuration.getPathProperty(CONFIG_PROPERTY_NAME_INPUT_FILE);
	}

	@Override
	public final void terminate(final boolean error) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Configuration getCurrentConfiguration() {
		final Configuration config = new Configuration();
		config.setProperty(SimpleTimeSeriesFileReader.CONFIG_PROPERTY_NAME_INPUT_FILE, this.inputFile);
		return config;
	}
}
