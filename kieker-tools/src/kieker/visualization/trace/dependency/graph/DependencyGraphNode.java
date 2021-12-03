/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.visualization.trace.dependency.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import kieker.model.repository.AbstractRepository;
import kieker.model.system.model.ISystemModelElement;
import kieker.model.system.model.TraceInformation;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractPayloadedVertex;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractVertexDecoration;
import kieker.tools.trace.analysis.filter.visualization.graph.IOriginRetentionPolicy;

/**
 * This class represents a single node within a dependency graph.
 *
 * @param <T>
 *            The type of the entity to be stored in this node.
 *
 * @author Andre van Hoorn
 *
 * @since 1.1
 */
public class DependencyGraphNode<T extends ISystemModelElement> extends
		AbstractPayloadedVertex<DependencyGraphNode<T>, WeightedBidirectionalDependencyGraphEdge<T>, TraceInformation, T> {

	public static final int ROOT_NODE_ID = AbstractRepository.ROOT_ELEMENT_ID;

	private final int id;
	private final Map<Integer, WeightedBidirectionalDependencyGraphEdge<T>> incomingDependencies = new ConcurrentHashMap<>(); // NOPMD(UseConcurrentHashMap)//NOCS
	private final Map<Integer, WeightedBidirectionalDependencyGraphEdge<T>> outgoingDependencies = new ConcurrentHashMap<>(); // NOPMD(UseConcurrentHashMap)//NOCS

	private final Map<Integer, WeightedBidirectionalDependencyGraphEdge<T>> assumedIncomingDependencies = new ConcurrentHashMap<>(); // NOPMD(UseConcurrentHashMap)//NOCS
	private final Map<Integer, WeightedBidirectionalDependencyGraphEdge<T>> assumedOutgoingDependencies = new ConcurrentHashMap<>(); // NOPMD(UseConcurrentHashMap)//NOCS

	private volatile boolean assumed; // false

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param id
	 *            The ID of this node.
	 * @param entity
	 *            The entity which will be the payload of this node.
	 * @param origin
	 *            The trace information which will be additional meta data for this node.
	 * @param originPolicy
	 *            The origin policy.
	 */
	public DependencyGraphNode(final int id, final T entity, final TraceInformation origin, final IOriginRetentionPolicy originPolicy) {
		super(origin, originPolicy, entity);
		this.id = id;
	}

	public final T getEntity() {
		return this.getPayload();
	}

	@Override
	public String getIdentifier() {
		return this.getEntity().getIdentifier();
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

	/**
	 * Sets the assumed flag of this node to {@code true}.
	 */
	public void setAssumed() {
		this.assumed = true;
	}

	public boolean isAssumed() {
		return this.assumed;
	}

	/**
	 * Adds an outgoing dependency to this node. The dependency will be marked as not assumed.
	 *
	 * @param destination
	 *            The destination of the dependency.
	 * @param origin
	 *            The origin of the destination.
	 * @param originPolicy
	 *            The origin policy of the destination.
	 */
	public void addOutgoingDependency(final DependencyGraphNode<T> destination, final TraceInformation origin, final IOriginRetentionPolicy originPolicy) {
		this.addOutgoingDependency(destination, false, origin, originPolicy);
	}

	/**
	 * Adds an outgoing dependency to this node.
	 *
	 * @param destination
	 *            The destination of the dependency.
	 * @param isAssumed
	 *            Whether the dependency is assumed or not.
	 * @param origin
	 *            The origin of the destination.
	 * @param originPolicy
	 *            The origin policy of the destination.
	 */
	public void addOutgoingDependency(final DependencyGraphNode<T> destination, final boolean isAssumed, final TraceInformation origin,
			final IOriginRetentionPolicy originPolicy) {
		synchronized (this) {
			final Map<Integer, WeightedBidirectionalDependencyGraphEdge<T>> relevantDependencies = // NOPMD(UseConcurrentHashMap)
					isAssumed ? this.assumedOutgoingDependencies : this.outgoingDependencies; // NOCS (inline ?)

			WeightedBidirectionalDependencyGraphEdge<T> e = relevantDependencies.get(destination.getId());
			if (e == null) {
				e = new WeightedBidirectionalDependencyGraphEdge<>(this, destination, origin, originPolicy);

				if (isAssumed) {
					e.setAssumed();
				}

				relevantDependencies.put(destination.getId(), e);
			} else {
				originPolicy.handleOrigin(e, origin);
			}
			e.getTargetWeight().incrementAndGet();
		}
	}

	/**
	 * Adds an incoming dependency to this node. The dependency will be marked as not assumed.
	 *
	 * @param source
	 *            The source of the dependency.
	 * @param origin
	 *            The origin of the destination.
	 * @param originPolicy
	 *            The origin policy of the destination.
	 */
	public void addIncomingDependency(final DependencyGraphNode<T> source, final TraceInformation origin, final IOriginRetentionPolicy originPolicy) {
		this.addIncomingDependency(source, false, origin, originPolicy);
	}

	/**
	 * Adds an incoming dependency to this node.
	 *
	 * @param source
	 *            The source of the dependency.
	 * @param isAssumed
	 *            Whether the dependency is assumed or not.
	 * @param origin
	 *            The origin of the destination.
	 * @param originPolicy
	 *            The origin policy of the destination.
	 */
	public void addIncomingDependency(final DependencyGraphNode<T> source, final boolean isAssumed, final TraceInformation origin,
			final IOriginRetentionPolicy originPolicy) {
		synchronized (this) {
			final Map<Integer, WeightedBidirectionalDependencyGraphEdge<T>> relevantDependencies = // NOPMD(UseConcurrentHashMap)
					isAssumed ? this.assumedIncomingDependencies : this.incomingDependencies; // NOCS (inline ?)

			WeightedBidirectionalDependencyGraphEdge<T> e = relevantDependencies.get(source.getId());
			if (e == null) {
				e = new WeightedBidirectionalDependencyGraphEdge<>(this, source, origin, originPolicy);
				relevantDependencies.put(source.getId(), e);
			} else {
				originPolicy.handleOrigin(e, origin);
			}
			e.getSourceWeight().incrementAndGet();
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
		final Collection<WeightedBidirectionalDependencyGraphEdge<T>> edges = new ArrayList<>();

		edges.addAll(this.getOutgoingDependencies());
		edges.addAll(this.getAssumedOutgoingDependencies());

		return edges;
	}
}
