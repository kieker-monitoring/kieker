/**
 */
package kieker.analysisteetime.model.analysismodel.execution;

import org.eclipse.emf.common.util.EList;

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
 *   <li>{@link kieker.analysisteetime.model.analysismodel.execution.ExecutionRoot#getAggregatedInvocations <em>Aggregated Invocations</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.execution.ExecutionPackage#getExecutionRoot()
 * @model
 * @generated
 */
public interface ExecutionRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Aggregated Invocations</b></em>' reference list.
	 * The list contents are of type {@link kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation}.
	 * It is bidirectional and its opposite is '{@link kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation#getExecutionRoot <em>Execution Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Aggregated Invocations</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Aggregated Invocations</em>' reference list.
	 * @see kieker.analysisteetime.model.analysismodel.execution.ExecutionPackage#getExecutionRoot_AggregatedInvocations()
	 * @see kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation#getExecutionRoot
	 * @model opposite="executionRoot"
	 * @generated
	 */
	EList<AggregatedInvocation> getAggregatedInvocations();

} // ExecutionRoot
