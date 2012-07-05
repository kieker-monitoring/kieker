/***************************************************************************
 * Copyright 2012 by
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractVertex;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractVertexDecoration;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;

/**
 * 
 * @param <T>
 * 
 * @author Andre van Hoorn
 */
public class DependencyGraphNode<T> extends AbstractVertex<DependencyGraphNode<T>, WeightedBidirectionalDependencyGraphEdge<T>, MessageTrace> {

	public static final int ROOT_NODE_ID = AbstractSystemSubRepository.ROOT_ELEMENT_ID;
	public static final String ROOT_NODE_NAME = "$";

	private final T entity;
	private final int id;
	private final Map<Integer, WeightedBidirectionalDependencyGraphEdge<T>> incomingDependencies = new ConcurrentHashMap<Integer, WeightedBidirectionalDependencyGraphEdge<T>>(); // NOPMD(UseConcurrentHashMap)//NOCS
	private final Map<Integer, WeightedBidirectionalDependencyGraphEdge<T>> outgoingDependencies = new ConcurrentHashMap<Integer, WeightedBidirectionalDependencyGraphEdge<T>>(); // NOPMD(UseConcurrentHashMap)//NOCS

	private final Map<Integer, WeightedBidirectionalDependencyGraphEdge<T>> assumedIncomingDependencies = new ConcurrentHashMap<Integer, WeightedBidirectionalDependencyGraphEdge<T>>(); // NOPMD(UseConcurrentHashMap)//NOCS
	private final Map<Integer, WeightedBidirectionalDependencyGraphEdge<T>> assumedOutgoingDependencies = new ConcurrentHashMap<Integer, WeightedBidirectionalDependencyGraphEdge<T>>(); // NOPMD(UseConcurrentHashMap)//NOCS

	private volatile boolean assumed = false;

	public DependencyGraphNode(final int id, final T entity, final MessageTrace origin) {
		super(origin);
		this.id = id;
		this.entity = entity;
	}

	public final T getEntity() {
		return this.entity;
	}

	public final Collection<WeightedBidirectionalDependencyGraphEdge<T>> getIncomingDependencies() {
		return this.incomingDependencies.values();
	}

	public final Collection<WeightedBidirectionalDependencyGraphEdge<T>> getOutgoingDependencies() {
		return this.outgoingDependencies.values();
	}

	public Collection<WeightedBidirectionalDependencyGraphEdge<T>> getAssumedIncomingDependencies() {
		return this.assumedIncomingDependencies.values();
	}

	public Collection<WeightedBidirectionalDependencyGraphEdge<T>> getAssumedOutgoingDependencies() {
		return this.assumedOutgoingDependencies.values();
	}

	public void setAssumed() {
		this.assumed = true;
	}

	public boolean isAssumed() {
		return this.assumed;
	}

	public void addOutgoingDependency(final DependencyGraphNode<T> destination, final MessageTrace origin) {
		this.addOutgoingDependency(destination, false, origin);
	}

	public void addOutgoingDependency(final DependencyGraphNode<T> destination, final boolean assumed, final MessageTrace origin) {
		synchronized (this) {
			final Map<Integer, WeightedBidirectionalDependencyGraphEdge<T>> relevantDependencies = // NOPMD(UseConcurrentHashMap)
			assumed ? this.assumedOutgoingDependencies : this.outgoingDependencies; // NOCS (inline ?)

			WeightedBidirectionalDependencyGraphEdge<T> e = relevantDependencies.get(destination.getId());
			if (e == null) {
				e = new WeightedBidirectionalDependencyGraphEdge<T>(this, destination, origin);

				if (assumed) {
					e.setAssumed();
				}

				relevantDependencies.put(destination.getId(), e);
			}
			e.getTargetWeight().increase();
		}
	}

	public void addIncomingDependency(final DependencyGraphNode<T> source, final MessageTrace origin) {
		this.addIncomingDependency(source, false, origin);
	}

	public void addIncomingDependency(final DependencyGraphNode<T> source, final boolean assumed, final MessageTrace origin) {
		synchronized (this) {
			final Map<Integer, WeightedBidirectionalDependencyGraphEdge<T>> relevantDependencies = // NOPMD(UseConcurrentHashMap)
			assumed ? this.assumedIncomingDependencies : this.incomingDependencies; // NOCS (inline ?)

			WeightedBidirectionalDependencyGraphEdge<T> e = relevantDependencies.get(source.getId());
			if (e == null) {
				e = new WeightedBidirectionalDependencyGraphEdge<T>(this, source, origin);
				relevantDependencies.put(source.getId(), e);
			}
			e.getSourceWeight().increase();
		}
	}

	public final int getId() {
		return this.id;
	}

	public String getFormattedDecorations() {
		synchronized (this) {
			final StringBuilder builder = new StringBuilder();
			final Iterator<AbstractVertexDecoration> decorationsIter = this.getDecorations().iterator();

			while (decorationsIter.hasNext()) {
				final String currentDecorationText = decorationsIter.next().createFormattedOutput();

				if ((currentDecorationText == null) || (currentDecorationText.length() == 0)) {
					continue;
				}

				builder.append(currentDecorationText);

				if (decorationsIter.hasNext()) {
					builder.append("\\n");
				}
			}

			return builder.toString();
		}
	}

	@Override
	public Collection<WeightedBidirectionalDependencyGraphEdge<T>> getOutgoingEdges() {
		final Collection<WeightedBidirectionalDependencyGraphEdge<T>> edges = new ArrayList<WeightedBidirectionalDependencyGraphEdge<T>>();

		edges.addAll(this.getOutgoingDependencies());
		edges.addAll(this.getAssumedOutgoingDependencies());

		return edges;
	}
}
