/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpan.recordConsumer;

/**
 *
 * @author Andre van Hoorn
 */
public class ExecutionEventProcessingException extends Exception {
    public ExecutionEventProcessingException (String msg){
        super(msg);
    }

    public ExecutionEventProcessingException (String msg, Throwable t){
        super(msg, t);
    }
}
