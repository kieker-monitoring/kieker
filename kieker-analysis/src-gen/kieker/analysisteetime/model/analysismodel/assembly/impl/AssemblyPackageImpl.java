/**
 */
package kieker.analysisteetime.model.analysismodel.assembly.impl;

import java.util.Map;

import kieker.analysisteetime.model.analysismodel.AnalysismodelPackage;

import kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyFactory;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyOperation;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyPackage;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyRoot;

import kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage;

import kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl;

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
public class AssemblyPackageImpl extends EPackageImpl implements AssemblyPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass assemblyRootEClass = null;

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
	 * @see kieker.analysisteetime.model.analysismodel.assembly.AssemblyPackage#eNS_URI
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
		AssemblyPackageImpl theAssemblyPackage = (AssemblyPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof AssemblyPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new AssemblyPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		AnalysismodelPackageImpl theAnalysismodelPackage = (AnalysismodelPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(AnalysismodelPackage.eNS_URI) instanceof AnalysismodelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(AnalysismodelPackage.eNS_URI) : AnalysismodelPackage.eINSTANCE);
		StatisticsPackageImpl theStatisticsPackage = (StatisticsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(StatisticsPackage.eNS_URI) instanceof StatisticsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(StatisticsPackage.eNS_URI) : StatisticsPackage.eINSTANCE);
		TypePackageImpl theTypePackage = (TypePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI) instanceof TypePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI) : TypePackage.eINSTANCE);
		DeploymentPackageImpl theDeploymentPackage = (DeploymentPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DeploymentPackage.eNS_URI) instanceof DeploymentPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DeploymentPackage.eNS_URI) : DeploymentPackage.eINSTANCE);
		ExecutionPackageImpl theExecutionPackage = (ExecutionPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ExecutionPackage.eNS_URI) instanceof ExecutionPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ExecutionPackage.eNS_URI) : ExecutionPackage.eINSTANCE);
		TracePackageImpl theTracePackage = (TracePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI) instanceof TracePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI) : TracePackage.eINSTANCE);

		// Create package meta-data objects
		theAssemblyPackage.createPackageContents();
		theAnalysismodelPackage.createPackageContents();
		theStatisticsPackage.createPackageContents();
		theTypePackage.createPackageContents();
		theDeploymentPackage.createPackageContents();
		theExecutionPackage.createPackageContents();
		theTracePackage.createPackageContents();

		// Initialize created meta-data
		theAssemblyPackage.initializePackageContents();
		theAnalysismodelPackage.initializePackageContents();
		theStatisticsPackage.initializePackageContents();
		theTypePackage.initializePackageContents();
		theDeploymentPackage.initializePackageContents();
		theExecutionPackage.initializePackageContents();
		theTracePackage.initializePackageContents();

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
	public EClass getAssemblyRoot() {
		return assemblyRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAssemblyRoot_AssemblyComponents() {
		return (EReference)assemblyRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEStringToAssemblyComponentMapEntry() {
		return eStringToAssemblyComponentMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEStringToAssemblyComponentMapEntry_Key() {
		return (EAttribute)eStringToAssemblyComponentMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEStringToAssemblyComponentMapEntry_Value() {
		return (EReference)eStringToAssemblyComponentMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAssemblyComponent() {
		return assemblyComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssemblyComponent_Signature() {
		return (EAttribute)assemblyComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAssemblyComponent_AssemblyOperations() {
		return (EReference)assemblyComponentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getAssemblyComponent__GetAssemblyRoot() {
		return assemblyComponentEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEStringToAssemblyOperationMapEntry() {
		return eStringToAssemblyOperationMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEStringToAssemblyOperationMapEntry_Key() {
		return (EAttribute)eStringToAssemblyOperationMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEStringToAssemblyOperationMapEntry_Value() {
		return (EReference)eStringToAssemblyOperationMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAssemblyOperation() {
		return assemblyOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssemblyOperation_Signature() {
		return (EAttribute)assemblyOperationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getAssemblyOperation__GetAssemblyComponent() {
		return assemblyOperationEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
		assemblyRootEClass = createEClass(ASSEMBLY_ROOT);
		createEReference(assemblyRootEClass, ASSEMBLY_ROOT__ASSEMBLY_COMPONENTS);

		eStringToAssemblyComponentMapEntryEClass = createEClass(ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY);
		createEAttribute(eStringToAssemblyComponentMapEntryEClass, ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__KEY);
		createEReference(eStringToAssemblyComponentMapEntryEClass, ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__VALUE);

		assemblyComponentEClass = createEClass(ASSEMBLY_COMPONENT);
		createEAttribute(assemblyComponentEClass, ASSEMBLY_COMPONENT__SIGNATURE);
		createEReference(assemblyComponentEClass, ASSEMBLY_COMPONENT__ASSEMBLY_OPERATIONS);
		createEOperation(assemblyComponentEClass, ASSEMBLY_COMPONENT___GET_ASSEMBLY_ROOT);

		eStringToAssemblyOperationMapEntryEClass = createEClass(ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY);
		createEAttribute(eStringToAssemblyOperationMapEntryEClass, ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY__KEY);
		createEReference(eStringToAssemblyOperationMapEntryEClass, ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY__VALUE);

		assemblyOperationEClass = createEClass(ASSEMBLY_OPERATION);
		createEAttribute(assemblyOperationEClass, ASSEMBLY_OPERATION__SIGNATURE);
		createEOperation(assemblyOperationEClass, ASSEMBLY_OPERATION___GET_ASSEMBLY_COMPONENT);
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(assemblyRootEClass, AssemblyRoot.class, "AssemblyRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAssemblyRoot_AssemblyComponents(), this.getEStringToAssemblyComponentMapEntry(), null, "assemblyComponents", null, 0, -1, AssemblyRoot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(eStringToAssemblyComponentMapEntryEClass, Map.Entry.class, "EStringToAssemblyComponentMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEStringToAssemblyComponentMapEntry_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEStringToAssemblyComponentMapEntry_Value(), this.getAssemblyComponent(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(assemblyComponentEClass, AssemblyComponent.class, "AssemblyComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAssemblyComponent_Signature(), ecorePackage.getEString(), "signature", null, 0, 1, AssemblyComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAssemblyComponent_AssemblyOperations(), this.getEStringToAssemblyOperationMapEntry(), null, "assemblyOperations", null, 0, -1, AssemblyComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEOperation(getAssemblyComponent__GetAssemblyRoot(), this.getAssemblyRoot(), "getAssemblyRoot", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(eStringToAssemblyOperationMapEntryEClass, Map.Entry.class, "EStringToAssemblyOperationMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEStringToAssemblyOperationMapEntry_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEStringToAssemblyOperationMapEntry_Value(), this.getAssemblyOperation(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(assemblyOperationEClass, AssemblyOperation.class, "AssemblyOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAssemblyOperation_Signature(), ecorePackage.getEString(), "signature", null, 0, 1, AssemblyOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getAssemblyOperation__GetAssemblyComponent(), this.getAssemblyComponent(), "getAssemblyComponent", 0, 1, IS_UNIQUE, IS_ORDERED);
	}

} //AssemblyPackageImpl
