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

import junit.framework.Assert;

import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.common.configuration.Configuration;
import kieker.test.analysis.junit.plugin.SimpleSinkPlugin;
import kieker.test.tools.junit.traceAnalysis.util.BookstoreEventRecordFactory;
import kieker.test.tools.junit.traceAnalysis.util.BookstoreExecutionFactory;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.flow.EventRecordTrace;
import kieker.tools.traceAnalysis.filter.flow.EventTrace2ExecutionAndMessageTraceFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestEventTrace2ExecutionTraceFilter {
	// private static final Log LOG = LogFactory.getLog(TestEventTrace2ExecutionTraceFilter.class);

	private static final long TRACE_ID = 4563L;
	private static final String SESSION_ID = "y2zGAI0VX"; // Same Session ID for all traces
	private static final String HOSTNAME = "srv090";

	private final SystemModelRepository systemEntityFactory = new SystemModelRepository(new Configuration());
	private final BookstoreExecutionFactory bookstoreExecutionFactory = new BookstoreExecutionFactory(this.systemEntityFactory);

	/* Executions of a valid trace */
	private final Execution exec0_0__bookstore_searchBook; // NOCS
	private final Execution exec1_1__catalog_getBook; // NOCS
	private final Execution exec2_1__crm_getOrders; // NOCS
	private final Execution exec3_2__catalog_getBook; // NOCS

	/**
	 * Borrowed from {@link kieker.test.tools.junit.traceAnalysis.filter.TestTraceReconstructionFilter}.
	 */
	public TestEventTrace2ExecutionTraceFilter() {
		// Note that we are using AbstractTraceAnalysisFilter.createExecutionByEntityNames in order to get the
		// *same* system entities as used by the tested filter.

		// Note that the tins and tout must match those created by BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents

		final long initialTimestamp = 1 * (1000 * 1000);

		/* Manually create Executions for a trace */
		this.exec0_0__bookstore_searchBook =
				this.bookstoreExecutionFactory.createBookstoreExecution_exec0_0__bookstore_searchBook(TestEventTrace2ExecutionTraceFilter.TRACE_ID,
						TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME,
						/* tin: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
						/* tout: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook);

		this.exec1_1__catalog_getBook =
				this.bookstoreExecutionFactory.createBookstoreExecution_exec1_1__catalog_getBook(TestEventTrace2ExecutionTraceFilter.TRACE_ID,
						TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME,
						/* tin: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry1_1__catalog_getBook,
						/* tout: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit1_1__catalog_getBook);

		this.exec2_1__crm_getOrders = this.bookstoreExecutionFactory.createBookstoreExecution_exec2_1__crm_getOrders(TestEventTrace2ExecutionTraceFilter.TRACE_ID,
				TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME,
				/* tin: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry2_1__crm_getOrders,
				/* tout: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit2_1__crm_getOrders);

		this.exec3_2__catalog_getBook = this.bookstoreExecutionFactory.createBookstoreExecution_exec3_2__catalog_getBook(
				TestEventTrace2ExecutionTraceFilter.TRACE_ID,
				TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME,
				/* tin: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry3_2__catalog_getBook,
				/* tout: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit3_2__catalog_getBook);
	}

	/**
	 * Generates an execution trace representation of the "well-known" bookstore
	 * trace.
	 * 
	 * Borrowed from {@link kieker.test.tools.junit.traceAnalysis.filter.TestTraceReconstructionFilter}.
	 * 
	 * @return
	 * @throws InvalidTraceException
	 */
	private ExecutionTrace genValidBookstoreTrace() throws InvalidTraceException {
		/*
		 * Create an Execution Trace and add Executions in
		 * arbitrary order
		 */
		final ExecutionTrace executionTrace =
				new ExecutionTrace(TestEventTrace2ExecutionTraceFilter.TRACE_ID, TestEventTrace2ExecutionTraceFilter.SESSION_ID);

		executionTrace.add(this.exec3_2__catalog_getBook);
		executionTrace.add(this.exec2_1__crm_getOrders);
		executionTrace.add(this.exec0_0__bookstore_searchBook);
		executionTrace.add(this.exec1_1__catalog_getBook);

		// just to make sure that this trace is valid
		executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);

		return executionTrace;
	}

	@Test
	public void testValidTraceWithBeforeAndAfterOperationEvents() throws InvalidTraceException, IllegalStateException, AnalysisConfigurationException { // NOPMD
		/*
		 * Create an EventRecordTrace, containing only Before- and AfterOperation events.
		 */
		final EventRecordTrace eventRecordTrace =
				BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(this.exec0_0__bookstore_searchBook.getTin(),
						TestEventTrace2ExecutionTraceFilter.TRACE_ID, TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME);
		final ExecutionTrace expectedExecutionTrace = this.genValidBookstoreTrace();

		this.checkTrace(eventRecordTrace, expectedExecutionTrace);
	}

	@Test
	public void testValidTraceWithBeforeAndAfterOperationEventsAndAdditionalCallEvents() throws InvalidTraceException, IllegalStateException, // NOPMD
			AnalysisConfigurationException {
		/*
		 * Create an EventRecordTrace, containing only Before- and AfterOperation events.
		 */
		final EventRecordTrace eventRecordTrace =
				BookstoreEventRecordFactory.validSyncTraceAdditionalCallEvents(this.exec0_0__bookstore_searchBook.getTin(),
						TestEventTrace2ExecutionTraceFilter.TRACE_ID, TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME);
		final ExecutionTrace expectedExecutionTrace = this.genValidBookstoreTrace();

		this.checkTrace(eventRecordTrace, expectedExecutionTrace);
	}

	/**
	 * Borrowed from {@link kieker.test.tools.junit.traceAnalysis.filter.TestTraceReconstructionFilter}.
	 */
	public ExecutionTrace genValidBookstoreTraceNoExitGetOrders() throws InvalidTraceException {
		final ExecutionTrace executionTrace =
				new ExecutionTrace(TestEventTrace2ExecutionTraceFilter.TRACE_ID, TestEventTrace2ExecutionTraceFilter.SESSION_ID);

		final long initialTimestamp = 1 * (1000 * 1000);

		/* Manually create Executions for a trace */
		executionTrace.add(this.exec0_0__bookstore_searchBook);
		executionTrace.add(this.exec1_1__catalog_getBook);

		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_exec2_1__crm_getOrders(
						TestEventTrace2ExecutionTraceFilter.TRACE_ID,
						TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME,
						/* The assumed entry timestamp is the exit timestamp of the previous call */
						/* tin: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders,
						/*
						 * We will only have a (before) call to CRM.getOrder(..), hence the assumed return timestamp is
						 * the return time of the wrapping Bookstore.searchBook(..) execution:
						 */
						/* tout: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook));

		executionTrace.add(this.exec3_2__catalog_getBook);

		return executionTrace;
	}

	@Test
	public void testValidTraceWithBeforeAndAfterOperationEventsAndAdditionalCallEventsAndGap() throws InvalidTraceException, IllegalStateException, // NOPMD
			AnalysisConfigurationException {
		/*
		 * Create an EventRecordTrace, containing only Before- and AfterOperation events.
		 */
		final EventRecordTrace eventRecordTrace =
				BookstoreEventRecordFactory.validSyncTraceAdditionalCallEventsGap(this.exec0_0__bookstore_searchBook.getTin(),
						TestEventTrace2ExecutionTraceFilter.TRACE_ID, TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME);
		final ExecutionTrace expectedExecutionTrace = this.genValidBookstoreTraceNoExitGetOrders();

		this.checkTrace(eventRecordTrace, expectedExecutionTrace);
	}

	/**
	 * Generates an a modified version of the the "well-known" bookstore
	 * trace, which included only the execution of <code>Bookstore.searchBook(..)<code> and the 
	 * nested execution of <code>Catalog.getBook(..)</code>.
	 * 
	 * Borrowed from {@link kieker.test.tools.junit.traceAnalysis.filter.TestTraceReconstructionFilter}.
	 * 
	 * @return
	 * @throws InvalidTraceException
	 */
	private ExecutionTrace genValidBookstoreTraceEntryCallExit() throws InvalidTraceException {
		/*
		 * Create an Execution Trace and add Executions in
		 * arbitrary order
		 */
		final ExecutionTrace executionTrace =
				new ExecutionTrace(TestEventTrace2ExecutionTraceFilter.TRACE_ID, TestEventTrace2ExecutionTraceFilter.SESSION_ID);

		final long initialTimestamp = this.exec0_0__bookstore_searchBook.getTin();

		/* Manually create Executions for a trace */
		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_exec0_0__bookstore_searchBook(TestEventTrace2ExecutionTraceFilter.TRACE_ID,
						TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME,
						/* tin: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
						/* tout: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook));

		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_exec1_1__catalog_getBook(TestEventTrace2ExecutionTraceFilter.TRACE_ID,
						TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME,
						/* tin: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call1_1__catalog_getBook,
						/*
						 * We will only have a (before) call to Catalog.getBook(..), hence the assumed return timestamp is
						 * the return time of the wrapping Bookstore.searchBook(..) execution:
						 */
						/* tout: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook));

		// just to make sure that this trace is valid
		executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);

		return executionTrace;
	}

	@Test
	public void testValidSyncTraceSimpleEntryCallExit() throws InvalidTraceException, IllegalStateException, AnalysisConfigurationException { // NOPMD
		/*
		 * Create an EventRecordTrace, containing only Before- and AfterOperation events.
		 */
		final EventRecordTrace eventRecordTrace =
				BookstoreEventRecordFactory.validSyncTraceSimpleEntryCallExit(this.exec0_0__bookstore_searchBook.getTin(),
						TestEventTrace2ExecutionTraceFilter.TRACE_ID, TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME);
		final ExecutionTrace expectedExecutionTrace = this.genValidBookstoreTraceEntryCallExit();

		this.checkTrace(eventRecordTrace, expectedExecutionTrace);
	}

	/**
	 * Generates an a modified version of the the "well-known" bookstore trace, which includes only the execution of <code>Bookstore.searchBook(..)<code> 
	 * and the nested (i.e., called both by <code>Bookstore.searchBook(..)<code>) executions of <code>Catalog.getBook(..)</code> and <code>CRM.getOrder(..)</code>.
	 * 
	 * Borrowed from {@link kieker.test.tools.junit.traceAnalysis.filter.TestTraceReconstructionFilter}.
	 * 
	 * @return
	 * @throws InvalidTraceException
	 */
	private ExecutionTrace genValidBookstoreTraceSimpleEntryCallReturnCallCallExit() throws InvalidTraceException {
		/*
		 * Create an Execution Trace and add Executions in
		 * arbitrary order
		 */
		final ExecutionTrace executionTrace =
				new ExecutionTrace(TestEventTrace2ExecutionTraceFilter.TRACE_ID, TestEventTrace2ExecutionTraceFilter.SESSION_ID);

		final long initialTimestamp = this.exec0_0__bookstore_searchBook.getTin();

		/* Manually create Executions for a trace */
		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_exec0_0__bookstore_searchBook(TestEventTrace2ExecutionTraceFilter.TRACE_ID,
						TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME,
						/* tin: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
						/* tout: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook));

		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_exec1_1__catalog_getBook(TestEventTrace2ExecutionTraceFilter.TRACE_ID,
						TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME,
						/* tin: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call1_1__catalog_getBook,
						/* tout: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders));

		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_exec2_1__crm_getOrders(
						TestEventTrace2ExecutionTraceFilter.TRACE_ID,
						TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME,
						/* The assumed entry timestamp is the exit timestamp of the previous call */
						/* tin: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders,
						/*
						 * We will only have a (before) call to CRM.getOrder(..), hence the assumed return timestamp is
						 * the return time of the wrapping Bookstore.searchBook(..) execution:
						 */
						/* tout: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook));

		// just to make sure that this trace is valid
		executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);

		return executionTrace;
	}

	@Test
	public void testValidSyncTraceSimpleEntryCallReturnCallCallExit() throws InvalidTraceException, IllegalStateException, AnalysisConfigurationException { // NOPMD
		/*
		 * Create an EventRecordTrace, containing only Before- and AfterOperation events.
		 */
		final EventRecordTrace eventRecordTrace =
				BookstoreEventRecordFactory.validSyncTraceSimpleEntryCallReturnCallCallExit(this.exec0_0__bookstore_searchBook.getTin(),
						TestEventTrace2ExecutionTraceFilter.TRACE_ID, TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME);
		final ExecutionTrace expectedExecutionTrace = this.genValidBookstoreTraceSimpleEntryCallReturnCallCallExit();

		this.checkTrace(eventRecordTrace, expectedExecutionTrace);
	}

	/**
	 * Generates an a modified version of the the "well-known" bookstore trace, which includes the execution of <code>Bookstore.searchBook(..)<code> 
	 * with a nested execution of <code>CRM.getOrder(..)</code> which again wraps the nested execution of <code>Catalog.getBook(..)</code>.
	 * 
	 * Borrowed from {@link kieker.test.tools.junit.traceAnalysis.filter.TestTraceReconstructionFilter}.
	 * 
	 * @return
	 * @throws InvalidTraceException
	 */
	private ExecutionTrace genValidSyncTraceSimpleEntryCallCallExit() throws InvalidTraceException {
		/*
		 * Create an Execution Trace and add Executions in
		 * arbitrary order
		 */
		final ExecutionTrace executionTrace =
				new ExecutionTrace(TestEventTrace2ExecutionTraceFilter.TRACE_ID, TestEventTrace2ExecutionTraceFilter.SESSION_ID);

		final long initialTimestamp = this.exec0_0__bookstore_searchBook.getTin();

		/* Manually create Executions for a trace */
		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_exec0_0__bookstore_searchBook(TestEventTrace2ExecutionTraceFilter.TRACE_ID,
						TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME,
						/* tin: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
						/* tout: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook));
		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_crm_getOrders(
						TestEventTrace2ExecutionTraceFilter.TRACE_ID,
						TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME,
						/* The assumed entry timestamp is the exit timestamp of the previous call */
						/* tin: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders,
						/*
						 * We will only have a (before) call to CRM.getOrder(..), hence the assumed return timestamp is
						 * the return time of the wrapping Bookstore.searchBook(..) execution:
						 */
						/* tout: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook,
						/* eoi: */1, /* ess: */1));
		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_catalog_getBook(
						TestEventTrace2ExecutionTraceFilter.TRACE_ID,
						TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME,
						/* tin: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call3_2__catalog_getBook,
						/* tout: */initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook,
						/* eoi: */2, /* ess: */2));

		// just to make sure that this trace is valid
		executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);

		return executionTrace;
	}

	@Test
	public void testValidSyncTraceSimpleEntryCallCallExit() throws InvalidTraceException, IllegalStateException, AnalysisConfigurationException { // NOPMD

		/*
		 * Create an EventRecordTrace, containing only Before- and AfterOperation events.
		 */
		final EventRecordTrace eventRecordTrace =
				BookstoreEventRecordFactory.validSyncTraceSimpleEntryCallCallExit(this.exec0_0__bookstore_searchBook.getTin(),
						TestEventTrace2ExecutionTraceFilter.TRACE_ID, TestEventTrace2ExecutionTraceFilter.SESSION_ID, TestEventTrace2ExecutionTraceFilter.HOSTNAME);
		final ExecutionTrace expectedExecutionTrace = this.genValidSyncTraceSimpleEntryCallCallExit();

		this.checkTrace(eventRecordTrace, expectedExecutionTrace);
	}

	private void checkTrace(final EventRecordTrace eventRecordTrace, final ExecutionTrace expectedExecutionTrace) throws InvalidTraceException,
			IllegalStateException, AnalysisConfigurationException {

		/*
		 * Create the transformation filter
		 */
		final Configuration filterConfiguration = new Configuration();
		final EventTrace2ExecutionAndMessageTraceFilter filter = new EventTrace2ExecutionAndMessageTraceFilter(filterConfiguration);
		/*
		 * Create and connect a sink plugin which collects the transformed
		 * ExecutionTraces
		 */
		final SimpleSinkPlugin<ExecutionTrace> executionTraceSinkPlugin = new SimpleSinkPlugin<ExecutionTrace>(new Configuration());
		final AnalysisController controller = new AnalysisController();

		controller.registerFilter(filter);
		controller.registerFilter(executionTraceSinkPlugin);
		controller.registerRepository(this.systemEntityFactory);
		controller.connect(filter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, this.systemEntityFactory);
		controller.connect(filter, EventTrace2ExecutionAndMessageTraceFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, executionTraceSinkPlugin,
				SimpleSinkPlugin.INPUT_PORT_NAME);

		// TODO: dirty. Use reader + controller.run()
		filter.inputEventTrace(eventRecordTrace);

		Assert.assertEquals("Unexpected number of received execution traces", 1, executionTraceSinkPlugin.getList().size());

		final ExecutionTrace resultingExecutionTrace = executionTraceSinkPlugin.getList().get(0);

		Assert.assertEquals("Unexpected execution trace", expectedExecutionTrace, resultingExecutionTrace);
	}
}
