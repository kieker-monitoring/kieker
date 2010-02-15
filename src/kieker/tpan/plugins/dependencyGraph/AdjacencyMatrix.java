package kieker.tpan.plugins.dependencyGraph;

import java.util.Collection;
import java.util.TreeMap;
import kieker.tpan.datamodel.AllocationComponentInstance;
import kieker.tpan.datamodel.Execution;
import kieker.tpan.datamodel.factories.SystemEntityFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn, Lena St&ouml;ver
 */
public class AdjacencyMatrix {

    private static final Log LOG = LogFactory.getLog(AdjacencyMatrix.class);
    private TreeMap<Integer, DependencyNode> allocationComponentNodes =
            new TreeMap<Integer, DependencyNode>();
    private DependencyNode allocationComponentDependenciesRootNode;

    public AdjacencyMatrix(final SystemEntityFactory systemEntityFactory) {
        final AllocationComponentInstance allocationComponentRoot =
                systemEntityFactory.getAllocationFactory().rootAllocationComponent;
        allocationComponentDependenciesRootNode =
                new DependencyNode(allocationComponentRoot.getId(),
                allocationComponentRoot);
        this.allocationComponentNodes.put(allocationComponentDependenciesRootNode.getId(), allocationComponentDependenciesRootNode);
    }

    public void addDependency(Execution sendingExecution, Execution receivingExecution) {
        AllocationComponentInstance senderComponent = sendingExecution.getAllocationComponent();
        AllocationComponentInstance receiverComponent = receivingExecution.getAllocationComponent();
        DependencyNode senderDependencyNode = this.allocationComponentNodes.get(senderComponent.getId());
        DependencyNode receiverDependencyNode = this.allocationComponentNodes.get(receiverComponent.getId());
        if (senderDependencyNode == null) {
            senderDependencyNode = new DependencyNode(senderComponent.getId(), senderComponent);
            this.allocationComponentNodes.put(senderDependencyNode.getId(), senderDependencyNode);
        }
        if (receiverDependencyNode == null) {
            receiverDependencyNode = new DependencyNode(receiverComponent.getId(), receiverComponent);
            this.allocationComponentNodes.put(receiverDependencyNode.getId(), receiverDependencyNode);
        }
        senderDependencyNode.addOutgoingDependency(receiverDependencyNode);
        receiverDependencyNode.addIncomingDependency(senderDependencyNode);
    }

    public final DependencyNode getAllocationComponentDependenciesRootNode() {
        return this.allocationComponentDependenciesRootNode;
    }

    public Collection<DependencyNode> getAllocationComponentNodes() {
        return allocationComponentNodes.values();
    }
}
