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

package kieker.analysis.architecture.dependency;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;

import teetime.stage.basic.AbstractTransformation;

/**
 * @author Sören Henning
 *
 * @param <T>
 *            configuration type to be used with a sepecific graph builder.
 *
 * @since 1.14
 */
public class DependencyGraphCreatorStage<T extends IDependencyGraphBuilderConfiguration> extends AbstractTransformation<ModelRepository, IGraph<INode, IEdge>> {

	private final IDependencyGraphBuilder graphBuilder;

	public DependencyGraphCreatorStage(final T configuration, final IDependencyGraphBuilderFactory<T> graphBuilderFactory) {
		this.graphBuilder = graphBuilderFactory.createDependencyGraphBuilder(configuration);
	}

	@Override
	protected void execute(final ModelRepository repository) {
		final IGraph<INode, IEdge> graph = this.graphBuilder.build(repository);
		this.outputPort.send(graph);
	}

}
