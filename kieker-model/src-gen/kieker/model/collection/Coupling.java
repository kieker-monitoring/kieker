/**
 */
package kieker.model.collection;

import kieker.model.analysismodel.type.ComponentType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coupling</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.collection.Coupling#getCaller <em>Caller</em>}</li>
 *   <li>{@link kieker.model.collection.Coupling#getCallee <em>Callee</em>}</li>
 * </ul>
 *
 * @see kieker.model.collection.CollectionPackage#getCoupling()
 * @model
 * @generated
 */
public interface Coupling extends EObject {
	/**
	 * Returns the value of the '<em><b>Caller</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Caller</em>' reference.
	 * @see #setCaller(ComponentType)
	 * @see kieker.model.collection.CollectionPackage#getCoupling_Caller()
	 * @model
	 * @generated
	 */
	ComponentType getCaller();

	/**
	 * Sets the value of the '{@link kieker.model.collection.Coupling#getCaller <em>Caller</em>}' reference.
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
	 * @see kieker.model.collection.CollectionPackage#getCoupling_Callee()
	 * @model
	 * @generated
	 */
	ComponentType getCallee();

	/**
	 * Sets the value of the '{@link kieker.model.collection.Coupling#getCallee <em>Callee</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Callee</em>' reference.
	 * @see #getCallee()
	 * @generated
	 */
	void setCallee(ComponentType value);

} // Coupling
