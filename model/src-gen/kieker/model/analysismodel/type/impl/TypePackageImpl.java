/**
 */
package kieker.model.analysismodel.type.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import kieker.model.analysismodel.AnalysismodelPackage;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl;
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
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.ProvidedInterfaceType;
import kieker.model.analysismodel.type.RequiredInterfaceType;
import kieker.model.analysismodel.type.StorageType;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;
import kieker.model.analysismodel.type.TypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class TypePackageImpl extends EPackageImpl implements TypePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass typeModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass eStringToComponentTypeMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass componentTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass eStringToOperationTypeMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass eStringToStorageTypeMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass operationTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass storageTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass providedInterfaceTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass eStringToProvidedInterfaceTypeMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass requiredInterfaceTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass interfaceEStringToOperationTypeMapEntryEClass = null;

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
	 * @see kieker.model.analysismodel.type.TypePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private TypePackageImpl() {
		super(eNS_URI, TypeFactory.eINSTANCE);
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
	 * This method is used to initialize {@link TypePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static TypePackage init() {
		if (isInited) {
			return (TypePackage) EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI);
		}

		// Obtain or create and register package
		final Object registeredTypePackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		final TypePackageImpl theTypePackage = registeredTypePackage instanceof TypePackageImpl ? (TypePackageImpl) registeredTypePackage : new TypePackageImpl();

		isInited = true;

		// Obtain or create and register interdependencies
		Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AnalysismodelPackage.eNS_URI);
		final AnalysismodelPackageImpl theAnalysismodelPackage = (AnalysismodelPackageImpl) (registeredPackage instanceof AnalysismodelPackageImpl
				? registeredPackage
				: AnalysismodelPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(StatisticsPackage.eNS_URI);
		final StatisticsPackageImpl theStatisticsPackage = (StatisticsPackageImpl) (registeredPackage instanceof StatisticsPackageImpl ? registeredPackage
				: StatisticsPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AssemblyPackage.eNS_URI);
		final AssemblyPackageImpl theAssemblyPackage = (AssemblyPackageImpl) (registeredPackage instanceof AssemblyPackageImpl ? registeredPackage
				: AssemblyPackage.eINSTANCE);
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
		theTypePackage.createPackageContents();
		theAnalysismodelPackage.createPackageContents();
		theStatisticsPackage.createPackageContents();
		theAssemblyPackage.createPackageContents();
		theDeploymentPackage.createPackageContents();
		theExecutionPackage.createPackageContents();
		theTracePackage.createPackageContents();
		theSourcePackage.createPackageContents();

		// Initialize created meta-data
		theTypePackage.initializePackageContents();
		theAnalysismodelPackage.initializePackageContents();
		theStatisticsPackage.initializePackageContents();
		theAssemblyPackage.initializePackageContents();
		theDeploymentPackage.initializePackageContents();
		theExecutionPackage.initializePackageContents();
		theTracePackage.initializePackageContents();
		theSourcePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theTypePackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(TypePackage.eNS_URI, theTypePackage);
		return theTypePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getTypeModel() {
		return this.typeModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getTypeModel_ComponentTypes() {
		return (EReference) this.typeModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getEStringToComponentTypeMapEntry() {
		return this.eStringToComponentTypeMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getEStringToComponentTypeMapEntry_Key() {
		return (EAttribute) this.eStringToComponentTypeMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getEStringToComponentTypeMapEntry_Value() {
		return (EReference) this.eStringToComponentTypeMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getComponentType() {
		return this.componentTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getComponentType_Signature() {
		return (EAttribute) this.componentTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getComponentType_ProvidedOperations() {
		return (EReference) this.componentTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getComponentType_Name() {
		return (EAttribute) this.componentTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getComponentType_Package() {
		return (EAttribute) this.componentTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getComponentType_ProvidedStorages() {
		return (EReference) this.componentTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getComponentType_ContainedComponents() {
		return (EReference) this.componentTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getComponentType_ProvidedInterfaceTypes() {
		return (EReference) this.componentTypeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getComponentType_RequiredInterfaceTypes() {
		return (EReference) this.componentTypeEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getEStringToOperationTypeMapEntry() {
		return this.eStringToOperationTypeMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getEStringToOperationTypeMapEntry_Key() {
		return (EAttribute) this.eStringToOperationTypeMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getEStringToOperationTypeMapEntry_Value() {
		return (EReference) this.eStringToOperationTypeMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getEStringToStorageTypeMapEntry() {
		return this.eStringToStorageTypeMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getEStringToStorageTypeMapEntry_Key() {
		return (EAttribute) this.eStringToStorageTypeMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getEStringToStorageTypeMapEntry_Value() {
		return (EReference) this.eStringToStorageTypeMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getOperationType() {
		return this.operationTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperationType_Signature() {
		return (EAttribute) this.operationTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperationType_Name() {
		return (EAttribute) this.operationTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperationType_ReturnType() {
		return (EAttribute) this.operationTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperationType_Modifiers() {
		return (EAttribute) this.operationTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperationType_ParameterTypes() {
		return (EAttribute) this.operationTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EOperation getOperationType__GetComponentType() {
		return this.operationTypeEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getStorageType() {
		return this.storageTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getStorageType_Name() {
		return (EAttribute) this.storageTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getStorageType_Type() {
		return (EAttribute) this.storageTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getProvidedInterfaceType() {
		return this.providedInterfaceTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getProvidedInterfaceType_ProvidedOperationTypes() {
		return (EReference) this.providedInterfaceTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getProvidedInterfaceType_Name() {
		return (EAttribute) this.providedInterfaceTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getProvidedInterfaceType_Signature() {
		return (EAttribute) this.providedInterfaceTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getProvidedInterfaceType_Package() {
		return (EAttribute) this.providedInterfaceTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getEStringToProvidedInterfaceTypeMapEntry() {
		return this.eStringToProvidedInterfaceTypeMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getEStringToProvidedInterfaceTypeMapEntry_Key() {
		return (EAttribute) this.eStringToProvidedInterfaceTypeMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getEStringToProvidedInterfaceTypeMapEntry_Value() {
		return (EReference) this.eStringToProvidedInterfaceTypeMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getRequiredInterfaceType() {
		return this.requiredInterfaceTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getRequiredInterfaceType_Requires() {
		return (EReference) this.requiredInterfaceTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getInterfaceEStringToOperationTypeMapEntry() {
		return this.interfaceEStringToOperationTypeMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getInterfaceEStringToOperationTypeMapEntry_Key() {
		return (EAttribute) this.interfaceEStringToOperationTypeMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getInterfaceEStringToOperationTypeMapEntry_Value() {
		return (EReference) this.interfaceEStringToOperationTypeMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public TypeFactory getTypeFactory() {
		return (TypeFactory) this.getEFactoryInstance();
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
		this.typeModelEClass = this.createEClass(TYPE_MODEL);
		this.createEReference(this.typeModelEClass, TYPE_MODEL__COMPONENT_TYPES);

		this.eStringToComponentTypeMapEntryEClass = this.createEClass(ESTRING_TO_COMPONENT_TYPE_MAP_ENTRY);
		this.createEAttribute(this.eStringToComponentTypeMapEntryEClass, ESTRING_TO_COMPONENT_TYPE_MAP_ENTRY__KEY);
		this.createEReference(this.eStringToComponentTypeMapEntryEClass, ESTRING_TO_COMPONENT_TYPE_MAP_ENTRY__VALUE);

		this.componentTypeEClass = this.createEClass(COMPONENT_TYPE);
		this.createEAttribute(this.componentTypeEClass, COMPONENT_TYPE__SIGNATURE);
		this.createEReference(this.componentTypeEClass, COMPONENT_TYPE__PROVIDED_OPERATIONS);
		this.createEAttribute(this.componentTypeEClass, COMPONENT_TYPE__NAME);
		this.createEAttribute(this.componentTypeEClass, COMPONENT_TYPE__PACKAGE);
		this.createEReference(this.componentTypeEClass, COMPONENT_TYPE__PROVIDED_STORAGES);
		this.createEReference(this.componentTypeEClass, COMPONENT_TYPE__CONTAINED_COMPONENTS);
		this.createEReference(this.componentTypeEClass, COMPONENT_TYPE__PROVIDED_INTERFACE_TYPES);
		this.createEReference(this.componentTypeEClass, COMPONENT_TYPE__REQUIRED_INTERFACE_TYPES);

		this.eStringToOperationTypeMapEntryEClass = this.createEClass(ESTRING_TO_OPERATION_TYPE_MAP_ENTRY);
		this.createEAttribute(this.eStringToOperationTypeMapEntryEClass, ESTRING_TO_OPERATION_TYPE_MAP_ENTRY__KEY);
		this.createEReference(this.eStringToOperationTypeMapEntryEClass, ESTRING_TO_OPERATION_TYPE_MAP_ENTRY__VALUE);

		this.eStringToStorageTypeMapEntryEClass = this.createEClass(ESTRING_TO_STORAGE_TYPE_MAP_ENTRY);
		this.createEAttribute(this.eStringToStorageTypeMapEntryEClass, ESTRING_TO_STORAGE_TYPE_MAP_ENTRY__KEY);
		this.createEReference(this.eStringToStorageTypeMapEntryEClass, ESTRING_TO_STORAGE_TYPE_MAP_ENTRY__VALUE);

		this.operationTypeEClass = this.createEClass(OPERATION_TYPE);
		this.createEAttribute(this.operationTypeEClass, OPERATION_TYPE__SIGNATURE);
		this.createEAttribute(this.operationTypeEClass, OPERATION_TYPE__NAME);
		this.createEAttribute(this.operationTypeEClass, OPERATION_TYPE__RETURN_TYPE);
		this.createEAttribute(this.operationTypeEClass, OPERATION_TYPE__MODIFIERS);
		this.createEAttribute(this.operationTypeEClass, OPERATION_TYPE__PARAMETER_TYPES);
		this.createEOperation(this.operationTypeEClass, OPERATION_TYPE___GET_COMPONENT_TYPE);

		this.storageTypeEClass = this.createEClass(STORAGE_TYPE);
		this.createEAttribute(this.storageTypeEClass, STORAGE_TYPE__NAME);
		this.createEAttribute(this.storageTypeEClass, STORAGE_TYPE__TYPE);

		this.providedInterfaceTypeEClass = this.createEClass(PROVIDED_INTERFACE_TYPE);
		this.createEReference(this.providedInterfaceTypeEClass, PROVIDED_INTERFACE_TYPE__PROVIDED_OPERATION_TYPES);
		this.createEAttribute(this.providedInterfaceTypeEClass, PROVIDED_INTERFACE_TYPE__NAME);
		this.createEAttribute(this.providedInterfaceTypeEClass, PROVIDED_INTERFACE_TYPE__SIGNATURE);
		this.createEAttribute(this.providedInterfaceTypeEClass, PROVIDED_INTERFACE_TYPE__PACKAGE);

		this.eStringToProvidedInterfaceTypeMapEntryEClass = this.createEClass(ESTRING_TO_PROVIDED_INTERFACE_TYPE_MAP_ENTRY);
		this.createEAttribute(this.eStringToProvidedInterfaceTypeMapEntryEClass, ESTRING_TO_PROVIDED_INTERFACE_TYPE_MAP_ENTRY__KEY);
		this.createEReference(this.eStringToProvidedInterfaceTypeMapEntryEClass, ESTRING_TO_PROVIDED_INTERFACE_TYPE_MAP_ENTRY__VALUE);

		this.requiredInterfaceTypeEClass = this.createEClass(REQUIRED_INTERFACE_TYPE);
		this.createEReference(this.requiredInterfaceTypeEClass, REQUIRED_INTERFACE_TYPE__REQUIRES);

		this.interfaceEStringToOperationTypeMapEntryEClass = this.createEClass(INTERFACE_ESTRING_TO_OPERATION_TYPE_MAP_ENTRY);
		this.createEAttribute(this.interfaceEStringToOperationTypeMapEntryEClass, INTERFACE_ESTRING_TO_OPERATION_TYPE_MAP_ENTRY__KEY);
		this.createEReference(this.interfaceEStringToOperationTypeMapEntryEClass, INTERFACE_ESTRING_TO_OPERATION_TYPE_MAP_ENTRY__VALUE);
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		this.initEClass(this.typeModelEClass, TypeModel.class, "TypeModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getTypeModel_ComponentTypes(), this.getEStringToComponentTypeMapEntry(), null, "componentTypes", null, 0, -1, TypeModel.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		this.initEClass(this.eStringToComponentTypeMapEntryEClass, Map.Entry.class, "EStringToComponentTypeMapEntry", !IS_ABSTRACT, !IS_INTERFACE,
				!IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getEStringToComponentTypeMapEntry_Key(), this.ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getEStringToComponentTypeMapEntry_Value(), this.getComponentType(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.componentTypeEClass, ComponentType.class, "ComponentType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getComponentType_Signature(), this.ecorePackage.getEString(), "signature", null, 1, 1, ComponentType.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getComponentType_ProvidedOperations(), this.getEStringToOperationTypeMapEntry(), null, "providedOperations", null, 0, -1,
				ComponentType.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		this.initEAttribute(this.getComponentType_Name(), this.ecorePackage.getEString(), "name", null, 0, 1, ComponentType.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getComponentType_Package(), this.ecorePackage.getEString(), "package", null, 0, 1, ComponentType.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getComponentType_ProvidedStorages(), this.getEStringToStorageTypeMapEntry(), null, "providedStorages", null, 0, -1,
				ComponentType.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		this.initEReference(this.getComponentType_ContainedComponents(), this.getComponentType(), null, "containedComponents", null, 0, -1, ComponentType.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getComponentType_ProvidedInterfaceTypes(), this.getProvidedInterfaceType(), null, "providedInterfaceTypes", null, 0, -1,
				ComponentType.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getComponentType_RequiredInterfaceTypes(), this.getRequiredInterfaceType(), null, "requiredInterfaceTypes", null, 0, -1,
				ComponentType.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.eStringToOperationTypeMapEntryEClass, Map.Entry.class, "EStringToOperationTypeMapEntry", !IS_ABSTRACT, !IS_INTERFACE,
				!IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getEStringToOperationTypeMapEntry_Key(), this.ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getEStringToOperationTypeMapEntry_Value(), this.getOperationType(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.eStringToStorageTypeMapEntryEClass, Map.Entry.class, "EStringToStorageTypeMapEntry", !IS_ABSTRACT, !IS_INTERFACE,
				!IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getEStringToStorageTypeMapEntry_Key(), this.ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getEStringToStorageTypeMapEntry_Value(), this.getStorageType(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.operationTypeEClass, OperationType.class, "OperationType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getOperationType_Signature(), this.ecorePackage.getEString(), "signature", null, 1, 1, OperationType.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getOperationType_Name(), this.ecorePackage.getEString(), "name", null, 0, 1, OperationType.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getOperationType_ReturnType(), this.ecorePackage.getEString(), "returnType", null, 0, 1, OperationType.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getOperationType_Modifiers(), this.ecorePackage.getEString(), "modifiers", null, 0, -1, OperationType.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getOperationType_ParameterTypes(), this.ecorePackage.getEString(), "parameterTypes", null, 0, -1, OperationType.class,
				!IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEOperation(this.getOperationType__GetComponentType(), this.getComponentType(), "getComponentType", 0, 1, IS_UNIQUE, IS_ORDERED);

		this.initEClass(this.storageTypeEClass, StorageType.class, "StorageType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getStorageType_Name(), this.ecorePackage.getEString(), "name", null, 1, 1, StorageType.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getStorageType_Type(), this.ecorePackage.getEString(), "type", null, 0, 1, StorageType.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.providedInterfaceTypeEClass, ProvidedInterfaceType.class, "ProvidedInterfaceType", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getProvidedInterfaceType_ProvidedOperationTypes(), this.getInterfaceEStringToOperationTypeMapEntry(), null,
				"providedOperationTypes", null, 0,
				-1, ProvidedInterfaceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
				!IS_DERIVED, !IS_ORDERED);
		this.initEAttribute(this.getProvidedInterfaceType_Name(), this.ecorePackage.getEString(), "name", null, 0, 1, ProvidedInterfaceType.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getProvidedInterfaceType_Signature(), this.ecorePackage.getEString(), "signature", null, 0, 1, ProvidedInterfaceType.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getProvidedInterfaceType_Package(), this.ecorePackage.getEString(), "package", null, 0, 1, ProvidedInterfaceType.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.eStringToProvidedInterfaceTypeMapEntryEClass, Map.Entry.class, "EStringToProvidedInterfaceTypeMapEntry", !IS_ABSTRACT, !IS_INTERFACE,
				!IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getEStringToProvidedInterfaceTypeMapEntry_Key(), this.ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getEStringToProvidedInterfaceTypeMapEntry_Value(), this.getProvidedInterfaceType(), null, "value", null, 0, 1, Map.Entry.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.requiredInterfaceTypeEClass, RequiredInterfaceType.class, "RequiredInterfaceType", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getRequiredInterfaceType_Requires(), this.getProvidedInterfaceType(), null, "requires", null, 0, 1, RequiredInterfaceType.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.interfaceEStringToOperationTypeMapEntryEClass, Map.Entry.class, "InterfaceEStringToOperationTypeMapEntry", !IS_ABSTRACT, !IS_INTERFACE,
				!IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getInterfaceEStringToOperationTypeMapEntry_Key(), this.ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getInterfaceEStringToOperationTypeMapEntry_Value(), this.getOperationType(), null, "value", null, 0, 1, Map.Entry.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
	}

} // TypePackageImpl
