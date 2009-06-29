package kieker.tpmon.monitoringRecord;

/**
 * kieker.tpmon.AbstractKiekerMonitoringRecord
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

import java.io.Serializable;
import kieker.tpmon.annotation.TpmonInternal;

/**
 * @author Andre van Hoorn
 */
public abstract class AbstractKiekerMonitoringRecord implements Serializable {

    private long loggingTimestamp = -1;

    /**
     * Sets the values of this monitoring object's field according to
     * the csvRecord. Additionally, the internal recordTypeId must be
     * set accordingly.
     *
     * @param recordTypeId
     * @param recordVector
     */
    @TpmonInternal()
      public abstract void initFromStringVector(String[] recordVector)
            throws IllegalArgumentException;

    @TpmonInternal()
    public static AbstractKiekerMonitoringRecord getInstance(){
        throw new UnsupportedOperationException("Extending classes must override"+
                "this method");
    }

    @TpmonInternal()
    public abstract String[] toStringVector();

    @TpmonInternal()
    public abstract int getRecordTypeId();

    @TpmonInternal()
    public final long getLoggingTimestamp(){
        return this.loggingTimestamp;
    }
    
    @TpmonInternal()
    public final void setLoggingTimestamp(long loggingTimestamp){
        this.loggingTimestamp = loggingTimestamp;
    }

}
