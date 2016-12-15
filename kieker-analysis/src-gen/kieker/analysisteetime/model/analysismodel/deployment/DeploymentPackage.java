/**
 */
package kieker.analysisteetime.model.analysismodel.deployment;

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
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentFactory
 * @model kind="package"
 * @generated
 */
public interface DeploymentPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "deployment";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/Kieker/model/analysismodel.ecore/deployment";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "deployment";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DeploymentPackage eINSTANCE = kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl.init();

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentRootImpl <em>Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentRootImpl
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeploymentRoot()
	 * @generated
	 */
	int DEPLOYMENT_ROOT = 0;

	/**
	 * The feature id for the '<em><b>Deployment Contexts</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_ROOT__DEPLOYMENT_CONTEXTS = 0;

	/**
	 * The number of structural features of the '<em>Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_ROOT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_ROOT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.EStringToDeploymentContextMapEntryImpl <em>EString To Deployment Context Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.EStringToDeploymentContextMapEntryImpl
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getEStringToDeploymentContextMapEntry()
	 * @generated
	 */
	int ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY = 1;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Deployment Context Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EString To Deployment Context Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentContextImpl <em>Context</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentContextImpl
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeploymentContext()
	 * @generated
	 */
	int DEPLOYMENT_CONTEXT = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_CONTEXT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Components</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_CONTEXT__COMPONENTS = 1;

	/**
	 * The number of structural features of the '<em>Context</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_CONTEXT_FEATURE_COUNT = 2;

	/**
	 * The operation id for the '<em>Get Deployment Root</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_CONTEXT___GET_DEPLOYMENT_ROOT = 0;

	/**
	 * The number of operations of the '<em>Context</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_CONTEXT_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.EStringToDeployedComponentMapEntryImpl <em>EString To Deployed Component Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.EStringToDeployedComponentMapEntryImpl
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getEStringToDeployedComponentMapEntry()
	 * @generated
	 */
	int ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY = 3;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Deployed Component Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EString To Deployed Component Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedComponentImpl <em>Deployed Component</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedComponentImpl
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeployedComponent()
	 * @generated
	 */
	int DEPLOYED_COMPONENT = 4;

	/**
	 * The feature id for the '<em><b>Component Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT__COMPONENT_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Contained Operations</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT__CONTAINED_OPERATIONS = 1;

	/**
	 * The number of structural features of the '<em>Deployed Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT_FEATURE_COUNT = 2;

	/**
	 * The operation id for the '<em>Get Deployment Context</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT___GET_DEPLOYMENT_CONTEXT = 0;

	/**
	 * The number of operations of the '<em>Deployed Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.EStringToDeployedOperationMapEntryImpl <em>EString To Deployed Operation Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.EStringToDeployedOperationMapEntryImpl
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getEStringToDeployedOperationMapEntry()
	 * @generated
	 */
	int ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY = 5;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EString To Deployed Operation Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EString To Deployed Operation Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedOperationImpl <em>Deployed Operation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedOperationImpl
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeployedOperation()
	 * @generated
	 */
	int DEPLOYED_OPERATION = 6;

	/**
	 * The feature id for the '<em><b>Operation Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATION__OPERATION_TYPE = 0;

	/**
	 * The number of structural features of the '<em>Deployed Operation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATION_FEATURE_COUNT = 1;

	/**
	 * The operation id for the '<em>Get Component</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATION___GET_COMPONENT = 0;

	/**
	 * The number of operations of the '<em>Deployed Operation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATION_OPERATION_COUNT = 1;


	/**
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot <em>Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Root</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot
	 * @generated
	 */
	EClass getDeploymentRoot();

	/**
	 * Returns the meta object for the map '{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot#getDeploymentContexts <em>Deployment Contexts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Deployment Contexts</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot#getDeploymentContexts()
	 * @see #getDeploymentRoot()
	 * @generated
	 */
	EReference getDeploymentRoot_DeploymentContexts();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Deployment Context Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EString To Deployment Context Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext" valueContainment="true"
	 * @generated
	 */
	EClass getEStringToDeploymentContextMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
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
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToDeploymentContextMapEntry()
	 * @generated
	 */
	EReference getEStringToDeploymentContextMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext <em>Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Context</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext
	 * @generated
	 */
	EClass getDeploymentContext();

	/**
	 * Returns the meta object for the map '{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getComponents <em>Components</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Components</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getComponents()
	 * @see #getDeploymentContext()
	 * @generated
	 */
	EReference getDeploymentContext_Components();

	/**
	 * Returns the meta object for the '{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getDeploymentRoot() <em>Get Deployment Root</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Deployment Root</em>' operation.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getDeploymentRoot()
	 * @generated
	 */
	EOperation getDeploymentContext__GetDeploymentRoot();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Deployed Component Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EString To Deployed Component Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent" valueContainment="true"
	 * @generated
	 */
	EClass getEStringToDeployedComponentMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
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
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToDeployedComponentMapEntry()
	 * @generated
	 */
	EReference getEStringToDeployedComponentMapEntry_Value();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getName()
	 * @see #getDeploymentContext()
	 * @generated
	 */
	EAttribute getDeploymentContext_Name();

	/**
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent <em>Deployed Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Deployed Component</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent
	 * @generated
	 */
	EClass getDeployedComponent();

	/**
	 * Returns the meta object for the reference '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getComponentType <em>Component Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Component Type</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getComponentType()
	 * @see #getDeployedComponent()
	 * @generated
	 */
	EReference getDeployedComponent_ComponentType();

	/**
	 * Returns the meta object for the map '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getContainedOperations <em>Contained Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Contained Operations</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getContainedOperations()
	 * @see #getDeployedComponent()
	 * @generated
	 */
	EReference getDeployedComponent_ContainedOperations();

	/**
	 * Returns the meta object for the '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getDeploymentContext() <em>Get Deployment Context</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Deployment Context</em>' operation.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getDeploymentContext()
	 * @generated
	 */
	EOperation getDeployedComponent__GetDeploymentContext();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EString To Deployed Operation Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EString To Deployed Operation Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueType="kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation" valueContainment="true"
	 * @generated
	 */
	EClass getEStringToDeployedOperationMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
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
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStringToDeployedOperationMapEntry()
	 * @generated
	 */
	EReference getEStringToDeployedOperationMapEntry_Value();

	/**
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation <em>Deployed Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Deployed Operation</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation
	 * @generated
	 */
	EClass getDeployedOperation();

	/**
	 * Returns the meta object for the reference '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getOperationType <em>Operation Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Operation Type</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getOperationType()
	 * @see #getDeployedOperation()
	 * @generated
	 */
	EReference getDeployedOperation_OperationType();

	/**
	 * Returns the meta object for the '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getComponent() <em>Get Component</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Component</em>' operation.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getComponent()
	 * @generated
	 */
	EOperation getDeployedOperation__GetComponent();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DeploymentFactory getDeploymentFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentRootImpl <em>Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentRootImpl
		 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeploymentRoot()
		 * @generated
		 */
		EClass DEPLOYMENT_ROOT = eINSTANCE.getDeploymentRoot();

		/**
		 * The meta object literal for the '<em><b>Deployment Contexts</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYMENT_ROOT__DEPLOYMENT_CONTEXTS = eINSTANCE.getDeploymentRoot_DeploymentContexts();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.EStringToDeploymentContextMapEntryImpl <em>EString To Deployment Context Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.EStringToDeploymentContextMapEntryImpl
		 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getEStringToDeploymentContextMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY = eINSTANCE.getEStringToDeploymentContextMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY__KEY = eINSTANCE.getEStringToDeploymentContextMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY__VALUE = eINSTANCE.getEStringToDeploymentContextMapEntry_Value();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentContextImpl <em>Context</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentContextImpl
		 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeploymentContext()
		 * @generated
		 */
		EClass DEPLOYMENT_CONTEXT = eINSTANCE.getDeploymentContext();

		/**
		 * The meta object literal for the '<em><b>Components</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYMENT_CONTEXT__COMPONENTS = eINSTANCE.getDeploymentContext_Components();

		/**
		 * The meta object literal for the '<em><b>Get Deployment Root</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DEPLOYMENT_CONTEXT___GET_DEPLOYMENT_ROOT = eINSTANCE.getDeploymentContext__GetDeploymentRoot();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.EStringToDeployedComponentMapEntryImpl <em>EString To Deployed Component Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.EStringToDeployedComponentMapEntryImpl
		 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getEStringToDeployedComponentMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY = eINSTANCE.getEStringToDeployedComponentMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY__KEY = eINSTANCE.getEStringToDeployedComponentMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY__VALUE = eINSTANCE.getEStringToDeployedComponentMapEntry_Value();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DEPLOYMENT_CONTEXT__NAME = eINSTANCE.getDeploymentContext_Name();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedComponentImpl <em>Deployed Component</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedComponentImpl
		 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeployedComponent()
		 * @generated
		 */
		EClass DEPLOYED_COMPONENT = eINSTANCE.getDeployedComponent();

		/**
		 * The meta object literal for the '<em><b>Component Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_COMPONENT__COMPONENT_TYPE = eINSTANCE.getDeployedComponent_ComponentType();

		/**
		 * The meta object literal for the '<em><b>Contained Operations</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_COMPONENT__CONTAINED_OPERATIONS = eINSTANCE.getDeployedComponent_ContainedOperations();

		/**
		 * The meta object literal for the '<em><b>Get Deployment Context</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DEPLOYED_COMPONENT___GET_DEPLOYMENT_CONTEXT = eINSTANCE.getDeployedComponent__GetDeploymentContext();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.EStringToDeployedOperationMapEntryImpl <em>EString To Deployed Operation Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.EStringToDeployedOperationMapEntryImpl
		 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getEStringToDeployedOperationMapEntry()
		 * @generated
		 */
		EClass ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY = eINSTANCE.getEStringToDeployedOperationMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY__KEY = eINSTANCE.getEStringToDeployedOperationMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY__VALUE = eINSTANCE.getEStringToDeployedOperationMapEntry_Value();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedOperationImpl <em>Deployed Operation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedOperationImpl
		 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeployedOperation()
		 * @generated
		 */
		EClass DEPLOYED_OPERATION = eINSTANCE.getDeployedOperation();

		/**
		 * The meta object literal for the '<em><b>Operation Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_OPERATION__OPERATION_TYPE = eINSTANCE.getDeployedOperation_OperationType();

		/**
		 * The meta object literal for the '<em><b>Get Component</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DEPLOYED_OPERATION___GET_COMPONENT = eINSTANCE.getDeployedOperation__GetComponent();

	}

} //DeploymentPackage
