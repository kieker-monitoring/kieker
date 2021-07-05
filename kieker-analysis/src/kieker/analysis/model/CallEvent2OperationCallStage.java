/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.model;

import kieker.analysis.model.data.CallEvent;
import kieker.analysis.model.data.OperationEvent;
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
public class CallEvent2OperationCallStage extends AbstractTransformation<CallEvent, Tuple<DeployedOperation, DeployedOperation>> {

	private final DeploymentModel deploymentModel;

	public CallEvent2OperationCallStage(final DeploymentModel deploymentModel) {
		this.deploymentModel = deploymentModel;
	}

	@Override
	protected void execute(final CallEvent element) {
		final Tuple<DeployedOperation, DeployedOperation> result = ExecutionFactory.eINSTANCE.createTuple();
		result.setFirst(this.findDeployedOperation(element.getCaller()));
		result.setSecond(this.findDeployedOperation(element.getCallee()));
		this.outputPort.send(result);
	}

	private DeployedOperation findDeployedOperation(final OperationEvent operationEvent) {
		final DeploymentContext context = this.deploymentModel.getDeploymentContexts().get(operationEvent.getHostname());
		final DeployedComponent component = context.getComponents().get(operationEvent.getComponentSignature());
		final DeployedOperation operation = component.getContainedOperations().get(operationEvent.getOperationSignature());

		return operation;
	}

}
