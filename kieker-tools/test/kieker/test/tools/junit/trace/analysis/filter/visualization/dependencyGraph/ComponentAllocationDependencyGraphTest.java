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

package kieker.test.tools.junit.trace.analysis.filter.visualization.dependencyGraph;

import java.util.ArrayList;
import java.util.List;
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
import kieker.tools.trace.analysis.systemModel.AllocationComponent;
import kieker.tools.trace.analysis.systemModel.ComponentType;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.tools.util.bookstore.graph.DependencyGraphTestUtil;
import kieker.test.tools.util.bookstore.graph.GraphReceiverPlugin;
import kieker.test.tools.util.bookstore.graph.GraphTestSetup;

/**
 * Test suite for the creation of component allocation dependency graphs ({@link ComponentDependencyGraphAllocationFilter}).
 *
 * @author Holger Knoche, Nils Christian Ehmke
 *
 * @since 1.6
 */
public class ComponentAllocationDependencyGraphTest extends AbstractKiekerTest {

	private static final long TRACE_ID = 1;
	private static final String SESSION_ID = "1234";
	private static final String HOSTNAME = "test";

	private static final String TYPE_NAME_1 = "A";
	private static final String TYPE_NAME_2 = "B";

	private static final String OPERATION_NAME_1 = "op1";
	private static final String OPERATION_NAME_2 = "op2";

	private static final String PARAMETERS = "()";

	private static final String OPERATION_SIGNATURE_1 = TYPE_NAME_1 + "." + OPERATION_NAME_1 + PARAMETERS;
	private static final String OPERATION_SIGNATURE_2 = TYPE_NAME_2 + "." + OPERATION_NAME_2 + PARAMETERS;

	private static final String EXPECTED_ALLOCATION_COMPONENT_NAME_1 = "@1";
	private static final String EXPECTED_ALLOCATION_COMPONENT_NAME_2 = "@2";

	private static GraphTestSetup testSetup;

	public ComponentAllocationDependencyGraphTest() {
		// default empty constructor
	}

	@BeforeClass
	public static void prepareSetup() throws AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();

		final ComponentDependencyGraphAllocationFilter filter = new ComponentDependencyGraphAllocationFilter(new Configuration(), analysisController);
		final String inputPortName = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES;
		final String repositoryPortName = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL;

		testSetup = DependencyGraphTestUtil.prepareEnvironmentForProducerTest(analysisController, filter, inputPortName, repositoryPortName,
				ComponentAllocationDependencyGraphTest.createExecutionRecords());
	}

	private static OperationExecutionRecord createExecutionRecord(final String signature, final int tin, final int tout, final int eoi, final int ess) {
		return new OperationExecutionRecord(signature, SESSION_ID, TRACE_ID, tin, tout, HOSTNAME, eoi, ess);
	}

	private static List<OperationExecutionRecord> createExecutionRecords() {
		final List<OperationExecutionRecord> records = new ArrayList<>();
		int eoi = 0;

		records.add(ComponentAllocationDependencyGraphTest.createExecutionRecord(OPERATION_SIGNATURE_1, 1, 2, eoi++, 0));
		records.add(ComponentAllocationDependencyGraphTest.createExecutionRecord(OPERATION_SIGNATURE_2, 2, 3, eoi++, 1));
		records.add(ComponentAllocationDependencyGraphTest.createExecutionRecord(OPERATION_SIGNATURE_2, 3, 4, eoi++, 0));

		return records;
	}

	@Test
	public void testGraphCreation() throws AnalysisConfigurationException {
		testSetup.run();
		final GraphReceiverPlugin graphReceiver = testSetup.getResultCollectionPlugin();

		// Check number of produced graphs
		Assert.assertEquals(1, graphReceiver.getNumberOfReceivedGraphs());

		// Inspect the graph itself
		final ComponentAllocationDependencyGraph graph = graphReceiver.<ComponentAllocationDependencyGraph> getFirstGraph(); // NOCS (generic)
		final ConcurrentMap<String, DependencyGraphNode<AllocationComponent>> nodeMap = DependencyGraphTestUtil.createNodeLookupTable(graph);

		// Obtain the expected allocation components
		final AllocationComponent allocationComponent1 = nodeMap.get(EXPECTED_ALLOCATION_COMPONENT_NAME_1).getEntity();
		final AllocationComponent allocationComponent2 = nodeMap.get(EXPECTED_ALLOCATION_COMPONENT_NAME_2).getEntity();
		Assert.assertNotNull(allocationComponent1);
		Assert.assertNotNull(allocationComponent2);

		// Inspect the attached types
		final ComponentType type1 = allocationComponent1.getAssemblyComponent().getType();
		final ComponentType type2 = allocationComponent2.getAssemblyComponent().getType();

		Assert.assertEquals(TYPE_NAME_1, type1.getTypeName());
		Assert.assertEquals(TYPE_NAME_2, type2.getTypeName());

		// Inspect the edges
		final DependencyGraphNode<AllocationComponent> rootNode = nodeMap.get(SystemModelRepository.ROOT_NODE_LABEL);
		final DependencyGraphNode<AllocationComponent> type1Node = nodeMap.get(EXPECTED_ALLOCATION_COMPONENT_NAME_1);
		final DependencyGraphNode<AllocationComponent> type2Node = nodeMap.get(EXPECTED_ALLOCATION_COMPONENT_NAME_2);

		// Check edges leaving the root
		Assert.assertEquals(2, rootNode.getOutgoingEdges().size());
		Assert.assertTrue(DependencyGraphTestUtil.dependencyEdgeExists(rootNode, 1, type1Node));
		Assert.assertTrue(DependencyGraphTestUtil.dependencyEdgeExists(rootNode, 1, type2Node));

		// Check edges leaving node 1
		Assert.assertEquals(1, type1Node.getOutgoingEdges().size());
		Assert.assertTrue(DependencyGraphTestUtil.dependencyEdgeExists(type1Node, 1, type2Node));

		// Check edges leaving node 2
		Assert.assertEquals(0, type2Node.getOutgoingEdges().size());
	}
}
