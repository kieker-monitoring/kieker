/**
 */
package kieker.model.analysismodel.deployment.util;

import java.util.Map;

import kieker.model.analysismodel.deployment.*;

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
 * @see kieker.model.analysismodel.deployment.DeploymentPackage
 * @generated
 */
public class DeploymentSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static DeploymentPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeploymentSwitch() {
		if (modelPackage == null) {
			modelPackage = DeploymentPackage.eINSTANCE;
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
			case DeploymentPackage.DEPLOYMENT_MODEL: {
				DeploymentModel deploymentModel = (DeploymentModel)theEObject;
				T result = caseDeploymentModel(deploymentModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DeploymentPackage.ESTRING_TO_DEPLOYMENT_CONTEXT_MAP_ENTRY: {
				@SuppressWarnings("unchecked") Map.Entry<String, DeploymentContext> eStringToDeploymentContextMapEntry = (Map.Entry<String, DeploymentContext>)theEObject;
				T result = caseEStringToDeploymentContextMapEntry(eStringToDeploymentContextMapEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DeploymentPackage.DEPLOYMENT_CONTEXT: {
				DeploymentContext deploymentContext = (DeploymentContext)theEObject;
				T result = caseDeploymentContext(deploymentContext);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DeploymentPackage.ESTRING_TO_DEPLOYED_COMPONENT_MAP_ENTRY: {
				@SuppressWarnings("unchecked") Map.Entry<String, DeployedComponent> eStringToDeployedComponentMapEntry = (Map.Entry<String, DeployedComponent>)theEObject;
				T result = caseEStringToDeployedComponentMapEntry(eStringToDeployedComponentMapEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DeploymentPackage.DEPLOYED_COMPONENT: {
				DeployedComponent deployedComponent = (DeployedComponent)theEObject;
				T result = caseDeployedComponent(deployedComponent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DeploymentPackage.ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY: {
				@SuppressWarnings("unchecked") Map.Entry<String, DeployedOperation> eStringToDeployedOperationMapEntry = (Map.Entry<String, DeployedOperation>)theEObject;
				T result = caseEStringToDeployedOperationMapEntry(eStringToDeployedOperationMapEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DeploymentPackage.DEPLOYED_OPERATION: {
				DeployedOperation deployedOperation = (DeployedOperation)theEObject;
				T result = caseDeployedOperation(deployedOperation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DeploymentPackage.ESTRING_TO_DEPLOYED_STORAGE_MAP_ENTRY: {
				@SuppressWarnings("unchecked") Map.Entry<String, DeployedStorage> eStringToDeployedStorageMapEntry = (Map.Entry<String, DeployedStorage>)theEObject;
				T result = caseEStringToDeployedStorageMapEntry(eStringToDeployedStorageMapEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DeploymentPackage.DEPLOYED_STORAGE: {
				DeployedStorage deployedStorage = (DeployedStorage)theEObject;
				T result = caseDeployedStorage(deployedStorage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DeploymentPackage.DEPLOYED_PROVIDED_INTERFACE: {
				DeployedProvidedInterface deployedProvidedInterface = (DeployedProvidedInterface)theEObject;
				T result = caseDeployedProvidedInterface(deployedProvidedInterface);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DeploymentPackage.ESTRING_TO_DEPLOYED_PROVIDED_INTERFACE_MAP_ENTRY: {
				@SuppressWarnings("unchecked") Map.Entry<String, DeployedProvidedInterface> eStringToDeployedProvidedInterfaceMapEntry = (Map.Entry<String, DeployedProvidedInterface>)theEObject;
				T result = caseEStringToDeployedProvidedInterfaceMapEntry(eStringToDeployedProvidedInterfaceMapEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DeploymentPackage.DEPLOYED_REQUIRED_INTERFACE: {
				DeployedRequiredInterface deployedRequiredInterface = (DeployedRequiredInterface)theEObject;
				T result = caseDeployedRequiredInterface(deployedRequiredInterface);
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
	public T caseDeploymentModel(DeploymentModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EString To Deployment Context Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EString To Deployment Context Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEStringToDeploymentContextMapEntry(Map.Entry<String, DeploymentContext> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Context</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Context</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDeploymentContext(DeploymentContext object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EString To Deployed Component Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EString To Deployed Component Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEStringToDeployedComponentMapEntry(Map.Entry<String, DeployedComponent> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Deployed Component</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Deployed Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDeployedComponent(DeployedComponent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EString To Deployed Operation Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EString To Deployed Operation Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEStringToDeployedOperationMapEntry(Map.Entry<String, DeployedOperation> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Deployed Operation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Deployed Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDeployedOperation(DeployedOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EString To Deployed Storage Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EString To Deployed Storage Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEStringToDeployedStorageMapEntry(Map.Entry<String, DeployedStorage> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Deployed Storage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Deployed Storage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDeployedStorage(DeployedStorage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Deployed Provided Interface</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Deployed Provided Interface</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDeployedProvidedInterface(DeployedProvidedInterface object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EString To Deployed Provided Interface Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EString To Deployed Provided Interface Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEStringToDeployedProvidedInterfaceMapEntry(Map.Entry<String, DeployedProvidedInterface> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Deployed Required Interface</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Deployed Required Interface</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDeployedRequiredInterface(DeployedRequiredInterface object) {
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

} //DeploymentSwitch
