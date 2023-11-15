/**
 */
package kieker.model.analysismodel.deployment;

import org.eclipse.emf.ecore.EObject;

import kieker.model.analysismodel.assembly.AssemblyStorage;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Deployed Storage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.deployment.DeployedStorage#getAssemblyStorage <em>Assembly Storage</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeployedStorage()
 * @model
 * @generated
 */
public interface DeployedStorage extends EObject {
	/**
	 * Returns the value of the '<em><b>Assembly Storage</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Assembly Storage</em>' reference.
	 * @see #setAssemblyStorage(AssemblyStorage)
	 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeployedStorage_AssemblyStorage()
	 * @model
	 * @generated
	 */
	AssemblyStorage getAssemblyStorage();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.deployment.DeployedStorage#getAssemblyStorage <em>Assembly Storage</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Assembly Storage</em>' reference.
	 * @see #getAssemblyStorage()
	 * @generated
	 */
	void setAssemblyStorage(AssemblyStorage value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @model kind="operation"
	 * @generated
	 */
	DeployedComponent getComponent();

} // DeployedStorage
