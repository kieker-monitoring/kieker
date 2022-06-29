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

/**
 * @author Sören Henning
 *
 * @since 1.14
 * @deprecated since 1.15 will be replaced by google graph library
 */
@Deprecated
public interface IVertex extends IGraphElement {

	/**
	 * @since 1.14
	 */
	public IGraph addChildGraph();

	/**
	 * @since 1.14
	 */
	public IGraph addChildGraphIfAbsent();

	/**
	 * @since 1.14
	 */
	public boolean hasChildGraph();

	/**
	 * @since 1.14
	 */
	public IGraph getChildGraph();

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
	public Iterable<IEdge> getEdges(Direction direction);

	/**
	 * @since 1.14
	 */
	public Iterable<IVertex> getVertices(Direction direction);

	/**
	 * @since 1.14
	 */
	public IEdge addEdge(IVertex inVertex);

	/**
	 * @since 1.14
	 */
	public IEdge addEdge(Object id, IVertex inVertex);

	/**
	 * @since 1.14
	 */
	public IEdge addEdgeIfAbsent(Object id, IVertex inVertex);

}
