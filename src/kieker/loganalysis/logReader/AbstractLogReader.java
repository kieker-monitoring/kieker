/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kieker.loganalysis.logReader;

import java.util.Vector;
import kieker.loganalysis.recordConsumer.IMonitoringRecordConsumer;

/**
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractLogReader implements ILogReader {

    private Vector<IMonitoringRecordConsumer> listeners = new Vector<IMonitoringRecordConsumer>();

    public void registerConsumer(IMonitoringRecordConsumer consumer) {
        this.listeners.add(consumer);
    }

    public Vector<IMonitoringRecordConsumer> getConsumers() {
        return this.listeners;
    }

    public abstract void run();
}
