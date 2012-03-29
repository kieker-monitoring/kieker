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

package kieker.tools.traceAnalysis.filter.traceFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.filter.AbstractExecutionTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * This class has exactly one input port named "in" and two output ports named
 * "messageTraceOutput", "executionTraceOutput".
 * 
 * @author Andre van Hoorn
 */
@Plugin(
		outputPorts = {
			@OutputPort(
					name = TraceEquivalenceClassFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE_REPRESENTATIVES,
					description = "Message Traces",
					eventTypes = { MessageTrace.class }),
			@OutputPort(
					name = TraceEquivalenceClassFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE_REPRESENTATIVES,
					description = "Execution Traces",
					eventTypes = { ExecutionTrace.class }) },
		repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class))
public class TraceEquivalenceClassFilter extends AbstractExecutionTraceProcessingFilter {
	private static final Log LOG = LogFactory.getLog(TraceEquivalenceClassFilter.class);

	public static final String INPUT_PORT_NAME_EXECUTION_TRACE = "executionTraces";

	public static final String OUTPUT_PORT_NAME_MESSAGE_TRACE_REPRESENTATIVES = "messageTraceRepresentatives";
	public static final String OUTPUT_PORT_NAME_EXECUTION_TRACE_REPRESENTATIVES = "executionTraceRepresentatives";

	public static enum TraceEquivalenceClassModes {
		DISABLED, ASSEMBLY, ALLOCATION
	}

	private TraceEquivalenceClassModes equivalenceMode;
	/** Representative x # of equivalents */
	private final Map<AbstractExecutionTraceHashContainer, AtomicInteger> eTracesEquivClassesMap = new HashMap<AbstractExecutionTraceHashContainer, AtomicInteger>(); // NOPMD

	/**
	 * Creates a new instance of this class using the given configuration object. Keep in mind that the Trace-Equivalence-Class-Mode has to be set via the method
	 * <i>setTraceEquivalenceCallMode</i> before using this component!
	 * 
	 * @param configuration
	 *            The configuration object used to initialize this object.
	 */
	public TraceEquivalenceClassFilter(final Configuration configuration) {
		super(configuration);
	}

	public void setTraceEquivalenceCallMode(final TraceEquivalenceClassModes traceEquivalenceCallMode) {
		this.equivalenceMode = traceEquivalenceCallMode;
	}

	@InputPort(
			name = TraceEquivalenceClassFilter.INPUT_PORT_NAME_EXECUTION_TRACE,
			description = "Execution traces",
			eventTypes = { ExecutionTrace.class })
	public void newExecutionTrace(final ExecutionTrace et) {
		try {
			if (this.equivalenceMode == TraceEquivalenceClassFilter.TraceEquivalenceClassModes.DISABLED) {
				super.deliver(TraceEquivalenceClassFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE_REPRESENTATIVES, et);
				super.deliver(TraceEquivalenceClassFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE_REPRESENTATIVES, et.toMessageTrace(SystemModelRepository.ROOT_EXECUTION));
			} else { // mode is ASSEMBLY or ALLOCATION
				final AbstractExecutionTraceHashContainer polledTraceHashContainer;
				if (this.equivalenceMode == TraceEquivalenceClassFilter.TraceEquivalenceClassModes.ASSEMBLY) {
					polledTraceHashContainer = new ExecutionTraceHashContainerAssemblyEquivalence(et);
				} else if (this.equivalenceMode == TraceEquivalenceClassFilter.TraceEquivalenceClassModes.ALLOCATION) {
					polledTraceHashContainer = new ExecutionTraceHashContainerAllocationEquivalence(et);
				} else { // just to make sure
					TraceEquivalenceClassFilter.LOG.error("Invalid trace equivalence mode: " + this.equivalenceMode);
					this.reportError(et.getTraceId());
					return;
				}

				AtomicInteger numOccurences = this.eTracesEquivClassesMap.get(polledTraceHashContainer);
				if (numOccurences == null) {
					numOccurences = new AtomicInteger(1);
					this.eTracesEquivClassesMap.put(polledTraceHashContainer, numOccurences);
					super.deliver(TraceEquivalenceClassFilter.OUTPUT_PORT_NAME_EXECUTION_TRACE_REPRESENTATIVES, et);
					super.deliver(TraceEquivalenceClassFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE_REPRESENTATIVES,
							et.toMessageTrace(SystemModelRepository.ROOT_EXECUTION));
				} else {
					numOccurences.incrementAndGet();
				}
			}
			this.reportSuccess(et.getTraceId());
		} catch (final InvalidTraceException ex) {
			TraceEquivalenceClassFilter.LOG.error("InvalidTraceException", ex);
			this.reportError(et.getTraceId());
		}
	}

	public Map<ExecutionTrace, Integer> getEquivalenceClassMap() {
		final Map<ExecutionTrace, Integer> map = new HashMap<ExecutionTrace, Integer>(); // NOPMD (UseConcurrentHashMap)
		for (final Entry<AbstractExecutionTraceHashContainer, AtomicInteger> entry : this.eTracesEquivClassesMap.entrySet()) {
			map.put(entry.getKey().getExecutionTrace(), entry.getValue().intValue());
		}
		return map;
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@Override
	public String getExecutionTraceInputPortName() {
		return TraceEquivalenceClassFilter.INPUT_PORT_NAME_EXECUTION_TRACE;
	}

}
