/**
 */
package kieker.model.analysismodel.statistics;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see kieker.model.analysismodel.statistics.StatisticsPackage
 * @generated
 */
public interface StatisticsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	StatisticsFactory eINSTANCE = kieker.model.analysismodel.statistics.impl.StatisticsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Statistics</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Statistics</em>'.
	 * @generated
	 */
	Statistics createStatistics();

	/**
	 * Returns a new object of class '<em>Record</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Record</em>'.
	 * @generated
	 */
	Record createRecord();

	/**
	 * Returns a new object of class '<em>Time Series</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Time Series</em>'.
	 * @generated
	 */
	<V extends Value, U extends Unit<V>> TimeSeries<V, U> createTimeSeries();

	/**
	 * Returns a new object of class '<em>Int Value</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Int Value</em>'.
	 * @generated
	 */
	IntValue createIntValue();

	/**
	 * Returns a new object of class '<em>Long Value</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Long Value</em>'.
	 * @generated
	 */
	LongValue createLongValue();

	/**
	 * Returns a new object of class '<em>Float Value</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Float Value</em>'.
	 * @generated
	 */
	FloatValue createFloatValue();

	/**
	 * Returns a new object of class '<em>Double Value</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Double Value</em>'.
	 * @generated
	 */
	DoubleValue createDoubleValue();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	StatisticsModel createStatisticsModel();

	/**
	 * Returns a new object of class '<em>Time Series Statistics</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Time Series Statistics</em>'.
	 * @generated
	 */
	TimeSeriesStatistics createTimeSeriesStatistics();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	StatisticsPackage getStatisticsPackage();

} //StatisticsFactory
