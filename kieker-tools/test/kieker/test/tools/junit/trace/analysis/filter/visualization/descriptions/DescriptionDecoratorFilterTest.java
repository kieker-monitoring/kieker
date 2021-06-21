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
package kieker.test.tools.junit.trace.analysis.filter.visualization.descriptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.trace.analysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.trace.analysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.ComponentAllocationDependencyGraph;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.ComponentDependencyGraphAllocationFilter;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.DependencyGraphNode;
import kieker.tools.trace.analysis.filter.visualization.descriptions.DescriptionDecoratorFilter;
import kieker.tools.trace.analysis.repository.DescriptionRepository;
import kieker.tools.trace.analysis.repository.DescriptionRepository.DescriptionRepositoryData;
import kieker.tools.trace.analysis.systemModel.AllocationComponent;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.tools.util.bookstore.graph.DependencyGraphTestUtil;
import kieker.test.tools.util.bookstore.graph.GraphReceiverPlugin;
import kieker.test.tools.util.bookstore.graph.GraphTestSetup;

/**
 * Test suite for the description decorator filter.
 *
 * @author Holger Knoche, Nils Christian Ehmke
 *
 * @since 1.6
 */
public class DescriptionDecoratorFilterTest extends AbstractKiekerTest {

	private static final long TRACE_ID = 1;

	private static final String SESSION_ID = "1234";
	private static final String HOSTNAME = "test";

	private static final String OPERATION_SIGNATURE_1 = "A.op1()";
	private static final String OPERATION_SIGNATURE_2 = "B.op2()";

	private static final String EXPECTED_ALLOCATION_COMPONENT_NAME_1 = "@1";
	private static final String EXPECTED_ALLOCATION_COMPONENT_NAME_2 = "@2";

	private static final String DESCRIPTION_1 = "Description 1";
	private static final String DESCRIPTION_2 = "Description 2";

	private static GraphTestSetup testSetup;

	/**
	 * Default constructor.
	 */
	public DescriptionDecoratorFilterTest() {
		// empty default constructor
	}

	/**
	 * Initializes the test setup.
	 *
	 * @throws AnalysisConfigurationException
	 *             If the preparation of the analysis failed.
	 */
	@BeforeClass
	public static void prepareSetup() throws AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();

		final ComponentDependencyGraphAllocationFilter filter = new ComponentDependencyGraphAllocationFilter(new Configuration(), analysisController);
		final String inputPortName = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES;
		final String repositoryPortName = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL;

		@SuppressWarnings("rawtypes")
		final DescriptionDecoratorFilter<?, ?, ?> descriptionDecoratorFilter = new DescriptionDecoratorFilter(new Configuration(), analysisController);
		final DescriptionRepository descriptionRepository = DescriptionDecoratorFilterTest.prepareDescriptionRepository(analysisController);

		testSetup = DependencyGraphTestUtil.prepareEnvironmentForGraphFilterTest(analysisController, filter, inputPortName, repositoryPortName,
				DescriptionDecoratorFilterTest.createExecutionRecords(), descriptionDecoratorFilter);

		analysisController.connect(descriptionDecoratorFilter, DescriptionDecoratorFilter.DESCRIPTION_REPOSITORY_PORT_NAME, descriptionRepository);
	}

	private static OperationExecutionRecord createExecutionRecord(final String signature, final int tIn, final int tOut, final int eoi, final int ess) {
		return new OperationExecutionRecord(signature, SESSION_ID, TRACE_ID, tIn, tOut, HOSTNAME, eoi, ess);
	}

	private static List<OperationExecutionRecord> createExecutionRecords() {
		final List<OperationExecutionRecord> records = new ArrayList<>();
		int eoi = -1;
		int time = 0;

		records.add(DescriptionDecoratorFilterTest.createExecutionRecord(OPERATION_SIGNATURE_1, ++time, ++time, ++eoi, 0));
		records.add(DescriptionDecoratorFilterTest.createExecutionRecord(OPERATION_SIGNATURE_2, ++time, ++time, ++eoi, 1));

		return records;
	}

	private static DescriptionRepository prepareDescriptionRepository(final AnalysisController analysisController) {
		final ConcurrentMap<String, String> descriptions = new ConcurrentHashMap<>();

		descriptions.put(EXPECTED_ALLOCATION_COMPONENT_NAME_1, DESCRIPTION_1);
		descriptions.put(EXPECTED_ALLOCATION_COMPONENT_NAME_2, DESCRIPTION_2);

		final DescriptionRepositoryData repositoryData = new DescriptionRepositoryData(descriptions);
		return new DescriptionRepository(new Configuration(), repositoryData, analysisController);
	}

	@Test
	public void testDescriptionDecorator() throws AnalysisConfigurationException {
		testSetup.run();
		final GraphReceiverPlugin graphReceiver = testSetup.getResultCollectionPlugin();

		// Check number of produced graphs
		Assert.assertEquals(1, graphReceiver.getNumberOfReceivedGraphs());

		// Prepare the produced graph
		final ComponentAllocationDependencyGraph graph = graphReceiver.<ComponentAllocationDependencyGraph> getFirstGraph(); // NOCS (generic)
		final ConcurrentMap<String, DependencyGraphNode<AllocationComponent>> nodeMap = DependencyGraphTestUtil.createNodeLookupTable(graph);

		final DependencyGraphNode<AllocationComponent> component1Node = nodeMap.get(EXPECTED_ALLOCATION_COMPONENT_NAME_1);
		final DependencyGraphNode<AllocationComponent> component2Node = nodeMap.get(EXPECTED_ALLOCATION_COMPONENT_NAME_2);

		// Check the decorations
		Assert.assertEquals(DESCRIPTION_1, component1Node.getDescription());
		Assert.assertEquals(DESCRIPTION_2, component2Node.getDescription());
	}

}
