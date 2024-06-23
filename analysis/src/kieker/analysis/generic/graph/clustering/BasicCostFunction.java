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
package kieker.analysis.generic.graph.clustering;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;

/**
 * Generic cost function implementation for graphs.
 *
 * @author Reiner Jung
 *
 * @param <N>
 *            node type
 * @param <E>
 *            edge type
 */
public class BasicCostFunction<N extends INode, E extends IEdge> {

	private final double nodeInsertionCost;
	private final double edgeInsertionCost;

	public BasicCostFunction(final double nodeInsertionCost, final double edgeInsertionCost) {
		this.nodeInsertionCost = nodeInsertionCost;
		this.edgeInsertionCost = edgeInsertionCost;
	}

	public double computeNodeInsertionCost(final N node) {
		return this.nodeInsertionCost;
	}

	public double computeEdgeInsertionCost(final E edge) {
		return this.edgeInsertionCost;
	}

	public double nodeAnnotationDistance(final N node1, final N node2) {
		return 0;
	}

	public double edgeAnnotationDistance(final E edge1, final E edge2) {
		return 0;
	}

}
