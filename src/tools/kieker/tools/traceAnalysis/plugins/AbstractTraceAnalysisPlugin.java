package kieker.tools.traceAnalysis.plugins;

import kieker.analysis.plugin.IAnalysisPlugin;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractTraceAnalysisPlugin implements IAnalysisPlugin {
    private final String name;
    private final SystemModelRepository systemEntityFactory;

    /**
     * Must not be used for construction
     */
    @SuppressWarnings("unused")
	private AbstractTraceAnalysisPlugin(){
        this.name = "no name";
        this.systemEntityFactory = null;
    }

    public AbstractTraceAnalysisPlugin (final String name,
            final SystemModelRepository systemEntityFactory){
        this.systemEntityFactory = systemEntityFactory;
        this.name = name;
    }

    protected void printMessage(final String[] lines){
        System.out.println("");
        System.out.println("#");
        System.out.println("# Plugin: " + this.name);
        for (final String l : lines){
            System.out.println(l);
        }
    }

   protected final SystemModelRepository getSystemEntityFactory() {
        return this.systemEntityFactory;
    }
}
