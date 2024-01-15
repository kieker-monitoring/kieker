/**
 */
package kieker.model.analysismodel.deployment.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.assembly.AssemblyRequiredInterface;
import kieker.model.analysismodel.deployment.DeployedProvidedInterface;
import kieker.model.analysismodel.deployment.DeployedRequiredInterface;
import kieker.model.analysismodel.deployment.DeploymentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deployed Required Interface</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.deployment.impl.DeployedRequiredInterfaceImpl#getRequiredInterface <em>Required Interface</em>}</li>
 * <li>{@link kieker.model.analysismodel.deployment.impl.DeployedRequiredInterfaceImpl#getRequires <em>Requires</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeployedRequiredInterfaceImpl extends MinimalEObjectImpl.Container implements DeployedRequiredInterface {
	/**
	 * The cached value of the '{@link #getRequiredInterface() <em>Required Interface</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getRequiredInterface()
	 * @generated
	 * @ordered
	 */
	protected AssemblyRequiredInterface requiredInterface;

	/**
	 * The cached value of the '{@link #getRequires() <em>Requires</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getRequires()
	 * @generated
	 * @ordered
	 */
	protected DeployedProvidedInterface requires;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected DeployedRequiredInterfaceImpl() {
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
		return DeploymentPackage.Literals.DEPLOYED_REQUIRED_INTERFACE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AssemblyRequiredInterface getRequiredInterface() {
		if ((this.requiredInterface != null) && this.requiredInterface.eIsProxy()) {
			final InternalEObject oldRequiredInterface = (InternalEObject) this.requiredInterface;
			this.requiredInterface = (AssemblyRequiredInterface) this.eResolveProxy(oldRequiredInterface);
			if (this.requiredInterface != oldRequiredInterface) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRED_INTERFACE,
							oldRequiredInterface, this.requiredInterface));
				}
			}
		}
		return this.requiredInterface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public AssemblyRequiredInterface basicGetRequiredInterface() {
		return this.requiredInterface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setRequiredInterface(final AssemblyRequiredInterface newRequiredInterface) {
		final AssemblyRequiredInterface oldRequiredInterface = this.requiredInterface;
		this.requiredInterface = newRequiredInterface;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRED_INTERFACE, oldRequiredInterface,
					this.requiredInterface));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeployedProvidedInterface getRequires() {
		if ((this.requires != null) && this.requires.eIsProxy()) {
			final InternalEObject oldRequires = (InternalEObject) this.requires;
			this.requires = (DeployedProvidedInterface) this.eResolveProxy(oldRequires);
			if (this.requires != oldRequires) {
				if (this.eNotificationRequired()) {
					this.eNotify(
							new ENotificationImpl(this, Notification.RESOLVE, DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRES, oldRequires, this.requires));
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
	public DeployedProvidedInterface basicGetRequires() {
		return this.requires;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setRequires(final DeployedProvidedInterface newRequires) {
		final DeployedProvidedInterface oldRequires = this.requires;
		this.requires = newRequires;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRES, oldRequires, this.requires));
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
		case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRED_INTERFACE:
			if (resolve) {
				return this.getRequiredInterface();
			}
			return this.basicGetRequiredInterface();
		case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRES:
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
		case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRED_INTERFACE:
			this.setRequiredInterface((AssemblyRequiredInterface) newValue);
			return;
		case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRES:
			this.setRequires((DeployedProvidedInterface) newValue);
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
		case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRED_INTERFACE:
			this.setRequiredInterface((AssemblyRequiredInterface) null);
			return;
		case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRES:
			this.setRequires((DeployedProvidedInterface) null);
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
		case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRED_INTERFACE:
			return this.requiredInterface != null;
		case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRES:
			return this.requires != null;
		}
		return super.eIsSet(featureID);
	}

} // DeployedRequiredInterfaceImpl
