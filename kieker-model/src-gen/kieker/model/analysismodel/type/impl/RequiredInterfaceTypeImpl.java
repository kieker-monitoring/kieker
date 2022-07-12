/**
 */
package kieker.model.analysismodel.type.impl;

import kieker.model.analysismodel.type.ProvidedInterfaceType;
import kieker.model.analysismodel.type.RequiredInterfaceType;
import kieker.model.analysismodel.type.TypePackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Required Interface Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.type.impl.RequiredInterfaceTypeImpl#getRequires <em>Requires</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RequiredInterfaceTypeImpl extends MinimalEObjectImpl.Container implements RequiredInterfaceType {
	/**
	 * The cached value of the '{@link #getRequires() <em>Requires</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequires()
	 * @generated
	 * @ordered
	 */
	protected ProvidedInterfaceType requires;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RequiredInterfaceTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TypePackage.Literals.REQUIRED_INTERFACE_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProvidedInterfaceType getRequires() {
		if (requires != null && requires.eIsProxy()) {
			InternalEObject oldRequires = (InternalEObject)requires;
			requires = (ProvidedInterfaceType)eResolveProxy(oldRequires);
			if (requires != oldRequires) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TypePackage.REQUIRED_INTERFACE_TYPE__REQUIRES, oldRequires, requires));
			}
		}
		return requires;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProvidedInterfaceType basicGetRequires() {
		return requires;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRequires(ProvidedInterfaceType newRequires) {
		ProvidedInterfaceType oldRequires = requires;
		requires = newRequires;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.REQUIRED_INTERFACE_TYPE__REQUIRES, oldRequires, requires));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TypePackage.REQUIRED_INTERFACE_TYPE__REQUIRES:
				if (resolve) return getRequires();
				return basicGetRequires();
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
			case TypePackage.REQUIRED_INTERFACE_TYPE__REQUIRES:
				setRequires((ProvidedInterfaceType)newValue);
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
			case TypePackage.REQUIRED_INTERFACE_TYPE__REQUIRES:
				setRequires((ProvidedInterfaceType)null);
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
			case TypePackage.REQUIRED_INTERFACE_TYPE__REQUIRES:
				return requires != null;
		}
		return super.eIsSet(featureID);
	}

} //RequiredInterfaceTypeImpl
