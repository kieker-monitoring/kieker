/**
 */
package kieker.model.analysismodel.execution;

import kieker.analysis.util.ComposedKey;
import kieker.model.analysismodel.deployment.DeployedOperation;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.execution.ExecutionModel#getAggregatedInvocations <em>Aggregated Invocations</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.execution.ExecutionPackage#getExecutionModel()
 * @model
 * @generated
 */
public interface ExecutionModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Aggregated Invocations</b></em>' map.
	 * The key is of type {@link kieker.analysisteetime.util.ComposedKey<kieker.model.analysismodel.deployment.DeployedOperation,
	 * kieker.model.analysismodel.deployment.DeployedOperation>},
	 * and the value is of type {@link kieker.model.analysismodel.execution.AggregatedInvocation},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Aggregated Invocations</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Aggregated Invocations</em>' map.
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getExecutionModel_AggregatedInvocations()
	 * @model mapType="kieker.model.analysismodel.execution.DeployedOperationsPairToAggregatedInvocationMapEntry<kieker.model.analysismodel.ComposedKey<kieker.model.analysismodel.deployment.DeployedOperation,
	 *        kieker.model.analysismodel.deployment.DeployedOperation>, kieker.model.analysismodel.execution.AggregatedInvocation>"
	 *        ordered="false"
	 * @generated
	 */
	EMap<ComposedKey<DeployedOperation, DeployedOperation>, AggregatedInvocation> getAggregatedInvocations();

} // ExecutionModel
