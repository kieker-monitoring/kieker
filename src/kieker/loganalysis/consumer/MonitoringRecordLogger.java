/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.loganalysis.consumer;

import java.util.Vector;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.consumer.MonitoringRecordLogger
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
public class MonitoringRecordLogger implements IMonitoringRecordConsumer {
    private static final Log log = LogFactory.getLog(MonitoringRecordLogger.class);

    public Vector<String> getRecordTypeSubscriptionList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void consumeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) {
        log.info("Consumed record:" + monitoringRecord.getRecordTypeId());
    }

    public void run(){
        /* We consume synchronously */
    }
}
