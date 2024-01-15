/**
 */
package kieker.model.collection.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import kieker.model.analysismodel.AnalysismodelPackage;
import kieker.model.analysismodel.type.TypePackage;
import kieker.model.collection.CollectionFactory;
import kieker.model.collection.CollectionPackage;
import kieker.model.collection.Connections;
import kieker.model.collection.Coupling;
import kieker.model.collection.OperationCollection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class CollectionPackageImpl extends EPackageImpl implements CollectionPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass connectionsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass operationCollectionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass couplingToOperationMapEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass nameToOperationMapEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass couplingEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see kieker.model.collection.CollectionPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private CollectionPackageImpl() {
		super(eNS_URI, CollectionFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>
	 * This method is used to initialize {@link CollectionPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static CollectionPackage init() {
		if (isInited) {
			return (CollectionPackage) EPackage.Registry.INSTANCE.getEPackage(CollectionPackage.eNS_URI);
		}

		// Obtain or create and register package
		final Object registeredCollectionPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		final CollectionPackageImpl theCollectionPackage = registeredCollectionPackage instanceof CollectionPackageImpl
				? (CollectionPackageImpl) registeredCollectionPackage
				: new CollectionPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		AnalysismodelPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theCollectionPackage.createPackageContents();

		// Initialize created meta-data
		theCollectionPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theCollectionPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(CollectionPackage.eNS_URI, theCollectionPackage);
		return theCollectionPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getConnections() {
		return this.connectionsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getConnections_Connections() {
		return (EReference) this.connectionsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getOperationCollection() {
		return this.operationCollectionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getOperationCollection_Caller() {
		return (EReference) this.operationCollectionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getOperationCollection_Callee() {
		return (EReference) this.operationCollectionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getOperationCollection_Operations() {
		return (EReference) this.operationCollectionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getCouplingToOperationMap() {
		return this.couplingToOperationMapEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getCouplingToOperationMap_Key() {
		return (EReference) this.couplingToOperationMapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getCouplingToOperationMap_Value() {
		return (EReference) this.couplingToOperationMapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getNameToOperationMap() {
		return this.nameToOperationMapEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getNameToOperationMap_Key() {
		return (EAttribute) this.nameToOperationMapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getNameToOperationMap_Value() {
		return (EReference) this.nameToOperationMapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getCoupling() {
		return this.couplingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getCoupling_Caller() {
		return (EReference) this.couplingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getCoupling_Callee() {
		return (EReference) this.couplingEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EOperation getCoupling__Equals__Object() {
		return this.couplingEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EOperation getCoupling__HashCode() {
		return this.couplingEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public CollectionFactory getCollectionFactory() {
		return (CollectionFactory) this.getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void createPackageContents() {
		if (this.isCreated) {
			return;
		}
		this.isCreated = true;

		// Create classes and their features
		this.connectionsEClass = this.createEClass(CONNECTIONS);
		this.createEReference(this.connectionsEClass, CONNECTIONS__CONNECTIONS);

		this.operationCollectionEClass = this.createEClass(OPERATION_COLLECTION);
		this.createEReference(this.operationCollectionEClass, OPERATION_COLLECTION__CALLER);
		this.createEReference(this.operationCollectionEClass, OPERATION_COLLECTION__CALLEE);
		this.createEReference(this.operationCollectionEClass, OPERATION_COLLECTION__OPERATIONS);

		this.couplingToOperationMapEClass = this.createEClass(COUPLING_TO_OPERATION_MAP);
		this.createEReference(this.couplingToOperationMapEClass, COUPLING_TO_OPERATION_MAP__KEY);
		this.createEReference(this.couplingToOperationMapEClass, COUPLING_TO_OPERATION_MAP__VALUE);

		this.nameToOperationMapEClass = this.createEClass(NAME_TO_OPERATION_MAP);
		this.createEAttribute(this.nameToOperationMapEClass, NAME_TO_OPERATION_MAP__KEY);
		this.createEReference(this.nameToOperationMapEClass, NAME_TO_OPERATION_MAP__VALUE);

		this.couplingEClass = this.createEClass(COUPLING);
		this.createEReference(this.couplingEClass, COUPLING__CALLER);
		this.createEReference(this.couplingEClass, COUPLING__CALLEE);
		this.createEOperation(this.couplingEClass, COUPLING___EQUALS__OBJECT);
		this.createEOperation(this.couplingEClass, COUPLING___HASH_CODE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void initializePackageContents() {
		if (this.isInitialized) {
			return;
		}
		this.isInitialized = true;

		// Initialize package
		this.setName(eNAME);
		this.setNsPrefix(eNS_PREFIX);
		this.setNsURI(eNS_URI);

		// Obtain other dependent packages
		final TypePackage theTypePackage = (TypePackage) EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		this.initEClass(this.connectionsEClass, Connections.class, "Connections", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getConnections_Connections(), this.getCouplingToOperationMap(), null, "connections", null, 0, -1, Connections.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.operationCollectionEClass, OperationCollection.class, "OperationCollection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getOperationCollection_Caller(), theTypePackage.getComponentType(), null, "caller", null, 0, 1, OperationCollection.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getOperationCollection_Callee(), theTypePackage.getComponentType(), null, "callee", null, 0, 1, OperationCollection.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getOperationCollection_Operations(), this.getNameToOperationMap(), null, "operations", null, 0, -1, OperationCollection.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.couplingToOperationMapEClass, Map.Entry.class, "CouplingToOperationMap", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getCouplingToOperationMap_Key(), this.getCoupling(), null, "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getCouplingToOperationMap_Value(), this.getOperationCollection(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.nameToOperationMapEClass, Map.Entry.class, "NameToOperationMap", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getNameToOperationMap_Key(), this.ecorePackage.getEString(), "key", null, 1, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getNameToOperationMap_Value(), theTypePackage.getOperationType(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.couplingEClass, Coupling.class, "Coupling", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getCoupling_Caller(), theTypePackage.getComponentType(), null, "caller", null, 0, 1, Coupling.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getCoupling_Callee(), theTypePackage.getComponentType(), null, "callee", null, 0, 1, Coupling.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		final EOperation op = this.initEOperation(this.getCoupling__Equals__Object(), this.ecorePackage.getEBoolean(), "equals", 0, 1, IS_UNIQUE, IS_ORDERED);
		this.addEParameter(op, this.ecorePackage.getEJavaObject(), "value", 1, 1, IS_UNIQUE, IS_ORDERED);

		this.initEOperation(this.getCoupling__HashCode(), this.ecorePackage.getEInt(), "hashCode", 0, 1, IS_UNIQUE, IS_ORDERED);

		// Create resource
		this.createResource(eNS_URI);
	}

} // CollectionPackageImpl
