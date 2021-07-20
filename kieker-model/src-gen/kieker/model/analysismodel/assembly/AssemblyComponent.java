/**
 */
package kieker.model.analysismodel.assembly;

import kieker.model.analysismodel.type.ComponentType;

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
 *   <li>{@link kieker.model.analysismodel.assembly.AssemblyComponent#getAssemblyOperations <em>Assembly Operations</em>}</li>
 *   <li>{@link kieker.model.analysismodel.assembly.AssemblyComponent#getComponentType <em>Component Type</em>}</li>
 *   <li>{@link kieker.model.analysismodel.assembly.AssemblyComponent#getAssemblyStorages <em>Assembly Storages</em>}</li>
 *   <li>{@link kieker.model.analysismodel.assembly.AssemblyComponent#getSignature <em>Signature</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyComponent()
 * @model
 * @generated
 */
public interface AssemblyComponent extends EObject {
	/**
	 * Returns the value of the '<em><b>Assembly Operations</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.model.analysismodel.assembly.AssemblyOperation},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assembly Operations</em>' map.
	 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyComponent_AssemblyOperations()
	 * @model mapType="kieker.model.analysismodel.assembly.EStringToAssemblyOperationMapEntry&lt;org.eclipse.emf.ecore.EString, kieker.model.analysismodel.assembly.AssemblyOperation&gt;" ordered="false"
	 * @generated
	 */
	EMap<String, AssemblyOperation> getAssemblyOperations();

	/**
	 * Returns the value of the '<em><b>Component Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component Type</em>' reference.
	 * @see #setComponentType(ComponentType)
	 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyComponent_ComponentType()
	 * @model
	 * @generated
	 */
	ComponentType getComponentType();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.assembly.AssemblyComponent#getComponentType <em>Component Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Component Type</em>' reference.
	 * @see #getComponentType()
	 * @generated
	 */
	void setComponentType(ComponentType value);

	/**
	 * Returns the value of the '<em><b>Assembly Storages</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.model.analysismodel.assembly.AssemblyStorage},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assembly Storages</em>' map.
	 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyComponent_AssemblyStorages()
	 * @model mapType="kieker.model.analysismodel.assembly.EStringToAssemblyStorageMapEntry&lt;org.eclipse.emf.ecore.EString, kieker.model.analysismodel.assembly.AssemblyStorage&gt;" ordered="false"
	 * @generated
	 */
	EMap<String, AssemblyStorage> getAssemblyStorages();

	/**
	 * Returns the value of the '<em><b>Signature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Signature</em>' attribute.
	 * @see #setSignature(String)
	 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyComponent_Signature()
	 * @model
	 * @generated
	 */
	String getSignature();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.assembly.AssemblyComponent#getSignature <em>Signature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Signature</em>' attribute.
	 * @see #getSignature()
	 * @generated
	 */
	void setSignature(String value);

} // AssemblyComponent
