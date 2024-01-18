/**
 */
package kieker.model.analysismodel.execution.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import kieker.model.analysismodel.AnalysismodelPackage;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl;
import kieker.model.analysismodel.execution.EDirection;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.OperationDataflow;
import kieker.model.analysismodel.execution.StorageDataflow;
import kieker.model.analysismodel.execution.Tuple;
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
public class ExecutionPackageImpl extends EPackageImpl implements ExecutionPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass executionModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass deployedOperationsPairToInvocationMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass invocationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass storageDataflowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass deployedOperationsPairToDeployedStorageMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass tupleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass operationDataflowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass deployedOperationsPairToDeployedOperationsMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EEnum eDirectionEEnum = null;

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
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ExecutionPackageImpl() {
		super(eNS_URI, ExecutionFactory.eINSTANCE);
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
	 * This method is used to initialize {@link ExecutionPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ExecutionPackage init() {
		if (isInited) {
			return (ExecutionPackage) EPackage.Registry.INSTANCE.getEPackage(ExecutionPackage.eNS_URI);
		}

		// Obtain or create and register package
		final Object registeredExecutionPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		final ExecutionPackageImpl theExecutionPackage = registeredExecutionPackage instanceof ExecutionPackageImpl
				? (ExecutionPackageImpl) registeredExecutionPackage
				: new ExecutionPackageImpl();

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
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(DeploymentPackage.eNS_URI);
		final DeploymentPackageImpl theDeploymentPackage = (DeploymentPackageImpl) (registeredPackage instanceof DeploymentPackageImpl ? registeredPackage
				: DeploymentPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI);
		final TracePackageImpl theTracePackage = (TracePackageImpl) (registeredPackage instanceof TracePackageImpl ? registeredPackage : TracePackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(SourcePackage.eNS_URI);
		final SourcePackageImpl theSourcePackage = (SourcePackageImpl) (registeredPackage instanceof SourcePackageImpl ? registeredPackage
				: SourcePackage.eINSTANCE);

		// Create package meta-data objects
		theExecutionPackage.createPackageContents();
		theAnalysismodelPackage.createPackageContents();
		theStatisticsPackage.createPackageContents();
		theTypePackage.createPackageContents();
		theAssemblyPackage.createPackageContents();
		theDeploymentPackage.createPackageContents();
		theTracePackage.createPackageContents();
		theSourcePackage.createPackageContents();

		// Initialize created meta-data
		theExecutionPackage.initializePackageContents();
		theAnalysismodelPackage.initializePackageContents();
		theStatisticsPackage.initializePackageContents();
		theTypePackage.initializePackageContents();
		theAssemblyPackage.initializePackageContents();
		theDeploymentPackage.initializePackageContents();
		theTracePackage.initializePackageContents();
		theSourcePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theExecutionPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ExecutionPackage.eNS_URI, theExecutionPackage);
		return theExecutionPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getExecutionModel() {
		return this.executionModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getExecutionModel_Invocations() {
		return (EReference) this.executionModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getExecutionModel_StorageDataflows() {
		return (EReference) this.executionModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getExecutionModel_OperationDataflows() {
		return (EReference) this.executionModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getDeployedOperationsPairToInvocationMapEntry() {
		return this.deployedOperationsPairToInvocationMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedOperationsPairToInvocationMapEntry_Value() {
		return (EReference) this.deployedOperationsPairToInvocationMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedOperationsPairToInvocationMapEntry_Key() {
		return (EReference) this.deployedOperationsPairToInvocationMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getInvocation() {
		return this.invocationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getInvocation_Caller() {
		return (EReference) this.invocationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getInvocation_Callee() {
		return (EReference) this.invocationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getStorageDataflow() {
		return this.storageDataflowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getStorageDataflow_Storage() {
		return (EReference) this.storageDataflowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getStorageDataflow_Code() {
		return (EReference) this.storageDataflowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getStorageDataflow_Direction() {
		return (EAttribute) this.storageDataflowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getDeployedOperationsPairToDeployedStorageMapEntry() {
		return this.deployedOperationsPairToDeployedStorageMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedOperationsPairToDeployedStorageMapEntry_Value() {
		return (EReference) this.deployedOperationsPairToDeployedStorageMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedOperationsPairToDeployedStorageMapEntry_Key() {
		return (EReference) this.deployedOperationsPairToDeployedStorageMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getTuple() {
		return this.tupleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getTuple_First() {
		return (EReference) this.tupleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getTuple_Second() {
		return (EReference) this.tupleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EOperation getTuple__Equals__Object() {
		return this.tupleEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EOperation getTuple__HashCode() {
		return this.tupleEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getOperationDataflow() {
		return this.operationDataflowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getOperationDataflow_Caller() {
		return (EReference) this.operationDataflowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getOperationDataflow_Callee() {
		return (EReference) this.operationDataflowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperationDataflow_Direction() {
		return (EAttribute) this.operationDataflowEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getDeployedOperationsPairToDeployedOperationsMapEntry() {
		return this.deployedOperationsPairToDeployedOperationsMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedOperationsPairToDeployedOperationsMapEntry_Value() {
		return (EReference) this.deployedOperationsPairToDeployedOperationsMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getDeployedOperationsPairToDeployedOperationsMapEntry_Key() {
		return (EReference) this.deployedOperationsPairToDeployedOperationsMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EEnum getEDirection() {
		return this.eDirectionEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ExecutionFactory getExecutionFactory() {
		return (ExecutionFactory) this.getEFactoryInstance();
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
		this.executionModelEClass = this.createEClass(EXECUTION_MODEL);
		this.createEReference(this.executionModelEClass, EXECUTION_MODEL__INVOCATIONS);
		this.createEReference(this.executionModelEClass, EXECUTION_MODEL__STORAGE_DATAFLOWS);
		this.createEReference(this.executionModelEClass, EXECUTION_MODEL__OPERATION_DATAFLOWS);

		this.deployedOperationsPairToInvocationMapEntryEClass = this.createEClass(DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY);
		this.createEReference(this.deployedOperationsPairToInvocationMapEntryEClass, DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__VALUE);
		this.createEReference(this.deployedOperationsPairToInvocationMapEntryEClass, DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__KEY);

		this.invocationEClass = this.createEClass(INVOCATION);
		this.createEReference(this.invocationEClass, INVOCATION__CALLER);
		this.createEReference(this.invocationEClass, INVOCATION__CALLEE);

		this.storageDataflowEClass = this.createEClass(STORAGE_DATAFLOW);
		this.createEReference(this.storageDataflowEClass, STORAGE_DATAFLOW__STORAGE);
		this.createEReference(this.storageDataflowEClass, STORAGE_DATAFLOW__CODE);
		this.createEAttribute(this.storageDataflowEClass, STORAGE_DATAFLOW__DIRECTION);

		this.deployedOperationsPairToDeployedStorageMapEntryEClass = this.createEClass(DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY);
		this.createEReference(this.deployedOperationsPairToDeployedStorageMapEntryEClass, DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY__VALUE);
		this.createEReference(this.deployedOperationsPairToDeployedStorageMapEntryEClass, DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY__KEY);

		this.tupleEClass = this.createEClass(TUPLE);
		this.createEReference(this.tupleEClass, TUPLE__FIRST);
		this.createEReference(this.tupleEClass, TUPLE__SECOND);
		this.createEOperation(this.tupleEClass, TUPLE___EQUALS__OBJECT);
		this.createEOperation(this.tupleEClass, TUPLE___HASH_CODE);

		this.operationDataflowEClass = this.createEClass(OPERATION_DATAFLOW);
		this.createEReference(this.operationDataflowEClass, OPERATION_DATAFLOW__CALLER);
		this.createEReference(this.operationDataflowEClass, OPERATION_DATAFLOW__CALLEE);
		this.createEAttribute(this.operationDataflowEClass, OPERATION_DATAFLOW__DIRECTION);

		this.deployedOperationsPairToDeployedOperationsMapEntryEClass = this.createEClass(DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY);
		this.createEReference(this.deployedOperationsPairToDeployedOperationsMapEntryEClass, DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY__VALUE);
		this.createEReference(this.deployedOperationsPairToDeployedOperationsMapEntryEClass, DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY__KEY);

		// Create enums
		this.eDirectionEEnum = this.createEEnum(EDIRECTION);
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
		final DeploymentPackage theDeploymentPackage = (DeploymentPackage) EPackage.Registry.INSTANCE.getEPackage(DeploymentPackage.eNS_URI);

		// Create type parameters
		final ETypeParameter tupleEClass_F = this.addETypeParameter(this.tupleEClass, "F");
		final ETypeParameter tupleEClass_S = this.addETypeParameter(this.tupleEClass, "S");

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		this.initEClass(this.executionModelEClass, ExecutionModel.class, "ExecutionModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getExecutionModel_Invocations(), this.getDeployedOperationsPairToInvocationMapEntry(), null, "invocations", null, 0, -1,
				ExecutionModel.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		this.initEReference(this.getExecutionModel_StorageDataflows(), this.getDeployedOperationsPairToDeployedStorageMapEntry(), null, "storageDataflows", null, 0,
				-1,
				ExecutionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				!IS_ORDERED);
		this.initEReference(this.getExecutionModel_OperationDataflows(), this.getDeployedOperationsPairToDeployedOperationsMapEntry(), null, "operationDataflows",
				null, 0, -1,
				ExecutionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				!IS_ORDERED);

		this.initEClass(this.deployedOperationsPairToInvocationMapEntryEClass, Map.Entry.class, "DeployedOperationsPairToInvocationMapEntry", !IS_ABSTRACT,
				!IS_INTERFACE,
				!IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getDeployedOperationsPairToInvocationMapEntry_Value(), this.getInvocation(), null, "value", null, 0, 1, Map.Entry.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		EGenericType g1 = this.createEGenericType(this.getTuple());
		EGenericType g2 = this.createEGenericType(theDeploymentPackage.getDeployedOperation());
		g1.getETypeArguments().add(g2);
		g2 = this.createEGenericType(theDeploymentPackage.getDeployedOperation());
		g1.getETypeArguments().add(g2);
		this.initEReference(this.getDeployedOperationsPairToInvocationMapEntry_Key(), g1, null, "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.invocationEClass, Invocation.class, "Invocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getInvocation_Caller(), theDeploymentPackage.getDeployedOperation(), null, "caller", null, 0, 1, Invocation.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getInvocation_Callee(), theDeploymentPackage.getDeployedOperation(), null, "callee", null, 0, 1, Invocation.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.storageDataflowEClass, StorageDataflow.class, "StorageDataflow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getStorageDataflow_Storage(), theDeploymentPackage.getDeployedStorage(), null, "storage", null, 0, 1, StorageDataflow.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getStorageDataflow_Code(), theDeploymentPackage.getDeployedOperation(), null, "code", null, 0, 1, StorageDataflow.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getStorageDataflow_Direction(), this.getEDirection(), "direction", null, 0, 1, StorageDataflow.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.deployedOperationsPairToDeployedStorageMapEntryEClass, Map.Entry.class, "DeployedOperationsPairToDeployedStorageMapEntry", !IS_ABSTRACT,
				!IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getDeployedOperationsPairToDeployedStorageMapEntry_Value(), this.getStorageDataflow(), null, "value", null, 0, 1, Map.Entry.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = this.createEGenericType(this.getTuple());
		g2 = this.createEGenericType(theDeploymentPackage.getDeployedOperation());
		g1.getETypeArguments().add(g2);
		g2 = this.createEGenericType(theDeploymentPackage.getDeployedStorage());
		g1.getETypeArguments().add(g2);
		this.initEReference(this.getDeployedOperationsPairToDeployedStorageMapEntry_Key(), g1, null, "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.tupleEClass, Tuple.class, "Tuple", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = this.createEGenericType(tupleEClass_F);
		this.initEReference(this.getTuple_First(), g1, null, "first", null, 0, 1, Tuple.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = this.createEGenericType(tupleEClass_S);
		this.initEReference(this.getTuple_Second(), g1, null, "second", null, 0, 1, Tuple.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		final EOperation op = this.initEOperation(this.getTuple__Equals__Object(), this.ecorePackage.getEBoolean(), "equals", 0, 1, IS_UNIQUE, IS_ORDERED);
		this.addEParameter(op, this.ecorePackage.getEJavaObject(), "value", 0, 1, IS_UNIQUE, IS_ORDERED);

		this.initEOperation(this.getTuple__HashCode(), this.ecorePackage.getEInt(), "hashCode", 0, 1, IS_UNIQUE, IS_ORDERED);

		this.initEClass(this.operationDataflowEClass, OperationDataflow.class, "OperationDataflow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getOperationDataflow_Caller(), theDeploymentPackage.getDeployedOperation(), null, "caller", null, 0, 1, OperationDataflow.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getOperationDataflow_Callee(), theDeploymentPackage.getDeployedOperation(), null, "callee", null, 0, 1, OperationDataflow.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getOperationDataflow_Direction(), this.getEDirection(), "direction", null, 0, 1, OperationDataflow.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.deployedOperationsPairToDeployedOperationsMapEntryEClass, Map.Entry.class, "DeployedOperationsPairToDeployedOperationsMapEntry",
				!IS_ABSTRACT,
				!IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getDeployedOperationsPairToDeployedOperationsMapEntry_Value(), this.getOperationDataflow(), null, "value", null, 0, 1,
				Map.Entry.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = this.createEGenericType(this.getTuple());
		g2 = this.createEGenericType(theDeploymentPackage.getDeployedOperation());
		g1.getETypeArguments().add(g2);
		g2 = this.createEGenericType(theDeploymentPackage.getDeployedOperation());
		g1.getETypeArguments().add(g2);
		this.initEReference(this.getDeployedOperationsPairToDeployedOperationsMapEntry_Key(), g1, null, "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		this.initEEnum(this.eDirectionEEnum, EDirection.class, "EDirection");
		this.addEEnumLiteral(this.eDirectionEEnum, EDirection.READ);
		this.addEEnumLiteral(this.eDirectionEEnum, EDirection.WRITE);
		this.addEEnumLiteral(this.eDirectionEEnum, EDirection.BOTH);
		this.addEEnumLiteral(this.eDirectionEEnum, EDirection.NONE);
	}

} // ExecutionPackageImpl
