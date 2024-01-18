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
package kieker.analysis.architecture.recovery.storage;

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
 * Create execution model dataflow entries.
 *
 * @author Reiner Jung
 * @author Yannick Illmann
 * @since 1.3.0
 */
public class ExecutionModelDataflowAssemblerStage extends AbstractDataflowAssemblerStage<DataflowEvent, DataflowEvent> {

	private final ExecutionModel executionModel;
	private final DeploymentModel deploymentModel;

	public ExecutionModelDataflowAssemblerStage(final ExecutionModel executionModel,
			final DeploymentModel deploymentModel, final SourceModel sourceModel, final String sourceLabel) {
		super(sourceModel, sourceLabel);
		this.executionModel = executionModel;
		this.deploymentModel = deploymentModel;
	}

	@Override
	protected void execute(final DataflowEvent element) throws Exception {
		final DeploymentContext sourceContext = this.deploymentModel.getContexts()
				.get(element.getSource().getHostname());
		final DeployedComponent callerComponent = sourceContext.getComponents()
				.get(element.getSource().getComponentSignature());

		final DeploymentContext targetContext = this.deploymentModel.getContexts()
				.get(element.getTarget().getHostname());
		final DeployedComponent calleeComponent = targetContext.getComponents()
				.get(element.getTarget().getComponentSignature());

		if (element.getSource() instanceof OperationEvent) {
			final OperationEvent sourceOperationEvent = (OperationEvent) element.getSource();
			final DeployedOperation sourceOperation = callerComponent.getOperations()
					.get(sourceOperationEvent.getOperationSignature());
			if (element.getTarget() instanceof OperationEvent) {
				final OperationEvent targetOperationEvent = (OperationEvent) element.getTarget();
				final DeployedOperation targetOperation = calleeComponent.getOperations()
						.get(targetOperationEvent.getOperationSignature());
				this.addOperationDataflow(sourceOperation, targetOperation, element.getDirection());
			} else if (element.getTarget() instanceof StorageEvent) {
				final StorageEvent storageEvent = (StorageEvent) element.getTarget();
				final DeployedStorage targetStorage = calleeComponent.getStorages()
						.get(storageEvent.getStorageSignature());
				this.addOperationStorageDataflow(sourceOperation, targetStorage, element.getDirection());
			} else {
				this.logger.error("Unsupported dataflow target type {}",
						element.getTarget().getClass().getCanonicalName());
			}
		} else if (element.getSource() instanceof StorageEvent) {
			final StorageEvent storageEvent = (StorageEvent) element.getSource();
			final DeployedStorage sourceStorage = callerComponent.getStorages().get(storageEvent.getStorageSignature());
			if (element.getTarget() instanceof OperationEvent) {
				final OperationEvent targetOperationEvent = (OperationEvent) element.getTarget();
				final DeployedOperation targetOperation = calleeComponent.getOperations()
						.get(targetOperationEvent.getOperationSignature());
				this.addOperationStorageDataflow(targetOperation, sourceStorage, this.invert(element.getDirection()));
			} else if (element.getTarget() instanceof StorageEvent) {
				this.logger.error("Storage to storage dataflow is not allowed {} -> {}", element.getSource().toString(),
						element.getTarget().toString());
			} else {
				this.logger.error("Unsupported dataflow target type {}",
						element.getTarget().getClass().getCanonicalName());
			}
		} else {
			this.logger.error("Unsupported dataflow source type {}", element.getTarget().getClass().getCanonicalName());
		}

		this.outputPort.send(element);
	}

	private void addOperationDataflow(final DeployedOperation sourceOperation, final DeployedOperation targetOperation,
			final EDirection direction) {
		final Tuple<DeployedOperation, DeployedOperation> key = ExecutionFactory.eINSTANCE.createTuple();

		key.setFirst(sourceOperation);
		key.setSecond(targetOperation);
		this.addObjectToSource(key);

		this.createOperationDataflow(key, sourceOperation, targetOperation, direction);
	}

	private void addOperationStorageDataflow(final DeployedOperation operation, final DeployedStorage storage,
			final EDirection direction) {
		final Tuple<DeployedOperation, DeployedStorage> key = ExecutionFactory.eINSTANCE.createTuple();
		key.setFirst(operation);
		key.setSecond(storage);
		this.addObjectToSource(key);

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
		this.addObjectToSource(storageDataflow);
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
		this.addObjectToSource(operationDataflow);
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
