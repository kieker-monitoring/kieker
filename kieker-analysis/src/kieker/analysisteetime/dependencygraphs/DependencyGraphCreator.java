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

package kieker.analysisteetime.dependencygraphs;

import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.statistics.StatisticsModel;
import kieker.analysisteetime.util.graph.IGraph;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DependencyGraphCreator {

	private final ExecutionModel executionModel;
	private final StatisticsModel statisticsModel;
	private final IDependencyGraphBuilderFactory graphBuilderFactory;

	public DependencyGraphCreator(final ExecutionModel executionModel, final StatisticsModel statisticsModel,
			final IDependencyGraphBuilderFactory graphBuilderFactory) {
		this.executionModel = executionModel;
		this.statisticsModel = statisticsModel;
		this.graphBuilderFactory = graphBuilderFactory;
	}

	public IGraph create() {
		return this.graphBuilderFactory.createDependencyGraphBuilder(this.executionModel, this.statisticsModel).build();
	}

}
