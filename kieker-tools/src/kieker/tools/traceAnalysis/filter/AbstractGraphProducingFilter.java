/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter;

import kieker.analysis.IProjectContext;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraphElement;
import kieker.tools.traceAnalysis.filter.visualization.graph.IOriginRetentionPolicy;
import kieker.tools.traceAnalysis.filter.visualization.graph.NoOriginRetentionPolicy;

/**
 * Abstract superclass for graph-producing filters.
 * 
 * @author Holger Knoche
 * 
 * @param <G>
 *            The graph type created by this filter
 * 
 * @since 1.6
 */
@Plugin
public abstract class AbstractGraphProducingFilter<G extends AbstractGraph<?, ?, ?>> extends AbstractMessageTraceProcessingFilter implements
		IGraphProducingFilter<G> {

	private static final String INCOMPATIBLE_RETENTION_ERROR_TEMPLATE =
			"%s: The current retention policy %s is incompatible with the requested retention policy %s.";

	private final G graph;
	private IOriginRetentionPolicy originRetentionPolicy = NoOriginRetentionPolicy.createInstance();

	/**
	 * Creates a new graph-producing filter using the given configuration and the given graph.
	 * 
	 * @param configuration
	 *            The configuration to use
	 * @param projectContext
	 *            The project context to use.
	 * @param graph
	 *            The (usually empty) graph to produce / extend
	 */
	public AbstractGraphProducingFilter(final Configuration configuration, final IProjectContext projectContext, final G graph) {
		super(configuration, projectContext);

		this.graph = graph;
	}

	/**
	 * Returns this filter's configuration name.
	 * 
	 * @return See above
	 */
	public abstract String getConfigurationName();

	@Override
	public void terminate(final boolean error) {
		if (!error) {
			this.deliver(this.getGraphOutputPortName(), this.getGraph());
		}
	}

	@Override
	public String getGraphOutputPortName() {
		return OUTPUT_PORT_NAME_GRAPH;
	}

	/**
	 * Delivers the graph stored in this filter.
	 * 
	 * @return The graph.
	 */
	protected G getGraph() {
		return this.graph;
	}

	protected IOriginRetentionPolicy getOriginRetentionPolicy() {
		return this.originRetentionPolicy;
	}

	@Override
	public void requestOriginRetentionPolicy(final IOriginRetentionPolicy policy) throws AnalysisConfigurationException {
		if (!this.originRetentionPolicy.isCompatibleWith(policy)) {
			throw new AnalysisConfigurationException(String.format(INCOMPATIBLE_RETENTION_ERROR_TEMPLATE, this, this.originRetentionPolicy, policy));
		}

		this.originRetentionPolicy = this.originRetentionPolicy.uniteWith(policy);
	}

	protected <T> void handleOrigin(final AbstractGraphElement<T> element, final T origin) {
		this.getOriginRetentionPolicy().handleOrigin(element, origin);
	}

}
