/**
 */
package kieker.analysisteetime.model.analysismodel.deployment;

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
	 * The feature id for the '<em><b>Deployment Contexts</b></em>' reference list.
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
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentContextImpl <em>Context</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentContextImpl
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeploymentContext()
	 * @generated
	 */
	int DEPLOYMENT_CONTEXT = 1;

	/**
	 * The feature id for the '<em><b>Deployment Root</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_CONTEXT__DEPLOYMENT_ROOT = 0;

	/**
	 * The feature id for the '<em><b>Components</b></em>' reference list.
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
	 * The number of operations of the '<em>Context</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYMENT_CONTEXT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedComponentImpl <em>Deployed Component</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedComponentImpl
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeployedComponent()
	 * @generated
	 */
	int DEPLOYED_COMPONENT = 2;

	/**
	 * The feature id for the '<em><b>Component Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT__COMPONENT_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Deployment Context</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT__DEPLOYMENT_CONTEXT = 1;

	/**
	 * The feature id for the '<em><b>Contained Operations</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT__CONTAINED_OPERATIONS = 2;

	/**
	 * The feature id for the '<em><b>Accessed Operations</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT__ACCESSED_OPERATIONS = 3;

	/**
	 * The number of structural features of the '<em>Deployed Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Deployed Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_COMPONENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedOperationImpl <em>Deployed Operation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedOperationImpl
	 * @see kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentPackageImpl#getDeployedOperation()
	 * @generated
	 */
	int DEPLOYED_OPERATION = 3;

	/**
	 * The feature id for the '<em><b>Operation Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATION__OPERATION_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Contained Component</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATION__CONTAINED_COMPONENT = 1;

	/**
	 * The feature id for the '<em><b>Accesssed Component</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATION__ACCESSSED_COMPONENT = 2;

	/**
	 * The number of structural features of the '<em>Deployed Operation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATION_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Deployed Operation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATION_OPERATION_COUNT = 0;


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
	 * Returns the meta object for the reference list '{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot#getDeploymentContexts <em>Deployment Contexts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Deployment Contexts</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot#getDeploymentContexts()
	 * @see #getDeploymentRoot()
	 * @generated
	 */
	EReference getDeploymentRoot_DeploymentContexts();

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
	 * Returns the meta object for the reference '{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getDeploymentRoot <em>Deployment Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Deployment Root</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getDeploymentRoot()
	 * @see #getDeploymentContext()
	 * @generated
	 */
	EReference getDeploymentContext_DeploymentRoot();

	/**
	 * Returns the meta object for the reference list '{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getComponents <em>Components</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Components</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext#getComponents()
	 * @see #getDeploymentContext()
	 * @generated
	 */
	EReference getDeploymentContext_Components();

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
	 * Returns the meta object for the reference '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getDeploymentContext <em>Deployment Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Deployment Context</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getDeploymentContext()
	 * @see #getDeployedComponent()
	 * @generated
	 */
	EReference getDeployedComponent_DeploymentContext();

	/**
	 * Returns the meta object for the reference list '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getContainedOperations <em>Contained Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Contained Operations</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getContainedOperations()
	 * @see #getDeployedComponent()
	 * @generated
	 */
	EReference getDeployedComponent_ContainedOperations();

	/**
	 * Returns the meta object for the reference list '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getAccessedOperations <em>Accessed Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Accessed Operations</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent#getAccessedOperations()
	 * @see #getDeployedComponent()
	 * @generated
	 */
	EReference getDeployedComponent_AccessedOperations();

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
	 * Returns the meta object for the reference '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getContainedComponent <em>Contained Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Contained Component</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getContainedComponent()
	 * @see #getDeployedOperation()
	 * @generated
	 */
	EReference getDeployedOperation_ContainedComponent();

	/**
	 * Returns the meta object for the reference '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getAccesssedComponent <em>Accesssed Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Accesssed Component</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation#getAccesssedComponent()
	 * @see #getDeployedOperation()
	 * @generated
	 */
	EReference getDeployedOperation_AccesssedComponent();

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
		 * The meta object literal for the '<em><b>Deployment Contexts</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYMENT_ROOT__DEPLOYMENT_CONTEXTS = eINSTANCE.getDeploymentRoot_DeploymentContexts();

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
		 * The meta object literal for the '<em><b>Deployment Root</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYMENT_CONTEXT__DEPLOYMENT_ROOT = eINSTANCE.getDeploymentContext_DeploymentRoot();

		/**
		 * The meta object literal for the '<em><b>Components</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYMENT_CONTEXT__COMPONENTS = eINSTANCE.getDeploymentContext_Components();

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
		 * The meta object literal for the '<em><b>Deployment Context</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_COMPONENT__DEPLOYMENT_CONTEXT = eINSTANCE.getDeployedComponent_DeploymentContext();

		/**
		 * The meta object literal for the '<em><b>Contained Operations</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_COMPONENT__CONTAINED_OPERATIONS = eINSTANCE.getDeployedComponent_ContainedOperations();

		/**
		 * The meta object literal for the '<em><b>Accessed Operations</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_COMPONENT__ACCESSED_OPERATIONS = eINSTANCE.getDeployedComponent_AccessedOperations();

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
		 * The meta object literal for the '<em><b>Contained Component</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_OPERATION__CONTAINED_COMPONENT = eINSTANCE.getDeployedOperation_ContainedComponent();

		/**
		 * The meta object literal for the '<em><b>Accesssed Component</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_OPERATION__ACCESSSED_COMPONENT = eINSTANCE.getDeployedOperation_AccesssedComponent();

	}

} //DeploymentPackage
