package kieker.tools.restructuring.transformations;

import java.util.ArrayList;
import java.util.List;

import kieker.model.analysismodel.assembly.AssemblyModel;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class SplitTransformation extends CompositeTransformation {

    public SplitTransformation(final AssemblyModel model) {
        super(model);
    }

    @Override
    public void applyTransformation(final AssemblyModel model) {
        // TODO CHEC IF THE LIST FORMAT IS APPROPRIETE
        // 1ST OPERATION Split other operation move
        for (final AbstractTransformationStep t : this.steps) {
            t.applyTransformation(model);
        }
        this.model = model;
    }

    public void add(final AbstractTransformationStep step) {
        this.steps.add(step);
    }

    public CreateTransformation getCreateTransformation() {
        return (CreateTransformation) this.steps.get(0);
    }

    public List<AbstractTransformationStep> getMoveTransformation() {
        final List<AbstractTransformationStep> result = new ArrayList<>();
        result.addAll(this.steps);
        result.remove(0);
        return result;

    }

}
