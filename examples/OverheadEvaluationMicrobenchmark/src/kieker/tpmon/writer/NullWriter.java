package kieker.tpmon.writer;

import java.util.Vector;
import kieker.common.record.IMonitoringRecord;
import kieker.tpmon.writer.util.async.AbstractWorkerThread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 * Monitoring writer which does nothing.
 *
 * @author Andre van Hoorn
 */
public class NullWriter implements IMonitoringLogWriter {

    private static final Log log = LogFactory.getLog(NullWriter.class);

    @Override
    public boolean newMonitoringRecord(IMonitoringRecord monitoringRecord) {
        // do nothing
        return true;
    }

    public boolean init(String initString) {
        log.info("NullWriter initializing with initString '"+initString+"'");
        return true;
    }

    public Vector<AbstractWorkerThread> getWorkers() {
        return new Vector<AbstractWorkerThread>();
    }

    public String getInfoString() {
        return new String("NullWriter");
    }
}
