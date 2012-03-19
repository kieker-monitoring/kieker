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

package kieker.tools.traceAnalysis.plugins;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.util.Signature;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.ComponentType;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * 
 * @author Andre van Hoorn
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, repositoryType = SystemModelRepository.class))
public abstract class AbstractTraceAnalysisPlugin extends AbstractAnalysisPlugin {
	private static final Log LOG = LogFactory.getLog(AbstractTraceAnalysisPlugin.class);

	public static final String SYSTEM_MODEL_REPOSITORY_NAME = "systemModelRepository";

	private volatile SystemModelRepository systemEntityFactory;

	public AbstractTraceAnalysisPlugin(final Configuration configuration) {
		super(configuration);
	}

	public static final Execution createExecutionByEntityNames(final SystemModelRepository systemModelRepository,
			final String executionContainerName, final String componentTypeName,
			final Signature operationSignature, final long traceId, final String sessionId, final int eoi, final int ess,
			final long tin, final long tout, final boolean assumed) {
		final String assemblyComponentName = componentTypeName;
		final String allocationComponentName = new StringBuilder(executionContainerName).append("::").append(assemblyComponentName).toString();
		final String operationFactoryName = new StringBuilder(assemblyComponentName).append(".").append(operationSignature).toString();

		AllocationComponent allocInst = systemModelRepository.getAllocationFactory()
				.lookupAllocationComponentInstanceByNamedIdentifier(allocationComponentName);
		if (allocInst == null) { /* Allocation component instance doesn't exist */
			AssemblyComponent assemblyComponent = systemModelRepository.getAssemblyFactory()
					.lookupAssemblyComponentInstanceByNamedIdentifier(assemblyComponentName);
			if (assemblyComponent == null) { // assembly instance doesn't exist
				ComponentType componentType = systemModelRepository.getTypeRepositoryFactory().lookupComponentTypeByNamedIdentifier(componentTypeName);
				if (componentType == null) { // NOCS (NestedIf)
					/* Component type doesn't exist */
					componentType = systemModelRepository.getTypeRepositoryFactory().createAndRegisterComponentType(componentTypeName, componentTypeName);
				}
				assemblyComponent = systemModelRepository.getAssemblyFactory()
						.createAndRegisterAssemblyComponentInstance(assemblyComponentName, componentType);
			}
			ExecutionContainer execContainer = systemModelRepository.getExecutionEnvironmentFactory()
					.lookupExecutionContainerByNamedIdentifier(executionContainerName);
			if (execContainer == null) { /* doesn't exist, yet */
				execContainer = systemModelRepository.getExecutionEnvironmentFactory()
						.createAndRegisterExecutionContainer(executionContainerName, executionContainerName);
			}
			allocInst = systemModelRepository.getAllocationFactory()
					.createAndRegisterAllocationComponentInstance(allocationComponentName, assemblyComponent, execContainer);
		}

		Operation op = systemModelRepository.getOperationFactory().lookupOperationByNamedIdentifier(operationFactoryName);
		if (op == null) { /* Operation doesn't exist */
			op = systemModelRepository.getOperationFactory()
					.createAndRegisterOperation(operationFactoryName, allocInst.getAssemblyComponent().getType(), operationSignature);
			allocInst.getAssemblyComponent().getType().addOperation(op);
		}

		return new Execution(op, allocInst, traceId, sessionId, eoi, ess, tin, tout, assumed);
	}

	protected final Execution createExecutionByEntityNames(final String executionContainerName, final String componentTypeName,
			final Signature operationSignature, final long traceId, final String sessionId, final int eoi, final int ess,
			final long tin, final long tout, final boolean assumed) {
		return AbstractTraceAnalysisPlugin.createExecutionByEntityNames(this.getSystemEntityFactory(), executionContainerName, componentTypeName,
				operationSignature, traceId, sessionId, eoi, ess, tin, tout, assumed);
	}

	protected void printMessage(final String[] lines) {
		System.out.println("");
		System.out.println("#");
		System.out.println("# Plugin: " + this.getName());
		for (final String l : lines) {
			System.out.println(l);
		}
	}

	protected final SystemModelRepository getSystemEntityFactory() {
		if (this.systemEntityFactory == null) {
			this.systemEntityFactory = (SystemModelRepository) this.getRepository(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME);
		}
		if (this.systemEntityFactory == null) {
			AbstractTraceAnalysisPlugin.LOG.error("Failed to connect to system model repository via repository port '"
					+ AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME + "' (not connected?)");
		}
		return this.systemEntityFactory;
	}
}
