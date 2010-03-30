package kieker.common.record;

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
 *@author Andre van Hoorn
 */
public class OperationExecutionRecord extends AbstractMonitoringRecord implements Comparable<OperationExecutionRecord> {

    private static final long serialVersionUID = 117L;
    /** Used to identify the type of CSV records */
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
     * Returns an instance of KiekerExecutionRecord.
     * The member variables are initialized that way that only actually
     * used variables must be updated.
     * Do not set unused member variables to dummy values such as -1 etc.!
     */
    public OperationExecutionRecord() {
    }

    public OperationExecutionRecord(
            String componentName, String methodName,
            long traceId) {
        this.componentName = componentName;
        this.opname = methodName;
        this.traceId = traceId;
    }

    public OperationExecutionRecord(
            String componentName, String opName,
            long traceId,
            long tin, long tout) {
        this(componentName, opName, traceId);
        this.tin = tin;
        this.tout = tout;
    }

    public OperationExecutionRecord(
            String componentName, String opName,
            String sessionId, long traceId,
            long tin, long tout) {
        this(componentName, opName, traceId, tin, tout);
        this.sessionId = sessionId;
    }

    public OperationExecutionRecord(
            String componentName, String opName,
            String sessionId, long traceId,
            long tin, long tout,
            String vnName,
            int eoi, int ess) {
        this(componentName, opName, sessionId, traceId, tin, tout);
        this.vmName = vnName;
        this.eoi = eoi;
        this.ess = ess;
    }

    public final Object[] toArray() {
        return new Object[]{
                    this.experimentId,
                    this.componentName + "." + this.opname,
                    (this.sessionId == null) ? "NULL" : this.sessionId,
                    this.traceId,
                    this.tin,
                    this.tout,
                    (this.vmName == null) ? "NULLHOST" : this.vmName,
                    this.eoi,
                    this.ess
                };
    }

    public final void initFromArray(Object[] values)
            throws IllegalArgumentException {
        try {
            if (values.length != OperationExecutionRecord.numRecordFields) {
                throw new IllegalArgumentException("Expecting vector with "
                        + OperationExecutionRecord.numRecordFields + " elements but found:" + values.length);
            }

            this.experimentId = (Integer)values[0];
            { // divide name into component and operation name
                String name = (String)values[1];
                int posParen = name.lastIndexOf('(');
                int posDot;
                if (posParen != -1) {
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
            this.sessionId = (String)values[2];
            this.traceId = (Long)values[3];
            this.tin = (Long)values[4];
            this.tout = (Long)values[5];
            this.vmName = (String)values[6];
            this.eoi = (Integer)values[7];
            this.ess = (Integer)values[8];
        } catch (Exception exc) {
            throw new IllegalArgumentException("Failed to init", exc);
        }
        return;
    }

    /**
     * If the passed object has the same traceId, the comparison is performed
     * based on the eio and ess values. Otherwise the comparison is based on
     * the tin value.
     *
     * @param o
     * @return
     */
    public int compareTo(OperationExecutionRecord o) {
        if (this.traceId == o.traceId) {
            if (this.eoi < o.eoi) {
                return -1;
            }
            if (this.eoi > o.eoi) {
                return 1;
            }
            return 0;
        } else {
            if (this.tin < o.tin) {
                return -1;
            }
            if (this.tin > o.tin) {
                return 1;
            }
            return 0;
        }
    }
}
