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

package kieker.analysisteetime.util.graph;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public interface Vertex extends GraphElement {

	/**
	 * @since 1.14
	 */
	public Graph addChildGraph();

	/**
	 * @since 1.14
	 */
	public Graph addChildGraphIfAbsent();

	/**
	 * @since 1.14
	 */
	public boolean hasChildGraph();

	/**
	 * @since 1.14
	 */
	public Graph getChildGraph();

	/**
	 * @since 1.14
	 */
	public void removeChildGraph();

	/**
	 * @since 1.14
	 */
	public int getDepth();

	/**
	 * @since 1.14
	 */
	public Iterable<Edge> getEdges(Direction direction);

	/**
	 * @since 1.14
	 */
	public Iterable<Vertex> getVertices(Direction direction);

	/**
	 * @since 1.14
	 */
	public Edge addEdge(Vertex inVertex);

	/**
	 * @since 1.14
	 */
	public Edge addEdge(Object id, Vertex inVertex);

	/**
	 * @since 1.14
	 */
	public Edge addEdgeIfAbsent(Object id, Vertex inVertex);

}
