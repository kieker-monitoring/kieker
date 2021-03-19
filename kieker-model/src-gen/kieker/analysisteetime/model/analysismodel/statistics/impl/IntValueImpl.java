/**
 */
package kieker.analysisteetime.model.analysismodel.statistics.impl;

import kieker.analysisteetime.model.analysismodel.statistics.IntValue;
import kieker.analysisteetime.model.analysismodel.statistics.StatisticsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Int Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.analysisteetime.model.analysismodel.statistics.impl.IntValueImpl#getMeasurement <em>Measurement</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IntValueImpl extends ValueImpl implements IntValue {
	/**
	 * The default value of the '{@link #getMeasurement() <em>Measurement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getMeasurement()
	 * @generated
	 * @ordered
	 */
	protected static final int MEASUREMENT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMeasurement() <em>Measurement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getMeasurement()
	 * @generated
	 * @ordered
	 */
	protected int measurement = MEASUREMENT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected IntValueImpl() {
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
		return StatisticsPackage.Literals.INT_VALUE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getMeasurement() {
		return measurement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setMeasurement(int newMeasurement) {
		int oldMeasurement = measurement;
		measurement = newMeasurement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, StatisticsPackage.INT_VALUE__MEASUREMENT, oldMeasurement, measurement));
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
		case StatisticsPackage.INT_VALUE__MEASUREMENT:
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
		case StatisticsPackage.INT_VALUE__MEASUREMENT:
			setMeasurement((Integer) newValue);
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
		case StatisticsPackage.INT_VALUE__MEASUREMENT:
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
		case StatisticsPackage.INT_VALUE__MEASUREMENT:
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

} // IntValueImpl
