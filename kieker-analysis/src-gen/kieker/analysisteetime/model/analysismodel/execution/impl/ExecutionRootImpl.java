/**
 */
package kieker.analysisteetime.model.analysismodel.execution.impl;

import java.util.Collection;

import kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionPackage;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionRoot;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
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
	 * The cached value of the '{@link #getAggregatedInvocations() <em>Aggregated Invocations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAggregatedInvocations()
	 * @generated
	 * @ordered
	 */
	protected EList<AggregatedInvocation> aggregatedInvocations;

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
	public EList<AggregatedInvocation> getAggregatedInvocations() {
		if (aggregatedInvocations == null) {
			aggregatedInvocations = new EObjectContainmentWithInverseEList<AggregatedInvocation>(AggregatedInvocation.class, this, ExecutionPackage.EXECUTION_ROOT__AGGREGATED_INVOCATIONS, ExecutionPackage.AGGREGATED_INVOCATION__EXECUTION_ROOT);
		}
		return aggregatedInvocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ExecutionPackage.EXECUTION_ROOT__AGGREGATED_INVOCATIONS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getAggregatedInvocations()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
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
				return getAggregatedInvocations();
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
				getAggregatedInvocations().clear();
				getAggregatedInvocations().addAll((Collection<? extends AggregatedInvocation>)newValue);
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
