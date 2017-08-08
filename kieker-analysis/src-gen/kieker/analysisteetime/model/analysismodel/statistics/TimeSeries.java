/**
 */
package kieker.analysisteetime.model.analysismodel.statistics;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Time Series</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.statistics.TimeSeries#getName <em>Name</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.statistics.TimeSeries#getUnit <em>Unit</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.statistics.TimeSeries#getValues <em>Values</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.statistics.StatisticsPackage#getTimeSeries()
 * @model
 * @generated
 */
public interface TimeSeries<V extends Value, U extends Unit<V>> extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.StatisticsPackage#getTimeSeries_Name()
	 * @model id="true" changeable="false"
	 * @generated
	 */
	String getName();

	/**
	 * Returns the value of the '<em><b>Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unit</em>' attribute.
	 * @see #setUnit(Object)
	 * @see kieker.analysisteetime.model.analysismodel.statistics.StatisticsPackage#getTimeSeries_Unit()
	 * @model
	 * @generated
	 */
	Object getUnit();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.statistics.TimeSeries#getUnit <em>Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit</em>' attribute.
	 * @see #getUnit()
	 * @generated
	 */
	void setUnit(Object value);

	/**
	 * Returns the value of the '<em><b>Values</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Values</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Values</em>' reference list.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.StatisticsPackage#getTimeSeries_Values()
	 * @model
	 * @generated
	 */
	EList<V> getValues();

} // TimeSeries
