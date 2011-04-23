package kieker.tools.traceAnalysis.plugins.executionFilter;

import java.util.TreeSet;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.analysis.plugin.IAnalysisPlugin;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.analysis.plugin.configuration.IOutputPort;
import kieker.analysis.plugin.configuration.OutputPort;

/**
 * Allows to filter Execution objects based on their traceId.
 *
 * @author Andre van Hoorn
 */
public class TraceIdFilter implements IAnalysisPlugin {

    private final TreeSet<Long> selectedTraces;

    /**
     * Creates a filter instance that only passes Execution objects <i>e</i>
     * whose traceId (<i>e.traceId</i>) is element of the set <i>selectedTraces</i>.
     *
     * @param selectedTraces 
     */
    public TraceIdFilter(final TreeSet<Long> selectedTraces) {
        this.selectedTraces = selectedTraces;
    }

    public IInputPort<Execution> getExecutionInputPort() {
        return this.executionInputPort;
    }
    private final IInputPort<Execution> executionInputPort =
            new AbstractInputPort<Execution>("Execution input") {

        @Override
        public void newEvent(Execution event) {
            newExecution(event);
        }
    };

    public IOutputPort<Execution> getExecutionOutputPort(){
        return this.executionOutputPort;
    }
    private final OutputPort<Execution> executionOutputPort =
            new OutputPort<Execution>("Execution output");

    private void newExecution(Execution execution){
        if (this.selectedTraces != null
                && !this.selectedTraces.contains(execution.getTraceId())) {
            // not interested in this trace
            return;
        }
        this.executionOutputPort.deliver(execution);
    }

    @Override
    public boolean execute() {
        return true; // do nothing
    }

    @Override
    public void terminate(boolean error) {
        return; // do nothing
    }

}
