/**
 */
package kieker.model.analysismodel.deployment;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 *
 * @see kieker.model.analysismodel.deployment.DeploymentPackage
 * @generated
 */
public interface DeploymentFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	DeploymentFactory eINSTANCE = kieker.model.analysismodel.deployment.impl.DeploymentFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	DeploymentModel createDeploymentModel();

	/**
	 * Returns a new object of class '<em>Context</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Context</em>'.
	 * @generated
	 */
	DeploymentContext createDeploymentContext();

	/**
	 * Returns a new object of class '<em>Deployed Component</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Deployed Component</em>'.
	 * @generated
	 */
	DeployedComponent createDeployedComponent();

	/**
	 * Returns a new object of class '<em>Deployed Operation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Deployed Operation</em>'.
	 * @generated
	 */
	DeployedOperation createDeployedOperation();

	/**
	 * Returns a new object of class '<em>Deployed Storage</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Deployed Storage</em>'.
	 * @generated
	 */
	DeployedStorage createDeployedStorage();

	/**
	 * Returns a new object of class '<em>Deployed Provided Interface</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Deployed Provided Interface</em>'.
	 * @generated
	 */
	DeployedProvidedInterface createDeployedProvidedInterface();

	/**
	 * Returns a new object of class '<em>Deployed Required Interface</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Deployed Required Interface</em>'.
	 * @generated
	 */
	DeployedRequiredInterface createDeployedRequiredInterface();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the package supported by this factory.
	 * @generated
	 */
	DeploymentPackage getDeploymentPackage();

} // DeploymentFactory
