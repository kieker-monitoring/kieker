/**
 */
package kieker.analysisteetime.model.analysismodel.deployment.impl;

import kieker.analysisteetime.model.analysismodel.architecture.OperationType;

import kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deployed Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedOperationImpl#getOperationType <em>Operation Type</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedOperationImpl#getContainedComponent <em>Contained Component</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedOperationImpl#getAccesssedComponent <em>Accesssed Component</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeployedOperationImpl extends MinimalEObjectImpl.Container implements DeployedOperation {
	/**
	 * The cached value of the '{@link #getOperationType() <em>Operation Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationType()
	 * @generated
	 * @ordered
	 */
	protected OperationType operationType;

	/**
	 * The cached value of the '{@link #getAccesssedComponent() <em>Accesssed Component</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccesssedComponent()
	 * @generated
	 * @ordered
	 */
	protected DeployedComponent accesssedComponent;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeployedOperationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DeploymentPackage.Literals.DEPLOYED_OPERATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationType getOperationType() {
		if (operationType != null && operationType.eIsProxy()) {
			InternalEObject oldOperationType = (InternalEObject)operationType;
			operationType = (OperationType)eResolveProxy(oldOperationType);
			if (operationType != oldOperationType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeploymentPackage.DEPLOYED_OPERATION__OPERATION_TYPE, oldOperationType, operationType));
			}
		}
		return operationType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationType basicGetOperationType() {
		return operationType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOperationType(OperationType newOperationType) {
		OperationType oldOperationType = operationType;
		operationType = newOperationType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_OPERATION__OPERATION_TYPE, oldOperationType, operationType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeployedComponent getContainedComponent() {
		if (eContainerFeatureID() != DeploymentPackage.DEPLOYED_OPERATION__CONTAINED_COMPONENT) return null;
		return (DeployedComponent)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetContainedComponent(DeployedComponent newContainedComponent, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newContainedComponent, DeploymentPackage.DEPLOYED_OPERATION__CONTAINED_COMPONENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContainedComponent(DeployedComponent newContainedComponent) {
		if (newContainedComponent != eInternalContainer() || (eContainerFeatureID() != DeploymentPackage.DEPLOYED_OPERATION__CONTAINED_COMPONENT && newContainedComponent != null)) {
			if (EcoreUtil.isAncestor(this, newContainedComponent))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newContainedComponent != null)
				msgs = ((InternalEObject)newContainedComponent).eInverseAdd(this, DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_OPERATIONS, DeployedComponent.class, msgs);
			msgs = basicSetContainedComponent(newContainedComponent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_OPERATION__CONTAINED_COMPONENT, newContainedComponent, newContainedComponent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeployedComponent getAccesssedComponent() {
		if (accesssedComponent != null && accesssedComponent.eIsProxy()) {
			InternalEObject oldAccesssedComponent = (InternalEObject)accesssedComponent;
			accesssedComponent = (DeployedComponent)eResolveProxy(oldAccesssedComponent);
			if (accesssedComponent != oldAccesssedComponent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeploymentPackage.DEPLOYED_OPERATION__ACCESSSED_COMPONENT, oldAccesssedComponent, accesssedComponent));
			}
		}
		return accesssedComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeployedComponent basicGetAccesssedComponent() {
		return accesssedComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAccesssedComponent(DeployedComponent newAccesssedComponent, NotificationChain msgs) {
		DeployedComponent oldAccesssedComponent = accesssedComponent;
		accesssedComponent = newAccesssedComponent;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_OPERATION__ACCESSSED_COMPONENT, oldAccesssedComponent, newAccesssedComponent);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAccesssedComponent(DeployedComponent newAccesssedComponent) {
		if (newAccesssedComponent != accesssedComponent) {
			NotificationChain msgs = null;
			if (accesssedComponent != null)
				msgs = ((InternalEObject)accesssedComponent).eInverseRemove(this, DeploymentPackage.DEPLOYED_COMPONENT__ACCESSED_OPERATIONS, DeployedComponent.class, msgs);
			if (newAccesssedComponent != null)
				msgs = ((InternalEObject)newAccesssedComponent).eInverseAdd(this, DeploymentPackage.DEPLOYED_COMPONENT__ACCESSED_OPERATIONS, DeployedComponent.class, msgs);
			msgs = basicSetAccesssedComponent(newAccesssedComponent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_OPERATION__ACCESSSED_COMPONENT, newAccesssedComponent, newAccesssedComponent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DeploymentPackage.DEPLOYED_OPERATION__CONTAINED_COMPONENT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetContainedComponent((DeployedComponent)otherEnd, msgs);
			case DeploymentPackage.DEPLOYED_OPERATION__ACCESSSED_COMPONENT:
				if (accesssedComponent != null)
					msgs = ((InternalEObject)accesssedComponent).eInverseRemove(this, DeploymentPackage.DEPLOYED_COMPONENT__ACCESSED_OPERATIONS, DeployedComponent.class, msgs);
				return basicSetAccesssedComponent((DeployedComponent)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DeploymentPackage.DEPLOYED_OPERATION__CONTAINED_COMPONENT:
				return basicSetContainedComponent(null, msgs);
			case DeploymentPackage.DEPLOYED_OPERATION__ACCESSSED_COMPONENT:
				return basicSetAccesssedComponent(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case DeploymentPackage.DEPLOYED_OPERATION__CONTAINED_COMPONENT:
				return eInternalContainer().eInverseRemove(this, DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_OPERATIONS, DeployedComponent.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DeploymentPackage.DEPLOYED_OPERATION__OPERATION_TYPE:
				if (resolve) return getOperationType();
				return basicGetOperationType();
			case DeploymentPackage.DEPLOYED_OPERATION__CONTAINED_COMPONENT:
				return getContainedComponent();
			case DeploymentPackage.DEPLOYED_OPERATION__ACCESSSED_COMPONENT:
				if (resolve) return getAccesssedComponent();
				return basicGetAccesssedComponent();
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
			case DeploymentPackage.DEPLOYED_OPERATION__OPERATION_TYPE:
				setOperationType((OperationType)newValue);
				return;
			case DeploymentPackage.DEPLOYED_OPERATION__CONTAINED_COMPONENT:
				setContainedComponent((DeployedComponent)newValue);
				return;
			case DeploymentPackage.DEPLOYED_OPERATION__ACCESSSED_COMPONENT:
				setAccesssedComponent((DeployedComponent)newValue);
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
			case DeploymentPackage.DEPLOYED_OPERATION__OPERATION_TYPE:
				setOperationType((OperationType)null);
				return;
			case DeploymentPackage.DEPLOYED_OPERATION__CONTAINED_COMPONENT:
				setContainedComponent((DeployedComponent)null);
				return;
			case DeploymentPackage.DEPLOYED_OPERATION__ACCESSSED_COMPONENT:
				setAccesssedComponent((DeployedComponent)null);
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
			case DeploymentPackage.DEPLOYED_OPERATION__OPERATION_TYPE:
				return operationType != null;
			case DeploymentPackage.DEPLOYED_OPERATION__CONTAINED_COMPONENT:
				return getContainedComponent() != null;
			case DeploymentPackage.DEPLOYED_OPERATION__ACCESSSED_COMPONENT:
				return accesssedComponent != null;
		}
		return super.eIsSet(featureID);
	}

} //DeployedOperationImpl
