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
package kieker.analysis.trace.execution;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.analysis.trace.AbstractTraceProcessingStage;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.ExecutionTrace;
import kieker.model.system.model.MessageTrace;
import kieker.model.system.model.exceptions.InvalidTraceException;

import teetime.framework.OutputPort;

/**
 * @author Andre van Hoorn
 * @author Reiner Jung -- teetime port
 *
 * @since 1.2
 */
public class TraceEquivalenceClassFilter extends AbstractTraceProcessingStage<ExecutionTrace> {

	/** This is the name of the input port receiving new execution traces. */
	public static final String INPUT_PORT_NAME_EXECUTION_TRACE = "executionTraces";

	/** This is the name of the property determining the equivalence mode. */
	public static final String CONFIG_PROPERTY_NAME_EQUIVALENCE_MODE = "equivalenceMode";

	/** This constant determines the default equivalence mode (the default value is disabled). */
	public static final TraceEquivalenceClassModes DEFAULT_EQUIVALENCE_MODE = TraceEquivalenceClassModes.DISABLED;

	private final OutputPort<MessageTrace> messageTraceOutputPort = this.createOutputPort(MessageTrace.class);
	private final OutputPort<ExecutionTrace> executionTraceOutputPort = this.createOutputPort(ExecutionTrace.class);
	private final OutputPort<Map<ExecutionTrace, Integer>> equivalenceMapOutputPort = this.createOutputPort();

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
	 * @param systemModelRepository
	 *            system model repository
	 * @param equivalenceMode
	 *            equivalence mode
	 */
	public TraceEquivalenceClassFilter(final SystemModelRepository systemModelRepository, final TraceEquivalenceClassModes equivalenceMode) {
		super(systemModelRepository);

		this.equivalenceMode = equivalenceMode;
	}

	/**
	 * This method represents the input port of this filter, processing incoming execution traces.
	 *
	 * @param et
	 *            The next execution trace.
	 */

	@Override
	protected void execute(final ExecutionTrace et) throws Exception {
		try {
			if (this.equivalenceMode == TraceEquivalenceClassModes.DISABLED) {
				this.executionTraceOutputPort.send(et);
				this.messageTraceOutputPort.send(et.toMessageTrace(SystemModelRepository.ROOT_EXECUTION));
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
					this.executionTraceOutputPort.send(et);
					this.messageTraceOutputPort.send(et.toMessageTrace(SystemModelRepository.ROOT_EXECUTION));
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

	@Override
	protected void onTerminating() {
		this.logger.debug("Terminating {}", this.getClass().getCanonicalName());
		this.equivalenceMapOutputPort.send(this.getEquivalenceClassMap());
		super.onTerminating();
	}

	public OutputPort<Map<ExecutionTrace, Integer>> getEquivalenceMapOutputPort() {
		return this.equivalenceMapOutputPort;
	}

	public OutputPort<ExecutionTrace> getExecutionTraceOutputPort() {
		return this.executionTraceOutputPort;
	}

	public OutputPort<MessageTrace> getMessageTraceOutputPort() {
		return this.messageTraceOutputPort;
	}
}
