/**
 */
package kieker.analysisteetime.model.analysismodel.deployment;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Context</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getName <em>Name</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getComponents <em>Components</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeploymentContext()
 * @model
 * @generated
 */
public interface DeploymentContext extends EObject {
	/**
	 * Returns the value of the '<em><b>Components</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Components</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Components</em>' map.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeploymentContext_Components()
	 * @model mapType="kieker.analysisteetime.model.analysismodel.deployment.EStringToDeployedOperationMapEntry<org.eclipse.emf.ecore.EString, kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation>" ordered="false"
	 * @generated
	 */
	EMap<String, DeployedOperation> getComponents();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='org.eclipse.emf.ecore.EObject container = this.eContainer();\r\nif (container != null) {\r\n\torg.eclipse.emf.ecore.EObject containerContainer = container.eContainer();\r\n\tif (containerContainer != null) {\r\n\t\treturn (DeploymentRoot) containerContainer ;\r\n\t}\r\n}\r\nreturn null;\r\n'"
	 * @generated
	 */
	DeploymentRoot getDeploymentRoot();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeploymentContext_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // DeploymentContext
