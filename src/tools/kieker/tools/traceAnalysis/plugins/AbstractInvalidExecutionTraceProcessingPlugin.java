package kieker.tools.traceAnalysis.plugins;

import kieker.tools.traceAnalysis.systemModel.InvalidExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.IInputPort;


/**
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractInvalidExecutionTraceProcessingPlugin
        extends AbstractTraceProcessingPlugin {
        public AbstractInvalidExecutionTraceProcessingPlugin (String name, SystemModelRepository systemEntityFactory){
            super(name, systemEntityFactory);
        }

        public abstract IInputPort<InvalidExecutionTrace> getInvalidExecutionTraceInputPort();
}
