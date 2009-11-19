package mySimpleKiekerExample.monitoringRecord;

import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

/**
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
public class MyRTMonitoringRecord extends AbstractKiekerMonitoringRecord {

    private static final long serialVersionUID = 1775L;
    /** Used to identify the type of CSV records
     * This record type has a fixed value of 0
     */
    private static int typeId = AbstractKiekerMonitoringRecord.registerMonitoringRecordType(MyRTMonitoringRecord.class);
    private static int numRecordFields = 3;
    public long rt = -1;
    public String component = null;
    public String service = null;

    public void initFromStringArray(String[] recordVector)
            throws IllegalArgumentException {
        // String[]
        if (recordVector.length > MyRTMonitoringRecord.numRecordFields) {
            throw new IllegalArgumentException("Expecting vector with " +
                    MyRTMonitoringRecord.numRecordFields + " elements but found:" + recordVector.length);
        }
        this.component = recordVector[0];
        this.service = recordVector[1];
        this.rt = Long.parseLong(recordVector[2]);
        return;
    }

    public String[] toStringArray() {
        // String[] = {....}
        String[] vec = {
            (this.component == null) ? "NULLCOMPONENT" : this.component,
            (this.service == null) ? "NULLSERVICE" : this.service,
            Long.toString(rt),};
        return vec;
    }

    public int getRecordTypeId() {
        return typeId;
    }

    public static AbstractKiekerMonitoringRecord getInstance() {
        return new MyRTMonitoringRecord();
    }
}
