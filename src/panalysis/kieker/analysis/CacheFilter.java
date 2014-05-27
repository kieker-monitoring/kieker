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

import java.util.ArrayList;
import java.util.List;

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
@Plugin(outputPorts = @OutputPort(name = CacheFilter.OUTPUT_PORT_NAME))
public class CacheFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME = "input";
	public static final String OUTPUT_PORT_NAME = "output";

	private final List<Object> cache = new ArrayList<Object>();

	public CacheFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public void terminate(final boolean error) {
		for (final Object data : this.cache) {
			super.deliver(EmptyPassOnFilter.OUTPUT_PORT_NAME, data);
		}
		super.terminate(error);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@InputPort(name = CacheFilter.INPUT_PORT_NAME)
	public void input(final Object data) {
		this.cache.add(data);
	}

}
