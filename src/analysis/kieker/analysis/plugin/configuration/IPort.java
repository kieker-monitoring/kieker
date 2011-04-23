package kieker.analysis.plugin.configuration;

import kieker.analysis.plugin.IAnalysisEvent;

/**
 *
 * @author Andre van Hoorn
 */
public interface IPort<T extends IAnalysisEvent> {
    public String getDescription();
}
