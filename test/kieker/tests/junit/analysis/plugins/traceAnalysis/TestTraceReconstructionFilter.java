package kieker.tests.junit.analysis.plugins.traceAnalysis;

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
import java.util.concurrent.atomic.AtomicReference;
import junit.framework.TestCase;
import kieker.analysis.datamodel.Execution;
import kieker.analysis.datamodel.ExecutionTrace;
import kieker.analysis.datamodel.InvalidExecutionTrace;
import kieker.analysis.datamodel.MessageTrace;
import kieker.analysis.datamodel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.traceAnalysis.traceReconstruction.InvalidTraceException;
import kieker.analysis.plugin.traceAnalysis.traceReconstruction.TraceReconstructionFilter;
import kieker.tests.junit.analysis.util.ExecutionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class TestTraceReconstructionFilter extends TestCase {

    private static final Log log = LogFactory.getLog(TestTraceReconstructionFilter.class);
    private final SystemModelRepository systemEntityFactory = new SystemModelRepository();
    private final ExecutionFactory eFactory = new ExecutionFactory(systemEntityFactory);

    /* Executions of a valid trace */
    private final Execution exec0_0__bookstore_searchBook;
    private final Execution exec1_1__catalog_getBook;
    private final Execution exec2_1__crm_getOrders;
    private final Execution exec3_2__catalog_getBook;
    private final long traceId = 62298l;
    private final long minTin;
    private final long maxTout;

    public TestTraceReconstructionFilter() {
        /* Manually create Executions for a trace */
        exec0_0__bookstore_searchBook = eFactory.genExecution(
                "Bookstore", "bookstore", "searchBook",
                traceId,
                1, // tin
                10, // tout
                0, 0);  // eoi, ess
        this.minTin = exec0_0__bookstore_searchBook.getTin();
        this.maxTout = exec0_0__bookstore_searchBook.getTout();

        exec1_1__catalog_getBook = eFactory.genExecution(
                "Catalog", "catalog", "getBook",
                traceId,
                2, // tin
                4, // tout
                1, 1);  // eoi, ess
        exec2_1__crm_getOrders = eFactory.genExecution(
                "CRM", "crm", "getOrders",
                traceId,
                5, // tin
                8, // tout
                2, 1);  // eoi, ess
        exec3_2__catalog_getBook = eFactory.genExecution(
                "Catalog", "catalog", "getBook",
                traceId,
                6, // tin
                7, // tout
                3, 2);  // eoi, ess
    }

    /**
     * Generates an execution trace representation of the "well-known" bookstore
     * trace.
     *
     * @return
     * @throws InvalidTraceException
     */
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

        try {
            /* Make sure that trace is valid: */
            executionTrace.toMessageTrace(this.systemEntityFactory.getRootExecution());
        } catch (InvalidTraceException ex) {
            log.error(ex);
            fail("Test invalid since used trace invalid");
            throw new InvalidTraceException("Test invalid since used trace invalid", ex);
        }

        return executionTrace;
    }

    /**
     * Tests whether a valid trace is correctly reconstructed and passed to the
     * right output port.
     */
    public void testValidBookstoreTracePassed() {
        TraceReconstructionFilter filter =
                new TraceReconstructionFilter(
                "TraceReconstructionFilter",
                systemEntityFactory,
                this.maxTout - this.minTin + 5, // maxTraceDurationMillis
                true); // ignoreInvalidTraces

        final AtomicReference<Boolean> receivedTheValidExecutionTrace =
                new AtomicReference<Boolean>(Boolean.FALSE);
        final AtomicReference<Boolean> receivedTheValidMessageTrace =
                new AtomicReference<Boolean>(Boolean.FALSE);
        final AtomicReference<Boolean> receivedAnInvalidExecutionTrace =
                new AtomicReference<Boolean>(Boolean.FALSE);

        /*
         * These are the trace representations we want to be reconstructed by
         * the filter
         */
        final ExecutionTrace validExecutionTrace;
        final MessageTrace validMessageTrace;
        try {
            validExecutionTrace = genValidBookstoreTrace();
            validMessageTrace = validExecutionTrace.toMessageTrace(this.systemEntityFactory.getRootExecution());
        } catch (InvalidTraceException ex) {
            log.error("InvalidTraceException", ex);
            fail("InvalidTraceException" + ex);
            return;
        }

        /*
         * Register a handler for reconstructed (valid) execution traces.
         * This handler MUST receive exactly this trace (and no other).
         */
        filter.getExecutionTraceOutputPort().subscribe(new AbstractInputPort<ExecutionTrace>("Execution traces") {

            public void newEvent(ExecutionTrace event) {
                if (event.equals(validExecutionTrace)) {
                    receivedTheValidExecutionTrace.set(Boolean.TRUE);
                }
                assertEquals(validExecutionTrace, event);
            }
        });

        /*
         * Register a handler for reconstructed (valid) message traces.
         * This handler MUST receive exactly this trace (and no other).
         */
        filter.getMessageTraceOutputPort().subscribe(new AbstractInputPort<MessageTrace>("Message traces") {

            public void newEvent(MessageTrace event) {
                if (event.equals(validMessageTrace)) {
                    receivedTheValidMessageTrace.set(Boolean.TRUE);
                }
                assertEquals(validMessageTrace, event);
            }
        });

        /*
         * Register a handler for invalid execution traces.
         * This handler MUST not be invoked.
         */
        filter.getInvalidExecutionTraceOutputPort().subscribe(new AbstractInputPort<InvalidExecutionTrace>("Invalid execution trace") {

            public void newEvent(InvalidExecutionTrace event) {
                receivedAnInvalidExecutionTrace.set(Boolean.TRUE);
                fail("Received an invalid execution trace" + event);
            }
        });

        if (!filter.execute()) {
            fail("Execution of filter failed");
            return;
        }

        /*
         * Pass executions of the trace to be reconstructed.
         */
        for (Execution curExec : validExecutionTrace.getTraceAsSortedExecutionSet()) {
            filter.getExecutionInputPort().newEvent(curExec);
        }

        filter.terminate(false);

        /* Analyse result of test case execution */
        if (!receivedTheValidExecutionTrace.get()) {
            fail("Execution trace didn't pass the filter");
        }
        if (!receivedTheValidMessageTrace.get()) {
            fail("Message trace didn't pass the filter");
        }
        if (receivedAnInvalidExecutionTrace.get()) {
            fail("Received invalid trace from filter");
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

        executionTrace.add(exec3_2__catalog_getBook);
        executionTrace.add(exec2_1__crm_getOrders);
        executionTrace.add(exec0_0__bookstore_searchBook);
        executionTrace.add(exec1_1__catalog_getBook__broken);

        return executionTrace;
    }

    /**
     * Tests whether a broken trace is correctly detected and passed to the
     * right output port.
     */
    public void testBrokenBookstoreTracePassed() {
        TraceReconstructionFilter filter =
                new TraceReconstructionFilter(
                "TraceReconstructionFilter",
                systemEntityFactory,
                this.maxTout - this.minTin + 5, // maxTraceDurationMillis
                true); // ignoreInvalidTraces

        final AtomicReference<Boolean> receivedValidExecutionTrace =
                new AtomicReference<Boolean>(Boolean.FALSE);
        final AtomicReference<Boolean> receivedValidMessageTrace =
                new AtomicReference<Boolean>(Boolean.FALSE);
        final AtomicReference<Boolean> receivedTheInvalidExecutionTrace =
                new AtomicReference<Boolean>(Boolean.FALSE);

        /*
         * These are the trace representations we want to be reconstructed by
         * the filter
         */
        final ExecutionTrace invalidExecutionTrace;
        try {
            invalidExecutionTrace = genBrokenBookstoreTraceEssSkip();
        } catch (InvalidTraceException ex) {
            log.error("InvalidTraceException", ex);
            fail("InvalidTraceException" + ex);
            return;
        }

        /*
         * Register a handler for reconstructed (valid) execution traces.
         * This handler MUST not be invoked.
         */
        filter.getExecutionTraceOutputPort().subscribe(new AbstractInputPort<ExecutionTrace>("Execution traces") {

            public void newEvent(ExecutionTrace event) {
                receivedValidExecutionTrace.set(Boolean.TRUE);
                fail("Received a valid execution trace" + event);
            }
        });

        /*
         * Register a handler for reconstructed (valid) message traces.
         * This handler MUST not be invoked.
         */
        filter.getMessageTraceOutputPort().subscribe(new AbstractInputPort<MessageTrace>("Message traces") {

            public void newEvent(MessageTrace event) {
                receivedValidMessageTrace.set(Boolean.TRUE);
                fail("Received a valid message trace" + event);
            }
        });

        /*
         * Register a handler for invalid execution traces.
         * This handler MUST receive exactly this trace (and no other).
         */
        filter.getInvalidExecutionTraceOutputPort().subscribe(new AbstractInputPort<InvalidExecutionTrace>("Invalid execution trace") {

            public void newEvent(InvalidExecutionTrace event) {
                if (event.getInvalidExecutionTrace().equals(invalidExecutionTrace)) {
                    receivedTheInvalidExecutionTrace.set(Boolean.TRUE);
                }
                assertEquals(invalidExecutionTrace, event.getInvalidExecutionTrace());
            }
        });

        if (!filter.execute()) {
            fail("Execution of filter failed");
            return;
        }

        /*
         * Pass executions of the trace to be reconstructed.
         */
        for (Execution curExec : invalidExecutionTrace.getTraceAsSortedExecutionSet()) {
            filter.getExecutionInputPort().newEvent(curExec);
        }

        filter.terminate(false);

        /* Analyse result of test case execution */
        if (receivedValidExecutionTrace.get()) {
            fail("A valid execution trace passed the filter");
        }
        if (receivedValidMessageTrace.get()) {
            fail("A message trace passed the filter");
        }
        if (!receivedTheInvalidExecutionTrace.get()) {
            fail("Invalid trace didn't pass the filter");
        }
    }

    /**
     * Generates an incomplete execution trace representation of the "well-known"
     * bookstore trace. The outer bookstore.searchBook(..) execution with eoi/ess
     * 0/0 is
     *
     * @return
     * @throws InvalidTraceException
     */
    private ExecutionTrace genBookstoreTraceWithoutEntryExecution() throws InvalidTraceException {
        /*
         * Create an Execution Trace and add Executions in
         * arbitrary order
         */
        ExecutionTrace executionTrace = new ExecutionTrace(traceId);

        executionTrace.add(exec3_2__catalog_getBook);
        executionTrace.add(exec2_1__crm_getOrders);
        executionTrace.add(exec1_1__catalog_getBook);

        return executionTrace;
    }

    /**
     * Tests the timeout of pending traces.
     */
    public void testBrokenTraceDueToTimeout() {

        final AtomicReference<Boolean> receivedValidExecutionTrace =
                new AtomicReference<Boolean>(Boolean.FALSE);
        final AtomicReference<Boolean> receivedValidMessageTrace =
                new AtomicReference<Boolean>(Boolean.FALSE);
        final AtomicReference<Boolean> receivedTheInvalidExecutionTrace =
                new AtomicReference<Boolean>(Boolean.FALSE);

        /*
         * This trace is incomplete and timeout.
         */
        final ExecutionTrace invalidExecutionTrace;
        try {
            invalidExecutionTrace = genBookstoreTraceWithoutEntryExecution();
        } catch (InvalidTraceException ex) {
            log.error("InvalidTraceException", ex);
            fail("InvalidTraceException" + ex);
            return;
        }

        final int TIMEOUT_MARGIN_MILLIS = 1;
        TraceReconstructionFilter filter =
                new TraceReconstructionFilter(
                "TraceReconstructionFilter",
                systemEntityFactory,
                (invalidExecutionTrace.getDurationInNanos() / (1000 * 1000)) + TIMEOUT_MARGIN_MILLIS, // maxTraceDurationMillis
                true); // ignoreInvalidTraces
         assertTrue("Test invalid",
                 invalidExecutionTrace.getDurationInNanos()>filter.getMaxTraceDurationNanos());

        /*
         * We will use this execution to trigger the timeout check for
         * pending traces within the filter.
         */
        final long triggerTraceId = traceId + 1;
        final Execution exec0_0__bookstore_searchBook__trigger = eFactory.genExecution(
                "Bookstore", "bookstore", "searchBook",
                triggerTraceId,
                invalidExecutionTrace.getMaxTout(), // tin
                invalidExecutionTrace.getMaxTout() + (TIMEOUT_MARGIN_MILLIS * (1000 * 1000)), // tout
                0, 0);  // eoi, ess
        final ExecutionTrace triggerTrace =
                new ExecutionTrace(triggerTraceId);
        try {
            triggerTrace.add(exec0_0__bookstore_searchBook__trigger);
        } catch (InvalidTraceException ex) {
            log.error("InvalidTraceException", ex);
            fail("InvalidTraceException" + ex);
            return;
        }

        /*
         * Register a handler for reconstructed (valid) execution traces.
         * This handler MUST not be invoked.
         */
        filter.getExecutionTraceOutputPort().subscribe(new AbstractInputPort<ExecutionTrace>("Execution traces") {

            public void newEvent(ExecutionTrace event) {
                receivedValidExecutionTrace.set(Boolean.TRUE);
                fail("Received a valid execution trace" + event);
            }
        });

        /*
         * Register a handler for reconstructed (valid) message traces.
         * This handler MUST not be invoked.
         */
        filter.getMessageTraceOutputPort().subscribe(new AbstractInputPort<MessageTrace>("Message traces") {

            public void newEvent(MessageTrace event) {
                receivedValidMessageTrace.set(Boolean.TRUE);
                fail("Received a valid message trace" + event);
            }
        });

        /*
         * Register a handler for invalid execution traces.
         * This handler MUST receive exactly this trace (and no other).
         */
        filter.getInvalidExecutionTraceOutputPort().subscribe(new AbstractInputPort<InvalidExecutionTrace>("Invalid execution trace") {

            public void newEvent(InvalidExecutionTrace event) {
                if (event.getInvalidExecutionTrace().equals(invalidExecutionTrace)) {
                    receivedTheInvalidExecutionTrace.set(Boolean.TRUE);
                }
                assertEquals(invalidExecutionTrace, event.getInvalidExecutionTrace());
            }
        });

        if (!filter.execute()) {
            fail("Execution of filter failed");
            return;
        }

        /*
         * Pass all executions but the [0,0] execution of the trace to be reconstructed.
         */
        for (Execution curExec : invalidExecutionTrace.getTraceAsSortedExecutionSet()) {
            if (curExec.equals(exec0_0__bookstore_searchBook)){
                continue; // skip this execution to have a chance to add our "trigger execution"
            }
            filter.getExecutionInputPort().newEvent(curExec);
        }
        /**
         * Pass the timeout "trigger execution"
         */
        filter.getExecutionInputPort().newEvent(exec0_0__bookstore_searchBook__trigger);

        filter.terminate(false);

        /* Analyse result of test case execution */
        if (receivedValidExecutionTrace.get()) {
            fail("A valid execution trace passed the filter");
        }
        if (receivedValidMessageTrace.get()) {
            fail("A message trace passed the filter");
        }
        if (!receivedTheInvalidExecutionTrace.get()) {
            fail("Invalid trace didn't pass the filter");
        }
    }
}
