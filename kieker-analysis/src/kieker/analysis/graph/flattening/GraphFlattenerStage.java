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

package kieker.analysis.graph.flattening;

import com.google.common.graph.MutableNetwork;

import kieker.analysis.graph.IEdge;
import kieker.analysis.graph.INode;

import teetime.stage.basic.AbstractFilter;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class GraphFlattenerStage extends AbstractFilter<MutableNetwork<INode, IEdge>> {

	private final IGraphFlattener flattener;

	public GraphFlattenerStage(final IGraphFlattener flattener) {
		super();
		this.flattener = flattener;
	}

	@Override
	protected void execute(final MutableNetwork<INode, IEdge> graph) {
		this.flattener.flatten(graph);
		this.getOutputPort().send(graph);
	}

}
