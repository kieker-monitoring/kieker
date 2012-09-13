/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;

/**
 * Abstract superclass for graph-producing filters.
 * 
 * @author Holger Knoche
 * 
 * @param <G>
 *            The graph type created by this filter
 */
@Plugin
public abstract class AbstractGraphProducingFilter<G extends AbstractGraph<?, ?, ?>> extends AbstractMessageTraceProcessingFilter implements
		IGraphOutputtingFilter<G> {

	private final G graph;

	public AbstractGraphProducingFilter(final Configuration configuration, final G graph) {
		super(configuration);

		this.graph = graph;
	}

	public Configuration getCurrentConfiguration() {
		return this.configuration;
	}

	public abstract String getConfigurationName();

	@Override
	public void terminate(final boolean error) {
		if (!error) {
			this.deliver(this.getGraphOutputPortName(), this.getGraph());
		}
	}

	public String getGraphOutputPortName() {
		return OUTPUT_PORT_NAME_GRAPH;
	}

	protected G getGraph() {
		return this.graph;
	}

}
