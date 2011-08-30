/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
 ***************************************************************************/

package kieker.analysis.plugin;

import java.util.Collection;
import kieker.common.record.IMonitoringRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andre van Hoorn
 */
public class MonitoringRecordTypeLogger implements IMonitoringRecordConsumerPlugin {

    private static final Log log = LogFactory.getLog(MonitoringRecordTypeLogger.class);

    public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
        return null; // receive records of any type
    }

    public boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
        log.info("Consumed record:" + monitoringRecord.getClass().getName());
        log.info(monitoringRecord.toString());
        return true;
    }

    public boolean execute() {
        /* We consume synchronously */
        return true;
    }

    public void terminate(final boolean error) {
        /* We consume synchronously */
    }
}
