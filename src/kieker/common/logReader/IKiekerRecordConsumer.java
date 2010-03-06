package kieker.common.logReader;

import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

/**
 * kieker.loganalysis.IKiekerRecordConsumer
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
public interface IKiekerRecordConsumer {

    /**
     * Simply return null to get records of all types.
     * @return
     */
    public String[] getRecordTypeSubscriptionList();

    public void consumeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) throws RecordConsumerExecutionException;

    /**
     * TODO: Add documentation! What means true? Whats the semantic of execute?
     * Has it to be called before or after consumeMonitoringRecord? Just once or multiple times?
     * @return
     */
    public boolean execute() throws RecordConsumerExecutionException;

    public void terminate();
}
