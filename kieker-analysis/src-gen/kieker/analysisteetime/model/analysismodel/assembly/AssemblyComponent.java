/**
 */
package kieker.analysisteetime.model.analysismodel.assembly;

import kieker.analysisteetime.model.analysismodel.type.ComponentType;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent#getAssemblyOperations <em>Assembly Operations</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent#getComponentType <em>Component Type</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.assembly.AssemblyPackage#getAssemblyComponent()
 * @model
 * @generated
 */
public interface AssemblyComponent extends EObject {
	/**
	 * Returns the value of the '<em><b>Assembly Operations</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.analysisteetime.model.analysismodel.assembly.AssemblyOperation},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assembly Operations</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assembly Operations</em>' map.
	 * @see kieker.analysisteetime.model.analysismodel.assembly.AssemblyPackage#getAssemblyComponent_AssemblyOperations()
	 * @model mapType="kieker.analysisteetime.model.analysismodel.assembly.EStringToAssemblyOperationMapEntry<org.eclipse.emf.ecore.EString, kieker.analysisteetime.model.analysismodel.assembly.AssemblyOperation>" ordered="false"
	 * @generated
	 */
	EMap<String, AssemblyOperation> getAssemblyOperations();

	/**
	 * Returns the value of the '<em><b>Component Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Component Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component Type</em>' reference.
	 * @see #setComponentType(ComponentType)
	 * @see kieker.analysisteetime.model.analysismodel.assembly.AssemblyPackage#getAssemblyComponent_ComponentType()
	 * @model
	 * @generated
	 */
	ComponentType getComponentType();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent#getComponentType <em>Component Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Component Type</em>' reference.
	 * @see #getComponentType()
	 * @generated
	 */
	void setComponentType(ComponentType value);

} // AssemblyComponent
