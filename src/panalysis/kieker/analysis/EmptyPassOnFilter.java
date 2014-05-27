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
package kieker.analysis;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
@Plugin(outputPorts = @OutputPort(name = EmptyPassOnFilter.OUTPUT_PORT_NAME))
public class EmptyPassOnFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME = "input";
	public static final String OUTPUT_PORT_NAME = "output";

	public EmptyPassOnFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@InputPort(name = EmptyPassOnFilter.INPUT_PORT_NAME)
	public void input(final Object data) {
		super.deliver(EmptyPassOnFilter.OUTPUT_PORT_NAME, data);
	}

}
