/**
 */
package kieker.analysis.model.analysisMetaModel.impl;

import java.util.Collection;
import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.MIPlugin;
import kieker.analysis.model.analysisMetaModel.MIRepositoryInputPort;
import kieker.analysis.model.analysisMetaModel.MIRepositoryPort;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Repository Port</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MRepositoryPort#getSubscribers <em>Subscribers</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MRepositoryPort#getParent <em>Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MRepositoryPort extends MPort implements MIRepositoryPort {
	/**
	 * The cached value of the '{@link #getSubscribers() <em>Subscribers</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubscribers()
	 * @generated
	 * @ordered
	 */
	protected EList<MIRepositoryInputPort> subscribers;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MRepositoryPort() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MIAnalysisMetaModelPackage.Literals.REPOSITORY_PORT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MIRepositoryInputPort> getSubscribers() {
		if (subscribers == null) {
			subscribers = new EObjectResolvingEList<MIRepositoryInputPort>(MIRepositoryInputPort.class, this, MIAnalysisMetaModelPackage.REPOSITORY_PORT__SUBSCRIBERS);
		}
		return subscribers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MIPlugin getParent() {
		if (eContainerFeatureID() != MIAnalysisMetaModelPackage.REPOSITORY_PORT__PARENT) return null;
		return (MIPlugin)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParent(MIPlugin newParent, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newParent, MIAnalysisMetaModelPackage.REPOSITORY_PORT__PARENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParent(MIPlugin newParent) {
		if (newParent != eInternalContainer() || (eContainerFeatureID() != MIAnalysisMetaModelPackage.REPOSITORY_PORT__PARENT && newParent != null)) {
			if (EcoreUtil.isAncestor(this, newParent))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newParent != null)
				msgs = ((InternalEObject)newParent).eInverseAdd(this, MIAnalysisMetaModelPackage.PLUGIN__REPOSITORY_PORTS, MIPlugin.class, msgs);
			msgs = basicSetParent(newParent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.REPOSITORY_PORT__PARENT, newParent, newParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MIAnalysisMetaModelPackage.REPOSITORY_PORT__PARENT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetParent((MIPlugin)otherEnd, msgs);
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
			case MIAnalysisMetaModelPackage.REPOSITORY_PORT__PARENT:
				return basicSetParent(null, msgs);
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
			case MIAnalysisMetaModelPackage.REPOSITORY_PORT__PARENT:
				return eInternalContainer().eInverseRemove(this, MIAnalysisMetaModelPackage.PLUGIN__REPOSITORY_PORTS, MIPlugin.class, msgs);
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
			case MIAnalysisMetaModelPackage.REPOSITORY_PORT__SUBSCRIBERS:
				return getSubscribers();
			case MIAnalysisMetaModelPackage.REPOSITORY_PORT__PARENT:
				return getParent();
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
			case MIAnalysisMetaModelPackage.REPOSITORY_PORT__SUBSCRIBERS:
				getSubscribers().clear();
				getSubscribers().addAll((Collection<? extends MIRepositoryInputPort>)newValue);
				return;
			case MIAnalysisMetaModelPackage.REPOSITORY_PORT__PARENT:
				setParent((MIPlugin)newValue);
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
			case MIAnalysisMetaModelPackage.REPOSITORY_PORT__SUBSCRIBERS:
				getSubscribers().clear();
				return;
			case MIAnalysisMetaModelPackage.REPOSITORY_PORT__PARENT:
				setParent((MIPlugin)null);
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
			case MIAnalysisMetaModelPackage.REPOSITORY_PORT__SUBSCRIBERS:
				return subscribers != null && !subscribers.isEmpty();
			case MIAnalysisMetaModelPackage.REPOSITORY_PORT__PARENT:
				return getParent() != null;
		}
		return super.eIsSet(featureID);
	}

} //MRepositoryPort
