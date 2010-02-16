package kieker.tpan.plugins.dependencyGraph;

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
import kieker.tpan.plugins.traceReconstruction.AbstractTpanMessageTraceProcessingComponent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import kieker.tpan.datamodel.AllocationComponentInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.datamodel.factories.SystemEntityFactory;

/**
 * Refactored copy from LogAnalysis-legacy tool
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
public abstract class AbstractDependencyGraphPlugin<T> extends AbstractTpanMessageTraceProcessingComponent {

    private static final Log log = LogFactory.getLog(AbstractDependencyGraphPlugin.class);
    private DependencyGraph<T> dependencyGraph;

    public AbstractDependencyGraphPlugin(final String name,
            final SystemEntityFactory systemEntityFactory) {
        super(name, systemEntityFactory);
        this.dependencyGraph =
                new DependencyGraph(
                systemEntityFactory.getAllocationFactory().rootAllocationComponent.getId(),
                systemEntityFactory.getAllocationFactory().rootAllocationComponent);
    }

    protected abstract void dotEdges(Collection<DependencyGraphNode<T>> nodes,
            PrintStream ps, final boolean shortLabels);

    protected abstract void dotVerticesFromSubTree(final DependencyGraphNode n,
            final PrintStream ps, final boolean includeWeights);

    private void graphToDot(
            final PrintStream ps, final boolean includeWeights,
            final boolean shortLabels) {
        // preamble:
        ps.println("digraph G {");
        StringBuilder edgestringBuilder = new StringBuilder();

        dotEdges(this.dependencyGraph.getNodes(), ps,
                shortLabels);
        dotVerticesFromSubTree(this.dependencyGraph.getRootNode(),
                ps, includeWeights);

        ps.println(edgestringBuilder.toString());
        ps.println("}");
    }
    private int numGraphsSaved = 0;

    public final void saveToDotFile(final String outputFnBase, final boolean includeWeights,
            final boolean shortLabels) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new FileOutputStream(outputFnBase + ".dot"));
        this.graphToDot(ps, includeWeights, shortLabels);
        ps.flush();
        ps.close();
        this.numGraphsSaved++;
        this.printMessage(new String[]{
                    "Wrote dependency graph to file '" + outputFnBase + ".dot" + "'",
                    "Dot file can be converted using the dot tool",
                    "Example: dot -T svg " + outputFnBase + ".dot" + " > " + outputFnBase + ".svg"
                });
    }

    public abstract void newTrace(MessageTrace t);

    @Override
    public void printStatusMessage() {
        super.printStatusMessage();
        System.out.println("Saved " + this.numGraphsSaved + " dependency graph" + (this.numGraphsSaved > 1 ? "s" : ""));
    }

    @Override
    public void cleanup() {
        // no cleanup required
    }
}
