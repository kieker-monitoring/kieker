/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.common.logReader;

import java.util.logging.Level;
import java.util.logging.Logger;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;

/**
 * This is a logReader for testing. It creates just some random data. All
 * have the same operation and component and some random wait time of approx.
 * a second. It never terminates.
 *
 * @author matthias
 */
public class DummyLogReader extends AbstractKiekerMonitoringLogReader{

    private int i = 1;

    @Override
    /**
     * It never finished to produce dummy data about each second
     */
    public boolean execute() throws LogReaderExecutionException {
        while(true) {
            long startTime = System.nanoTime();
            
            //wait a bit
            long sleeptime = Math.round(Math.random() * 750d + 250d);                        
            try {Thread.sleep(sleeptime);} catch (InterruptedException ex) {
                Logger.getLogger(DummyLogReader.class.getName()).log(Level.SEVERE, null, ex);}

            KiekerExecutionRecord testRecord = KiekerExecutionRecord.getInstance("ComponentA", "OperationA", i, startTime, System.nanoTime());
            deliverRecordToConsumers(testRecord);
        }        
    }

    public void init(String initString) throws IllegalArgumentException {
        //nothing to do...
    }
}
