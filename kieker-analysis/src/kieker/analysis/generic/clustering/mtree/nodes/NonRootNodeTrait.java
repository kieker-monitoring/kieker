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
package kieker.analysis.generic.clustering.mtree.nodes;

import kieker.analysis.generic.clustering.mtree.IRootness;

/**
 * @param <T>
 *            data element type
 *
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public class NonRootNodeTrait<T> extends AbstractNodeTrait<T> implements IRootness {

	public NonRootNodeTrait(final AbstractNode<T> thisNode) {
		super(thisNode);
	}

	@Override
	public int getMinCapacity() {
		return this.thisNode.getMTree().getMinNodeCapacity();
	}

	@Override
	public void checkMinCapacity() {
		assert this.thisNode.getChildren().size() >= this.thisNode.getMTree().getMinNodeCapacity();
	}

	@Override
	public void checkDistanceToParent() {
		assert this.thisNode.getDistanceToParent() >= 0;
	}
};
