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
package kieker.analysis.generic.graph.mtree.nodes;

import kieker.analysis.generic.graph.mtree.ILeafness;

/**
 * @param <T>
 *            data element type
 *
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public class LeafNodeTrait<T> extends AbstractNodeTrait<T> implements ILeafness<T> {

	public LeafNodeTrait(final AbstractNode<T> thisNode) {
		super(thisNode);
	}

	@Override
	public void doAddData(final T data, final double distance) {
		final Entry<T> entry = new Entry<T>(data);
		assert !this.thisNode.getChildren().containsKey(data);
		this.thisNode.getChildren().put(data, entry);
		assert this.thisNode.getChildren().containsKey(data);
		this.thisNode.updateMetrics(entry, distance);
	}

	@Override
	public void addChild(final IndexItem<T> child, final double distance) {
		assert !this.thisNode.getChildren().containsKey(child.getData());
		this.thisNode.getChildren().put(child.getData(), child);
		assert this.thisNode.getChildren().containsKey(child.getData());
		this.thisNode.updateMetrics(child, distance);
	}

	@Override
	public AbstractNode<T> newSplitNodeReplacement(final T data) {
		return NodeFactory.createLeafNode(this.thisNode.getMTree(), data);
	}

	@Override
	public boolean doRemoveData(final T data, final double distance) {
		return this.thisNode.getChildren().remove(data) != null;
	}

	@Override
	public void checkChildClass(final IndexItem<T> child) {
		assert child instanceof Entry;
	}
}
