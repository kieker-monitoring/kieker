package kieker.tests.junit.analysis.datamodel;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
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
 * ==================================================
 */
import java.util.Vector;
import junit.framework.TestCase;
import kieker.analysis.datamodel.Execution;
import kieker.analysis.datamodel.ExecutionTrace;
import kieker.analysis.datamodel.Message;
import kieker.analysis.datamodel.MessageTrace;
import kieker.analysis.datamodel.SynchronousCallMessage;
import kieker.analysis.datamodel.SynchronousReplyMessage;
import kieker.analysis.datamodel.repository.SystemModelRepository;
import kieker.analysis.plugin.traceAnalysis.traceReconstruction.InvalidTraceException;
import kieker.tests.junit.analysis.plugins.traceAnalysis.util.ExecutionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class TestExecutionTraceBookstore extends TestCase {

    private static final Log log = LogFactory.getLog(TestExecutionTraceBookstore.class);
    private final SystemModelRepository systemEntityFactory = new SystemModelRepository();
    private final ExecutionFactory eFactory = new ExecutionFactory(systemEntityFactory);
    private final long traceId = 69898l;
    private final long minTin = 1;
    private final long maxTout = 10;
    private final int numExecutions;

    /* Executions of a valid trace */
    private final Execution exec0_0__bookstore_searchBook;
    private final Execution exec1_1__catalog_getBook;
    private final Execution exec2_1__crm_getOrders;
    private final Execution exec3_2__catalog_getBook;

    public TestExecutionTraceBookstore() {
        int numExecutions_l = 0;

        /* Manually create Executions for a trace */
        numExecutions_l++;
        exec0_0__bookstore_searchBook = eFactory.genExecution(
                "Bookstore", "bookstore", "searchBook",
                traceId,
                minTin, // tin
                maxTout, // tout
                0, 0);  // eoi, ess

        numExecutions_l++;
        exec1_1__catalog_getBook = eFactory.genExecution(
                "Catalog", "catalog", "getBook",
                traceId,
                2, // tin
                4, // tout
                1, 1);  // eoi, ess
        numExecutions_l++;
        exec2_1__crm_getOrders = eFactory.genExecution(
                "CRM", "crm", "getOrders",
                traceId,
                5, // tin
                8, // tout
                2, 1);  // eoi, ess
        numExecutions_l++;
        exec3_2__catalog_getBook = eFactory.genExecution(
                "Catalog", "catalog", "getBook",
                traceId,
                6, // tin
                7, // tout
                3, 2);  // eoi, ess
        this.numExecutions = numExecutions_l;
    }

    private ExecutionTrace genValidBookstoreTrace() throws InvalidTraceException {
        /*
         * Create an Execution Trace and add Executions in
         * arbitrary order
         */
        ExecutionTrace executionTrace = new ExecutionTrace(traceId);

        executionTrace.add(exec3_2__catalog_getBook);
        executionTrace.add(exec2_1__crm_getOrders);
        executionTrace.add(exec0_0__bookstore_searchBook);
        executionTrace.add(exec1_1__catalog_getBook);

        return executionTrace;
    }

    /**
     * Tests whether the "well-known" Bookstore trace gets correctly
     * represented as an Execution Trace.
     */
    public void testValidExecutionTrace() {
        try {
            ExecutionTrace executionTrace = genValidBookstoreTrace();
            /* Perform some validity checks on the execution trace object */
            assertEquals("Invalid length of Execution Trace", executionTrace.getLength(), numExecutions);
            assertEquals("Invalid maximum stack depth", executionTrace.getMaxStackDepth(), 2);
            assertEquals("Invalid minimum tin timestamp", executionTrace.getMinTin(), minTin);
            assertEquals("Invalid maximum tout timestamp", executionTrace.getMaxTout(), maxTout);

        } catch (InvalidTraceException ex) {
            log.error("InvalidTraceException", ex);
            fail(ex.getMessage());
            return;
        }
    }

    /**
     * Tests whether the "well-known" Bookstore trace can be correctly transformed
     * from an Execution Trace representation into a Message Trace representation.
     */
    public void testMessageTraceTransformationValidTrace() {

        ExecutionTrace executionTrace;
        try {
            executionTrace = genValidBookstoreTrace();
        } catch (InvalidTraceException ex) {
            log.error("InvalidTraceException", ex);
            fail(ex.getMessage());
            return;
        }

        /*
         * Transform Execution Trace to Message Trace representation
         */
        MessageTrace messageTrace;
        try {
            messageTrace = executionTrace.toMessageTrace(systemEntityFactory.getRootExecution());
        } catch (InvalidTraceException ex) {
            log.error("InvalidTraceException", ex);
            fail("Transformation to Message Trace failed: " + ex.getMessage());
            return;
        }

        /*
         * Validate Message Trace representation.
         */
        assertEquals("Invalid traceId", messageTrace.getTraceId(), traceId);
        Vector<Message> msgVector = messageTrace.getSequenceAsVector();
        assertEquals("Invalid number of messages in trace", msgVector.size(), numExecutions * 2);
        Message[] msgArray = msgVector.toArray(new Message[0]);
        assertEquals(msgArray.length, numExecutions * 2);

        int curIdx = 0;
        { /* 1.: [0,0].Call $->bookstore.searchBook(..) */
            Message call0_0___root__bookstore_searchBook = msgArray[curIdx++];
            assertTrue("Message is not a call", call0_0___root__bookstore_searchBook instanceof SynchronousCallMessage);
            assertEquals("Sending execution is not root execution",
                    call0_0___root__bookstore_searchBook.getSendingExecution(), this.systemEntityFactory.getRootExecution());
            assertEquals(call0_0___root__bookstore_searchBook.getReceivingExecution(), exec0_0__bookstore_searchBook);
            assertEquals("Message has wrong timestamp", call0_0___root__bookstore_searchBook.getTimestamp(), exec0_0__bookstore_searchBook.getTin());
        }
        { /* 2.: [1,1].Call bookstore.searchBook(..)->catalog.getBook(..) */
            Message call1_1___bookstore_searchBook_catalog_getBook = msgArray[curIdx++];
            assertTrue("Message is not a call",
                    call1_1___bookstore_searchBook_catalog_getBook instanceof SynchronousCallMessage);
            assertEquals(call1_1___bookstore_searchBook_catalog_getBook.getSendingExecution(), exec0_0__bookstore_searchBook);
            assertEquals(call1_1___bookstore_searchBook_catalog_getBook.getReceivingExecution(), exec1_1__catalog_getBook);
            assertEquals("Message has wrong timestamp",
                    call1_1___bookstore_searchBook_catalog_getBook.getTimestamp(),
                    exec1_1__catalog_getBook.getTin());
        }
        { /* 2.: [1,1].Return catalog.getBook(..)->bookstore.searchBook(..) */
            Message return1_1___catalog_getBook__bookstore_searchBook = msgArray[curIdx++];
            assertTrue("Message is not a reply",
                    return1_1___catalog_getBook__bookstore_searchBook instanceof SynchronousReplyMessage);
            assertEquals(return1_1___catalog_getBook__bookstore_searchBook.getSendingExecution(), exec1_1__catalog_getBook);
            assertEquals(return1_1___catalog_getBook__bookstore_searchBook.getReceivingExecution(), exec0_0__bookstore_searchBook);
            assertEquals("Message has wrong timestamp",
                    return1_1___catalog_getBook__bookstore_searchBook.getTimestamp(),
                    exec1_1__catalog_getBook.getTout());
        }
        { /* 3.: [2,1].Call bookstore.searchBook(..)->crm.getOrders(..) */
            Message call2_1___bookstore_searchBook__crm_getOrders = msgArray[curIdx++];
            assertTrue("Message is not a call",
                    call2_1___bookstore_searchBook__crm_getOrders instanceof SynchronousCallMessage);
            assertEquals(call2_1___bookstore_searchBook__crm_getOrders.getSendingExecution(), exec0_0__bookstore_searchBook);
            assertEquals(call2_1___bookstore_searchBook__crm_getOrders.getReceivingExecution(), exec2_1__crm_getOrders);
            assertEquals("Message has wrong timestamp",
                    call2_1___bookstore_searchBook__crm_getOrders.getTimestamp(),
                    exec2_1__crm_getOrders.getTin());
        }
        { /* 4.: [3,2].Call crm.getOrders(..)->catalog.getBook(..) */
            Message call3_2___bookstore_searchBook__catalog_getBook = msgArray[curIdx++];
            assertTrue("Message is not a call",
                    call3_2___bookstore_searchBook__catalog_getBook instanceof SynchronousCallMessage);
            assertEquals(call3_2___bookstore_searchBook__catalog_getBook.getSendingExecution(), exec2_1__crm_getOrders);
            assertEquals(call3_2___bookstore_searchBook__catalog_getBook.getReceivingExecution(), exec3_2__catalog_getBook);
            assertEquals("Message has wrong timestamp",
                    call3_2___bookstore_searchBook__catalog_getBook.getTimestamp(),
                    exec3_2__catalog_getBook.getTin());
        }
        { /* 5.: [3,2].Return catalog.getBook(..)->crm.getOrders(..) */
            Message return3_2___catalog_getBook__crm_getOrders = msgArray[curIdx++];
            assertTrue("Message is not a reply",
                    return3_2___catalog_getBook__crm_getOrders instanceof SynchronousReplyMessage);
            assertEquals(return3_2___catalog_getBook__crm_getOrders.getSendingExecution(), exec3_2__catalog_getBook);
            assertEquals(return3_2___catalog_getBook__crm_getOrders.getReceivingExecution(), exec2_1__crm_getOrders);
            assertEquals("Message has wrong timestamp",
                    return3_2___catalog_getBook__crm_getOrders.getTimestamp(),
                    exec3_2__catalog_getBook.getTout());
        }
        { /* 6.: [2,1].Return crm.getOrders(..)->bookstore.searchBook */
            Message return2_1___crm_getOrders__bookstore_searchBook = msgArray[curIdx++];
            assertTrue("Message is not a reply",
                    return2_1___crm_getOrders__bookstore_searchBook instanceof SynchronousReplyMessage);
            assertEquals(return2_1___crm_getOrders__bookstore_searchBook.getSendingExecution(), exec2_1__crm_getOrders);
            assertEquals(return2_1___crm_getOrders__bookstore_searchBook.getReceivingExecution(), exec0_0__bookstore_searchBook);
            assertEquals("Message has wrong timestamp",
                    return2_1___crm_getOrders__bookstore_searchBook.getTimestamp(),
                    exec2_1__crm_getOrders.getTout());
        }
    }

    /**
     * Make sure that the transformation from an Execution Trace to a Message
     * Trace is performed only once.
     */
    public void testMessageTraceTransformationOnlyOnce() {
        try {
            ExecutionTrace executionTrace = genValidBookstoreTrace();
            /*
             * Transform Execution Trace to Message Trace representation (twice)
             * and make sure, that the instances are the same.
             */
            MessageTrace messageTrace1 = executionTrace.toMessageTrace(systemEntityFactory.getRootExecution());
            MessageTrace messageTrace2 = executionTrace.toMessageTrace(systemEntityFactory.getRootExecution());
            assertSame(messageTrace1, messageTrace2);
        } catch (InvalidTraceException ex) {
            log.error("InvalidTraceException", ex);
            fail(ex.getMessage());
            return;
        }
    }

    /**
     * Make sure that the transformation from an Execution Trace to a Message
     * Trace is performed only once.
     */
    public void testMessageTraceTransformationTwiceOnChange() {
        try {
            ExecutionTrace executionTrace = genValidBookstoreTrace();

            final Execution exec4_1__catalog_getBook = eFactory.genExecution(
                    "Catalog", "catalog", "getBook",
                    traceId,
                    9, // tin
                    10, // tout
                    4, 1);  // eoi, ess
            MessageTrace messageTrace1 = executionTrace.toMessageTrace(systemEntityFactory.getRootExecution());
            executionTrace.add(exec4_1__catalog_getBook);
            MessageTrace messageTrace2 = executionTrace.toMessageTrace(systemEntityFactory.getRootExecution());
            assertNotSame(messageTrace1, messageTrace2);
        } catch (InvalidTraceException ex) {
            log.error("InvalidTraceException", ex);
            fail(ex.getMessage());
            return;
        }
    }

    /**
     * Assert that the transformation of a broken execution trace version of the
     * "well-known" Bookstore trace leads to an exception.
     *
     * The trace is broken in that the eoi/ess values of an execution with eoi/ess
     * [1,1] are replaced by the eoi/ess values [1,3]. Since ess values must only
     * increment/decrement by 1, this test must lead to an exception.
     */
    public void testMessageTraceTransformationBrokenTraceEssSkip() {
        /*
         * Create an Execution Trace and add Executions in
         * arbitrary order
         */
        final ExecutionTrace executionTrace =
                new ExecutionTrace(traceId);
        final Execution exec1_1__catalog_getBook__broken =
                eFactory.genExecution(
                "Catalog", "catalog", "getBook",
                traceId,
                2, // tin
                4, // tout
                1, 3);  // eoi, ess
        assertFalse("Invalid test", exec1_1__catalog_getBook__broken.equals(exec1_1__catalog_getBook));

        try {
            executionTrace.add(exec3_2__catalog_getBook);
            executionTrace.add(exec2_1__crm_getOrders);
            executionTrace.add(exec0_0__bookstore_searchBook);
            executionTrace.add(exec1_1__catalog_getBook__broken);

        } catch (InvalidTraceException ex) {
            log.error("InvalidTraceException", ex);
            fail(ex.getMessage());
            return;
        }

        /**
         * Transform Execution Trace to Message Trace representation
         */
        try {
            /* The following call must throw an Exception in this test case */
            executionTrace.toMessageTrace(systemEntityFactory.getRootExecution());
            fail("An invalid execution has been transformed to a message trace");
        } catch (InvalidTraceException ex) {
            /* we wanted this exception to happen */
        }

    }

    /**
     * Assert that the transformation of a broken execution trace version of the
     * "well-known" Bookstore trace leads to an exception.
     *
     * The trace is broken in that the eoi/ess values of an execution with eoi/ess
     * [3,2] are replaced by the eoi/ess values [4,2]. Since eoi values must only
     * increment by 1, this test must lead to an exception.
     */
    public void testMessageTraceTransformationBrokenTraceEoiSkip() {
        /*
         * Create an Execution Trace and add Executions in
         * arbitrary order
         */
        final ExecutionTrace executionTrace =
                new ExecutionTrace(traceId);
        final Execution exec3_2__catalog_getBook__broken = eFactory.genExecution(
                "Catalog", "catalog", "getBook",
                traceId,
                6, // tin
                7, // tout
                4, 2);  // eoi, ess
        assertFalse("Invalid test", exec3_2__catalog_getBook__broken.equals(exec3_2__catalog_getBook));

        try {
            //executionTrace.add(exec3_2__catalog_getBook);
            executionTrace.add(exec3_2__catalog_getBook__broken);
            executionTrace.add(exec2_1__crm_getOrders);
            executionTrace.add(exec0_0__bookstore_searchBook);
            executionTrace.add(exec1_1__catalog_getBook);

        } catch (InvalidTraceException ex) {
            log.error("InvalidTraceException", ex);
            fail(ex.getMessage());
            return;
        }

        /**
         * Transform Execution Trace to Message Trace representation
         */
        try {
            /* The following call must throw an Exception in this test case */
            executionTrace.toMessageTrace(systemEntityFactory.getRootExecution());
            fail("An invalid execution has been transformed to a message trace");
        } catch (InvalidTraceException ex) {
            /* we wanted this exception to happen */
        }

    }
}
