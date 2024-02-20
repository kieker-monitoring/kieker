/**
 */
package kieker.model.collection;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Collection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.collection.OperationCollection#getRequired <em>Required</em>}</li>
 *   <li>{@link kieker.model.collection.OperationCollection#getProvided <em>Provided</em>}</li>
 *   <li>{@link kieker.model.collection.OperationCollection#getCallees <em>Callees</em>}</li>
 *   <li>{@link kieker.model.collection.OperationCollection#getCallers <em>Callers</em>}</li>
 * </ul>
 *
 * @see kieker.model.collection.CollectionPackage#getOperationCollection()
 * @model
 * @generated
 */
public interface OperationCollection extends EObject {
	/**
	 * Returns the value of the '<em><b>Required</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Required</em>' reference.
	 * @see #setRequired(ComponentType)
	 * @see kieker.model.collection.CollectionPackage#getOperationCollection_Required()
	 * @model
	 * @generated
	 */
	ComponentType getRequired();

	/**
	 * Sets the value of the '{@link kieker.model.collection.OperationCollection#getRequired <em>Required</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Required</em>' reference.
	 * @see #getRequired()
	 * @generated
	 */
	void setRequired(ComponentType value);

	/**
	 * Returns the value of the '<em><b>Provided</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provided</em>' reference.
	 * @see #setProvided(ComponentType)
	 * @see kieker.model.collection.CollectionPackage#getOperationCollection_Provided()
	 * @model
	 * @generated
	 */
	ComponentType getProvided();

	/**
	 * Sets the value of the '{@link kieker.model.collection.OperationCollection#getProvided <em>Provided</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Provided</em>' reference.
	 * @see #getProvided()
	 * @generated
	 */
	void setProvided(ComponentType value);

	/**
	 * Returns the value of the '<em><b>Callees</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.model.analysismodel.type.OperationType},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Callees</em>' map.
	 * @see kieker.model.collection.CollectionPackage#getOperationCollection_Callees()
	 * @model mapType="kieker.model.collection.NameToOperationMap&lt;org.eclipse.emf.ecore.EString, kieker.model.analysismodel.type.OperationType&gt;"
	 * @generated
	 */
	EMap<String, OperationType> getCallees();

	/**
	 * Returns the value of the '<em><b>Callers</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.model.analysismodel.type.OperationType},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Callers</em>' map.
	 * @see kieker.model.collection.CollectionPackage#getOperationCollection_Callers()
	 * @model mapType="kieker.model.collection.NameToOperationMap&lt;org.eclipse.emf.ecore.EString, kieker.model.analysismodel.type.OperationType&gt;"
	 * @generated
	 */
	EMap<String, OperationType> getCallers();

} // OperationCollection
