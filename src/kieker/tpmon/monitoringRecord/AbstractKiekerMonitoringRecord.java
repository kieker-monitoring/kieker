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
import java.util.ArrayList;
import java.util.Vector;
import kieker.tpmon.annotation.TpmonInternal;

/**
 * @author Andre van Hoorn
 */
public abstract class AbstractKiekerMonitoringRecord implements Serializable {

    /**
     * Sets the values of this monitoring object's field according to
     * the csvRecord. Additionally, the internal recordTypeId must be
     * set accordingly.
     *
     * @param recordTypeId
     * @param recordVector
     */
    @TpmonInternal()
      public abstract void initFromStringVector(Vector<String> recordVector)
            throws IllegalArgumentException;

    @TpmonInternal()
    public abstract Vector<String> toStringVector();

    /**
     * Returns an id which can be used to identify CSV records of this
     * type. The returned value should be unique for the class extending
     * AbstractKiekerMonitoringRecord. 
     *
     * The recommended way is to use the hashCode() method of the class
     * (not that of the object) stored into a static variable.
     *
     * @return an ID unique for the monitoring record's type.
     */
    @TpmonInternal()
    public abstract int getRecordTypeId();
}
