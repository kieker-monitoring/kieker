package kieker.loganalysis.recordConsumer;

import java.util.Vector;
import kieker.loganalysis.datamodel.ExecutionSequenceRepository;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.loganalysis.consumer.ExecutionSequenceRepositoryFiller
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
public class ExecutionSequenceRepositoryFiller implements IMonitoringRecordConsumer {
    private static final Log log = LogFactory.getLog(ExecutionSequenceRepositoryFiller.class);

    private ExecutionSequenceRepository repo = new ExecutionSequenceRepository();
    
    public Vector<String> getRecordTypeSubscriptionList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void consumeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) {
        if(monitoringRecord instanceof KiekerExecutionRecord){
            this.repo.addExecution((KiekerExecutionRecord)monitoringRecord);   
        }else{
            log.error("Can only consume records of type KiekerExecutionRecord"+
                    " but passed record is of type " + monitoringRecord.getClass().getName());
        }
    }

    public void run(){
        /* We consume synchronously */
    }
    
    public ExecutionSequenceRepository getExecutionSequenceRepository(){
        return this.repo;
    }
}
