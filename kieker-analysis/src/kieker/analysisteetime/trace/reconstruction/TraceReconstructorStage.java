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

import java.util.Optional;

import kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot;
import kieker.analysisteetime.model.analysismodel.trace.TraceRoot;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AbstractOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

import teetime.stage.basic.AbstractTransformation;

/**
 * Reconstruct traces based on the incoming instances of {@code IFlowRecord}. Currently only {@link TraceMetadata}, {@link BeforeOperationEvent} and
 * {@link AfterOperationEvent}
 * instances are supported.
 *
 * @author Nils Christian Ehmke, Sören Henning
 */
public class TraceReconstructorStage extends AbstractTransformation<IFlowRecord, TraceRoot> {

	private final TraceReconstructor traceReconstructor;

	public TraceReconstructorStage(final DeploymentRoot deploymentRoot, final boolean activateAdditionalLogChecks) {
		this.traceReconstructor = new TraceReconstructor(deploymentRoot, activateAdditionalLogChecks);
	}

	@Override
	protected void execute(final IFlowRecord record) {
		if (record instanceof TraceMetadata) {
			this.traceReconstructor.handleMetadataRecord((TraceMetadata) record);
		} else if (record instanceof BeforeOperationEvent) {
			this.traceReconstructor.handleBeforeOperationEventRecord((BeforeOperationEvent) record);
		} else if (record instanceof AbstractOperationEvent) {
			final Optional<TraceRoot> trace = this.traceReconstructor.handleAfterOperationEventRecord((AfterOperationEvent) record);
			// trace.ifPresent(this.outputPort::send);
			if (trace.isPresent()) {
				this.outputPort.send(trace.get());
			}
		}
	}

}
