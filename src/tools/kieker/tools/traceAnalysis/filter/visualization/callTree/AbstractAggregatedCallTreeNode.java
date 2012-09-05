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

import java.util.Map;
import java.util.TreeMap;

import kieker.tools.traceAnalysis.systemModel.MessageTrace;

/**
 * 
 * @param <T>
 * 
 * @author Andre van Hoorn
 */
public abstract class AbstractAggregatedCallTreeNode<T> extends AbstractCallTreeNode<T> {

	/** For faster lookup of existing children */
	protected final Map<Integer, WeightedDirectedCallTreeEdge<T>> childMap = new TreeMap<Integer, WeightedDirectedCallTreeEdge<T>>(); // NOPMD (not synchronized)

	public AbstractAggregatedCallTreeNode(final int id, final T entity, final boolean rootNode, final MessageTrace origin) {
		super(id, entity, rootNode, origin);
	}
}
