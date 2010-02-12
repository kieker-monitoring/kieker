package kieker.tpan.datamodel;

import java.util.Collection;
import java.util.Hashtable;

public class CallingTreeNode {

    private CallingTreeNode parent;
    private Hashtable<CallingTreeOperationHashKey, CallingTreeNode> children =
            new Hashtable<CallingTreeOperationHashKey, CallingTreeNode>();
    private final CallingTreeOperationHashKey opInfo;

    public CallingTreeNode(final CallingTreeNode parent, CallingTreeOperationHashKey opInfo) {
        this.parent = parent;
        if (opInfo == null) {
            throw new IllegalArgumentException("opInfo must not be null");
        }
        this.opInfo = opInfo;
    }

    public final Collection<CallingTreeNode> getChildren() {
        return children.values();
    }

    /** Returns the child node with given operation, name, and vmName.
     *  The node is created if it doesn't exist. */
    public final CallingTreeNode getChildForName(final String componentName,
            final String operationName, final String vmName) {
        CallingTreeOperationHashKey k =
                new CallingTreeOperationHashKey(componentName, operationName, vmName);
        CallingTreeNode node =
                this.children.get(k);
        if (node == null) {
            node = new CallingTreeNode(this, k);
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
