/**
 */
package kieker.model.analysismodel.execution.impl;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.execution.AggregatedInvocation;
import kieker.model.analysismodel.execution.AggregatedStorageAccess;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
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
 *   <li>{@link kieker.model.analysismodel.execution.impl.ExecutionModelImpl#getAggregatedStorageAccesses <em>Aggregated Storage Accesses</em>}</li>
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
	protected EMap<Tuple<DeployedOperation, DeployedOperation>, AggregatedInvocation> aggregatedInvocations;

	/**
	 * The cached value of the '{@link #getAggregatedStorageAccesses() <em>Aggregated Storage Accesses</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAggregatedStorageAccesses()
	 * @generated
	 * @ordered
	 */
	protected EMap<Tuple<DeployedOperation, DeployedOperation>, AggregatedStorageAccess> aggregatedStorageAccesses;

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
	public EMap<Tuple<DeployedOperation, DeployedOperation>, AggregatedInvocation> getAggregatedInvocations() {
		if (aggregatedInvocations == null) {
			aggregatedInvocations = new EcoreEMap<Tuple<DeployedOperation, DeployedOperation>,AggregatedInvocation>(ExecutionPackage.Literals.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY, DeployedOperationsPairToAggregatedInvocationMapEntryImpl.class, this, ExecutionPackage.EXECUTION_MODEL__AGGREGATED_INVOCATIONS);
		}
		return aggregatedInvocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<Tuple<DeployedOperation, DeployedOperation>, AggregatedStorageAccess> getAggregatedStorageAccesses() {
		if (aggregatedStorageAccesses == null) {
			aggregatedStorageAccesses = new EcoreEMap<Tuple<DeployedOperation, DeployedOperation>,AggregatedStorageAccess>(ExecutionPackage.Literals.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY, DeployedOperationsPairToAggregatedStorageAccessMapEntryImpl.class, this, ExecutionPackage.EXECUTION_MODEL__AGGREGATED_STORAGE_ACCESSES);
		}
		return aggregatedStorageAccesses;
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
			case ExecutionPackage.EXECUTION_MODEL__AGGREGATED_STORAGE_ACCESSES:
				return ((InternalEList<?>)getAggregatedStorageAccesses()).basicRemove(otherEnd, msgs);
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
			case ExecutionPackage.EXECUTION_MODEL__AGGREGATED_STORAGE_ACCESSES:
				if (coreType) return getAggregatedStorageAccesses();
				else return getAggregatedStorageAccesses().map();
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
			case ExecutionPackage.EXECUTION_MODEL__AGGREGATED_STORAGE_ACCESSES:
				((EStructuralFeature.Setting)getAggregatedStorageAccesses()).set(newValue);
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
			case ExecutionPackage.EXECUTION_MODEL__AGGREGATED_STORAGE_ACCESSES:
				getAggregatedStorageAccesses().clear();
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
			case ExecutionPackage.EXECUTION_MODEL__AGGREGATED_STORAGE_ACCESSES:
				return aggregatedStorageAccesses != null && !aggregatedStorageAccesses.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // ExecutionModelImpl
