package kieker.tools.restructuring.transformations;

import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class CutTransformation extends AtomicTransformation {

    private String componentName;
    private String operationName;
    private AssemblyOperation operation;

    public CutTransformation(final AssemblyModel model) {
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
        // System.out.println("cutting" +this.componentName);
        model.getComponents().get(this.componentName).getOperations().removeKey(this.operationName);
        this.model = model;

    }

}
