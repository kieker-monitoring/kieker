/**
 */
package kieker.model.analysismodel.statistics;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Int Measurement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.statistics.IntMeasurement#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getIntMeasurement()
 * @model
 * @generated
 */
public interface IntMeasurement extends ScalarMeasurement {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(int)
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getIntMeasurement_Value()
	 * @model
	 * @generated
	 */
	int getValue();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.statistics.IntMeasurement#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(int value);

} // IntMeasurement
