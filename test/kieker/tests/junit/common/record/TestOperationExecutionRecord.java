package kieker.tests.junit.common.record;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
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

import junit.framework.TestCase;
import kieker.common.record.OperationExecutionRecord;

/**
 *@author Andre van Hoorn
 */
public class TestOperationExecutionRecord extends TestCase {
    
    /**
     * Tests the toArray(..) and initFromArray(..) methods of
     * OperationExecutionRecord.
     *
     * Assert that a record instance r1 equals an
     * instance r2 created by serializing r1 to an
     * array a1 and using a1 to init r2.
     *
     */
    public void testSerializeDeserializeEquals(){
        OperationExecutionRecord r1 = new OperationExecutionRecord();
        r1.componentName = "p1.p2.p3.componentname";
        r1.eoi=1;
        r1.ess=2;
        r1.experimentId=55;
        r1.isEntryPoint=true;
        r1.opname="operation(boolean arg1, int arg2)";
        r1.retVal=new Object();
        r1.sessionId="XXLJHDJHDHF";
        r1.tin=5577376;
        r1.tout=7544522;
        r1.traceId=882287444;
        r1.vmName="myVM";

        Object[] r1Array = r1.toArray();

        OperationExecutionRecord r2 = new OperationExecutionRecord();
        r2.initFromArray(r1Array);

        assertEquals(r1, r2);
    }

    /**
     * Tests the equals(..) method of OperationExecutionRecord.
     *
     * Assert that two record instances with equal variables values
     * are equal.
     */
    public void testEqualsEqualVariablesValues(){
        OperationExecutionRecord r1 = new OperationExecutionRecord();
        r1.componentName = "p1.p2.p3.componentname";
        r1.eoi=1;
        r1.ess=2;
        r1.experimentId=55;
        r1.isEntryPoint=true;
        r1.opname="operation(boolean arg1, int arg2)";
        r1.retVal=new Object();
        r1.sessionId="XXLJHDJHDHF";
        r1.tin=5577376;
        r1.tout=7544522;
        r1.traceId=882287444;
        r1.vmName="myVM";

        OperationExecutionRecord r2 = new OperationExecutionRecord();
        r2.componentName = "p1.p2.p3.componentname";
        r2.eoi=1;
        r2.ess=2;
        r2.experimentId=55;
        r2.isEntryPoint=true;
        r2.opname="operation(boolean arg1, int arg2)";
        r2.retVal=new Object();
        r2.sessionId="XXLJHDJHDHF";
        r2.tin=5577376;
        r2.tout=7544522;
        r2.traceId=882287444;
        r2.vmName="myVM";

        assertEquals(r1, r2);
    }

    /**
     * Tests the equals(..) method of OperationExecutionRecord.
     *
     * Assert that two record instances with null variables values
     * are not equal.
     */
    public void testEqualsNullVariableValues(){
        OperationExecutionRecord r1 = new OperationExecutionRecord();
        r1.componentName = "p1.p2.p3.componentname";
        r1.eoi=1;
        r1.ess=2;
        r1.experimentId=55;
        r1.isEntryPoint=true;
        r1.opname="operation(boolean arg1, int arg2)";
        r1.retVal=new Object();
        r1.sessionId="XXLJHDJHDHF";
        r1.tin=5577376;
        r1.tout=7544522;
        r1.traceId=882287444;
        r1.vmName="myVM";

        OperationExecutionRecord r2 = new OperationExecutionRecord();
        r2.componentName = "p1.p2.p3.componentname";
        r2.eoi=1;
        r2.ess=2;
        r2.experimentId=55;
        r2.isEntryPoint=true;
        r2.opname="operation(boolean arg1, int arg2)";
        r2.retVal=new Object();
        r2.sessionId="XXLJHDJHDHF";
        r2.tin=5577376;
        r2.tout=7544522;
        r2.traceId=882287444;
        r2.vmName="myVM";

        assertEquals(r1, r2);

        /* Modification of componentName */
        String oldComponentName1 = r1.componentName;
        String oldComponentName2 = r2.componentName;
        r1.componentName = null;
        r2.componentName = null;
        assertFalse(r1.equals(r2));
        r1.componentName = oldComponentName1;
        r2.componentName = oldComponentName2;
        assertEquals(r1, r2);

        /* Modification of opname */
        String oldOpname1 = r1.opname;
        String oldOpname2 = r2.opname;
        r1.opname = null;
        r2.opname = null;
        assertFalse(r1.equals(r2));
        r1.opname = oldOpname1;
        r2.opname = oldOpname2;
        assertEquals(r1, r2);

        /* Modification of sessionId */
        String oldSessionId1 = r1.sessionId;
        String oldSessionId2 = r2.sessionId;
        r1.sessionId = null;
        r2.sessionId = null;
        assertFalse(r1.equals(r2));
        r1.sessionId = oldSessionId1;
        r2.sessionId = oldSessionId2;
        assertEquals(r1, r2);

        /* Modification of vmName */
        String oldVmName1 = r1.vmName;
        String oldVmName2 = r2.vmName;
        r1.vmName = null;
        r2.vmName = null;
        assertFalse(r1.equals(r2));
        r1.vmName = oldVmName1;
        r2.vmName = oldVmName2;
        assertEquals(r1, r2);
    }

    /**
     * Tests the equals(..) method of OperationExecutionRecord.
     *
     * Assert that two record instances with differing variables values
     * are not equal.
     */
    public void testEqualsDifferentVariablesValues(){
        OperationExecutionRecord r1 = new OperationExecutionRecord();
        r1.componentName = "p1.p2.p3.componentname";
        r1.eoi=1;
        r1.ess=2;
        r1.experimentId=55;
        r1.isEntryPoint=true;
        r1.opname="operation(boolean arg1, int arg2)";
        r1.retVal=new Object();
        r1.sessionId="XXLJHDJHDHF";
        r1.tin=5577376;
        r1.tout=7544522;
        r1.traceId=882287444;
        r1.vmName="myVM";

        OperationExecutionRecord r2 = new OperationExecutionRecord();
        r2.componentName = "p1.p2.p3.componentname";
        r2.eoi=1;
        r2.ess=2;
        r2.experimentId=55;
        r2.isEntryPoint=true;
        r2.opname="operation(boolean arg1, int arg2)";
        r2.retVal=new Object();
        r2.sessionId="XXLJHDJHDHF";
        r2.tin=5577376;
        r2.tout=7544522;
        r2.traceId=882287444;
        r2.vmName="myVM";

        assertEquals(r1, r2);

        /* Modification of componentName */
        String oldComponentName = r2.componentName;
        r2.componentName = r2.componentName+"_";
        assertFalse(r1.equals(r2));
        r2.componentName = oldComponentName;
        assertEquals(r1, r2);

        /* Modification of eoi */
        int oldEoi = r2.eoi;
        r2.eoi++;
        assertFalse(r1.equals(r2));
        r2.eoi = oldEoi;
        assertEquals(r1, r2);

        /* Modification of ess */
        int oldEss = r2.ess;
        r2.ess++;
        assertFalse(r1.equals(r2));
        r2.ess = oldEss;
        assertEquals(r1, r2);

        /* Modification of opname */
        String oldOpname = r2.opname;
        r2.opname = r2.opname+"_";
        assertFalse(r1.equals(r2));
        r2.opname = oldOpname;
        assertEquals(r1, r2);

        /* Modification of sessionId */
        String oldsessionId = r2.sessionId;
        r2.sessionId = r2.sessionId+"_";
        assertFalse(r1.equals(r2));
        r2.sessionId = oldsessionId;
        assertEquals(r1, r2);

        /* Modification of tin */
        long oldTin = r2.tin;
        r2.tin++;
        assertFalse(r1.equals(r2));
        r2.tin = oldTin;
        assertEquals(r1, r2);

        /* Modification of tout */
        long oldTout = r2.tout;
        r2.tout++;
        assertFalse(r1.equals(r2));
        r2.tout = oldTout;
        assertEquals(r1, r2);

        /* Modification of traceId */
        long oldTraceId = r2.traceId;
        r2.traceId++;
        assertFalse(r1.equals(r2));
        r2.traceId = oldTraceId;
        assertEquals(r1, r2);

        /* Modification of vmName */
        String oldVmName = r2.vmName;
        r2.vmName = r2.vmName+"_";
        assertFalse(r1.equals(r2));
        r2.vmName = oldVmName;
        assertEquals(r1, r2);
    }
}