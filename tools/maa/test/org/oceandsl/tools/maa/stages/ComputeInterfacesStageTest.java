/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package org.oceandsl.tools.maa.stages;

import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyProvidedInterface;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedProvidedInterface;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.ProvidedInterfaceType;
import kieker.tools.maa.stages.CollectInterModuleCallsStage;

import teetime.framework.test.StageTester;

/**
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
class ComputeInterfacesStageTest {

	@Test
	void noProvidedInterface() {
		final ModelRepository modelRepository = TestModelRepositoryUtils.createThreeComponentModel();
		TestModelInvocationUtils.addInvocations(modelRepository);

		final CollectInterModuleCallsStage stage = new CollectInterModuleCallsStage();
		StageTester.test(stage).send(modelRepository).to(stage.getInputPort()).start();
		final DeploymentModel deploymentModel = modelRepository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL);
		for (final DeploymentContext context : deploymentModel.getContexts().values()) {
			final DeployedComponent componentA = context.getComponents()
					.get(TestModelRepositoryUtils.FQN_COMPONENT_A + ":1");
			final DeployedComponent componentB = context.getComponents()
					.get(TestModelRepositoryUtils.FQN_COMPONENT_B + ":1");
			final DeployedComponent componentC = context.getComponents()
					.get(TestModelRepositoryUtils.FQN_COMPONENT_C + ":1");

			this.assertInterfaces(TestModelRepositoryUtils.COMPONENT_A, componentA, new String[] {}, 0);
			this.assertInterfaces(TestModelRepositoryUtils.COMPONENT_B, componentB, new String[] {}, 0);
			this.assertInterfaces(TestModelRepositoryUtils.COMPONENT_C, componentC, new String[] {}, 0);
		}
	}

	private void assertInterfaces(final String label, final DeployedComponent deployedComponent,
			final String[] providedSignatures, final int requiredCount) {
		Assertions.assertEquals(providedSignatures.length, deployedComponent.getProvidedInterfaces().size(),
				label + " provided deployed interface");
		Assertions.assertEquals(requiredCount, deployedComponent.getRequiredInterfaces().size(),
				label + " required deployed interface");

		final Collection<DeployedProvidedInterface> providedInterfaces = deployedComponent.getProvidedInterfaces()
				.values();
		if (providedInterfaces.size() == 1) {
			final DeployedProvidedInterface[] deployedProvidedInterface = providedInterfaces
					.toArray(new DeployedProvidedInterface[1]);
			for (final String signature : providedSignatures) {
				if (!deployedProvidedInterface[0].getProvidedInterface().getProvidedInterfaceType()
						.getProvidedOperationTypes().containsKey(signature)) {
					Assertions.fail(String.format("%s: deployed operation %s missing from interface %s", label,
							signature, deployedProvidedInterface[0].getProvidedInterface().getProvidedInterfaceType()
									.getSignature()));
				}
			}
		}

		this.assertInterfaces(label, deployedComponent.getAssemblyComponent(), providedSignatures, requiredCount);
	}

	private void assertInterfaces(final String label, final AssemblyComponent assemblyComponent,
			final String[] providedSignatures, final int requiredCount) {
		Assertions.assertEquals(providedSignatures.length, assemblyComponent.getProvidedInterfaces().size(),
				label + " provided assembly interface");
		Assertions.assertEquals(requiredCount, assemblyComponent.getRequiredInterfaces().size(),
				label + " required assembly interface");

		final Collection<AssemblyProvidedInterface> assemblyProvidedInterfaces = assemblyComponent
				.getProvidedInterfaces().values();
		if (assemblyProvidedInterfaces.size() == 1) {
			final AssemblyProvidedInterface[] assemblyProvidedInterface = assemblyProvidedInterfaces
					.toArray(new AssemblyProvidedInterface[1]);
			for (final String signature : providedSignatures) {
				if (!assemblyProvidedInterface[0].getProvidedInterfaceType().getProvidedOperationTypes()
						.containsKey(signature)) {
					Assertions.fail(String.format("%s: assembly operation %s missing from interface %s", label,
							signature, assemblyProvidedInterface[0].getProvidedInterfaceType().getSignature()));
				}
			}
		}

		this.assertInterfaces(label, assemblyComponent.getComponentType(), providedSignatures, requiredCount);
	}

	private void assertInterfaces(final String label, final ComponentType componentType,
			final String[] providedSignatures, final int requiredCount) {
		Assertions.assertEquals(providedSignatures.length, componentType.getProvidedInterfaceTypes().size(),
				label + " provided assembly interface");
		Assertions.assertEquals(requiredCount, componentType.getRequiredInterfaceTypes().size(),
				label + " required assembly interface");

		if (componentType.getProvidedInterfaceTypes().size() == 1) {
			final ProvidedInterfaceType providedInterfaceType = componentType.getProvidedInterfaceTypes().get(0);

			for (final String signature : providedSignatures) {
				if (!providedInterfaceType.getProvidedOperationTypes().containsKey(signature)) {
					Assertions.fail(String.format("%s: operation type %s missing from interface %s", label, signature,
							providedInterfaceType.getSignature()));
				}
			}
		}
	}

	@Test
	void noRequiredInterface() {
		final ModelRepository modelRepository = TestModelRepositoryUtils.createThreeComponentModel();
		TestModelInvocationUtils.addProvidedInterfaces(modelRepository);
		TestModelInvocationUtils.addInvocations(modelRepository);

		final CollectInterModuleCallsStage stage = new CollectInterModuleCallsStage();
		StageTester.test(stage).send(modelRepository).to(stage.getInputPort()).start();
		final DeploymentModel deploymentModel = modelRepository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL);
		for (final DeploymentContext context : deploymentModel.getContexts().values()) {
			final DeployedComponent componentA = context.getComponents()
					.get(TestModelRepositoryUtils.FQN_COMPONENT_A + ":1");
			final DeployedComponent componentB = context.getComponents()
					.get(TestModelRepositoryUtils.FQN_COMPONENT_B + ":1");
			final DeployedComponent componentC = context.getComponents()
					.get(TestModelRepositoryUtils.FQN_COMPONENT_C + ":1");

			this.assertInterfaces(TestModelRepositoryUtils.COMPONENT_A, componentA, new String[] {}, 0);
			this.assertInterfaces(TestModelRepositoryUtils.COMPONENT_B, componentB,
					new String[] { TestModelRepositoryUtils.OP_B_NAME_SIGNATURE }, 0);
			this.assertInterfaces(TestModelRepositoryUtils.COMPONENT_C, componentC,
					new String[] { TestModelRepositoryUtils.OP_C_NAME_SIGNATURE }, 0);
		}
	}

	@Test
	void completeModelInterface() {
		final ModelRepository modelRepository = TestModelRepositoryUtils.createThreeComponentModel();
		TestModelInvocationUtils.addProvidedInterfaces(modelRepository);
		TestModelInvocationUtils.addRequiredInterfaces(modelRepository);
		TestModelInvocationUtils.addInvocations(modelRepository);

		final CollectInterModuleCallsStage stage = new CollectInterModuleCallsStage();
		StageTester.test(stage).send(modelRepository).to(stage.getInputPort()).start();
		final DeploymentModel deploymentModel = modelRepository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL);
		for (final DeploymentContext context : deploymentModel.getContexts().values()) {
			final DeployedComponent componentA = context.getComponents()
					.get(TestModelRepositoryUtils.FQN_COMPONENT_A + ":1");
			final DeployedComponent componentB = context.getComponents()
					.get(TestModelRepositoryUtils.FQN_COMPONENT_B + ":1");
			final DeployedComponent componentC = context.getComponents()
					.get(TestModelRepositoryUtils.FQN_COMPONENT_C + ":1");

			this.assertInterfaces(TestModelRepositoryUtils.COMPONENT_A, componentA, new String[] {}, 2);
			this.assertInterfaces(TestModelRepositoryUtils.COMPONENT_B, componentB,
					new String[] { TestModelRepositoryUtils.OP_B_NAME_SIGNATURE }, 1);
			this.assertInterfaces(TestModelRepositoryUtils.COMPONENT_C, componentC,
					new String[] { TestModelRepositoryUtils.OP_C_NAME_SIGNATURE }, 0);
		}
	}
}
