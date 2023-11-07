package org.oceandsl.tools.restructuring.transformations;

import java.util.ArrayList;
import java.util.List;

import kieker.model.analysismodel.assembly.AssemblyModel;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class MergeTransformation extends CompositeTransformation {

    public MergeTransformation(final AssemblyModel model) {
        super(model);
        this.steps.addAll(this.steps);
    }

    @Override
    public void applyTransformation(final AssemblyModel model) {
        // TODO CHECK IF THE LIST FORMAT IS APPROPRIETE
        // 1ST OPERATION Split other operation move
        for (final AbstractTransformationStep t : this.steps) {
            t.applyTransformation(model);
        }
        this.model = model;
    }

    public void add(final AbstractTransformationStep step) {
        this.steps.add(step);
    }

    public DeleteTransformation getDeleteTransformation() {
        return (DeleteTransformation) this.steps.get(this.steps.size() - 1);
    }

    public List<AbstractTransformationStep> getMoveTransformations() {
        final List<AbstractTransformationStep> result = new ArrayList<>();
        result.addAll(this.steps);
        result.remove(this.steps.size() - 1);
        return result;
    }

}
