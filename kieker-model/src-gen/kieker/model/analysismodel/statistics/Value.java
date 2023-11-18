/**
 */
package kieker.model.analysismodel.statistics;

import java.time.Instant;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.statistics.Value#getTimestamp <em>Timestamp</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getValue()
 * @model abstract="true"
 * @generated
 */
public interface Value extends EObject {
	/**
	 * Returns the value of the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Timestamp</em>' attribute.
	 * @see #setTimestamp(Instant)
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getValue_Timestamp()
	 * @model dataType="kieker.model.analysismodel.Instant"
	 * @generated
	 */
	Instant getTimestamp();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.statistics.Value#getTimestamp <em>Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Timestamp</em>' attribute.
	 * @see #getTimestamp()
	 * @generated
	 */
	void setTimestamp(Instant value);

} // Value
