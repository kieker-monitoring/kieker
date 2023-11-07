/**
 */
package org.oceandsl.tools.sar.fxtran.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import org.oceandsl.tools.sar.fxtran.*;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides an adapter
 * <code>createXXX</code> method for each class of the model. <!-- end-user-doc -->
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage
 * @generated
 */
public class FxtranAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected static FxtranPackage modelPackage;

    /**
     * Creates an instance of the adapter factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public FxtranAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = FxtranPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object. <!-- begin-user-doc
     * --> This implementation returns <code>true</code> if the object is either the model's package
     * or is an instance object of the model. <!-- end-user-doc -->
     *
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(final Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject) object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    protected FxtranSwitch<Adapter> modelSwitch = new FxtranSwitch<>() {
        @Override
        public Adapter caseActionStmtType(final ActionStmtType object) {
            return FxtranAdapterFactory.this.createActionStmtTypeAdapter();
        }

        @Override
        public Adapter caseAcValueLTType(final AcValueLTType object) {
            return FxtranAdapterFactory.this.createAcValueLTTypeAdapter();
        }

        @Override
        public Adapter caseAcValueType(final AcValueType object) {
            return FxtranAdapterFactory.this.createAcValueTypeAdapter();
        }

        @Override
        public Adapter caseAllocateStmtType(final AllocateStmtType object) {
            return FxtranAdapterFactory.this.createAllocateStmtTypeAdapter();
        }

        @Override
        public Adapter caseArgNType(final ArgNType object) {
            return FxtranAdapterFactory.this.createArgNTypeAdapter();
        }

        @Override
        public Adapter caseArgSpecType(final ArgSpecType object) {
            return FxtranAdapterFactory.this.createArgSpecTypeAdapter();
        }

        @Override
        public Adapter caseArgType(final ArgType object) {
            return FxtranAdapterFactory.this.createArgTypeAdapter();
        }

        @Override
        public Adapter caseArrayConstructorEType(final ArrayConstructorEType object) {
            return FxtranAdapterFactory.this.createArrayConstructorETypeAdapter();
        }

        @Override
        public Adapter caseArrayRType(final ArrayRType object) {
            return FxtranAdapterFactory.this.createArrayRTypeAdapter();
        }

        @Override
        public Adapter caseArraySpecType(final ArraySpecType object) {
            return FxtranAdapterFactory.this.createArraySpecTypeAdapter();
        }

        @Override
        public Adapter caseAStmtType(final AStmtType object) {
            return FxtranAdapterFactory.this.createAStmtTypeAdapter();
        }

        @Override
        public Adapter caseAttributeType(final AttributeType object) {
            return FxtranAdapterFactory.this.createAttributeTypeAdapter();
        }

        @Override
        public Adapter caseCallStmtType(final CallStmtType object) {
            return FxtranAdapterFactory.this.createCallStmtTypeAdapter();
        }

        @Override
        public Adapter caseCaseEType(final CaseEType object) {
            return FxtranAdapterFactory.this.createCaseETypeAdapter();
        }

        @Override
        public Adapter caseCaseSelectorType(final CaseSelectorType object) {
            return FxtranAdapterFactory.this.createCaseSelectorTypeAdapter();
        }

        @Override
        public Adapter caseCaseStmtType(final CaseStmtType object) {
            return FxtranAdapterFactory.this.createCaseStmtTypeAdapter();
        }

        @Override
        public Adapter caseCaseValueRangeLTType(final CaseValueRangeLTType object) {
            return FxtranAdapterFactory.this.createCaseValueRangeLTTypeAdapter();
        }

        @Override
        public Adapter caseCaseValueRangeType(final CaseValueRangeType object) {
            return FxtranAdapterFactory.this.createCaseValueRangeTypeAdapter();
        }

        @Override
        public Adapter caseCaseValueType(final CaseValueType object) {
            return FxtranAdapterFactory.this.createCaseValueTypeAdapter();
        }

        @Override
        public Adapter caseCharSelectorType(final CharSelectorType object) {
            return FxtranAdapterFactory.this.createCharSelectorTypeAdapter();
        }

        @Override
        public Adapter caseCharSpecType(final CharSpecType object) {
            return FxtranAdapterFactory.this.createCharSpecTypeAdapter();
        }

        @Override
        public Adapter caseCloseSpecSpecType(final CloseSpecSpecType object) {
            return FxtranAdapterFactory.this.createCloseSpecSpecTypeAdapter();
        }

        @Override
        public Adapter caseCloseSpecType(final CloseSpecType object) {
            return FxtranAdapterFactory.this.createCloseSpecTypeAdapter();
        }

        @Override
        public Adapter caseCloseStmtType(final CloseStmtType object) {
            return FxtranAdapterFactory.this.createCloseStmtTypeAdapter();
        }

        @Override
        public Adapter caseComponentDeclStmtType(final ComponentDeclStmtType object) {
            return FxtranAdapterFactory.this.createComponentDeclStmtTypeAdapter();
        }

        @Override
        public Adapter caseComponentRType(final ComponentRType object) {
            return FxtranAdapterFactory.this.createComponentRTypeAdapter();
        }

        @Override
        public Adapter caseConditionEType(final ConditionEType object) {
            return FxtranAdapterFactory.this.createConditionETypeAdapter();
        }

        @Override
        public Adapter caseConnectSpecSpecType(final ConnectSpecSpecType object) {
            return FxtranAdapterFactory.this.createConnectSpecSpecTypeAdapter();
        }

        @Override
        public Adapter caseConnectSpecType(final ConnectSpecType object) {
            return FxtranAdapterFactory.this.createConnectSpecTypeAdapter();
        }

        @Override
        public Adapter caseCycleStmtType(final CycleStmtType object) {
            return FxtranAdapterFactory.this.createCycleStmtTypeAdapter();
        }

        @Override
        public Adapter caseDeallocateStmtType(final DeallocateStmtType object) {
            return FxtranAdapterFactory.this.createDeallocateStmtTypeAdapter();
        }

        @Override
        public Adapter caseDerivedTSpecType(final DerivedTSpecType object) {
            return FxtranAdapterFactory.this.createDerivedTSpecTypeAdapter();
        }

        @Override
        public Adapter caseDocumentRoot(final DocumentRoot object) {
            return FxtranAdapterFactory.this.createDocumentRootAdapter();
        }

        @Override
        public Adapter caseDoStmtType(final DoStmtType object) {
            return FxtranAdapterFactory.this.createDoStmtTypeAdapter();
        }

        @Override
        public Adapter caseDoVType(final DoVType object) {
            return FxtranAdapterFactory.this.createDoVTypeAdapter();
        }

        @Override
        public Adapter caseDummyArgLTType(final DummyArgLTType object) {
            return FxtranAdapterFactory.this.createDummyArgLTTypeAdapter();
        }

        @Override
        public Adapter caseE1Type(final E1Type object) {
            return FxtranAdapterFactory.this.createE1TypeAdapter();
        }

        @Override
        public Adapter caseE2Type(final E2Type object) {
            return FxtranAdapterFactory.this.createE2TypeAdapter();
        }

        @Override
        public Adapter caseElementLTType(final ElementLTType object) {
            return FxtranAdapterFactory.this.createElementLTTypeAdapter();
        }

        @Override
        public Adapter caseElementType(final ElementType object) {
            return FxtranAdapterFactory.this.createElementTypeAdapter();
        }

        @Override
        public Adapter caseElseIfStmtType(final ElseIfStmtType object) {
            return FxtranAdapterFactory.this.createElseIfStmtTypeAdapter();
        }

        @Override
        public Adapter caseEndDoStmtType(final EndDoStmtType object) {
            return FxtranAdapterFactory.this.createEndDoStmtTypeAdapter();
        }

        @Override
        public Adapter caseENDeclLTType(final ENDeclLTType object) {
            return FxtranAdapterFactory.this.createENDeclLTTypeAdapter();
        }

        @Override
        public Adapter caseENDeclType(final ENDeclType object) {
            return FxtranAdapterFactory.this.createENDeclTypeAdapter();
        }

        @Override
        public Adapter caseEndForallStmtType(final EndForallStmtType object) {
            return FxtranAdapterFactory.this.createEndForallStmtTypeAdapter();
        }

        @Override
        public Adapter caseEndFunctionStmtType(final EndFunctionStmtType object) {
            return FxtranAdapterFactory.this.createEndFunctionStmtTypeAdapter();
        }

        @Override
        public Adapter caseEndInterfaceStmtType(final EndInterfaceStmtType object) {
            return FxtranAdapterFactory.this.createEndInterfaceStmtTypeAdapter();
        }

        @Override
        public Adapter caseEndModuleStmtType(final EndModuleStmtType object) {
            return FxtranAdapterFactory.this.createEndModuleStmtTypeAdapter();
        }

        @Override
        public Adapter caseEndProgramStmtType(final EndProgramStmtType object) {
            return FxtranAdapterFactory.this.createEndProgramStmtTypeAdapter();
        }

        @Override
        public Adapter caseEndSelectCaseStmtType(final EndSelectCaseStmtType object) {
            return FxtranAdapterFactory.this.createEndSelectCaseStmtTypeAdapter();
        }

        @Override
        public Adapter caseEndSubroutineStmtType(final EndSubroutineStmtType object) {
            return FxtranAdapterFactory.this.createEndSubroutineStmtTypeAdapter();
        }

        @Override
        public Adapter caseEndTStmtType(final EndTStmtType object) {
            return FxtranAdapterFactory.this.createEndTStmtTypeAdapter();
        }

        @Override
        public Adapter caseENLTType(final ENLTType object) {
            return FxtranAdapterFactory.this.createENLTTypeAdapter();
        }

        @Override
        public Adapter caseENNType(final ENNType object) {
            return FxtranAdapterFactory.this.createENNTypeAdapter();
        }

        @Override
        public Adapter caseENType(final ENType object) {
            return FxtranAdapterFactory.this.createENTypeAdapter();
        }

        @Override
        public Adapter caseErrorType(final ErrorType object) {
            return FxtranAdapterFactory.this.createErrorTypeAdapter();
        }

        @Override
        public Adapter caseFileType(final FileType object) {
            return FxtranAdapterFactory.this.createFileTypeAdapter();
        }

        @Override
        public Adapter caseForallConstructStmtType(final ForallConstructStmtType object) {
            return FxtranAdapterFactory.this.createForallConstructStmtTypeAdapter();
        }

        @Override
        public Adapter caseForallStmtType(final ForallStmtType object) {
            return FxtranAdapterFactory.this.createForallStmtTypeAdapter();
        }

        @Override
        public Adapter caseForallTripletSpecLTType(final ForallTripletSpecLTType object) {
            return FxtranAdapterFactory.this.createForallTripletSpecLTTypeAdapter();
        }

        @Override
        public Adapter caseForallTripletSpecType(final ForallTripletSpecType object) {
            return FxtranAdapterFactory.this.createForallTripletSpecTypeAdapter();
        }

        @Override
        public Adapter caseFunctionNType(final FunctionNType object) {
            return FxtranAdapterFactory.this.createFunctionNTypeAdapter();
        }

        @Override
        public Adapter caseFunctionStmtType(final FunctionStmtType object) {
            return FxtranAdapterFactory.this.createFunctionStmtTypeAdapter();
        }

        @Override
        public Adapter caseIfStmtType(final IfStmtType object) {
            return FxtranAdapterFactory.this.createIfStmtTypeAdapter();
        }

        @Override
        public Adapter caseIfThenStmtType(final IfThenStmtType object) {
            return FxtranAdapterFactory.this.createIfThenStmtTypeAdapter();
        }

        @Override
        public Adapter caseInitEType(final InitEType object) {
            return FxtranAdapterFactory.this.createInitETypeAdapter();
        }

        @Override
        public Adapter caseInquireStmtType(final InquireStmtType object) {
            return FxtranAdapterFactory.this.createInquireStmtTypeAdapter();
        }

        @Override
        public Adapter caseInquirySpecSpecType(final InquirySpecSpecType object) {
            return FxtranAdapterFactory.this.createInquirySpecSpecTypeAdapter();
        }

        @Override
        public Adapter caseInquirySpecType(final InquirySpecType object) {
            return FxtranAdapterFactory.this.createInquirySpecTypeAdapter();
        }

        @Override
        public Adapter caseInterfaceStmtType(final InterfaceStmtType object) {
            return FxtranAdapterFactory.this.createInterfaceStmtTypeAdapter();
        }

        @Override
        public Adapter caseIntrinsicTSpecType(final IntrinsicTSpecType object) {
            return FxtranAdapterFactory.this.createIntrinsicTSpecTypeAdapter();
        }

        @Override
        public Adapter caseIoControlSpecType(final IoControlSpecType object) {
            return FxtranAdapterFactory.this.createIoControlSpecTypeAdapter();
        }

        @Override
        public Adapter caseIoControlType(final IoControlType object) {
            return FxtranAdapterFactory.this.createIoControlTypeAdapter();
        }

        @Override
        public Adapter caseIteratorDefinitionLTType(final IteratorDefinitionLTType object) {
            return FxtranAdapterFactory.this.createIteratorDefinitionLTTypeAdapter();
        }

        @Override
        public Adapter caseIteratorElementType(final IteratorElementType object) {
            return FxtranAdapterFactory.this.createIteratorElementTypeAdapter();
        }

        @Override
        public Adapter caseIteratorType(final IteratorType object) {
            return FxtranAdapterFactory.this.createIteratorTypeAdapter();
        }

        @Override
        public Adapter caseKSelectorType(final KSelectorType object) {
            return FxtranAdapterFactory.this.createKSelectorTypeAdapter();
        }

        @Override
        public Adapter caseKSpecType(final KSpecType object) {
            return FxtranAdapterFactory.this.createKSpecTypeAdapter();
        }

        @Override
        public Adapter caseLabelType(final LabelType object) {
            return FxtranAdapterFactory.this.createLabelTypeAdapter();
        }

        @Override
        public Adapter caseLiteralEType(final LiteralEType object) {
            return FxtranAdapterFactory.this.createLiteralETypeAdapter();
        }

        @Override
        public Adapter caseLowerBoundType(final LowerBoundType object) {
            return FxtranAdapterFactory.this.createLowerBoundTypeAdapter();
        }

        @Override
        public Adapter caseMaskEType(final MaskEType object) {
            return FxtranAdapterFactory.this.createMaskETypeAdapter();
        }

        @Override
        public Adapter caseModuleNType(final ModuleNType object) {
            return FxtranAdapterFactory.this.createModuleNTypeAdapter();
        }

        @Override
        public Adapter caseModuleProcedureNLTType(final ModuleProcedureNLTType object) {
            return FxtranAdapterFactory.this.createModuleProcedureNLTTypeAdapter();
        }

        @Override
        public Adapter caseModuleStmtType(final ModuleStmtType object) {
            return FxtranAdapterFactory.this.createModuleStmtTypeAdapter();
        }

        @Override
        public Adapter caseNamedEType(final NamedEType object) {
            return FxtranAdapterFactory.this.createNamedETypeAdapter();
        }

        @Override
        public Adapter caseNamelistGroupNType(final NamelistGroupNType object) {
            return FxtranAdapterFactory.this.createNamelistGroupNTypeAdapter();
        }

        @Override
        public Adapter caseNamelistGroupObjLTType(final NamelistGroupObjLTType object) {
            return FxtranAdapterFactory.this.createNamelistGroupObjLTTypeAdapter();
        }

        @Override
        public Adapter caseNamelistGroupObjNType(final NamelistGroupObjNType object) {
            return FxtranAdapterFactory.this.createNamelistGroupObjNTypeAdapter();
        }

        @Override
        public Adapter caseNamelistGroupObjType(final NamelistGroupObjType object) {
            return FxtranAdapterFactory.this.createNamelistGroupObjTypeAdapter();
        }

        @Override
        public Adapter caseNamelistStmtType(final NamelistStmtType object) {
            return FxtranAdapterFactory.this.createNamelistStmtTypeAdapter();
        }

        @Override
        public Adapter caseNType(final NType object) {
            return FxtranAdapterFactory.this.createNTypeAdapter();
        }

        @Override
        public Adapter caseNullifyStmtType(final NullifyStmtType object) {
            return FxtranAdapterFactory.this.createNullifyStmtTypeAdapter();
        }

        @Override
        public Adapter caseObjectType(final ObjectType object) {
            return FxtranAdapterFactory.this.createObjectTypeAdapter();
        }

        @Override
        public Adapter caseOpenStmtType(final OpenStmtType object) {
            return FxtranAdapterFactory.this.createOpenStmtTypeAdapter();
        }

        @Override
        public Adapter caseOpEType(final OpEType object) {
            return FxtranAdapterFactory.this.createOpETypeAdapter();
        }

        @Override
        public Adapter caseOpType(final OpType object) {
            return FxtranAdapterFactory.this.createOpTypeAdapter();
        }

        @Override
        public Adapter caseOutputItemLTType(final OutputItemLTType object) {
            return FxtranAdapterFactory.this.createOutputItemLTTypeAdapter();
        }

        @Override
        public Adapter caseOutputItemType(final OutputItemType object) {
            return FxtranAdapterFactory.this.createOutputItemTypeAdapter();
        }

        @Override
        public Adapter caseParensEType(final ParensEType object) {
            return FxtranAdapterFactory.this.createParensETypeAdapter();
        }

        @Override
        public Adapter caseParensRType(final ParensRType object) {
            return FxtranAdapterFactory.this.createParensRTypeAdapter();
        }

        @Override
        public Adapter casePointerAStmtType(final PointerAStmtType object) {
            return FxtranAdapterFactory.this.createPointerAStmtTypeAdapter();
        }

        @Override
        public Adapter casePointerStmtType(final PointerStmtType object) {
            return FxtranAdapterFactory.this.createPointerStmtTypeAdapter();
        }

        @Override
        public Adapter caseProcedureDesignatorType(final ProcedureDesignatorType object) {
            return FxtranAdapterFactory.this.createProcedureDesignatorTypeAdapter();
        }

        @Override
        public Adapter caseProcedureStmtType(final ProcedureStmtType object) {
            return FxtranAdapterFactory.this.createProcedureStmtTypeAdapter();
        }

        @Override
        public Adapter caseProgramNType(final ProgramNType object) {
            return FxtranAdapterFactory.this.createProgramNTypeAdapter();
        }

        @Override
        public Adapter caseProgramStmtType(final ProgramStmtType object) {
            return FxtranAdapterFactory.this.createProgramStmtTypeAdapter();
        }

        @Override
        public Adapter casePublicStmtType(final PublicStmtType object) {
            return FxtranAdapterFactory.this.createPublicStmtTypeAdapter();
        }

        @Override
        public Adapter caseReadStmtType(final ReadStmtType object) {
            return FxtranAdapterFactory.this.createReadStmtTypeAdapter();
        }

        @Override
        public Adapter caseRenameLTType(final RenameLTType object) {
            return FxtranAdapterFactory.this.createRenameLTTypeAdapter();
        }

        @Override
        public Adapter caseRenameType(final RenameType object) {
            return FxtranAdapterFactory.this.createRenameTypeAdapter();
        }

        @Override
        public Adapter caseResultSpecType(final ResultSpecType object) {
            return FxtranAdapterFactory.this.createResultSpecTypeAdapter();
        }

        @Override
        public Adapter caseRLTType(final RLTType object) {
            return FxtranAdapterFactory.this.createRLTTypeAdapter();
        }

        @Override
        public Adapter caseSectionSubscriptLTType(final SectionSubscriptLTType object) {
            return FxtranAdapterFactory.this.createSectionSubscriptLTTypeAdapter();
        }

        @Override
        public Adapter caseSectionSubscriptType(final SectionSubscriptType object) {
            return FxtranAdapterFactory.this.createSectionSubscriptTypeAdapter();
        }

        @Override
        public Adapter caseSelectCaseStmtType(final SelectCaseStmtType object) {
            return FxtranAdapterFactory.this.createSelectCaseStmtTypeAdapter();
        }

        @Override
        public Adapter caseShapeSpecLTType(final ShapeSpecLTType object) {
            return FxtranAdapterFactory.this.createShapeSpecLTTypeAdapter();
        }

        @Override
        public Adapter caseShapeSpecType(final ShapeSpecType object) {
            return FxtranAdapterFactory.this.createShapeSpecTypeAdapter();
        }

        @Override
        public Adapter caseStopStmtType(final StopStmtType object) {
            return FxtranAdapterFactory.this.createStopStmtTypeAdapter();
        }

        @Override
        public Adapter caseStringEType(final StringEType object) {
            return FxtranAdapterFactory.this.createStringETypeAdapter();
        }

        @Override
        public Adapter caseSubroutineNType(final SubroutineNType object) {
            return FxtranAdapterFactory.this.createSubroutineNTypeAdapter();
        }

        @Override
        public Adapter caseSubroutineStmtType(final SubroutineStmtType object) {
            return FxtranAdapterFactory.this.createSubroutineStmtTypeAdapter();
        }

        @Override
        public Adapter caseTDeclStmtType(final TDeclStmtType object) {
            return FxtranAdapterFactory.this.createTDeclStmtTypeAdapter();
        }

        @Override
        public Adapter caseTestEType(final TestEType object) {
            return FxtranAdapterFactory.this.createTestETypeAdapter();
        }

        @Override
        public Adapter caseTNType(final TNType object) {
            return FxtranAdapterFactory.this.createTNTypeAdapter();
        }

        @Override
        public Adapter caseTSpecType(final TSpecType object) {
            return FxtranAdapterFactory.this.createTSpecTypeAdapter();
        }

        @Override
        public Adapter caseTStmtType(final TStmtType object) {
            return FxtranAdapterFactory.this.createTStmtTypeAdapter();
        }

        @Override
        public Adapter caseUpperBoundType(final UpperBoundType object) {
            return FxtranAdapterFactory.this.createUpperBoundTypeAdapter();
        }

        @Override
        public Adapter caseUseNType(final UseNType object) {
            return FxtranAdapterFactory.this.createUseNTypeAdapter();
        }

        @Override
        public Adapter caseUseStmtType(final UseStmtType object) {
            return FxtranAdapterFactory.this.createUseStmtTypeAdapter();
        }

        @Override
        public Adapter caseVNType(final VNType object) {
            return FxtranAdapterFactory.this.createVNTypeAdapter();
        }

        @Override
        public Adapter caseVType(final VType object) {
            return FxtranAdapterFactory.this.createVTypeAdapter();
        }

        @Override
        public Adapter caseWhereConstructStmtType(final WhereConstructStmtType object) {
            return FxtranAdapterFactory.this.createWhereConstructStmtTypeAdapter();
        }

        @Override
        public Adapter caseWhereStmtType(final WhereStmtType object) {
            return FxtranAdapterFactory.this.createWhereStmtTypeAdapter();
        }

        @Override
        public Adapter caseWriteStmtType(final WriteStmtType object) {
            return FxtranAdapterFactory.this.createWriteStmtTypeAdapter();
        }

        @Override
        public Adapter defaultCase(final EObject object) {
            return FxtranAdapterFactory.this.createEObjectAdapter();
        }
    };

    /**
     * Creates an adapter for the <code>target</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param target
     *            the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(final Notifier target) {
        return this.modelSwitch.doSwitch((EObject) target);
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType <em>Action Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ActionStmtType
     * @generated
     */
    public Adapter createActionStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.AcValueLTType <em>Ac Value LT Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.AcValueLTType
     * @generated
     */
    public Adapter createAcValueLTTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.AcValueType <em>Ac Value Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.AcValueType
     * @generated
     */
    public Adapter createAcValueTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.AllocateStmtType <em>Allocate Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.AllocateStmtType
     * @generated
     */
    public Adapter createAllocateStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.ArgNType
     * <em>Arg NType</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ArgNType
     * @generated
     */
    public Adapter createArgNTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ArgSpecType <em>Arg Spec Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ArgSpecType
     * @generated
     */
    public Adapter createArgSpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.ArgType
     * <em>Arg Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that
     * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
     * cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ArgType
     * @generated
     */
    public Adapter createArgTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ArrayConstructorEType <em>Array Constructor
     * EType</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ArrayConstructorEType
     * @generated
     */
    public Adapter createArrayConstructorETypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.ArrayRType
     * <em>Array RType</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ArrayRType
     * @generated
     */
    public Adapter createArrayRTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ArraySpecType <em>Array Spec Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ArraySpecType
     * @generated
     */
    public Adapter createArraySpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.AStmtType
     * <em>AStmt Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.AStmtType
     * @generated
     */
    public Adapter createAStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.AttributeType <em>Attribute Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.AttributeType
     * @generated
     */
    public Adapter createAttributeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.CallStmtType <em>Call Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.CallStmtType
     * @generated
     */
    public Adapter createCallStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.CaseEType
     * <em>Case EType</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.CaseEType
     * @generated
     */
    public Adapter createCaseETypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.CaseSelectorType <em>Case Selector Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.CaseSelectorType
     * @generated
     */
    public Adapter createCaseSelectorTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.CaseStmtType <em>Case Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.CaseStmtType
     * @generated
     */
    public Adapter createCaseStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.CaseValueRangeLTType <em>Case Value Range LT
     * Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.CaseValueRangeLTType
     * @generated
     */
    public Adapter createCaseValueRangeLTTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.CaseValueRangeType <em>Case Value Range Type</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.CaseValueRangeType
     * @generated
     */
    public Adapter createCaseValueRangeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.CaseValueType <em>Case Value Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.CaseValueType
     * @generated
     */
    public Adapter createCaseValueTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.CharSelectorType <em>Char Selector Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.CharSelectorType
     * @generated
     */
    public Adapter createCharSelectorTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.CharSpecType <em>Char Spec Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.CharSpecType
     * @generated
     */
    public Adapter createCharSpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.CloseSpecSpecType <em>Close Spec Spec Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.CloseSpecSpecType
     * @generated
     */
    public Adapter createCloseSpecSpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.CloseSpecType <em>Close Spec Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.CloseSpecType
     * @generated
     */
    public Adapter createCloseSpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.CloseStmtType <em>Close Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.CloseStmtType
     * @generated
     */
    public Adapter createCloseStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType <em>Component Decl Stmt
     * Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType
     * @generated
     */
    public Adapter createComponentDeclStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ComponentRType <em>Component RType</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ComponentRType
     * @generated
     */
    public Adapter createComponentRTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ConditionEType <em>Condition EType</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ConditionEType
     * @generated
     */
    public Adapter createConditionETypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ConnectSpecSpecType <em>Connect Spec Spec Type</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ConnectSpecSpecType
     * @generated
     */
    public Adapter createConnectSpecSpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ConnectSpecType <em>Connect Spec Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ConnectSpecType
     * @generated
     */
    public Adapter createConnectSpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.CycleStmtType <em>Cycle Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.CycleStmtType
     * @generated
     */
    public Adapter createCycleStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.DeallocateStmtType <em>Deallocate Stmt Type</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.DeallocateStmtType
     * @generated
     */
    public Adapter createDeallocateStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.DerivedTSpecType <em>Derived TSpec Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.DerivedTSpecType
     * @generated
     */
    public Adapter createDerivedTSpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot <em>Document Root</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot
     * @generated
     */
    public Adapter createDocumentRootAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.DoStmtType
     * <em>Do Stmt Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.DoStmtType
     * @generated
     */
    public Adapter createDoStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.DoVType
     * <em>Do VType</em>}'. <!-- begin-user-doc --> This default implementation returns null so that
     * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
     * cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.DoVType
     * @generated
     */
    public Adapter createDoVTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.DummyArgLTType <em>Dummy Arg LT Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.DummyArgLTType
     * @generated
     */
    public Adapter createDummyArgLTTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.E1Type
     * <em>E1 Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that
     * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
     * cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.E1Type
     * @generated
     */
    public Adapter createE1TypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.E2Type
     * <em>E2 Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that
     * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
     * cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.E2Type
     * @generated
     */
    public Adapter createE2TypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ElementLTType <em>Element LT Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ElementLTType
     * @generated
     */
    public Adapter createElementLTTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ElementType <em>Element Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ElementType
     * @generated
     */
    public Adapter createElementTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ElseIfStmtType <em>Else If Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ElseIfStmtType
     * @generated
     */
    public Adapter createElseIfStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.EndDoStmtType <em>End Do Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.EndDoStmtType
     * @generated
     */
    public Adapter createEndDoStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ENDeclLTType <em>EN Decl LT Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ENDeclLTType
     * @generated
     */
    public Adapter createENDeclLTTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.ENDeclType
     * <em>EN Decl Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ENDeclType
     * @generated
     */
    public Adapter createENDeclTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.EndForallStmtType <em>End Forall Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.EndForallStmtType
     * @generated
     */
    public Adapter createEndForallStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.EndFunctionStmtType <em>End Function Stmt Type</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.EndFunctionStmtType
     * @generated
     */
    public Adapter createEndFunctionStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.EndInterfaceStmtType <em>End Interface Stmt
     * Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.EndInterfaceStmtType
     * @generated
     */
    public Adapter createEndInterfaceStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.EndModuleStmtType <em>End Module Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.EndModuleStmtType
     * @generated
     */
    public Adapter createEndModuleStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.EndProgramStmtType <em>End Program Stmt Type</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.EndProgramStmtType
     * @generated
     */
    public Adapter createEndProgramStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.EndSelectCaseStmtType <em>End Select Case Stmt
     * Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.EndSelectCaseStmtType
     * @generated
     */
    public Adapter createEndSelectCaseStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.EndSubroutineStmtType <em>End Subroutine Stmt
     * Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.EndSubroutineStmtType
     * @generated
     */
    public Adapter createEndSubroutineStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.EndTStmtType <em>End TStmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.EndTStmtType
     * @generated
     */
    public Adapter createEndTStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.ENLTType
     * <em>ENLT Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ENLTType
     * @generated
     */
    public Adapter createENLTTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.ENNType
     * <em>ENN Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that
     * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
     * cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ENNType
     * @generated
     */
    public Adapter createENNTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.ENType
     * <em>EN Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that
     * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
     * cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ENType
     * @generated
     */
    public Adapter createENTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.ErrorType
     * <em>Error Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ErrorType
     * @generated
     */
    public Adapter createErrorTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.FileType
     * <em>File Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.FileType
     * @generated
     */
    public Adapter createFileTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ForallConstructStmtType <em>Forall Construct Stmt
     * Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ForallConstructStmtType
     * @generated
     */
    public Adapter createForallConstructStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ForallStmtType <em>Forall Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ForallStmtType
     * @generated
     */
    public Adapter createForallStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ForallTripletSpecLTType <em>Forall Triplet Spec LT
     * Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ForallTripletSpecLTType
     * @generated
     */
    public Adapter createForallTripletSpecLTTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ForallTripletSpecType <em>Forall Triplet Spec
     * Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ForallTripletSpecType
     * @generated
     */
    public Adapter createForallTripletSpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.FunctionNType <em>Function NType</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.FunctionNType
     * @generated
     */
    public Adapter createFunctionNTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType <em>Function Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.FunctionStmtType
     * @generated
     */
    public Adapter createFunctionStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.IfStmtType
     * <em>If Stmt Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.IfStmtType
     * @generated
     */
    public Adapter createIfStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.IfThenStmtType <em>If Then Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.IfThenStmtType
     * @generated
     */
    public Adapter createIfThenStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.InitEType
     * <em>Init EType</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.InitEType
     * @generated
     */
    public Adapter createInitETypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.InquireStmtType <em>Inquire Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.InquireStmtType
     * @generated
     */
    public Adapter createInquireStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.InquirySpecSpecType <em>Inquiry Spec Spec Type</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.InquirySpecSpecType
     * @generated
     */
    public Adapter createInquirySpecSpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.InquirySpecType <em>Inquiry Spec Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.InquirySpecType
     * @generated
     */
    public Adapter createInquirySpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.InterfaceStmtType <em>Interface Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.InterfaceStmtType
     * @generated
     */
    public Adapter createInterfaceStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType <em>Intrinsic TSpec Type</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType
     * @generated
     */
    public Adapter createIntrinsicTSpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.IoControlSpecType <em>Io Control Spec Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.IoControlSpecType
     * @generated
     */
    public Adapter createIoControlSpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.IoControlType <em>Io Control Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.IoControlType
     * @generated
     */
    public Adapter createIoControlTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.IteratorDefinitionLTType <em>Iterator Definition LT
     * Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.IteratorDefinitionLTType
     * @generated
     */
    public Adapter createIteratorDefinitionLTTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.IteratorElementType <em>Iterator Element Type</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.IteratorElementType
     * @generated
     */
    public Adapter createIteratorElementTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.IteratorType <em>Iterator Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.IteratorType
     * @generated
     */
    public Adapter createIteratorTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.KSelectorType <em>KSelector Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.KSelectorType
     * @generated
     */
    public Adapter createKSelectorTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.KSpecType
     * <em>KSpec Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.KSpecType
     * @generated
     */
    public Adapter createKSpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.LabelType
     * <em>Label Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.LabelType
     * @generated
     */
    public Adapter createLabelTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.LiteralEType <em>Literal EType</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.LiteralEType
     * @generated
     */
    public Adapter createLiteralETypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.LowerBoundType <em>Lower Bound Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.LowerBoundType
     * @generated
     */
    public Adapter createLowerBoundTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.MaskEType
     * <em>Mask EType</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.MaskEType
     * @generated
     */
    public Adapter createMaskETypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ModuleNType <em>Module NType</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ModuleNType
     * @generated
     */
    public Adapter createModuleNTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ModuleProcedureNLTType <em>Module Procedure NLT
     * Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ModuleProcedureNLTType
     * @generated
     */
    public Adapter createModuleProcedureNLTTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ModuleStmtType <em>Module Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ModuleStmtType
     * @generated
     */
    public Adapter createModuleStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.NamedEType
     * <em>Named EType</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.NamedEType
     * @generated
     */
    public Adapter createNamedETypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupNType <em>Namelist Group NType</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.NamelistGroupNType
     * @generated
     */
    public Adapter createNamelistGroupNTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupObjLTType <em>Namelist Group Obj LT
     * Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.NamelistGroupObjLTType
     * @generated
     */
    public Adapter createNamelistGroupObjLTTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupObjNType <em>Namelist Group Obj
     * NType</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.NamelistGroupObjNType
     * @generated
     */
    public Adapter createNamelistGroupObjNTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupObjType <em>Namelist Group Obj
     * Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.NamelistGroupObjType
     * @generated
     */
    public Adapter createNamelistGroupObjTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistStmtType <em>Namelist Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.NamelistStmtType
     * @generated
     */
    public Adapter createNamelistStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.NType
     * <em>NType</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we
     * can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
     * cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.NType
     * @generated
     */
    public Adapter createNTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.NullifyStmtType <em>Nullify Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.NullifyStmtType
     * @generated
     */
    public Adapter createNullifyStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.ObjectType
     * <em>Object Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ObjectType
     * @generated
     */
    public Adapter createObjectTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.OpenStmtType <em>Open Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.OpenStmtType
     * @generated
     */
    public Adapter createOpenStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.OpEType
     * <em>Op EType</em>}'. <!-- begin-user-doc --> This default implementation returns null so that
     * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
     * cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.OpEType
     * @generated
     */
    public Adapter createOpETypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.OpType
     * <em>Op Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that
     * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
     * cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.OpType
     * @generated
     */
    public Adapter createOpTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.OutputItemLTType <em>Output Item LT Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.OutputItemLTType
     * @generated
     */
    public Adapter createOutputItemLTTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.OutputItemType <em>Output Item Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.OutputItemType
     * @generated
     */
    public Adapter createOutputItemTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ParensEType <em>Parens EType</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ParensEType
     * @generated
     */
    public Adapter createParensETypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ParensRType <em>Parens RType</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ParensRType
     * @generated
     */
    public Adapter createParensRTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.PointerAStmtType <em>Pointer AStmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.PointerAStmtType
     * @generated
     */
    public Adapter createPointerAStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.PointerStmtType <em>Pointer Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.PointerStmtType
     * @generated
     */
    public Adapter createPointerStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ProcedureDesignatorType <em>Procedure Designator
     * Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ProcedureDesignatorType
     * @generated
     */
    public Adapter createProcedureDesignatorTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ProcedureStmtType <em>Procedure Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ProcedureStmtType
     * @generated
     */
    public Adapter createProcedureStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ProgramNType <em>Program NType</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ProgramNType
     * @generated
     */
    public Adapter createProgramNTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ProgramStmtType <em>Program Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ProgramStmtType
     * @generated
     */
    public Adapter createProgramStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.PublicStmtType <em>Public Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.PublicStmtType
     * @generated
     */
    public Adapter createPublicStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ReadStmtType <em>Read Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ReadStmtType
     * @generated
     */
    public Adapter createReadStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.RenameLTType <em>Rename LT Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.RenameLTType
     * @generated
     */
    public Adapter createRenameLTTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.RenameType
     * <em>Rename Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.RenameType
     * @generated
     */
    public Adapter createRenameTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ResultSpecType <em>Result Spec Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ResultSpecType
     * @generated
     */
    public Adapter createResultSpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.RLTType
     * <em>RLT Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that
     * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
     * cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.RLTType
     * @generated
     */
    public Adapter createRLTTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.SectionSubscriptLTType <em>Section Subscript LT
     * Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.SectionSubscriptLTType
     * @generated
     */
    public Adapter createSectionSubscriptLTTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.SectionSubscriptType <em>Section Subscript Type</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.SectionSubscriptType
     * @generated
     */
    public Adapter createSectionSubscriptTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.SelectCaseStmtType <em>Select Case Stmt Type</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.SelectCaseStmtType
     * @generated
     */
    public Adapter createSelectCaseStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ShapeSpecLTType <em>Shape Spec LT Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ShapeSpecLTType
     * @generated
     */
    public Adapter createShapeSpecLTTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.ShapeSpecType <em>Shape Spec Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.ShapeSpecType
     * @generated
     */
    public Adapter createShapeSpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.StopStmtType <em>Stop Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.StopStmtType
     * @generated
     */
    public Adapter createStopStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.StringEType <em>String EType</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.StringEType
     * @generated
     */
    public Adapter createStringETypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.SubroutineNType <em>Subroutine NType</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.SubroutineNType
     * @generated
     */
    public Adapter createSubroutineNTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.SubroutineStmtType <em>Subroutine Stmt Type</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.SubroutineStmtType
     * @generated
     */
    public Adapter createSubroutineStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.TDeclStmtType <em>TDecl Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.TDeclStmtType
     * @generated
     */
    public Adapter createTDeclStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.TestEType
     * <em>Test EType</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.TestEType
     * @generated
     */
    public Adapter createTestETypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.TNType
     * <em>TN Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that
     * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
     * cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.TNType
     * @generated
     */
    public Adapter createTNTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.TSpecType
     * <em>TSpec Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.TSpecType
     * @generated
     */
    public Adapter createTSpecTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.TStmtType
     * <em>TStmt Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.TStmtType
     * @generated
     */
    public Adapter createTStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.UpperBoundType <em>Upper Bound Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.UpperBoundType
     * @generated
     */
    public Adapter createUpperBoundTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.UseNType
     * <em>Use NType</em>}'. <!-- begin-user-doc --> This default implementation returns null so
     * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
     * the cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.UseNType
     * @generated
     */
    public Adapter createUseNTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.UseStmtType <em>Use Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.UseStmtType
     * @generated
     */
    public Adapter createUseStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.VNType
     * <em>VN Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that
     * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
     * cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.VNType
     * @generated
     */
    public Adapter createVNTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.oceandsl.tools.sar.fxtran.VType
     * <em>VType</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we
     * can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
     * cases anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.VType
     * @generated
     */
    public Adapter createVTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.WhereConstructStmtType <em>Where Construct Stmt
     * Type</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can
     * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases
     * anyway. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.WhereConstructStmtType
     * @generated
     */
    public Adapter createWhereConstructStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.WhereStmtType <em>Where Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.WhereStmtType
     * @generated
     */
    public Adapter createWhereStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.oceandsl.tools.sar.fxtran.WriteStmtType <em>Write Stmt Type</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     *
     * @return the new adapter.
     * @see org.oceandsl.tools.sar.fxtran.WriteStmtType
     * @generated
     */
    public Adapter createWriteStmtTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case. <!-- begin-user-doc --> This default
     * implementation returns null. <!-- end-user-doc -->
     *
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} // FxtranAdapterFactory
