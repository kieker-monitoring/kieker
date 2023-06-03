/**
 */
package kieker.model.analysismodel.assembly.impl;

import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.AssemblyProvidedInterface;

import kieker.model.analysismodel.type.ProvidedInterfaceType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Provided Interface</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyProvidedInterfaceImpl#getProvidedInterfaceType <em>Provided Interface Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssemblyProvidedInterfaceImpl extends MinimalEObjectImpl.Container implements AssemblyProvidedInterface {
	/**
	 * The cached value of the '{@link #getProvidedInterfaceType() <em>Provided Interface Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProvidedInterfaceType()
	 * @generated
	 * @ordered
	 */
	protected ProvidedInterfaceType providedInterfaceType;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AssemblyProvidedInterfaceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AssemblyPackage.Literals.ASSEMBLY_PROVIDED_INTERFACE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProvidedInterfaceType getProvidedInterfaceType() {
		if (providedInterfaceType != null && providedInterfaceType.eIsProxy()) {
			InternalEObject oldProvidedInterfaceType = (InternalEObject)providedInterfaceType;
			providedInterfaceType = (ProvidedInterfaceType)eResolveProxy(oldProvidedInterfaceType);
			if (providedInterfaceType != oldProvidedInterfaceType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE, oldProvidedInterfaceType, providedInterfaceType));
			}
		}
		return providedInterfaceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProvidedInterfaceType basicGetProvidedInterfaceType() {
		return providedInterfaceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProvidedInterfaceType(ProvidedInterfaceType newProvidedInterfaceType) {
		ProvidedInterfaceType oldProvidedInterfaceType = providedInterfaceType;
		providedInterfaceType = newProvidedInterfaceType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE, oldProvidedInterfaceType, providedInterfaceType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE:
				if (resolve) return getProvidedInterfaceType();
				return basicGetProvidedInterfaceType();
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
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE:
				setProvidedInterfaceType((ProvidedInterfaceType)newValue);
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
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE:
				setProvidedInterfaceType((ProvidedInterfaceType)null);
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
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE:
				return providedInterfaceType != null;
		}
		return super.eIsSet(featureID);
	}

} //AssemblyProvidedInterfaceImpl
