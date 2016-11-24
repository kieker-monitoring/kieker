/**
 */
package kieker.analysisteetime.model.analysismodel.trace;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Failed Operation Call</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.trace.FailedOperationCall#getFailedCause <em>Failed Cause</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.trace.TracePackage#getFailedOperationCall()
 * @model
 * @generated
 */
public interface FailedOperationCall extends OperationCall {
	/**
	 * Returns the value of the '<em><b>Failed Cause</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Failed Cause</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Failed Cause</em>' attribute.
	 * @see #setFailedCause(String)
	 * @see kieker.analysisteetime.model.analysismodel.trace.TracePackage#getFailedOperationCall_FailedCause()
	 * @model
	 * @generated
	 */
	String getFailedCause();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.trace.FailedOperationCall#getFailedCause <em>Failed Cause</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Failed Cause</em>' attribute.
	 * @see #getFailedCause()
	 * @generated
	 */
	void setFailedCause(String value);

} // FailedOperationCall
