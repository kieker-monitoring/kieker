/**
 */
package kieker.model.analysismodel.assembly.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.AssemblyStorage;
import kieker.model.analysismodel.type.StorageType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Storage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyStorageImpl#getStorageType <em>Storage Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssemblyStorageImpl extends MinimalEObjectImpl.Container implements AssemblyStorage {
	/**
	 * The cached value of the '{@link #getStorageType() <em>Storage Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getStorageType()
	 * @generated
	 * @ordered
	 */
	protected StorageType storageType;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected AssemblyStorageImpl() {
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
		return AssemblyPackage.Literals.ASSEMBLY_STORAGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public StorageType getStorageType() {
		if ((this.storageType != null) && this.storageType.eIsProxy()) {
			final InternalEObject oldStorageType = (InternalEObject) this.storageType;
			this.storageType = (StorageType) this.eResolveProxy(oldStorageType);
			if (this.storageType != oldStorageType) {
				if (this.eNotificationRequired()) {
					this.eNotify(
							new ENotificationImpl(this, Notification.RESOLVE, AssemblyPackage.ASSEMBLY_STORAGE__STORAGE_TYPE, oldStorageType, this.storageType));
				}
			}
		}
		return this.storageType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public StorageType basicGetStorageType() {
		return this.storageType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setStorageType(final StorageType newStorageType) {
		final StorageType oldStorageType = this.storageType;
		this.storageType = newStorageType;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_STORAGE__STORAGE_TYPE, oldStorageType, this.storageType));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AssemblyComponent getComponent() {
		final org.eclipse.emf.ecore.EObject container = this.eContainer();
		if (container != null) {
			final org.eclipse.emf.ecore.EObject containerContainer = container.eContainer();
			if (containerContainer != null) {
				return (AssemblyComponent) containerContainer;
			}
		}
		return null;

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
		case AssemblyPackage.ASSEMBLY_STORAGE__STORAGE_TYPE:
			if (resolve) {
				return this.getStorageType();
			}
			return this.basicGetStorageType();
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
		case AssemblyPackage.ASSEMBLY_STORAGE__STORAGE_TYPE:
			this.setStorageType((StorageType) newValue);
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
		case AssemblyPackage.ASSEMBLY_STORAGE__STORAGE_TYPE:
			this.setStorageType((StorageType) null);
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
		case AssemblyPackage.ASSEMBLY_STORAGE__STORAGE_TYPE:
			return this.storageType != null;
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
	public Object eInvoke(final int operationID, final EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
		case AssemblyPackage.ASSEMBLY_STORAGE___GET_COMPONENT:
			return this.getComponent();
		}
		return super.eInvoke(operationID, arguments);
	}

} // AssemblyStorageImpl
