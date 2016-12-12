/**
 */
package kieker.analysisteetime.model.analysismodel.deployment.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;

import kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage;
import kieker.analysisteetime.model.analysismodel.deployment.IndexedDeploymentRoot;
import kieker.analysisteetime.modeltooling.EReferenceIndex;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class IndexedDeploymentRootImpl extends DeploymentRootImpl implements IndexedDeploymentRoot {

	private final EReferenceIndex<String, DeploymentContext> deploymentContexts;

	protected IndexedDeploymentRootImpl() {
		super();

		final EReference deploymentContextsFeature = DeploymentPackage.eINSTANCE.getDeploymentRoot_DeploymentContexts();
		// TODO
		// final EAttribute contextNameFeature = DeploymentPackage.eINSTANCE.getDeploymentContext();
		final EAttribute contextNameFeature = null;
		// TODO
		// this.deploymentContexts = EReferenceIndex.createEmpty(this, deploymentContextsFeature, Arrays.asList(deploymentContextsFeature),
		// DeploymentContext::getName());
		this.deploymentContexts = null;
	}

	@Override
	public DeploymentContext getDeploymentContextByName(final String name) {
		return this.deploymentContexts.get(name);
	}

	@Override
	public boolean containsDeploymentContextByName(final String name) {
		return this.deploymentContexts.contains(name);
	}

}
