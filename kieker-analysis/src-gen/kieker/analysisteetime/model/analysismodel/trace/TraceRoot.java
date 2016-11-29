/**
 */
package kieker.analysisteetime.model.analysismodel.trace;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.trace.TraceRoot#getTraceID <em>Trace ID</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.trace.TraceRoot#getRootOperationCall <em>Root Operation Call</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.trace.TracePackage#getTraceRoot()
 * @model
 * @generated
 */
public interface TraceRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Trace ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Trace ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Trace ID</em>' attribute.
	 * @see #setTraceID(long)
	 * @see kieker.analysisteetime.model.analysismodel.trace.TracePackage#getTraceRoot_TraceID()
	 * @model
	 * @generated
	 */
	long getTraceID();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.trace.TraceRoot#getTraceID <em>Trace ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Trace ID</em>' attribute.
	 * @see #getTraceID()
	 * @generated
	 */
	void setTraceID(long value);

	/**
	 * Returns the value of the '<em><b>Root Operation Call</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Root Operation Call</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Root Operation Call</em>' containment reference.
	 * @see #setRootOperationCall(OperationCall)
	 * @see kieker.analysisteetime.model.analysismodel.trace.TracePackage#getTraceRoot_RootOperationCall()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
	OperationCall getRootOperationCall();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.trace.TraceRoot#getRootOperationCall <em>Root Operation Call</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Root Operation Call</em>' containment reference.
	 * @see #getRootOperationCall()
	 * @generated
	 */
	void setRootOperationCall(OperationCall value);

} // TraceRoot
