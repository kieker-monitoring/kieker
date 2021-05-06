/**
 */
package kieker.model.analysismodel.assembly;

import kieker.model.analysismodel.type.StorageType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Storage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.assembly.AssemblyStorage#getOperationType <em>Operation Type</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyStorage()
 * @model
 * @generated
 */
public interface AssemblyStorage extends EObject {
	/**
	 * Returns the value of the '<em><b>Operation Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operation Type</em>' reference.
	 * @see #setOperationType(StorageType)
	 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyStorage_OperationType()
	 * @model
	 * @generated
	 */
	StorageType getOperationType();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.assembly.AssemblyStorage#getOperationType <em>Operation Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operation Type</em>' reference.
	 * @see #getOperationType()
	 * @generated
	 */
	void setOperationType(StorageType value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	AssemblyComponent getAssemblyComponent();

} // AssemblyStorage
