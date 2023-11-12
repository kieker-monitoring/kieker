/***************************************************************************
 * Copyright (C) 2022 OceanDSL (https://oceandsl.uni-kiel.de)
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

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;

import teetime.stage.basic.AbstractTransformation;

import org.oceandsl.analysis.generic.Table;

/**
 * @author Reiner Jung
 * @since 1.4
 *
 */
public class OperationCallsStage extends AbstractTransformation<ModelRepository, Table<String, CallEntry>> {

    @Override
    protected void execute(final ModelRepository element) throws Exception {
        final Table<String, CallEntry> result = new Table<>("operation-calls");

        final ExecutionModel executionModel = element.getModel(ExecutionPackage.Literals.EXECUTION_MODEL);

        executionModel.getInvocations().values().forEach(invocation -> {
            final DeployedOperation caller = invocation.getCaller();
            final DeployedOperation callee = invocation.getCallee();
            final Integer numOfCalls = 0;

            final String callerComponent = this.getComponentName(caller);
            final String callerSignature = caller.getAssemblyOperation().getOperationType().getSignature();
            final String calleeComponent = this.getComponentName(callee);
            final String calleeSignature = callee.getAssemblyOperation().getOperationType().getSignature();
            result.getRows()
                    .add(new CallEntry(callerComponent, callerSignature, calleeComponent, calleeSignature, numOfCalls));

        });

        this.outputPort.send(result);
    }

    private String getComponentName(final DeployedOperation caller) {
        final DeploymentContext context = caller.getComponent().getContext();
        return String.format("%s:%s::%d", context.getName(),
                caller.getComponent().getAssemblyComponent().getComponentType().getSignature(),
                this.getPosition(context, caller.getComponent()));
    }

    private int getPosition(final DeploymentContext context, final DeployedComponent component) {
        int i = 0;
        for (final DeployedComponent element : context.getComponents().values()) {
            if (element.equals(component)) {
                return i;
            }
            i++;
        }
        return -1;
    }

}
