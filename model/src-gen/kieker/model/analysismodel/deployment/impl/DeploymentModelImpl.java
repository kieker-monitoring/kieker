/**
 */
package kieker.model.analysismodel.deployment.impl;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.deployment.DeploymentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.deployment.impl.DeploymentModelImpl#getContexts <em>Contexts</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeploymentModelImpl extends MinimalEObjectImpl.Container implements DeploymentModel {
	/**
	 * The cached value of the '{@link #getContexts() <em>Contexts</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getContexts()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, DeploymentContext> contexts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected DeploymentModelImpl() {
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
		return DeploymentPackage.Literals.DEPLOYMENT_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<String, DeploymentContext> getContexts() {
		if (this.contexts == null) {
			this.contexts = new EcoreEMap<>(DeploymentPackage.Literals.ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY,
					EStringToDeploymentContextMapEntryImpl.class, this, DeploymentPackage.DEPLOYMENT_MODEL__CONTEXTS);
		}
		return this.contexts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(final InternalEObject otherEnd, final int featureID, final NotificationChain msgs) {
		switch (featureID) {
		case DeploymentPackage.DEPLOYMENT_MODEL__CONTEXTS:
			return ((InternalEList<?>) this.getContexts()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
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
		case DeploymentPackage.DEPLOYMENT_MODEL__CONTEXTS:
			if (coreType) {
				return this.getContexts();
			} else {
				return this.getContexts().map();
			}
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
		case DeploymentPackage.DEPLOYMENT_MODEL__CONTEXTS:
			((EStructuralFeature.Setting) this.getContexts()).set(newValue);
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
		case DeploymentPackage.DEPLOYMENT_MODEL__CONTEXTS:
			this.getContexts().clear();
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
		case DeploymentPackage.DEPLOYMENT_MODEL__CONTEXTS:
			return (this.contexts != null) && !this.contexts.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // DeploymentModelImpl
