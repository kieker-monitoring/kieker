/**
 */
package kieker.model.analysismodel.assembly.impl;

import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.AssemblyProvidedInterface;
import kieker.model.analysismodel.assembly.AssemblyRequiredInterface;

import kieker.model.analysismodel.type.RequiredInterfaceType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Required Interface</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyRequiredInterfaceImpl#getRequires <em>Requires</em>}</li>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyRequiredInterfaceImpl#getRequiredInterfaceType <em>Required Interface Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssemblyRequiredInterfaceImpl extends MinimalEObjectImpl.Container implements AssemblyRequiredInterface {
	/**
	 * The cached value of the '{@link #getRequires() <em>Requires</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequires()
	 * @generated
	 * @ordered
	 */
	protected AssemblyProvidedInterface requires;

	/**
	 * The cached value of the '{@link #getRequiredInterfaceType() <em>Required Interface Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequiredInterfaceType()
	 * @generated
	 * @ordered
	 */
	protected RequiredInterfaceType requiredInterfaceType;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AssemblyRequiredInterfaceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AssemblyPackage.Literals.ASSEMBLY_REQUIRED_INTERFACE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AssemblyProvidedInterface getRequires() {
		if (requires != null && requires.eIsProxy()) {
			InternalEObject oldRequires = (InternalEObject)requires;
			requires = (AssemblyProvidedInterface)eResolveProxy(oldRequires);
			if (requires != oldRequires) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRES, oldRequires, requires));
			}
		}
		return requires;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssemblyProvidedInterface basicGetRequires() {
		return requires;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRequires(AssemblyProvidedInterface newRequires) {
		AssemblyProvidedInterface oldRequires = requires;
		requires = newRequires;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRES, oldRequires, requires));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RequiredInterfaceType getRequiredInterfaceType() {
		if (requiredInterfaceType != null && requiredInterfaceType.eIsProxy()) {
			InternalEObject oldRequiredInterfaceType = (InternalEObject)requiredInterfaceType;
			requiredInterfaceType = (RequiredInterfaceType)eResolveProxy(oldRequiredInterfaceType);
			if (requiredInterfaceType != oldRequiredInterfaceType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRED_INTERFACE_TYPE, oldRequiredInterfaceType, requiredInterfaceType));
			}
		}
		return requiredInterfaceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RequiredInterfaceType basicGetRequiredInterfaceType() {
		return requiredInterfaceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRequiredInterfaceType(RequiredInterfaceType newRequiredInterfaceType) {
		RequiredInterfaceType oldRequiredInterfaceType = requiredInterfaceType;
		requiredInterfaceType = newRequiredInterfaceType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRED_INTERFACE_TYPE, oldRequiredInterfaceType, requiredInterfaceType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRES:
				if (resolve) return getRequires();
				return basicGetRequires();
			case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRED_INTERFACE_TYPE:
				if (resolve) return getRequiredInterfaceType();
				return basicGetRequiredInterfaceType();
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
			case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRES:
				setRequires((AssemblyProvidedInterface)newValue);
				return;
			case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRED_INTERFACE_TYPE:
				setRequiredInterfaceType((RequiredInterfaceType)newValue);
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
			case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRES:
				setRequires((AssemblyProvidedInterface)null);
				return;
			case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRED_INTERFACE_TYPE:
				setRequiredInterfaceType((RequiredInterfaceType)null);
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
			case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRES:
				return requires != null;
			case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE__REQUIRED_INTERFACE_TYPE:
				return requiredInterfaceType != null;
		}
		return super.eIsSet(featureID);
	}

} //AssemblyRequiredInterfaceImpl
