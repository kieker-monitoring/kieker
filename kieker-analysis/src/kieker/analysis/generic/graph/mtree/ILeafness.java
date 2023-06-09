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
package kieker.analysis.generic.graph.mtree;

import kieker.analysis.exception.InternalErrorException;
import kieker.analysis.generic.graph.mtree.nodes.AbstractNode;
import kieker.analysis.generic.graph.mtree.nodes.IndexItem;

/**
 *
 *
 * @param <D>
 *            data class
 */
public interface ILeafness<D> {
	void doAddData(D data, double distance) throws InternalErrorException;

	void addChild(IndexItem<D> child, double distance) throws InternalErrorException;

	boolean doRemoveData(D data, double distance) throws InternalErrorException;

	AbstractNode<D> newSplitNodeReplacement(D data);

	void checkChildClass(IndexItem<D> child);
}
