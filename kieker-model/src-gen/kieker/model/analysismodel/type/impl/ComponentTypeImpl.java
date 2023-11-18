/**
 */
package kieker.model.analysismodel.type.impl;

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

import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.ProvidedInterfaceType;
import kieker.model.analysismodel.type.RequiredInterfaceType;
import kieker.model.analysismodel.type.StorageType;
import kieker.model.analysismodel.type.TypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl#getSignature <em>Signature</em>}</li>
 * <li>{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl#getProvidedOperations <em>Provided Operations</em>}</li>
 * <li>{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl#getName <em>Name</em>}</li>
 * <li>{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl#getPackage <em>Package</em>}</li>
 * <li>{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl#getProvidedStorages <em>Provided Storages</em>}</li>
 * <li>{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl#getContainedComponents <em>Contained Components</em>}</li>
 * <li>{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl#getProvidedInterfaceTypes <em>Provided Interface Types</em>}</li>
 * <li>{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl#getRequiredInterfaceTypes <em>Required Interface Types</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ComponentTypeImpl extends MinimalEObjectImpl.Container implements ComponentType {
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
	 * The cached value of the '{@link #getProvidedOperations() <em>Provided Operations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getProvidedOperations()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, OperationType> providedOperations;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getPackage() <em>Package</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getPackage()
	 * @generated
	 * @ordered
	 */
	protected static final String PACKAGE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPackage() <em>Package</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getPackage()
	 * @generated
	 * @ordered
	 */
	protected String package_ = PACKAGE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getProvidedStorages() <em>Provided Storages</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getProvidedStorages()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, StorageType> providedStorages;

	/**
	 * The cached value of the '{@link #getContainedComponents() <em>Contained Components</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getContainedComponents()
	 * @generated
	 * @ordered
	 */
	protected EList<ComponentType> containedComponents;

	/**
	 * The cached value of the '{@link #getProvidedInterfaceTypes() <em>Provided Interface Types</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getProvidedInterfaceTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<ProvidedInterfaceType> providedInterfaceTypes;

	/**
	 * The cached value of the '{@link #getRequiredInterfaceTypes() <em>Required Interface Types</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getRequiredInterfaceTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<RequiredInterfaceType> requiredInterfaceTypes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected ComponentTypeImpl() {
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
		return TypePackage.Literals.COMPONENT_TYPE;
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
			this.eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.COMPONENT_TYPE__SIGNATURE, oldSignature, this.signature));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<String, OperationType> getProvidedOperations() {
		if (this.providedOperations == null) {
			this.providedOperations = new EcoreEMap<>(TypePackage.Literals.ESTRING_TO_OPERATION_TYPE_MAP_ENTRY,
					EStringToOperationTypeMapEntryImpl.class, this, TypePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS);
		}
		return this.providedOperations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setName(final String newName) {
		final String oldName = this.name;
		this.name = newName;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.COMPONENT_TYPE__NAME, oldName, this.name));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getPackage() {
		return this.package_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setPackage(final String newPackage) {
		final String oldPackage = this.package_;
		this.package_ = newPackage;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.COMPONENT_TYPE__PACKAGE, oldPackage, this.package_));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<String, StorageType> getProvidedStorages() {
		if (this.providedStorages == null) {
			this.providedStorages = new EcoreEMap<>(TypePackage.Literals.ESTRING_TO_STORAGE_TYPE_MAP_ENTRY,
					EStringToStorageTypeMapEntryImpl.class,
					this, TypePackage.COMPONENT_TYPE__PROVIDED_STORAGES);
		}
		return this.providedStorages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<ComponentType> getContainedComponents() {
		if (this.containedComponents == null) {
			this.containedComponents = new EObjectResolvingEList<>(ComponentType.class, this, TypePackage.COMPONENT_TYPE__CONTAINED_COMPONENTS);
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
	public EList<ProvidedInterfaceType> getProvidedInterfaceTypes() {
		if (this.providedInterfaceTypes == null) {
			this.providedInterfaceTypes = new EObjectContainmentEList<>(ProvidedInterfaceType.class, this,
					TypePackage.COMPONENT_TYPE__PROVIDED_INTERFACE_TYPES);
		}
		return this.providedInterfaceTypes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<RequiredInterfaceType> getRequiredInterfaceTypes() {
		if (this.requiredInterfaceTypes == null) {
			this.requiredInterfaceTypes = new EObjectContainmentEList<>(RequiredInterfaceType.class, this,
					TypePackage.COMPONENT_TYPE__REQUIRED_INTERFACE_TYPES);
		}
		return this.requiredInterfaceTypes;
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
		case TypePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS:
			return ((InternalEList<?>) this.getProvidedOperations()).basicRemove(otherEnd, msgs);
		case TypePackage.COMPONENT_TYPE__PROVIDED_STORAGES:
			return ((InternalEList<?>) this.getProvidedStorages()).basicRemove(otherEnd, msgs);
		case TypePackage.COMPONENT_TYPE__PROVIDED_INTERFACE_TYPES:
			return ((InternalEList<?>) this.getProvidedInterfaceTypes()).basicRemove(otherEnd, msgs);
		case TypePackage.COMPONENT_TYPE__REQUIRED_INTERFACE_TYPES:
			return ((InternalEList<?>) this.getRequiredInterfaceTypes()).basicRemove(otherEnd, msgs);
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
		case TypePackage.COMPONENT_TYPE__SIGNATURE:
			return this.getSignature();
		case TypePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS:
			if (coreType) {
				return this.getProvidedOperations();
			} else {
				return this.getProvidedOperations().map();
			}
		case TypePackage.COMPONENT_TYPE__NAME:
			return this.getName();
		case TypePackage.COMPONENT_TYPE__PACKAGE:
			return this.getPackage();
		case TypePackage.COMPONENT_TYPE__PROVIDED_STORAGES:
			if (coreType) {
				return this.getProvidedStorages();
			} else {
				return this.getProvidedStorages().map();
			}
		case TypePackage.COMPONENT_TYPE__CONTAINED_COMPONENTS:
			return this.getContainedComponents();
		case TypePackage.COMPONENT_TYPE__PROVIDED_INTERFACE_TYPES:
			return this.getProvidedInterfaceTypes();
		case TypePackage.COMPONENT_TYPE__REQUIRED_INTERFACE_TYPES:
			return this.getRequiredInterfaceTypes();
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
		case TypePackage.COMPONENT_TYPE__SIGNATURE:
			this.setSignature((String) newValue);
			return;
		case TypePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS:
			((EStructuralFeature.Setting) this.getProvidedOperations()).set(newValue);
			return;
		case TypePackage.COMPONENT_TYPE__NAME:
			this.setName((String) newValue);
			return;
		case TypePackage.COMPONENT_TYPE__PACKAGE:
			this.setPackage((String) newValue);
			return;
		case TypePackage.COMPONENT_TYPE__PROVIDED_STORAGES:
			((EStructuralFeature.Setting) this.getProvidedStorages()).set(newValue);
			return;
		case TypePackage.COMPONENT_TYPE__CONTAINED_COMPONENTS:
			this.getContainedComponents().clear();
			this.getContainedComponents().addAll((Collection<? extends ComponentType>) newValue);
			return;
		case TypePackage.COMPONENT_TYPE__PROVIDED_INTERFACE_TYPES:
			this.getProvidedInterfaceTypes().clear();
			this.getProvidedInterfaceTypes().addAll((Collection<? extends ProvidedInterfaceType>) newValue);
			return;
		case TypePackage.COMPONENT_TYPE__REQUIRED_INTERFACE_TYPES:
			this.getRequiredInterfaceTypes().clear();
			this.getRequiredInterfaceTypes().addAll((Collection<? extends RequiredInterfaceType>) newValue);
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
		case TypePackage.COMPONENT_TYPE__SIGNATURE:
			this.setSignature(SIGNATURE_EDEFAULT);
			return;
		case TypePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS:
			this.getProvidedOperations().clear();
			return;
		case TypePackage.COMPONENT_TYPE__NAME:
			this.setName(NAME_EDEFAULT);
			return;
		case TypePackage.COMPONENT_TYPE__PACKAGE:
			this.setPackage(PACKAGE_EDEFAULT);
			return;
		case TypePackage.COMPONENT_TYPE__PROVIDED_STORAGES:
			this.getProvidedStorages().clear();
			return;
		case TypePackage.COMPONENT_TYPE__CONTAINED_COMPONENTS:
			this.getContainedComponents().clear();
			return;
		case TypePackage.COMPONENT_TYPE__PROVIDED_INTERFACE_TYPES:
			this.getProvidedInterfaceTypes().clear();
			return;
		case TypePackage.COMPONENT_TYPE__REQUIRED_INTERFACE_TYPES:
			this.getRequiredInterfaceTypes().clear();
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
		case TypePackage.COMPONENT_TYPE__SIGNATURE:
			return SIGNATURE_EDEFAULT == null ? this.signature != null : !SIGNATURE_EDEFAULT.equals(this.signature);
		case TypePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS:
			return (this.providedOperations != null) && !this.providedOperations.isEmpty();
		case TypePackage.COMPONENT_TYPE__NAME:
			return NAME_EDEFAULT == null ? this.name != null : !NAME_EDEFAULT.equals(this.name);
		case TypePackage.COMPONENT_TYPE__PACKAGE:
			return PACKAGE_EDEFAULT == null ? this.package_ != null : !PACKAGE_EDEFAULT.equals(this.package_);
		case TypePackage.COMPONENT_TYPE__PROVIDED_STORAGES:
			return (this.providedStorages != null) && !this.providedStorages.isEmpty();
		case TypePackage.COMPONENT_TYPE__CONTAINED_COMPONENTS:
			return (this.containedComponents != null) && !this.containedComponents.isEmpty();
		case TypePackage.COMPONENT_TYPE__PROVIDED_INTERFACE_TYPES:
			return (this.providedInterfaceTypes != null) && !this.providedInterfaceTypes.isEmpty();
		case TypePackage.COMPONENT_TYPE__REQUIRED_INTERFACE_TYPES:
			return (this.requiredInterfaceTypes != null) && !this.requiredInterfaceTypes.isEmpty();
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
		result.append(", name: ");
		result.append(this.name);
		result.append(", package: ");
		result.append(this.package_);
		result.append(')');
		return result.toString();
	}

} // ComponentTypeImpl
