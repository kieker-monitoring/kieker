/**
 */
package kieker.model.analysismodel.deployment;

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
 * <li>{@link kieker.model.analysismodel.deployment.DeploymentContext#getName <em>Name</em>}</li>
 * <li>{@link kieker.model.analysismodel.deployment.DeploymentContext#getComponents <em>Components</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeploymentContext()
 * @model
 * @generated
 */
public interface DeploymentContext extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeploymentContext_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.deployment.DeploymentContext#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Components</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.model.analysismodel.deployment.DeployedComponent},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Components</em>' map.
	 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeploymentContext_Components()
	 * @model mapType="kieker.model.analysismodel.deployment.EStringToDeployedComponentMapEntry&lt;org.eclipse.emf.ecore.EString,
	 *        kieker.model.analysismodel.deployment.DeployedComponent&gt;" ordered="false"
	 * @generated
	 */
	EMap<String, DeployedComponent> getComponents();

} // DeploymentContext
