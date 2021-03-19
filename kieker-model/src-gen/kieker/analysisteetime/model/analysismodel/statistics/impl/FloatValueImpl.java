/**
 */
package kieker.analysisteetime.model.analysismodel.statistics.impl;

import kieker.analysisteetime.model.analysismodel.statistics.FloatValue;
import kieker.analysisteetime.model.analysismodel.statistics.StatisticsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Float Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.analysisteetime.model.analysismodel.statistics.impl.FloatValueImpl#getMeasurement <em>Measurement</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FloatValueImpl extends ValueImpl implements FloatValue {
	/**
	 * The default value of the '{@link #getMeasurement() <em>Measurement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getMeasurement()
	 * @generated
	 * @ordered
	 */
	protected static final float MEASUREMENT_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getMeasurement() <em>Measurement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getMeasurement()
	 * @generated
	 * @ordered
	 */
	protected float measurement = MEASUREMENT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected FloatValueImpl() {
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
		return StatisticsPackage.Literals.FLOAT_VALUE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public float getMeasurement() {
		return measurement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setMeasurement(float newMeasurement) {
		float oldMeasurement = measurement;
		measurement = newMeasurement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, StatisticsPackage.FLOAT_VALUE__MEASUREMENT, oldMeasurement, measurement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case StatisticsPackage.FLOAT_VALUE__MEASUREMENT:
			return getMeasurement();
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
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case StatisticsPackage.FLOAT_VALUE__MEASUREMENT:
			setMeasurement((Float) newValue);
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
	public void eUnset(int featureID) {
		switch (featureID) {
		case StatisticsPackage.FLOAT_VALUE__MEASUREMENT:
			setMeasurement(MEASUREMENT_EDEFAULT);
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
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case StatisticsPackage.FLOAT_VALUE__MEASUREMENT:
			return measurement != MEASUREMENT_EDEFAULT;
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
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (measurement: ");
		result.append(measurement);
		result.append(')');
		return result.toString();
	}

} // FloatValueImpl
