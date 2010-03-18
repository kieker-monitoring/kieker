package kieker.common.monitoringRecord;

/**
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
public class BranchingRecord extends AbstractMonitoringRecord {

    private static final long serialVersionUID = 1113L;
    private static int numRecordFields = 3;
    private long timestamp = -1;
    private int branchID = -1;
    private int branchingOutcome = -1;

    public BranchingRecord (long timestamp, int branchID, int branchingOutcome) {
        this.timestamp = timestamp;
        this.branchID = branchID;
        this.branchingOutcome = branchingOutcome;
    }

    public void initFromArray(Object[] values) throws IllegalArgumentException {
        try {
            if (values.length != BranchingRecord.numRecordFields) {
                throw new IllegalArgumentException("Expecting vector with "
                        + BranchingRecord.numRecordFields + " elements but found:" + values.length);
            }
            this.timestamp = (Long) values[0];
            this.branchID = (Integer) values[1];
            this.branchingOutcome = (Integer) values[2];
        } catch (Exception exc) {
            throw new IllegalArgumentException("Failed to init", exc);
        }
        return;
    }

    public Object[] toArray() {
        return new Object[] {
            timestamp,
            branchID,
            branchingOutcome
        };
    }
}
