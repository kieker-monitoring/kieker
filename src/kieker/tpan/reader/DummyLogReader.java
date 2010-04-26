/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpan.reader;

import java.util.logging.Level;
import java.util.logging.Logger;
import kieker.common.record.OperationExecutionRecord;

/**
 * This is a logReader for testing. It creates just some random data. All
 * have the same operation and component and some random wait time of approx.
 * a second. It never terminates.
 *
 * @author matthias
 */
public class DummyLogReader extends AbstractMonitoringLogReader{

    private int i = 1;

    @Override
    /**
     * It never finished to produce dummy data about each second
     */
    public boolean read() throws MonitoringLogReaderException {
        while(true) {
            long startTime = System.nanoTime();
            
            //wait a bit
            long sleeptime = Math.round(Math.random() * 750d + 250d);                        
            try {Thread.sleep(sleeptime);} catch (InterruptedException ex) {
                Logger.getLogger(DummyLogReader.class.getName()).log(Level.SEVERE, null, ex);}

            OperationExecutionRecord testRecord = new OperationExecutionRecord("ComponentA", "OperationA", i, startTime, System.nanoTime());
            deliverRecord(testRecord);
        }        
    }

    public void init(String initString) throws IllegalArgumentException {
        //nothing to do...
    }
}
