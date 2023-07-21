/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.trace.analysis.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.util.signature.Signature;
import kieker.tools.trace.analysis.systemModel.AllocationComponent;
import kieker.tools.trace.analysis.systemModel.AssemblyComponent;
import kieker.tools.trace.analysis.systemModel.ComponentType;
import kieker.tools.trace.analysis.systemModel.Execution;
import kieker.tools.trace.analysis.systemModel.ExecutionContainer;
import kieker.tools.trace.analysis.systemModel.Operation;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;

/**
 * @author Andre van Hoorn
 *
 * @since 1.2
 * @deprecated 1.15 has been ported to teetime
 */
@Deprecated
@Plugin(repositoryPorts = {
	@RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class) })
public abstract class AbstractTraceAnalysisFilter extends AbstractFilterPlugin {

	public static final String CONFIG_PROPERTY_VALUE_VERBOSE = "false";

	/** The name of the repository port for the system model repository. */
	public static final String REPOSITORY_PORT_NAME_SYSTEM_MODEL = "systemModelRepository";

	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTraceAnalysisFilter.class); // NOPMD
																										// (inherited
																										// constructor)

	private volatile SystemModelRepository systemEntityFactory;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public AbstractTraceAnalysisFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	public static final Execution createExecutionByEntityNames(final SystemModelRepository systemModelRepository,
			final String executionContainerName, final String assemblyComponentTypeName, final String componentTypeName,
			final Signature operationSignature, final long traceId, final String sessionId, final int eoi,
			final int ess, final long tin, final long tout, final boolean assumed) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(
					"static createExecutionByEntityNames executionContainerName={} assemblyComponentTypeName={} componentTypeName={} operationSignature={} traceId={} sessionId={} eoi={} ess={} tin={} tout={} assumed={}",
					executionContainerName, assemblyComponentTypeName, componentTypeName, operationSignature, traceId, sessionId, eoi, ess, tin, tout,
					assumed ? "true" : "false");
		}
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
		return AbstractTraceAnalysisFilter.createExecutionByEntityNames(systemModelRepository, executionContainerName,
				assemblyComponentTypeName, assemblyComponentTypeName, operationSignature, traceId, sessionId, eoi, ess,
				tin, tout, assumed);
	}

	// protected final Execution createExecutionByEntityNames(final String executionContainerName,
	// final String assemblyComponentTypeName, final String componentTypeName, final Signature operationSignature,
	// final long traceId, final String sessionId, final int eoi, final int ess, final long tin, final long tout,
	// final boolean assumed) {
	// return AbstractTraceAnalysisFilter.createExecutionByEntityNames(this.getSystemEntityFactory(),
	// executionContainerName, assemblyComponentTypeName, componentTypeName, operationSignature, traceId,
	// sessionId, eoi, ess, tin, tout, assumed);
	// }

	protected final Execution createExecutionByEntityNames(final String executionContainerName,
			final String assemblyComponentTypeName, final Signature operationSignature, final long traceId,
			final String sessionId, final int eoi, final int ess, final long tin, final long tout,
			final boolean assumed) {
		return AbstractTraceAnalysisFilter.createExecutionByEntityNames(this.getSystemEntityFactory(),
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
		if (AbstractTraceAnalysisFilter.LOGGER.isDebugEnabled()) {
			AbstractTraceAnalysisFilter.LOGGER.debug("");
			AbstractTraceAnalysisFilter.LOGGER.debug("#");
			AbstractTraceAnalysisFilter.LOGGER.debug("# Plugin: " + this.getClass().getName());
			for (final String l : lines) {
				AbstractTraceAnalysisFilter.LOGGER.debug(l);
			}
		}
	}

	protected void printErrorLogMessage(final String[] lines) {
		if (AbstractTraceAnalysisFilter.LOGGER.isErrorEnabled()) {
			AbstractTraceAnalysisFilter.LOGGER.error("");
			AbstractTraceAnalysisFilter.LOGGER.error("#");
			AbstractTraceAnalysisFilter.LOGGER.error("# Plugin: " + this.getClass().getName());
			for (final String l : lines) {
				AbstractTraceAnalysisFilter.LOGGER.error(l);
			}
		}
	}

	public final SystemModelRepository getSystemEntityFactory() {
		if (this.systemEntityFactory == null) {
			this.systemEntityFactory = (SystemModelRepository) this
					.getRepository(AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL);
		}
		if (this.systemEntityFactory == null) {
			AbstractTraceAnalysisFilter.LOGGER.error(
					"Failed to connect to system model repository via repository port '{}' (not connected?)",
					AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL);
		}
		return this.systemEntityFactory;
	}

}
