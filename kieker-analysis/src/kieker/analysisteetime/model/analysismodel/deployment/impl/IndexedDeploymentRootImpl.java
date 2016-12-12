/**
 */
package kieker.analysisteetime.model.analysismodel.deployment.impl;

import java.util.Arrays;

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

	private final EReferenceIndex<String, DeploymentContext> deploymentContextsIndex;

	protected IndexedDeploymentRootImpl() {
		super();

		final EReference deploymentContextsFeature = DeploymentPackage.eINSTANCE.getDeploymentRoot_DeploymentContexts();
		final EAttribute contextNameFeature = DeploymentPackage.eINSTANCE.getDeploymentContext_Name();
		this.deploymentContextsIndex = EReferenceIndex.createEmpty(this, deploymentContextsFeature, Arrays.asList(contextNameFeature), c -> c.getName());
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
