package kieker.tools.restructuring.mapper;

import java.util.Map.Entry;
import java.util.Set;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyModel;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class EmptyMapper extends BasicComponentMapper {

    /**
     * Constructor is used to create an empty mapper.
     *
     * @param original
     *            original model
     * @param goal
     *            goal model
     * @param originalModelName
     *            name of the original model
     * @param goalModelName
     *            name of the goal model
     */
    public EmptyMapper(final AssemblyModel original, final AssemblyModel goal, final String originalModelName,
            final String goalModelName) {
        super(originalModelName, goalModelName);
        this.original = original;
        this.goal = goal;
        // System.out.println(this.orig != null);
        // System.out.println(this.goal != null);
        // init mappings
        this.populateOperationTocomponentG();
        this.populateOperationToComponentO();
        // populateTraceModel();
        // computeOriginalComponentNames();

    }

    private void populateOperationToComponentO() {
        for (final Entry<String, AssemblyComponent> e : this.original.getComponents().entrySet()) {
            final Set<String> ops = e.getValue().getOperations().keySet();
            for (final String s : ops) {
                this.operationToComponentO.put(s, e.getKey());
            }
        }
    }

    private void populateOperationTocomponentG() {
        for (final Entry<String, AssemblyComponent> e : this.goal.getComponents().entrySet()) {
            final Set<String> ops = e.getValue().getOperations().keySet();
            for (final String s : ops) {
                this.operationToComponentG.put(s, e.getKey());
            }
        }
    }
}
