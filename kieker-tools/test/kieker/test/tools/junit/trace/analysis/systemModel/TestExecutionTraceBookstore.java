/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.trace.analysis.systemModel;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.filter.traceReconstruction.InvalidTraceException;
import kieker.tools.trace.analysis.systemModel.AbstractMessage;
import kieker.tools.trace.analysis.systemModel.AllocationComponent;
import kieker.tools.trace.analysis.systemModel.Execution;
import kieker.tools.trace.analysis.systemModel.ExecutionTrace;
import kieker.tools.trace.analysis.systemModel.MessageTrace;
import kieker.tools.trace.analysis.systemModel.Operation;
import kieker.tools.trace.analysis.systemModel.SynchronousCallMessage;
import kieker.tools.trace.analysis.systemModel.SynchronousReplyMessage;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.tools.util.bookstore.ExecutionFactory;

/**
 *
 * @author Andre van Hoorn
 *
 * @since 1.2
 */
public class TestExecutionTraceBookstore extends AbstractKiekerTest {
	private static final long TRACE_ID = 69898L;
	private static final String SESSION_ID = "iXsnm70o4N";

	private volatile ExecutionFactory eFactory;
	private volatile long minTin;
	private volatile long maxTout;
	private volatile int numExecutions;

	// Executions of a valid trace
	private volatile Execution exec0_0__bookstore_searchBook; // NOPMD NOCS (VariableNamingConventions)
	private volatile Execution exec1_1__catalog_getBook; // NOPMD NOCS (VariableNamingConventions)
	private volatile Execution exec2_1__crm_getOrders; // NOPMD NOCS (VariableNamingConventions)
	private volatile Execution exec3_2__catalog_getBook; // NOPMD NOCS (VariableNamingConventions)

	/**
	 * Default constructor.
	 */
	public TestExecutionTraceBookstore() {
		// empty default constructor
	}

	@Before
	public void setUp() throws Exception {
		final SystemModelRepository systemEntityFactory = new SystemModelRepository(new Configuration(), new AnalysisController());
		this.eFactory = new ExecutionFactory(systemEntityFactory);

		int myNumExecutions = 0;

		// Manually create Executions for a trace
		myNumExecutions++;
		this.exec0_0__bookstore_searchBook = this.eFactory.genExecution("Bookstore", "bookstore", "searchBook", TestExecutionTraceBookstore.TRACE_ID,
				TestExecutionTraceBookstore.SESSION_ID, 1, 10, 0, 0);
		this.minTin = this.exec0_0__bookstore_searchBook.getTin();
		this.maxTout = this.exec0_0__bookstore_searchBook.getTout();

		myNumExecutions++;
		this.exec1_1__catalog_getBook = this.eFactory.genExecution("Catalog", "catalog", "getBook", TestExecutionTraceBookstore.TRACE_ID,
				TestExecutionTraceBookstore.SESSION_ID, 2, 4, 1, 1);
		myNumExecutions++;
		this.exec2_1__crm_getOrders = this.eFactory.genExecution("CRM", "crm", "getOrders", TestExecutionTraceBookstore.TRACE_ID,
				TestExecutionTraceBookstore.SESSION_ID, 5, 8, 2, 1);
		myNumExecutions++;
		this.exec3_2__catalog_getBook = this.eFactory.genExecution("Catalog", "catalog", "getBook", TestExecutionTraceBookstore.TRACE_ID,
				TestExecutionTraceBookstore.SESSION_ID, 6, 7, 3, 2);

		// Just some basic checks to make sure that the trace has been set up properly (we've had some trouble here)
		Assert.assertNotSame(this.exec3_2__catalog_getBook.getOperation(), this.exec2_1__crm_getOrders.getOperation());
		Assert.assertNotSame(this.exec0_0__bookstore_searchBook.getAllocationComponent(), this.exec1_1__catalog_getBook.getAllocationComponent());

		this.numExecutions = myNumExecutions;
	}

	private ExecutionTrace genValidBookstoreTrace() throws InvalidTraceException {
		// Create an Execution Trace and add Executions in arbitrary order
		final ExecutionTrace executionTrace = new ExecutionTrace(TestExecutionTraceBookstore.TRACE_ID, TestExecutionTraceBookstore.SESSION_ID);

		executionTrace.add(this.exec3_2__catalog_getBook);
		executionTrace.add(this.exec2_1__crm_getOrders);
		executionTrace.add(this.exec0_0__bookstore_searchBook);
		executionTrace.add(this.exec1_1__catalog_getBook);

		return executionTrace;
	}

	/**
	 * Tests whether the "well-known" Bookstore trace gets correctly
	 * represented as an Execution Trace.
	 *
	 * @throws InvalidTraceException
	 *             If the internally assembled execution trace is somehow invalid.
	 */
	@Test
	public void testValidExecutionTrace() throws InvalidTraceException {
		final ExecutionTrace executionTrace = this.genValidBookstoreTrace();
		// Perform some validity checks on the execution trace object
		Assert.assertEquals("Invalid length of Execution Trace", executionTrace.getLength(), this.numExecutions);
		Assert.assertEquals("Invalid maximum stack depth", executionTrace.getMaxEss(), 2);
		Assert.assertEquals("Invalid minimum tin timestamp", executionTrace.getMinTin(), this.minTin);
		Assert.assertEquals("Invalid maximum tout timestamp", executionTrace.getMaxTout(), this.maxTout);
	}

	/**
	 * Tests the equals method of the ExecutionTrace class with two equal
	 * traces.
	 *
	 * @throws InvalidTraceException
	 *             If the internally assembled execution trace is somehow invalid.
	 */
	@Test
	public void testEqualMethodEqualTraces() throws InvalidTraceException {
		final ExecutionTrace execTrace1 = this.genValidBookstoreTrace();
		final ExecutionTrace execTrace2 = this.genValidBookstoreTrace();
		Assert.assertEquals(execTrace1, execTrace2);
	}

	/**
	 * Tests the equals method of the ExecutionTrace class with two different
	 * traces.
	 *
	 * @throws InvalidTraceException
	 *             If the internally assembled execution trace is somehow invalid.
	 */
	@Test
	public void testEqualMethodDifferentTraces() throws InvalidTraceException {
		final ExecutionTrace execTrace1 = this.genValidBookstoreTrace();
		final ExecutionTrace execTrace2 = this.genBrokenBookstoreTraceEoiSkip();
		Assert.assertFalse(execTrace1.equals(execTrace2));
	}

	private enum VariationPoint {
		OPERATION, ALLOCATION, TRACE_ID, SESSION_ID, EOI, ESS, TIN, TOUT
	}

	/**
	 * Returns an {@link Execution} with each field being equal to that of <i>executionTemplate</i> except for the value
	 * of the given {@link VariationPoint} being set to the respective value of <i>variationTemplate</i>.
	 *
	 * @param executionTemplate
	 * @param vPoint
	 * @param variationTemplate
	 * @return
	 */
	private Execution cloneExecutionWithVariation(final Execution executionTemplate, final VariationPoint vPoint,
			final Execution variationTemplate) {
		Operation op = executionTemplate.getOperation();
		AllocationComponent allocComp = executionTemplate.getAllocationComponent();
		long traceId = executionTemplate.getTraceId();
		String sessionId = executionTemplate.getSessionId();
		int eoi = executionTemplate.getEoi();
		int ess = executionTemplate.getEss();
		long tin = executionTemplate.getTin();
		long tout = executionTemplate.getTout();
		final boolean assumed = executionTemplate.isAssumed();

		// Now perform the selected variation
		switch (vPoint) {
		case ALLOCATION:
			allocComp = variationTemplate.getAllocationComponent();
			break;
		case EOI:
			eoi = variationTemplate.getEoi();
			break;
		case ESS:
			ess = variationTemplate.getEss();
			break;
		case OPERATION:
			op = variationTemplate.getOperation();
			break;
		case SESSION_ID:
			sessionId = variationTemplate.getSessionId();
			break;
		case TIN:
			tin = variationTemplate.getTin();
			break;
		case TOUT:
			tout = variationTemplate.getTout();
			break;
		case TRACE_ID:
			traceId = variationTemplate.getTraceId();
			break;
		default:
			Assert.fail();
			break;
		}

		final Execution retVal = new Execution(op, allocComp, traceId, sessionId, eoi, ess, tin, tout, assumed);

		Assert.assertFalse("executions must vary in " + vPoint + " but are equal: " + executionTemplate + " ; " + retVal,
				retVal.equals(executionTemplate));

		return retVal;
	}

	@Test
	public void testExecutionTraceEqualMethod() throws InvalidTraceException {
		final ExecutionTrace trace0 = this.genValidBookstoreTrace();

		/**
		 * Will be used to create a clone of exec0_0__bookstore_searchBook with certain variations
		 * selected from the execution.
		 */
		final Execution variationTemplate = new Execution(this.exec1_1__catalog_getBook.getOperation(), this.exec1_1__catalog_getBook.getAllocationComponent(),
				this.exec1_1__catalog_getBook.getTraceId() + 100, this.exec1_1__catalog_getBook.getSessionId() + "_",
				this.exec1_1__catalog_getBook.getEoi() + 100, this.exec1_1__catalog_getBook.getEss() + 100,
				this.exec1_1__catalog_getBook.getTin() + 100, this.exec1_1__catalog_getBook.getTout(), !this.exec1_1__catalog_getBook.isAssumed());

		vLoop: for (final VariationPoint vPoint : VariationPoint.values()) {
			final ExecutionTrace trace1 = new ExecutionTrace(trace0.getTraceId(), trace0.getSessionId());
			for (final Execution execFromTrace0 : trace0.getTraceAsSortedExecutionSet()) {
				final Execution execToAddToTrace1;
				if (execFromTrace0 == this.exec0_0__bookstore_searchBook) {
					execToAddToTrace1 = this.cloneExecutionWithVariation(this.exec0_0__bookstore_searchBook, vPoint, variationTemplate);
					// This tests the Execution's equals method already
					Assert.assertFalse("Executions must not be equal (variation point: " + vPoint + " ) but they are: " + execFromTrace0 + "; " + execToAddToTrace1,
							execFromTrace0.equals(execToAddToTrace1));
					if (vPoint == VariationPoint.TRACE_ID) {
						// We won't be able to continue for this variation because we cannot add an execution
						// with a varying trace id. However, at least we've tested the Execution's equal method.
						continue vLoop;
					}
				} else {
					execToAddToTrace1 = execFromTrace0;
				}
				trace1.add(execToAddToTrace1);
			}

			Assert.assertFalse("Execution traces must not be equal (variation point: " + vPoint + " ) but they are: " + trace0 + "; " + trace1,
					trace0.equals(trace1));
		}
	}

	/**
	 * This method can be used to debug the {@link java.util.Comparator} provided by {@link ExecutionTrace#createExecutionTraceComparator()}.
	 */
	@Test
	public void testTreeSet() {
		final SortedSet<Execution> s0 = new TreeSet<>(ExecutionTrace.createExecutionTraceComparator());
		final SortedSet<Execution> s1 = new TreeSet<>(ExecutionTrace.createExecutionTraceComparator());
		final Execution execFromTrace0 = this.exec0_0__bookstore_searchBook;
		final Execution long1 = new Execution(execFromTrace0.getOperation(), execFromTrace0.getAllocationComponent(), execFromTrace0.getTraceId(),
				execFromTrace0.getSessionId(), execFromTrace0.getEoi(), execFromTrace0.getEss(), execFromTrace0.getTin(),
				execFromTrace0.getTout(),
				execFromTrace0.isAssumed());
		s0.add(execFromTrace0);
		s1.add(long1);
		Assert.assertEquals("Expected sets to be equal", s0, s1);
	}

	/**
	 * Tests whether the "well-known" Bookstore trace can be correctly transformed
	 * from an Execution Trace representation into a Message Trace representation.
	 *
	 * @throws InvalidTraceException
	 *             If the internally assembled execution trace is somehow invalid.
	 */
	@Test
	public void testMessageTraceTransformationValidTrace() throws InvalidTraceException {
		final ExecutionTrace executionTrace = this.genValidBookstoreTrace();

		// Transform Execution Trace to Message Trace representation

		final MessageTrace messageTrace = executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);

		// Validate Message Trace representation.

		Assert.assertEquals("Invalid traceId", messageTrace.getTraceId(), TestExecutionTraceBookstore.TRACE_ID);
		final List<AbstractMessage> msgVector = messageTrace.getSequenceAsVector();
		Assert.assertEquals("Invalid number of messages in trace", msgVector.size(), this.numExecutions * 2);
		final AbstractMessage[] msgArray = msgVector.toArray(new AbstractMessage[0]);
		Assert.assertEquals(msgArray.length, this.numExecutions * 2);

		int curIdx = 0;
		{ // 1.: [0,0].Call #->bookstore.searchBook(..) // NOCS
			final AbstractMessage call0_0___root__bookstore_searchBook = msgArray[curIdx++]; // NOCS NOPMD
			Assert.assertTrue("Message is not a call", call0_0___root__bookstore_searchBook instanceof SynchronousCallMessage);
			Assert.assertEquals("Sending execution is not root execution", call0_0___root__bookstore_searchBook.getSendingExecution(),
					SystemModelRepository.ROOT_EXECUTION);
			Assert.assertEquals(call0_0___root__bookstore_searchBook.getReceivingExecution(), this.exec0_0__bookstore_searchBook);
			Assert.assertEquals("Message has wrong timestamp", call0_0___root__bookstore_searchBook.getTimestamp(), this.exec0_0__bookstore_searchBook.getTin());
		}
		{ // 2.: [1,1].Call bookstore.searchBook(..)->catalog.getBook(..) // NOCS
			final AbstractMessage call1_1___bookstore_searchBook_catalog_getBook = msgArray[curIdx++]; // NOCS NOPMD
			Assert.assertTrue("Message is not a call", call1_1___bookstore_searchBook_catalog_getBook instanceof SynchronousCallMessage);
			Assert.assertEquals(call1_1___bookstore_searchBook_catalog_getBook.getSendingExecution(), this.exec0_0__bookstore_searchBook);
			Assert.assertEquals(call1_1___bookstore_searchBook_catalog_getBook.getReceivingExecution(), this.exec1_1__catalog_getBook);
			Assert.assertEquals("Message has wrong timestamp", call1_1___bookstore_searchBook_catalog_getBook.getTimestamp(),
					this.exec1_1__catalog_getBook.getTin());
		}
		{ // 2.: [1,1].Return catalog.getBook(..)->bookstore.searchBook(..) // NOCS
			final AbstractMessage return1_1___catalog_getBook__bookstore_searchBook = msgArray[curIdx++]; // NOCS NOPMD
			Assert.assertTrue("Message is not a reply", return1_1___catalog_getBook__bookstore_searchBook instanceof SynchronousReplyMessage);
			Assert.assertEquals(return1_1___catalog_getBook__bookstore_searchBook.getSendingExecution(), this.exec1_1__catalog_getBook);
			Assert.assertEquals(return1_1___catalog_getBook__bookstore_searchBook.getReceivingExecution(), this.exec0_0__bookstore_searchBook);
			Assert.assertEquals("Message has wrong timestamp", return1_1___catalog_getBook__bookstore_searchBook.getTimestamp(),
					this.exec1_1__catalog_getBook.getTout());
		}
		{// 3.: [2,1].Call bookstore.searchBook(..)->crm.getOrders(..) // NOCS
			final AbstractMessage call2_1___bookstore_searchBook__crm_getOrders = msgArray[curIdx++]; // NOCS NOPMD
			Assert.assertTrue("Message is not a call", call2_1___bookstore_searchBook__crm_getOrders instanceof SynchronousCallMessage);
			Assert.assertEquals(call2_1___bookstore_searchBook__crm_getOrders.getSendingExecution(), this.exec0_0__bookstore_searchBook);
			Assert.assertEquals(call2_1___bookstore_searchBook__crm_getOrders.getReceivingExecution(), this.exec2_1__crm_getOrders);
			Assert.assertEquals("Message has wrong timestamp", call2_1___bookstore_searchBook__crm_getOrders.getTimestamp(), this.exec2_1__crm_getOrders.getTin());
		}
		{ // 4.: [3,2].Call crm.getOrders(..)->catalog.getBook(..) // NOCS
			final AbstractMessage call3_2___bookstore_searchBook__catalog_getBook = msgArray[curIdx++]; // NOCS NOPMD
			Assert.assertTrue("Message is not a call", call3_2___bookstore_searchBook__catalog_getBook instanceof SynchronousCallMessage);
			Assert.assertEquals(call3_2___bookstore_searchBook__catalog_getBook.getSendingExecution(), this.exec2_1__crm_getOrders);
			Assert.assertEquals(call3_2___bookstore_searchBook__catalog_getBook.getReceivingExecution(), this.exec3_2__catalog_getBook);
			Assert.assertEquals("Message has wrong timestamp", call3_2___bookstore_searchBook__catalog_getBook.getTimestamp(),
					this.exec3_2__catalog_getBook.getTin());
		}
		{ // 5.: [3,2].Return catalog.getBook(..)->crm.getOrders(..) // NOCS
			final AbstractMessage return3_2___catalog_getBook__crm_getOrders = msgArray[curIdx++]; // NOCS NOPMD
			Assert.assertTrue("Message is not a reply", return3_2___catalog_getBook__crm_getOrders instanceof SynchronousReplyMessage);
			Assert.assertEquals(return3_2___catalog_getBook__crm_getOrders.getSendingExecution(), this.exec3_2__catalog_getBook);
			Assert.assertEquals(return3_2___catalog_getBook__crm_getOrders.getReceivingExecution(), this.exec2_1__crm_getOrders);
			Assert.assertEquals("Message has wrong timestamp", return3_2___catalog_getBook__crm_getOrders.getTimestamp(), this.exec3_2__catalog_getBook.getTout());
		}
		{ // 6.: [2,1].Return crm.getOrders(..)->bookstore.searchBook // NOCS
			final AbstractMessage return2_1___crm_getOrders__bookstore_searchBook = msgArray[curIdx++]; // NOCS NOPMD
			Assert.assertTrue("Message is not a reply", return2_1___crm_getOrders__bookstore_searchBook instanceof SynchronousReplyMessage);
			Assert.assertEquals(return2_1___crm_getOrders__bookstore_searchBook.getSendingExecution(), this.exec2_1__crm_getOrders);
			Assert.assertEquals(return2_1___crm_getOrders__bookstore_searchBook.getReceivingExecution(), this.exec0_0__bookstore_searchBook);
			Assert.assertEquals("Message has wrong timestamp", return2_1___crm_getOrders__bookstore_searchBook.getTimestamp(),
					this.exec2_1__crm_getOrders.getTout());
		}
	}

	/**
	 * Make sure that the transformation from an Execution Trace to a Message
	 * Trace is performed only once.
	 *
	 * @throws InvalidTraceException
	 *             If the internally assembled execution trace is somehow invalid.
	 */
	@Test
	public void testMessageTraceTransformationOnlyOnce() throws InvalidTraceException {
		final ExecutionTrace executionTrace = this.genValidBookstoreTrace();
		// Transform Execution Trace to Message Trace representation (twice) and make sure, that the instances are the same.
		final MessageTrace messageTrace1 = executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);
		final MessageTrace messageTrace2 = executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);
		Assert.assertSame(messageTrace1, messageTrace2);
	}

	/**
	 * Make sure that the transformation from an Execution Trace to a Message
	 * Trace is performed only once.
	 *
	 * @throws InvalidTraceException
	 *             If the internally assembled execution trace is somehow invalid.
	 */
	@Test
	public void testMessageTraceTransformationTwiceOnChange() throws InvalidTraceException {
		final ExecutionTrace executionTrace = this.genValidBookstoreTrace();

		final Execution exec4_1__catalog_getBook = this.eFactory // NOCS NOPMD
				.genExecution("Catalog", "catalog", "getBook", TestExecutionTraceBookstore.TRACE_ID, TestExecutionTraceBookstore.SESSION_ID, 9, 10, 4, 1);
		final MessageTrace messageTrace1 = executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);
		executionTrace.add(exec4_1__catalog_getBook);
		final MessageTrace messageTrace2 = executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);
		Assert.assertNotSame(messageTrace1, messageTrace2);
	}

	/**
	 * Creates a broken execution trace version of the "well-known" Bookstore
	 * trace leads to an exception.
	 *
	 * The trace is broken in that the eoi/ess values of an execution with eoi/ess
	 * [1,1] are replaced by the eoi/ess values [1,3]. Since ess values must only
	 * increment/decrement by 1, this test must lead to an exception.
	 *
	 * @return
	 * @throws InvalidTraceException
	 *             If the internally assembled execution trace is somehow invalid.
	 */
	private ExecutionTrace genBrokenBookstoreTraceEssSkip() throws InvalidTraceException {
		// Create an Execution Trace and add Executions in arbitrary order
		final ExecutionTrace executionTrace = new ExecutionTrace(TestExecutionTraceBookstore.TRACE_ID, TestExecutionTraceBookstore.SESSION_ID);
		final Execution exec1_1__catalog_getBook__broken = this.eFactory.genExecution("Catalog", "catalog", "getBook", TestExecutionTraceBookstore.TRACE_ID, // NOCS
																																								// NOPMD
				TestExecutionTraceBookstore.SESSION_ID, 2, 4, 1, 3); // NOCS
		Assert.assertFalse("Invalid test", exec1_1__catalog_getBook__broken.equals(this.exec1_1__catalog_getBook));

		executionTrace.add(this.exec3_2__catalog_getBook);
		executionTrace.add(this.exec2_1__crm_getOrders);
		executionTrace.add(this.exec0_0__bookstore_searchBook);
		executionTrace.add(exec1_1__catalog_getBook__broken);

		return executionTrace;
	}

	/**
	 * Assert that the transformation of a broken execution trace version of the
	 * "well-known" Bookstore trace leads to an exception.
	 *
	 * The trace is broken in that the eoi/ess values of an execution with eoi/ess
	 * [1,1] are replaced by the eoi/ess values [1,3]. Since ess values must only
	 * increment/decrement by 1, this test must lead to an exception.
	 *
	 * @throws InvalidTraceException
	 *             If the internally assembled execution trace is somehow invalid.
	 */
	@Test(expected = InvalidTraceException.class)
	public void testMessageTraceTransformationBrokenTraceEssSkip() throws InvalidTraceException {
		final ExecutionTrace executionTrace = this.genBrokenBookstoreTraceEssSkip();
		// Transform Execution Trace to Message Trace representation

		// The following call must throw an Exception in this test case
		executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);
	}

	/**
	 * Creates a broken execution trace version of the "well-known" Bookstore
	 * trace leads to an exception.
	 *
	 * The trace is broken in that the eoi/ess values of an execution with eoi/ess
	 * [3,2] are replaced by the eoi/ess values [4,2]. Since eoi values must only
	 * increment by 1, this test must lead to an exception.
	 *
	 * @return
	 * @throws InvalidTraceException
	 *             If the internally assembled execution trace is somehow invalid.
	 */
	private ExecutionTrace genBrokenBookstoreTraceEoiSkip() throws InvalidTraceException {
		// Create an Execution Trace and add Executions in arbitrary order
		final ExecutionTrace executionTrace = new ExecutionTrace(TestExecutionTraceBookstore.TRACE_ID, TestExecutionTraceBookstore.SESSION_ID);
		final Execution exec3_2__catalog_getBook__broken = // NOCS NOPMD
				this.eFactory.genExecution("Catalog", "catalog", "getBook", TestExecutionTraceBookstore.TRACE_ID, // NOCS NOPMD
						TestExecutionTraceBookstore.SESSION_ID, 6, 7, 4, 2);
		Assert.assertFalse("Invalid test", exec3_2__catalog_getBook__broken.equals(this.exec3_2__catalog_getBook));

		executionTrace.add(exec3_2__catalog_getBook__broken);
		executionTrace.add(this.exec2_1__crm_getOrders);
		executionTrace.add(this.exec0_0__bookstore_searchBook);
		executionTrace.add(this.exec1_1__catalog_getBook);

		return executionTrace;
	}

	/**
	 * Assert that the transformation of a broken execution trace version of the
	 * "well-known" Bookstore trace leads to an exception.
	 *
	 * The trace is broken in that the eoi/ess values of an execution with eoi/ess
	 * [3,2] are replaced by the eoi/ess values [4,2]. Since eoi values must only
	 * increment by 1, this test must lead to an exception.
	 *
	 * @throws InvalidTraceException
	 *             If the internally assembled execution trace is somehow invalid.
	 */
	@Test(expected = InvalidTraceException.class)
	public void testMessageTraceTransformationBrokenTraceEoiSkip() throws InvalidTraceException {
		// Create an Execution Trace and add Executions in arbitrary order
		final ExecutionTrace executionTrace = this.genBrokenBookstoreTraceEoiSkip();

		// Transform Execution Trace to Message Trace representation

		// The following call must throw an Exception in this test case
		executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);
	}
}
