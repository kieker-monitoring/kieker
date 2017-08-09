/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Generic superclass for all vertices in the visualization package.
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
public abstract class AbstractVertex<V extends AbstractVertex<V, E, O>, E extends AbstractEdge<V, E, O>, O> extends AbstractGraphElement<O> {

	private final Map<Class<? extends AbstractVertexDecoration>, AbstractVertexDecoration> decorations = new ConcurrentHashMap<Class<? extends AbstractVertexDecoration>, AbstractVertexDecoration>(); // NOPMD(UseConcurrentHashMap)//NOCS

	protected AbstractVertex(final O origin, final IOriginRetentionPolicy originPolicy) {
		super(origin, originPolicy);
	}

	/**
	 * Returns the outgoing edges of this vertex.
	 * 
	 * @return See above
	 */
	public abstract Collection<E> getOutgoingEdges();

	/**
	 * Returns the decoration of this vertex of the given type.
	 * 
	 * @param type
	 *            The type of the desired decoration
	 * @return The given decoration or {@code null} if no such type exists
	 */
	@SuppressWarnings("unchecked")
	public <DecorationT extends AbstractVertexDecoration> DecorationT getDecoration(final Class<DecorationT> type) { // NOCS (DecorationT istaed of T)
		return (DecorationT) this.decorations.get(type);
	}

	/**
	 * Adds a decoration to this vertex. Note that the given decoration may replace an existing
	 * decoration of the same type.
	 * 
	 * @param decoration
	 *            The decoration to add
	 */
	public void addDecoration(final AbstractVertexDecoration decoration) {
		this.decorations.put(decoration.getClass(), decoration);
	}

	/**
	 * Returns all decorations of this vertex.
	 * 
	 * @return See above
	 */
	public Collection<AbstractVertexDecoration> getDecorations() {
		return Collections.unmodifiableCollection(this.decorations.values());
	}

}
