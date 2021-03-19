/**
 */
package kieker.model.analysismodel.execution;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Aggregated Storage Access</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.execution.AggregatedStorageAccess#getStorage <em>Storage</em>}</li>
 *   <li>{@link kieker.model.analysismodel.execution.AggregatedStorageAccess#getCode <em>Code</em>}</li>
 *   <li>{@link kieker.model.analysismodel.execution.AggregatedStorageAccess#getSources <em>Sources</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.execution.ExecutionPackage#getAggregatedStorageAccess()
 * @model
 * @generated
 */
public interface AggregatedStorageAccess extends EObject {
	/**
	 * Returns the value of the '<em><b>Storage</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Storage</em>' reference.
	 * @see #setStorage(DeployedStorage)
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getAggregatedStorageAccess_Storage()
	 * @model
	 * @generated
	 */
	DeployedStorage getStorage();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.execution.AggregatedStorageAccess#getStorage <em>Storage</em>}' reference.
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
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getAggregatedStorageAccess_Code()
	 * @model
	 * @generated
	 */
	DeployedOperation getCode();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.execution.AggregatedStorageAccess#getCode <em>Code</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Code</em>' reference.
	 * @see #getCode()
	 * @generated
	 */
	void setCode(DeployedOperation value);

	/**
	 * Returns the value of the '<em><b>Sources</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sources</em>' attribute list.
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getAggregatedStorageAccess_Sources()
	 * @model
	 * @generated
	 */
	EList<String> getSources();

} // AggregatedStorageAccess
