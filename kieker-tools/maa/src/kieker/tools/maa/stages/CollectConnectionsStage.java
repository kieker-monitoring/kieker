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
package kieker.tools.maa.stages;

import java.util.Collection;

import kieker.analysis.architecture.repository.ModelDescriptor;
import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.collection.CollectionFactory;
import kieker.model.collection.CollectionPackage;
import kieker.model.collection.Connections;
import kieker.model.collection.Coupling;
import kieker.model.collection.OperationCollection;

import teetime.stage.basic.AbstractTransformation;

/**
 * Identify interfaces in the model based on inter-module calls. An interface is defined as all
 * calls that occur from one external component.
 *
 * Component types in the model have a set of operations and on instance level, there can be calls
 * of one operations to other operations in other component types. In CS component types usually
 * have operations that are only used internally and those exposed in form of interfaces.
 *
 * To be able to derive interfaces based on the set of inter-component calls, we first collect all
 * calls from one component to one other. These triples of caller component, callee component, and
 * set of operations (Caller, Callee, { o_1, ..., o_n }) can have shared operations.
 *
 * Here different strategies can be applied, e.g.,: (a) The all including interface which represents
 * all exposed operations in one interface. (b) Generation of a set of disjunct operations sets,
 * i.e, if two operation sets have joint operations these are separated out in a new operation set
 * and removed from the two matching sets. Option (a) generates exactly one provided interface per
 * component where (b) will most likely fragmentize interfaces when on calles is only using a
 * portion of the interface while another component is using more of a set interface.
 *
 * @author Reiner Jung
 * @since 1.3
 */
public class CollectConnectionsStage extends AbstractTransformation<ModelRepository, ModelRepository> {

    @Override
    protected void execute(final ModelRepository repository) throws Exception {
        final ExecutionModel executionModel = repository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL);

        repository.register(
                new ModelDescriptor("connections.xmi", CollectionPackage.Literals.CONNECTIONS,
                        CollectionFactory.eINSTANCE),
                this.collectInterconnections(executionModel.getInvocations().values()));

        this.outputPort.send(repository);
    }

    private Connections collectInterconnections(final Collection<Invocation> invocations) {
        final Connections interconnections = CollectionFactory.eINSTANCE.createConnections();

        for (final Invocation invocation : invocations) {
            if (!invocation.getCaller().getComponent().equals(invocation.getCallee().getComponent())) { // only
                                                                                                        // inter-component
                                                                                                        // edges
                final Coupling key = CollectionFactory.eINSTANCE.createCoupling();
                key.setCaller(invocation.getCaller().getComponent().getAssemblyComponent().getComponentType());
                key.setCallee(invocation.getCallee().getComponent().getAssemblyComponent().getComponentType());

                OperationCollection calleeOperationCollection = interconnections.getConnections().get(key);

                if (calleeOperationCollection == null) {
                    calleeOperationCollection = CollectionFactory.eINSTANCE.createOperationCollection();
                    calleeOperationCollection.setCaller(key.getCaller());
                    calleeOperationCollection.setCallee(key.getCallee());
                    interconnections.getConnections().put(key, calleeOperationCollection);
                }

                final OperationType calleeOperation = invocation.getCallee().getAssemblyOperation().getOperationType();
                calleeOperationCollection.getOperations().put(calleeOperation.getSignature(), calleeOperation);
            }
        }

        return interconnections;
    }

}
