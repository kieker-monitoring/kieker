/**
 */
package kieker.analysis.model.analysisMetaModel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.MIDisplay;
import kieker.analysis.model.analysisMetaModel.MIDisplayConnector;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Display Connector</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MDisplayConnector#getName <em>Name</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MDisplayConnector#getDisplay <em>Display</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MDisplayConnector#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MDisplayConnector extends EObjectImpl implements MIDisplayConnector {
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
	 * The cached value of the '{@link #getDisplay() <em>Display</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getDisplay()
	 * @generated
	 * @ordered
	 */
	protected MIDisplay display;

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
	protected MDisplayConnector() {
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
		return MIAnalysisMetaModelPackage.Literals.DISPLAY_CONNECTOR;
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
			this.eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR__NAME, oldName, this.name));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIDisplay getDisplay() {
		if ((this.display != null) && this.display.eIsProxy()) {
			final InternalEObject oldDisplay = (InternalEObject) this.display;
			this.display = (MIDisplay) this.eResolveProxy(oldDisplay);
			if (this.display != oldDisplay) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR__DISPLAY, oldDisplay, this.display));
				}
			}
		}
		return this.display;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIDisplay basicGetDisplay() {
		return this.display;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setDisplay(final MIDisplay newDisplay) {
		final MIDisplay oldDisplay = this.display;
		this.display = newDisplay;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR__DISPLAY, oldDisplay, this.display));
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
			this.eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR__ID, oldId, this.id));
		}
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
		case MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR__NAME:
			return this.getName();
		case MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR__DISPLAY:
			if (resolve) {
				return this.getDisplay();
			}
			return this.basicGetDisplay();
		case MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR__ID:
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
		case MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR__NAME:
			this.setName((String) newValue);
			return;
		case MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR__DISPLAY:
			this.setDisplay((MIDisplay) newValue);
			return;
		case MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR__ID:
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
		case MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR__NAME:
			this.setName(NAME_EDEFAULT);
			return;
		case MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR__DISPLAY:
			this.setDisplay((MIDisplay) null);
			return;
		case MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR__ID:
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
		case MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR__NAME:
			return NAME_EDEFAULT == null ? this.name != null : !NAME_EDEFAULT.equals(this.name);
		case MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR__DISPLAY:
			return this.display != null;
		case MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR__ID:
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

} // MDisplayConnector
