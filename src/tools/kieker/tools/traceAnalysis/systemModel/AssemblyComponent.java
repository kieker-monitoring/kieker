package kieker.tools.traceAnalysis.systemModel;

/**
 *
 * @author Andre van Hoorn
 */
public class AssemblyComponent {
    private final int id;
    private final String name;
    private final ComponentType type;

    private AssemblyComponent(){
        this.id = -1;
        this.name = null;
        this.type = null;
    }

    public AssemblyComponent(
            final int id, final String name,
            final ComponentType type){
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public final int getId() {
        return this.id;
    }

    public final String getName() {
        return this.name;
    }

    public final ComponentType getType() {
        return this.type;
    }

    @Override
    public final String toString(){
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(this.name).append(":")
                .append(this.type.getFullQualifiedName());
        return strBuild.toString();
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AssemblyComponent)){
            return false;
        }
        AssemblyComponent other = (AssemblyComponent)obj;
        return other.id == this.id;
    }
}
