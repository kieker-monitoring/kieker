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
package kieker.tools.mop.merge;

import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.deployment.impl.EStringToDeployedComponentMapEntryImpl;
import kieker.model.analysismodel.deployment.impl.EStringToDeployedOperationMapEntryImpl;
import kieker.model.analysismodel.deployment.impl.EStringToDeployedStorageMapEntryImpl;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.OperationDataflow;
import kieker.model.analysismodel.execution.StorageDataflow;

/**
 * @author Reiner Jung
 * @since 1.1
 */
public final class ExecutionModelCloneUtils {

    private ExecutionModelCloneUtils() {
        // Utility class
    }

    public static Invocation duplicate(final DeploymentModel deploymentModel, final Invocation invocation) {
        final Invocation newInvocation = ExecutionFactory.eINSTANCE.createInvocation();
        newInvocation
                .setCaller(ExecutionModelCloneUtils.findDeployedOperation(deploymentModel, invocation.getCaller()));
        newInvocation
                .setCallee(ExecutionModelCloneUtils.findDeployedOperation(deploymentModel, invocation.getCallee()));

        return newInvocation;
    }

    public static StorageDataflow duplicate(final DeploymentModel deploymentModel,
            final StorageDataflow storageDataflow) {
        final StorageDataflow newStorageDataflow = ExecutionFactory.eINSTANCE.createStorageDataflow();
        newStorageDataflow.setDirection(storageDataflow.getDirection());

        newStorageDataflow
                .setCode(ExecutionModelCloneUtils.findDeployedOperation(deploymentModel, storageDataflow.getCode()));
        newStorageDataflow.setStorage(
                ExecutionModelCloneUtils.findDeployedStorage(deploymentModel, storageDataflow.getStorage()));

        return newStorageDataflow;
    }

    public static OperationDataflow duplicate(final DeploymentModel deploymentModel,
            final OperationDataflow operationDataflow) {
        final OperationDataflow newOperationDataflow = ExecutionFactory.eINSTANCE.createOperationDataflow();
        newOperationDataflow.setDirection(operationDataflow.getDirection());

        newOperationDataflow.setCaller(
                ExecutionModelCloneUtils.findDeployedOperation(deploymentModel, operationDataflow.getCaller()));
        newOperationDataflow.setCallee(
                ExecutionModelCloneUtils.findDeployedOperation(deploymentModel, operationDataflow.getCallee()));

        return newOperationDataflow;
    }

    private static DeployedOperation findDeployedOperation(final DeploymentModel targetModel,
            final DeployedOperation operation) {
        final EStringToDeployedOperationMapEntryImpl mapOperationEntry = (EStringToDeployedOperationMapEntryImpl) operation
                .eContainer();
        final DeployedComponent component = (DeployedComponent) mapOperationEntry.eContainer();

        final EStringToDeployedComponentMapEntryImpl mapComponentEntry = (EStringToDeployedComponentMapEntryImpl) component
                .eContainer();
        final DeploymentContext context = (DeploymentContext) mapComponentEntry.eContainer();

        final DeploymentContext newContext = targetModel.getContexts().get(context.getName());
        final DeployedComponent newComponent = newContext.getComponents().get(component.getSignature());

        return newComponent.getOperations().get(operation.getAssemblyOperation().getOperationType().getSignature());
    }

    private static DeployedStorage findDeployedStorage(final DeploymentModel targetModel,
            final DeployedStorage storage) {
        final EStringToDeployedStorageMapEntryImpl mapStorageEntry = (EStringToDeployedStorageMapEntryImpl) storage
                .eContainer();
        final DeployedComponent component = (DeployedComponent) mapStorageEntry.eContainer();

        final EStringToDeployedComponentMapEntryImpl mapComponentEntry = (EStringToDeployedComponentMapEntryImpl) component
                .eContainer();
        final DeploymentContext context = (DeploymentContext) mapComponentEntry.eContainer();

        final DeploymentContext newContext = targetModel.getContexts().get(context.getName());
        final DeployedComponent newComponent = newContext.getComponents().get(component.getSignature());
        return newComponent.getStorages().get(storage.getAssemblyStorage().getStorageType().getName());
    }

}
