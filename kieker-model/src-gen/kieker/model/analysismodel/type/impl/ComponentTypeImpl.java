/**
 */
package kieker.model.analysismodel.type.impl;

import java.util.Collection;

import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.ProvidedInterfaceType;
import kieker.model.analysismodel.type.RequiredInterfaceType;
import kieker.model.analysismodel.type.StorageType;
import kieker.model.analysismodel.type.TypePackage;

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
 * An implementation of the model object '<em><b>Component Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl#getSignature <em>Signature</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl#getProvidedOperations <em>Provided Operations</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl#getPackage <em>Package</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl#getProvidedStorages <em>Provided Storages</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl#getContainedComponents <em>Contained Components</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl#getProvidedInterfaceTypes <em>Provided Interface Types</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.impl.ComponentTypeImpl#getRequiredInterfaceTypes <em>Required Interface Types</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ComponentTypeImpl extends MinimalEObjectImpl.Container implements ComponentType {
	/**
	 * The default value of the '{@link #getSignature() <em>Signature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSignature()
	 * @generated
	 * @ordered
	 */
	protected static final String SIGNATURE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSignature() <em>Signature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSignature()
	 * @generated
	 * @ordered
	 */
	protected String signature = SIGNATURE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getProvidedOperations() <em>Provided Operations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProvidedOperations()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, OperationType> providedOperations;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getPackage() <em>Package</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPackage()
	 * @generated
	 * @ordered
	 */
	protected static final String PACKAGE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPackage() <em>Package</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPackage()
	 * @generated
	 * @ordered
	 */
	protected String package_ = PACKAGE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getProvidedStorages() <em>Provided Storages</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProvidedStorages()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, StorageType> providedStorages;

	/**
	 * The cached value of the '{@link #getContainedComponents() <em>Contained Components</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainedComponents()
	 * @generated
	 * @ordered
	 */
	protected EList<ComponentType> containedComponents;

	/**
	 * The cached value of the '{@link #getProvidedInterfaceTypes() <em>Provided Interface Types</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProvidedInterfaceTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<ProvidedInterfaceType> providedInterfaceTypes;

	/**
	 * The cached value of the '{@link #getRequiredInterfaceTypes() <em>Required Interface Types</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequiredInterfaceTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<RequiredInterfaceType> requiredInterfaceTypes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComponentTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TypePackage.Literals.COMPONENT_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getSignature() {
		return signature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSignature(String newSignature) {
		String oldSignature = signature;
		signature = newSignature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.COMPONENT_TYPE__SIGNATURE, oldSignature, signature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, OperationType> getProvidedOperations() {
		if (providedOperations == null) {
			providedOperations = new EcoreEMap<String,OperationType>(TypePackage.Literals.ESTRING_TO_OPERATION_TYPE_MAP_ENTRY, EStringToOperationTypeMapEntryImpl.class, this, TypePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS);
		}
		return providedOperations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.COMPONENT_TYPE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPackage() {
		return package_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPackage(String newPackage) {
		String oldPackage = package_;
		package_ = newPackage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.COMPONENT_TYPE__PACKAGE, oldPackage, package_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, StorageType> getProvidedStorages() {
		if (providedStorages == null) {
			providedStorages = new EcoreEMap<String,StorageType>(TypePackage.Literals.ESTRING_TO_STORAGE_TYPE_MAP_ENTRY, EStringToStorageTypeMapEntryImpl.class, this, TypePackage.COMPONENT_TYPE__PROVIDED_STORAGES);
		}
		return providedStorages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ComponentType> getContainedComponents() {
		if (containedComponents == null) {
			containedComponents = new EObjectResolvingEList<ComponentType>(ComponentType.class, this, TypePackage.COMPONENT_TYPE__CONTAINED_COMPONENTS);
		}
		return containedComponents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ProvidedInterfaceType> getProvidedInterfaceTypes() {
		if (providedInterfaceTypes == null) {
			providedInterfaceTypes = new EObjectContainmentEList<ProvidedInterfaceType>(ProvidedInterfaceType.class, this, TypePackage.COMPONENT_TYPE__PROVIDED_INTERFACE_TYPES);
		}
		return providedInterfaceTypes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<RequiredInterfaceType> getRequiredInterfaceTypes() {
		if (requiredInterfaceTypes == null) {
			requiredInterfaceTypes = new EObjectContainmentEList<RequiredInterfaceType>(RequiredInterfaceType.class, this, TypePackage.COMPONENT_TYPE__REQUIRED_INTERFACE_TYPES);
		}
		return requiredInterfaceTypes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TypePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS:
				return ((InternalEList<?>)getProvidedOperations()).basicRemove(otherEnd, msgs);
			case TypePackage.COMPONENT_TYPE__PROVIDED_STORAGES:
				return ((InternalEList<?>)getProvidedStorages()).basicRemove(otherEnd, msgs);
			case TypePackage.COMPONENT_TYPE__PROVIDED_INTERFACE_TYPES:
				return ((InternalEList<?>)getProvidedInterfaceTypes()).basicRemove(otherEnd, msgs);
			case TypePackage.COMPONENT_TYPE__REQUIRED_INTERFACE_TYPES:
				return ((InternalEList<?>)getRequiredInterfaceTypes()).basicRemove(otherEnd, msgs);
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
			case TypePackage.COMPONENT_TYPE__SIGNATURE:
				return getSignature();
			case TypePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS:
				if (coreType) return getProvidedOperations();
				else return getProvidedOperations().map();
			case TypePackage.COMPONENT_TYPE__NAME:
				return getName();
			case TypePackage.COMPONENT_TYPE__PACKAGE:
				return getPackage();
			case TypePackage.COMPONENT_TYPE__PROVIDED_STORAGES:
				if (coreType) return getProvidedStorages();
				else return getProvidedStorages().map();
			case TypePackage.COMPONENT_TYPE__CONTAINED_COMPONENTS:
				return getContainedComponents();
			case TypePackage.COMPONENT_TYPE__PROVIDED_INTERFACE_TYPES:
				return getProvidedInterfaceTypes();
			case TypePackage.COMPONENT_TYPE__REQUIRED_INTERFACE_TYPES:
				return getRequiredInterfaceTypes();
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
			case TypePackage.COMPONENT_TYPE__SIGNATURE:
				setSignature((String)newValue);
				return;
			case TypePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS:
				((EStructuralFeature.Setting)getProvidedOperations()).set(newValue);
				return;
			case TypePackage.COMPONENT_TYPE__NAME:
				setName((String)newValue);
				return;
			case TypePackage.COMPONENT_TYPE__PACKAGE:
				setPackage((String)newValue);
				return;
			case TypePackage.COMPONENT_TYPE__PROVIDED_STORAGES:
				((EStructuralFeature.Setting)getProvidedStorages()).set(newValue);
				return;
			case TypePackage.COMPONENT_TYPE__CONTAINED_COMPONENTS:
				getContainedComponents().clear();
				getContainedComponents().addAll((Collection<? extends ComponentType>)newValue);
				return;
			case TypePackage.COMPONENT_TYPE__PROVIDED_INTERFACE_TYPES:
				getProvidedInterfaceTypes().clear();
				getProvidedInterfaceTypes().addAll((Collection<? extends ProvidedInterfaceType>)newValue);
				return;
			case TypePackage.COMPONENT_TYPE__REQUIRED_INTERFACE_TYPES:
				getRequiredInterfaceTypes().clear();
				getRequiredInterfaceTypes().addAll((Collection<? extends RequiredInterfaceType>)newValue);
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
			case TypePackage.COMPONENT_TYPE__SIGNATURE:
				setSignature(SIGNATURE_EDEFAULT);
				return;
			case TypePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS:
				getProvidedOperations().clear();
				return;
			case TypePackage.COMPONENT_TYPE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case TypePackage.COMPONENT_TYPE__PACKAGE:
				setPackage(PACKAGE_EDEFAULT);
				return;
			case TypePackage.COMPONENT_TYPE__PROVIDED_STORAGES:
				getProvidedStorages().clear();
				return;
			case TypePackage.COMPONENT_TYPE__CONTAINED_COMPONENTS:
				getContainedComponents().clear();
				return;
			case TypePackage.COMPONENT_TYPE__PROVIDED_INTERFACE_TYPES:
				getProvidedInterfaceTypes().clear();
				return;
			case TypePackage.COMPONENT_TYPE__REQUIRED_INTERFACE_TYPES:
				getRequiredInterfaceTypes().clear();
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
			case TypePackage.COMPONENT_TYPE__SIGNATURE:
				return SIGNATURE_EDEFAULT == null ? signature != null : !SIGNATURE_EDEFAULT.equals(signature);
			case TypePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS:
				return providedOperations != null && !providedOperations.isEmpty();
			case TypePackage.COMPONENT_TYPE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case TypePackage.COMPONENT_TYPE__PACKAGE:
				return PACKAGE_EDEFAULT == null ? package_ != null : !PACKAGE_EDEFAULT.equals(package_);
			case TypePackage.COMPONENT_TYPE__PROVIDED_STORAGES:
				return providedStorages != null && !providedStorages.isEmpty();
			case TypePackage.COMPONENT_TYPE__CONTAINED_COMPONENTS:
				return containedComponents != null && !containedComponents.isEmpty();
			case TypePackage.COMPONENT_TYPE__PROVIDED_INTERFACE_TYPES:
				return providedInterfaceTypes != null && !providedInterfaceTypes.isEmpty();
			case TypePackage.COMPONENT_TYPE__REQUIRED_INTERFACE_TYPES:
				return requiredInterfaceTypes != null && !requiredInterfaceTypes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (signature: ");
		result.append(signature);
		result.append(", name: ");
		result.append(name);
		result.append(", package: ");
		result.append(package_);
		result.append(')');
		return result.toString();
	}

} //ComponentTypeImpl
