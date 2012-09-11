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

package kieker.tools.traceAnalysis.filter.visualization.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Superclass for graph elements, i.e. edges and vertices, in the visualization package.
 * 
 * @author Holger Knoche
 * 
 * @param <O>
 *            The type of the objects which caused the creation of this graph element
 * 
 */

public class GraphElement<O> {

	private Color color;

	private final Set<O> origins = new HashSet<O>();

	protected GraphElement(final O origin) { // NOPMD (unsued paramter)
		// Prevent external instantiation
	}

	/**
	 * Returns this graph element's color.
	 * 
	 * @return See above
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * Sets this graph element's color.
	 * 
	 * @param color
	 *            The color to set
	 */
	public void setColor(final Color color) {
		this.color = color;
	}

	/**
	 * Returns the objects which caused the creation of this element.
	 * 
	 * @return See above
	 */
	public Set<O> getOrigins() {
		return Collections.unmodifiableSet(this.origins);
	}

	/**
	 * Adds a new origin object to this element.
	 * 
	 * @param origin
	 *            The origin object
	 */
	public void addOrigin(final O origin) {
		// TODO: Deactivated due to memory issues: http://kieker.uni-kiel.de/trac/ticket/583
		// this.origins.add(origin);
	}

}
