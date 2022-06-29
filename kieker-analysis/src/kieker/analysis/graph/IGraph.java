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

package kieker.analysis.graph;

import kieker.analysis.graph.impl.GraphImpl;

/**
 * @author Sören Henning
 *
 * @since 1.14
 * @deprecated since 1.15 will be replaced by google graph library
 */
@Deprecated
public interface IGraph extends IElement {

	/**
	 * Factory method to create a new graph using the default implementation.
	 *
	 * @return a new graph instance
	 * @since 1.14
	 */
	public static IGraph create() {
		// Create an anonymous subclass of GraphImpl to access its protected constructor
		return new GraphImpl() {
		};
	}

	/**
	 * @since 1.14
	 */
	public String getName();

	/**
	 * @since 1.14
	 */
	public void setName(String name);

	/**
	 * @since 1.14
	 */
	public IVertex addVertex(Object id);

	/**
	 * @since 1.14
	 */
	public IVertex addVertexIfAbsent(Object id);

	/**
	 * @since 1.14
	 */
	public IVertex getVertex(Object id);

	/**
	 * @since 1.14
	 */
	public void removeVertex(IVertex vertex);

	/**
	 * @since 1.14
	 */
	public Iterable<IVertex> getVertices();

	/**
	 * @since 1.14
	 */
	public IEdge addEdge(Object id, IVertex outVertex, IVertex inVertex);

	/**
	 * @since 1.14
	 */
	public IEdge addEdgeIfAbsent(Object id, IVertex outVertex, IVertex inVertex);

	/**
	 * @since 1.14
	 */
	public IEdge getEdge(Object id);

	/**
	 * @since 1.14
	 */
	public void removeEdge(IEdge edge);

	/**
	 * @since 1.14
	 */
	public Iterable<IEdge> getEdges();

}
