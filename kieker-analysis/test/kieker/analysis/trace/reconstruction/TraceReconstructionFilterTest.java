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

package kieker.analysis.trace.reconstruction;

import java.util.concurrent.TimeUnit;

import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.util.bookstore.ExecutionFactory;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.Execution;
import kieker.model.system.model.ExecutionTrace;
import kieker.model.system.model.InvalidExecutionTrace;
import kieker.model.system.model.MessageTrace;
import kieker.model.system.model.exceptions.InvalidTraceException;

import kieker.test.common.junit.AbstractKiekerTest;

import teetime.framework.test.StageTester;

/**
 * A test for the {@link TraceReconstructionStage}.
 *
 * @author Andre van Hoorn, Nils Christian Ehmke
 *
 * @since 1.2
 */
public class TraceReconstructionFilterTest extends AbstractKiekerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(TraceReconstructionFilterTest.class);
	private static final long TRACE_ID = 62298L;
	private static final String SESSION_ID = "Y2zm6CRc";

	// Executions of a valid trace
	private final Execution exec0_0__bookstore_searchBook; // NOCS NOPMD
	private final Execution exec1_1__catalog_getBook; // NOCS NOPMD
	private final Execution exec2_1__crm_getOrders; // NOCS NOPMD
	private final Execution exec3_2__catalog_getBook; // NOCS NOPMD

	/**
	 * Creates a new instance of this class.
	 */
	public TraceReconstructionFilterTest() {
		final SystemModelRepository systemEntityFactory = new SystemModelRepository();
		final ExecutionFactory executionFactory = new ExecutionFactory(systemEntityFactory);

		// Manually create Executions for a trace
		this.exec0_0__bookstore_searchBook = executionFactory.genExecution("Bookstore", "bookstore", "searchBook", TraceReconstructionFilterTest.TRACE_ID,
				TraceReconstructionFilterTest.SESSION_ID, 1 * (1000 * 1000), 10 * (1000 * 1000), 0, 0);

		this.exec1_1__catalog_getBook = executionFactory.genExecution("Catalog", "catalog", "getBook", TraceReconstructionFilterTest.TRACE_ID,
				TraceReconstructionFilterTest.SESSION_ID, 2 * (1000 * 1000), 4 * (1000 * 1000), 1, 1);
		this.exec2_1__crm_getOrders = executionFactory.genExecution("CRM", "crm", "getOrders", TraceReconstructionFilterTest.TRACE_ID,
				TraceReconstructionFilterTest.SESSION_ID, 5 * (1000 * 1000), 8 * (1000 * 1000), 2, 1);
		this.exec3_2__catalog_getBook = executionFactory.genExecution("Catalog", "catalog", "getBook", TraceReconstructionFilterTest.TRACE_ID,
				TraceReconstructionFilterTest.SESSION_ID, 6 * (1000 * 1000), 7 * (1000 * 1000), 3, 2);
	}

	/**
	 * Generates an execution trace representation of the "well-known" bookstore trace.
	 *
	 * @return
	 * @throws InvalidTraceException
	 *             If the internally assembled execution trace is somehow invalid.
	 */
	private ExecutionTrace genValidBookstoreTrace() throws InvalidTraceException {
		// Create an Execution Trace and add Executions in arbitrary order
		final ExecutionTrace executionTrace = new ExecutionTrace(TraceReconstructionFilterTest.TRACE_ID, TraceReconstructionFilterTest.SESSION_ID);

		executionTrace.add(this.exec3_2__catalog_getBook);
		executionTrace.add(this.exec2_1__crm_getOrders);
		executionTrace.add(this.exec0_0__bookstore_searchBook);
		executionTrace.add(this.exec1_1__catalog_getBook);

		executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);

		return executionTrace;
	}

	/**
	 * Tests whether a valid trace is correctly reconstructed and passed to the right output port.
	 *
	 * @throws InvalidTraceException
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internal analysis is in an invalid state.
	 * @throws InvalidTraceException
	 *             If the internally assembled execution trace is somehow invalid.
	 */
	@Test
	public void testValidBookstoreTracePassed() throws InvalidTraceException, IllegalStateException, AnalysisConfigurationException {
		// These are the trace representations we want to be reconstructed by the filter
		final ExecutionTrace validExecutionTrace = this.genValidBookstoreTrace();
		final MessageTrace validMessageTrace = validExecutionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);

		final SystemModelRepository repository = new SystemModelRepository();

		final TraceReconstructionStage filter = new TraceReconstructionStage(repository, TimeUnit.NANOSECONDS, true, Long.MAX_VALUE);

		final Execution[] events = validExecutionTrace.getTraceAsSortedExecutionSet()
				.toArray(new Execution[validExecutionTrace.getTraceAsSortedExecutionSet().size()]);

		StageTester.test(filter).and().send(events).to(filter.getInputPort()).start();

		MatcherAssert.assertThat(filter.getExecutionTraceOutputPort(), StageTester.produces(validExecutionTrace));
		MatcherAssert.assertThat(filter.getMessageTraceOutputPort(), StageTester.produces(validMessageTrace));
		MatcherAssert.assertThat(filter.getInvalidExecutionTraceOutputPort(), StageTester.producesNothing());
	}

	/**
	 * Creates a broken execution trace version of the "well-known" Bookstore trace.
	 *
	 * The trace is broken in that the eoi/ess values of an execution with eoi/ess [1,1] are replaced by the eoi/ess values [1,3]. Since ess values must only
	 * increment/decrement by 1, this test must lead to an exception.
	 *
	 * @param executionFactory
	 *            The factory to be used to create the executions.
	 *
	 * @return The execution trace in question.
	 *
	 * @throws InvalidTraceException
	 *             If the traceIds of the execution trace and the executions are incompatible.
	 */
	private ExecutionTrace genBrokenBookstoreTraceEssSkip(final ExecutionFactory executionFactory) throws InvalidTraceException {
		// Create an Execution Trace and add Executions in arbitrary order
		final ExecutionTrace executionTrace = new ExecutionTrace(TraceReconstructionFilterTest.TRACE_ID, TraceReconstructionFilterTest.SESSION_ID);
		final Execution exec1_1__catalog_getBook__broken = executionFactory.genExecution("Catalog", "catalog", "getBook", // NOCS NOPMD
				TraceReconstructionFilterTest.TRACE_ID, TraceReconstructionFilterTest.SESSION_ID, 2 * (1000 * 1000), 4 * (1000 * 1000), 1, 3); // NOCS

		// (MagicNumberCheck)
		Assert.assertFalse("Invalid test", exec1_1__catalog_getBook__broken.equals(this.exec1_1__catalog_getBook));

		executionTrace.add(this.exec3_2__catalog_getBook);
		executionTrace.add(this.exec2_1__crm_getOrders);
		executionTrace.add(this.exec0_0__bookstore_searchBook);
		executionTrace.add(exec1_1__catalog_getBook__broken);

		return executionTrace;
	}

	/**
	 * Tests whether a broken trace is correctly detected and passed to the right output port.
	 *
	 * @throws InvalidTraceException
	 *             If the trace to reconstruct is somehow invalid.
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internal analysis is in an invalid state.
	 */
	@Test
	public void testBrokenBookstoreTracePassed() throws InvalidTraceException, IllegalStateException, AnalysisConfigurationException {
		final SystemModelRepository systemEntityFactory = new SystemModelRepository();
		final ExecutionFactory executionFactory = new ExecutionFactory(systemEntityFactory);

		// These are the trace representations we want to be reconstructed by the filter
		final ExecutionTrace invalidExecutionTrace = this.genBrokenBookstoreTraceEssSkip(executionFactory);

		final TraceReconstructionStage filter = new TraceReconstructionStage(systemEntityFactory, TimeUnit.NANOSECONDS, true, Long.MAX_VALUE);
		Assert.assertTrue("Test invalid since trace length smaller than filter timeout", invalidExecutionTrace.getDuration() <= filter
				.getMaxTraceDuration());

		TraceReconstructionFilterTest.LOGGER.info("This test triggers a FATAL warning about an ess skip <0,3> which can simply be ignored because it is desired");

		final Execution[] events = invalidExecutionTrace.getTraceAsSortedExecutionSet()
				.toArray(new Execution[invalidExecutionTrace.getTraceAsSortedExecutionSet().size()]);

		StageTester.test(filter).and().send(events).to(filter.getInputPort()).start();

		MatcherAssert.assertThat(filter.getExecutionTraceOutputPort(), StageTester.producesNothing());
		MatcherAssert.assertThat(filter.getMessageTraceOutputPort(), StageTester.producesNothing());
		MatcherAssert.assertThat(filter.getInvalidExecutionTraceOutputPort(), StageTester.produces(new InvalidExecutionTrace(invalidExecutionTrace)));
	}

}
