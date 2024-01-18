/**
 */
package kieker.model.analysismodel.deployment;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 *
 * @see kieker.model.analysismodel.deployment.DeploymentFactory
 * @model kind="package"
 * @generated
 */
public interface DeploymentPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	String eNAME = "deployment";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	String eNS_URI = "platform:/resource/Kieker/model/analysismodel.ecore/deployment";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	String eNS_PREFIX = "deployment";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	DeploymentPackage eINSTANCE = kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl.init();

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.deployment.impl.DeploymentModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see kieker.model.analysismodel.deployment.impl.DeploymentModelImpl
	 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeploymentModel()
	 * @generated
	 */
	int DEPLOYMENT_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Contexts</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_MODEL__CONTEXTS = 0;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_MODEL_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.deployment.impl.EStringToDeploymentContextMapEntryImpl <em>EString To Deployment Context Map
	 * Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see kieker.model.analysismodel.deployment.impl.EStringToDeploymentContextMapEntryImpl
	 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getEStringToDeploymentContextMapEntry()
	 * @generated
	 */
	int ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY = 1;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Deployment Context Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EString To Deployment Context Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.deployment.impl.DeploymentContextImpl <em>Context</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see kieker.model.analysismodel.deployment.impl.DeploymentContextImpl
	 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeploymentContext()
	 * @generated
	 */
	int DEPLOYMENT_CONTEXT = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_CONTEXT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Components</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_CONTEXT__COMPONENTS = 1;

	/**
	 * The number of structural features of the '<em>Context</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_CONTEXT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Context</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_CONTEXT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.deployment.impl.EStringToDeployedComponentMapEntryImpl <em>EString To Deployed Component Map
	 * Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see kieker.model.analysismodel.deployment.impl.EStringToDeployedComponentMapEntryImpl
	 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getEStringToDeployedComponentMapEntry()
	 * @generated
	 */
	int ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY = 3;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Deployed Component Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EString To Deployed Component Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl <em>Deployed Component</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see kieker.model.analysismodel.deployment.impl.DeployedComponentImpl
	 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeployedComponent()
	 * @generated
	 */
	int DEPLOYED_COMPONENT = 4;

	/**
	 * The feature id for the '<em><b>Assembly Component</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT = 0;

	/**
	 * The feature id for the '<em><b>Operations</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT__OPERATIONS = 1;

	/**
	 * The feature id for the '<em><b>Storages</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT__STORAGES = 2;

	/**
	 * The feature id for the '<em><b>Contained Components</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT__CONTAINED_COMPONENTS = 3;

	/**
	 * The feature id for the '<em><b>Provided Interfaces</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT__PROVIDED_INTERFACES = 4;

	/**
	 * The feature id for the '<em><b>Required Interfaces</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT__REQUIRED_INTERFACES = 5;

	/**
	 * The feature id for the '<em><b>Signature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT__SIGNATURE = 6;

	/**
	 * The number of structural features of the '<em>Deployed Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT_FEATURE_COUNT = 7;

	/**
	 * The operation id for the '<em>Get Context</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT___GET_CONTEXT = 0;

	/**
	 * The number of operations of the '<em>Deployed Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.deployment.impl.EStringToDeployedOperationMapEntryImpl <em>EString To Deployed Operation Map
	 * Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see kieker.model.analysismodel.deployment.impl.EStringToDeployedOperationMapEntryImpl
	 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getEStringToDeployedOperationMapEntry()
	 * @generated
	 */
	int ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY = 5;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Deployed Operation Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EString To Deployed Operation Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.deployment.impl.DeployedOperationImpl <em>Deployed Operation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see kieker.model.analysismodel.deployment.impl.DeployedOperationImpl
	 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeployedOperation()
	 * @generated
	 */
	int DEPLOYED_OPERATION = 6;

	/**
	 * The feature id for the '<em><b>Assembly Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATION__ASSEMBLY_OPERATION = 0;

	/**
	 * The number of structural features of the '<em>Deployed Operation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATION_FEATURE_COUNT = 1;

	/**
	 * The operation id for the '<em>Get Component</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATION___GET_COMPONENT = 0;

	/**
	 * The number of operations of the '<em>Deployed Operation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATION_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.deployment.impl.EStringToDeployedStorageMapEntryImpl <em>EString To Deployed Storage Map
	 * Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see kieker.model.analysismodel.deployment.impl.EStringToDeployedStorageMapEntryImpl
	 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getEStringToDeployedStorageMapEntry()
	 * @generated
	 */
	int ESTRING_TO_DEPLOYED_STORAGE_MAP_ENTRY = 7;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_STORAGE_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_STORAGE_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Deployed Storage Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_STORAGE_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EString To Deployed Storage Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_STORAGE_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.deployment.impl.DeployedStorageImpl <em>Deployed Storage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see kieker.model.analysismodel.deployment.impl.DeployedStorageImpl
	 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeployedStorage()
	 * @generated
	 */
	int DEPLOYED_STORAGE = 8;

	/**
	 * The feature id for the '<em><b>Assembly Storage</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_STORAGE__ASSEMBLY_STORAGE = 0;

	/**
	 * The number of structural features of the '<em>Deployed Storage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_STORAGE_FEATURE_COUNT = 1;

	/**
	 * The operation id for the '<em>Get Component</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_STORAGE___GET_COMPONENT = 0;

	/**
	 * The number of operations of the '<em>Deployed Storage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_STORAGE_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.deployment.impl.DeployedProvidedInterfaceImpl <em>Deployed Provided Interface</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see kieker.model.analysismodel.deployment.impl.DeployedProvidedInterfaceImpl
	 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeployedProvidedInterface()
	 * @generated
	 */
	int DEPLOYED_PROVIDED_INTERFACE = 9;

	/**
	 * The feature id for the '<em><b>Provided Interface</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_PROVIDED_INTERFACE__PROVIDED_INTERFACE = 0;

	/**
	 * The number of structural features of the '<em>Deployed Provided Interface</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_PROVIDED_INTERFACE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Deployed Provided Interface</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_PROVIDED_INTERFACE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.deployment.impl.EStringToDeployedProvidedInterfaceMapEntryImpl <em>EString To Deployed Provided
	 * Interface Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see kieker.model.analysismodel.deployment.impl.EStringToDeployedProvidedInterfaceMapEntryImpl
	 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getEStringToDeployedProvidedInterfaceMapEntry()
	 * @generated
	 */
	int ESTRING_TO_DEPLOYED_PROVIDED_INTERFACE_MAP_ENTRY = 10;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_PROVIDED_INTERFACE_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_PROVIDED_INTERFACE_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Deployed Provided Interface Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_PROVIDED_INTERFACE_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EString To Deployed Provided Interface Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_PROVIDED_INTERFACE_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.deployment.impl.DeployedRequiredInterfaceImpl <em>Deployed Required Interface</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see kieker.model.analysismodel.deployment.impl.DeployedRequiredInterfaceImpl
	 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeployedRequiredInterface()
	 * @generated
	 */
	int DEPLOYED_REQUIRED_INTERFACE = 11;

	/**
	 * The feature id for the '<em><b>Required Interface</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_REQUIRED_INTERFACE__REQUIRED_INTERFACE = 0;

	/**
	 * The feature id for the '<em><b>Requires</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_REQUIRED_INTERFACE__REQUIRES = 1;

	/**
	 * The number of structural features of the '<em>Deployed Required Interface</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_REQUIRED_INTERFACE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Deployed Required Interface</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_REQUIRED_INTERFACE_OPERATION_COUNT = 0;

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.deployment.DeploymentModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Model</em>'.
	 * @see kieker.model.analysismodel.deployment.DeploymentModel
	 * @generated
	 */
	EClass getDeploymentModel();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.deployment.DeploymentModel#getContexts <em>Contexts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the map '<em>Contexts</em>'.
	 * @see kieker.model.analysismodel.deployment.DeploymentModel#getContexts()
	 * @see #getDeploymentModel()
	 * @generated
	 */
	EReference getDeploymentModel_Contexts();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Deployment Context Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>EString To Deployment Context Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.model.analysismodel.deployment.DeploymentContext" valueContainment="true"
	 * @generated
	 */
	EClass getEStringToDeploymentContextMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToDeploymentContextMapEntry()
	 * @generated
	 */
	EAttribute getEStringToDeploymentContextMapEntry_Key();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToDeploymentContextMapEntry()
	 * @generated
	 */
	EReference getEStringToDeploymentContextMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.deployment.DeploymentContext <em>Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Context</em>'.
	 * @see kieker.model.analysismodel.deployment.DeploymentContext
	 * @generated
	 */
	EClass getDeploymentContext();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.deployment.DeploymentContext#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.model.analysismodel.deployment.DeploymentContext#getName()
	 * @see #getDeploymentContext()
	 * @generated
	 */
	EAttribute getDeploymentContext_Name();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.deployment.DeploymentContext#getComponents <em>Components</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the map '<em>Components</em>'.
	 * @see kieker.model.analysismodel.deployment.DeploymentContext#getComponents()
	 * @see #getDeploymentContext()
	 * @generated
	 */
	EReference getDeploymentContext_Components();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Deployed Component Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>EString To Deployed Component Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.model.analysismodel.deployment.DeployedComponent" valueContainment="true"
	 * @generated
	 */
	EClass getEStringToDeployedComponentMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToDeployedComponentMapEntry()
	 * @generated
	 */
	EAttribute getEStringToDeployedComponentMapEntry_Key();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToDeployedComponentMapEntry()
	 * @generated
	 */
	EReference getEStringToDeployedComponentMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.deployment.DeployedComponent <em>Deployed Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Deployed Component</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedComponent
	 * @generated
	 */
	EClass getDeployedComponent();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.deployment.DeployedComponent#getAssemblyComponent <em>Assembly Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the reference '<em>Assembly Component</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedComponent#getAssemblyComponent()
	 * @see #getDeployedComponent()
	 * @generated
	 */
	EReference getDeployedComponent_AssemblyComponent();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.deployment.DeployedComponent#getOperations <em>Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the map '<em>Operations</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedComponent#getOperations()
	 * @see #getDeployedComponent()
	 * @generated
	 */
	EReference getDeployedComponent_Operations();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.deployment.DeployedComponent#getStorages <em>Storages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the map '<em>Storages</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedComponent#getStorages()
	 * @see #getDeployedComponent()
	 * @generated
	 */
	EReference getDeployedComponent_Storages();

	/**
	 * Returns the meta object for the reference list '{@link kieker.model.analysismodel.deployment.DeployedComponent#getContainedComponents <em>Contained
	 * Components</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the reference list '<em>Contained Components</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedComponent#getContainedComponents()
	 * @see #getDeployedComponent()
	 * @generated
	 */
	EReference getDeployedComponent_ContainedComponents();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.deployment.DeployedComponent#getProvidedInterfaces <em>Provided Interfaces</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the map '<em>Provided Interfaces</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedComponent#getProvidedInterfaces()
	 * @see #getDeployedComponent()
	 * @generated
	 */
	EReference getDeployedComponent_ProvidedInterfaces();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.model.analysismodel.deployment.DeployedComponent#getRequiredInterfaces <em>Required
	 * Interfaces</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference list '<em>Required Interfaces</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedComponent#getRequiredInterfaces()
	 * @see #getDeployedComponent()
	 * @generated
	 */
	EReference getDeployedComponent_RequiredInterfaces();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.deployment.DeployedComponent#getSignature <em>Signature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Signature</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedComponent#getSignature()
	 * @see #getDeployedComponent()
	 * @generated
	 */
	EAttribute getDeployedComponent_Signature();

	/**
	 * Returns the meta object for the '{@link kieker.model.analysismodel.deployment.DeployedComponent#getContext() <em>Get Context</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the '<em>Get Context</em>' operation.
	 * @see kieker.model.analysismodel.deployment.DeployedComponent#getContext()
	 * @generated
	 */
	EOperation getDeployedComponent__GetContext();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Deployed Operation Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>EString To Deployed Operation Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.model.analysismodel.deployment.DeployedOperation" valueContainment="true"
	 * @generated
	 */
	EClass getEStringToDeployedOperationMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToDeployedOperationMapEntry()
	 * @generated
	 */
	EAttribute getEStringToDeployedOperationMapEntry_Key();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToDeployedOperationMapEntry()
	 * @generated
	 */
	EReference getEStringToDeployedOperationMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.deployment.DeployedOperation <em>Deployed Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Deployed Operation</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedOperation
	 * @generated
	 */
	EClass getDeployedOperation();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.deployment.DeployedOperation#getAssemblyOperation <em>Assembly Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the reference '<em>Assembly Operation</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedOperation#getAssemblyOperation()
	 * @see #getDeployedOperation()
	 * @generated
	 */
	EReference getDeployedOperation_AssemblyOperation();

	/**
	 * Returns the meta object for the '{@link kieker.model.analysismodel.deployment.DeployedOperation#getComponent() <em>Get Component</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the '<em>Get Component</em>' operation.
	 * @see kieker.model.analysismodel.deployment.DeployedOperation#getComponent()
	 * @generated
	 */
	EOperation getDeployedOperation__GetComponent();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Deployed Storage Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>EString To Deployed Storage Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.model.analysismodel.deployment.DeployedStorage" valueContainment="true"
	 * @generated
	 */
	EClass getEStringToDeployedStorageMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToDeployedStorageMapEntry()
	 * @generated
	 */
	EAttribute getEStringToDeployedStorageMapEntry_Key();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToDeployedStorageMapEntry()
	 * @generated
	 */
	EReference getEStringToDeployedStorageMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.deployment.DeployedStorage <em>Deployed Storage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Deployed Storage</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedStorage
	 * @generated
	 */
	EClass getDeployedStorage();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.deployment.DeployedStorage#getAssemblyStorage <em>Assembly Storage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the reference '<em>Assembly Storage</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedStorage#getAssemblyStorage()
	 * @see #getDeployedStorage()
	 * @generated
	 */
	EReference getDeployedStorage_AssemblyStorage();

	/**
	 * Returns the meta object for the '{@link kieker.model.analysismodel.deployment.DeployedStorage#getComponent() <em>Get Component</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the '<em>Get Component</em>' operation.
	 * @see kieker.model.analysismodel.deployment.DeployedStorage#getComponent()
	 * @generated
	 */
	EOperation getDeployedStorage__GetComponent();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.deployment.DeployedProvidedInterface <em>Deployed Provided Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Deployed Provided Interface</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedProvidedInterface
	 * @generated
	 */
	EClass getDeployedProvidedInterface();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.deployment.DeployedProvidedInterface#getProvidedInterface <em>Provided
	 * Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the reference '<em>Provided Interface</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedProvidedInterface#getProvidedInterface()
	 * @see #getDeployedProvidedInterface()
	 * @generated
	 */
	EReference getDeployedProvidedInterface_ProvidedInterface();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Deployed Provided Interface Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>EString To Deployed Provided Interface Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.model.analysismodel.deployment.DeployedProvidedInterface" valueContainment="true"
	 * @generated
	 */
	EClass getEStringToDeployedProvidedInterfaceMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToDeployedProvidedInterfaceMapEntry()
	 * @generated
	 */
	EAttribute getEStringToDeployedProvidedInterfaceMapEntry_Key();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToDeployedProvidedInterfaceMapEntry()
	 * @generated
	 */
	EReference getEStringToDeployedProvidedInterfaceMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.deployment.DeployedRequiredInterface <em>Deployed Required Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for class '<em>Deployed Required Interface</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedRequiredInterface
	 * @generated
	 */
	EClass getDeployedRequiredInterface();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.deployment.DeployedRequiredInterface#getRequiredInterface <em>Required
	 * Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the reference '<em>Required Interface</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedRequiredInterface#getRequiredInterface()
	 * @see #getDeployedRequiredInterface()
	 * @generated
	 */
	EReference getDeployedRequiredInterface_RequiredInterface();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.deployment.DeployedRequiredInterface#getRequires <em>Requires</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the meta object for the reference '<em>Requires</em>'.
	 * @see kieker.model.analysismodel.deployment.DeployedRequiredInterface#getRequires()
	 * @see #getDeployedRequiredInterface()
	 * @generated
	 */
	EReference getDeployedRequiredInterface_Requires();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DeploymentFactory getDeploymentFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each operation of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.deployment.impl.DeploymentModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see kieker.model.analysismodel.deployment.impl.DeploymentModelImpl
		 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeploymentModel()
		 * @generated
		 */
		EClass DEPLOYMENT_MODEL = eINSTANCE.getDeploymentModel();

		/**
		 * The meta object literal for the '<em><b>Contexts</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference DEPLOYMENT_MODEL__CONTEXTS = eINSTANCE.getDeploymentModel_Contexts();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.deployment.impl.EStringToDeploymentContextMapEntryImpl <em>EString To Deployment
		 * Context Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see kieker.model.analysismodel.deployment.impl.EStringToDeploymentContextMapEntryImpl
		 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getEStringToDeploymentContextMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY = eINSTANCE.getEStringToDeploymentContextMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY__KEY = eINSTANCE.getEStringToDeploymentContextMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY__VALUE = eINSTANCE.getEStringToDeploymentContextMapEntry_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.deployment.impl.DeploymentContextImpl <em>Context</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see kieker.model.analysismodel.deployment.impl.DeploymentContextImpl
		 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeploymentContext()
		 * @generated
		 */
		EClass DEPLOYMENT_CONTEXT = eINSTANCE.getDeploymentContext();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute DEPLOYMENT_CONTEXT__NAME = eINSTANCE.getDeploymentContext_Name();

		/**
		 * The meta object literal for the '<em><b>Components</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference DEPLOYMENT_CONTEXT__COMPONENTS = eINSTANCE.getDeploymentContext_Components();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.deployment.impl.EStringToDeployedComponentMapEntryImpl <em>EString To Deployed
		 * Component Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see kieker.model.analysismodel.deployment.impl.EStringToDeployedComponentMapEntryImpl
		 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getEStringToDeployedComponentMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY = eINSTANCE.getEStringToDeployedComponentMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY__KEY = eINSTANCE.getEStringToDeployedComponentMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY__VALUE = eINSTANCE.getEStringToDeployedComponentMapEntry_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl <em>Deployed Component</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see kieker.model.analysismodel.deployment.impl.DeployedComponentImpl
		 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeployedComponent()
		 * @generated
		 */
		EClass DEPLOYED_COMPONENT = eINSTANCE.getDeployedComponent();

		/**
		 * The meta object literal for the '<em><b>Assembly Component</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT = eINSTANCE.getDeployedComponent_AssemblyComponent();

		/**
		 * The meta object literal for the '<em><b>Operations</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference DEPLOYED_COMPONENT__OPERATIONS = eINSTANCE.getDeployedComponent_Operations();

		/**
		 * The meta object literal for the '<em><b>Storages</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference DEPLOYED_COMPONENT__STORAGES = eINSTANCE.getDeployedComponent_Storages();

		/**
		 * The meta object literal for the '<em><b>Contained Components</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference DEPLOYED_COMPONENT__CONTAINED_COMPONENTS = eINSTANCE.getDeployedComponent_ContainedComponents();

		/**
		 * The meta object literal for the '<em><b>Provided Interfaces</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference DEPLOYED_COMPONENT__PROVIDED_INTERFACES = eINSTANCE.getDeployedComponent_ProvidedInterfaces();

		/**
		 * The meta object literal for the '<em><b>Required Interfaces</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference DEPLOYED_COMPONENT__REQUIRED_INTERFACES = eINSTANCE.getDeployedComponent_RequiredInterfaces();

		/**
		 * The meta object literal for the '<em><b>Signature</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute DEPLOYED_COMPONENT__SIGNATURE = eINSTANCE.getDeployedComponent_Signature();

		/**
		 * The meta object literal for the '<em><b>Get Context</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EOperation DEPLOYED_COMPONENT___GET_CONTEXT = eINSTANCE.getDeployedComponent__GetContext();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.deployment.impl.EStringToDeployedOperationMapEntryImpl <em>EString To Deployed
		 * Operation Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see kieker.model.analysismodel.deployment.impl.EStringToDeployedOperationMapEntryImpl
		 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getEStringToDeployedOperationMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY = eINSTANCE.getEStringToDeployedOperationMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY__KEY = eINSTANCE.getEStringToDeployedOperationMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY__VALUE = eINSTANCE.getEStringToDeployedOperationMapEntry_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.deployment.impl.DeployedOperationImpl <em>Deployed Operation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see kieker.model.analysismodel.deployment.impl.DeployedOperationImpl
		 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeployedOperation()
		 * @generated
		 */
		EClass DEPLOYED_OPERATION = eINSTANCE.getDeployedOperation();

		/**
		 * The meta object literal for the '<em><b>Assembly Operation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference DEPLOYED_OPERATION__ASSEMBLY_OPERATION = eINSTANCE.getDeployedOperation_AssemblyOperation();

		/**
		 * The meta object literal for the '<em><b>Get Component</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EOperation DEPLOYED_OPERATION___GET_COMPONENT = eINSTANCE.getDeployedOperation__GetComponent();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.deployment.impl.EStringToDeployedStorageMapEntryImpl <em>EString To Deployed Storage
		 * Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see kieker.model.analysismodel.deployment.impl.EStringToDeployedStorageMapEntryImpl
		 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getEStringToDeployedStorageMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_DEPLOYED_STORAGE_MAP_ENTRY = eINSTANCE.getEStringToDeployedStorageMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute ESTRING_TO_DEPLOYED_STORAGE_MAP_ENTRY__KEY = eINSTANCE.getEStringToDeployedStorageMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference ESTRING_TO_DEPLOYED_STORAGE_MAP_ENTRY__VALUE = eINSTANCE.getEStringToDeployedStorageMapEntry_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.deployment.impl.DeployedStorageImpl <em>Deployed Storage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see kieker.model.analysismodel.deployment.impl.DeployedStorageImpl
		 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeployedStorage()
		 * @generated
		 */
		EClass DEPLOYED_STORAGE = eINSTANCE.getDeployedStorage();

		/**
		 * The meta object literal for the '<em><b>Assembly Storage</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference DEPLOYED_STORAGE__ASSEMBLY_STORAGE = eINSTANCE.getDeployedStorage_AssemblyStorage();

		/**
		 * The meta object literal for the '<em><b>Get Component</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EOperation DEPLOYED_STORAGE___GET_COMPONENT = eINSTANCE.getDeployedStorage__GetComponent();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.deployment.impl.DeployedProvidedInterfaceImpl <em>Deployed Provided Interface</em>}'
		 * class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see kieker.model.analysismodel.deployment.impl.DeployedProvidedInterfaceImpl
		 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeployedProvidedInterface()
		 * @generated
		 */
		EClass DEPLOYED_PROVIDED_INTERFACE = eINSTANCE.getDeployedProvidedInterface();

		/**
		 * The meta object literal for the '<em><b>Provided Interface</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference DEPLOYED_PROVIDED_INTERFACE__PROVIDED_INTERFACE = eINSTANCE.getDeployedProvidedInterface_ProvidedInterface();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.deployment.impl.EStringToDeployedProvidedInterfaceMapEntryImpl <em>EString To Deployed
		 * Provided Interface Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see kieker.model.analysismodel.deployment.impl.EStringToDeployedProvidedInterfaceMapEntryImpl
		 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getEStringToDeployedProvidedInterfaceMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_DEPLOYED_PROVIDED_INTERFACE_MAP_ENTRY = eINSTANCE.getEStringToDeployedProvidedInterfaceMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EAttribute ESTRING_TO_DEPLOYED_PROVIDED_INTERFACE_MAP_ENTRY__KEY = eINSTANCE.getEStringToDeployedProvidedInterfaceMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference ESTRING_TO_DEPLOYED_PROVIDED_INTERFACE_MAP_ENTRY__VALUE = eINSTANCE.getEStringToDeployedProvidedInterfaceMapEntry_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.deployment.impl.DeployedRequiredInterfaceImpl <em>Deployed Required Interface</em>}'
		 * class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @see kieker.model.analysismodel.deployment.impl.DeployedRequiredInterfaceImpl
		 * @see kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeployedRequiredInterface()
		 * @generated
		 */
		EClass DEPLOYED_REQUIRED_INTERFACE = eINSTANCE.getDeployedRequiredInterface();

		/**
		 * The meta object literal for the '<em><b>Required Interface</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference DEPLOYED_REQUIRED_INTERFACE__REQUIRED_INTERFACE = eINSTANCE.getDeployedRequiredInterface_RequiredInterface();

		/**
		 * The meta object literal for the '<em><b>Requires</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		EReference DEPLOYED_REQUIRED_INTERFACE__REQUIRES = eINSTANCE.getDeployedRequiredInterface_Requires();

	}

} // DeploymentPackage
