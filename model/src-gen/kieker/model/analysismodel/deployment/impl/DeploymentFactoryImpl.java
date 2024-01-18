/**
 */
package kieker.model.analysismodel.deployment.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedProvidedInterface;
import kieker.model.analysismodel.deployment.DeployedRequiredInterface;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.deployment.DeploymentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class DeploymentFactoryImpl extends EFactoryImpl implements DeploymentFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static DeploymentFactory init() {
		try {
			final DeploymentFactory theDeploymentFactory = (DeploymentFactory) EPackage.Registry.INSTANCE.getEFactory(DeploymentPackage.eNS_URI);
			if (theDeploymentFactory != null) {
				return theDeploymentFactory;
			}
		} catch (final Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DeploymentFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DeploymentFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EObject create(final EClass eClass) {
		switch (eClass.getClassifierID()) {
		case DeploymentPackage.DEPLOYMENT_MODEL:
			return this.createDeploymentModel();
		case DeploymentPackage.ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY:
			return (EObject) this.createEStringToDeploymentContextMapEntry();
		case DeploymentPackage.DEPLOYMENT_CONTEXT:
			return this.createDeploymentContext();
		case DeploymentPackage.ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY:
			return (EObject) this.createEStringToDeployedComponentMapEntry();
		case DeploymentPackage.DEPLOYED_COMPONENT:
			return this.createDeployedComponent();
		case DeploymentPackage.ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY:
			return (EObject) this.createEStringToDeployedOperationMapEntry();
		case DeploymentPackage.DEPLOYED_OPERATION:
			return this.createDeployedOperation();
		case DeploymentPackage.ESTRING_TO_DEPLOYED_STORAGE_MAP_ENTRY:
			return (EObject) this.createEStringToDeployedStorageMapEntry();
		case DeploymentPackage.DEPLOYED_STORAGE:
			return this.createDeployedStorage();
		case DeploymentPackage.DEPLOYED_PROVIDED_INTERFACE:
			return this.createDeployedProvidedInterface();
		case DeploymentPackage.ESTRING_TO_DEPLOYED_PROVIDED_INTERFACE_MAP_ENTRY:
			return (EObject) this.createEStringToDeployedProvidedInterfaceMapEntry();
		case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE:
			return this.createDeployedRequiredInterface();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeploymentModel createDeploymentModel() {
		final DeploymentModelImpl deploymentModel = new DeploymentModelImpl();
		return deploymentModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, DeploymentContext> createEStringToDeploymentContextMapEntry() {
		final EStringToDeploymentContextMapEntryImpl eStringToDeploymentContextMapEntry = new EStringToDeploymentContextMapEntryImpl();
		return eStringToDeploymentContextMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeploymentContext createDeploymentContext() {
		final DeploymentContextImpl deploymentContext = new DeploymentContextImpl();
		return deploymentContext;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, DeployedComponent> createEStringToDeployedComponentMapEntry() {
		final EStringToDeployedComponentMapEntryImpl eStringToDeployedComponentMapEntry = new EStringToDeployedComponentMapEntryImpl();
		return eStringToDeployedComponentMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeployedComponent createDeployedComponent() {
		final DeployedComponentImpl deployedComponent = new DeployedComponentImpl();
		return deployedComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, DeployedOperation> createEStringToDeployedOperationMapEntry() {
		final EStringToDeployedOperationMapEntryImpl eStringToDeployedOperationMapEntry = new EStringToDeployedOperationMapEntryImpl();
		return eStringToDeployedOperationMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeployedOperation createDeployedOperation() {
		final DeployedOperationImpl deployedOperation = new DeployedOperationImpl();
		return deployedOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, DeployedStorage> createEStringToDeployedStorageMapEntry() {
		final EStringToDeployedStorageMapEntryImpl eStringToDeployedStorageMapEntry = new EStringToDeployedStorageMapEntryImpl();
		return eStringToDeployedStorageMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeployedStorage createDeployedStorage() {
		final DeployedStorageImpl deployedStorage = new DeployedStorageImpl();
		return deployedStorage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeployedProvidedInterface createDeployedProvidedInterface() {
		final DeployedProvidedInterfaceImpl deployedProvidedInterface = new DeployedProvidedInterfaceImpl();
		return deployedProvidedInterface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, DeployedProvidedInterface> createEStringToDeployedProvidedInterfaceMapEntry() {
		final EStringToDeployedProvidedInterfaceMapEntryImpl eStringToDeployedProvidedInterfaceMapEntry = new EStringToDeployedProvidedInterfaceMapEntryImpl();
		return eStringToDeployedProvidedInterfaceMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeployedRequiredInterface createDeployedRequiredInterface() {
		final DeployedRequiredInterfaceImpl deployedRequiredInterface = new DeployedRequiredInterfaceImpl();
		return deployedRequiredInterface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeploymentPackage getDeploymentPackage() {
		return (DeploymentPackage) this.getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DeploymentPackage getPackage() {
		return DeploymentPackage.eINSTANCE;
	}

} // DeploymentFactoryImpl
