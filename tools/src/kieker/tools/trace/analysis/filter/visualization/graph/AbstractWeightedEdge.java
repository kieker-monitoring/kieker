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
package kieker.tools.trace.analysis.filter.visualization.graph;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract superclass for weighted edges in the visualization package. This class provides weights for the
 * edge itself and both source and target ends.
 *
 * @author Holger Knoche
 *
 * @param <V>
 *            The type of the graph's vertices
 * @param <E>
 *            The type of the graph's edges
 * @param <O>
 *            The type of object from which the graph's elements originate
 *
 * @since 1.6
 */
public abstract class AbstractWeightedEdge<V extends AbstractVertex<V, E, O>, E extends AbstractEdge<V, E, O>, O> extends AbstractEdge<V, E, O> {

	private final AtomicInteger sourceWeight = new AtomicInteger();
	private final AtomicInteger targetWeight = new AtomicInteger();
	private final AtomicInteger weight = new AtomicInteger();

	/**
	 * Creates a new weighted edge between the given vertices.
	 *
	 * @param source
	 *            The source vertex of the edge
	 * @param target
	 *            The target vertex of the edge
	 * @param origin
	 *            The edge's origin object
	 * @param originPolicy
	 *            The origin policy to use for storing the origin object
	 */
	public AbstractWeightedEdge(final V source, final V target, final O origin, final IOriginRetentionPolicy originPolicy) {
		super(source, target, origin, originPolicy);
	}

	/**
	 * Return this edge's source weight.
	 *
	 * @return See above
	 */
	public AtomicInteger getSourceWeight() {
		return this.sourceWeight;
	}

	/**
	 * Return this edge's target weight.
	 *
	 * @return See above
	 */
	public AtomicInteger getTargetWeight() {
		return this.targetWeight;
	}

	/**
	 * Returns this edge's weight.
	 *
	 * @return See above
	 */
	public AtomicInteger getWeight() {
		return this.weight;
	}

}
