package kieker.tpan.plugins.callTreePlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kieker.tpan.datamodel.AllocationComponent;
import kieker.tpan.datamodel.Operation;

public class CallTreeNode {

    private CallTreeNode parent;
    private List<CallTreeNode> children =
            new ArrayList<CallTreeNode>();
    private final CallTreeOperationHashKey opInfo;

    public CallTreeNode(final CallTreeNode parent, CallTreeOperationHashKey opInfo) {
        this.parent = parent;
        if (opInfo == null) {
            throw new IllegalArgumentException("opInfo must not be null");
        }
        this.opInfo = opInfo;
    }

    public final Collection<CallTreeNode> getChildren() {
        return children;
    }

    /** Creates a new child and adds it to the nodes list of children */
    public final CallTreeNode createNewChild(final AllocationComponent allocationComponent,
            final Operation operation) {
        CallTreeOperationHashKey k = new CallTreeOperationHashKey(allocationComponent, operation);
        CallTreeNode node = new CallTreeNode(this, k);
        this.children.add(node);
        return node;
    }

    /** Returns the child node with given operation, name, and vmName.
     *  The node is created if it doesn't exist. */
    public final CallTreeNode getChild(final AllocationComponent allocationComponent,
            final Operation operation) {
        CallTreeOperationHashKey k =
                new CallTreeOperationHashKey(allocationComponent, operation);
        CallTreeNode node = null;
        for (CallTreeNode n : children) {
            if (n.opInfo.equals(k)) {
                node = n;
            }
        }
        if (node == null) {
            node = new CallTreeNode(this, k);
            this.children.add(node);
        }
        return node;
    }

    public final AllocationComponent getAllocationComponent() {
        return opInfo.getAllocationComponent();
    }

    public final Operation getOperation() {
        return opInfo.getOperation();
    }

    public final CallTreeNode getParent() {
        return parent;
    }

    public final boolean isRootNode(){
        return this.parent == null;
    }
}
