/**
 */
package kieker.model.analysismodel.statistics;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.statistics.StatisticsModel#getStatistics <em>Statistics</em>}</li>
 * <li>{@link kieker.model.analysismodel.statistics.StatisticsModel#getUnits <em>Units</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getStatisticsModel()
 * @model
 * @generated
 */
public interface StatisticsModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Statistics</b></em>' map.
	 * The key is of type {@link org.eclipse.emf.ecore.EObject},
	 * and the value is of type {@link kieker.model.analysismodel.statistics.StatisticRecord},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Statistics</em>' map.
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getStatisticsModel_Statistics()
	 * @model mapType="kieker.model.analysismodel.statistics.EObjectToStatisticsMapEntry&lt;org.eclipse.emf.ecore.EObject,
	 *        kieker.model.analysismodel.statistics.StatisticRecord&gt;"
	 * @generated
	 */
	EMap<EObject, StatisticRecord> getStatistics();

	/**
	 * Returns the value of the '<em><b>Units</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.model.analysismodel.statistics.Unit}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Units</em>' containment reference list.
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getStatisticsModel_Units()
	 * @model containment="true"
	 * @generated
	 */
	EList<Unit> getUnits();

} // StatisticsModel
