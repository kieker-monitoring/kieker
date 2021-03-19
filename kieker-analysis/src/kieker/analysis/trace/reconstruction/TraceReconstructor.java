/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.trace.reconstruction;

import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.trace.Trace;

/**
 * Reconstruct traces based on the incoming instances of {@code IFlowRecord}.
 * Currently only {@link TraceMetadata}, {@link BeforeOperationEvent} and
 * {@link AfterOperationEvent} instances are supported.
 *
 * @author Nils Christian Ehmke, Sören Henning
 *
 * @since 1.14
 */
final class TraceReconstructor {

	private final DeploymentModel deploymentModel;
	private final Map<Long, TraceReconstructionBuffer> traceBuffers = new HashMap<>(); // NOPMD (no concurrent access
																						// intended)
	private final List<TraceReconstructionBuffer> faultyTraceBuffers = new ArrayList<>();
	// private final boolean activateAdditionalLogChecks;
	private int danglingRecords;
	private final TemporalUnit temporalUnit; // BETTER get this value by KiekerMetadataRecord

	public TraceReconstructor(final DeploymentModel deploymentRoot, final TemporalUnit temporalUnit) {
		this.deploymentModel = deploymentRoot;
		this.temporalUnit = temporalUnit;
	}

	// "activateAdditionalLogChecks" not yet implemented
	//
	// public TraceReconstructor(final DeploymentModel deploymentRoot, final boolean
	// activateAdditionalLogChecks, final TemporalUnit temporalUnit) {
	// this.deploymentModel = deploymentRoot;
	// this.activateAdditionalLogChecks = activateAdditionalLogChecks;
	// this.temporalUnit = temporalUnit;
	// }

	public int countIncompleteTraces() {
		return this.traceBuffers.size() + this.faultyTraceBuffers.size();
	}

	public int countDanglingRecords() {
		return this.danglingRecords - this.faultyTraceBuffers.size();
	}

	public void handleTraceMetadataRecord(final TraceMetadata record) {
		final long traceID = record.getTraceId();
		final TraceReconstructionBuffer newTraceBuffer = new TraceReconstructionBuffer(this.deploymentModel, record,
				this.temporalUnit);

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

	public Optional<Trace> handleAfterOperationEventRecord(final AfterOperationEvent event) {
		final long traceID = event.getTraceId();
		final TraceReconstructionBuffer traceBuffer = this.traceBuffers.get(traceID);

		if (traceBuffer != null) {
			traceBuffer.handleAfterOperationEventRecord(event);
			if (traceBuffer.isTraceComplete()) {
				final Trace trace = traceBuffer.reconstructTrace();
				this.traceBuffers.remove(traceID);
				return Optional.of(trace);
			}
		} else {
			this.danglingRecords++;
		}
		return Optional.empty();
	}

}
