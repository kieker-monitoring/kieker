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

import kieker.analysis.exception.InternalErrorException;
import kieker.analysis.generic.graph.mtree.IRootness;

/**
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public class RootNodeTrait<T> extends AbstractNodeTrait<T> implements IRootness {

	public RootNodeTrait(final AbstractNode<T> thisNode) {
		super(thisNode);
	}

	@Override
	public int getMinCapacity() throws InternalErrorException {
		throw new InternalErrorException("Should not be called!");
	}

	@Override
	public void checkDistanceToParent() {
		assert this.thisNode.getDistanceToParent() == -1;
	}

	@Override
	public void checkMinCapacity() {
		this.thisNode.checkMinCapacity();
	}
}
