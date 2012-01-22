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
 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelFactory
 * @model kind="package"
 * @generated
 */
public interface MIAnalysisMetaModelPackage extends EPackage {
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
	MIAnalysisMetaModelPackage eINSTANCE = kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage.init();

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.MProject <em>Project</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.MProject
	 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getProject()
	 * @generated
	 */
	int PROJECT = 0;

	/**
	 * The feature id for the '<em><b>Plugins</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT__PLUGINS = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT__NAME = 1;

	/**
	 * The feature id for the '<em><b>Repositories</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT__REPOSITORIES = 2;

	/**
	 * The feature id for the '<em><b>Dependencies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT__DEPENDENCIES = 3;

	/**
	 * The number of structural features of the '<em>Project</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROJECT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.MPlugin <em>Plugin</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.MPlugin
	 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getPlugin()
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
	int PLUGIN__NAME = 0;

	/**
	 * The feature id for the '<em><b>Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLUGIN__CLASSNAME = 1;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLUGIN__PROPERTIES = 2;

	/**
	 * The feature id for the '<em><b>Repositories</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLUGIN__REPOSITORIES = 3;

	/**
	 * The feature id for the '<em><b>Output Ports</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLUGIN__OUTPUT_PORTS = 4;

	/**
	 * The number of structural features of the '<em>Plugin</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLUGIN_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.MPort <em>Port</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.MPort
	 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getPort()
	 * @generated
	 */
	int PORT = 2;

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
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.MInputPort <em>Input Port</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.MInputPort
	 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getInputPort()
	 * @generated
	 */
	int INPUT_PORT = 3;

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
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_PORT__PARENT = PORT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Input Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_PORT_FEATURE_COUNT = PORT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.MOutputPort <em>Output Port</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.MOutputPort
	 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getOutputPort()
	 * @generated
	 */
	int OUTPUT_PORT = 4;

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
	 * The feature id for the '<em><b>Subscribers</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT__SUBSCRIBERS = PORT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT__PARENT = PORT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Output Port</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_PORT_FEATURE_COUNT = PORT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.MProperty <em>Property</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.MProperty
	 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getProperty()
	 * @generated
	 */
	int PROPERTY = 5;

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
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.MAnalysisPlugin <em>Analysis Plugin</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisPlugin
	 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getAnalysisPlugin()
	 * @generated
	 */
	int ANALYSIS_PLUGIN = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_PLUGIN__NAME = PLUGIN__NAME;

	/**
	 * The feature id for the '<em><b>Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_PLUGIN__CLASSNAME = PLUGIN__CLASSNAME;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_PLUGIN__PROPERTIES = PLUGIN__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Repositories</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_PLUGIN__REPOSITORIES = PLUGIN__REPOSITORIES;

	/**
	 * The feature id for the '<em><b>Output Ports</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANALYSIS_PLUGIN__OUTPUT_PORTS = PLUGIN__OUTPUT_PORTS;

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
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.MReader <em>Reader</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.MReader
	 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getReader()
	 * @generated
	 */
	int READER = 7;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__NAME = PLUGIN__NAME;

	/**
	 * The feature id for the '<em><b>Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__CLASSNAME = PLUGIN__CLASSNAME;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__PROPERTIES = PLUGIN__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Repositories</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__REPOSITORIES = PLUGIN__REPOSITORIES;

	/**
	 * The feature id for the '<em><b>Output Ports</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__OUTPUT_PORTS = PLUGIN__OUTPUT_PORTS;

	/**
	 * The number of structural features of the '<em>Reader</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER_FEATURE_COUNT = PLUGIN_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.MRepository <em>Repository</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.MRepository
	 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getRepository()
	 * @generated
	 */
	int REPOSITORY = 8;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPOSITORY__PROPERTIES = 0;

	/**
	 * The feature id for the '<em><b>Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPOSITORY__CLASSNAME = 1;

	/**
	 * The number of structural features of the '<em>Repository</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPOSITORY_FEATURE_COUNT = 2;


	/**
	 * The meta object id for the '{@link kieker.analysis.model.analysisMetaModel.impl.MDependency <em>Dependency</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysis.model.analysisMetaModel.impl.MDependency
	 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getDependency()
	 * @generated
	 */
	int DEPENDENCY = 9;

	/**
	 * The feature id for the '<em><b>File Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__FILE_PATH = 0;

	/**
	 * The number of structural features of the '<em>Dependency</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY_FEATURE_COUNT = 1;


	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.MIProject <em>Project</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Project</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIProject
	 * @generated
	 */
	EClass getProject();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.MIProject#getPlugins <em>Plugins</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Plugins</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIProject#getPlugins()
	 * @see #getProject()
	 * @generated
	 */
	EReference getProject_Plugins();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.MIProject#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIProject#getName()
	 * @see #getProject()
	 * @generated
	 */
	EAttribute getProject_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.MIProject#getRepositories <em>Repositories</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Repositories</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIProject#getRepositories()
	 * @see #getProject()
	 * @generated
	 */
	EReference getProject_Repositories();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.MIProject#getDependencies <em>Dependencies</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Dependencies</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIProject#getDependencies()
	 * @see #getProject()
	 * @generated
	 */
	EReference getProject_Dependencies();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.MIPlugin <em>Plugin</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Plugin</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIPlugin
	 * @generated
	 */
	EClass getPlugin();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.MIPlugin#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIPlugin#getName()
	 * @see #getPlugin()
	 * @generated
	 */
	EAttribute getPlugin_Name();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.MIPlugin#getClassname <em>Classname</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Classname</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIPlugin#getClassname()
	 * @see #getPlugin()
	 * @generated
	 */
	EAttribute getPlugin_Classname();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.MIPlugin#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIPlugin#getProperties()
	 * @see #getPlugin()
	 * @generated
	 */
	EReference getPlugin_Properties();

	/**
	 * Returns the meta object for the reference list '{@link kieker.analysis.model.analysisMetaModel.MIPlugin#getRepositories <em>Repositories</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Repositories</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIPlugin#getRepositories()
	 * @see #getPlugin()
	 * @generated
	 */
	EReference getPlugin_Repositories();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.MIPlugin#getOutputPorts <em>Output Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Output Ports</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIPlugin#getOutputPorts()
	 * @see #getPlugin()
	 * @generated
	 */
	EReference getPlugin_OutputPorts();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.MIPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Port</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIPort
	 * @generated
	 */
	EClass getPort();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.MIPort#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIPort#getName()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_Name();

	/**
	 * Returns the meta object for the attribute list '{@link kieker.analysis.model.analysisMetaModel.MIPort#getEventTypes <em>Event Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Event Types</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIPort#getEventTypes()
	 * @see #getPort()
	 * @generated
	 */
	EAttribute getPort_EventTypes();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.MIInputPort <em>Input Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input Port</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIInputPort
	 * @generated
	 */
	EClass getInputPort();

	/**
	 * Returns the meta object for the container reference '{@link kieker.analysis.model.analysisMetaModel.MIInputPort#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Parent</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIInputPort#getParent()
	 * @see #getInputPort()
	 * @generated
	 */
	EReference getInputPort_Parent();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.MIOutputPort <em>Output Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Output Port</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIOutputPort
	 * @generated
	 */
	EClass getOutputPort();

	/**
	 * Returns the meta object for the reference list '{@link kieker.analysis.model.analysisMetaModel.MIOutputPort#getSubscribers <em>Subscribers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Subscribers</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIOutputPort#getSubscribers()
	 * @see #getOutputPort()
	 * @generated
	 */
	EReference getOutputPort_Subscribers();

	/**
	 * Returns the meta object for the container reference '{@link kieker.analysis.model.analysisMetaModel.MIOutputPort#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Parent</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIOutputPort#getParent()
	 * @see #getOutputPort()
	 * @generated
	 */
	EReference getOutputPort_Parent();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.MIProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIProperty
	 * @generated
	 */
	EClass getProperty();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.MIProperty#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIProperty#getName()
	 * @see #getProperty()
	 * @generated
	 */
	EAttribute getProperty_Name();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.MIProperty#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIProperty#getValue()
	 * @see #getProperty()
	 * @generated
	 */
	EAttribute getProperty_Value();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.MIAnalysisPlugin <em>Analysis Plugin</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Analysis Plugin</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisPlugin
	 * @generated
	 */
	EClass getAnalysisPlugin();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.MIAnalysisPlugin#getInputPorts <em>Input Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Input Ports</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisPlugin#getInputPorts()
	 * @see #getAnalysisPlugin()
	 * @generated
	 */
	EReference getAnalysisPlugin_InputPorts();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.MIReader <em>Reader</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reader</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIReader
	 * @generated
	 */
	EClass getReader();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.MIRepository <em>Repository</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Repository</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIRepository
	 * @generated
	 */
	EClass getRepository();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysis.model.analysisMetaModel.MIRepository#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIRepository#getProperties()
	 * @see #getRepository()
	 * @generated
	 */
	EReference getRepository_Properties();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.MIRepository#getClassname <em>Classname</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Classname</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIRepository#getClassname()
	 * @see #getRepository()
	 * @generated
	 */
	EAttribute getRepository_Classname();

	/**
	 * Returns the meta object for class '{@link kieker.analysis.model.analysisMetaModel.MIDependency <em>Dependency</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dependency</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIDependency
	 * @generated
	 */
	EClass getDependency();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysis.model.analysisMetaModel.MIDependency#getFilePath <em>File Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>File Path</em>'.
	 * @see kieker.analysis.model.analysisMetaModel.MIDependency#getFilePath()
	 * @see #getDependency()
	 * @generated
	 */
	EAttribute getDependency_FilePath();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MIAnalysisMetaModelFactory getAnalysisMetaModelFactory();

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
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.MProject <em>Project</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.MProject
		 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getProject()
		 * @generated
		 */
		EClass PROJECT = eINSTANCE.getProject();

		/**
		 * The meta object literal for the '<em><b>Plugins</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROJECT__PLUGINS = eINSTANCE.getProject_Plugins();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROJECT__NAME = eINSTANCE.getProject_Name();

		/**
		 * The meta object literal for the '<em><b>Repositories</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROJECT__REPOSITORIES = eINSTANCE.getProject_Repositories();

		/**
		 * The meta object literal for the '<em><b>Dependencies</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROJECT__DEPENDENCIES = eINSTANCE.getProject_Dependencies();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.MPlugin <em>Plugin</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.MPlugin
		 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getPlugin()
		 * @generated
		 */
		EClass PLUGIN = eINSTANCE.getPlugin();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLUGIN__NAME = eINSTANCE.getPlugin_Name();

		/**
		 * The meta object literal for the '<em><b>Classname</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLUGIN__CLASSNAME = eINSTANCE.getPlugin_Classname();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLUGIN__PROPERTIES = eINSTANCE.getPlugin_Properties();

		/**
		 * The meta object literal for the '<em><b>Repositories</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLUGIN__REPOSITORIES = eINSTANCE.getPlugin_Repositories();

		/**
		 * The meta object literal for the '<em><b>Output Ports</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLUGIN__OUTPUT_PORTS = eINSTANCE.getPlugin_OutputPorts();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.MPort <em>Port</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.MPort
		 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getPort()
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
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.MInputPort <em>Input Port</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.MInputPort
		 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getInputPort()
		 * @generated
		 */
		EClass INPUT_PORT = eINSTANCE.getInputPort();

		/**
		 * The meta object literal for the '<em><b>Parent</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT_PORT__PARENT = eINSTANCE.getInputPort_Parent();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.MOutputPort <em>Output Port</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.MOutputPort
		 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getOutputPort()
		 * @generated
		 */
		EClass OUTPUT_PORT = eINSTANCE.getOutputPort();

		/**
		 * The meta object literal for the '<em><b>Subscribers</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OUTPUT_PORT__SUBSCRIBERS = eINSTANCE.getOutputPort_Subscribers();

		/**
		 * The meta object literal for the '<em><b>Parent</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OUTPUT_PORT__PARENT = eINSTANCE.getOutputPort_Parent();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.MProperty <em>Property</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.MProperty
		 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getProperty()
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
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.MAnalysisPlugin <em>Analysis Plugin</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisPlugin
		 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getAnalysisPlugin()
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
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.MReader <em>Reader</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.MReader
		 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getReader()
		 * @generated
		 */
		EClass READER = eINSTANCE.getReader();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.MRepository <em>Repository</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.MRepository
		 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getRepository()
		 * @generated
		 */
		EClass REPOSITORY = eINSTANCE.getRepository();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REPOSITORY__PROPERTIES = eINSTANCE.getRepository_Properties();

		/**
		 * The meta object literal for the '<em><b>Classname</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REPOSITORY__CLASSNAME = eINSTANCE.getRepository_Classname();

		/**
		 * The meta object literal for the '{@link kieker.analysis.model.analysisMetaModel.impl.MDependency <em>Dependency</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysis.model.analysisMetaModel.impl.MDependency
		 * @see kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage#getDependency()
		 * @generated
		 */
		EClass DEPENDENCY = eINSTANCE.getDependency();

		/**
		 * The meta object literal for the '<em><b>File Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DEPENDENCY__FILE_PATH = eINSTANCE.getDependency_FilePath();

	}

} //MIAnalysisMetaModelPackage
