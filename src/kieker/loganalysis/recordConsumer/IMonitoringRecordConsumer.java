package kieker.loganalysis.recordConsumer;

import java.util.Vector;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

/**
 * kieker.loganalysis.IMonitoringRecordConsumer
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
public interface IMonitoringRecordConsumer {
    public Vector<String> getRecordTypeSubscriptionList();

    public void consumeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord);

    public void run();
}
