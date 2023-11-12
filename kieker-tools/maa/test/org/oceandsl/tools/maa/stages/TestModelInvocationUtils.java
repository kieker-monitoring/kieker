/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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

import kieker.analysis.architecture.repository.ModelDescriptor;
import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.AssemblyProvidedInterface;
import kieker.model.analysismodel.assembly.AssemblyRequiredInterface;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedProvidedInterface;
import kieker.model.analysismodel.deployment.DeployedRequiredInterface;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.Tuple;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.ProvidedInterfaceType;
import kieker.model.analysismodel.type.RequiredInterfaceType;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;
import kieker.model.analysismodel.type.TypePackage;

/**
 * @author Reiner Jung
 *
 */
public final class TestModelInvocationUtils { // NOPMD is not a test class

    private TestModelInvocationUtils() {
        // utility class
    }

    public static void addInvocations(final ModelRepository modelRepository) {
        final DeploymentModel deploymentModel = modelRepository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL);

        modelRepository.register(new ModelDescriptor("execution-model.xmi", ExecutionPackage.Literals.EXECUTION_MODEL,
                ExecutionFactory.eINSTANCE), TestModelInvocationUtils.createExecutions(deploymentModel));
    }

    private static ExecutionModel createExecutions(final DeploymentModel deploymentModel) {
        final ExecutionModel executionModel = ExecutionFactory.eINSTANCE.createExecutionModel();

        final DeploymentContext context = deploymentModel.getContexts().get(TestModelRepositoryUtils.CONTEXT_A);

        final DeployedComponent componentA = context.getComponents()
                .get(TestModelRepositoryUtils.FQN_COMPONENT_A + ":1");
        final DeployedComponent componentB = context.getComponents()
                .get(TestModelRepositoryUtils.FQN_COMPONENT_B + ":1");
        final DeployedComponent componentC = context.getComponents()
                .get(TestModelRepositoryUtils.FQN_COMPONENT_C + ":1");

        final DeployedOperation opAMain = componentA.getOperations()
                .get(TestModelRepositoryUtils.OP_A_NAME_MAIN_SIGNATURE);
        final DeployedOperation opASecond = componentA.getOperations()
                .get(TestModelRepositoryUtils.OP_A_NAME_SECOND_SIGNATURE);
        final DeployedOperation opB = componentB.getOperations().get(TestModelRepositoryUtils.OP_B_NAME_SIGNATURE);
        final DeployedOperation opC = componentC.getOperations().get(TestModelRepositoryUtils.OP_C_NAME_SIGNATURE);

        TestModelInvocationUtils.createInvocation(executionModel, opAMain, opASecond);
        TestModelInvocationUtils.createInvocation(executionModel, opASecond, opB);
        TestModelInvocationUtils.createInvocation(executionModel, opB, opC);

        return executionModel;
    }

    private static void createInvocation(final ExecutionModel executionModel, final DeployedOperation caller,
            final DeployedOperation callee) {
        final Tuple<DeployedOperation, DeployedOperation> key = ExecutionFactory.eINSTANCE.createTuple();
        key.setFirst(caller);
        key.setSecond(callee);

        final Invocation value = ExecutionFactory.eINSTANCE.createInvocation();
        value.setCaller(caller);
        value.setCallee(callee);

        executionModel.getInvocations().put(key, value);
    }

    public static void addProvidedInterfaces(final ModelRepository modelRepository) {
        TestModelInvocationUtils.createProvidedInterface(modelRepository, TestModelRepositoryUtils.FQN_COMPONENT_B,
                TestModelRepositoryUtils.OP_B_NAME_SIGNATURE);
        TestModelInvocationUtils.createProvidedInterface(modelRepository, TestModelRepositoryUtils.FQN_COMPONENT_C,
                TestModelRepositoryUtils.OP_C_NAME_SIGNATURE);
    }

    public static void addEmptyProvidedInterfaces(final ModelRepository modelRepository) {
        TestModelInvocationUtils.createEmptyProvidedInterface(modelRepository,
                TestModelRepositoryUtils.FQN_COMPONENT_B);
        TestModelInvocationUtils.createEmptyProvidedInterface(modelRepository,
                TestModelRepositoryUtils.FQN_COMPONENT_C);
    }

    private static void createEmptyProvidedInterface(final ModelRepository modelRepository,
            final String componentName) {
        final TypeModel typeModel = modelRepository.getModel(TypePackage.Literals.TYPE_MODEL);
        final AssemblyModel assmeblyModel = modelRepository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL);
        final DeploymentModel deploymentModel = modelRepository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL);

        final ComponentType componentType = typeModel.getComponentTypes().get(componentName);
        final AssemblyComponent assemblyComponent = assmeblyModel.getComponents().get(componentName + ":1");
        final DeployedComponent deployedComponent = deploymentModel.getContexts()
                .get(TestModelRepositoryUtils.CONTEXT_A).getComponents().get(componentName + ":1");

        final ProvidedInterfaceType providedInterfaceType = TestModelInvocationUtils
                .createProvidedInterfaceType(componentType);
        final AssemblyProvidedInterface assemblyProvidedInterface = TestModelInvocationUtils
                .createAssemblyProvidedInterface(assemblyComponent, providedInterfaceType);
        TestModelInvocationUtils.createDeployedProvidedInterface(deployedComponent, assemblyProvidedInterface);
    }

    private static void createProvidedInterface(final ModelRepository modelRepository, final String componentName,
            final String operationName) {
        final TypeModel typeModel = modelRepository.getModel(TypePackage.Literals.TYPE_MODEL);
        final AssemblyModel assemblyModel = modelRepository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL);
        final DeploymentModel deploymentModel = modelRepository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL);

        final ComponentType componentType = typeModel.getComponentTypes().get(componentName);
        final AssemblyComponent assemblyComponent = assemblyModel.getComponents().get(componentName + ":1");
        final DeployedComponent deployedComponent = deploymentModel.getContexts()
                .get(TestModelRepositoryUtils.CONTEXT_A).getComponents().get(componentName + ":1");

        final ProvidedInterfaceType providedInterfaceType = TestModelInvocationUtils
                .createProvidedInterfaceType(componentType, operationName);
        final AssemblyProvidedInterface assemblyProvidedInterface = TestModelInvocationUtils
                .createAssemblyProvidedInterface(assemblyComponent, providedInterfaceType);
        TestModelInvocationUtils.createDeployedProvidedInterface(deployedComponent, assemblyProvidedInterface);
    }

    private static DeployedProvidedInterface createDeployedProvidedInterface(final DeployedComponent deployedComponent,
            final AssemblyProvidedInterface assemblyProvidedInterface) {
        final DeployedProvidedInterface deployedProvidedInterface = DeploymentFactory.eINSTANCE
                .createDeployedProvidedInterface();
        deployedProvidedInterface.setProvidedInterface(assemblyProvidedInterface);
        deployedComponent.getProvidedInterfaces().put(
                deployedProvidedInterface.getProvidedInterface().getProvidedInterfaceType().getSignature(),
                deployedProvidedInterface);

        return deployedProvidedInterface;
    }

    private static AssemblyProvidedInterface createAssemblyProvidedInterface(final AssemblyComponent assemblyComponent,
            final ProvidedInterfaceType providedInterfaceType) {
        final AssemblyProvidedInterface assemblyProvidedInterface = AssemblyFactory.eINSTANCE
                .createAssemblyProvidedInterface();
        assemblyProvidedInterface.setProvidedInterfaceType(providedInterfaceType);
        assemblyComponent.getProvidedInterfaces().put(providedInterfaceType.getSignature(), assemblyProvidedInterface);

        return assemblyProvidedInterface;
    }

    private static ProvidedInterfaceType createProvidedInterfaceType(final ComponentType componentType,
            final String operationSignature) {
        final ProvidedInterfaceType providedInterfaceType = TestModelInvocationUtils
                .createProvidedInterfaceType(componentType);
        final OperationType operation = componentType.getProvidedOperations().get(operationSignature);
        providedInterfaceType.getProvidedOperationTypes().put(operationSignature, operation);

        return providedInterfaceType;
    }

    private static ProvidedInterfaceType createProvidedInterfaceType(final ComponentType componentType) {
        final ProvidedInterfaceType providedInterfaceType = TypeFactory.eINSTANCE.createProvidedInterfaceType();
        providedInterfaceType
                .setSignature(componentType.getSignature() + componentType.getProvidedInterfaceTypes().size());
        providedInterfaceType.setPackage(componentType.getPackage());
        providedInterfaceType.setName(componentType.getName() + componentType.getProvidedInterfaceTypes().size());

        componentType.getProvidedInterfaceTypes().add(providedInterfaceType);

        return providedInterfaceType;
    }

    public static void addRequiredInterfaces(final ModelRepository modelRepository) {
        TestModelInvocationUtils.createRquiredInterface(modelRepository, TestModelRepositoryUtils.FQN_COMPONENT_A,
                TestModelRepositoryUtils.FQN_COMPONENT_C);
        TestModelInvocationUtils.createRquiredInterface(modelRepository, TestModelRepositoryUtils.FQN_COMPONENT_A,
                TestModelRepositoryUtils.FQN_COMPONENT_B);
        TestModelInvocationUtils.createRquiredInterface(modelRepository, TestModelRepositoryUtils.FQN_COMPONENT_B,
                TestModelRepositoryUtils.FQN_COMPONENT_C);

    }

    private static void createRquiredInterface(final ModelRepository modelRepository,
            final String requiredComponentName, final String providedComponentName) {
        final TypeModel typeModel = modelRepository.getModel(TypePackage.Literals.TYPE_MODEL);
        final AssemblyModel assemblyModel = modelRepository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL);
        final DeploymentModel deploymentModel = modelRepository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL);

        final ComponentType requiredComponentType = typeModel.getComponentTypes().get(requiredComponentName);
        final AssemblyComponent requiredAssemblyComponent = assemblyModel.getComponents()
                .get(requiredComponentName + ":1");
        final DeployedComponent requiredDeployedComponent = deploymentModel.getContexts()
                .get(TestModelRepositoryUtils.CONTEXT_A).getComponents().get(requiredComponentName + ":1");

        final ComponentType providedComponentType = typeModel.getComponentTypes().get(providedComponentName);
        final AssemblyComponent providedAssemblyComponent = assemblyModel.getComponents()
                .get(providedComponentName + ":1");
        final DeployedComponent providedDeployedComponent = deploymentModel.getContexts()
                .get(TestModelRepositoryUtils.CONTEXT_A).getComponents().get(providedComponentName + ":1");

        final ProvidedInterfaceType providedIFaceType = providedComponentType.getProvidedInterfaceTypes().get(0);
        final RequiredInterfaceType requiredIFaceType = TypeFactory.eINSTANCE.createRequiredInterfaceType();
        requiredIFaceType.setRequires(providedIFaceType);
        requiredComponentType.getRequiredInterfaceTypes().add(requiredIFaceType);

        final AssemblyProvidedInterface providedAssemblyIFace = providedAssemblyComponent.getProvidedInterfaces()
                .get(providedIFaceType.getSignature());
        final AssemblyRequiredInterface requiredAssemblyIFace = AssemblyFactory.eINSTANCE
                .createAssemblyRequiredInterface();
        requiredAssemblyIFace.setRequiredInterfaceType(requiredIFaceType);
        requiredAssemblyIFace.setRequires(providedAssemblyIFace);
        requiredAssemblyComponent.getRequiredInterfaces().add(requiredAssemblyIFace);

        final DeployedProvidedInterface providedDeployedIFace = providedDeployedComponent.getProvidedInterfaces()
                .get(providedIFaceType.getSignature());
        final DeployedRequiredInterface requiredDeployedIFace = DeploymentFactory.eINSTANCE
                .createDeployedRequiredInterface();
        requiredDeployedIFace.setRequiredInterface(requiredAssemblyIFace);
        requiredDeployedIFace.setRequires(providedDeployedIFace);
        requiredDeployedComponent.getRequiredInterfaces().add(requiredDeployedIFace);
    }
}
