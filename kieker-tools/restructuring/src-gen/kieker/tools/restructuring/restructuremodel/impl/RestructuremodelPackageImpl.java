/**
 */
package kieker.tools.restructuring.restructuremodel.impl;

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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class RestructuremodelPackageImpl extends EPackageImpl implements RestructuremodelPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass transformationModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractTransformationStepEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass createComponentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deleteComponentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cutOperationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pasteOperationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass moveOperationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mergeComponentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass splitComponentEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
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
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link RestructuremodelPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static RestructuremodelPackage init() {
		if (isInited) return (RestructuremodelPackage)EPackage.Registry.INSTANCE.getEPackage(RestructuremodelPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredRestructuremodelPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		RestructuremodelPackageImpl theRestructuremodelPackage = registeredRestructuremodelPackage instanceof RestructuremodelPackageImpl ? (RestructuremodelPackageImpl)registeredRestructuremodelPackage : new RestructuremodelPackageImpl();

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
	 * @generated
	 */
	public EClass getTransformationModel() {
		return transformationModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTransformationModel_Transformations() {
		return (EReference)transformationModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTransformationModel_Name() {
		return (EAttribute)transformationModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractTransformationStep() {
		return abstractTransformationStepEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCreateComponent() {
		return createComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCreateComponent_ComponentName() {
		return (EAttribute)createComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDeleteComponent() {
		return deleteComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDeleteComponent_ComponentName() {
		return (EAttribute)deleteComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCutOperation() {
		return cutOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCutOperation_ComponentName() {
		return (EAttribute)cutOperationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCutOperation_OperationName() {
		return (EAttribute)cutOperationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPasteOperation() {
		return pasteOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPasteOperation_ComponentName() {
		return (EAttribute)pasteOperationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPasteOperation_OperationName() {
		return (EAttribute)pasteOperationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMoveOperation() {
		return moveOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMoveOperation_From() {
		return (EAttribute)moveOperationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMoveOperation_To() {
		return (EAttribute)moveOperationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMoveOperation_OperationName() {
		return (EAttribute)moveOperationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMoveOperation_CutOperation() {
		return (EReference)moveOperationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMoveOperation_PasteOperation() {
		return (EReference)moveOperationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMergeComponent() {
		return mergeComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMergeComponent_MergeGoalComponent() {
		return (EAttribute)mergeComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMergeComponent_ComponentName() {
		return (EAttribute)mergeComponentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMergeComponent_Operations() {
		return (EAttribute)mergeComponentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMergeComponent_DeleteTransformation() {
		return (EReference)mergeComponentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMergeComponent_OperationToMove() {
		return (EReference)mergeComponentEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSplitComponent() {
		return splitComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSplitComponent_NewComponent() {
		return (EAttribute)splitComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSplitComponent_OperationsToMove() {
		return (EAttribute)splitComponentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSplitComponent_OldComponent() {
		return (EAttribute)splitComponentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSplitComponent_CreateComponent() {
		return (EReference)splitComponentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSplitComponent_MoveOperations() {
		return (EReference)splitComponentEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RestructuremodelFactory getRestructuremodelFactory() {
		return (RestructuremodelFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		transformationModelEClass = createEClass(TRANSFORMATION_MODEL);
		createEReference(transformationModelEClass, TRANSFORMATION_MODEL__TRANSFORMATIONS);
		createEAttribute(transformationModelEClass, TRANSFORMATION_MODEL__NAME);

		abstractTransformationStepEClass = createEClass(ABSTRACT_TRANSFORMATION_STEP);

		createComponentEClass = createEClass(CREATE_COMPONENT);
		createEAttribute(createComponentEClass, CREATE_COMPONENT__COMPONENT_NAME);

		deleteComponentEClass = createEClass(DELETE_COMPONENT);
		createEAttribute(deleteComponentEClass, DELETE_COMPONENT__COMPONENT_NAME);

		cutOperationEClass = createEClass(CUT_OPERATION);
		createEAttribute(cutOperationEClass, CUT_OPERATION__COMPONENT_NAME);
		createEAttribute(cutOperationEClass, CUT_OPERATION__OPERATION_NAME);

		pasteOperationEClass = createEClass(PASTE_OPERATION);
		createEAttribute(pasteOperationEClass, PASTE_OPERATION__COMPONENT_NAME);
		createEAttribute(pasteOperationEClass, PASTE_OPERATION__OPERATION_NAME);

		moveOperationEClass = createEClass(MOVE_OPERATION);
		createEAttribute(moveOperationEClass, MOVE_OPERATION__FROM);
		createEAttribute(moveOperationEClass, MOVE_OPERATION__TO);
		createEAttribute(moveOperationEClass, MOVE_OPERATION__OPERATION_NAME);
		createEReference(moveOperationEClass, MOVE_OPERATION__CUT_OPERATION);
		createEReference(moveOperationEClass, MOVE_OPERATION__PASTE_OPERATION);

		mergeComponentEClass = createEClass(MERGE_COMPONENT);
		createEAttribute(mergeComponentEClass, MERGE_COMPONENT__MERGE_GOAL_COMPONENT);
		createEAttribute(mergeComponentEClass, MERGE_COMPONENT__COMPONENT_NAME);
		createEAttribute(mergeComponentEClass, MERGE_COMPONENT__OPERATIONS);
		createEReference(mergeComponentEClass, MERGE_COMPONENT__DELETE_TRANSFORMATION);
		createEReference(mergeComponentEClass, MERGE_COMPONENT__OPERATION_TO_MOVE);

		splitComponentEClass = createEClass(SPLIT_COMPONENT);
		createEAttribute(splitComponentEClass, SPLIT_COMPONENT__NEW_COMPONENT);
		createEAttribute(splitComponentEClass, SPLIT_COMPONENT__OPERATIONS_TO_MOVE);
		createEAttribute(splitComponentEClass, SPLIT_COMPONENT__OLD_COMPONENT);
		createEReference(splitComponentEClass, SPLIT_COMPONENT__CREATE_COMPONENT);
		createEReference(splitComponentEClass, SPLIT_COMPONENT__MOVE_OPERATIONS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		createComponentEClass.getESuperTypes().add(this.getAbstractTransformationStep());
		deleteComponentEClass.getESuperTypes().add(this.getAbstractTransformationStep());
		cutOperationEClass.getESuperTypes().add(this.getAbstractTransformationStep());
		pasteOperationEClass.getESuperTypes().add(this.getAbstractTransformationStep());
		moveOperationEClass.getESuperTypes().add(this.getAbstractTransformationStep());
		mergeComponentEClass.getESuperTypes().add(this.getAbstractTransformationStep());
		splitComponentEClass.getESuperTypes().add(this.getAbstractTransformationStep());

		// Initialize classes, features, and operations; add parameters
		initEClass(transformationModelEClass, TransformationModel.class, "TransformationModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTransformationModel_Transformations(), this.getAbstractTransformationStep(), null, "transformations", null, 1, -1, TransformationModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransformationModel_Name(), ecorePackage.getEString(), "name", null, 1, 1, TransformationModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(abstractTransformationStepEClass, AbstractTransformationStep.class, "AbstractTransformationStep", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(createComponentEClass, CreateComponent.class, "CreateComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCreateComponent_ComponentName(), ecorePackage.getEString(), "componentName", null, 0, 1, CreateComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(deleteComponentEClass, DeleteComponent.class, "DeleteComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDeleteComponent_ComponentName(), ecorePackage.getEString(), "componentName", null, 0, 1, DeleteComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cutOperationEClass, CutOperation.class, "CutOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCutOperation_ComponentName(), ecorePackage.getEString(), "componentName", null, 0, 1, CutOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCutOperation_OperationName(), ecorePackage.getEString(), "operationName", null, 0, 1, CutOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(pasteOperationEClass, PasteOperation.class, "PasteOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPasteOperation_ComponentName(), ecorePackage.getEString(), "componentName", null, 0, 1, PasteOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPasteOperation_OperationName(), ecorePackage.getEString(), "operationName", null, 0, 1, PasteOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(moveOperationEClass, MoveOperation.class, "MoveOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMoveOperation_From(), ecorePackage.getEString(), "from", null, 0, 1, MoveOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMoveOperation_To(), ecorePackage.getEString(), "to", null, 0, 1, MoveOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMoveOperation_OperationName(), ecorePackage.getEString(), "operationName", null, 0, 1, MoveOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMoveOperation_CutOperation(), this.getCutOperation(), null, "cutOperation", null, 0, 1, MoveOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMoveOperation_PasteOperation(), this.getPasteOperation(), null, "pasteOperation", null, 0, 1, MoveOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mergeComponentEClass, MergeComponent.class, "MergeComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMergeComponent_MergeGoalComponent(), ecorePackage.getEString(), "mergeGoalComponent", null, 0, 1, MergeComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMergeComponent_ComponentName(), ecorePackage.getEString(), "componentName", null, 0, 1, MergeComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMergeComponent_Operations(), ecorePackage.getEString(), "operations", null, 1, -1, MergeComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMergeComponent_DeleteTransformation(), this.getDeleteComponent(), null, "deleteTransformation", null, 0, 1, MergeComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMergeComponent_OperationToMove(), this.getMoveOperation(), null, "operationToMove", null, 1, -1, MergeComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(splitComponentEClass, SplitComponent.class, "SplitComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSplitComponent_NewComponent(), ecorePackage.getEString(), "newComponent", null, 0, 1, SplitComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSplitComponent_OperationsToMove(), ecorePackage.getEString(), "operationsToMove", null, 1, -1, SplitComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSplitComponent_OldComponent(), ecorePackage.getEString(), "oldComponent", null, 0, 1, SplitComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSplitComponent_CreateComponent(), this.getCreateComponent(), null, "createComponent", null, 0, 1, SplitComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSplitComponent_MoveOperations(), this.getMoveOperation(), null, "moveOperations", null, 1, -1, SplitComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //RestructuremodelPackageImpl
