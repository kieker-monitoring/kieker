/**
 */
package kieker.analysisteetime.model.analysismodel.deployment;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public interface IndexedDeploymentContext extends DeploymentContext {

	DeployedComponent getDeployedComponentByName(String name);

	boolean containsDeployedComponentByName(String name);
}
