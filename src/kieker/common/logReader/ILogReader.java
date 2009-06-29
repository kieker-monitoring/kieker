/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.common.logReader;

/**
 *
 * @author Andre van Hoorn
 */
public interface ILogReader {
    public void addConsumer(IMonitoringRecordConsumer consumer, String[] recordTypeSubscriptionList);
    public void run();
}
