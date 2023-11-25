/**
 */
package kieker.analysis.model.analysisMetaModel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.MIDisplay;
import kieker.analysis.model.analysisMetaModel.MIPlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Display</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MDisplay#getName <em>Name</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MDisplay#getParent <em>Parent</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MDisplay#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MDisplay extends EObjectImpl implements MIDisplay {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected MDisplay() {
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
		return MIAnalysisMetaModelPackage.Literals.DISPLAY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setName(final String newName) {
		final String oldName = this.name;
		this.name = newName;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.DISPLAY__NAME, oldName, this.name));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIPlugin getParent() {
		if (this.eContainerFeatureID() != MIAnalysisMetaModelPackage.DISPLAY__PARENT) {
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
		msgs = this.eBasicSetContainer((InternalEObject) newParent, MIAnalysisMetaModelPackage.DISPLAY__PARENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setParent(final MIPlugin newParent) {
		if ((newParent != this.eInternalContainer()) || ((this.eContainerFeatureID() != MIAnalysisMetaModelPackage.DISPLAY__PARENT) && (newParent != null))) {
			if (EcoreUtil.isAncestor(this, newParent)) {
				throw new IllegalArgumentException("Recursive containment not allowed for " + this.toString());
			}
			NotificationChain msgs = null;
			if (this.eInternalContainer() != null) {
				msgs = this.eBasicRemoveFromContainer(msgs);
			}
			if (newParent != null) {
				msgs = ((InternalEObject) newParent).eInverseAdd(this, MIAnalysisMetaModelPackage.PLUGIN__DISPLAYS, MIPlugin.class, msgs);
			}
			msgs = this.basicSetParent(newParent, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.DISPLAY__PARENT, newParent, newParent));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setId(final String newId) {
		final String oldId = this.id;
		this.id = newId;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.DISPLAY__ID, oldId, this.id));
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
		case MIAnalysisMetaModelPackage.DISPLAY__PARENT:
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
		case MIAnalysisMetaModelPackage.DISPLAY__PARENT:
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
		case MIAnalysisMetaModelPackage.DISPLAY__PARENT:
			return this.eInternalContainer().eInverseRemove(this, MIAnalysisMetaModelPackage.PLUGIN__DISPLAYS, MIPlugin.class, msgs);
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
		case MIAnalysisMetaModelPackage.DISPLAY__NAME:
			return this.getName();
		case MIAnalysisMetaModelPackage.DISPLAY__PARENT:
			return this.getParent();
		case MIAnalysisMetaModelPackage.DISPLAY__ID:
			return this.getId();
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
		case MIAnalysisMetaModelPackage.DISPLAY__NAME:
			this.setName((String) newValue);
			return;
		case MIAnalysisMetaModelPackage.DISPLAY__PARENT:
			this.setParent((MIPlugin) newValue);
			return;
		case MIAnalysisMetaModelPackage.DISPLAY__ID:
			this.setId((String) newValue);
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
		case MIAnalysisMetaModelPackage.DISPLAY__NAME:
			this.setName(NAME_EDEFAULT);
			return;
		case MIAnalysisMetaModelPackage.DISPLAY__PARENT:
			this.setParent((MIPlugin) null);
			return;
		case MIAnalysisMetaModelPackage.DISPLAY__ID:
			this.setId(ID_EDEFAULT);
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
		case MIAnalysisMetaModelPackage.DISPLAY__NAME:
			return NAME_EDEFAULT == null ? this.name != null : !NAME_EDEFAULT.equals(this.name);
		case MIAnalysisMetaModelPackage.DISPLAY__PARENT:
			return this.getParent() != null;
		case MIAnalysisMetaModelPackage.DISPLAY__ID:
			return ID_EDEFAULT == null ? this.id != null : !ID_EDEFAULT.equals(this.id);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String toString() {
		if (this.eIsProxy()) {
			return super.toString();
		}

		final StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(this.name);
		result.append(", id: ");
		result.append(this.id);
		result.append(')');
		return result.toString();
	}

} // MDisplay
