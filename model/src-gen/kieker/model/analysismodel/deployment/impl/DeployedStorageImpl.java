/**
 */
package kieker.model.analysismodel.deployment.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.assembly.AssemblyStorage;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deployed Storage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.deployment.impl.DeployedStorageImpl#getAssemblyStorage <em>Assembly Storage</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeployedStorageImpl extends MinimalEObjectImpl.Container implements DeployedStorage {
	/**
	 * The cached value of the '{@link #getAssemblyStorage() <em>Assembly Storage</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getAssemblyStorage()
	 * @generated
	 * @ordered
	 */
	protected AssemblyStorage assemblyStorage;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected DeployedStorageImpl() {
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
		return DeploymentPackage.Literals.DEPLOYED_STORAGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AssemblyStorage getAssemblyStorage() {
		if ((this.assemblyStorage != null) && this.assemblyStorage.eIsProxy()) {
			final InternalEObject oldAssemblyStorage = (InternalEObject) this.assemblyStorage;
			this.assemblyStorage = (AssemblyStorage) this.eResolveProxy(oldAssemblyStorage);
			if (this.assemblyStorage != oldAssemblyStorage) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeploymentPackage.DEPLOYED_STORAGE__ASSEMBLY_STORAGE, oldAssemblyStorage,
							this.assemblyStorage));
				}
			}
		}
		return this.assemblyStorage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public AssemblyStorage basicGetAssemblyStorage() {
		return this.assemblyStorage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setAssemblyStorage(final AssemblyStorage newAssemblyStorage) {
		final AssemblyStorage oldAssemblyStorage = this.assemblyStorage;
		this.assemblyStorage = newAssemblyStorage;
		if (this.eNotificationRequired()) {
			this.eNotify(
					new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_STORAGE__ASSEMBLY_STORAGE, oldAssemblyStorage, this.assemblyStorage));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeployedComponent getComponent() {
		final org.eclipse.emf.ecore.EObject container = this.eContainer();
		if (container != null) {
			final org.eclipse.emf.ecore.EObject containerContainer = container.eContainer();
			if (containerContainer != null) {
				return (DeployedComponent) containerContainer;
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
		case DeploymentPackage.DEPLOYED_STORAGE__ASSEMBLY_STORAGE:
			if (resolve) {
				return this.getAssemblyStorage();
			}
			return this.basicGetAssemblyStorage();
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
		case DeploymentPackage.DEPLOYED_STORAGE__ASSEMBLY_STORAGE:
			this.setAssemblyStorage((AssemblyStorage) newValue);
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
		case DeploymentPackage.DEPLOYED_STORAGE__ASSEMBLY_STORAGE:
			this.setAssemblyStorage((AssemblyStorage) null);
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
		case DeploymentPackage.DEPLOYED_STORAGE__ASSEMBLY_STORAGE:
			return this.assemblyStorage != null;
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
		case DeploymentPackage.DEPLOYED_STORAGE___GET_COMPONENT:
			return this.getComponent();
		}
		return super.eInvoke(operationID, arguments);
	}

} // DeployedStorageImpl
