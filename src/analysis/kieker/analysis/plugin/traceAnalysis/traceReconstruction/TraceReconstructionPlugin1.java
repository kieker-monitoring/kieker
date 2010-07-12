package kieker.analysis.plugin.traceAnalysis.traceReconstruction;

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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.common.util.LoggingTimestampConverter;
import kieker.analysis.datamodel.Execution;
import kieker.analysis.datamodel.ExecutionTrace;
import kieker.analysis.datamodel.InvalidExecutionTrace;
import kieker.analysis.datamodel.MessageTrace;
import kieker.analysis.datamodel.repository.SystemModelRepository;
import kieker.analysis.plugin.IAnalysisPlugin;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.analysis.plugin.configuration.IOutputPort;
import kieker.analysis.plugin.configuration.OutputPort;
import kieker.analysis.plugin.traceAnalysis.AbstractTraceProcessingPlugin;
import kieker.analysis.plugin.traceAnalysis.executionRecordTransformation.ExecutionEventProcessingException;
import kieker.analysis.plugin.util.event.EventProcessingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public class TraceReconstructionPlugin1 extends AbstractTraceProcessingPlugin
        implements IAnalysisPlugin {

    private static final Log log = LogFactory.getLog(TraceReconstructionPlugin1.class);
    public static final long MAX_TIMESTAMP = Long.MAX_VALUE;
    public static final long MIN_TIMESTAMP = 0;
    private static final long MAX_DURATION_NANOS = Long.MAX_VALUE;
    public static final int MAX_DURATION_MILLIS = Integer.MAX_VALUE;

    public enum TraceEquivalenceClassModes {

        DISABLED, ASSEMBLY, ALLOCATION
    }
    /** TraceId x trace */
    private final Hashtable<Long, ExecutionTrace> pendingTraces = new Hashtable<Long, ExecutionTrace>();
    /** Representative x # of equivalents */
    private final HashMap<ExecutionTraceHashContainer, AtomicInteger> eTracesEquivClassesMap = new HashMap<ExecutionTraceHashContainer, AtomicInteger>();
    /** We need to keep track of invalid trace's IDs */
    private final Set<Long> invalidTraces = new TreeSet<Long>();

    public Set<Long> getInvalidTraces() {
        return this.invalidTraces;
    }
    /** Timestamp of most recent execution x trace */
    private final TreeSet<ExecutionTrace> timeoutMap = new TreeSet<ExecutionTrace>(
            new Comparator<ExecutionTrace>() {

                /** Order traces by tins */
                public int compare(final ExecutionTrace t1,
                        final ExecutionTrace t2) {
                    if (t1 == t2) {
                        return 0;
                    }
                    final long t1LowestTin = t1.getTraceAsSortedSet().first().getTin();
                    final long t2LowestTin = t2.getTraceAsSortedSet().first().getTin();
                    return t1LowestTin < t2LowestTin ? -1 : 1;
                }
            });
    private final long maxTraceDurationNanos;
    private long minTin = -1;

    public final long getFirstTimestamp() {
        return this.minTin;
    }
    private long highestTout = -1;

    /** Return latest timestamp */
    public final long getLastTimestamp() {
        return this.highestTout;
    }
    private volatile boolean terminate = false;
    private final boolean ignoreInvalidTraces;
    // private final boolean onlyEquivClasses;
    private final TraceEquivalenceClassModes equivalenceMode;
    private final long ignoreRecordsBeforeTimestamp;
    private final long ignoreRecordsAfterTimestamp;

    private final TreeSet<Long> selectedTraces;
    private final Execution rootExecution;

    public boolean execute() {
        return true; // no need to do anything here
    }

    public TraceReconstructionPlugin1(
            final String name,
            final SystemModelRepository systemEntityFactory,
            final long maxTraceDurationMillis,
            final boolean ignoreInvalidTraces,
            // final boolean onlyEquivClasses,
            final TraceEquivalenceClassModes traceEquivalenceCallMode,
            final TreeSet<Long> selectedTraces, final long ignoreRecordsBefore,
            final long ignoreRecordsAfter) {
        super(name, systemEntityFactory);
        this.rootExecution = new Execution(
                super.getSystemEntityFactory().getOperationFactory().rootOperation,
                super.getSystemEntityFactory().getAllocationFactory().rootAllocationComponent,
                -1, "-1", -1, -1, -1, -1);
        if (maxTraceDurationMillis < 0) {
            throw new IllegalArgumentException(
                    "value maxTraceDurationMillis must not be negative (found: "
                    + maxTraceDurationMillis + ")");
        }
        if (maxTraceDurationMillis == TraceReconstructionPlugin1.MAX_DURATION_MILLIS) {
            this.maxTraceDurationNanos = TraceReconstructionPlugin1.MAX_DURATION_NANOS;
        } else {
            this.maxTraceDurationNanos = maxTraceDurationMillis * (1000 * 1000);
        }
        this.ignoreInvalidTraces = ignoreInvalidTraces;
        // this.onlyEquivClasses = onlyEquivClasses;
        this.equivalenceMode = traceEquivalenceCallMode;
        this.selectedTraces = selectedTraces;
        this.ignoreRecordsBeforeTimestamp = ignoreRecordsBefore;
        this.ignoreRecordsAfterTimestamp = ignoreRecordsAfter;
    }

    private void newExecution(final Execution execution) {
        if (execution.getTin() < this.ignoreRecordsBeforeTimestamp
                || execution.getTout() > this.ignoreRecordsAfterTimestamp) {
            return;
        }

        final long traceId = execution.getTraceId();

        if (this.selectedTraces != null
                && !this.selectedTraces.contains(traceId)) {
            // not interested in this trace
            return;
        }

        this.minTin = (this.minTin < 0 || execution.getTin() < this.minTin) ? execution.getTin()
                : this.minTin;
        this.highestTout = execution.getTout() > this.highestTout ? execution.getTout() : this.highestTout;
        ExecutionTrace seq = this.pendingTraces.get(traceId);
        if (seq != null) { // create and add new sequence
            if (!this.timeoutMap.remove(seq)) { // remove from timeoutMap. Will
                // be re-added below
                TraceReconstructionPlugin1.log.error("Missing entry for trace in timeoutMap: " + seq);
            }
        } else {
            seq = new ExecutionTrace(traceId);
            this.pendingTraces.put(traceId, seq);
        }
        try {
            seq.add(execution);
        } catch (final InvalidTraceException ex) { // this would be a bug!
            log.fatal(
                    "Attempt to add record to wrong trace", ex);
        }
        if (!this.timeoutMap.add(seq)) { // (re-)add trace to timeoutMap
            TraceReconstructionPlugin1.log.error("Equal entry existed in timeout already:" + seq);
        }
        try {
            this.processQueue();
        } catch (ExecutionEventProcessingException ex) {
            log.error("ExecutionEventProcessingException occured", ex);
        }
    }

    private void processQueue() throws ExecutionEventProcessingException {
        while (!this.timeoutMap.isEmpty()
                && (this.terminate || (this.timeoutMap.first().getTraceAsSortedSet().first().getTin() < (this.highestTout - this.maxTraceDurationNanos)))) {
            final ExecutionTrace polledTrace = this.timeoutMap.pollFirst();
            final long curTraceId = polledTrace.getTraceId();
            this.pendingTraces.remove(curTraceId);
            // log.info("Removed pending trace (ID:" + curTraceId + "):" +
            // polledTrace);
            try {
                // if the polled trace is invalid, the following method
                // toMesageTrace
                // throws an exception
                final MessageTrace mt = polledTrace.toMessageTrace(this.rootExecution);
                boolean isNewTrace = true;
                // if (this.onlyEquivClasses) {
                if (this.equivalenceMode != TraceEquivalenceClassModes.DISABLED) {
                    final ExecutionTraceHashContainer polledTraceHashContainer = new ExecutionTraceHashContainer(
                            polledTrace);
                    AtomicInteger numOccurences = this.eTracesEquivClassesMap.get(polledTraceHashContainer);
                    if (numOccurences == null) {
                        numOccurences = new AtomicInteger(1);
                        this.eTracesEquivClassesMap.put(
                                polledTraceHashContainer, numOccurences);
                    } else {
                        isNewTrace = false;
                        numOccurences.incrementAndGet();
                    }
                }

                if (!isNewTrace) {
                    continue;
                }

                if (mt != null) {
                    this.messageTraceOutputPort.deliver(mt);
                    this.executionTraceOutputPort.deliver(polledTrace);
                }
                this.reportSuccess(curTraceId);
            } catch (final InvalidTraceException ex) {
//                try {
                    this.invalidExecutionTraceOutputPort.deliver(new InvalidExecutionTrace(polledTrace));
//                } catch (final EventProcessingException ex1) {
//                    log.error(
//                            "EventProcessingException for trace ID:"
//                            + curTraceId, ex1);
//                    this.reportError(curTraceId);
//                    throw new ExecutionEventProcessingException(
//                            "EventProcessingException for trace ID:"
//                            + curTraceId, ex1);
//                }
                if (!this.invalidTraces.contains(curTraceId)) {
                    // only once per traceID (otherwise, we would report all
                    // trace fragments)
                    this.reportError(curTraceId);
                    this.invalidTraces.add(curTraceId);
                    if (!this.ignoreInvalidTraces) {
                        TraceReconstructionPlugin1.log.error(
                                "Failed to transform execution trace to message trace (ID:"
                                + curTraceId + "): " + polledTrace, ex);
                        throw new ExecutionEventProcessingException(
                                "Failed to transform execution trace to message trace (ID:"
                                + curTraceId + "): " + polledTrace, ex);
                    }
                }
            }
//            catch (final EventProcessingException ex) {
//                TraceReconstructionPlugin1.log.error(
//                        "EventProcessingException for trace ID:" + curTraceId,
//                        ex);
//                this.reportError(curTraceId);
//                throw new ExecutionEventProcessingException(
//                        "EventProcessingException for trace ID:" + curTraceId,
//                        ex);
//            }
        }
    }

    @Override
    public void terminate(final boolean error) {
        try {
            this.terminate = true;
            this.processQueue();
        } catch (final ExecutionEventProcessingException ex) {
            TraceReconstructionPlugin1.log.error("Error prossessing queue", ex);
        }
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
                if (TraceReconstructionPlugin1.this.equivalenceMode == TraceEquivalenceClassModes.ALLOCATION) {
                    h ^= r.getAllocationComponent().getId();
                } else if (TraceReconstructionPlugin1.this.equivalenceMode == TraceEquivalenceClassModes.ASSEMBLY) {
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
            final boolean retVal = (((TraceReconstructionPlugin1.this.equivalenceMode == TraceEquivalenceClassModes.ALLOCATION) && r1.getAllocationComponent().getId() == r2.getAllocationComponent().getId()) || ((TraceReconstructionPlugin1.this.equivalenceMode == TraceEquivalenceClassModes.ASSEMBLY) && r1.getAllocationComponent().getAssemblyComponent().getId() == r2.getAllocationComponent().getAssemblyComponent().getId()))
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

    @Override
    public void printStatusMessage() {
        super.printStatusMessage();
        final String minTinStr = new StringBuilder().append(this.minTin).append(" (").append(
                LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.minTin)).append(",").append(
                LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(this.minTin)).append(")").toString();
        final String maxToutStr = new StringBuilder().append(this.highestTout).append(" (").append(
                LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.highestTout)).append(",").append(
                LoggingTimestampConverter.convertLoggingTimestampLocalTimeZoneString(this.highestTout)).append(")").toString();
        System.out.println("First timestamp: " + minTinStr);
        System.out.println("Last timestamp: " + maxToutStr);
    }

    private final IInputPort<Execution> executionInputPort =
            new AbstractInputPort<Execution>("Execution input"){
        public void newEvent(Execution event) {
            newExecution(event);
        }
    };

    public IInputPort<Execution> getExecutionInputPort(){
        return this.executionInputPort;
    }

    private final OutputPort<MessageTrace> messageTraceOutputPort =
            new OutputPort<MessageTrace>("Reconstructed Message Traces");

    public IOutputPort<MessageTrace> getMessageTraceOutputPort(){
        return this.messageTraceOutputPort;
    }

    private final OutputPort<ExecutionTrace> executionTraceOutputPort =
            new OutputPort<ExecutionTrace>("Reconstructed Execution Traces");

    public IOutputPort<ExecutionTrace> getExecutionTraceOutputPort(){
        return this.executionTraceOutputPort;
    }

    private final OutputPort<InvalidExecutionTrace> invalidExecutionTraceOutputPort =
            new OutputPort<InvalidExecutionTrace>("Invalid Execution Traces");

    public IOutputPort<InvalidExecutionTrace> getInvalidExecutionTraceOutputPort(){
        return this.invalidExecutionTraceOutputPort;
    }
}
