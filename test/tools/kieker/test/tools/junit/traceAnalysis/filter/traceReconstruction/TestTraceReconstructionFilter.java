/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.traceAnalysis.filter.traceReconstruction;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.filter.traceReconstruction.TraceReconstructionFilter;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.InvalidExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.tools.util.ExecutionFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public class TestTraceReconstructionFilter extends AbstractKiekerTest {

	private static final Log LOG = LogFactory.getLog(TestTraceReconstructionFilter.class);
	private static final long TRACE_ID = 62298L;
	private static final String SESSION_ID = "Y2zm6CRc";

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
				TestTraceReconstructionFilter.SESSION_ID, 1 * (1000 * 1000), 10 * (1000 * 1000), 0, 0);

		this.exec1_1__catalog_getBook = this.executionFactory.genExecution("Catalog", "catalog", "getBook", TestTraceReconstructionFilter.TRACE_ID,
				TestTraceReconstructionFilter.SESSION_ID, 2 * (1000 * 1000), 4 * (1000 * 1000), 1, 1);
		this.exec2_1__crm_getOrders = this.executionFactory.genExecution("CRM", "crm", "getOrders", TestTraceReconstructionFilter.TRACE_ID,
				TestTraceReconstructionFilter.SESSION_ID, 5 * (1000 * 1000), 8 * (1000 * 1000), 2, 1);
		this.exec3_2__catalog_getBook = this.executionFactory.genExecution("Catalog", "catalog", "getBook", TestTraceReconstructionFilter.TRACE_ID,
				TestTraceReconstructionFilter.SESSION_ID, 6 * (1000 * 1000), 7 * (1000 * 1000), 3, 2);
	}

	/**
	 * Generates an execution trace representation of the "well-known" bookstore trace.
	 * 
	 * @return
	 * @throws InvalidTraceException
	 */
	private ExecutionTrace genValidBookstoreTrace() throws InvalidTraceException {
		/*
		 * Create an Execution Trace and add Executions in arbitrary order
		 */
		final ExecutionTrace executionTrace = new ExecutionTrace(TestTraceReconstructionFilter.TRACE_ID, TestTraceReconstructionFilter.SESSION_ID);

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
	 * @throws IllegalStateException
	 */
	@Test
	public void testValidBookstoreTracePassed() throws InvalidTraceException, IllegalStateException, AnalysisConfigurationException {
		/*
		 * These are the trace representations we want to be reconstructed by the filter
		 */
		final ExecutionTrace validExecutionTrace;
		final MessageTrace validMessageTrace;
		final AnalysisController controller = new AnalysisController();

		validExecutionTrace = this.genValidBookstoreTrace();
		validMessageTrace = validExecutionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);

		final ListReader<Execution> reader = new ListReader<Execution>(new Configuration());
		for (final Execution curExec : validExecutionTrace.getTraceAsSortedExecutionSet()) {
			reader.addObject(curExec);
		}

		final Configuration configuration = new Configuration();
		configuration.setProperty(TraceReconstructionFilter.class.getName() + ".name", "TraceReconstructionFilter");
		configuration.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_IGNORE_INVALID_TRACES, "true");
		configuration.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION_MILLIS, Long
				.toString(AbstractTraceProcessingFilter.MAX_DURATION_MILLIS));
		final TraceReconstructionFilter filter = new TraceReconstructionFilter(configuration);

		Assert.assertTrue("Test invalid since trace length smaller than filter timeout", validExecutionTrace.getDurationInNanos() <= filter
				.getMaxTraceDurationNanos());

		final ListCollectionFilter<ExecutionTrace> executionTraceSinkPlugin = new ListCollectionFilter<ExecutionTrace>(new Configuration());
		final ListCollectionFilter<MessageTrace> messageTraceSinkPlugin = new ListCollectionFilter<MessageTrace>(new Configuration());
		final ListCollectionFilter<InvalidExecutionTrace> invalidExecutionTraceSinkPlugin = new ListCollectionFilter<InvalidExecutionTrace>(new Configuration());
		controller.registerReader(reader);
		controller.registerFilter(executionTraceSinkPlugin);
		controller.registerFilter(messageTraceSinkPlugin);
		controller.registerFilter(invalidExecutionTraceSinkPlugin);
		controller.registerFilter(filter);

		controller.registerRepository(this.systemEntityFactory);
		controller.connect(filter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, this.systemEntityFactory);

		controller.connect(reader, ListReader.OUTPUT_PORT_NAME, filter, TraceReconstructionFilter.INPUT_PORT_NAME_EXECUTIONS);
		/*
		 * Register a handler for reconstructed (valid) execution traces. This handler MUST receive exactly this trace (and no other).
		 */
		controller.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, executionTraceSinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);
		/*
		 * Register a handler for reconstructed (valid) message traces. This handler MUST receive exactly this trace (and no other).
		 */
		controller.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, messageTraceSinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);
		/*
		 * Register a handler for invalid execution traces. This handler MUST not be invoked.
		 */
		controller.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE, invalidExecutionTraceSinkPlugin,
				ListCollectionFilter.INPUT_PORT_NAME);

		controller.run();

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
	 * Creates a broken execution trace version of the "well-known" Bookstore trace.
	 * 
	 * The trace is broken in that the eoi/ess values of an execution with eoi/ess [1,1] are replaced by the eoi/ess values [1,3]. Since ess values must only
	 * increment/decrement by 1, this test must lead to an exception.
	 * 
	 * @return
	 * @throws InvalidTraceException
	 */
	private ExecutionTrace genBrokenBookstoreTraceEssSkip() throws InvalidTraceException {
		/*
		 * Create an Execution Trace and add Executions in arbitrary order
		 */
		final ExecutionTrace executionTrace = new ExecutionTrace(TestTraceReconstructionFilter.TRACE_ID, TestTraceReconstructionFilter.SESSION_ID);
		final Execution exec1_1__catalog_getBook__broken = this.executionFactory.genExecution("Catalog", "catalog", "getBook", // NOCS
				TestTraceReconstructionFilter.TRACE_ID, TestTraceReconstructionFilter.SESSION_ID, 2 * (1000 * 1000), 4 * (1000 * 1000), 1, 3); // NOCS
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
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	@Test
	public void testBrokenBookstoreTracePassed() throws InvalidTraceException, IllegalStateException, AnalysisConfigurationException {
		/*
		 * These are the trace representations we want to be reconstructed by the filter
		 */
		final ExecutionTrace invalidExecutionTrace;
		final AnalysisController controller = new AnalysisController();
		invalidExecutionTrace = this.genBrokenBookstoreTraceEssSkip();

		final ListReader<Execution> reader = new ListReader<Execution>(new Configuration());
		for (final Execution curExec : invalidExecutionTrace.getTraceAsSortedExecutionSet()) {
			reader.addObject(curExec);
		}

		final Configuration configuration = new Configuration();
		configuration.setProperty(TraceReconstructionFilter.class.getName() + ".name", "TraceReconstructionFilter");
		configuration.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_IGNORE_INVALID_TRACES, "true");
		configuration.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION_MILLIS, Long
				.toString(AbstractTraceProcessingFilter.MAX_DURATION_MILLIS));
		final TraceReconstructionFilter filter = new TraceReconstructionFilter(configuration);
		Assert.assertTrue("Test invalid since trace length smaller than filter timeout", invalidExecutionTrace.getDurationInNanos() <= filter
				.getMaxTraceDurationNanos());

		final ListCollectionFilter<ExecutionTrace> executionTraceSinkPlugin = new ListCollectionFilter<ExecutionTrace>(new Configuration());
		final ListCollectionFilter<MessageTrace> messageTraceSinkPlugin = new ListCollectionFilter<MessageTrace>(new Configuration());
		final ListCollectionFilter<InvalidExecutionTrace> invalidExecutionTraceSinkPlugin = new ListCollectionFilter<InvalidExecutionTrace>(new Configuration());
		controller.registerReader(reader);
		controller.registerFilter(filter);
		controller.registerFilter(invalidExecutionTraceSinkPlugin);
		controller.registerFilter(messageTraceSinkPlugin);
		controller.registerFilter(executionTraceSinkPlugin);

		controller.registerRepository(this.systemEntityFactory);
		controller.connect(filter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, this.systemEntityFactory);

		controller.connect(reader, ListReader.OUTPUT_PORT_NAME, filter, TraceReconstructionFilter.INPUT_PORT_NAME_EXECUTIONS);
		/*
		 * Register a handler for reconstructed (valid) execution traces. This handler MUST not be invoked.
		 */
		controller.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, executionTraceSinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);
		/*
		 * Register a handler for reconstructed (valid) message traces. This handler MUST not be invoked.
		 */
		controller.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, messageTraceSinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);
		/*
		 * Register a handler for invalid execution traces. This handler MUST receive exactly this trace (and no other).
		 */
		controller.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE, invalidExecutionTraceSinkPlugin,
				ListCollectionFilter.INPUT_PORT_NAME);

		TestTraceReconstructionFilter.LOG.info("This test triggers a FATAL warning about an ess skip <0,3> which can simply be ignored because it is desired");

		controller.run();

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
			Assert.assertEquals("Unexpected invalid execution trace", invalidExecutionTrace, invalidExecutionTraceSinkPlugin.getList().get(0)
					.getInvalidExecutionTraceArtifacts());
		}
	}

	/**
	 * Generates an incomplete execution trace representation of the "well-known" bookstore trace. The outer bookstore.searchBook(..) execution with eoi/ess 0/0 is
	 * missing.
	 * 
	 * @return
	 * @throws InvalidTraceException
	 */
	private ExecutionTrace genBookstoreTraceWithoutEntryExecution() throws InvalidTraceException {
		/*
		 * Create an Execution Trace and add Executions in arbitrary order
		 */
		final ExecutionTrace executionTrace = new ExecutionTrace(TestTraceReconstructionFilter.TRACE_ID, TestTraceReconstructionFilter.SESSION_ID);

		executionTrace.add(this.exec3_2__catalog_getBook);
		executionTrace.add(this.exec2_1__crm_getOrders);
		executionTrace.add(this.exec1_1__catalog_getBook);

		return executionTrace;
	}

	/**
	 * Tests the timeout of pending (incomplete) traces. A corresponding test for a valid trace is not required.
	 * 
	 * @throws InvalidTraceException
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	@Test
	public void testIncompleteTraceDueToTimeout() throws InvalidTraceException, IllegalStateException, AnalysisConfigurationException {
		/*
		 * This trace is incomplete.
		 */
		final ExecutionTrace incompleteExecutionTrace;
		incompleteExecutionTrace = this.genBookstoreTraceWithoutEntryExecution();

		/**
		 * We will now create a trace that contains an execution which would make the incomplete trace complete.
		 * 
		 * But: Then, it would exceed the maximum trace duration.
		 */
		final ExecutionTrace completingExecutionTrace = new ExecutionTrace(incompleteExecutionTrace.getTraceId(), incompleteExecutionTrace.getSessionId());
		Assert.assertTrue("Test invalid (traceIds not matching)", this.exec0_0__bookstore_searchBook.getTraceId() == completingExecutionTrace.getTraceId());
		completingExecutionTrace.add(this.exec0_0__bookstore_searchBook);

		/*
		 * We will use this execution to trigger the timeout check for pending traces within the filter.
		 */
		final int triggerTraceLengthMillis = 1;
		final long triggerTraceId = TestTraceReconstructionFilter.TRACE_ID + 1;
		final Execution exec0_0__bookstore_searchBook__trigger = this.executionFactory.genExecution("Bookstore", "bookstore", "searchBook", triggerTraceId, // NOCS
				TestTraceReconstructionFilter.SESSION_ID, incompleteExecutionTrace.getMaxTout(), incompleteExecutionTrace.getMaxTout()
						+ (triggerTraceLengthMillis * (1000 * 1000)), 0, 0); // NOCS
		final ExecutionTrace triggerExecutionTrace = new ExecutionTrace(triggerTraceId, TestTraceReconstructionFilter.SESSION_ID);
		final MessageTrace triggerMessageTrace;
		triggerExecutionTrace.add(exec0_0__bookstore_searchBook__trigger);
		triggerMessageTrace = triggerExecutionTrace.toMessageTrace(SystemModelRepository.ROOT_EXECUTION);

		/**
		 * Instantiate reconstruction filter with timeout.
		 */
		final AnalysisController controller = new AnalysisController();
		final ListReader<Execution> reader = new ListReader<Execution>(new Configuration());
		for (final Execution curExec : incompleteExecutionTrace.getTraceAsSortedExecutionSet()) {
			reader.addObject(curExec);
		}
		final Configuration configuration = new Configuration();
		configuration.setProperty(TraceReconstructionFilter.class.getName() + ".name", "TraceReconstructionFilter");
		configuration.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_IGNORE_INVALID_TRACES, "true");
		configuration.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION_MILLIS, Long
				.toString(((triggerExecutionTrace.getMaxTout() - incompleteExecutionTrace.getMinTin()) / (1000 * 1000)) - 1));
		final TraceReconstructionFilter filter = new TraceReconstructionFilter(configuration);

		final ListCollectionFilter<ExecutionTrace> executionTraceSink = new ListCollectionFilter<ExecutionTrace>(new Configuration());
		final ListCollectionFilter<MessageTrace> messageTraceSink = new ListCollectionFilter<MessageTrace>(new Configuration());
		final ListCollectionFilter<InvalidExecutionTrace> invalidExecutionTraceSink = new ListCollectionFilter<InvalidExecutionTrace>(new Configuration());

		Assert.assertTrue("Test invalid: NOT (tout of trigger trace - tin of incomplete > filter max. duration)\n" + "triggerExecutionTrace.getMaxTout()"
				+ triggerExecutionTrace.getMaxTout() + "\n" + "incompleteExecutionTrace.getMinTin()" + incompleteExecutionTrace.getMinTin() + "\n"
				+ "filter.getMaxTraceDurationNanos()" + filter.getMaxTraceDurationNanos(), (triggerExecutionTrace.getMaxTout() - incompleteExecutionTrace
				.getMinTin()) > filter.getMaxTraceDurationNanos());
		controller.registerReader(reader);
		controller.registerFilter(filter);
		controller.registerFilter(invalidExecutionTraceSink);
		controller.registerFilter(messageTraceSink);
		controller.registerFilter(executionTraceSink);

		controller.registerRepository(this.systemEntityFactory);
		controller.connect(filter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, this.systemEntityFactory);

		controller.connect(reader, ListReader.OUTPUT_PORT_NAME, filter, TraceReconstructionFilter.INPUT_PORT_NAME_EXECUTIONS);
		/*
		 * Register a handler for reconstructed (valid) execution traces. This handler MUST not be invoked.
		 */
		controller.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE, executionTraceSink, ListCollectionFilter.INPUT_PORT_NAME);
		Assert.assertTrue(executionTraceSink.getList().isEmpty());

		/*
		 * Register a handler for reconstructed (valid) message traces. This handler MUST not be invoked.
		 */
		controller.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, messageTraceSink, ListCollectionFilter.INPUT_PORT_NAME);
		Assert.assertTrue(messageTraceSink.getList().isEmpty());

		/*
		 * Register a handler for invalid execution traces. This handler MUST receive exactly this trace (and no other).
		 */
		controller.connect(filter, TraceReconstructionFilter.OUTPUT_PORT_NAME_INVALID_EXECUTION_TRACE, invalidExecutionTraceSink,
				ListCollectionFilter.INPUT_PORT_NAME);
		Assert.assertTrue(invalidExecutionTraceSink.getList().isEmpty());

		/**
		 * Pass the timeout "trigger execution"
		 */
		LOG.info("Expecting (caught/logged) exception in TraceReconstructionFilter:");
		reader.addObject(exec0_0__bookstore_searchBook__trigger);

		/**
		 * Now, will pass the execution that would make the incomplete trace complete. But that incomplete trace should have been considered to be timeout already.
		 * Thus, the completing execution trace should appear as a single incomplete execution trace.
		 */
		reader.addObject(this.exec0_0__bookstore_searchBook);

		controller.run();

		/* Analyse result of test case execution */
		Assert.assertFalse("Valid execution trace didn't pass the filter", executionTraceSink.getList().isEmpty());
		Assert.assertEquals("Received an unexpected valid execution trace " + executionTraceSink.getList().get(0), triggerExecutionTrace, executionTraceSink
				.getList().get(0));

		Assert.assertFalse("Message trace didn't pass the filter", messageTraceSink.getList().isEmpty());
		Assert.assertEquals("Received an unexpected message trace " + messageTraceSink.getList().get(0), triggerMessageTrace, messageTraceSink.getList().get(0));

		Assert.assertEquals("An incomplete or complete trace didn't pass the filter", 2, invalidExecutionTraceSink.getList().size());
		for (int i = 0; i < 2; i++) {
			final InvalidExecutionTrace event = invalidExecutionTraceSink.getList().get(i);
			if (event.getInvalidExecutionTraceArtifacts().equals(incompleteExecutionTrace)) { // NOPMD NOCS (empty if)
				// Nothing to do
			} else if (event.getInvalidExecutionTraceArtifacts().equals(completingExecutionTrace)) { // NOPMD NOCS (empty if)
				// Nothing to do
			} else {
				Assert.fail("Received an unexpected invalid execution trace: " + event);
			}
		}
	}
}
