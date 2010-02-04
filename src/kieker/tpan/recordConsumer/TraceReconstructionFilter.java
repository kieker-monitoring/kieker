package kieker.tpan.recordConsumer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kieker.common.logReader.RecordConsumerExecutionException;
import kieker.tpan.datamodel.InvalidTraceException;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.TreeSet;
import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.tpan.datamodel.ExecutionTrace;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;

/*
 *kieker.loganalysis.datamodel.ExecutionSequenceRepository
 *
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
/**
 *
 * @author Andre van Hoorn
 */
public class TraceReconstructionFilter implements IKiekerRecordConsumer {

    private static final Log log = LogFactory.getLog(TraceReconstructionFilter.class);
    /** TraceId x trace */
    private Hashtable<Long, ExecutionTrace> pendingTraces = new Hashtable<Long, ExecutionTrace>();
    /** Timestamp of most recent execution x trace */
    private TreeSet<ExecutionTrace> timeoutMap = new TreeSet<ExecutionTrace>(new Comparator<ExecutionTrace>() {

        /** Order traces by tins  */
        public int compare(ExecutionTrace t1, ExecutionTrace t2) {
            if (t1 == t2) {
                return 0;
            }
            long t1LowestTin = t1.getTraceAsSortedSet().first().tin;
            long t2LowestTin = t2.getTraceAsSortedSet().first().tin;
            return t1LowestTin < t2LowestTin ? -1 : 1;
        }
    });
    private final long maxTraceDurationNanosecs;
    private long highestTout = -1;
    private boolean terminate = false;
    private boolean ignoreInvalidTraces = false;
    private List<IMessageTraceReceiver> messageTraceListeners = new ArrayList<IMessageTraceReceiver>();
    private List<IExecutionTraceReceiver> executionTraceListeners = new ArrayList<IExecutionTraceReceiver>();
    private final TreeSet<Long> selectedTraces;
    private final static String[] recordTypeSubscriptionList = {
        KiekerExecutionRecord.class.getName()
    };

    public TraceReconstructionFilter(final long maxTraceDurationSecs, final boolean ignoreInvalidTraces, final TreeSet<Long> selectedTraces) {
        if (maxTraceDurationSecs > 0) {
            this.maxTraceDurationNanosecs = maxTraceDurationSecs / (1000 * 1000 * 1000);
        } else {
            this.maxTraceDurationNanosecs = Long.MAX_VALUE;
        }
        this.ignoreInvalidTraces = ignoreInvalidTraces;
        this.selectedTraces = selectedTraces;
    }

    public void consumeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) throws RecordConsumerExecutionException {
        if (!(monitoringRecord instanceof KiekerExecutionRecord)) {
            log.error("Received monitoring record no instance of " + KiekerExecutionRecord.class.getName());
        }
        KiekerExecutionRecord execRecord = (KiekerExecutionRecord) monitoringRecord;

        long traceId = execRecord.traceId;

        if (this.selectedTraces != null && !this.selectedTraces.contains(traceId)) {
            // not interested in this trace
            return;
        }

        this.highestTout = execRecord.tout > this.highestTout ? execRecord.tout : this.highestTout;
        ExecutionTrace seq = pendingTraces.get(traceId);
        if (seq != null) { // create and add new sequence
            if (!this.timeoutMap.remove(seq)) { // remove from timeoutMap. Will be re-added below
                log.error("Missing entry for trace in timeoutMap: " + seq);
            }
        } else {
            seq = new ExecutionTrace(traceId);
            pendingTraces.put(traceId, seq);
        }
        seq.add(execRecord);
        if (!this.timeoutMap.add(seq)) { // (re-)add trace to timeoutMap
            log.error("Equal entry existed in timeout already:" + seq);
        }
        this.processQueue();
    }

    private void processQueue() throws RecordConsumerExecutionException {
        while (!timeoutMap.isEmpty()
                && (terminate
                || (timeoutMap.first().getTraceAsSortedSet().first().tin < (highestTout - maxTraceDurationNanosecs)))) {
            ExecutionTrace polledTrace = timeoutMap.pollFirst();
            pendingTraces.remove(polledTrace.getTraceId());
            log.info("Removed pending trace:" + polledTrace);
            try {
                MessageTrace mt = polledTrace.toMessageTrace();
                if (mt != null) {
                    for (IMessageTraceReceiver l : messageTraceListeners) {
                        l.newTrace(mt);
                    }
                    for (IExecutionTraceReceiver l : executionTraceListeners) {
                        l.newTrace(polledTrace);
                    }
                }
            } catch (InvalidTraceException ex) {
                if (!ignoreInvalidTraces) {
                    log.error("Failed to transform execution trace to message trace: " + polledTrace, ex);
                    throw new RecordConsumerExecutionException("Failed to transform execution trace to message trace: " + polledTrace, ex);
                }
            }
        }
    }

    public void addMessageTraceListener(IMessageTraceReceiver l) {
        this.messageTraceListeners.add(l);
    }

    public void addExecutionTraceListener(IExecutionTraceReceiver l) {
        this.executionTraceListeners.add(l);
    }

    public boolean execute() throws RecordConsumerExecutionException {
        return true;
    }

    public String[] getRecordTypeSubscriptionList() {
        return recordTypeSubscriptionList;
    }

    public void terminate() {
        try {
            this.terminate = true;
            this.processQueue();
        } catch (RecordConsumerExecutionException ex) {
            log.error("Error prossessing queue", ex);
        }
    }
}
