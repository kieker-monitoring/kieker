/**
 */
package kieker.model.analysismodel.deployment.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

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

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedProvidedInterface;
import kieker.model.analysismodel.deployment.DeployedRequiredInterface;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deployed Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getAssemblyComponent <em>Assembly Component</em>}</li>
 * <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getOperations <em>Operations</em>}</li>
 * <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getStorages <em>Storages</em>}</li>
 * <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getContainedComponents <em>Contained Components</em>}</li>
 * <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getProvidedInterfaces <em>Provided Interfaces</em>}</li>
 * <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getRequiredInterfaces <em>Required Interfaces</em>}</li>
 * <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getSignature <em>Signature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeployedComponentImpl extends MinimalEObjectImpl.Container implements DeployedComponent {
	/**
	 * The cached value of the '{@link #getAssemblyComponent() <em>Assembly Component</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getAssemblyComponent()
	 * @generated
	 * @ordered
	 */
	protected AssemblyComponent assemblyComponent;

	/**
	 * The cached value of the '{@link #getOperations() <em>Operations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOperations()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, DeployedOperation> operations;

	/**
	 * The cached value of the '{@link #getStorages() <em>Storages</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getStorages()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, DeployedStorage> storages;

	/**
	 * The cached value of the '{@link #getContainedComponents() <em>Contained Components</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getContainedComponents()
	 * @generated
	 * @ordered
	 */
	protected EList<DeployedComponent> containedComponents;

	/**
	 * The cached value of the '{@link #getProvidedInterfaces() <em>Provided Interfaces</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getProvidedInterfaces()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, DeployedProvidedInterface> providedInterfaces;

	/**
	 * The cached value of the '{@link #getRequiredInterfaces() <em>Required Interfaces</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getRequiredInterfaces()
	 * @generated
	 * @ordered
	 */
	protected EList<DeployedRequiredInterface> requiredInterfaces;

	/**
	 * The default value of the '{@link #getSignature() <em>Signature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getSignature()
	 * @generated
	 * @ordered
	 */
	protected static final String SIGNATURE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSignature() <em>Signature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getSignature()
	 * @generated
	 * @ordered
	 */
	protected String signature = SIGNATURE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected DeployedComponentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DeploymentPackage.Literals.DEPLOYED_COMPONENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AssemblyComponent getAssemblyComponent() {
		if ((this.assemblyComponent != null) && this.assemblyComponent.eIsProxy()) {
			final InternalEObject oldAssemblyComponent = (InternalEObject) this.assemblyComponent;
			this.assemblyComponent = (AssemblyComponent) this.eResolveProxy(oldAssemblyComponent);
			if (this.assemblyComponent != oldAssemblyComponent) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT, oldAssemblyComponent,
							this.assemblyComponent));
				}
			}
		}
		return this.assemblyComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public AssemblyComponent basicGetAssemblyComponent() {
		return this.assemblyComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setAssemblyComponent(final AssemblyComponent newAssemblyComponent) {
		final AssemblyComponent oldAssemblyComponent = this.assemblyComponent;
		this.assemblyComponent = newAssemblyComponent;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT, oldAssemblyComponent,
					this.assemblyComponent));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<String, DeployedOperation> getOperations() {
		if (this.operations == null) {
			this.operations = new EcoreEMap<>(DeploymentPackage.Literals.ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY,
					EStringToDeployedOperationMapEntryImpl.class, this, DeploymentPackage.DEPLOYED_COMPONENT__OPERATIONS);
		}
		return this.operations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<String, DeployedStorage> getStorages() {
		if (this.storages == null) {
			this.storages = new EcoreEMap<>(DeploymentPackage.Literals.ESTRING_TO_DEPLOYED_STORAGE_MAP_ENTRY,
					EStringToDeployedStorageMapEntryImpl.class, this, DeploymentPackage.DEPLOYED_COMPONENT__STORAGES);
		}
		return this.storages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<DeployedComponent> getContainedComponents() {
		if (this.containedComponents == null) {
			this.containedComponents = new EObjectResolvingEList<>(DeployedComponent.class, this,
					DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_COMPONENTS);
		}
		return this.containedComponents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<String, DeployedProvidedInterface> getProvidedInterfaces() {
		if (this.providedInterfaces == null) {
			this.providedInterfaces = new EcoreEMap<>(DeploymentPackage.Literals.ESTRING_TO_DEPLOYED_PROVIDED_INTERFACE_MAP_ENTRY,
					EStringToDeployedProvidedInterfaceMapEntryImpl.class, this, DeploymentPackage.DEPLOYED_COMPONENT__PROVIDED_INTERFACES);
		}
		return this.providedInterfaces;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<DeployedRequiredInterface> getRequiredInterfaces() {
		if (this.requiredInterfaces == null) {
			this.requiredInterfaces = new EObjectContainmentEList<>(DeployedRequiredInterface.class, this,
					DeploymentPackage.DEPLOYED_COMPONENT__REQUIRED_INTERFACES);
		}
		return this.requiredInterfaces;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getSignature() {
		return this.signature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setSignature(final String newSignature) {
		final String oldSignature = this.signature;
		this.signature = newSignature;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_COMPONENT__SIGNATURE, oldSignature, this.signature));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeploymentContext getContext() {
		final org.eclipse.emf.ecore.EObject container = this.eContainer();
		if (container != null) {
			final org.eclipse.emf.ecore.EObject containerContainer = container.eContainer();
			if (containerContainer != null) {
				return (DeploymentContext) containerContainer;
			}
		}
		return null;

	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(final InternalEObject otherEnd, final int featureID, final NotificationChain msgs) {
		switch (featureID) {
		case DeploymentPackage.DEPLOYED_COMPONENT__OPERATIONS:
			return ((InternalEList<?>) this.getOperations()).basicRemove(otherEnd, msgs);
		case DeploymentPackage.DEPLOYED_COMPONENT__STORAGES:
			return ((InternalEList<?>) this.getStorages()).basicRemove(otherEnd, msgs);
		case DeploymentPackage.DEPLOYED_COMPONENT__PROVIDED_INTERFACES:
			return ((InternalEList<?>) this.getProvidedInterfaces()).basicRemove(otherEnd, msgs);
		case DeploymentPackage.DEPLOYED_COMPONENT__REQUIRED_INTERFACES:
			return ((InternalEList<?>) this.getRequiredInterfaces()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
		switch (featureID) {
		case DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT:
			if (resolve) {
				return this.getAssemblyComponent();
			}
			return this.basicGetAssemblyComponent();
		case DeploymentPackage.DEPLOYED_COMPONENT__OPERATIONS:
			if (coreType) {
				return this.getOperations();
			} else {
				return this.getOperations().map();
			}
		case DeploymentPackage.DEPLOYED_COMPONENT__STORAGES:
			if (coreType) {
				return this.getStorages();
			} else {
				return this.getStorages().map();
			}
		case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_COMPONENTS:
			return this.getContainedComponents();
		case DeploymentPackage.DEPLOYED_COMPONENT__PROVIDED_INTERFACES:
			if (coreType) {
				return this.getProvidedInterfaces();
			} else {
				return this.getProvidedInterfaces().map();
			}
		case DeploymentPackage.DEPLOYED_COMPONENT__REQUIRED_INTERFACES:
			return this.getRequiredInterfaces();
		case DeploymentPackage.DEPLOYED_COMPONENT__SIGNATURE:
			return this.getSignature();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT:
			this.setAssemblyComponent((AssemblyComponent) newValue);
			return;
		case DeploymentPackage.DEPLOYED_COMPONENT__OPERATIONS:
			((EStructuralFeature.Setting) this.getOperations()).set(newValue);
			return;
		case DeploymentPackage.DEPLOYED_COMPONENT__STORAGES:
			((EStructuralFeature.Setting) this.getStorages()).set(newValue);
			return;
		case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_COMPONENTS:
			this.getContainedComponents().clear();
			this.getContainedComponents().addAll((Collection<? extends DeployedComponent>) newValue);
			return;
		case DeploymentPackage.DEPLOYED_COMPONENT__PROVIDED_INTERFACES:
			((EStructuralFeature.Setting) this.getProvidedInterfaces()).set(newValue);
			return;
		case DeploymentPackage.DEPLOYED_COMPONENT__REQUIRED_INTERFACES:
			this.getRequiredInterfaces().clear();
			this.getRequiredInterfaces().addAll((Collection<? extends DeployedRequiredInterface>) newValue);
			return;
		case DeploymentPackage.DEPLOYED_COMPONENT__SIGNATURE:
			this.setSignature((String) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eUnset(final int featureID) {
		switch (featureID) {
		case DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT:
			this.setAssemblyComponent((AssemblyComponent) null);
			return;
		case DeploymentPackage.DEPLOYED_COMPONENT__OPERATIONS:
			this.getOperations().clear();
			return;
		case DeploymentPackage.DEPLOYED_COMPONENT__STORAGES:
			this.getStorages().clear();
			return;
		case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_COMPONENTS:
			this.getContainedComponents().clear();
			return;
		case DeploymentPackage.DEPLOYED_COMPONENT__PROVIDED_INTERFACES:
			this.getProvidedInterfaces().clear();
			return;
		case DeploymentPackage.DEPLOYED_COMPONENT__REQUIRED_INTERFACES:
			this.getRequiredInterfaces().clear();
			return;
		case DeploymentPackage.DEPLOYED_COMPONENT__SIGNATURE:
			this.setSignature(SIGNATURE_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean eIsSet(final int featureID) {
		switch (featureID) {
		case DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT:
			return this.assemblyComponent != null;
		case DeploymentPackage.DEPLOYED_COMPONENT__OPERATIONS:
			return (this.operations != null) && !this.operations.isEmpty();
		case DeploymentPackage.DEPLOYED_COMPONENT__STORAGES:
			return (this.storages != null) && !this.storages.isEmpty();
		case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_COMPONENTS:
			return (this.containedComponents != null) && !this.containedComponents.isEmpty();
		case DeploymentPackage.DEPLOYED_COMPONENT__PROVIDED_INTERFACES:
			return (this.providedInterfaces != null) && !this.providedInterfaces.isEmpty();
		case DeploymentPackage.DEPLOYED_COMPONENT__REQUIRED_INTERFACES:
			return (this.requiredInterfaces != null) && !this.requiredInterfaces.isEmpty();
		case DeploymentPackage.DEPLOYED_COMPONENT__SIGNATURE:
			return SIGNATURE_EDEFAULT == null ? this.signature != null : !SIGNATURE_EDEFAULT.equals(this.signature);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eInvoke(final int operationID, final EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
		case DeploymentPackage.DEPLOYED_COMPONENT___GET_CONTEXT:
			return this.getContext();
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String toString() {
		if (this.eIsProxy()) {
			return super.toString();
		}

		final StringBuilder result = new StringBuilder(super.toString());
		result.append(" (signature: ");
		result.append(this.signature);
		result.append(')');
		return result.toString();
	}

} // DeployedComponentImpl
