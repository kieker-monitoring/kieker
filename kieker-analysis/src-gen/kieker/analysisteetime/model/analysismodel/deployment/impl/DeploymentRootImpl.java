/**
 */
package kieker.analysisteetime.model.analysismodel.deployment.impl;

import kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
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
	 * The cached value of the '{@link #getDeploymentContexts() <em>Deployment Contexts</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeploymentContexts()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, DeploymentContext> deploymentContexts;

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
	public EMap<String, DeploymentContext> getDeploymentContexts() {
		if (deploymentContexts == null) {
			deploymentContexts = new EcoreEMap<String,DeploymentContext>(DeploymentPackage.Literals.ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY, EStringToDeploymentContextMapEntryImpl.class, this, DeploymentPackage.DEPLOYMENT_ROOT__DEPLOYMENT_CONTEXTS);
		}
		return deploymentContexts;
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
				if (coreType) return getDeploymentContexts();
				else return getDeploymentContexts().map();
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
				((EStructuralFeature.Setting)getDeploymentContexts()).set(newValue);
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
