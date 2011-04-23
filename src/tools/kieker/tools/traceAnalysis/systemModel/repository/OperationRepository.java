package kieker.tools.traceAnalysis.systemModel.repository;

import java.util.Collection;
import java.util.Hashtable;
import kieker.tools.traceAnalysis.systemModel.ComponentType;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.Signature;

/**
 *
 * @author Andre van Hoorn
 */
public class OperationRepository extends AbstractSystemSubRepository {
    private final Hashtable<String, Operation> operationsByName =
            new Hashtable<String, Operation>();
    private final Hashtable<Integer, Operation> operationsById =
            new Hashtable<Integer, Operation>();

    public final Operation rootOperation;

    public OperationRepository(final SystemModelRepository systemFactory,
            final Operation rootOperation){
        super(systemFactory);
        this.rootOperation = rootOperation;
    }

    /** Returns the instance for the passed namedIdentifier; null if no instance
     *  with this namedIdentifier.
     */
    public final Operation lookupOperationByNamedIdentifier(final String namedIdentifier){
        return this.operationsByName.get(namedIdentifier);
    }

    public final Operation createAndRegisterOperation(
            final String namedIdentifier,
            final ComponentType componentType,
            final Signature signature){
            Operation newInst;
            if (this.operationsByName.containsKey(namedIdentifier)){
                throw new IllegalArgumentException("Element with name " + namedIdentifier + "exists already");
            }
            int id = this.getAndIncrementNextId();
            newInst = new Operation(id,
                    componentType, signature);
            this.operationsById.put(id, newInst);
            this.operationsByName.put(namedIdentifier, newInst);
            return newInst;
    }

    public final Collection<Operation> getOperations(){
        return this.operationsById.values();
    }
}
