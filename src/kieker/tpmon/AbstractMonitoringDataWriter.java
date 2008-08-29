/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpmon;

/**
 *
 * @author voorn
 */
public abstract class AbstractMonitoringDataWriter implements IMonitoringDataWriter{
    private boolean debugEnabled;
    
    public abstract boolean insertMonitoringDataNow(int experimentId, String vmName, String opname, String sessionID, String requestID, long tin, long tout, int executionOrderIndex, int executionStackSize);
    public void setDebug(boolean debug) {
        this.debugEnabled = debug;
    }

    public boolean isDebug() {
        return this.debugEnabled;
    }

}
