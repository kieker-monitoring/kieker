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

package kieker.tools.traceAnalysis.filter.visualization.dependencyGraph;

import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractWeightedEdge;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;

/**
 * 
 * @param <T>
 * 
 * @author Andre van Hoorn
 */
public class WeightedBidirectionalDependencyGraphEdge<T> extends
		AbstractWeightedEdge<DependencyGraphNode<T>, WeightedBidirectionalDependencyGraphEdge<T>, MessageTrace> {

	private boolean assumed; // false

	public WeightedBidirectionalDependencyGraphEdge(final DependencyGraphNode<T> source, final DependencyGraphNode<T> target, final MessageTrace origin) {
		super(source, target, origin);
	}

	public boolean isAssumed() {
		return this.assumed;
	}

	public void setAssumed() {
		this.assumed = true;
	}

}
