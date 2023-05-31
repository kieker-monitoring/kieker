/**
 */
package kieker.model.analysismodel.execution;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Storage Dataflow</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.execution.StorageDataflow#getStorage <em>Storage</em>}</li>
 *   <li>{@link kieker.model.analysismodel.execution.StorageDataflow#getCode <em>Code</em>}</li>
 *   <li>{@link kieker.model.analysismodel.execution.StorageDataflow#getDirection <em>Direction</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.execution.ExecutionPackage#getStorageDataflow()
 * @model
 * @generated
 */
public interface StorageDataflow extends EObject {
	/**
	 * Returns the value of the '<em><b>Storage</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Storage</em>' reference.
	 * @see #setStorage(DeployedStorage)
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getStorageDataflow_Storage()
	 * @model
	 * @generated
	 */
	DeployedStorage getStorage();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.execution.StorageDataflow#getStorage <em>Storage</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Storage</em>' reference.
	 * @see #getStorage()
	 * @generated
	 */
	void setStorage(DeployedStorage value);

	/**
	 * Returns the value of the '<em><b>Code</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Code</em>' reference.
	 * @see #setCode(DeployedOperation)
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getStorageDataflow_Code()
	 * @model
	 * @generated
	 */
	DeployedOperation getCode();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.execution.StorageDataflow#getCode <em>Code</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Code</em>' reference.
	 * @see #getCode()
	 * @generated
	 */
	void setCode(DeployedOperation value);

	/**
	 * Returns the value of the '<em><b>Direction</b></em>' attribute.
	 * The literals are from the enumeration {@link kieker.model.analysismodel.execution.EDirection}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Direction</em>' attribute.
	 * @see kieker.model.analysismodel.execution.EDirection
	 * @see #setDirection(EDirection)
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getStorageDataflow_Direction()
	 * @model
	 * @generated
	 */
	EDirection getDirection();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.execution.StorageDataflow#getDirection <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Direction</em>' attribute.
	 * @see kieker.model.analysismodel.execution.EDirection
	 * @see #getDirection()
	 * @generated
	 */
	void setDirection(EDirection value);

} // StorageDataflow
