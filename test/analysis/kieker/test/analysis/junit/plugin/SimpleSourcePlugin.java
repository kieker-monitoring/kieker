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

package kieker.test.analysis.junit.plugin;

import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

import org.junit.Assert;

/**
 * @author Nils Christian Ehmke, Jan Waller
 */
@Plugin(outputPorts = @OutputPort(name = SimpleSourcePlugin.OUTPUT_PORT_NAME))
public class SimpleSourcePlugin extends AbstractFilterPlugin {

	public static final String OUTPUT_PORT_NAME = "output";

	public SimpleSourcePlugin(final Configuration configuration) {
		super(configuration);
	}

	public void deliver(final Object data) {
		if (data != null) {
			Assert.assertTrue(super.deliver(SimpleSourcePlugin.OUTPUT_PORT_NAME, data));
		} else {
			Assert.assertFalse(super.deliver(SimpleSourcePlugin.OUTPUT_PORT_NAME, null));
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
