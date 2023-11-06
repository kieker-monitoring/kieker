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
package kieker.visualization.trace.call.tree;

import kieker.model.system.model.MessageTrace;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractWeightedEdge;
import kieker.tools.trace.analysis.filter.visualization.graph.IOriginRetentionPolicy;

/**
 * This class represents a weighted and directed edge within a call tree.
 *
 * @param <T>
 *            The type of the entity to be stored in the nodes linked by this edge.
 *
 * @author Andre van Hoorn
 *
 * @since 1.1
 */
public class WeightedDirectedCallTreeEdge<T> extends AbstractWeightedEdge<AbstractCallTreeNode<T>, WeightedDirectedCallTreeEdge<T>, MessageTrace> {

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param source
	 *            The source of this edge.
	 * @param target
	 *            The target of this edge.
	 * @param origin
	 *            The meta information for this edge.
	 * @param originPolicy
	 *            The origin policy.
	 */
	public WeightedDirectedCallTreeEdge(final AbstractCallTreeNode<T> source, final AbstractCallTreeNode<T> target, final MessageTrace origin,
			final IOriginRetentionPolicy originPolicy) {
		super(source, target, origin, originPolicy);
	}
}
