/**
 */
package kieker.analysisteetime.model.analysismodel.type;

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
 * @see kieker.analysisteetime.model.analysismodel.type.TypeFactory
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
	TypePackage eINSTANCE = kieker.analysisteetime.model.analysismodel.type.impl.TypePackageImpl.init();

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.type.impl.TypeModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.type.impl.TypeModelImpl
	 * @see kieker.analysisteetime.model.analysismodel.type.impl.TypePackageImpl#getTypeModel()
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
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.type.impl.EStringToComponentTypeMapEntryImpl <em>EString To Component Type Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.type.impl.EStringToComponentTypeMapEntryImpl
	 * @see kieker.analysisteetime.model.analysismodel.type.impl.TypePackageImpl#getEStringToComponentTypeMapEntry()
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
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.type.impl.ComponentTypeImpl <em>Component Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.type.impl.ComponentTypeImpl
	 * @see kieker.analysisteetime.model.analysismodel.type.impl.TypePackageImpl#getComponentType()
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
	 * The number of structural features of the '<em>Component Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Component Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.type.impl.EStringToOperationTypeMapEntryImpl <em>EString To Operation Type Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.type.impl.EStringToOperationTypeMapEntryImpl
	 * @see kieker.analysisteetime.model.analysismodel.type.impl.TypePackageImpl#getEStringToOperationTypeMapEntry()
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
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.type.impl.OperationTypeImpl <em>Operation Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.type.impl.OperationTypeImpl
	 * @see kieker.analysisteetime.model.analysismodel.type.impl.TypePackageImpl#getOperationType()
	 * @generated
	 */
	int OPERATION_TYPE = 4;

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
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.type.TypeModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.type.TypeModel
	 * @generated
	 */
	EClass getTypeModel();

	/**
	 * Returns the meta object for the map '{@link kieker.analysisteetime.model.analysismodel.type.TypeModel#getComponentTypes <em>Component Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Component Types</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.type.TypeModel#getComponentTypes()
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
	 *        valueType="kieker.analysisteetime.model.analysismodel.type.ComponentType" valueContainment="true"
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
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.type.ComponentType <em>Component Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Component Type</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.type.ComponentType
	 * @generated
	 */
	EClass getComponentType();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysisteetime.model.analysismodel.type.ComponentType#getSignature <em>Signature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Signature</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.type.ComponentType#getSignature()
	 * @see #getComponentType()
	 * @generated
	 */
	EAttribute getComponentType_Signature();

	/**
	 * Returns the meta object for the map '{@link kieker.analysisteetime.model.analysismodel.type.ComponentType#getProvidedOperations <em>Provided Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Provided Operations</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.type.ComponentType#getProvidedOperations()
	 * @see #getComponentType()
	 * @generated
	 */
	EReference getComponentType_ProvidedOperations();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysisteetime.model.analysismodel.type.ComponentType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.type.ComponentType#getName()
	 * @see #getComponentType()
	 * @generated
	 */
	EAttribute getComponentType_Name();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysisteetime.model.analysismodel.type.ComponentType#getPackage <em>Package</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Package</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.type.ComponentType#getPackage()
	 * @see #getComponentType()
	 * @generated
	 */
	EAttribute getComponentType_Package();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Operation Type Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EString To Operation Type Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.analysisteetime.model.analysismodel.type.OperationType" valueContainment="true"
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
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.type.OperationType <em>Operation Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation Type</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.type.OperationType
	 * @generated
	 */
	EClass getOperationType();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysisteetime.model.analysismodel.type.OperationType#getSignature <em>Signature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Signature</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.type.OperationType#getSignature()
	 * @see #getOperationType()
	 * @generated
	 */
	EAttribute getOperationType_Signature();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysisteetime.model.analysismodel.type.OperationType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.type.OperationType#getName()
	 * @see #getOperationType()
	 * @generated
	 */
	EAttribute getOperationType_Name();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysisteetime.model.analysismodel.type.OperationType#getReturnType <em>Return Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Return Type</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.type.OperationType#getReturnType()
	 * @see #getOperationType()
	 * @generated
	 */
	EAttribute getOperationType_ReturnType();

	/**
	 * Returns the meta object for the attribute list '{@link kieker.analysisteetime.model.analysismodel.type.OperationType#getModifiers <em>Modifiers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Modifiers</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.type.OperationType#getModifiers()
	 * @see #getOperationType()
	 * @generated
	 */
	EAttribute getOperationType_Modifiers();

	/**
	 * Returns the meta object for the attribute list '{@link kieker.analysisteetime.model.analysismodel.type.OperationType#getParameterTypes <em>Parameter Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Parameter Types</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.type.OperationType#getParameterTypes()
	 * @see #getOperationType()
	 * @generated
	 */
	EAttribute getOperationType_ParameterTypes();

	/**
	 * Returns the meta object for the '{@link kieker.analysisteetime.model.analysismodel.type.OperationType#getComponentType() <em>Get Component Type</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Component Type</em>' operation.
	 * @see kieker.analysisteetime.model.analysismodel.type.OperationType#getComponentType()
	 * @generated
	 */
	EOperation getOperationType__GetComponentType();

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
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.type.impl.TypeModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.type.impl.TypeModelImpl
		 * @see kieker.analysisteetime.model.analysismodel.type.impl.TypePackageImpl#getTypeModel()
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
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.type.impl.EStringToComponentTypeMapEntryImpl <em>EString To Component Type Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.type.impl.EStringToComponentTypeMapEntryImpl
		 * @see kieker.analysisteetime.model.analysismodel.type.impl.TypePackageImpl#getEStringToComponentTypeMapEntry()
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
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.type.impl.ComponentTypeImpl <em>Component Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.type.impl.ComponentTypeImpl
		 * @see kieker.analysisteetime.model.analysismodel.type.impl.TypePackageImpl#getComponentType()
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
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.type.impl.EStringToOperationTypeMapEntryImpl <em>EString To Operation Type Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.type.impl.EStringToOperationTypeMapEntryImpl
		 * @see kieker.analysisteetime.model.analysismodel.type.impl.TypePackageImpl#getEStringToOperationTypeMapEntry()
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
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.type.impl.OperationTypeImpl <em>Operation Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.type.impl.OperationTypeImpl
		 * @see kieker.analysisteetime.model.analysismodel.type.impl.TypePackageImpl#getOperationType()
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

	}

} //TypePackage
