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
package kieker.tools.trace.analysis.filter.visualization.descriptions;

import java.util.Map;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.filter.IGraphOutputtingFilter;
import kieker.tools.trace.analysis.filter.visualization.AbstractGraphFilter;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractEdge;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractGraph;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractGraph.IGraphVisitor;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractPayloadedVertex;
import kieker.tools.trace.analysis.filter.visualization.graph.IOriginRetentionPolicy;
import kieker.tools.trace.analysis.filter.visualization.graph.NoOriginRetentionPolicy;
import kieker.tools.trace.analysis.repository.DescriptionRepository;
import kieker.tools.trace.analysis.systemModel.ISystemModelElement;

/**
 * A filter which attaches decorations from a repository to graph entities.
 *
 * @author Holger Knoche
 *
 * @param <V>
 *            The type of the graph's vertices
 * @param <E>
 *            The type of the graph's edges
 * @param <O>
 *            The type of the origin of the graph's elements
 *
 * @since 1.6
 * @deprecated 1.15 ported to teetime
 */
@Deprecated
@Plugin(name = "", description = "This filter attaches decorations to graph entities",
		repositoryPorts = @RepositoryPort(name = DescriptionDecoratorFilter.DESCRIPTION_REPOSITORY_PORT_NAME, repositoryType = DescriptionRepository.class),
		outputPorts = @OutputPort(name = IGraphOutputtingFilter.OUTPUT_PORT_NAME_GRAPH, eventTypes = AbstractGraph.class))
public class DescriptionDecoratorFilter<V extends AbstractPayloadedVertex<V, E, O, ISystemModelElement>, E extends AbstractEdge<V, E, O>, O> extends
		AbstractGraphFilter<AbstractGraph<V, E, O>, V, E, O> implements IGraphVisitor<V, E> {

	/**
	 * Port name at which the description repository must be connected.
	 */
	public static final String DESCRIPTION_REPOSITORY_PORT_NAME = "descriptionRepository";

	private Map<String, String> decorationsMap;

	/**
	 * Creates a new description decorator filter using the given configuration.
	 *
	 * @param configuration
	 *            The configuration to use for this filter.
	 * @param projectContext
	 *            The project context to use for this filter.
	 */
	public DescriptionDecoratorFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitVertex(final V vertex) {
		final ISystemModelElement element = vertex.getPayload();
		if (element == null) {
			return;
		}

		final String decoration = this.decorationsMap.get(element.getIdentifier());
		vertex.setDescription(decoration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitEdge(final E edge) {
		// Not yet supported
	}

	private void initialize() {
		final DescriptionRepository repository = (DescriptionRepository) this.getRepository(DESCRIPTION_REPOSITORY_PORT_NAME);
		this.decorationsMap = repository.getDescriptionMap();
	}

	@Override
	protected AbstractGraph<V, E, O> performConcreteGraphProcessing(final AbstractGraph<V, E, O> graph) {
		this.initialize();

		graph.traverse(this);

		return graph;
	}

	@Override
	protected IOriginRetentionPolicy getDesiredOriginRetentionPolicy() {
		return NoOriginRetentionPolicy.createInstance();
	}

}
