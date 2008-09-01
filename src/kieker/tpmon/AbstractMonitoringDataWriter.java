/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpmon;

import kieker.tpmon.annotations.TpmonInternal;

/**
 *
 * @author voorn
 */
public abstract class AbstractMonitoringDataWriter implements IMonitoringDataWriter{
    private boolean debugEnabled;
    
     @TpmonInternal()
    public abstract boolean insertMonitoringDataNow(int experimentId, String vmName, String opname, String sessionID, String requestID, long tin, long tout, int executionOrderIndex, int executionStackSize);
      
     @TpmonInternal()
    public void setDebug(boolean debug) {
        this.debugEnabled = debug;
    }
     
 @TpmonInternal()
    public boolean isDebug() {
        return this.debugEnabled;
    }

}
