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
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import kieker.analysis.datamodel.Execution;
import kieker.analysis.datamodel.ExecutionTrace;
import kieker.analysis.datamodel.Message;
import kieker.analysis.datamodel.MessageTrace;
import kieker.analysis.datamodel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.traceAnalysis.executionFilter.TraceIdFilter;
import kieker.analysis.plugin.traceAnalysis.traceReconstruction.InvalidTraceException;
import kieker.tests.junit.analysis.plugins.traceAnalysis.util.ExecutionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class TestExecutionTrace extends TestCase {

    private static final Log log = LogFactory.getLog(TestExecutionTrace.class);

    private final SystemModelRepository systemEntityFactory = new SystemModelRepository();
    private final ExecutionFactory eFactory = new ExecutionFactory(systemEntityFactory);

    /**
     * Tests whether the "well-known" Bookstore trace gets correctly
     * represented as an Execution Trace and subsequently transformed
     * into a Message Trace representation.
     */
    public void testMessageTraceTransformationValidTrace() {
        final long traceId = 69898l;
        int numExecutions = 0;

        /* (1) Manually create Executions of the trace */
        numExecutions++;
        final long minTin = 1;
        final long maxTout = 10;
        final Execution exec0_0__bookstore_searchBook = eFactory.genExecution(
                "Bookstore", "bookstore", "searchBook",
                traceId,
                minTin,      // tin
                maxTout,     // tout
                0, 0);  // eoi, ess
        numExecutions++;
        final Execution exec1_1__catalog_getBook = eFactory.genExecution(
                "Catalog", "catalog", "getBook",
                traceId,
                2,      // tin
                4,     // tout
                1, 1);  // eoi, ess
        numExecutions++;
        final Execution exec2_1__crm_getOrders = eFactory.genExecution(
                "CRM", "crm", "getOrders",
                traceId,
                5,      // tin
                8,     // tout
                2, 1);  // eoi, ess
        numExecutions++;
        final Execution exec3_2__catalog_getBook = eFactory.genExecution(
                "Catalog", "catalog", "getBook",
                traceId,
                6,      // tin
                7,     // tout
                3, 2);  // eoi, ess

        /*
         * (2) Create the Execution Trace and add Executions in
         *     arbitrary order */
        final ExecutionTrace executionTrace =
                new ExecutionTrace(traceId);
        try {
            executionTrace.add(exec3_2__catalog_getBook);
            executionTrace.add(exec2_1__crm_getOrders);
            executionTrace.add(exec0_0__bookstore_searchBook);
            executionTrace.add(exec1_1__catalog_getBook);
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

        /**
         * (3) Transform Execution Trace to Message Trace representation
         */
        MessageTrace messageTrace;
        try {
            messageTrace = executionTrace.toMessageTrace(systemEntityFactory.getRootExecution());
        } catch (InvalidTraceException ex) {
            log.error("InvalidTraceException", ex);
            fail("Transformation to Message Trace failed: " + ex.getMessage());
            return;
        }

        /**
         * (4) Validate Message Trace representation.
         */
        assertEquals("Invalid traceId", messageTrace.getTraceId(), traceId);
        Vector<Message> msgVector = messageTrace.getSequenceAsVector();
        assertEquals("Invalid number of messages in trace", msgVector.size(), numExecutions*2);
        Message[] msgArray = msgVector.toArray(new Message[0]);
        assertEquals(msgArray.length, numExecutions*2);

        /* 1. $->bookstore.search */
        int curIdx = 0;
        Message call0_0___root__bookstore_searchBook
                = msgArray[curIdx++];
        assertEquals("Sending execution is not root execution",
                call0_0___root__bookstore_searchBook.getSendingExecution(), this.systemEntityFactory.getRootExecution());
        assertEquals(call0_0___root__bookstore_searchBook.getReceivingExecution(), exec0_0__bookstore_searchBook);
    }
}