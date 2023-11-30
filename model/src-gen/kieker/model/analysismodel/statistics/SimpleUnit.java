/**
 */
package kieker.model.analysismodel.statistics;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Simple Unit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.statistics.SimpleUnit#getPrefix <em>Prefix</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getSimpleUnit()
 * @model
 * @generated
 */
public interface SimpleUnit extends Unit {
	/**
	 * Returns the value of the '<em><b>Prefix</b></em>' attribute.
	 * The literals are from the enumeration {@link kieker.model.analysismodel.statistics.EPrefix}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Prefix</em>' attribute.
	 * @see kieker.model.analysismodel.statistics.EPrefix
	 * @see #setPrefix(EPrefix)
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getSimpleUnit_Prefix()
	 * @model
	 * @generated
	 */
	EPrefix getPrefix();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.statistics.SimpleUnit#getPrefix <em>Prefix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Prefix</em>' attribute.
	 * @see kieker.model.analysismodel.statistics.EPrefix
	 * @see #getPrefix()
	 * @generated
	 */
	void setPrefix(EPrefix value);

} // SimpleUnit
