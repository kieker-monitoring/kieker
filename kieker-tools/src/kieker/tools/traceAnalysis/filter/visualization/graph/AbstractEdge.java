/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.visualization.graph;

/**
 * Generic superclass for all graph edges in the visualization package.
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
public abstract class AbstractEdge<V extends AbstractVertex<V, E, O>, E extends AbstractEdge<V, E, O>, O> extends AbstractGraphElement<O> {

	private final V source;
	private final V target;

	/**
	 * Creates a new edge between the given vertices.
	 * 
	 * @param source
	 *            The source vertex of the edge
	 * @param target
	 *            The target vertex of the edge
	 * @param origin
	 *            The origin of the edge
	 * @param originPolicy
	 *            The origin policy to use
	 */
	public AbstractEdge(final V source, final V target, final O origin, final IOriginRetentionPolicy originPolicy) {
		super(origin, originPolicy);
		this.source = source;
		this.target = target;
	}

	/**
	 * Returns the source of this edge.
	 * 
	 * @return See above
	 */
	public V getSource() {
		return this.source;
	}

	/**
	 * Returns the target of this edge.
	 * 
	 * @return See above
	 */
	public V getTarget() {
		return this.target;
	}

	@Override
	public String getIdentifier() { // NOPMD ( EmptyMethodInAbstractClassShouldBeAbstract)
		return null;
	}
}
