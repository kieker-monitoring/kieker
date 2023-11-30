/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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

import java.util.Optional;

import com.google.common.graph.MutableNetwork;

import kieker.analysis.generic.clustering.mtree.IDistanceFunction;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;

/**
 * This class calculates a custom graph edit distance between two Behavior Models.
 *
 * The following operations are allowed:
 *
 * insert/delete nodes; insert/delete edges; insert/delete EventGroups; insert/delete
 * Events; duplicate/remove duplicate events
 *
 * Insertion and Deletion always costs the same to satisfy the symmetry property.
 *
 * @param <N>
 *            node type
 * @param <E>
 *            edge type
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class GraphEditDistance<N extends INode, E extends IEdge> implements IDistanceFunction<MutableNetwork<N, E>> {

	private final BasicCostFunction<N, E> costFunction;

	/**
	 * This can be used, to set the node, edge and eventgroup insertion cost for the Graph Edit
	 * Distance Algorithm to the values in the configuration.
	 *
	 * @param costFunction
	 *            cost function
	 */
	public GraphEditDistance(final BasicCostFunction<N, E> costFunction) {
		this.costFunction = costFunction;
	}

	/**
	 * Calculates the Graph Edit Distance between two objects.
	 *
	 * @param modelA
	 *            The first model.
	 * @param modelB
	 *            The second model.
	 */
	@Override
	public double calculate(final MutableNetwork<N, E> modelA, final MutableNetwork<N, E> modelB) {
		double distance = 0;

		// check if nodes from model1 are in model2
		for (final N node : modelA.nodes()) {
			final Optional<N> match = this.findNode(modelB, node.getId());

			if (!match.isPresent()) { // node only occurs in one objects => must be inserted
				distance += this.nodeInsertionCost(modelA, node);
			} else { // node occurs in both objects => must be compared
				distance += this.nodeDistance(modelA, node, modelB, match.get());
			}
		}

		// check if nodes from model2 are in model1
		for (final N node : modelB.nodes()) {
			final Optional<N> match = this.findNode(modelA, node.getId());

			// node only occurs in one objects => must be inserted
			if (!match.isPresent()) {
				distance += this.nodeInsertionCost(modelB, node);
			}
		}
		return distance;
	}

	private Optional<N> findNode(final MutableNetwork<N, E> model, final String signature) {
		return model.nodes().stream().filter(node -> node.getId().equals(signature)).findFirst();
	}

	/**
	 * Calculates the distance between two nodes. This includes the distance between the ingoing
	 * edges.h
	 */
	private double nodeDistance(final MutableNetwork<N, E> modelA, final N nodeA, final MutableNetwork<N, E> modelB, final N nodeB) {
		double distance = this.costFunction.nodeAnnotationDistance(nodeA, nodeB);
		for (final E edge : modelA.inEdges(nodeA)) {
			final N sourceA = modelA.incidentNodes(edge).source();
			final N sourceB = this.findNode(modelB, sourceA.getId()).get();

			// find the matching edge.
			final Optional<E> match = this.findEdge(modelB, sourceB, nodeB);

			if (match.isPresent()) { // edge occurs in both nodes => must be compared
				distance += this.costFunction.edgeAnnotationDistance(edge, match.get());
			} else { // edge only occurs in one node => must be inserted
				distance += this.costFunction.computeEdgeInsertionCost(edge);
			}
		}
		for (final E edge : modelB.inEdges(nodeB)) {
			final N sourceB = modelB.incidentNodes(edge).source();
			final N sourceA = this.findNode(modelA, sourceB.getId()).get();

			final Optional<E> match = this.findEdge(modelA, sourceA, nodeA);

			// edge only occurs in one node => must be inserted
			if (!match.isPresent()) {
				distance += this.costFunction.computeEdgeInsertionCost(edge);
			}
		}
		return distance;
	}

	private Optional<E> findEdge(final MutableNetwork<N, E> model, final N source, final N target) {
		return model.inEdges(target).stream().filter(edge -> model.incidentNodes(edge).source().equals(source)).findFirst();
	}

	/**
	 * calculates the insertion cost of a node including the insertion cost of the ingoing edges.
	 *
	 * @param model
	 *            graph
	 * @param node
	 *            node for which we whan to compute the cost
	 */
	private double nodeInsertionCost(final MutableNetwork<N, E> model, final N node) {
		double distance = this.costFunction.computeNodeInsertionCost(node);

		for (final E edge : model.inEdges(node)) {
			distance += this.costFunction.computeEdgeInsertionCost(edge);
		}
		return distance;
	}

}
