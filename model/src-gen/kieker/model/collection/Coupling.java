/**
 */
package kieker.model.collection;

import org.eclipse.emf.ecore.EObject;

import kieker.model.analysismodel.type.ComponentType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coupling</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.collection.Coupling#getRequired <em>Required</em>}</li>
 *   <li>{@link kieker.model.collection.Coupling#getProvided <em>Provided</em>}</li>
 * </ul>
 *
 * @see kieker.model.collection.CollectionPackage#getCoupling()
 * @model
 * @generated
 */
public interface Coupling extends EObject {
	/**
	 * Returns the value of the '<em><b>Required</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Required</em>' reference.
	 * @see #setRequired(ComponentType)
	 * @see kieker.model.collection.CollectionPackage#getCoupling_Required()
	 * @model
	 * @generated
	 */
	ComponentType getRequired();

	/**
	 * Sets the value of the '{@link kieker.model.collection.Coupling#getRequired <em>Required</em>}' reference.
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
	 * @see kieker.model.collection.CollectionPackage#getCoupling_Provided()
	 * @model
	 * @generated
	 */
	ComponentType getProvided();

	/**
	 * Sets the value of the '{@link kieker.model.collection.Coupling#getProvided <em>Provided</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Provided</em>' reference.
	 * @see #getProvided()
	 * @generated
	 */
	void setProvided(ComponentType value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model valueRequired="true"
	 * @generated
	 */
	boolean equals(Object value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	int hashCode();

} // Coupling
