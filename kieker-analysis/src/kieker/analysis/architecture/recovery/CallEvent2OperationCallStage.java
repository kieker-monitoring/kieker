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
package kieker.analysis.architecture.recovery;

import kieker.analysis.architecture.recovery.data.CallEvent;
import kieker.analysis.architecture.recovery.data.OperationCallDurationEvent;
import kieker.analysis.architecture.recovery.data.OperationEvent;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.Tuple;

import teetime.stage.basic.AbstractTransformation;

/**
 * Transforms call events into model based OperationCalls.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class CallEvent2OperationCallStage extends AbstractTransformation<CallEvent, OperationCallDurationEvent> {

	private final DeploymentModel deploymentModel;

	public CallEvent2OperationCallStage(final DeploymentModel deploymentModel) {
		this.deploymentModel = deploymentModel;
	}

	@Override
	protected void execute(final CallEvent element) {
		final Tuple<DeployedOperation, DeployedOperation> operationCall = ExecutionFactory.eINSTANCE.createTuple();
		operationCall.setFirst(this.findDeployedOperation(element.getCaller()));
		operationCall.setSecond(this.findDeployedOperation(element.getCallee()));
		this.outputPort.send(new OperationCallDurationEvent(operationCall, element.getDuration()));
	}

	private DeployedOperation findDeployedOperation(final OperationEvent operationEvent) {
		final DeploymentContext context = this.deploymentModel.getContexts().get(operationEvent.getHostname());
		final DeployedComponent component = context.getComponents().get(operationEvent.getComponentSignature());
		final DeployedOperation operation = component.getOperations().get(operationEvent.getOperationSignature());

		return operation;
	}

}
