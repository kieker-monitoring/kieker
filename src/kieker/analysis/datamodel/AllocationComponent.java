package kieker.analysis.datamodel;

/**
 *
 * @author Andre van Hoorn
 */
public class AllocationComponent {


    private final int id;
    private final AssemblyComponent assemblyComponent;
    private final ExecutionContainer executionContainer;

    private AllocationComponent(){
        this.id = -1;
        this.assemblyComponent = null;
        this.executionContainer = null;
    }

    public AllocationComponent(
            final int id,
            final AssemblyComponent assemblyComponent,
            final ExecutionContainer executionContainer){
        this.id = id;
        this.assemblyComponent = assemblyComponent;
        this.executionContainer = executionContainer;
    }

    public final int getId() {
        return this.id;
    }

    public final AssemblyComponent getAssemblyComponent() {
        return this.assemblyComponent;
    }

    public final ExecutionContainer getExecutionContainer() {
        return this.executionContainer;
    }

    @Override
    public final String toString(){
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(this.executionContainer.getName())
                .append("::").append(this.assemblyComponent.toString());
        return strBuild.toString();
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AllocationComponent)){
            return false;
        }
        AllocationComponent other = (AllocationComponent)obj;
        return other.id == this.id;
    }
}
