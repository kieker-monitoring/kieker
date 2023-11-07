/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.oceandsl.tools.sar.fxtran.*;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 *
 * @generated
 */
public class FxtranFactoryImpl extends EFactoryImpl implements FxtranFactory {
    /**
     * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public static FxtranFactory init() {
        try {
            final FxtranFactory theFxtranFactory = (FxtranFactory) EPackage.Registry.INSTANCE
                    .getEFactory(FxtranPackage.eNS_URI);
            if (theFxtranFactory != null) {
                return theFxtranFactory;
            }
        } catch (final Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new FxtranFactoryImpl();
    }

    /**
     * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public FxtranFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EObject create(final EClass eClass) {
        switch (eClass.getClassifierID()) {
        case FxtranPackage.ACTION_STMT_TYPE:
            return this.createActionStmtType();
        case FxtranPackage.AC_VALUE_LT_TYPE:
            return this.createAcValueLTType();
        case FxtranPackage.AC_VALUE_TYPE:
            return this.createAcValueType();
        case FxtranPackage.ALLOCATE_STMT_TYPE:
            return this.createAllocateStmtType();
        case FxtranPackage.ARG_NTYPE:
            return this.createArgNType();
        case FxtranPackage.ARG_SPEC_TYPE:
            return this.createArgSpecType();
        case FxtranPackage.ARG_TYPE:
            return this.createArgType();
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE:
            return this.createArrayConstructorEType();
        case FxtranPackage.ARRAY_RTYPE:
            return this.createArrayRType();
        case FxtranPackage.ARRAY_SPEC_TYPE:
            return this.createArraySpecType();
        case FxtranPackage.ASTMT_TYPE:
            return this.createAStmtType();
        case FxtranPackage.ATTRIBUTE_TYPE:
            return this.createAttributeType();
        case FxtranPackage.CALL_STMT_TYPE:
            return this.createCallStmtType();
        case FxtranPackage.CASE_ETYPE:
            return this.createCaseEType();
        case FxtranPackage.CASE_SELECTOR_TYPE:
            return this.createCaseSelectorType();
        case FxtranPackage.CASE_STMT_TYPE:
            return this.createCaseStmtType();
        case FxtranPackage.CASE_VALUE_RANGE_LT_TYPE:
            return this.createCaseValueRangeLTType();
        case FxtranPackage.CASE_VALUE_RANGE_TYPE:
            return this.createCaseValueRangeType();
        case FxtranPackage.CASE_VALUE_TYPE:
            return this.createCaseValueType();
        case FxtranPackage.CHAR_SELECTOR_TYPE:
            return this.createCharSelectorType();
        case FxtranPackage.CHAR_SPEC_TYPE:
            return this.createCharSpecType();
        case FxtranPackage.CLOSE_SPEC_SPEC_TYPE:
            return this.createCloseSpecSpecType();
        case FxtranPackage.CLOSE_SPEC_TYPE:
            return this.createCloseSpecType();
        case FxtranPackage.CLOSE_STMT_TYPE:
            return this.createCloseStmtType();
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE:
            return this.createComponentDeclStmtType();
        case FxtranPackage.COMPONENT_RTYPE:
            return this.createComponentRType();
        case FxtranPackage.CONDITION_ETYPE:
            return this.createConditionEType();
        case FxtranPackage.CONNECT_SPEC_SPEC_TYPE:
            return this.createConnectSpecSpecType();
        case FxtranPackage.CONNECT_SPEC_TYPE:
            return this.createConnectSpecType();
        case FxtranPackage.CYCLE_STMT_TYPE:
            return this.createCycleStmtType();
        case FxtranPackage.DEALLOCATE_STMT_TYPE:
            return this.createDeallocateStmtType();
        case FxtranPackage.DERIVED_TSPEC_TYPE:
            return this.createDerivedTSpecType();
        case FxtranPackage.DOCUMENT_ROOT:
            return this.createDocumentRoot();
        case FxtranPackage.DO_STMT_TYPE:
            return this.createDoStmtType();
        case FxtranPackage.DO_VTYPE:
            return this.createDoVType();
        case FxtranPackage.DUMMY_ARG_LT_TYPE:
            return this.createDummyArgLTType();
        case FxtranPackage.E1_TYPE:
            return this.createE1Type();
        case FxtranPackage.E2_TYPE:
            return this.createE2Type();
        case FxtranPackage.ELEMENT_LT_TYPE:
            return this.createElementLTType();
        case FxtranPackage.ELEMENT_TYPE:
            return this.createElementType();
        case FxtranPackage.ELSE_IF_STMT_TYPE:
            return this.createElseIfStmtType();
        case FxtranPackage.END_DO_STMT_TYPE:
            return this.createEndDoStmtType();
        case FxtranPackage.EN_DECL_LT_TYPE:
            return this.createENDeclLTType();
        case FxtranPackage.EN_DECL_TYPE:
            return this.createENDeclType();
        case FxtranPackage.END_FORALL_STMT_TYPE:
            return this.createEndForallStmtType();
        case FxtranPackage.END_FUNCTION_STMT_TYPE:
            return this.createEndFunctionStmtType();
        case FxtranPackage.END_INTERFACE_STMT_TYPE:
            return this.createEndInterfaceStmtType();
        case FxtranPackage.END_MODULE_STMT_TYPE:
            return this.createEndModuleStmtType();
        case FxtranPackage.END_PROGRAM_STMT_TYPE:
            return this.createEndProgramStmtType();
        case FxtranPackage.END_SELECT_CASE_STMT_TYPE:
            return this.createEndSelectCaseStmtType();
        case FxtranPackage.END_SUBROUTINE_STMT_TYPE:
            return this.createEndSubroutineStmtType();
        case FxtranPackage.END_TSTMT_TYPE:
            return this.createEndTStmtType();
        case FxtranPackage.ENLT_TYPE:
            return this.createENLTType();
        case FxtranPackage.ENN_TYPE:
            return this.createENNType();
        case FxtranPackage.EN_TYPE:
            return this.createENType();
        case FxtranPackage.ERROR_TYPE:
            return this.createErrorType();
        case FxtranPackage.FILE_TYPE:
            return this.createFileType();
        case FxtranPackage.FORALL_CONSTRUCT_STMT_TYPE:
            return this.createForallConstructStmtType();
        case FxtranPackage.FORALL_STMT_TYPE:
            return this.createForallStmtType();
        case FxtranPackage.FORALL_TRIPLET_SPEC_LT_TYPE:
            return this.createForallTripletSpecLTType();
        case FxtranPackage.FORALL_TRIPLET_SPEC_TYPE:
            return this.createForallTripletSpecType();
        case FxtranPackage.FUNCTION_NTYPE:
            return this.createFunctionNType();
        case FxtranPackage.FUNCTION_STMT_TYPE:
            return this.createFunctionStmtType();
        case FxtranPackage.IF_STMT_TYPE:
            return this.createIfStmtType();
        case FxtranPackage.IF_THEN_STMT_TYPE:
            return this.createIfThenStmtType();
        case FxtranPackage.INIT_ETYPE:
            return this.createInitEType();
        case FxtranPackage.INQUIRE_STMT_TYPE:
            return this.createInquireStmtType();
        case FxtranPackage.INQUIRY_SPEC_SPEC_TYPE:
            return this.createInquirySpecSpecType();
        case FxtranPackage.INQUIRY_SPEC_TYPE:
            return this.createInquirySpecType();
        case FxtranPackage.INTERFACE_STMT_TYPE:
            return this.createInterfaceStmtType();
        case FxtranPackage.INTRINSIC_TSPEC_TYPE:
            return this.createIntrinsicTSpecType();
        case FxtranPackage.IO_CONTROL_SPEC_TYPE:
            return this.createIoControlSpecType();
        case FxtranPackage.IO_CONTROL_TYPE:
            return this.createIoControlType();
        case FxtranPackage.ITERATOR_DEFINITION_LT_TYPE:
            return this.createIteratorDefinitionLTType();
        case FxtranPackage.ITERATOR_ELEMENT_TYPE:
            return this.createIteratorElementType();
        case FxtranPackage.ITERATOR_TYPE:
            return this.createIteratorType();
        case FxtranPackage.KSELECTOR_TYPE:
            return this.createKSelectorType();
        case FxtranPackage.KSPEC_TYPE:
            return this.createKSpecType();
        case FxtranPackage.LABEL_TYPE:
            return this.createLabelType();
        case FxtranPackage.LITERAL_ETYPE:
            return this.createLiteralEType();
        case FxtranPackage.LOWER_BOUND_TYPE:
            return this.createLowerBoundType();
        case FxtranPackage.MASK_ETYPE:
            return this.createMaskEType();
        case FxtranPackage.MODULE_NTYPE:
            return this.createModuleNType();
        case FxtranPackage.MODULE_PROCEDURE_NLT_TYPE:
            return this.createModuleProcedureNLTType();
        case FxtranPackage.MODULE_STMT_TYPE:
            return this.createModuleStmtType();
        case FxtranPackage.NAMED_ETYPE:
            return this.createNamedEType();
        case FxtranPackage.NAMELIST_GROUP_NTYPE:
            return this.createNamelistGroupNType();
        case FxtranPackage.NAMELIST_GROUP_OBJ_LT_TYPE:
            return this.createNamelistGroupObjLTType();
        case FxtranPackage.NAMELIST_GROUP_OBJ_NTYPE:
            return this.createNamelistGroupObjNType();
        case FxtranPackage.NAMELIST_GROUP_OBJ_TYPE:
            return this.createNamelistGroupObjType();
        case FxtranPackage.NAMELIST_STMT_TYPE:
            return this.createNamelistStmtType();
        case FxtranPackage.NTYPE:
            return this.createNType();
        case FxtranPackage.NULLIFY_STMT_TYPE:
            return this.createNullifyStmtType();
        case FxtranPackage.OBJECT_TYPE:
            return this.createObjectType();
        case FxtranPackage.OPEN_STMT_TYPE:
            return this.createOpenStmtType();
        case FxtranPackage.OP_ETYPE:
            return this.createOpEType();
        case FxtranPackage.OP_TYPE:
            return this.createOpType();
        case FxtranPackage.OUTPUT_ITEM_LT_TYPE:
            return this.createOutputItemLTType();
        case FxtranPackage.OUTPUT_ITEM_TYPE:
            return this.createOutputItemType();
        case FxtranPackage.PARENS_ETYPE:
            return this.createParensEType();
        case FxtranPackage.PARENS_RTYPE:
            return this.createParensRType();
        case FxtranPackage.POINTER_ASTMT_TYPE:
            return this.createPointerAStmtType();
        case FxtranPackage.POINTER_STMT_TYPE:
            return this.createPointerStmtType();
        case FxtranPackage.PROCEDURE_DESIGNATOR_TYPE:
            return this.createProcedureDesignatorType();
        case FxtranPackage.PROCEDURE_STMT_TYPE:
            return this.createProcedureStmtType();
        case FxtranPackage.PROGRAM_NTYPE:
            return this.createProgramNType();
        case FxtranPackage.PROGRAM_STMT_TYPE:
            return this.createProgramStmtType();
        case FxtranPackage.PUBLIC_STMT_TYPE:
            return this.createPublicStmtType();
        case FxtranPackage.READ_STMT_TYPE:
            return this.createReadStmtType();
        case FxtranPackage.RENAME_LT_TYPE:
            return this.createRenameLTType();
        case FxtranPackage.RENAME_TYPE:
            return this.createRenameType();
        case FxtranPackage.RESULT_SPEC_TYPE:
            return this.createResultSpecType();
        case FxtranPackage.RLT_TYPE:
            return this.createRLTType();
        case FxtranPackage.SECTION_SUBSCRIPT_LT_TYPE:
            return this.createSectionSubscriptLTType();
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE:
            return this.createSectionSubscriptType();
        case FxtranPackage.SELECT_CASE_STMT_TYPE:
            return this.createSelectCaseStmtType();
        case FxtranPackage.SHAPE_SPEC_LT_TYPE:
            return this.createShapeSpecLTType();
        case FxtranPackage.SHAPE_SPEC_TYPE:
            return this.createShapeSpecType();
        case FxtranPackage.STOP_STMT_TYPE:
            return this.createStopStmtType();
        case FxtranPackage.STRING_ETYPE:
            return this.createStringEType();
        case FxtranPackage.SUBROUTINE_NTYPE:
            return this.createSubroutineNType();
        case FxtranPackage.SUBROUTINE_STMT_TYPE:
            return this.createSubroutineStmtType();
        case FxtranPackage.TDECL_STMT_TYPE:
            return this.createTDeclStmtType();
        case FxtranPackage.TEST_ETYPE:
            return this.createTestEType();
        case FxtranPackage.TN_TYPE:
            return this.createTNType();
        case FxtranPackage.TSPEC_TYPE:
            return this.createTSpecType();
        case FxtranPackage.TSTMT_TYPE:
            return this.createTStmtType();
        case FxtranPackage.UPPER_BOUND_TYPE:
            return this.createUpperBoundType();
        case FxtranPackage.USE_NTYPE:
            return this.createUseNType();
        case FxtranPackage.USE_STMT_TYPE:
            return this.createUseStmtType();
        case FxtranPackage.VN_TYPE:
            return this.createVNType();
        case FxtranPackage.VTYPE:
            return this.createVType();
        case FxtranPackage.WHERE_CONSTRUCT_STMT_TYPE:
            return this.createWhereConstructStmtType();
        case FxtranPackage.WHERE_STMT_TYPE:
            return this.createWhereStmtType();
        case FxtranPackage.WRITE_STMT_TYPE:
            return this.createWriteStmtType();
        default:
            throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ActionStmtType createActionStmtType() {
        final ActionStmtTypeImpl actionStmtType = new ActionStmtTypeImpl();
        return actionStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public AcValueLTType createAcValueLTType() {
        final AcValueLTTypeImpl acValueLTType = new AcValueLTTypeImpl();
        return acValueLTType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public AcValueType createAcValueType() {
        final AcValueTypeImpl acValueType = new AcValueTypeImpl();
        return acValueType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public AllocateStmtType createAllocateStmtType() {
        final AllocateStmtTypeImpl allocateStmtType = new AllocateStmtTypeImpl();
        return allocateStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ArgNType createArgNType() {
        final ArgNTypeImpl argNType = new ArgNTypeImpl();
        return argNType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ArgSpecType createArgSpecType() {
        final ArgSpecTypeImpl argSpecType = new ArgSpecTypeImpl();
        return argSpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ArgType createArgType() {
        final ArgTypeImpl argType = new ArgTypeImpl();
        return argType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ArrayConstructorEType createArrayConstructorEType() {
        final ArrayConstructorETypeImpl arrayConstructorEType = new ArrayConstructorETypeImpl();
        return arrayConstructorEType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ArrayRType createArrayRType() {
        final ArrayRTypeImpl arrayRType = new ArrayRTypeImpl();
        return arrayRType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ArraySpecType createArraySpecType() {
        final ArraySpecTypeImpl arraySpecType = new ArraySpecTypeImpl();
        return arraySpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public AStmtType createAStmtType() {
        final AStmtTypeImpl aStmtType = new AStmtTypeImpl();
        return aStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public AttributeType createAttributeType() {
        final AttributeTypeImpl attributeType = new AttributeTypeImpl();
        return attributeType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CallStmtType createCallStmtType() {
        final CallStmtTypeImpl callStmtType = new CallStmtTypeImpl();
        return callStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CaseEType createCaseEType() {
        final CaseETypeImpl caseEType = new CaseETypeImpl();
        return caseEType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CaseSelectorType createCaseSelectorType() {
        final CaseSelectorTypeImpl caseSelectorType = new CaseSelectorTypeImpl();
        return caseSelectorType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CaseStmtType createCaseStmtType() {
        final CaseStmtTypeImpl caseStmtType = new CaseStmtTypeImpl();
        return caseStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CaseValueRangeLTType createCaseValueRangeLTType() {
        final CaseValueRangeLTTypeImpl caseValueRangeLTType = new CaseValueRangeLTTypeImpl();
        return caseValueRangeLTType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CaseValueRangeType createCaseValueRangeType() {
        final CaseValueRangeTypeImpl caseValueRangeType = new CaseValueRangeTypeImpl();
        return caseValueRangeType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CaseValueType createCaseValueType() {
        final CaseValueTypeImpl caseValueType = new CaseValueTypeImpl();
        return caseValueType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CharSelectorType createCharSelectorType() {
        final CharSelectorTypeImpl charSelectorType = new CharSelectorTypeImpl();
        return charSelectorType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CharSpecType createCharSpecType() {
        final CharSpecTypeImpl charSpecType = new CharSpecTypeImpl();
        return charSpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CloseSpecSpecType createCloseSpecSpecType() {
        final CloseSpecSpecTypeImpl closeSpecSpecType = new CloseSpecSpecTypeImpl();
        return closeSpecSpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CloseSpecType createCloseSpecType() {
        final CloseSpecTypeImpl closeSpecType = new CloseSpecTypeImpl();
        return closeSpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CloseStmtType createCloseStmtType() {
        final CloseStmtTypeImpl closeStmtType = new CloseStmtTypeImpl();
        return closeStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ComponentDeclStmtType createComponentDeclStmtType() {
        final ComponentDeclStmtTypeImpl componentDeclStmtType = new ComponentDeclStmtTypeImpl();
        return componentDeclStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ComponentRType createComponentRType() {
        final ComponentRTypeImpl componentRType = new ComponentRTypeImpl();
        return componentRType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ConditionEType createConditionEType() {
        final ConditionETypeImpl conditionEType = new ConditionETypeImpl();
        return conditionEType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ConnectSpecSpecType createConnectSpecSpecType() {
        final ConnectSpecSpecTypeImpl connectSpecSpecType = new ConnectSpecSpecTypeImpl();
        return connectSpecSpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ConnectSpecType createConnectSpecType() {
        final ConnectSpecTypeImpl connectSpecType = new ConnectSpecTypeImpl();
        return connectSpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CycleStmtType createCycleStmtType() {
        final CycleStmtTypeImpl cycleStmtType = new CycleStmtTypeImpl();
        return cycleStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DeallocateStmtType createDeallocateStmtType() {
        final DeallocateStmtTypeImpl deallocateStmtType = new DeallocateStmtTypeImpl();
        return deallocateStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DerivedTSpecType createDerivedTSpecType() {
        final DerivedTSpecTypeImpl derivedTSpecType = new DerivedTSpecTypeImpl();
        return derivedTSpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DocumentRoot createDocumentRoot() {
        final DocumentRootImpl documentRoot = new DocumentRootImpl();
        return documentRoot;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DoStmtType createDoStmtType() {
        final DoStmtTypeImpl doStmtType = new DoStmtTypeImpl();
        return doStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DoVType createDoVType() {
        final DoVTypeImpl doVType = new DoVTypeImpl();
        return doVType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DummyArgLTType createDummyArgLTType() {
        final DummyArgLTTypeImpl dummyArgLTType = new DummyArgLTTypeImpl();
        return dummyArgLTType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public E1Type createE1Type() {
        final E1TypeImpl e1Type = new E1TypeImpl();
        return e1Type;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public E2Type createE2Type() {
        final E2TypeImpl e2Type = new E2TypeImpl();
        return e2Type;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ElementLTType createElementLTType() {
        final ElementLTTypeImpl elementLTType = new ElementLTTypeImpl();
        return elementLTType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ElementType createElementType() {
        final ElementTypeImpl elementType = new ElementTypeImpl();
        return elementType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ElseIfStmtType createElseIfStmtType() {
        final ElseIfStmtTypeImpl elseIfStmtType = new ElseIfStmtTypeImpl();
        return elseIfStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndDoStmtType createEndDoStmtType() {
        final EndDoStmtTypeImpl endDoStmtType = new EndDoStmtTypeImpl();
        return endDoStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ENDeclLTType createENDeclLTType() {
        final ENDeclLTTypeImpl enDeclLTType = new ENDeclLTTypeImpl();
        return enDeclLTType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ENDeclType createENDeclType() {
        final ENDeclTypeImpl enDeclType = new ENDeclTypeImpl();
        return enDeclType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndForallStmtType createEndForallStmtType() {
        final EndForallStmtTypeImpl endForallStmtType = new EndForallStmtTypeImpl();
        return endForallStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndFunctionStmtType createEndFunctionStmtType() {
        final EndFunctionStmtTypeImpl endFunctionStmtType = new EndFunctionStmtTypeImpl();
        return endFunctionStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndInterfaceStmtType createEndInterfaceStmtType() {
        final EndInterfaceStmtTypeImpl endInterfaceStmtType = new EndInterfaceStmtTypeImpl();
        return endInterfaceStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndModuleStmtType createEndModuleStmtType() {
        final EndModuleStmtTypeImpl endModuleStmtType = new EndModuleStmtTypeImpl();
        return endModuleStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndProgramStmtType createEndProgramStmtType() {
        final EndProgramStmtTypeImpl endProgramStmtType = new EndProgramStmtTypeImpl();
        return endProgramStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndSelectCaseStmtType createEndSelectCaseStmtType() {
        final EndSelectCaseStmtTypeImpl endSelectCaseStmtType = new EndSelectCaseStmtTypeImpl();
        return endSelectCaseStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndSubroutineStmtType createEndSubroutineStmtType() {
        final EndSubroutineStmtTypeImpl endSubroutineStmtType = new EndSubroutineStmtTypeImpl();
        return endSubroutineStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndTStmtType createEndTStmtType() {
        final EndTStmtTypeImpl endTStmtType = new EndTStmtTypeImpl();
        return endTStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ENLTType createENLTType() {
        final ENLTTypeImpl enltType = new ENLTTypeImpl();
        return enltType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ENNType createENNType() {
        final ENNTypeImpl ennType = new ENNTypeImpl();
        return ennType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ENType createENType() {
        final ENTypeImpl enType = new ENTypeImpl();
        return enType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ErrorType createErrorType() {
        final ErrorTypeImpl errorType = new ErrorTypeImpl();
        return errorType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FileType createFileType() {
        final FileTypeImpl fileType = new FileTypeImpl();
        return fileType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ForallConstructStmtType createForallConstructStmtType() {
        final ForallConstructStmtTypeImpl forallConstructStmtType = new ForallConstructStmtTypeImpl();
        return forallConstructStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ForallStmtType createForallStmtType() {
        final ForallStmtTypeImpl forallStmtType = new ForallStmtTypeImpl();
        return forallStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ForallTripletSpecLTType createForallTripletSpecLTType() {
        final ForallTripletSpecLTTypeImpl forallTripletSpecLTType = new ForallTripletSpecLTTypeImpl();
        return forallTripletSpecLTType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ForallTripletSpecType createForallTripletSpecType() {
        final ForallTripletSpecTypeImpl forallTripletSpecType = new ForallTripletSpecTypeImpl();
        return forallTripletSpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FunctionNType createFunctionNType() {
        final FunctionNTypeImpl functionNType = new FunctionNTypeImpl();
        return functionNType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FunctionStmtType createFunctionStmtType() {
        final FunctionStmtTypeImpl functionStmtType = new FunctionStmtTypeImpl();
        return functionStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IfStmtType createIfStmtType() {
        final IfStmtTypeImpl ifStmtType = new IfStmtTypeImpl();
        return ifStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IfThenStmtType createIfThenStmtType() {
        final IfThenStmtTypeImpl ifThenStmtType = new IfThenStmtTypeImpl();
        return ifThenStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public InitEType createInitEType() {
        final InitETypeImpl initEType = new InitETypeImpl();
        return initEType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public InquireStmtType createInquireStmtType() {
        final InquireStmtTypeImpl inquireStmtType = new InquireStmtTypeImpl();
        return inquireStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public InquirySpecSpecType createInquirySpecSpecType() {
        final InquirySpecSpecTypeImpl inquirySpecSpecType = new InquirySpecSpecTypeImpl();
        return inquirySpecSpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public InquirySpecType createInquirySpecType() {
        final InquirySpecTypeImpl inquirySpecType = new InquirySpecTypeImpl();
        return inquirySpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public InterfaceStmtType createInterfaceStmtType() {
        final InterfaceStmtTypeImpl interfaceStmtType = new InterfaceStmtTypeImpl();
        return interfaceStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IntrinsicTSpecType createIntrinsicTSpecType() {
        final IntrinsicTSpecTypeImpl intrinsicTSpecType = new IntrinsicTSpecTypeImpl();
        return intrinsicTSpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IoControlSpecType createIoControlSpecType() {
        final IoControlSpecTypeImpl ioControlSpecType = new IoControlSpecTypeImpl();
        return ioControlSpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IoControlType createIoControlType() {
        final IoControlTypeImpl ioControlType = new IoControlTypeImpl();
        return ioControlType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IteratorDefinitionLTType createIteratorDefinitionLTType() {
        final IteratorDefinitionLTTypeImpl iteratorDefinitionLTType = new IteratorDefinitionLTTypeImpl();
        return iteratorDefinitionLTType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IteratorElementType createIteratorElementType() {
        final IteratorElementTypeImpl iteratorElementType = new IteratorElementTypeImpl();
        return iteratorElementType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IteratorType createIteratorType() {
        final IteratorTypeImpl iteratorType = new IteratorTypeImpl();
        return iteratorType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public KSelectorType createKSelectorType() {
        final KSelectorTypeImpl kSelectorType = new KSelectorTypeImpl();
        return kSelectorType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public KSpecType createKSpecType() {
        final KSpecTypeImpl kSpecType = new KSpecTypeImpl();
        return kSpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public LabelType createLabelType() {
        final LabelTypeImpl labelType = new LabelTypeImpl();
        return labelType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public LiteralEType createLiteralEType() {
        final LiteralETypeImpl literalEType = new LiteralETypeImpl();
        return literalEType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public LowerBoundType createLowerBoundType() {
        final LowerBoundTypeImpl lowerBoundType = new LowerBoundTypeImpl();
        return lowerBoundType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public MaskEType createMaskEType() {
        final MaskETypeImpl maskEType = new MaskETypeImpl();
        return maskEType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ModuleNType createModuleNType() {
        final ModuleNTypeImpl moduleNType = new ModuleNTypeImpl();
        return moduleNType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ModuleProcedureNLTType createModuleProcedureNLTType() {
        final ModuleProcedureNLTTypeImpl moduleProcedureNLTType = new ModuleProcedureNLTTypeImpl();
        return moduleProcedureNLTType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ModuleStmtType createModuleStmtType() {
        final ModuleStmtTypeImpl moduleStmtType = new ModuleStmtTypeImpl();
        return moduleStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NamedEType createNamedEType() {
        final NamedETypeImpl namedEType = new NamedETypeImpl();
        return namedEType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NamelistGroupNType createNamelistGroupNType() {
        final NamelistGroupNTypeImpl namelistGroupNType = new NamelistGroupNTypeImpl();
        return namelistGroupNType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NamelistGroupObjLTType createNamelistGroupObjLTType() {
        final NamelistGroupObjLTTypeImpl namelistGroupObjLTType = new NamelistGroupObjLTTypeImpl();
        return namelistGroupObjLTType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NamelistGroupObjNType createNamelistGroupObjNType() {
        final NamelistGroupObjNTypeImpl namelistGroupObjNType = new NamelistGroupObjNTypeImpl();
        return namelistGroupObjNType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NamelistGroupObjType createNamelistGroupObjType() {
        final NamelistGroupObjTypeImpl namelistGroupObjType = new NamelistGroupObjTypeImpl();
        return namelistGroupObjType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NamelistStmtType createNamelistStmtType() {
        final NamelistStmtTypeImpl namelistStmtType = new NamelistStmtTypeImpl();
        return namelistStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NType createNType() {
        final NTypeImpl nType = new NTypeImpl();
        return nType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NullifyStmtType createNullifyStmtType() {
        final NullifyStmtTypeImpl nullifyStmtType = new NullifyStmtTypeImpl();
        return nullifyStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ObjectType createObjectType() {
        final ObjectTypeImpl objectType = new ObjectTypeImpl();
        return objectType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OpenStmtType createOpenStmtType() {
        final OpenStmtTypeImpl openStmtType = new OpenStmtTypeImpl();
        return openStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OpEType createOpEType() {
        final OpETypeImpl opEType = new OpETypeImpl();
        return opEType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OpType createOpType() {
        final OpTypeImpl opType = new OpTypeImpl();
        return opType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OutputItemLTType createOutputItemLTType() {
        final OutputItemLTTypeImpl outputItemLTType = new OutputItemLTTypeImpl();
        return outputItemLTType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OutputItemType createOutputItemType() {
        final OutputItemTypeImpl outputItemType = new OutputItemTypeImpl();
        return outputItemType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ParensEType createParensEType() {
        final ParensETypeImpl parensEType = new ParensETypeImpl();
        return parensEType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ParensRType createParensRType() {
        final ParensRTypeImpl parensRType = new ParensRTypeImpl();
        return parensRType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PointerAStmtType createPointerAStmtType() {
        final PointerAStmtTypeImpl pointerAStmtType = new PointerAStmtTypeImpl();
        return pointerAStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PointerStmtType createPointerStmtType() {
        final PointerStmtTypeImpl pointerStmtType = new PointerStmtTypeImpl();
        return pointerStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ProcedureDesignatorType createProcedureDesignatorType() {
        final ProcedureDesignatorTypeImpl procedureDesignatorType = new ProcedureDesignatorTypeImpl();
        return procedureDesignatorType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ProcedureStmtType createProcedureStmtType() {
        final ProcedureStmtTypeImpl procedureStmtType = new ProcedureStmtTypeImpl();
        return procedureStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ProgramNType createProgramNType() {
        final ProgramNTypeImpl programNType = new ProgramNTypeImpl();
        return programNType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ProgramStmtType createProgramStmtType() {
        final ProgramStmtTypeImpl programStmtType = new ProgramStmtTypeImpl();
        return programStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PublicStmtType createPublicStmtType() {
        final PublicStmtTypeImpl publicStmtType = new PublicStmtTypeImpl();
        return publicStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ReadStmtType createReadStmtType() {
        final ReadStmtTypeImpl readStmtType = new ReadStmtTypeImpl();
        return readStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RenameLTType createRenameLTType() {
        final RenameLTTypeImpl renameLTType = new RenameLTTypeImpl();
        return renameLTType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RenameType createRenameType() {
        final RenameTypeImpl renameType = new RenameTypeImpl();
        return renameType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ResultSpecType createResultSpecType() {
        final ResultSpecTypeImpl resultSpecType = new ResultSpecTypeImpl();
        return resultSpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RLTType createRLTType() {
        final RLTTypeImpl rltType = new RLTTypeImpl();
        return rltType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SectionSubscriptLTType createSectionSubscriptLTType() {
        final SectionSubscriptLTTypeImpl sectionSubscriptLTType = new SectionSubscriptLTTypeImpl();
        return sectionSubscriptLTType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SectionSubscriptType createSectionSubscriptType() {
        final SectionSubscriptTypeImpl sectionSubscriptType = new SectionSubscriptTypeImpl();
        return sectionSubscriptType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SelectCaseStmtType createSelectCaseStmtType() {
        final SelectCaseStmtTypeImpl selectCaseStmtType = new SelectCaseStmtTypeImpl();
        return selectCaseStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ShapeSpecLTType createShapeSpecLTType() {
        final ShapeSpecLTTypeImpl shapeSpecLTType = new ShapeSpecLTTypeImpl();
        return shapeSpecLTType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ShapeSpecType createShapeSpecType() {
        final ShapeSpecTypeImpl shapeSpecType = new ShapeSpecTypeImpl();
        return shapeSpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public StopStmtType createStopStmtType() {
        final StopStmtTypeImpl stopStmtType = new StopStmtTypeImpl();
        return stopStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public StringEType createStringEType() {
        final StringETypeImpl stringEType = new StringETypeImpl();
        return stringEType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SubroutineNType createSubroutineNType() {
        final SubroutineNTypeImpl subroutineNType = new SubroutineNTypeImpl();
        return subroutineNType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SubroutineStmtType createSubroutineStmtType() {
        final SubroutineStmtTypeImpl subroutineStmtType = new SubroutineStmtTypeImpl();
        return subroutineStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TDeclStmtType createTDeclStmtType() {
        final TDeclStmtTypeImpl tDeclStmtType = new TDeclStmtTypeImpl();
        return tDeclStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TestEType createTestEType() {
        final TestETypeImpl testEType = new TestETypeImpl();
        return testEType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TNType createTNType() {
        final TNTypeImpl tnType = new TNTypeImpl();
        return tnType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TSpecType createTSpecType() {
        final TSpecTypeImpl tSpecType = new TSpecTypeImpl();
        return tSpecType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TStmtType createTStmtType() {
        final TStmtTypeImpl tStmtType = new TStmtTypeImpl();
        return tStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public UpperBoundType createUpperBoundType() {
        final UpperBoundTypeImpl upperBoundType = new UpperBoundTypeImpl();
        return upperBoundType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public UseNType createUseNType() {
        final UseNTypeImpl useNType = new UseNTypeImpl();
        return useNType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public UseStmtType createUseStmtType() {
        final UseStmtTypeImpl useStmtType = new UseStmtTypeImpl();
        return useStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public VNType createVNType() {
        final VNTypeImpl vnType = new VNTypeImpl();
        return vnType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public VType createVType() {
        final VTypeImpl vType = new VTypeImpl();
        return vType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public WhereConstructStmtType createWhereConstructStmtType() {
        final WhereConstructStmtTypeImpl whereConstructStmtType = new WhereConstructStmtTypeImpl();
        return whereConstructStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public WhereStmtType createWhereStmtType() {
        final WhereStmtTypeImpl whereStmtType = new WhereStmtTypeImpl();
        return whereStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public WriteStmtType createWriteStmtType() {
        final WriteStmtTypeImpl writeStmtType = new WriteStmtTypeImpl();
        return writeStmtType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FxtranPackage getFxtranPackage() {
        return (FxtranPackage) this.getEPackage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @deprecated
     * @generated
     */
    @Deprecated
    public static FxtranPackage getPackage() {
        return FxtranPackage.eINSTANCE;
    }

} // FxtranFactoryImpl
