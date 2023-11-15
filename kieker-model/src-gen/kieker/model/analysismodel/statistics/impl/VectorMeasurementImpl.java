/**
 */
package kieker.model.analysismodel.statistics.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import kieker.model.analysismodel.statistics.ScalarMeasurement;
import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.statistics.VectorMeasurement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vector Measurement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.statistics.impl.VectorMeasurementImpl#getValues <em>Values</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VectorMeasurementImpl extends MeasurementImpl implements VectorMeasurement {
	/**
	 * The cached value of the '{@link #getValues() <em>Values</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getValues()
	 * @generated
	 * @ordered
	 */
	protected EList<ScalarMeasurement> values;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected VectorMeasurementImpl() {
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
		return StatisticsPackage.Literals.VECTOR_MEASUREMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<ScalarMeasurement> getValues() {
		if (this.values == null) {
			this.values = new EObjectContainmentEList<>(ScalarMeasurement.class, this, StatisticsPackage.VECTOR_MEASUREMENT__VALUES);
		}
		return this.values;
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
		case StatisticsPackage.VECTOR_MEASUREMENT__VALUES:
			return ((InternalEList<?>) this.getValues()).basicRemove(otherEnd, msgs);
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
		case StatisticsPackage.VECTOR_MEASUREMENT__VALUES:
			return this.getValues();
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
		case StatisticsPackage.VECTOR_MEASUREMENT__VALUES:
			this.getValues().clear();
			this.getValues().addAll((Collection<? extends ScalarMeasurement>) newValue);
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
		case StatisticsPackage.VECTOR_MEASUREMENT__VALUES:
			this.getValues().clear();
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
		case StatisticsPackage.VECTOR_MEASUREMENT__VALUES:
			return (this.values != null) && !this.values.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // VectorMeasurementImpl
