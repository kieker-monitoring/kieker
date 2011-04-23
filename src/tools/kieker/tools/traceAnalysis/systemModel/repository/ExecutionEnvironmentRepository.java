package kieker.tools.traceAnalysis.systemModel.repository;

import java.util.Collection;
import java.util.Hashtable;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;

/**
 *
 * @author Andre van Hoorn
 */
public class ExecutionEnvironmentRepository extends AbstractSystemSubRepository {
    private final Hashtable<String, ExecutionContainer> executionContainersByName =
            new Hashtable<String, ExecutionContainer>();
    private final Hashtable<Integer, ExecutionContainer> executionContainersById =
            new Hashtable<Integer, ExecutionContainer>();

    public final ExecutionContainer rootExecutionContainer;

    public ExecutionEnvironmentRepository(final SystemModelRepository systemFactory,
            final ExecutionContainer rootExecutionContainer){
        super(systemFactory);
        this.rootExecutionContainer = rootExecutionContainer;
        //this.executionContainersById.put(rootExecutionContainer.getId(),
        //        rootExecutionContainer);
    }

   /** Returns the instance for the passed namedIdentifier; null if no instance
     *  with this namedIdentifier.
     */
    public final ExecutionContainer lookupExecutionContainerByNamedIdentifier(
            final String namedIdentifier){
        return this.executionContainersByName.get(namedIdentifier);
    }

   /** Returns the instance for the passed container ID; null if no instance
     *  with this ID.
     */
    public final ExecutionContainer lookupExecutionContainerByContainerId(
            final int containerId){
        return this.executionContainersById.get(containerId);
    }

    public final ExecutionContainer createAndRegisterExecutionContainer(
            final String namedIdentifier,
            final String name){
            ExecutionContainer newInst;
            if (this.executionContainersByName.containsKey(namedIdentifier)){
                throw new IllegalArgumentException("Element with name " + namedIdentifier + "exists already");
            }
            int id = this.getAndIncrementNextId();
            newInst = new ExecutionContainer(id, null, name);
            this.executionContainersById.put(id, newInst);
            this.executionContainersByName.put(namedIdentifier, newInst);
            return newInst;
    }

    public final Collection<ExecutionContainer> getExecutionContainers(){
        return this.executionContainersById.values();
    }
}
