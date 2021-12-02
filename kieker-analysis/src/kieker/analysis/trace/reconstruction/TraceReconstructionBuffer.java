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

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Deque;
import java.util.LinkedList;

import kieker.analysis.util.time.Instants;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.trace.OperationCall;
import kieker.model.analysismodel.trace.Trace;
import kieker.model.analysismodel.trace.TraceFactory;

/**
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TraceReconstructionBuffer {

	private final TraceFactory factory = TraceFactory.eINSTANCE;
	private final DeploymentModel deploymentModel;
	private final TraceMetadata traceMetadata;
	private final TemporalUnit temporalUnit;

	private final Deque<BeforeOperationEvent> stack = new LinkedList<>();
	private OperationCall root;
	private OperationCall current;

	public TraceReconstructionBuffer(final DeploymentModel deploymentModel, final TraceMetadata traceMetadata,
			final TemporalUnit temporalUnit) {
		this.deploymentModel = deploymentModel;
		this.traceMetadata = traceMetadata;
		this.temporalUnit = temporalUnit; // ChronoUnit.NANOS;
	}

	public void handleBeforeOperationEventRecord(final BeforeOperationEvent record) {
		this.stack.push(record);

		final OperationCall newCall = this.factory.createOperationCall();
		final Instant start = Instants.createFromEpochTimestamp(record.getTimestamp(), this.temporalUnit);
		newCall.setStart(start);

		final DeploymentContext context = this.deploymentModel.getDeploymentContexts()
				.get(this.traceMetadata.getHostname());
		final DeployedComponent component = context.getComponents().get(record.getClassSignature());
		final DeployedOperation operation = component.getOperations().get(record.getOperationSignature());
		newCall.setOperation(operation);

		newCall.setOrderIndex(record.getOrderIndex());
		newCall.setStackDepth(this.stack.size() - 1);

		if (this.root == null) {
			this.root = newCall;
		} else {
			this.current.getChildren().add(newCall);
		}
		this.current = newCall;
	}

	public void handleAfterOperationEventRecord(final AfterOperationEvent record) {
		final BeforeOperationEvent beforeEvent = this.stack.pop();

		final long timestampDifference = record.getTimestamp() - beforeEvent.getTimestamp();
		this.current.setDuration(Duration.of(timestampDifference, this.temporalUnit));

		if (record instanceof AfterOperationFailedEvent) {
			final String failedCause = ((AfterOperationFailedEvent) record).getCause();
			this.current.setFailed(true);
			this.current.setFailedCause(failedCause);
		}

		this.current = this.current.getParent();

		// BETTER handle additional log checks
		//
		// if (TraceReconstructor.this.activateAdditionalLogChecks) {
		// if
		// (!beforeEvent.getOperationSignature().equals(record.getOperationSignature()))
		// {
		// TraceReconstructor.this.faultyTraceBuffers.add(this);
		// TraceReconstructor.this.traceBuffers.remove(this.traceID);
		// }
		// }
		//
	}

	public Trace reconstructTrace() {
		final Trace trace = this.factory.createTrace();
		trace.setRootOperationCall(this.root);
		trace.setTraceID(this.traceMetadata.getTraceId());
		return trace;
	}

	public boolean isTraceComplete() {
		return this.stack.isEmpty();
	}

}
