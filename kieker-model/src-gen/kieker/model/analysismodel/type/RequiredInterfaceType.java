/**
 */
package kieker.model.analysismodel.type;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Required Interface Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.type.RequiredInterfaceType#getRequires <em>Requires</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.type.TypePackage#getRequiredInterfaceType()
 * @model
 * @generated
 */
public interface RequiredInterfaceType extends EObject {
	/**
	 * Returns the value of the '<em><b>Requires</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Requires</em>' reference.
	 * @see #setRequires(ProvidedInterfaceType)
	 * @see kieker.model.analysismodel.type.TypePackage#getRequiredInterfaceType_Requires()
	 * @model
	 * @generated
	 */
	ProvidedInterfaceType getRequires();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.type.RequiredInterfaceType#getRequires <em>Requires</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Requires</em>' reference.
	 * @see #getRequires()
	 * @generated
	 */
	void setRequires(ProvidedInterfaceType value);

} // RequiredInterfaceType
