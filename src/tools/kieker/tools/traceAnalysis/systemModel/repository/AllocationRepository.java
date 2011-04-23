package kieker.tools.traceAnalysis.systemModel.repository;

import java.util.Collection;
import java.util.Hashtable;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;

/**
 *
 * @author Andre van Hoorn
 */
public class AllocationRepository extends AbstractSystemSubRepository {
    private final Hashtable<String, AllocationComponent>
            allocationComponentInstancesByName =
            new Hashtable<String, AllocationComponent>();
    private final Hashtable<Integer, AllocationComponent>
            allocationComponentInstancesById = new Hashtable<Integer, AllocationComponent>();

    public final AllocationComponent rootAllocationComponent;

    public AllocationRepository(final SystemModelRepository systemFactory,
            final AllocationComponent rootAllocationComponent){
        super(systemFactory);
        this.rootAllocationComponent = rootAllocationComponent;
    }

    /** Returns the instance for the passed factoryIdentifier; null if no instance
     *  with this factoryIdentifier.
     */
    public final AllocationComponent lookupAllocationComponentInstanceByNamedIdentifier(final String namedIdentifier){
        return this.allocationComponentInstancesByName.get(namedIdentifier);
    }

    public final AllocationComponent createAndRegisterAllocationComponentInstance(
            final String namedIdentifier,
            final AssemblyComponent assemblyComponentInstance,
            final ExecutionContainer executionContainer){
            AllocationComponent newInst;
            if (this.allocationComponentInstancesByName.containsKey(namedIdentifier)){
                throw new IllegalArgumentException("Element with name " + namedIdentifier + "exists already");
            }
            int id = this.getAndIncrementNextId();
            newInst = new AllocationComponent(id,
                    assemblyComponentInstance, executionContainer);
            this.allocationComponentInstancesById.put(id, newInst);
            this.allocationComponentInstancesByName.put(namedIdentifier, newInst);
            return newInst;
    }

    public final Collection<AllocationComponent> getAllocationComponentInstances(){
        return this.allocationComponentInstancesById.values();
    }
}
