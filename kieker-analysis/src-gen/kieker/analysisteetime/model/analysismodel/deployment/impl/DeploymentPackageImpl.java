/**
 */
package kieker.analysisteetime.model.analysismodel.deployment.impl;

import java.util.Map;
import kieker.analysisteetime.model.analysismodel.AnalysismodelPackage;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyPackage;
import kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyPackageImpl;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentFactory;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentModel;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionPackage;

import kieker.analysisteetime.model.analysismodel.execution.impl.ExecutionPackageImpl;

import kieker.analysisteetime.model.analysismodel.impl.AnalysismodelPackageImpl;

import kieker.analysisteetime.model.analysismodel.statistics.StatisticsPackage;

import kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl;

import kieker.analysisteetime.model.analysismodel.trace.TracePackage;

import kieker.analysisteetime.model.analysismodel.trace.impl.TracePackageImpl;

import kieker.analysisteetime.model.analysismodel.type.TypePackage;
import kieker.analysisteetime.model.analysismodel.type.impl.TypePackageImpl;
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
public class DeploymentPackageImpl extends EPackageImpl implements DeploymentPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deploymentModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eStringToDeploymentContextMapEntryEClass = null;

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
	private EClass eStringToDeployedComponentMapEntryEClass = null;

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
	private EClass eStringToDeployedOperationMapEntryEClass = null;

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
		TypePackageImpl theTypePackage = (TypePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI) instanceof TypePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI) : TypePackage.eINSTANCE);
		AssemblyPackageImpl theAssemblyPackage = (AssemblyPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(AssemblyPackage.eNS_URI) instanceof AssemblyPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(AssemblyPackage.eNS_URI) : AssemblyPackage.eINSTANCE);
		ExecutionPackageImpl theExecutionPackage = (ExecutionPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ExecutionPackage.eNS_URI) instanceof ExecutionPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ExecutionPackage.eNS_URI) : ExecutionPackage.eINSTANCE);
		TracePackageImpl theTracePackage = (TracePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI) instanceof TracePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI) : TracePackage.eINSTANCE);

		// Create package meta-data objects
		theDeploymentPackage.createPackageContents();
		theAnalysismodelPackage.createPackageContents();
		theStatisticsPackage.createPackageContents();
		theTypePackage.createPackageContents();
		theAssemblyPackage.createPackageContents();
		theExecutionPackage.createPackageContents();
		theTracePackage.createPackageContents();

		// Initialize created meta-data
		theDeploymentPackage.initializePackageContents();
		theAnalysismodelPackage.initializePackageContents();
		theStatisticsPackage.initializePackageContents();
		theTypePackage.initializePackageContents();
		theAssemblyPackage.initializePackageContents();
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
	public EClass getDeploymentModel() {
		return deploymentModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDeploymentModel_DeploymentContexts() {
		return (EReference)deploymentModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEStringToDeploymentContextMapEntry() {
		return eStringToDeploymentContextMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEStringToDeploymentContextMapEntry_Key() {
		return (EAttribute)eStringToDeploymentContextMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEStringToDeploymentContextMapEntry_Value() {
		return (EReference)eStringToDeploymentContextMapEntryEClass.getEStructuralFeatures().get(1);
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
	public EReference getDeploymentContext_Components() {
		return (EReference)deploymentContextEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEStringToDeployedComponentMapEntry() {
		return eStringToDeployedComponentMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEStringToDeployedComponentMapEntry_Key() {
		return (EAttribute)eStringToDeployedComponentMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEStringToDeployedComponentMapEntry_Value() {
		return (EReference)eStringToDeployedComponentMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDeploymentContext_Name() {
		return (EAttribute)deploymentContextEClass.getEStructuralFeatures().get(0);
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
	public EReference getDeployedComponent_AssemblyComponent() {
		return (EReference)deployedComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDeployedComponent_ContainedOperations() {
		return (EReference)deployedComponentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDeployedComponent__GetDeploymentContext() {
		return deployedComponentEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEStringToDeployedOperationMapEntry() {
		return eStringToDeployedOperationMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEStringToDeployedOperationMapEntry_Key() {
		return (EAttribute)eStringToDeployedOperationMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEStringToDeployedOperationMapEntry_Value() {
		return (EReference)eStringToDeployedOperationMapEntryEClass.getEStructuralFeatures().get(1);
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
	public EReference getDeployedOperation_AssemblyOperation() {
		return (EReference)deployedOperationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getDeployedOperation__GetComponent() {
		return deployedOperationEClass.getEOperations().get(0);
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
		deploymentModelEClass = createEClass(DEPLOYMENT_MODEL);
		createEReference(deploymentModelEClass, DEPLOYMENT_MODEL__DEPLOYMENT_CONTEXTS);

		eStringToDeploymentContextMapEntryEClass = createEClass(ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY);
		createEAttribute(eStringToDeploymentContextMapEntryEClass, ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY__KEY);
		createEReference(eStringToDeploymentContextMapEntryEClass, ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY__VALUE);

		deploymentContextEClass = createEClass(DEPLOYMENT_CONTEXT);
		createEAttribute(deploymentContextEClass, DEPLOYMENT_CONTEXT__NAME);
		createEReference(deploymentContextEClass, DEPLOYMENT_CONTEXT__COMPONENTS);

		eStringToDeployedComponentMapEntryEClass = createEClass(ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY);
		createEAttribute(eStringToDeployedComponentMapEntryEClass, ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY__KEY);
		createEReference(eStringToDeployedComponentMapEntryEClass, ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY__VALUE);

		deployedComponentEClass = createEClass(DEPLOYED_COMPONENT);
		createEReference(deployedComponentEClass, DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT);
		createEReference(deployedComponentEClass, DEPLOYED_COMPONENT__CONTAINED_OPERATIONS);
		createEOperation(deployedComponentEClass, DEPLOYED_COMPONENT___GET_DEPLOYMENT_CONTEXT);

		eStringToDeployedOperationMapEntryEClass = createEClass(ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY);
		createEAttribute(eStringToDeployedOperationMapEntryEClass, ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY__KEY);
		createEReference(eStringToDeployedOperationMapEntryEClass, ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY__VALUE);

		deployedOperationEClass = createEClass(DEPLOYED_OPERATION);
		createEReference(deployedOperationEClass, DEPLOYED_OPERATION__ASSEMBLY_OPERATION);
		createEOperation(deployedOperationEClass, DEPLOYED_OPERATION___GET_COMPONENT);
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
		AssemblyPackage theAssemblyPackage = (AssemblyPackage)EPackage.Registry.INSTANCE.getEPackage(AssemblyPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(deploymentModelEClass, DeploymentModel.class, "DeploymentModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDeploymentModel_DeploymentContexts(), this.getEStringToDeploymentContextMapEntry(), null, "deploymentContexts", null, 0, -1, DeploymentModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(eStringToDeploymentContextMapEntryEClass, Map.Entry.class, "EStringToDeploymentContextMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEStringToDeploymentContextMapEntry_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEStringToDeploymentContextMapEntry_Value(), this.getDeploymentContext(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(deploymentContextEClass, DeploymentContext.class, "DeploymentContext", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDeploymentContext_Name(), ecorePackage.getEString(), "name", null, 0, 1, DeploymentContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDeploymentContext_Components(), this.getEStringToDeployedComponentMapEntry(), null, "components", null, 0, -1, DeploymentContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(eStringToDeployedComponentMapEntryEClass, Map.Entry.class, "EStringToDeployedComponentMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEStringToDeployedComponentMapEntry_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEStringToDeployedComponentMapEntry_Value(), this.getDeployedComponent(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(deployedComponentEClass, DeployedComponent.class, "DeployedComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDeployedComponent_AssemblyComponent(), theAssemblyPackage.getAssemblyComponent(), null, "assemblyComponent", null, 0, 1, DeployedComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDeployedComponent_ContainedOperations(), this.getEStringToDeployedOperationMapEntry(), null, "containedOperations", null, 0, -1, DeployedComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEOperation(getDeployedComponent__GetDeploymentContext(), this.getDeploymentContext(), "getDeploymentContext", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(eStringToDeployedOperationMapEntryEClass, Map.Entry.class, "EStringToDeployedOperationMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEStringToDeployedOperationMapEntry_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEStringToDeployedOperationMapEntry_Value(), this.getDeployedOperation(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(deployedOperationEClass, DeployedOperation.class, "DeployedOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDeployedOperation_AssemblyOperation(), theAssemblyPackage.getAssemblyOperation(), null, "assemblyOperation", null, 0, 1, DeployedOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getDeployedOperation__GetComponent(), this.getDeployedComponent(), "getComponent", 0, 1, IS_UNIQUE, IS_ORDERED);
	}

} //DeploymentPackageImpl
