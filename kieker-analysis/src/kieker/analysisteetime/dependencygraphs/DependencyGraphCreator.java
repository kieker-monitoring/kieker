/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.dependencygraphs;

import java.util.Map;

import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.statistics.Statistics;
import kieker.analysisteetime.util.graph.Graph;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class DependencyGraphCreator {

	private final ExecutionModel executionModel;
	private final Map<Object, Statistics> statisticsModel;
	private final DependencyGraphBuilderFactory graphBuilderFactory;

	public DependencyGraphCreator(final ExecutionModel executionModel, final Map<Object, Statistics> statisticsModel,
			final DependencyGraphBuilderFactory graphBuilderFactory) {
		this.executionModel = executionModel;
		this.statisticsModel = statisticsModel;
		this.graphBuilderFactory = graphBuilderFactory;
	}

	public Graph create() {
		final DependencyGraphBuilder graphBuilder = this.graphBuilderFactory.createDependencyGraphBuilder(this.statisticsModel);
		final Graph graph = graphBuilder.build(this.executionModel);
		return graph;
	}

}
