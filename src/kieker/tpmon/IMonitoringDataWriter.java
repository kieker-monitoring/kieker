/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpmon;

/**
 *
 * @author voorn
 */
public interface IMonitoringDataWriter {
    public boolean insertMonitoringDataNow(int experimentId, String vmName, String opname, String sessionID, String requestID, long tin, long tout, int executionOrderIndex, int executionStackSize);
    
    public void setDebug(boolean debug);
    public boolean isDebug();
}
