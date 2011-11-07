/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel.impl;

import kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelFactory;
import kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.IAnalysisPlugin;
import kieker.analysis.model.analysisMetaModel.IConfigurable;
import kieker.analysis.model.analysisMetaModel.IConnector;
import kieker.analysis.model.analysisMetaModel.IInputPort;
import kieker.analysis.model.analysisMetaModel.IOutputPort;
import kieker.analysis.model.analysisMetaModel.IPlugin;
import kieker.analysis.model.analysisMetaModel.IPort;
import kieker.analysis.model.analysisMetaModel.IProject;
import kieker.analysis.model.analysisMetaModel.IProperty;
import kieker.analysis.model.analysisMetaModel.IReader;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * 
 * @generated
 */
public class AnalysisMetaModelPackage extends EPackageImpl implements IAnalysisMetaModelPackage {
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
	private EClass connectorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass configurableEClass = null;

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
	private EClass analysisPluginEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass readerEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory method {@link #init init()}, which also performs initialization of the package, or
	 * returns the registered package, if one already exists. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private AnalysisMetaModelPackage() {
		super(IAnalysisMetaModelPackage.eNS_URI, IAnalysisMetaModelFactory.eINSTANCE);
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
	 * This method is used to initialize {@link IAnalysisMetaModelPackage#eINSTANCE} when that field is accessed. Clients should not invoke it directly. Instead,
	 * they should simply access that field to obtain the package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static IAnalysisMetaModelPackage init() {
		if (AnalysisMetaModelPackage.isInited) {
			return (IAnalysisMetaModelPackage) EPackage.Registry.INSTANCE.getEPackage(IAnalysisMetaModelPackage.eNS_URI);
		}

		// Obtain or create and register package
		final AnalysisMetaModelPackage theAnalysisMetaModelPackage = (AnalysisMetaModelPackage) (EPackage.Registry.INSTANCE.get(IAnalysisMetaModelPackage.eNS_URI) instanceof AnalysisMetaModelPackage ? EPackage.Registry.INSTANCE
				.get(IAnalysisMetaModelPackage.eNS_URI) : new AnalysisMetaModelPackage());

		AnalysisMetaModelPackage.isInited = true;

		// Create package meta-data objects
		theAnalysisMetaModelPackage.createPackageContents();

		// Initialize created meta-data
		theAnalysisMetaModelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theAnalysisMetaModelPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(IAnalysisMetaModelPackage.eNS_URI, theAnalysisMetaModelPackage);
		return theAnalysisMetaModelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getProject() {
		return this.projectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getProject_Configurables() {
		return (EReference) this.projectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getPlugin() {
		return this.pluginEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getPlugin_OutputPorts() {
		return (EReference) this.pluginEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getPlugin_Classname() {
		return (EAttribute) this.pluginEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getConnector() {
		return this.connectorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getConnector_DstInputPort() {
		return (EReference) this.connectorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getConnector_SicOutputPort() {
		return (EReference) this.connectorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getConfigurable() {
		return this.configurableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getConfigurable_Name() {
		return (EAttribute) this.configurableEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getConfigurable_Properties() {
		return (EReference) this.configurableEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getPort() {
		return this.portEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getPort_Name() {
		return (EAttribute) this.portEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getPort_EventTypes() {
		return (EAttribute) this.portEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getInputPort() {
		return this.inputPortEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getInputPort_InConnector() {
		return (EReference) this.inputPortEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getOutputPort() {
		return this.outputPortEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getOutputPort_OutConnector() {
		return (EReference) this.outputPortEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getProperty() {
		return this.propertyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getProperty_Name() {
		return (EAttribute) this.propertyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getProperty_Value() {
		return (EAttribute) this.propertyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getAnalysisPlugin() {
		return this.analysisPluginEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EReference getAnalysisPlugin_InputPorts() {
		return (EReference) this.analysisPluginEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EClass getReader() {
		return this.readerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EAttribute getReader_InitString() {
		return (EAttribute) this.readerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public IAnalysisMetaModelFactory getAnalysisMetaModelFactory() {
		return (IAnalysisMetaModelFactory) this.getEFactoryInstance();
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
		this.projectEClass = this.createEClass(IAnalysisMetaModelPackage.PROJECT);
		this.createEReference(this.projectEClass, IAnalysisMetaModelPackage.PROJECT__CONFIGURABLES);

		this.pluginEClass = this.createEClass(IAnalysisMetaModelPackage.PLUGIN);
		this.createEReference(this.pluginEClass, IAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS);
		this.createEAttribute(this.pluginEClass, IAnalysisMetaModelPackage.PLUGIN__CLASSNAME);

		this.connectorEClass = this.createEClass(IAnalysisMetaModelPackage.CONNECTOR);
		this.createEReference(this.connectorEClass, IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT);
		this.createEReference(this.connectorEClass, IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT);

		this.configurableEClass = this.createEClass(IAnalysisMetaModelPackage.CONFIGURABLE);
		this.createEAttribute(this.configurableEClass, IAnalysisMetaModelPackage.CONFIGURABLE__NAME);
		this.createEReference(this.configurableEClass, IAnalysisMetaModelPackage.CONFIGURABLE__PROPERTIES);

		this.portEClass = this.createEClass(IAnalysisMetaModelPackage.PORT);
		this.createEAttribute(this.portEClass, IAnalysisMetaModelPackage.PORT__NAME);
		this.createEAttribute(this.portEClass, IAnalysisMetaModelPackage.PORT__EVENT_TYPES);

		this.inputPortEClass = this.createEClass(IAnalysisMetaModelPackage.INPUT_PORT);
		this.createEReference(this.inputPortEClass, IAnalysisMetaModelPackage.INPUT_PORT__IN_CONNECTOR);

		this.outputPortEClass = this.createEClass(IAnalysisMetaModelPackage.OUTPUT_PORT);
		this.createEReference(this.outputPortEClass, IAnalysisMetaModelPackage.OUTPUT_PORT__OUT_CONNECTOR);

		this.propertyEClass = this.createEClass(IAnalysisMetaModelPackage.PROPERTY);
		this.createEAttribute(this.propertyEClass, IAnalysisMetaModelPackage.PROPERTY__NAME);
		this.createEAttribute(this.propertyEClass, IAnalysisMetaModelPackage.PROPERTY__VALUE);

		this.analysisPluginEClass = this.createEClass(IAnalysisMetaModelPackage.ANALYSIS_PLUGIN);
		this.createEReference(this.analysisPluginEClass, IAnalysisMetaModelPackage.ANALYSIS_PLUGIN__INPUT_PORTS);

		this.readerEClass = this.createEClass(IAnalysisMetaModelPackage.READER);
		this.createEAttribute(this.readerEClass, IAnalysisMetaModelPackage.READER__INIT_STRING);
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
		this.setName(IAnalysisMetaModelPackage.eNAME);
		this.setNsPrefix(IAnalysisMetaModelPackage.eNS_PREFIX);
		this.setNsURI(IAnalysisMetaModelPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		this.projectEClass.getESuperTypes().add(this.getConfigurable());
		this.pluginEClass.getESuperTypes().add(this.getConfigurable());
		this.inputPortEClass.getESuperTypes().add(this.getPort());
		this.outputPortEClass.getESuperTypes().add(this.getPort());
		this.analysisPluginEClass.getESuperTypes().add(this.getPlugin());
		this.readerEClass.getESuperTypes().add(this.getPlugin());

		// Initialize classes and features; add operations and parameters
		this.initEClass(this.projectEClass, IProject.class, "Project", !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE,
				EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getProject_Configurables(), this.getConfigurable(), null, "configurables", null, 0, -1, IProject.class, !EPackageImpl.IS_TRANSIENT,
				!EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, EPackageImpl.IS_COMPOSITE, !EPackageImpl.IS_RESOLVE_PROXIES, !EPackageImpl.IS_UNSETTABLE,
				EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

		this.initEClass(this.pluginEClass, IPlugin.class, "Plugin", EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getPlugin_OutputPorts(), this.getOutputPort(), null, "outputPorts", null, 0, -1, IPlugin.class, !EPackageImpl.IS_TRANSIENT,
				!EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, EPackageImpl.IS_COMPOSITE, !EPackageImpl.IS_RESOLVE_PROXIES, !EPackageImpl.IS_UNSETTABLE,
				EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);
		this.initEAttribute(this.getPlugin_Classname(), this.ecorePackage.getEJavaClass(), "classname", null, 1, 1, IPlugin.class, !EPackageImpl.IS_TRANSIENT,
				!EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE,
				!EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

		this.initEClass(this.connectorEClass, IConnector.class, "Connector", !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE,
				EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getConnector_DstInputPort(), this.getInputPort(), this.getInputPort_InConnector(), "dstInputPort", null, 1, 1, IConnector.class,
				!EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_COMPOSITE, EPackageImpl.IS_RESOLVE_PROXIES,
				!EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);
		this.initEReference(this.getConnector_SicOutputPort(), this.getOutputPort(), this.getOutputPort_OutConnector(), "sicOutputPort", null, 1, 1,
				IConnector.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_COMPOSITE,
				!EPackageImpl.IS_RESOLVE_PROXIES, !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

		this.initEClass(this.configurableEClass, IConfigurable.class, "Configurable", EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE,
				EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getConfigurable_Name(), this.ecorePackage.getEString(), "name", null, 1, 1, IConfigurable.class, !EPackageImpl.IS_TRANSIENT,
				!EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE,
				!EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);
		this.initEReference(this.getConfigurable_Properties(), this.getProperty(), null, "properties", null, 0, -1, IConfigurable.class, !EPackageImpl.IS_TRANSIENT,
				!EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, EPackageImpl.IS_COMPOSITE, !EPackageImpl.IS_RESOLVE_PROXIES, !EPackageImpl.IS_UNSETTABLE,
				EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

		this.initEClass(this.portEClass, IPort.class, "Port", EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getPort_Name(), this.ecorePackage.getEString(), "name", null, 1, 1, IPort.class, !EPackageImpl.IS_TRANSIENT,
				!EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE,
				!EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);
		this.initEAttribute(this.getPort_EventTypes(), this.ecorePackage.getEJavaClass(), "eventTypes", null, 1, -1, IPort.class, !EPackageImpl.IS_TRANSIENT,
				!EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE,
				!EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

		this.initEClass(this.inputPortEClass, IInputPort.class, "InputPort", !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE,
				EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getInputPort_InConnector(), this.getConnector(), this.getConnector_DstInputPort(), "inConnector", null, 0, -1, IInputPort.class,
				!EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_COMPOSITE, EPackageImpl.IS_RESOLVE_PROXIES,
				!EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

		this.initEClass(this.outputPortEClass, IOutputPort.class, "OutputPort", !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE,
				EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getOutputPort_OutConnector(), this.getConnector(), this.getConnector_SicOutputPort(), "outConnector", null, 0, -1,
				IOutputPort.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, EPackageImpl.IS_COMPOSITE,
				!EPackageImpl.IS_RESOLVE_PROXIES, !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

		this.initEClass(this.propertyEClass, IProperty.class, "Property", !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE,
				EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getProperty_Name(), this.ecorePackage.getEString(), "name", null, 1, 1, IProperty.class, !EPackageImpl.IS_TRANSIENT,
				!EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE,
				!EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);
		this.initEAttribute(this.getProperty_Value(), this.ecorePackage.getEString(), "value", null, 1, 1, IProperty.class, !EPackageImpl.IS_TRANSIENT,
				!EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE,
				!EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

		this.initEClass(this.analysisPluginEClass, IAnalysisPlugin.class, "AnalysisPlugin", !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE,
				EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getAnalysisPlugin_InputPorts(), this.getInputPort(), null, "inputPorts", null, 0, -1, IAnalysisPlugin.class,
				!EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, EPackageImpl.IS_COMPOSITE, !EPackageImpl.IS_RESOLVE_PROXIES,
				!EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

		this.initEClass(this.readerEClass, IReader.class, "Reader", !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getReader_InitString(), this.ecorePackage.getEString(), "initString", null, 0, 1, IReader.class, !EPackageImpl.IS_TRANSIENT,
				!EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE,
				!EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

		// Create resource
		this.createResource(IAnalysisMetaModelPackage.eNS_URI);
	}

} // AnalysisMetaModelPackage
