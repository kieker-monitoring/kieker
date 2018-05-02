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

package kieker.test.tools.junit.traceAnalysis.filter.traceFilter;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.tools.util.ExecutionFactory;

/**
 * @author Andre van Hoorn
 */
public class TestTraceEquivalenceFilterAssemblyEquivalence extends AbstractKiekerTest { // NOCS

	private static final String SESSION_ID = "j8tVhvDPYL";

	private final SystemModelRepository systemEntityFactory = new SystemModelRepository(new Configuration(), new AnalysisController());
	private final ExecutionFactory executionFactory = new ExecutionFactory(this.systemEntityFactory);

	@Test
	public void testEqualTrace() throws InvalidTraceException {
		final ExecutionTrace trace0;
		final ExecutionTrace trace1;

		trace0 = this.genValidBookstoreTrace(45653L, TestTraceEquivalenceFilterAssemblyEquivalence.SESSION_ID, 17);
		trace1 = this.genValidBookstoreTrace(45653L, TestTraceEquivalenceFilterAssemblyEquivalence.SESSION_ID, 17);
		Assert.assertEquals(trace0, trace1);

		//
		// final Configuration configuration = new Configuration();
		// configuration.setProperty(AbstractTraceAnalysisPlugin.CONFIG_NAME, "TraceEquivalenceClassFilter");
		//
		// final Map<String, AbstractRepository> repositoryMap = new HashMap<String, AbstractRepository>();
		// repositoryMap.put(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, this.systemEntityFactory);
		// final TraceEquivalenceClassFilter filter = new TraceEquivalenceClassFilter(configuration, repositoryMap,
		// TraceEquivalenceClassFilter.TraceEquivalenceClassModes.ASSEMBLY);
		//
		// Register a handler for equivalence class representatives.
		//
		//
		// filter.getExecutionTraceOutputPort().subscribe(new AbstractInputPort("Execution traces", null) {
		//
		//
		// public void newEvent(final Object event) {
		// throw new UnsupportedOperationException("Not supported yet.");
		// }
		// });
	}

	private ExecutionTrace genValidBookstoreTrace(final long traceId, final String sessionId, final long offset) throws InvalidTraceException {
		// Executions of a valid trace
		final Execution exec0_0__bookstore_searchBook; // NOCS
		final Execution exec1_1__catalog_getBook; // NOCS
		final Execution exec2_1__crm_getOrders; // NOCS
		final Execution exec3_2__catalog_getBook; // NOCS

		// Manually create Executions for a trace
		exec0_0__bookstore_searchBook = this.executionFactory.genExecution("Bookstore", "bookstore", "searchBook", traceId,
				sessionId,
				(1 * (1000 * 1000)) + offset, // tin //NOCS (MagicNumberCheck)
				(10 * (1000 * 1000)) + offset, // tout
				0, 0); // eoi, ess

		exec1_1__catalog_getBook = this.executionFactory.genExecution("Catalog", "catalog", "getBook", traceId,
				sessionId,
				(2 * (1000 * 1000)) + offset,
				(4 * (1000 * 1000)) + offset,
				1, 1);
		exec2_1__crm_getOrders = this.executionFactory.genExecution("CRM", "crm", "getOrders", traceId,
				sessionId,
				(5 * (1000 * 1000)) + offset,
				(8 * (1000 * 1000)) + offset,
				2, 1);
		exec3_2__catalog_getBook = this.executionFactory.genExecution("Catalog", "catalog", "getBook", traceId,
				sessionId,
				(6 * (1000 * 1000)) + offset,
				(7 * (1000 * 1000)) + offset,
				3, 2);

		// Create an Execution Trace and add Executions in arbitrary order
		final ExecutionTrace executionTrace = new ExecutionTrace(traceId, TestTraceEquivalenceFilterAssemblyEquivalence.SESSION_ID);

		executionTrace.add(exec3_2__catalog_getBook);
		executionTrace.add(exec2_1__crm_getOrders);
		executionTrace.add(exec0_0__bookstore_searchBook);
		executionTrace.add(exec1_1__catalog_getBook);

		executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);

		return executionTrace;
	}
}
