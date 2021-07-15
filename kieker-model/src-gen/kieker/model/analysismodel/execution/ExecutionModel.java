/**
 */
package kieker.model.analysismodel.execution;

import kieker.model.analysismodel.deployment.DeployedOperation;

import kieker.model.analysismodel.deployment.DeployedStorage;
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
 *   <li>{@link kieker.model.analysismodel.execution.ExecutionModel#getAggregatedInvocations <em>Aggregated Invocations</em>}</li>
 *   <li>{@link kieker.model.analysismodel.execution.ExecutionModel#getAggregatedStorageAccesses <em>Aggregated Storage Accesses</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.execution.ExecutionPackage#getExecutionModel()
 * @model
 * @generated
 */
public interface ExecutionModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Aggregated Invocations</b></em>' map.
	 * The key is of type {@link kieker.model.analysismodel.execution.Tuple<kieker.model.analysismodel.deployment.DeployedOperation, kieker.model.analysismodel.deployment.DeployedOperation>},
	 * and the value is of type {@link kieker.model.analysismodel.execution.AggregatedInvocation},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Aggregated Invocations</em>' map.
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getExecutionModel_AggregatedInvocations()
	 * @model mapType="kieker.model.analysismodel.execution.DeployedOperationsPairToAggregatedInvocationMapEntry&lt;kieker.model.analysismodel.execution.Tuple&lt;kieker.model.analysismodel.deployment.DeployedOperation, kieker.model.analysismodel.deployment.DeployedOperation&gt;, kieker.model.analysismodel.execution.AggregatedInvocation&gt;" ordered="false"
	 * @generated
	 */
	EMap<Tuple<DeployedOperation, DeployedOperation>, AggregatedInvocation> getAggregatedInvocations();

	/**
	 * Returns the value of the '<em><b>Aggregated Storage Accesses</b></em>' map.
	 * The key is of type {@link kieker.model.analysismodel.execution.Tuple<kieker.model.analysismodel.deployment.DeployedOperation, kieker.model.analysismodel.deployment.DeployedStorage>},
	 * and the value is of type {@link kieker.model.analysismodel.execution.AggregatedStorageAccess},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Aggregated Storage Accesses</em>' map.
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getExecutionModel_AggregatedStorageAccesses()
	 * @model mapType="kieker.model.analysismodel.execution.DeployedOperationsPairToAggregatedStorageAccessMapEntry&lt;kieker.model.analysismodel.execution.Tuple&lt;kieker.model.analysismodel.deployment.DeployedOperation, kieker.model.analysismodel.deployment.DeployedStorage&gt;, kieker.model.analysismodel.execution.AggregatedStorageAccess&gt;" ordered="false"
	 * @generated
	 */
	EMap<Tuple<DeployedOperation, DeployedStorage>, AggregatedStorageAccess> getAggregatedStorageAccesses();

} // ExecutionModel
