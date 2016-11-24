/**
 */
package kieker.analysisteetime.model.analysismodel.deployment;

import kieker.analysisteetime.model.analysismodel.architecture.OperationType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Deployed Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getOperationType <em>Operation Type</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getContainedComponent <em>Contained Component</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getAccesssedComponent <em>Accesssed Component</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeployedOperation()
 * @model
 * @generated
 */
public interface DeployedOperation extends EObject {
	/**
	 * Returns the value of the '<em><b>Operation Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operation Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operation Type</em>' reference.
	 * @see #setOperationType(OperationType)
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeployedOperation_OperationType()
	 * @model
	 * @generated
	 */
	OperationType getOperationType();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getOperationType <em>Operation Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operation Type</em>' reference.
	 * @see #getOperationType()
	 * @generated
	 */
	void setOperationType(OperationType value);

	/**
	 * Returns the value of the '<em><b>Contained Component</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getContainedOperations <em>Contained Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contained Component</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contained Component</em>' reference.
	 * @see #setContainedComponent(DeployedComponent)
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeployedOperation_ContainedComponent()
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getContainedOperations
	 * @model opposite="containedOperations"
	 * @generated
	 */
	DeployedComponent getContainedComponent();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getContainedComponent <em>Contained Component</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contained Component</em>' reference.
	 * @see #getContainedComponent()
	 * @generated
	 */
	void setContainedComponent(DeployedComponent value);

	/**
	 * Returns the value of the '<em><b>Accesssed Component</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getAccessedOperations <em>Accessed Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Accesssed Component</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Accesssed Component</em>' reference.
	 * @see #setAccesssedComponent(DeployedComponent)
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeployedOperation_AccesssedComponent()
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getAccessedOperations
	 * @model opposite="accessedOperations"
	 * @generated
	 */
	DeployedComponent getAccesssedComponent();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getAccesssedComponent <em>Accesssed Component</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Accesssed Component</em>' reference.
	 * @see #getAccesssedComponent()
	 * @generated
	 */
	void setAccesssedComponent(DeployedComponent value);

} // DeployedOperation
