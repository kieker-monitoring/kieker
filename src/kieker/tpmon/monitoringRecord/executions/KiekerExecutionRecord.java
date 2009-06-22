package kieker.tpmon.monitoringRecord.executions;

import java.util.Vector;
import kieker.tpmon.monitoringRecord.*;
import kieker.tpmon.annotation.TpmonInternal;
import kieker.tpmon.core.TpmonController;

/**
 * kieker.tpmon.KiekerExecutionRecord
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
 *
 * @author Andre van Hoorn
 */
public class KiekerExecutionRecord extends AbstractKiekerMonitoringRecord {

    private static final long serialVersionUID = 117L;
    /** Used to identify the type of CSV records */
    private static int typeId = TpmonController.getInstance().registerMonitoringRecordType(KiekerExecutionRecord.class);
    private static int numRecordFields = 9;
    public int experimentId = -1;
    public String vmName = null;
    public String componentName = null;
    public String opname = null;
    public String sessionId = null;
    public long traceId = -1;
    public long tin = -1;
    public long tout = -1;
    public int eoi = -1;
    public int ess = -1;
    public boolean isEntryPoint = false;
    public Object retVal = null;

    /**
     * Constructor private such that instances are only created by calling 
     * the static method getInstance().
     * The reason is that we might eventually introduce an object pool in 
     * order to avoid the frequent creation of KiekerExecutionRecord objects.
     */
    private KiekerExecutionRecord() {
    }

    /**
     * Returns an instance of KiekerExecutionRecord.
     * The member variables are initialized that way that only actually
     * used variables must be updated.
     * Do not set unused member variables to dummy values such as -1 etc.!
     */
    @TpmonInternal()
    public static AbstractKiekerMonitoringRecord getInstance() {
        return new KiekerExecutionRecord();
    }

    @TpmonInternal()
    public static KiekerExecutionRecord getInstance(
            String componentName, String methodName,
            long traceId) {
        KiekerExecutionRecord execData = (KiekerExecutionRecord)getInstance();
        execData.componentName = componentName;
        execData.opname = methodName;
        execData.traceId = traceId;
        return execData;
    }

    @TpmonInternal()
    public static KiekerExecutionRecord getInstance(
            String componentName, String opName,
            String sessionId, long traceId,
            long tin, long tout) {
        KiekerExecutionRecord execData = (KiekerExecutionRecord)getInstance();
        execData.componentName = componentName;
        execData.opname = opName;
        execData.sessionId = sessionId;
        execData.traceId = traceId;
        execData.tin = tin;
        execData.tout = tout;
        return execData;
    }

    @TpmonInternal()
    public static KiekerExecutionRecord getInstance(
            String componentName, String opName,
            long traceId,
            long tin, long tout) {
        KiekerExecutionRecord execData = (KiekerExecutionRecord)getInstance();
        execData.componentName = componentName;
        execData.opname = opName;
        execData.traceId = traceId;
        execData.tin = tin;
        execData.tout = tout;
        return execData;
    }

    @TpmonInternal()
    public static KiekerExecutionRecord getInstance(
            String componentName, String opName,
            String sessionId, long traceId,
            long tin, long tout,
            int eoi, int ess) {
        KiekerExecutionRecord execData = (KiekerExecutionRecord)getInstance();
        execData.componentName = componentName;
        execData.opname = opName;
        execData.sessionId = sessionId;
        execData.traceId = traceId;
        execData.tin = tin;
        execData.tout = tout;
        execData.eoi = eoi;
        execData.ess = ess;
        return execData;
    }

    /**
     * Returns a CSV record for the object.
     */
    @TpmonInternal()
    public Vector<String> toStringVector() {
        Vector<String> vec = new Vector<String>(numRecordFields);
        vec.insertElementAt(Integer.toString(this.experimentId), 0);
        vec.insertElementAt(this.componentName + "." + this.opname, 1);
        vec.insertElementAt((this.sessionId == null) ? "NULL" : this.sessionId, 2);
        vec.insertElementAt(Long.toString(this.traceId), 3);
        vec.insertElementAt(Long.toString(this.tin), 4);
        vec.insertElementAt(Long.toString(this.tout), 5);
        vec.insertElementAt(this.vmName, 6);
        vec.insertElementAt(Integer.toString(this.eoi), 7);
        vec.insertElementAt(Integer.toString(this.eoi), 8);
        return vec;

    // TODO: remove this old implementation:
//        StringBuilder strB = new StringBuilder();
//        strB.append(this.experimentId); strB.append(';');
//        // concatenate opname
//        strB.append(this.componentName);
//        strB.append('.');
//        strB.append(this.opname);
//        strB.append(';');
//        // end concat opname
//        strB.append(this.sessionId); strB.append(';');
//        strB.append(this.traceId); strB.append(';');
//        strB.append(this.tin); strB.append(';');
//        strB.append(this.tout); strB.append(';');
//        strB.append(this.vmName); strB.append(';');
//        strB.append(this.eoi); strB.append(';');
//        strB.append(this.ess);
//        return strB.toString();
    }

    @TpmonInternal()
    public void initFromStringVector(Vector<String> recordVector)
            throws IllegalArgumentException {
        if (recordVector.size() > numRecordFields) {
            throw new IllegalArgumentException("Expecting vector with " +
                    numRecordFields + " elements but found:" + recordVector.size());
        }

        this.experimentId = Integer.parseInt(recordVector.elementAt(0));
        { // divide name into component and operation name
            String name = recordVector.elementAt(1);
            int pos = name.lastIndexOf('.');
            if (pos == -1) {
                componentName = "";
                this.opname = name;
            } else {
                componentName = name.substring(0, pos);
                this.opname = name.substring(pos + 1);
            }
        }
        this.sessionId = recordVector.elementAt(2);
        this.traceId = (Long.parseLong(recordVector.elementAt(3)));
        this.tin = Long.parseLong(recordVector.elementAt(4));
        this.tout = Long.parseLong(recordVector.elementAt(5));
        this.vmName = recordVector.elementAt(6);
        this.eoi = Integer.parseInt(recordVector.elementAt(7));
        this.ess = Integer.parseInt(recordVector.elementAt(8));

        return;
    }

    @TpmonInternal()
    public int getRecordTypeId() {
        return typeId;
    }
}
