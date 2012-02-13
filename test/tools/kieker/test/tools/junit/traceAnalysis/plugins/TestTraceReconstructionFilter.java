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

package kieker.test.tools.junit.traceAnalysis.plugins;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.test.tools.junit.traceAnalysis.util.ExecutionFactory;
import kieker.test.tools.junit.traceAnalysis.util.SimpleSinkPlugin;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.plugins.AbstractTraceProcessingPlugin;
import kieker.tools.traceAnalysis.plugins.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.plugins.traceReconstruction.TraceReconstructionFilter;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.InvalidExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import org.junit.Test;

/**
 * 
 * @author Andre van Hoorn
 */
public class TestTraceReconstructionFilter extends TestCase {

	private static final Log LOG = LogFactory.getLog(TestTraceReconstructionFilter.class);
	private static final long TRACE_ID = 62298L;
	private final SystemModelRepository systemEntityFactory = new SystemModelRepository(new Configuration());
	private final ExecutionFactory executionFactory = new ExecutionFactory(this.systemEntityFactory);

	/* Executions of a valid trace */
	private final Execution exec0_0__bookstore_searchBook; // NOCS
	private final Execution exec1_1__catalog_getBook; // NOCS
	private final Execution exec2_1__crm_getOrders; // NOCS
	private final Execution exec3_2__catalog_getBook; // NOCS

	public TestTraceReconstructionFilter() {
		/* Manually create Executions for a trace */
		this.exec0_0__bookstore_searchBook = this.executionFactory.genExecution("Bookstore", "bookstore", "searchBook", TestTraceReconstructionFilter.TRACE_ID,
				1 * (1000 * 1000), 10 * (1000 * 1000), 0, 0); // NOCS (MagicNumberCheck)

		this.exec1_1__catalog_getBook = this.executionFactory.genExecution("Catalog", "catalog", "getBook", TestTraceReconstructionFilter.TRACE_ID,
				2 * (1000 * 1000), 4 * (1000 * 1000), 1, 1); // NOCS (MagicNumberCheck)
		this.exec2_1__crm_getOrders = this.executionFactory.genExecution("CRM", "crm", "getOrders", TestTraceReconstructionFilter.TRACE_ID,
				5 * (1000 * 1000), 8 * (1000 * 1000), 2, 1); // NOCS (MagicNumberCheck)
		this.exec3_2__catalog_getBook = this.executionFactory.genExecution("Catalog", "catalog", "getBook", TestTraceReconstructionFilter.TRACE_ID,
				6 * (1000 * 1000), 7 * (1000 * 1000), 3, 2); // NOCS (MagicNumberCheck)
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
		final ExecutionTrace executionTrace = new ExecutionTrace(TestTraceReconstructionFilter.TRACE_ID);

		executionTrace.add(this.exec3_2__catalog_getBook);
		executionTrace.add(this.exec2_1__crm_getOrders);
		executionTrace.add(this.exec0_0__bookstore_searchBook);
		executionTrace.add(this.exec1_1__catalog_getBook);

		try {
			/* Make sure that trace is valid: */
			executionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);
		} catch (final InvalidTraceException ex) {
			TestTraceReconstructionFilter.LOG.error("", ex);
			Assert.fail("Test invalid since used trace invalid");
			throw new InvalidTraceException("Test invalid since used trace invalid", ex);
		}

		return executionTrace;
	}

	/**
	 * Tests whether a valid trace is correctly reconstructed and passed to the
	 * right output port.
	 */
	@Test
	public void testValidBookstoreTracePassed() {
		/*
		 * These are the trace representations we want to be reconstructed by
		 * the filter
		 */
		final ExecutionTrace validExecutionTrace;
		final MessageTrace validMessageTrace;
		try {
			validExecutionTrace = this.genValidBookstoreTrace();
			validMessageTrace = validExecutionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);
		} catch (final InvalidTraceException ex) {
			TestTraceReconstructionFilter.LOG.error("InvalidTraceException", ex); // NOPMD (string literal)
			Assert.fail("InvalidTraceException" + ex);
			return;
		}

		final Configuration configuration = new Configuration();
		configuration.setProperty(AbstractTraceAnalysisPlugin.CONFIG_NAME, "TraceReconstructionFilter");
		configuration.setProperty(TraceReconstructionFilter.CONFIG_IGNORE_INVALID_TRACES, "true");
		configuration.setProperty(TraceReconstructionFilter.CONFIG_MAX_TRACE_DURATION_MILLIS, Long.toString(AbstractTraceProcessingPlugin.MAX_DURATION_MILLIS));
		final Map<String, AbstractRepository> repositoryMap = new HashMap<String, AbstractRepository>();
		repositoryMap.put(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, this.systemEntityFactory);
		final TraceReconstructionFilter filter = new TraceReconstructionFilter(configuration);

		Assert.assertTrue("Test invalid since trace length smaller than filter timeout",
				validExecutionTrace.getDurationInNanos() <= filter.getMaxTraceDurationNanos());

		final SimpleSinkPlugin executionTraceSinkPlugin = new SimpleSinkPlugin();
		final SimpleSinkPlugin messageTraceSinkPlugin = new SimpleSinkPlugin();
		final SimpleSinkPlugin invalidExecutionTraceSinkPlugin = new SimpleSinkPlugin();
		/*
		 * Register a handler for reconstructed (valid) execution traces.
		 * This handler MUST receive exactly this trace (and no other).
		 */

		AbstractPlugin.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, executionTraceSinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);
		/*
		 * Register a handler for reconstructed (valid) message traces.
		 * This handler MUST receive exactly this trace (and no other).
		 */
		AbstractPlugin.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, messageTraceSinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);

		/*
		 * Register a handler for invalid execution traces.
		 * This handler MUST not be invoked.
		 */
		AbstractPlugin.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE, invalidExecutionTraceSinkPlugin,
				SimpleSinkPlugin.INPUT_PORT_NAME);

		if (!filter.execute()) {
			Assert.fail("Execution of filter failed");
			return;
		}

		/*
		 * Pass executions of the trace to be reconstructed.
		 */
		for (final Execution curExec : validExecutionTrace.getTraceAsSortedExecutionSet()) {
			filter.newExecution(curExec);
		}

		filter.terminate(false);

		/* Analyse result of test case execution */
		if (executionTraceSinkPlugin.getList().isEmpty()) {
			Assert.fail("Execution trace didn't pass the filter");
		} else {
			Assert.assertEquals("Unexpected execution trace", validExecutionTrace, executionTraceSinkPlugin.getList().get(0));
		}

		if (messageTraceSinkPlugin.getList().isEmpty()) {
			Assert.fail("Message trace didn't pass the filter");
		} else {
			Assert.assertEquals("Unexpected message trace", validMessageTrace, messageTraceSinkPlugin.getList().get(0));
		}

		if (!invalidExecutionTraceSinkPlugin.getList().isEmpty()) {
			Assert.fail("Received invalid trace from filter");
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
		final ExecutionTrace executionTrace = new ExecutionTrace(TestTraceReconstructionFilter.TRACE_ID);
		final Execution exec1_1__catalog_getBook__broken = this.executionFactory.genExecution("Catalog", "catalog", "getBook", // NOCS
				TestTraceReconstructionFilter.TRACE_ID, 2 * (1000 * 1000), 4 * (1000 * 1000), 1, 3); // NOCS (MagicNumberCheck)
		Assert.assertFalse("Invalid test", exec1_1__catalog_getBook__broken.equals(this.exec1_1__catalog_getBook));

		executionTrace.add(this.exec3_2__catalog_getBook);
		executionTrace.add(this.exec2_1__crm_getOrders);
		executionTrace.add(this.exec0_0__bookstore_searchBook);
		executionTrace.add(exec1_1__catalog_getBook__broken);

		return executionTrace;
	}

	/**
	 * Tests whether a broken trace is correctly detected and passed to the
	 * right output port.
	 */
	@Test
	public void testBrokenBookstoreTracePassed() {
		/*
		 * These are the trace representations we want to be reconstructed by
		 * the filter
		 */
		final ExecutionTrace invalidExecutionTrace;
		try {
			invalidExecutionTrace = this.genBrokenBookstoreTraceEssSkip();
		} catch (final InvalidTraceException ex) {
			TestTraceReconstructionFilter.LOG.error("InvalidTraceException", ex);
			Assert.fail("InvalidTraceException" + ex);
			return;
		}

		final Configuration configuration = new Configuration();
		configuration.setProperty(AbstractTraceAnalysisPlugin.CONFIG_NAME, "TraceReconstructionFilter");
		configuration.setProperty(TraceReconstructionFilter.CONFIG_IGNORE_INVALID_TRACES, "true");
		configuration.setProperty(TraceReconstructionFilter.CONFIG_MAX_TRACE_DURATION_MILLIS, Long.toString(AbstractTraceProcessingPlugin.MAX_DURATION_MILLIS));
		final Map<String, AbstractRepository> repositoryMap = new HashMap<String, AbstractRepository>();
		repositoryMap.put(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, this.systemEntityFactory);
		final TraceReconstructionFilter filter = new TraceReconstructionFilter(configuration);
		Assert.assertTrue("Test invalid since trace length smaller than filter timeout",
				invalidExecutionTrace.getDurationInNanos() <= filter.getMaxTraceDurationNanos());

		final SimpleSinkPlugin executionTraceSinkPlugin = new SimpleSinkPlugin();
		final SimpleSinkPlugin messageTraceSinkPlugin = new SimpleSinkPlugin();
		final SimpleSinkPlugin invalidExecutionTraceSinkPlugin = new SimpleSinkPlugin();

		/*
		 * Register a handler for reconstructed (valid) execution traces.
		 * This handler MUST not be invoked.
		 */

		AbstractPlugin.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, executionTraceSinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);
		/*
		 * Register a handler for reconstructed (valid) message traces.
		 * This handler MUST not be invoked.
		 */
		AbstractPlugin.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, messageTraceSinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);

		/*
		 * Register a handler for invalid execution traces.
		 * This handler MUST receive exactly this trace (and no other).
		 */
		AbstractPlugin.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE, invalidExecutionTraceSinkPlugin,
				SimpleSinkPlugin.INPUT_PORT_NAME);

		if (!filter.execute()) {
			Assert.fail("Execution of filter failed");
			return;
		}

		/*
		 * Pass executions of the trace to be reconstructed.
		 */
		for (final Execution curExec : invalidExecutionTrace.getTraceAsSortedExecutionSet()) {
			filter.newExecution(curExec);
		}

		TestTraceReconstructionFilter.LOG.info("This test triggers a FATAL warning about an ess skip <0,3> which can simply be ignored because it is desired");
		filter.terminate(false);

		/* Analyse result of test case execution */
		/* Analyse result of test case execution */
		if (!executionTraceSinkPlugin.getList().isEmpty()) {
			Assert.fail("A valid execution trace passed the filter");
		}

		if (!messageTraceSinkPlugin.getList().isEmpty()) {
			Assert.fail("A message trace passed the filter");
		}

		if (invalidExecutionTraceSinkPlugin.getList().isEmpty()) {
			Assert.fail("Invalid trace didn't pass the filter");
		} else {
			Assert.assertEquals("Unexpected invalid execution trace", invalidExecutionTrace,
					((InvalidExecutionTrace) invalidExecutionTraceSinkPlugin.getList().get(0)).getInvalidExecutionTraceArtifacts());
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
		final ExecutionTrace executionTrace = new ExecutionTrace(TestTraceReconstructionFilter.TRACE_ID);

		executionTrace.add(this.exec3_2__catalog_getBook);
		executionTrace.add(this.exec2_1__crm_getOrders);
		executionTrace.add(this.exec1_1__catalog_getBook);

		return executionTrace;
	}

	/**
	 * Tests the timeout of pending (incomplete) traces.
	 * A corresponding test for a valid trace is not required.
	 */
	@Test
	public void testIncompleteTraceDueToTimeout() {
		/*
		 * This trace is incomplete.
		 */
		final ExecutionTrace incompleteExecutionTrace;
		try {
			incompleteExecutionTrace = this.genBookstoreTraceWithoutEntryExecution();
		} catch (final InvalidTraceException ex) {
			TestTraceReconstructionFilter.LOG.error("InvalidTraceException", ex);
			Assert.fail("InvalidTraceException" + ex);
			return;
		}

		/**
		 * We will now create a trace that contains an execution which
		 * would make the incomplete trace complete.
		 * 
		 * But: Then, it would exceed the maximum trace duration.
		 */
		final ExecutionTrace completingExecutionTrace = new ExecutionTrace(incompleteExecutionTrace.getTraceId());
		Assert.assertTrue("Test invalid (traceIds not matching)", this.exec0_0__bookstore_searchBook.getTraceId() == completingExecutionTrace.getTraceId());
		try {
			completingExecutionTrace.add(this.exec0_0__bookstore_searchBook);
		} catch (final InvalidTraceException ex) {
			TestTraceReconstructionFilter.LOG.error("InvalidTraceException", ex);
			Assert.fail("InvalidTraceException" + ex);
			return;
		}

		/*
		 * We will use this execution to trigger the timeout check for
		 * pending traces within the filter.
		 */
		final int triggerTraceLengthMillis = 1;
		final long triggerTraceId = TestTraceReconstructionFilter.TRACE_ID + 1;
		final Execution exec0_0__bookstore_searchBook__trigger = this.executionFactory.genExecution("Bookstore", "bookstore", "searchBook", triggerTraceId, // NOCS
				incompleteExecutionTrace.getMaxTout(), incompleteExecutionTrace.getMaxTout() + (triggerTraceLengthMillis * (1000 * 1000)), 0, 0); // NOCS
		final ExecutionTrace triggerExecutionTrace = new ExecutionTrace(triggerTraceId);
		final MessageTrace triggerMessageTrace;
		try {
			triggerExecutionTrace.add(exec0_0__bookstore_searchBook__trigger);
			triggerMessageTrace = triggerExecutionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);
		} catch (final InvalidTraceException ex) {
			TestTraceReconstructionFilter.LOG.error("InvalidTraceException", ex);
			Assert.fail("InvalidTraceException" + ex);
			return;
		}

		/**
		 * Instantiate reconstruction filter with timeout.
		 */
		final Configuration configuration = new Configuration();
		configuration.setProperty(AbstractTraceAnalysisPlugin.CONFIG_NAME, "TraceReconstructionFilter");
		configuration.setProperty(TraceReconstructionFilter.CONFIG_IGNORE_INVALID_TRACES, "true");
		configuration.setProperty(TraceReconstructionFilter.CONFIG_MAX_TRACE_DURATION_MILLIS, Long.toString(
				((triggerExecutionTrace.getMaxTout() - incompleteExecutionTrace.getMinTin()) / (1000 * 1000)) - 1));
		final Map<String, AbstractRepository> repositoryMap = new HashMap<String, AbstractRepository>();
		repositoryMap.put(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, this.systemEntityFactory);
		final TraceReconstructionFilter filter = new TraceReconstructionFilter(configuration);

		final SimpleSinkPlugin executionTraceSink = new SimpleSinkPlugin();
		final SimpleSinkPlugin messageTraceSink = new SimpleSinkPlugin();
		final SimpleSinkPlugin invalidExecutionTraceSink = new SimpleSinkPlugin();

		Assert.assertTrue("Test invalid: NOT (tout of trigger trace - tin of incomplete > filter max. duration)\n" + "triggerExecutionTrace.getMaxTout()"
				+ triggerExecutionTrace.getMaxTout() + "\n" + "incompleteExecutionTrace.getMinTin()" + incompleteExecutionTrace.getMinTin() + "\n"
				+ "filter.getMaxTraceDurationNanos()" + filter.getMaxTraceDurationNanos(),
				(triggerExecutionTrace.getMaxTout() - incompleteExecutionTrace.getMinTin()) > filter.getMaxTraceDurationNanos());

		/*
		 * Register a handler for reconstructed (valid) execution traces.
		 * This handler MUST not be invoked.
		 */
		AbstractPlugin.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, executionTraceSink, SimpleSinkPlugin.INPUT_PORT_NAME);
		Assert.assertTrue(executionTraceSink.getList().isEmpty());

		/*
		 * Register a handler for reconstructed (valid) message traces.
		 * This handler MUST not be invoked.
		 */
		AbstractPlugin.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, messageTraceSink, SimpleSinkPlugin.INPUT_PORT_NAME);
		Assert.assertTrue(messageTraceSink.getList().isEmpty());

		/*
		 * Register a handler for invalid execution traces.
		 * This handler MUST receive exactly this trace (and no other).
		 */
		AbstractPlugin.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE, invalidExecutionTraceSink,
				SimpleSinkPlugin.INPUT_PORT_NAME);
		Assert.assertTrue(invalidExecutionTraceSink.getList().isEmpty());

		if (!filter.execute()) {
			Assert.fail("Execution of filter failed");
			return;
		}

		/*
		 * Pass the executions of the incomplete trace intended to time out
		 */
		for (final Execution curExec : incompleteExecutionTrace.getTraceAsSortedExecutionSet()) {
			filter.newExecution(curExec);
		}

		/**
		 * Pass the timeout "trigger execution"
		 */
		filter.newExecution(exec0_0__bookstore_searchBook__trigger);

		/**
		 * Now, will pass the execution that would make the incomplete trace
		 * complete. But that incomplete trace should have been considered
		 * to be timeout already. Thus, the completing execution trace should
		 * appear as a single incomplete execution trace.
		 */
		filter.newExecution(this.exec0_0__bookstore_searchBook);

		/**
		 * Terminate the filter
		 */
		filter.terminate(false); // no error

		/* Analyse result of test case execution */
		Assert.assertFalse("Valid execution trace didn't pass the filter", executionTraceSink.getList().isEmpty());
		Assert.assertEquals("Received an unexpected valid execution trace " + executionTraceSink.getList().get(0), triggerExecutionTrace, executionTraceSink
				.getList().get(0));

		Assert.assertFalse("Message trace didn't pass the filter", messageTraceSink.getList().isEmpty());
		Assert.assertEquals("Received an unexpected message trace " + messageTraceSink.getList().get(0), triggerMessageTrace, messageTraceSink
				.getList().get(0));

		Assert.assertEquals("An incomplete or complete trace didn't pass the filter", 2, invalidExecutionTraceSink.getList().size());
		for (int i = 0; i < 2; i++) {
			final InvalidExecutionTrace event = (InvalidExecutionTrace) invalidExecutionTraceSink.getList().get(i);
			if (event.getInvalidExecutionTraceArtifacts().equals(incompleteExecutionTrace)) {
				// Nothing to do
			} else if (event.getInvalidExecutionTraceArtifacts().equals(completingExecutionTrace)) {
				// Nothing to do
			} else {
				Assert.fail("Received an unexpected invalid execution trace: " + event);
			}
		}
	}
}
