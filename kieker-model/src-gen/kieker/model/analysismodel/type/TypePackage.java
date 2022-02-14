/**
 */
package kieker.model.analysismodel.type;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see kieker.model.analysismodel.type.TypeFactory
 * @model kind="package"
 * @generated
 */
public interface TypePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "type";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/Kieker/model/analysismodel.ecore/type";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "type";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TypePackage eINSTANCE = kieker.model.analysismodel.type.impl.TypePackageImpl.init();

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.type.impl.TypeModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.type.impl.TypeModelImpl
	 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getTypeModel()
	 * @generated
	 */
	int TYPE_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Component Types</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_MODEL__COMPONENT_TYPES = 0;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_MODEL_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.type.impl.EStringToComponentTypeMapEntryImpl <em>EString To Component Type Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.type.impl.EStringToComponentTypeMapEntryImpl
	 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getEStringToComponentTypeMapEntry()
	 * @generated
	 */
	int ESTRING_TO_COMPONENT_TYPE_MAP_ENTRY = 1;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_COMPONENT_TYPE_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_COMPONENT_TYPE_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Component Type Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_COMPONENT_TYPE_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EString To Component Type Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_COMPONENT_TYPE_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl <em>Component Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.type.impl.ComponentTypeImpl
	 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getComponentType()
	 * @generated
	 */
	int COMPONENT_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Signature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE__SIGNATURE = 0;

	/**
	 * The feature id for the '<em><b>Provided Operations</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE__PROVIDED_OPERATIONS = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE__NAME = 2;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE__PACKAGE = 3;

	/**
	 * The feature id for the '<em><b>Provided Storages</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE__PROVIDED_STORAGES = 4;

	/**
	 * The feature id for the '<em><b>Contained Components</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE__CONTAINED_COMPONENTS = 5;

	/**
	 * The feature id for the '<em><b>Provided Interface Types</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE__PROVIDED_INTERFACE_TYPES = 6;

	/**
	 * The feature id for the '<em><b>Required Interface Types</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE__REQUIRED_INTERFACE_TYPES = 7;

	/**
	 * The number of structural features of the '<em>Component Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE_FEATURE_COUNT = 8;

	/**
	 * The number of operations of the '<em>Component Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.type.impl.EStringToOperationTypeMapEntryImpl <em>EString To Operation Type Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.type.impl.EStringToOperationTypeMapEntryImpl
	 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getEStringToOperationTypeMapEntry()
	 * @generated
	 */
	int ESTRING_TO_OPERATION_TYPE_MAP_ENTRY = 3;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_OPERATION_TYPE_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_OPERATION_TYPE_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Operation Type Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_OPERATION_TYPE_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EString To Operation Type Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_OPERATION_TYPE_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.type.impl.EStringToStorageTypeMapEntryImpl <em>EString To Storage Type Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.type.impl.EStringToStorageTypeMapEntryImpl
	 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getEStringToStorageTypeMapEntry()
	 * @generated
	 */
	int ESTRING_TO_STORAGE_TYPE_MAP_ENTRY = 4;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_STORAGE_TYPE_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_STORAGE_TYPE_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Storage Type Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_STORAGE_TYPE_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EString To Storage Type Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_STORAGE_TYPE_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.type.impl.OperationTypeImpl <em>Operation Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.type.impl.OperationTypeImpl
	 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getOperationType()
	 * @generated
	 */
	int OPERATION_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Signature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_TYPE__SIGNATURE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_TYPE__NAME = 1;

	/**
	 * The feature id for the '<em><b>Return Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_TYPE__RETURN_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Modifiers</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_TYPE__MODIFIERS = 3;

	/**
	 * The feature id for the '<em><b>Parameter Types</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_TYPE__PARAMETER_TYPES = 4;

	/**
	 * The number of structural features of the '<em>Operation Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_TYPE_FEATURE_COUNT = 5;

	/**
	 * The operation id for the '<em>Get Component Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_TYPE___GET_COMPONENT_TYPE = 0;

	/**
	 * The number of operations of the '<em>Operation Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_TYPE_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.type.impl.StorageTypeImpl <em>Storage Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.type.impl.StorageTypeImpl
	 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getStorageType()
	 * @generated
	 */
	int STORAGE_TYPE = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STORAGE_TYPE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STORAGE_TYPE__TYPE = 1;

	/**
	 * The number of structural features of the '<em>Storage Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STORAGE_TYPE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Storage Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STORAGE_TYPE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.type.impl.ProvidedInterfaceTypeImpl <em>Provided Interface Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.type.impl.ProvidedInterfaceTypeImpl
	 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getProvidedInterfaceType()
	 * @generated
	 */
	int PROVIDED_INTERFACE_TYPE = 7;

	/**
	 * The feature id for the '<em><b>Provided Operation Types</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROVIDED_INTERFACE_TYPE__PROVIDED_OPERATION_TYPES = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROVIDED_INTERFACE_TYPE__NAME = 1;

	/**
	 * The feature id for the '<em><b>Signature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROVIDED_INTERFACE_TYPE__SIGNATURE = 2;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROVIDED_INTERFACE_TYPE__PACKAGE = 3;

	/**
	 * The number of structural features of the '<em>Provided Interface Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROVIDED_INTERFACE_TYPE_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Provided Interface Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROVIDED_INTERFACE_TYPE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.type.impl.EStringToProvidedInterfaceTypeMapEntryImpl <em>EString To Provided Interface Type Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.type.impl.EStringToProvidedInterfaceTypeMapEntryImpl
	 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getEStringToProvidedInterfaceTypeMapEntry()
	 * @generated
	 */
	int ESTRING_TO_PROVIDED_INTERFACE_TYPE_MAP_ENTRY = 8;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_PROVIDED_INTERFACE_TYPE_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_PROVIDED_INTERFACE_TYPE_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Provided Interface Type Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_PROVIDED_INTERFACE_TYPE_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EString To Provided Interface Type Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_PROVIDED_INTERFACE_TYPE_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.type.impl.RequiredInterfaceTypeImpl <em>Required Interface Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.type.impl.RequiredInterfaceTypeImpl
	 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getRequiredInterfaceType()
	 * @generated
	 */
	int REQUIRED_INTERFACE_TYPE = 9;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUIRED_INTERFACE_TYPE__REQUIRES = 0;

	/**
	 * The number of structural features of the '<em>Required Interface Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUIRED_INTERFACE_TYPE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Required Interface Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUIRED_INTERFACE_TYPE_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.type.impl.InterfaceEStringToOperationTypeMapEntryImpl <em>Interface EString To Operation Type Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.type.impl.InterfaceEStringToOperationTypeMapEntryImpl
	 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getInterfaceEStringToOperationTypeMapEntry()
	 * @generated
	 */
	int INTERFACE_ESTRING_TO_OPERATION_TYPE_MAP_ENTRY = 10;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERFACE_ESTRING_TO_OPERATION_TYPE_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERFACE_ESTRING_TO_OPERATION_TYPE_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Interface EString To Operation Type Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERFACE_ESTRING_TO_OPERATION_TYPE_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Interface EString To Operation Type Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERFACE_ESTRING_TO_OPERATION_TYPE_MAP_ENTRY_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.type.TypeModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see kieker.model.analysismodel.type.TypeModel
	 * @generated
	 */
	EClass getTypeModel();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.type.TypeModel#getComponentTypes <em>Component Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Component Types</em>'.
	 * @see kieker.model.analysismodel.type.TypeModel#getComponentTypes()
	 * @see #getTypeModel()
	 * @generated
	 */
	EReference getTypeModel_ComponentTypes();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Component Type Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EString To Component Type Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.model.analysismodel.type.ComponentType" valueContainment="true"
	 * @generated
	 */
	EClass getEStringToComponentTypeMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToComponentTypeMapEntry()
	 * @generated
	 */
	EAttribute getEStringToComponentTypeMapEntry_Key();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToComponentTypeMapEntry()
	 * @generated
	 */
	EReference getEStringToComponentTypeMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.type.ComponentType <em>Component Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Component Type</em>'.
	 * @see kieker.model.analysismodel.type.ComponentType
	 * @generated
	 */
	EClass getComponentType();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.type.ComponentType#getSignature <em>Signature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Signature</em>'.
	 * @see kieker.model.analysismodel.type.ComponentType#getSignature()
	 * @see #getComponentType()
	 * @generated
	 */
	EAttribute getComponentType_Signature();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.type.ComponentType#getProvidedOperations <em>Provided Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Provided Operations</em>'.
	 * @see kieker.model.analysismodel.type.ComponentType#getProvidedOperations()
	 * @see #getComponentType()
	 * @generated
	 */
	EReference getComponentType_ProvidedOperations();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.type.ComponentType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.model.analysismodel.type.ComponentType#getName()
	 * @see #getComponentType()
	 * @generated
	 */
	EAttribute getComponentType_Name();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.type.ComponentType#getPackage <em>Package</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Package</em>'.
	 * @see kieker.model.analysismodel.type.ComponentType#getPackage()
	 * @see #getComponentType()
	 * @generated
	 */
	EAttribute getComponentType_Package();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.type.ComponentType#getProvidedStorages <em>Provided Storages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Provided Storages</em>'.
	 * @see kieker.model.analysismodel.type.ComponentType#getProvidedStorages()
	 * @see #getComponentType()
	 * @generated
	 */
	EReference getComponentType_ProvidedStorages();

	/**
	 * Returns the meta object for the reference list '{@link kieker.model.analysismodel.type.ComponentType#getContainedComponents <em>Contained Components</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Contained Components</em>'.
	 * @see kieker.model.analysismodel.type.ComponentType#getContainedComponents()
	 * @see #getComponentType()
	 * @generated
	 */
	EReference getComponentType_ContainedComponents();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.model.analysismodel.type.ComponentType#getProvidedInterfaceTypes <em>Provided Interface Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Provided Interface Types</em>'.
	 * @see kieker.model.analysismodel.type.ComponentType#getProvidedInterfaceTypes()
	 * @see #getComponentType()
	 * @generated
	 */
	EReference getComponentType_ProvidedInterfaceTypes();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.model.analysismodel.type.ComponentType#getRequiredInterfaceTypes <em>Required Interface Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Required Interface Types</em>'.
	 * @see kieker.model.analysismodel.type.ComponentType#getRequiredInterfaceTypes()
	 * @see #getComponentType()
	 * @generated
	 */
	EReference getComponentType_RequiredInterfaceTypes();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Operation Type Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EString To Operation Type Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.model.analysismodel.type.OperationType" valueContainment="true"
	 * @generated
	 */
	EClass getEStringToOperationTypeMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToOperationTypeMapEntry()
	 * @generated
	 */
	EAttribute getEStringToOperationTypeMapEntry_Key();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToOperationTypeMapEntry()
	 * @generated
	 */
	EReference getEStringToOperationTypeMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Storage Type Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EString To Storage Type Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.model.analysismodel.type.StorageType" valueContainment="true"
	 * @generated
	 */
	EClass getEStringToStorageTypeMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToStorageTypeMapEntry()
	 * @generated
	 */
	EAttribute getEStringToStorageTypeMapEntry_Key();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToStorageTypeMapEntry()
	 * @generated
	 */
	EReference getEStringToStorageTypeMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.type.OperationType <em>Operation Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation Type</em>'.
	 * @see kieker.model.analysismodel.type.OperationType
	 * @generated
	 */
	EClass getOperationType();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.type.OperationType#getSignature <em>Signature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Signature</em>'.
	 * @see kieker.model.analysismodel.type.OperationType#getSignature()
	 * @see #getOperationType()
	 * @generated
	 */
	EAttribute getOperationType_Signature();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.type.OperationType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.model.analysismodel.type.OperationType#getName()
	 * @see #getOperationType()
	 * @generated
	 */
	EAttribute getOperationType_Name();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.type.OperationType#getReturnType <em>Return Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Return Type</em>'.
	 * @see kieker.model.analysismodel.type.OperationType#getReturnType()
	 * @see #getOperationType()
	 * @generated
	 */
	EAttribute getOperationType_ReturnType();

	/**
	 * Returns the meta object for the attribute list '{@link kieker.model.analysismodel.type.OperationType#getModifiers <em>Modifiers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Modifiers</em>'.
	 * @see kieker.model.analysismodel.type.OperationType#getModifiers()
	 * @see #getOperationType()
	 * @generated
	 */
	EAttribute getOperationType_Modifiers();

	/**
	 * Returns the meta object for the attribute list '{@link kieker.model.analysismodel.type.OperationType#getParameterTypes <em>Parameter Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Parameter Types</em>'.
	 * @see kieker.model.analysismodel.type.OperationType#getParameterTypes()
	 * @see #getOperationType()
	 * @generated
	 */
	EAttribute getOperationType_ParameterTypes();

	/**
	 * Returns the meta object for the '{@link kieker.model.analysismodel.type.OperationType#getComponentType() <em>Get Component Type</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Component Type</em>' operation.
	 * @see kieker.model.analysismodel.type.OperationType#getComponentType()
	 * @generated
	 */
	EOperation getOperationType__GetComponentType();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.type.StorageType <em>Storage Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Storage Type</em>'.
	 * @see kieker.model.analysismodel.type.StorageType
	 * @generated
	 */
	EClass getStorageType();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.type.StorageType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.model.analysismodel.type.StorageType#getName()
	 * @see #getStorageType()
	 * @generated
	 */
	EAttribute getStorageType_Name();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.type.StorageType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see kieker.model.analysismodel.type.StorageType#getType()
	 * @see #getStorageType()
	 * @generated
	 */
	EAttribute getStorageType_Type();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.type.ProvidedInterfaceType <em>Provided Interface Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Provided Interface Type</em>'.
	 * @see kieker.model.analysismodel.type.ProvidedInterfaceType
	 * @generated
	 */
	EClass getProvidedInterfaceType();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.type.ProvidedInterfaceType#getProvidedOperationTypes <em>Provided Operation Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Provided Operation Types</em>'.
	 * @see kieker.model.analysismodel.type.ProvidedInterfaceType#getProvidedOperationTypes()
	 * @see #getProvidedInterfaceType()
	 * @generated
	 */
	EReference getProvidedInterfaceType_ProvidedOperationTypes();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.type.ProvidedInterfaceType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.model.analysismodel.type.ProvidedInterfaceType#getName()
	 * @see #getProvidedInterfaceType()
	 * @generated
	 */
	EAttribute getProvidedInterfaceType_Name();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.type.ProvidedInterfaceType#getSignature <em>Signature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Signature</em>'.
	 * @see kieker.model.analysismodel.type.ProvidedInterfaceType#getSignature()
	 * @see #getProvidedInterfaceType()
	 * @generated
	 */
	EAttribute getProvidedInterfaceType_Signature();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.type.ProvidedInterfaceType#getPackage <em>Package</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Package</em>'.
	 * @see kieker.model.analysismodel.type.ProvidedInterfaceType#getPackage()
	 * @see #getProvidedInterfaceType()
	 * @generated
	 */
	EAttribute getProvidedInterfaceType_Package();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Provided Interface Type Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EString To Provided Interface Type Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.model.analysismodel.type.ProvidedInterfaceType" valueContainment="true"
	 * @generated
	 */
	EClass getEStringToProvidedInterfaceTypeMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToProvidedInterfaceTypeMapEntry()
	 * @generated
	 */
	EAttribute getEStringToProvidedInterfaceTypeMapEntry_Key();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToProvidedInterfaceTypeMapEntry()
	 * @generated
	 */
	EReference getEStringToProvidedInterfaceTypeMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.type.RequiredInterfaceType <em>Required Interface Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Required Interface Type</em>'.
	 * @see kieker.model.analysismodel.type.RequiredInterfaceType
	 * @generated
	 */
	EClass getRequiredInterfaceType();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.type.RequiredInterfaceType#getRequires <em>Requires</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Requires</em>'.
	 * @see kieker.model.analysismodel.type.RequiredInterfaceType#getRequires()
	 * @see #getRequiredInterfaceType()
	 * @generated
	 */
	EReference getRequiredInterfaceType_Requires();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Interface EString To Operation Type Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Interface EString To Operation Type Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.model.analysismodel.type.OperationType"
	 * @generated
	 */
	EClass getInterfaceEStringToOperationTypeMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getInterfaceEStringToOperationTypeMapEntry()
	 * @generated
	 */
	EAttribute getInterfaceEStringToOperationTypeMapEntry_Key();

	/**
	 * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getInterfaceEStringToOperationTypeMapEntry()
	 * @generated
	 */
	EReference getInterfaceEStringToOperationTypeMapEntry_Value();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TypeFactory getTypeFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.type.impl.TypeModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.type.impl.TypeModelImpl
		 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getTypeModel()
		 * @generated
		 */
		EClass TYPE_MODEL = eINSTANCE.getTypeModel();

		/**
		 * The meta object literal for the '<em><b>Component Types</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TYPE_MODEL__COMPONENT_TYPES = eINSTANCE.getTypeModel_ComponentTypes();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.type.impl.EStringToComponentTypeMapEntryImpl <em>EString To Component Type Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.type.impl.EStringToComponentTypeMapEntryImpl
		 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getEStringToComponentTypeMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_COMPONENT_TYPE_MAP_ENTRY = eINSTANCE.getEStringToComponentTypeMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTRING_TO_COMPONENT_TYPE_MAP_ENTRY__KEY = eINSTANCE.getEStringToComponentTypeMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTRING_TO_COMPONENT_TYPE_MAP_ENTRY__VALUE = eINSTANCE.getEStringToComponentTypeMapEntry_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl <em>Component Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.type.impl.ComponentTypeImpl
		 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getComponentType()
		 * @generated
		 */
		EClass COMPONENT_TYPE = eINSTANCE.getComponentType();

		/**
		 * The meta object literal for the '<em><b>Signature</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT_TYPE__SIGNATURE = eINSTANCE.getComponentType_Signature();

		/**
		 * The meta object literal for the '<em><b>Provided Operations</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT_TYPE__PROVIDED_OPERATIONS = eINSTANCE.getComponentType_ProvidedOperations();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT_TYPE__NAME = eINSTANCE.getComponentType_Name();

		/**
		 * The meta object literal for the '<em><b>Package</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT_TYPE__PACKAGE = eINSTANCE.getComponentType_Package();

		/**
		 * The meta object literal for the '<em><b>Provided Storages</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT_TYPE__PROVIDED_STORAGES = eINSTANCE.getComponentType_ProvidedStorages();

		/**
		 * The meta object literal for the '<em><b>Contained Components</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT_TYPE__CONTAINED_COMPONENTS = eINSTANCE.getComponentType_ContainedComponents();

		/**
		 * The meta object literal for the '<em><b>Provided Interface Types</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT_TYPE__PROVIDED_INTERFACE_TYPES = eINSTANCE.getComponentType_ProvidedInterfaceTypes();

		/**
		 * The meta object literal for the '<em><b>Required Interface Types</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT_TYPE__REQUIRED_INTERFACE_TYPES = eINSTANCE.getComponentType_RequiredInterfaceTypes();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.type.impl.EStringToOperationTypeMapEntryImpl <em>EString To Operation Type Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.type.impl.EStringToOperationTypeMapEntryImpl
		 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getEStringToOperationTypeMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_OPERATION_TYPE_MAP_ENTRY = eINSTANCE.getEStringToOperationTypeMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTRING_TO_OPERATION_TYPE_MAP_ENTRY__KEY = eINSTANCE.getEStringToOperationTypeMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTRING_TO_OPERATION_TYPE_MAP_ENTRY__VALUE = eINSTANCE.getEStringToOperationTypeMapEntry_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.type.impl.EStringToStorageTypeMapEntryImpl <em>EString To Storage Type Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.type.impl.EStringToStorageTypeMapEntryImpl
		 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getEStringToStorageTypeMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_STORAGE_TYPE_MAP_ENTRY = eINSTANCE.getEStringToStorageTypeMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTRING_TO_STORAGE_TYPE_MAP_ENTRY__KEY = eINSTANCE.getEStringToStorageTypeMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTRING_TO_STORAGE_TYPE_MAP_ENTRY__VALUE = eINSTANCE.getEStringToStorageTypeMapEntry_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.type.impl.OperationTypeImpl <em>Operation Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.type.impl.OperationTypeImpl
		 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getOperationType()
		 * @generated
		 */
		EClass OPERATION_TYPE = eINSTANCE.getOperationType();

		/**
		 * The meta object literal for the '<em><b>Signature</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_TYPE__SIGNATURE = eINSTANCE.getOperationType_Signature();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_TYPE__NAME = eINSTANCE.getOperationType_Name();

		/**
		 * The meta object literal for the '<em><b>Return Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_TYPE__RETURN_TYPE = eINSTANCE.getOperationType_ReturnType();

		/**
		 * The meta object literal for the '<em><b>Modifiers</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_TYPE__MODIFIERS = eINSTANCE.getOperationType_Modifiers();

		/**
		 * The meta object literal for the '<em><b>Parameter Types</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_TYPE__PARAMETER_TYPES = eINSTANCE.getOperationType_ParameterTypes();

		/**
		 * The meta object literal for the '<em><b>Get Component Type</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation OPERATION_TYPE___GET_COMPONENT_TYPE = eINSTANCE.getOperationType__GetComponentType();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.type.impl.StorageTypeImpl <em>Storage Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.type.impl.StorageTypeImpl
		 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getStorageType()
		 * @generated
		 */
		EClass STORAGE_TYPE = eINSTANCE.getStorageType();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STORAGE_TYPE__NAME = eINSTANCE.getStorageType_Name();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STORAGE_TYPE__TYPE = eINSTANCE.getStorageType_Type();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.type.impl.ProvidedInterfaceTypeImpl <em>Provided Interface Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.type.impl.ProvidedInterfaceTypeImpl
		 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getProvidedInterfaceType()
		 * @generated
		 */
		EClass PROVIDED_INTERFACE_TYPE = eINSTANCE.getProvidedInterfaceType();

		/**
		 * The meta object literal for the '<em><b>Provided Operation Types</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROVIDED_INTERFACE_TYPE__PROVIDED_OPERATION_TYPES = eINSTANCE.getProvidedInterfaceType_ProvidedOperationTypes();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROVIDED_INTERFACE_TYPE__NAME = eINSTANCE.getProvidedInterfaceType_Name();

		/**
		 * The meta object literal for the '<em><b>Signature</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROVIDED_INTERFACE_TYPE__SIGNATURE = eINSTANCE.getProvidedInterfaceType_Signature();

		/**
		 * The meta object literal for the '<em><b>Package</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROVIDED_INTERFACE_TYPE__PACKAGE = eINSTANCE.getProvidedInterfaceType_Package();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.type.impl.EStringToProvidedInterfaceTypeMapEntryImpl <em>EString To Provided Interface Type Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.type.impl.EStringToProvidedInterfaceTypeMapEntryImpl
		 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getEStringToProvidedInterfaceTypeMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_PROVIDED_INTERFACE_TYPE_MAP_ENTRY = eINSTANCE.getEStringToProvidedInterfaceTypeMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTRING_TO_PROVIDED_INTERFACE_TYPE_MAP_ENTRY__KEY = eINSTANCE.getEStringToProvidedInterfaceTypeMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTRING_TO_PROVIDED_INTERFACE_TYPE_MAP_ENTRY__VALUE = eINSTANCE.getEStringToProvidedInterfaceTypeMapEntry_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.type.impl.RequiredInterfaceTypeImpl <em>Required Interface Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.type.impl.RequiredInterfaceTypeImpl
		 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getRequiredInterfaceType()
		 * @generated
		 */
		EClass REQUIRED_INTERFACE_TYPE = eINSTANCE.getRequiredInterfaceType();

		/**
		 * The meta object literal for the '<em><b>Requires</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REQUIRED_INTERFACE_TYPE__REQUIRES = eINSTANCE.getRequiredInterfaceType_Requires();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.type.impl.InterfaceEStringToOperationTypeMapEntryImpl <em>Interface EString To Operation Type Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.type.impl.InterfaceEStringToOperationTypeMapEntryImpl
		 * @see kieker.model.analysismodel.type.impl.TypePackageImpl#getInterfaceEStringToOperationTypeMapEntry()
		 * @generated
		 */
		EClass INTERFACE_ESTRING_TO_OPERATION_TYPE_MAP_ENTRY = eINSTANCE.getInterfaceEStringToOperationTypeMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INTERFACE_ESTRING_TO_OPERATION_TYPE_MAP_ENTRY__KEY = eINSTANCE.getInterfaceEStringToOperationTypeMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INTERFACE_ESTRING_TO_OPERATION_TYPE_MAP_ENTRY__VALUE = eINSTANCE.getInterfaceEStringToOperationTypeMapEntry_Value();

	}

} //TypePackage
