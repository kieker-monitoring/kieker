/**
 */
package kieker.model.analysismodel.trace.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
import kieker.model.analysismodel.trace.OperationCall;
import kieker.model.analysismodel.trace.Trace;
import kieker.model.analysismodel.trace.TraceFactory;
import kieker.model.analysismodel.trace.TracePackage;
import kieker.model.analysismodel.type.TypePackage;
import kieker.model.analysismodel.type.impl.TypePackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class TracePackageImpl extends EPackageImpl implements TracePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass traceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass operationCallEClass = null;

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
	 * @see kieker.model.analysismodel.trace.TracePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private TracePackageImpl() {
		super(eNS_URI, TraceFactory.eINSTANCE);
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
	 * This method is used to initialize {@link TracePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static TracePackage init() {
		if (isInited) {
			return (TracePackage) EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI);
		}

		// Obtain or create and register package
		final Object registeredTracePackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		final TracePackageImpl theTracePackage = registeredTracePackage instanceof TracePackageImpl ? (TracePackageImpl) registeredTracePackage
				: new TracePackageImpl();

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
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(ExecutionPackage.eNS_URI);
		final ExecutionPackageImpl theExecutionPackage = (ExecutionPackageImpl) (registeredPackage instanceof ExecutionPackageImpl ? registeredPackage
				: ExecutionPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(SourcePackage.eNS_URI);
		final SourcePackageImpl theSourcePackage = (SourcePackageImpl) (registeredPackage instanceof SourcePackageImpl ? registeredPackage
				: SourcePackage.eINSTANCE);

		// Create package meta-data objects
		theTracePackage.createPackageContents();
		theAnalysismodelPackage.createPackageContents();
		theStatisticsPackage.createPackageContents();
		theTypePackage.createPackageContents();
		theAssemblyPackage.createPackageContents();
		theDeploymentPackage.createPackageContents();
		theExecutionPackage.createPackageContents();
		theSourcePackage.createPackageContents();

		// Initialize created meta-data
		theTracePackage.initializePackageContents();
		theAnalysismodelPackage.initializePackageContents();
		theStatisticsPackage.initializePackageContents();
		theTypePackage.initializePackageContents();
		theAssemblyPackage.initializePackageContents();
		theDeploymentPackage.initializePackageContents();
		theExecutionPackage.initializePackageContents();
		theSourcePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theTracePackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(TracePackage.eNS_URI, theTracePackage);
		return theTracePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getTrace() {
		return this.traceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getTrace_TraceID() {
		return (EAttribute) this.traceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getTrace_RootOperationCall() {
		return (EReference) this.traceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getOperationCall() {
		return this.operationCallEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getOperationCall_Operation() {
		return (EReference) this.operationCallEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getOperationCall_Parent() {
		return (EReference) this.operationCallEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getOperationCall_Children() {
		return (EReference) this.operationCallEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperationCall_Duration() {
		return (EAttribute) this.operationCallEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperationCall_Start() {
		return (EAttribute) this.operationCallEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperationCall_DurRatioToParent() {
		return (EAttribute) this.operationCallEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperationCall_DurRatioToRootParent() {
		return (EAttribute) this.operationCallEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperationCall_StackDepth() {
		return (EAttribute) this.operationCallEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperationCall_OrderIndex() {
		return (EAttribute) this.operationCallEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperationCall_Failed() {
		return (EAttribute) this.operationCallEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getOperationCall_FailedCause() {
		return (EAttribute) this.operationCallEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public TraceFactory getTraceFactory() {
		return (TraceFactory) this.getEFactoryInstance();
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
		this.traceEClass = this.createEClass(TRACE);
		this.createEAttribute(this.traceEClass, TRACE__TRACE_ID);
		this.createEReference(this.traceEClass, TRACE__ROOT_OPERATION_CALL);

		this.operationCallEClass = this.createEClass(OPERATION_CALL);
		this.createEReference(this.operationCallEClass, OPERATION_CALL__OPERATION);
		this.createEReference(this.operationCallEClass, OPERATION_CALL__PARENT);
		this.createEReference(this.operationCallEClass, OPERATION_CALL__CHILDREN);
		this.createEAttribute(this.operationCallEClass, OPERATION_CALL__DURATION);
		this.createEAttribute(this.operationCallEClass, OPERATION_CALL__START);
		this.createEAttribute(this.operationCallEClass, OPERATION_CALL__DUR_RATIO_TO_PARENT);
		this.createEAttribute(this.operationCallEClass, OPERATION_CALL__DUR_RATIO_TO_ROOT_PARENT);
		this.createEAttribute(this.operationCallEClass, OPERATION_CALL__STACK_DEPTH);
		this.createEAttribute(this.operationCallEClass, OPERATION_CALL__ORDER_INDEX);
		this.createEAttribute(this.operationCallEClass, OPERATION_CALL__FAILED);
		this.createEAttribute(this.operationCallEClass, OPERATION_CALL__FAILED_CAUSE);
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
		final AnalysismodelPackage theAnalysismodelPackage = (AnalysismodelPackage) EPackage.Registry.INSTANCE.getEPackage(AnalysismodelPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		this.initEClass(this.traceEClass, Trace.class, "Trace", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getTrace_TraceID(), this.ecorePackage.getELong(), "traceID", null, 0, 1, Trace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getTrace_RootOperationCall(), this.getOperationCall(), null, "rootOperationCall", null, 0, 1, Trace.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		this.initEClass(this.operationCallEClass, OperationCall.class, "OperationCall", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getOperationCall_Operation(), theDeploymentPackage.getDeployedOperation(), null, "operation", null, 0, 1, OperationCall.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getOperationCall_Parent(), this.getOperationCall(), this.getOperationCall_Children(), "parent", null, 0, 1, OperationCall.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getOperationCall_Children(), this.getOperationCall(), this.getOperationCall_Parent(), "children", null, 0, -1, OperationCall.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getOperationCall_Duration(), theAnalysismodelPackage.getDuration(), "duration", null, 0, 1, OperationCall.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getOperationCall_Start(), theAnalysismodelPackage.getInstant(), "start", null, 0, 1, OperationCall.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getOperationCall_DurRatioToParent(), this.ecorePackage.getEFloat(), "durRatioToParent", null, 0, 1, OperationCall.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getOperationCall_DurRatioToRootParent(), this.ecorePackage.getEFloat(), "durRatioToRootParent", null, 0, 1, OperationCall.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getOperationCall_StackDepth(), this.ecorePackage.getEInt(), "stackDepth", null, 0, 1, OperationCall.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getOperationCall_OrderIndex(), this.ecorePackage.getEInt(), "orderIndex", null, 0, 1, OperationCall.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getOperationCall_Failed(), this.ecorePackage.getEBoolean(), "failed", "false", 0, 1, OperationCall.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getOperationCall_FailedCause(), this.ecorePackage.getEString(), "failedCause", null, 0, 1, OperationCall.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
	}

} // TracePackageImpl
