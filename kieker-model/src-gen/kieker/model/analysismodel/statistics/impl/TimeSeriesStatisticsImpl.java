/**
 */
package kieker.model.analysismodel.statistics.impl;

import java.util.Collection;

import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.statistics.TimeSeries;
import kieker.model.analysismodel.statistics.TimeSeriesStatistics;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Time Series Statistics</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.statistics.impl.TimeSeriesStatisticsImpl#getTimeSeries <em>Time Series</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TimeSeriesStatisticsImpl extends MinimalEObjectImpl.Container implements TimeSeriesStatistics {
	/**
	 * The cached value of the '{@link #getTimeSeries() <em>Time Series</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeSeries()
	 * @generated
	 * @ordered
	 */
	protected EList<TimeSeries<?, ?>> timeSeries;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TimeSeriesStatisticsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return StatisticsPackage.Literals.TIME_SERIES_STATISTICS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<TimeSeries<?, ?>> getTimeSeries() {
		if (timeSeries == null) {
			timeSeries = new EObjectResolvingEList<TimeSeries<?, ?>>(TimeSeries.class, this, StatisticsPackage.TIME_SERIES_STATISTICS__TIME_SERIES);
		}
		return timeSeries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case StatisticsPackage.TIME_SERIES_STATISTICS__TIME_SERIES:
				return getTimeSeries();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case StatisticsPackage.TIME_SERIES_STATISTICS__TIME_SERIES:
				getTimeSeries().clear();
				getTimeSeries().addAll((Collection<? extends TimeSeries<?, ?>>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case StatisticsPackage.TIME_SERIES_STATISTICS__TIME_SERIES:
				getTimeSeries().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case StatisticsPackage.TIME_SERIES_STATISTICS__TIME_SERIES:
				return timeSeries != null && !timeSeries.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //TimeSeriesStatisticsImpl
