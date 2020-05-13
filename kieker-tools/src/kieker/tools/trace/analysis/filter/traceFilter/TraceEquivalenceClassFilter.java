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

package kieker.tools.trace.analysis.filter.traceFilter;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.filter.AbstractExecutionTraceProcessingFilter;
import kieker.tools.trace.analysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.trace.analysis.filter.traceReconstruction.InvalidTraceException;
import kieker.tools.trace.analysis.systemModel.ExecutionTrace;
import kieker.tools.trace.analysis.systemModel.MessageTrace;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;

/**
 * @author Andre van Hoorn
 *
 * @since 1.2
 */
@Plugin(description = "Puts the incoming traces into equivalence classes", outputPorts = {
	@OutputPort(name = TraceEquivalenceClassFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE_REPRESENTATIVES, description = "Message Traces", eventTypes = {
		MessageTrace.class }),
	@OutputPort(name = TraceEquivalenceClassFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE_REPRESENTATIVES, description = "Execution Traces", eventTypes = {
		ExecutionTrace.class })
}, repositoryPorts = {
	@RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class)
}, configuration = @Property(name = TraceEquivalenceClassFilter.CONFIG_PROPERTY_NAME_EQUIVALENCE_MODE,
		description = "The trace equivalence criteria: DISABLED (default value), ASSEMBLY (assembly-level equivalence), or ALLOCATION"
				+ " (allocation-level equivalence)",
		defaultValue = "DISABLED") // one of TraceEquivalenceClassFilter.TraceEquivalenceClassModes
)
public class TraceEquivalenceClassFilter extends AbstractExecutionTraceProcessingFilter {

	/** This is the name of the input port receiving new execution traces. */
	public static final String INPUT_PORT_NAME_EXECUTION_TRACE = "executionTraces";

	public static final String OUTPUT_PORT_NAME_MESSAGE_TRACE_REPRESENTATIVES = "messageTraceRepresentatives";
	public static final String OUTPUT_PORT_NAME_EXECUTION_TRACE_REPRESENTATIVES = "executionTraceRepresentatives";

	/** This is the name of the property determining the equivalence mode. */
	public static final String CONFIG_PROPERTY_NAME_EQUIVALENCE_MODE = "equivalenceMode";

	/** This constant determines the default equivalence mode (the default value is disabled). */
	public static final TraceEquivalenceClassModes DEFAULT_EQUIVALENCE_MODE = TraceEquivalenceClassModes.DISABLED;

	private final TraceEquivalenceClassModes equivalenceMode;

	/** Representative x # of equivalents. */
	private final ConcurrentMap<AbstractExecutionTraceHashContainer, AtomicInteger> eTracesEquivClassesMap = new ConcurrentHashMap<>();

	/**
	 * This enum represents the different trace equivalence class modes.
	 *
	 * @author Andre van Hoorn
	 *
	 * @since 1.2
	 */
	public static enum TraceEquivalenceClassModes {
		/** Disabled equivalence mode */
		DISABLED,
		/** Assembly-level equivalence */
		ASSEMBLY,
		/** Allocation-level equivalence */
		ALLOCATION
	}

	/**
	 * Creates a new instance of this class using the given parameters. Keep in mind that the Trace-Equivalence-Class-Mode has to be set via the method
	 * <i>setTraceEquivalenceCallMode</i> before using this component!
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public TraceEquivalenceClassFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.equivalenceMode = this.extractTraceEquivalenceClassMode(this.configuration.getStringProperty(CONFIG_PROPERTY_NAME_EQUIVALENCE_MODE));
	}

	private TraceEquivalenceClassModes extractTraceEquivalenceClassMode(final String traceEquivalenceCallModeString) {
		TraceEquivalenceClassModes extractedEquivalenceMode;
		try {
			extractedEquivalenceMode = TraceEquivalenceClassModes.valueOf(traceEquivalenceCallModeString);
		} catch (final IllegalArgumentException exc) {
			this.logger.error("Error extracting enum value from String: '{}'", traceEquivalenceCallModeString, exc);
			extractedEquivalenceMode = DEFAULT_EQUIVALENCE_MODE;
		}
		return extractedEquivalenceMode;
	}

	/**
	 * This method represents the input port of this filter, processing incoming execution traces.
	 *
	 * @param et
	 *            The next execution trace.
	 */
	@InputPort(name = INPUT_PORT_NAME_EXECUTION_TRACE, description = "Execution traces", eventTypes = { ExecutionTrace.class })
	public void newExecutionTrace(final ExecutionTrace et) {
		try {
			if (this.equivalenceMode == TraceEquivalenceClassModes.DISABLED) {
				super.deliver(OUTPUT_PORT_NAME_EXECUTION_TRACE_REPRESENTATIVES, et);
				super.deliver(OUTPUT_PORT_NAME_MESSAGE_TRACE_REPRESENTATIVES, et.toMessageTrace(SystemModelRepository.ROOT_EXECUTION));
			} else { // mode is ASSEMBLY or ALLOCATION
				final AbstractExecutionTraceHashContainer polledTraceHashContainer;
				if (this.equivalenceMode == TraceEquivalenceClassModes.ASSEMBLY) {
					polledTraceHashContainer = new ExecutionTraceHashContainerAssemblyEquivalence(et);
				} else if (this.equivalenceMode == TraceEquivalenceClassModes.ALLOCATION) {
					polledTraceHashContainer = new ExecutionTraceHashContainerAllocationEquivalence(et);
				} else { // just to make sure
					this.logger.error("Invalid trace equivalence mode: {}", this.equivalenceMode);
					this.reportError(et.getTraceId());
					return;
				}

				AtomicInteger numOccurences = this.eTracesEquivClassesMap.get(polledTraceHashContainer);
				if (numOccurences == null) {
					numOccurences = new AtomicInteger(1);
					this.eTracesEquivClassesMap.put(polledTraceHashContainer, numOccurences);
					super.deliver(OUTPUT_PORT_NAME_EXECUTION_TRACE_REPRESENTATIVES, et);
					super.deliver(OUTPUT_PORT_NAME_MESSAGE_TRACE_REPRESENTATIVES,
							et.toMessageTrace(SystemModelRepository.ROOT_EXECUTION));
				} else {
					numOccurences.incrementAndGet();
				}
			}
			this.reportSuccess(et.getTraceId());
		} catch (final InvalidTraceException ex) {
			this.logger.error("InvalidTraceException: {}", ex.getMessage()); // do not pass 'ex' to LOG.error because this makes the output verbose (#584)
			this.reportError(et.getTraceId());
		}
	}

	public ConcurrentMap<ExecutionTrace, Integer> getEquivalenceClassMap() {
		final ConcurrentMap<ExecutionTrace, Integer> map = new ConcurrentHashMap<>();
		for (final Entry<AbstractExecutionTraceHashContainer, AtomicInteger> entry : this.eTracesEquivClassesMap.entrySet()) {
			map.put(entry.getKey().getExecutionTrace(), entry.getValue().intValue());
		}
		return map;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = super.getCurrentConfiguration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_EQUIVALENCE_MODE, this.equivalenceMode.toString());

		return configuration;
	}
}
