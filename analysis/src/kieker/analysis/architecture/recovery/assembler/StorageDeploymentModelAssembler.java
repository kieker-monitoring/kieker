/***************************************************************************
 * Copyright (C) 2022 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.architecture.recovery.assembler;

import kieker.analysis.architecture.recovery.events.StorageEvent;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyStorage;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.source.SourceModel;

/**
 *
 * @author Reiner Jung
 * @author Yannick Illmann (initial contribution)
 *
 * @since 2.0.0
 */
public class StorageDeploymentModelAssembler extends AbstractModelAssembler<StorageEvent> {

	private final DeploymentFactory factory = DeploymentFactory.eINSTANCE;

	private final AssemblyModel assemblyModel;
	private final DeploymentModel deploymentModel;

	public StorageDeploymentModelAssembler(final AssemblyModel assemblyModel, final DeploymentModel deploymentModel,
			final SourceModel sourceModel, final String sourceLabel) {
		super(sourceModel, sourceLabel);
		this.assemblyModel = assemblyModel;
		this.deploymentModel = deploymentModel;
	}

	@Override
	public void assemble(final StorageEvent event) {
		final DeployedComponent deployedComponent = this.deployedComponentSetUp(event);
		this.addStorage(deployedComponent, event);
	}

	/**
	 * This function retrieves a stored or new created DeployedComponent. Depending on the given
	 * identifier in the transfer-object a subroutine is called to set up a new DeployedComponent
	 * instance.
	 *
	 * @param event
	 *            storage event containing all information for a data storage
	 * @return component, stored for the given identifier string
	 */
	private DeployedComponent deployedComponentSetUp(final StorageEvent event) {
		DeploymentContext deploymentContext = this.findOrAddDeploymentContext(event.getHostname());
		if (deploymentContext == null) {
			deploymentContext = this.factory.createDeploymentContext();
			deploymentContext.setName(event.getHostname());
			this.deploymentModel.getContexts().put(event.getHostname(), deploymentContext);
		}
		this.updateSourceModel(deploymentContext);

		DeployedComponent deployedComponent = deploymentContext.getComponents().get(event.getComponentSignature());
		if (deployedComponent == null) {
			deployedComponent = this.createDeployedComponent(deploymentContext, event);
		}

		return deployedComponent;
	}

	private DeploymentContext findOrAddDeploymentContext(final String hostname) {
		final String deploymentContextKey = hostname;
		DeploymentContext deploymentContext = this.deploymentModel.getContexts().get(deploymentContextKey);
		if (deploymentContext == null) {
			deploymentContext = this.factory.createDeploymentContext();
			deploymentContext.setName(hostname);
			this.deploymentModel.getContexts().put(deploymentContextKey, deploymentContext);
		}
		this.updateSourceModel(deploymentContext);

		return deploymentContext;
	}

	/**
	 * This function is used to create a new file DeployedComponentObject and store it in the given
	 * deployment model.
	 *
	 * @param deploymentContext
	 *            context the deploymentComponents are stored in
	 * @param event
	 *            storage event
	 * @return file component created and stored in the deployment model
	 */
	private DeployedComponent createDeployedComponent(final DeploymentContext deploymentContext,
			final StorageEvent event) {
		final DeployedComponent newDeployedComponent = DeploymentFactory.eINSTANCE.createDeployedComponent();

		final String componentSignature = event.getComponentSignature();
		newDeployedComponent.setSignature(componentSignature);
		newDeployedComponent.setAssemblyComponent(this.assemblyModel.getComponents().get(componentSignature));
		deploymentContext.getComponents().put(componentSignature, newDeployedComponent);

		this.updateSourceModel(newDeployedComponent);
		return newDeployedComponent;
	}

	/**
	 * This function adds an DeployedStorage to a given component.
	 *
	 * @param deployedComponent
	 *            the storage should be stored in
	 * @param event
	 *            StorageEvent containing all dataflow information in one step.
	 * @return the added operation. Useful for DEBUG Reasons
	 */
	private DeployedStorage addStorage(final DeployedComponent deployedComponent, final StorageEvent event) {
		final String storageSignature = event.getStorageSignature();
		DeployedStorage deployedStorage = deployedComponent.getStorages().get(storageSignature);
		if (deployedStorage == null) {
			deployedStorage = DeploymentFactory.eINSTANCE.createDeployedStorage();
			final AssemblyStorage assemblyStorage = deployedComponent.getAssemblyComponent().getStorages()
					.get(storageSignature);
			deployedStorage.setAssemblyStorage(assemblyStorage);
			deployedComponent.getStorages().put(storageSignature, deployedStorage);
			this.deploymentModel.getContexts().get(event.getHostname()).getComponents()
					.put(deployedComponent.getSignature(), deployedComponent);
			this.updateSourceModel(deployedStorage);
		}
		return deployedStorage;
	}
}
