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

import java.time.Duration;
import java.time.Instant;
import java.util.Deque;
import java.util.LinkedList;

import kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot;
import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.model.analysismodel.trace.TraceFactory;
import kieker.analysisteetime.model.analysismodel.trace.TraceRoot;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

/**
 *
 * @author Sören Henning
 *
 * @since 1.13
 */
public class TraceReconstructionBuffer {

	private final TraceFactory factory = TraceFactory.eINSTANCE;
	private final DeploymentRoot deploymentRoot;
	private final TraceMetadata traceMetadata;

	private final Deque<BeforeOperationEvent> stack = new LinkedList<>();
	private OperationCall root;
	private OperationCall current;

	public TraceReconstructionBuffer(final DeploymentRoot deploymentRoot, final TraceMetadata traceMetadata) {
		this.deploymentRoot = deploymentRoot;
		this.traceMetadata = traceMetadata;
	}

	public void handleBeforeOperationEventRecord(final BeforeOperationEvent record) {
		this.stack.push(record);

		final OperationCall newCall = this.factory.createOperationCall();

		// TODO Calculate Start
		final long epochMilli = 0; // TODO Temp
		final long nanosOffset = 0; // TODO Temp
		newCall.setStart(Instant.ofEpochMilli(epochMilli).plusNanos(nanosOffset));

		// TODO Retrieve Deployed Operation
		final DeploymentContext context = this.deploymentRoot.getDeploymentContexts().get(this.traceMetadata.getHostname());
		final DeployedComponent component = context.getComponents().get(record.getClassSignature());
		final DeployedOperation operation = component.getContainedOperations().get(record.getOperationSignature());
		newCall.setOperation(operation);

		// TODO Set OrderIndex and Stack Depth
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

		final long durationInNanos = record.getTimestamp() - beforeEvent.getTimestamp();
		this.current.setDuration(Duration.ofNanos(durationInNanos));

		if (record instanceof AfterOperationFailedEvent) {
			final String failedCause = ((AfterOperationFailedEvent) record).getCause();
			this.current.setFailed(true);
			this.current.setFailedCause(failedCause);
		}

		this.current = this.current.getParent();

		// TODO
		/*
		 * if (TraceReconstructor.this.activateAdditionalLogChecks) {
		 * if (!beforeEvent.getOperationSignature().equals(record.getOperationSignature())) {
		 * TraceReconstructor.this.faultyTraceBuffers.add(this);
		 * TraceReconstructor.this.traceBuffers.remove(this.traceID);
		 * }
		 * }
		 */
	}

	public TraceRoot reconstructTrace() {
		final TraceRoot traceRoot = this.factory.createTraceRoot();
		traceRoot.setRootOperationCall(this.root);
		traceRoot.setTraceID(this.traceMetadata.getTraceId());
		// TODO Maybe further attributes
		return traceRoot;
	}

	public boolean isTraceComplete() {
		return this.stack.isEmpty();
	}

}
