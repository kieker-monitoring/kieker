/**
 */
package kieker.model.analysismodel.deployment;

import kieker.model.analysismodel.assembly.AssemblyProvidedInterface;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Deployed Provided Interface</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.deployment.DeployedProvidedInterface#getProvidedOperations <em>Provided Operations</em>}</li>
 *   <li>{@link kieker.model.analysismodel.deployment.DeployedProvidedInterface#getProvidedInterface <em>Provided Interface</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeployedProvidedInterface()
 * @model
 * @generated
 */
public interface DeployedProvidedInterface extends EObject {
	/**
	 * Returns the value of the '<em><b>Provided Operations</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.model.analysismodel.deployment.DeployedOperation},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provided Operations</em>' map.
	 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeployedProvidedInterface_ProvidedOperations()
	 * @model mapType="kieker.model.analysismodel.deployment.EStringToInterfaceDeployedOperationMapEntry&lt;org.eclipse.emf.ecore.EString, kieker.model.analysismodel.deployment.DeployedOperation&gt;"
	 * @generated
	 */
	EMap<String, DeployedOperation> getProvidedOperations();

	/**
	 * Returns the value of the '<em><b>Provided Interface</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provided Interface</em>' reference.
	 * @see #setProvidedInterface(AssemblyProvidedInterface)
	 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeployedProvidedInterface_ProvidedInterface()
	 * @model
	 * @generated
	 */
	AssemblyProvidedInterface getProvidedInterface();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.deployment.DeployedProvidedInterface#getProvidedInterface <em>Provided Interface</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Provided Interface</em>' reference.
	 * @see #getProvidedInterface()
	 * @generated
	 */
	void setProvidedInterface(AssemblyProvidedInterface value);

} // DeployedProvidedInterface
