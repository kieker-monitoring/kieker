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

package kieker.analysis.generic.sink.graph.blueprints;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;

import teetime.stage.basic.AbstractTransformation;

/**
 * @param <N>
 *            node type
 * @param <E>
 *            edge type
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class BlueprintsTransformerStage<N extends INode, E extends IEdge> extends AbstractTransformation<IGraph<N, E>, com.tinkerpop.blueprints.Graph> {

	public BlueprintsTransformerStage() {
		super();
	}

	@Override
	protected void execute(final IGraph<N, E> graph) {
		final BlueprintsTransformer<N, E> transformer = new BlueprintsTransformer<>(graph);
		this.getOutputPort().send(transformer.transform());
	}

}
