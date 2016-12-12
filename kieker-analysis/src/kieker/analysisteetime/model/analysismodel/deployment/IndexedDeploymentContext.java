/**
 */
package kieker.analysisteetime.model.analysismodel.deployment;

public interface IndexedDeploymentContext extends DeploymentContext {

	DeployedComponent getDeployedComponentByName(String name);

	boolean containsDeployedComponentByName(String name);
}
