/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.dependencygraphs;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.graph.dependency.IDependencyGraphBuilder;
import kieker.analysis.graph.dependency.IDependencyGraphBuilderConfiguration;
import kieker.analysis.graph.dependency.TypeLevelOperationDependencyGraphBuilder;
import kieker.analysis.graph.dependency.TypeLevelOperationDependencyGraphBuilderFactory;
import kieker.analysis.stage.model.ModelRepository;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.statistics.StatisticsFactory;
import kieker.model.analysismodel.statistics.StatisticsModel;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TypeLevelOperationDependencyGraphBuilderFactoryTest {

	private TypeLevelOperationDependencyGraphBuilderFactory factory;
	private final ExecutionModel executionModel = ExecutionFactory.eINSTANCE.createExecutionModel();
	private final StatisticsModel statisticsModel = StatisticsFactory.eINSTANCE.createStatisticsModel();

	public TypeLevelOperationDependencyGraphBuilderFactoryTest() {
		super();
	}

	@Before
	public void setUp() throws Exception {
		this.factory = new TypeLevelOperationDependencyGraphBuilderFactory();
	}

	@After
	public void tearDown() throws Exception {
		this.factory = null; // NOPMD (resetting to null intended)
	}

	/**
	 * Test method for {@link kieker.analysis.graph.dependency.AssemblyLevelComponentDependencyGraphBuilderFactory#createDependencyGraphBuilder()}.
	 */
	@Test
	public void testCreateDependencyGraphBuilder() {
		final ModelRepository repository = new ModelRepository("test");
		repository.register(ExecutionModel.class, this.executionModel);
		repository.register(StatisticsModel.class, this.statisticsModel);

		final IDependencyGraphBuilder graphBuilder = this.factory.createDependencyGraphBuilder(new IDependencyGraphBuilderConfiguration() {
		});
		Assert.assertTrue(graphBuilder instanceof TypeLevelOperationDependencyGraphBuilder);
	}

}
