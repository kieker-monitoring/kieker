/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.trace;

import kieker.common.util.signature.Signature;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.AllocationComponent;
import kieker.model.system.model.AssemblyComponent;
import kieker.model.system.model.ComponentType;
import kieker.model.system.model.Execution;
import kieker.model.system.model.ExecutionContainer;
import kieker.model.system.model.Operation;

import teetime.framework.AbstractConsumerStage;

/**
 * @author Andre van Hoorn
 * @author Reiner Jung -- ported to teetime
 *
 * @param <T>
 *            flow or operation execution types
 *
 * @since 1.15
 */
public abstract class AbstractTraceAnalysisStage<T> extends AbstractConsumerStage<T> {

	private volatile SystemModelRepository systemModelRepository;

	public AbstractTraceAnalysisStage(final SystemModelRepository systemModelRepository) {
		this.systemModelRepository = systemModelRepository;
	}

	public static final Execution createExecutionByEntityNames(final SystemModelRepository systemModelRepository,
			final String executionContainerName, final String assemblyComponentTypeName, final String componentTypeName,
			final Signature operationSignature, final long traceId, final String sessionId, final int eoi,
			final int ess, final long tin, final long tout, final boolean assumed) {
		final String allocationComponentName = new StringBuilder(executionContainerName).append("::")
				.append(assemblyComponentTypeName).toString();
		final String operationFactoryName = new StringBuilder(componentTypeName).append('.').append(operationSignature)
				.toString();

		AllocationComponent allocInst = systemModelRepository.getAllocationFactory()
				.lookupAllocationComponentInstanceByNamedIdentifier(allocationComponentName);
		if (allocInst == null) { // Allocation component instance doesn't exist
			AssemblyComponent assemblyComponent = systemModelRepository.getAssemblyFactory()
					.lookupAssemblyComponentInstanceByNamedIdentifier(assemblyComponentTypeName);
			if (assemblyComponent == null) { // assembly instance doesn't exist
				ComponentType componentType = systemModelRepository.getTypeRepositoryFactory()
						.lookupComponentTypeByNamedIdentifier(assemblyComponentTypeName);
				if (componentType == null) { // NOPMD NOCS (NestedIf)
					// Component type doesn't exist
					componentType = systemModelRepository.getTypeRepositoryFactory()
							.createAndRegisterComponentType(assemblyComponentTypeName, assemblyComponentTypeName);
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
			allocInst = systemModelRepository.getAllocationFactory().createAndRegisterAllocationComponentInstance(
					allocationComponentName, assemblyComponent, execContainer);
		}

		Operation op = systemModelRepository.getOperationFactory()
				.lookupOperationByNamedIdentifier(operationFactoryName);
		if (op == null) { // Operation doesn't exist
			op = systemModelRepository.getOperationFactory().createAndRegisterOperation(operationFactoryName,
					allocInst.getAssemblyComponent().getType(), operationSignature);
			allocInst.getAssemblyComponent().getType().addOperation(op);
		}

		return new Execution(op, allocInst, traceId, sessionId, eoi, ess, tin, tout, assumed);
	}

	public static final Execution createExecutionByEntityNames(final SystemModelRepository systemModelRepository,
			final String executionContainerName, final String assemblyComponentTypeName,
			final Signature operationSignature, final long traceId, final String sessionId, final int eoi,
			final int ess, final long tin, final long tout, final boolean assumed) {
		return AbstractTraceAnalysisStage.createExecutionByEntityNames(systemModelRepository, executionContainerName,
				assemblyComponentTypeName, assemblyComponentTypeName, operationSignature, traceId, sessionId, eoi, ess,
				tin, tout, assumed);
	}

	protected final Execution createExecutionByEntityNames(final String executionContainerName,
			final String assemblyComponentTypeName, final String componentTypeName, final Signature operationSignature,
			final long traceId, final String sessionId, final int eoi, final int ess, final long tin, final long tout,
			final boolean assumed) {
		return AbstractTraceAnalysisStage.createExecutionByEntityNames(this.systemModelRepository,
				executionContainerName, assemblyComponentTypeName, componentTypeName, operationSignature, traceId,
				sessionId, eoi, ess, tin, tout, assumed);
	}

	protected final Execution createExecutionByEntityNames(final String executionContainerName,
			final String assemblyComponentTypeName, final Signature operationSignature, final long traceId,
			final String sessionId, final int eoi, final int ess, final long tin, final long tout,
			final boolean assumed) {
		return AbstractTraceAnalysisStage.createExecutionByEntityNames(this.systemModelRepository,
				executionContainerName, assemblyComponentTypeName, assemblyComponentTypeName, operationSignature,
				traceId, sessionId, eoi, ess, tin, tout, assumed);
	}

	/**
	 * Prints a debug message to the logger. The output is prepended by a header
	 * which includes the name of this plugin instance.
	 *
	 * @param lines
	 *            The lines to be printed.
	 */
	protected void printDebugLogMessage(final String[] lines) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("");
			this.logger.debug("#");
			this.logger.debug("# Plugin: " + this.getClass().getName());
			for (final String l : lines) {
				this.logger.debug(l);
			}
		}
	}

	protected void printErrorLogMessage(final String[] lines) {
		if (this.logger.isErrorEnabled()) {
			this.logger.error("");
			this.logger.error("#");
			this.logger.error("# Plugin: " + this.getClass().getName());
			for (final String l : lines) {
				this.logger.error(l);
			}
		}
	}

	public SystemModelRepository getSystemModelRepository() {
		return this.systemModelRepository;
	}

}
