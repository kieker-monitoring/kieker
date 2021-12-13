/**
 */
package kieker.model.analysismodel.assembly;

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
 * @see kieker.model.analysismodel.assembly.AssemblyFactory
 * @model kind="package"
 * @generated
 */
public interface AssemblyPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "assembly";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/Kieker/model/analysismodel.ecore/assembly";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "assembly";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AssemblyPackage eINSTANCE = kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl.init();

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.assembly.impl.AssemblyModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.assembly.impl.AssemblyModelImpl
	 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyModel()
	 * @generated
	 */
	int ASSEMBLY_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Assembly Components</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_MODEL__ASSEMBLY_COMPONENTS = 0;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_MODEL_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.assembly.impl.EStringToAssemblyComponentMapEntryImpl <em>EString To Assembly Component Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.assembly.impl.EStringToAssemblyComponentMapEntryImpl
	 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getEStringToAssemblyComponentMapEntry()
	 * @generated
	 */
	int ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY = 1;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Assembly Component Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EString To Assembly Component Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl <em>Component</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl
	 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyComponent()
	 * @generated
	 */
	int ASSEMBLY_COMPONENT = 2;

	/**
	 * The feature id for the '<em><b>Operations</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_COMPONENT__OPERATIONS = 0;

	/**
	 * The feature id for the '<em><b>Component Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_COMPONENT__COMPONENT_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Storages</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_COMPONENT__STORAGES = 2;

	/**
	 * The feature id for the '<em><b>Contained Components</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_COMPONENT__CONTAINED_COMPONENTS = 3;

	/**
	 * The feature id for the '<em><b>Provided Interfaces</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_COMPONENT__PROVIDED_INTERFACES = 4;

	/**
	 * The feature id for the '<em><b>Required Interfaces</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_COMPONENT__REQUIRED_INTERFACES = 5;

	/**
	 * The feature id for the '<em><b>Signature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_COMPONENT__SIGNATURE = 6;

	/**
	 * The number of structural features of the '<em>Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_COMPONENT_FEATURE_COUNT = 7;

	/**
	 * The number of operations of the '<em>Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_COMPONENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.assembly.impl.EStringToAssemblyOperationMapEntryImpl <em>EString To Assembly Operation Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.assembly.impl.EStringToAssemblyOperationMapEntryImpl
	 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getEStringToAssemblyOperationMapEntry()
	 * @generated
	 */
	int ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY = 3;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Assembly Operation Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EString To Assembly Operation Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.assembly.impl.AssemblyOperationImpl <em>Operation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.assembly.impl.AssemblyOperationImpl
	 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyOperation()
	 * @generated
	 */
	int ASSEMBLY_OPERATION = 4;

	/**
	 * The feature id for the '<em><b>Operation Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_OPERATION__OPERATION_TYPE = 0;

	/**
	 * The number of structural features of the '<em>Operation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_OPERATION_FEATURE_COUNT = 1;

	/**
	 * The operation id for the '<em>Get Assembly Component</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_OPERATION___GET_ASSEMBLY_COMPONENT = 0;

	/**
	 * The number of operations of the '<em>Operation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_OPERATION_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.assembly.impl.AssemblyStorageImpl <em>Storage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.assembly.impl.AssemblyStorageImpl
	 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyStorage()
	 * @generated
	 */
	int ASSEMBLY_STORAGE = 5;

	/**
	 * The feature id for the '<em><b>Storage Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_STORAGE__STORAGE_TYPE = 0;

	/**
	 * The number of structural features of the '<em>Storage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_STORAGE_FEATURE_COUNT = 1;

	/**
	 * The operation id for the '<em>Get Assembly Component</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_STORAGE___GET_ASSEMBLY_COMPONENT = 0;

	/**
	 * The number of operations of the '<em>Storage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_STORAGE_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.assembly.impl.EStringToAssemblyStorageMapEntryImpl <em>EString To Assembly Storage Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.assembly.impl.EStringToAssemblyStorageMapEntryImpl
	 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getEStringToAssemblyStorageMapEntry()
	 * @generated
	 */
	int ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY = 6;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Assembly Storage Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EString To Assembly Storage Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.assembly.impl.AssemblyProvidedInterfaceImpl <em>Provided Interface</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.assembly.impl.AssemblyProvidedInterfaceImpl
	 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyProvidedInterface()
	 * @generated
	 */
	int ASSEMBLY_PROVIDED_INTERFACE = 7;

	/**
	 * The feature id for the '<em><b>Provided Operations</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_OPERATIONS = 0;

	/**
	 * The feature id for the '<em><b>Provided Interface Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE = 1;

	/**
	 * The number of structural features of the '<em>Provided Interface</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_PROVIDED_INTERFACE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Provided Interface</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_PROVIDED_INTERFACE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.assembly.impl.EStringToAssemblyProvidedInterfaceMapEntryImpl <em>EString To Assembly Provided Interface Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.assembly.impl.EStringToAssemblyProvidedInterfaceMapEntryImpl
	 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getEStringToAssemblyProvidedInterfaceMapEntry()
	 * @generated
	 */
	int ESTRING_TO_ASSEMBLY_PROVIDED_INTERFACE_MAP_ENTRY = 8;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_ASSEMBLY_PROVIDED_INTERFACE_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_ASSEMBLY_PROVIDED_INTERFACE_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Assembly Provided Interface Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_ASSEMBLY_PROVIDED_INTERFACE_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EString To Assembly Provided Interface Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_ASSEMBLY_PROVIDED_INTERFACE_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.assembly.impl.AssemblyRequiredInterfaceImpl <em>Required Interface</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.assembly.impl.AssemblyRequiredInterfaceImpl
	 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyRequiredInterface()
	 * @generated
	 */
	int ASSEMBLY_REQUIRED_INTERFACE = 9;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_REQUIRED_INTERFACE__REQUIRES = 0;

	/**
	 * The feature id for the '<em><b>Required Interface Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_REQUIRED_INTERFACE__REQUIRED_INTERFACE_TYPE = 1;

	/**
	 * The number of structural features of the '<em>Required Interface</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_REQUIRED_INTERFACE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Required Interface</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_REQUIRED_INTERFACE_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.assembly.AssemblyModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyModel
	 * @generated
	 */
	EClass getAssemblyModel();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.assembly.AssemblyModel#getAssemblyComponents <em>Assembly Components</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Assembly Components</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyModel#getAssemblyComponents()
	 * @see #getAssemblyModel()
	 * @generated
	 */
	EReference getAssemblyModel_AssemblyComponents();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Assembly Component Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EString To Assembly Component Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.model.analysismodel.assembly.AssemblyComponent" valueContainment="true"
	 * @generated
	 */
	EClass getEStringToAssemblyComponentMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToAssemblyComponentMapEntry()
	 * @generated
	 */
	EAttribute getEStringToAssemblyComponentMapEntry_Key();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToAssemblyComponentMapEntry()
	 * @generated
	 */
	EReference getEStringToAssemblyComponentMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.assembly.AssemblyComponent <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Component</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyComponent
	 * @generated
	 */
	EClass getAssemblyComponent();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.assembly.AssemblyComponent#getOperations <em>Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Operations</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyComponent#getOperations()
	 * @see #getAssemblyComponent()
	 * @generated
	 */
	EReference getAssemblyComponent_Operations();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.assembly.AssemblyComponent#getComponentType <em>Component Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Component Type</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyComponent#getComponentType()
	 * @see #getAssemblyComponent()
	 * @generated
	 */
	EReference getAssemblyComponent_ComponentType();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.assembly.AssemblyComponent#getStorages <em>Storages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Storages</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyComponent#getStorages()
	 * @see #getAssemblyComponent()
	 * @generated
	 */
	EReference getAssemblyComponent_Storages();

	/**
	 * Returns the meta object for the reference list '{@link kieker.model.analysismodel.assembly.AssemblyComponent#getContainedComponents <em>Contained Components</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Contained Components</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyComponent#getContainedComponents()
	 * @see #getAssemblyComponent()
	 * @generated
	 */
	EReference getAssemblyComponent_ContainedComponents();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.assembly.AssemblyComponent#getProvidedInterfaces <em>Provided Interfaces</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Provided Interfaces</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyComponent#getProvidedInterfaces()
	 * @see #getAssemblyComponent()
	 * @generated
	 */
	EReference getAssemblyComponent_ProvidedInterfaces();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.model.analysismodel.assembly.AssemblyComponent#getRequiredInterfaces <em>Required Interfaces</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Required Interfaces</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyComponent#getRequiredInterfaces()
	 * @see #getAssemblyComponent()
	 * @generated
	 */
	EReference getAssemblyComponent_RequiredInterfaces();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.assembly.AssemblyComponent#getSignature <em>Signature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Signature</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyComponent#getSignature()
	 * @see #getAssemblyComponent()
	 * @generated
	 */
	EAttribute getAssemblyComponent_Signature();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Assembly Operation Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EString To Assembly Operation Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.model.analysismodel.assembly.AssemblyOperation" valueContainment="true"
	 * @generated
	 */
	EClass getEStringToAssemblyOperationMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToAssemblyOperationMapEntry()
	 * @generated
	 */
	EAttribute getEStringToAssemblyOperationMapEntry_Key();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToAssemblyOperationMapEntry()
	 * @generated
	 */
	EReference getEStringToAssemblyOperationMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.assembly.AssemblyOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyOperation
	 * @generated
	 */
	EClass getAssemblyOperation();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.assembly.AssemblyOperation#getOperationType <em>Operation Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Operation Type</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyOperation#getOperationType()
	 * @see #getAssemblyOperation()
	 * @generated
	 */
	EReference getAssemblyOperation_OperationType();

	/**
	 * Returns the meta object for the '{@link kieker.model.analysismodel.assembly.AssemblyOperation#getAssemblyComponent() <em>Get Assembly Component</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Assembly Component</em>' operation.
	 * @see kieker.model.analysismodel.assembly.AssemblyOperation#getAssemblyComponent()
	 * @generated
	 */
	EOperation getAssemblyOperation__GetAssemblyComponent();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.assembly.AssemblyStorage <em>Storage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Storage</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyStorage
	 * @generated
	 */
	EClass getAssemblyStorage();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.assembly.AssemblyStorage#getStorageType <em>Storage Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Storage Type</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyStorage#getStorageType()
	 * @see #getAssemblyStorage()
	 * @generated
	 */
	EReference getAssemblyStorage_StorageType();

	/**
	 * Returns the meta object for the '{@link kieker.model.analysismodel.assembly.AssemblyStorage#getAssemblyComponent() <em>Get Assembly Component</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Assembly Component</em>' operation.
	 * @see kieker.model.analysismodel.assembly.AssemblyStorage#getAssemblyComponent()
	 * @generated
	 */
	EOperation getAssemblyStorage__GetAssemblyComponent();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Assembly Storage Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EString To Assembly Storage Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.model.analysismodel.assembly.AssemblyStorage" valueContainment="true"
	 * @generated
	 */
	EClass getEStringToAssemblyStorageMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToAssemblyStorageMapEntry()
	 * @generated
	 */
	EAttribute getEStringToAssemblyStorageMapEntry_Key();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToAssemblyStorageMapEntry()
	 * @generated
	 */
	EReference getEStringToAssemblyStorageMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.assembly.AssemblyProvidedInterface <em>Provided Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Provided Interface</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyProvidedInterface
	 * @generated
	 */
	EClass getAssemblyProvidedInterface();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.assembly.AssemblyProvidedInterface#getProvidedOperations <em>Provided Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Provided Operations</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyProvidedInterface#getProvidedOperations()
	 * @see #getAssemblyProvidedInterface()
	 * @generated
	 */
	EReference getAssemblyProvidedInterface_ProvidedOperations();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.assembly.AssemblyProvidedInterface#getProvidedInterfaceType <em>Provided Interface Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Provided Interface Type</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyProvidedInterface#getProvidedInterfaceType()
	 * @see #getAssemblyProvidedInterface()
	 * @generated
	 */
	EReference getAssemblyProvidedInterface_ProvidedInterfaceType();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Assembly Provided Interface Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EString To Assembly Provided Interface Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.model.analysismodel.assembly.AssemblyProvidedInterface"
	 * @generated
	 */
	EClass getEStringToAssemblyProvidedInterfaceMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToAssemblyProvidedInterfaceMapEntry()
	 * @generated
	 */
	EAttribute getEStringToAssemblyProvidedInterfaceMapEntry_Key();

	/**
	 * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToAssemblyProvidedInterfaceMapEntry()
	 * @generated
	 */
	EReference getEStringToAssemblyProvidedInterfaceMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.assembly.AssemblyRequiredInterface <em>Required Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Required Interface</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyRequiredInterface
	 * @generated
	 */
	EClass getAssemblyRequiredInterface();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.assembly.AssemblyRequiredInterface#getRequires <em>Requires</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Requires</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyRequiredInterface#getRequires()
	 * @see #getAssemblyRequiredInterface()
	 * @generated
	 */
	EReference getAssemblyRequiredInterface_Requires();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.assembly.AssemblyRequiredInterface#getRequiredInterfaceType <em>Required Interface Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Required Interface Type</em>'.
	 * @see kieker.model.analysismodel.assembly.AssemblyRequiredInterface#getRequiredInterfaceType()
	 * @see #getAssemblyRequiredInterface()
	 * @generated
	 */
	EReference getAssemblyRequiredInterface_RequiredInterfaceType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AssemblyFactory getAssemblyFactory();

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
		 * The meta object literal for the '{@link kieker.model.analysismodel.assembly.impl.AssemblyModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.assembly.impl.AssemblyModelImpl
		 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyModel()
		 * @generated
		 */
		EClass ASSEMBLY_MODEL = eINSTANCE.getAssemblyModel();

		/**
		 * The meta object literal for the '<em><b>Assembly Components</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_MODEL__ASSEMBLY_COMPONENTS = eINSTANCE.getAssemblyModel_AssemblyComponents();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.assembly.impl.EStringToAssemblyComponentMapEntryImpl <em>EString To Assembly Component Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.assembly.impl.EStringToAssemblyComponentMapEntryImpl
		 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getEStringToAssemblyComponentMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY = eINSTANCE.getEStringToAssemblyComponentMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__KEY = eINSTANCE.getEStringToAssemblyComponentMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__VALUE = eINSTANCE.getEStringToAssemblyComponentMapEntry_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl <em>Component</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl
		 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyComponent()
		 * @generated
		 */
		EClass ASSEMBLY_COMPONENT = eINSTANCE.getAssemblyComponent();

		/**
		 * The meta object literal for the '<em><b>Operations</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_COMPONENT__OPERATIONS = eINSTANCE.getAssemblyComponent_Operations();

		/**
		 * The meta object literal for the '<em><b>Component Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_COMPONENT__COMPONENT_TYPE = eINSTANCE.getAssemblyComponent_ComponentType();

		/**
		 * The meta object literal for the '<em><b>Storages</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_COMPONENT__STORAGES = eINSTANCE.getAssemblyComponent_Storages();

		/**
		 * The meta object literal for the '<em><b>Contained Components</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_COMPONENT__CONTAINED_COMPONENTS = eINSTANCE.getAssemblyComponent_ContainedComponents();

		/**
		 * The meta object literal for the '<em><b>Provided Interfaces</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_COMPONENT__PROVIDED_INTERFACES = eINSTANCE.getAssemblyComponent_ProvidedInterfaces();

		/**
		 * The meta object literal for the '<em><b>Required Interfaces</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_COMPONENT__REQUIRED_INTERFACES = eINSTANCE.getAssemblyComponent_RequiredInterfaces();

		/**
		 * The meta object literal for the '<em><b>Signature</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSEMBLY_COMPONENT__SIGNATURE = eINSTANCE.getAssemblyComponent_Signature();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.assembly.impl.EStringToAssemblyOperationMapEntryImpl <em>EString To Assembly Operation Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.assembly.impl.EStringToAssemblyOperationMapEntryImpl
		 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getEStringToAssemblyOperationMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY = eINSTANCE.getEStringToAssemblyOperationMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY__KEY = eINSTANCE.getEStringToAssemblyOperationMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY__VALUE = eINSTANCE.getEStringToAssemblyOperationMapEntry_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.assembly.impl.AssemblyOperationImpl <em>Operation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.assembly.impl.AssemblyOperationImpl
		 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyOperation()
		 * @generated
		 */
		EClass ASSEMBLY_OPERATION = eINSTANCE.getAssemblyOperation();

		/**
		 * The meta object literal for the '<em><b>Operation Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_OPERATION__OPERATION_TYPE = eINSTANCE.getAssemblyOperation_OperationType();

		/**
		 * The meta object literal for the '<em><b>Get Assembly Component</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ASSEMBLY_OPERATION___GET_ASSEMBLY_COMPONENT = eINSTANCE.getAssemblyOperation__GetAssemblyComponent();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.assembly.impl.AssemblyStorageImpl <em>Storage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.assembly.impl.AssemblyStorageImpl
		 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyStorage()
		 * @generated
		 */
		EClass ASSEMBLY_STORAGE = eINSTANCE.getAssemblyStorage();

		/**
		 * The meta object literal for the '<em><b>Storage Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_STORAGE__STORAGE_TYPE = eINSTANCE.getAssemblyStorage_StorageType();

		/**
		 * The meta object literal for the '<em><b>Get Assembly Component</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ASSEMBLY_STORAGE___GET_ASSEMBLY_COMPONENT = eINSTANCE.getAssemblyStorage__GetAssemblyComponent();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.assembly.impl.EStringToAssemblyStorageMapEntryImpl <em>EString To Assembly Storage Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.assembly.impl.EStringToAssemblyStorageMapEntryImpl
		 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getEStringToAssemblyStorageMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY = eINSTANCE.getEStringToAssemblyStorageMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY__KEY = eINSTANCE.getEStringToAssemblyStorageMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY__VALUE = eINSTANCE.getEStringToAssemblyStorageMapEntry_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.assembly.impl.AssemblyProvidedInterfaceImpl <em>Provided Interface</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.assembly.impl.AssemblyProvidedInterfaceImpl
		 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyProvidedInterface()
		 * @generated
		 */
		EClass ASSEMBLY_PROVIDED_INTERFACE = eINSTANCE.getAssemblyProvidedInterface();

		/**
		 * The meta object literal for the '<em><b>Provided Operations</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_OPERATIONS = eINSTANCE.getAssemblyProvidedInterface_ProvidedOperations();

		/**
		 * The meta object literal for the '<em><b>Provided Interface Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE = eINSTANCE.getAssemblyProvidedInterface_ProvidedInterfaceType();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.assembly.impl.EStringToAssemblyProvidedInterfaceMapEntryImpl <em>EString To Assembly Provided Interface Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.assembly.impl.EStringToAssemblyProvidedInterfaceMapEntryImpl
		 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getEStringToAssemblyProvidedInterfaceMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_ASSEMBLY_PROVIDED_INTERFACE_MAP_ENTRY = eINSTANCE.getEStringToAssemblyProvidedInterfaceMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTRING_TO_ASSEMBLY_PROVIDED_INTERFACE_MAP_ENTRY__KEY = eINSTANCE.getEStringToAssemblyProvidedInterfaceMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTRING_TO_ASSEMBLY_PROVIDED_INTERFACE_MAP_ENTRY__VALUE = eINSTANCE.getEStringToAssemblyProvidedInterfaceMapEntry_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.assembly.impl.AssemblyRequiredInterfaceImpl <em>Required Interface</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.assembly.impl.AssemblyRequiredInterfaceImpl
		 * @see kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyRequiredInterface()
		 * @generated
		 */
		EClass ASSEMBLY_REQUIRED_INTERFACE = eINSTANCE.getAssemblyRequiredInterface();

		/**
		 * The meta object literal for the '<em><b>Requires</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_REQUIRED_INTERFACE__REQUIRES = eINSTANCE.getAssemblyRequiredInterface_Requires();

		/**
		 * The meta object literal for the '<em><b>Required Interface Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_REQUIRED_INTERFACE__REQUIRED_INTERFACE_TYPE = eINSTANCE.getAssemblyRequiredInterface_RequiredInterfaceType();

	}

} //AssemblyPackage
