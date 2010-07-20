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
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;


import kieker.common.util.LoggingTimestampConverter;
import kieker.analysis.datamodel.Execution;
import kieker.analysis.datamodel.ExecutionTrace;
import kieker.analysis.datamodel.InvalidExecutionTrace;
import kieker.analysis.datamodel.MessageTrace;
import kieker.analysis.datamodel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.analysis.plugin.configuration.IOutputPort;
import kieker.analysis.plugin.configuration.OutputPort;
import kieker.analysis.plugin.traceAnalysis.AbstractTraceProcessingPlugin;
import kieker.analysis.plugin.traceAnalysis.executionRecordTransformation.ExecutionEventProcessingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class TraceReconstructionFilter extends AbstractTraceProcessingPlugin {

    private static final Log log = LogFactory.getLog(TraceReconstructionFilter.class);

    private static final long MAX_DURATION_NANOS = Long.MAX_VALUE;
    public static final int MAX_DURATION_MILLIS = Integer.MAX_VALUE;

    /** TraceId x trace */
    private final Hashtable<Long, ExecutionTrace> pendingTraces = new Hashtable<Long, ExecutionTrace>();
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

    private final Execution rootExecution;

    public boolean execute() {
        return true; // no need to do anything here
    }

    public TraceReconstructionFilter(
            final String name,
            final SystemModelRepository systemEntityFactory,
            final Execution rootExecution,
            final long maxTraceDurationMillis,
            final boolean ignoreInvalidTraces
            // final boolean onlyEquivClasses,
            ) {
        super(name, systemEntityFactory);
        this.rootExecution = rootExecution;
        if (maxTraceDurationMillis < 0) {
            throw new IllegalArgumentException(
                    "value maxTraceDurationMillis must not be negative (found: "
                    + maxTraceDurationMillis + ")");
        }
        if (maxTraceDurationMillis == TraceReconstructionFilter.MAX_DURATION_MILLIS) {
            this.maxTraceDurationNanos = TraceReconstructionFilter.MAX_DURATION_NANOS;
        } else {
            this.maxTraceDurationNanos = maxTraceDurationMillis * (1000 * 1000);
        }
        this.ignoreInvalidTraces = ignoreInvalidTraces;
        // this.onlyEquivClasses = onlyEquivClasses;
    }

    private void newExecution(final Execution execution) {
        final long traceId = execution.getTraceId();

        this.minTin = (this.minTin < 0 || execution.getTin() < this.minTin) ? execution.getTin()
                : this.minTin;
        this.highestTout = execution.getTout() > this.highestTout ? execution.getTout() : this.highestTout;
        ExecutionTrace seq = this.pendingTraces.get(traceId);
        if (seq != null) { // create and add new sequence
            if (!this.timeoutMap.remove(seq)) { // remove from timeoutMap. Will
                // be re-added below
                TraceReconstructionFilter.log.error("Missing entry for trace in timeoutMap: " + seq);
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
            TraceReconstructionFilter.log.error("Equal entry existed in timeout already:" + seq);
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
                        TraceReconstructionFilter.log.error(
                                "Failed to transform execution trace to message trace (ID:"
                                + curTraceId + "): " + polledTrace, ex);
                        throw new ExecutionEventProcessingException(
                                "Failed to transform execution trace to message trace (ID:"
                                + curTraceId + "): " + polledTrace, ex);
                    }
                }
            }
//            catch (final EventProcessingException ex) {
//                TraceReconstructionPlugin.log.error(
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
            TraceReconstructionFilter.log.error("Error processing queue", ex);
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
