/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
package kieker.examples.analysis.traceanalysis;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.signature.Signature;
import kieker.tools.trace.analysis.systemModel.AllocationComponent;
import kieker.tools.trace.analysis.systemModel.AssemblyComponent;
import kieker.tools.trace.analysis.systemModel.ComponentType;
import kieker.tools.trace.analysis.systemModel.Execution;
import kieker.tools.trace.analysis.systemModel.ExecutionContainer;
import kieker.tools.trace.analysis.systemModel.Operation;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;
import teetime.framework.AbstractConsumerStage;

/**
 * @author Andre van Hoorn
 * @author Reiner Jung
 *
 * @since 1.15
 */
public abstract class AbstractTraceAnalysisFilter extends AbstractConsumerStage<OperationExecutionRecord> {
	
	private volatile SystemModelRepository systemModelRepository;
	
	public AbstractTraceAnalysisFilter(SystemModelRepository systemModelRepository) {
		this.systemModelRepository = systemModelRepository;
	}
	
	public static final Execution createExecutionByEntityNames(final SystemModelRepository systemModelRepository,
			final String executionContainerName, final String assemblyComponentTypeName, final String componentTypeName,
			final Signature operationSignature, final long traceId, final String sessionId, final int eoi, final int ess,
			final long tin, final long tout, final boolean assumed) {
		final String allocationComponentName = new StringBuilder(executionContainerName).append("::").append(assemblyComponentTypeName).toString();
		final String operationFactoryName = new StringBuilder(componentTypeName).append(".").append(operationSignature).toString();

		AllocationComponent allocInst = systemModelRepository.getAllocationFactory()
				.lookupAllocationComponentInstanceByNamedIdentifier(allocationComponentName);
		if (allocInst == null) { // Allocation component instance doesn't exist
			AssemblyComponent assemblyComponent = systemModelRepository.getAssemblyFactory()
					.lookupAssemblyComponentInstanceByNamedIdentifier(assemblyComponentTypeName);
			if (assemblyComponent == null) { // assembly instance doesn't exist
				ComponentType componentType = systemModelRepository.getTypeRepositoryFactory().lookupComponentTypeByNamedIdentifier(assemblyComponentTypeName);
				if (componentType == null) { // NOPMD NOCS (NestedIf)
					// Component type doesn't exist
					componentType = systemModelRepository.getTypeRepositoryFactory().createAndRegisterComponentType(assemblyComponentTypeName,
							assemblyComponentTypeName);
				}
				assemblyComponent = systemModelRepository.getAssemblyFactory()
						.createAndRegisterAssemblyComponentInstance(assemblyComponentTypeName, componentType);
			}
			ExecutionContainer execContainer = systemModelRepository.getExecutionEnvironmentFactory()
					.lookupExecutionContainerByNamedIdentifier(executionContainerName);
			if (execContainer == null) { // doesn't exist, yet
				execContainer = systemModelRepository.getExecutionEnvironmentFactory()
						.createAndRegisterExecutionContainer(executionContainerName, executionContainerName);
			}
			allocInst = systemModelRepository.getAllocationFactory()
					.createAndRegisterAllocationComponentInstance(allocationComponentName, assemblyComponent, execContainer);
		}

		Operation op = systemModelRepository.getOperationFactory().lookupOperationByNamedIdentifier(operationFactoryName);
		if (op == null) { // Operation doesn't exist
			op = systemModelRepository.getOperationFactory()
					.createAndRegisterOperation(operationFactoryName, allocInst.getAssemblyComponent().getType(), operationSignature);
			allocInst.getAssemblyComponent().getType().addOperation(op);
		}

		return new Execution(op, allocInst, traceId, sessionId, eoi, ess, tin, tout, assumed);
	}

	public static final Execution createExecutionByEntityNames(final SystemModelRepository systemModelRepository,
			final String executionContainerName, final String assemblyComponentTypeName,
			final Signature operationSignature, final long traceId, final String sessionId, final int eoi, final int ess,
			final long tin, final long tout, final boolean assumed) {
		return AbstractTraceAnalysisFilter.createExecutionByEntityNames(systemModelRepository, executionContainerName, assemblyComponentTypeName,
				assemblyComponentTypeName, operationSignature, traceId, sessionId, eoi, ess, tin, tout, assumed);
	}

	protected final Execution createExecutionByEntityNames(final String executionContainerName, final String assemblyComponentTypeName,
			final String componentTypeName, final Signature operationSignature, final long traceId, final String sessionId, final int eoi, final int ess,
			final long tin, final long tout, final boolean assumed) {
		return AbstractTraceAnalysisFilter.createExecutionByEntityNames(this.systemModelRepository, executionContainerName, assemblyComponentTypeName,
				componentTypeName, operationSignature, traceId, sessionId, eoi, ess, tin, tout, assumed);
	}

	protected final Execution createExecutionByEntityNames(final String executionContainerName, final String assemblyComponentTypeName,
			final Signature operationSignature, final long traceId, final String sessionId, final int eoi, final int ess,
			final long tin, final long tout, final boolean assumed) {
		return AbstractTraceAnalysisFilter.createExecutionByEntityNames(this.systemModelRepository, executionContainerName, assemblyComponentTypeName,
				assemblyComponentTypeName, operationSignature, traceId, sessionId, eoi, ess, tin, tout, assumed);
	}

	/**
	 * Prints a debug message to the logger. The output is prepended by a header which includes the name of this plugin instance.
	 *
	 * @param lines
	 *            The lines to be printed.
	 */
	protected void printDebugLogMessage(final String[] lines) {
		if (logger.isDebugEnabled()) {
			logger.debug("");
			logger.debug("#");
			logger.debug("# Plugin: " + this.getClass().getName());
			for (final String l : lines) {
				logger.debug(l);
			}
		}
	}

	protected void printErrorLogMessage(final String[] lines) {
		if (logger.isErrorEnabled()) {
			logger.error("");
			logger.error("#");
			logger.error("# Plugin: " + this.getClass().getName());
			for (final String l : lines) {
				logger.error(l);
			}
		}
	}

}
