/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpan.recordConsumer;

import kieker.tpan.datamodel.ExecutionTrace;

/**
 *
 * @author Andre van Hoorn
 */
public interface IExecutionTraceReceiver {
    public void newTrace (ExecutionTrace t);
}
