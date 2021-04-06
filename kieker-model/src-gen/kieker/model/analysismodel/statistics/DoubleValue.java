/**
 */
package kieker.model.analysismodel.statistics;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Double Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.statistics.DoubleValue#getMeasurement <em>Measurement</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getDoubleValue()
 * @model
 * @generated
 */
public interface DoubleValue extends Value {
	/**
	 * Returns the value of the '<em><b>Measurement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Measurement</em>' attribute.
	 * @see #setMeasurement(double)
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getDoubleValue_Measurement()
	 * @model
	 * @generated
	 */
	double getMeasurement();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.statistics.DoubleValue#getMeasurement <em>Measurement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Measurement</em>' attribute.
	 * @see #getMeasurement()
	 * @generated
	 */
	void setMeasurement(double value);

} // DoubleValue
