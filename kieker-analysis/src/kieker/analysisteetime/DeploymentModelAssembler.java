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

import kieker.analysisteetime.model.analysismodel.architecture.IndexedArchitectureRoot;
import kieker.analysisteetime.model.analysismodel.architecture.IndexedComponentType;
import kieker.analysisteetime.model.analysismodel.architecture.OperationType;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentFactory;
import kieker.analysisteetime.model.analysismodel.deployment.IndexedDeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.IndexedDeploymentContext;
import kieker.analysisteetime.model.analysismodel.deployment.IndexedDeploymentFactory;
import kieker.analysisteetime.model.analysismodel.deployment.IndexedDeploymentRoot;
import kieker.common.record.flow.IOperationRecord;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
// TODO initial
public class DeploymentModelAssembler {

	private final IndexedDeploymentFactory indexedFactory = IndexedDeploymentFactory.INSTANCE;
	private final DeploymentFactory factory = DeploymentFactory.eINSTANCE;

	private final IndexedArchitectureRoot architectureRoot;
	private final IndexedDeploymentRoot deploymentRoot;

	public DeploymentModelAssembler(final IndexedArchitectureRoot architectureRoot, final IndexedDeploymentRoot deploymentRoot) {
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

		// TODO ugly cast
		IndexedDeploymentContext deploymentContext = (IndexedDeploymentContext) this.deploymentRoot.getDeploymentContextByName(hostName);
		if (deploymentContext == null) {
			deploymentContext = this.indexedFactory.createDeploymentContext();
			// TODO set Name
			deploymentContext.setDeploymentRoot(this.deploymentRoot);
		}

		// TODO ugly cast
		IndexedDeployedComponent component = (IndexedDeployedComponent) deploymentContext.getDeployedComponentByName(componentSignature);
		if (component == null) {
			component = this.indexedFactory.createDeployedComponent();
			component.setDeploymentContext(deploymentContext);
			// TODO ugly cast
			final IndexedComponentType componentType = (IndexedComponentType) this.architectureRoot.getComponentTypeByName(componentSignature);
			component.setComponentType(componentType);
		}

		DeployedOperation operation = component.getDeployedOperationByName(operationSignature);
		if (operation == null) {
			operation = this.factory.createDeployedOperation();
			operation.setContainedComponent(component);
			// TODO ugly cast
			final IndexedComponentType componentType = (IndexedComponentType) component.getComponentType();
			final OperationType operationType = componentType.getOperationTypeByName(operationSignature);
			operation.setOperationType(operationType);
		}

	}

}
