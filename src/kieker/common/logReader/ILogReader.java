/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.common.logReader;

import java.util.Vector;
import kieker.common.logReader.IMonitoringRecordConsumer;

/**
 *
 * @author Andre van Hoorn
 */
public interface ILogReader {
    public void registerConsumer(IMonitoringRecordConsumer consumer);
    public Vector<IMonitoringRecordConsumer> getConsumers();
    public void run();
}
