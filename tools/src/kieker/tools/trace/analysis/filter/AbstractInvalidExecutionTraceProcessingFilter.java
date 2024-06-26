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

package kieker.tools.trace.analysis.filter;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;

/**
 * This is an abstract base for filters processing invalid execution traces.
 *
 * @author Andre van Hoorn
 *
 * @since 1.2
 * @deprecated 1.15 has no relevant functionality in the teetime port
 */
@Deprecated
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class))
public abstract class AbstractInvalidExecutionTraceProcessingFilter extends AbstractTraceProcessingFilter {

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public AbstractInvalidExecutionTraceProcessingFilter(final Configuration configuration,
			final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * Implementing classes should return the name of the input port for the invalid
	 * executions.
	 *
	 * @return The name of the input port.
	 */
	public abstract String getInvalidExecutionTraceInputPortName();

}
