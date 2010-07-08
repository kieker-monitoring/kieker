package kieker.monitoring.writer;

import java.util.Vector;
import kieker.common.record.IMonitoringRecordReceiver;
import kieker.monitoring.writer.util.async.AbstractWorkerThread;

/*
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

/**
 * @author Andre van Hoorn
 */
public interface IMonitoringLogWriter extends IMonitoringRecordReceiver {
    /**
     * Initialize instance from passed initialization string which is typically
     * a list of separated parameter/values pairs.
     * The implementing class AbstractMonitoringLogWriter includes convenient
     * methods to extract configuration values from an initString.
     *
     * @param initString the initialization string
     * @return true iff the initialiation was successful
     */
    public boolean init(String initString);

    // TODO: do we need an invoke(), or similar, method?

    // TODO: try to remove the need for this method!
    /**
     *
     * Returns a vector of workers, or null if none.
     */
    public Vector<AbstractWorkerThread> getWorkers();

    /**
     * Returns a human-readable information string about the writer's
     * configuration and state.
     *
     * @return the information string.
     */
    public String getInfoString();
}
