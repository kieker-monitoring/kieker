package kieker.tools.traceAnalysis.plugins.traceWriter;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.tools.traceAnalysis.plugins.AbstractMessageTraceProcessingPlugin;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class MessageTraceWriterPlugin extends AbstractMessageTraceProcessingPlugin {

    private static final Log log = LogFactory.getLog(MessageTraceWriterPlugin.class);
    private final String outputFn;
    private final BufferedWriter ps;

    public MessageTraceWriterPlugin(
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
        System.out.println("Wrote " + numTraces + " trace"
                + (numTraces > 1 ? "s" : "") + " to file '" + this.outputFn
                + "'");
    }

    @Override
    public void terminate(final boolean error) {
        if (this.ps != null) {
            try {
                this.ps.close();
            } catch (final IOException ex) {
                MessageTraceWriterPlugin.log.error("IOException", ex);
            }
        }
    }

    @Override
    public boolean execute() {
        return true; // no need to do anything here
    }

    @Override
    public IInputPort<MessageTrace> getMessageTraceInputPort() {
        return this.MessageTraceInputPort;
    }
    private final IInputPort<MessageTrace> MessageTraceInputPort =
            new AbstractInputPort<MessageTrace>("Message traces") {

                @Override
                public void newEvent(final MessageTrace mt) {
                    try {
                        MessageTraceWriterPlugin.this.ps.append(mt.toString());
                        MessageTraceWriterPlugin.this.reportSuccess(mt.getTraceId());
                    } catch (final IOException ex) {
                        MessageTraceWriterPlugin.log.error("IOException", ex);
                        MessageTraceWriterPlugin.this.reportError(mt.getTraceId());
                    }
                }
            };
}
