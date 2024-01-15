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

package kieker.analysis.generic.graph.flattening;

import com.google.common.graph.MutableNetwork;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;

/**
 * @author Sören Henning
 *
 * @param <N>
 *            node type
 * @param <E>
 *            edge type
 *
 * @since 1.14
 */
public interface IGraphFlattener<N extends INode, E extends IEdge> {

	/**
	 * @param graph
	 *            graph to be flattened
	 *
	 * @since 1.14
	 */
	public void flatten(MutableNetwork<N, E> graph);

}
