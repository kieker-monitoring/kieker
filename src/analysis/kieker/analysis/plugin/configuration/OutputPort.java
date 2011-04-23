package kieker.analysis.plugin.configuration;

import java.util.ArrayList;
import java.util.Collection;
import kieker.analysis.plugin.IAnalysisEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class OutputPort<T extends IAnalysisEvent> implements IOutputPort<T> {

    private static final Log log = LogFactory.getLog(OutputPort.class);
    /** Should use "better" data structure from java.concurrent */
    private final Collection<IInputPort<T>> subscriber =
            new ArrayList<IInputPort<T>>();
    private final String description;

    private OutputPort() {
        this.description = null;
    }

    public OutputPort(final String description) {
        this.description = description;
    }

    public synchronized void deliver(T event) {
        for (IInputPort<T> l : this.subscriber) {
            l.newEvent(event);
        }
    }

    public synchronized void subscribe(IInputPort<T> subscriber) {
        this.subscriber.add(subscriber);
    }

    public synchronized void unsubscribe(IInputPort<T> subscriber) {
        this.subscriber.remove(subscriber);
    }

    public String getDescription() {
        return this.description;
    }
}
