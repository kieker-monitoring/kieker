package kieker.tpan.plugins.callTreePlugin;

import kieker.tpan.datamodel.AllocationComponent;
import kieker.tpan.datamodel.Operation;

/**
 *
 * @author Andre van Hoorn
 */
public class CallTreeOperationHashKey {
    private final AllocationComponent allocationComponent;
    private final Operation operation;

    private final int hashCode; // the final is computed once and never changes

    public CallTreeOperationHashKey(final AllocationComponent allocationComponent,
            final Operation operation) {
        this.allocationComponent = allocationComponent;
        this.operation = operation;
        this.hashCode =
                this.allocationComponent.hashCode()
                ^ this.operation.hashCode();
    }

    @Override
    public final int hashCode() {
        return this.hashCode;
    }

    @Override
    public final boolean equals(Object o){
        if (o == this) return true;
        if (! (o instanceof CallTreeOperationHashKey)) return false;
        CallTreeOperationHashKey k = (CallTreeOperationHashKey)o;

        return this.allocationComponent.equals(k.allocationComponent)
                && this.operation.equals(k.operation);
    }

    public final AllocationComponent getAllocationComponent() {
        return this.allocationComponent;
    }

    public final Operation getOperation() {
        return this.operation;
    }
}
