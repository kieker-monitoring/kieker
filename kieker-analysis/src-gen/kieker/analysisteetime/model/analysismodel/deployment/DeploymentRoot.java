/**
 */
package kieker.analysisteetime.model.analysismodel.deployment;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot#getDeploymentContexts <em>Deployment Contexts</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeploymentRoot()
 * @model
 * @generated
 */
public interface DeploymentRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Deployment Contexts</b></em>' reference list.
	 * The list contents are of type {@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext}.
	 * It is bidirectional and its opposite is '{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getDeploymentRoot <em>Deployment Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Deployment Contexts</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Deployment Contexts</em>' reference list.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeploymentRoot_DeploymentContexts()
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getDeploymentRoot
	 * @model opposite="deploymentRoot"
	 * @generated
	 */
	EList<DeploymentContext> getDeploymentContexts();

} // DeploymentRoot
