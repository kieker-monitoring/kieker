package kieker.tpmon.writer.core;

import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import kieker.tpmon.*;
import java.util.Vector;
import kieker.tpmon.annotation.TpmonInternal;

/**
 * kieker.tpmon.IMonitoringDataWriter
 * 
 * ==================LICENCE=========================
 * Copyright 2006-2008 Matthias Rohr and the Kieker Project 
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
public interface IMonitoringDataWriter {

    @TpmonInternal()
    public boolean insertMonitoringDataNow(AbstractKiekerMonitoringRecord execData);

    @TpmonInternal()
    public boolean init(String initString);

    /**
     * Returns a vector of workers, or null if none.
     */
    @TpmonInternal()
    public Vector<AbstractWorkerThread> getWorkers();

    @TpmonInternal()
    public void setDebug(boolean debug);

    @TpmonInternal()
    public boolean isDebug();

    @TpmonInternal()
    public String getInfoString();
}
