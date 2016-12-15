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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='org.eclipse.emf.ecore.EObject container = this.eContainer();\r\nif (container != null) {\r\n\torg.eclipse.emf.ecore.EObject containerContainer = container.eContainer();\r\n\tif (containerContainer != null) {\r\n\t\treturn (DeployedComponent) containerContainer ;\r\n\t}\r\n}\r\nreturn null;\r\n'"
	 * @generated
	 */
	DeployedComponent getComponent();

} // DeployedOperation
