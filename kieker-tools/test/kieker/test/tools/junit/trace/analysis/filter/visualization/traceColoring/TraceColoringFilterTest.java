/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.trace.analysis.filter.visualization.traceColoring;

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
import kieker.tools.trace.analysis.filter.visualization.graph.Color;
import kieker.tools.trace.analysis.filter.visualization.traceColoring.TraceColoringFilter;
import kieker.tools.trace.analysis.repository.TraceColorRepository;
import kieker.tools.trace.analysis.repository.TraceColorRepository.TraceColorRepositoryData;
import kieker.tools.trace.analysis.systemModel.AllocationComponent;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.tools.util.graph.DependencyGraphTestUtil;
import kieker.test.tools.util.graph.GraphReceiverPlugin;
import kieker.test.tools.util.graph.GraphTestSetup;

/**
 * Test suite for the graph coloring filter.
 * 
 * @author Holger Knoche, Nils Christian Ehmke
 * 
 * @since 1.6
 */
public class TraceColoringFilterTest extends AbstractKiekerTest {

	private static final long TRACE_ID_1 = 1;
	private static final long TRACE_ID_2 = 2;
	private static final long TRACE_ID_3 = 3;

	private static final String SESSION_ID = "1234";
	private static final String HOSTNAME = "test";

	private static final String OPERATION_SIGNATURE_1 = "A.op1()";
	private static final String OPERATION_SIGNATURE_2 = "B.op2()";
	private static final String OPERATION_SIGNATURE_3 = "C.op3()";

	private static final String EXPECTED_ALLOCATION_COMPONENT_NAME_1 = "@1";
	private static final String EXPECTED_ALLOCATION_COMPONENT_NAME_2 = "@2";
	private static final String EXPECTED_ALLOCATION_COMPONENT_NAME_3 = "@3";

	private static final Color HIGHLIGHT_COLOR = Color.BLUE;
	private static final Color HIGHLIGHT_COLOR_2 = Color.GREEN;
	private static final Color DEFAULT_COLOR = Color.BLACK;
	private static final Color COLLISION_COLOR = Color.GRAY;

	private static GraphTestSetup testSetup;

	/**
	 * Default constructor.
	 */
	public TraceColoringFilterTest() {
		// default constructor
	}

	/**
	 * Prepares the setup for the test.
	 * 
	 * @throws AnalysisConfigurationException
	 *             If the setup of the filters failed.
	 */
	@BeforeClass
	public static void prepareSetup() throws AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();

		final ComponentDependencyGraphAllocationFilter filter = new ComponentDependencyGraphAllocationFilter(new Configuration(), analysisController);
		final String inputPortName = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES;
		final String repositoryPortName = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL;

		@SuppressWarnings("rawtypes")
		final TraceColoringFilter<?, ?> traceColoringFilter = new TraceColoringFilter(new Configuration(), analysisController);
		final TraceColorRepository traceColorRepository = TraceColoringFilterTest.prepareTraceColorRepository(analysisController);

		testSetup = DependencyGraphTestUtil.prepareEnvironmentForGraphFilterTest(analysisController, filter, inputPortName, repositoryPortName,
				TraceColoringFilterTest.createExecutionRecords(), traceColoringFilter);

		analysisController.connect(traceColoringFilter, TraceColoringFilter.COLOR_REPOSITORY_PORT_NAME, traceColorRepository);
	}

	private static TraceColorRepository prepareTraceColorRepository(final AnalysisController analysisController) {
		final ConcurrentMap<Long, Color> colorMap = new ConcurrentHashMap<Long, Color>();
		colorMap.put(TRACE_ID_1, HIGHLIGHT_COLOR);
		colorMap.put(TRACE_ID_2, HIGHLIGHT_COLOR_2);

		final TraceColorRepositoryData repositoryData = new TraceColorRepositoryData(colorMap, DEFAULT_COLOR, COLLISION_COLOR);
		return new TraceColorRepository(new Configuration(), repositoryData, analysisController);
	}

	private static OperationExecutionRecord createExecutionRecord(final String signature, final long traceId, final int tIn, final int tOut, final int eoi,
			final int ess) {
		return new OperationExecutionRecord(signature, SESSION_ID, traceId, tIn, tOut, HOSTNAME, eoi, ess);
	}

	private static List<OperationExecutionRecord> createExecutionRecords() {
		final List<OperationExecutionRecord> records = new ArrayList<OperationExecutionRecord>();
		int time = 0;

		int eoi = 0;
		long traceId = TRACE_ID_1;
		records.add(TraceColoringFilterTest.createExecutionRecord(OPERATION_SIGNATURE_1, traceId, ++time, ++time, eoi++, 0));
		records.add(TraceColoringFilterTest.createExecutionRecord(OPERATION_SIGNATURE_2, traceId, ++time, ++time, eoi++, 1));

		eoi = 0;
		traceId = TRACE_ID_2;
		records.add(TraceColoringFilterTest.createExecutionRecord(OPERATION_SIGNATURE_2, traceId, ++time, ++time, eoi++, 0));

		eoi = 0;
		traceId = TRACE_ID_3;
		records.add(TraceColoringFilterTest.createExecutionRecord(OPERATION_SIGNATURE_3, traceId, ++time, ++time, eoi++, 0));

		return records;
	}

	/**
	 * This method tests whether the trace coloring works or not. It uses the nodes resulting from the test setup and checks them against the expected colors.
	 * 
	 * @throws AnalysisConfigurationException
	 *             If the assembled test setup is somehow invalid.
	 */
	@Test
	public void testTraceColoring() throws AnalysisConfigurationException {
		testSetup.run();
		final GraphReceiverPlugin graphReceiver = testSetup.getResultCollectionPlugin();

		// Check number of produced graphs
		Assert.assertEquals(1, graphReceiver.getNumberOfReceivedGraphs());

		// Prepare the produced graph
		final ComponentAllocationDependencyGraph graph = graphReceiver.<ComponentAllocationDependencyGraph>getFirstGraph(); // NOCS (generic)
		final ConcurrentMap<String, DependencyGraphNode<AllocationComponent>> nodeMap = DependencyGraphTestUtil.createNodeLookupTable(graph);

		final DependencyGraphNode<AllocationComponent> component1Node = nodeMap.get(EXPECTED_ALLOCATION_COMPONENT_NAME_1);
		final DependencyGraphNode<AllocationComponent> component2Node = nodeMap.get(EXPECTED_ALLOCATION_COMPONENT_NAME_2);
		final DependencyGraphNode<AllocationComponent> component3Node = nodeMap.get(EXPECTED_ALLOCATION_COMPONENT_NAME_3);

		// Check the node colors
		Assert.assertEquals(HIGHLIGHT_COLOR, component1Node.getColor());
		Assert.assertEquals(COLLISION_COLOR, component2Node.getColor());
		Assert.assertEquals(DEFAULT_COLOR, component3Node.getColor());
	}
}
