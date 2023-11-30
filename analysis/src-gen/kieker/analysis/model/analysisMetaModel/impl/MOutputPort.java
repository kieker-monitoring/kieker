/**
 */
package kieker.analysis.model.analysisMetaModel.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.MIInputPort;
import kieker.analysis.model.analysisMetaModel.MIOutputPort;
import kieker.analysis.model.analysisMetaModel.MIPlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Output Port</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MOutputPort#getSubscribers <em>Subscribers</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MOutputPort#getParent <em>Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MOutputPort extends MPort implements MIOutputPort {
	/**
	 * The cached value of the '{@link #getSubscribers() <em>Subscribers</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getSubscribers()
	 * @generated
	 * @ordered
	 */
	protected EList<MIInputPort> subscribers;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected MOutputPort() {
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
		return MIAnalysisMetaModelPackage.Literals.OUTPUT_PORT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EList<MIInputPort> getSubscribers() {
		if (this.subscribers == null) {
			this.subscribers = new EObjectResolvingEList<>(MIInputPort.class, this, MIAnalysisMetaModelPackage.OUTPUT_PORT__SUBSCRIBERS);
		}
		return this.subscribers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIPlugin getParent() {
		if (this.eContainerFeatureID() != MIAnalysisMetaModelPackage.OUTPUT_PORT__PARENT) {
			return null;
		}
		return (MIPlugin) this.eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetParent(final MIPlugin newParent, NotificationChain msgs) {
		msgs = this.eBasicSetContainer((InternalEObject) newParent, MIAnalysisMetaModelPackage.OUTPUT_PORT__PARENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setParent(final MIPlugin newParent) {
		if ((newParent != this.eInternalContainer()) || ((this.eContainerFeatureID() != MIAnalysisMetaModelPackage.OUTPUT_PORT__PARENT) && (newParent != null))) {
			if (EcoreUtil.isAncestor(this, newParent)) {
				throw new IllegalArgumentException("Recursive containment not allowed for " + this.toString());
			}
			NotificationChain msgs = null;
			if (this.eInternalContainer() != null) {
				msgs = this.eBasicRemoveFromContainer(msgs);
			}
			if (newParent != null) {
				msgs = ((InternalEObject) newParent).eInverseAdd(this, MIAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS, MIPlugin.class, msgs);
			}
			msgs = this.basicSetParent(newParent, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.OUTPUT_PORT__PARENT, newParent, newParent));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(final InternalEObject otherEnd, final int featureID, NotificationChain msgs) {
		switch (featureID) {
		case MIAnalysisMetaModelPackage.OUTPUT_PORT__PARENT:
			if (this.eInternalContainer() != null) {
				msgs = this.eBasicRemoveFromContainer(msgs);
			}
			return this.basicSetParent((MIPlugin) otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
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
		case MIAnalysisMetaModelPackage.OUTPUT_PORT__PARENT:
			return this.basicSetParent(null, msgs);
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
	public NotificationChain eBasicRemoveFromContainerFeature(final NotificationChain msgs) {
		switch (this.eContainerFeatureID()) {
		case MIAnalysisMetaModelPackage.OUTPUT_PORT__PARENT:
			return this.eInternalContainer().eInverseRemove(this, MIAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS, MIPlugin.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
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
		case MIAnalysisMetaModelPackage.OUTPUT_PORT__SUBSCRIBERS:
			return this.getSubscribers();
		case MIAnalysisMetaModelPackage.OUTPUT_PORT__PARENT:
			return this.getParent();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case MIAnalysisMetaModelPackage.OUTPUT_PORT__SUBSCRIBERS:
			this.getSubscribers().clear();
			this.getSubscribers().addAll((Collection<? extends MIInputPort>) newValue);
			return;
		case MIAnalysisMetaModelPackage.OUTPUT_PORT__PARENT:
			this.setParent((MIPlugin) newValue);
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
		case MIAnalysisMetaModelPackage.OUTPUT_PORT__SUBSCRIBERS:
			this.getSubscribers().clear();
			return;
		case MIAnalysisMetaModelPackage.OUTPUT_PORT__PARENT:
			this.setParent((MIPlugin) null);
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
		case MIAnalysisMetaModelPackage.OUTPUT_PORT__SUBSCRIBERS:
			return (this.subscribers != null) && !this.subscribers.isEmpty();
		case MIAnalysisMetaModelPackage.OUTPUT_PORT__PARENT:
			return this.getParent() != null;
		}
		return super.eIsSet(featureID);
	}

} // MOutputPort
