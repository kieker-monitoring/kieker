/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package kieker.analysis.behavior.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * One node of a BehaviorModel.
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class Node {
	@JsonIgnore
	private final Map<Node, Edge> outgoingEdges;
	@JsonIgnore
	private final Map<Node, Edge> ingoingEdges;

	private final String name;

	public Node(final String signiture) {
		this.outgoingEdges = new HashMap<>();
		this.ingoingEdges = new HashMap<>();
		this.name = signiture;
	}

	public Map<Node, Edge> getOutgoingEdges() {
		return this.outgoingEdges;
	}

	public String getName() {
		return this.name;
	}

	public Map<Node, Edge> getIngoingEdges() {
		return this.ingoingEdges;
	}

}
