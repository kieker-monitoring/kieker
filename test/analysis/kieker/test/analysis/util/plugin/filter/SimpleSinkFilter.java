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

package kieker.test.analysis.util.plugin.filter;

import java.util.ArrayList;
import java.util.List;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * TODO: This filter should be improved/fixed with respect to thread safety
 * TOOD: This filter could be made available in the core distro (not only in the tests package)
 * TODO: Could additionally be a relay/forward filter, i.e., forward incoming events to an output port
 * 
 * @param <T>
 * 
 * @author Nils Ehmke, Jan Waller
 */
@Plugin
public class SimpleSinkFilter<T> extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME = "inputObject";

	// TODO: Use a concurrent data structure instead
	private final List<T> list = new ArrayList<T>();

	public SimpleSinkFilter(final Configuration configuration) {
		super(configuration);
	}

	@InputPort(name = SimpleSinkFilter.INPUT_PORT_NAME)
	// TODO: we could run into trouble here because the port accepts events of type Object
	public void input(final T data) {
		synchronized (this) {
			this.list.add(data);
		}
	}

	public void clear() {
		synchronized (this) {
			this.list.clear();
		}
	}

	// TODO: this is pretty dangerous (return a read-only copy or alike?)
	public List<T> getList() {
		return this.list;
	}

	/**
	 * Returns the current number of collected objects.
	 * 
	 * @return
	 */
	public int size() {
		synchronized (this) {
			return this.list.size();
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
