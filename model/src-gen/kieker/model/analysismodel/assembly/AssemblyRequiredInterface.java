/**
 */
package kieker.model.analysismodel.assembly;

import org.eclipse.emf.ecore.EObject;

import kieker.model.analysismodel.type.RequiredInterfaceType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Required Interface</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.assembly.AssemblyRequiredInterface#getRequires <em>Requires</em>}</li>
 * <li>{@link kieker.model.analysismodel.assembly.AssemblyRequiredInterface#getRequiredInterfaceType <em>Required Interface Type</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyRequiredInterface()
 * @model
 * @generated
 */
public interface AssemblyRequiredInterface extends EObject {
	/**
	 * Returns the value of the '<em><b>Requires</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Requires</em>' reference.
	 * @see #setRequires(AssemblyProvidedInterface)
	 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyRequiredInterface_Requires()
	 * @model
	 * @generated
	 */
	AssemblyProvidedInterface getRequires();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.assembly.AssemblyRequiredInterface#getRequires <em>Requires</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Requires</em>' reference.
	 * @see #getRequires()
	 * @generated
	 */
	void setRequires(AssemblyProvidedInterface value);

	/**
	 * Returns the value of the '<em><b>Required Interface Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Required Interface Type</em>' reference.
	 * @see #setRequiredInterfaceType(RequiredInterfaceType)
	 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyRequiredInterface_RequiredInterfaceType()
	 * @model
	 * @generated
	 */
	RequiredInterfaceType getRequiredInterfaceType();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.assembly.AssemblyRequiredInterface#getRequiredInterfaceType <em>Required Interface Type</em>}'
	 * reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Required Interface Type</em>' reference.
	 * @see #getRequiredInterfaceType()
	 * @generated
	 */
	void setRequiredInterfaceType(RequiredInterfaceType value);

} // AssemblyRequiredInterface
