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

import org.eclipse.emf.common.util.EMap;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.execution.EDirection;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.OperationDataflow;
import kieker.model.analysismodel.execution.StorageDataflow;
import kieker.model.analysismodel.execution.Tuple;

/**
 * @author Reiner Jung
 * @since 2.0.0
 */
public final class ExecutionModelMerger {

	private ExecutionModelMerger() {
		// Utility class
	}

	/* default */ static void mergeExecutionModel(final DeploymentModel deploymentModel, // NOPMD
			final ExecutionModel lastModel, final DeploymentModel mergeDeploymentModel,
			final ExecutionModel mergeModel) {
		ExecutionModelMerger.mergeInvocations(deploymentModel, lastModel, mergeDeploymentModel, mergeModel);
		ExecutionModelMerger.mergeStorageDataflows(deploymentModel, lastModel, mergeModel);
		ExecutionModelMerger.mergeOperationDataflows(deploymentModel, lastModel, mergeModel);
	}

	private static void mergeInvocations(final DeploymentModel deploymentModel, final ExecutionModel lastModel,
			final DeploymentModel mergeDeploymentModel, final ExecutionModel mergeModel) {
		// checkExecution(lastModel, "LAST");
		// checkExecution(mergeModel, "MERGE");
		// checkDeployment(deploymentModel, "LAST");
		// checkDeployment(mergeDeploymentModel, "MERGE");
		// checkWhereResourceAreFrom(mergeDeploymentModel, mergeModel, "MERGE");
		for (final Entry<Tuple<DeployedOperation, DeployedOperation>, Invocation> entry : mergeModel.getInvocations()) {
			if (!ExecutionModelMerger.compareTupleOperationKeys(lastModel.getInvocations(), entry.getKey())) {
				final Invocation value = ExecutionModelCloneUtils.duplicate(deploymentModel, entry.getValue());
				final Tuple<DeployedOperation, DeployedOperation> key = ExecutionFactory.eINSTANCE.createTuple();
				key.setFirst(value.getCaller());
				key.setSecond(value.getCallee());
				lastModel.getInvocations().put(key, value);
			}
		}
	}

	private static boolean compareTupleOperationKeys(
			final EMap<Tuple<DeployedOperation, DeployedOperation>, Invocation> invocations,
			final Tuple<DeployedOperation, DeployedOperation> searchKey) {
		for (final Tuple<DeployedOperation, DeployedOperation> invocationKey : invocations.keySet()) {
			if (ModelUtils.isEqual(invocationKey.getFirst(), searchKey.getFirst())
					&& ModelUtils.isEqual(invocationKey.getSecond(), searchKey.getSecond())) {
				return true;
			}
		}
		return false;
	}

	private static void mergeStorageDataflows(final DeploymentModel deploymentModel, final ExecutionModel lastModel,
			final ExecutionModel mergeModel) {
		for (final Entry<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> entry : mergeModel
				.getStorageDataflows()) {
			final Tuple<DeployedOperation, DeployedStorage> lastModelKey = ExecutionModelMerger
					.findTupleStorageKeys(lastModel.getStorageDataflows(), entry.getKey());
			ExecutionModelMerger.mergeStorageDataflow(lastModelKey, deploymentModel, lastModel, entry.getValue());
		}
	}

	private static void mergeStorageDataflow(final Tuple<DeployedOperation, DeployedStorage> lastModelKey,
			final DeploymentModel deploymentModel, final ExecutionModel lastModel,
			final StorageDataflow sourceStorageDataflow) {
		if (lastModelKey == null) {
			ExecutionModelMerger.mergeStorageDataflowNotExistingInlastModel(deploymentModel, lastModel,
					sourceStorageDataflow);
		} else {
			ExecutionModelMerger.mergeStorageDataflowExistingInTarget(lastModel, lastModelKey, sourceStorageDataflow);
		}
	}

	private static void mergeStorageDataflowNotExistingInlastModel(final DeploymentModel deploymentModel,
			final ExecutionModel lastModel, final StorageDataflow sourceStorageDataflow) {
		final StorageDataflow value = ExecutionModelCloneUtils.duplicate(deploymentModel, sourceStorageDataflow);
		final Tuple<DeployedOperation, DeployedStorage> key = ExecutionFactory.eINSTANCE.createTuple();
		key.setFirst(value.getCode());
		key.setSecond(value.getStorage());
		lastModel.getStorageDataflows().put(key, value);
	}

	private static void mergeStorageDataflowExistingInTarget(final ExecutionModel lastModel,
			final Tuple<DeployedOperation, DeployedStorage> lastModelKey, final StorageDataflow sourceStorageDataflow) {
		final StorageDataflow targetStorageDataflow = lastModel.getStorageDataflows().get(lastModelKey);
		switch (sourceStorageDataflow.getDirection()) {
		case READ:
			if (targetStorageDataflow.getDirection() == EDirection.WRITE) {
				targetStorageDataflow.setDirection(EDirection.BOTH);
			}
			break;
		case WRITE:
			if (targetStorageDataflow.getDirection() == EDirection.READ) {
				targetStorageDataflow.setDirection(EDirection.BOTH);
			}
			break;
		case BOTH:
			targetStorageDataflow.setDirection(EDirection.BOTH);
			break;
		default:
			throw new InternalError(
					"Found unsupported direction type " + sourceStorageDataflow.getDirection().getName());
		}
		lastModel.getStorageDataflows().put(lastModelKey, targetStorageDataflow);
	}

	private static Tuple<DeployedOperation, DeployedStorage> findTupleStorageKeys(
			final EMap<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> storageDataflows,
			final Tuple<DeployedOperation, DeployedStorage> key) {
		for (final Tuple<DeployedOperation, DeployedStorage> invocationKey : storageDataflows.keySet()) {
			if (ModelUtils.isEqual(invocationKey.getFirst(), key.getFirst())
					&& ModelUtils.isEqual(invocationKey.getSecond(), key.getSecond())) {
				return invocationKey;
			}
		}
		return null;
	}

	private static void mergeOperationDataflows(final DeploymentModel deploymentModel, final ExecutionModel lastModel,
			final ExecutionModel mergeModel) {
		for (final Entry<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> entry : mergeModel
				.getOperationDataflows()) {
			final Tuple<DeployedOperation, DeployedOperation> lastModelKey = ExecutionModelMerger
					.findTupleOperationKeys(lastModel.getOperationDataflows(), entry.getKey());
			final OperationDataflow sourceOperationDataflow = entry.getValue();
			if (lastModelKey == null) {
				ExecutionModelMerger.mergeOperationDataflowNotExistingInlastModel(deploymentModel, lastModel,
						sourceOperationDataflow);
			} else {
				ExecutionModelMerger.mergeOperationDataflowExistingInTarget(lastModel, lastModelKey,
						sourceOperationDataflow);
			}
		}
	}

	private static void mergeOperationDataflowNotExistingInlastModel(final DeploymentModel deploymentModel,
			final ExecutionModel lastModel, final OperationDataflow sourceOperationDataflow) {
		final OperationDataflow value = ExecutionModelCloneUtils.duplicate(deploymentModel, sourceOperationDataflow);
		final Tuple<DeployedOperation, DeployedOperation> key = ExecutionFactory.eINSTANCE.createTuple();
		key.setFirst(value.getCaller());
		key.setSecond(value.getCallee());
		lastModel.getOperationDataflows().put(key, value);
	}

	private static void mergeOperationDataflowExistingInTarget(final ExecutionModel lastModel,
			final Tuple<DeployedOperation, DeployedOperation> lastModelKey,
			final OperationDataflow sourceOperationDataflow) {
		final OperationDataflow targetOperationDataflow = lastModel.getOperationDataflows().get(lastModelKey);
		switch (sourceOperationDataflow.getDirection()) {
		case READ:
			if (targetOperationDataflow.getDirection() == EDirection.WRITE) {
				targetOperationDataflow.setDirection(EDirection.BOTH);
			}
			break;
		case WRITE:
			if (targetOperationDataflow.getDirection() == EDirection.READ) {
				targetOperationDataflow.setDirection(EDirection.BOTH);
			}
			break;
		case BOTH:
			targetOperationDataflow.setDirection(EDirection.BOTH);
			break;
		default:
			throw new InternalError(
					"Found unsupported direction type " + sourceOperationDataflow.getDirection().getName());
		}
		lastModel.getOperationDataflows().put(lastModelKey, targetOperationDataflow);
	}

	private static Tuple<DeployedOperation, DeployedOperation> findTupleOperationKeys(
			final EMap<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> operationDataflows,
			final Tuple<DeployedOperation, DeployedOperation> tuple) {
		for (final Tuple<DeployedOperation, DeployedOperation> invocationKey : operationDataflows.keySet()) {
			if (ModelUtils.isEqual(invocationKey.getFirst(), tuple.getFirst())
					&& ModelUtils.isEqual(invocationKey.getSecond(), tuple.getSecond())) {
				return invocationKey;
			}
		}
		return null;
	}
}
