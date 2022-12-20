/**
 */
package kieker.model.analysismodel.execution.util;

import java.util.Map;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;

import kieker.model.analysismodel.execution.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see kieker.model.analysismodel.execution.ExecutionPackage
 * @generated
 */
public class ExecutionSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ExecutionPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExecutionSwitch() {
		if (modelPackage == null) {
			modelPackage = ExecutionPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case ExecutionPackage.EXECUTION_MODEL: {
				ExecutionModel executionModel = (ExecutionModel)theEObject;
				T result = caseExecutionModel(executionModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY: {
				@SuppressWarnings("unchecked") Map.Entry<Tuple<DeployedOperation, DeployedOperation>, Invocation> deployedOperationsPairToInvocationMapEntry = (Map.Entry<Tuple<DeployedOperation, DeployedOperation>, Invocation>)theEObject;
				T result = caseDeployedOperationsPairToInvocationMapEntry(deployedOperationsPairToInvocationMapEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExecutionPackage.INVOCATION: {
				Invocation invocation = (Invocation)theEObject;
				T result = caseInvocation(invocation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExecutionPackage.STORAGE_DATAFLOW: {
				StorageDataflow storageDataflow = (StorageDataflow)theEObject;
				T result = caseStorageDataflow(storageDataflow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY: {
				@SuppressWarnings("unchecked") Map.Entry<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> deployedOperationsPairToDeployedStorageMapEntry = (Map.Entry<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow>)theEObject;
				T result = caseDeployedOperationsPairToDeployedStorageMapEntry(deployedOperationsPairToDeployedStorageMapEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExecutionPackage.TUPLE: {
				Tuple<?, ?> tuple = (Tuple<?, ?>)theEObject;
				T result = caseTuple(tuple);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExecutionPackage.OPERATION_DATAFLOW: {
				OperationDataflow operationDataflow = (OperationDataflow)theEObject;
				T result = caseOperationDataflow(operationDataflow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY: {
				@SuppressWarnings("unchecked") Map.Entry<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> deployedOperationsPairToDeployedOperationsMapEntry = (Map.Entry<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow>)theEObject;
				T result = caseDeployedOperationsPairToDeployedOperationsMapEntry(deployedOperationsPairToDeployedOperationsMapEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExecutionModel(ExecutionModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Deployed Operations Pair To Invocation Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Deployed Operations Pair To Invocation Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDeployedOperationsPairToInvocationMapEntry(Map.Entry<Tuple<DeployedOperation, DeployedOperation>, Invocation> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Invocation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Invocation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInvocation(Invocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Storage Dataflow</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Storage Dataflow</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStorageDataflow(StorageDataflow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Deployed Operations Pair To Deployed Storage Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Deployed Operations Pair To Deployed Storage Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDeployedOperationsPairToDeployedStorageMapEntry(Map.Entry<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tuple</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tuple</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <F, S> T caseTuple(Tuple<F, S> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operation Dataflow</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operation Dataflow</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOperationDataflow(OperationDataflow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Deployed Operations Pair To Deployed Operations Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Deployed Operations Pair To Deployed Operations Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDeployedOperationsPairToDeployedOperationsMapEntry(Map.Entry<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //ExecutionSwitch
