package kieker.tools.traceAnalysis.plugins.traceWriter;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import kieker.tools.traceAnalysis.systemModel.ExecutionTrace;

import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.tools.traceAnalysis.plugins.AbstractExecutionTraceProcessingPlugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class ExecutionTraceWriterPlugin extends AbstractExecutionTraceProcessingPlugin {

    private static final Log log = LogFactory.getLog(ExecutionTraceWriterPlugin.class);
    private final String outputFn;
    private final BufferedWriter ps;

    public ExecutionTraceWriterPlugin(
            final String name,
            final SystemModelRepository systemEntityFactory,
            final String outputFn) throws FileNotFoundException, IOException {
        super(name, systemEntityFactory);
        this.outputFn = outputFn;
        this.ps = new BufferedWriter(new FileWriter(outputFn));
    }

    @Override
    public void printStatusMessage() {
        super.printStatusMessage();
        final int numTraces = this.getSuccessCount();
        System.out.println("Wrote " + numTraces + " execution trace"
                + (numTraces > 1 ? "s" : "") + " to file '"
                + outputFn + "'");
    }

    @Override
    public void terminate(final boolean error) {
        if (this.ps != null) {
            try {
                this.ps.close();
            } catch (IOException ex) {
                log.error("IOException", ex);
            }
        }
    }

    @Override
    public boolean execute() {
        return true; // no need to do anything here
    }

    @Override
    public IInputPort<ExecutionTrace> getExecutionTraceInputPort() {
        return this.executionTraceInputPort;
    }
    private final IInputPort<ExecutionTrace> executionTraceInputPort =
            new AbstractInputPort<ExecutionTrace>("Execution traces") {

                @Override
                public void newEvent(ExecutionTrace et) {
                    try {
                        ps.append(et.toString());
                        reportSuccess(et.getTraceId());
                    } catch (IOException ex) {
                        reportError(et.getTraceId());
                        log.error(ex, ex);
                    }
                }
            };
}
