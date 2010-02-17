/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpan.plugins.traceReconstruction;

/**
 *
 * @author Andre van Hoorn
 */
public class TraceProcessingException extends Exception {
    public static final long serialVersionUID = 189899L;

    public TraceProcessingException (String msg){
        super(msg);
    }

    public TraceProcessingException (String msg, Throwable t){
        super(msg, t);
    }
}
