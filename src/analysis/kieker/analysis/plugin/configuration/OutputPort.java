package kieker.analysis.plugin.configuration;

import java.util.ArrayList;
import java.util.Collection;

import kieker.analysis.plugin.IAnalysisEvent;

/**
 *
 * @author Andre van Hoorn
 */
public class OutputPort<T extends IAnalysisEvent> implements IOutputPort<T> {

    //private static final Log log = LogFactory.getLog(OutputPort.class);
    
    /** Should use "better" data structure from java.concurrent */
    private final Collection<IInputPort<T>> subscriber =
            new ArrayList<IInputPort<T>>();
    private final String description;

    @SuppressWarnings("unused")
	private OutputPort() {
        this.description = null;
    }

    public OutputPort(final String description) {
        this.description = description;
    }

    public synchronized void deliver(final T event) {
        for (final IInputPort<T> l : this.subscriber) {
            l.newEvent(event);
        }
    }

    @Override
	public synchronized void subscribe(final IInputPort<T> subscriber) {
        this.subscriber.add(subscriber);
    }

    @Override
	public synchronized void unsubscribe(final IInputPort<T> subscriber) {
        this.subscriber.remove(subscriber);
    }

    @Override
	public String getDescription() {
        return this.description;
    }
}
