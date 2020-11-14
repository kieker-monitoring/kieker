/**
 */
package kieker.analysisteetime.model.analysismodel.deployment;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentModel#getDeploymentContexts <em>Deployment Contexts</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeploymentModel()
 * @model
 * @generated
 */
public interface DeploymentModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Deployment Contexts</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Deployment Contexts</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Deployment Contexts</em>' map.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#getDeploymentModel_DeploymentContexts()
	 * @model mapType="kieker.analysisteetime.model.analysismodel.deployment.EStringToDeploymentContextMapEntry<org.eclipse.emf.ecore.EString,
	 *        kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext>" ordered="false"
	 * @generated
	 */
	EMap<String, DeploymentContext> getDeploymentContexts();

} // DeploymentModel
