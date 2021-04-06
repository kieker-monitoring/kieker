/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.graph.dependency;

import kieker.analysis.graph.IGraph;
import kieker.analysis.util.stage.trigger.Trigger;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.statistics.StatisticsModel;

import teetime.stage.basic.AbstractTransformation;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DependencyGraphCreatorStage extends AbstractTransformation<Trigger, IGraph> {

	private final DependencyGraphCreator graphCreator;

	public DependencyGraphCreatorStage(final ExecutionModel executionModel, final StatisticsModel statisticsModel,
			final IDependencyGraphBuilderFactory graphBuilderFactory) {
		this.graphCreator = new DependencyGraphCreator(executionModel, statisticsModel, graphBuilderFactory);
	}

	@Override
	protected void execute(final Trigger trigger) {
		final IGraph graph = this.graphCreator.create();
		this.outputPort.send(graph);
	}

}
