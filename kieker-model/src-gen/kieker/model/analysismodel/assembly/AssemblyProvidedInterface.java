/**
 */
package kieker.model.analysismodel.assembly;

import kieker.model.analysismodel.type.ProvidedInterfaceType;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Provided Interface</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.assembly.AssemblyProvidedInterface#getProvidedInterfaceType <em>Provided Interface Type</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyProvidedInterface()
 * @model
 * @generated
 */
public interface AssemblyProvidedInterface extends EObject {
	/**
	 * Returns the value of the '<em><b>Provided Interface Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provided Interface Type</em>' reference.
	 * @see #setProvidedInterfaceType(ProvidedInterfaceType)
	 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyProvidedInterface_ProvidedInterfaceType()
	 * @model
	 * @generated
	 */
	ProvidedInterfaceType getProvidedInterfaceType();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.assembly.AssemblyProvidedInterface#getProvidedInterfaceType <em>Provided Interface Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Provided Interface Type</em>' reference.
	 * @see #getProvidedInterfaceType()
	 * @generated
	 */
	void setProvidedInterfaceType(ProvidedInterfaceType value);

} // AssemblyProvidedInterface
