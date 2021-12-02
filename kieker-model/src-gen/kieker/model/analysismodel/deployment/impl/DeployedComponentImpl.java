/**
 */
package kieker.model.analysismodel.deployment.impl;

import java.lang.reflect.InvocationTargetException;

import java.util.Collection;
import kieker.model.analysismodel.assembly.AssemblyComponent;

import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedProvidedInterface;
import kieker.model.analysismodel.deployment.DeployedRequiredInterface;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deployed Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getAssemblyComponent <em>Assembly Component</em>}</li>
 *   <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getOperations <em>Operations</em>}</li>
 *   <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getStorages <em>Storages</em>}</li>
 *   <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getContainedComponents <em>Contained Components</em>}</li>
 *   <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getProvidedInterfaces <em>Provided Interfaces</em>}</li>
 *   <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getRequiredInterfaces <em>Required Interfaces</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeployedComponentImpl extends MinimalEObjectImpl.Container implements DeployedComponent {
	/**
	 * The cached value of the '{@link #getAssemblyComponent() <em>Assembly Component</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssemblyComponent()
	 * @generated
	 * @ordered
	 */
	protected AssemblyComponent assemblyComponent;

	/**
	 * The cached value of the '{@link #getOperations() <em>Operations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperations()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, DeployedOperation> operations;

	/**
	 * The cached value of the '{@link #getStorages() <em>Storages</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStorages()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, DeployedStorage> storages;

	/**
	 * The cached value of the '{@link #getContainedComponents() <em>Contained Components</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainedComponents()
	 * @generated
	 * @ordered
	 */
	protected EList<DeployedComponent> containedComponents;

	/**
	 * The cached value of the '{@link #getProvidedInterfaces() <em>Provided Interfaces</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProvidedInterfaces()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, DeployedProvidedInterface> providedInterfaces;

	/**
	 * The cached value of the '{@link #getRequiredInterfaces() <em>Required Interfaces</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequiredInterfaces()
	 * @generated
	 * @ordered
	 */
	protected EList<DeployedRequiredInterface> requiredInterfaces;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeployedComponentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DeploymentPackage.Literals.DEPLOYED_COMPONENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AssemblyComponent getAssemblyComponent() {
		if (assemblyComponent != null && assemblyComponent.eIsProxy()) {
			InternalEObject oldAssemblyComponent = (InternalEObject)assemblyComponent;
			assemblyComponent = (AssemblyComponent)eResolveProxy(oldAssemblyComponent);
			if (assemblyComponent != oldAssemblyComponent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT, oldAssemblyComponent, assemblyComponent));
			}
		}
		return assemblyComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssemblyComponent basicGetAssemblyComponent() {
		return assemblyComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAssemblyComponent(AssemblyComponent newAssemblyComponent) {
		AssemblyComponent oldAssemblyComponent = assemblyComponent;
		assemblyComponent = newAssemblyComponent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT, oldAssemblyComponent, assemblyComponent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, DeployedOperation> getOperations() {
		if (operations == null) {
			operations = new EcoreEMap<String,DeployedOperation>(DeploymentPackage.Literals.ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY, EStringToDeployedOperationMapEntryImpl.class, this, DeploymentPackage.DEPLOYED_COMPONENT__OPERATIONS);
		}
		return operations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, DeployedStorage> getStorages() {
		if (storages == null) {
			storages = new EcoreEMap<String,DeployedStorage>(DeploymentPackage.Literals.ESTRING_TO_DEPLOYED_STORAGE_MAP_ENTRY, EStringToDeployedStorageMapEntryImpl.class, this, DeploymentPackage.DEPLOYED_COMPONENT__STORAGES);
		}
		return storages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DeployedComponent> getContainedComponents() {
		if (containedComponents == null) {
			containedComponents = new EObjectResolvingEList<DeployedComponent>(DeployedComponent.class, this, DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_COMPONENTS);
		}
		return containedComponents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, DeployedProvidedInterface> getProvidedInterfaces() {
		if (providedInterfaces == null) {
			providedInterfaces = new EcoreEMap<String,DeployedProvidedInterface>(DeploymentPackage.Literals.ESTRING_TO_DEPLOYED_PROVIDED_INTERFACE_MAP_ENTRY, EStringToDeployedProvidedInterfaceMapEntryImpl.class, this, DeploymentPackage.DEPLOYED_COMPONENT__PROVIDED_INTERFACES);
		}
		return providedInterfaces;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DeployedRequiredInterface> getRequiredInterfaces() {
		if (requiredInterfaces == null) {
			requiredInterfaces = new EObjectContainmentEList<DeployedRequiredInterface>(DeployedRequiredInterface.class, this, DeploymentPackage.DEPLOYED_COMPONENT__REQUIRED_INTERFACES);
		}
		return requiredInterfaces;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DeploymentContext getDeploymentContext() {
		org.eclipse.emf.ecore.EObject container = this.eContainer();
		if (container != null) {
			org.eclipse.emf.ecore.EObject containerContainer = container.eContainer();
			if (containerContainer != null) {
				return (DeploymentContext) containerContainer ;
			}
		}
		return null;
		
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DeploymentPackage.DEPLOYED_COMPONENT__OPERATIONS:
				return ((InternalEList<?>)getOperations()).basicRemove(otherEnd, msgs);
			case DeploymentPackage.DEPLOYED_COMPONENT__STORAGES:
				return ((InternalEList<?>)getStorages()).basicRemove(otherEnd, msgs);
			case DeploymentPackage.DEPLOYED_COMPONENT__PROVIDED_INTERFACES:
				return ((InternalEList<?>)getProvidedInterfaces()).basicRemove(otherEnd, msgs);
			case DeploymentPackage.DEPLOYED_COMPONENT__REQUIRED_INTERFACES:
				return ((InternalEList<?>)getRequiredInterfaces()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT:
				if (resolve) return getAssemblyComponent();
				return basicGetAssemblyComponent();
			case DeploymentPackage.DEPLOYED_COMPONENT__OPERATIONS:
				if (coreType) return getOperations();
				else return getOperations().map();
			case DeploymentPackage.DEPLOYED_COMPONENT__STORAGES:
				if (coreType) return getStorages();
				else return getStorages().map();
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_COMPONENTS:
				return getContainedComponents();
			case DeploymentPackage.DEPLOYED_COMPONENT__PROVIDED_INTERFACES:
				if (coreType) return getProvidedInterfaces();
				else return getProvidedInterfaces().map();
			case DeploymentPackage.DEPLOYED_COMPONENT__REQUIRED_INTERFACES:
				return getRequiredInterfaces();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT:
				setAssemblyComponent((AssemblyComponent)newValue);
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__OPERATIONS:
				((EStructuralFeature.Setting)getOperations()).set(newValue);
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__STORAGES:
				((EStructuralFeature.Setting)getStorages()).set(newValue);
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_COMPONENTS:
				getContainedComponents().clear();
				getContainedComponents().addAll((Collection<? extends DeployedComponent>)newValue);
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__PROVIDED_INTERFACES:
				((EStructuralFeature.Setting)getProvidedInterfaces()).set(newValue);
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__REQUIRED_INTERFACES:
				getRequiredInterfaces().clear();
				getRequiredInterfaces().addAll((Collection<? extends DeployedRequiredInterface>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT:
				setAssemblyComponent((AssemblyComponent)null);
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__OPERATIONS:
				getOperations().clear();
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__STORAGES:
				getStorages().clear();
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_COMPONENTS:
				getContainedComponents().clear();
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__PROVIDED_INTERFACES:
				getProvidedInterfaces().clear();
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__REQUIRED_INTERFACES:
				getRequiredInterfaces().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT:
				return assemblyComponent != null;
			case DeploymentPackage.DEPLOYED_COMPONENT__OPERATIONS:
				return operations != null && !operations.isEmpty();
			case DeploymentPackage.DEPLOYED_COMPONENT__STORAGES:
				return storages != null && !storages.isEmpty();
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_COMPONENTS:
				return containedComponents != null && !containedComponents.isEmpty();
			case DeploymentPackage.DEPLOYED_COMPONENT__PROVIDED_INTERFACES:
				return providedInterfaces != null && !providedInterfaces.isEmpty();
			case DeploymentPackage.DEPLOYED_COMPONENT__REQUIRED_INTERFACES:
				return requiredInterfaces != null && !requiredInterfaces.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case DeploymentPackage.DEPLOYED_COMPONENT___GET_DEPLOYMENT_CONTEXT:
				return getDeploymentContext();
		}
		return super.eInvoke(operationID, arguments);
	}

} //DeployedComponentImpl
