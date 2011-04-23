package kieker.tools.traceAnalysis.plugins;

import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.IInputPort;

/**
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractExecutionTraceProcessingPlugin
        extends AbstractTraceProcessingPlugin {
        public AbstractExecutionTraceProcessingPlugin (String name, SystemModelRepository systemEntityFactory){
            super(name, systemEntityFactory);
        }

        public abstract IInputPort<ExecutionTrace> getExecutionTraceInputPort();
}
