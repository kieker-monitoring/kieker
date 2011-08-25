package kieker.tools.traceAnalysis.systemModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Andre van Hoorn
 */
public class ExecutionContainer {
    private final int id;
    private final String name;
    private final ExecutionContainer parent;
    private final Collection<ExecutionContainer> childContainers = new ArrayList<ExecutionContainer>();

    @SuppressWarnings("unused")
	private ExecutionContainer(){
        this.id = -1;
        this.parent = null;
        this.name = null; }

    public ExecutionContainer(final int id,
            final ExecutionContainer parent,
            final String name){
        this.id = id;
        this.name = name;
        this.parent = parent;
    }

    public final int getId() {
        return this.id;
    }

    public final String getName() {
        return this.name;
    }

    public final ExecutionContainer getParent() {
        return this.parent;
    }

    public final Collection<ExecutionContainer> getChildContainers() {
        return this.childContainers;
    }

    public final void addChildContainer(final ExecutionContainer container){
        this.childContainers.add(this);
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof ExecutionContainer)){
            return false;
        }
        final ExecutionContainer other = (ExecutionContainer)obj;
        return other.id == this.id;
    }
}
