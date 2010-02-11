package kieker.tpan.datamodel;

import java.util.Collection;
import java.util.Hashtable;

public class CallingTreeNode {

    private CallingTreeNode parent;
    private Hashtable<CallingTreeOperationHashKey, CallingTreeNode> children =
            new Hashtable<CallingTreeOperationHashKey, CallingTreeNode>();

    private CallingTreeOperationHashKey opInfo;

    public CallingTreeNode(final CallingTreeNode parent) {
        this.parent = parent;
    }

    public final Collection<CallingTreeNode> getChildren() {
        return children.values();
    }

    /** Returns the child node with given operation, name, and vmName.
     *  The node is created if it doesn't exist. */
    public final CallingTreeNode getChildForName(final String componentName,
            final String operationName, final String vmName){
        CallingTreeOperationHashKey k = new CallingTreeOperationHashKey(componentName, operationName, vmName);
        CallingTreeNode node = 
                this.children.get(k);
        if (node == null){
            node = new CallingTreeNode(this);
            this.children.put(k, node);
        }
        return node;
    }

    public final String getComponentName() {
        return opInfo.getComponentName();
    }

    public final String getOperationName() {
        return opInfo.getOperationName();
    }

    public final CallingTreeNode getParent() {
        return parent;
    }

    public final String getVmName() {
        return this.opInfo.getVmName();
    }
}
