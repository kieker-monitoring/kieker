/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

import kieker.test.analysis.util.repository.SimpleRepository;

/**
 * A simple filter, used only for test purposes.
 *
 * @author Nils Christian Ehmke, Jan Waller
 *
 * @since 1.6
 */
@Plugin(programmaticOnly = true,
		name = SimpleForwardFilterWithRepository.FILTER_NAME, description = SimpleForwardFilterWithRepository.FILTER_DESCRIPTION,
		outputPorts = { @OutputPort(name = SimpleForwardFilterWithRepository.OUTPUT_PORT_NAME, eventTypes = { Object.class }) }, // NOCS
		repositoryPorts = @RepositoryPort(name = SimpleForwardFilterWithRepository.REPOSITORY_PORT_NAME, repositoryType = SimpleRepository.class))
public class SimpleForwardFilterWithRepository extends AbstractFilterPlugin {
	/** The dummy name of the filter. */
	public static final String FILTER_NAME = "pluginName-EfpvPSE0";
	/** The dummy description of the filter. */
	public static final String FILTER_DESCRIPTION = "pluginDescription-TB5UV1LdSz";

	/** The name of the filter's repository port. */
	public static final String REPOSITORY_PORT_NAME = "repository";
	/** The name of the filter's output port. */
	public static final String OUTPUT_PORT_NAME = "output";
	/** The name of the filter's input port. */
	public static final String INPUT_PORT_NAME = "input";

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this filter.
	 * @param projectContext
	 *            The project context for this filter.
	 */
	public SimpleForwardFilterWithRepository(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * This method represents the input port of this method, receiving the new objects.
	 *
	 * @param event
	 *            The next event.
	 */
	@InputPort(name = INPUT_PORT_NAME, eventTypes = { Object.class }) // NOCS
	public final void inputEvent(final Object event) {
		super.deliver(OUTPUT_PORT_NAME, event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
