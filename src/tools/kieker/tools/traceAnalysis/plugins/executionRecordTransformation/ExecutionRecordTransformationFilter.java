/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.tools.traceAnalysis.plugins.executionRecordTransformation;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.plugin.port.OutputPort;
import kieker.analysis.plugin.port.Plugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
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

/**
 * Transforms KiekerExecutionRecords into Execution objects.<br>
 * 
 * This class has exactly one input port and one output port. It receives objects inheriting from {@link OperationExecutionRecord}. The received object is
 * transformed into an instance of {@link Execution}.
 * 
 * @author Andre van Hoorn
 */
@Plugin(
		outputPorts = {
			@OutputPort(name = ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME, description = "Execution output stream", eventTypes = { Execution.class })
		})
public class ExecutionRecordTransformationFilter extends AbstractTraceAnalysisPlugin {

	public static final String INPUT_PORT_NAME = "newMonitoringRecord";
	public static final String OUTPUT_PORT_NAME = "defaultOutput";
	private static final Log LOG = LogFactory.getLog(ExecutionRecordTransformationFilter.class);

	public ExecutionRecordTransformationFilter(final Configuration configuration, final Map<String, AbstractRepository> repositories) {
		super(configuration, repositories);
	}

	private Signature createSignature(final String operationSignatureStr) {
		final String returnType = "N/A";
		String name;
		String[] paramTypeList;
		final int openParenIdx = operationSignatureStr.indexOf('(');
		if (openParenIdx == -1) { // no parameter list
			paramTypeList = new String[] {};
			name = operationSignatureStr;
		} else {
			name = operationSignatureStr.substring(0, openParenIdx);
			final StringTokenizer strTokenizer = new StringTokenizer(operationSignatureStr.substring(openParenIdx + 1, operationSignatureStr.length() - 1), ",");
			paramTypeList = new String[strTokenizer.countTokens()];
			for (int i = 0; strTokenizer.hasMoreTokens(); i++) {
				paramTypeList[i] = strTokenizer.nextToken().trim();
			}
		}

		return new Signature(name, returnType, paramTypeList);
	}

	@InputPort(description = "Input", eventTypes = { IMonitoringRecord.class })
	public boolean newMonitoringRecord(final Object data) {
		final IMonitoringRecord record = (IMonitoringRecord) data;
		if (!(record instanceof OperationExecutionRecord)) {
			ExecutionRecordTransformationFilter.LOG.error("Can only process records of type" + OperationExecutionRecord.class.getName() + " but received"
					+ record.getClass().getName());
			return false;
		}
		final OperationExecutionRecord execRec = (OperationExecutionRecord) record;

		final String executionContainerName = execRec.getHostName();
		final String componentTypeName = execRec.getClassName();
		final String assemblyComponentName = componentTypeName;
		final String allocationComponentName = new StringBuilder(executionContainerName).append("::").append(assemblyComponentName).toString();
		final String operationFactoryName = new StringBuilder(assemblyComponentName).append(".").append(execRec.getOperationName()).toString();
		final String operationSignatureStr = execRec.getOperationName();

		AllocationComponent allocInst = this.getSystemEntityFactory().getAllocationFactory()
				.lookupAllocationComponentInstanceByNamedIdentifier(allocationComponentName);
		if (allocInst == null) { /* Allocation component instance doesn't exist */
			AssemblyComponent assemblyComponent = this.getSystemEntityFactory().getAssemblyFactory()
					.lookupAssemblyComponentInstanceByNamedIdentifier(assemblyComponentName);
			if (assemblyComponent == null) { // assembly instance doesn't exist
				ComponentType componentType = this.getSystemEntityFactory().getTypeRepositoryFactory().lookupComponentTypeByNamedIdentifier(componentTypeName);
				if (componentType == null) { // NOCS (NestedIf)
					/* Component type doesn't exist */
					componentType = this.getSystemEntityFactory().getTypeRepositoryFactory().createAndRegisterComponentType(componentTypeName, componentTypeName);
				}
				assemblyComponent = this.getSystemEntityFactory().getAssemblyFactory()
						.createAndRegisterAssemblyComponentInstance(assemblyComponentName, componentType);
			}
			ExecutionContainer execContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory()
					.lookupExecutionContainerByNamedIdentifier(executionContainerName);
			if (execContainer == null) { /* doesn't exist, yet */
				execContainer = this.getSystemEntityFactory().getExecutionEnvironmentFactory()
						.createAndRegisterExecutionContainer(executionContainerName, executionContainerName);
			}
			allocInst = this.getSystemEntityFactory().getAllocationFactory()
					.createAndRegisterAllocationComponentInstance(allocationComponentName, assemblyComponent, execContainer);
		}

		Operation op = this.getSystemEntityFactory().getOperationFactory().lookupOperationByNamedIdentifier(operationFactoryName);
		if (op == null) { /* Operation doesn't exist */
			final Signature signature = this.createSignature(operationSignatureStr);
			op = this.getSystemEntityFactory().getOperationFactory()
					.createAndRegisterOperation(operationFactoryName, allocInst.getAssemblyComponent().getType(), signature);
			allocInst.getAssemblyComponent().getType().addOperation(op);
		}

		final Execution execution = new Execution(op, allocInst, execRec.getTraceId(), execRec.getSessionId(), execRec.getEoi(), execRec.getEss(), execRec.getTin(),
				execRec.getTout());
		super.deliver(ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME, execution);
		return true;
	}

	@Override
	public boolean execute() {
		return true;
	}

	@Override
	public void terminate(final boolean error) {
		// nothing to do
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		// TODO: Save the current configuration

		return configuration;
	}

	@Override
	protected Map<String, AbstractRepository> getDefaultRepositories() {
		return new HashMap<String, AbstractRepository>();
	}
}
