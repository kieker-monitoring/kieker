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

import java.util.HashMap;
import java.util.Map;

import kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyOperation;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyRoot;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentFactory;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AbstractOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class DeploymentModelAssembler {

	private final DeploymentFactory factory = DeploymentFactory.eINSTANCE;

	private final Map<Long, TraceRepositoryEntry> traceRepository = new HashMap<>();

	private final AssemblyRoot assemblyRoot;
	private final DeploymentRoot deploymentRoot;

	public DeploymentModelAssembler(final AssemblyRoot assemblyRoot, final DeploymentRoot deploymentRoot) {
		this.assemblyRoot = assemblyRoot;
		this.deploymentRoot = deploymentRoot;
	}

	public void handleMetadataRecord(final TraceMetadata metadata) {
		final String hostname = metadata.getHostname();
		final long traceId = metadata.getTraceId();
		this.traceRepository.put(traceId, new TraceRepositoryEntry(hostname));
	}

	public void handleBeforeOperationEvent(final BeforeOperationEvent event) {
		this.traceRepository.get(event.getTraceId()).size++;

		this.addRecord(event);
	}

	public void handleAfterOperationEvent(final AfterOperationEvent event) {
		final long traceId = event.getTraceId();
		final TraceRepositoryEntry entry = this.traceRepository.get(traceId);
		entry.size--;
		if (entry.size == 0) {
			this.traceRepository.remove(traceId);
		}
	}

	private void addRecord(final AbstractOperationEvent record) {
		final String hostName = this.traceRepository.get(record.getTraceId()).hostname;
		final String classSignature = record.getClassSignature();
		final String operationSignature = record.getOperationSignature();

		this.addRecord(hostName, classSignature, operationSignature);
	}

	public void addRecord(final String hostname, final String componentSignature, final String operationSignature) {
		final DeploymentContext deploymentContext = this.addDeploymentContext(hostname);
		final DeployedComponent component = this.addDeployedComponent(deploymentContext, componentSignature);
		this.addDeployedOperation(component, operationSignature);
	}

	private DeploymentContext addDeploymentContext(final String hostname) {
		final String deploymentContextKey = hostname;
		DeploymentContext deploymentContext = this.deploymentRoot.getDeploymentContexts().get(deploymentContextKey);
		if (deploymentContext == null) {
			deploymentContext = this.factory.createDeploymentContext();
			deploymentContext.setName(hostname);
			this.deploymentRoot.getDeploymentContexts().put(deploymentContextKey, deploymentContext);
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
			final AssemblyComponent assemblyComponent = this.assemblyRoot.getAssemblyComponents().get(componentTypeKey);
			component.setAssemblyOperation(assemblyComponent);
		}
		return component;
	}

	private DeployedOperation addDeployedOperation(final DeployedComponent component, final String operationSignature) {
		final String operationKey = operationSignature;
		DeployedOperation operation = component.getContainedOperations().get(operationKey);
		if (operation == null) {
			operation = this.factory.createDeployedOperation();
			component.getContainedOperations().put(operationKey, operation);

			final AssemblyComponent assemblyComponent = component.getAssemblyOperation();
			final String operationTypeKey = operationSignature;
			final AssemblyOperation assemblyOperation = assemblyComponent.getAssemblyOperations().get(operationTypeKey);
			operation.setAssemblyOperation(assemblyOperation);
		}
		return operation;
	}

	protected static class TraceRepositoryEntry {
		protected int size = 0;
		protected String hostname;

		public TraceRepositoryEntry(final String hostname) {
			this.hostname = hostname;
		}
	}

}
