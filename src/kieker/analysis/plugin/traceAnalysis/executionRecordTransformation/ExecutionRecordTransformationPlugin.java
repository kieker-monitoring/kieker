package kieker.analysis.plugin.traceAnalysis.executionRecordTransformation;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import kieker.common.record.IMonitoringRecord;
import kieker.analysis.datamodel.AllocationComponent;
import kieker.analysis.datamodel.AssemblyComponent;
import kieker.analysis.datamodel.ComponentType;
import kieker.analysis.datamodel.Execution;
import kieker.analysis.datamodel.ExecutionContainer;
import kieker.analysis.datamodel.Operation;
import kieker.analysis.datamodel.Signature;
import kieker.analysis.datamodel.repository.SystemModelRepository;
import kieker.common.record.OperationExecutionRecord;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.plugin.traceAnalysis.AbstractTraceAnalysisPlugin;
import kieker.analysis.plugin.util.event.EventProcessingException;
import kieker.analysis.plugin.util.event.EventPublishSubscribeConnector;
import kieker.analysis.plugin.util.event.IEventListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Transforms KiekerExecutionRecords into Execution objects.
 *
 * @author Andre van Hoorn
 */
public class ExecutionRecordTransformationPlugin extends AbstractTraceAnalysisPlugin implements IMonitoringRecordConsumerPlugin, IExecutionEventProvider {

    private static final Log log = LogFactory.getLog(ExecutionRecordTransformationPlugin.class);
    private final EventPublishSubscribeConnector<Execution> executionPublishingSystem =
            new EventPublishSubscribeConnector<Execution>(false); // do not fail fast

    public ExecutionRecordTransformationPlugin(
            final String name,
            final SystemModelRepository systemFactory) {
        super(name, systemFactory);
    }
    private final static Collection<Class<? extends IMonitoringRecord>> recordTypeSubscriptionList =
            new ArrayList<Class<? extends IMonitoringRecord>>();

    static {
        recordTypeSubscriptionList.add(OperationExecutionRecord.class);
    }

    public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
        return recordTypeSubscriptionList;
    }

    public void addListener(IEventListener<Execution> listener) {
        this.executionPublishingSystem.addListener(listener);
    }

    private Signature createSignature(final String operationSignatureStr) {
        String returnType = "N/A";
        String name;
        String[] paramTypeList;
        int openParenIdx = operationSignatureStr.indexOf('(');
        if (openParenIdx == -1) { // no parameter list
            paramTypeList = new String[]{};
            name = operationSignatureStr;
        } else {
            name = operationSignatureStr.substring(0, openParenIdx);
            StringTokenizer strTokenizer =
                    new StringTokenizer(operationSignatureStr.substring(openParenIdx + 1, operationSignatureStr.length() - 1), ",");
            paramTypeList = new String[strTokenizer.countTokens()];
            for (int i = 0; strTokenizer.hasMoreTokens(); i++) {
                paramTypeList[i] = strTokenizer.nextToken().trim();
            }
        }

        return new Signature(name, returnType, paramTypeList);
    }

    public boolean newMonitoringRecord(IMonitoringRecord record) {
        if (!(record instanceof OperationExecutionRecord)) {
            log.error("Can only process records of type"
                    + OperationExecutionRecord.class.getName() + " but received" + record.getClass().getName());
        }
        OperationExecutionRecord execRec = (OperationExecutionRecord) record;

        String executionContainerName = execRec.vmName;
        //(this.considerExecutionContainer) ? execRec.vmName : "DEFAULTCONTAINER";
        String componentTypeName = execRec.componentName;
        String assemblyComponentName = componentTypeName;
        String allocationComponentName =
                new StringBuilder(executionContainerName).append("::").append(assemblyComponentName).toString();
        String operationFactoryName =
                new StringBuilder(assemblyComponentName).append(".").append(execRec.opname).toString();
        String operationSignatureStr = execRec.opname;

        AllocationComponent allocInst = this.getSystemEntityFactory().getAllocationFactory().getAllocationComponentInstanceByFactoryIdentifier(allocationComponentName);
        if (allocInst == null) { /* Allocation component instance doesn't exist */
            AssemblyComponent assemblyComponent =
                    this.getSystemEntityFactory().getAssemblyFactory().getAssemblyComponentInstanceByFactoryIdentifier(assemblyComponentName);
            if (assemblyComponent == null) { // assembly instance doesn't exist
                ComponentType componentType =
                        this.getSystemEntityFactory().getTypeRepositoryFactory().getComponentTypeByFactoryIdentifier(componentTypeName);
                if (componentType == null) {
                    /* Component type doesn't exist */
                    componentType = this.getSystemEntityFactory().getTypeRepositoryFactory().createAndRegisterComponentType(componentTypeName, componentTypeName);
                }
                assemblyComponent = this.getSystemEntityFactory().getAssemblyFactory().createAndRegisterAssemblyComponentInstance(assemblyComponentName, componentType);
            }
            ExecutionContainer execContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory().getExecutionContainerByFactoryIdentifier(executionContainerName);
            if (execContainer == null) { /* doesn't exist, yet */
                execContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory().createAndRegisterExecutionContainer(executionContainerName, executionContainerName);
            }
            allocInst = this.getSystemEntityFactory().getAllocationFactory().createAndRegisterAllocationComponentInstance(allocationComponentName, assemblyComponent, execContainer);
        }

        Operation op = this.getSystemEntityFactory().getOperationFactory().getOperationByFactoryIdentifier(operationFactoryName);
        if (op == null) { /* Operation doesn't exist */
            Signature signature = createSignature(operationSignatureStr);
            op = this.getSystemEntityFactory().getOperationFactory().createAndRegisterOperation(operationFactoryName, allocInst.getAssemblyComponent().getType(), signature);
            allocInst.getAssemblyComponent().getType().addOperation(op);
        }

        Execution execution = new Execution(op, allocInst, execRec.traceId,
                execRec.sessionId, execRec.eoi, execRec.ess, execRec.tin, execRec.tout);
        try {
            this.executionPublishingSystem.publish(execution);
        } catch (EventProcessingException ex) {
            log.error("Failed to publish execution", ex);
            return false;
        }
        return true;
    }

    public boolean execute() {
        return true;
    }

    public void terminate(final boolean error) {
//        for (IExecutionListener l : this.listeners){
//            l.terminate(error);
//        }
    }

    public boolean removeListener(IEventListener<Execution> listener) {
        return this.executionPublishingSystem.removeListener(listener);
    }
}
