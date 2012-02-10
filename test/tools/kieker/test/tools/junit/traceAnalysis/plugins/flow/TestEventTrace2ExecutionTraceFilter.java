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

package kieker.test.tools.junit.traceAnalysis.plugins.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.flow.TraceEvent;
import kieker.test.tools.junit.traceAnalysis.plugins.TestTraceReconstructionFilter;
import kieker.test.tools.junit.traceAnalysis.util.BookstoreEventRecordFactory;
import kieker.test.tools.junit.traceAnalysis.util.ExecutionFactory;
import kieker.test.tools.junit.traceAnalysis.util.SimpleSinkPlugin;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.plugins.flow.EventRecordTrace;
import kieker.tools.traceAnalysis.plugins.flow.EventTrace2ExecutionTraceFilter;
import kieker.tools.traceAnalysis.plugins.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import org.junit.Test;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestEventTrace2ExecutionTraceFilter extends TestCase {
	private static final Log LOG = LogFactory.getLog(TestEventTrace2ExecutionTraceFilter.class);
	private static final long TRACE_ID = 4563l;
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
				1 * (1000 * 1000), 10 * (1000 * 1000), 0, 0); // NOCS (MagicNumberCheck)

		this.exec1_1__catalog_getBook = this.executionFactory.genExecution("Catalog", "catalog", "getBook", TestEventTrace2ExecutionTraceFilter.TRACE_ID,
				2 * (1000 * 1000), 4 * (1000 * 1000), 1, 1); // NOCS (MagicNumberCheck)
		this.exec2_1__crm_getOrders = this.executionFactory.genExecution("CRM", "crm", "getOrders", TestEventTrace2ExecutionTraceFilter.TRACE_ID,
				5 * (1000 * 1000), 8 * (1000 * 1000), 2, 1); // NOCS (MagicNumberCheck)
		this.exec3_2__catalog_getBook = this.executionFactory.genExecution("Catalog", "catalog", "getBook", TestEventTrace2ExecutionTraceFilter.TRACE_ID,
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
		final ExecutionTrace executionTrace = new ExecutionTrace(TestEventTrace2ExecutionTraceFilter.TRACE_ID);

		executionTrace.add(this.exec3_2__catalog_getBook);
		executionTrace.add(this.exec2_1__crm_getOrders);
		executionTrace.add(this.exec0_0__bookstore_searchBook);
		executionTrace.add(this.exec1_1__catalog_getBook);

		try {
			/* Make sure that trace is valid: */
			executionTrace.toMessageTrace(this.systemEntityFactory.getRootExecution());
		} catch (final InvalidTraceException ex) {
			TestEventTrace2ExecutionTraceFilter.LOG.error("", ex);
			Assert.fail("Test invalid since used trace invalid");
			throw new InvalidTraceException("Test invalid since used trace invalid", ex);
		}

		return executionTrace;
	}

	@Test
	public void testValidTraceWithBeforeAndAfterOperationEvents() throws InvalidTraceException {
		/*
		 * Create an EventRecordTrace, containing only Before- and AfterOperation events.
		 */
		final List<TraceEvent> eventList =
				BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(this.exec0_0__bookstore_searchBook.getTin(),
						TestEventTrace2ExecutionTraceFilter.TRACE_ID);
		final EventRecordTrace eventRecordTrace = new EventRecordTrace(TestEventTrace2ExecutionTraceFilter.TRACE_ID);
		for (final TraceEvent ev : eventList) {
			eventRecordTrace.add(ev);
		}

		/*
		 * Create the transformation filter
		 */
		final Configuration filterConfiguration = new Configuration();
		final Map<String, AbstractRepository> repositoryMap = new HashMap<String, AbstractRepository>();
		repositoryMap.put(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, this.systemEntityFactory);
		final EventTrace2ExecutionTraceFilter filter = new EventTrace2ExecutionTraceFilter(filterConfiguration, repositoryMap);

		/*
		 * Create and connect a sink plugin which collects the transformed
		 * ExecutionTraces
		 */
		final SimpleSinkPlugin executionTraceSinkPlugin = new SimpleSinkPlugin();
		AbstractPlugin.connect(filter, EventTrace2ExecutionTraceFilter.OUTPUT_EXECUTION_TRACE, executionTraceSinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);

		filter.inputEventTrace(eventRecordTrace);

		Assert.assertEquals("Unexpected number of received execution traces", 1, executionTraceSinkPlugin.getList().size());

		final ExecutionTrace resultingExecutionTrace = (ExecutionTrace) executionTraceSinkPlugin.getList().get(0);
		final Execution[] resultingExecutionsArr = resultingExecutionTrace.getTraceAsSortedExecutionSet().toArray(new Execution[] {});

		/*
		 * Check validity of transformed execution trace
		 */

	}
}
