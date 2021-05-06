/**
 */
package kieker.model.analysismodel.deployment;

import kieker.model.analysismodel.assembly.AssemblyComponent;

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
 *   <li>{@link kieker.model.analysismodel.deployment.DeployedComponent#getAssemblyComponent <em>Assembly Component</em>}</li>
 *   <li>{@link kieker.model.analysismodel.deployment.DeployedComponent#getContainedOperations <em>Contained Operations</em>}</li>
 *   <li>{@link kieker.model.analysismodel.deployment.DeployedComponent#getContainedStorages <em>Contained Storages</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeployedComponent()
 * @model
 * @generated
 */
public interface DeployedComponent extends EObject {
	/**
	 * Returns the value of the '<em><b>Assembly Component</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assembly Component</em>' reference.
	 * @see #setAssemblyComponent(AssemblyComponent)
	 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeployedComponent_AssemblyComponent()
	 * @model
	 * @generated
	 */
	AssemblyComponent getAssemblyComponent();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.deployment.DeployedComponent#getAssemblyComponent <em>Assembly Component</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Assembly Component</em>' reference.
	 * @see #getAssemblyComponent()
	 * @generated
	 */
	void setAssemblyComponent(AssemblyComponent value);

	/**
	 * Returns the value of the '<em><b>Contained Operations</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.model.analysismodel.deployment.DeployedOperation},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contained Operations</em>' map.
	 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeployedComponent_ContainedOperations()
	 * @model mapType="kieker.model.analysismodel.deployment.EStringToDeployedOperationMapEntry&lt;org.eclipse.emf.ecore.EString, kieker.model.analysismodel.deployment.DeployedOperation&gt;" ordered="false"
	 * @generated
	 */
	EMap<String, DeployedOperation> getContainedOperations();

	/**
	 * Returns the value of the '<em><b>Contained Storages</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.model.analysismodel.deployment.DeployedStorage},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contained Storages</em>' map.
	 * @see kieker.model.analysismodel.deployment.DeploymentPackage#getDeployedComponent_ContainedStorages()
	 * @model mapType="kieker.model.analysismodel.deployment.EStringToDeployedStorageMapEntry&lt;org.eclipse.emf.ecore.EString, kieker.model.analysismodel.deployment.DeployedStorage&gt;" ordered="false"
	 * @generated
	 */
	EMap<String, DeployedStorage> getContainedStorages();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	DeploymentContext getDeploymentContext();

} // DeployedComponent
