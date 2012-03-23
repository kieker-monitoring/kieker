/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.test.tools.junit.traceAnalysis.filter.flow;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.analysis.AnalysisController;
import kieker.common.configuration.Configuration;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.test.analysis.junit.plugin.SimpleSinkPlugin;
import kieker.test.tools.junit.traceAnalysis.filter.TestTraceReconstructionFilter;
import kieker.test.tools.junit.traceAnalysis.util.BookstoreEventRecordFactory;
import kieker.test.tools.junit.traceAnalysis.util.ExecutionFactory;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.flow.EventRecordTrace;
import kieker.tools.traceAnalysis.filter.flow.EventTrace2ExecutionAndMessageTraceFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import org.junit.Test;

/**
 * TODO: Finalize
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestEventTrace2ExecutionTraceFilter extends TestCase {
	private static final long TRACE_ID = 4563l;
	private static final String SESSION_ID = "y2zGAI0VX"; // Same Session ID for all traces

	private final SystemModelRepository systemEntityFactory = new SystemModelRepository(new Configuration());
	private final ExecutionFactory executionFactory = new ExecutionFactory(this.systemEntityFactory);

	/* Executions of a valid trace */
	private final Execution exec0_0__bookstore_searchBook; // NOCS
	private final Execution exec1_1__catalog_getBook; // NOCS
	private final Execution exec2_1__crm_getOrders; // NOCS
	private final Execution exec3_2__catalog_getBook; // NOCS

	/**
	 * Borrowed from {@link TestTraceReconstructionFilter}.
	 */
	public TestEventTrace2ExecutionTraceFilter() {
		/* Manually create Executions for a trace */
		this.exec0_0__bookstore_searchBook = this.executionFactory.genExecution("Bookstore", "bookstore", "searchBook",
				TestEventTrace2ExecutionTraceFilter.TRACE_ID,
				TestEventTrace2ExecutionTraceFilter.SESSION_ID,
				1 * (1000 * 1000), 10 * (1000 * 1000), 0, 0); // NOCS (MagicNumberCheck)

		this.exec1_1__catalog_getBook = this.executionFactory.genExecution("Catalog", "catalog", "getBook", TestEventTrace2ExecutionTraceFilter.TRACE_ID,
				TestEventTrace2ExecutionTraceFilter.SESSION_ID,
				2 * (1000 * 1000), 4 * (1000 * 1000), 1, 1); // NOCS (MagicNumberCheck)
		this.exec2_1__crm_getOrders = this.executionFactory.genExecution("CRM", "crm", "getOrders", TestEventTrace2ExecutionTraceFilter.TRACE_ID,
				TestEventTrace2ExecutionTraceFilter.SESSION_ID,
				5 * (1000 * 1000), 8 * (1000 * 1000), 2, 1); // NOCS (MagicNumberCheck)
		this.exec3_2__catalog_getBook = this.executionFactory.genExecution("Catalog", "catalog", "getBook", TestEventTrace2ExecutionTraceFilter.TRACE_ID,
				TestEventTrace2ExecutionTraceFilter.SESSION_ID,
				6 * (1000 * 1000), 7 * (1000 * 1000), 3, 2); // NOCS (MagicNumberCheck)
	}

	/**
	 * Generates an execution trace representation of the "well-known" bookstore
	 * trace.
	 * 
	 * Borrowed from {@link TestTraceReconstructionFilter}.
	 * 
	 * @return
	 * @throws InvalidTraceException
	 */
	private ExecutionTrace genValidBookstoreTrace() throws InvalidTraceException {
		/*
		 * Create an Execution Trace and add Executions in
		 * arbitrary order
		 */
		final ExecutionTrace executionTrace = new ExecutionTrace(TestEventTrace2ExecutionTraceFilter.TRACE_ID, TestEventTrace2ExecutionTraceFilter.SESSION_ID);

		executionTrace.add(this.exec3_2__catalog_getBook);
		executionTrace.add(this.exec2_1__crm_getOrders);
		executionTrace.add(this.exec0_0__bookstore_searchBook);
		executionTrace.add(this.exec1_1__catalog_getBook);

		executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);

		return executionTrace;
	}

	@Test
	public void testValidTraceWithBeforeAndAfterOperationEvents() throws InvalidTraceException {
		/*
		 * Create an EventRecordTrace, containing only Before- and AfterOperation events.
		 */
		final List<AbstractTraceEvent> eventList =
				BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(this.exec0_0__bookstore_searchBook.getTin(),
						TestEventTrace2ExecutionTraceFilter.TRACE_ID);
		final EventRecordTrace eventRecordTrace = new EventRecordTrace(TestEventTrace2ExecutionTraceFilter.TRACE_ID, TestEventTrace2ExecutionTraceFilter.SESSION_ID);
		for (final AbstractTraceEvent ev : eventList) {
			eventRecordTrace.add(ev);
		}

		/*
		 * Create the transformation filter
		 */
		final Configuration filterConfiguration = new Configuration();
		final EventTrace2ExecutionAndMessageTraceFilter filter = new EventTrace2ExecutionAndMessageTraceFilter(filterConfiguration);

		/*
		 * Create and connect a sink plugin which collects the transformed
		 * ExecutionTraces
		 */
		final SimpleSinkPlugin executionTraceSinkPlugin = new SimpleSinkPlugin(new Configuration());
		final AnalysisController controller = new AnalysisController();

		controller.registerFilter(filter);
		controller.registerFilter(executionTraceSinkPlugin);
		controller.connect(filter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, this.systemEntityFactory);
		controller.connect(filter, EventTrace2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, executionTraceSinkPlugin,
				SimpleSinkPlugin.INPUT_PORT_NAME);

		filter.inputEventTrace(eventRecordTrace);

		Assert.assertEquals("Unexpected number of received execution traces", 1, executionTraceSinkPlugin.getList().size());

		// final ExecutionTrace resultingExecutionTrace = (ExecutionTrace) executionTraceSinkPlugin.getList().get(0);
		// final SortedSet<Execution> traceAsSortedSet = resultingExecutionTrace.getTraceAsSortedExecutionSet();
		// final Execution[] resultingExecutionsArr = traceAsSortedSet.toArray(new Execution[traceAsSortedSet.size()]);

		/*
		 * TODO: Check validity of transformed execution trace
		 */
	}
}
