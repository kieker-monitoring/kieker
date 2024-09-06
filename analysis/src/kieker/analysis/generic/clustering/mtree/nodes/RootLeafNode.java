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

import kieker.analysis.generic.clustering.mtree.MTree;

/**
 * @param <T>
 *            data element type
 *
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public final class RootLeafNode<T> extends AbstractNode<T> {

	protected RootLeafNode(final MTree<T> mtree, final T data) {
		super(mtree, data);
	}

	@Override
	protected int getMinCapacity() {
		return 1;
	}

	@Override
	public void checkMinCapacity() {
		assert this.getChildren().size() >= 1;
	}
}
