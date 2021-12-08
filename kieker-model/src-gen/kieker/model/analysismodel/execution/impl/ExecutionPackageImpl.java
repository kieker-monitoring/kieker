/**
 */
package kieker.model.analysismodel.execution.impl;

import java.util.Map;

import kieker.model.analysismodel.AnalysismodelPackage;

import kieker.model.analysismodel.assembly.AssemblyPackage;

import kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl;

import kieker.model.analysismodel.deployment.DeploymentPackage;

import kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl;

import kieker.model.analysismodel.execution.AggregatedInvocation;
import kieker.model.analysismodel.execution.AggregatedStorageAccess;
import kieker.model.analysismodel.execution.EDirection;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Tuple;

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
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ExecutionPackageImpl extends EPackageImpl implements ExecutionPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass executionModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deployedOperationsPairToAggregatedInvocationMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aggregatedInvocationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aggregatedStorageAccessEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deployedOperationsPairToAggregatedStorageAccessMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tupleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum eDirectionEEnum = null;

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
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link ExecutionPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ExecutionPackage init() {
		if (isInited) return (ExecutionPackage)EPackage.Registry.INSTANCE.getEPackage(ExecutionPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredExecutionPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		ExecutionPackageImpl theExecutionPackage = registeredExecutionPackage instanceof ExecutionPackageImpl ? (ExecutionPackageImpl)registeredExecutionPackage : new ExecutionPackageImpl();

		isInited = true;

		// Obtain or create and register interdependencies
		Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AnalysismodelPackage.eNS_URI);
		AnalysismodelPackageImpl theAnalysismodelPackage = (AnalysismodelPackageImpl)(registeredPackage instanceof AnalysismodelPackageImpl ? registeredPackage : AnalysismodelPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(StatisticsPackage.eNS_URI);
		StatisticsPackageImpl theStatisticsPackage = (StatisticsPackageImpl)(registeredPackage instanceof StatisticsPackageImpl ? registeredPackage : StatisticsPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI);
		TypePackageImpl theTypePackage = (TypePackageImpl)(registeredPackage instanceof TypePackageImpl ? registeredPackage : TypePackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AssemblyPackage.eNS_URI);
		AssemblyPackageImpl theAssemblyPackage = (AssemblyPackageImpl)(registeredPackage instanceof AssemblyPackageImpl ? registeredPackage : AssemblyPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(DeploymentPackage.eNS_URI);
		DeploymentPackageImpl theDeploymentPackage = (DeploymentPackageImpl)(registeredPackage instanceof DeploymentPackageImpl ? registeredPackage : DeploymentPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI);
		TracePackageImpl theTracePackage = (TracePackageImpl)(registeredPackage instanceof TracePackageImpl ? registeredPackage : TracePackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(SourcesPackage.eNS_URI);
		SourcesPackageImpl theSourcesPackage = (SourcesPackageImpl)(registeredPackage instanceof SourcesPackageImpl ? registeredPackage : SourcesPackage.eINSTANCE);

		// Create package meta-data objects
		theExecutionPackage.createPackageContents();
		theAnalysismodelPackage.createPackageContents();
		theStatisticsPackage.createPackageContents();
		theTypePackage.createPackageContents();
		theAssemblyPackage.createPackageContents();
		theDeploymentPackage.createPackageContents();
		theTracePackage.createPackageContents();
		theSourcesPackage.createPackageContents();

		// Initialize created meta-data
		theExecutionPackage.initializePackageContents();
		theAnalysismodelPackage.initializePackageContents();
		theStatisticsPackage.initializePackageContents();
		theTypePackage.initializePackageContents();
		theAssemblyPackage.initializePackageContents();
		theDeploymentPackage.initializePackageContents();
		theTracePackage.initializePackageContents();
		theSourcesPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theExecutionPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ExecutionPackage.eNS_URI, theExecutionPackage);
		return theExecutionPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getExecutionModel() {
		return executionModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getExecutionModel_AggregatedInvocations() {
		return (EReference)executionModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getExecutionModel_AggregatedStorageAccesses() {
		return (EReference)executionModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDeployedOperationsPairToAggregatedInvocationMapEntry() {
		return deployedOperationsPairToAggregatedInvocationMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDeployedOperationsPairToAggregatedInvocationMapEntry_Value() {
		return (EReference)deployedOperationsPairToAggregatedInvocationMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDeployedOperationsPairToAggregatedInvocationMapEntry_Key() {
		return (EReference)deployedOperationsPairToAggregatedInvocationMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAggregatedInvocation() {
		return aggregatedInvocationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAggregatedInvocation_Source() {
		return (EReference)aggregatedInvocationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAggregatedInvocation_Target() {
		return (EReference)aggregatedInvocationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAggregatedStorageAccess() {
		return aggregatedStorageAccessEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAggregatedStorageAccess_Storage() {
		return (EReference)aggregatedStorageAccessEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAggregatedStorageAccess_Code() {
		return (EReference)aggregatedStorageAccessEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAggregatedStorageAccess_Direction() {
		return (EAttribute)aggregatedStorageAccessEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDeployedOperationsPairToAggregatedStorageAccessMapEntry() {
		return deployedOperationsPairToAggregatedStorageAccessMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDeployedOperationsPairToAggregatedStorageAccessMapEntry_Value() {
		return (EReference)deployedOperationsPairToAggregatedStorageAccessMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDeployedOperationsPairToAggregatedStorageAccessMapEntry_Key() {
		return (EReference)deployedOperationsPairToAggregatedStorageAccessMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTuple() {
		return tupleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTuple_First() {
		return (EReference)tupleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTuple_Second() {
		return (EReference)tupleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getTuple__Equals__Object() {
		return tupleEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getTuple__HashCode() {
		return tupleEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getEDirection() {
		return eDirectionEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ExecutionFactory getExecutionFactory() {
		return (ExecutionFactory)getEFactoryInstance();
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
		executionModelEClass = createEClass(EXECUTION_MODEL);
		createEReference(executionModelEClass, EXECUTION_MODEL__AGGREGATED_INVOCATIONS);
		createEReference(executionModelEClass, EXECUTION_MODEL__AGGREGATED_STORAGE_ACCESSES);

		deployedOperationsPairToAggregatedInvocationMapEntryEClass = createEClass(DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY);
		createEReference(deployedOperationsPairToAggregatedInvocationMapEntryEClass, DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY__VALUE);
		createEReference(deployedOperationsPairToAggregatedInvocationMapEntryEClass, DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY__KEY);

		aggregatedInvocationEClass = createEClass(AGGREGATED_INVOCATION);
		createEReference(aggregatedInvocationEClass, AGGREGATED_INVOCATION__SOURCE);
		createEReference(aggregatedInvocationEClass, AGGREGATED_INVOCATION__TARGET);

		aggregatedStorageAccessEClass = createEClass(AGGREGATED_STORAGE_ACCESS);
		createEReference(aggregatedStorageAccessEClass, AGGREGATED_STORAGE_ACCESS__STORAGE);
		createEReference(aggregatedStorageAccessEClass, AGGREGATED_STORAGE_ACCESS__CODE);
		createEAttribute(aggregatedStorageAccessEClass, AGGREGATED_STORAGE_ACCESS__DIRECTION);

		deployedOperationsPairToAggregatedStorageAccessMapEntryEClass = createEClass(DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY);
		createEReference(deployedOperationsPairToAggregatedStorageAccessMapEntryEClass, DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__VALUE);
		createEReference(deployedOperationsPairToAggregatedStorageAccessMapEntryEClass, DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__KEY);

		tupleEClass = createEClass(TUPLE);
		createEReference(tupleEClass, TUPLE__FIRST);
		createEReference(tupleEClass, TUPLE__SECOND);
		createEOperation(tupleEClass, TUPLE___EQUALS__OBJECT);
		createEOperation(tupleEClass, TUPLE___HASH_CODE);

		// Create enums
		eDirectionEEnum = createEEnum(EDIRECTION);
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
		DeploymentPackage theDeploymentPackage = (DeploymentPackage)EPackage.Registry.INSTANCE.getEPackage(DeploymentPackage.eNS_URI);

		// Create type parameters
		ETypeParameter tupleEClass_F = addETypeParameter(tupleEClass, "F");
		ETypeParameter tupleEClass_S = addETypeParameter(tupleEClass, "S");

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(executionModelEClass, ExecutionModel.class, "ExecutionModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getExecutionModel_AggregatedInvocations(), this.getDeployedOperationsPairToAggregatedInvocationMapEntry(), null, "aggregatedInvocations", null, 0, -1, ExecutionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEReference(getExecutionModel_AggregatedStorageAccesses(), this.getDeployedOperationsPairToAggregatedStorageAccessMapEntry(), null, "aggregatedStorageAccesses", null, 0, -1, ExecutionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(deployedOperationsPairToAggregatedInvocationMapEntryEClass, Map.Entry.class, "DeployedOperationsPairToAggregatedInvocationMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDeployedOperationsPairToAggregatedInvocationMapEntry_Value(), this.getAggregatedInvocation(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		EGenericType g1 = createEGenericType(this.getTuple());
		EGenericType g2 = createEGenericType(theDeploymentPackage.getDeployedOperation());
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(theDeploymentPackage.getDeployedOperation());
		g1.getETypeArguments().add(g2);
		initEReference(getDeployedOperationsPairToAggregatedInvocationMapEntry_Key(), g1, null, "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(aggregatedInvocationEClass, AggregatedInvocation.class, "AggregatedInvocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAggregatedInvocation_Source(), theDeploymentPackage.getDeployedOperation(), null, "source", null, 0, 1, AggregatedInvocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAggregatedInvocation_Target(), theDeploymentPackage.getDeployedOperation(), null, "target", null, 0, 1, AggregatedInvocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(aggregatedStorageAccessEClass, AggregatedStorageAccess.class, "AggregatedStorageAccess", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAggregatedStorageAccess_Storage(), theDeploymentPackage.getDeployedStorage(), null, "storage", null, 0, 1, AggregatedStorageAccess.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAggregatedStorageAccess_Code(), theDeploymentPackage.getDeployedOperation(), null, "code", null, 0, 1, AggregatedStorageAccess.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAggregatedStorageAccess_Direction(), this.getEDirection(), "direction", null, 0, 1, AggregatedStorageAccess.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(deployedOperationsPairToAggregatedStorageAccessMapEntryEClass, Map.Entry.class, "DeployedOperationsPairToAggregatedStorageAccessMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDeployedOperationsPairToAggregatedStorageAccessMapEntry_Value(), this.getAggregatedStorageAccess(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(this.getTuple());
		g2 = createEGenericType(theDeploymentPackage.getDeployedOperation());
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(theDeploymentPackage.getDeployedStorage());
		g1.getETypeArguments().add(g2);
		initEReference(getDeployedOperationsPairToAggregatedStorageAccessMapEntry_Key(), g1, null, "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(tupleEClass, Tuple.class, "Tuple", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(tupleEClass_F);
		initEReference(getTuple_First(), g1, null, "first", null, 0, 1, Tuple.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(tupleEClass_S);
		initEReference(getTuple_Second(), g1, null, "second", null, 0, 1, Tuple.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = initEOperation(getTuple__Equals__Object(), ecorePackage.getEBoolean(), "equals", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEJavaObject(), "value", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getTuple__HashCode(), ecorePackage.getEInt(), "hashCode", 0, 1, IS_UNIQUE, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(eDirectionEEnum, EDirection.class, "EDirection");
		addEEnumLiteral(eDirectionEEnum, EDirection.READ);
		addEEnumLiteral(eDirectionEEnum, EDirection.WRITE);
		addEEnumLiteral(eDirectionEEnum, EDirection.BOTH);
	}

} //ExecutionPackageImpl
