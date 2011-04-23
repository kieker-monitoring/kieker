package kieker.analysis.plugin;

/**
 *
 * @author Andre van Hoorn
 */
public class EventProcessingException extends Exception {
    private static final long serialVersionUID = 345L;
    public EventProcessingException (String msg){
        super(msg);
    }

    public EventProcessingException (String msg, Throwable t){
        super(msg, t);
    }
}
