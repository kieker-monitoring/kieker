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
 *   <li>{@link kieker.model.analysismodel.execution.impl.ExecutionModelImpl#getInvocations <em>Invocations</em>}</li>
 *   <li>{@link kieker.model.analysismodel.execution.impl.ExecutionModelImpl#getStorageDataflows <em>Storage Dataflows</em>}</li>
 *   <li>{@link kieker.model.analysismodel.execution.impl.ExecutionModelImpl#getOperationDataflows <em>Operation Dataflows</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExecutionModelImpl extends MinimalEObjectImpl.Container implements ExecutionModel {
	/**
	 * The cached value of the '{@link #getInvocations() <em>Invocations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInvocations()
	 * @generated
	 * @ordered
	 */
	protected EMap<Tuple<DeployedOperation, DeployedOperation>, Invocation> invocations;

	/**
	 * The cached value of the '{@link #getStorageDataflows() <em>Storage Dataflows</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStorageDataflows()
	 * @generated
	 * @ordered
	 */
	protected EMap<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> storageDataflows;

	/**
	 * The cached value of the '{@link #getOperationDataflows() <em>Operation Dataflows</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationDataflows()
	 * @generated
	 * @ordered
	 */
	protected EMap<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> operationDataflows;

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
	public EMap<Tuple<DeployedOperation, DeployedOperation>, Invocation> getInvocations() {
		if (invocations == null) {
			invocations = new EcoreEMap<Tuple<DeployedOperation, DeployedOperation>,Invocation>(ExecutionPackage.Literals.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY, DeployedOperationsPairToInvocationMapEntryImpl.class, this, ExecutionPackage.EXECUTION_MODEL__INVOCATIONS);
		}
		return invocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> getStorageDataflows() {
		if (storageDataflows == null) {
			storageDataflows = new EcoreEMap<Tuple<DeployedOperation, DeployedStorage>,StorageDataflow>(ExecutionPackage.Literals.DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY, DeployedOperationsPairToDeployedStorageMapEntryImpl.class, this, ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOWS);
		}
		return storageDataflows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> getOperationDataflows() {
		if (operationDataflows == null) {
			operationDataflows = new EcoreEMap<Tuple<DeployedOperation, DeployedOperation>,OperationDataflow>(ExecutionPackage.Literals.DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY, DeployedOperationsPairToDeployedOperationsMapEntryImpl.class, this, ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOWS);
		}
		return operationDataflows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ExecutionPackage.EXECUTION_MODEL__INVOCATIONS:
				return ((InternalEList<?>)getInvocations()).basicRemove(otherEnd, msgs);
			case ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOWS:
				return ((InternalEList<?>)getStorageDataflows()).basicRemove(otherEnd, msgs);
			case ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOWS:
				return ((InternalEList<?>)getOperationDataflows()).basicRemove(otherEnd, msgs);
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
			case ExecutionPackage.EXECUTION_MODEL__INVOCATIONS:
				if (coreType) return getInvocations();
				else return getInvocations().map();
			case ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOWS:
				if (coreType) return getStorageDataflows();
				else return getStorageDataflows().map();
			case ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOWS:
				if (coreType) return getOperationDataflows();
				else return getOperationDataflows().map();
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
			case ExecutionPackage.EXECUTION_MODEL__INVOCATIONS:
				((EStructuralFeature.Setting)getInvocations()).set(newValue);
				return;
			case ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOWS:
				((EStructuralFeature.Setting)getStorageDataflows()).set(newValue);
				return;
			case ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOWS:
				((EStructuralFeature.Setting)getOperationDataflows()).set(newValue);
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
			case ExecutionPackage.EXECUTION_MODEL__INVOCATIONS:
				getInvocations().clear();
				return;
			case ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOWS:
				getStorageDataflows().clear();
				return;
			case ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOWS:
				getOperationDataflows().clear();
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
			case ExecutionPackage.EXECUTION_MODEL__INVOCATIONS:
				return invocations != null && !invocations.isEmpty();
			case ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOWS:
				return storageDataflows != null && !storageDataflows.isEmpty();
			case ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOWS:
				return operationDataflows != null && !operationDataflows.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ExecutionModelImpl
