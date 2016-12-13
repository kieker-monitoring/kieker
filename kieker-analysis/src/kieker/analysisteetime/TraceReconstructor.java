/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.model.analysismodel.trace.TraceRoot;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AbstractOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

import teetime.stage.basic.AbstractTransformation;

/**
 * Reconstruct traces based on the incoming instances of {@code IFlowRecord}. Currently only {@link TraceMetadata}, {@link BeforeOperationEvent} and
 * {@link AfterOperationEvent}
 * instances are supported.
 *
 * @author Nils Christian Ehmke, Sören Henning
 */
final class TraceReconstructor extends AbstractTransformation<IFlowRecord, TraceRoot> {

	private final Map<Long, TraceBuffer> traceBuffers = new HashMap<>();
	private final List<TraceBuffer> faultyTraceBuffers = new ArrayList<>();
	private final boolean activateAdditionalLogChecks;
	private int danglingRecords;

	public TraceReconstructor(final boolean activateAdditionalLogChecks) {
		this.activateAdditionalLogChecks = activateAdditionalLogChecks;
	}

	public int countIncompleteTraces() {
		return this.traceBuffers.size() + this.faultyTraceBuffers.size();
	}

	public int countDanglingRecords() {
		return this.danglingRecords - this.faultyTraceBuffers.size();
	}

	@Override
	protected void execute(final IFlowRecord input) {
		if (input instanceof TraceMetadata) {
			this.handleMetadataRecord((TraceMetadata) input);
		} else if (input instanceof AbstractOperationEvent) {
			this.handleOperationEventRecord((AbstractOperationEvent) input);
		}
	}

	private void handleMetadataRecord(final TraceMetadata record) {
		final long traceID = record.getTraceId();
		final TraceBuffer newTraceBuffer = new TraceBuffer(record);

		this.traceBuffers.put(traceID, newTraceBuffer);
	}

	private void handleOperationEventRecord(final AbstractOperationEvent input) {
		final long traceID = input.getTraceId();
		final TraceBuffer traceBuffer = this.traceBuffers.get(traceID);

		if (traceBuffer != null) {
			traceBuffer.handleEvent(input);
			if (traceBuffer.isTraceComplete()) {
				final TraceRoot trace = traceBuffer.reconstructTrace();
				this.traceBuffers.remove(traceID);
				super.getOutputPort().send(trace);
			}
		} else {
			this.danglingRecords++;
		}
	}

	private final class TraceBuffer {

		private final String hostname;
		private final Deque<BeforeOperationEvent> stack = new LinkedList<>();
		private OperationCall root;
		private OperationCall header;
		private final long traceID;

		public TraceBuffer(final TraceMetadata traceMetadata) {
			this.hostname = traceMetadata.getHostname();
			this.traceID = traceMetadata.getTraceId();
		}

		public void handleEvent(final AbstractOperationEvent record) {
			if (record instanceof BeforeOperationEvent) {
				this.handleBeforeOperationEventRecord((BeforeOperationEvent) record);
			} else if (record instanceof AfterOperationEvent) {
				this.handleAfterOperationEventRecord((AfterOperationEvent) record);
			}
		}

		private void handleBeforeOperationEventRecord(final BeforeOperationEvent record) {
			this.stack.push(record);

			final OperationCall newCall = null; // TODO
			// final OperationCall newCall = new OperationCall(this.hostname, record.getClassSignature(), record.getOperationSignature(), record.getOrderIndex(),
			// this.traceID, record.getLoggingTimestamp());
			if (this.root == null) {
				this.root = newCall;
			} else {
				// TODO
				// this.header.addChild(newCall);
			}
			this.header = newCall;
		}

		private void handleAfterOperationEventRecord(final AfterOperationEvent record) {
			final BeforeOperationEvent beforeEvent = this.stack.pop();

			final long durationInNanos = record.getTimestamp() - beforeEvent.getTimestamp();
			this.header.setDuration(Duration.ofNanos(durationInNanos));

			if (record instanceof AfterOperationFailedEvent) {
				// this.header.setFailedCause(((AfterOperationFailedEvent) record).getCause());
			}

			this.header = this.header.getParent();

			// TODO generell question
			// TODO warning on or off? in Kieker-TeeTime-Stages off, in Kieker on
			if (TraceReconstructor.this.activateAdditionalLogChecks) {
				if (!beforeEvent.getOperationSignature().equals(record.getOperationSignature())) {
					TraceReconstructor.this.faultyTraceBuffers.add(this);
					TraceReconstructor.this.traceBuffers.remove(this.traceID);
				}
			}
		}

		public TraceRoot reconstructTrace() {
			return null; // TODO
			// return new Trace(this.root, this.traceID);
		}

		public boolean isTraceComplete() {
			return this.stack.isEmpty();
		}

	}

}
