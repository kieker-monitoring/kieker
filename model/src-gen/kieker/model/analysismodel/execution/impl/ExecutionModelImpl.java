/**
 */
package kieker.model.analysismodel.execution.impl;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.OperationDataflow;
import kieker.model.analysismodel.execution.StorageDataflow;
import kieker.model.analysismodel.execution.Tuple;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.execution.impl.ExecutionModelImpl#getInvocations <em>Invocations</em>}</li>
 * <li>{@link kieker.model.analysismodel.execution.impl.ExecutionModelImpl#getStorageDataflows <em>Storage Dataflows</em>}</li>
 * <li>{@link kieker.model.analysismodel.execution.impl.ExecutionModelImpl#getOperationDataflows <em>Operation Dataflows</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExecutionModelImpl extends MinimalEObjectImpl.Container implements ExecutionModel {
	/**
	 * The cached value of the '{@link #getInvocations() <em>Invocations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getInvocations()
	 * @generated
	 * @ordered
	 */
	protected EMap<Tuple<DeployedOperation, DeployedOperation>, Invocation> invocations;

	/**
	 * The cached value of the '{@link #getStorageDataflows() <em>Storage Dataflows</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getStorageDataflows()
	 * @generated
	 * @ordered
	 */
	protected EMap<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> storageDataflows;

	/**
	 * The cached value of the '{@link #getOperationDataflows() <em>Operation Dataflows</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOperationDataflows()
	 * @generated
	 * @ordered
	 */
	protected EMap<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> operationDataflows;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected ExecutionModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExecutionPackage.Literals.EXECUTION_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<Tuple<DeployedOperation, DeployedOperation>, Invocation> getInvocations() {
		if (this.invocations == null) {
			this.invocations = new EcoreEMap<>(
					ExecutionPackage.Literals.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY, DeployedOperationsPairToInvocationMapEntryImpl.class, this,
					ExecutionPackage.EXECUTION_MODEL__INVOCATIONS);
		}
		return this.invocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> getStorageDataflows() {
		if (this.storageDataflows == null) {
			this.storageDataflows = new EcoreEMap<>(
					ExecutionPackage.Literals.DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY, DeployedOperationsPairToDeployedStorageMapEntryImpl.class,
					this, ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOWS);
		}
		return this.storageDataflows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> getOperationDataflows() {
		if (this.operationDataflows == null) {
			this.operationDataflows = new EcoreEMap<>(
					ExecutionPackage.Literals.DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY,
					DeployedOperationsPairToDeployedOperationsMapEntryImpl.class, this, ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOWS);
		}
		return this.operationDataflows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(final InternalEObject otherEnd, final int featureID, final NotificationChain msgs) {
		switch (featureID) {
		case ExecutionPackage.EXECUTION_MODEL__INVOCATIONS:
			return ((InternalEList<?>) this.getInvocations()).basicRemove(otherEnd, msgs);
		case ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOWS:
			return ((InternalEList<?>) this.getStorageDataflows()).basicRemove(otherEnd, msgs);
		case ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOWS:
			return ((InternalEList<?>) this.getOperationDataflows()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
		switch (featureID) {
		case ExecutionPackage.EXECUTION_MODEL__INVOCATIONS:
			if (coreType) {
				return this.getInvocations();
			} else {
				return this.getInvocations().map();
			}
		case ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOWS:
			if (coreType) {
				return this.getStorageDataflows();
			} else {
				return this.getStorageDataflows().map();
			}
		case ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOWS:
			if (coreType) {
				return this.getOperationDataflows();
			} else {
				return this.getOperationDataflows().map();
			}
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case ExecutionPackage.EXECUTION_MODEL__INVOCATIONS:
			((EStructuralFeature.Setting) this.getInvocations()).set(newValue);
			return;
		case ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOWS:
			((EStructuralFeature.Setting) this.getStorageDataflows()).set(newValue);
			return;
		case ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOWS:
			((EStructuralFeature.Setting) this.getOperationDataflows()).set(newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eUnset(final int featureID) {
		switch (featureID) {
		case ExecutionPackage.EXECUTION_MODEL__INVOCATIONS:
			this.getInvocations().clear();
			return;
		case ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOWS:
			this.getStorageDataflows().clear();
			return;
		case ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOWS:
			this.getOperationDataflows().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean eIsSet(final int featureID) {
		switch (featureID) {
		case ExecutionPackage.EXECUTION_MODEL__INVOCATIONS:
			return (this.invocations != null) && !this.invocations.isEmpty();
		case ExecutionPackage.EXECUTION_MODEL__STORAGE_DATAFLOWS:
			return (this.storageDataflows != null) && !this.storageDataflows.isEmpty();
		case ExecutionPackage.EXECUTION_MODEL__OPERATION_DATAFLOWS:
			return (this.operationDataflows != null) && !this.operationDataflows.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // ExecutionModelImpl
