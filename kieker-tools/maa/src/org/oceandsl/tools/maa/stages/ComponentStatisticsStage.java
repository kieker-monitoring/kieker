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
package org.oceandsl.tools.maa.stages;

import java.util.Collection;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;

import teetime.stage.basic.AbstractTransformation;

import org.oceandsl.analysis.generic.Table;

/**
 * Calculate the size of a component by number of operations, how many of its operations are called
 * and how many external operations are calley by the component.
 *
 * @author Reiner Jung
 * @since 1.4
 */
public class ComponentStatisticsStage
        extends AbstractTransformation<ModelRepository, Table<String, ComponentStatistics>> {

    @Override
    protected void execute(final ModelRepository element) throws Exception {
        final AssemblyModel assemblyModel = element.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL);
        final ExecutionModel executionModel = element.getModel(ExecutionPackage.Literals.EXECUTION_MODEL);
        final Table<String, ComponentStatistics> table = new Table<>("component-statistics");

        int i = 0;
        for (final AssemblyComponent component : assemblyModel.getComponents().values()) {
            table.getRows()
                    .add(new ComponentStatistics(
                            String.format("%s::%d", component.getComponentType().getSignature(), i++),
                            component.getOperations().size(),
                            this.countAllProvidedOperations(component, component.getOperations().values(),
                                    executionModel.getInvocations().values()),
                            this.countAllRequiredOperations(component, component.getOperations().values(),
                                    executionModel.getInvocations().values())));
        }
        this.outputPort.send(table);
    }

    private Long countAllProvidedOperations(final AssemblyComponent component,
            final Collection<AssemblyOperation> operations, final Collection<Invocation> invocations) {
        return operations.stream().filter(operation -> this.isCalledExternally(component, operation, invocations))
                .count();
    }

    private boolean isCalledExternally(final AssemblyComponent component, final AssemblyOperation operation,
            final Collection<Invocation> invocations) {
        return invocations.stream()
                .anyMatch(invocation -> invocation.getCallee().getAssemblyOperation().equals(operation)
                        && (invocation.getCaller().getComponent().getAssemblyComponent() != component));
    }

    private Long countAllRequiredOperations(final AssemblyComponent component,
            final Collection<AssemblyOperation> operations, final Collection<Invocation> invocations) {
        return operations.stream().filter(operation -> this.callsExternally(component, operation, invocations)).count();
    }

    private boolean callsExternally(final AssemblyComponent component, final AssemblyOperation operation,
            final Collection<Invocation> invocations) {
        return invocations.stream()
                .anyMatch(invocation -> invocation.getCaller().getAssemblyOperation().equals(operation)
                        && (invocation.getCallee().getComponent().getAssemblyComponent() != component));
    }

}
