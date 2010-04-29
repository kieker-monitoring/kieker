/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
package kieker.tpan.plugin.callTree;

import java.io.FileNotFoundException;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.datamodel.factories.SystemEntityFactory;
import kieker.tpan.plugin.traceAnalysis.traceReconstruction.TraceProcessingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class AggregatedCallTreePlugin<T> extends AbstractCallTreePlugin<T> {

    private static final Log log = LogFactory.getLog(AbstractCallTreePlugin.class);
    private final AbstractAggregatedCallTreeNode<T> root;

    public AggregatedCallTreePlugin(final String name,
            SystemEntityFactory systemEntityFactory,
            AbstractAggregatedCallTreeNode<T> root) {
        super(name, systemEntityFactory);
        this.root = root;
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

    public void newEvent(MessageTrace t) {
        try {
            addTraceToTree(root, t, true); // aggregated
            this.reportSuccess(t.getTraceId());
        } catch (TraceProcessingException ex) {
            log.error("TraceProcessingException", ex);
            this.reportError(t.getTraceId());
        }
    }

    @Override
    public void printStatusMessage() {
        super.printStatusMessage();
        System.out.println("Saved " + this.numGraphsSaved + " call tree" + (this.numGraphsSaved > 1 ? "s" : ""));
    }

    public boolean execute() {
        return true; // no need to do anything here
    }

    public void terminate(boolean error) {
        // no need to do anything here
    }
}
