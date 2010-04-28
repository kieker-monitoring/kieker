package mySimpleKiekerJMSExample.record;

import kieker.common.record.AbstractMonitoringRecord;

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
 * @author Andre van Hoorn
 */
public class MyRTMonitoringRecord extends AbstractMonitoringRecord {

    private static final long serialVersionUID = 1799875L;

    private static int numRecordFields = 3;

    public String component = null;
    public String service = null;
    public long rt = -1;

    public final void initFromArray(Object[] values)
            throws IllegalArgumentException {
        try {
            if (values.length != MyRTMonitoringRecord.numRecordFields) {
                throw new IllegalArgumentException("Expecting vector with "
                        + MyRTMonitoringRecord.numRecordFields + " elements but found:" + values.length);
            }

            this.component = (String) values[0];
            this.service = (String) values[1];
            this.rt = (Long) values[2];

        } catch (Exception exc) {
            throw new IllegalArgumentException("Failed to init", exc);
        }

        return;
    }

    public final Object[] toArray() {
        return new Object[]{
                    (this.component == null) ? "NULLCOMPONENT" : this.component,
                    (this.service == null) ? "NULLSERVICE" : this.service,
                    this.rt};
    }

    public Class[] getValueTypes() {
        return new Class[]{
            String.class, // component
            String.class, // service
            long.class    // rt
        };
    }
}
