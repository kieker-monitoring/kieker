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
    public boolean insertMonitoringDataNow(KiekerExecutionRecord execData);

    @TpmonInternal()
    public void setDebug(boolean debug);

    @TpmonInternal()
    public boolean isDebug();
}
