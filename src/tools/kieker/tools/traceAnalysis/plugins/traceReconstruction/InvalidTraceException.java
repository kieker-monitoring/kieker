/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tools.traceAnalysis.plugins.traceReconstruction;

/**
 *
 * @author avanhoorn
 */
public class InvalidTraceException extends Exception {

    private static final long serialVersionUID = 1893L;

    public InvalidTraceException(String message) {
        super(message);
    }

    public InvalidTraceException(String message, Throwable t) {
        super(message, t);
    }
}
