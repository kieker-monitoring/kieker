/**
 */
package kieker.model.collection;

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
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
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
	 * The meta object id for the '{@link kieker.model.collection.impl.ConnectionsImpl <em>Connections</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.collection.impl.ConnectionsImpl
	 * @see kieker.model.collection.impl.CollectionPackageImpl#getConnections()
	 * @generated
	 */
	int CONNECTIONS = 0;

	/**
	 * The feature id for the '<em><b>Connections</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTIONS__CONNECTIONS = 0;

	/**
	 * The number of structural features of the '<em>Connections</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTIONS_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Connections</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTIONS_OPERATION_COUNT = 0;

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
	 * The feature id for the '<em><b>Required</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_COLLECTION__REQUIRED = 0;

	/**
	 * The feature id for the '<em><b>Provided</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_COLLECTION__PROVIDED = 1;

	/**
	 * The feature id for the '<em><b>Callees</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_COLLECTION__CALLEES = 2;

	/**
	 * The feature id for the '<em><b>Callers</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_COLLECTION__CALLERS = 3;

	/**
	 * The number of structural features of the '<em>Operation Collection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_COLLECTION_FEATURE_COUNT = 4;

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
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
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
	 * The feature id for the '<em><b>Required</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUPLING__REQUIRED = 0;

	/**
	 * The feature id for the '<em><b>Provided</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUPLING__PROVIDED = 1;

	/**
	 * The number of structural features of the '<em>Coupling</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUPLING_FEATURE_COUNT = 2;

	/**
	 * The operation id for the '<em>Equals</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUPLING___EQUALS__OBJECT = 0;

	/**
	 * The operation id for the '<em>Hash Code</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUPLING___HASH_CODE = 1;

	/**
	 * The number of operations of the '<em>Coupling</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUPLING_OPERATION_COUNT = 2;

	/**
	 * Returns the meta object for class '{@link kieker.model.collection.Connections <em>Connections</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Connections</em>'.
	 * @see kieker.model.collection.Connections
	 * @generated
	 */
	EClass getConnections();

	/**
	 * Returns the meta object for the map '{@link kieker.model.collection.Connections#getConnections <em>Connections</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Connections</em>'.
	 * @see kieker.model.collection.Connections#getConnections()
	 * @see #getConnections()
	 * @generated
	 */
	EReference getConnections_Connections();

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
	 * Returns the meta object for the reference '{@link kieker.model.collection.OperationCollection#getRequired <em>Required</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Required</em>'.
	 * @see kieker.model.collection.OperationCollection#getRequired()
	 * @see #getOperationCollection()
	 * @generated
	 */
	EReference getOperationCollection_Required();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.collection.OperationCollection#getProvided <em>Provided</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Provided</em>'.
	 * @see kieker.model.collection.OperationCollection#getProvided()
	 * @see #getOperationCollection()
	 * @generated
	 */
	EReference getOperationCollection_Provided();

	/**
	 * Returns the meta object for the map '{@link kieker.model.collection.OperationCollection#getCallees <em>Callees</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Callees</em>'.
	 * @see kieker.model.collection.OperationCollection#getCallees()
	 * @see #getOperationCollection()
	 * @generated
	 */
	EReference getOperationCollection_Callees();

	/**
	 * Returns the meta object for the map '{@link kieker.model.collection.OperationCollection#getCallers <em>Callers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Callers</em>'.
	 * @see kieker.model.collection.OperationCollection#getCallers()
	 * @see #getOperationCollection()
	 * @generated
	 */
	EReference getOperationCollection_Callers();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Coupling To Operation Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Coupling To Operation Map</em>'.
	 * @see java.util.Map.Entry
	 * @model keyType="kieker.model.collection.Coupling" keyContainment="true"
	 *        valueType="kieker.model.collection.OperationCollection" valueContainment="true"
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
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
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
	 * Returns the meta object for the reference '{@link kieker.model.collection.Coupling#getRequired <em>Required</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Required</em>'.
	 * @see kieker.model.collection.Coupling#getRequired()
	 * @see #getCoupling()
	 * @generated
	 */
	EReference getCoupling_Required();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.collection.Coupling#getProvided <em>Provided</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Provided</em>'.
	 * @see kieker.model.collection.Coupling#getProvided()
	 * @see #getCoupling()
	 * @generated
	 */
	EReference getCoupling_Provided();

	/**
	 * Returns the meta object for the '{@link kieker.model.collection.Coupling#equals(java.lang.Object) <em>Equals</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Equals</em>' operation.
	 * @see kieker.model.collection.Coupling#equals(java.lang.Object)
	 * @generated
	 */
	EOperation getCoupling__Equals__Object();

	/**
	 * Returns the meta object for the '{@link kieker.model.collection.Coupling#hashCode() <em>Hash Code</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Hash Code</em>' operation.
	 * @see kieker.model.collection.Coupling#hashCode()
	 * @generated
	 */
	EOperation getCoupling__HashCode();

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
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each operation of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link kieker.model.collection.impl.ConnectionsImpl <em>Connections</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.collection.impl.ConnectionsImpl
		 * @see kieker.model.collection.impl.CollectionPackageImpl#getConnections()
		 * @generated
		 */
		EClass CONNECTIONS = eINSTANCE.getConnections();

		/**
		 * The meta object literal for the '<em><b>Connections</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONNECTIONS__CONNECTIONS = eINSTANCE.getConnections_Connections();

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
		 * The meta object literal for the '<em><b>Required</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_COLLECTION__REQUIRED = eINSTANCE.getOperationCollection_Required();

		/**
		 * The meta object literal for the '<em><b>Provided</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_COLLECTION__PROVIDED = eINSTANCE.getOperationCollection_Provided();

		/**
		 * The meta object literal for the '<em><b>Callees</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_COLLECTION__CALLEES = eINSTANCE.getOperationCollection_Callees();

		/**
		 * The meta object literal for the '<em><b>Callers</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_COLLECTION__CALLERS = eINSTANCE.getOperationCollection_Callers();

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
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
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
		 * The meta object literal for the '<em><b>Required</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COUPLING__REQUIRED = eINSTANCE.getCoupling_Required();

		/**
		 * The meta object literal for the '<em><b>Provided</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COUPLING__PROVIDED = eINSTANCE.getCoupling_Provided();

		/**
		 * The meta object literal for the '<em><b>Equals</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation COUPLING___EQUALS__OBJECT = eINSTANCE.getCoupling__Equals__Object();

		/**
		 * The meta object literal for the '<em><b>Hash Code</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation COUPLING___HASH_CODE = eINSTANCE.getCoupling__HashCode();

	}

} // CollectionPackage
