package kieker.tools.traceAnalysis.plugins;

import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.IInputPort;

/**
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractMessageTraceProcessingPlugin
        extends AbstractTraceProcessingPlugin {
    public AbstractMessageTraceProcessingPlugin (String name, SystemModelRepository systemEntityFactory){
        super (name, systemEntityFactory);
    }

    public abstract IInputPort<MessageTrace> getMessageTraceInputPort();
}
