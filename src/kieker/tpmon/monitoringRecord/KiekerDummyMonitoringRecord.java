package kieker.tpmon.monitoringRecord;

/**
 * kieker.tpmon.DummyMonitoringRecord
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
import kieker.tpmon.annotation.TpmonInternal;

/**
 * @author Andre van Hoorn
 */
public class KiekerDummyMonitoringRecord extends AbstractKiekerMonitoringRecord {

    private static final long serialVersionUID = 1133L;

   /** Used to identify the type of CSV records
    * This record type has a fixed value of 0
    */
    private static int typeId = 0;

    @TpmonInternal()
    public void initFromStringVector(Vector<String> recordVector)
            throws IllegalArgumentException {
        if(recordVector.size()>0) {
            throw new IllegalArgumentException("Expecting vector with "+
                    "0 elements but found:" + recordVector.size());
        }
        return;
    }

    @TpmonInternal()
    public Vector<String> toStringVector() {
        return new Vector<String>();
    }

    @TpmonInternal()
    public int getRecordTypeId() {
        return typeId;
    }

    @TpmonInternal()
    public static AbstractKiekerMonitoringRecord getInstance() {
        return new KiekerDummyMonitoringRecord();
    }
}