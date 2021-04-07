/**
 */
package kieker.model.analysismodel.statistics;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Record</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.statistics.Record#getProperties <em>Properties</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getRecord()
 * @model
 * @generated
 */
public interface Record extends EObject {
	/**
	 * Returns the value of the '<em><b>Properties</b></em>' map.
	 * The key is of type {@link kieker.model.analysismodel.statistics.EPropertyType},
	 * and the value is of type {@link java.lang.Object},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Properties</em>' map.
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getRecord_Properties()
	 * @model mapType="kieker.model.analysismodel.statistics.EPropertyTypeToValue&lt;kieker.model.analysismodel.statistics.EPropertyType, org.eclipse.emf.ecore.EJavaObject&gt;"
	 * @generated
	 */
	EMap<EPropertyType, Object> getProperties();

} // Record
