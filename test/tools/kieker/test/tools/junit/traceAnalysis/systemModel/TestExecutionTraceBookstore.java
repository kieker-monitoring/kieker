/***************************************************************************
 * Copyright 2011 by
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

package kieker.test.tools.junit.traceAnalysis.systemModel;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.test.tools.junit.traceAnalysis.util.ExecutionFactory;
import kieker.tools.traceAnalysis.plugins.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.SynchronousCallMessage;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * 
 * @author Andre van Hoorn
 */
public class TestExecutionTraceBookstore extends TestCase {

	private static final Log LOG = LogFactory.getLog(TestExecutionTraceBookstore.class);
	private static final long TRACE_ID = 69898L;
	private final SystemModelRepository systemEntityFactory = new SystemModelRepository();
	private final ExecutionFactory eFactory = new ExecutionFactory(this.systemEntityFactory);
	private final long minTin;
	private final long maxTout;
	private final int numExecutions;

	/* Executions of a valid trace */
	private final Execution exec0_0__bookstore_searchBook; // NOCS
	private final Execution exec1_1__catalog_getBook; // NOCS
	private final Execution exec2_1__crm_getOrders; // NOCS
	private final Execution exec3_2__catalog_getBook; // NOCS

	public TestExecutionTraceBookstore() {
		int numExecutions_l = 0;

		/* Manually create Executions for a trace */
		numExecutions_l++;
		this.exec0_0__bookstore_searchBook = this.eFactory.genExecution("Bookstore", "bookstore", "searchBook", TestExecutionTraceBookstore.TRACE_ID,
				1, 10, 0, 0); // NOCS (MagicNumberCheck)
		this.minTin = this.exec0_0__bookstore_searchBook.getTin();
		this.maxTout = this.exec0_0__bookstore_searchBook.getTout();

		numExecutions_l++;
		this.exec1_1__catalog_getBook = this.eFactory.genExecution("Catalog", "catalog", "getBook", TestExecutionTraceBookstore.TRACE_ID, // NOPMD
				2, 4, 1, 1); // NOCS (MagicNumberCheck)
		numExecutions_l++;
		this.exec2_1__crm_getOrders = this.eFactory.genExecution("CRM", "crm", "getOrders", TestExecutionTraceBookstore.TRACE_ID,
				5, 8, 2, 1); // NOCS (MagicNumberCheck)
		numExecutions_l++;
		this.exec3_2__catalog_getBook = this.eFactory.genExecution("Catalog", "catalog", "getBook", TestExecutionTraceBookstore.TRACE_ID,
				6, 7, 3, 2); // NOCS (MagicNumberCheck)
		this.numExecutions = numExecutions_l;
	}

	private ExecutionTrace genValidBookstoreTrace() throws InvalidTraceException {
		/*
		 * Create an Execution Trace and add Executions in
		 * arbitrary order
		 */
		final ExecutionTrace executionTrace = new ExecutionTrace(TestExecutionTraceBookstore.TRACE_ID);

		executionTrace.add(this.exec3_2__catalog_getBook);
		executionTrace.add(this.exec2_1__crm_getOrders);
		executionTrace.add(this.exec0_0__bookstore_searchBook);
		executionTrace.add(this.exec1_1__catalog_getBook);

		return executionTrace;
	}

	/**
	 * Tests whether the "well-known" Bookstore trace gets correctly
	 * represented as an Execution Trace.
	 */
	@Test
	public void testValidExecutionTrace() {
		try {
			final ExecutionTrace executionTrace = this.genValidBookstoreTrace();
			/* Perform some validity checks on the execution trace object */
			Assert.assertEquals("Invalid length of Execution Trace", executionTrace.getLength(), this.numExecutions);
			Assert.assertEquals("Invalid maximum stack depth", executionTrace.getMaxEss(), 2);
			Assert.assertEquals("Invalid minimum tin timestamp", executionTrace.getMinTin(), this.minTin);
			Assert.assertEquals("Invalid maximum tout timestamp", executionTrace.getMaxTout(), this.maxTout);

		} catch (final InvalidTraceException ex) {
			TestExecutionTraceBookstore.LOG.error("InvalidTraceException", ex); // NOPMD (string literal)
			Assert.fail(ex.getMessage());
			return;
		}
	}

	/**
	 * Tests the equals method of the ExecutionTrace class with two equal
	 * traces.
	 */
	@Test
	public void testEqualMethodEqualTraces() {
		try {
			final ExecutionTrace execTrace1 = this.genValidBookstoreTrace();
			final ExecutionTrace execTrace2 = this.genValidBookstoreTrace();
			Assert.assertEquals(execTrace1, execTrace2);
		} catch (final InvalidTraceException ex) {
			TestExecutionTraceBookstore.LOG.error("InvalidTraceException", ex);
			Assert.fail(ex.getMessage());
			return;
		}
	}

	/**
	 * Tests the equals method of the ExecutionTrace class with two different
	 * traces.
	 */
	@Test
	public void testEqualMethodDifferentTraces() {
		try {
			final ExecutionTrace execTrace1 = this.genValidBookstoreTrace();
			final ExecutionTrace execTrace2 = this.genBrokenBookstoreTraceEoiSkip();
			Assert.assertFalse(execTrace1.equals(execTrace2));
		} catch (final InvalidTraceException ex) {
			TestExecutionTraceBookstore.LOG.error("InvalidTraceException", ex);
			Assert.fail(ex.getMessage());
			return;
		}
	}

	/**
	 * Tests whether the "well-known" Bookstore trace can be correctly transformed
	 * from an Execution Trace representation into a Message Trace representation.
	 */
	@Test
	public void testMessageTraceTransformationValidTrace() {

		ExecutionTrace executionTrace;
		try {
			executionTrace = this.genValidBookstoreTrace();
		} catch (final InvalidTraceException ex) {
			TestExecutionTraceBookstore.LOG.error("InvalidTraceException", ex);
			Assert.fail(ex.getMessage());
			return;
		}

		/*
		 * Transform Execution Trace to Message Trace representation
		 */
		MessageTrace messageTrace;
		try {
			messageTrace = executionTrace.toMessageTrace(this.systemEntityFactory.getRootExecution());
		} catch (final InvalidTraceException ex) {
			TestExecutionTraceBookstore.LOG.error("InvalidTraceException", ex);
			Assert.fail("Transformation to Message Trace failed: " + ex.getMessage());
			return;
		}

		/*
		 * Validate Message Trace representation.
		 */
		Assert.assertEquals("Invalid traceId", messageTrace.getTraceId(), TestExecutionTraceBookstore.TRACE_ID);
		final List<AbstractMessage> msgVector = messageTrace.getSequenceAsVector();
		Assert.assertEquals("Invalid number of messages in trace", msgVector.size(), this.numExecutions * 2);
		final AbstractMessage[] msgArray = msgVector.toArray(new AbstractMessage[msgVector.size()]);
		Assert.assertEquals(msgArray.length, this.numExecutions * 2);

		int curIdx = 0;
		{ /* 1.: [0,0].Call $->bookstore.searchBook(..) */// NOCS
			final AbstractMessage call0_0___root__bookstore_searchBook = msgArray[curIdx++]; // NOCS
			Assert.assertTrue("Message is not a call", call0_0___root__bookstore_searchBook instanceof SynchronousCallMessage); // NOPMD (string literal)
			Assert.assertEquals("Sending execution is not root execution", call0_0___root__bookstore_searchBook.getSendingExecution(),
					this.systemEntityFactory.getRootExecution());
			Assert.assertEquals(call0_0___root__bookstore_searchBook.getReceivingExecution(), this.exec0_0__bookstore_searchBook);
			Assert.assertEquals("Message has wrong timestamp", call0_0___root__bookstore_searchBook.getTimestamp(), this.exec0_0__bookstore_searchBook.getTin()); // NOPMD
		}
		{ /* 2.: [1,1].Call bookstore.searchBook(..)->catalog.getBook(..) */// NOCS
			final AbstractMessage call1_1___bookstore_searchBook_catalog_getBook = msgArray[curIdx++]; // NOCS
			Assert.assertTrue("Message is not a call", call1_1___bookstore_searchBook_catalog_getBook instanceof SynchronousCallMessage);
			Assert.assertEquals(call1_1___bookstore_searchBook_catalog_getBook.getSendingExecution(), this.exec0_0__bookstore_searchBook);
			Assert.assertEquals(call1_1___bookstore_searchBook_catalog_getBook.getReceivingExecution(), this.exec1_1__catalog_getBook);
			Assert.assertEquals("Message has wrong timestamp", call1_1___bookstore_searchBook_catalog_getBook.getTimestamp(), this.exec1_1__catalog_getBook.getTin());
		}
		{ /* 2.: [1,1].Return catalog.getBook(..)->bookstore.searchBook(..) */// NOCS
			final AbstractMessage return1_1___catalog_getBook__bookstore_searchBook = msgArray[curIdx++]; // NOCS
			Assert.assertTrue("Message is not a reply", return1_1___catalog_getBook__bookstore_searchBook instanceof SynchronousReplyMessage);
			Assert.assertEquals(return1_1___catalog_getBook__bookstore_searchBook.getSendingExecution(), this.exec1_1__catalog_getBook);
			Assert.assertEquals(return1_1___catalog_getBook__bookstore_searchBook.getReceivingExecution(), this.exec0_0__bookstore_searchBook);
			Assert.assertEquals("Message has wrong timestamp", return1_1___catalog_getBook__bookstore_searchBook.getTimestamp(),
					this.exec1_1__catalog_getBook.getTout());
		}
		{ /* 3.: [2,1].Call bookstore.searchBook(..)->crm.getOrders(..) */// NOCS
			final AbstractMessage call2_1___bookstore_searchBook__crm_getOrders = msgArray[curIdx++]; // NOCS
			Assert.assertTrue("Message is not a call", call2_1___bookstore_searchBook__crm_getOrders instanceof SynchronousCallMessage);
			Assert.assertEquals(call2_1___bookstore_searchBook__crm_getOrders.getSendingExecution(), this.exec0_0__bookstore_searchBook);
			Assert.assertEquals(call2_1___bookstore_searchBook__crm_getOrders.getReceivingExecution(), this.exec2_1__crm_getOrders);
			Assert.assertEquals("Message has wrong timestamp", call2_1___bookstore_searchBook__crm_getOrders.getTimestamp(), this.exec2_1__crm_getOrders.getTin());
		}
		{ /* 4.: [3,2].Call crm.getOrders(..)->catalog.getBook(..) */// NOCS
			final AbstractMessage call3_2___bookstore_searchBook__catalog_getBook = msgArray[curIdx++]; // NOCS
			Assert.assertTrue("Message is not a call", call3_2___bookstore_searchBook__catalog_getBook instanceof SynchronousCallMessage);
			Assert.assertEquals(call3_2___bookstore_searchBook__catalog_getBook.getSendingExecution(), this.exec2_1__crm_getOrders);
			Assert.assertEquals(call3_2___bookstore_searchBook__catalog_getBook.getReceivingExecution(), this.exec3_2__catalog_getBook);
			Assert.assertEquals("Message has wrong timestamp", call3_2___bookstore_searchBook__catalog_getBook.getTimestamp(),
					this.exec3_2__catalog_getBook.getTin());
		}
		{ /* 5.: [3,2].Return catalog.getBook(..)->crm.getOrders(..) */// NOCS
			final AbstractMessage return3_2___catalog_getBook__crm_getOrders = msgArray[curIdx++]; // NOCS
			Assert.assertTrue("Message is not a reply", return3_2___catalog_getBook__crm_getOrders instanceof SynchronousReplyMessage);
			Assert.assertEquals(return3_2___catalog_getBook__crm_getOrders.getSendingExecution(), this.exec3_2__catalog_getBook);
			Assert.assertEquals(return3_2___catalog_getBook__crm_getOrders.getReceivingExecution(), this.exec2_1__crm_getOrders);
			Assert.assertEquals("Message has wrong timestamp", return3_2___catalog_getBook__crm_getOrders.getTimestamp(), this.exec3_2__catalog_getBook.getTout());
		}
		{ /* 6.: [2,1].Return crm.getOrders(..)->bookstore.searchBook */// NOCS
			final AbstractMessage return2_1___crm_getOrders__bookstore_searchBook = msgArray[curIdx++]; // NOCS
			Assert.assertTrue("Message is not a reply", return2_1___crm_getOrders__bookstore_searchBook instanceof SynchronousReplyMessage);
			Assert.assertEquals(return2_1___crm_getOrders__bookstore_searchBook.getSendingExecution(), this.exec2_1__crm_getOrders);
			Assert.assertEquals(return2_1___crm_getOrders__bookstore_searchBook.getReceivingExecution(), this.exec0_0__bookstore_searchBook);
			Assert.assertEquals("Message has wrong timestamp", return2_1___crm_getOrders__bookstore_searchBook.getTimestamp(), this.exec2_1__crm_getOrders.getTout());
		}
	}

	/**
	 * Make sure that the transformation from an Execution Trace to a Message
	 * Trace is performed only once.
	 */
	@Test
	public void testMessageTraceTransformationOnlyOnce() {
		try {
			final ExecutionTrace executionTrace = this.genValidBookstoreTrace();
			/*
			 * Transform Execution Trace to Message Trace representation (twice)
			 * and make sure, that the instances are the same.
			 */
			final MessageTrace messageTrace1 = executionTrace.toMessageTrace(this.systemEntityFactory.getRootExecution());
			final MessageTrace messageTrace2 = executionTrace.toMessageTrace(this.systemEntityFactory.getRootExecution());
			Assert.assertSame(messageTrace1, messageTrace2);
		} catch (final InvalidTraceException ex) {
			TestExecutionTraceBookstore.LOG.error("InvalidTraceException", ex);
			Assert.fail(ex.getMessage());
			return;
		}
	}

	/**
	 * Make sure that the transformation from an Execution Trace to a Message
	 * Trace is performed only once.
	 */
	@Test
	public void testMessageTraceTransformationTwiceOnChange() {
		try {
			final ExecutionTrace executionTrace = this.genValidBookstoreTrace();

			final Execution exec4_1__catalog_getBook = this.eFactory // NOCS
					.genExecution("Catalog", "catalog", "getBook", TestExecutionTraceBookstore.TRACE_ID, 9, 10, 4, 1); // NOCS (MagicNumberCheck)
			final MessageTrace messageTrace1 = executionTrace.toMessageTrace(this.systemEntityFactory.getRootExecution());
			executionTrace.add(exec4_1__catalog_getBook);
			final MessageTrace messageTrace2 = executionTrace.toMessageTrace(this.systemEntityFactory.getRootExecution());
			Assert.assertNotSame(messageTrace1, messageTrace2);
		} catch (final InvalidTraceException ex) {
			TestExecutionTraceBookstore.LOG.error("InvalidTraceException", ex);
			Assert.fail(ex.getMessage());
			return;
		}
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
	 */
	private ExecutionTrace genBrokenBookstoreTraceEssSkip() throws InvalidTraceException {
		/*
		 * Create an Execution Trace and add Executions in
		 * arbitrary order
		 */
		final ExecutionTrace executionTrace = new ExecutionTrace(TestExecutionTraceBookstore.TRACE_ID);
		final Execution exec1_1__catalog_getBook__broken = this.eFactory.genExecution("Catalog", "catalog", "getBook", TestExecutionTraceBookstore.TRACE_ID, // NOCS
				2, 4, 1, 3); // NOCS
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
	 */
	@Test
	public void testMessageTraceTransformationBrokenTraceEssSkip() {
		final ExecutionTrace executionTrace;
		try {
			executionTrace = this.genBrokenBookstoreTraceEssSkip();
		} catch (final InvalidTraceException ex) {
			TestExecutionTraceBookstore.LOG.error("InvalidTraceException", ex);
			Assert.fail(ex.getMessage());
			return;
		}

		/**
		 * Transform Execution Trace to Message Trace representation
		 */
		try {
			/* The following call must throw an Exception in this test case */
			TestExecutionTraceBookstore.LOG.info("This test triggers a FATAL warning about an ess skip <0,3> which can simply be ignored because it is desired");
			executionTrace.toMessageTrace(this.systemEntityFactory.getRootExecution());
			Assert.fail("An invalid execution has been transformed to a message trace");
		} catch (final InvalidTraceException ex) { // NOPMD
			/* we wanted this exception to happen */
		}
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
	 */
	private ExecutionTrace genBrokenBookstoreTraceEoiSkip() throws InvalidTraceException {
		/*
		 * Create an Execution Trace and add Executions in
		 * arbitrary order
		 */
		final ExecutionTrace executionTrace = new ExecutionTrace(TestExecutionTraceBookstore.TRACE_ID);
		final Execution exec3_2__catalog_getBook__broken = this.eFactory.genExecution("Catalog", "catalog", "getBook", TestExecutionTraceBookstore.TRACE_ID, // NOCS
				6, 7, 4, 2); // NOCS (MagicNumberCheck)
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
	 */
	@Test
	public void testMessageTraceTransformationBrokenTraceEoiSkip() {
		/*
		 * Create an Execution Trace and add Executions in
		 * arbitrary order
		 */
		final ExecutionTrace executionTrace;
		try {
			executionTrace = this.genBrokenBookstoreTraceEoiSkip();
		} catch (final InvalidTraceException ex) {
			TestExecutionTraceBookstore.LOG.error("InvalidTraceException", ex);
			Assert.fail(ex.getMessage());
			return;
		}

		/**
		 * Transform Execution Trace to Message Trace representation
		 */
		try {
			/* The following call must throw an Exception in this test case */
			executionTrace.toMessageTrace(this.systemEntityFactory.getRootExecution());
			Assert.fail("An invalid execution has been transformed to a message trace");
		} catch (final InvalidTraceException ex) { // NOPMD
			/* we wanted this exception to happen */
		}

	}
}
