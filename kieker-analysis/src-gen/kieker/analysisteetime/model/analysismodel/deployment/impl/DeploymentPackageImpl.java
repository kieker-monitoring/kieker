/**
 */
package kieker.analysisteetime.model.analysismodel.deployment.impl;

import kieker.analysisteetime.model.analysismodel.AnalysismodelPackage;

import kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage;

import kieker.analysisteetime.model.analysismodel.architecture.impl.ArchitecturePackageImpl;

import kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentFactory;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot;

import kieker.analysisteetime.model.analysismodel.execution.ExecutionPackage;

import kieker.analysisteetime.model.analysismodel.execution.impl.ExecutionPackageImpl;

import kieker.analysisteetime.model.analysismodel.impl.AnalysismodelPackageImpl;

import kieker.analysisteetime.model.analysismodel.statistics.StatisticsPackage;

import kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl;

import kieker.analysisteetime.model.analysismodel.trace.TracePackage;

import kieker.analysisteetime.model.analysismodel.trace.impl.TracePackageImpl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DeploymentPackageImpl extends EPackageImpl implements DeploymentPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deploymentRootEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deploymentContextEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deployedComponentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deployedOperationEClass = null;

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
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DeploymentPackageImpl() {
		super(eNS_URI, DeploymentFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link DeploymentPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DeploymentPackage init() {
		if (isInited) return (DeploymentPackage)EPackage.Registry.INSTANCE.getEPackage(DeploymentPackage.eNS_URI);

		// Obtain or create and register package
		DeploymentPackageImpl theDeploymentPackage = (DeploymentPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof DeploymentPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new DeploymentPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		AnalysismodelPackageImpl theAnalysismodelPackage = (AnalysismodelPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(AnalysismodelPackage.eNS_URI) instanceof AnalysismodelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(AnalysismodelPackage.eNS_URI) : AnalysismodelPackage.eINSTANCE);
		StatisticsPackageImpl theStatisticsPackage = (StatisticsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(StatisticsPackage.eNS_URI) instanceof StatisticsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(StatisticsPackage.eNS_URI) : StatisticsPackage.eINSTANCE);
		ArchitecturePackageImpl theArchitecturePackage = (ArchitecturePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ArchitecturePackage.eNS_URI) instanceof ArchitecturePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ArchitecturePackage.eNS_URI) : ArchitecturePackage.eINSTANCE);
		ExecutionPackageImpl theExecutionPackage = (ExecutionPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ExecutionPackage.eNS_URI) instanceof ExecutionPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ExecutionPackage.eNS_URI) : ExecutionPackage.eINSTANCE);
		TracePackageImpl theTracePackage = (TracePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI) instanceof TracePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI) : TracePackage.eINSTANCE);

		// Create package meta-data objects
		theDeploymentPackage.createPackageContents();
		theAnalysismodelPackage.createPackageContents();
		theStatisticsPackage.createPackageContents();
		theArchitecturePackage.createPackageContents();
		theExecutionPackage.createPackageContents();
		theTracePackage.createPackageContents();

		// Initialize created meta-data
		theDeploymentPackage.initializePackageContents();
		theAnalysismodelPackage.initializePackageContents();
		theStatisticsPackage.initializePackageContents();
		theArchitecturePackage.initializePackageContents();
		theExecutionPackage.initializePackageContents();
		theTracePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDeploymentPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(DeploymentPackage.eNS_URI, theDeploymentPackage);
		return theDeploymentPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDeploymentRoot() {
		return deploymentRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDeploymentRoot_DeploymentContexts() {
		return (EReference)deploymentRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDeploymentContext() {
		return deploymentContextEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDeploymentContext_DeploymentRoot() {
		return (EReference)deploymentContextEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDeploymentContext_Components() {
		return (EReference)deploymentContextEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDeployedComponent() {
		return deployedComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDeployedComponent_ComponentType() {
		return (EReference)deployedComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDeployedComponent_DeploymentContext() {
		return (EReference)deployedComponentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDeployedComponent_ContainedOperations() {
		return (EReference)deployedComponentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDeployedComponent_AccessedOperations() {
		return (EReference)deployedComponentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDeployedOperation() {
		return deployedOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDeployedOperation_OperationType() {
		return (EReference)deployedOperationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDeployedOperation_ContainedComponent() {
		return (EReference)deployedOperationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDeployedOperation_AccesssedComponent() {
		return (EReference)deployedOperationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeploymentFactory getDeploymentFactory() {
		return (DeploymentFactory)getEFactoryInstance();
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
		deploymentRootEClass = createEClass(DEPLOYMENT_ROOT);
		createEReference(deploymentRootEClass, DEPLOYMENT_ROOT__DEPLOYMENT_CONTEXTS);

		deploymentContextEClass = createEClass(DEPLOYMENT_CONTEXT);
		createEReference(deploymentContextEClass, DEPLOYMENT_CONTEXT__DEPLOYMENT_ROOT);
		createEReference(deploymentContextEClass, DEPLOYMENT_CONTEXT__COMPONENTS);

		deployedComponentEClass = createEClass(DEPLOYED_COMPONENT);
		createEReference(deployedComponentEClass, DEPLOYED_COMPONENT__COMPONENT_TYPE);
		createEReference(deployedComponentEClass, DEPLOYED_COMPONENT__DEPLOYMENT_CONTEXT);
		createEReference(deployedComponentEClass, DEPLOYED_COMPONENT__CONTAINED_OPERATIONS);
		createEReference(deployedComponentEClass, DEPLOYED_COMPONENT__ACCESSED_OPERATIONS);

		deployedOperationEClass = createEClass(DEPLOYED_OPERATION);
		createEReference(deployedOperationEClass, DEPLOYED_OPERATION__OPERATION_TYPE);
		createEReference(deployedOperationEClass, DEPLOYED_OPERATION__CONTAINED_COMPONENT);
		createEReference(deployedOperationEClass, DEPLOYED_OPERATION__ACCESSSED_COMPONENT);
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
		ArchitecturePackage theArchitecturePackage = (ArchitecturePackage)EPackage.Registry.INSTANCE.getEPackage(ArchitecturePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(deploymentRootEClass, DeploymentRoot.class, "DeploymentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDeploymentRoot_DeploymentContexts(), this.getDeploymentContext(), this.getDeploymentContext_DeploymentRoot(), "deploymentContexts", null, 0, -1, DeploymentRoot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(deploymentContextEClass, DeploymentContext.class, "DeploymentContext", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDeploymentContext_DeploymentRoot(), this.getDeploymentRoot(), this.getDeploymentRoot_DeploymentContexts(), "deploymentRoot", null, 0, 1, DeploymentContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDeploymentContext_Components(), this.getDeployedComponent(), this.getDeployedComponent_DeploymentContext(), "components", null, 0, -1, DeploymentContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(deployedComponentEClass, DeployedComponent.class, "DeployedComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDeployedComponent_ComponentType(), theArchitecturePackage.getComponentType(), null, "componentType", null, 0, 1, DeployedComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDeployedComponent_DeploymentContext(), this.getDeploymentContext(), this.getDeploymentContext_Components(), "deploymentContext", null, 0, 1, DeployedComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDeployedComponent_ContainedOperations(), this.getDeployedOperation(), this.getDeployedOperation_ContainedComponent(), "containedOperations", null, 0, -1, DeployedComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEReference(getDeployedComponent_AccessedOperations(), this.getDeployedOperation(), this.getDeployedOperation_AccesssedComponent(), "accessedOperations", null, 0, -1, DeployedComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(deployedOperationEClass, DeployedOperation.class, "DeployedOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDeployedOperation_OperationType(), theArchitecturePackage.getOperationType(), null, "operationType", null, 0, 1, DeployedOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDeployedOperation_ContainedComponent(), this.getDeployedComponent(), this.getDeployedComponent_ContainedOperations(), "containedComponent", null, 0, 1, DeployedOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDeployedOperation_AccesssedComponent(), this.getDeployedComponent(), this.getDeployedComponent_AccessedOperations(), "accesssedComponent", null, 0, 1, DeployedOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
	}

} //DeploymentPackageImpl
