/**
 */
package kieker.model.analysismodel.statistics;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Custom Unit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.statistics.CustomUnit#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getCustomUnit()
 * @model
 * @generated
 */
public interface CustomUnit extends SimpleUnit {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getCustomUnit_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.statistics.CustomUnit#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // CustomUnit
