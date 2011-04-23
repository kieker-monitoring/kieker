package kieker.tools.traceAnalysis.plugins.traceWriter;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import kieker.tools.traceAnalysis.systemModel.InvalidExecutionTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.tools.traceAnalysis.plugins.AbstractInvalidExecutionTraceProcessingPlugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class InvalidExecutionTraceWriterPlugin extends AbstractInvalidExecutionTraceProcessingPlugin {

    private static final Log log = LogFactory.getLog(InvalidExecutionTraceWriterPlugin.class);
    private final String outputFn;
    private final BufferedWriter ps;

    public InvalidExecutionTraceWriterPlugin(
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
        System.out.println("Wrote " + numTraces + " execution trace artifact"
                + (numTraces > 1 ? "s" : "") + " to file '"
                + this.outputFn + "'");
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
    public IInputPort<InvalidExecutionTrace> getInvalidExecutionTraceInputPort() {
        return this.InvalidExecutionTraceInputPort;
    }
    private final IInputPort<InvalidExecutionTrace> InvalidExecutionTraceInputPort =
            new AbstractInputPort<InvalidExecutionTrace>("Invalid execution traces") {

                @Override
                public void newEvent(InvalidExecutionTrace et) {
                    try {
                        ps.append(et.getInvalidExecutionTraceArtifacts().toString());
                        reportSuccess(et.getInvalidExecutionTraceArtifacts().getTraceId());
                    } catch (IOException ex) {
                        reportError(et.getInvalidExecutionTraceArtifacts().getTraceId());
                        log.error(ex, ex);
                    }
                }
            };
}
