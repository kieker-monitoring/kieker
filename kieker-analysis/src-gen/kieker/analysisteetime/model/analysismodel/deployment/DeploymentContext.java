/**
 */
package kieker.analysisteetime.model.analysismodel.deployment;

import org.eclipse.emf.common.util.EList;

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
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getDeploymentRoot <em>Deployment Root</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getComponents <em>Components</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeploymentContext()
 * @model
 * @generated
 */
public interface DeploymentContext extends EObject {
	/**
	 * Returns the value of the '<em><b>Deployment Root</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot#getDeploymentContexts <em>Deployment Contexts</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Deployment Root</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Deployment Root</em>' container reference.
	 * @see #setDeploymentRoot(DeploymentRoot)
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeploymentContext_DeploymentRoot()
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot#getDeploymentContexts
	 * @model opposite="deploymentContexts" transient="false"
	 * @generated
	 */
	DeploymentRoot getDeploymentRoot();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getDeploymentRoot <em>Deployment Root</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Deployment Root</em>' container reference.
	 * @see #getDeploymentRoot()
	 * @generated
	 */
	void setDeploymentRoot(DeploymentRoot value);

	/**
	 * Returns the value of the '<em><b>Components</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent}.
	 * It is bidirectional and its opposite is '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getDeploymentContext <em>Deployment Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Components</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Components</em>' containment reference list.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeploymentContext_Components()
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getDeploymentContext
	 * @model opposite="deploymentContext" containment="true" ordered="false"
	 * @generated
	 */
	EList<DeployedComponent> getComponents();

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
