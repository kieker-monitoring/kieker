package kieker.tools.restructuring.transformations;

import java.util.ArrayList;
import java.util.List;

import kieker.model.analysismodel.assembly.AssemblyModel;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public abstract class CompositeTransformation extends AbstractTransformationStep {

    protected List<AbstractTransformationStep> steps;

    public CompositeTransformation(final AssemblyModel model) {
        super(model);
        this.steps = new ArrayList<>();
    }

    public List<AbstractTransformationStep> getSteps() {
        return this.steps;
    }

}
