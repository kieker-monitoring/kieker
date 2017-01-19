/**
 */
package kieker.analysisteetime.model.analysismodel.assembly;

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
 * @see kieker.analysisteetime.model.analysismodel.assembly.AssemblyFactory
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
	AssemblyPackage eINSTANCE = kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyPackageImpl.init();

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyRootImpl <em>Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyRootImpl
	 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyRoot()
	 * @generated
	 */
	int ASSEMBLY_ROOT = 0;

	/**
	 * The feature id for the '<em><b>Assembly Components</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_ROOT__ASSEMBLY_COMPONENTS = 0;

	/**
	 * The number of structural features of the '<em>Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_ROOT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_ROOT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.assembly.impl.EStringToAssemblyComponentMapEntryImpl <em>EString To Assembly Component Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.EStringToAssemblyComponentMapEntryImpl
	 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyPackageImpl#getEStringToAssemblyComponentMapEntry()
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
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyComponentImpl <em>Component</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyComponentImpl
	 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyComponent()
	 * @generated
	 */
	int ASSEMBLY_COMPONENT = 2;

	/**
	 * The feature id for the '<em><b>Assembly Operations</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_COMPONENT__ASSEMBLY_OPERATIONS = 0;

	/**
	 * The feature id for the '<em><b>Component Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_COMPONENT__COMPONENT_TYPE = 1;

	/**
	 * The number of structural features of the '<em>Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_COMPONENT_FEATURE_COUNT = 2;

	/**
	 * The operation id for the '<em>Get Assembly Root</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_COMPONENT___GET_ASSEMBLY_ROOT = 0;

	/**
	 * The number of operations of the '<em>Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_COMPONENT_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.assembly.impl.EStringToAssemblyOperationMapEntryImpl <em>EString To Assembly Operation Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.EStringToAssemblyOperationMapEntryImpl
	 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyPackageImpl#getEStringToAssemblyOperationMapEntry()
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
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyOperationImpl <em>Operation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyOperationImpl
	 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyOperation()
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
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.assembly.AssemblyRoot <em>Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Root</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.assembly.AssemblyRoot
	 * @generated
	 */
	EClass getAssemblyRoot();

	/**
	 * Returns the meta object for the map '{@link kieker.analysisteetime.model.analysismodel.assembly.AssemblyRoot#getAssemblyComponents <em>Assembly Components</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Assembly Components</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.assembly.AssemblyRoot#getAssemblyComponents()
	 * @see #getAssemblyRoot()
	 * @generated
	 */
	EReference getAssemblyRoot_AssemblyComponents();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Assembly Component Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EString To Assembly Component Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent" valueContainment="true"
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
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Component</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent
	 * @generated
	 */
	EClass getAssemblyComponent();

	/**
	 * Returns the meta object for the map '{@link kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent#getAssemblyOperations <em>Assembly Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Assembly Operations</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent#getAssemblyOperations()
	 * @see #getAssemblyComponent()
	 * @generated
	 */
	EReference getAssemblyComponent_AssemblyOperations();

	/**
	 * Returns the meta object for the reference '{@link kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent#getComponentType <em>Component Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Component Type</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent#getComponentType()
	 * @see #getAssemblyComponent()
	 * @generated
	 */
	EReference getAssemblyComponent_ComponentType();

	/**
	 * Returns the meta object for the '{@link kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent#getAssemblyRoot() <em>Get Assembly Root</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Assembly Root</em>' operation.
	 * @see kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent#getAssemblyRoot()
	 * @generated
	 */
	EOperation getAssemblyComponent__GetAssemblyRoot();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Assembly Operation Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EString To Assembly Operation Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.analysisteetime.model.analysismodel.assembly.AssemblyOperation" valueContainment="true"
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
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.assembly.AssemblyOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.assembly.AssemblyOperation
	 * @generated
	 */
	EClass getAssemblyOperation();

	/**
	 * Returns the meta object for the reference '{@link kieker.analysisteetime.model.analysismodel.assembly.AssemblyOperation#getOperationType <em>Operation Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Operation Type</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.assembly.AssemblyOperation#getOperationType()
	 * @see #getAssemblyOperation()
	 * @generated
	 */
	EReference getAssemblyOperation_OperationType();

	/**
	 * Returns the meta object for the '{@link kieker.analysisteetime.model.analysismodel.assembly.AssemblyOperation#getAssemblyComponent() <em>Get Assembly Component</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Assembly Component</em>' operation.
	 * @see kieker.analysisteetime.model.analysismodel.assembly.AssemblyOperation#getAssemblyComponent()
	 * @generated
	 */
	EOperation getAssemblyOperation__GetAssemblyComponent();

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
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyRootImpl <em>Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyRootImpl
		 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyRoot()
		 * @generated
		 */
		EClass ASSEMBLY_ROOT = eINSTANCE.getAssemblyRoot();

		/**
		 * The meta object literal for the '<em><b>Assembly Components</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_ROOT__ASSEMBLY_COMPONENTS = eINSTANCE.getAssemblyRoot_AssemblyComponents();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.assembly.impl.EStringToAssemblyComponentMapEntryImpl <em>EString To Assembly Component Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.EStringToAssemblyComponentMapEntryImpl
		 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyPackageImpl#getEStringToAssemblyComponentMapEntry()
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
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyComponentImpl <em>Component</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyComponentImpl
		 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyComponent()
		 * @generated
		 */
		EClass ASSEMBLY_COMPONENT = eINSTANCE.getAssemblyComponent();

		/**
		 * The meta object literal for the '<em><b>Assembly Operations</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_COMPONENT__ASSEMBLY_OPERATIONS = eINSTANCE.getAssemblyComponent_AssemblyOperations();

		/**
		 * The meta object literal for the '<em><b>Component Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_COMPONENT__COMPONENT_TYPE = eINSTANCE.getAssemblyComponent_ComponentType();

		/**
		 * The meta object literal for the '<em><b>Get Assembly Root</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ASSEMBLY_COMPONENT___GET_ASSEMBLY_ROOT = eINSTANCE.getAssemblyComponent__GetAssemblyRoot();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.assembly.impl.EStringToAssemblyOperationMapEntryImpl <em>EString To Assembly Operation Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.EStringToAssemblyOperationMapEntryImpl
		 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyPackageImpl#getEStringToAssemblyOperationMapEntry()
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
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyOperationImpl <em>Operation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyOperationImpl
		 * @see kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyPackageImpl#getAssemblyOperation()
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

	}

} //AssemblyPackage
