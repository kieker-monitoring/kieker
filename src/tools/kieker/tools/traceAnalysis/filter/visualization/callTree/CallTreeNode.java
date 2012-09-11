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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.Operation;

/**
 * @author Andre van Hoorn
 */
public class CallTreeNode {

	private final CallTreeNode parent;
	private final List<CallTreeNode> children = Collections.synchronizedList(new ArrayList<CallTreeNode>());
	private final CallTreeOperationHashKey opInfo;

	public CallTreeNode(final CallTreeNode parent, final CallTreeOperationHashKey opInfo) {
		this.parent = parent;
		if (opInfo == null) {
			throw new IllegalArgumentException("opInfo must not be null");
		}
		this.opInfo = opInfo;
	}

	public final Collection<CallTreeNode> getChildren() {
		return this.children;
	}

	/** Creates a new child and adds it to the nodes list of children */
	public final CallTreeNode createNewChild(final AllocationComponent allocationComponent, final Operation operation) {
		final CallTreeOperationHashKey k = new CallTreeOperationHashKey(allocationComponent, operation);
		final CallTreeNode node = new CallTreeNode(this, k);
		this.children.add(node);
		return node;
	}

	/**
	 * Returns the child node with given operation, name, and vmName.
	 * The node is created if it doesn't exist.
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

	public final AllocationComponent getAllocationComponent() {
		return this.opInfo.getAllocationComponent();
	}

	public final Operation getOperation() {
		return this.opInfo.getOperation();
	}

	public final CallTreeNode getParent() {
		return this.parent;
	}

	public final boolean isRootNode() {
		return this.parent == null;
	}
}
