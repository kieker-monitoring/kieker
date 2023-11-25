/**
 */
package kieker.analysis.model.analysisMetaModel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.MIFilter;
import kieker.analysis.model.analysisMetaModel.MIInputPort;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Input Port</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MInputPort#getParent <em>Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MInputPort extends MPort implements MIInputPort {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected MInputPort() {
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
		return MIAnalysisMetaModelPackage.Literals.INPUT_PORT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIFilter getParent() {
		if (this.eContainerFeatureID() != MIAnalysisMetaModelPackage.INPUT_PORT__PARENT) {
			return null;
		}
		return (MIFilter) this.eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetParent(final MIFilter newParent, NotificationChain msgs) {
		msgs = this.eBasicSetContainer((InternalEObject) newParent, MIAnalysisMetaModelPackage.INPUT_PORT__PARENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setParent(final MIFilter newParent) {
		if ((newParent != this.eInternalContainer()) || ((this.eContainerFeatureID() != MIAnalysisMetaModelPackage.INPUT_PORT__PARENT) && (newParent != null))) {
			if (EcoreUtil.isAncestor(this, newParent)) {
				throw new IllegalArgumentException("Recursive containment not allowed for " + this.toString());
			}
			NotificationChain msgs = null;
			if (this.eInternalContainer() != null) {
				msgs = this.eBasicRemoveFromContainer(msgs);
			}
			if (newParent != null) {
				msgs = ((InternalEObject) newParent).eInverseAdd(this, MIAnalysisMetaModelPackage.FILTER__INPUT_PORTS, MIFilter.class, msgs);
			}
			msgs = this.basicSetParent(newParent, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.INPUT_PORT__PARENT, newParent, newParent));
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
		case MIAnalysisMetaModelPackage.INPUT_PORT__PARENT:
			if (this.eInternalContainer() != null) {
				msgs = this.eBasicRemoveFromContainer(msgs);
			}
			return this.basicSetParent((MIFilter) otherEnd, msgs);
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
		case MIAnalysisMetaModelPackage.INPUT_PORT__PARENT:
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
		case MIAnalysisMetaModelPackage.INPUT_PORT__PARENT:
			return this.eInternalContainer().eInverseRemove(this, MIAnalysisMetaModelPackage.FILTER__INPUT_PORTS, MIFilter.class, msgs);
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
		case MIAnalysisMetaModelPackage.INPUT_PORT__PARENT:
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
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case MIAnalysisMetaModelPackage.INPUT_PORT__PARENT:
			this.setParent((MIFilter) newValue);
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
		case MIAnalysisMetaModelPackage.INPUT_PORT__PARENT:
			this.setParent((MIFilter) null);
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
		case MIAnalysisMetaModelPackage.INPUT_PORT__PARENT:
			return this.getParent() != null;
		}
		return super.eIsSet(featureID);
	}

} // MInputPort
