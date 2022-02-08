/**
 */
package kieker.model.collection.util;

import java.util.Map;

import kieker.model.analysismodel.type.OperationType;

import kieker.model.collection.*;

import org.eclipse.emf.common.util.EMap;

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
 * @see kieker.model.collection.CollectionPackage
 * @generated
 */
public class CollectionSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static CollectionPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CollectionSwitch() {
		if (modelPackage == null) {
			modelPackage = CollectionPackage.eINSTANCE;
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
			case CollectionPackage.INTERFACE_COLLECTION: {
				InterfaceCollection interfaceCollection = (InterfaceCollection)theEObject;
				T result = caseInterfaceCollection(interfaceCollection);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CollectionPackage.OPERATION_COLLECTION: {
				OperationCollection operationCollection = (OperationCollection)theEObject;
				T result = caseOperationCollection(operationCollection);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CollectionPackage.COUPLING_TO_OPERATION_MAP: {
				@SuppressWarnings("unchecked") Map.Entry<Coupling, EMap<String, OperationType>> couplingToOperationMap = (Map.Entry<Coupling, EMap<String, OperationType>>)theEObject;
				T result = caseCouplingToOperationMap(couplingToOperationMap);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CollectionPackage.NAME_TO_OPERATION_MAP: {
				@SuppressWarnings("unchecked") Map.Entry<String, OperationType> nameToOperationMap = (Map.Entry<String, OperationType>)theEObject;
				T result = caseNameToOperationMap(nameToOperationMap);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case CollectionPackage.COUPLING: {
				Coupling coupling = (Coupling)theEObject;
				T result = caseCoupling(coupling);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Interface Collection</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Interface Collection</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInterfaceCollection(InterfaceCollection object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operation Collection</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operation Collection</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOperationCollection(OperationCollection object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Coupling To Operation Map</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Coupling To Operation Map</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCouplingToOperationMap(Map.Entry<Coupling, EMap<String, OperationType>> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Name To Operation Map</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Name To Operation Map</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNameToOperationMap(Map.Entry<String, OperationType> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Coupling</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Coupling</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCoupling(Coupling object) {
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

} //CollectionSwitch
