/**
 */
package kieker.model.analysismodel.statistics.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import kieker.model.analysismodel.statistics.ScalarMeasurement;
import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.statistics.Unit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scalar Measurement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.statistics.impl.ScalarMeasurementImpl#getUnit <em>Unit</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ScalarMeasurementImpl extends MeasurementImpl implements ScalarMeasurement {
	/**
	 * The cached value of the '{@link #getUnit() <em>Unit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getUnit()
	 * @generated
	 * @ordered
	 */
	protected Unit unit;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected ScalarMeasurementImpl() {
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
		return StatisticsPackage.Literals.SCALAR_MEASUREMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Unit getUnit() {
		if ((this.unit != null) && this.unit.eIsProxy()) {
			final InternalEObject oldUnit = (InternalEObject) this.unit;
			this.unit = (Unit) this.eResolveProxy(oldUnit);
			if (this.unit != oldUnit) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, StatisticsPackage.SCALAR_MEASUREMENT__UNIT, oldUnit, this.unit));
				}
			}
		}
		return this.unit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Unit basicGetUnit() {
		return this.unit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setUnit(final Unit newUnit) {
		final Unit oldUnit = this.unit;
		this.unit = newUnit;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, StatisticsPackage.SCALAR_MEASUREMENT__UNIT, oldUnit, this.unit));
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
		case StatisticsPackage.SCALAR_MEASUREMENT__UNIT:
			if (resolve) {
				return this.getUnit();
			}
			return this.basicGetUnit();
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
		case StatisticsPackage.SCALAR_MEASUREMENT__UNIT:
			this.setUnit((Unit) newValue);
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
		case StatisticsPackage.SCALAR_MEASUREMENT__UNIT:
			this.setUnit((Unit) null);
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
		case StatisticsPackage.SCALAR_MEASUREMENT__UNIT:
			return this.unit != null;
		}
		return super.eIsSet(featureID);
	}

} // ScalarMeasurementImpl
