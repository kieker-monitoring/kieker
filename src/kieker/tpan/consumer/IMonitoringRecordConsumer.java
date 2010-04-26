package kieker.tpan.consumer;

import kieker.common.record.IMonitoringRecord;

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
public interface IMonitoringRecordConsumer {

    /**
     * Simply return null to get records of all types.
     * 
     * @return
     */
    public Class<? extends IMonitoringRecord>[] getRecordTypeSubscriptionList();

    /**
     * Called for each new record.
     *
     * @param monitoringRecord
     * @throws MonitoringRecordConsumerExecutionException
     */
    public void consumeMonitoringRecord(IMonitoringRecord monitoringRecord) throws MonitoringRecordConsumerExecutionException;

    /**
     * Starts a record consumer.
     * This method is called once when a TpanInstance's run() method is called.
     * This implementation must not be blocking!
     * Asynchronous consumers would spawn (an) aynchronous thread(s) in this
     * method.
     *
     * @return true on success; false otherwise.
     */
    public boolean execute() throws MonitoringRecordConsumerExecutionException;

    /**
     * Initiates a termination of the consumer. The value of the parameter
     * error indicates whether an error occured.
     *
     * @param error true iff an error occured.
     */
    public void terminate(boolean error);
}
