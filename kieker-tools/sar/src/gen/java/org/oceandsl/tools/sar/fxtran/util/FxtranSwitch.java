/**
 */
package org.oceandsl.tools.sar.fxtran.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

import org.oceandsl.tools.sar.fxtran.*;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance hierarchy. It supports the
 * call {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method for
 * each class of the model, starting with the actual class of the object and proceeding up the
 * inheritance hierarchy until a non-null result is returned, which is the result of the switch.
 * <!-- end-user-doc -->
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage
 * @generated
 */
public class FxtranSwitch<T> extends Switch<T> {
    /**
     * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected static FxtranPackage modelPackage;

    /**
     * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public FxtranSwitch() {
        if (modelPackage == null) {
            modelPackage = FxtranPackage.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package. <!-- begin-user-doc --> <!--
     * end-user-doc -->
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
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result;
     * it yields that result. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(final int classifierID, final EObject theEObject) {
        switch (classifierID) {
        case FxtranPackage.ACTION_STMT_TYPE: {
            final ActionStmtType actionStmtType = (ActionStmtType) theEObject;
            T result = this.caseActionStmtType(actionStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.AC_VALUE_LT_TYPE: {
            final AcValueLTType acValueLTType = (AcValueLTType) theEObject;
            T result = this.caseAcValueLTType(acValueLTType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.AC_VALUE_TYPE: {
            final AcValueType acValueType = (AcValueType) theEObject;
            T result = this.caseAcValueType(acValueType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ALLOCATE_STMT_TYPE: {
            final AllocateStmtType allocateStmtType = (AllocateStmtType) theEObject;
            T result = this.caseAllocateStmtType(allocateStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ARG_NTYPE: {
            final ArgNType argNType = (ArgNType) theEObject;
            T result = this.caseArgNType(argNType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ARG_SPEC_TYPE: {
            final ArgSpecType argSpecType = (ArgSpecType) theEObject;
            T result = this.caseArgSpecType(argSpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ARG_TYPE: {
            final ArgType argType = (ArgType) theEObject;
            T result = this.caseArgType(argType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE: {
            final ArrayConstructorEType arrayConstructorEType = (ArrayConstructorEType) theEObject;
            T result = this.caseArrayConstructorEType(arrayConstructorEType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ARRAY_RTYPE: {
            final ArrayRType arrayRType = (ArrayRType) theEObject;
            T result = this.caseArrayRType(arrayRType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ARRAY_SPEC_TYPE: {
            final ArraySpecType arraySpecType = (ArraySpecType) theEObject;
            T result = this.caseArraySpecType(arraySpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ASTMT_TYPE: {
            final AStmtType aStmtType = (AStmtType) theEObject;
            T result = this.caseAStmtType(aStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ATTRIBUTE_TYPE: {
            final AttributeType attributeType = (AttributeType) theEObject;
            T result = this.caseAttributeType(attributeType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.CALL_STMT_TYPE: {
            final CallStmtType callStmtType = (CallStmtType) theEObject;
            T result = this.caseCallStmtType(callStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.CASE_ETYPE: {
            final CaseEType caseEType = (CaseEType) theEObject;
            T result = this.caseCaseEType(caseEType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.CASE_SELECTOR_TYPE: {
            final CaseSelectorType caseSelectorType = (CaseSelectorType) theEObject;
            T result = this.caseCaseSelectorType(caseSelectorType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.CASE_STMT_TYPE: {
            final CaseStmtType caseStmtType = (CaseStmtType) theEObject;
            T result = this.caseCaseStmtType(caseStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.CASE_VALUE_RANGE_LT_TYPE: {
            final CaseValueRangeLTType caseValueRangeLTType = (CaseValueRangeLTType) theEObject;
            T result = this.caseCaseValueRangeLTType(caseValueRangeLTType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.CASE_VALUE_RANGE_TYPE: {
            final CaseValueRangeType caseValueRangeType = (CaseValueRangeType) theEObject;
            T result = this.caseCaseValueRangeType(caseValueRangeType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.CASE_VALUE_TYPE: {
            final CaseValueType caseValueType = (CaseValueType) theEObject;
            T result = this.caseCaseValueType(caseValueType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.CHAR_SELECTOR_TYPE: {
            final CharSelectorType charSelectorType = (CharSelectorType) theEObject;
            T result = this.caseCharSelectorType(charSelectorType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.CHAR_SPEC_TYPE: {
            final CharSpecType charSpecType = (CharSpecType) theEObject;
            T result = this.caseCharSpecType(charSpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.CLOSE_SPEC_SPEC_TYPE: {
            final CloseSpecSpecType closeSpecSpecType = (CloseSpecSpecType) theEObject;
            T result = this.caseCloseSpecSpecType(closeSpecSpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.CLOSE_SPEC_TYPE: {
            final CloseSpecType closeSpecType = (CloseSpecType) theEObject;
            T result = this.caseCloseSpecType(closeSpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.CLOSE_STMT_TYPE: {
            final CloseStmtType closeStmtType = (CloseStmtType) theEObject;
            T result = this.caseCloseStmtType(closeStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE: {
            final ComponentDeclStmtType componentDeclStmtType = (ComponentDeclStmtType) theEObject;
            T result = this.caseComponentDeclStmtType(componentDeclStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.COMPONENT_RTYPE: {
            final ComponentRType componentRType = (ComponentRType) theEObject;
            T result = this.caseComponentRType(componentRType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.CONDITION_ETYPE: {
            final ConditionEType conditionEType = (ConditionEType) theEObject;
            T result = this.caseConditionEType(conditionEType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.CONNECT_SPEC_SPEC_TYPE: {
            final ConnectSpecSpecType connectSpecSpecType = (ConnectSpecSpecType) theEObject;
            T result = this.caseConnectSpecSpecType(connectSpecSpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.CONNECT_SPEC_TYPE: {
            final ConnectSpecType connectSpecType = (ConnectSpecType) theEObject;
            T result = this.caseConnectSpecType(connectSpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.CYCLE_STMT_TYPE: {
            final CycleStmtType cycleStmtType = (CycleStmtType) theEObject;
            T result = this.caseCycleStmtType(cycleStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.DEALLOCATE_STMT_TYPE: {
            final DeallocateStmtType deallocateStmtType = (DeallocateStmtType) theEObject;
            T result = this.caseDeallocateStmtType(deallocateStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.DERIVED_TSPEC_TYPE: {
            final DerivedTSpecType derivedTSpecType = (DerivedTSpecType) theEObject;
            T result = this.caseDerivedTSpecType(derivedTSpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.DOCUMENT_ROOT: {
            final DocumentRoot documentRoot = (DocumentRoot) theEObject;
            T result = this.caseDocumentRoot(documentRoot);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.DO_STMT_TYPE: {
            final DoStmtType doStmtType = (DoStmtType) theEObject;
            T result = this.caseDoStmtType(doStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.DO_VTYPE: {
            final DoVType doVType = (DoVType) theEObject;
            T result = this.caseDoVType(doVType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.DUMMY_ARG_LT_TYPE: {
            final DummyArgLTType dummyArgLTType = (DummyArgLTType) theEObject;
            T result = this.caseDummyArgLTType(dummyArgLTType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.E1_TYPE: {
            final E1Type e1Type = (E1Type) theEObject;
            T result = this.caseE1Type(e1Type);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.E2_TYPE: {
            final E2Type e2Type = (E2Type) theEObject;
            T result = this.caseE2Type(e2Type);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ELEMENT_LT_TYPE: {
            final ElementLTType elementLTType = (ElementLTType) theEObject;
            T result = this.caseElementLTType(elementLTType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ELEMENT_TYPE: {
            final ElementType elementType = (ElementType) theEObject;
            T result = this.caseElementType(elementType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ELSE_IF_STMT_TYPE: {
            final ElseIfStmtType elseIfStmtType = (ElseIfStmtType) theEObject;
            T result = this.caseElseIfStmtType(elseIfStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.END_DO_STMT_TYPE: {
            final EndDoStmtType endDoStmtType = (EndDoStmtType) theEObject;
            T result = this.caseEndDoStmtType(endDoStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.EN_DECL_LT_TYPE: {
            final ENDeclLTType enDeclLTType = (ENDeclLTType) theEObject;
            T result = this.caseENDeclLTType(enDeclLTType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.EN_DECL_TYPE: {
            final ENDeclType enDeclType = (ENDeclType) theEObject;
            T result = this.caseENDeclType(enDeclType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.END_FORALL_STMT_TYPE: {
            final EndForallStmtType endForallStmtType = (EndForallStmtType) theEObject;
            T result = this.caseEndForallStmtType(endForallStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.END_FUNCTION_STMT_TYPE: {
            final EndFunctionStmtType endFunctionStmtType = (EndFunctionStmtType) theEObject;
            T result = this.caseEndFunctionStmtType(endFunctionStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.END_INTERFACE_STMT_TYPE: {
            final EndInterfaceStmtType endInterfaceStmtType = (EndInterfaceStmtType) theEObject;
            T result = this.caseEndInterfaceStmtType(endInterfaceStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.END_MODULE_STMT_TYPE: {
            final EndModuleStmtType endModuleStmtType = (EndModuleStmtType) theEObject;
            T result = this.caseEndModuleStmtType(endModuleStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.END_PROGRAM_STMT_TYPE: {
            final EndProgramStmtType endProgramStmtType = (EndProgramStmtType) theEObject;
            T result = this.caseEndProgramStmtType(endProgramStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.END_SELECT_CASE_STMT_TYPE: {
            final EndSelectCaseStmtType endSelectCaseStmtType = (EndSelectCaseStmtType) theEObject;
            T result = this.caseEndSelectCaseStmtType(endSelectCaseStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.END_SUBROUTINE_STMT_TYPE: {
            final EndSubroutineStmtType endSubroutineStmtType = (EndSubroutineStmtType) theEObject;
            T result = this.caseEndSubroutineStmtType(endSubroutineStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.END_TSTMT_TYPE: {
            final EndTStmtType endTStmtType = (EndTStmtType) theEObject;
            T result = this.caseEndTStmtType(endTStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ENLT_TYPE: {
            final ENLTType enltType = (ENLTType) theEObject;
            T result = this.caseENLTType(enltType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ENN_TYPE: {
            final ENNType ennType = (ENNType) theEObject;
            T result = this.caseENNType(ennType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.EN_TYPE: {
            final ENType enType = (ENType) theEObject;
            T result = this.caseENType(enType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ERROR_TYPE: {
            final ErrorType errorType = (ErrorType) theEObject;
            T result = this.caseErrorType(errorType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.FILE_TYPE: {
            final FileType fileType = (FileType) theEObject;
            T result = this.caseFileType(fileType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.FORALL_CONSTRUCT_STMT_TYPE: {
            final ForallConstructStmtType forallConstructStmtType = (ForallConstructStmtType) theEObject;
            T result = this.caseForallConstructStmtType(forallConstructStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.FORALL_STMT_TYPE: {
            final ForallStmtType forallStmtType = (ForallStmtType) theEObject;
            T result = this.caseForallStmtType(forallStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.FORALL_TRIPLET_SPEC_LT_TYPE: {
            final ForallTripletSpecLTType forallTripletSpecLTType = (ForallTripletSpecLTType) theEObject;
            T result = this.caseForallTripletSpecLTType(forallTripletSpecLTType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.FORALL_TRIPLET_SPEC_TYPE: {
            final ForallTripletSpecType forallTripletSpecType = (ForallTripletSpecType) theEObject;
            T result = this.caseForallTripletSpecType(forallTripletSpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.FUNCTION_NTYPE: {
            final FunctionNType functionNType = (FunctionNType) theEObject;
            T result = this.caseFunctionNType(functionNType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.FUNCTION_STMT_TYPE: {
            final FunctionStmtType functionStmtType = (FunctionStmtType) theEObject;
            T result = this.caseFunctionStmtType(functionStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.IF_STMT_TYPE: {
            final IfStmtType ifStmtType = (IfStmtType) theEObject;
            T result = this.caseIfStmtType(ifStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.IF_THEN_STMT_TYPE: {
            final IfThenStmtType ifThenStmtType = (IfThenStmtType) theEObject;
            T result = this.caseIfThenStmtType(ifThenStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.INIT_ETYPE: {
            final InitEType initEType = (InitEType) theEObject;
            T result = this.caseInitEType(initEType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.INQUIRE_STMT_TYPE: {
            final InquireStmtType inquireStmtType = (InquireStmtType) theEObject;
            T result = this.caseInquireStmtType(inquireStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.INQUIRY_SPEC_SPEC_TYPE: {
            final InquirySpecSpecType inquirySpecSpecType = (InquirySpecSpecType) theEObject;
            T result = this.caseInquirySpecSpecType(inquirySpecSpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.INQUIRY_SPEC_TYPE: {
            final InquirySpecType inquirySpecType = (InquirySpecType) theEObject;
            T result = this.caseInquirySpecType(inquirySpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.INTERFACE_STMT_TYPE: {
            final InterfaceStmtType interfaceStmtType = (InterfaceStmtType) theEObject;
            T result = this.caseInterfaceStmtType(interfaceStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.INTRINSIC_TSPEC_TYPE: {
            final IntrinsicTSpecType intrinsicTSpecType = (IntrinsicTSpecType) theEObject;
            T result = this.caseIntrinsicTSpecType(intrinsicTSpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.IO_CONTROL_SPEC_TYPE: {
            final IoControlSpecType ioControlSpecType = (IoControlSpecType) theEObject;
            T result = this.caseIoControlSpecType(ioControlSpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.IO_CONTROL_TYPE: {
            final IoControlType ioControlType = (IoControlType) theEObject;
            T result = this.caseIoControlType(ioControlType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ITERATOR_DEFINITION_LT_TYPE: {
            final IteratorDefinitionLTType iteratorDefinitionLTType = (IteratorDefinitionLTType) theEObject;
            T result = this.caseIteratorDefinitionLTType(iteratorDefinitionLTType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ITERATOR_ELEMENT_TYPE: {
            final IteratorElementType iteratorElementType = (IteratorElementType) theEObject;
            T result = this.caseIteratorElementType(iteratorElementType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.ITERATOR_TYPE: {
            final IteratorType iteratorType = (IteratorType) theEObject;
            T result = this.caseIteratorType(iteratorType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.KSELECTOR_TYPE: {
            final KSelectorType kSelectorType = (KSelectorType) theEObject;
            T result = this.caseKSelectorType(kSelectorType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.KSPEC_TYPE: {
            final KSpecType kSpecType = (KSpecType) theEObject;
            T result = this.caseKSpecType(kSpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.LABEL_TYPE: {
            final LabelType labelType = (LabelType) theEObject;
            T result = this.caseLabelType(labelType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.LITERAL_ETYPE: {
            final LiteralEType literalEType = (LiteralEType) theEObject;
            T result = this.caseLiteralEType(literalEType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.LOWER_BOUND_TYPE: {
            final LowerBoundType lowerBoundType = (LowerBoundType) theEObject;
            T result = this.caseLowerBoundType(lowerBoundType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.MASK_ETYPE: {
            final MaskEType maskEType = (MaskEType) theEObject;
            T result = this.caseMaskEType(maskEType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.MODULE_NTYPE: {
            final ModuleNType moduleNType = (ModuleNType) theEObject;
            T result = this.caseModuleNType(moduleNType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.MODULE_PROCEDURE_NLT_TYPE: {
            final ModuleProcedureNLTType moduleProcedureNLTType = (ModuleProcedureNLTType) theEObject;
            T result = this.caseModuleProcedureNLTType(moduleProcedureNLTType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.MODULE_STMT_TYPE: {
            final ModuleStmtType moduleStmtType = (ModuleStmtType) theEObject;
            T result = this.caseModuleStmtType(moduleStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.NAMED_ETYPE: {
            final NamedEType namedEType = (NamedEType) theEObject;
            T result = this.caseNamedEType(namedEType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.NAMELIST_GROUP_NTYPE: {
            final NamelistGroupNType namelistGroupNType = (NamelistGroupNType) theEObject;
            T result = this.caseNamelistGroupNType(namelistGroupNType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.NAMELIST_GROUP_OBJ_LT_TYPE: {
            final NamelistGroupObjLTType namelistGroupObjLTType = (NamelistGroupObjLTType) theEObject;
            T result = this.caseNamelistGroupObjLTType(namelistGroupObjLTType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.NAMELIST_GROUP_OBJ_NTYPE: {
            final NamelistGroupObjNType namelistGroupObjNType = (NamelistGroupObjNType) theEObject;
            T result = this.caseNamelistGroupObjNType(namelistGroupObjNType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.NAMELIST_GROUP_OBJ_TYPE: {
            final NamelistGroupObjType namelistGroupObjType = (NamelistGroupObjType) theEObject;
            T result = this.caseNamelistGroupObjType(namelistGroupObjType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.NAMELIST_STMT_TYPE: {
            final NamelistStmtType namelistStmtType = (NamelistStmtType) theEObject;
            T result = this.caseNamelistStmtType(namelistStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.NTYPE: {
            final NType nType = (NType) theEObject;
            T result = this.caseNType(nType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.NULLIFY_STMT_TYPE: {
            final NullifyStmtType nullifyStmtType = (NullifyStmtType) theEObject;
            T result = this.caseNullifyStmtType(nullifyStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.OBJECT_TYPE: {
            final ObjectType objectType = (ObjectType) theEObject;
            T result = this.caseObjectType(objectType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.OPEN_STMT_TYPE: {
            final OpenStmtType openStmtType = (OpenStmtType) theEObject;
            T result = this.caseOpenStmtType(openStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.OP_ETYPE: {
            final OpEType opEType = (OpEType) theEObject;
            T result = this.caseOpEType(opEType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.OP_TYPE: {
            final OpType opType = (OpType) theEObject;
            T result = this.caseOpType(opType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.OUTPUT_ITEM_LT_TYPE: {
            final OutputItemLTType outputItemLTType = (OutputItemLTType) theEObject;
            T result = this.caseOutputItemLTType(outputItemLTType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.OUTPUT_ITEM_TYPE: {
            final OutputItemType outputItemType = (OutputItemType) theEObject;
            T result = this.caseOutputItemType(outputItemType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.PARENS_ETYPE: {
            final ParensEType parensEType = (ParensEType) theEObject;
            T result = this.caseParensEType(parensEType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.PARENS_RTYPE: {
            final ParensRType parensRType = (ParensRType) theEObject;
            T result = this.caseParensRType(parensRType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.POINTER_ASTMT_TYPE: {
            final PointerAStmtType pointerAStmtType = (PointerAStmtType) theEObject;
            T result = this.casePointerAStmtType(pointerAStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.POINTER_STMT_TYPE: {
            final PointerStmtType pointerStmtType = (PointerStmtType) theEObject;
            T result = this.casePointerStmtType(pointerStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.PROCEDURE_DESIGNATOR_TYPE: {
            final ProcedureDesignatorType procedureDesignatorType = (ProcedureDesignatorType) theEObject;
            T result = this.caseProcedureDesignatorType(procedureDesignatorType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.PROCEDURE_STMT_TYPE: {
            final ProcedureStmtType procedureStmtType = (ProcedureStmtType) theEObject;
            T result = this.caseProcedureStmtType(procedureStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.PROGRAM_NTYPE: {
            final ProgramNType programNType = (ProgramNType) theEObject;
            T result = this.caseProgramNType(programNType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.PROGRAM_STMT_TYPE: {
            final ProgramStmtType programStmtType = (ProgramStmtType) theEObject;
            T result = this.caseProgramStmtType(programStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.PUBLIC_STMT_TYPE: {
            final PublicStmtType publicStmtType = (PublicStmtType) theEObject;
            T result = this.casePublicStmtType(publicStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.READ_STMT_TYPE: {
            final ReadStmtType readStmtType = (ReadStmtType) theEObject;
            T result = this.caseReadStmtType(readStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.RENAME_LT_TYPE: {
            final RenameLTType renameLTType = (RenameLTType) theEObject;
            T result = this.caseRenameLTType(renameLTType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.RENAME_TYPE: {
            final RenameType renameType = (RenameType) theEObject;
            T result = this.caseRenameType(renameType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.RESULT_SPEC_TYPE: {
            final ResultSpecType resultSpecType = (ResultSpecType) theEObject;
            T result = this.caseResultSpecType(resultSpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.RLT_TYPE: {
            final RLTType rltType = (RLTType) theEObject;
            T result = this.caseRLTType(rltType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.SECTION_SUBSCRIPT_LT_TYPE: {
            final SectionSubscriptLTType sectionSubscriptLTType = (SectionSubscriptLTType) theEObject;
            T result = this.caseSectionSubscriptLTType(sectionSubscriptLTType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE: {
            final SectionSubscriptType sectionSubscriptType = (SectionSubscriptType) theEObject;
            T result = this.caseSectionSubscriptType(sectionSubscriptType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.SELECT_CASE_STMT_TYPE: {
            final SelectCaseStmtType selectCaseStmtType = (SelectCaseStmtType) theEObject;
            T result = this.caseSelectCaseStmtType(selectCaseStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.SHAPE_SPEC_LT_TYPE: {
            final ShapeSpecLTType shapeSpecLTType = (ShapeSpecLTType) theEObject;
            T result = this.caseShapeSpecLTType(shapeSpecLTType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.SHAPE_SPEC_TYPE: {
            final ShapeSpecType shapeSpecType = (ShapeSpecType) theEObject;
            T result = this.caseShapeSpecType(shapeSpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.STOP_STMT_TYPE: {
            final StopStmtType stopStmtType = (StopStmtType) theEObject;
            T result = this.caseStopStmtType(stopStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.STRING_ETYPE: {
            final StringEType stringEType = (StringEType) theEObject;
            T result = this.caseStringEType(stringEType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.SUBROUTINE_NTYPE: {
            final SubroutineNType subroutineNType = (SubroutineNType) theEObject;
            T result = this.caseSubroutineNType(subroutineNType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.SUBROUTINE_STMT_TYPE: {
            final SubroutineStmtType subroutineStmtType = (SubroutineStmtType) theEObject;
            T result = this.caseSubroutineStmtType(subroutineStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.TDECL_STMT_TYPE: {
            final TDeclStmtType tDeclStmtType = (TDeclStmtType) theEObject;
            T result = this.caseTDeclStmtType(tDeclStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.TEST_ETYPE: {
            final TestEType testEType = (TestEType) theEObject;
            T result = this.caseTestEType(testEType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.TN_TYPE: {
            final TNType tnType = (TNType) theEObject;
            T result = this.caseTNType(tnType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.TSPEC_TYPE: {
            final TSpecType tSpecType = (TSpecType) theEObject;
            T result = this.caseTSpecType(tSpecType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.TSTMT_TYPE: {
            final TStmtType tStmtType = (TStmtType) theEObject;
            T result = this.caseTStmtType(tStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.UPPER_BOUND_TYPE: {
            final UpperBoundType upperBoundType = (UpperBoundType) theEObject;
            T result = this.caseUpperBoundType(upperBoundType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.USE_NTYPE: {
            final UseNType useNType = (UseNType) theEObject;
            T result = this.caseUseNType(useNType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.USE_STMT_TYPE: {
            final UseStmtType useStmtType = (UseStmtType) theEObject;
            T result = this.caseUseStmtType(useStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.VN_TYPE: {
            final VNType vnType = (VNType) theEObject;
            T result = this.caseVNType(vnType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.VTYPE: {
            final VType vType = (VType) theEObject;
            T result = this.caseVType(vType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.WHERE_CONSTRUCT_STMT_TYPE: {
            final WhereConstructStmtType whereConstructStmtType = (WhereConstructStmtType) theEObject;
            T result = this.caseWhereConstructStmtType(whereConstructStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.WHERE_STMT_TYPE: {
            final WhereStmtType whereStmtType = (WhereStmtType) theEObject;
            T result = this.caseWhereStmtType(whereStmtType);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case FxtranPackage.WRITE_STMT_TYPE: {
            final WriteStmtType writeStmtType = (WriteStmtType) theEObject;
            T result = this.caseWriteStmtType(writeStmtType);
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
     * Returns the result of interpreting the object as an instance of '<em>Action Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Action Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseActionStmtType(final ActionStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Ac Value LT Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Ac Value LT Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAcValueLTType(final AcValueLTType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Ac Value Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Ac Value Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAcValueType(final AcValueType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Allocate Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Allocate Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAllocateStmtType(final AllocateStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Arg NType</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Arg NType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArgNType(final ArgNType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Arg Spec Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Arg Spec Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArgSpecType(final ArgSpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Arg Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Arg Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArgType(final ArgType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Array Constructor
     * EType</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Array Constructor
     *         EType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArrayConstructorEType(final ArrayConstructorEType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Array RType</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Array RType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArrayRType(final ArrayRType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Array Spec Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Array Spec Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArraySpecType(final ArraySpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>AStmt Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>AStmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAStmtType(final AStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Attribute Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Attribute Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAttributeType(final AttributeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Call Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Call Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCallStmtType(final CallStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Case EType</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Case EType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCaseEType(final CaseEType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Case Selector
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Case Selector
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCaseSelectorType(final CaseSelectorType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Case Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Case Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCaseStmtType(final CaseStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Case Value Range LT
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Case Value Range LT
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCaseValueRangeLTType(final CaseValueRangeLTType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Case Value Range
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Case Value Range
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCaseValueRangeType(final CaseValueRangeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Case Value Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Case Value Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCaseValueType(final CaseValueType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Char Selector
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Char Selector
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCharSelectorType(final CharSelectorType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Char Spec Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Char Spec Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCharSpecType(final CharSpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Close Spec Spec
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Close Spec Spec
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCloseSpecSpecType(final CloseSpecSpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Close Spec Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Close Spec Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCloseSpecType(final CloseSpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Close Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Close Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCloseStmtType(final CloseStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Component Decl Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Component Decl Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComponentDeclStmtType(final ComponentDeclStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Component RType</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Component RType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComponentRType(final ComponentRType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Condition EType</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Condition EType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConditionEType(final ConditionEType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Connect Spec Spec
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Connect Spec Spec
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConnectSpecSpecType(final ConnectSpecSpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Connect Spec Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Connect Spec Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConnectSpecType(final ConnectSpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cycle Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cycle Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCycleStmtType(final CycleStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Deallocate Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Deallocate Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDeallocateStmtType(final DeallocateStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Derived TSpec
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Derived TSpec
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDerivedTSpecType(final DerivedTSpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Document Root</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Document Root</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDocumentRoot(final DocumentRoot object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Do Stmt Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Do Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDoStmtType(final DoStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Do VType</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Do VType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDoVType(final DoVType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dummy Arg LT Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dummy Arg LT Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDummyArgLTType(final DummyArgLTType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>E1 Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>E1 Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseE1Type(final E1Type object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>E2 Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>E2 Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseE2Type(final E2Type object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Element LT Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Element LT Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseElementLTType(final ElementLTType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Element Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Element Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseElementType(final ElementType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Else If Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Else If Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseElseIfStmtType(final ElseIfStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>End Do Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>End Do Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEndDoStmtType(final EndDoStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EN Decl LT Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EN Decl LT Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseENDeclLTType(final ENDeclLTType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EN Decl Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EN Decl Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseENDeclType(final ENDeclType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>End Forall Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>End Forall Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEndForallStmtType(final EndForallStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>End Function Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>End Function Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEndFunctionStmtType(final EndFunctionStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>End Interface Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>End Interface Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEndInterfaceStmtType(final EndInterfaceStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>End Module Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>End Module Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEndModuleStmtType(final EndModuleStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>End Program Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>End Program Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEndProgramStmtType(final EndProgramStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>End Select Case Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>End Select Case Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEndSelectCaseStmtType(final EndSelectCaseStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>End Subroutine Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>End Subroutine Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEndSubroutineStmtType(final EndSubroutineStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>End TStmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>End TStmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEndTStmtType(final EndTStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>ENLT Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>ENLT Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseENLTType(final ENLTType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>ENN Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>ENN Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseENNType(final ENNType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EN Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EN Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseENType(final ENType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Error Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Error Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseErrorType(final ErrorType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>File Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>File Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFileType(final FileType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Forall Construct Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Forall Construct Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseForallConstructStmtType(final ForallConstructStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Forall Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Forall Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseForallStmtType(final ForallStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Forall Triplet Spec LT
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Forall Triplet Spec LT
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseForallTripletSpecLTType(final ForallTripletSpecLTType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Forall Triplet Spec
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Forall Triplet Spec
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseForallTripletSpecType(final ForallTripletSpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Function NType</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Function NType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFunctionNType(final FunctionNType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Function Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Function Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFunctionStmtType(final FunctionStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>If Stmt Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>If Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIfStmtType(final IfStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>If Then Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>If Then Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIfThenStmtType(final IfThenStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Init EType</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Init EType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInitEType(final InitEType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Inquire Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Inquire Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInquireStmtType(final InquireStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Inquiry Spec Spec
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Inquiry Spec Spec
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInquirySpecSpecType(final InquirySpecSpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Inquiry Spec Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Inquiry Spec Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInquirySpecType(final InquirySpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Interface Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Interface Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInterfaceStmtType(final InterfaceStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Intrinsic TSpec
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Intrinsic TSpec
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIntrinsicTSpecType(final IntrinsicTSpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Io Control Spec
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Io Control Spec
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIoControlSpecType(final IoControlSpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Io Control Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Io Control Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIoControlType(final IoControlType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Iterator Definition LT
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Iterator Definition LT
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIteratorDefinitionLTType(final IteratorDefinitionLTType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Iterator Element
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Iterator Element
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIteratorElementType(final IteratorElementType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Iterator Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Iterator Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIteratorType(final IteratorType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>KSelector Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>KSelector Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseKSelectorType(final KSelectorType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>KSpec Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>KSpec Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseKSpecType(final KSpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Label Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Label Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLabelType(final LabelType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Literal EType</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Literal EType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLiteralEType(final LiteralEType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Lower Bound Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Lower Bound Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLowerBoundType(final LowerBoundType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Mask EType</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Mask EType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMaskEType(final MaskEType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Module NType</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Module NType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseModuleNType(final ModuleNType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Module Procedure NLT
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Module Procedure NLT
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseModuleProcedureNLTType(final ModuleProcedureNLTType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Module Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Module Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseModuleStmtType(final ModuleStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Named EType</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Named EType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNamedEType(final NamedEType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Namelist Group
     * NType</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Namelist Group
     *         NType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNamelistGroupNType(final NamelistGroupNType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Namelist Group Obj LT
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Namelist Group Obj LT
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNamelistGroupObjLTType(final NamelistGroupObjLTType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Namelist Group Obj
     * NType</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Namelist Group Obj
     *         NType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNamelistGroupObjNType(final NamelistGroupObjNType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Namelist Group Obj
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Namelist Group Obj
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNamelistGroupObjType(final NamelistGroupObjType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Namelist Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Namelist Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNamelistStmtType(final NamelistStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>NType</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>NType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNType(final NType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Nullify Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Nullify Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNullifyStmtType(final NullifyStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Object Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Object Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseObjectType(final ObjectType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Open Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Open Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOpenStmtType(final OpenStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Op EType</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Op EType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOpEType(final OpEType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Op Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Op Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOpType(final OpType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Output Item LT
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Output Item LT
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOutputItemLTType(final OutputItemLTType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Output Item Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Output Item Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOutputItemType(final OutputItemType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Parens EType</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Parens EType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseParensEType(final ParensEType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Parens RType</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Parens RType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseParensRType(final ParensRType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Pointer AStmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Pointer AStmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePointerAStmtType(final PointerAStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Pointer Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Pointer Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePointerStmtType(final PointerStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Procedure Designator
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Procedure Designator
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProcedureDesignatorType(final ProcedureDesignatorType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Procedure Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Procedure Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProcedureStmtType(final ProcedureStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Program NType</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Program NType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProgramNType(final ProgramNType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Program Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Program Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProgramStmtType(final ProgramStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Public Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Public Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePublicStmtType(final PublicStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Read Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Read Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReadStmtType(final ReadStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Rename LT Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Rename LT Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRenameLTType(final RenameLTType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Rename Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Rename Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRenameType(final RenameType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Result Spec Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Result Spec Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseResultSpecType(final ResultSpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>RLT Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>RLT Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRLTType(final RLTType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Section Subscript LT
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Section Subscript LT
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSectionSubscriptLTType(final SectionSubscriptLTType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Section Subscript
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Section Subscript
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSectionSubscriptType(final SectionSubscriptType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Select Case Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Select Case Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSelectCaseStmtType(final SelectCaseStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Shape Spec LT
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Shape Spec LT
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseShapeSpecLTType(final ShapeSpecLTType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Shape Spec Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Shape Spec Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseShapeSpecType(final ShapeSpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Stop Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Stop Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStopStmtType(final StopStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>String EType</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>String EType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStringEType(final StringEType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Subroutine NType</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Subroutine NType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSubroutineNType(final SubroutineNType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Subroutine Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Subroutine Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSubroutineStmtType(final SubroutineStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>TDecl Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>TDecl Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTDeclStmtType(final TDeclStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Test EType</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Test EType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTestEType(final TestEType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>TN Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>TN Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTNType(final TNType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>TSpec Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>TSpec Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTSpecType(final TSpecType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>TStmt Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>TStmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTStmtType(final TStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Upper Bound Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Upper Bound Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUpperBoundType(final UpperBoundType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Use NType</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Use NType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUseNType(final UseNType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Use Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Use Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUseStmtType(final UseStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>VN Type</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>VN Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVNType(final VNType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>VType</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>VType</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVType(final VType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Where Construct Stmt
     * Type</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Where Construct Stmt
     *         Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWhereConstructStmtType(final WhereConstructStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Where Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Where Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWhereStmtType(final WhereStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Write Stmt Type</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     *
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Write Stmt Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWriteStmtType(final WriteStmtType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch, but this is the last case anyway. <!-- end-user-doc -->
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

} // FxtranSwitch
