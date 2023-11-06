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

package kieker.tools.trace.analysis.filter.visualization.callTree;

import java.util.Map;
import java.util.TreeMap;

import kieker.tools.trace.analysis.filter.visualization.graph.IOriginRetentionPolicy;
import kieker.tools.trace.analysis.systemModel.MessageTrace;

/**
 * This is an abstract base for a single node within an aggregated call tree.
 *
 * @param <T>
 *            The type of the entity to be stored in this node.
 *
 * @author Andre van Hoorn
 *
 * @since 1.1
 * @deprecated 1.15 moved to teetime
 */
@Deprecated
public abstract class AbstractAggregatedCallTreeNode<T> extends AbstractCallTreeNode<T> {

	/** For faster lookup of existing children. */
	protected final Map<Integer, WeightedDirectedCallTreeEdge<T>> childMap = new TreeMap<>(); // NOPMD (not synchronized)

	/**
	 * This constructor uses the given parameters to initialize the fields of this class.
	 *
	 * @param id
	 *            The identifier of this node.
	 * @param entity
	 *            The content of this node.
	 * @param rootNode
	 *            Determines whether this node is the root node or not.
	 * @param origin
	 *            The meta data of this node.
	 * @param originPolicy
	 *            The origin policy.
	 */
	public AbstractAggregatedCallTreeNode(final int id, final T entity, final boolean rootNode, final MessageTrace origin,
			final IOriginRetentionPolicy originPolicy) {
		super(id, entity, rootNode, origin, originPolicy);
	}
}
