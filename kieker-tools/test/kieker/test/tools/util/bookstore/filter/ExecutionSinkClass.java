/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.util.bookstore.filter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.systemModel.Execution;

/**
 * This is just a simple helper class which collects {@link Execution}s.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.5
 */
public class ExecutionSinkClass extends AbstractFilterPlugin {

	/**
	 * The name of the default input port.
	 */
	public static final String INPUT_PORT_NAME = "doJob";

	/**
	 * This list will contain the records this plugin received.
	 */
	private final CopyOnWriteArrayList<Execution> lst = new CopyOnWriteArrayList<>();

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this plugin. It will not be used.
	 * @param projectContext
	 *            The project context for this plugin. It will not be used.
	 */
	public ExecutionSinkClass(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	/**
	 * This method represents the input ports for the execution objects.
	 *
	 * @param data
	 *            The next execution.
	 */
	@InputPort(name = ExecutionSinkClass.INPUT_PORT_NAME, eventTypes = Execution.class)
	public void doJob(final Object data) {
		this.lst.add((Execution) data);
	}

	/**
	 * Delivers the list containing the received records.
	 *
	 * @return The list with the records.
	 */
	public List<Execution> getExecutions() {
		return this.lst;
	}
}
