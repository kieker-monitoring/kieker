/**
 */
package kieker.model.collection;

import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Collection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.collection.OperationCollection#getCaller <em>Caller</em>}</li>
 *   <li>{@link kieker.model.collection.OperationCollection#getCallee <em>Callee</em>}</li>
 *   <li>{@link kieker.model.collection.OperationCollection#getOperations <em>Operations</em>}</li>
 * </ul>
 *
 * @see kieker.model.collection.CollectionPackage#getOperationCollection()
 * @model
 * @generated
 */
public interface OperationCollection extends EObject {
	/**
	 * Returns the value of the '<em><b>Caller</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Caller</em>' reference.
	 * @see #setCaller(ComponentType)
	 * @see kieker.model.collection.CollectionPackage#getOperationCollection_Caller()
	 * @model
	 * @generated
	 */
	ComponentType getCaller();

	/**
	 * Sets the value of the '{@link kieker.model.collection.OperationCollection#getCaller <em>Caller</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Caller</em>' reference.
	 * @see #getCaller()
	 * @generated
	 */
	void setCaller(ComponentType value);

	/**
	 * Returns the value of the '<em><b>Callee</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Callee</em>' reference.
	 * @see #setCallee(ComponentType)
	 * @see kieker.model.collection.CollectionPackage#getOperationCollection_Callee()
	 * @model
	 * @generated
	 */
	ComponentType getCallee();

	/**
	 * Sets the value of the '{@link kieker.model.collection.OperationCollection#getCallee <em>Callee</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Callee</em>' reference.
	 * @see #getCallee()
	 * @generated
	 */
	void setCallee(ComponentType value);

	/**
	 * Returns the value of the '<em><b>Operations</b></em>' map.
	 * The key is of type {@link kieker.model.collection.Coupling},
	 * and the value is of type list of {@link java.util.Map.Entry<java.lang.String, kieker.model.analysismodel.type.OperationType>},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operations</em>' map.
	 * @see kieker.model.collection.CollectionPackage#getOperationCollection_Operations()
	 * @model mapType="kieker.model.collection.CouplingToOperationMap&lt;kieker.model.collection.Coupling, kieker.model.collection.NameToOperationMap&gt;"
	 * @generated
	 */
	EMap<Coupling, EMap<String, OperationType>> getOperations();

} // OperationCollection
