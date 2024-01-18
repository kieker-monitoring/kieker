/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.architecture.trace.graph;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;
import kieker.model.analysismodel.trace.Trace;

import teetime.stage.basic.AbstractTransformation;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TraceToGraphTransformerStage extends AbstractTransformation<Trace, IGraph<INode, IEdge>> {

	private final TraceToGraphTransformer transformer = new TraceToGraphTransformer();

	public TraceToGraphTransformerStage() {
		super();
	}

	@Override
	protected void execute(final Trace trace) {
		final IGraph<INode, IEdge> graph = this.transformer.transform(trace);
		this.getOutputPort().send(graph);
	}

}
