/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see kieker.analysis.model.analysisMetaModel.AnalysisMetaModelFactory
 * @model kind="package"
 * @generated
 */
public interface AnalysisMetaModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "analysisMetaModel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/Kieker/model/AnalysisMetaModel.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AnalysisMetaModelPackage eINSTANCE = kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.ConfigurableImpl <em>Configurable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.ConfigurableImpl
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getConfigurable()
	 * @generated
	 */
	int CONFIGURABLE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURABLE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURABLE__PROPERTIES = 1;

	/**
	 * The number of structural features of the '<em>Configurable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURABLE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.ProjectImpl <em>Project</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.ProjectImpl
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getProject()
	 * @generated
	 */
	int PROJECT = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT__NAME = CONFIGURABLE__NAME;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT__PROPERTIES = CONFIGURABLE__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Configurables</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT__CONFIGURABLES = CONFIGURABLE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Project</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT_FEATURE_COUNT = CONFIGURABLE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.PluginImpl <em>Plugin</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.PluginImpl
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getPlugin()
	 * @generated
	 */
	int PLUGIN = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLUGIN__NAME = CONFIGURABLE__NAME;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLUGIN__PROPERTIES = CONFIGURABLE__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Input Ports</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLUGIN__INPUT_PORTS = CONFIGURABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Output Ports</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLUGIN__OUTPUT_PORTS = CONFIGURABLE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLUGIN__CLASSNAME = CONFIGURABLE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Plugin</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLUGIN_FEATURE_COUNT = CONFIGURABLE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.ConnectorImpl <em>Connector</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.ConnectorImpl
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getConnector()
	 * @generated
	 */
	int CONNECTOR = 2;

	/**
	 * The feature id for the '<em><b>Dst Input Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTOR__DST_INPUT_PORT = 0;

	/**
	 * The feature id for the '<em><b>Sic Output Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTOR__SIC_OUTPUT_PORT = 1;

	/**
	 * The number of structural features of the '<em>Connector</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTOR_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.PortImpl <em>Port</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.PortImpl
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getPort()
	 * @generated
	 */
	int PORT = 4;

	/**
	 * The feature id for the '<em><b>Event Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__EVENT_TYPES = 0;

	/**
	 * The number of structural features of the '<em>Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.InputPortImpl <em>Input Port</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.InputPortImpl
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getInputPort()
	 * @generated
	 */
	int INPUT_PORT = 5;

	/**
	 * The feature id for the '<em><b>Event Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_PORT__EVENT_TYPES = PORT__EVENT_TYPES;

	/**
	 * The feature id for the '<em><b>In Connector</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_PORT__IN_CONNECTOR = PORT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Input Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_PORT_FEATURE_COUNT = PORT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.OutputPortImpl <em>Output Port</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.OutputPortImpl
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getOutputPort()
	 * @generated
	 */
	int OUTPUT_PORT = 6;

	/**
	 * The feature id for the '<em><b>Event Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT__EVENT_TYPES = PORT__EVENT_TYPES;

	/**
	 * The feature id for the '<em><b>Out Connector</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT__OUT_CONNECTOR = PORT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Output Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT_FEATURE_COUNT = PORT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.ClassImpl <em>Class</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.ClassImpl
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getClass_()
	 * @generated
	 */
	int CLASS = 7;

	/**
	 * The feature id for the '<em><b>Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS__CLASSNAME = 0;

	/**
	 * The number of structural features of the '<em>Class</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.PropertyImpl <em>Property</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.PropertyImpl
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getProperty()
	 * @generated
	 */
	int PROPERTY = 8;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Property</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_FEATURE_COUNT = 2;


	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.Project <em>Project</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Project</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Project
	 * @generated
	 */
	EClass getProject();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.Project#getConfigurables <em>Configurables</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Configurables</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Project#getConfigurables()
	 * @see #getProject()
	 * @generated
	 */
	EReference getProject_Configurables();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.Plugin <em>Plugin</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Plugin</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Plugin
	 * @generated
	 */
	EClass getPlugin();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.Plugin#getInputPorts <em>Input Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Input Ports</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Plugin#getInputPorts()
	 * @see #getPlugin()
	 * @generated
	 */
	EReference getPlugin_InputPorts();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.Plugin#getOutputPorts <em>Output Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Output Ports</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Plugin#getOutputPorts()
	 * @see #getPlugin()
	 * @generated
	 */
	EReference getPlugin_OutputPorts();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.Plugin#getClassname <em>Classname</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Classname</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Plugin#getClassname()
	 * @see #getPlugin()
	 * @generated
	 */
	EAttribute getPlugin_Classname();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.Connector <em>Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Connector</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Connector
	 * @generated
	 */
	EClass getConnector();

	/**
	 * Returns the meta object for the reference '{@link kieker.analysis.model.analysisMetaModel.Connector#getDstInputPort <em>Dst Input Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Dst Input Port</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Connector#getDstInputPort()
	 * @see #getConnector()
	 * @generated
	 */
	EReference getConnector_DstInputPort();

	/**
	 * Returns the meta object for the reference '{@link kieker.analysis.model.analysisMetaModel.Connector#getSicOutputPort <em>Sic Output Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Sic Output Port</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Connector#getSicOutputPort()
	 * @see #getConnector()
	 * @generated
	 */
	EReference getConnector_SicOutputPort();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.Configurable <em>Configurable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Configurable</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Configurable
	 * @generated
	 */
	EClass getConfigurable();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.Configurable#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Configurable#getName()
	 * @see #getConfigurable()
	 * @generated
	 */
	EAttribute getConfigurable_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.Configurable#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Configurable#getProperties()
	 * @see #getConfigurable()
	 * @generated
	 */
	EReference getConfigurable_Properties();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.Port <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Port</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Port
	 * @generated
	 */
	EClass getPort();

	/**
	 * Returns the meta object for the reference list '{@link kieker.analysis.model.analysisMetaModel.Port#getEventTypes <em>Event Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Event Types</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Port#getEventTypes()
	 * @see #getPort()
	 * @generated
	 */
	EReference getPort_EventTypes();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.InputPort <em>Input Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input Port</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.InputPort
	 * @generated
	 */
	EClass getInputPort();

	/**
	 * Returns the meta object for the reference list '{@link kieker.analysis.model.analysisMetaModel.InputPort#getInConnector <em>In Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>In Connector</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.InputPort#getInConnector()
	 * @see #getInputPort()
	 * @generated
	 */
	EReference getInputPort_InConnector();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.OutputPort <em>Output Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Output Port</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.OutputPort
	 * @generated
	 */
	EClass getOutputPort();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.OutputPort#getOutConnector <em>Out Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Out Connector</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.OutputPort#getOutConnector()
	 * @see #getOutputPort()
	 * @generated
	 */
	EReference getOutputPort_OutConnector();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.Class <em>Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Class</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Class
	 * @generated
	 */
	EClass getClass_();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.Class#getClassname <em>Classname</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Classname</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Class#getClassname()
	 * @see #getClass_()
	 * @generated
	 */
	EAttribute getClass_Classname();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.Property <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Property
	 * @generated
	 */
	EClass getProperty();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.Property#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Property#getName()
	 * @see #getProperty()
	 * @generated
	 */
	EAttribute getProperty_Name();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.Property#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.Property#getValue()
	 * @see #getProperty()
	 * @generated
	 */
	EAttribute getProperty_Value();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AnalysisMetaModelFactory getAnalysisMetaModelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.ProjectImpl <em>Project</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.ProjectImpl
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getProject()
		 * @generated
		 */
		EClass PROJECT = eINSTANCE.getProject();

		/**
		 * The meta object literal for the '<em><b>Configurables</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROJECT__CONFIGURABLES = eINSTANCE.getProject_Configurables();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.PluginImpl <em>Plugin</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.PluginImpl
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getPlugin()
		 * @generated
		 */
		EClass PLUGIN = eINSTANCE.getPlugin();

		/**
		 * The meta object literal for the '<em><b>Input Ports</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLUGIN__INPUT_PORTS = eINSTANCE.getPlugin_InputPorts();

		/**
		 * The meta object literal for the '<em><b>Output Ports</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLUGIN__OUTPUT_PORTS = eINSTANCE.getPlugin_OutputPorts();

		/**
		 * The meta object literal for the '<em><b>Classname</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLUGIN__CLASSNAME = eINSTANCE.getPlugin_Classname();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.ConnectorImpl <em>Connector</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.ConnectorImpl
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getConnector()
		 * @generated
		 */
		EClass CONNECTOR = eINSTANCE.getConnector();

		/**
		 * The meta object literal for the '<em><b>Dst Input Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONNECTOR__DST_INPUT_PORT = eINSTANCE.getConnector_DstInputPort();

		/**
		 * The meta object literal for the '<em><b>Sic Output Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONNECTOR__SIC_OUTPUT_PORT = eINSTANCE.getConnector_SicOutputPort();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.ConfigurableImpl <em>Configurable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.ConfigurableImpl
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getConfigurable()
		 * @generated
		 */
		EClass CONFIGURABLE = eINSTANCE.getConfigurable();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONFIGURABLE__NAME = eINSTANCE.getConfigurable_Name();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONFIGURABLE__PROPERTIES = eINSTANCE.getConfigurable_Properties();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.PortImpl <em>Port</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.PortImpl
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getPort()
		 * @generated
		 */
		EClass PORT = eINSTANCE.getPort();

		/**
		 * The meta object literal for the '<em><b>Event Types</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PORT__EVENT_TYPES = eINSTANCE.getPort_EventTypes();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.InputPortImpl <em>Input Port</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.InputPortImpl
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getInputPort()
		 * @generated
		 */
		EClass INPUT_PORT = eINSTANCE.getInputPort();

		/**
		 * The meta object literal for the '<em><b>In Connector</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT_PORT__IN_CONNECTOR = eINSTANCE.getInputPort_InConnector();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.OutputPortImpl <em>Output Port</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.OutputPortImpl
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getOutputPort()
		 * @generated
		 */
		EClass OUTPUT_PORT = eINSTANCE.getOutputPort();

		/**
		 * The meta object literal for the '<em><b>Out Connector</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OUTPUT_PORT__OUT_CONNECTOR = eINSTANCE.getOutputPort_OutConnector();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.ClassImpl <em>Class</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.ClassImpl
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getClass_()
		 * @generated
		 */
		EClass CLASS = eINSTANCE.getClass_();

		/**
		 * The meta object literal for the '<em><b>Classname</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLASS__CLASSNAME = eINSTANCE.getClass_Classname();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.PropertyImpl <em>Property</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.PropertyImpl
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackageImpl#getProperty()
		 * @generated
		 */
		EClass PROPERTY = eINSTANCE.getProperty();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPERTY__NAME = eINSTANCE.getProperty_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPERTY__VALUE = eINSTANCE.getProperty_Value();

	}

} //AnalysisMetaModelPackage
