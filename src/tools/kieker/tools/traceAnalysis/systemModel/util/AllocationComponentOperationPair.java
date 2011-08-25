package kieker.tools.traceAnalysis.systemModel.util;

import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.Operation;

/**
 *
 * @author Andre van Hoorn
 */
public class AllocationComponentOperationPair {
    private final int id;
    private final Operation operation;

    private final AllocationComponent allocationComponent;

    @SuppressWarnings("unused")
	private AllocationComponentOperationPair (){
        this.id = -1;
        this.operation = null;
        this.allocationComponent = null;
    }

    public AllocationComponentOperationPair (
            final int id, final Operation operation, final AllocationComponent allocationComponent){
        this.id = id;
        this.operation = operation;
        this.allocationComponent = allocationComponent;
    }

    public final int getId() {
        return this.id;
    }

    public final AllocationComponent getAllocationComponent() {
        return this.allocationComponent;
    }

    public final Operation getOperation() {
        return this.operation;
    }

    @Override
    public String toString() {
        return  +  this.allocationComponent.getId()+":"
                + this.operation.getId()
                + "@"+this.id + "";
    }
}
