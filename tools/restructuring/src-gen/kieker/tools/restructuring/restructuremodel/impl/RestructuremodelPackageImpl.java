/**
 */
package kieker.tools.restructuring.restructuremodel.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import kieker.tools.restructuring.restructuremodel.AbstractTransformationStep;
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
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class RestructuremodelPackageImpl extends EPackageImpl implements RestructuremodelPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass transformationModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass abstractTransformationStepEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass createComponentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass deleteComponentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass cutOperationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass pasteOperationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass moveOperationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass mergeComponentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass splitComponentEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see kieker.tools.restructuring.restructuremodel.RestructuremodelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private RestructuremodelPackageImpl() {
		super(eNS_URI, RestructuremodelFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>
	 * This method is used to initialize {@link RestructuremodelPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static RestructuremodelPackage init() {
		if (isInited) {
			return (RestructuremodelPackage) EPackage.Registry.INSTANCE.getEPackage(RestructuremodelPackage.eNS_URI);
		}

		// Obtain or create and register package
		final Object registeredRestructuremodelPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		final RestructuremodelPackageImpl theRestructuremodelPackage = registeredRestructuremodelPackage instanceof RestructuremodelPackageImpl
				? (RestructuremodelPackageImpl) registeredRestructuremodelPackage
				: new RestructuremodelPackageImpl();

		isInited = true;

		// Create package meta-data objects
		theRestructuremodelPackage.createPackageContents();

		// Initialize created meta-data
		theRestructuremodelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theRestructuremodelPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(RestructuremodelPackage.eNS_URI, theRestructuremodelPackage);
		return theRestructuremodelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getTransformationModel() {
		return this.transformationModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getTransformationModel_Transformations() {
		return (EReference) this.transformationModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getTransformationModel_Name() {
		return (EAttribute) this.transformationModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getAbstractTransformationStep() {
		return this.abstractTransformationStepEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getCreateComponent() {
		return this.createComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getCreateComponent_ComponentName() {
		return (EAttribute) this.createComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getDeleteComponent() {
		return this.deleteComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getDeleteComponent_ComponentName() {
		return (EAttribute) this.deleteComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getCutOperation() {
		return this.cutOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getCutOperation_ComponentName() {
		return (EAttribute) this.cutOperationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getCutOperation_OperationName() {
		return (EAttribute) this.cutOperationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getPasteOperation() {
		return this.pasteOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getPasteOperation_ComponentName() {
		return (EAttribute) this.pasteOperationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getPasteOperation_OperationName() {
		return (EAttribute) this.pasteOperationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getMoveOperation() {
		return this.moveOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getMoveOperation_From() {
		return (EAttribute) this.moveOperationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getMoveOperation_To() {
		return (EAttribute) this.moveOperationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getMoveOperation_OperationName() {
		return (EAttribute) this.moveOperationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getMoveOperation_CutOperation() {
		return (EReference) this.moveOperationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getMoveOperation_PasteOperation() {
		return (EReference) this.moveOperationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getMergeComponent() {
		return this.mergeComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getMergeComponent_MergeGoalComponent() {
		return (EAttribute) this.mergeComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getMergeComponent_ComponentName() {
		return (EAttribute) this.mergeComponentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getMergeComponent_Operations() {
		return (EAttribute) this.mergeComponentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getMergeComponent_DeleteTransformation() {
		return (EReference) this.mergeComponentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getMergeComponent_OperationToMove() {
		return (EReference) this.mergeComponentEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EClass getSplitComponent() {
		return this.splitComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getSplitComponent_NewComponent() {
		return (EAttribute) this.splitComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getSplitComponent_OperationsToMove() {
		return (EAttribute) this.splitComponentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EAttribute getSplitComponent_OldComponent() {
		return (EAttribute) this.splitComponentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getSplitComponent_CreateComponent() {
		return (EReference) this.splitComponentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EReference getSplitComponent_MoveOperations() {
		return (EReference) this.splitComponentEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public RestructuremodelFactory getRestructuremodelFactory() {
		return (RestructuremodelFactory) this.getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void createPackageContents() {
		if (this.isCreated) {
			return;
		}
		this.isCreated = true;

		// Create classes and their features
		this.transformationModelEClass = this.createEClass(TRANSFORMATION_MODEL);
		this.createEReference(this.transformationModelEClass, TRANSFORMATION_MODEL__TRANSFORMATIONS);
		this.createEAttribute(this.transformationModelEClass, TRANSFORMATION_MODEL__NAME);

		this.abstractTransformationStepEClass = this.createEClass(ABSTRACT_TRANSFORMATION_STEP);

		this.createComponentEClass = this.createEClass(CREATE_COMPONENT);
		this.createEAttribute(this.createComponentEClass, CREATE_COMPONENT__COMPONENT_NAME);

		this.deleteComponentEClass = this.createEClass(DELETE_COMPONENT);
		this.createEAttribute(this.deleteComponentEClass, DELETE_COMPONENT__COMPONENT_NAME);

		this.cutOperationEClass = this.createEClass(CUT_OPERATION);
		this.createEAttribute(this.cutOperationEClass, CUT_OPERATION__COMPONENT_NAME);
		this.createEAttribute(this.cutOperationEClass, CUT_OPERATION__OPERATION_NAME);

		this.pasteOperationEClass = this.createEClass(PASTE_OPERATION);
		this.createEAttribute(this.pasteOperationEClass, PASTE_OPERATION__COMPONENT_NAME);
		this.createEAttribute(this.pasteOperationEClass, PASTE_OPERATION__OPERATION_NAME);

		this.moveOperationEClass = this.createEClass(MOVE_OPERATION);
		this.createEAttribute(this.moveOperationEClass, MOVE_OPERATION__FROM);
		this.createEAttribute(this.moveOperationEClass, MOVE_OPERATION__TO);
		this.createEAttribute(this.moveOperationEClass, MOVE_OPERATION__OPERATION_NAME);
		this.createEReference(this.moveOperationEClass, MOVE_OPERATION__CUT_OPERATION);
		this.createEReference(this.moveOperationEClass, MOVE_OPERATION__PASTE_OPERATION);

		this.mergeComponentEClass = this.createEClass(MERGE_COMPONENT);
		this.createEAttribute(this.mergeComponentEClass, MERGE_COMPONENT__MERGE_GOAL_COMPONENT);
		this.createEAttribute(this.mergeComponentEClass, MERGE_COMPONENT__COMPONENT_NAME);
		this.createEAttribute(this.mergeComponentEClass, MERGE_COMPONENT__OPERATIONS);
		this.createEReference(this.mergeComponentEClass, MERGE_COMPONENT__DELETE_TRANSFORMATION);
		this.createEReference(this.mergeComponentEClass, MERGE_COMPONENT__OPERATION_TO_MOVE);

		this.splitComponentEClass = this.createEClass(SPLIT_COMPONENT);
		this.createEAttribute(this.splitComponentEClass, SPLIT_COMPONENT__NEW_COMPONENT);
		this.createEAttribute(this.splitComponentEClass, SPLIT_COMPONENT__OPERATIONS_TO_MOVE);
		this.createEAttribute(this.splitComponentEClass, SPLIT_COMPONENT__OLD_COMPONENT);
		this.createEReference(this.splitComponentEClass, SPLIT_COMPONENT__CREATE_COMPONENT);
		this.createEReference(this.splitComponentEClass, SPLIT_COMPONENT__MOVE_OPERATIONS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void initializePackageContents() {
		if (this.isInitialized) {
			return;
		}
		this.isInitialized = true;

		// Initialize package
		this.setName(eNAME);
		this.setNsPrefix(eNS_PREFIX);
		this.setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		this.createComponentEClass.getESuperTypes().add(this.getAbstractTransformationStep());
		this.deleteComponentEClass.getESuperTypes().add(this.getAbstractTransformationStep());
		this.cutOperationEClass.getESuperTypes().add(this.getAbstractTransformationStep());
		this.pasteOperationEClass.getESuperTypes().add(this.getAbstractTransformationStep());
		this.moveOperationEClass.getESuperTypes().add(this.getAbstractTransformationStep());
		this.mergeComponentEClass.getESuperTypes().add(this.getAbstractTransformationStep());
		this.splitComponentEClass.getESuperTypes().add(this.getAbstractTransformationStep());

		// Initialize classes, features, and operations; add parameters
		this.initEClass(this.transformationModelEClass, TransformationModel.class, "TransformationModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getTransformationModel_Transformations(), this.getAbstractTransformationStep(), null, "transformations", null, 1, -1,
				TransformationModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		this.initEAttribute(this.getTransformationModel_Name(), this.ecorePackage.getEString(), "name", null, 1, 1, TransformationModel.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.abstractTransformationStepEClass, AbstractTransformationStep.class, "AbstractTransformationStep", IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);

		this.initEClass(this.createComponentEClass, CreateComponent.class, "CreateComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getCreateComponent_ComponentName(), this.ecorePackage.getEString(), "componentName", null, 0, 1, CreateComponent.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.deleteComponentEClass, DeleteComponent.class, "DeleteComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getDeleteComponent_ComponentName(), this.ecorePackage.getEString(), "componentName", null, 0, 1, DeleteComponent.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.cutOperationEClass, CutOperation.class, "CutOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getCutOperation_ComponentName(), this.ecorePackage.getEString(), "componentName", null, 0, 1, CutOperation.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getCutOperation_OperationName(), this.ecorePackage.getEString(), "operationName", null, 0, 1, CutOperation.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.pasteOperationEClass, PasteOperation.class, "PasteOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getPasteOperation_ComponentName(), this.ecorePackage.getEString(), "componentName", null, 0, 1, PasteOperation.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getPasteOperation_OperationName(), this.ecorePackage.getEString(), "operationName", null, 0, 1, PasteOperation.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.moveOperationEClass, MoveOperation.class, "MoveOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getMoveOperation_From(), this.ecorePackage.getEString(), "from", null, 0, 1, MoveOperation.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getMoveOperation_To(), this.ecorePackage.getEString(), "to", null, 0, 1, MoveOperation.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getMoveOperation_OperationName(), this.ecorePackage.getEString(), "operationName", null, 0, 1, MoveOperation.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getMoveOperation_CutOperation(), this.getCutOperation(), null, "cutOperation", null, 0, 1, MoveOperation.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getMoveOperation_PasteOperation(), this.getPasteOperation(), null, "pasteOperation", null, 0, 1, MoveOperation.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.mergeComponentEClass, MergeComponent.class, "MergeComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getMergeComponent_MergeGoalComponent(), this.ecorePackage.getEString(), "mergeGoalComponent", null, 0, 1, MergeComponent.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getMergeComponent_ComponentName(), this.ecorePackage.getEString(), "componentName", null, 0, 1, MergeComponent.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getMergeComponent_Operations(), this.ecorePackage.getEString(), "operations", null, 1, -1, MergeComponent.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getMergeComponent_DeleteTransformation(), this.getDeleteComponent(), null, "deleteTransformation", null, 0, 1, MergeComponent.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getMergeComponent_OperationToMove(), this.getMoveOperation(), null, "operationToMove", null, 1, -1, MergeComponent.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.splitComponentEClass, SplitComponent.class, "SplitComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getSplitComponent_NewComponent(), this.ecorePackage.getEString(), "newComponent", null, 0, 1, SplitComponent.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getSplitComponent_OperationsToMove(), this.ecorePackage.getEString(), "operationsToMove", null, 1, -1, SplitComponent.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getSplitComponent_OldComponent(), this.ecorePackage.getEString(), "oldComponent", null, 0, 1, SplitComponent.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getSplitComponent_CreateComponent(), this.getCreateComponent(), null, "createComponent", null, 0, 1, SplitComponent.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getSplitComponent_MoveOperations(), this.getMoveOperation(), null, "moveOperations", null, 1, -1, SplitComponent.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		this.createResource(eNS_URI);
	}

} // RestructuremodelPackageImpl
