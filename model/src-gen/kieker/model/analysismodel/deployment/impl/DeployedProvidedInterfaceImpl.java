/**
 */
package kieker.model.analysismodel.deployment.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.assembly.AssemblyProvidedInterface;
import kieker.model.analysismodel.deployment.DeployedProvidedInterface;
import kieker.model.analysismodel.deployment.DeploymentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deployed Provided Interface</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.deployment.impl.DeployedProvidedInterfaceImpl#getProvidedInterface <em>Provided Interface</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeployedProvidedInterfaceImpl extends MinimalEObjectImpl.Container implements DeployedProvidedInterface {
	/**
	 * The cached value of the '{@link #getProvidedInterface() <em>Provided Interface</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getProvidedInterface()
	 * @generated
	 * @ordered
	 */
	protected AssemblyProvidedInterface providedInterface;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected DeployedProvidedInterfaceImpl() {
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
		return DeploymentPackage.Literals.DEPLOYED_PROVIDED_INTERFACE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AssemblyProvidedInterface getProvidedInterface() {
		if ((this.providedInterface != null) && this.providedInterface.eIsProxy()) {
			final InternalEObject oldProvidedInterface = (InternalEObject) this.providedInterface;
			this.providedInterface = (AssemblyProvidedInterface) this.eResolveProxy(oldProvidedInterface);
			if (this.providedInterface != oldProvidedInterface) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeploymentPackage.DEPLOYED_PROVIDED_INTERFACE__PROVIDED_INTERFACE,
							oldProvidedInterface, this.providedInterface));
				}
			}
		}
		return this.providedInterface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public AssemblyProvidedInterface basicGetProvidedInterface() {
		return this.providedInterface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setProvidedInterface(final AssemblyProvidedInterface newProvidedInterface) {
		final AssemblyProvidedInterface oldProvidedInterface = this.providedInterface;
		this.providedInterface = newProvidedInterface;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_PROVIDED_INTERFACE__PROVIDED_INTERFACE, oldProvidedInterface,
					this.providedInterface));
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
		case DeploymentPackage.DEPLOYED_PROVIDED_INTERFACE__PROVIDED_INTERFACE:
			if (resolve) {
				return this.getProvidedInterface();
			}
			return this.basicGetProvidedInterface();
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
		case DeploymentPackage.DEPLOYED_PROVIDED_INTERFACE__PROVIDED_INTERFACE:
			this.setProvidedInterface((AssemblyProvidedInterface) newValue);
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
		case DeploymentPackage.DEPLOYED_PROVIDED_INTERFACE__PROVIDED_INTERFACE:
			this.setProvidedInterface((AssemblyProvidedInterface) null);
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
		case DeploymentPackage.DEPLOYED_PROVIDED_INTERFACE__PROVIDED_INTERFACE:
			return this.providedInterface != null;
		}
		return super.eIsSet(featureID);
	}

} // DeployedProvidedInterfaceImpl
