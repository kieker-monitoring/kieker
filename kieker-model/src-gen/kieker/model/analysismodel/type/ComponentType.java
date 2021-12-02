/**
 */
package kieker.model.analysismodel.type;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.type.ComponentType#getSignature <em>Signature</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.ComponentType#getProvidedOperations <em>Provided Operations</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.ComponentType#getName <em>Name</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.ComponentType#getPackage <em>Package</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.ComponentType#getProvidedStorages <em>Provided Storages</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.ComponentType#getContainedComponents <em>Contained Components</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.ComponentType#getProvidedInterfaceTypes <em>Provided Interface Types</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.ComponentType#getRequiredInterfaceTypes <em>Required Interface Types</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.type.TypePackage#getComponentType()
 * @model
 * @generated
 */
public interface ComponentType extends EObject {
	/**
	 * Returns the value of the '<em><b>Signature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Signature</em>' attribute.
	 * @see #setSignature(String)
	 * @see kieker.model.analysismodel.type.TypePackage#getComponentType_Signature()
	 * @model
	 * @generated
	 */
	String getSignature();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.type.ComponentType#getSignature <em>Signature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Signature</em>' attribute.
	 * @see #getSignature()
	 * @generated
	 */
	void setSignature(String value);

	/**
	 * Returns the value of the '<em><b>Provided Operations</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.model.analysismodel.type.OperationType},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provided Operations</em>' map.
	 * @see kieker.model.analysismodel.type.TypePackage#getComponentType_ProvidedOperations()
	 * @model mapType="kieker.model.analysismodel.type.EStringToOperationTypeMapEntry&lt;org.eclipse.emf.ecore.EString, kieker.model.analysismodel.type.OperationType&gt;" ordered="false"
	 * @generated
	 */
	EMap<String, OperationType> getProvidedOperations();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see kieker.model.analysismodel.type.TypePackage#getComponentType_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.type.ComponentType#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Package</em>' attribute.
	 * @see #setPackage(String)
	 * @see kieker.model.analysismodel.type.TypePackage#getComponentType_Package()
	 * @model
	 * @generated
	 */
	String getPackage();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.type.ComponentType#getPackage <em>Package</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Package</em>' attribute.
	 * @see #getPackage()
	 * @generated
	 */
	void setPackage(String value);

	/**
	 * Returns the value of the '<em><b>Provided Storages</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.model.analysismodel.type.StorageType},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provided Storages</em>' map.
	 * @see kieker.model.analysismodel.type.TypePackage#getComponentType_ProvidedStorages()
	 * @model mapType="kieker.model.analysismodel.type.EStringToStorageTypeMapEntry&lt;org.eclipse.emf.ecore.EString, kieker.model.analysismodel.type.StorageType&gt;" ordered="false"
	 * @generated
	 */
	EMap<String, StorageType> getProvidedStorages();

	/**
	 * Returns the value of the '<em><b>Contained Components</b></em>' reference list.
	 * The list contents are of type {@link kieker.model.analysismodel.type.ComponentType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contained Components</em>' reference list.
	 * @see kieker.model.analysismodel.type.TypePackage#getComponentType_ContainedComponents()
	 * @model
	 * @generated
	 */
	EList<ComponentType> getContainedComponents();

	/**
	 * Returns the value of the '<em><b>Provided Interface Types</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.model.analysismodel.type.ProvidedInterfaceType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provided Interface Types</em>' containment reference list.
	 * @see kieker.model.analysismodel.type.TypePackage#getComponentType_ProvidedInterfaceTypes()
	 * @model containment="true"
	 * @generated
	 */
	EList<ProvidedInterfaceType> getProvidedInterfaceTypes();

	/**
	 * Returns the value of the '<em><b>Required Interface Types</b></em>' reference list.
	 * The list contents are of type {@link kieker.model.analysismodel.type.RequiredInterfaceType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Required Interface Types</em>' reference list.
	 * @see kieker.model.analysismodel.type.TypePackage#getComponentType_RequiredInterfaceTypes()
	 * @model
	 * @generated
	 */
	EList<RequiredInterfaceType> getRequiredInterfaceTypes();

} // ComponentType
