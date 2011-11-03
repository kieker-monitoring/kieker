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
 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelFactory
 * @model kind="package"
 * @generated
 */
public interface IAnalysisMetaModelPackage extends EPackage {
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
	IAnalysisMetaModelPackage eINSTANCE = kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage.init();

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.Configurable <em>Configurable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.Configurable
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getConfigurable()
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
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.Project <em>Project</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.Project
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getProject()
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
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.Plugin <em>Plugin</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.Plugin
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getPlugin()
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
	 * The feature id for the '<em><b>Output Ports</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLUGIN__OUTPUT_PORTS = CONFIGURABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLUGIN__CLASSNAME = CONFIGURABLE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Plugin</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLUGIN_FEATURE_COUNT = CONFIGURABLE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.Connector <em>Connector</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.Connector
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getConnector()
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
	 * The feature id for the '<em><b>Sic Output Port</b></em>' container reference.
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
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.Port <em>Port</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.Port
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getPort()
	 * @generated
	 */
	int PORT = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Event Types</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT__EVENT_TYPES = 1;

	/**
	 * The number of structural features of the '<em>Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PORT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.InputPort <em>Input Port</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.InputPort
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getInputPort()
	 * @generated
	 */
	int INPUT_PORT = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_PORT__NAME = PORT__NAME;

	/**
	 * The feature id for the '<em><b>Event Types</b></em>' attribute list.
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
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.OutputPort <em>Output Port</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.OutputPort
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getOutputPort()
	 * @generated
	 */
	int OUTPUT_PORT = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT__NAME = PORT__NAME;

	/**
	 * The feature id for the '<em><b>Event Types</b></em>' attribute list.
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
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.Property <em>Property</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.Property
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getProperty()
	 * @generated
	 */
	int PROPERTY = 7;

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
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.AnalysisPlugin <em>Analysis Plugin</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisPlugin
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getAnalysisPlugin()
	 * @generated
	 */
	int ANALYSIS_PLUGIN = 8;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_PLUGIN__NAME = PLUGIN__NAME;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_PLUGIN__PROPERTIES = PLUGIN__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Output Ports</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_PLUGIN__OUTPUT_PORTS = PLUGIN__OUTPUT_PORTS;

	/**
	 * The feature id for the '<em><b>Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_PLUGIN__CLASSNAME = PLUGIN__CLASSNAME;

	/**
	 * The feature id for the '<em><b>Input Ports</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_PLUGIN__INPUT_PORTS = PLUGIN_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Analysis Plugin</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_PLUGIN_FEATURE_COUNT = PLUGIN_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.Reader <em>Reader</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.Reader
	 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getReader()
	 * @generated
	 */
	int READER = 9;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__NAME = PLUGIN__NAME;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__PROPERTIES = PLUGIN__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Output Ports</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__OUTPUT_PORTS = PLUGIN__OUTPUT_PORTS;

	/**
	 * The feature id for the '<em><b>Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__CLASSNAME = PLUGIN__CLASSNAME;

	/**
	 * The feature id for the '<em><b>Init String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__INIT_STRING = PLUGIN_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Reader</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER_FEATURE_COUNT = PLUGIN_FEATURE_COUNT + 1;


	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.IProject <em>Project</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Project</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IProject
	 * @generated
	 */
	EClass getProject();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.IProject#getConfigurables <em>Configurables</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Configurables</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IProject#getConfigurables()
	 * @see #getProject()
	 * @generated
	 */
	EReference getProject_Configurables();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.IPlugin <em>Plugin</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Plugin</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IPlugin
	 * @generated
	 */
	EClass getPlugin();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.IPlugin#getOutputPorts <em>Output Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Output Ports</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IPlugin#getOutputPorts()
	 * @see #getPlugin()
	 * @generated
	 */
	EReference getPlugin_OutputPorts();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.IPlugin#getClassname <em>Classname</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Classname</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IPlugin#getClassname()
	 * @see #getPlugin()
	 * @generated
	 */
	EAttribute getPlugin_Classname();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.IConnector <em>Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Connector</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IConnector
	 * @generated
	 */
	EClass getConnector();

	/**
	 * Returns the meta object for the reference '{@link kieker.analysis.model.analysisMetaModel.IConnector#getDstInputPort <em>Dst Input Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Dst Input Port</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IConnector#getDstInputPort()
	 * @see #getConnector()
	 * @generated
	 */
	EReference getConnector_DstInputPort();

	/**
	 * Returns the meta object for the container reference '{@link kieker.analysis.model.analysisMetaModel.IConnector#getSicOutputPort <em>Sic Output Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Sic Output Port</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IConnector#getSicOutputPort()
	 * @see #getConnector()
	 * @generated
	 */
	EReference getConnector_SicOutputPort();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.IConfigurable <em>Configurable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Configurable</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IConfigurable
	 * @generated
	 */
	EClass getConfigurable();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.IConfigurable#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IConfigurable#getName()
	 * @see #getConfigurable()
	 * @generated
	 */
	EAttribute getConfigurable_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.IConfigurable#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IConfigurable#getProperties()
	 * @see #getConfigurable()
	 * @generated
	 */
	EReference getConfigurable_Properties();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.IPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Port</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IPort
	 * @generated
	 */
	EClass getPort();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.IPort#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IPort#getName()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_Name();

	/**
	 * Returns the meta object for the attribute list '{@link kieker.analysis.model.analysisMetaModel.IPort#getEventTypes <em>Event Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Event Types</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IPort#getEventTypes()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_EventTypes();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.IInputPort <em>Input Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input Port</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IInputPort
	 * @generated
	 */
	EClass getInputPort();

	/**
	 * Returns the meta object for the reference list '{@link kieker.analysis.model.analysisMetaModel.IInputPort#getInConnector <em>In Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>In Connector</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IInputPort#getInConnector()
	 * @see #getInputPort()
	 * @generated
	 */
	EReference getInputPort_InConnector();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.IOutputPort <em>Output Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Output Port</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IOutputPort
	 * @generated
	 */
	EClass getOutputPort();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.IOutputPort#getOutConnector <em>Out Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Out Connector</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IOutputPort#getOutConnector()
	 * @see #getOutputPort()
	 * @generated
	 */
	EReference getOutputPort_OutConnector();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.IProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IProperty
	 * @generated
	 */
	EClass getProperty();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.IProperty#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IProperty#getName()
	 * @see #getProperty()
	 * @generated
	 */
	EAttribute getProperty_Name();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.IProperty#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IProperty#getValue()
	 * @see #getProperty()
	 * @generated
	 */
	EAttribute getProperty_Value();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.IAnalysisPlugin <em>Analysis Plugin</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Analysis Plugin</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IAnalysisPlugin
	 * @generated
	 */
	EClass getAnalysisPlugin();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.IAnalysisPlugin#getInputPorts <em>Input Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Input Ports</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IAnalysisPlugin#getInputPorts()
	 * @see #getAnalysisPlugin()
	 * @generated
	 */
	EReference getAnalysisPlugin_InputPorts();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.IReader <em>Reader</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reader</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IReader
	 * @generated
	 */
	EClass getReader();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.IReader#getInitString <em>Init String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Init String</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.IReader#getInitString()
	 * @see #getReader()
	 * @generated
	 */
	EAttribute getReader_InitString();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	IAnalysisMetaModelFactory getAnalysisMetaModelFactory();

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
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.Project <em>Project</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.Project
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getProject()
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
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.Plugin <em>Plugin</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.Plugin
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getPlugin()
		 * @generated
		 */
		EClass PLUGIN = eINSTANCE.getPlugin();

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
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.Connector <em>Connector</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.Connector
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getConnector()
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
		 * The meta object literal for the '<em><b>Sic Output Port</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONNECTOR__SIC_OUTPUT_PORT = eINSTANCE.getConnector_SicOutputPort();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.Configurable <em>Configurable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.Configurable
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getConfigurable()
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
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.Port <em>Port</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.Port
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getPort()
		 * @generated
		 */
		EClass PORT = eINSTANCE.getPort();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__NAME = eINSTANCE.getPort_Name();

		/**
		 * The meta object literal for the '<em><b>Event Types</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PORT__EVENT_TYPES = eINSTANCE.getPort_EventTypes();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.InputPort <em>Input Port</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.InputPort
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getInputPort()
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
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.OutputPort <em>Output Port</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.OutputPort
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getOutputPort()
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
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.Property <em>Property</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.Property
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getProperty()
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

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.AnalysisPlugin <em>Analysis Plugin</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisPlugin
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getAnalysisPlugin()
		 * @generated
		 */
		EClass ANALYSIS_PLUGIN = eINSTANCE.getAnalysisPlugin();

		/**
		 * The meta object literal for the '<em><b>Input Ports</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANALYSIS_PLUGIN__INPUT_PORTS = eINSTANCE.getAnalysisPlugin_InputPorts();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.Reader <em>Reader</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.Reader
		 * @see kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage#getReader()
		 * @generated
		 */
		EClass READER = eINSTANCE.getReader();

		/**
		 * The meta object literal for the '<em><b>Init String</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute READER__INIT_STRING = eINSTANCE.getReader_InitString();

	}

} //IAnalysisMetaModelPackage
