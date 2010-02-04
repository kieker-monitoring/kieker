/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpan.recordConsumer;

import kieker.tpan.datamodel.MessageTrace;

/**
 *
 * @author Andre van Hoorn
 */
public interface IMessageTraceReceiver {
    public void newTrace (MessageTrace t);
}
