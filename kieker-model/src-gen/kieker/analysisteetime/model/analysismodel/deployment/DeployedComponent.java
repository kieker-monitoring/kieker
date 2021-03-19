/**
 */
package kieker.analysisteetime.model.analysismodel.deployment;

import kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Deployed Component</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getAssemblyComponent <em>Assembly Component</em>}</li>
 * <li>{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getContainedOperations <em>Contained Operations</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeployedComponent()
 * @model
 * @generated
 */
public interface DeployedComponent extends EObject {
	/**
	 * Returns the value of the '<em><b>Assembly Component</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assembly Component</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Assembly Component</em>' reference.
	 * @see #setAssemblyComponent(AssemblyComponent)
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeployedComponent_AssemblyComponent()
	 * @model
	 * @generated
	 */
	AssemblyComponent getAssemblyComponent();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getAssemblyComponent <em>Assembly Component</em>}'
	 * reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Assembly Component</em>' reference.
	 * @see #getAssemblyComponent()
	 * @generated
	 */
	void setAssemblyComponent(AssemblyComponent value);

	/**
	 * Returns the value of the '<em><b>Contained Operations</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contained Operations</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Contained Operations</em>' map.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeployedComponent_ContainedOperations()
	 * @model mapType="kieker.analysisteetime.model.analysismodel.deployment.EStringToDeployedOperationMapEntry<org.eclipse.emf.ecore.EString,
	 *        kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation>" ordered="false"
	 * @generated
	 */
	EMap<String, DeployedOperation> getContainedOperations();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='org.eclipse.emf.ecore.EObject container = this.eContainer();\r\nif (container != null)
	 *        {\r\n\torg.eclipse.emf.ecore.EObject containerContainer = container.eContainer();\r\n\tif (containerContainer != null) {\r\n\t\treturn
	 *        (DeploymentContext) containerContainer ;\r\n\t}\r\n}\r\nreturn null;\r\n'"
	 * @generated
	 */
	DeploymentContext getDeploymentContext();

} // DeployedComponent
