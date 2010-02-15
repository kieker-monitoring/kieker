/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpan.plugins.dependencyGraph;

import kieker.tpan.datamodel.AllocationComponentInstance;
import kieker.tpan.datamodel.Execution;
import kieker.tpan.datamodel.factories.SystemEntityFactory;

/**
 *
 * @author avanhoorn
 */
public class ComponentDependencyGraph extends AbstractDependencyGraph<AllocationComponentInstance> {

    public ComponentDependencyGraph(final SystemEntityFactory systemEntityFactory){
        super (systemEntityFactory.getAllocationFactory().rootAllocationComponent.getId(),
                systemEntityFactory.getAllocationFactory().rootAllocationComponent);
    }

    public void addDependency(Execution sendingExecution, Execution receivingExecution) {
        AllocationComponentInstance senderComponent = sendingExecution.getAllocationComponent();
        AllocationComponentInstance receiverComponent = receivingExecution.getAllocationComponent();
        DependencyNode<AllocationComponentInstance> senderDependencyNode = this.getNode(senderComponent.getId());
        DependencyNode<AllocationComponentInstance> receiverDependencyNode = this.getNode(receiverComponent.getId());
        if (senderDependencyNode == null) {
            senderDependencyNode = new DependencyNode(senderComponent.getId(), senderComponent);
            this.setNode(senderDependencyNode.getId(), senderDependencyNode);
        }
        if (receiverDependencyNode == null) {
            receiverDependencyNode = new DependencyNode(receiverComponent.getId(), receiverComponent);
            this.setNode(receiverDependencyNode.getId(), receiverDependencyNode);
        }
        senderDependencyNode.addOutgoingDependency(receiverDependencyNode);
        receiverDependencyNode.addIncomingDependency(senderDependencyNode);
    }
}
