/**
 */
package kieker.model.analysismodel.execution.impl;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.OperationDataflow;
import kieker.model.analysismodel.execution.StorageDataflow;
import kieker.model.analysismodel.execution.Tuple;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.execution.impl.ExecutionModelImpl#getAggregatedInvocations <em>Aggregated Invocations</em>}</li>
 *   <li>{@link kieker.model.analysismodel.execution.impl.ExecutionModelImpl#getStorageDataflow <em>Storage Dataflow</em>}</li>
 *   <li>{@link kieker.model.analysismodel.execution.impl.ExecutionModelImpl#getOperationDataflow <em>Operation Dataflow</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExecutionModelImpl extends MinimalEObjectImpl.Container implements ExecutionModel {
	/**
	 * The cached value of the '{@link #getAggregatedInvocations() <em>Aggregated Invocations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAggregatedInvocations()
	 * @generated
	 * @ordered
	 */
	protected EMap<Tuple<DeployedOperation, DeployedOperation>, Invocation> aggregatedInvocations;

	/**
	 * The cached value of the '{@link #getStorageDataflow() <em>Storage Dataflow</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStorageDataflow()
	 * @generated
	 * @ordered
	 */
	protected EMap<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> storageDataflow;

	/**
	 * The cached value of the '{@link #getOperationDataflow() <em>Operation Dataflow</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationDataflow()
	 * @generated
	 * @ordered
	 */
	protected EMap<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> operationDataflow;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExecutionModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExecutionPackage.Literals.EXECUTION_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<Tuple<DeployedOperation, DeployedOperation>, Invocation> getAggregatedInvocations() {
		if (aggregatedInvocations == null) {
			aggregatedInvocations = new EcoreEMap<Tuple<DeployedOperation, DeployedOperation>,Invocation>(ExecutionPackage.Literals.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY, DeployedOperationsPairToAggregatedInvocationMapEntryImpl.class, this, ExecutionPackage.EXECUTION_MODEL__AGGREGATED_INVOCATIONS);
		}
		return aggregatedInvocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> getStorageDataflow() {
		if (storageDataflow == null) {
			storageDataflow = new EcoreEMap<Tuple<DeployedOperation, DeployedStorage>,StorageDataflow>(ExecutionPackage.Literals.DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY, DeployedOperationsPairToDeployedStorageMapEntryImpl.class, this, ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOW);
		}
		return storageDataflow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> getOperationDataflow() {
		if (operationDataflow == null) {
			operationDataflow = new EcoreEMap<Tuple<DeployedOperation, DeployedOperation>,OperationDataflow>(ExecutionPackage.Literals.DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY, DeployedOperationsPairToDeployedOperationsMapEntryImpl.class, this, ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOW);
		}
		return operationDataflow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ExecutionPackage.EXECUTION_MODEL__AGGREGATED_INVOCATIONS:
				return ((InternalEList<?>)getAggregatedInvocations()).basicRemove(otherEnd, msgs);
			case ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOW:
				return ((InternalEList<?>)getStorageDataflow()).basicRemove(otherEnd, msgs);
			case ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOW:
				return ((InternalEList<?>)getOperationDataflow()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ExecutionPackage.EXECUTION_MODEL__AGGREGATED_INVOCATIONS:
				if (coreType) return getAggregatedInvocations();
				else return getAggregatedInvocations().map();
			case ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOW:
				if (coreType) return getStorageDataflow();
				else return getStorageDataflow().map();
			case ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOW:
				if (coreType) return getOperationDataflow();
				else return getOperationDataflow().map();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ExecutionPackage.EXECUTION_MODEL__AGGREGATED_INVOCATIONS:
				((EStructuralFeature.Setting)getAggregatedInvocations()).set(newValue);
				return;
			case ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOW:
				((EStructuralFeature.Setting)getStorageDataflow()).set(newValue);
				return;
			case ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOW:
				((EStructuralFeature.Setting)getOperationDataflow()).set(newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ExecutionPackage.EXECUTION_MODEL__AGGREGATED_INVOCATIONS:
				getAggregatedInvocations().clear();
				return;
			case ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOW:
				getStorageDataflow().clear();
				return;
			case ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOW:
				getOperationDataflow().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ExecutionPackage.EXECUTION_MODEL__AGGREGATED_INVOCATIONS:
				return aggregatedInvocations != null && !aggregatedInvocations.isEmpty();
			case ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOW:
				return storageDataflow != null && !storageDataflow.isEmpty();
			case ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOW:
				return operationDataflow != null && !operationDataflow.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ExecutionModelImpl
