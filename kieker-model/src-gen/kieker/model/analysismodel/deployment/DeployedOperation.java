/**
 */
package kieker.model.analysismodel.deployment;

import org.eclipse.emf.ecore.EObject;

import kieker.model.analysismodel.assembly.AssemblyOperation;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Deployed Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.deployment.DeployedOperation#getAssemblyOperation <em>Assembly Operation</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeployedOperation()
 * @model
 * @generated
 */
public interface DeployedOperation extends EObject {
	/**
	 * Returns the value of the '<em><b>Assembly Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assembly Operation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assembly Operation</em>' reference.
	 * @see #setAssemblyOperation(AssemblyOperation)
	 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeployedOperation_AssemblyOperation()
	 * @model
	 * @generated
	 */
	AssemblyOperation getAssemblyOperation();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.deployment.DeployedOperation#getAssemblyOperation <em>Assembly Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Assembly Operation</em>' reference.
	 * @see #getAssemblyOperation()
	 * @generated
	 */
	void setAssemblyOperation(AssemblyOperation value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	DeployedComponent getComponent();

} // DeployedOperation
