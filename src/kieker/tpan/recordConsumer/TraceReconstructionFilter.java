package kieker.tpan.recordConsumer;

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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import kieker.common.logReader.RecordConsumerExecutionException;
import kieker.tpan.datamodel.InvalidTraceException;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.tpan.datamodel.ExecutionTrace;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.datamodel.system.factories.SystemEntityFactory;
import kieker.tpan.plugins.AbstractTpanTraceProcessingComponent;
import kieker.tpan.plugins.TraceProcessingException;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;

/**
 *
 * @author Andre van Hoorn
 */
public class TraceReconstructionFilter extends AbstractTpanTraceProcessingComponent implements IKiekerRecordConsumer {

    private static final Log log = LogFactory.getLog(TraceReconstructionFilter.class);
    /** TraceId x trace */
    private final Hashtable<Long, ExecutionTrace> pendingTraces = new Hashtable<Long, ExecutionTrace>();
    private final boolean considerHostname;
    /** Representative x # of equivalents */
    private final HashMap<ExecutionTraceHashContainer, AtomicInteger> eTracesEquivClassesMap =
            new HashMap<ExecutionTraceHashContainer, AtomicInteger>();
    /** We need to keep track of invalid trace's IDs */
    private final Set<Long> invalidTraces = new TreeSet<Long>();

    public static final long MAX_TIMESTAMP = Long.MAX_VALUE;
    public static final long MIN_TIMESTAMP = 0;
    private static final long MAX_DURATION_NANOS = Long.MAX_VALUE;
    public static final int MAX_DURATION_MILLIS = Integer.MAX_VALUE;

    public Set<Long> getInvalidTraces() {
        return invalidTraces;
    }
    /** Timestamp of most recent execution x trace */
    private final TreeSet<ExecutionTrace> timeoutMap =
            new TreeSet<ExecutionTrace>(new Comparator<ExecutionTrace>() {

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
    private final long maxTraceDurationNanos;
    private long highestTout = -1;
    private boolean terminate = false;
    private final boolean ignoreInvalidTraces;
    private final boolean onlyEquivClasses;
    private final long ignoreRecordsBeforeTimestamp;
    private final long ignoreRecordsAfterTimestamp;
    private List<IMessageTraceReceiver> messageTraceListeners = new ArrayList<IMessageTraceReceiver>();
    private List<IExecutionTraceReceiver> executionTraceListeners = new ArrayList<IExecutionTraceReceiver>();
    private List<IExecutionTraceReceiver> invalidExecutionTraceArtifactListeners = new ArrayList<IExecutionTraceReceiver>();
    private final TreeSet<Long> selectedTraces;
    private final static String[] recordTypeSubscriptionList = {
        KiekerExecutionRecord.class.getName()
    };

    public TraceReconstructionFilter(final String name, 
            final SystemEntityFactory systemEntityFactory,
            final long maxTraceDurationMillis,
            final boolean ignoreInvalidTraces,
            final boolean onlyEquivClasses, final boolean considerHostname,
            final TreeSet<Long> selectedTraces,
            final long ignoreRecordsBefore, final long ignoreRecordsAfter) {
        super(name, systemEntityFactory);
        if (maxTraceDurationMillis < 0){
            throw new IllegalArgumentException("value maxTraceDurationMillis must not be negative (found: "+maxTraceDurationMillis+")");
        }
        if (maxTraceDurationMillis == MAX_DURATION_MILLIS){
            this.maxTraceDurationNanos = MAX_DURATION_NANOS;
        } else {
            this.maxTraceDurationNanos = maxTraceDurationMillis * (1000 * 1000);
        } 
        this.ignoreInvalidTraces = ignoreInvalidTraces;
        this.onlyEquivClasses = onlyEquivClasses;
        this.considerHostname = considerHostname;
        this.selectedTraces = selectedTraces;
        this.ignoreRecordsBeforeTimestamp = ignoreRecordsBefore;
        this.ignoreRecordsAfterTimestamp = ignoreRecordsAfter;
    }

    public void consumeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) throws RecordConsumerExecutionException {
        if (!(monitoringRecord instanceof KiekerExecutionRecord)) {
            log.error("Received monitoring record no instance of " + KiekerExecutionRecord.class.getName());
        }
        KiekerExecutionRecord execRecord = (KiekerExecutionRecord) monitoringRecord;

        long curLoggingTimestamp = monitoringRecord.getLoggingTimestamp();
        if (curLoggingTimestamp < this.ignoreRecordsBeforeTimestamp
                || curLoggingTimestamp > this.ignoreRecordsAfterTimestamp){
            return;
        }

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
        try {
            seq.add(execRecord);
        } catch (InvalidTraceException ex) { // this would be a bug!
            log.fatal("Attempt to add record to wrong trace", ex);
            throw new RecordConsumerExecutionException("Attempt to add record to wrong trace");
        }
        if (!this.timeoutMap.add(seq)) { // (re-)add trace to timeoutMap
            log.error("Equal entry existed in timeout already:" + seq);
        }

        this.processQueue();

    }

    private void processQueue() throws RecordConsumerExecutionException {
        while (!timeoutMap.isEmpty()
                && (terminate
                || (timeoutMap.first().getTraceAsSortedSet().first().tin < (highestTout - maxTraceDurationNanos)))) {
            ExecutionTrace polledTrace = timeoutMap.pollFirst();
            long curTraceId = polledTrace.getTraceId();
            pendingTraces.remove(curTraceId);
            //log.info("Removed pending trace (ID:" + curTraceId + "):" + polledTrace);
            try {
                // if the polled trace is invalid, the following method toMesageTrace
                // throws an exception
                MessageTrace mt = polledTrace.toMessageTrace();
                boolean isNewTrace = true;
                if (this.onlyEquivClasses) {
                    ExecutionTraceHashContainer polledTraceHashContainer =
                            new ExecutionTraceHashContainer(polledTrace);
                    AtomicInteger numOccurences = this.eTracesEquivClassesMap.get(polledTraceHashContainer);
                    if (numOccurences == null) {
                        numOccurences = new AtomicInteger(1);
                        this.eTracesEquivClassesMap.put(polledTraceHashContainer, numOccurences);
                    } else {
                        isNewTrace = false;
                        numOccurences.incrementAndGet();
                    }
                }

                if (!isNewTrace) {
                    continue;
                }

                if (mt != null) {
                    for (IMessageTraceReceiver l : messageTraceListeners) {
                        l.newTrace(mt);
                    }
                    for (IExecutionTraceReceiver l : executionTraceListeners) {
                        l.newTrace(polledTrace);
                    }
                }
                this.reportSuccess(curTraceId);
            } catch (InvalidTraceException ex) {
                for (IExecutionTraceReceiver l : invalidExecutionTraceArtifactListeners) {
                    try {
                        l.newTrace(polledTrace);
                    } catch (TraceProcessingException ex1) {
                        log.error("Trace processing exception (ID:" + curTraceId + ")", ex);
                        this.reportError(curTraceId);
                        throw new RecordConsumerExecutionException("Trace processing exception", ex);
                    }
                }
                if (!this.invalidTraces.contains(curTraceId)) {
                    // only once per traceID (otherwise, we would report all trace fragments)
                    this.reportError(curTraceId);
                    this.invalidTraces.add(curTraceId);
                    if (!ignoreInvalidTraces) {
                        log.error("Failed to transform execution trace to message trace (ID:" + curTraceId + "): " + polledTrace, ex);
                        throw new RecordConsumerExecutionException("Failed to transform execution trace to message trace (ID:" + curTraceId + "): " + polledTrace, ex);
                    }
                }
            } catch (TraceProcessingException ex) {
                log.error("Trace processing exception (ID:" + curTraceId + ")", ex);
                this.reportError(curTraceId);
                throw new RecordConsumerExecutionException("Trace processing exception", ex);
            }
        }
    }

    public void addMessageTraceListener(IMessageTraceReceiver l) {
        this.messageTraceListeners.add(l);
    }

    public void addExecutionTraceListener(IExecutionTraceReceiver l) {
        this.executionTraceListeners.add(l);
    }

    public void addInvalidExecutionTraceArtifactListener(IExecutionTraceReceiver l) {
        this.invalidExecutionTraceArtifactListeners.add(l);
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

    public HashMap<ExecutionTrace, Integer> getEquivalenceClassMap() {
        final HashMap<ExecutionTrace, Integer> map = new HashMap<ExecutionTrace, Integer>();
        for (Entry<ExecutionTraceHashContainer, AtomicInteger> entry : this.eTracesEquivClassesMap.entrySet()) {
            map.put(entry.getKey().t, entry.getValue().intValue());
        }
        return map;
    }

    @Override
    public void cleanup() {
        // no need to do anything
    }

    private class ExecutionTraceHashContainer {

        private final ExecutionTrace t;
        private final int hashCode;

        public ExecutionTraceHashContainer(final ExecutionTrace t) {
            this.t = t;
            int h = 0;
            // TODO: need a better hash function considering the order (e.g., MD5)
            for (KiekerExecutionRecord r : t.getTraceAsSortedSet()) {
                h ^= r.componentName.hashCode();
                h ^= r.opname.hashCode();
                if (considerHostname) {
                    h ^= r.vmName.hashCode();
                }
            }
            //
            this.hashCode = h;
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        private boolean executionsEqual(KiekerExecutionRecord r1, KiekerExecutionRecord r2) {
            if (r1 == r2) {
                return true;
            }
            if (r1 == null || r2 == null) {
                return false;
            }
            return r1.componentName.equals(r2.componentName)
                    && r1.opname.equals(r2.opname)
                    && r1.eoi == r2.eoi
                    && r1.ess == r2.ess
                    && (!considerHostname || r1.vmName.equals(r2.vmName));
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof ExecutionTrace)) {
                return false;
            }
            ExecutionTrace otherTrace = ((ExecutionTraceHashContainer) obj).t;
            if (this.t.getLength() != otherTrace.getLength()) {
                return false;
            }
            Iterator<KiekerExecutionRecord> otherIterator = otherTrace.getTraceAsSortedSet().iterator();
            for (KiekerExecutionRecord r1 : this.t.getTraceAsSortedSet()) {
                if (!this.executionsEqual(r1, otherIterator.next())) {
                    return false;
                }
            }
            return true;
        }
    }
}
