package kieker.tools.traceAnalysis.analysisPlugins.traceFilter;

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

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.analysis.plugin.configuration.IOutputPort;
import kieker.analysis.plugin.configuration.OutputPort;
import kieker.tools.traceAnalysis.analysisPlugins.AbstractExecutionTraceProcessingPlugin;
import kieker.tools.traceAnalysis.analysisPlugins.traceReconstruction.InvalidTraceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class TraceEquivalenceClassFilter extends AbstractExecutionTraceProcessingPlugin {

    private static final Log log = LogFactory.getLog(TraceEquivalenceClassFilter.class);

    public enum TraceEquivalenceClassModes {

        DISABLED, ASSEMBLY, ALLOCATION
    }
    private final Execution rootExecution;
    private final TraceEquivalenceClassModes equivalenceMode;
    /** Representative x # of equivalents */
    private final HashMap<AbstractExecutionTraceHashContainer, AtomicInteger> eTracesEquivClassesMap =
            new HashMap<AbstractExecutionTraceHashContainer, AtomicInteger>();

    public TraceEquivalenceClassFilter(final String name,
            final SystemModelRepository systemEntityFactory,
            final TraceEquivalenceClassModes traceEquivalenceCallMode) {
        super(name, systemEntityFactory);
        this.rootExecution = systemEntityFactory.getRootExecution();
        this.equivalenceMode = traceEquivalenceCallMode;
    }

    private void newExecutionTrace(ExecutionTrace et) {
        try {
            if (this.equivalenceMode == TraceEquivalenceClassModes.DISABLED) {
                this.executionTraceOutputPort.deliver(et);
                this.messageTraceOutputPort.deliver(et.toMessageTrace(this.rootExecution));
            } else { // mode is ASSEMBLY or ALLOCATION
                final AbstractExecutionTraceHashContainer polledTraceHashContainer;
                if (equivalenceMode == TraceEquivalenceClassModes.ASSEMBLY){
                    polledTraceHashContainer = new ExecutionTraceHashContainerAssemblyEquivalence(et);
                } else if (equivalenceMode == TraceEquivalenceClassModes.ALLOCATION){
                    polledTraceHashContainer = new ExecutionTraceHashContainerAllocationEquivalence(et);
                } else { // just to make sure
                    log.error("Invalid trace equivalence mode: " + equivalenceMode);
                    reportError(et.getTraceId());
                    return;
                }

                AtomicInteger numOccurences = this.eTracesEquivClassesMap.get(polledTraceHashContainer);
                if (numOccurences == null) {
                    numOccurences = new AtomicInteger(1);
                    this.eTracesEquivClassesMap.put(polledTraceHashContainer, numOccurences);
                    this.executionTraceOutputPort.deliver(et);
                    this.messageTraceOutputPort.deliver(et.toMessageTrace(this.rootExecution));
                } else {
                    numOccurences.incrementAndGet();
                }
            }
            reportSuccess(et.getTraceId());
        } catch (InvalidTraceException ex) {
            log.error("InvalidTraceException", ex);
            reportError(et.getTraceId());
        }
    }

    public IInputPort<ExecutionTrace> getExecutionTraceInputPort() {
        return this.executionTraceInputPort;
    }
    private final IInputPort<ExecutionTrace> executionTraceInputPort =
            new AbstractInputPort<ExecutionTrace>("Execution traces") {

                @Override
                public void newEvent(ExecutionTrace mt) {
                    newExecutionTrace(mt);
                }
            };

    public IOutputPort<MessageTrace> getMessageTraceOutputPort() {
        return this.messageTraceOutputPort;
    }
    private final OutputPort<MessageTrace> messageTraceOutputPort =
            new OutputPort<MessageTrace>("Message Traces");

    public IOutputPort<ExecutionTrace> getExecutionTraceOutputPort() {
        return this.executionTraceOutputPort;
    }
    private final OutputPort<ExecutionTrace> executionTraceOutputPort =
            new OutputPort<ExecutionTrace>("Execution Traces");

    @Override
    public boolean execute() {
        return true; // do nothing
    }

    @Override
    public void terminate(boolean error) {
        return; // do nothing
    }

    public HashMap<ExecutionTrace, Integer> getEquivalenceClassMap() {
        final HashMap<ExecutionTrace, Integer> map = new HashMap<ExecutionTrace, Integer>();
        for (final Entry<AbstractExecutionTraceHashContainer, AtomicInteger> entry : this.eTracesEquivClassesMap.entrySet()) {
            map.put(entry.getKey().getExecutionTrace(), entry.getValue().intValue());
        }
        return map;
    }
}