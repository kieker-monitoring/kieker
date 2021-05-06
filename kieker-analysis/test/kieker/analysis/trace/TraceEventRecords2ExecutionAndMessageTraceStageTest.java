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
package kieker.analysis.trace;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.stage.flow.TraceEventRecords;
import kieker.analysis.util.bookstore.BookstoreEventRecordFactory;
import kieker.analysis.util.bookstore.BookstoreExecutionFactory;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.Execution;
import kieker.model.system.model.ExecutionTrace;
import kieker.model.system.model.exceptions.InvalidTraceException;

import teetime.framework.test.StageTester;

/**
 * @author Andre van Hoorn
 * @author Reiner Jung -- teetime port
 *
 * @since 1.5
 */
public class TraceEventRecords2ExecutionAndMessageTraceStageTest {

	private static final long TRACE_ID = 4563L;
	private static final String SESSION_ID = "y2zGAI0VX"; // Same Session ID for all traces
	private static final String HOSTNAME = "srv090";

	private final SystemModelRepository systemEntityFactory = new SystemModelRepository();
	private final BookstoreExecutionFactory bookstoreExecutionFactory = new BookstoreExecutionFactory(this.systemEntityFactory);

	// Executions of a valid trace
	private final Execution exec0_0__bookstore_searchBook; // NOCS NOPMD
	private final Execution exec1_1__catalog_getBook; // NOCS NOPMD
	private final Execution exec2_1__crm_getOrders; // NOCS NOPMD
	private final Execution exec3_2__catalog_getBook; // NOCS NOPMD
	// might be needed, eventually: private final Execution exec0_0__bookstore_searchBook_assumed; // NOCS
	private final Execution exec1_1__catalog_getBook_assumed; // NOCS NOPMD
	private final Execution exec2_1__crm_getOrders_assumed; // NOCS NOPMD
	private final Execution exec3_2__catalog_getBook_assumed; // NOCS NOPMD

	/**
	 * Borrowed from {@link kieker.test.tools.junit.TraceReconstructionFilterTest.filter.TestTraceReconstructionFilter}.
	 */
	public TraceEventRecords2ExecutionAndMessageTraceStageTest() {
		// Note that we are using AbstractTraceAnalysisFilter.createExecutionByEntityNames in order to get the
		// *same* system entities as used by the tested filter.

		// Note that the tins and tout must match those created by BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents

		final long initialTimestamp = 1 * (1000 * 1000);

		// Manually create Executions for a trace
		this.exec0_0__bookstore_searchBook = this.bookstoreExecutionFactory.createBookstoreExecution_exec0_0__bookstore_searchBook(
				TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
				initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook, // tin
				initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook, // tout
				false); // assumed
		// might be needed, eventually:
		// this.exec0_0__bookstore_searchBook_assumed =
		// this.bookstoreExecutionFactory.createBookstoreExecution_exec0_0__bookstore_searchBook(TraceEventRecords2ExecutionAndMessageTraceFilterTest.TRACE_ID,
		// TraceEventRecords2ExecutionAndMessageTraceFilterTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceFilterTest.HOSTNAME,
		// initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook, // tin
		// initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook, // tout
		// true);// assumed

		this.exec1_1__catalog_getBook = this.bookstoreExecutionFactory.createBookstoreExecution_exec1_1__catalog_getBook(
				TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
				initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry1_1__catalog_getBook, // tin
				initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit1_1__catalog_getBook, // tout
				false); // assumed
		this.exec1_1__catalog_getBook_assumed = this.bookstoreExecutionFactory.createBookstoreExecution_exec1_1__catalog_getBook(
				TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
				initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry1_1__catalog_getBook, // tin
				initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit1_1__catalog_getBook, // tout
				true); // assumed

		this.exec2_1__crm_getOrders = this.bookstoreExecutionFactory.createBookstoreExecution_exec2_1__crm_getOrders(
				TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
				initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry2_1__crm_getOrders, // tin
				initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit2_1__crm_getOrders, // tout
				false); // assumed
		this.exec2_1__crm_getOrders_assumed = this.bookstoreExecutionFactory.createBookstoreExecution_exec2_1__crm_getOrders(
				TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
				initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry2_1__crm_getOrders, // tin
				initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit2_1__crm_getOrders, // tout
				true); // assumed

		this.exec3_2__catalog_getBook = this.bookstoreExecutionFactory.createBookstoreExecution_exec3_2__catalog_getBook(
				TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
				initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry3_2__catalog_getBook, // tin
				initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit3_2__catalog_getBook, // tout
				false); // assumed
		this.exec3_2__catalog_getBook_assumed = this.bookstoreExecutionFactory.createBookstoreExecution_exec3_2__catalog_getBook(
				TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
				initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry3_2__catalog_getBook, // tin
				initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit3_2__catalog_getBook, // tout
				true); // assumed
	}

	/**
	 * Generates an execution trace representation of the "well-known" bookstore
	 * trace.
	 *
	 * Borrowed from {@link kieker.test.tools.junit.TraceReconstructionFilterTest.filter.TestTraceReconstructionFilter}.
	 *
	 * @return
	 * @throws InvalidTraceException
	 *             If the internally assembled execution trace is somehow invalid.
	 */
	private ExecutionTrace genValidBookstoreTrace() throws InvalidTraceException {
		// Create an Execution Trace and add Executions in arbitrary order
		final ExecutionTrace executionTrace = new ExecutionTrace(TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID);

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
		// Create an EventRecordTrace, containing only Before- and AfterOperation events.
		final TraceEventRecords traceEvents = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(this.exec0_0__bookstore_searchBook.getTin(),
				TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME);
		final ExecutionTrace expectedExecutionTrace;
		{ // NOCS
			// Create an Execution Trace and add Executions in arbitrary order
			expectedExecutionTrace = new ExecutionTrace(TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
					TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID);

			expectedExecutionTrace.add(this.exec3_2__catalog_getBook_assumed); // assumed because no call event
			expectedExecutionTrace.add(this.exec2_1__crm_getOrders_assumed); // assumed because no call event
			expectedExecutionTrace.add(this.exec0_0__bookstore_searchBook);
			expectedExecutionTrace.add(this.exec1_1__catalog_getBook_assumed); // assumed because no call event

			// just to make sure that this trace is valid
			expectedExecutionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);
		}

		this.checkTrace(traceEvents, expectedExecutionTrace);
	}

	@Test
	public void testValidTraceWithBeforeAndAfterOperationEventsAndAdditionalCallEvents() throws InvalidTraceException, IllegalStateException, // NOPMD
			AnalysisConfigurationException {
		// Create an EventRecordTrace, containing only Before- and AfterOperation events.
		final TraceEventRecords traceEvents = BookstoreEventRecordFactory.validSyncTraceAdditionalCallEvents(this.exec0_0__bookstore_searchBook.getTin(),
				TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME);
		final ExecutionTrace expectedExecutionTrace = this.genValidBookstoreTrace();

		this.checkTrace(traceEvents, expectedExecutionTrace);
	}

	/**
	 * Borrowed from {@link kieker.test.tools.junit.TraceReconstructionFilterTest.filter.TestTraceReconstructionFilter}.
	 *
	 * @throws InvalidTraceException
	 *             If the trace to reconstruct is somehow invalid.
	 *
	 * @return An execution trace from the Bookstore example.
	 */
	public ExecutionTrace genValidBookstoreTraceNoExitGetOrders() throws InvalidTraceException {
		final ExecutionTrace executionTrace = new ExecutionTrace(TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID);

		final long initialTimestamp = 1 * (1000 * 1000);

		// Manually create Executions for a trace
		executionTrace.add(this.exec0_0__bookstore_searchBook);
		executionTrace.add(this.exec1_1__catalog_getBook);

		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_exec2_1__crm_getOrders(
						TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
						TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
						// The assumed entry timestamp is the exit timestamp of the previous call
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders, // tin
						// We will only have a (before) call to CRM.getOrder(..), hence the assumed return timestamp is the return time of the wrapping
						// Bookstore.searchBook(..) execution:
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook, // tout
						false)); // assumed

		executionTrace.add(this.exec3_2__catalog_getBook);

		return executionTrace;
	}

	@Test
	public void testValidTraceWithBeforeAndAfterOperationEventsAndAdditionalCallEventsAndGap() throws InvalidTraceException, IllegalStateException, // NOPMD
			AnalysisConfigurationException {
		// Create an EventRecordTrace, containing only Before- and AfterOperation events.
		final TraceEventRecords traceEvents = BookstoreEventRecordFactory.validSyncTraceAdditionalCallEventsGap(this.exec0_0__bookstore_searchBook.getTin(),
				TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME);
		final ExecutionTrace expectedExecutionTrace;

		{ // NOCS
			expectedExecutionTrace = new ExecutionTrace(TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
					TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID);

			final long initialTimestamp = 1 * (1000 * 1000);

			// Manually create Executions for a trace
			expectedExecutionTrace.add(this.exec0_0__bookstore_searchBook);
			expectedExecutionTrace.add(this.exec1_1__catalog_getBook);

			expectedExecutionTrace.add(
					this.bookstoreExecutionFactory.createBookstoreExecution_exec2_1__crm_getOrders(
							TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
							TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
							// The assumed entry timestamp is the exit timestamp of the previous call
							initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders, // tin
							// We will only have a (before) call to CRM.getOrder(..), hence the assumed return timestamp is the return time of the wrapping
							// Bookstore.searchBook(..) execution:
							initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook, // tout
							true)); // assumed, because call is missing

			expectedExecutionTrace.add(this.exec3_2__catalog_getBook);
		}

		this.checkTrace(traceEvents, expectedExecutionTrace);
	}

	/**
	 * Generates an a modified version of the the "well-known" bookstore
	 * trace, which included only the execution of <code>Bookstore.searchBook(..)</code> and the
	 * nested execution of <code>Catalog.getBook(..)</code>.
	 *
	 * Borrowed from {@link kieker.test.tools.junit.TraceReconstructionFilterTest.filter.TestTraceReconstructionFilter}.
	 *
	 * @return
	 * @throws InvalidTraceException
	 *             If the internally assembled execution trace is somehow invalid.
	 */
	private ExecutionTrace genValidBookstoreTraceEntryCallExit() throws InvalidTraceException {
		// Create an Execution Trace and add Executions in arbitrary order
		final ExecutionTrace executionTrace = new ExecutionTrace(TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID);

		final long initialTimestamp = this.exec0_0__bookstore_searchBook.getTin();

		// Manually create Executions for a trace
		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_exec0_0__bookstore_searchBook(TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
						TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook, // tin
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook, // tout
						false)); // assumed

		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_exec1_1__catalog_getBook(TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
						TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call1_1__catalog_getBook, // tin
						// We will only have a (before) call to Catalog.getBook(..), hence the assumed return timestamp is the return time of the wrapping
						// Bookstore.searchBook(..) execution:
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook, // tout
						true)); // assumed, because no entry

		// just to make sure that this trace is valid
		executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);

		return executionTrace;
	}

	@Test
	public void testValidSyncTraceSimpleEntryCallExit() throws InvalidTraceException, IllegalStateException, AnalysisConfigurationException { // NOPMD
		// Create an EventRecordTrace, containing only Before- and AfterOperation events.
		final TraceEventRecords traceEvents = BookstoreEventRecordFactory.validSyncTraceSimpleEntryCallExit(this.exec0_0__bookstore_searchBook.getTin(),
				TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME);
		final ExecutionTrace expectedExecutionTrace = this.genValidBookstoreTraceEntryCallExit();

		this.checkTrace(traceEvents, expectedExecutionTrace);
	}

	@Test
	public void testValidSyncTraceSimpleEntryCallReturnCallCallExit() throws InvalidTraceException, IllegalStateException, AnalysisConfigurationException { // NOPMD
		// Create an EventRecordTrace, containing only Before- and AfterOperation events.
		final TraceEventRecords traceEvents = BookstoreEventRecordFactory.validSyncTraceSimpleEntryCallReturnCallCallExit(
				this.exec0_0__bookstore_searchBook.getTin(),
				TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME);
		final ExecutionTrace expectedExecutionTrace = this.genValidBookstoreTraceSimpleEntryCallReturnCallCallExit();

		this.checkTrace(traceEvents, expectedExecutionTrace);
	}

	@Test
	public void testValidSyncTraceSimpleEntryCallCallExit() throws InvalidTraceException, IllegalStateException, AnalysisConfigurationException { // NOPMD
		// Create an EventRecordTrace, containing only Before- and AfterOperation events.
		final TraceEventRecords traceEvents = BookstoreEventRecordFactory.validSyncTraceSimpleEntryCallCallExit(this.exec0_0__bookstore_searchBook.getTin(),
				TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME);
		final ExecutionTrace expectedExecutionTrace = this.genValidSyncTraceSimpleEntryCallCallExit();

		this.checkTrace(traceEvents, expectedExecutionTrace);
	}

	@Test
	public void testValidSyncTraceSimpleCallCall() throws InvalidTraceException, IllegalStateException, AnalysisConfigurationException { // NOPMD
		// Create an EventRecordTrace, containing only CallOperation events.
		final TraceEventRecords traceEvents = BookstoreEventRecordFactory.validSyncTraceSimpleCallCall(this.exec0_0__bookstore_searchBook.getTin(),
				TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME);
		final ExecutionTrace expectedExecutionTrace = this.genValidSyncTraceSimpleCallCall();

		this.checkTrace(traceEvents, expectedExecutionTrace);
	}

	/**
	 * Generates an a modified version of the the "well-known" bookstore trace, which includes only the execution of <code>Bookstore.searchBook(..)</code> and the
	 * nested (i.e., called both by <code>Bookstore.searchBook(..)</code>) executions of <code>Catalog.getBook(..)</code> and <code>CRM.getOrder(..)</code>.
	 *
	 * Borrowed from {@link kieker.test.tools.junit.TraceReconstructionFilterTest.filter.TestTraceReconstructionFilter}.
	 *
	 * @return
	 * @throws InvalidTraceException
	 *             If the internally assembled execution trace is somehow invalid.
	 */
	private ExecutionTrace genValidBookstoreTraceSimpleEntryCallReturnCallCallExit() throws InvalidTraceException {
		// Create an Execution Trace and add Executions in arbitrary order
		final ExecutionTrace executionTrace = new ExecutionTrace(TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID);

		final long initialTimestamp = this.exec0_0__bookstore_searchBook.getTin();

		// Manually create Executions for a trace
		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_exec0_0__bookstore_searchBook(TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
						TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook, // tin
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook, // tout
						false)); // assumed

		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_exec1_1__catalog_getBook(TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
						TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call1_1__catalog_getBook, // tin
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders, // tout
						true)); // assumed, because only call no entry

		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_exec2_1__crm_getOrders(
						TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
						TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
						// The assumed entry timestamp is the exit timestamp of the previous call
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders, // tin
						// We will only have a (before) call to CRM.getOrder(..), hence the assumed return timestamp is the return time of the wrapping
						// Bookstore.searchBook(..) execution:
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook, // tout
						true)); // , assumedbecause only call no entry

		// just to make sure that this trace is valid
		executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);

		return executionTrace;
	}

	/**
	 * Generates an a modified version of the the "well-known" bookstore trace, which includes the execution of <code>Bookstore.searchBook(..)</code> with a nested
	 * execution of <code>CRM.getOrder(..)</code> which again wraps the nested execution of <code>Catalog.getBook(..)</code>.
	 *
	 * Borrowed from {@link kieker.test.tools.junit.TraceReconstructionFilterTest.filter.TestTraceReconstructionFilter}.
	 *
	 * @return
	 * @throws InvalidTraceException
	 *             If the internally assembled execution trace is somehow invalid.
	 */
	private ExecutionTrace genValidSyncTraceSimpleEntryCallCallExit() throws InvalidTraceException {
		// Create an Execution Trace and add Executions in arbitrary order
		final ExecutionTrace executionTrace = new ExecutionTrace(TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID);

		final long initialTimestamp = this.exec0_0__bookstore_searchBook.getTin();

		// Manually create Executions for a trace
		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_exec0_0__bookstore_searchBook(TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
						TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook, // tin
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook, // tout
						false)); // assumed
		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_crm_getOrders(
						TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
						TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
						// The assumed entry timestamp is the exit timestamp of the previous call
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders, // tin
						// We will only have a (before) call to CRM.getOrder(..), hence the assumed return timestamp is the return time of the wrapping
						// Bookstore.searchBook(..) execution:
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook, // tout
						1, // eoi
						1, // ess
						true)); // assumed, because only call no entry
		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_catalog_getBook(
						TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
						TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call3_2__catalog_getBook, // tin
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook, // tout
						2, // eoi
						2, // ess
						true)); // assumed, because only call no entry

		// just to make sure that this trace is valid
		executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);

		return executionTrace;
	}

	/**
	 * Generates an a modified version of the the "well-known" bookstore trace, which includes the execution of <code>Bookstore.searchBook(..)<code>
	 * with a nested execution of <code>CRM.getOrder(..)</code> which again wraps the nested execution of <code>Catalog.getBook(..)</code>.
	 *
	 * Borrowed from {@link kieker.test.tools.junit.TraceReconstructionFilterTest.filter.TestTraceReconstructionFilter}.
	 *
	 * @return
	 * @throws InvalidTraceException
	 */
	private ExecutionTrace genValidSyncTraceSimpleCallCall() throws InvalidTraceException {
		// Create an Execution Trace and add Executions in arbitrary order
		final ExecutionTrace executionTrace = new ExecutionTrace(TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
				TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID);

		final long initialTimestamp = this.exec0_0__bookstore_searchBook.getTin();

		// Manually create Executions for a trace
		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_crm_getOrders(
						TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
						TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
						// The assumed entry timestamp is the exit timestamp of the previous call
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders, // tin
						// We will only have a (before) call to CRM.getOrder(..), hence the assumed return timestamp is the return time of the wrapping
						// Bookstore.searchBook(..) execution:
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call3_2__catalog_getBook, // tout
						0, // eois
						0, // ess
						true));
		executionTrace.add(
				this.bookstoreExecutionFactory.createBookstoreExecution_catalog_getBook(
						TraceEventRecords2ExecutionAndMessageTraceStageTest.TRACE_ID,
						TraceEventRecords2ExecutionAndMessageTraceStageTest.SESSION_ID, TraceEventRecords2ExecutionAndMessageTraceStageTest.HOSTNAME,
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call3_2__catalog_getBook, // tin
						initialTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call3_2__catalog_getBook, // tout
						1, 1, true));

		// just to make sure that this trace is valid
		executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);

		return executionTrace;
	}

	/**
	 *
	 * @param traceEvents
	 * @param expectedExecutionTrace
	 * @throws InvalidTraceException
	 * @throws IllegalStateException
	 *             If the internally assembled analysis is in an invalid state.
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 *
	 */
	private void checkTrace(final TraceEventRecords traceEvents, final ExecutionTrace expectedExecutionTrace) throws InvalidTraceException,
			IllegalStateException, AnalysisConfigurationException {

		final TraceEventRecords2ExecutionAndMessageTraceStage filter = new TraceEventRecords2ExecutionAndMessageTraceStage(this.systemEntityFactory, false, false,
				false);

		StageTester.test(filter).and().send(traceEvents).to(filter.getInputPort()).and().start();

		MatcherAssert.assertThat(filter.getExecutionTraceOutputPort(), StageTester.produces(expectedExecutionTrace));
	}
}
