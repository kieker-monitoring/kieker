/**
 */
package kieker.model.analysismodel.assembly.util;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import kieker.model.analysismodel.assembly.*;

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
 * @see kieker.model.analysismodel.assembly.AssemblyPackage
 * @generated
 */
public class AssemblySwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static AssemblyPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssemblySwitch() {
		if (modelPackage == null) {
			modelPackage = AssemblyPackage.eINSTANCE;
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
			case AssemblyPackage.ASSEMBLY_MODEL: {
				AssemblyModel assemblyModel = (AssemblyModel)theEObject;
				T result = caseAssemblyModel(assemblyModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY: {
				@SuppressWarnings("unchecked") Map.Entry<String, AssemblyComponent> eStringToAssemblyComponentMapEntry = (Map.Entry<String, AssemblyComponent>)theEObject;
				T result = caseEStringToAssemblyComponentMapEntry(eStringToAssemblyComponentMapEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AssemblyPackage.ASSEMBLY_COMPONENT: {
				AssemblyComponent assemblyComponent = (AssemblyComponent)theEObject;
				T result = caseAssemblyComponent(assemblyComponent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AssemblyPackage.ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY: {
				@SuppressWarnings("unchecked") Map.Entry<String, AssemblyOperation> eStringToAssemblyOperationMapEntry = (Map.Entry<String, AssemblyOperation>)theEObject;
				T result = caseEStringToAssemblyOperationMapEntry(eStringToAssemblyOperationMapEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AssemblyPackage.ASSEMBLY_OPERATION: {
				AssemblyOperation assemblyOperation = (AssemblyOperation)theEObject;
				T result = caseAssemblyOperation(assemblyOperation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AssemblyPackage.ASSEMBLY_STORAGE: {
				AssemblyStorage assemblyStorage = (AssemblyStorage)theEObject;
				T result = caseAssemblyStorage(assemblyStorage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AssemblyPackage.ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY: {
				@SuppressWarnings("unchecked") Map.Entry<String, AssemblyStorage> eStringToAssemblyStorageMapEntry = (Map.Entry<String, AssemblyStorage>)theEObject;
				T result = caseEStringToAssemblyStorageMapEntry(eStringToAssemblyStorageMapEntry);
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
	public T caseAssemblyModel(AssemblyModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EString To Assembly Component Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EString To Assembly Component Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEStringToAssemblyComponentMapEntry(Map.Entry<String, AssemblyComponent> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Component</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAssemblyComponent(AssemblyComponent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EString To Assembly Operation Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EString To Assembly Operation Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEStringToAssemblyOperationMapEntry(Map.Entry<String, AssemblyOperation> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAssemblyOperation(AssemblyOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Storage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Storage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAssemblyStorage(AssemblyStorage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EString To Assembly Storage Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EString To Assembly Storage Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEStringToAssemblyStorageMapEntry(Map.Entry<String, AssemblyStorage> object) {
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

} // AssemblySwitch
