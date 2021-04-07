/**
 */
package kieker.model.analysismodel.trace;

import java.time.Duration;
import java.time.Instant;

import kieker.model.analysismodel.deployment.DeployedOperation;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Call</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.trace.OperationCall#getOperation <em>Operation</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.OperationCall#getParent <em>Parent</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.OperationCall#getChildren <em>Children</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.OperationCall#getDuration <em>Duration</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.OperationCall#getStart <em>Start</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.OperationCall#getDurRatioToParent <em>Dur Ratio To Parent</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.OperationCall#getDurRatioToRootParent <em>Dur Ratio To Root Parent</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.OperationCall#getStackDepth <em>Stack Depth</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.OperationCall#getOrderIndex <em>Order Index</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.OperationCall#isFailed <em>Failed</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.OperationCall#getFailedCause <em>Failed Cause</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.trace.TracePackage#getOperationCall()
 * @model
 * @generated
 */
public interface OperationCall extends EObject {
	/**
	 * Returns the value of the '<em><b>Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operation</em>' reference.
	 * @see #setOperation(DeployedOperation)
	 * @see kieker.model.analysismodel.trace.TracePackage#getOperationCall_Operation()
	 * @model
	 * @generated
	 */
	DeployedOperation getOperation();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.trace.OperationCall#getOperation <em>Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operation</em>' reference.
	 * @see #getOperation()
	 * @generated
	 */
	void setOperation(DeployedOperation value);

	/**
	 * Returns the value of the '<em><b>Parent</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link kieker.model.analysismodel.trace.OperationCall#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' reference.
	 * @see #setParent(OperationCall)
	 * @see kieker.model.analysismodel.trace.TracePackage#getOperationCall_Parent()
	 * @see kieker.model.analysismodel.trace.OperationCall#getChildren
	 * @model opposite="children"
	 * @generated
	 */
	OperationCall getParent();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.trace.OperationCall#getParent <em>Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(OperationCall value);

	/**
	 * Returns the value of the '<em><b>Children</b></em>' reference list.
	 * The list contents are of type {@link kieker.model.analysismodel.trace.OperationCall}.
	 * It is bidirectional and its opposite is '{@link kieker.model.analysismodel.trace.OperationCall#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' reference list.
	 * @see kieker.model.analysismodel.trace.TracePackage#getOperationCall_Children()
	 * @see kieker.model.analysismodel.trace.OperationCall#getParent
	 * @model opposite="parent"
	 * @generated
	 */
	EList<OperationCall> getChildren();

	/**
	 * Returns the value of the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duration</em>' attribute.
	 * @see #setDuration(Duration)
	 * @see kieker.model.analysismodel.trace.TracePackage#getOperationCall_Duration()
	 * @model dataType="kieker.model.analysismodel.Duration"
	 * @generated
	 */
	Duration getDuration();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.trace.OperationCall#getDuration <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duration</em>' attribute.
	 * @see #getDuration()
	 * @generated
	 */
	void setDuration(Duration value);

	/**
	 * Returns the value of the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start</em>' attribute.
	 * @see #setStart(Instant)
	 * @see kieker.model.analysismodel.trace.TracePackage#getOperationCall_Start()
	 * @model dataType="kieker.model.analysismodel.Instant"
	 * @generated
	 */
	Instant getStart();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.trace.OperationCall#getStart <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start</em>' attribute.
	 * @see #getStart()
	 * @generated
	 */
	void setStart(Instant value);

	/**
	 * Returns the value of the '<em><b>Dur Ratio To Parent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dur Ratio To Parent</em>' attribute.
	 * @see #setDurRatioToParent(float)
	 * @see kieker.model.analysismodel.trace.TracePackage#getOperationCall_DurRatioToParent()
	 * @model
	 * @generated
	 */
	float getDurRatioToParent();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.trace.OperationCall#getDurRatioToParent <em>Dur Ratio To Parent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dur Ratio To Parent</em>' attribute.
	 * @see #getDurRatioToParent()
	 * @generated
	 */
	void setDurRatioToParent(float value);

	/**
	 * Returns the value of the '<em><b>Dur Ratio To Root Parent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dur Ratio To Root Parent</em>' attribute.
	 * @see #setDurRatioToRootParent(float)
	 * @see kieker.model.analysismodel.trace.TracePackage#getOperationCall_DurRatioToRootParent()
	 * @model
	 * @generated
	 */
	float getDurRatioToRootParent();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.trace.OperationCall#getDurRatioToRootParent <em>Dur Ratio To Root Parent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dur Ratio To Root Parent</em>' attribute.
	 * @see #getDurRatioToRootParent()
	 * @generated
	 */
	void setDurRatioToRootParent(float value);

	/**
	 * Returns the value of the '<em><b>Stack Depth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Stack Depth</em>' attribute.
	 * @see #setStackDepth(int)
	 * @see kieker.model.analysismodel.trace.TracePackage#getOperationCall_StackDepth()
	 * @model
	 * @generated
	 */
	int getStackDepth();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.trace.OperationCall#getStackDepth <em>Stack Depth</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Stack Depth</em>' attribute.
	 * @see #getStackDepth()
	 * @generated
	 */
	void setStackDepth(int value);

	/**
	 * Returns the value of the '<em><b>Order Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Order Index</em>' attribute.
	 * @see #setOrderIndex(int)
	 * @see kieker.model.analysismodel.trace.TracePackage#getOperationCall_OrderIndex()
	 * @model
	 * @generated
	 */
	int getOrderIndex();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.trace.OperationCall#getOrderIndex <em>Order Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Order Index</em>' attribute.
	 * @see #getOrderIndex()
	 * @generated
	 */
	void setOrderIndex(int value);

	/**
	 * Returns the value of the '<em><b>Failed</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Failed</em>' attribute.
	 * @see #setFailed(boolean)
	 * @see kieker.model.analysismodel.trace.TracePackage#getOperationCall_Failed()
	 * @model default="false"
	 * @generated
	 */
	boolean isFailed();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.trace.OperationCall#isFailed <em>Failed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Failed</em>' attribute.
	 * @see #isFailed()
	 * @generated
	 */
	void setFailed(boolean value);

	/**
	 * Returns the value of the '<em><b>Failed Cause</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Failed Cause</em>' attribute.
	 * @see #setFailedCause(String)
	 * @see kieker.model.analysismodel.trace.TracePackage#getOperationCall_FailedCause()
	 * @model
	 * @generated
	 */
	String getFailedCause();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.trace.OperationCall#getFailedCause <em>Failed Cause</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Failed Cause</em>' attribute.
	 * @see #getFailedCause()
	 * @generated
	 */
	void setFailedCause(String value);

} // OperationCall
