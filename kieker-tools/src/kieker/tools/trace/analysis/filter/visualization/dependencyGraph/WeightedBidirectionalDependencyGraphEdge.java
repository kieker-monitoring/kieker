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

package kieker.tools.trace.analysis.filter.visualization.dependencyGraph;

import kieker.tools.trace.analysis.filter.visualization.graph.AbstractWeightedEdge;
import kieker.tools.trace.analysis.filter.visualization.graph.IOriginRetentionPolicy;
import kieker.tools.trace.analysis.systemModel.ISystemModelElement;
import kieker.tools.trace.analysis.systemModel.TraceInformation;

/**
 * This class represents a weighted but bidirected edge within a dependency graph.
 *
 * @param <T>
 *            The type of the entity stored in the nodes linked by this edge.
 *
 * @author Andre van Hoorn
 *
 * @since 1.1
 * @deprecated 1.15 ported to teetime
 */
@Deprecated
public class WeightedBidirectionalDependencyGraphEdge<T extends ISystemModelElement> extends
		AbstractWeightedEdge<DependencyGraphNode<T>, WeightedBidirectionalDependencyGraphEdge<T>, TraceInformation> {

	private boolean assumed; // false

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
	public WeightedBidirectionalDependencyGraphEdge(final DependencyGraphNode<T> source, final DependencyGraphNode<T> target, final TraceInformation origin,
			final IOriginRetentionPolicy originPolicy) {
		super(source, target, origin, originPolicy);
	}

	public boolean isAssumed() {
		return this.assumed;
	}

	/**
	 * Sets the assumed flag to {@code true}.
	 */
	public void setAssumed() {
		this.assumed = true;
	}

}
