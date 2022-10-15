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
 *   <li>{@link kieker.model.analysismodel.execution.ExecutionModel#getInvocations <em>Invocations</em>}</li>
 *   <li>{@link kieker.model.analysismodel.execution.ExecutionModel#getStorageDataflow <em>Storage Dataflow</em>}</li>
 *   <li>{@link kieker.model.analysismodel.execution.ExecutionModel#getOperationDataflow <em>Operation Dataflow</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.execution.ExecutionPackage#getExecutionModel()
 * @model
 * @generated
 */
public interface ExecutionModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Invocations</b></em>' map.
	 * The key is of type {@link kieker.model.analysismodel.execution.Tuple<kieker.model.analysismodel.deployment.DeployedOperation, kieker.model.analysismodel.deployment.DeployedOperation>},
	 * and the value is of type {@link kieker.model.analysismodel.execution.Invocation},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Invocations</em>' map.
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getExecutionModel_Invocations()
	 * @model mapType="kieker.model.analysismodel.execution.DeployedOperationsPairToInvocationMapEntry&lt;kieker.model.analysismodel.execution.Tuple&lt;kieker.model.analysismodel.deployment.DeployedOperation, kieker.model.analysismodel.deployment.DeployedOperation&gt;, kieker.model.analysismodel.execution.Invocation&gt;" ordered="false"
	 * @generated
	 */
	EMap<Tuple<DeployedOperation, DeployedOperation>, Invocation> getInvocations();

	/**
	 * Returns the value of the '<em><b>Storage Dataflow</b></em>' map.
	 * The key is of type {@link kieker.model.analysismodel.execution.Tuple<kieker.model.analysismodel.deployment.DeployedOperation, kieker.model.analysismodel.deployment.DeployedStorage>},
	 * and the value is of type {@link kieker.model.analysismodel.execution.StorageDataflow},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Storage Dataflow</em>' map.
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getExecutionModel_StorageDataflow()
	 * @model mapType="kieker.model.analysismodel.execution.DeployedOperationsPairToDeployedStorageMapEntry&lt;kieker.model.analysismodel.execution.Tuple&lt;kieker.model.analysismodel.deployment.DeployedOperation, kieker.model.analysismodel.deployment.DeployedStorage&gt;, kieker.model.analysismodel.execution.StorageDataflow&gt;" ordered="false"
	 * @generated
	 */
	EMap<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> getStorageDataflow();

	/**
	 * Returns the value of the '<em><b>Operation Dataflow</b></em>' map.
	 * The key is of type {@link kieker.model.analysismodel.execution.Tuple<kieker.model.analysismodel.deployment.DeployedOperation, kieker.model.analysismodel.deployment.DeployedOperation>},
	 * and the value is of type {@link kieker.model.analysismodel.execution.OperationDataflow},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operation Dataflow</em>' map.
	 * @see kieker.model.analysismodel.execution.ExecutionPackage#getExecutionModel_OperationDataflow()
	 * @model mapType="kieker.model.analysismodel.execution.DeployedOperationsPairToDeployedOperationsMapEntry&lt;kieker.model.analysismodel.execution.Tuple&lt;kieker.model.analysismodel.deployment.DeployedOperation, kieker.model.analysismodel.deployment.DeployedOperation&gt;, kieker.model.analysismodel.execution.OperationDataflow&gt;" ordered="false"
	 * @generated
	 */
	EMap<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> getOperationDataflow();

} // ExecutionModel
