package org.oceandsl.tools.restructuring.transformations;

import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class PasteTransformation extends AtomicTransformation {

    private String componentName;
    private String operationName;
    private AssemblyOperation operation;

    public PasteTransformation(final AssemblyModel model) {
        super(model);
    }

    public void setComponentName(final String componentName) {
        this.componentName = componentName;
    }

    public void setOperationName(final String operationName) {
        this.operationName = operationName;
    }

    public AssemblyOperation getOperation() {
        return this.operation;
    }

    public String getComponentName() {
        return this.componentName;
    }

    public String getOperationName() {
        return this.operationName;
    }

    @Override
    public void applyTransformation(final AssemblyModel model) {
        model.getComponents().get(this.componentName).getOperations().put(this.operationName,
                AbstractTransformationStep.FACTORY.createAssemblyOperation());
        this.model = model;
        // System.out.println("x");
    }

}
