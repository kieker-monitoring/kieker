/**
 */
package kieker.model.analysismodel.statistics;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Float Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.statistics.FloatValue#getMeasurement <em>Measurement</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getFloatValue()
 * @model
 * @generated
 */
public interface FloatValue extends Value {
	/**
	 * Returns the value of the '<em><b>Measurement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Measurement</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Measurement</em>' attribute.
	 * @see #setMeasurement(float)
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getFloatValue_Measurement()
	 * @model
	 * @generated
	 */
	float getMeasurement();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.statistics.FloatValue#getMeasurement <em>Measurement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Measurement</em>' attribute.
	 * @see #getMeasurement()
	 * @generated
	 */
	void setMeasurement(float value);

} // FloatValue
