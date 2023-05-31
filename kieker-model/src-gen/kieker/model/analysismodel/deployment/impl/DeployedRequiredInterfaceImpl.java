/**
 */
package kieker.model.analysismodel.deployment.impl;

import kieker.model.analysismodel.assembly.AssemblyRequiredInterface;

import kieker.model.analysismodel.deployment.DeployedProvidedInterface;
import kieker.model.analysismodel.deployment.DeployedRequiredInterface;
import kieker.model.analysismodel.deployment.DeploymentPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deployed Required Interface</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.deployment.impl.DeployedRequiredInterfaceImpl#getRequiredInterface <em>Required Interface</em>}</li>
 *   <li>{@link kieker.model.analysismodel.deployment.impl.DeployedRequiredInterfaceImpl#getRequires <em>Requires</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeployedRequiredInterfaceImpl extends MinimalEObjectImpl.Container implements DeployedRequiredInterface {
	/**
	 * The cached value of the '{@link #getRequiredInterface() <em>Required Interface</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequiredInterface()
	 * @generated
	 * @ordered
	 */
	protected AssemblyRequiredInterface requiredInterface;

	/**
	 * The cached value of the '{@link #getRequires() <em>Requires</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequires()
	 * @generated
	 * @ordered
	 */
	protected DeployedProvidedInterface requires;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeployedRequiredInterfaceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DeploymentPackage.Literals.DEPLOYED_REQUIRED_INTERFACE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AssemblyRequiredInterface getRequiredInterface() {
		if (requiredInterface != null && requiredInterface.eIsProxy()) {
			InternalEObject oldRequiredInterface = (InternalEObject)requiredInterface;
			requiredInterface = (AssemblyRequiredInterface)eResolveProxy(oldRequiredInterface);
			if (requiredInterface != oldRequiredInterface) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRED_INTERFACE, oldRequiredInterface, requiredInterface));
			}
		}
		return requiredInterface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssemblyRequiredInterface basicGetRequiredInterface() {
		return requiredInterface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRequiredInterface(AssemblyRequiredInterface newRequiredInterface) {
		AssemblyRequiredInterface oldRequiredInterface = requiredInterface;
		requiredInterface = newRequiredInterface;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRED_INTERFACE, oldRequiredInterface, requiredInterface));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DeployedProvidedInterface getRequires() {
		if (requires != null && requires.eIsProxy()) {
			InternalEObject oldRequires = (InternalEObject)requires;
			requires = (DeployedProvidedInterface)eResolveProxy(oldRequires);
			if (requires != oldRequires) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRES, oldRequires, requires));
			}
		}
		return requires;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeployedProvidedInterface basicGetRequires() {
		return requires;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRequires(DeployedProvidedInterface newRequires) {
		DeployedProvidedInterface oldRequires = requires;
		requires = newRequires;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRES, oldRequires, requires));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRED_INTERFACE:
				if (resolve) return getRequiredInterface();
				return basicGetRequiredInterface();
			case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRES:
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
			case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRED_INTERFACE:
				setRequiredInterface((AssemblyRequiredInterface)newValue);
				return;
			case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRES:
				setRequires((DeployedProvidedInterface)newValue);
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
			case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRED_INTERFACE:
				setRequiredInterface((AssemblyRequiredInterface)null);
				return;
			case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRES:
				setRequires((DeployedProvidedInterface)null);
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
			case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRED_INTERFACE:
				return requiredInterface != null;
			case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE__REQUIRES:
				return requires != null;
		}
		return super.eIsSet(featureID);
	}

} //DeployedRequiredInterfaceImpl
