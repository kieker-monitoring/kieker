package kieker.analysis.plugin;

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
 *
 */

import java.util.Collection;
import kieker.common.record.IMonitoringRecord;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;

/**
 *
 * @author matthias
 */
public class DummyRecordConsumer implements IMonitoringRecordConsumerPlugin {


    public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
        return null; // receive records of any type
    }

    public boolean newMonitoringRecord(IMonitoringRecord monitoringRecord) {
        System.out.println("DummyRecordConsumer consumed "+monitoringRecord);
        return true;
    }

    public boolean execute() {
        System.out.println("DummyRecordConsumer.execute()");
        return true;
    }

    public void terminate(final boolean error) {
        // nothing to do
    }

}
