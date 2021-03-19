/**
 */
package kieker.analysisteetime.model.analysismodel.statistics;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Statistics</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.analysisteetime.model.analysismodel.statistics.Statistics#getTimeSeries <em>Time Series</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.statistics.StatisticsPackage#getStatistics()
 * @model
 * @generated
 */
public interface Statistics extends EObject {
	/**
	 * Returns the value of the '<em><b>Time Series</b></em>' reference list.
	 * The list contents are of type {@link kieker.analysisteetime.model.analysismodel.statistics.TimeSeries}&lt;?, ?&gt;.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Time Series</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Time Series</em>' reference list.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.StatisticsPackage#getStatistics_TimeSeries()
	 * @model
	 * @generated
	 */
	EList<TimeSeries<?, ?>> getTimeSeries();

} // Statistics
