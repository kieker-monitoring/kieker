package kieker.analysis.plugin.configuration;

import kieker.analysis.plugin.IAnalysisEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractInputPort<T extends IAnalysisEvent> implements IInputPort<T> {

    private static final Log log = LogFactory.getLog(AbstractInputPort.class);
    private final String description;

    @SuppressWarnings("unused")
	private AbstractInputPort() {
        this.description = null;
    }

    public AbstractInputPort(final String description) {
        this.description = description;
    }

    @Override
	public String getDescription() {
        return this.description;
    }
}
