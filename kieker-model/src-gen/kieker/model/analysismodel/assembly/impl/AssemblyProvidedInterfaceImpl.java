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
import kieker.model.analysismodel.type.ProvidedInterfaceType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Provided Interface</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyProvidedInterfaceImpl#getProvidedInterfaceType <em>Provided Interface Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssemblyProvidedInterfaceImpl extends MinimalEObjectImpl.Container implements AssemblyProvidedInterface {
	/**
	 * The cached value of the '{@link #getProvidedInterfaceType() <em>Provided Interface Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getProvidedInterfaceType()
	 * @generated
	 * @ordered
	 */
	protected ProvidedInterfaceType providedInterfaceType;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected AssemblyProvidedInterfaceImpl() {
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
		return AssemblyPackage.Literals.ASSEMBLY_PROVIDED_INTERFACE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ProvidedInterfaceType getProvidedInterfaceType() {
		if ((this.providedInterfaceType != null) && this.providedInterfaceType.eIsProxy()) {
			final InternalEObject oldProvidedInterfaceType = (InternalEObject) this.providedInterfaceType;
			this.providedInterfaceType = (ProvidedInterfaceType) this.eResolveProxy(oldProvidedInterfaceType);
			if (this.providedInterfaceType != oldProvidedInterfaceType) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE,
							oldProvidedInterfaceType, this.providedInterfaceType));
				}
			}
		}
		return this.providedInterfaceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ProvidedInterfaceType basicGetProvidedInterfaceType() {
		return this.providedInterfaceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setProvidedInterfaceType(final ProvidedInterfaceType newProvidedInterfaceType) {
		final ProvidedInterfaceType oldProvidedInterfaceType = this.providedInterfaceType;
		this.providedInterfaceType = newProvidedInterfaceType;
		if (this.eNotificationRequired()) {
			this.eNotify(
					new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE, oldProvidedInterfaceType,
							this.providedInterfaceType));
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
		case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE:
			if (resolve) {
				return this.getProvidedInterfaceType();
			}
			return this.basicGetProvidedInterfaceType();
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
		case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE:
			this.setProvidedInterfaceType((ProvidedInterfaceType) newValue);
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
		case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE:
			this.setProvidedInterfaceType((ProvidedInterfaceType) null);
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
		case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE:
			return this.providedInterfaceType != null;
		}
		return super.eIsSet(featureID);
	}

} // AssemblyProvidedInterfaceImpl
