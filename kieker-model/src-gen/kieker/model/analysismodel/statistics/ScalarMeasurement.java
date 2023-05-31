/**
 */
package kieker.model.analysismodel.statistics;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scalar Measurement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.statistics.ScalarMeasurement#getUnit <em>Unit</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getScalarMeasurement()
 * @model
 * @generated
 */
public interface ScalarMeasurement extends Measurement {
	/**
	 * Returns the value of the '<em><b>Unit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unit</em>' reference.
	 * @see #setUnit(Unit)
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getScalarMeasurement_Unit()
	 * @model
	 * @generated
	 */
	Unit getUnit();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.statistics.ScalarMeasurement#getUnit <em>Unit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit</em>' reference.
	 * @see #getUnit()
	 * @generated
	 */
	void setUnit(Unit value);

} // ScalarMeasurement
