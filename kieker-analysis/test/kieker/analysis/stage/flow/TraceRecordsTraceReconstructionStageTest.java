/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.stage.flow;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.util.bookstore.BookstoreEventRecordFactory;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

import teetime.framework.test.StageTester;

/**
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class TraceRecordsTraceReconstructionStageTest { // NOCS test do not need constructors

	private static final int NUMBER_OF_EVENTS = 10000;

	@Test
	public void testTraceReconstructions() {
		final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
		final boolean repairEventBasedTrace = false;
		final long maxTraceDuraction = Long.MAX_VALUE;
		final long maxTraceTimeout = Long.MAX_VALUE;
		final TraceRecordsTraceReconstructionStage stage = new TraceRecordsTraceReconstructionStage(timeUnit, repairEventBasedTrace, maxTraceDuraction,
				maxTraceTimeout);

		final TraceEventRecords elements = BookstoreEventRecordFactory.validSyncTraceAdditionalCallEvents(0, 1, "session", "hostname");

		StageTester.test(stage).send(elements).to(stage.getTraceEventRecordsInputPort()).start();

		Assert.assertThat(stage.getValidTracesOutputPort(), StageTester.produces(elements));
	}

	@Test
	public void testHugeTrace() {
		final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
		final boolean repairEventBasedTrace = false;
		final long maxTraceDuraction = Long.MAX_VALUE;
		final long maxTraceTimeout = Long.MAX_VALUE;
		final TraceRecordsTraceReconstructionStage stage = new TraceRecordsTraceReconstructionStage(timeUnit, repairEventBasedTrace, maxTraceDuraction,
				maxTraceTimeout);

		final TraceMetadata metadata = new TraceMetadata(1, 0, "test-session", "test-hostname", -1, -1);
		final AbstractTraceEvent[] traceEvents = new AbstractTraceEvent[NUMBER_OF_EVENTS];
		for (int i = 0; i < (NUMBER_OF_EVENTS / 2); i++) {
			traceEvents[i] = new BeforeOperationEvent(i, 1, i, "op()", "TestClass");
			traceEvents[i + (NUMBER_OF_EVENTS / 2)] = new AfterOperationEvent(i + NUMBER_OF_EVENTS, 1, i + (NUMBER_OF_EVENTS / 2), "op()", "TestClass");
		}

		final TraceEventRecords traceEventRecord = new TraceEventRecords(metadata, traceEvents);

		StageTester.test(stage).send(traceEventRecord).to(stage.getTraceEventRecordsInputPort()).start();

		Assert.assertThat(stage.getValidTracesOutputPort(), StageTester.produces(traceEventRecord));
	}
}
