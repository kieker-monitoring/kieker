package kieker.tpan.datamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    /** Returns the child node with given operation, name, and vmName.
     *  The node is created if it doesn't exist. */
    public final CallTreeNode getChildForName(final String componentName,
            final String operationName, final String vmName) {
        CallTreeOperationHashKey k =
                new CallTreeOperationHashKey(componentName, operationName, vmName);
        CallTreeNode node = null;
        for (CallTreeNode n : children){
         if (n.opInfo.equals(k)){
             node = n;
         }
        }
        if (node == null) {
            node = new CallTreeNode(this, k);
            this.children.add(node);
        }
        return node;
    }

    public final String getComponentName() {
        return opInfo.getComponentName();
    }

    public final String getOperationName() {
        return opInfo.getOperationName();
    }

    public final CallTreeNode getParent() {
        return parent;
    }

    public final String getVmName() {
        return this.opInfo.getVmName();
    }

    private String getShortComponentName() {
        String shortComponentName = this.getComponentName();
        if (shortComponentName.indexOf('.') != -1) {
            int index = 0;
            for (index = shortComponentName.length() - 1; index > 0; index--) {
                if (shortComponentName.charAt(index) == '.') {
                    break;
                }
            }
            shortComponentName = shortComponentName.substring(index + 1);

        }
        return shortComponentName;
    }

    /** Convenience function which returns "$" in case 'senderComponentName' field is null */
    public String getLabel(final boolean shortComponentName, final boolean includeHostname) {
        if (this.opInfo == null) {
            return "$";
        }
        StringBuilder strBuild = new StringBuilder();
        if (includeHostname) {
            strBuild.append(this.getVmName()).append("::\\n");
        }
        if (shortComponentName) {
            strBuild.append(this.getShortComponentName());
        } else {
            strBuild.append(this.getComponentName());
        }
        strBuild.append(".").append(this.getOperationName());

        return strBuild.toString();


    }

    @Override
    public String toString() {
        return this.getLabel(false, true); // false, include hostname
    }
}
