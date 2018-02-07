/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.util.graph.impl;

import kieker.analysisteetime.util.graph.GraphElement;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
abstract class GraphElementImpl extends ElementImpl implements GraphElement { // NOPMD NOCS (GraphElement is in this context the abstraction of Vertex and Edge))

	protected final Object id;
	protected final GraphImpl graph;

	protected GraphElementImpl(final Object id, final GraphImpl graph) {
		super();
		this.graph = graph;
		this.id = id;
	}

	@Override
	public abstract void remove();

	@Override
	public Object getId() {
		return this.id;
	}

}
