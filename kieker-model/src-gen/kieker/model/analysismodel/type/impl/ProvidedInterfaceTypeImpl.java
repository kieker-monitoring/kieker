/**
 */
package kieker.model.analysismodel.type.impl;

import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.ProvidedInterfaceType;
import kieker.model.analysismodel.type.TypePackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Provided Interface Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.type.impl.ProvidedInterfaceTypeImpl#getProvidedOperationTypes <em>Provided Operation Types</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.impl.ProvidedInterfaceTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.impl.ProvidedInterfaceTypeImpl#getSignature <em>Signature</em>}</li>
 *   <li>{@link kieker.model.analysismodel.type.impl.ProvidedInterfaceTypeImpl#getPackage <em>Package</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProvidedInterfaceTypeImpl extends MinimalEObjectImpl.Container implements ProvidedInterfaceType {
	/**
	 * The cached value of the '{@link #getProvidedOperationTypes() <em>Provided Operation Types</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProvidedOperationTypes()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, OperationType> providedOperationTypes;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProvidedInterfaceTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TypePackage.Literals.PROVIDED_INTERFACE_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, OperationType> getProvidedOperationTypes() {
		if (providedOperationTypes == null) {
			providedOperationTypes = new EcoreEMap<String,OperationType>(TypePackage.Literals.ESTRING_TO_OPERATION_TYPE_MAP_ENTRY, EStringToOperationTypeMapEntryImpl.class, this, TypePackage.PROVIDED_INTERFACE_TYPE__PROVIDED_OPERATION_TYPES);
		}
		return providedOperationTypes;
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
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PROVIDED_INTERFACE_TYPE__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PROVIDED_INTERFACE_TYPE__SIGNATURE, oldSignature, signature));
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
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PROVIDED_INTERFACE_TYPE__PACKAGE, oldPackage, package_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TypePackage.PROVIDED_INTERFACE_TYPE__PROVIDED_OPERATION_TYPES:
				return ((InternalEList<?>)getProvidedOperationTypes()).basicRemove(otherEnd, msgs);
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
			case TypePackage.PROVIDED_INTERFACE_TYPE__PROVIDED_OPERATION_TYPES:
				if (coreType) return getProvidedOperationTypes();
				else return getProvidedOperationTypes().map();
			case TypePackage.PROVIDED_INTERFACE_TYPE__NAME:
				return getName();
			case TypePackage.PROVIDED_INTERFACE_TYPE__SIGNATURE:
				return getSignature();
			case TypePackage.PROVIDED_INTERFACE_TYPE__PACKAGE:
				return getPackage();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case TypePackage.PROVIDED_INTERFACE_TYPE__PROVIDED_OPERATION_TYPES:
				((EStructuralFeature.Setting)getProvidedOperationTypes()).set(newValue);
				return;
			case TypePackage.PROVIDED_INTERFACE_TYPE__NAME:
				setName((String)newValue);
				return;
			case TypePackage.PROVIDED_INTERFACE_TYPE__SIGNATURE:
				setSignature((String)newValue);
				return;
			case TypePackage.PROVIDED_INTERFACE_TYPE__PACKAGE:
				setPackage((String)newValue);
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
			case TypePackage.PROVIDED_INTERFACE_TYPE__PROVIDED_OPERATION_TYPES:
				getProvidedOperationTypes().clear();
				return;
			case TypePackage.PROVIDED_INTERFACE_TYPE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case TypePackage.PROVIDED_INTERFACE_TYPE__SIGNATURE:
				setSignature(SIGNATURE_EDEFAULT);
				return;
			case TypePackage.PROVIDED_INTERFACE_TYPE__PACKAGE:
				setPackage(PACKAGE_EDEFAULT);
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
			case TypePackage.PROVIDED_INTERFACE_TYPE__PROVIDED_OPERATION_TYPES:
				return providedOperationTypes != null && !providedOperationTypes.isEmpty();
			case TypePackage.PROVIDED_INTERFACE_TYPE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case TypePackage.PROVIDED_INTERFACE_TYPE__SIGNATURE:
				return SIGNATURE_EDEFAULT == null ? signature != null : !SIGNATURE_EDEFAULT.equals(signature);
			case TypePackage.PROVIDED_INTERFACE_TYPE__PACKAGE:
				return PACKAGE_EDEFAULT == null ? package_ != null : !PACKAGE_EDEFAULT.equals(package_);
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
		result.append(" (name: ");
		result.append(name);
		result.append(", signature: ");
		result.append(signature);
		result.append(", package: ");
		result.append(package_);
		result.append(')');
		return result.toString();
	}

} //ProvidedInterfaceTypeImpl
