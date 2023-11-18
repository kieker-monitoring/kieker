/**
 */
package kieker.model.analysismodel.type.util;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.ProvidedInterfaceType;
import kieker.model.analysismodel.type.RequiredInterfaceType;
import kieker.model.analysismodel.type.StorageType;
import kieker.model.analysismodel.type.TypeModel;
import kieker.model.analysismodel.type.TypePackage;

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
 *
 * @see kieker.model.analysismodel.type.TypePackage
 * @generated
 */
public class TypeSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected static TypePackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public TypeSwitch() {
		if (modelPackage == null) {
			modelPackage = TypePackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param ePackage
	 *            the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(final EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(final int classifierID, final EObject theEObject) {
		switch (classifierID) {
		case TypePackage.TYPE_MODEL: {
			final TypeModel typeModel = (TypeModel) theEObject;
			T result = this.caseTypeModel(typeModel);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case TypePackage.ESTRING_TO_COMPONENT_TYPE_MAP_ENTRY: {
			@SuppressWarnings("unchecked")
			final Map.Entry<String, ComponentType> eStringToComponentTypeMapEntry = (Map.Entry<String, ComponentType>) theEObject;
			T result = this.caseEStringToComponentTypeMapEntry(eStringToComponentTypeMapEntry);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case TypePackage.COMPONENT_TYPE: {
			final ComponentType componentType = (ComponentType) theEObject;
			T result = this.caseComponentType(componentType);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case TypePackage.ESTRING_TO_OPERATION_TYPE_MAP_ENTRY: {
			@SuppressWarnings("unchecked")
			final Map.Entry<String, OperationType> eStringToOperationTypeMapEntry = (Map.Entry<String, OperationType>) theEObject;
			T result = this.caseEStringToOperationTypeMapEntry(eStringToOperationTypeMapEntry);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case TypePackage.ESTRING_TO_STORAGE_TYPE_MAP_ENTRY: {
			@SuppressWarnings("unchecked")
			final Map.Entry<String, StorageType> eStringToStorageTypeMapEntry = (Map.Entry<String, StorageType>) theEObject;
			T result = this.caseEStringToStorageTypeMapEntry(eStringToStorageTypeMapEntry);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case TypePackage.OPERATION_TYPE: {
			final OperationType operationType = (OperationType) theEObject;
			T result = this.caseOperationType(operationType);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case TypePackage.STORAGE_TYPE: {
			final StorageType storageType = (StorageType) theEObject;
			T result = this.caseStorageType(storageType);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case TypePackage.PROVIDED_INTERFACE_TYPE: {
			final ProvidedInterfaceType providedInterfaceType = (ProvidedInterfaceType) theEObject;
			T result = this.caseProvidedInterfaceType(providedInterfaceType);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case TypePackage.ESTRING_TO_PROVIDED_INTERFACE_TYPE_MAP_ENTRY: {
			@SuppressWarnings("unchecked")
			final Map.Entry<String, ProvidedInterfaceType> eStringToProvidedInterfaceTypeMapEntry = (Map.Entry<String, ProvidedInterfaceType>) theEObject;
			T result = this.caseEStringToProvidedInterfaceTypeMapEntry(eStringToProvidedInterfaceTypeMapEntry);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case TypePackage.REQUIRED_INTERFACE_TYPE: {
			final RequiredInterfaceType requiredInterfaceType = (RequiredInterfaceType) theEObject;
			T result = this.caseRequiredInterfaceType(requiredInterfaceType);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case TypePackage.INTERFACE_ESTRING_TO_OPERATION_TYPE_MAP_ENTRY: {
			@SuppressWarnings("unchecked")
			final Map.Entry<String, OperationType> interfaceEStringToOperationTypeMapEntry = (Map.Entry<String, OperationType>) theEObject;
			T result = this.caseInterfaceEStringToOperationTypeMapEntry(interfaceEStringToOperationTypeMapEntry);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		default:
			return this.defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTypeModel(final TypeModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EString To Component Type Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EString To Component Type Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEStringToComponentTypeMapEntry(final Map.Entry<String, ComponentType> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Component Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Component Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseComponentType(final ComponentType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EString To Operation Type Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EString To Operation Type Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEStringToOperationTypeMapEntry(final Map.Entry<String, OperationType> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EString To Storage Type Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EString To Storage Type Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEStringToStorageTypeMapEntry(final Map.Entry<String, StorageType> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operation Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operation Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOperationType(final OperationType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Storage Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Storage Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStorageType(final StorageType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Provided Interface Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Provided Interface Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProvidedInterfaceType(final ProvidedInterfaceType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EString To Provided Interface Type Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EString To Provided Interface Type Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEStringToProvidedInterfaceTypeMapEntry(final Map.Entry<String, ProvidedInterfaceType> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Required Interface Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Required Interface Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRequiredInterfaceType(final RequiredInterfaceType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Interface EString To Operation Type Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Interface EString To Operation Type Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInterfaceEStringToOperationTypeMapEntry(final Map.Entry<String, OperationType> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(final EObject object) {
		return null;
	}

} // TypeSwitch
