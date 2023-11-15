/**
 */
package kieker.tools.restructuring.restructuremodel.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

import kieker.tools.restructuring.restructuremodel.AbstractTransformationStep;
import kieker.tools.restructuring.restructuremodel.CreateComponent;
import kieker.tools.restructuring.restructuremodel.CutOperation;
import kieker.tools.restructuring.restructuremodel.DeleteComponent;
import kieker.tools.restructuring.restructuremodel.MergeComponent;
import kieker.tools.restructuring.restructuremodel.MoveOperation;
import kieker.tools.restructuring.restructuremodel.PasteOperation;
import kieker.tools.restructuring.restructuremodel.RestructuremodelPackage;
import kieker.tools.restructuring.restructuremodel.SplitComponent;
import kieker.tools.restructuring.restructuremodel.TransformationModel;

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
 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage
 * @generated
 */
public class RestructuremodelSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected static RestructuremodelPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
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
		case RestructuremodelPackage.TRANSFORMATION_MODEL: {
			final TransformationModel transformationModel = (TransformationModel) theEObject;
			T result = this.caseTransformationModel(transformationModel);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case RestructuremodelPackage.ABSTRACT_TRANSFORMATION_STEP: {
			final AbstractTransformationStep abstractTransformationStep = (AbstractTransformationStep) theEObject;
			T result = this.caseAbstractTransformationStep(abstractTransformationStep);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case RestructuremodelPackage.CREATE_COMPONENT: {
			final CreateComponent createComponent = (CreateComponent) theEObject;
			T result = this.caseCreateComponent(createComponent);
			if (result == null) {
				result = this.caseAbstractTransformationStep(createComponent);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case RestructuremodelPackage.DELETE_COMPONENT: {
			final DeleteComponent deleteComponent = (DeleteComponent) theEObject;
			T result = this.caseDeleteComponent(deleteComponent);
			if (result == null) {
				result = this.caseAbstractTransformationStep(deleteComponent);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case RestructuremodelPackage.CUT_OPERATION: {
			final CutOperation cutOperation = (CutOperation) theEObject;
			T result = this.caseCutOperation(cutOperation);
			if (result == null) {
				result = this.caseAbstractTransformationStep(cutOperation);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case RestructuremodelPackage.PASTE_OPERATION: {
			final PasteOperation pasteOperation = (PasteOperation) theEObject;
			T result = this.casePasteOperation(pasteOperation);
			if (result == null) {
				result = this.caseAbstractTransformationStep(pasteOperation);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case RestructuremodelPackage.MOVE_OPERATION: {
			final MoveOperation moveOperation = (MoveOperation) theEObject;
			T result = this.caseMoveOperation(moveOperation);
			if (result == null) {
				result = this.caseAbstractTransformationStep(moveOperation);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case RestructuremodelPackage.MERGE_COMPONENT: {
			final MergeComponent mergeComponent = (MergeComponent) theEObject;
			T result = this.caseMergeComponent(mergeComponent);
			if (result == null) {
				result = this.caseAbstractTransformationStep(mergeComponent);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case RestructuremodelPackage.SPLIT_COMPONENT: {
			final SplitComponent splitComponent = (SplitComponent) theEObject;
			T result = this.caseSplitComponent(splitComponent);
			if (result == null) {
				result = this.caseAbstractTransformationStep(splitComponent);
			}
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
	 * Returns the result of interpreting the object as an instance of '<em>Transformation Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Transformation Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTransformationModel(final TransformationModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Abstract Transformation Step</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract Transformation Step</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAbstractTransformationStep(final AbstractTransformationStep object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Create Component</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Create Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCreateComponent(final CreateComponent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Delete Component</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Delete Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDeleteComponent(final DeleteComponent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cut Operation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cut Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCutOperation(final CutOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Paste Operation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Paste Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePasteOperation(final PasteOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Move Operation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Move Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMoveOperation(final MoveOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Merge Component</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Merge Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMergeComponent(final MergeComponent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Split Component</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Split Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSplitComponent(final SplitComponent object) {
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

} // RestructuremodelSwitch
