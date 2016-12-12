/**
 */
package kieker.analysisteetime.model.analysismodel.deployment.impl;

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

	private final EReferenceIndex<String, DeploymentContext> deploymentContextsIndex;

	protected IndexedDeploymentRootImpl() {
		super();

		final EReference deploymentContextsFeature = DeploymentPackage.eINSTANCE.getDeploymentRoot_DeploymentContexts();
		// final EReference contextNameFeature = DeploymentPackage.eINSTANCE.getDeploymentContext_
		// TODO
		// this.deploymentContextsIndex = EReferenceIndex.createEmpty(this, deploymentContextsFeature, Arrays.asList(deploymentContextsFeature),
		// DeploymentContext::getName());
		this.deploymentContextsIndex = null;
	}

	@Override
	public DeploymentContext getDeploymentContextByName(final String name) {
		return this.deploymentContextsIndex.get(name);
	}

	@Override
	public boolean containsDeploymentContextByName(final String name) {
		return this.deploymentContextsIndex.contains(name);
	}

}
