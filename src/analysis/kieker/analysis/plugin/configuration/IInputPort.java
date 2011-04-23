package kieker.analysis.plugin.configuration;

import kieker.analysis.plugin.IAnalysisEvent;

/**
 *
 * @author Andre van Hoorn
 */
public interface IInputPort<T extends IAnalysisEvent> extends IPort<T>{
    public void newEvent(T event);
}
