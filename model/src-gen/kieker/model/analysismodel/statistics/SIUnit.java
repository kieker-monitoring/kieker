/**
 */
package kieker.model.analysismodel.statistics;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SI Unit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.statistics.SIUnit#getUnitType <em>Unit Type</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getSIUnit()
 * @model
 * @generated
 */
public interface SIUnit extends SimpleUnit {
	/**
	 * Returns the value of the '<em><b>Unit Type</b></em>' attribute.
	 * The literals are from the enumeration {@link kieker.model.analysismodel.statistics.ESIUnitType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Unit Type</em>' attribute.
	 * @see kieker.model.analysismodel.statistics.ESIUnitType
	 * @see #setUnitType(ESIUnitType)
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getSIUnit_UnitType()
	 * @model required="true"
	 * @generated
	 */
	ESIUnitType getUnitType();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.statistics.SIUnit#getUnitType <em>Unit Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Unit Type</em>' attribute.
	 * @see kieker.model.analysismodel.statistics.ESIUnitType
	 * @see #getUnitType()
	 * @generated
	 */
	void setUnitType(ESIUnitType value);

} // SIUnit
