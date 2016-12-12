/**
 */
package kieker.analysisteetime.model.analysismodel.deployment.impl;

import kieker.analysisteetime.model.analysismodel.deployment.IndexedDeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.IndexedDeploymentContext;
import kieker.analysisteetime.model.analysismodel.deployment.IndexedDeploymentFactory;
import kieker.analysisteetime.model.analysismodel.deployment.IndexedDeploymentRoot;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class IndexedDeploymentFactoryImpl implements IndexedDeploymentFactory {

	public IndexedDeploymentFactoryImpl() {}

	@Override
	public IndexedDeploymentRoot createDeploymentRoot() {
		return new IndexedDeploymentRootImpl();
	}

	@Override
	public IndexedDeploymentContext createDeploymentContext() {
		return new IndexedDeploymentContextImpl();
	}

	@Override
	public IndexedDeployedComponent createDeployedComponent() {
		return new IndexedDeployedComponentImpl();
	}

}
