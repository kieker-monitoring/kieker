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
package org.oceandsl.tools.sar.stages.dataflow;

import java.util.Map.Entry;
import java.util.function.Function;

import org.eclipse.emf.ecore.EObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.architecture.recovery.events.DataflowEvent;
import kieker.analysis.architecture.recovery.events.OperationEvent;
import kieker.analysis.architecture.recovery.events.StorageEvent;
import kieker.analysis.statistics.StatisticsDecoratorStage;
import kieker.analysis.statistics.calculating.CountCalculator;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.OperationDataflow;
import kieker.model.analysismodel.execution.StorageDataflow;
import kieker.model.analysismodel.execution.Tuple;
import kieker.model.analysismodel.statistics.StatisticsModel;

import org.oceandsl.analysis.architecture.stages.CountUniqueCallsStage;

/**
 *
 * @author Reiner Jung
 * @since 1.1.0
 */
public class CountUniqueDataflowCallsStage extends StatisticsDecoratorStage<DataflowEvent> {

    private static final String DATAFLOW = "dataflow";

    public CountUniqueDataflowCallsStage(final StatisticsModel statisticsModel, final ExecutionModel executionModel) {
        super(statisticsModel, new CountCalculator<>(CountUniqueDataflowCallsStage.DATAFLOW),
                CountUniqueDataflowCallsStage.createForDataflow(executionModel));
    }

    public static final Function<DataflowEvent, EObject> createForDataflow(final ExecutionModel executionModel) {
        return dataflow -> { // NOCS
            final Tuple<DeployedOperation, DeployedStorage> storageTupleKey = CountUniqueDataflowCallsStage
                    .getStorageKeyTuple(dataflow, executionModel);
            if (storageTupleKey != null) {
                return CountUniqueDataflowCallsStage.getStorageDataflow(executionModel, storageTupleKey);
            } else {
                final OperationDataflow resultOperation = CountUniqueDataflowCallsStage.getOperationDataflow(
                        executionModel, CountUniqueDataflowCallsStage.getOperationKeyTuple(dataflow, executionModel));

                if (resultOperation == null) {
                    final Logger logger = LoggerFactory.getLogger(CountUniqueCallsStage.class);
                    logger.error("Fatal error: call not does not exist {}:{}", dataflow.getSource().toString(), // NOPMD
                                                                                                                // GuardLogStatement
                            dataflow.getTarget().toString());
                    return null;
                } else {
                    return resultOperation;
                }
            }
        };
    }

    private static Tuple<DeployedOperation, DeployedStorage> getStorageKeyTuple(final DataflowEvent dataflow,
            final ExecutionModel executionModel) {
        for (final Entry<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> entry : executionModel
                .getStorageDataflows().entrySet()) {
            final Tuple<DeployedOperation, DeployedStorage> key = entry.getKey();

            if (dataflow.getSource() instanceof OperationEvent && dataflow.getTarget() instanceof StorageEvent) {
                final OperationEvent operationEvent = (OperationEvent) dataflow.getSource();
                final StorageEvent storageEvent = (StorageEvent) dataflow.getTarget();
                if (key.getFirst().getComponent().getSignature().equals(operationEvent.getComponentSignature())
                        && key.getFirst().getAssemblyOperation().getOperationType().getSignature()
                                .equals(operationEvent.getOperationSignature())
                        && key.getSecond().getComponent().getSignature().equals(storageEvent.getComponentSignature())
                        && key.getSecond().getAssemblyStorage().getStorageType().getName()
                                .equals(storageEvent.getStorageSignature())) {
                    return key;
                }
            } else if (dataflow.getSource() instanceof StorageEvent && dataflow.getTarget() instanceof OperationEvent) {
                final OperationEvent operationEvent = (OperationEvent) dataflow.getTarget();
                final StorageEvent storageEvent = (StorageEvent) dataflow.getSource();
                if (key.getFirst().getAssemblyOperation().getOperationType().getSignature()
                        .equals(operationEvent.getOperationSignature())
                        && key.getFirst().getComponent().getSignature().equals(operationEvent.getComponentSignature())
                        && key.getSecond().getAssemblyStorage().getStorageType().getName()
                                .equals(storageEvent.getStorageSignature())
                        && key.getSecond().getComponent().getSignature().equals(storageEvent.getComponentSignature())) {
                    return key;
                }
            }
        }
        return null;
    }

    private static Tuple<DeployedOperation, DeployedOperation> getOperationKeyTuple(final DataflowEvent dataflow,
            final ExecutionModel executionModel) {
        for (final Entry<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> entry : executionModel
                .getOperationDataflows().entrySet()) {
            final Tuple<DeployedOperation, DeployedOperation> key = entry.getKey();

            if (dataflow.getSource() instanceof OperationEvent && dataflow.getTarget() instanceof OperationEvent) {
                final OperationEvent sourceOperationEvent = (OperationEvent) dataflow.getSource();
                final OperationEvent targetOperationEvent = (OperationEvent) dataflow.getTarget();
                if (key.getFirst().getAssemblyOperation().getOperationType().getSignature()
                        .equals(sourceOperationEvent.getOperationSignature())
                        && key.getFirst().getComponent().getSignature()
                                .equals(sourceOperationEvent.getComponentSignature())
                        && key.getSecond().getAssemblyOperation().getOperationType().getSignature()
                                .equals(targetOperationEvent.getOperationSignature())
                        && key.getSecond().getComponent().getSignature()
                                .equals(targetOperationEvent.getComponentSignature())) {
                    return key;
                }
            }
        }
        return null;
    }

    private static StorageDataflow getStorageDataflow(final ExecutionModel executionModel,
            final Tuple<DeployedOperation, DeployedStorage> key) {
        for (final Entry<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> ag : executionModel
                .getStorageDataflows()) {
            if (ag.getKey().hashCode() == key.hashCode()) {
                return ag.getValue();
            }
        }
        return null;
    }

    private static OperationDataflow getOperationDataflow(final ExecutionModel executionModel,
            final Tuple<DeployedOperation, DeployedOperation> key) {
        for (final Entry<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> ag : executionModel
                .getOperationDataflows()) {
            if (ag.getKey().hashCode() == key.hashCode()) {
                return ag.getValue();
            }
        }
        return null;
    }
}
