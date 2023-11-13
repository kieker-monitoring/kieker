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
package kieker.tools.mop.merge;

import java.util.Map.Entry;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.assembly.AssemblyStorage;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentFactory;

/**
 * @author Reiner Jung
 * @since 2.0.0
 */
public final class DeploymentModelCloneUtils {

    private DeploymentModelCloneUtils() {
        // private constructor for utils
    }

    public static DeploymentContext duplicate(final AssemblyModel assemblyModel,
            final DeploymentContext deploymentContext) {
        final DeploymentContext newContext = DeploymentFactory.eINSTANCE.createDeploymentContext();
        newContext.setName(deploymentContext.getName());

        for (final Entry<String, DeployedComponent> component : deploymentContext.getComponents()) {
            newContext.getComponents().put(component.getKey(),
                    DeploymentModelCloneUtils.duplicate(assemblyModel, component.getValue()));
        }

        return newContext;
    }

    public static DeployedComponent duplicate(final AssemblyModel assemblyModel, final DeployedComponent component) {
        final DeployedComponent newComponent = DeploymentFactory.eINSTANCE.createDeployedComponent();
        newComponent.setSignature(component.getSignature());
        newComponent.setAssemblyComponent(DeploymentModelCloneUtils.findAssemblyComponent(assemblyModel,
                component.getAssemblyComponent().getSignature()));

        for (final Entry<String, DeployedOperation> operation : component.getOperations()) {
            newComponent.getOperations().put(operation.getKey(),
                    DeploymentModelCloneUtils.duplicate(newComponent.getAssemblyComponent(), operation.getValue()));
        }

        for (final Entry<String, DeployedStorage> storage : component.getStorages()) {
            newComponent.getStorages().put(storage.getKey(),
                    DeploymentModelCloneUtils.duplicate(newComponent.getAssemblyComponent(), storage.getValue()));
        }

        return newComponent;
    }

    public static DeployedStorage duplicate(final AssemblyComponent assemblyComponent, final DeployedStorage storage) {
        final DeployedStorage newStorage = DeploymentFactory.eINSTANCE.createDeployedStorage();
        newStorage.setAssemblyStorage(DeploymentModelCloneUtils.findAssemblyStorage(assemblyComponent,
                storage.getAssemblyStorage().getStorageType().getName()));
        return newStorage;
    }

    public static DeployedOperation duplicate(final AssemblyComponent assemblyComponent,
            final DeployedOperation operation) {
        final DeployedOperation newOperation = DeploymentFactory.eINSTANCE.createDeployedOperation();
        newOperation.setAssemblyOperation(DeploymentModelCloneUtils.findAssemblyOperation(assemblyComponent,
                operation.getAssemblyOperation().getOperationType().getSignature()));
        return newOperation;
    }

    private static AssemblyStorage findAssemblyStorage(final AssemblyComponent assemblyComponent, final String name) {
        return assemblyComponent.getStorages().get(name);
    }

    private static AssemblyOperation findAssemblyOperation(final AssemblyComponent assemblyComponent,
            final String signature) {
        return assemblyComponent.getOperations().get(signature);
    }

    private static AssemblyComponent findAssemblyComponent(final AssemblyModel assemblyModel, final String signature) {
        return assemblyModel.getComponents().get(signature);
    }

}
