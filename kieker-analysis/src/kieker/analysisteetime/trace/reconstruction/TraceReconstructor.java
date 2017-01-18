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

package kieker.analysisteetime.trace.reconstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot;
import kieker.analysisteetime.model.analysismodel.trace.TraceRoot;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

/**
 * Reconstruct traces based on the incoming instances of {@code IFlowRecord}. Currently only {@link TraceMetadata}, {@link BeforeOperationEvent} and
 * {@link AfterOperationEvent}
 * instances are supported.
 *
 * @author Nils Christian Ehmke, Sören Henning
 */
final class TraceReconstructor {

	private final DeploymentRoot deploymentRoot;
	private final Map<Long, TraceReconstructionBuffer> traceBuffers = new HashMap<>();
	private final List<TraceReconstructionBuffer> faultyTraceBuffers = new ArrayList<>();
	private final boolean activateAdditionalLogChecks;
	private int danglingRecords;

	public TraceReconstructor(final DeploymentRoot deploymentRoot, final boolean activateAdditionalLogChecks) {
		this.deploymentRoot = deploymentRoot;
		this.activateAdditionalLogChecks = activateAdditionalLogChecks;
	}

	public int countIncompleteTraces() {
		return this.traceBuffers.size() + this.faultyTraceBuffers.size();
	}

	public int countDanglingRecords() {
		return this.danglingRecords - this.faultyTraceBuffers.size();
	}

	public void handleMetadataRecord(final TraceMetadata record) {
		final long traceID = record.getTraceId();
		final TraceReconstructionBuffer newTraceBuffer = new TraceReconstructionBuffer(this.deploymentRoot, record);

		this.traceBuffers.put(traceID, newTraceBuffer);
	}

	public void handleBeforeOperationEventRecord(final BeforeOperationEvent event) {
		final long traceID = event.getTraceId();
		final TraceReconstructionBuffer traceBuffer = this.traceBuffers.get(traceID);

		if (traceBuffer != null) {
			traceBuffer.handleBeforeOperationEventRecord(event);
		} else {
			this.danglingRecords++;
		}
	}

	public Optional<TraceRoot> handleAfterOperationEventRecord(final AfterOperationEvent event) {
		final long traceID = event.getTraceId();
		final TraceReconstructionBuffer traceBuffer = this.traceBuffers.get(traceID);

		if (traceBuffer != null) {
			traceBuffer.handleAfterOperationEventRecord(event);
			if (traceBuffer.isTraceComplete()) {
				final TraceRoot trace = traceBuffer.reconstructTrace();
				this.traceBuffers.remove(traceID);
				return Optional.of(trace);
			}
		} else {
			this.danglingRecords++;
		}
		return Optional.empty();
	}

}
