/**
 */
package kieker.model.analysismodel.statistics;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vector Measurement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.statistics.VectorMeasurement#getValues <em>Values</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getVectorMeasurement()
 * @model
 * @generated
 */
public interface VectorMeasurement extends Measurement {
	/**
	 * Returns the value of the '<em><b>Values</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.model.analysismodel.statistics.ScalarMeasurement}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Values</em>' containment reference list.
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getVectorMeasurement_Values()
	 * @model containment="true"
	 * @generated
	 */
	EList<ScalarMeasurement> getValues();

} // VectorMeasurement
