/**
 */
package kieker.analysisteetime.model.analysismodel.deployment;

public interface IndexedDeployedComponent extends DeployedComponent {

	DeployedOperation getDeployedOperationByName(String name);

	boolean containsDeployedOperationByName(String name);

}
