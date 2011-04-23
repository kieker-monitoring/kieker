package kieker.tools.traceAnalysis.systemModel.util;

import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.Operation;

/**
 *
 * @author Andre van Hoorn
 */
public class AssemblyComponentOperationPair {
    private final int id;
    private final Operation operation;

    private final AssemblyComponent assemblyComponent;

    private AssemblyComponentOperationPair (){
        this.id = -1;
        this.operation = null;
        this.assemblyComponent = null;
    }

    public AssemblyComponentOperationPair (
            final int id, final Operation operation, final AssemblyComponent AssemblyComponent){
        this.id = id;
        this.operation = operation;
        this.assemblyComponent = AssemblyComponent;
    }

    public final int getId() {
        return this.id;
    }

    public final AssemblyComponent getAssemblyComponent() {
        return this.assemblyComponent;
    }

    public final Operation getOperation() {
        return this.operation;
    }

    @Override
    public String toString() {
        return  +  this.assemblyComponent.getId()+":"
                + this.operation.getId()
                + "@"+this.id + "";
    }
}
