/**
 */
package kieker.tools.restructuring.restructuremodel.util;

import kieker.tools.restructuring.restructuremodel.*;

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
 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage
 * @generated
 */
public class RestructuremodelSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static RestructuremodelPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RestructuremodelSwitch() {
		if (modelPackage == null) {
			modelPackage = RestructuremodelPackage.eINSTANCE;
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
			case RestructuremodelPackage.TRANSFORMATION_MODEL: {
				TransformationModel transformationModel = (TransformationModel)theEObject;
				T result = caseTransformationModel(transformationModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RestructuremodelPackage.ABSTRACT_TRANSFORMATION_STEP: {
				AbstractTransformationStep abstractTransformationStep = (AbstractTransformationStep)theEObject;
				T result = caseAbstractTransformationStep(abstractTransformationStep);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RestructuremodelPackage.CREATE_COMPONENT: {
				CreateComponent createComponent = (CreateComponent)theEObject;
				T result = caseCreateComponent(createComponent);
				if (result == null) result = caseAbstractTransformationStep(createComponent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RestructuremodelPackage.DELETE_COMPONENT: {
				DeleteComponent deleteComponent = (DeleteComponent)theEObject;
				T result = caseDeleteComponent(deleteComponent);
				if (result == null) result = caseAbstractTransformationStep(deleteComponent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RestructuremodelPackage.CUT_OPERATION: {
				CutOperation cutOperation = (CutOperation)theEObject;
				T result = caseCutOperation(cutOperation);
				if (result == null) result = caseAbstractTransformationStep(cutOperation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RestructuremodelPackage.PASTE_OPERATION: {
				PasteOperation pasteOperation = (PasteOperation)theEObject;
				T result = casePasteOperation(pasteOperation);
				if (result == null) result = caseAbstractTransformationStep(pasteOperation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RestructuremodelPackage.MOVE_OPERATION: {
				MoveOperation moveOperation = (MoveOperation)theEObject;
				T result = caseMoveOperation(moveOperation);
				if (result == null) result = caseAbstractTransformationStep(moveOperation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RestructuremodelPackage.MERGE_COMPONENT: {
				MergeComponent mergeComponent = (MergeComponent)theEObject;
				T result = caseMergeComponent(mergeComponent);
				if (result == null) result = caseAbstractTransformationStep(mergeComponent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RestructuremodelPackage.SPLIT_COMPONENT: {
				SplitComponent splitComponent = (SplitComponent)theEObject;
				T result = caseSplitComponent(splitComponent);
				if (result == null) result = caseAbstractTransformationStep(splitComponent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Transformation Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Transformation Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTransformationModel(TransformationModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Abstract Transformation Step</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract Transformation Step</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAbstractTransformationStep(AbstractTransformationStep object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Create Component</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Create Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCreateComponent(CreateComponent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Delete Component</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Delete Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDeleteComponent(DeleteComponent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cut Operation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cut Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCutOperation(CutOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Paste Operation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Paste Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePasteOperation(PasteOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Move Operation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Move Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMoveOperation(MoveOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Merge Component</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Merge Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMergeComponent(MergeComponent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Split Component</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Split Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSplitComponent(SplitComponent object) {
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

} //RestructuremodelSwitch
