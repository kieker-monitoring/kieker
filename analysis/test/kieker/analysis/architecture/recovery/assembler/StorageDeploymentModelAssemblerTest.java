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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kieker.analysis.architecture.recovery.events.StorageEvent;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.source.SourceFactory;
import kieker.model.analysismodel.source.SourceModel;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;

/**
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
class StorageDeploymentModelAssemblerTest {

	private static final String SOURCE_LABEL = "test";
	private static final String HOSTNAME_1 = "host1";
	private static final String HOSTNAME_2 = "host2";
	private static final String COMPONENT_SIGNATUE = "test.component";
	private static final String STORAGE_SIGNATURE = "data";
	private static final String STORAGE_TYPE = "integer";

	@Test
	void test() {
		// setup model
		final TypeModel typeModel = TypeFactory.eINSTANCE.createTypeModel();
		final AssemblyModel assemblyModel = AssemblyFactory.eINSTANCE.createAssemblyModel();
		final DeploymentModel deploymentModel = DeploymentFactory.eINSTANCE.createDeploymentModel();
		final SourceModel sourceModel = SourceFactory.eINSTANCE.createSourceModel();

		// setup model assembler
		final StorageTypeModelAssembler typeAssembler = new StorageTypeModelAssembler(typeModel, sourceModel,
				StorageDeploymentModelAssemblerTest.SOURCE_LABEL, new SimpleComponentSignatureExtractor(),
				new SimpleStorageSignatureExtractor());
		final StorageAssemblyModelAssembler assemblyAssembler = new StorageAssemblyModelAssembler(typeModel,
				assemblyModel, sourceModel, StorageDeploymentModelAssemblerTest.SOURCE_LABEL);
		final StorageDeploymentModelAssembler deploymentAssembler = new StorageDeploymentModelAssembler(assemblyModel,
				deploymentModel, sourceModel, StorageDeploymentModelAssemblerTest.SOURCE_LABEL);

		// send events
		this.processStorageEvent(typeAssembler, assemblyAssembler, deploymentAssembler,
				StorageDeploymentModelAssemblerTest.HOSTNAME_1);
		this.processStorageEvent(typeAssembler, assemblyAssembler, deploymentAssembler,
				StorageDeploymentModelAssemblerTest.HOSTNAME_2);

		// get elements from models
		final ComponentType componentType = typeModel.getComponentTypes()
				.get(StorageDeploymentModelAssemblerTest.COMPONENT_SIGNATUE);
		final AssemblyComponent assemblyComponent = assemblyModel.getComponents()
				.get(StorageDeploymentModelAssemblerTest.COMPONENT_SIGNATUE);

		final DeploymentContext context1 = deploymentModel.getContexts()
				.get(StorageDeploymentModelAssemblerTest.HOSTNAME_1);
		final DeployedComponent deployedComponent1 = context1.getComponents()
				.get(StorageDeploymentModelAssemblerTest.COMPONENT_SIGNATUE);

		final DeploymentContext context2 = deploymentModel.getContexts()
				.get(StorageDeploymentModelAssemblerTest.HOSTNAME_2);
		final DeployedComponent deployedComponent2 = context2.getComponents()
				.get(StorageDeploymentModelAssemblerTest.COMPONENT_SIGNATUE);

		// assertions
		Assertions.assertNotNull(deployedComponent1);
		Assertions.assertNotNull(deployedComponent2);

		Assertions.assertEquals(StorageDeploymentModelAssemblerTest.COMPONENT_SIGNATUE,
				assemblyComponent.getSignature());
		Assertions.assertEquals(componentType, assemblyComponent.getComponentType());

		Assertions.assertEquals(context1, deployedComponent1.getContext());
		Assertions.assertEquals(context2, deployedComponent2.getContext());

		this.checkStorage(assemblyComponent, deployedComponent1);
		this.checkStorage(assemblyComponent, deployedComponent2);
	}

	private void checkStorage(final AssemblyComponent assemblyComponent, final DeployedComponent deployedComponent) {
		final DeployedStorage storage2 = deployedComponent.getStorages()
				.get(StorageDeploymentModelAssemblerTest.STORAGE_SIGNATURE);
		Assertions.assertNotNull(storage2);
		Assertions.assertNotNull(storage2.getAssemblyStorage());
		Assertions.assertNotNull(storage2.getAssemblyStorage().getStorageType());
		Assertions.assertEquals(storage2.getAssemblyStorage(),
				assemblyComponent.getStorages().get(StorageDeploymentModelAssemblerTest.STORAGE_SIGNATURE));
		Assertions.assertEquals(StorageDeploymentModelAssemblerTest.STORAGE_SIGNATURE,
				storage2.getAssemblyStorage().getStorageType().getName());
	}

	private void processStorageEvent(final StorageTypeModelAssembler typeAssembler,
			final StorageAssemblyModelAssembler assemblyAssembler,
			final StorageDeploymentModelAssembler deploymentAssembler, final String hostname) {
		final StorageEvent storageEvent = new StorageEvent(hostname,
				StorageDeploymentModelAssemblerTest.COMPONENT_SIGNATUE,
				StorageDeploymentModelAssemblerTest.STORAGE_SIGNATURE,
				StorageDeploymentModelAssemblerTest.STORAGE_TYPE);
		typeAssembler.assemble(storageEvent);
		assemblyAssembler.assemble(storageEvent);
		deploymentAssembler.assemble(storageEvent);
	}

}
