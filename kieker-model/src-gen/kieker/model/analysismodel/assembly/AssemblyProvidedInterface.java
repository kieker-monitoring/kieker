/**
 */
package kieker.model.analysismodel.assembly;

import kieker.model.analysismodel.type.ProvidedInterfaceType;

import org.eclipse.emf.common.util.EMap;

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
 *   <li>{@link kieker.model.analysismodel.assembly.AssemblyProvidedInterface#getProvidedOperations <em>Provided Operations</em>}</li>
 *   <li>{@link kieker.model.analysismodel.assembly.AssemblyProvidedInterface#getName <em>Name</em>}</li>
 *   <li>{@link kieker.model.analysismodel.assembly.AssemblyProvidedInterface#getDeclaration <em>Declaration</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyProvidedInterface()
 * @model
 * @generated
 */
public interface AssemblyProvidedInterface extends EObject {
	/**
	 * Returns the value of the '<em><b>Provided Operations</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.model.analysismodel.assembly.AssemblyOperation},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provided Operations</em>' map.
	 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyProvidedInterface_ProvidedOperations()
	 * @model mapType="kieker.model.analysismodel.assembly.EStringToAssemblyOperationMapEntry&lt;org.eclipse.emf.ecore.EString, kieker.model.analysismodel.assembly.AssemblyOperation&gt;"
	 * @generated
	 */
	EMap<String, AssemblyOperation> getProvidedOperations();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyProvidedInterface_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.assembly.AssemblyProvidedInterface#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Declaration</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Declaration</em>' reference.
	 * @see #setDeclaration(ProvidedInterfaceType)
	 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyProvidedInterface_Declaration()
	 * @model
	 * @generated
	 */
	ProvidedInterfaceType getDeclaration();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.assembly.AssemblyProvidedInterface#getDeclaration <em>Declaration</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Declaration</em>' reference.
	 * @see #getDeclaration()
	 * @generated
	 */
	void setDeclaration(ProvidedInterfaceType value);

} // AssemblyProvidedInterface
