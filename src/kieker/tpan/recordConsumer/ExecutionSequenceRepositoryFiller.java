package kieker.tpan.recordConsumer;

import java.util.TreeSet;
import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.tpan.datamodel.ExecutionTraceRepository;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
 * @author Andre van Hoorn
 */
public class ExecutionSequenceRepositoryFiller implements IKiekerRecordConsumer {

    private static final Log log = LogFactory.getLog(ExecutionSequenceRepositoryFiller.class);
    private final ExecutionTraceRepository repo = new ExecutionTraceRepository();
    private TreeSet<Long> selectedTraces = null;
    /** Consuming only execution records */
    private final static String[] recordTypeSubscriptionList = {
        KiekerExecutionRecord.class.getName()
    };

    public ExecutionSequenceRepositoryFiller() {
    }

    /** Considers only the traces with ID given in selectedTraces.
     *  If selectedTraces is null all traces are considered */
    public ExecutionSequenceRepositoryFiller(TreeSet<Long> selectedTraces) {
        this.selectedTraces = selectedTraces;
    }

    public String[] getRecordTypeSubscriptionList() {
        return recordTypeSubscriptionList;
    }

    public void consumeMonitoringRecord(final AbstractKiekerMonitoringRecord monitoringRecord) {
        if (monitoringRecord instanceof KiekerExecutionRecord) {
            KiekerExecutionRecord execRecord = (KiekerExecutionRecord) monitoringRecord;
            if (this.selectedTraces == null || this.selectedTraces.contains(execRecord.sessionId)) {
                this.repo.addExecution((KiekerExecutionRecord) monitoringRecord);
            }
        } else {
            log.error("Can only consume records of type KiekerExecutionRecord" +
                    " but passed record is of type " + monitoringRecord.getClass().getName());
        }
    }

    public boolean execute() {
        /* We consume synchronously */
        return true;
    }

    public ExecutionTraceRepository getExecutionSequenceRepository() {
        return this.repo;
    }

    @Override
    public void terminate() {
        /* We consume synchronously */
    }
}
