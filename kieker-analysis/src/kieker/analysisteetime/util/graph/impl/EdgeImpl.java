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

import kieker.analysisteetime.util.graph.Direction;
import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Vertex;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
class EdgeImpl extends GraphElementImpl implements Edge {

	private final Vertex outVertex;
	private final Vertex inVertex;

	protected EdgeImpl(final Object id, final Vertex outVertex, final Vertex inVertex, final GraphImpl graph) {
		super(id, graph);
		this.outVertex = outVertex;
		this.inVertex = inVertex;
	}

	@Override
	public void remove() {
		this.graph.removeEdge(this);
	}

	@Override
	public Vertex getVertex(final Direction direction) throws IllegalArgumentException {
		switch (direction) {
		case IN:
			return this.inVertex;
		case OUT:
			return this.outVertex;
		default:
			throw ExceptionFactory.bothIsNotSupported();
		}
	}

}
