/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

/**
 * 
 * @param <T>
 * 
 * @author Andre van Hoorn
 */
public class WeightedBidirectionalDependencyGraphEdge<T> {
	private DependencyGraphNode<T> source;
	private DependencyGraphNode<T> destination;

	private int outgoingWeight = 0;
	private int incomingWeight = 0;

	private boolean assumed = false;

	public WeightedBidirectionalDependencyGraphEdge() {
		// nothing to do
	};

	public WeightedBidirectionalDependencyGraphEdge(final DependencyGraphNode<T> source, final DependencyGraphNode<T> destination) {
		this.source = source;
		this.destination = destination;
	}

	public boolean isAssumed() {
		return this.assumed;
	}

	public void setAssumed() {
		this.assumed = true;
	}

	public final DependencyGraphNode<T> getDestination() {
		return this.destination;
	}

	public final DependencyGraphNode<T> getSource() {
		return this.source;
	}

	public final void setDestination(final DependencyGraphNode<T> destination) {
		this.destination = destination;
	}

	public final void setSource(final DependencyGraphNode<T> source) {
		this.source = source;
	}

	public final int getIncomingWeight() {
		return this.incomingWeight;
	}

	public final int getOutgoingWeight() {
		return this.outgoingWeight;
	}

	public final void incOutgoingWeight() {
		this.outgoingWeight++;
	}

	public final void incIncomingWeight() {
		this.incomingWeight++;
	}
}
