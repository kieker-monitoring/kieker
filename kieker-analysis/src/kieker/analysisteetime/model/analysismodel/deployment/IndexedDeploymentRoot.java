/**
 */
package kieker.analysisteetime.model.analysismodel.deployment;

public interface IndexedDeploymentRoot extends DeploymentRoot {

	DeploymentContext getDeploymentContextByName(String name);

	boolean containsDeploymentContextByName(String name);
}
