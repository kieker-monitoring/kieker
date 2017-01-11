/**
 */
package kieker.analysisteetime.model.analysismodel.execution.impl;

import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionPackage;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionRoot;

import org.apache.commons.lang3.tuple.Pair;
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
 * An implementation of the model object '<em><b>Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.execution.impl.ExecutionRootImpl#getAggregatedInvocations <em>Aggregated Invocations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExecutionRootImpl extends MinimalEObjectImpl.Container implements ExecutionRoot {
	/**
	 * The cached value of the '{@link #getAggregatedInvocations() <em>Aggregated Invocations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAggregatedInvocations()
	 * @generated
	 * @ordered
	 */
	protected EMap<Pair<DeployedOperation, DeployedOperation>, AggregatedInvocation> aggregatedInvocations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExecutionRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExecutionPackage.Literals.EXECUTION_ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap<Pair<DeployedOperation, DeployedOperation>, AggregatedInvocation> getAggregatedInvocations() {
		if (aggregatedInvocations == null) {
			aggregatedInvocations = new EcoreEMap<Pair<DeployedOperation, DeployedOperation>,AggregatedInvocation>(ExecutionPackage.Literals.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY, DeployedOperationsPairToAggregatedInvocationMapEntryImpl.class, this, ExecutionPackage.EXECUTION_ROOT__AGGREGATED_INVOCATIONS);
		}
		return aggregatedInvocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ExecutionPackage.EXECUTION_ROOT__AGGREGATED_INVOCATIONS:
				return ((InternalEList<?>)getAggregatedInvocations()).basicRemove(otherEnd, msgs);
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
			case ExecutionPackage.EXECUTION_ROOT__AGGREGATED_INVOCATIONS:
				if (coreType) return getAggregatedInvocations();
				else return getAggregatedInvocations().map();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ExecutionPackage.EXECUTION_ROOT__AGGREGATED_INVOCATIONS:
				((EStructuralFeature.Setting)getAggregatedInvocations()).set(newValue);
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
			case ExecutionPackage.EXECUTION_ROOT__AGGREGATED_INVOCATIONS:
				getAggregatedInvocations().clear();
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
			case ExecutionPackage.EXECUTION_ROOT__AGGREGATED_INVOCATIONS:
				return aggregatedInvocations != null && !aggregatedInvocations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ExecutionRootImpl
