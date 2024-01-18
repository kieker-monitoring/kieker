/**
 */
package kieker.model.analysismodel.deployment.impl;

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
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedProvidedInterface;
import kieker.model.analysismodel.deployment.DeployedRequiredInterface;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.deployment.DeploymentPackage;
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
public class DeploymentPackageImpl extends EPackageImpl implements DeploymentPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass deploymentModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass eStringToDeploymentContextMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass deploymentContextEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass eStringToDeployedComponentMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass deployedComponentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass eStringToDeployedOperationMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass deployedOperationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass eStringToDeployedStorageMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass deployedStorageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass deployedProvidedInterfaceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass eStringToDeployedProvidedInterfaceMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass deployedRequiredInterfaceEClass = null;

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
	 * @see kieker.model.analysismodel.deployment.DeploymentPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DeploymentPackageImpl() {
		super(eNS_URI, DeploymentFactory.eINSTANCE);
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
	 * This method is used to initialize {@link DeploymentPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DeploymentPackage init() {
		if (isInited) {
			return (DeploymentPackage) EPackage.Registry.INSTANCE.getEPackage(DeploymentPackage.eNS_URI);
		}

		// Obtain or create and register package
		final Object registeredDeploymentPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		final DeploymentPackageImpl theDeploymentPackage = registeredDeploymentPackage instanceof DeploymentPackageImpl
				? (DeploymentPackageImpl) registeredDeploymentPackage
				: new DeploymentPackageImpl();

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
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AssemblyPackage.eNS_URI);
		final AssemblyPackageImpl theAssemblyPackage = (AssemblyPackageImpl) (registeredPackage instanceof AssemblyPackageImpl ? registeredPackage
				: AssemblyPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(ExecutionPackage.eNS_URI);
		final ExecutionPackageImpl theExecutionPackage = (ExecutionPackageImpl) (registeredPackage instanceof ExecutionPackageImpl ? registeredPackage
				: ExecutionPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI);
		final TracePackageImpl theTracePackage = (TracePackageImpl) (registeredPackage instanceof TracePackageImpl ? registeredPackage : TracePackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(SourcePackage.eNS_URI);
		final SourcePackageImpl theSourcePackage = (SourcePackageImpl) (registeredPackage instanceof SourcePackageImpl ? registeredPackage
				: SourcePackage.eINSTANCE);

		// Create package meta-data objects
		theDeploymentPackage.createPackageContents();
		theAnalysismodelPackage.createPackageContents();
		theStatisticsPackage.createPackageContents();
		theTypePackage.createPackageContents();
		theAssemblyPackage.createPackageContents();
		theExecutionPackage.createPackageContents();
		theTracePackage.createPackageContents();
		theSourcePackage.createPackageContents();

		// Initialize created meta-data
		theDeploymentPackage.initializePackageContents();
		theAnalysismodelPackage.initializePackageContents();
		theStatisticsPackage.initializePackageContents();
		theTypePackage.initializePackageContents();
		theAssemblyPackage.initializePackageContents();
		theExecutionPackage.initializePackageContents();
		theTracePackage.initializePackageContents();
		theSourcePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDeploymentPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(DeploymentPackage.eNS_URI, theDeploymentPackage);
		return theDeploymentPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getDeploymentModel() {
		return this.deploymentModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeploymentModel_Contexts() {
		return (EReference) this.deploymentModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getEStringToDeploymentContextMapEntry() {
		return this.eStringToDeploymentContextMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getEStringToDeploymentContextMapEntry_Key() {
		return (EAttribute) this.eStringToDeploymentContextMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getEStringToDeploymentContextMapEntry_Value() {
		return (EReference) this.eStringToDeploymentContextMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getDeploymentContext() {
		return this.deploymentContextEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getDeploymentContext_Name() {
		return (EAttribute) this.deploymentContextEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeploymentContext_Components() {
		return (EReference) this.deploymentContextEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getEStringToDeployedComponentMapEntry() {
		return this.eStringToDeployedComponentMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getEStringToDeployedComponentMapEntry_Key() {
		return (EAttribute) this.eStringToDeployedComponentMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getEStringToDeployedComponentMapEntry_Value() {
		return (EReference) this.eStringToDeployedComponentMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getDeployedComponent() {
		return this.deployedComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedComponent_AssemblyComponent() {
		return (EReference) this.deployedComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedComponent_Operations() {
		return (EReference) this.deployedComponentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedComponent_Storages() {
		return (EReference) this.deployedComponentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedComponent_ContainedComponents() {
		return (EReference) this.deployedComponentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedComponent_ProvidedInterfaces() {
		return (EReference) this.deployedComponentEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedComponent_RequiredInterfaces() {
		return (EReference) this.deployedComponentEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getDeployedComponent_Signature() {
		return (EAttribute) this.deployedComponentEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EOperation getDeployedComponent__GetContext() {
		return this.deployedComponentEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getEStringToDeployedOperationMapEntry() {
		return this.eStringToDeployedOperationMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getEStringToDeployedOperationMapEntry_Key() {
		return (EAttribute) this.eStringToDeployedOperationMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getEStringToDeployedOperationMapEntry_Value() {
		return (EReference) this.eStringToDeployedOperationMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getDeployedOperation() {
		return this.deployedOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedOperation_AssemblyOperation() {
		return (EReference) this.deployedOperationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EOperation getDeployedOperation__GetComponent() {
		return this.deployedOperationEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getEStringToDeployedStorageMapEntry() {
		return this.eStringToDeployedStorageMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getEStringToDeployedStorageMapEntry_Key() {
		return (EAttribute) this.eStringToDeployedStorageMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getEStringToDeployedStorageMapEntry_Value() {
		return (EReference) this.eStringToDeployedStorageMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getDeployedStorage() {
		return this.deployedStorageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedStorage_AssemblyStorage() {
		return (EReference) this.deployedStorageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EOperation getDeployedStorage__GetComponent() {
		return this.deployedStorageEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getDeployedProvidedInterface() {
		return this.deployedProvidedInterfaceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedProvidedInterface_ProvidedInterface() {
		return (EReference) this.deployedProvidedInterfaceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getEStringToDeployedProvidedInterfaceMapEntry() {
		return this.eStringToDeployedProvidedInterfaceMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getEStringToDeployedProvidedInterfaceMapEntry_Key() {
		return (EAttribute) this.eStringToDeployedProvidedInterfaceMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getEStringToDeployedProvidedInterfaceMapEntry_Value() {
		return (EReference) this.eStringToDeployedProvidedInterfaceMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getDeployedRequiredInterface() {
		return this.deployedRequiredInterfaceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedRequiredInterface_RequiredInterface() {
		return (EReference) this.deployedRequiredInterfaceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedRequiredInterface_Requires() {
		return (EReference) this.deployedRequiredInterfaceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeploymentFactory getDeploymentFactory() {
		return (DeploymentFactory) this.getEFactoryInstance();
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
		this.deploymentModelEClass = this.createEClass(DEPLOYMENT_MODEL);
		this.createEReference(this.deploymentModelEClass, DEPLOYMENT_MODEL__CONTEXTS);

		this.eStringToDeploymentContextMapEntryEClass = this.createEClass(ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY);
		this.createEAttribute(this.eStringToDeploymentContextMapEntryEClass, ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY__KEY);
		this.createEReference(this.eStringToDeploymentContextMapEntryEClass, ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY__VALUE);

		this.deploymentContextEClass = this.createEClass(DEPLOYMENT_CONTEXT);
		this.createEAttribute(this.deploymentContextEClass, DEPLOYMENT_CONTEXT__NAME);
		this.createEReference(this.deploymentContextEClass, DEPLOYMENT_CONTEXT__COMPONENTS);

		this.eStringToDeployedComponentMapEntryEClass = this.createEClass(ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY);
		this.createEAttribute(this.eStringToDeployedComponentMapEntryEClass, ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY__KEY);
		this.createEReference(this.eStringToDeployedComponentMapEntryEClass, ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY__VALUE);

		this.deployedComponentEClass = this.createEClass(DEPLOYED_COMPONENT);
		this.createEReference(this.deployedComponentEClass, DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT);
		this.createEReference(this.deployedComponentEClass, DEPLOYED_COMPONENT__OPERATIONS);
		this.createEReference(this.deployedComponentEClass, DEPLOYED_COMPONENT__STORAGES);
		this.createEReference(this.deployedComponentEClass, DEPLOYED_COMPONENT__CONTAINED_COMPONENTS);
		this.createEReference(this.deployedComponentEClass, DEPLOYED_COMPONENT__PROVIDED_INTERFACES);
		this.createEReference(this.deployedComponentEClass, DEPLOYED_COMPONENT__REQUIRED_INTERFACES);
		this.createEAttribute(this.deployedComponentEClass, DEPLOYED_COMPONENT__SIGNATURE);
		this.createEOperation(this.deployedComponentEClass, DEPLOYED_COMPONENT___GET_CONTEXT);

		this.eStringToDeployedOperationMapEntryEClass = this.createEClass(ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY);
		this.createEAttribute(this.eStringToDeployedOperationMapEntryEClass, ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY__KEY);
		this.createEReference(this.eStringToDeployedOperationMapEntryEClass, ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY__VALUE);

		this.deployedOperationEClass = this.createEClass(DEPLOYED_OPERATION);
		this.createEReference(this.deployedOperationEClass, DEPLOYED_OPERATION__ASSEMBLY_OPERATION);
		this.createEOperation(this.deployedOperationEClass, DEPLOYED_OPERATION___GET_COMPONENT);

		this.eStringToDeployedStorageMapEntryEClass = this.createEClass(ESTRING_TO_DEPLOYED_STORAGE_MAP_ENTRY);
		this.createEAttribute(this.eStringToDeployedStorageMapEntryEClass, ESTRING_TO_DEPLOYED_STORAGE_MAP_ENTRY__KEY);
		this.createEReference(this.eStringToDeployedStorageMapEntryEClass, ESTRING_TO_DEPLOYED_STORAGE_MAP_ENTRY__VALUE);

		this.deployedStorageEClass = this.createEClass(DEPLOYED_STORAGE);
		this.createEReference(this.deployedStorageEClass, DEPLOYED_STORAGE__ASSEMBLY_STORAGE);
		this.createEOperation(this.deployedStorageEClass, DEPLOYED_STORAGE___GET_COMPONENT);

		this.deployedProvidedInterfaceEClass = this.createEClass(DEPLOYED_PROVIDED_INTERFACE);
		this.createEReference(this.deployedProvidedInterfaceEClass, DEPLOYED_PROVIDED_INTERFACE__PROVIDED_INTERFACE);

		this.eStringToDeployedProvidedInterfaceMapEntryEClass = this.createEClass(ESTRING_TO_DEPLOYED_PROVIDED_INTERFACE_MAP_ENTRY);
		this.createEAttribute(this.eStringToDeployedProvidedInterfaceMapEntryEClass, ESTRING_TO_DEPLOYED_PROVIDED_INTERFACE_MAP_ENTRY__KEY);
		this.createEReference(this.eStringToDeployedProvidedInterfaceMapEntryEClass, ESTRING_TO_DEPLOYED_PROVIDED_INTERFACE_MAP_ENTRY__VALUE);

		this.deployedRequiredInterfaceEClass = this.createEClass(DEPLOYED_REQUIRED_INTERFACE);
		this.createEReference(this.deployedRequiredInterfaceEClass, DEPLOYED_REQUIRED_INTERFACE__REQUIRED_INTERFACE);
		this.createEReference(this.deployedRequiredInterfaceEClass, DEPLOYED_REQUIRED_INTERFACE__REQUIRES);
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
		final AssemblyPackage theAssemblyPackage = (AssemblyPackage) EPackage.Registry.INSTANCE.getEPackage(AssemblyPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		this.initEClass(this.deploymentModelEClass, DeploymentModel.class, "DeploymentModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getDeploymentModel_Contexts(), this.getEStringToDeploymentContextMapEntry(), null, "contexts", null, 0, -1, DeploymentModel.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		this.initEClass(this.eStringToDeploymentContextMapEntryEClass, Map.Entry.class, "EStringToDeploymentContextMapEntry", !IS_ABSTRACT, !IS_INTERFACE,
				!IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getEStringToDeploymentContextMapEntry_Key(), this.ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getEStringToDeploymentContextMapEntry_Value(), this.getDeploymentContext(), null, "value", null, 0, 1, Map.Entry.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.deploymentContextEClass, DeploymentContext.class, "DeploymentContext", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getDeploymentContext_Name(), this.ecorePackage.getEString(), "name", null, 0, 1, DeploymentContext.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getDeploymentContext_Components(), this.getEStringToDeployedComponentMapEntry(), null, "components", null, 0, -1,
				DeploymentContext.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		this.initEClass(this.eStringToDeployedComponentMapEntryEClass, Map.Entry.class, "EStringToDeployedComponentMapEntry", !IS_ABSTRACT, !IS_INTERFACE,
				!IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getEStringToDeployedComponentMapEntry_Key(), this.ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getEStringToDeployedComponentMapEntry_Value(), this.getDeployedComponent(), null, "value", null, 0, 1, Map.Entry.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.deployedComponentEClass, DeployedComponent.class, "DeployedComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getDeployedComponent_AssemblyComponent(), theAssemblyPackage.getAssemblyComponent(), null, "assemblyComponent", null, 0, 1,
				DeployedComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		this.initEReference(this.getDeployedComponent_Operations(), this.getEStringToDeployedOperationMapEntry(), null, "operations", null, 0, -1,
				DeployedComponent.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		this.initEReference(this.getDeployedComponent_Storages(), this.getEStringToDeployedStorageMapEntry(), null, "storages", null, 0, -1, DeployedComponent.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		this.initEReference(this.getDeployedComponent_ContainedComponents(), this.getDeployedComponent(), null, "containedComponents", null, 0, -1,
				DeployedComponent.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getDeployedComponent_ProvidedInterfaces(), this.getEStringToDeployedProvidedInterfaceMapEntry(), null, "providedInterfaces", null,
				0, -1,
				DeployedComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		this.initEReference(this.getDeployedComponent_RequiredInterfaces(), this.getDeployedRequiredInterface(), null, "requiredInterfaces", null, 0, -1,
				DeployedComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		this.initEAttribute(this.getDeployedComponent_Signature(), this.ecorePackage.getEString(), "signature", null, 0, 1, DeployedComponent.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEOperation(this.getDeployedComponent__GetContext(), this.getDeploymentContext(), "getContext", 0, 1, IS_UNIQUE, IS_ORDERED);

		this.initEClass(this.eStringToDeployedOperationMapEntryEClass, Map.Entry.class, "EStringToDeployedOperationMapEntry", !IS_ABSTRACT, !IS_INTERFACE,
				!IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getEStringToDeployedOperationMapEntry_Key(), this.ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getEStringToDeployedOperationMapEntry_Value(), this.getDeployedOperation(), null, "value", null, 0, 1, Map.Entry.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.deployedOperationEClass, DeployedOperation.class, "DeployedOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getDeployedOperation_AssemblyOperation(), theAssemblyPackage.getAssemblyOperation(), null, "assemblyOperation", null, 0, 1,
				DeployedOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);

		this.initEOperation(this.getDeployedOperation__GetComponent(), this.getDeployedComponent(), "getComponent", 0, 1, IS_UNIQUE, IS_ORDERED);

		this.initEClass(this.eStringToDeployedStorageMapEntryEClass, Map.Entry.class, "EStringToDeployedStorageMapEntry", !IS_ABSTRACT, !IS_INTERFACE,
				!IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getEStringToDeployedStorageMapEntry_Key(), this.ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getEStringToDeployedStorageMapEntry_Value(), this.getDeployedStorage(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.deployedStorageEClass, DeployedStorage.class, "DeployedStorage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getDeployedStorage_AssemblyStorage(), theAssemblyPackage.getAssemblyStorage(), null, "assemblyStorage", null, 0, 1,
				DeployedStorage.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEOperation(this.getDeployedStorage__GetComponent(), this.getDeployedComponent(), "getComponent", 0, 1, IS_UNIQUE, IS_ORDERED);

		this.initEClass(this.deployedProvidedInterfaceEClass, DeployedProvidedInterface.class, "DeployedProvidedInterface", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getDeployedProvidedInterface_ProvidedInterface(), theAssemblyPackage.getAssemblyProvidedInterface(), null, "providedInterface",
				null, 0, 1,
				DeployedProvidedInterface.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		this.initEClass(this.eStringToDeployedProvidedInterfaceMapEntryEClass, Map.Entry.class, "EStringToDeployedProvidedInterfaceMapEntry", !IS_ABSTRACT,
				!IS_INTERFACE,
				!IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getEStringToDeployedProvidedInterfaceMapEntry_Key(), this.ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getEStringToDeployedProvidedInterfaceMapEntry_Value(), this.getDeployedProvidedInterface(), null, "value", null, 0, 1,
				Map.Entry.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.deployedRequiredInterfaceEClass, DeployedRequiredInterface.class, "DeployedRequiredInterface", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getDeployedRequiredInterface_RequiredInterface(), theAssemblyPackage.getAssemblyRequiredInterface(), null, "requiredInterface",
				null, 0, 1,
				DeployedRequiredInterface.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getDeployedRequiredInterface_Requires(), this.getDeployedProvidedInterface(), null, "requires", null, 0, 1,
				DeployedRequiredInterface.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
	}

} // DeploymentPackageImpl
