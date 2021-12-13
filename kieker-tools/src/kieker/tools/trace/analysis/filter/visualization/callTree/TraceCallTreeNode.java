/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

import kieker.tools.trace.analysis.filter.visualization.graph.IOriginRetentionPolicy;
import kieker.tools.trace.analysis.systemModel.MessageTrace;
import kieker.tools.trace.analysis.systemModel.util.AllocationComponentOperationPair;

/**
 * This class represents a single node within the trace call tree.
 *
 * @author Andre van Hoorn
 *
 * @since 1.1
 * @deprecated 1.15 ported to teetime
 */
@Deprecated
public class TraceCallTreeNode extends AbstractCallTreeNode<AllocationComponentOperationPair> {

	/**
	 * Creates a new instance of this class using the given parameters.
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
	public TraceCallTreeNode(final int id, final AllocationComponentOperationPair entity, final boolean rootNode, final MessageTrace origin,
			final IOriginRetentionPolicy originPolicy) {
		super(id, entity, rootNode, origin, originPolicy);
	}

	@Override
	public AbstractCallTreeNode<AllocationComponentOperationPair> newCall(final AllocationComponentOperationPair destination, final MessageTrace origin,
			final IOriginRetentionPolicy originPolicy) {
		final AllocationComponentOperationPair destPair = destination;
		final TraceCallTreeNode destNode = new TraceCallTreeNode(destPair.getId(), destPair, false, origin, originPolicy);
		final WeightedDirectedCallTreeEdge<AllocationComponentOperationPair> e = new WeightedDirectedCallTreeEdge<>(this, destNode, origin, originPolicy);
		super.appendChildEdge(e);
		return destNode;
	}

	@Override
	public String getIdentifier() {
		return null;
	}
}
