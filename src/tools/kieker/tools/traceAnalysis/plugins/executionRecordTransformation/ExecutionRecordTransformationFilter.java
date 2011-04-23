package kieker.tools.traceAnalysis.plugins.executionRecordTransformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.plugin.configuration.IOutputPort;
import kieker.analysis.plugin.configuration.OutputPort;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.OperationExecutionRecord;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.ComponentType;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.Signature;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Transforms KiekerExecutionRecords into Execution objects.
 *
 * @author Andre van Hoorn
 */
public class ExecutionRecordTransformationFilter extends AbstractTraceAnalysisPlugin implements IMonitoringRecordConsumerPlugin {

    private static final Log log = LogFactory.getLog(ExecutionRecordTransformationFilter.class);

    public ExecutionRecordTransformationFilter(
            final String name,
            final SystemModelRepository systemFactory) {
        super(name, systemFactory);
    }
    private final static Collection<Class<? extends IMonitoringRecord>> recordTypeSubscriptionList =
            new ArrayList<Class<? extends IMonitoringRecord>>();

    static {
        ExecutionRecordTransformationFilter.recordTypeSubscriptionList.add(OperationExecutionRecord.class);
    }

    @Override
	public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
        return ExecutionRecordTransformationFilter.recordTypeSubscriptionList;
    }

    private Signature createSignature(final String operationSignatureStr) {
        final String returnType = "N/A";
        String name;
        String[] paramTypeList;
        final int openParenIdx = operationSignatureStr.indexOf('(');
        if (openParenIdx == -1) { // no parameter list
            paramTypeList = new String[]{};
            name = operationSignatureStr;
        } else {
            name = operationSignatureStr.substring(0, openParenIdx);
            final StringTokenizer strTokenizer =
                    new StringTokenizer(operationSignatureStr.substring(openParenIdx + 1, operationSignatureStr.length() - 1), ",");
            paramTypeList = new String[strTokenizer.countTokens()];
            for (int i = 0; strTokenizer.hasMoreTokens(); i++) {
                paramTypeList[i] = strTokenizer.nextToken().trim();
            }
        }

        return new Signature(name, returnType, paramTypeList);
    }

    @Override
    public boolean newMonitoringRecord(final IMonitoringRecord record) {
        if (!(record instanceof OperationExecutionRecord)) {
            ExecutionRecordTransformationFilter.log.error("Can only process records of type"
                    + OperationExecutionRecord.class.getName() + " but received" + record.getClass().getName());
        }
        final OperationExecutionRecord execRec = (OperationExecutionRecord) record;

        final String executionContainerName = execRec.hostName;
        //(this.considerExecutionContainer) ? execRec.hostName : "DEFAULTCONTAINER";
        final String componentTypeName = execRec.className;
        final String assemblyComponentName = componentTypeName;
        final String allocationComponentName =
                new StringBuilder(executionContainerName).append("::").append(assemblyComponentName).toString();
        final String operationFactoryName =
                new StringBuilder(assemblyComponentName).append(".").append(execRec.operationName).toString();
        final String operationSignatureStr = execRec.operationName;

        AllocationComponent allocInst = this.getSystemEntityFactory().getAllocationFactory().lookupAllocationComponentInstanceByNamedIdentifier(allocationComponentName);
        if (allocInst == null) { /* Allocation component instance doesn't exist */
            AssemblyComponent assemblyComponent =
                    this.getSystemEntityFactory().getAssemblyFactory().lookupAssemblyComponentInstanceByNamedIdentifier(assemblyComponentName);
            if (assemblyComponent == null) { // assembly instance doesn't exist
                ComponentType componentType =
                        this.getSystemEntityFactory().getTypeRepositoryFactory().lookupComponentTypeByNamedIdentifier(componentTypeName);
                if (componentType == null) {
                    /* Component type doesn't exist */
                    componentType = this.getSystemEntityFactory().getTypeRepositoryFactory().createAndRegisterComponentType(componentTypeName, componentTypeName);
                }
                assemblyComponent = this.getSystemEntityFactory().getAssemblyFactory().createAndRegisterAssemblyComponentInstance(assemblyComponentName, componentType);
            }
            ExecutionContainer execContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory().lookupExecutionContainerByNamedIdentifier(executionContainerName);
            if (execContainer == null) { /* doesn't exist, yet */
                execContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory().createAndRegisterExecutionContainer(executionContainerName, executionContainerName);
            }
            allocInst = this.getSystemEntityFactory().getAllocationFactory().createAndRegisterAllocationComponentInstance(allocationComponentName, assemblyComponent, execContainer);
        }

        Operation op = this.getSystemEntityFactory().getOperationFactory().lookupOperationByNamedIdentifier(operationFactoryName);
        if (op == null) { /* Operation doesn't exist */
            final Signature signature = this.createSignature(operationSignatureStr);
            op = this.getSystemEntityFactory().getOperationFactory().createAndRegisterOperation(operationFactoryName, allocInst.getAssemblyComponent().getType(), signature);
            allocInst.getAssemblyComponent().getType().addOperation(op);
        }

        final Execution execution = new Execution(op, allocInst, execRec.traceId,
                execRec.sessionId, execRec.eoi, execRec.ess, execRec.tin, execRec.tout);
        this.executionOutputPort.deliver(execution);
        return true;
    }

    @Override
    public boolean execute() {
        return true;
    }

    @Override
    public void terminate(final boolean error) {
    }

    private final OutputPort<Execution> executionOutputPort =
            new OutputPort<Execution>("Execution output stream");

    public IOutputPort<Execution> getExecutionOutputPort(){
        return this.executionOutputPort;
    }
}
