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
public interface IMonitoringDataWriter {

    @TpmonInternal()
    public boolean insertMonitoringDataNow(int experimentId, String vmName, String opname, String sessionID, String requestID, long tin, long tout, int executionOrderIndex, int executionStackSize);

    @TpmonInternal()
    public void setDebug(boolean debug);

    @TpmonInternal()
    public boolean isDebug();
}
