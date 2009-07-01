package kieker.tpmon.monitoringRecord.executions;

import kieker.tpmon.monitoringRecord.*;
import kieker.tpmon.annotation.TpmonInternal;
import kieker.tpmon.core.TpmonController;

/**
 * kieker.tpmon.monitoringRecord.KiekerExecutionRecord
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
public class KiekerExecutionRecord extends AbstractKiekerMonitoringRecord implements Comparable<KiekerExecutionRecord> {

    private static final long serialVersionUID = 117L;
    /** Used to identify the type of CSV records */
    private static final int typeId = TpmonController.getInstance().registerMonitoringRecordType(KiekerExecutionRecord.class);
    private static final int numRecordFields = 9;
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
    public static final AbstractKiekerMonitoringRecord getInstance() {
        return new KiekerExecutionRecord();
    }

    @TpmonInternal()
    public static final KiekerExecutionRecord getInstance(
            String componentName, String methodName,
            long traceId) {
        KiekerExecutionRecord execData = (KiekerExecutionRecord)getInstance();
        execData.componentName = componentName;
        execData.opname = methodName;
        execData.traceId = traceId;
        return execData;
    }

    @TpmonInternal()
    public static final KiekerExecutionRecord getInstance(
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
    public static final KiekerExecutionRecord getInstance(
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
    public static final KiekerExecutionRecord getInstance(
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
    public final String[] toStringVector() {
        String[] vec = {
            Integer.toString(this.experimentId),
            this.componentName + "." + this.opname,
            (this.sessionId == null) ? "NULL" : this.sessionId,
            Long.toString(this.traceId),
            Long.toString(this.tin),
            Long.toString(this.tout),
            this.vmName,
            Integer.toString(this.eoi),
            Integer.toString(this.ess)
        };
        return vec;
    }

    @TpmonInternal()
    public final void initFromStringVector(String[] recordVector)
            throws IllegalArgumentException {
        if (recordVector.length > KiekerExecutionRecord.numRecordFields) {
            throw new IllegalArgumentException("Expecting vector with " +
                    KiekerExecutionRecord.numRecordFields + " elements but found:" + recordVector.length);
        }

        this.experimentId = Integer.parseInt(recordVector[0]);
        { // divide name into component and operation name
            String name = recordVector[1];
            int posParen = name.lastIndexOf('(');
            int posDot;
            if ( posParen != -1) {
            posDot = name.substring(0, posParen).lastIndexOf('.');
            } else {
                posDot = name.lastIndexOf('.');
            }
            if (posDot == -1) {
                componentName = "";
                this.opname = name;
            } else {
                componentName = name.substring(0, posDot);
                this.opname = name.substring(posDot + 1);
            }
        }
        this.sessionId = recordVector[2];
        this.traceId = (Long.parseLong(recordVector[3]));
        this.tin = Long.parseLong(recordVector[4]);
        this.tout = Long.parseLong(recordVector[5]);
        this.vmName = recordVector[6];
        this.eoi = Integer.parseInt(recordVector[7]);
        this.ess = Integer.parseInt(recordVector[8]);

        return;
    }

    @TpmonInternal()
    public int getRecordTypeId() {
        return typeId;
    }

    /**
     * If the passed object has the same traceId, the comparison is performed
     * based on the eio and ess values. Otherwise the comparison is based on
     * the tin value.
     *
     * @param o
     * @return
     */
    @TpmonInternal()
    public int compareTo(KiekerExecutionRecord o) {
        if (this.traceId == o.traceId){
            if(this.eoi<o.eoi) return -1;
            if(this.eoi>o.eoi) return 1;
            return 0;
        }else{
            if(this.tin<o.tin) return -1;
            if(this.tin>o.tin) return 1;
            return 0;
        }
    }
    
    @TpmonInternal()
    public String toString(){
        StringBuilder strBuild = new StringBuilder();
        String[] valueVec = this.toStringVector();
        for (String v : valueVec){
            strBuild.append(v);
            strBuild.append(' ');
        }
        return strBuild.toString();
    }
}
