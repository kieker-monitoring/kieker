/**
 */
package kieker.model.analysismodel.type.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.type.ProvidedInterfaceType;
import kieker.model.analysismodel.type.RequiredInterfaceType;
import kieker.model.analysismodel.type.TypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Required Interface Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.type.impl.RequiredInterfaceTypeImpl#getRequires <em>Requires</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RequiredInterfaceTypeImpl extends MinimalEObjectImpl.Container implements RequiredInterfaceType {
	/**
	 * The cached value of the '{@link #getRequires() <em>Requires</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getRequires()
	 * @generated
	 * @ordered
	 */
	protected ProvidedInterfaceType requires;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected RequiredInterfaceTypeImpl() {
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
		return TypePackage.Literals.REQUIRED_INTERFACE_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ProvidedInterfaceType getRequires() {
		if ((this.requires != null) && this.requires.eIsProxy()) {
			final InternalEObject oldRequires = (InternalEObject) this.requires;
			this.requires = (ProvidedInterfaceType) this.eResolveProxy(oldRequires);
			if (this.requires != oldRequires) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, TypePackage.REQUIRED_INTERFACE_TYPE__REQUIRES, oldRequires, this.requires));
				}
			}
		}
		return this.requires;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ProvidedInterfaceType basicGetRequires() {
		return this.requires;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setRequires(final ProvidedInterfaceType newRequires) {
		final ProvidedInterfaceType oldRequires = this.requires;
		this.requires = newRequires;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.REQUIRED_INTERFACE_TYPE__REQUIRES, oldRequires, this.requires));
		}
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
		case TypePackage.REQUIRED_INTERFACE_TYPE__REQUIRES:
			if (resolve) {
				return this.getRequires();
			}
			return this.basicGetRequires();
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
		case TypePackage.REQUIRED_INTERFACE_TYPE__REQUIRES:
			this.setRequires((ProvidedInterfaceType) newValue);
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
		case TypePackage.REQUIRED_INTERFACE_TYPE__REQUIRES:
			this.setRequires((ProvidedInterfaceType) null);
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
		case TypePackage.REQUIRED_INTERFACE_TYPE__REQUIRES:
			return this.requires != null;
		}
		return super.eIsSet(featureID);
	}

} // RequiredInterfaceTypeImpl
