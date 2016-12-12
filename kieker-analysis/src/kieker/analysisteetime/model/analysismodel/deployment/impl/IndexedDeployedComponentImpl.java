/**
 */
package kieker.analysisteetime.model.analysismodel.deployment.impl;

import java.util.Arrays;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;

import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage;
import kieker.analysisteetime.model.analysismodel.deployment.IndexedDeployedComponent;
import kieker.analysisteetime.modeltooling.EReferenceIndex;

public class IndexedDeployedComponentImpl extends DeployedComponentImpl implements IndexedDeployedComponent {

	private final EReferenceIndex<String, DeployedOperation> containedOperationsIndex;

	protected IndexedDeployedComponentImpl() {
		super();

		final EReference containedOperationsFeature = DeploymentPackage.eINSTANCE.getDeployedComponent_ContainedOperations();
		// TODO
		// final EReference operationTypeFeature = DeploymentPackage.eINSTANCE.getDeployedOperation_OperationType();
		final EAttribute operationTypeFeature = null;
		this.containedOperationsIndex = EReferenceIndex.createEmpty(this, containedOperationsFeature, Arrays.asList(operationTypeFeature),
				o -> o.getOperationType().getSignature());
	}

	@Override
	public DeployedOperation getDeployedOperationByName(final String name) {
		return this.containedOperationsIndex.get(name);
	}

	@Override
	public boolean containsDeployedOperationByName(final String name) {
		return this.containedOperationsIndex.contains(name);
	}

}
