/**
 */
package kieker.model.analysismodel.statistics;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage
 * @generated
 */
public interface StatisticsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	StatisticsFactory eINSTANCE = kieker.model.analysismodel.statistics.impl.StatisticsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Statistic Record</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Statistic Record</em>'.
	 * @generated
	 */
	StatisticRecord createStatisticRecord();

	/**
	 * Returns a new object of class '<em>Scalar Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Scalar Measurement</em>'.
	 * @generated
	 */
	ScalarMeasurement createScalarMeasurement();

	/**
	 * Returns a new object of class '<em>Vector Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Vector Measurement</em>'.
	 * @generated
	 */
	VectorMeasurement createVectorMeasurement();

	/**
	 * Returns a new object of class '<em>Int Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Int Measurement</em>'.
	 * @generated
	 */
	IntMeasurement createIntMeasurement();

	/**
	 * Returns a new object of class '<em>Long Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Long Measurement</em>'.
	 * @generated
	 */
	LongMeasurement createLongMeasurement();

	/**
	 * Returns a new object of class '<em>Float Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Float Measurement</em>'.
	 * @generated
	 */
	FloatMeasurement createFloatMeasurement();

	/**
	 * Returns a new object of class '<em>Double Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Double Measurement</em>'.
	 * @generated
	 */
	DoubleMeasurement createDoubleMeasurement();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	StatisticsModel createStatisticsModel();

	/**
	 * Returns a new object of class '<em>Unit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Unit</em>'.
	 * @generated
	 */
	Unit createUnit();

	/**
	 * Returns a new object of class '<em>Composed Unit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Composed Unit</em>'.
	 * @generated
	 */
	ComposedUnit createComposedUnit();

	/**
	 * Returns a new object of class '<em>Simple Unit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Simple Unit</em>'.
	 * @generated
	 */
	SimpleUnit createSimpleUnit();

	/**
	 * Returns a new object of class '<em>SI Unit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>SI Unit</em>'.
	 * @generated
	 */
	SIUnit createSIUnit();

	/**
	 * Returns a new object of class '<em>Custom Unit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Custom Unit</em>'.
	 * @generated
	 */
	CustomUnit createCustomUnit();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the package supported by this factory.
	 * @generated
	 */
	StatisticsPackage getStatisticsPackage();

} // StatisticsFactory
