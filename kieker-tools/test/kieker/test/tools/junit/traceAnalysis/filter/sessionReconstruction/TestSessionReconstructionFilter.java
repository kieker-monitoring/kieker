/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.traceAnalysis.filter.sessionReconstruction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.traceAnalysis.systemModel.ExecutionTraceBasedSession;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.tools.util.filter.sessionReconstruction.SessionReconstructionTestSetup;
import kieker.test.tools.util.filter.sessionReconstruction.SessionReconstructionTestUtil;

/**
 * Test suite for the {@link kieker.tools.traceAnalysis.filter.sessionReconstruction.SessionReconstructionFilter}.
 * 
 * @author Holger Knoche
 * @since 1.10
 * 
 */
public class TestSessionReconstructionFilter extends AbstractKiekerTest { // NOCS (MissingCtorCheck)

	private static final long MAX_THINK_TIME_MILLIS = 1;
	private static final long MAX_THINK_TIME_NANOS = TimeUnit.MILLISECONDS.toNanos(MAX_THINK_TIME_MILLIS);

	private static final String HOST_NAME = "test";
	private static final String NO_PARAMETERS = "()";

	private static final String OPERATION_NAME = "op1";

	private static final String OPERATION_SIGNATURE = OPERATION_NAME + NO_PARAMETERS;

	private static final String SESSION_ID_1 = "SESS1";
	private static final String SESSION_ID_2 = "SESS2";

	private static OperationExecutionRecord createOperationExecutionRecord(final String signature, final String sessionId, final long traceId, final long tIn,
			final long tOut, final int eoi, final int ess) {
		return new OperationExecutionRecord(signature, sessionId, traceId, tIn, tOut, HOST_NAME, eoi, ess);
	}

	/**
	 * Returns a list with two {@link OperationExecutionRecord}s, having different trace identifiers
	 * but equal session identifiers. The time distance between the two traces is small in order
	 * to have them in the same session.
	 */
	private static List<OperationExecutionRecord> createSimpleExecutionTrace() {
		final List<OperationExecutionRecord> records = new ArrayList<OperationExecutionRecord>();
		long time = 0;
		final long traceId = 0;

		records.add(TestSessionReconstructionFilter.createOperationExecutionRecord(OPERATION_SIGNATURE, SESSION_ID_1, traceId, time++, time++, 0, 0));
		records.add(TestSessionReconstructionFilter.createOperationExecutionRecord(OPERATION_SIGNATURE, SESSION_ID_1, traceId + 1, time++, time, 0, 0));

		return records;
	}

	/**
	 * Returns a list with two {@link OperationExecutionRecord}s, having different trace identifiers
	 * but equal session identifiers. The time distance between the two traces is large in order
	 * to have them in the separate sessions.
	 */
	private static List<OperationExecutionRecord> createSplitSessionExecutionTrace() {
		final List<OperationExecutionRecord> records = new ArrayList<OperationExecutionRecord>();
		long time = 0;
		final long traceId = 0;

		records.add(TestSessionReconstructionFilter.createOperationExecutionRecord(OPERATION_SIGNATURE, SESSION_ID_1, traceId, time++, time++, 0, 0));
		time += (MAX_THINK_TIME_NANOS + 1);
		records.add(TestSessionReconstructionFilter.createOperationExecutionRecord(OPERATION_SIGNATURE, SESSION_ID_1, traceId + 1, time++, time, 0, 0));

		return records;
	}

	/**
	 * Returns a list with two {@link OperationExecutionRecord}s, having different trace identifiers
	 * and different session identifiers.
	 */
	private static List<OperationExecutionRecord> createTwoSessionsExecutionTrace() {
		final List<OperationExecutionRecord> records = new ArrayList<OperationExecutionRecord>();
		long time = 0;
		final long traceId = 0;

		records.add(TestSessionReconstructionFilter.createOperationExecutionRecord(OPERATION_SIGNATURE, SESSION_ID_1, traceId, time++, time++, 0, 0));
		records.add(TestSessionReconstructionFilter.createOperationExecutionRecord(OPERATION_SIGNATURE, SESSION_ID_2, traceId + 1, time++, time, 0, 0));

		return records;
	}

	/**
	 * Tests a simple trace constellation with two traces which get assigned to the same session.
	 * 
	 * @throws AnalysisConfigurationException
	 *             If a configuration error occurs
	 */
	@Test
	public void testSimpleExecutionTraceSession() throws AnalysisConfigurationException {
		final List<OperationExecutionRecord> records = TestSessionReconstructionFilter.createSimpleExecutionTrace();
		final SessionReconstructionTestSetup<ExecutionTraceBasedSession> setup = SessionReconstructionTestUtil.createSetup(records, MAX_THINK_TIME_MILLIS);

		setup.run();

		final List<ExecutionTraceBasedSession> sessions = setup.getResult();
		Assert.assertEquals(1, sessions.size());

		final ExecutionTraceBasedSession session = sessions.get(0);
		Assert.assertEquals(2, session.getStateContainedTraces().size());
	}

	/**
	 * Tests a simple trace constellation with two traces which are put into two different sessions because
	 * the maximum think time is exceeded.
	 * 
	 * @throws AnalysisConfigurationException
	 *             If a configuration error occurs
	 */
	@Test
	public void testExecutionTraceSessionSplit() throws AnalysisConfigurationException {
		final List<OperationExecutionRecord> records = TestSessionReconstructionFilter.createSplitSessionExecutionTrace();
		final SessionReconstructionTestSetup<ExecutionTraceBasedSession> setup = SessionReconstructionTestUtil.createSetup(records, MAX_THINK_TIME_MILLIS);

		setup.run();

		final List<ExecutionTraceBasedSession> sessions = setup.getResult();
		Assert.assertEquals(2, sessions.size());

		final ExecutionTraceBasedSession session1 = sessions.get(0);
		Assert.assertEquals(1, session1.getStateContainedTraces().size());

		final ExecutionTraceBasedSession session2 = sessions.get(1);
		Assert.assertEquals(1, session2.getStateContainedTraces().size());
	}

	/**
	 * Tests a trace constellation with two traces which carry different session IDs.
	 * 
	 * @throws AnalysisConfigurationException
	 *             If a configuration error occurs
	 */
	@Test
	public void testExecutionTraceTwoSessions() throws AnalysisConfigurationException {
		final List<OperationExecutionRecord> records = TestSessionReconstructionFilter.createTwoSessionsExecutionTrace();
		final SessionReconstructionTestSetup<ExecutionTraceBasedSession> setup = SessionReconstructionTestUtil.createSetup(records, MAX_THINK_TIME_MILLIS);

		setup.run();

		final List<ExecutionTraceBasedSession> sessions = setup.getResult();
		Assert.assertEquals(2, sessions.size());

		final ExecutionTraceBasedSession session1 = sessions.get(0);
		Assert.assertEquals(1, session1.getStateContainedTraces().size());

		final ExecutionTraceBasedSession session2 = sessions.get(1);
		Assert.assertEquals(1, session2.getStateContainedTraces().size());
	}
}
