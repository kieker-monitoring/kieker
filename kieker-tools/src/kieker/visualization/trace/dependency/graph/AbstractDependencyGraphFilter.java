/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.visualization.trace.dependency.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.AbstractMessage;
import kieker.model.system.model.ISystemModelElement;
import kieker.visualization.trace.AbstractGraphProducingFilter;

/**
 * Refactored copy from LogAnalysis-legacy tool.
 *
 * @param <T>
 *            subtype of a ISystemModelElement
 *
 * @author Andre van Hoorn, Lena Stoever, Matthias Rohr,
 *
 * @since 1.1
 */
public abstract class AbstractDependencyGraphFilter<T extends ISystemModelElement> extends AbstractGraphProducingFilter<AbstractDependencyGraph<T>> {

	private final List<AbstractNodeDecorator> decorators = new ArrayList<>();
	private final TimeUnit timeUnit;

	/**
	 * Creates a new abstract dependency graph filter using the given data.
	 *
	 * @param configuration
	 *            The configuration to use for this filter.
	 * @param projectContext
	 *            The project context to use for this filter.
	 * @param graph
	 *            The graph to produce / extend
	 */
	public AbstractDependencyGraphFilter(final SystemModelRepository repository, final TimeUnit timeUnit, final AbstractDependencyGraph<T> graph) {
		super(repository, graph);
		this.timeUnit = timeUnit;
	}

	/**
	 * Adds a node decorator to this graph.
	 *
	 * @param decorator
	 *            The decorator to add
	 */
	public void addDecorator(final AbstractNodeDecorator decorator) {
		this.decorators.add(decorator);
	}

	/**
	 * This is a helper method to invoke all decorators and send them a message.
	 *
	 * @param message
	 *            The message to send the decorators.
	 * @param sourceNode
	 *            The source node.
	 * @param targetNode
	 *            The target node.
	 */
	protected void invokeDecorators(final AbstractMessage message, final DependencyGraphNode<?> sourceNode, final DependencyGraphNode<?> targetNode) {
		for (final AbstractNodeDecorator currentDecorator : this.decorators) {
			currentDecorator.processMessage(message, sourceNode, targetNode, this.timeUnit);
		}
	}

	/**
	 * Determines whether the given edge is assumed or not.
	 *
	 * @param source
	 *            The source of the edge.
	 * @param target
	 *            The target of the edge.
	 *
	 * @return true iff the edge is assumed (which means in fact that either the source or the target or both are assumed).
	 */
	protected boolean isDependencyAssumed(final DependencyGraphNode<?> source, final DependencyGraphNode<?> target) {
		return source.isAssumed() || target.isAssumed();
	}
}
