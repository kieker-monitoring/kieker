/**
 */
package kieker.tools.restructuring.restructuremodel.impl;

import kieker.tools.restructuring.restructuremodel.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class RestructuremodelFactoryImpl extends EFactoryImpl implements RestructuremodelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static RestructuremodelFactory init() {
		try {
			RestructuremodelFactory theRestructuremodelFactory = (RestructuremodelFactory)EPackage.Registry.INSTANCE.getEFactory(RestructuremodelPackage.eNS_URI);
			if (theRestructuremodelFactory != null) {
				return theRestructuremodelFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new RestructuremodelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RestructuremodelFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case RestructuremodelPackage.TRANSFORMATION_MODEL: return createTransformationModel();
			case RestructuremodelPackage.CREATE_COMPONENT: return createCreateComponent();
			case RestructuremodelPackage.DELETE_COMPONENT: return createDeleteComponent();
			case RestructuremodelPackage.CUT_OPERATION: return createCutOperation();
			case RestructuremodelPackage.PASTE_OPERATION: return createPasteOperation();
			case RestructuremodelPackage.MOVE_OPERATION: return createMoveOperation();
			case RestructuremodelPackage.MERGE_COMPONENT: return createMergeComponent();
			case RestructuremodelPackage.SPLIT_COMPONENT: return createSplitComponent();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransformationModel createTransformationModel() {
		TransformationModelImpl transformationModel = new TransformationModelImpl();
		return transformationModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CreateComponent createCreateComponent() {
		CreateComponentImpl createComponent = new CreateComponentImpl();
		return createComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeleteComponent createDeleteComponent() {
		DeleteComponentImpl deleteComponent = new DeleteComponentImpl();
		return deleteComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CutOperation createCutOperation() {
		CutOperationImpl cutOperation = new CutOperationImpl();
		return cutOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PasteOperation createPasteOperation() {
		PasteOperationImpl pasteOperation = new PasteOperationImpl();
		return pasteOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MoveOperation createMoveOperation() {
		MoveOperationImpl moveOperation = new MoveOperationImpl();
		return moveOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MergeComponent createMergeComponent() {
		MergeComponentImpl mergeComponent = new MergeComponentImpl();
		return mergeComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SplitComponent createSplitComponent() {
		SplitComponentImpl splitComponent = new SplitComponentImpl();
		return splitComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RestructuremodelPackage getRestructuremodelPackage() {
		return (RestructuremodelPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static RestructuremodelPackage getPackage() {
		return RestructuremodelPackage.eINSTANCE;
	}

} //RestructuremodelFactoryImpl
