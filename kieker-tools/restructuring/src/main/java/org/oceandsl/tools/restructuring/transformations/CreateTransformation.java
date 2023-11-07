package org.oceandsl.tools.restructuring.transformations;

import kieker.model.analysismodel.assembly.AssemblyModel;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class CreateTransformation extends AtomicTransformation {

    private String componentName;

    public CreateTransformation(final AssemblyModel model) {
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

        // Entry<String, AssemblyComponent> entry = Map.entry(this.componentName,
        // fac.createAssemblyComponent());
        model.getComponents().put(this.componentName, AbstractTransformationStep.FACTORY.createAssemblyComponent());
        this.model = model;
        // this.model.getComponents().add(entry);

    }

}
