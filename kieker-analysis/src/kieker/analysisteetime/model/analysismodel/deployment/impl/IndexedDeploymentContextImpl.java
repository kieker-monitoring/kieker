/**
 */
package kieker.analysisteetime.model.analysismodel.deployment.impl;

import java.util.Arrays;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;

import kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage;
import kieker.analysisteetime.model.analysismodel.deployment.IndexedDeploymentContext;
import kieker.analysisteetime.modeltooling.EReferenceIndex;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class IndexedDeploymentContextImpl extends DeploymentContextImpl implements IndexedDeploymentContext {

	private final EReferenceIndex<String, DeployedComponent> componentsIndex;

	protected IndexedDeploymentContextImpl() {
		super();

		final EReference componentsFeature = DeploymentPackage.eINSTANCE.getDeploymentContext_Components();
		// TODO
		// final EReference componentTypeFeature = DeploymentPackage.eINSTANCE.getDeployedComponent_ComponentType();
		final EAttribute componentTypeFeature = null;
		this.componentsIndex = EReferenceIndex.createEmpty(this, componentsFeature, Arrays.asList(componentTypeFeature), c -> c.getComponentType().getSignature());
	}

	@Override
	public DeployedComponent getDeployedComponentByName(final String name) {
		return this.componentsIndex.get(name);
	}

	@Override
	public boolean containsDeployedComponentByName(final String name) {
		return this.componentsIndex.contains(name);
	}

}
