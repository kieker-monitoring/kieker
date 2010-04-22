package kieker.common.record;

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
public interface IMonitoringRecordReceiver {
    /**
     * Called for each new record.
     * 
     * Notice, that this method should not throw an exception,
     * but indicate an error by the return value false.
     *
     * @param monitoringRecord the record.
     * @return true on success; false in case of an error.
     */
    public boolean newMonitoringRecord(IMonitoringRecord monitoringRecord);
}
