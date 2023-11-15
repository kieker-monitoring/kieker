/**
 */
package kieker.model.analysismodel.type.impl;

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

import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.ProvidedInterfaceType;
import kieker.model.analysismodel.type.TypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Provided Interface Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.type.impl.ProvidedInterfaceTypeImpl#getProvidedOperationTypes <em>Provided Operation Types</em>}</li>
 * <li>{@link kieker.model.analysismodel.type.impl.ProvidedInterfaceTypeImpl#getName <em>Name</em>}</li>
 * <li>{@link kieker.model.analysismodel.type.impl.ProvidedInterfaceTypeImpl#getSignature <em>Signature</em>}</li>
 * <li>{@link kieker.model.analysismodel.type.impl.ProvidedInterfaceTypeImpl#getPackage <em>Package</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProvidedInterfaceTypeImpl extends MinimalEObjectImpl.Container implements ProvidedInterfaceType {
	/**
	 * The cached value of the '{@link #getProvidedOperationTypes() <em>Provided Operation Types</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getProvidedOperationTypes()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, OperationType> providedOperationTypes;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected ProvidedInterfaceTypeImpl() {
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
		return TypePackage.Literals.PROVIDED_INTERFACE_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<String, OperationType> getProvidedOperationTypes() {
		if (this.providedOperationTypes == null) {
			this.providedOperationTypes = new EcoreEMap<>(TypePackage.Literals.INTERFACE_ESTRING_TO_OPERATION_TYPE_MAP_ENTRY,
					InterfaceEStringToOperationTypeMapEntryImpl.class, this, TypePackage.PROVIDED_INTERFACE_TYPE__PROVIDED_OPERATION_TYPES);
		}
		return this.providedOperationTypes;
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
			this.eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PROVIDED_INTERFACE_TYPE__NAME, oldName, this.name));
		}
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
			this.eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PROVIDED_INTERFACE_TYPE__SIGNATURE, oldSignature, this.signature));
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
			this.eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.PROVIDED_INTERFACE_TYPE__PACKAGE, oldPackage, this.package_));
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
		case TypePackage.PROVIDED_INTERFACE_TYPE__PROVIDED_OPERATION_TYPES:
			return ((InternalEList<?>) this.getProvidedOperationTypes()).basicRemove(otherEnd, msgs);
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
		case TypePackage.PROVIDED_INTERFACE_TYPE__PROVIDED_OPERATION_TYPES:
			if (coreType) {
				return this.getProvidedOperationTypes();
			} else {
				return this.getProvidedOperationTypes().map();
			}
		case TypePackage.PROVIDED_INTERFACE_TYPE__NAME:
			return this.getName();
		case TypePackage.PROVIDED_INTERFACE_TYPE__SIGNATURE:
			return this.getSignature();
		case TypePackage.PROVIDED_INTERFACE_TYPE__PACKAGE:
			return this.getPackage();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case TypePackage.PROVIDED_INTERFACE_TYPE__PROVIDED_OPERATION_TYPES:
			((EStructuralFeature.Setting) this.getProvidedOperationTypes()).set(newValue);
			return;
		case TypePackage.PROVIDED_INTERFACE_TYPE__NAME:
			this.setName((String) newValue);
			return;
		case TypePackage.PROVIDED_INTERFACE_TYPE__SIGNATURE:
			this.setSignature((String) newValue);
			return;
		case TypePackage.PROVIDED_INTERFACE_TYPE__PACKAGE:
			this.setPackage((String) newValue);
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
		case TypePackage.PROVIDED_INTERFACE_TYPE__PROVIDED_OPERATION_TYPES:
			this.getProvidedOperationTypes().clear();
			return;
		case TypePackage.PROVIDED_INTERFACE_TYPE__NAME:
			this.setName(NAME_EDEFAULT);
			return;
		case TypePackage.PROVIDED_INTERFACE_TYPE__SIGNATURE:
			this.setSignature(SIGNATURE_EDEFAULT);
			return;
		case TypePackage.PROVIDED_INTERFACE_TYPE__PACKAGE:
			this.setPackage(PACKAGE_EDEFAULT);
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
		case TypePackage.PROVIDED_INTERFACE_TYPE__PROVIDED_OPERATION_TYPES:
			return (this.providedOperationTypes != null) && !this.providedOperationTypes.isEmpty();
		case TypePackage.PROVIDED_INTERFACE_TYPE__NAME:
			return NAME_EDEFAULT == null ? this.name != null : !NAME_EDEFAULT.equals(this.name);
		case TypePackage.PROVIDED_INTERFACE_TYPE__SIGNATURE:
			return SIGNATURE_EDEFAULT == null ? this.signature != null : !SIGNATURE_EDEFAULT.equals(this.signature);
		case TypePackage.PROVIDED_INTERFACE_TYPE__PACKAGE:
			return PACKAGE_EDEFAULT == null ? this.package_ != null : !PACKAGE_EDEFAULT.equals(this.package_);
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
		result.append(" (name: ");
		result.append(this.name);
		result.append(", signature: ");
		result.append(this.signature);
		result.append(", package: ");
		result.append(this.package_);
		result.append(')');
		return result.toString();
	}

} // ProvidedInterfaceTypeImpl
