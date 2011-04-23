package kieker.analysis.plugin.configuration;

import kieker.analysis.plugin.IAnalysisEvent;

/**
 *
 * @author Andre van Hoorn
 */
public interface IOutputPort<T extends IAnalysisEvent> extends IPort<T>{
    public void subscribe(IInputPort<T> subscriber);
    public void unsubscribe(IInputPort<T> subscriber);
}
