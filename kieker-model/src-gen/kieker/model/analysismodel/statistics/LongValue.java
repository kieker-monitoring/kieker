/**
 */
package kieker.model.analysismodel.statistics;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Long Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.statistics.LongValue#getMeasurement <em>Measurement</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getLongValue()
 * @model
 * @generated
 */
public interface LongValue extends Value {
	/**
	 * Returns the value of the '<em><b>Measurement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Measurement</em>' attribute.
	 * @see #setMeasurement(long)
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getLongValue_Measurement()
	 * @model
	 * @generated
	 */
	long getMeasurement();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.statistics.LongValue#getMeasurement <em>Measurement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Measurement</em>' attribute.
	 * @see #getMeasurement()
	 * @generated
	 */
	void setMeasurement(long value);

} // LongValue
