package org.oceandsl.tools.restructuring.transformations;

import kieker.model.analysismodel.assembly.AssemblyModel;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class FullTransformation extends CompositeTransformation {

    public FullTransformation(final AssemblyModel model) {
        super(model);
    }

    @Override
    public void applyTransformation(final AssemblyModel model) {
        for (final AbstractTransformationStep step : this.steps) {
            step.applyTransformation(model);
        }
        this.model = model;

    }

}
