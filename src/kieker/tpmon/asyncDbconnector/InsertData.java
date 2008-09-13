/*
 * InsertData.java
 *
 * Created on July 30, 2007, 9:22 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package kieker.tpmon.asyncDbconnector;

import java.io.Serializable;

/**
 *
 * @author matthias
 */

public class InsertData implements Serializable{
    //public String componentname;
    //public String methodname;
    public int experimentId;
    public String vmName;
    public String opname;
    public String sessionid;
    public String traceid;
    public long tin;
    public long tout;
    public int executionOrderIndex;
    public int executionStackSize;
    
    public InsertData(int experimentId, String vmName, String inopname, String insessionid, String intraceid, long intin, long intout, int inexecutionOrderIndex, int inexecutionStackSize) {
        this.experimentId = experimentId;
        this.vmName = vmName;
        this.tin = intin;
        this.tout = intout;
        this.traceid = intraceid;
        this.sessionid = insessionid;
        this.opname = inopname;        
        this.executionOrderIndex = inexecutionOrderIndex;
        this.executionStackSize = inexecutionStackSize;
    }        
}




