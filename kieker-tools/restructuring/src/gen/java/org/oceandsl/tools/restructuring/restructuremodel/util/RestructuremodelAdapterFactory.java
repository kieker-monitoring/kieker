/**
 */
package org.oceandsl.tools.restructuring.restructuremodel.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.oceandsl.tools.restructuring.restructuremodel.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.oceandsl.tools.restructuring.restructuremodel.RestructuremodelPackage
 * @generated
 */
public class RestructuremodelAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static RestructuremodelPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RestructuremodelAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = RestructuremodelPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RestructuremodelSwitch<Adapter> modelSwitch =
        new RestructuremodelSwitch<Adapter>() {
            @Override
            public Adapter caseTransformationModel(TransformationModel object) {
                return createTransformationModelAdapter();
            }
            @Override
            public Adapter caseAbstractTransformationStep(AbstractTransformationStep object) {
                return createAbstractTransformationStepAdapter();
            }
            @Override
            public Adapter caseCreateComponent(CreateComponent object) {
                return createCreateComponentAdapter();
            }
            @Override
            public Adapter caseDeleteComponent(DeleteComponent object) {
                return createDeleteComponentAdapter();
            }
            @Override
            public Adapter caseCutOperation(CutOperation object) {
                return createCutOperationAdapter();
            }
            @Override
            public Adapter casePasteOperation(PasteOperation object) {
                return createPasteOperationAdapter();
            }
            @Override
            public Adapter caseMoveOperation(MoveOperation object) {
                return createMoveOperationAdapter();
            }
            @Override
            public Adapter caseMergeComponent(MergeComponent object) {
                return createMergeComponentAdapter();
            }
            @Override
            public Adapter caseSplitComponent(SplitComponent object) {
                return createSplitComponentAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.restructuring.restructuremodel.TransformationModel <em>Transformation Model</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.oceandsl.tools.restructuring.restructuremodel.TransformationModel
     * @generated
     */
    public Adapter createTransformationModelAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.restructuring.restructuremodel.AbstractTransformationStep <em>Abstract Transformation Step</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.oceandsl.tools.restructuring.restructuremodel.AbstractTransformationStep
     * @generated
     */
    public Adapter createAbstractTransformationStepAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.restructuring.restructuremodel.CreateComponent <em>Create Component</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.oceandsl.tools.restructuring.restructuremodel.CreateComponent
     * @generated
     */
    public Adapter createCreateComponentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.restructuring.restructuremodel.DeleteComponent <em>Delete Component</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.oceandsl.tools.restructuring.restructuremodel.DeleteComponent
     * @generated
     */
    public Adapter createDeleteComponentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.restructuring.restructuremodel.CutOperation <em>Cut Operation</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.oceandsl.tools.restructuring.restructuremodel.CutOperation
     * @generated
     */
    public Adapter createCutOperationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.restructuring.restructuremodel.PasteOperation <em>Paste Operation</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.oceandsl.tools.restructuring.restructuremodel.PasteOperation
     * @generated
     */
    public Adapter createPasteOperationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.restructuring.restructuremodel.MoveOperation <em>Move Operation</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.oceandsl.tools.restructuring.restructuremodel.MoveOperation
     * @generated
     */
    public Adapter createMoveOperationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.restructuring.restructuremodel.MergeComponent <em>Merge Component</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.oceandsl.tools.restructuring.restructuremodel.MergeComponent
     * @generated
     */
    public Adapter createMergeComponentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.restructuring.restructuremodel.SplitComponent <em>Split Component</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.oceandsl.tools.restructuring.restructuremodel.SplitComponent
     * @generated
     */
    public Adapter createSplitComponentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //RestructuremodelAdapterFactory
