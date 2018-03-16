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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysisteetime.model.analysismodel.execution.ExecutionFactory;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.statistics.StatisticsModel;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class AssemblyLevelComponentDependencyGraphBuilderFactoryTest {

	private AssemblyLevelComponentDependencyGraphBuilderFactory factory;
	private final ExecutionModel executionModel = ExecutionFactory.eINSTANCE.createExecutionModel();
	private final StatisticsModel statisticsModel = new StatisticsModel();

	public AssemblyLevelComponentDependencyGraphBuilderFactoryTest() {
		super();
	}

	@Before
	public void setUp() throws Exception {
		this.factory = new AssemblyLevelComponentDependencyGraphBuilderFactory();
	}

	@After
	public void tearDown() throws Exception {
		this.factory = null; // NOPMD (resetting to null intended)
	}

	/**
	 * Test method for {@link kieker.analysisteetime.dependencygraphs.AssemblyLevelComponentDependencyGraphBuilderFactory#createDependencyGraphBuilder()}.
	 */
	@Test
	public void testCreateDependencyGraphBuilder() {
		final IDependencyGraphBuilder graphBuilder = this.factory.createDependencyGraphBuilder(this.executionModel, this.statisticsModel);
		Assert.assertTrue(graphBuilder instanceof AssemblyLevelComponentDependencyGraphBuilder);
	}

}
