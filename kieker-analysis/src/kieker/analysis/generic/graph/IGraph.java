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
package kieker.analysis.generic.graph;

import java.util.Optional;

import com.google.common.graph.MutableNetwork;

/**
 * The Kieker IGraph is based on the google Graph library. It addes primarily the notion of a name for the graph and subgraphs inside nodes.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public interface IGraph {

	/**
	 * Get the underlying google graph (network).
	 *
	 * @return return the graph
	 */
	public MutableNetwork<INode, IEdge> getGraph();

	/**
	 * @return returns the graph label
	 */
	public String getLabel();

	/**
	 * Set the graph label.
	 *
	 * @param label
	 *            label of the graph
	 */
	public void setLabel(String label);

	/**
	 * Find a node by its id.
	 *
	 * @param id
	 *            id of the node
	 * @return returns an optional which may contain the node or nothing when no node with the given id exists
	 */
	public Optional<INode> findNode(String id);

	/**
	 * Find an edge by its id.
	 *
	 * @param id
	 *            id of the edge
	 * @return returns an optional which may contain the edge or nothing when no edge with the given id exists
	 */
	public Optional<IEdge> findEdge(String id);
}
