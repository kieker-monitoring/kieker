/**
 */
package kieker.model.analysismodel.execution;

import org.eclipse.emf.ecore.EObject;

import kieker.model.analysismodel.deployment.DeployedOperation;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Dataflow</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.execution.OperationDataflow#getCaller <em>Caller</em>}</li>
 * <li>{@link kieker.model.analysismodel.execution.OperationDataflow#getCallee <em>Callee</em>}</li>
 * <li>{@link kieker.model.analysismodel.execution.OperationDataflow#getDirection <em>Direction</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.execution.ExecutionPackage#getOperationDataflow()
 * @model
 * @generated
 */
public interface OperationDataflow extends EObject {
	/**
	 * Returns the value of the '<em><b>Caller</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Caller</em>' reference.
	 * @see #setCaller(DeployedOperation)
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getOperationDataflow_Caller()
	 * @model
	 * @generated
	 */
	DeployedOperation getCaller();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.execution.OperationDataflow#getCaller <em>Caller</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Caller</em>' reference.
	 * @see #getCaller()
	 * @generated
	 */
	void setCaller(DeployedOperation value);

	/**
	 * Returns the value of the '<em><b>Callee</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Callee</em>' reference.
	 * @see #setCallee(DeployedOperation)
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getOperationDataflow_Callee()
	 * @model
	 * @generated
	 */
	DeployedOperation getCallee();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.execution.OperationDataflow#getCallee <em>Callee</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Callee</em>' reference.
	 * @see #getCallee()
	 * @generated
	 */
	void setCallee(DeployedOperation value);

	/**
	 * Returns the value of the '<em><b>Direction</b></em>' attribute.
	 * The literals are from the enumeration {@link kieker.model.analysismodel.execution.EDirection}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Direction</em>' attribute.
	 * @see kieker.model.analysismodel.execution.EDirection
	 * @see #setDirection(EDirection)
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getOperationDataflow_Direction()
	 * @model
	 * @generated
	 */
	EDirection getDirection();

	/**
	 * Sets the value of the '{@link kieker.model.analysismodel.execution.OperationDataflow#getDirection <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Direction</em>' attribute.
	 * @see kieker.model.analysismodel.execution.EDirection
	 * @see #getDirection()
	 * @generated
	 */
	void setDirection(EDirection value);

} // OperationDataflow
