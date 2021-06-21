/**
 */
package kieker.model.analysismodel.deployment;

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
 *   <li>{@link kieker.model.analysismodel.deployment.DeploymentModel#getDeploymentContexts <em>Deployment Contexts</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeploymentModel()
 * @model
 * @generated
 */
public interface DeploymentModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Deployment Contexts</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.model.analysismodel.deployment.DeploymentContext},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Deployment Contexts</em>' map.
	 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeploymentModel_DeploymentContexts()
	 * @model mapType="kieker.model.analysismodel.deployment.EStringToDeploymentContextMapEntry&lt;org.eclipse.emf.ecore.EString, kieker.model.analysismodel.deployment.DeploymentContext&gt;" ordered="false"
	 * @generated
	 */
	EMap<String, DeploymentContext> getDeploymentContexts();

} // DeploymentModel
