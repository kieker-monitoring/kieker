/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
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
 * ==================================================
 */
package kieker.tpan.recordConsumer.executionRecordTransformation;

import java.util.ArrayList;
import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.common.logReader.RecordConsumerExecutionException;
import kieker.tpan.datamodel.system.AllocationComponentInstance;
import kieker.tpan.datamodel.system.AssemblyComponentInstance;
import kieker.tpan.datamodel.system.ComponentType;
import kieker.tpan.datamodel.system.Execution;
import kieker.tpan.datamodel.system.ExecutionContainer;
import kieker.tpan.datamodel.system.Operation;
import kieker.tpan.datamodel.system.Signature;
import kieker.tpan.datamodel.system.factories.SystemEntityFactory;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;

/**
 * Transforms KiekerExecutionRecords into Execution objects.
 *
 * @author Andre van Hoorn
 */
public class ExecutionRecordTransformer implements IKiekerRecordConsumer {

    private final SystemEntityFactory systemFactory;
    private final boolean considerExecutionContainer;

    private ArrayList<IExecutionListener> listeners =
            new ArrayList<IExecutionListener>();

    public ExecutionRecordTransformer(
            final SystemEntityFactory systemFactory,
            final boolean considerExecutionEnvironment) {
        this.systemFactory = systemFactory;
        this.considerExecutionContainer = considerExecutionEnvironment;
    }

    public String[] getRecordTypeSubscriptionList() {
        return new String[]{KiekerExecutionRecord.class.getName()};
    }

    public void addListener (IExecutionListener l){
        this.listeners.add(l);
    }

    public void consumeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) throws RecordConsumerExecutionException {
        if (!(monitoringRecord instanceof KiekerExecutionRecord)) {
            throw new RecordConsumerExecutionException("Can only process records of type"
                    + KiekerExecutionRecord.class.getName() + " but received" + monitoringRecord.getClass().getName());
        }
        KiekerExecutionRecord execRec = (KiekerExecutionRecord) monitoringRecord;

        String executionContainerName =
                (this.considerExecutionContainer) ? execRec.vmName : "DEFAULTCONTAINER";
        String componentTypeName = execRec.componentName;
        String assemblyComponentName = componentTypeName;
        String allocationComponentName =
                new StringBuilder(executionContainerName).append("::").append(assemblyComponentName).toString();
        String operationFactoryName =
                new StringBuilder(allocationComponentName).append(".").append(execRec.opname).toString();
        String operationName = execRec.opname;

        AllocationComponentInstance allocInst = this.systemFactory.getAllocationFactory().getAllocationComponentInstanceByFactoryIdentifier(allocationComponentName);
        if (allocInst == null) { /* Allocation component instance doesn't exist */
            AssemblyComponentInstance assemblyComponent =
                    this.systemFactory.getAssemblyFactory().getAssemblyComponentInstanceByFactoryIdentifier(assemblyComponentName);
            if (assemblyComponent == null) { // assembly instance doesn't exist
                ComponentType componentType =
                        this.systemFactory.getTypeRepositoryFactory().getComponentTypeByFactoryIdentifier(componentTypeName);
                if (componentType == null) {
                    /* Component type doesn't exist */
                    componentType = this.systemFactory.getTypeRepositoryFactory().createAndRegisterComponentType(componentTypeName, componentTypeName);
                }
                assemblyComponent = this.systemFactory.getAssemblyFactory().createAndRegisterAssemblyComponentInstance(assemblyComponentName, componentType);
            }
            ExecutionContainer execContainer = this.systemFactory.getExecutionEnvironmentFactory().getExecutionContainerByFactoryIdentifier(executionContainerName);
            if (execContainer == null){ /* doesn't exist, yet */
               execContainer = systemFactory.getExecutionEnvironmentFactory().createAndRegisterExecutionContainer(executionContainerName, executionContainerName);
            }
            allocInst = this.systemFactory.getAllocationFactory().createAndRegisterAllocationComponentInstance(allocationComponentName, assemblyComponent, execContainer);
        }

        Operation op = this.systemFactory.getOperationFactory().getOperationByFactoryIdentifier(operationFactoryName);
        if (op == null) { /* Operation doesn't exist */
            Signature signature = new Signature(operationName, "<>", new String[0]);
            op = this.systemFactory.getOperationFactory().createAndRegisterOperation(operationFactoryName, allocInst.getAssemblyComponent().getType(), signature);
        }

        Execution execution = new Execution(op, allocInst, execRec.traceId,
                execRec.sessionId, execRec.eoi, execRec.ess, execRec.tin, execRec.tout);
        for (IExecutionListener l : this.listeners){
            try {
                l.newExecutionEvent(execution);
            } catch (ExecutionEventProcessingException ex) {
                throw new RecordConsumerExecutionException("ExecutionEventProcessingException occured", ex);
            }
        }
    }

    public boolean execute() throws RecordConsumerExecutionException {
        return true;
    }

    public void terminate() {
        for (IExecutionListener l : this.listeners){
            l.terminate();
        }
    }
}
