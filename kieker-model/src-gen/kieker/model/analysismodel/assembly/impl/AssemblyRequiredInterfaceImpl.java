/**
 */
package kieker.model.analysismodel.assembly.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.AssemblyProvidedInterface;
import kieker.model.analysismodel.assembly.AssemblyRequiredInterface;
import kieker.model.analysismodel.type.RequiredInterfaceType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Required Interface</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyRequiredInterfaceImpl#getRequires <em>Requires</em>}</li>
 * <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyRequiredInterfaceImpl#getRequiredInterfaceType <em>Required Interface Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssemblyRequiredInterfaceImpl extends MinimalEObjectImpl.Container implements AssemblyRequiredInterface {
	/**
	 * The cached value of the '{@link #getRequires() <em>Requires</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getRequires()
	 * @generated
	 * @ordered
	 */
	protected AssemblyProvidedInterface requires;

	/**
	 * The cached value of the '{@link #getRequiredInterfaceType() <em>Required Interface Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getRequiredInterfaceType()
	 * @generated
	 * @ordered
	 */
	protected RequiredInterfaceType requiredInterfaceType;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected AssemblyRequiredInterfaceImpl() {
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
		return AssemblyPackage.Literals.ASSEMBLY_REQUIRED_INTERFACE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AssemblyProvidedInterface getRequires() {
		if ((this.requires != null) && this.requires.eIsProxy()) {
			final InternalEObject oldRequires = (InternalEObject) this.requires;
			this.requires = (AssemblyProvidedInterface) this.eResolveProxy(oldRequires);
			if (this.requires != oldRequires) {
				if (this.eNotificationRequired()) {
					this.eNotify(
							new ENotificationImpl(this, Notification.RESOLVE, AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRES, oldRequires, this.requires));
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
	public AssemblyProvidedInterface basicGetRequires() {
		return this.requires;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setRequires(final AssemblyProvidedInterface newRequires) {
		final AssemblyProvidedInterface oldRequires = this.requires;
		this.requires = newRequires;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRES, oldRequires, this.requires));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public RequiredInterfaceType getRequiredInterfaceType() {
		if ((this.requiredInterfaceType != null) && this.requiredInterfaceType.eIsProxy()) {
			final InternalEObject oldRequiredInterfaceType = (InternalEObject) this.requiredInterfaceType;
			this.requiredInterfaceType = (RequiredInterfaceType) this.eResolveProxy(oldRequiredInterfaceType);
			if (this.requiredInterfaceType != oldRequiredInterfaceType) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRED_INTERFACE_TYPE,
							oldRequiredInterfaceType, this.requiredInterfaceType));
				}
			}
		}
		return this.requiredInterfaceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public RequiredInterfaceType basicGetRequiredInterfaceType() {
		return this.requiredInterfaceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setRequiredInterfaceType(final RequiredInterfaceType newRequiredInterfaceType) {
		final RequiredInterfaceType oldRequiredInterfaceType = this.requiredInterfaceType;
		this.requiredInterfaceType = newRequiredInterfaceType;
		if (this.eNotificationRequired()) {
			this.eNotify(
					new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRED_INTERFACE_TYPE, oldRequiredInterfaceType,
							this.requiredInterfaceType));
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
		case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRES:
			if (resolve) {
				return this.getRequires();
			}
			return this.basicGetRequires();
		case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRED_INTERFACE_TYPE:
			if (resolve) {
				return this.getRequiredInterfaceType();
			}
			return this.basicGetRequiredInterfaceType();
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
		case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRES:
			this.setRequires((AssemblyProvidedInterface) newValue);
			return;
		case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRED_INTERFACE_TYPE:
			this.setRequiredInterfaceType((RequiredInterfaceType) newValue);
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
		case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRES:
			this.setRequires((AssemblyProvidedInterface) null);
			return;
		case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRED_INTERFACE_TYPE:
			this.setRequiredInterfaceType((RequiredInterfaceType) null);
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
		case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRES:
			return this.requires != null;
		case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRED_INTERFACE_TYPE:
			return this.requiredInterfaceType != null;
		}
		return super.eIsSet(featureID);
	}

} // AssemblyRequiredInterfaceImpl
