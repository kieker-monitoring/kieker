/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.Operation;

/**
 * This class represents a single node within the call tree.
 * 
 * @author Andre van Hoorn
 * 
 * @deprecated To be removed in Kieker 1.8.
 */
@Deprecated
@SuppressWarnings("deprecation")
public class CallTreeNode {

	private final CallTreeNode parent;
	private final List<CallTreeNode> children = Collections.synchronizedList(new ArrayList<CallTreeNode>());
	private final kieker.tools.traceAnalysis.filter.visualization.callTree.CallTreeOperationHashKey opInfo;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param parent
	 *            The parent of this node. If this is null, the node will be interpreted as a root node.
	 * @param opInfo
	 *            The info to be stored in this node.
	 * 
	 * @deprecated To be removed in Kieker 1.8.
	 */
	@Deprecated
	@SuppressWarnings("deprecation")
	public CallTreeNode(final kieker.tools.traceAnalysis.filter.visualization.callTree.CallTreeNode parent,
			final kieker.tools.traceAnalysis.filter.visualization.callTree.CallTreeOperationHashKey opInfo) {
		this.parent = parent;
		if (opInfo == null) {
			throw new IllegalArgumentException("opInfo must not be null");
		}
		this.opInfo = opInfo;
	}

	/**
	 * Delivers a collection containing the child nodes.
	 * 
	 * @return The children of this node.
	 */
	public final Collection<CallTreeNode> getChildren() {
		return this.children;
	}

	/**
	 * Creates a new child and adds it to the nodes list of children.
	 * 
	 * @param allocationComponent
	 *            The allocation component used for the hash key within the new node.
	 * @param operation
	 *            The operation used for the hash key within the new node.
	 * 
	 * @return The newly created node.
	 */
	public final CallTreeNode createNewChild(final AllocationComponent allocationComponent, final Operation operation) {
		final kieker.tools.traceAnalysis.filter.visualization.callTree.CallTreeOperationHashKey k =
				new kieker.tools.traceAnalysis.filter.visualization.callTree.CallTreeOperationHashKey(allocationComponent, operation);
		final CallTreeNode node = new CallTreeNode(this, k);
		this.children.add(node);
		return node;
	}

	/**
	 * Returns the child node with given operation, name, and vmName. The node is created if it doesn't exist.
	 * 
	 * @param allocationComponent
	 *            The allocation component which is used to find the node.
	 * @param operation
	 *            The operation which is used to find the node.
	 * 
	 * @return The corresponding node to the given parameters if it exists, a new node otherwise.
	 */
	public final CallTreeNode getChild(final AllocationComponent allocationComponent, final Operation operation) {
		final CallTreeOperationHashKey k = new CallTreeOperationHashKey(allocationComponent, operation);
		CallTreeNode node = null;
		for (final CallTreeNode n : this.children) {
			if (n.opInfo.equals(k)) {
				node = n;
			}
		}
		if (node == null) {
			node = new CallTreeNode(this, k);
			this.children.add(node);
		}
		return node;
	}

	/**
	 * Delivers the allocation component stored within this node.
	 * 
	 * @return The allocation component.
	 */
	public final AllocationComponent getAllocationComponent() {
		return this.opInfo.getAllocationComponent();
	}

	/**
	 * Delivers the operation stored within this node.
	 * 
	 * @return The operation.
	 */
	public final Operation getOperation() {
		return this.opInfo.getOperation();
	}

	/**
	 * Delivers the parent of this node.
	 * 
	 * @return The parent.
	 */
	public final CallTreeNode getParent() {
		return this.parent;
	}

	/**
	 * Tells whether the current node is the root or not.
	 * 
	 * @return true if and only if this node should be interpreted as a root.
	 */
	public final boolean isRootNode() {
		return this.parent == null;
	}
}
