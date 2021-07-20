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

package kieker.analysis.stage.model;

import kieker.analysis.stage.model.data.OperationEvent;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.sources.SourceModel;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DeploymentModelAssembler extends AbstractSourceModelAssembler {

	private final DeploymentFactory factory = DeploymentFactory.eINSTANCE;
	private final AssemblyModel assemblyModel;
	private final DeploymentModel deploymentModel;

	public DeploymentModelAssembler(final AssemblyModel assemblyModel, final DeploymentModel deploymentModel, final SourceModel sourceModel,
			final String sourceLabel) {
		super(sourceModel, sourceLabel);
		this.assemblyModel = assemblyModel;
		this.deploymentModel = deploymentModel;
	}

	public void addOperation(final OperationEvent event) {
		final String hostname = event.getHostname();
		final String classSignature = event.getComponentSignature();
		final String operationSignature = event.getOperationSignature();

		this.addOperation(hostname, classSignature, operationSignature);
	}

	private void addOperation(final String hostname, final String componentSignature, final String operationSignature) {
		final DeploymentContext deploymentContext = this.addDeploymentContext(hostname);
		final DeployedComponent component = this.addDeployedComponent(deploymentContext, componentSignature);
		this.addDeployedOperation(component, operationSignature);
	}

	private DeploymentContext addDeploymentContext(final String hostname) {
		final String deploymentContextKey = hostname;
		DeploymentContext deploymentContext = this.deploymentModel.getDeploymentContexts().get(deploymentContextKey);
		if (deploymentContext == null) {
			deploymentContext = this.factory.createDeploymentContext();
			deploymentContext.setName(hostname);
			this.deploymentModel.getDeploymentContexts().put(deploymentContextKey, deploymentContext);
		}
		this.updateSourceModel(deploymentContext);

		return deploymentContext;
	}

	private DeployedComponent addDeployedComponent(final DeploymentContext deploymentContext, final String componentSignature) {
		final String componentKey = componentSignature;
		DeployedComponent component = deploymentContext.getComponents().get(componentKey);
		if (component == null) {
			component = this.factory.createDeployedComponent();
			deploymentContext.getComponents().put(componentKey, component);

			final String componentTypeKey = componentSignature;
			final AssemblyComponent assemblyComponent = this.assemblyModel.getAssemblyComponents().get(componentTypeKey);
			component.setAssemblyComponent(assemblyComponent);
		}

		this.updateSourceModel(component);

		return component;
	}

	private DeployedOperation addDeployedOperation(final DeployedComponent component, final String operationSignature) {
		final String operationKey = operationSignature;
		DeployedOperation operation = component.getContainedOperations().get(operationKey);
		if (operation == null) {
			operation = this.factory.createDeployedOperation();
			component.getContainedOperations().put(operationKey, operation);

			final AssemblyComponent assemblyComponent = component.getAssemblyComponent();
			final String operationTypeKey = operationSignature;
			final AssemblyOperation assemblyOperation = assemblyComponent.getAssemblyOperations().get(operationTypeKey);
			operation.setAssemblyOperation(assemblyOperation);
		}

		this.updateSourceModel(operation);

		return operation;
	}

}
