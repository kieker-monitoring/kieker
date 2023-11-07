/***************************************************************************
 * Copyright (C) 2023 OceanDSL (https://oceandsl.uni-kiel.de)
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
package org.oceandsl.tools.cmi.stages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.OperationDataflow;
import kieker.model.analysismodel.execution.StorageDataflow;
import kieker.model.analysismodel.execution.Tuple;

import org.oceandsl.tools.cmi.RepositoryUtils;

/**
 * Check execution model.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CheckExecutionModelStage extends AbstractCollector<ModelRepository> {

    @Override
    protected void execute(final ModelRepository repository) throws Exception {
        final Report report = new Report("execution model");

        final ExecutionModel executionModel = repository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL);

        GenericCheckUtils.missingSignature(executionModel.eAllContents(), report);
        GenericCheckUtils.checkReferences(ExecutionPackage.Literals.EXECUTION_MODEL, executionModel.eAllContents(),
                report);

        this.checkExecutionInvocationIntegrity(executionModel, report);
        this.checkExecutionStorageDataflowIntegrity(executionModel, report);
        this.checkExecutionOperationDataflowIntegrity(executionModel, report);
        this.checkForDuplicateInvocations(executionModel, report);

        this.outputPort.send(repository);
        this.reportOutputPort.send(report);
    }

    private void checkExecutionInvocationIntegrity(final ExecutionModel model, final Report report) {
        long errors = 0;
        for (final Entry<Tuple<DeployedOperation, DeployedOperation>, Invocation> entry : model.getInvocations()) {
            final Tuple<DeployedOperation, DeployedOperation> tuple = entry.getKey();
            final Invocation invocation = entry.getValue();
            if (tuple.getFirst() != invocation.getCaller()) {
                report.addMessage("Caller does not match lookup key %s ++ %s",
                        RepositoryUtils.getName(tuple.getFirst()), RepositoryUtils.getName(invocation.getCaller()));

                errors++;
            }
            if (tuple.getSecond() != invocation.getCallee()) {
                report.addMessage("Callee does not match lookup key %s ++ %s",
                        RepositoryUtils.getName(tuple.getSecond()), RepositoryUtils.getName(invocation.getCallee()));

                final DeployedComponent keyComponent = tuple.getSecond().getComponent();
                final DeployedComponent targetComponent = invocation.getCallee().getComponent();
                if (keyComponent != targetComponent) { // NOPMD objects must not be identical
                    report.addMessage("Callee component does not match lookup key component %s ++ %s",
                            RepositoryUtils.getName(keyComponent), RepositoryUtils.getName(targetComponent));
                    final DeploymentContext keyContext = keyComponent.getContext();
                    final DeploymentContext targetContext = targetComponent.getContext();
                    if (keyContext != targetContext) { // NOPMD objects must not be identical
                        report.addMessage("Callee context does not match lookup key context %s ++ %s",
                                RepositoryUtils.getName(keyContext), RepositoryUtils.getName(targetContext));
                        if (keyContext.eContainer() != targetContext.eContainer()) {
                            report.addMessage("Duplicate deployment models: %s ++ %s", keyContext.eResource().getURI(),
                                    targetContext.eResource().getURI());
                        }
                    }
                }

                errors++;
            }
        }
        report.addMessage("Number of errors in execution model invocations %s", errors);
    }

    private void checkExecutionOperationDataflowIntegrity(final ExecutionModel model, final Report report) {
        long errors = 0;
        for (final Entry<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> entry : model
                .getOperationDataflows()) {
            final Tuple<DeployedOperation, DeployedOperation> tuple = entry.getKey();
            final OperationDataflow operationDataflow = entry.getValue();
            if (tuple.getFirst() != operationDataflow.getCaller()) {
                report.addMessage("Caller does not match %s:%s", // NOPMD
                        RepositoryUtils.getName(operationDataflow.getCaller().getComponent()),
                        RepositoryUtils.getName(operationDataflow.getCaller()));
                errors++;
            }
            if (tuple.getSecond() != operationDataflow.getCallee()) {
                report.addMessage("Storage does not match %s:%s",
                        RepositoryUtils.getName(operationDataflow.getCallee().getComponent()),
                        RepositoryUtils.getName(operationDataflow.getCallee()));
                errors++;
            }
        }
        report.addMessage("Number of errors in execution model operation dataflows %s", errors); // NOPMD
    }

    private void checkExecutionStorageDataflowIntegrity(final ExecutionModel model, final Report report) {
        long errors = 0;
        for (final Entry<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> entry : model
                .getStorageDataflows()) {
            final Tuple<DeployedOperation, DeployedStorage> tuple = entry.getKey();
            final StorageDataflow storageDataflow = entry.getValue();
            if (tuple.getFirst() != storageDataflow.getCode()) {
                report.addMessage("Caller does not match %s:%s", // NOPMD
                        RepositoryUtils.getName(storageDataflow.getCode().getComponent()),
                        RepositoryUtils.getName(storageDataflow.getCode()));
                errors++;
            }
            if (tuple.getSecond() != storageDataflow.getStorage()) {
                report.addMessage("Storage does not match %s:%s",
                        RepositoryUtils.getName(storageDataflow.getStorage().getComponent()),
                        RepositoryUtils.getName(storageDataflow.getStorage()));
                errors++;
            }
        }
        report.addMessage("Number of errors in execution model storage dataflows %s", errors);
    }

    private void checkForDuplicateInvocations(final ExecutionModel model, final Report report) {
        report.addMessage("Check for duplicate invocations based on DeployedOperation");
        final Map<DeployedOperation, Map<DeployedOperation, Invocation>> map = new HashMap<>();
        for (final Invocation invocation : model.getInvocations().values()) {
            Map<DeployedOperation, Invocation> targetMap = map.get(invocation.getCaller());
            if (targetMap == null) {
                targetMap = new HashMap<>();
                targetMap.put(invocation.getCallee(), invocation);
            } else if (targetMap.get(invocation.getCallee()) != null) {
                report.addMessage("Found duplicate %s -> %s",
                        invocation.getCaller().getAssemblyOperation().getOperationType().getName(),
                        invocation.getCallee().getAssemblyOperation().getOperationType().getName());
            }
        }

        report.addMessage("Check for duplicate invocations based on DeployedOperation names");
        final List<String> l = new ArrayList<>();
        for (final Invocation invocation : model.getInvocations().values()) {
            final String m = String.format("%s:%s:%s -> %s:%s:%s",
                    invocation.getCaller().getComponent().getContext().getName(),
                    invocation.getCaller().getComponent().getAssemblyComponent().getSignature(),
                    invocation.getCaller().getAssemblyOperation().getOperationType().getSignature(),
                    invocation.getCallee().getComponent().getContext().getName(),
                    invocation.getCallee().getComponent().getAssemblyComponent().getSignature(),
                    invocation.getCallee().getAssemblyOperation().getOperationType().getSignature());
            boolean g = false;
            for (final String x : l) {
                if (x.equals(m)) {
                    report.addMessage("Found duplicate %s", m); // NOPMD
                    g = true;
                }
            }
            if (!g) {
                l.add(m);
            }
        }
    }

}
