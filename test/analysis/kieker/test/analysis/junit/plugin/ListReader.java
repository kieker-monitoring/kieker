/***************************************************************************
 * Copyright 2012 by
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

import java.util.ArrayList;
import java.util.List;

import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;

/**
 * Helper class that reads records added using the method {@link #addAllRecords(List)}.
 * 
 * @author Andre van Hoorn
 * 
 */
@Plugin(outputPorts = { @OutputPort(name = ListReader.OUTPUT_PORT_NAME, eventTypes = { Object.class }) })
public class ListReader extends AbstractReaderPlugin {

	public static final String OUTPUT_PORT_NAME = "defaultOutput";

	private final List<Object> objects = new ArrayList<Object>();

	public ListReader(final Configuration configuration) {
		super(configuration);
	}

	public void addAllObjects(final List<Object> records) {
		this.objects.addAll(records);
	}

	public void addObject(final Object object) {
		this.objects.add(object);
	}

	public boolean read() {
		for (final Object obj : this.objects) {
			super.deliver(ListReader.OUTPUT_PORT_NAME, obj);
		}
		return true;
	}

	public void terminate(final boolean error) {
		// nothing to do
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
