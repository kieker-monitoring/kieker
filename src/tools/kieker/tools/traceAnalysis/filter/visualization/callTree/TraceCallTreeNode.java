/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.visualization.callTree;

import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.util.AllocationComponentOperationPair;

/**
 * 
 * @author Andre van Hoorn
 */
public class TraceCallTreeNode extends AbstractCallTreeNode<AllocationComponentOperationPair> {

	public TraceCallTreeNode(final int id, final AllocationComponentOperationPair entity, final boolean rootNode, final MessageTrace origin) {
		super(id, entity, rootNode, origin);
	}

	@Override
	public AbstractCallTreeNode<AllocationComponentOperationPair> newCall(final Object destination, final MessageTrace origin) {
		final AllocationComponentOperationPair destPair = (AllocationComponentOperationPair) destination;
		final TraceCallTreeNode destNode = new TraceCallTreeNode(destPair.getId(), destPair, false, origin);
		final WeightedDirectedCallTreeEdge<AllocationComponentOperationPair> e = new WeightedDirectedCallTreeEdge<AllocationComponentOperationPair>(this, destNode,
				origin);
		super.appendChildEdge(e);
		return destNode;
	}

	@Override
	public String getIdentifier() {
		return null;
	}
}
