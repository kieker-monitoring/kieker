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

    public MessageSequence toMessageSequence() {
        Vector<Message> mSeq = new Vector<Message>();
        Stack<Message> curStack = new Stack<Message>();
        Iterator<KiekerExecutionRecord> eSeqIt = this.sequence.iterator();
        KiekerExecutionRecord curE = null, prevE = null;
        while (eSeqIt.hasNext()) {
            curE = eSeqIt.next();
            if (prevE == null) { // initial execution call
                Message m = new Message(true, curE.tin, null, curE.componentName, curE);
                mSeq.add(m);
                curStack.push(m);
            } else {
                if (prevE.ess < curE.ess) { // we have a subsequent call
                    log.info("prevE.ess < curE.ess");
                    Message m = new Message(true, curE.tin, curE.componentName, prevE.componentName, curE);
                    mSeq.add(m);
                    curStack.push(m);
                } else if (prevE.ess == curE.ess) { // return + call
                    Message mStack = curStack.pop();
                    log.info("prevE.ess == curE.ess");
                    Message m = new Message(false, mStack.execution.tout, prevE.componentName, mStack.execution.componentName, mStack.execution);
                    mSeq.add(m);
                    m = new Message(true, curE.tin, mStack.execution.componentName, curE.componentName, curE);
                    mSeq.add(m);
                    curStack.push(m);
                } else { // prevE.ess > curE.ess: returns + call
                    log.info("else");
                    // first, we need to clean up the stack
                    KiekerExecutionRecord curReturnCaller = curStack.pop().execution;
                    KiekerExecutionRecord curReturnCallee = curStack.peek().execution;
                    while (curReturnCaller.ess > curE.ess) {
                        Message m = new Message(false, curReturnCaller.tout, curReturnCaller.componentName, curReturnCallee.componentName, curReturnCaller);
                        mSeq.add(m);
                        curReturnCaller = curReturnCallee;
                        curReturnCaller = curStack.pop().execution;
                    }
                    // second, the actual call resulting from the exeuction
                    Message mStack = curStack.pop();
                    Message m = new Message(false, mStack.execution.tout, prevE.componentName, mStack.execution.componentName, mStack.execution);
                    mSeq.add(m);
                    m = new Message(true, curE.tin, mStack.execution.componentName, curE.componentName, curE);
                    mSeq.add(m);
                    curStack.push(m);
                }
            }
            if (!eSeqIt.hasNext()) { // we possibly need to clean up the stack
                log.info("!eSeqIt.hasNext()");
                Message curReturnCaller;
                Message curReturnCallee;
                while (!curStack.empty()) {
                    curReturnCaller = curStack.pop();
                    curReturnCallee = curStack.empty()?null:curStack.peek();
                    Message m = new Message(false, curReturnCaller.execution.tout, curReturnCaller.execution.componentName, (curReturnCallee==null)?null:curReturnCallee.execution.componentName, curReturnCaller.execution);
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
        StringBuilder strBuild = new StringBuilder("Trace " + this.traceId + ":");
        Iterator<KiekerExecutionRecord> it = sequence.iterator();
        while (it.hasNext()) {
            KiekerExecutionRecord e = it.next();
            strBuild.append("<");
            strBuild.append(e.toString());
            strBuild.append(">");
        }
        return strBuild.toString();
    }
}
