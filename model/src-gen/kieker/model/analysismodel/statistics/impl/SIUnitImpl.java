/**
 */
package kieker.model.analysismodel.statistics.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import kieker.model.analysismodel.statistics.ESIUnitType;
import kieker.model.analysismodel.statistics.SIUnit;
import kieker.model.analysismodel.statistics.StatisticsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>SI Unit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.statistics.impl.SIUnitImpl#getUnitType <em>Unit Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SIUnitImpl extends SimpleUnitImpl implements SIUnit {
	/**
	 * The default value of the '{@link #getUnitType() <em>Unit Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getUnitType()
	 * @generated
	 * @ordered
	 */
	protected static final ESIUnitType UNIT_TYPE_EDEFAULT = ESIUnitType.METER;

	/**
	 * The cached value of the '{@link #getUnitType() <em>Unit Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getUnitType()
	 * @generated
	 * @ordered
	 */
	protected ESIUnitType unitType = UNIT_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected SIUnitImpl() {
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
		return StatisticsPackage.Literals.SI_UNIT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ESIUnitType getUnitType() {
		return this.unitType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setUnitType(final ESIUnitType newUnitType) {
		final ESIUnitType oldUnitType = this.unitType;
		this.unitType = newUnitType == null ? UNIT_TYPE_EDEFAULT : newUnitType;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, StatisticsPackage.SI_UNIT__UNIT_TYPE, oldUnitType, this.unitType));
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
		case StatisticsPackage.SI_UNIT__UNIT_TYPE:
			return this.getUnitType();
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
		case StatisticsPackage.SI_UNIT__UNIT_TYPE:
			this.setUnitType((ESIUnitType) newValue);
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
		case StatisticsPackage.SI_UNIT__UNIT_TYPE:
			this.setUnitType(UNIT_TYPE_EDEFAULT);
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
		case StatisticsPackage.SI_UNIT__UNIT_TYPE:
			return this.unitType != UNIT_TYPE_EDEFAULT;
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

		final StringBuilder result = new StringBuilder(super.toString());
		result.append(" (unitType: ");
		result.append(this.unitType);
		result.append(')');
		return result.toString();
	}

} // SIUnitImpl
