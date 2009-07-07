package kieker.loganalysis.datamodel;

import java.util.Hashtable;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;

/**
 *kieker.loganalysis.datamodel.ExecutionSequenceRepository
 *
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
 * 
 * @author Andre van Hoorn
 */
public class ExecutionTraceRepository {

    public Hashtable<Long, ExecutionTrace> repository = new Hashtable<Long, ExecutionTrace>();

    /**
     * Adds an execution to the repository.
     * 
     * @param execRecord
     */
    public void addExecution(KiekerExecutionRecord execRecord) {
        long traceId = execRecord.traceId;
        ExecutionTrace seq = repository.get(traceId);
        if (seq == null) { // create and add new sequence
            seq = new ExecutionTrace(traceId);
            repository.put(traceId, seq);
        }
        seq.add(execRecord);
    }
    
    public Hashtable<Long, ExecutionTrace> getRepositoryAsHashTable(){
        return this.repository;
    }
}
