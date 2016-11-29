/**
 */
package kieker.analysisteetime.model.analysismodel.deployment;

import kieker.analysisteetime.model.analysismodel.architecture.ComponentType;

import org.eclipse.emf.common.util.EList;

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
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getComponentType <em>Component Type</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getDeploymentContext <em>Deployment Context</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getContainedOperations <em>Contained Operations</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getAccessedOperations <em>Accessed Operations</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeployedComponent()
 * @model
 * @generated
 */
public interface DeployedComponent extends EObject {
	/**
	 * Returns the value of the '<em><b>Component Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Component Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component Type</em>' reference.
	 * @see #setComponentType(ComponentType)
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeployedComponent_ComponentType()
	 * @model
	 * @generated
	 */
	ComponentType getComponentType();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getComponentType <em>Component Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Component Type</em>' reference.
	 * @see #getComponentType()
	 * @generated
	 */
	void setComponentType(ComponentType value);

	/**
	 * Returns the value of the '<em><b>Deployment Context</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getComponents <em>Components</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Deployment Context</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Deployment Context</em>' container reference.
	 * @see #setDeploymentContext(DeploymentContext)
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeployedComponent_DeploymentContext()
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getComponents
	 * @model opposite="components" transient="false"
	 * @generated
	 */
	DeploymentContext getDeploymentContext();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getDeploymentContext <em>Deployment Context</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Deployment Context</em>' container reference.
	 * @see #getDeploymentContext()
	 * @generated
	 */
	void setDeploymentContext(DeploymentContext value);

	/**
	 * Returns the value of the '<em><b>Contained Operations</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation}.
	 * It is bidirectional and its opposite is '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getContainedComponent <em>Contained Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contained Operations</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contained Operations</em>' containment reference list.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeployedComponent_ContainedOperations()
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getContainedComponent
	 * @model opposite="containedComponent" containment="true" ordered="false"
	 * @generated
	 */
	EList<DeployedOperation> getContainedOperations();

	/**
	 * Returns the value of the '<em><b>Accessed Operations</b></em>' reference list.
	 * The list contents are of type {@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation}.
	 * It is bidirectional and its opposite is '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getAccesssedComponent <em>Accesssed Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Accessed Operations</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Accessed Operations</em>' reference list.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeployedComponent_AccessedOperations()
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getAccesssedComponent
	 * @model opposite="accesssedComponent"
	 * @generated
	 */
	EList<DeployedOperation> getAccessedOperations();

} // DeployedComponent
