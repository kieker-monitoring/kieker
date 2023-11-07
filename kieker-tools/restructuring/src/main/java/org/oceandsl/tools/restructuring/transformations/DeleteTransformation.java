package org.oceandsl.tools.restructuring.transformations;

import kieker.model.analysismodel.assembly.AssemblyModel;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class DeleteTransformation extends AtomicTransformation {

    private String componentName;

    public DeleteTransformation(final AssemblyModel model) {
        super(model);
    }

    public void setComponentName(final String componentName) {
        this.componentName = componentName;
    }

    public String getComponentName() {
        return this.componentName;
    }

    @Override
    public void applyTransformation(final AssemblyModel model) {
        model.getComponents().removeKey(this.componentName);
        this.model = model;
    }

}
