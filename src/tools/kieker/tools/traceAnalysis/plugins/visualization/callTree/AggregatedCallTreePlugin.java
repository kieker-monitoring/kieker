package kieker.tools.traceAnalysis.plugins.visualization.callTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.tools.traceAnalysis.plugins.traceReconstruction.TraceProcessingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class AggregatedCallTreePlugin<T> extends AbstractCallTreePlugin<T> {

    private static final Log log = LogFactory.getLog(AbstractCallTreePlugin.class);
    private final AbstractAggregatedCallTreeNode<T> root;
    private final File dotOutputFile;
    private final boolean includeWeights;
    private final boolean shortLabels;

    public AggregatedCallTreePlugin(final String name,
            SystemModelRepository systemEntityFactory,
            AbstractAggregatedCallTreeNode<T> root,
            final File dotOutputFile,
            final boolean includeWeights,
            final boolean shortLabels) {
        super(name, systemEntityFactory);
        this.root = root;
       this.dotOutputFile = dotOutputFile;
        this.includeWeights = includeWeights;
        this.shortLabels = shortLabels;
    }
    private int numGraphsSaved = 0;

    public void saveTreeToDotFile(final String outputFnBase, final boolean includeWeights,
            final boolean shortLabels) throws FileNotFoundException {
        saveTreeToDotFile(super.getSystemEntityFactory(), this.root,
                outputFnBase, includeWeights, false, // do not include EOIs
                shortLabels);
        this.numGraphsSaved++;
        this.printMessage(new String[]{
                    "Wrote call tree to file '" + outputFnBase + ".dot" + "'",
                    "Dot file can be converted using the dot tool",
                    "Example: dot -T svg " + outputFnBase + ".dot" + " > " + outputFnBase + ".svg"
                });
    }

    @Override
    public void printStatusMessage() {
        super.printStatusMessage();
        System.out.println("Saved " + this.numGraphsSaved + " call tree" + (this.numGraphsSaved > 1 ? "s" : ""));
    }

    @Override
    public boolean execute() {
        return true; // no need to do anything here
    }

    /**
     * Saves the call tree to the dot file if error is not true.
     *
     * @param error
     */
    @Override
    public void terminate(boolean error) {
        if (!error){
            try {
                this.saveTreeToDotFile(
                        this.dotOutputFile.getCanonicalPath(),
                        this.includeWeights,
                        this.shortLabels);
            } catch (IOException ex) {
               log.error("IOException", ex);
            }
        }
    }

    @Override
    public IInputPort<MessageTrace> getMessageTraceInputPort() {
        return this.messageTraceInputPort;
    }

    private final IInputPort<MessageTrace> messageTraceInputPort =
            new AbstractInputPort<MessageTrace>("Message traces") {

                @Override
                public void newEvent(MessageTrace t) {
                    try {
                        addTraceToTree(root, t, true); // aggregated
                        reportSuccess(t.getTraceId());
                    } catch (TraceProcessingException ex) {
                        log.error("TraceProcessingException", ex);
                        reportError(t.getTraceId());
                    }
                }
            };
}
