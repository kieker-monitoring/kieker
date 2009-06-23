package kieker.tpmon.monitoringRecord;

/**
 * kieker.tpmon.KiekerBranchingRecord
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
import kieker.tpmon.core.TpmonController;

/**
 * @author Andre van Hoorn
 */
public class KiekerBranchingRecord extends AbstractKiekerMonitoringRecord {

    private static final long serialVersionUID = 1113L;

   /** Used to identify the type of CSV records
    * This record type has a fixed value of 0
    */
    private static int typeId = TpmonController.getInstance().registerMonitoringRecordType(KiekerBranchingRecord.class);
    private static int numRecordFields = 3;
    private long timestamp = -1;
    private int branchID = -1;
    private int branchingOutcome = -1;
    
    @TpmonInternal()
    public void initFromStringVector(Vector<String> recordVector)
            throws IllegalArgumentException {
        if(recordVector.size() > KiekerBranchingRecord.numRecordFields) {
            throw new IllegalArgumentException("Expecting vector with "+
                    KiekerBranchingRecord.numRecordFields + " elements but found:" + recordVector.size());
        }
        this.timestamp = Long.parseLong(recordVector.elementAt(0));
        this.branchID = Integer.parseInt(recordVector.elementAt(1));
        this.branchingOutcome = Integer.parseInt(recordVector.elementAt(2));
        return;
    }

    @TpmonInternal()
    public Vector<String> toStringVector() {
        Vector<String> vec = new Vector<String>(3);
        vec.insertElementAt(Long.toString(timestamp), 0);
        vec.insertElementAt(Integer.toString(branchID), 1);
        vec.insertElementAt(Integer.toString(branchingOutcome), 2);
        return vec;
    }

    @TpmonInternal()
    public int getRecordTypeId() {
        return typeId;
    }

    @TpmonInternal()
    public static AbstractKiekerMonitoringRecord getInstance() {
        return new KiekerBranchingRecord();
    }

    @TpmonInternal()
    public static KiekerBranchingRecord getInstance(long timestamp, int branchID, int branchingOutcome){
        KiekerBranchingRecord rec = (KiekerBranchingRecord)KiekerBranchingRecord.getInstance();
        rec.timestamp = timestamp;
        rec.branchID = branchID;
        rec.branchingOutcome = branchingOutcome;
        return rec;
    }
}