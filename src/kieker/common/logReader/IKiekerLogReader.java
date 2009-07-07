/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.common.logReader;

/**
 *
 * @author Andre van Hoorn
 */
public interface IKiekerLogReader {
    public void addConsumer(IKiekerRecordConsumer consumer, String[] recordTypeSubscriptionList);
    public boolean execute();
    public void terminate();
}
