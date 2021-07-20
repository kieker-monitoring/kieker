/**
 */
package kieker.model.analysismodel.assembly.impl;

import java.util.Map;

import kieker.model.analysismodel.AnalysismodelPackage;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.AssemblyStorage;

import kieker.model.analysismodel.deployment.DeploymentPackage;

import kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl;

import kieker.model.analysismodel.execution.ExecutionPackage;

import kieker.model.analysismodel.execution.impl.ExecutionPackageImpl;

import kieker.model.analysismodel.impl.AnalysismodelPackageImpl;

import kieker.model.analysismodel.sources.SourcesPackage;
import kieker.model.analysismodel.sources.impl.SourcesPackageImpl;
import kieker.model.analysismodel.statistics.StatisticsPackage;

import kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl;

import kieker.model.analysismodel.trace.TracePackage;

import kieker.model.analysismodel.trace.impl.TracePackageImpl;

import kieker.model.analysismodel.type.TypePackage;

import kieker.model.analysismodel.type.impl.TypePackageImpl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AssemblyPackageImpl extends EPackageImpl implements AssemblyPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass assemblyModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eStringToAssemblyComponentMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass assemblyComponentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eStringToAssemblyOperationMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass assemblyOperationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass assemblyStorageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eStringToAssemblyStorageMapEntryEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see kieker.model.analysismodel.assembly.AssemblyPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private AssemblyPackageImpl() {
		super(eNS_URI, AssemblyFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link AssemblyPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static AssemblyPackage init() {
		if (isInited) return (AssemblyPackage)EPackage.Registry.INSTANCE.getEPackage(AssemblyPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredAssemblyPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		AssemblyPackageImpl theAssemblyPackage = registeredAssemblyPackage instanceof AssemblyPackageImpl ? (AssemblyPackageImpl)registeredAssemblyPackage : new AssemblyPackageImpl();

		isInited = true;

		// Obtain or create and register interdependencies
		Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AnalysismodelPackage.eNS_URI);
		AnalysismodelPackageImpl theAnalysismodelPackage = (AnalysismodelPackageImpl)(registeredPackage instanceof AnalysismodelPackageImpl ? registeredPackage : AnalysismodelPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(StatisticsPackage.eNS_URI);
		StatisticsPackageImpl theStatisticsPackage = (StatisticsPackageImpl)(registeredPackage instanceof StatisticsPackageImpl ? registeredPackage : StatisticsPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI);
		TypePackageImpl theTypePackage = (TypePackageImpl)(registeredPackage instanceof TypePackageImpl ? registeredPackage : TypePackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(DeploymentPackage.eNS_URI);
		DeploymentPackageImpl theDeploymentPackage = (DeploymentPackageImpl)(registeredPackage instanceof DeploymentPackageImpl ? registeredPackage : DeploymentPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(ExecutionPackage.eNS_URI);
		ExecutionPackageImpl theExecutionPackage = (ExecutionPackageImpl)(registeredPackage instanceof ExecutionPackageImpl ? registeredPackage : ExecutionPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI);
		TracePackageImpl theTracePackage = (TracePackageImpl)(registeredPackage instanceof TracePackageImpl ? registeredPackage : TracePackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(SourcesPackage.eNS_URI);
		SourcesPackageImpl theSourcesPackage = (SourcesPackageImpl)(registeredPackage instanceof SourcesPackageImpl ? registeredPackage : SourcesPackage.eINSTANCE);

		// Create package meta-data objects
		theAssemblyPackage.createPackageContents();
		theAnalysismodelPackage.createPackageContents();
		theStatisticsPackage.createPackageContents();
		theTypePackage.createPackageContents();
		theDeploymentPackage.createPackageContents();
		theExecutionPackage.createPackageContents();
		theTracePackage.createPackageContents();
		theSourcesPackage.createPackageContents();

		// Initialize created meta-data
		theAssemblyPackage.initializePackageContents();
		theAnalysismodelPackage.initializePackageContents();
		theStatisticsPackage.initializePackageContents();
		theTypePackage.initializePackageContents();
		theDeploymentPackage.initializePackageContents();
		theExecutionPackage.initializePackageContents();
		theTracePackage.initializePackageContents();
		theSourcesPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theAssemblyPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(AssemblyPackage.eNS_URI, theAssemblyPackage);
		return theAssemblyPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAssemblyModel() {
		return assemblyModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAssemblyModel_AssemblyComponents() {
		return (EReference)assemblyModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEStringToAssemblyComponentMapEntry() {
		return eStringToAssemblyComponentMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEStringToAssemblyComponentMapEntry_Key() {
		return (EAttribute)eStringToAssemblyComponentMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEStringToAssemblyComponentMapEntry_Value() {
		return (EReference)eStringToAssemblyComponentMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAssemblyComponent() {
		return assemblyComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAssemblyComponent_AssemblyOperations() {
		return (EReference)assemblyComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAssemblyComponent_ComponentType() {
		return (EReference)assemblyComponentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAssemblyComponent_AssemblyStorages() {
		return (EReference)assemblyComponentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAssemblyComponent_Signature() {
		return (EAttribute)assemblyComponentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEStringToAssemblyOperationMapEntry() {
		return eStringToAssemblyOperationMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEStringToAssemblyOperationMapEntry_Key() {
		return (EAttribute)eStringToAssemblyOperationMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEStringToAssemblyOperationMapEntry_Value() {
		return (EReference)eStringToAssemblyOperationMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAssemblyOperation() {
		return assemblyOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAssemblyOperation_OperationType() {
		return (EReference)assemblyOperationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getAssemblyOperation__GetAssemblyComponent() {
		return assemblyOperationEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAssemblyStorage() {
		return assemblyStorageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAssemblyStorage_StorageType() {
		return (EReference)assemblyStorageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getAssemblyStorage__GetAssemblyComponent() {
		return assemblyStorageEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEStringToAssemblyStorageMapEntry() {
		return eStringToAssemblyStorageMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEStringToAssemblyStorageMapEntry_Key() {
		return (EAttribute)eStringToAssemblyStorageMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEStringToAssemblyStorageMapEntry_Value() {
		return (EReference)eStringToAssemblyStorageMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AssemblyFactory getAssemblyFactory() {
		return (AssemblyFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		assemblyModelEClass = createEClass(ASSEMBLY_MODEL);
		createEReference(assemblyModelEClass, ASSEMBLY_MODEL__ASSEMBLY_COMPONENTS);

		eStringToAssemblyComponentMapEntryEClass = createEClass(ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY);
		createEAttribute(eStringToAssemblyComponentMapEntryEClass, ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__KEY);
		createEReference(eStringToAssemblyComponentMapEntryEClass, ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__VALUE);

		assemblyComponentEClass = createEClass(ASSEMBLY_COMPONENT);
		createEReference(assemblyComponentEClass, ASSEMBLY_COMPONENT__ASSEMBLY_OPERATIONS);
		createEReference(assemblyComponentEClass, ASSEMBLY_COMPONENT__COMPONENT_TYPE);
		createEReference(assemblyComponentEClass, ASSEMBLY_COMPONENT__ASSEMBLY_STORAGES);
		createEAttribute(assemblyComponentEClass, ASSEMBLY_COMPONENT__SIGNATURE);

		eStringToAssemblyOperationMapEntryEClass = createEClass(ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY);
		createEAttribute(eStringToAssemblyOperationMapEntryEClass, ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY__KEY);
		createEReference(eStringToAssemblyOperationMapEntryEClass, ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY__VALUE);

		assemblyOperationEClass = createEClass(ASSEMBLY_OPERATION);
		createEReference(assemblyOperationEClass, ASSEMBLY_OPERATION__OPERATION_TYPE);
		createEOperation(assemblyOperationEClass, ASSEMBLY_OPERATION___GET_ASSEMBLY_COMPONENT);

		assemblyStorageEClass = createEClass(ASSEMBLY_STORAGE);
		createEReference(assemblyStorageEClass, ASSEMBLY_STORAGE__STORAGE_TYPE);
		createEOperation(assemblyStorageEClass, ASSEMBLY_STORAGE___GET_ASSEMBLY_COMPONENT);

		eStringToAssemblyStorageMapEntryEClass = createEClass(ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY);
		createEAttribute(eStringToAssemblyStorageMapEntryEClass, ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY__KEY);
		createEReference(eStringToAssemblyStorageMapEntryEClass, ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY__VALUE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		TypePackage theTypePackage = (TypePackage)EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(assemblyModelEClass, AssemblyModel.class, "AssemblyModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAssemblyModel_AssemblyComponents(), this.getEStringToAssemblyComponentMapEntry(), null, "assemblyComponents", null, 0, -1, AssemblyModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(eStringToAssemblyComponentMapEntryEClass, Map.Entry.class, "EStringToAssemblyComponentMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEStringToAssemblyComponentMapEntry_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEStringToAssemblyComponentMapEntry_Value(), this.getAssemblyComponent(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(assemblyComponentEClass, AssemblyComponent.class, "AssemblyComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAssemblyComponent_AssemblyOperations(), this.getEStringToAssemblyOperationMapEntry(), null, "assemblyOperations", null, 0, -1, AssemblyComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEReference(getAssemblyComponent_ComponentType(), theTypePackage.getComponentType(), null, "componentType", null, 0, 1, AssemblyComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAssemblyComponent_AssemblyStorages(), this.getEStringToAssemblyStorageMapEntry(), null, "assemblyStorages", null, 0, -1, AssemblyComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getAssemblyComponent_Signature(), ecorePackage.getEString(), "signature", null, 0, 1, AssemblyComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eStringToAssemblyOperationMapEntryEClass, Map.Entry.class, "EStringToAssemblyOperationMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEStringToAssemblyOperationMapEntry_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEStringToAssemblyOperationMapEntry_Value(), this.getAssemblyOperation(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(assemblyOperationEClass, AssemblyOperation.class, "AssemblyOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAssemblyOperation_OperationType(), theTypePackage.getOperationType(), null, "operationType", null, 0, 1, AssemblyOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getAssemblyOperation__GetAssemblyComponent(), this.getAssemblyComponent(), "getAssemblyComponent", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(assemblyStorageEClass, AssemblyStorage.class, "AssemblyStorage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAssemblyStorage_StorageType(), theTypePackage.getStorageType(), null, "storageType", null, 0, 1, AssemblyStorage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getAssemblyStorage__GetAssemblyComponent(), this.getAssemblyComponent(), "getAssemblyComponent", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(eStringToAssemblyStorageMapEntryEClass, Map.Entry.class, "EStringToAssemblyStorageMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEStringToAssemblyStorageMapEntry_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEStringToAssemblyStorageMapEntry_Value(), this.getAssemblyStorage(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
	}

} //AssemblyPackageImpl
