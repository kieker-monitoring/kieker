/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.sar.stages.dataflow;

import org.slf4j.Logger;

import kieker.analysis.architecture.recovery.assembler.AbstractModelAssembler;
import kieker.analysis.architecture.recovery.events.DataflowEvent;
import kieker.analysis.architecture.recovery.events.OperationEvent;
import kieker.analysis.architecture.recovery.events.StorageEvent;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.execution.EDirection;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.OperationDataflow;
import kieker.model.analysismodel.execution.StorageDataflow;
import kieker.model.analysismodel.execution.Tuple;
import kieker.model.analysismodel.source.SourceModel;

/**
 * @author Reiner Jung
 * @since 2.0.0
 */
public class DataflowExecutionModelAssembler extends AbstractModelAssembler<DataflowEvent> {

    private final ExecutionModel executionModel;
    private final DeploymentModel deploymentModel;
    private final Logger logger;

    public DataflowExecutionModelAssembler(final ExecutionModel executionModel, final DeploymentModel deploymentModel,
            final SourceModel sourceModel, final String sourceLabel, final Logger logger) {
        super(sourceModel, sourceLabel);
        this.executionModel = executionModel;
        this.deploymentModel = deploymentModel;
        this.logger = logger;
    }

    @Override
    public void assemble(final DataflowEvent event) {
        final DeploymentContext sourceContext = this.deploymentModel.getContexts().get(event.getSource().getHostname());
        final DeployedComponent callerComponent = sourceContext.getComponents()
                .get(event.getSource().getComponentSignature());
        if (callerComponent == null) {
            this.logger.error("Event refers to not existing caller component {}",
                    event.getSource().getComponentSignature());
            return;
        }

        final DeploymentContext targetContext = this.deploymentModel.getContexts().get(event.getTarget().getHostname());
        final DeployedComponent calleeComponent = targetContext.getComponents()
                .get(event.getTarget().getComponentSignature());
        if (calleeComponent == null) {
            this.logger.error("Event refers to not existing callee component {}",
                    event.getTarget().getComponentSignature());
            return;
        }

        if (event.getSource() instanceof OperationEvent) {
            final OperationEvent sourceOperationEvent = (OperationEvent) event.getSource();
            final DeployedOperation sourceOperation = callerComponent.getOperations()
                    .get(sourceOperationEvent.getOperationSignature());
            if (event.getTarget() instanceof OperationEvent) {
                final OperationEvent targetOperationEvent = (OperationEvent) event.getTarget();
                final DeployedOperation targetOperation = calleeComponent.getOperations()
                        .get(targetOperationEvent.getOperationSignature());
                this.addOperationDataflow(sourceOperation, targetOperation, event.getDirection());
            } else if (event.getTarget() instanceof StorageEvent) {
                final StorageEvent storageEvent = (StorageEvent) event.getTarget();
                final DeployedStorage targetStorage = calleeComponent.getStorages()
                        .get(storageEvent.getStorageSignature());
                this.addOperationStorageDataflow(sourceOperation, targetStorage, event.getDirection());
            } else {
                this.logger.error("Unsupported dataflow target type {}",
                        event.getTarget().getClass().getCanonicalName());
            }
        } else if (event.getSource() instanceof StorageEvent) {
            final StorageEvent storageEvent = (StorageEvent) event.getSource();
            final DeployedStorage sourceStorage = callerComponent.getStorages().get(storageEvent.getStorageSignature());
            if (event.getTarget() instanceof OperationEvent) {
                final OperationEvent targetOperationEvent = (OperationEvent) event.getTarget();
                final DeployedOperation targetOperation = calleeComponent.getOperations()
                        .get(targetOperationEvent.getOperationSignature());
                this.addOperationStorageDataflow(targetOperation, sourceStorage, this.invert(event.getDirection()));
            } else if (event.getTarget() instanceof StorageEvent) {
                this.logger.error("Storage to storage dataflow is not allowed {} -> {}", event.getSource().toString(),
                        event.getTarget().toString());
            } else {
                this.logger.error("Unsupported dataflow target type {}",
                        event.getTarget().getClass().getCanonicalName());
            }
        } else {
            this.logger.error("Unsupported dataflow source type {}", event.getTarget().getClass().getCanonicalName());
        }

    }

    private void addOperationDataflow(final DeployedOperation sourceOperation, final DeployedOperation targetOperation,
            final EDirection direction) {
        final Tuple<DeployedOperation, DeployedOperation> key = ExecutionFactory.eINSTANCE.createTuple();

        key.setFirst(sourceOperation);
        key.setSecond(targetOperation);
        this.updateSourceModel(key);

        this.createOperationDataflow(key, sourceOperation, targetOperation, direction);
    }

    private void addOperationStorageDataflow(final DeployedOperation operation, final DeployedStorage storage,
            final EDirection direction) {
        final Tuple<DeployedOperation, DeployedStorage> key = ExecutionFactory.eINSTANCE.createTuple();
        key.setFirst(operation);
        key.setSecond(storage);
        this.updateSourceModel(key);

        this.createStorageDataflow(key, operation, storage, direction);
    }

    /**
     *
     * @param key
     *            the created Access should be stored in.
     * @param sourceOperation
     *            of the dataflow step, stored in Deployment model
     * @param accessedStorage
     *            of the dataflow step, stored in Deployment model
     * @param direction
     *            dataflow direction
     */
    private void createStorageDataflow(final Tuple<DeployedOperation, DeployedStorage> key,
            final DeployedOperation sourceOperation, final DeployedStorage accessedStorage,
            final EDirection direction) {
        final StorageDataflow storageDataflow = ExecutionFactory.eINSTANCE.createStorageDataflow();
        storageDataflow.setCode(sourceOperation);
        storageDataflow.setStorage(accessedStorage);
        storageDataflow.setDirection(direction);

        this.executionModel.getStorageDataflows().put(key, storageDataflow);
        this.updateSourceModel(key);
    }

    /**
     *
     * @param key
     *            the created Access should be stored in.
     * @param sourceOperation
     *            of the dataflow step, stored in Deployment model
     * @param targetOperation
     *            of the dataflow step, stored in Deployment model
     * @param direction
     *            dataflow direction
     */
    private void createOperationDataflow(final Tuple<DeployedOperation, DeployedOperation> key,
            final DeployedOperation sourceOperation, final DeployedOperation targetOperation,
            final EDirection direction) {
        final OperationDataflow operationDataflow = ExecutionFactory.eINSTANCE.createOperationDataflow();
        operationDataflow.setCaller(sourceOperation);
        operationDataflow.setCallee(targetOperation);
        operationDataflow.setDirection(direction);

        this.executionModel.getOperationDataflows().put(key, operationDataflow);
        this.updateSourceModel(key);
    }

    private EDirection invert(final EDirection direction) {
        switch (direction) {
        case READ:
            return EDirection.WRITE;
        case WRITE:
            return EDirection.READ;
        case BOTH:
            return EDirection.BOTH;
        default:
            throw new InternalError("Unknown direction type found " + direction.name());
        }
    }

}
