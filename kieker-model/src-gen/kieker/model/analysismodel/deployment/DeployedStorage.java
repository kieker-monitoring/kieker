/**
 */
package kieker.model.analysismodel.deployment;

import kieker.model.analysismodel.assembly.AssemblyStorage;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Deployed Storage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.deployment.DeployedStorage#getAssemblyOperation <em>Assembly Operation</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeployedStorage()
 * @model
 * @generated
 */
public interface DeployedStorage extends EObject {
	/**
	 * Returns the value of the '<em><b>Assembly Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assembly Operation</em>' reference.
	 * @see #setAssemblyOperation(AssemblyStorage)
	 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeployedStorage_AssemblyOperation()
	 * @model
	 * @generated
	 */
	AssemblyStorage getAssemblyOperation();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.deployment.DeployedStorage#getAssemblyOperation <em>Assembly Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Assembly Operation</em>' reference.
	 * @see #getAssemblyOperation()
	 * @generated
	 */
	void setAssemblyOperation(AssemblyStorage value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	DeployedComponent getComponent();

} // DeployedStorage
