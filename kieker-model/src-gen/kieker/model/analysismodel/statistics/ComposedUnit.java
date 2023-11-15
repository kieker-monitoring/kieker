/**
 */
package kieker.model.analysismodel.statistics;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Composed Unit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.statistics.ComposedUnit#getUnits <em>Units</em>}</li>
 * <li>{@link kieker.model.analysismodel.statistics.ComposedUnit#getExponent <em>Exponent</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getComposedUnit()
 * @model
 * @generated
 */
public interface ComposedUnit extends EObject {
	/**
	 * Returns the value of the '<em><b>Units</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.model.analysismodel.statistics.Unit}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Units</em>' containment reference list.
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getComposedUnit_Units()
	 * @model containment="true"
	 * @generated
	 */
	EList<Unit> getUnits();

	/**
	 * Returns the value of the '<em><b>Exponent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Exponent</em>' attribute.
	 * @see #setExponent(long)
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getComposedUnit_Exponent()
	 * @model required="true"
	 * @generated
	 */
	long getExponent();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.statistics.ComposedUnit#getExponent <em>Exponent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Exponent</em>' attribute.
	 * @see #getExponent()
	 * @generated
	 */
	void setExponent(long value);

} // ComposedUnit
