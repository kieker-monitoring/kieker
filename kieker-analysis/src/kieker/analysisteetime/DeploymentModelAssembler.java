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

import kieker.analysisteetime.model.analysismodel.architecture.ComponentType;
import kieker.analysisteetime.model.analysismodel.architecture.IndexedArchitectureRoot;
import kieker.analysisteetime.model.analysismodel.architecture.IndexedComponentType;
import kieker.analysisteetime.model.analysismodel.architecture.OperationType;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentFactory;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot;
import kieker.common.record.flow.IOperationRecord;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class DeploymentModelAssembler {

	private final DeploymentFactory factory = DeploymentFactory.eINSTANCE;

	private final IndexedArchitectureRoot architectureRoot;
	private final DeploymentRoot deploymentRoot;

	public DeploymentModelAssembler(final IndexedArchitectureRoot architectureRoot, final DeploymentRoot deploymentRoot) {
		this.architectureRoot = architectureRoot;
		this.deploymentRoot = deploymentRoot;
	}

	public void addRecord(final IOperationRecord record) {
		final String hostName = ""; // TODO
		final String classSignature = record.getClassSignature();
		final String operationSignature = record.getOperationSignature();

		this.addRecord(hostName, classSignature, operationSignature);
	}

	public void addRecord(final String hostName, final String componentSignature, final String operationSignature) {

		final String deploymentContextKey = hostName;
		DeploymentContext deploymentContext = this.deploymentRoot.getDeploymentContexts().get(deploymentContextKey);
		if (deploymentContext == null) {
			deploymentContext = this.factory.createDeploymentContext();
			deploymentContext.setName(hostName);
			this.deploymentRoot.getDeploymentContexts().put(deploymentContextKey, deploymentContext);
			// Old version:
			// deploymentContext.setDeploymentRoot(this.deploymentRoot);
		}

		final String componentKey = componentSignature;
		DeployedComponent component = deploymentContext.getComponents().get(componentKey);
		if (component == null) {
			component = this.factory.createDeployedComponent();
			deploymentContext.getComponents().put(componentKey, component);
			// Old version
			// component.setDeploymentContext(deploymentContext);
			final ComponentType componentType = this.architectureRoot.getComponentTypeByName(componentSignature);
			component.setComponentType(componentType);
		}

		final String operationKey = operationSignature;
		DeployedOperation operation = component.getContainedOperations().get(operationKey);
		if (operation == null) {
			operation = this.factory.createDeployedOperation();
			component.getContainedOperations().put(operationKey, operation);
			// TODO ugly cast
			final IndexedComponentType componentType = (IndexedComponentType) component.getComponentType();
			final OperationType operationType = componentType.getOperationTypeByName(operationSignature);
			operation.setOperationType(operationType);
		}

	}

}
