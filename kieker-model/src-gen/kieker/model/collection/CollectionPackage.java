/**
 */
package kieker.model.collection;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see kieker.model.collection.CollectionFactory
 * @model kind="package"
 * @generated
 */
public interface CollectionPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "collection";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://kieker-monitoring.net/collection";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "collection";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CollectionPackage eINSTANCE = kieker.model.collection.impl.CollectionPackageImpl.init();

	/**
	 * The meta object id for the '{@link kieker.model.collection.impl.InterfaceCollectionImpl <em>Interface Collection</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.collection.impl.InterfaceCollectionImpl
	 * @see kieker.model.collection.impl.CollectionPackageImpl#getInterfaceCollection()
	 * @generated
	 */
	int INTERFACE_COLLECTION = 0;

	/**
	 * The number of structural features of the '<em>Interface Collection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERFACE_COLLECTION_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Interface Collection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTERFACE_COLLECTION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.collection.impl.OperationCollectionImpl <em>Operation Collection</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.collection.impl.OperationCollectionImpl
	 * @see kieker.model.collection.impl.CollectionPackageImpl#getOperationCollection()
	 * @generated
	 */
	int OPERATION_COLLECTION = 1;

	/**
	 * The feature id for the '<em><b>Caller</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_COLLECTION__CALLER = 0;

	/**
	 * The feature id for the '<em><b>Callee</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_COLLECTION__CALLEE = 1;

	/**
	 * The feature id for the '<em><b>Operations</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_COLLECTION__OPERATIONS = 2;

	/**
	 * The number of structural features of the '<em>Operation Collection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_COLLECTION_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Operation Collection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_COLLECTION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.collection.impl.CouplingToOperationMapImpl <em>Coupling To Operation Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.collection.impl.CouplingToOperationMapImpl
	 * @see kieker.model.collection.impl.CollectionPackageImpl#getCouplingToOperationMap()
	 * @generated
	 */
	int COUPLING_TO_OPERATION_MAP = 2;

	/**
	 * The feature id for the '<em><b>Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUPLING_TO_OPERATION_MAP__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUPLING_TO_OPERATION_MAP__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Coupling To Operation Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUPLING_TO_OPERATION_MAP_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Coupling To Operation Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUPLING_TO_OPERATION_MAP_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.collection.impl.NameToOperationMapImpl <em>Name To Operation Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.collection.impl.NameToOperationMapImpl
	 * @see kieker.model.collection.impl.CollectionPackageImpl#getNameToOperationMap()
	 * @generated
	 */
	int NAME_TO_OPERATION_MAP = 3;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAME_TO_OPERATION_MAP__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAME_TO_OPERATION_MAP__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Name To Operation Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAME_TO_OPERATION_MAP_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Name To Operation Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAME_TO_OPERATION_MAP_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.collection.impl.CouplingImpl <em>Coupling</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.collection.impl.CouplingImpl
	 * @see kieker.model.collection.impl.CollectionPackageImpl#getCoupling()
	 * @generated
	 */
	int COUPLING = 4;

	/**
	 * The feature id for the '<em><b>Caller</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUPLING__CALLER = 0;

	/**
	 * The feature id for the '<em><b>Callee</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUPLING__CALLEE = 1;

	/**
	 * The number of structural features of the '<em>Coupling</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUPLING_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Coupling</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUPLING_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link kieker.model.collection.InterfaceCollection <em>Interface Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Interface Collection</em>'.
	 * @see kieker.model.collection.InterfaceCollection
	 * @generated
	 */
	EClass getInterfaceCollection();

	/**
	 * Returns the meta object for class '{@link kieker.model.collection.OperationCollection <em>Operation Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation Collection</em>'.
	 * @see kieker.model.collection.OperationCollection
	 * @generated
	 */
	EClass getOperationCollection();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.collection.OperationCollection#getCaller <em>Caller</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Caller</em>'.
	 * @see kieker.model.collection.OperationCollection#getCaller()
	 * @see #getOperationCollection()
	 * @generated
	 */
	EReference getOperationCollection_Caller();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.collection.OperationCollection#getCallee <em>Callee</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Callee</em>'.
	 * @see kieker.model.collection.OperationCollection#getCallee()
	 * @see #getOperationCollection()
	 * @generated
	 */
	EReference getOperationCollection_Callee();

	/**
	 * Returns the meta object for the map '{@link kieker.model.collection.OperationCollection#getOperations <em>Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Operations</em>'.
	 * @see kieker.model.collection.OperationCollection#getOperations()
	 * @see #getOperationCollection()
	 * @generated
	 */
	EReference getOperationCollection_Operations();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Coupling To Operation Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Coupling To Operation Map</em>'.
	 * @see java.util.Map.Entry
	 * @model keyType="kieker.model.collection.Coupling" keyContainment="true"
	 *        valueMapType="kieker.model.collection.NameToOperationMap&lt;org.eclipse.emf.ecore.EString, kieker.model.analysismodel.type.OperationType&gt;"
	 * @generated
	 */
	EClass getCouplingToOperationMap();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getCouplingToOperationMap()
	 * @generated
	 */
	EReference getCouplingToOperationMap_Key();

	/**
	 * Returns the meta object for the map '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getCouplingToOperationMap()
	 * @generated
	 */
	EReference getCouplingToOperationMap_Value();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Name To Operation Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Name To Operation Map</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString" keyRequired="true"
	 *        valueType="kieker.model.analysismodel.type.OperationType"
	 * @generated
	 */
	EClass getNameToOperationMap();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getNameToOperationMap()
	 * @generated
	 */
	EAttribute getNameToOperationMap_Key();

	/**
	 * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getNameToOperationMap()
	 * @generated
	 */
	EReference getNameToOperationMap_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.collection.Coupling <em>Coupling</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Coupling</em>'.
	 * @see kieker.model.collection.Coupling
	 * @generated
	 */
	EClass getCoupling();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.collection.Coupling#getCaller <em>Caller</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Caller</em>'.
	 * @see kieker.model.collection.Coupling#getCaller()
	 * @see #getCoupling()
	 * @generated
	 */
	EReference getCoupling_Caller();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.collection.Coupling#getCallee <em>Callee</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Callee</em>'.
	 * @see kieker.model.collection.Coupling#getCallee()
	 * @see #getCoupling()
	 * @generated
	 */
	EReference getCoupling_Callee();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CollectionFactory getCollectionFactory();

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
		 * The meta object literal for the '{@link kieker.model.collection.impl.InterfaceCollectionImpl <em>Interface Collection</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.collection.impl.InterfaceCollectionImpl
		 * @see kieker.model.collection.impl.CollectionPackageImpl#getInterfaceCollection()
		 * @generated
		 */
		EClass INTERFACE_COLLECTION = eINSTANCE.getInterfaceCollection();

		/**
		 * The meta object literal for the '{@link kieker.model.collection.impl.OperationCollectionImpl <em>Operation Collection</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.collection.impl.OperationCollectionImpl
		 * @see kieker.model.collection.impl.CollectionPackageImpl#getOperationCollection()
		 * @generated
		 */
		EClass OPERATION_COLLECTION = eINSTANCE.getOperationCollection();

		/**
		 * The meta object literal for the '<em><b>Caller</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_COLLECTION__CALLER = eINSTANCE.getOperationCollection_Caller();

		/**
		 * The meta object literal for the '<em><b>Callee</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_COLLECTION__CALLEE = eINSTANCE.getOperationCollection_Callee();

		/**
		 * The meta object literal for the '<em><b>Operations</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_COLLECTION__OPERATIONS = eINSTANCE.getOperationCollection_Operations();

		/**
		 * The meta object literal for the '{@link kieker.model.collection.impl.CouplingToOperationMapImpl <em>Coupling To Operation Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.collection.impl.CouplingToOperationMapImpl
		 * @see kieker.model.collection.impl.CollectionPackageImpl#getCouplingToOperationMap()
		 * @generated
		 */
		EClass COUPLING_TO_OPERATION_MAP = eINSTANCE.getCouplingToOperationMap();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COUPLING_TO_OPERATION_MAP__KEY = eINSTANCE.getCouplingToOperationMap_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COUPLING_TO_OPERATION_MAP__VALUE = eINSTANCE.getCouplingToOperationMap_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.collection.impl.NameToOperationMapImpl <em>Name To Operation Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.collection.impl.NameToOperationMapImpl
		 * @see kieker.model.collection.impl.CollectionPackageImpl#getNameToOperationMap()
		 * @generated
		 */
		EClass NAME_TO_OPERATION_MAP = eINSTANCE.getNameToOperationMap();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAME_TO_OPERATION_MAP__KEY = eINSTANCE.getNameToOperationMap_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NAME_TO_OPERATION_MAP__VALUE = eINSTANCE.getNameToOperationMap_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.collection.impl.CouplingImpl <em>Coupling</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.collection.impl.CouplingImpl
		 * @see kieker.model.collection.impl.CollectionPackageImpl#getCoupling()
		 * @generated
		 */
		EClass COUPLING = eINSTANCE.getCoupling();

		/**
		 * The meta object literal for the '<em><b>Caller</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COUPLING__CALLER = eINSTANCE.getCoupling_Caller();

		/**
		 * The meta object literal for the '<em><b>Callee</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COUPLING__CALLEE = eINSTANCE.getCoupling_Callee();

	}

} //CollectionPackage
