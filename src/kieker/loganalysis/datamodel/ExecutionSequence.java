package kieker.loganalysis.datamodel;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.loganalysis.datamodel.ExecutionSequence
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
 *
 * @author Andre van Hoorn
 */
public class ExecutionSequence {

    private static final Log log = LogFactory.getLog(ExecutionSequence.class);
    private long traceId = -1; // convenience field. All executions have this traceId.
    private SortedSet<KiekerExecutionRecord> sequence = new TreeSet<KiekerExecutionRecord>();

    private ExecutionSequence() {
    }

    public ExecutionSequence(long traceId) {
        this.traceId = traceId;
    }

    public long getTraceId() {
        return traceId;
    }

    public void add(KiekerExecutionRecord record) {
        // TODO: check traceId
        this.sequence.add(record);
    }

    public MessageSequence toMessageSequence() throws InvalidTraceException {
        Vector<Message> mSeq = new Vector<Message>();
        Stack<Message> curStack = new Stack<Message>();
        Iterator<KiekerExecutionRecord> eSeqIt = this.sequence.iterator();
        KiekerExecutionRecord curE = null, prevE = null;
        int itNum = 0;
        //log.info("Analyzing trace " + this.traceId);
        while (eSeqIt.hasNext()) {
            curE = eSeqIt.next();
            if(itNum++ == 0 && curE.ess != 0){
                InvalidTraceException ex = new InvalidTraceException("First execution must have ess 0 (found " + curE.ess + ")\n Causing execution: " + curE);
                log.fatal("Found invalid trace", ex);
                throw ex;
            }
            /*log.info("");
            log.info("Iteration" + (itNum++));
            log.info("curE:" + curE);
            log.info("prevE:" + prevE);*/
            // First, we might need to clean up the stack for the next execution callMessage 
            if (prevE != null && prevE.ess >= curE.ess) {
                //log.info("Cleaning stack ...");
                KiekerExecutionRecord curReturnReceiver; // receiver of return message
                while (curStack.size() > curE.ess) {
                    //log.info("loop begin: curStack.size() " + curStack.size());
                    prevE = curStack.pop().execution;
                    curReturnReceiver = curStack.peek().execution;
                    Message m = new Message(false, prevE.tout, prevE.componentName, curReturnReceiver.componentName, prevE);
                    mSeq.add(m);
                    prevE = curReturnReceiver;
                    //log.info(m);
                    //log.info("loop end: curStack.size() " + curStack.size());
                }
            }
            // Now, we handle the current execution callMessage 
            if (prevE == null) { // initial execution callMessage
                Message m = new Message(true, curE.tin, null, curE.componentName, curE);
                mSeq.add(m);
                curStack.push(m);
            } else if (prevE.ess < curE.ess) { // usual callMessage with sender and receiver
                Message m = new Message(true, curE.tin, prevE.componentName, curE.componentName, curE);
                mSeq.add(m);
                curStack.push(m);
            }
            if (!eSeqIt.hasNext()) { // empty stack completely, since no more executions
                while (!curStack.empty()) {
                    curE = curStack.pop().execution;
                    prevE = curStack.empty() ? null : curStack.peek().execution;
                    Message m = new Message(false, curE.tout, curE.componentName, prevE==null?null:prevE.componentName, curE);
                    mSeq.add(m);
                }
            }
            prevE = curE; // prepair next loop
        }
        return new MessageSequence(this.traceId, mSeq);
    }

    public SortedSet<KiekerExecutionRecord> getSequenceAsSortedSet() {
        return this.sequence;
    }

    public String toString() {
        StringBuilder strBuild = new StringBuilder("Trace " + this.traceId + ":\n");
        Iterator<KiekerExecutionRecord> it = sequence.iterator();
        while (it.hasNext()) {
            KiekerExecutionRecord e = it.next();
            strBuild.append("<");
            strBuild.append(e.toString());
            strBuild.append(">\n");
        }
        return strBuild.toString();
    }
}
