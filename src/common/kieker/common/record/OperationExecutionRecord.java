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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * None of the String variables must be null.
 *
 * @author Andre van Hoorn
 */
public class OperationExecutionRecord extends AbstractMonitoringRecord {

   private static final Log log = LogFactory.getLog(OperationExecutionRecord.class);

    private static final String DEFAULT_VALUE="N/A";

    private static final long serialVersionUID = 1179L;
    /** Used to identify the type of CSV records */
    private static final int numRecordFields = 9;
    public int experimentId = -1;
    public String hostName = DEFAULT_VALUE;
    public String className = DEFAULT_VALUE;
    public String operationName = DEFAULT_VALUE;
    public String sessionId = DEFAULT_VALUE;
    public long traceId = -1;
    public long tin = -1;
    public long tout = -1;
    public int eoi = -1;
    public int ess = -1;
    public boolean isEntryPoint = false;
    public Object retVal = null;

    /**
     * Returns an instance of OperationExecutionRecord.
     * The member variables are initialized that way that only actually
     * used variables must be updated.
     */
    public OperationExecutionRecord() {
    }

    public OperationExecutionRecord(
            String componentName, String methodName,
            long traceId) {
        this.className = componentName;
        this.operationName = methodName;
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
            long tin, long tout) {
        this(componentName, opName, -1, tin, tout);
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
        this.hostName = vnName;
        this.eoi = eoi;
        this.ess = ess;
    }

    public final Object[] toArray() {
        return new Object[]{
                    this.experimentId,
                    this.className + "." + this.operationName,
                    (this.sessionId == null) ? "NULL" : this.sessionId,
                    this.traceId,
                    this.tin,
                    this.tout,
                    (this.hostName == null) ? "NULLHOST" : this.hostName,
                    this.eoi,
                    this.ess
                };
    }

    public Class[] getValueTypes() {
        return new Class[] {
                    int.class,    // experimentId
                    String.class, // component + op
                    String.class, // sessionId
                    long.class,   // traceId
                    long.class,   // tin
                    long.class,   // tout
                    String.class, // hostName
                    int.class,    // eoi
                    int.class     // ess
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
                    className = "";
                    this.operationName = name;
                } else {
                    className = name.substring(0, posDot);
                    this.operationName = name.substring(posDot + 1);
                }
            }
            this.sessionId = (String)values[2];
            this.traceId = (Long)values[3];
            this.tin = (Long)values[4];
            this.tout = (Long)values[5];
            this.hostName = (String)values[6];
            this.eoi = (Integer)values[7];
            this.ess = (Integer)values[8];
        } catch (Exception exc) {
            throw new IllegalArgumentException("Failed to init", exc);
        }
        return;
    }

    /**
     * Compares two records. 
     * 
     * If one of the records contains null values for its variables,
     * false is returned.
     *
     * @param o
     * @return true iff the compared records are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (! (o instanceof OperationExecutionRecord)){
            return false;
        }
        
        OperationExecutionRecord ro = (OperationExecutionRecord) o;

        try {
        return
                this.className.equals(ro.className) &&
                this.eoi == ro.eoi &&
                this.ess == ro.ess &&
                //this.experimentId == ro.experimentId &&
                this.operationName.equals(ro.operationName) &&
                this.sessionId.equals(ro.sessionId) &&
                this.tin == ro.tin &&
                this.tout == ro.tout &&
                this.traceId == ro.traceId &&
                this.hostName.equals(ro.hostName);
        } catch (NullPointerException ex){
            log.error(ex);
            return false;
        }
    }
}
