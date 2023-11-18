/**
 */
package kieker.analysis.model.analysisMetaModel.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import kieker.analysis.model.analysisMetaModel.MIAnalysisComponent;
import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelFactory;
import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.MIDependency;
import kieker.analysis.model.analysisMetaModel.MIDisplay;
import kieker.analysis.model.analysisMetaModel.MIDisplayConnector;
import kieker.analysis.model.analysisMetaModel.MIFilter;
import kieker.analysis.model.analysisMetaModel.MIInputPort;
import kieker.analysis.model.analysisMetaModel.MIOutputPort;
import kieker.analysis.model.analysisMetaModel.MIPlugin;
import kieker.analysis.model.analysisMetaModel.MIPort;
import kieker.analysis.model.analysisMetaModel.MIProject;
import kieker.analysis.model.analysisMetaModel.MIProperty;
import kieker.analysis.model.analysisMetaModel.MIReader;
import kieker.analysis.model.analysisMetaModel.MIRepository;
import kieker.analysis.model.analysisMetaModel.MIRepositoryConnector;
import kieker.analysis.model.analysisMetaModel.MIView;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class MAnalysisMetaModelPackage extends EPackageImpl implements MIAnalysisMetaModelPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass projectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass pluginEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass portEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass inputPortEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass outputPortEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass propertyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass filterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass readerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass repositoryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass dependencyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass repositoryConnectorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass displayEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass viewEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass displayConnectorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass analysisComponentEClass = null;

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
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MAnalysisMetaModelPackage() {
		super(eNS_URI, MIAnalysisMetaModelFactory.eINSTANCE);
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
	 * This method is used to initialize {@link MIAnalysisMetaModelPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MIAnalysisMetaModelPackage init() {
		if (isInited) {
			return (MIAnalysisMetaModelPackage) EPackage.Registry.INSTANCE.getEPackage(MIAnalysisMetaModelPackage.eNS_URI);
		}

		// Obtain or create and register package
		final MAnalysisMetaModelPackage theAnalysisMetaModelPackage = (MAnalysisMetaModelPackage) (EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof MAnalysisMetaModelPackage ? EPackage.Registry.INSTANCE.get(eNS_URI) : new MAnalysisMetaModelPackage());

		isInited = true;

		// Create package meta-data objects
		theAnalysisMetaModelPackage.createPackageContents();

		// Initialize created meta-data
		theAnalysisMetaModelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theAnalysisMetaModelPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MIAnalysisMetaModelPackage.eNS_URI, theAnalysisMetaModelPackage);
		return theAnalysisMetaModelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getProject() {
		return this.projectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getProject_Plugins() {
		return (EReference) this.projectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getProject_Name() {
		return (EAttribute) this.projectEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getProject_Repositories() {
		return (EReference) this.projectEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getProject_Dependencies() {
		return (EReference) this.projectEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getProject_Views() {
		return (EReference) this.projectEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getProject_Properties() {
		return (EReference) this.projectEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getPlugin() {
		return this.pluginEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getPlugin_Repositories() {
		return (EReference) this.pluginEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getPlugin_OutputPorts() {
		return (EReference) this.pluginEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getPlugin_Displays() {
		return (EReference) this.pluginEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getPort() {
		return this.portEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getPort_Name() {
		return (EAttribute) this.portEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getPort_EventTypes() {
		return (EAttribute) this.portEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getPort_Id() {
		return (EAttribute) this.portEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getInputPort() {
		return this.inputPortEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getInputPort_Parent() {
		return (EReference) this.inputPortEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getOutputPort() {
		return this.outputPortEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getOutputPort_Subscribers() {
		return (EReference) this.outputPortEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getOutputPort_Parent() {
		return (EReference) this.outputPortEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getProperty() {
		return this.propertyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getProperty_Name() {
		return (EAttribute) this.propertyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getProperty_Value() {
		return (EAttribute) this.propertyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getFilter() {
		return this.filterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getFilter_InputPorts() {
		return (EReference) this.filterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getReader() {
		return this.readerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getRepository() {
		return this.repositoryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getDependency() {
		return this.dependencyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getDependency_FilePath() {
		return (EAttribute) this.dependencyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getRepositoryConnector() {
		return this.repositoryConnectorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getRepositoryConnector_Name() {
		return (EAttribute) this.repositoryConnectorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getRepositoryConnector_Repository() {
		return (EReference) this.repositoryConnectorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getRepositoryConnector_Id() {
		return (EAttribute) this.repositoryConnectorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getDisplay() {
		return this.displayEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getDisplay_Name() {
		return (EAttribute) this.displayEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getDisplay_Parent() {
		return (EReference) this.displayEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getDisplay_Id() {
		return (EAttribute) this.displayEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getView() {
		return this.viewEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getView_Name() {
		return (EAttribute) this.viewEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getView_Description() {
		return (EAttribute) this.viewEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getView_DisplayConnectors() {
		return (EReference) this.viewEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getView_Id() {
		return (EAttribute) this.viewEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getDisplayConnector() {
		return this.displayConnectorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getDisplayConnector_Name() {
		return (EAttribute) this.displayConnectorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getDisplayConnector_Display() {
		return (EReference) this.displayConnectorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getDisplayConnector_Id() {
		return (EAttribute) this.displayConnectorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getAnalysisComponent() {
		return this.analysisComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getAnalysisComponent_Name() {
		return (EAttribute) this.analysisComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getAnalysisComponent_Classname() {
		return (EAttribute) this.analysisComponentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getAnalysisComponent_Properties() {
		return (EReference) this.analysisComponentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getAnalysisComponent_Id() {
		return (EAttribute) this.analysisComponentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIAnalysisMetaModelFactory getAnalysisMetaModelFactory() {
		return (MIAnalysisMetaModelFactory) this.getEFactoryInstance();
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
		this.projectEClass = this.createEClass(PROJECT);
		this.createEReference(this.projectEClass, PROJECT__PLUGINS);
		this.createEAttribute(this.projectEClass, PROJECT__NAME);
		this.createEReference(this.projectEClass, PROJECT__REPOSITORIES);
		this.createEReference(this.projectEClass, PROJECT__DEPENDENCIES);
		this.createEReference(this.projectEClass, PROJECT__VIEWS);
		this.createEReference(this.projectEClass, PROJECT__PROPERTIES);

		this.pluginEClass = this.createEClass(PLUGIN);
		this.createEReference(this.pluginEClass, PLUGIN__REPOSITORIES);
		this.createEReference(this.pluginEClass, PLUGIN__OUTPUT_PORTS);
		this.createEReference(this.pluginEClass, PLUGIN__DISPLAYS);

		this.portEClass = this.createEClass(PORT);
		this.createEAttribute(this.portEClass, PORT__NAME);
		this.createEAttribute(this.portEClass, PORT__EVENT_TYPES);
		this.createEAttribute(this.portEClass, PORT__ID);

		this.inputPortEClass = this.createEClass(INPUT_PORT);
		this.createEReference(this.inputPortEClass, INPUT_PORT__PARENT);

		this.outputPortEClass = this.createEClass(OUTPUT_PORT);
		this.createEReference(this.outputPortEClass, OUTPUT_PORT__SUBSCRIBERS);
		this.createEReference(this.outputPortEClass, OUTPUT_PORT__PARENT);

		this.propertyEClass = this.createEClass(PROPERTY);
		this.createEAttribute(this.propertyEClass, PROPERTY__NAME);
		this.createEAttribute(this.propertyEClass, PROPERTY__VALUE);

		this.filterEClass = this.createEClass(FILTER);
		this.createEReference(this.filterEClass, FILTER__INPUT_PORTS);

		this.readerEClass = this.createEClass(READER);

		this.repositoryEClass = this.createEClass(REPOSITORY);

		this.dependencyEClass = this.createEClass(DEPENDENCY);
		this.createEAttribute(this.dependencyEClass, DEPENDENCY__FILE_PATH);

		this.repositoryConnectorEClass = this.createEClass(REPOSITORY_CONNECTOR);
		this.createEAttribute(this.repositoryConnectorEClass, REPOSITORY_CONNECTOR__NAME);
		this.createEReference(this.repositoryConnectorEClass, REPOSITORY_CONNECTOR__REPOSITORY);
		this.createEAttribute(this.repositoryConnectorEClass, REPOSITORY_CONNECTOR__ID);

		this.displayEClass = this.createEClass(DISPLAY);
		this.createEAttribute(this.displayEClass, DISPLAY__NAME);
		this.createEReference(this.displayEClass, DISPLAY__PARENT);
		this.createEAttribute(this.displayEClass, DISPLAY__ID);

		this.viewEClass = this.createEClass(VIEW);
		this.createEAttribute(this.viewEClass, VIEW__NAME);
		this.createEAttribute(this.viewEClass, VIEW__DESCRIPTION);
		this.createEReference(this.viewEClass, VIEW__DISPLAY_CONNECTORS);
		this.createEAttribute(this.viewEClass, VIEW__ID);

		this.displayConnectorEClass = this.createEClass(DISPLAY_CONNECTOR);
		this.createEAttribute(this.displayConnectorEClass, DISPLAY_CONNECTOR__NAME);
		this.createEReference(this.displayConnectorEClass, DISPLAY_CONNECTOR__DISPLAY);
		this.createEAttribute(this.displayConnectorEClass, DISPLAY_CONNECTOR__ID);

		this.analysisComponentEClass = this.createEClass(ANALYSIS_COMPONENT);
		this.createEAttribute(this.analysisComponentEClass, ANALYSIS_COMPONENT__NAME);
		this.createEAttribute(this.analysisComponentEClass, ANALYSIS_COMPONENT__CLASSNAME);
		this.createEReference(this.analysisComponentEClass, ANALYSIS_COMPONENT__PROPERTIES);
		this.createEAttribute(this.analysisComponentEClass, ANALYSIS_COMPONENT__ID);
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
		this.pluginEClass.getESuperTypes().add(this.getAnalysisComponent());
		this.inputPortEClass.getESuperTypes().add(this.getPort());
		this.outputPortEClass.getESuperTypes().add(this.getPort());
		this.filterEClass.getESuperTypes().add(this.getPlugin());
		this.readerEClass.getESuperTypes().add(this.getPlugin());
		this.repositoryEClass.getESuperTypes().add(this.getAnalysisComponent());

		// Initialize classes and features; add operations and parameters
		this.initEClass(this.projectEClass, MIProject.class, "Project", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getProject_Plugins(), this.getPlugin(), null, "plugins", null, 0, -1, MIProject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getProject_Name(), this.ecorePackage.getEString(), "name", null, 1, 1, MIProject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getProject_Repositories(), this.getRepository(), null, "repositories", null, 0, -1, MIProject.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getProject_Dependencies(), this.getDependency(), null, "dependencies", null, 0, -1, MIProject.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getProject_Views(), this.getView(), null, "views", null, 0, -1, MIProject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getProject_Properties(), this.getProperty(), null, "properties", null, 0, -1, MIProject.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.pluginEClass, MIPlugin.class, "Plugin", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getPlugin_Repositories(), this.getRepositoryConnector(), null, "repositories", null, 0, -1, MIPlugin.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getPlugin_OutputPorts(), this.getOutputPort(), this.getOutputPort_Parent(), "outputPorts", null, 0, -1, MIPlugin.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getPlugin_Displays(), this.getDisplay(), this.getDisplay_Parent(), "displays", null, 0, -1, MIPlugin.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.portEClass, MIPort.class, "Port", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getPort_Name(), this.ecorePackage.getEString(), "name", null, 1, 1, MIPort.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getPort_EventTypes(), this.ecorePackage.getEString(), "eventTypes", null, 1, -1, MIPort.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getPort_Id(), this.ecorePackage.getEString(), "id", null, 1, 1, MIPort.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, IS_ID,
				IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		this.initEClass(this.inputPortEClass, MIInputPort.class, "InputPort", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getInputPort_Parent(), this.getFilter(), this.getFilter_InputPorts(), "parent", null, 1, 1, MIInputPort.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.outputPortEClass, MIOutputPort.class, "OutputPort", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getOutputPort_Subscribers(), this.getInputPort(), null, "subscribers", null, 0, -1, MIOutputPort.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getOutputPort_Parent(), this.getPlugin(), this.getPlugin_OutputPorts(), "parent", null, 1, 1, MIOutputPort.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.propertyEClass, MIProperty.class, "Property", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getProperty_Name(), this.ecorePackage.getEString(), "name", null, 1, 1, MIProperty.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getProperty_Value(), this.ecorePackage.getEString(), "value", null, 1, 1, MIProperty.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.filterEClass, MIFilter.class, "Filter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getFilter_InputPorts(), this.getInputPort(), this.getInputPort_Parent(), "inputPorts", null, 0, -1, MIFilter.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.readerEClass, MIReader.class, "Reader", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		this.initEClass(this.repositoryEClass, MIRepository.class, "Repository", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		this.initEClass(this.dependencyEClass, MIDependency.class, "Dependency", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getDependency_FilePath(), this.ecorePackage.getEString(), "filePath", null, 1, 1, MIDependency.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.repositoryConnectorEClass, MIRepositoryConnector.class, "RepositoryConnector", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getRepositoryConnector_Name(), this.ecorePackage.getEString(), "name", null, 1, 1, MIRepositoryConnector.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getRepositoryConnector_Repository(), this.getRepository(), null, "repository", null, 1, 1, MIRepositoryConnector.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getRepositoryConnector_Id(), this.ecorePackage.getEString(), "id", null, 1, 1, MIRepositoryConnector.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		this.initEClass(this.displayEClass, MIDisplay.class, "Display", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getDisplay_Name(), this.ecorePackage.getEString(), "name", null, 1, 1, MIDisplay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getDisplay_Parent(), this.getPlugin(), this.getPlugin_Displays(), "parent", null, 1, 1, MIDisplay.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getDisplay_Id(), this.ecorePackage.getEString(), "id", null, 1, 1, MIDisplay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE,
				IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		this.initEClass(this.viewEClass, MIView.class, "View", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getView_Name(), this.ecorePackage.getEString(), "name", null, 1, 1, MIView.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getView_Description(), this.ecorePackage.getEString(), "description", null, 0, 1, MIView.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getView_DisplayConnectors(), this.getDisplayConnector(), null, "displayConnectors", null, 0, -1, MIView.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getView_Id(), this.ecorePackage.getEString(), "id", null, 1, 1, MIView.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, IS_ID,
				IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		this.initEClass(this.displayConnectorEClass, MIDisplayConnector.class, "DisplayConnector", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getDisplayConnector_Name(), this.ecorePackage.getEString(), "name", null, 1, 1, MIDisplayConnector.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getDisplayConnector_Display(), this.getDisplay(), null, "display", null, 1, 1, MIDisplayConnector.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getDisplayConnector_Id(), this.ecorePackage.getEString(), "id", null, 1, 1, MIDisplayConnector.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		this.initEClass(this.analysisComponentEClass, MIAnalysisComponent.class, "AnalysisComponent", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getAnalysisComponent_Name(), this.ecorePackage.getEString(), "name", null, 1, 1, MIAnalysisComponent.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getAnalysisComponent_Classname(), this.ecorePackage.getEString(), "classname", null, 1, 1, MIAnalysisComponent.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getAnalysisComponent_Properties(), this.getProperty(), null, "properties", null, 0, -1, MIAnalysisComponent.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getAnalysisComponent_Id(), this.ecorePackage.getEString(), "id", null, 1, 1, MIAnalysisComponent.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		// Create resource
		this.createResource(eNS_URI);
	}

} // MAnalysisMetaModelPackage
