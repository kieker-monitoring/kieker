/**
 */
package kieker.model.analysismodel.statistics;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Time Series Statistics</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.statistics.TimeSeriesStatistics#getTimeSeries <em>Time Series</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getTimeSeriesStatistics()
 * @model
 * @generated
 */
public interface TimeSeriesStatistics extends EObject {
	/**
	 * Returns the value of the '<em><b>Time Series</b></em>' reference list.
	 * The list contents are of type {@link kieker.model.analysismodel.statistics.TimeSeries}<code>&lt;?, ?&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Time Series</em>' reference list.
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getTimeSeriesStatistics_TimeSeries()
	 * @model
	 * @generated
	 */
	EList<TimeSeries<?, ?>> getTimeSeries();

} // TimeSeriesStatistics
