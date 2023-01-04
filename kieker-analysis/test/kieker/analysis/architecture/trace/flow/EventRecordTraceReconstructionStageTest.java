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
package kieker.analysis.architecture.trace.flow;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

import kieker.test.analysis.util.stage.BookstoreEventRecordFactory;

import teetime.framework.test.StageTester;

/**
 * @author Reiner Jung
 * @since 1.15
 */
public class EventRecordTraceReconstructionStageTest { // NOCS test do not need constructors

	private static final int NUMBER_OF_EVENTS = 10000;

	@Test
	public void testValidTraceNoTimeInput() {
		for (final TimeUnit timeUnit : TimeUnit.values()) {
			final boolean repairEventBasedTraces = false;
			final long maxTraceDuration = Long.MAX_VALUE;
			final long maxTraceTimeout = Long.MAX_VALUE;
			final EventRecordTraceReconstructionStage stage = new EventRecordTraceReconstructionStage(timeUnit, repairEventBasedTraces, maxTraceDuration,
					maxTraceTimeout);

			final TraceEventRecords traceEvents = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(0, 1, "test-session", "test-host");

			final AbstractTraceEvent[] events = traceEvents.getTraceEvents();

			final IFlowRecord[] elements = new IFlowRecord[events.length + 1];
			elements[0] = traceEvents.getTraceMetadata();
			int i = 1;
			for (final AbstractTraceEvent event : events) {
				elements[i++] = event;
			}

			StageTester.test(stage).send(elements).to(stage.getTraceRecordsInputPort()).start();

			Assert.assertThat(stage.getValidTracesOutputPort(), StageTester.produces(traceEvents));
		}
	}

	@Test
	public void testInvalidTraceNoTimeInput() {
		for (final TimeUnit timeUnit : TimeUnit.values()) {
			final boolean repairEventBasedTraces = false;
			final long maxTraceDuration = Long.MAX_VALUE;
			final long maxTraceTimeout = Long.MAX_VALUE;
			final EventRecordTraceReconstructionStage stage = new EventRecordTraceReconstructionStage(timeUnit, repairEventBasedTraces, maxTraceDuration,
					maxTraceTimeout);

			final TraceEventRecords traceEvents = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(0, 1, "test-session", "test-host");

			final AbstractTraceEvent[] events = traceEvents.getTraceEvents();

			final IFlowRecord[] elements = new IFlowRecord[events.length];
			elements[0] = traceEvents.getTraceMetadata();

			final AbstractTraceEvent[] resultEvents = new AbstractTraceEvent[events.length - 1];

			for (int i = 0; i < events.length - 1; i++) {
				elements[i + 1] = events[i];
				resultEvents[i] = events[i];
			}

			final TraceEventRecords resultTraceEvents = new TraceEventRecords(traceEvents.getTraceMetadata(), resultEvents);

			StageTester.test(stage).send(elements).to(stage.getTraceRecordsInputPort()).start();

			Assert.assertThat(stage.getInvalidTracesOutputPort(), StageTester.produces(resultTraceEvents));
		}
	}

	@Test
	public void testHugeValidTraceNoTimeInput() {
		for (final TimeUnit timeUnit : TimeUnit.values()) {
			final boolean repairEventBasedTraces = false;
			final long maxTraceDuration = Long.MAX_VALUE;
			final long maxTraceTimeout = Long.MAX_VALUE;
			final EventRecordTraceReconstructionStage stage = new EventRecordTraceReconstructionStage(timeUnit, repairEventBasedTraces, maxTraceDuration,
					maxTraceTimeout);

			final TraceMetadata metadata = new TraceMetadata(1, 0, "test-session", "test-hostname", -1, -1);
			final AbstractTraceEvent[] traceEvents = new AbstractTraceEvent[NUMBER_OF_EVENTS];
			final IFlowRecord[] sendTraceEvents = new IFlowRecord[NUMBER_OF_EVENTS + 1];
			sendTraceEvents[0] = metadata;
			for (int i = 0; i < NUMBER_OF_EVENTS / 2; i++) {
				traceEvents[i] = new BeforeOperationEvent(i, 1, i, "op()", "TestClass");
				sendTraceEvents[i + 1] = traceEvents[i];
				traceEvents[i + NUMBER_OF_EVENTS / 2] = new AfterOperationEvent(i + NUMBER_OF_EVENTS, 1, i + NUMBER_OF_EVENTS / 2, "op()", "TestClass");
				sendTraceEvents[i + NUMBER_OF_EVENTS / 2 + 1] = traceEvents[i + NUMBER_OF_EVENTS / 2];
			}

			final TraceEventRecords traceEventRecord = new TraceEventRecords(metadata, traceEvents);

			StageTester.test(stage).send(sendTraceEvents).to(stage.getTraceRecordsInputPort()).start();

			Assert.assertThat(stage.getValidTracesOutputPort(), StageTester.produces(traceEventRecord));
		}
	}

}
