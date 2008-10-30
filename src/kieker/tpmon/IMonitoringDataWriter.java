/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kieker.tpmon;

import java.util.Vector;
import kieker.tpmon.annotations.TpmonInternal;

/**
 *
 * @author voorn
 */
public interface IMonitoringDataWriter {
   
    @TpmonInternal()
    public boolean insertMonitoringDataNow(KiekerExecutionRecord execData);

    public boolean init(String initString);
    
    /**
     * Returns a vector of workers or null if none.
     * 
     * @return
     */
    @TpmonInternal()    
    public Vector<Worker> getWorkers();
    
    @TpmonInternal()
    public void setDebug(boolean debug);

    @TpmonInternal()
    public boolean isDebug();
}
