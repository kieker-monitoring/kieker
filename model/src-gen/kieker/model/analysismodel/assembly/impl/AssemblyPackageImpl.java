/**
 */
package kieker.model.analysismodel.assembly.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import kieker.model.analysismodel.AnalysismodelPackage;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.AssemblyProvidedInterface;
import kieker.model.analysismodel.assembly.AssemblyRequiredInterface;
import kieker.model.analysismodel.assembly.AssemblyStorage;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.impl.ExecutionPackageImpl;
import kieker.model.analysismodel.impl.AnalysismodelPackageImpl;
import kieker.model.analysismodel.source.SourcePackage;
import kieker.model.analysismodel.source.impl.SourcePackageImpl;
import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl;
import kieker.model.analysismodel.trace.TracePackage;
import kieker.model.analysismodel.trace.impl.TracePackageImpl;
import kieker.model.analysismodel.type.TypePackage;
import kieker.model.analysismodel.type.impl.TypePackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class AssemblyPackageImpl extends EPackageImpl implements AssemblyPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass assemblyModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass eStringToAssemblyComponentMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass assemblyComponentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass eStringToAssemblyOperationMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass assemblyOperationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass assemblyStorageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass eStringToAssemblyStorageMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass assemblyProvidedInterfaceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass eStringToAssemblyProvidedInterfaceMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass assemblyRequiredInterfaceEClass = null;

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
	 *
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>
	 * This method is used to initialize {@link AssemblyPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static AssemblyPackage init() {
		if (isInited) {
			return (AssemblyPackage) EPackage.Registry.INSTANCE.getEPackage(AssemblyPackage.eNS_URI);
		}

		// Obtain or create and register package
		final Object registeredAssemblyPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		final AssemblyPackageImpl theAssemblyPackage = registeredAssemblyPackage instanceof AssemblyPackageImpl ? (AssemblyPackageImpl) registeredAssemblyPackage
				: new AssemblyPackageImpl();

		isInited = true;

		// Obtain or create and register interdependencies
		Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AnalysismodelPackage.eNS_URI);
		final AnalysismodelPackageImpl theAnalysismodelPackage = (AnalysismodelPackageImpl) (registeredPackage instanceof AnalysismodelPackageImpl
				? registeredPackage
				: AnalysismodelPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(StatisticsPackage.eNS_URI);
		final StatisticsPackageImpl theStatisticsPackage = (StatisticsPackageImpl) (registeredPackage instanceof StatisticsPackageImpl ? registeredPackage
				: StatisticsPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI);
		final TypePackageImpl theTypePackage = (TypePackageImpl) (registeredPackage instanceof TypePackageImpl ? registeredPackage : TypePackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(DeploymentPackage.eNS_URI);
		final DeploymentPackageImpl theDeploymentPackage = (DeploymentPackageImpl) (registeredPackage instanceof DeploymentPackageImpl ? registeredPackage
				: DeploymentPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(ExecutionPackage.eNS_URI);
		final ExecutionPackageImpl theExecutionPackage = (ExecutionPackageImpl) (registeredPackage instanceof ExecutionPackageImpl ? registeredPackage
				: ExecutionPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI);
		final TracePackageImpl theTracePackage = (TracePackageImpl) (registeredPackage instanceof TracePackageImpl ? registeredPackage : TracePackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(SourcePackage.eNS_URI);
		final SourcePackageImpl theSourcePackage = (SourcePackageImpl) (registeredPackage instanceof SourcePackageImpl ? registeredPackage
				: SourcePackage.eINSTANCE);

		// Create package meta-data objects
		theAssemblyPackage.createPackageContents();
		theAnalysismodelPackage.createPackageContents();
		theStatisticsPackage.createPackageContents();
		theTypePackage.createPackageContents();
		theDeploymentPackage.createPackageContents();
		theExecutionPackage.createPackageContents();
		theTracePackage.createPackageContents();
		theSourcePackage.createPackageContents();

		// Initialize created meta-data
		theAssemblyPackage.initializePackageContents();
		theAnalysismodelPackage.initializePackageContents();
		theStatisticsPackage.initializePackageContents();
		theTypePackage.initializePackageContents();
		theDeploymentPackage.initializePackageContents();
		theExecutionPackage.initializePackageContents();
		theTracePackage.initializePackageContents();
		theSourcePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theAssemblyPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(AssemblyPackage.eNS_URI, theAssemblyPackage);
		return theAssemblyPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getAssemblyModel() {
		return this.assemblyModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getAssemblyModel_Components() {
		return (EReference) this.assemblyModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getEStringToAssemblyComponentMapEntry() {
		return this.eStringToAssemblyComponentMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getEStringToAssemblyComponentMapEntry_Key() {
		return (EAttribute) this.eStringToAssemblyComponentMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getEStringToAssemblyComponentMapEntry_Value() {
		return (EReference) this.eStringToAssemblyComponentMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getAssemblyComponent() {
		return this.assemblyComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getAssemblyComponent_Operations() {
		return (EReference) this.assemblyComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getAssemblyComponent_ComponentType() {
		return (EReference) this.assemblyComponentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getAssemblyComponent_Storages() {
		return (EReference) this.assemblyComponentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getAssemblyComponent_ContainedComponents() {
		return (EReference) this.assemblyComponentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getAssemblyComponent_ProvidedInterfaces() {
		return (EReference) this.assemblyComponentEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getAssemblyComponent_RequiredInterfaces() {
		return (EReference) this.assemblyComponentEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getAssemblyComponent_Signature() {
		return (EAttribute) this.assemblyComponentEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getEStringToAssemblyOperationMapEntry() {
		return this.eStringToAssemblyOperationMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getEStringToAssemblyOperationMapEntry_Key() {
		return (EAttribute) this.eStringToAssemblyOperationMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getEStringToAssemblyOperationMapEntry_Value() {
		return (EReference) this.eStringToAssemblyOperationMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getAssemblyOperation() {
		return this.assemblyOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getAssemblyOperation_OperationType() {
		return (EReference) this.assemblyOperationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EOperation getAssemblyOperation__GetComponent() {
		return this.assemblyOperationEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getAssemblyStorage() {
		return this.assemblyStorageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getAssemblyStorage_StorageType() {
		return (EReference) this.assemblyStorageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EOperation getAssemblyStorage__GetComponent() {
		return this.assemblyStorageEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getEStringToAssemblyStorageMapEntry() {
		return this.eStringToAssemblyStorageMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getEStringToAssemblyStorageMapEntry_Key() {
		return (EAttribute) this.eStringToAssemblyStorageMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getEStringToAssemblyStorageMapEntry_Value() {
		return (EReference) this.eStringToAssemblyStorageMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getAssemblyProvidedInterface() {
		return this.assemblyProvidedInterfaceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getAssemblyProvidedInterface_ProvidedInterfaceType() {
		return (EReference) this.assemblyProvidedInterfaceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getEStringToAssemblyProvidedInterfaceMapEntry() {
		return this.eStringToAssemblyProvidedInterfaceMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getEStringToAssemblyProvidedInterfaceMapEntry_Key() {
		return (EAttribute) this.eStringToAssemblyProvidedInterfaceMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getEStringToAssemblyProvidedInterfaceMapEntry_Value() {
		return (EReference) this.eStringToAssemblyProvidedInterfaceMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getAssemblyRequiredInterface() {
		return this.assemblyRequiredInterfaceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getAssemblyRequiredInterface_Requires() {
		return (EReference) this.assemblyRequiredInterfaceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getAssemblyRequiredInterface_RequiredInterfaceType() {
		return (EReference) this.assemblyRequiredInterfaceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AssemblyFactory getAssemblyFactory() {
		return (AssemblyFactory) this.getEFactoryInstance();
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
		this.assemblyModelEClass = this.createEClass(ASSEMBLY_MODEL);
		this.createEReference(this.assemblyModelEClass, ASSEMBLY_MODEL__COMPONENTS);

		this.eStringToAssemblyComponentMapEntryEClass = this.createEClass(ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY);
		this.createEAttribute(this.eStringToAssemblyComponentMapEntryEClass, ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__KEY);
		this.createEReference(this.eStringToAssemblyComponentMapEntryEClass, ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__VALUE);

		this.assemblyComponentEClass = this.createEClass(ASSEMBLY_COMPONENT);
		this.createEReference(this.assemblyComponentEClass, ASSEMBLY_COMPONENT__OPERATIONS);
		this.createEReference(this.assemblyComponentEClass, ASSEMBLY_COMPONENT__COMPONENT_TYPE);
		this.createEReference(this.assemblyComponentEClass, ASSEMBLY_COMPONENT__STORAGES);
		this.createEReference(this.assemblyComponentEClass, ASSEMBLY_COMPONENT__CONTAINED_COMPONENTS);
		this.createEReference(this.assemblyComponentEClass, ASSEMBLY_COMPONENT__PROVIDED_INTERFACES);
		this.createEReference(this.assemblyComponentEClass, ASSEMBLY_COMPONENT__REQUIRED_INTERFACES);
		this.createEAttribute(this.assemblyComponentEClass, ASSEMBLY_COMPONENT__SIGNATURE);

		this.eStringToAssemblyOperationMapEntryEClass = this.createEClass(ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY);
		this.createEAttribute(this.eStringToAssemblyOperationMapEntryEClass, ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY__KEY);
		this.createEReference(this.eStringToAssemblyOperationMapEntryEClass, ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY__VALUE);

		this.assemblyOperationEClass = this.createEClass(ASSEMBLY_OPERATION);
		this.createEReference(this.assemblyOperationEClass, ASSEMBLY_OPERATION__OPERATION_TYPE);
		this.createEOperation(this.assemblyOperationEClass, ASSEMBLY_OPERATION___GET_COMPONENT);

		this.assemblyStorageEClass = this.createEClass(ASSEMBLY_STORAGE);
		this.createEReference(this.assemblyStorageEClass, ASSEMBLY_STORAGE__STORAGE_TYPE);
		this.createEOperation(this.assemblyStorageEClass, ASSEMBLY_STORAGE___GET_COMPONENT);

		this.eStringToAssemblyStorageMapEntryEClass = this.createEClass(ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY);
		this.createEAttribute(this.eStringToAssemblyStorageMapEntryEClass, ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY__KEY);
		this.createEReference(this.eStringToAssemblyStorageMapEntryEClass, ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY__VALUE);

		this.assemblyProvidedInterfaceEClass = this.createEClass(ASSEMBLY_PROVIDED_INTERFACE);
		this.createEReference(this.assemblyProvidedInterfaceEClass, ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE);

		this.eStringToAssemblyProvidedInterfaceMapEntryEClass = this.createEClass(ESTRING_TO_ASSEMBLY_PROVIDED_INTERFACE_MAP_ENTRY);
		this.createEAttribute(this.eStringToAssemblyProvidedInterfaceMapEntryEClass, ESTRING_TO_ASSEMBLY_PROVIDED_INTERFACE_MAP_ENTRY__KEY);
		this.createEReference(this.eStringToAssemblyProvidedInterfaceMapEntryEClass, ESTRING_TO_ASSEMBLY_PROVIDED_INTERFACE_MAP_ENTRY__VALUE);

		this.assemblyRequiredInterfaceEClass = this.createEClass(ASSEMBLY_REQUIRED_INTERFACE);
		this.createEReference(this.assemblyRequiredInterfaceEClass, ASSEMBLY_REQUIRED_INTERFACE__REQUIRES);
		this.createEReference(this.assemblyRequiredInterfaceEClass, ASSEMBLY_REQUIRED_INTERFACE__REQUIRED_INTERFACE_TYPE);
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
		this.initEClass(this.assemblyModelEClass, AssemblyModel.class, "AssemblyModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getAssemblyModel_Components(), this.getEStringToAssemblyComponentMapEntry(), null, "components", null, 0, -1, AssemblyModel.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		this.initEClass(this.eStringToAssemblyComponentMapEntryEClass, Map.Entry.class, "EStringToAssemblyComponentMapEntry", !IS_ABSTRACT, !IS_INTERFACE,
				!IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getEStringToAssemblyComponentMapEntry_Key(), this.ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getEStringToAssemblyComponentMapEntry_Value(), this.getAssemblyComponent(), null, "value", null, 0, 1, Map.Entry.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.assemblyComponentEClass, AssemblyComponent.class, "AssemblyComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getAssemblyComponent_Operations(), this.getEStringToAssemblyOperationMapEntry(), null, "operations", null, 0, -1,
				AssemblyComponent.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		this.initEReference(this.getAssemblyComponent_ComponentType(), theTypePackage.getComponentType(), null, "componentType", null, 0, 1, AssemblyComponent.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getAssemblyComponent_Storages(), this.getEStringToAssemblyStorageMapEntry(), null, "storages", null, 0, -1, AssemblyComponent.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		this.initEReference(this.getAssemblyComponent_ContainedComponents(), this.getAssemblyComponent(), null, "containedComponents", null, 0, -1,
				AssemblyComponent.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getAssemblyComponent_ProvidedInterfaces(), this.getEStringToAssemblyProvidedInterfaceMapEntry(), null, "providedInterfaces", null,
				0, -1,
				AssemblyComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		this.initEReference(this.getAssemblyComponent_RequiredInterfaces(), this.getAssemblyRequiredInterface(), null, "requiredInterfaces", null, 0, -1,
				AssemblyComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		this.initEAttribute(this.getAssemblyComponent_Signature(), this.ecorePackage.getEString(), "signature", null, 0, 1, AssemblyComponent.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.eStringToAssemblyOperationMapEntryEClass, Map.Entry.class, "EStringToAssemblyOperationMapEntry", !IS_ABSTRACT, !IS_INTERFACE,
				!IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getEStringToAssemblyOperationMapEntry_Key(), this.ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getEStringToAssemblyOperationMapEntry_Value(), this.getAssemblyOperation(), null, "value", null, 0, 1, Map.Entry.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.assemblyOperationEClass, AssemblyOperation.class, "AssemblyOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getAssemblyOperation_OperationType(), theTypePackage.getOperationType(), null, "operationType", null, 0, 1, AssemblyOperation.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEOperation(this.getAssemblyOperation__GetComponent(), this.getAssemblyComponent(), "getComponent", 0, 1, IS_UNIQUE, IS_ORDERED);

		this.initEClass(this.assemblyStorageEClass, AssemblyStorage.class, "AssemblyStorage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getAssemblyStorage_StorageType(), theTypePackage.getStorageType(), null, "storageType", null, 0, 1, AssemblyStorage.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEOperation(this.getAssemblyStorage__GetComponent(), this.getAssemblyComponent(), "getComponent", 0, 1, IS_UNIQUE, IS_ORDERED);

		this.initEClass(this.eStringToAssemblyStorageMapEntryEClass, Map.Entry.class, "EStringToAssemblyStorageMapEntry", !IS_ABSTRACT, !IS_INTERFACE,
				!IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getEStringToAssemblyStorageMapEntry_Key(), this.ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getEStringToAssemblyStorageMapEntry_Value(), this.getAssemblyStorage(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.assemblyProvidedInterfaceEClass, AssemblyProvidedInterface.class, "AssemblyProvidedInterface", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getAssemblyProvidedInterface_ProvidedInterfaceType(), theTypePackage.getProvidedInterfaceType(), null, "providedInterfaceType",
				null, 0, 1,
				AssemblyProvidedInterface.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		this.initEClass(this.eStringToAssemblyProvidedInterfaceMapEntryEClass, Map.Entry.class, "EStringToAssemblyProvidedInterfaceMapEntry", !IS_ABSTRACT,
				!IS_INTERFACE,
				!IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getEStringToAssemblyProvidedInterfaceMapEntry_Key(), this.ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getEStringToAssemblyProvidedInterfaceMapEntry_Value(), this.getAssemblyProvidedInterface(), null, "value", null, 0, 1,
				Map.Entry.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.assemblyRequiredInterfaceEClass, AssemblyRequiredInterface.class, "AssemblyRequiredInterface", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getAssemblyRequiredInterface_Requires(), this.getAssemblyProvidedInterface(), null, "requires", null, 0, 1,
				AssemblyRequiredInterface.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getAssemblyRequiredInterface_RequiredInterfaceType(), theTypePackage.getRequiredInterfaceType(), null, "requiredInterfaceType",
				null, 0, 1,
				AssemblyRequiredInterface.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
	}

} // AssemblyPackageImpl
