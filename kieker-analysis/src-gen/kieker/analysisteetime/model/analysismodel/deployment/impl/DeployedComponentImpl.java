/**
 */
package kieker.analysisteetime.model.analysismodel.deployment.impl;

import java.util.Collection;

import kieker.analysisteetime.model.analysismodel.architecture.ComponentType;

import kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deployed Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedComponentImpl#getComponentType <em>Component Type</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedComponentImpl#getDeploymentContext <em>Deployment Context</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedComponentImpl#getContainedOperations <em>Contained Operations</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedComponentImpl#getAccessedOperations <em>Accessed Operations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeployedComponentImpl extends MinimalEObjectImpl.Container implements DeployedComponent {
	/**
	 * The cached value of the '{@link #getComponentType() <em>Component Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComponentType()
	 * @generated
	 * @ordered
	 */
	protected ComponentType componentType;

	/**
	 * The cached value of the '{@link #getDeploymentContext() <em>Deployment Context</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeploymentContext()
	 * @generated
	 * @ordered
	 */
	protected DeploymentContext deploymentContext;

	/**
	 * The cached value of the '{@link #getContainedOperations() <em>Contained Operations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainedOperations()
	 * @generated
	 * @ordered
	 */
	protected EList<DeployedOperation> containedOperations;

	/**
	 * The cached value of the '{@link #getAccessedOperations() <em>Accessed Operations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccessedOperations()
	 * @generated
	 * @ordered
	 */
	protected EList<DeployedOperation> accessedOperations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeployedComponentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DeploymentPackage.Literals.DEPLOYED_COMPONENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentType getComponentType() {
		if (componentType != null && componentType.eIsProxy()) {
			InternalEObject oldComponentType = (InternalEObject)componentType;
			componentType = (ComponentType)eResolveProxy(oldComponentType);
			if (componentType != oldComponentType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeploymentPackage.DEPLOYED_COMPONENT__COMPONENT_TYPE, oldComponentType, componentType));
			}
		}
		return componentType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentType basicGetComponentType() {
		return componentType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComponentType(ComponentType newComponentType) {
		ComponentType oldComponentType = componentType;
		componentType = newComponentType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_COMPONENT__COMPONENT_TYPE, oldComponentType, componentType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeploymentContext getDeploymentContext() {
		if (deploymentContext != null && deploymentContext.eIsProxy()) {
			InternalEObject oldDeploymentContext = (InternalEObject)deploymentContext;
			deploymentContext = (DeploymentContext)eResolveProxy(oldDeploymentContext);
			if (deploymentContext != oldDeploymentContext) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeploymentPackage.DEPLOYED_COMPONENT__DEPLOYMENT_CONTEXT, oldDeploymentContext, deploymentContext));
			}
		}
		return deploymentContext;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeploymentContext basicGetDeploymentContext() {
		return deploymentContext;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDeploymentContext(DeploymentContext newDeploymentContext, NotificationChain msgs) {
		DeploymentContext oldDeploymentContext = deploymentContext;
		deploymentContext = newDeploymentContext;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_COMPONENT__DEPLOYMENT_CONTEXT, oldDeploymentContext, newDeploymentContext);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeploymentContext(DeploymentContext newDeploymentContext) {
		if (newDeploymentContext != deploymentContext) {
			NotificationChain msgs = null;
			if (deploymentContext != null)
				msgs = ((InternalEObject)deploymentContext).eInverseRemove(this, DeploymentPackage.DEPLOYMENT_CONTEXT__COMPONENTS, DeploymentContext.class, msgs);
			if (newDeploymentContext != null)
				msgs = ((InternalEObject)newDeploymentContext).eInverseAdd(this, DeploymentPackage.DEPLOYMENT_CONTEXT__COMPONENTS, DeploymentContext.class, msgs);
			msgs = basicSetDeploymentContext(newDeploymentContext, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_COMPONENT__DEPLOYMENT_CONTEXT, newDeploymentContext, newDeploymentContext));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DeployedOperation> getContainedOperations() {
		if (containedOperations == null) {
			containedOperations = new EObjectWithInverseResolvingEList<DeployedOperation>(DeployedOperation.class, this, DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_OPERATIONS, DeploymentPackage.DEPLOYED_OPERATION__CONTAINED_COMPONENT);
		}
		return containedOperations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DeployedOperation> getAccessedOperations() {
		if (accessedOperations == null) {
			accessedOperations = new EObjectWithInverseResolvingEList<DeployedOperation>(DeployedOperation.class, this, DeploymentPackage.DEPLOYED_COMPONENT__ACCESSED_OPERATIONS, DeploymentPackage.DEPLOYED_OPERATION__ACCESSSED_COMPONENT);
		}
		return accessedOperations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DeploymentPackage.DEPLOYED_COMPONENT__DEPLOYMENT_CONTEXT:
				if (deploymentContext != null)
					msgs = ((InternalEObject)deploymentContext).eInverseRemove(this, DeploymentPackage.DEPLOYMENT_CONTEXT__COMPONENTS, DeploymentContext.class, msgs);
				return basicSetDeploymentContext((DeploymentContext)otherEnd, msgs);
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_OPERATIONS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getContainedOperations()).basicAdd(otherEnd, msgs);
			case DeploymentPackage.DEPLOYED_COMPONENT__ACCESSED_OPERATIONS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getAccessedOperations()).basicAdd(otherEnd, msgs);
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
			case DeploymentPackage.DEPLOYED_COMPONENT__DEPLOYMENT_CONTEXT:
				return basicSetDeploymentContext(null, msgs);
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_OPERATIONS:
				return ((InternalEList<?>)getContainedOperations()).basicRemove(otherEnd, msgs);
			case DeploymentPackage.DEPLOYED_COMPONENT__ACCESSED_OPERATIONS:
				return ((InternalEList<?>)getAccessedOperations()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DeploymentPackage.DEPLOYED_COMPONENT__COMPONENT_TYPE:
				if (resolve) return getComponentType();
				return basicGetComponentType();
			case DeploymentPackage.DEPLOYED_COMPONENT__DEPLOYMENT_CONTEXT:
				if (resolve) return getDeploymentContext();
				return basicGetDeploymentContext();
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_OPERATIONS:
				return getContainedOperations();
			case DeploymentPackage.DEPLOYED_COMPONENT__ACCESSED_OPERATIONS:
				return getAccessedOperations();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DeploymentPackage.DEPLOYED_COMPONENT__COMPONENT_TYPE:
				setComponentType((ComponentType)newValue);
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__DEPLOYMENT_CONTEXT:
				setDeploymentContext((DeploymentContext)newValue);
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_OPERATIONS:
				getContainedOperations().clear();
				getContainedOperations().addAll((Collection<? extends DeployedOperation>)newValue);
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__ACCESSED_OPERATIONS:
				getAccessedOperations().clear();
				getAccessedOperations().addAll((Collection<? extends DeployedOperation>)newValue);
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
			case DeploymentPackage.DEPLOYED_COMPONENT__COMPONENT_TYPE:
				setComponentType((ComponentType)null);
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__DEPLOYMENT_CONTEXT:
				setDeploymentContext((DeploymentContext)null);
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_OPERATIONS:
				getContainedOperations().clear();
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__ACCESSED_OPERATIONS:
				getAccessedOperations().clear();
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
			case DeploymentPackage.DEPLOYED_COMPONENT__COMPONENT_TYPE:
				return componentType != null;
			case DeploymentPackage.DEPLOYED_COMPONENT__DEPLOYMENT_CONTEXT:
				return deploymentContext != null;
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_OPERATIONS:
				return containedOperations != null && !containedOperations.isEmpty();
			case DeploymentPackage.DEPLOYED_COMPONENT__ACCESSED_OPERATIONS:
				return accessedOperations != null && !accessedOperations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //DeployedComponentImpl
