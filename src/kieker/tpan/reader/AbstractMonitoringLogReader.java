package kieker.tpan.reader;

/*
 *
 * ==================LICENCE=========================
 * Copyright 2010 Kieker Project
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

import java.util.Vector;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractMonitoringLogReader implements IMonitoringLogReader {

    private static final Log log = LogFactory.getLog(AbstractMonitoringLogReader.class);

    private final Vector<IMonitoringRecordReceiver> recordReceivers = new Vector<IMonitoringRecordReceiver>();

    public final void addRecordReceiver(final IMonitoringRecordReceiver consumer) {
        this.recordReceivers.add(consumer);
    }

    /**
     * Delivers the given record to the consumers that are registered for this
     * type of records.
     *
     * This method should be used by implementing classes.
     *
     * @param monitoringRecord the record
     * @throws LogReaderExecutionException if an error occurs
     */
    protected final void deliverRecordToConsumers(final IMonitoringRecord monitoringRecord) throws LogReaderExecutionException {
        try {
            for (IMonitoringRecordReceiver c : this.recordReceivers) {
                c.newMonitoringRecord(monitoringRecord);
            }
        } catch (Exception ex) {
            log.fatal("Caught Exception while delivering record", ex);
            throw new LogReaderExecutionException("Caught Exception while delivering record", ex);
        }
    }
}
