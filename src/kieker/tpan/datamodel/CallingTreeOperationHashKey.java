package kieker.tpan.datamodel;

/**
 *
 * @author Andre van Hoorn
 */
public class CallingTreeOperationHashKey {
    private final String componentName;
    private final String operationName;
    private final String vmName;

    private final int hashCode; // the final is computed once and never changes

    public CallingTreeOperationHashKey(final String componentName,
            final String operationName, final String vmName) {
        this.componentName = componentName;
        this.operationName = operationName;
        this.vmName = vmName;
        this.hashCode =
                this.componentName.hashCode()
                ^ this.operationName.hashCode()
                ^ this.vmName.hashCode();
    }

    @Override
    public final int hashCode() {
        return this.hashCode;
    }

    public final boolean equals(Object o){
        if (o == this) return true;
        CallingTreeOperationHashKey k = (CallingTreeOperationHashKey)o;

        return this.componentName.equals(k.componentName)
                && this.operationName.equals(k.operationName)
                && this.vmName.equals(k.vmName);
    }

    public final String getComponentName() {
        return this.componentName;
    }

    public final String getOperationName() {
        return this.operationName;
    }

    public final String getVmName() {
        return this.vmName;
    }
}
