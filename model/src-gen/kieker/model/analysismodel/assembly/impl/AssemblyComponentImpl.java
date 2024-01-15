/**
 */
package kieker.model.analysismodel.assembly.impl;

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
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.AssemblyProvidedInterface;
import kieker.model.analysismodel.assembly.AssemblyRequiredInterface;
import kieker.model.analysismodel.assembly.AssemblyStorage;
import kieker.model.analysismodel.type.ComponentType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getOperations <em>Operations</em>}</li>
 * <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getComponentType <em>Component Type</em>}</li>
 * <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getStorages <em>Storages</em>}</li>
 * <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getContainedComponents <em>Contained Components</em>}</li>
 * <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getProvidedInterfaces <em>Provided Interfaces</em>}</li>
 * <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getRequiredInterfaces <em>Required Interfaces</em>}</li>
 * <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getSignature <em>Signature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssemblyComponentImpl extends MinimalEObjectImpl.Container implements AssemblyComponent {
	/**
	 * The cached value of the '{@link #getOperations() <em>Operations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOperations()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, AssemblyOperation> operations;

	/**
	 * The cached value of the '{@link #getComponentType() <em>Component Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getComponentType()
	 * @generated
	 * @ordered
	 */
	protected ComponentType componentType;

	/**
	 * The cached value of the '{@link #getStorages() <em>Storages</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getStorages()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, AssemblyStorage> storages;

	/**
	 * The cached value of the '{@link #getContainedComponents() <em>Contained Components</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getContainedComponents()
	 * @generated
	 * @ordered
	 */
	protected EList<AssemblyComponent> containedComponents;

	/**
	 * The cached value of the '{@link #getProvidedInterfaces() <em>Provided Interfaces</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getProvidedInterfaces()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, AssemblyProvidedInterface> providedInterfaces;

	/**
	 * The cached value of the '{@link #getRequiredInterfaces() <em>Required Interfaces</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getRequiredInterfaces()
	 * @generated
	 * @ordered
	 */
	protected EList<AssemblyRequiredInterface> requiredInterfaces;

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
	protected AssemblyComponentImpl() {
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
		return AssemblyPackage.Literals.ASSEMBLY_COMPONENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<String, AssemblyOperation> getOperations() {
		if (this.operations == null) {
			this.operations = new EcoreEMap<>(AssemblyPackage.Literals.ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY,
					EStringToAssemblyOperationMapEntryImpl.class, this, AssemblyPackage.ASSEMBLY_COMPONENT__OPERATIONS);
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
	public ComponentType getComponentType() {
		if ((this.componentType != null) && this.componentType.eIsProxy()) {
			final InternalEObject oldComponentType = (InternalEObject) this.componentType;
			this.componentType = (ComponentType) this.eResolveProxy(oldComponentType);
			if (this.componentType != oldComponentType) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE, oldComponentType,
							this.componentType));
				}
			}
		}
		return this.componentType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ComponentType basicGetComponentType() {
		return this.componentType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setComponentType(final ComponentType newComponentType) {
		final ComponentType oldComponentType = this.componentType;
		this.componentType = newComponentType;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE, oldComponentType, this.componentType));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<String, AssemblyStorage> getStorages() {
		if (this.storages == null) {
			this.storages = new EcoreEMap<>(AssemblyPackage.Literals.ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY,
					EStringToAssemblyStorageMapEntryImpl.class, this, AssemblyPackage.ASSEMBLY_COMPONENT__STORAGES);
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
	public EList<AssemblyComponent> getContainedComponents() {
		if (this.containedComponents == null) {
			this.containedComponents = new EObjectResolvingEList<>(AssemblyComponent.class, this,
					AssemblyPackage.ASSEMBLY_COMPONENT__CONTAINED_COMPONENTS);
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
	public EMap<String, AssemblyProvidedInterface> getProvidedInterfaces() {
		if (this.providedInterfaces == null) {
			this.providedInterfaces = new EcoreEMap<>(AssemblyPackage.Literals.ESTRING_TO_ASSEMBLY_PROVIDED_INTERFACE_MAP_ENTRY,
					EStringToAssemblyProvidedInterfaceMapEntryImpl.class, this, AssemblyPackage.ASSEMBLY_COMPONENT__PROVIDED_INTERFACES);
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
	public EList<AssemblyRequiredInterface> getRequiredInterfaces() {
		if (this.requiredInterfaces == null) {
			this.requiredInterfaces = new EObjectContainmentEList<>(AssemblyRequiredInterface.class, this,
					AssemblyPackage.ASSEMBLY_COMPONENT__REQUIRED_INTERFACES);
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
			this.eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_COMPONENT__SIGNATURE, oldSignature, this.signature));
		}
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
		case AssemblyPackage.ASSEMBLY_COMPONENT__OPERATIONS:
			return ((InternalEList<?>) this.getOperations()).basicRemove(otherEnd, msgs);
		case AssemblyPackage.ASSEMBLY_COMPONENT__STORAGES:
			return ((InternalEList<?>) this.getStorages()).basicRemove(otherEnd, msgs);
		case AssemblyPackage.ASSEMBLY_COMPONENT__PROVIDED_INTERFACES:
			return ((InternalEList<?>) this.getProvidedInterfaces()).basicRemove(otherEnd, msgs);
		case AssemblyPackage.ASSEMBLY_COMPONENT__REQUIRED_INTERFACES:
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
		case AssemblyPackage.ASSEMBLY_COMPONENT__OPERATIONS:
			if (coreType) {
				return this.getOperations();
			} else {
				return this.getOperations().map();
			}
		case AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE:
			if (resolve) {
				return this.getComponentType();
			}
			return this.basicGetComponentType();
		case AssemblyPackage.ASSEMBLY_COMPONENT__STORAGES:
			if (coreType) {
				return this.getStorages();
			} else {
				return this.getStorages().map();
			}
		case AssemblyPackage.ASSEMBLY_COMPONENT__CONTAINED_COMPONENTS:
			return this.getContainedComponents();
		case AssemblyPackage.ASSEMBLY_COMPONENT__PROVIDED_INTERFACES:
			if (coreType) {
				return this.getProvidedInterfaces();
			} else {
				return this.getProvidedInterfaces().map();
			}
		case AssemblyPackage.ASSEMBLY_COMPONENT__REQUIRED_INTERFACES:
			return this.getRequiredInterfaces();
		case AssemblyPackage.ASSEMBLY_COMPONENT__SIGNATURE:
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
		case AssemblyPackage.ASSEMBLY_COMPONENT__OPERATIONS:
			((EStructuralFeature.Setting) this.getOperations()).set(newValue);
			return;
		case AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE:
			this.setComponentType((ComponentType) newValue);
			return;
		case AssemblyPackage.ASSEMBLY_COMPONENT__STORAGES:
			((EStructuralFeature.Setting) this.getStorages()).set(newValue);
			return;
		case AssemblyPackage.ASSEMBLY_COMPONENT__CONTAINED_COMPONENTS:
			this.getContainedComponents().clear();
			this.getContainedComponents().addAll((Collection<? extends AssemblyComponent>) newValue);
			return;
		case AssemblyPackage.ASSEMBLY_COMPONENT__PROVIDED_INTERFACES:
			((EStructuralFeature.Setting) this.getProvidedInterfaces()).set(newValue);
			return;
		case AssemblyPackage.ASSEMBLY_COMPONENT__REQUIRED_INTERFACES:
			this.getRequiredInterfaces().clear();
			this.getRequiredInterfaces().addAll((Collection<? extends AssemblyRequiredInterface>) newValue);
			return;
		case AssemblyPackage.ASSEMBLY_COMPONENT__SIGNATURE:
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
		case AssemblyPackage.ASSEMBLY_COMPONENT__OPERATIONS:
			this.getOperations().clear();
			return;
		case AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE:
			this.setComponentType((ComponentType) null);
			return;
		case AssemblyPackage.ASSEMBLY_COMPONENT__STORAGES:
			this.getStorages().clear();
			return;
		case AssemblyPackage.ASSEMBLY_COMPONENT__CONTAINED_COMPONENTS:
			this.getContainedComponents().clear();
			return;
		case AssemblyPackage.ASSEMBLY_COMPONENT__PROVIDED_INTERFACES:
			this.getProvidedInterfaces().clear();
			return;
		case AssemblyPackage.ASSEMBLY_COMPONENT__REQUIRED_INTERFACES:
			this.getRequiredInterfaces().clear();
			return;
		case AssemblyPackage.ASSEMBLY_COMPONENT__SIGNATURE:
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
		case AssemblyPackage.ASSEMBLY_COMPONENT__OPERATIONS:
			return (this.operations != null) && !this.operations.isEmpty();
		case AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE:
			return this.componentType != null;
		case AssemblyPackage.ASSEMBLY_COMPONENT__STORAGES:
			return (this.storages != null) && !this.storages.isEmpty();
		case AssemblyPackage.ASSEMBLY_COMPONENT__CONTAINED_COMPONENTS:
			return (this.containedComponents != null) && !this.containedComponents.isEmpty();
		case AssemblyPackage.ASSEMBLY_COMPONENT__PROVIDED_INTERFACES:
			return (this.providedInterfaces != null) && !this.providedInterfaces.isEmpty();
		case AssemblyPackage.ASSEMBLY_COMPONENT__REQUIRED_INTERFACES:
			return (this.requiredInterfaces != null) && !this.requiredInterfaces.isEmpty();
		case AssemblyPackage.ASSEMBLY_COMPONENT__SIGNATURE:
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

} // AssemblyComponentImpl
