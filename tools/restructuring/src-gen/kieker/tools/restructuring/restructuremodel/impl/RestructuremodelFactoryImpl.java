/**
 */
package kieker.tools.restructuring.restructuremodel.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import kieker.tools.restructuring.restructuremodel.CreateComponent;
import kieker.tools.restructuring.restructuremodel.CutOperation;
import kieker.tools.restructuring.restructuremodel.DeleteComponent;
import kieker.tools.restructuring.restructuremodel.MergeComponent;
import kieker.tools.restructuring.restructuremodel.MoveOperation;
import kieker.tools.restructuring.restructuremodel.PasteOperation;
import kieker.tools.restructuring.restructuremodel.RestructuremodelFactory;
import kieker.tools.restructuring.restructuremodel.RestructuremodelPackage;
import kieker.tools.restructuring.restructuremodel.SplitComponent;
import kieker.tools.restructuring.restructuremodel.TransformationModel;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class RestructuremodelFactoryImpl extends EFactoryImpl implements RestructuremodelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static RestructuremodelFactory init() {
		try {
			final RestructuremodelFactory theRestructuremodelFactory = (RestructuremodelFactory) EPackage.Registry.INSTANCE
					.getEFactory(RestructuremodelPackage.eNS_URI);
			if (theRestructuremodelFactory != null) {
				return theRestructuremodelFactory;
			}
		} catch (final Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new RestructuremodelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public RestructuremodelFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EObject create(final EClass eClass) {
		switch (eClass.getClassifierID()) {
		case RestructuremodelPackage.TRANSFORMATION_MODEL:
			return this.createTransformationModel();
		case RestructuremodelPackage.CREATE_COMPONENT:
			return this.createCreateComponent();
		case RestructuremodelPackage.DELETE_COMPONENT:
			return this.createDeleteComponent();
		case RestructuremodelPackage.CUT_OPERATION:
			return this.createCutOperation();
		case RestructuremodelPackage.PASTE_OPERATION:
			return this.createPasteOperation();
		case RestructuremodelPackage.MOVE_OPERATION:
			return this.createMoveOperation();
		case RestructuremodelPackage.MERGE_COMPONENT:
			return this.createMergeComponent();
		case RestructuremodelPackage.SPLIT_COMPONENT:
			return this.createSplitComponent();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public TransformationModel createTransformationModel() {
		final TransformationModelImpl transformationModel = new TransformationModelImpl();
		return transformationModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public CreateComponent createCreateComponent() {
		final CreateComponentImpl createComponent = new CreateComponentImpl();
		return createComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DeleteComponent createDeleteComponent() {
		final DeleteComponentImpl deleteComponent = new DeleteComponentImpl();
		return deleteComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public CutOperation createCutOperation() {
		final CutOperationImpl cutOperation = new CutOperationImpl();
		return cutOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public PasteOperation createPasteOperation() {
		final PasteOperationImpl pasteOperation = new PasteOperationImpl();
		return pasteOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MoveOperation createMoveOperation() {
		final MoveOperationImpl moveOperation = new MoveOperationImpl();
		return moveOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MergeComponent createMergeComponent() {
		final MergeComponentImpl mergeComponent = new MergeComponentImpl();
		return mergeComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public SplitComponent createSplitComponent() {
		final SplitComponentImpl splitComponent = new SplitComponentImpl();
		return splitComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public RestructuremodelPackage getRestructuremodelPackage() {
		return (RestructuremodelPackage) this.getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static RestructuremodelPackage getPackage() {
		return RestructuremodelPackage.eINSTANCE;
	}

} // RestructuremodelFactoryImpl
