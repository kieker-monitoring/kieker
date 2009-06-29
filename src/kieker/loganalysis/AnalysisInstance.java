package kieker.loganalysis;

/**
 * kieker.loganalysis.AnalysisInstance
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
 */
import java.util.Vector;
import kieker.common.logReader.IMonitoringRecordConsumer;
import kieker.common.logReader.ILogReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class AnalysisInstance {

    private static final Log log = LogFactory.getLog(AnalysisInstance.class);
    private Vector<ILogReader> logReaders = new Vector<ILogReader>();
    private Vector<IMonitoringRecordConsumer> consumers = new Vector<IMonitoringRecordConsumer>();

    public void run() {
        for (ILogReader r : this.logReaders) {
            for (IMonitoringRecordConsumer c : this.consumers) {
                r.registerConsumer(c);
                c.run();
            }
        }
        for (ILogReader r : this.logReaders) {
            r.run();
        }
    }

    public void addLogReader(ILogReader reader) {
        this.logReaders.add(reader);
    }

    public void addConsumer(IMonitoringRecordConsumer consumer) {
        this.consumers.add(consumer);
    }
}
