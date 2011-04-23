package kieker.tools.traceAnalysis.systemModel;

import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;

/**
 *
 * @author Andre van Hoorn
 */
public class Operation {
    public static final int ROOT_OPERATION_ID = AbstractSystemSubRepository.ROOT_ELEMENT_ID;
    private final int id;
    private final ComponentType componentType;
    private final Signature signature;

    private Operation (){
        this.id = -1;
        this.componentType = null;
        this.signature = null;
    }

    public Operation (final int id,
            final ComponentType componentType,
            final Signature signature){
        this.id = id;
        this.componentType = componentType;
        this.signature = signature;
    }

    public final int getId() {
        return this.id;
    }

    public final ComponentType getComponentType() {
        return this.componentType;
    }

    public final Signature getSignature() {
        return signature;
    }



    /**
     * Two Operation objects are equal iff their ids are equal.
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Operation)){
            return false;
        }
        Operation other = (Operation)obj;
        return other.id == this.id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.id;
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(this.componentType.getFullQualifiedName())
                .append(".")
                .append(this.signature.toString());
        return strBuild.toString();
    }
}
