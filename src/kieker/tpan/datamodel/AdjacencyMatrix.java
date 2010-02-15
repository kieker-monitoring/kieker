package kieker.tpan.datamodel;

import java.util.Collection;
import java.util.TreeMap;
import kieker.tpan.datamodel.system.AllocationComponentInstance;
import kieker.tpan.datamodel.system.Execution;
import kieker.tpan.datamodel.system.factories.SystemEntityFactory;
import kieker.tpan.plugins.dependencyGraph.AllocationComponentDependencyNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn, Lena St&ouml;ver
 */
public class AdjacencyMatrix {

    private static final Log LOG = LogFactory.getLog(AdjacencyMatrix.class);
    private TreeMap<Integer, AllocationComponentDependencyNode> allocationComponentNodes =
            new TreeMap<Integer, AllocationComponentDependencyNode>();
    private AllocationComponentDependencyNode allocationComponentDependenciesRootNode;

    public AdjacencyMatrix(final SystemEntityFactory systemEntityFactory) {
        final AllocationComponentInstance allocationComponentRoot =
                systemEntityFactory.getAllocationFactory().rootAllocationComponent;
        allocationComponentDependenciesRootNode =
                new AllocationComponentDependencyNode(allocationComponentRoot.getId(),
                allocationComponentRoot);
        this.allocationComponentNodes.put(allocationComponentDependenciesRootNode.getId(), allocationComponentDependenciesRootNode);
    }

    public void addDependency(Execution sendingExecution, Execution receivingExecution) {
        AllocationComponentInstance senderComponent = sendingExecution.getAllocationComponent();
        AllocationComponentInstance receiverComponent = receivingExecution.getAllocationComponent();
        AllocationComponentDependencyNode senderDependencyNode = this.allocationComponentNodes.get(senderComponent.getId());
        AllocationComponentDependencyNode receiverDependencyNode = this.allocationComponentNodes.get(receiverComponent.getId());
        if (senderDependencyNode == null) {
            senderDependencyNode = new AllocationComponentDependencyNode(senderComponent.getId(), senderComponent);
            this.allocationComponentNodes.put(senderDependencyNode.getId(), senderDependencyNode);
        }
        if (receiverDependencyNode == null) {
            receiverDependencyNode = new AllocationComponentDependencyNode(receiverComponent.getId(), receiverComponent);
            this.allocationComponentNodes.put(receiverDependencyNode.getId(), receiverDependencyNode);
        }
        senderDependencyNode.addOutgoingDependency(receiverDependencyNode);
        receiverDependencyNode.addIncomingDependency(senderDependencyNode);
    }

    public final AllocationComponentDependencyNode getAllocationComponentDependenciesRootNode() {
        return this.allocationComponentDependenciesRootNode;
    }

    public Collection<AllocationComponentDependencyNode> getAllocationComponentNodes() {
        return allocationComponentNodes.values();
    }
}
