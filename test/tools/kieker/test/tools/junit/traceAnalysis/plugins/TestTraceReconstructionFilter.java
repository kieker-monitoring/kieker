package kieker.test.tools.junit.traceAnalysis.plugins;

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
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.InvalidExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.tools.traceAnalysis.plugins.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.plugins.traceReconstruction.TraceReconstructionFilter;
import kieker.test.tools.junit.traceAnalysis.util.ExecutionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class TestTraceReconstructionFilter extends TestCase {

    private static final Log log = LogFactory.getLog(TestTraceReconstructionFilter.class);
    private final SystemModelRepository systemEntityFactory = new SystemModelRepository();
    private final ExecutionFactory executionFactory = new ExecutionFactory(systemEntityFactory);

    /* Executions of a valid trace */
    private final Execution exec0_0__bookstore_searchBook;
    private final Execution exec1_1__catalog_getBook;
    private final Execution exec2_1__crm_getOrders;
    private final Execution exec3_2__catalog_getBook;
    private final long traceId = 62298l;

    public TestTraceReconstructionFilter() {
        /* Manually create Executions for a trace */
        exec0_0__bookstore_searchBook = executionFactory.genExecution(
                "Bookstore", "bookstore", "searchBook",
                traceId,
                1 * (1000 * 1000), // tin
                10 * (1000 * 1000), // tout
                0, 0);  // eoi, ess

        exec1_1__catalog_getBook = executionFactory.genExecution(
                "Catalog", "catalog", "getBook",
                traceId,
                2 * (1000 * 1000), // tin
                4 * (1000 * 1000), // tout
                1, 1);  // eoi, ess
        exec2_1__crm_getOrders = executionFactory.genExecution(
                "CRM", "crm", "getOrders",
                traceId,
                5 * (1000 * 1000), // tin
                8 * (1000 * 1000), // tout
                2, 1);  // eoi, ess
        exec3_2__catalog_getBook = executionFactory.genExecution(
                "Catalog", "catalog", "getBook",
                traceId,
                6 * (1000 * 1000), // tin
                7 * (1000 * 1000), // tout
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

        TraceReconstructionFilter filter =
                new TraceReconstructionFilter(
                "TraceReconstructionFilter",
                systemEntityFactory,
                TraceReconstructionFilter.MAX_DURATION_MILLIS, // maxTraceDurationMillis
                true); // ignoreInvalidTraces
        assertTrue("Test invalid since trace length smaller than filter timeout",
                validExecutionTrace.getDurationInNanos() <= filter.getMaxTraceDurationNanos());

        /*
         * Register a handler for reconstructed (valid) execution traces.
         * This handler MUST receive exactly this trace (and no other).
         */
        filter.getExecutionTraceOutputPort().subscribe(new AbstractInputPort<ExecutionTrace>("Execution traces") {

            public void newEvent(ExecutionTrace event) {
                if (event.equals(validExecutionTrace)) {
                    receivedTheValidExecutionTrace.set(Boolean.TRUE);
                }
                assertEquals("Unexpected execution trace",
                        validExecutionTrace, event);
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
                assertEquals("Unexpected message trace",
                        validMessageTrace, event);
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
     * trace.
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
                executionFactory.genExecution(
                "Catalog", "catalog", "getBook",
                traceId,
                2 * (1000 * 1000), // tin
                4 * (1000 * 1000), // tout
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

        TraceReconstructionFilter filter =
                new TraceReconstructionFilter(
                "TraceReconstructionFilter",
                systemEntityFactory,
                TraceReconstructionFilter.MAX_DURATION_MILLIS, // maxTraceDurationMillis
                true); // ignoreInvalidTraces
        assertTrue("Test invalid since trace length smaller than filter timeout",
                invalidExecutionTrace.getDurationInNanos() <= filter.getMaxTraceDurationNanos());

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
                if (event.getInvalidExecutionTraceArtifacts().equals(invalidExecutionTrace)) {
                    receivedTheInvalidExecutionTrace.set(Boolean.TRUE);
                }
                assertEquals("Unexpected invalid execution trace",
                        invalidExecutionTrace, event.getInvalidExecutionTraceArtifacts());
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
     * 0/0 is missing.
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
     * Tests the timeout of pending (incomplete) traces.
     * A corresponding test for a valid trace is not required.
     */
    public void testIncompleteTraceDueToTimeout() {

        final AtomicReference<Boolean> receivedTheValidTriggerExecutionTrace =
                new AtomicReference<Boolean>(Boolean.FALSE);
        final AtomicReference<Boolean> receivedTheValidTriggerMessageTrace =
                new AtomicReference<Boolean>(Boolean.FALSE);
        final AtomicReference<Boolean> receivedTheIncompleteExecutionTraceArtifact =
                new AtomicReference<Boolean>(Boolean.FALSE);
        final AtomicReference<Boolean> receivedTheCompletingExecutionTraceArtifact =
                new AtomicReference<Boolean>(Boolean.FALSE);

        /*
         * This trace is incomplete.
         */
        final ExecutionTrace incompleteExecutionTrace;
        try {
            incompleteExecutionTrace = genBookstoreTraceWithoutEntryExecution();
        } catch (InvalidTraceException ex) {
            log.error("InvalidTraceException", ex);
            fail("InvalidTraceException" + ex);
            return;
        }

        /**
         * We will now create a trace that contains an execution which
         * would make the incomplete trace complete.
         *
         * But: Then, it would exceed the maximum trace duration.
         */
        final ExecutionTrace completingExecutionTrace =
                new ExecutionTrace(incompleteExecutionTrace.getTraceId());
        assertTrue("Test invalid (traceIds not matching)",
                exec0_0__bookstore_searchBook.getTraceId() == completingExecutionTrace.getTraceId());
        try {
            completingExecutionTrace.add(exec0_0__bookstore_searchBook);
        } catch (InvalidTraceException ex) {
            log.error("InvalidTraceException", ex);
            fail("InvalidTraceException" + ex);
            return;
        }

        /*
         * We will use this execution to trigger the timeout check for
         * pending traces within the filter.
         */
        final int TRIGGER_TRACE_LENGTH_MILLIS = 1;
        final long triggerTraceId = traceId + 1;
        final Execution exec0_0__bookstore_searchBook__trigger =
                executionFactory.genExecution(
                "Bookstore", "bookstore", "searchBook",
                triggerTraceId,
                incompleteExecutionTrace.getMaxTout(), // tin
                incompleteExecutionTrace.getMaxTout() + TRIGGER_TRACE_LENGTH_MILLIS * (1000 * 1000), // tout
                0, 0);  // eoi, ess
        final ExecutionTrace triggerExecutionTrace =
                new ExecutionTrace(triggerTraceId);
        final MessageTrace triggerMessageTrace;
        try {
            triggerExecutionTrace.add(exec0_0__bookstore_searchBook__trigger);
            triggerMessageTrace = triggerExecutionTrace.toMessageTrace(systemEntityFactory.getRootExecution());
        } catch (InvalidTraceException ex) {
            log.error("InvalidTraceException", ex);
            fail("InvalidTraceException" + ex);
            return;
        }

        /**
         * Instantiate reconstruction filter with timeout.
         */
        TraceReconstructionFilter filter =
                new TraceReconstructionFilter(
                "TraceReconstructionFilter",
                systemEntityFactory,
                /* Force timeout on reception of trigger execution */
                (triggerExecutionTrace.getMaxTout() - incompleteExecutionTrace.getMinTin()) / (1000 * 1000) - 1, // maxTraceDurationMillis
                true); // ignoreInvalidTraces

        assertTrue("Test invalid: NOT (tout of trigger trace - tin of incomplete > filter max. duration)\n"
                + "triggerExecutionTrace.getMaxTout()" + triggerExecutionTrace.getMaxTout() + "\n"
                + "incompleteExecutionTrace.getMinTin()" + incompleteExecutionTrace.getMinTin() + "\n"
                + "filter.getMaxTraceDurationNanos()" + filter.getMaxTraceDurationNanos(),
                triggerExecutionTrace.getMaxTout() - incompleteExecutionTrace.getMinTin() > filter.getMaxTraceDurationNanos());

        /*
         * Register a handler for reconstructed (valid) execution traces.
         * This handler MUST not be invoked.
         */
        filter.getExecutionTraceOutputPort().subscribe(new AbstractInputPort<ExecutionTrace>("Execution traces") {

            public void newEvent(ExecutionTrace event) {
                if (event.equals(triggerExecutionTrace)) {
                    receivedTheValidTriggerExecutionTrace.set(Boolean.TRUE);
                } else {
                    // TODO: for consistency reasons we would need to set an additional flag here
                    fail("Received an unexpected valid execution trace " + event);
                }
            }
        });

        /*
         * Register a handler for reconstructed (valid) message traces.
         * This handler MUST not be invoked.
         */
        filter.getMessageTraceOutputPort().subscribe(new AbstractInputPort<MessageTrace>("Message traces") {

            public void newEvent(MessageTrace event) {
                if (event.equals(triggerMessageTrace)) {
                    receivedTheValidTriggerMessageTrace.set(Boolean.TRUE);
                } else {
                    // TODO: for consistency reasons we would need to set an additional flag here
                    fail("Received an unexpected message trace " + event);
                }
            }
        });

        /*
         * Register a handler for invalid execution traces.
         * This handler MUST receive exactly this trace (and no other).
         */
        filter.getInvalidExecutionTraceOutputPort().subscribe(new AbstractInputPort<InvalidExecutionTrace>("Invalid execution trace") {

            public void newEvent(InvalidExecutionTrace event) {
                if (event.getInvalidExecutionTraceArtifacts().equals(incompleteExecutionTrace)) {
                    receivedTheIncompleteExecutionTraceArtifact.set(Boolean.TRUE);
                } else if (event.getInvalidExecutionTraceArtifacts().equals(completingExecutionTrace)) {
                    receivedTheCompletingExecutionTraceArtifact.set(Boolean.TRUE);
                } else {
                    fail("Received an unexpected invalid execution trace: " + event);
                }
            }
        });

        if (!filter.execute()) {
            fail("Execution of filter failed");
            return;
        }

        /*
         * Pass the executions of the incomplete trace intended to time out
         */
        for (Execution curExec : incompleteExecutionTrace.getTraceAsSortedExecutionSet()) {
            filter.getExecutionInputPort().newEvent(curExec);
        }

        /**
         * Pass the timeout "trigger execution"
         */
        filter.getExecutionInputPort().newEvent(exec0_0__bookstore_searchBook__trigger);

        /**
         * Now, will pass the execution that would make the incomplete trace
         * complete. But that incomplete trace should have been considered
         * to be timeout already. Thus, the completing execution trace should
         * appear as a single incomplete execution trace.
         */
        filter.getExecutionInputPort().newEvent(exec0_0__bookstore_searchBook);


        /**
         * Terminate the filter
         */
        filter.terminate(false); // no error

        /* Analyse result of test case execution */
        if (!receivedTheValidTriggerExecutionTrace.get()) {
            fail("Valid execution trace didn't pass the filter");
        }
        if (!receivedTheValidTriggerMessageTrace.get()) {
            fail("Message trace didn't pass the filter");
        }
        if (!receivedTheIncompleteExecutionTraceArtifact.get()) {
            fail("Incomplete trace didn't pass the filter");
        }
        if (!receivedTheCompletingExecutionTraceArtifact.get()) {
            fail("Completing trace didn't pass the filter");
        }
    }
}