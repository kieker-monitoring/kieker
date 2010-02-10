package kieker.tpan.datamodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.Vector;
import kieker.tpan.TpanInstance;
import kieker.tpan.tools.LoggingTimestampConverterTool;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 *
 * ==================LICENCE=========================
 * Copyright 2009 Kieker Project
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

/** @author Andre van Hoorn
 */
public class ExecutionTrace {
    private static final Log log = LogFactory.getLog(ExecutionTrace.class);
    private long traceId = -1; // convenience field. All executions have this traceId.
    private SortedSet<KiekerExecutionRecord> set = new TreeSet<KiekerExecutionRecord>();

    private long minTin = Long.MAX_VALUE;
    private long maxTout = Long.MIN_VALUE;
    private int maxStackDepth = -1;

    private ExecutionTrace() {
    }

    public ExecutionTrace(long traceId) {
        this.traceId = traceId;
    }

    public long getTraceId() {
        return traceId;
    }

    public void add(KiekerExecutionRecord record) throws InvalidTraceException {
        if (this.traceId != record.traceId){
            throw new InvalidTraceException("TraceId of new record ("+record.traceId+") differs from Id of this trace ("+this.traceId+")");
        }
        if (record.tin < this.minTin) this.minTin = record.tin;
        if (record.tout > this.maxTout) this.maxTout = record.tout;
        if (record.ess > this.maxStackDepth) this.maxStackDepth = record.ess;
        this.set.add(record);
    }

    public MessageTrace toMessageTrace() throws InvalidTraceException {
        Vector<Message> mSeq = new Vector<Message>();
        Stack<Message> curStack = new Stack<Message>();
        Iterator<KiekerExecutionRecord> eSeqIt = this.set.iterator();
        KiekerExecutionRecord curE = null, prevE = null;
        int itNum = 0;
        //log.info("Analyzing trace " + this.traceId);
        int prevEoi = -1;
        while (eSeqIt.hasNext()) {
            curE = eSeqIt.next();
            if(itNum++ == 0 && curE.ess != 0){
                InvalidTraceException ex = 
                        new InvalidTraceException("First execution must have ess "+
                        "0 (found " + curE.ess + ")\n Causing execution: " + curE);
                log.fatal("Found invalid trace", ex);
                throw ex;
            }
            if (prevEoi!=curE.eoi-1){
                InvalidTraceException ex =
                        new InvalidTraceException("Eois must increment by 1 --" +
                        "but found sequence <"+prevEoi+","+curE.eoi+">" +"(Execution: "+curE+")");
                log.fatal("Found invalid trace", ex);
                throw ex;
            }
            prevEoi = curE.eoi;

            /*log.info("");
            log.info("Iteration" + (itNum++));
            log.info("curE:" + curE);
            log.info("prevE:" + prevE);*/
            // First, we might need to clean up the stack for the next execution callMessage 
            if (prevE != null && prevE.ess >= curE.ess) {
                //log.info("Cleaning stack ...");
                KiekerExecutionRecord curReturnReceiver; // receiverComponentName of return message
                while (curStack.size() > curE.ess) {
                    //log.info("loop begin: curStack.size() " + curStack.size());
                    prevE = curStack.pop().execution;
                    curReturnReceiver = curStack.peek().execution;
                    Message m = new Message(false, prevE.tout, prevE, curReturnReceiver, prevE);
                    mSeq.add(m);
                    prevE = curReturnReceiver;
                    //log.info(m);
                    //log.info("loop end: curStack.size() " + curStack.size());
                }
            }
            // Now, we handle the current execution callMessage 
            if (prevE == null) { // initial execution callMessage
                Message m = new Message(true, curE.tin, null, curE, curE);
                mSeq.add(m);
                curStack.push(m);
            } else if (prevE.ess < curE.ess) { // usual callMessage with senderComponentName and receiverComponentName
                Message m = new Message(true, curE.tin, prevE, curE, curE);
                mSeq.add(m);
                curStack.push(m);
            }
            if (!eSeqIt.hasNext()) { // empty stack completely, since no more executions
                while (!curStack.empty()) {
                    curE = curStack.pop().execution;
                    prevE = curStack.empty() ? null : curStack.peek().execution;
                    Message m = new Message(false, curE.tout, curE, prevE, curE);
                    mSeq.add(m);
                }
            }
            prevE = curE; // prepair next loop
        }
        return new MessageTrace(this.traceId, mSeq);
    }

    public final SortedSet<KiekerExecutionRecord> getTraceAsSortedSet() {
        return this.set;
    }

    public final int getLength(){
        return this.set.size();
    }

    public String toString() {
        StringBuilder strBuild = new StringBuilder("TraceId " + this.traceId)
                .append(" (minTin=").append(this.minTin).append(" (")
                .append(LoggingTimestampConverterTool.convertLoggingTimestampToUTCString(this.minTin)).append(")")
                .append("; maxTout=").append(this.maxTout).append(" (")
                .append(LoggingTimestampConverterTool.convertLoggingTimestampToUTCString(this.maxTout)).append(")")
                .append("; maxStackDepth=").append(this.maxStackDepth)
                .append("):\n");
        Iterator<KiekerExecutionRecord> it = set.iterator();
        while (it.hasNext()) {
            KiekerExecutionRecord e = it.next();
            strBuild.append("<");
            strBuild.append("[").append(e.eoi)
                    .append(",").append(e.ess).append("]").append(" ");
            strBuild.append(e.tin).append("-").append(e.tout).append(" ");
            strBuild.append((e.vmName!=null)?e.vmName:"<NOVMNAME>").append("::");
            strBuild.append((e.componentName!=null)?e.componentName:"<NOCOMPONENTNAME>").append(".");            
            strBuild.append((e.opname!=null)?e.opname:"<NOOPNAME>").append(" ");            
            
            strBuild.append((e.sessionId!=null)?e.sessionId:"<NOSESSIONID>");            
            
            strBuild.append(">\n");
        }
        return strBuild.toString();
    }

    public int getMaxStackDepth() {
        return this.maxStackDepth;
    }

    public long getMaxTout() {
        return this.maxTout;
    }

    public long getMinTin() {
        return this.minTin;
    }

}
