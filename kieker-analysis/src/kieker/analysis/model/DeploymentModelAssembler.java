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

package kieker.analysis.model;

import kieker.analysis.HostnameRepository;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AbstractOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentModel;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DeploymentModelAssembler {

	private final DeploymentFactory factory = DeploymentFactory.eINSTANCE;

	private final HostnameRepository hostnameRepository = new HostnameRepository();

	private final AssemblyModel assemblyModel;
	private final DeploymentModel deploymentModel;

	public DeploymentModelAssembler(final AssemblyModel assemblyModel, final DeploymentModel deploymentModel) {
		this.assemblyModel = assemblyModel;
		this.deploymentModel = deploymentModel;
	}

	public void handleMetadataRecord(final TraceMetadata metadata) {
		final String hostname = metadata.getHostname();
		final long traceId = metadata.getTraceId();
		this.hostnameRepository.addEntry(traceId, hostname);
	}

	public void handleBeforeOperationEvent(final BeforeOperationEvent event) {
		this.hostnameRepository.inc(event.getTraceId());

		this.addRecord(event);
	}

	public void handleAfterOperationEvent(final AfterOperationEvent event) {
		this.hostnameRepository.dec(event.getTraceId());
	}

	private void addRecord(final AbstractOperationEvent record) {
		final String hostname = this.hostnameRepository.getHostname(record.getTraceId());
		final String classSignature = record.getClassSignature();
		final String operationSignature = record.getOperationSignature();

		this.addRecord(hostname, classSignature, operationSignature);
	}

	private void addRecord(final String hostname, final String componentSignature, final String operationSignature) {
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
		return operation;
	}

}
