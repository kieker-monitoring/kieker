/**
 */
package kieker.analysisteetime.model.analysismodel.deployment.impl;

import java.util.Collection;

import kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Context</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentContextImpl#getDeploymentRoot <em>Deployment Root</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentContextImpl#getComponents <em>Components</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeploymentContextImpl extends MinimalEObjectImpl.Container implements DeploymentContext {
	/**
	 * The cached value of the '{@link #getComponents() <em>Components</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComponents()
	 * @generated
	 * @ordered
	 */
	protected EList<DeployedComponent> components;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeploymentContextImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DeploymentPackage.Literals.DEPLOYMENT_CONTEXT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeploymentRoot getDeploymentRoot() {
		if (eContainerFeatureID() != DeploymentPackage.DEPLOYMENT_CONTEXT__DEPLOYMENT_ROOT) return null;
		return (DeploymentRoot)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDeploymentRoot(DeploymentRoot newDeploymentRoot, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newDeploymentRoot, DeploymentPackage.DEPLOYMENT_CONTEXT__DEPLOYMENT_ROOT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeploymentRoot(DeploymentRoot newDeploymentRoot) {
		if (newDeploymentRoot != eInternalContainer() || (eContainerFeatureID() != DeploymentPackage.DEPLOYMENT_CONTEXT__DEPLOYMENT_ROOT && newDeploymentRoot != null)) {
			if (EcoreUtil.isAncestor(this, newDeploymentRoot))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newDeploymentRoot != null)
				msgs = ((InternalEObject)newDeploymentRoot).eInverseAdd(this, DeploymentPackage.DEPLOYMENT_ROOT__DEPLOYMENT_CONTEXTS, DeploymentRoot.class, msgs);
			msgs = basicSetDeploymentRoot(newDeploymentRoot, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYMENT_CONTEXT__DEPLOYMENT_ROOT, newDeploymentRoot, newDeploymentRoot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DeployedComponent> getComponents() {
		if (components == null) {
			components = new EObjectContainmentWithInverseEList<DeployedComponent>(DeployedComponent.class, this, DeploymentPackage.DEPLOYMENT_CONTEXT__COMPONENTS, DeploymentPackage.DEPLOYED_COMPONENT__DEPLOYMENT_CONTEXT);
		}
		return components;
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
			case DeploymentPackage.DEPLOYMENT_CONTEXT__DEPLOYMENT_ROOT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetDeploymentRoot((DeploymentRoot)otherEnd, msgs);
			case DeploymentPackage.DEPLOYMENT_CONTEXT__COMPONENTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getComponents()).basicAdd(otherEnd, msgs);
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
			case DeploymentPackage.DEPLOYMENT_CONTEXT__DEPLOYMENT_ROOT:
				return basicSetDeploymentRoot(null, msgs);
			case DeploymentPackage.DEPLOYMENT_CONTEXT__COMPONENTS:
				return ((InternalEList<?>)getComponents()).basicRemove(otherEnd, msgs);
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
			case DeploymentPackage.DEPLOYMENT_CONTEXT__DEPLOYMENT_ROOT:
				return eInternalContainer().eInverseRemove(this, DeploymentPackage.DEPLOYMENT_ROOT__DEPLOYMENT_CONTEXTS, DeploymentRoot.class, msgs);
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
			case DeploymentPackage.DEPLOYMENT_CONTEXT__DEPLOYMENT_ROOT:
				return getDeploymentRoot();
			case DeploymentPackage.DEPLOYMENT_CONTEXT__COMPONENTS:
				return getComponents();
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
			case DeploymentPackage.DEPLOYMENT_CONTEXT__DEPLOYMENT_ROOT:
				setDeploymentRoot((DeploymentRoot)newValue);
				return;
			case DeploymentPackage.DEPLOYMENT_CONTEXT__COMPONENTS:
				getComponents().clear();
				getComponents().addAll((Collection<? extends DeployedComponent>)newValue);
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
			case DeploymentPackage.DEPLOYMENT_CONTEXT__DEPLOYMENT_ROOT:
				setDeploymentRoot((DeploymentRoot)null);
				return;
			case DeploymentPackage.DEPLOYMENT_CONTEXT__COMPONENTS:
				getComponents().clear();
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
			case DeploymentPackage.DEPLOYMENT_CONTEXT__DEPLOYMENT_ROOT:
				return getDeploymentRoot() != null;
			case DeploymentPackage.DEPLOYMENT_CONTEXT__COMPONENTS:
				return components != null && !components.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //DeploymentContextImpl
