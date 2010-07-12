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
package kieker.analysis.plugin.traceAnalysis.traceFilter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import kieker.analysis.datamodel.Execution;
import kieker.analysis.datamodel.ExecutionTrace;
import kieker.analysis.datamodel.MessageTrace;
import kieker.analysis.datamodel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.analysis.plugin.configuration.IOutputPort;
import kieker.analysis.plugin.configuration.OutputPort;
import kieker.analysis.plugin.traceAnalysis.AbstractExecutionTraceProcessingPlugin;
import kieker.analysis.plugin.traceAnalysis.traceReconstruction.InvalidTraceException;

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
    private final HashMap<ExecutionTraceHashContainer, AtomicInteger> eTracesEquivClassesMap =
            new HashMap<ExecutionTraceHashContainer, AtomicInteger>();

    public TraceEquivalenceClassFilter(final String name,
            final SystemModelRepository systemEntityFactory,
            final Execution rootExecution,
            final TraceEquivalenceClassModes traceEquivalenceCallMode) {
        super(name, systemEntityFactory);
        this.rootExecution = rootExecution;
        this.equivalenceMode = traceEquivalenceCallMode;
    }

    private void newExecutionTrace(ExecutionTrace et) {
        try {
            if (this.equivalenceMode != TraceEquivalenceClassModes.DISABLED) {
                this.executionTraceOutputPort.deliver(et);
                this.messageTraceOutputPort.deliver(et.toMessageTrace(this.rootExecution));
            } else {
                final ExecutionTraceHashContainer polledTraceHashContainer =
                        new ExecutionTraceHashContainer(et);
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
        for (final Entry<ExecutionTraceHashContainer, AtomicInteger> entry : this.eTracesEquivClassesMap.entrySet()) {
            map.put(entry.getKey().t, entry.getValue().intValue());
        }
        return map;
    }

    private class ExecutionTraceHashContainer {

        private final ExecutionTrace t;
        private final int hashCode;

        public ExecutionTraceHashContainer(final ExecutionTrace t) {
            this.t = t;
            int h = 0;
            // TODO: need a better hash function considering the order (e.g.,
            // MD5)
            for (final Execution r : t.getTraceAsSortedSet()) {
                h ^= r.getOperation().getId();
                if (equivalenceMode == TraceEquivalenceClassModes.ALLOCATION) {
                    h ^= r.getAllocationComponent().getId();
                } else if (equivalenceMode == TraceEquivalenceClassModes.ASSEMBLY) {
                    h ^= r.getAllocationComponent().getAssemblyComponent().getId();
                }
                h ^= r.getEoi();
                h ^= r.getEss();
            }
            //
            this.hashCode = h;
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        private boolean executionsEqual(final Execution r1, final Execution r2) {
            if (r1 == r2) {
                return true;
            }
            if (r1 == null || r2 == null) {
                return false;
            }
            final boolean retVal = (((equivalenceMode == TraceEquivalenceClassModes.ALLOCATION) && r1.getAllocationComponent().getId() == r2.getAllocationComponent().getId()) || ((equivalenceMode == TraceEquivalenceClassModes.ASSEMBLY) && r1.getAllocationComponent().getAssemblyComponent().getId() == r2.getAllocationComponent().getAssemblyComponent().getId()))
                    && r1.getOperation().getId() == r2.getOperation().getId()
                    && r1.getEoi() == r2.getEoi() && r1.getEss() == r2.getEss();
            return retVal;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof ExecutionTraceHashContainer)) {
                return false;
            }
            final ExecutionTrace otherTrace = ((ExecutionTraceHashContainer) obj).t;
            if (this.t.getLength() != otherTrace.getLength()) {
                return false;
            }
            final Iterator<Execution> otherIterator = otherTrace.getTraceAsSortedSet().iterator();
            for (final Execution r1 : this.t.getTraceAsSortedSet()) {
                final Execution r2 = otherIterator.next();
                if (!this.executionsEqual(r1, r2)) {
                    return false;
                }
            }
            return true;
        }
    }
}
