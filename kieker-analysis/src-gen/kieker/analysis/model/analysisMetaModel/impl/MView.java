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
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.MIDisplayConnector;
import kieker.analysis.model.analysisMetaModel.MIView;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>View</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MView#getName <em>Name</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MView#getDescription <em>Description</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MView#getDisplayConnectors <em>Display Connectors</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MView#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MView extends EObjectImpl implements MIView {
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
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDisplayConnectors() <em>Display Connectors</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getDisplayConnectors()
	 * @generated
	 * @ordered
	 */
	protected EList<MIDisplayConnector> displayConnectors;

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
	protected MView() {
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
		return MIAnalysisMetaModelPackage.Literals.VIEW;
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
			this.eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.VIEW__NAME, oldName, this.name));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setDescription(final String newDescription) {
		final String oldDescription = this.description;
		this.description = newDescription;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.VIEW__DESCRIPTION, oldDescription, this.description));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EList<MIDisplayConnector> getDisplayConnectors() {
		if (this.displayConnectors == null) {
			this.displayConnectors = new EObjectContainmentEList<>(MIDisplayConnector.class, this,
					MIAnalysisMetaModelPackage.VIEW__DISPLAY_CONNECTORS);
		}
		return this.displayConnectors;
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
			this.eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.VIEW__ID, oldId, this.id));
		}
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
		case MIAnalysisMetaModelPackage.VIEW__DISPLAY_CONNECTORS:
			return ((InternalEList<?>) this.getDisplayConnectors()).basicRemove(otherEnd, msgs);
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
		case MIAnalysisMetaModelPackage.VIEW__NAME:
			return this.getName();
		case MIAnalysisMetaModelPackage.VIEW__DESCRIPTION:
			return this.getDescription();
		case MIAnalysisMetaModelPackage.VIEW__DISPLAY_CONNECTORS:
			return this.getDisplayConnectors();
		case MIAnalysisMetaModelPackage.VIEW__ID:
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
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case MIAnalysisMetaModelPackage.VIEW__NAME:
			this.setName((String) newValue);
			return;
		case MIAnalysisMetaModelPackage.VIEW__DESCRIPTION:
			this.setDescription((String) newValue);
			return;
		case MIAnalysisMetaModelPackage.VIEW__DISPLAY_CONNECTORS:
			this.getDisplayConnectors().clear();
			this.getDisplayConnectors().addAll((Collection<? extends MIDisplayConnector>) newValue);
			return;
		case MIAnalysisMetaModelPackage.VIEW__ID:
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
		case MIAnalysisMetaModelPackage.VIEW__NAME:
			this.setName(NAME_EDEFAULT);
			return;
		case MIAnalysisMetaModelPackage.VIEW__DESCRIPTION:
			this.setDescription(DESCRIPTION_EDEFAULT);
			return;
		case MIAnalysisMetaModelPackage.VIEW__DISPLAY_CONNECTORS:
			this.getDisplayConnectors().clear();
			return;
		case MIAnalysisMetaModelPackage.VIEW__ID:
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
		case MIAnalysisMetaModelPackage.VIEW__NAME:
			return NAME_EDEFAULT == null ? this.name != null : !NAME_EDEFAULT.equals(this.name);
		case MIAnalysisMetaModelPackage.VIEW__DESCRIPTION:
			return DESCRIPTION_EDEFAULT == null ? this.description != null : !DESCRIPTION_EDEFAULT.equals(this.description);
		case MIAnalysisMetaModelPackage.VIEW__DISPLAY_CONNECTORS:
			return (this.displayConnectors != null) && !this.displayConnectors.isEmpty();
		case MIAnalysisMetaModelPackage.VIEW__ID:
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
		result.append(", description: ");
		result.append(this.description);
		result.append(", id: ");
		result.append(this.id);
		result.append(')');
		return result.toString();
	}

} // MView
