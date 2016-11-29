/**
 */
package kieker.analysisteetime.model.analysismodel.deployment.impl;

import java.util.Collection;

import kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeploymentRootImpl#getDeploymentContexts <em>Deployment Contexts</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeploymentRootImpl extends MinimalEObjectImpl.Container implements DeploymentRoot {
	/**
	 * The cached value of the '{@link #getDeploymentContexts() <em>Deployment Contexts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeploymentContexts()
	 * @generated
	 * @ordered
	 */
	protected EList<DeploymentContext> deploymentContexts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeploymentRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DeploymentPackage.Literals.DEPLOYMENT_ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DeploymentContext> getDeploymentContexts() {
		if (deploymentContexts == null) {
			deploymentContexts = new EObjectContainmentWithInverseEList<DeploymentContext>(DeploymentContext.class, this, DeploymentPackage.DEPLOYMENT_ROOT__DEPLOYMENT_CONTEXTS, DeploymentPackage.DEPLOYMENT_CONTEXT__DEPLOYMENT_ROOT);
		}
		return deploymentContexts;
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
			case DeploymentPackage.DEPLOYMENT_ROOT__DEPLOYMENT_CONTEXTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getDeploymentContexts()).basicAdd(otherEnd, msgs);
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
			case DeploymentPackage.DEPLOYMENT_ROOT__DEPLOYMENT_CONTEXTS:
				return ((InternalEList<?>)getDeploymentContexts()).basicRemove(otherEnd, msgs);
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
			case DeploymentPackage.DEPLOYMENT_ROOT__DEPLOYMENT_CONTEXTS:
				return getDeploymentContexts();
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
			case DeploymentPackage.DEPLOYMENT_ROOT__DEPLOYMENT_CONTEXTS:
				getDeploymentContexts().clear();
				getDeploymentContexts().addAll((Collection<? extends DeploymentContext>)newValue);
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
			case DeploymentPackage.DEPLOYMENT_ROOT__DEPLOYMENT_CONTEXTS:
				getDeploymentContexts().clear();
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
			case DeploymentPackage.DEPLOYMENT_ROOT__DEPLOYMENT_CONTEXTS:
				return deploymentContexts != null && !deploymentContexts.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //DeploymentRootImpl
