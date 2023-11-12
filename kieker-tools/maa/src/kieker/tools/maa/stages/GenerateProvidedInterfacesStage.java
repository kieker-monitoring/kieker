/***************************************************************************
 * Copyright (C) 2022 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.maa.stages;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.analysis.util.Tuple;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.AssemblyProvidedInterface;
import kieker.model.analysismodel.assembly.AssemblyRequiredInterface;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedProvidedInterface;
import kieker.model.analysismodel.deployment.DeployedRequiredInterface;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.ProvidedInterfaceType;
import kieker.model.analysismodel.type.RequiredInterfaceType;
import kieker.model.analysismodel.type.TypeFactory;

import teetime.stage.basic.AbstractTransformation;

/**
 * Generate interfaces. TODO currently sub components are not supported.
 *
 * @author Reiner Jung
 * @since 1.3
 */
public class GenerateProvidedInterfacesStage extends
        AbstractTransformation<Tuple<ModelRepository, Map<ComponentType, Map<Set<ComponentType>, Set<OperationType>>>>, ModelRepository> {

    private int interfaceCount;

    @Override
    protected void execute(
            final Tuple<ModelRepository, Map<ComponentType, Map<Set<ComponentType>, Set<OperationType>>>> element) {
        final ModelRepository repository = element.getFirst();

        final Map<ComponentType, Map<Set<ComponentType>, Set<OperationType>>> interfaceMap = element.getSecond();

        final Map<ProvidedInterfaceType, Set<RequiredInterfaceType>> providedtoRequiredInterfaceTypeMap = this
                .createInterfaceTypes(interfaceMap);
        this.createAssemblyInterfaces(repository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL),
                providedtoRequiredInterfaceTypeMap);
        this.createDeploymentInterfaces(repository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL),
                providedtoRequiredInterfaceTypeMap);

        this.outputPort.send(repository);
    }

    private void createDeploymentInterfaces(final DeploymentModel deploymentModel,
            final Map<ProvidedInterfaceType, Set<RequiredInterfaceType>> providedtoRequiredInterfaceTypeMap) {
        deploymentModel.getContexts().values().forEach(context -> {
            context.getComponents().values().forEach(deployedComponent -> {
                deployedComponent.getAssemblyComponent().getProvidedInterfaces().values().forEach(providedInterface -> {
                    final DeployedProvidedInterface deployedProvidedInterface = this
                            .createDeploymentProvidedInterface(deployedComponent, providedInterface);
                    providedtoRequiredInterfaceTypeMap.get(providedInterface.getProvidedInterfaceType())
                            .forEach(requiredInterfaceType -> {
                                this.createRequiredInterfaceForDeploymentComponents(deploymentModel,
                                        requiredInterfaceType, deployedProvidedInterface);
                            });
                });
            });
        });
    }

    private void createRequiredInterfaceForDeploymentComponents(final DeploymentModel deploymentModel,
            final RequiredInterfaceType requiredInterfaceType,
            final DeployedProvidedInterface deployedProvidedInterface) {
        final ComponentType componentType = (ComponentType) requiredInterfaceType.eContainer();
        deploymentModel.getContexts().values().forEach(context -> {
            context.getComponents().values().forEach(deployedComponent -> {
                if (deployedComponent.getAssemblyComponent().getComponentType() == componentType) {
                    final Optional<AssemblyRequiredInterface> assemblyRequiredInterface = deployedComponent
                            .getAssemblyComponent().getRequiredInterfaces().stream()
                            .filter(requiredInterface -> requiredInterface
                                    .getRequiredInterfaceType() == requiredInterfaceType)
                            .findFirst();
                    final DeployedRequiredInterface requiredInterface = this.createRequiredDeployedInterface(
                            assemblyRequiredInterface.get(), deployedProvidedInterface);
                    deployedComponent.getRequiredInterfaces().add(requiredInterface);
                }
            });
        });
    }

    private DeployedRequiredInterface createRequiredDeployedInterface(
            final AssemblyRequiredInterface assemblyRequiredInterface,
            final DeployedProvidedInterface deployedProvidedInterface) {
        final DeployedRequiredInterface deployedRequiredInterface = DeploymentFactory.eINSTANCE
                .createDeployedRequiredInterface();
        deployedRequiredInterface.setRequiredInterface(assemblyRequiredInterface);
        deployedRequiredInterface.setRequires(deployedProvidedInterface);

        return deployedRequiredInterface;
    }

    private DeployedProvidedInterface createDeploymentProvidedInterface(final DeployedComponent deployedComponent,
            final AssemblyProvidedInterface providedInterface) {
        final DeployedProvidedInterface deployedProvidedInterface = DeploymentFactory.eINSTANCE
                .createDeployedProvidedInterface();
        deployedProvidedInterface.setProvidedInterface(providedInterface);
        deployedComponent.getProvidedInterfaces().put(providedInterface.getProvidedInterfaceType().getSignature(),
                deployedProvidedInterface);

        return deployedProvidedInterface;
    }

    private void createAssemblyInterfaces(final AssemblyModel assemblyModel,
            final Map<ProvidedInterfaceType, Set<RequiredInterfaceType>> providedtoRequiredInterfaceTypeMap) {
        assemblyModel.getComponents().values().forEach(assemblyComponent -> {
            assemblyComponent.getComponentType().getProvidedInterfaceTypes().forEach(providedInterfaceType -> {
                final AssemblyProvidedInterface providedInterface = this
                        .createAssemblyProvidedInterface(providedInterfaceType);
                assemblyComponent.getProvidedInterfaces().put(providedInterfaceType.getSignature(), providedInterface);

                providedtoRequiredInterfaceTypeMap.get(providedInterfaceType).forEach(requiredInterfaceType -> {
                    this.createRequiredInterfaceForAssemblyComponents(assemblyModel.getComponents().values(),
                            requiredInterfaceType, providedInterface);
                });
            });
        });
    }

    private void createRequiredInterfaceForAssemblyComponents(final Collection<AssemblyComponent> assemblyComponents,
            final RequiredInterfaceType requiredInterfaceType, final AssemblyProvidedInterface providedInterface) {
        final ComponentType componentType = (ComponentType) requiredInterfaceType.eContainer();
        assemblyComponents.forEach(assemblyComponent -> {
            if (assemblyComponent.getComponentType() == componentType) {
                final AssemblyRequiredInterface requiredInterface = this
                        .createRequiredAssemblyInterface(requiredInterfaceType, providedInterface);
                assemblyComponent.getRequiredInterfaces().add(requiredInterface);
            }
        });
    }

    private AssemblyRequiredInterface createRequiredAssemblyInterface(final RequiredInterfaceType requiredInterfaceType,
            final AssemblyProvidedInterface providedInterface) {
        final AssemblyRequiredInterface requiredInterface = AssemblyFactory.eINSTANCE.createAssemblyRequiredInterface();
        requiredInterface.setRequiredInterfaceType(requiredInterfaceType);
        requiredInterface.setRequires(providedInterface);

        return requiredInterface;
    }

    private AssemblyProvidedInterface createAssemblyProvidedInterface(
            final ProvidedInterfaceType providedInterfaceType) {

        final AssemblyProvidedInterface providedInterface = AssemblyFactory.eINSTANCE.createAssemblyProvidedInterface();
        providedInterface.setProvidedInterfaceType(providedInterfaceType);

        return providedInterface;
    }

    private Map<ProvidedInterfaceType, Set<RequiredInterfaceType>> createInterfaceTypes(
            final Map<ComponentType, Map<Set<ComponentType>, Set<OperationType>>> interfaceMap) {
        final Map<ProvidedInterfaceType, Set<RequiredInterfaceType>> providedToRequiredMap = new HashMap<>();

        interfaceMap.entrySet().forEach(calleeEntry -> {
            final ComponentType calleeComponentType = calleeEntry.getKey();
            calleeEntry.getValue().entrySet().forEach(callerSetEntry -> {
                this.createAndLinkTypeInterface(calleeComponentType, callerSetEntry, providedToRequiredMap);
            });
        });

        return providedToRequiredMap;
    }

    private void createAndLinkTypeInterface(final ComponentType calleeComponentType,
            final Entry<Set<ComponentType>, Set<OperationType>> callerSetEntry,
            final Map<ProvidedInterfaceType, Set<RequiredInterfaceType>> providedToRequiredMap) {
        final ProvidedInterfaceType providedInterfaceType = TypeFactory.eINSTANCE.createProvidedInterfaceType();
        providedInterfaceType.setName(String.format("IFace%d", this.interfaceCount++));
        providedInterfaceType.setPackage(calleeComponentType.getPackage());
        providedInterfaceType.setSignature(
                String.format("%s.%s", providedInterfaceType.getPackage(), providedInterfaceType.getName()));

        callerSetEntry.getValue().forEach(operationType -> providedInterfaceType.getProvidedOperationTypes()
                .put(operationType.getSignature(), operationType));

        final Set<RequiredInterfaceType> requiredInterfaceTypes = new HashSet<>();
        callerSetEntry.getKey().forEach(caller -> {
            requiredInterfaceTypes.add(this.createRequiredInterfaceType(caller, providedInterfaceType));
        });
        providedToRequiredMap.put(providedInterfaceType, requiredInterfaceTypes);
        calleeComponentType.getProvidedInterfaceTypes().add(providedInterfaceType);
    }

    private RequiredInterfaceType createRequiredInterfaceType(final ComponentType caller,
            final ProvidedInterfaceType providedInterface) {
        final RequiredInterfaceType requiredInterfaceType = TypeFactory.eINSTANCE.createRequiredInterfaceType();

        requiredInterfaceType.setRequires(providedInterface);
        caller.getRequiredInterfaceTypes().add(requiredInterfaceType);

        return requiredInterfaceType;
    }

}
