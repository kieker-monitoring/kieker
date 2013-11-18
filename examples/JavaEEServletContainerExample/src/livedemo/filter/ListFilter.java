/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package livedemo.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * @param <T>
 * 
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
@Plugin(programmaticOnly = true,
		description = "A filter collecting incoming objects in a list",
		outputPorts = @OutputPort(name = ListFilter.OUTPUT_PORT_NAME, eventTypes = { Object.class }, description = "Provides each incoming object"))
public class ListFilter<T> extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME = "inputObject";
	public static final String OUTPUT_PORT_NAME = "outputObjects";

	private final List<T> list = Collections.synchronizedList(new ArrayList<T>());

	public ListFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@InputPort(name = ListFilter.INPUT_PORT_NAME)
	@SuppressWarnings("unchecked")
	public synchronized void input(final Object data) {
		this.list.add((T) data);
		super.deliver(OUTPUT_PORT_NAME, data);
	}

	@SuppressWarnings("unchecked")
	public synchronized List<T> getListAndClear() {
		final CopyOnWriteArrayList<T> list = new CopyOnWriteArrayList<T>((T[]) this.list.toArray());
		this.list.clear();
		return list;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
