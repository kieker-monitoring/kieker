/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.opad.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.opad.record.INamedElement;

/**
 * Filters every incoming events by a given list of names.
 * 
 * @author Tillmann Carlos Bielefeld, Andre van Hoorn
 * 
 */
@Plugin(name = "Name Filter",
		outputPorts = { @OutputPort(eventTypes = { INamedElement.class }, name = NameFilter.OUTPUT_PORT_NAME_VALUE) },
		configuration = {
			@Property(name = NameFilter.CONFIG_PROPERTY_NAME_NAMES, defaultValue = "")
		})
public class NameFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_VALUE = "inputValue";

	public static final String OUTPUT_PORT_NAME_VALUE = "outputValue";

	public static final String CONFIG_PROPERTY_NAME_NAMES = "names";

	private static final Log LOG = LogFactory.getLog(NameFilter.class);

	private final Set<String> names = new HashSet<String>();

	public NameFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
		final String[] arrNames = configuration.getStringArrayProperty(NameFilter.CONFIG_PROPERTY_NAME_NAMES);
		this.names.addAll(Arrays.asList(arrNames));

		NameFilter.LOG.info("Started NameFilter filtering for names: " + this.names);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@InputPort(eventTypes = { INamedElement.class }, name = NameFilter.INPUT_PORT_NAME_VALUE)
	public void inputEvent(final INamedElement input) {
		if (this.names.contains(input.getName())) {
			LogFactory.getLog("DATAFLOW").info("MFRR valid input: " + input.getName());
			super.deliver(NameFilter.OUTPUT_PORT_NAME_VALUE, input);
		}
	}

}
