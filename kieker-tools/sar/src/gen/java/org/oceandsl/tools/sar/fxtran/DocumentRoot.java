/**
 */
package org.oceandsl.tools.sar.fxtran;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix
 * Map</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getXSISchemaLocation <em>XSI Schema
 * Location</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getTSpec <em>TSpec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getA <em>A</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAStmt <em>AStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAcValue <em>Ac Value</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAcValueLT <em>Ac Value LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getActionStmt <em>Action Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAllocateStmt <em>Allocate
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArg <em>Arg</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArgN <em>Arg N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArgSpec <em>Arg Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArrayConstructorE <em>Array Constructor
 * E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArrayR <em>Array R</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArraySpec <em>Array Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAttribute <em>Attribute</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAttributeN <em>Attribute N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getC <em>C</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCallStmt <em>Call Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseE <em>Case E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseSelector <em>Case
 * Selector</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseStmt <em>Case Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseValue <em>Case Value</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseValueRange <em>Case Value
 * Range</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseValueRangeLT <em>Case Value Range
 * LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCharSelector <em>Char
 * Selector</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCharSpec <em>Char Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCloseSpec <em>Close Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCloseSpecSpec <em>Close Spec
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCloseStmt <em>Close Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCnt <em>Cnt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getComponentDeclStmt <em>Component Decl
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getComponentR <em>Component R</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getConditionE <em>Condition E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getConnectSpec <em>Connect Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getConnectSpecSpec <em>Connect Spec
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getContainsStmt <em>Contains
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCpp <em>Cpp</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCt <em>Ct</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCycleStmt <em>Cycle Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getDeallocateStmt <em>Deallocate
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getDerivedTSpec <em>Derived
 * TSpec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getDoStmt <em>Do Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getDoV <em>Do V</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getDummyArgLT <em>Dummy Arg LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getE1 <em>E1</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getE2 <em>E2</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getElement <em>Element</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getElementLT <em>Element LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getElseIfStmt <em>Else If Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getElseStmt <em>Else Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getElseWhereStmt <em>Else Where
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEN <em>EN</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getENDecl <em>EN Decl</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getENDeclLT <em>EN Decl LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getENLT <em>ENLT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getENN <em>ENN</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndDoStmt <em>End Do Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndForallStmt <em>End Forall
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndFunctionStmt <em>End Function
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndIfStmt <em>End If Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndInterfaceStmt <em>End Interface
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndModuleStmt <em>End Module
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndProgramStmt <em>End Program
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndSelectCaseStmt <em>End Select Case
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndSubroutineStmt <em>End Subroutine
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndTStmt <em>End TStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndWhereStmt <em>End Where
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getError <em>Error</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getExitStmt <em>Exit Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getFile <em>File</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getForallConstructStmt <em>Forall Construct
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getForallStmt <em>Forall Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getForallTripletSpec <em>Forall Triplet
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getForallTripletSpecLT <em>Forall Triplet
 * Spec LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getFunctionN <em>Function N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getFunctionStmt <em>Function
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIfStmt <em>If Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIfThenStmt <em>If Then Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getImplicitNoneStmt <em>Implicit None
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getInitE <em>Init E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getInquireStmt <em>Inquire Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getInquirySpec <em>Inquiry Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getInquirySpecSpec <em>Inquiry Spec
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIntentSpec <em>Intent Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getInterfaceStmt <em>Interface
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIntrinsicTSpec <em>Intrinsic
 * TSpec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIoControl <em>Io Control</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIoControlSpec <em>Io Control
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIterator <em>Iterator</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIteratorDefinitionLT <em>Iterator
 * Definition LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIteratorElement <em>Iterator
 * Element</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getK <em>K</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getKSelector <em>KSelector</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getKSpec <em>KSpec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getL <em>L</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getLabel <em>Label</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getLiteralE <em>Literal E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getLowerBound <em>Lower Bound</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getMaskE <em>Mask E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getModuleN <em>Module N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getModuleProcedureNLT <em>Module Procedure
 * NLT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getModuleStmt <em>Module Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getN <em>N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getN1 <em>N1</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamedE <em>Named E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistGroupN <em>Namelist Group
 * N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistGroupObj <em>Namelist Group
 * Obj</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistGroupObjLT <em>Namelist Group
 * Obj LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistGroupObjN <em>Namelist Group Obj
 * N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistStmt <em>Namelist
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNullifyStmt <em>Nullify Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getO <em>O</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getObject <em>Object</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getOp <em>Op</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getOpE <em>Op E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getOpenStmt <em>Open Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getOutputItem <em>Output Item</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getOutputItemLT <em>Output Item
 * LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getParensE <em>Parens E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getParensR <em>Parens R</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getPointerAStmt <em>Pointer
 * AStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getPointerStmt <em>Pointer Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getPrefix <em>Prefix</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getPrivateStmt <em>Private Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getProcedureDesignator <em>Procedure
 * Designator</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getProcedureStmt <em>Procedure
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getProgramN <em>Program N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getProgramStmt <em>Program Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getPublicStmt <em>Public Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getRLT <em>RLT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getReadStmt <em>Read Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getRename <em>Rename</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getRenameLT <em>Rename LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getResultSpec <em>Result Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getReturnStmt <em>Return Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getS <em>S</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSaveStmt <em>Save Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSectionSubscript <em>Section
 * Subscript</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSectionSubscriptLT <em>Section Subscript
 * LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSelectCaseStmt <em>Select Case
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getShapeSpec <em>Shape Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getShapeSpecLT <em>Shape Spec LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getStarE <em>Star E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getStopCode <em>Stop Code</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getStopStmt <em>Stop Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getStringE <em>String E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSubroutineN <em>Subroutine N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSubroutineStmt <em>Subroutine
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getTDeclStmt <em>TDecl Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getTN <em>TN</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getTStmt <em>TStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getTestE <em>Test E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getUpperBound <em>Upper Bound</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getUseN <em>Use N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getUseStmt <em>Use Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getV <em>V</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getVN <em>VN</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getWhereConstructStmt <em>Where Construct
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getWhereStmt <em>Where Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getWriteStmt <em>Write Stmt</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map. The key is of type
     * {@link java.lang.String}, and the value is of type {@link java.lang.String}, <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>XMLNS Prefix Map</em>' map.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_XMLNSPrefixMap()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry&lt;org.eclipse.emf.ecore.EString,
     *        org.eclipse.emf.ecore.EString&gt;" transient="true" extendedMetaData="kind='attribute'
     *        name='xmlns:prefix'"
     * @generated
     */
    EMap<String, String> getXMLNSPrefixMap();

    /**
     * Returns the value of the '<em><b>XSI Schema Location</b></em>' map. The key is of type
     * {@link java.lang.String}, and the value is of type {@link java.lang.String}, <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>XSI Schema Location</em>' map.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_XSISchemaLocation()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry&lt;org.eclipse.emf.ecore.EString,
     *        org.eclipse.emf.ecore.EString&gt;" transient="true" extendedMetaData="kind='attribute'
     *        name='xsi:schemaLocation'"
     * @generated
     */
    EMap<String, String> getXSISchemaLocation();

    /**
     * Returns the value of the '<em><b>TSpec</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>TSpec</em>' containment reference.
     * @see #setTSpec(TSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_TSpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_T-spec_' namespace='##targetNamespace'"
     * @generated
     */
    TSpecType getTSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getTSpec
     * <em>TSpec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>TSpec</em>' containment reference.
     * @see #getTSpec()
     * @generated
     */
    void setTSpec(TSpecType value);

    /**
     * Returns the value of the '<em><b>A</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>A</em>' attribute.
     * @see #setA(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_A()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='a' namespace='##targetNamespace'"
     * @generated
     */
    String getA();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getA <em>A</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>A</em>' attribute.
     * @see #getA()
     * @generated
     */
    void setA(String value);

    /**
     * Returns the value of the '<em><b>AStmt</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>AStmt</em>' containment reference.
     * @see #setAStmt(AStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_AStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='a-stmt' namespace='##targetNamespace'"
     * @generated
     */
    AStmtType getAStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAStmt
     * <em>AStmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>AStmt</em>' containment reference.
     * @see #getAStmt()
     * @generated
     */
    void setAStmt(AStmtType value);

    /**
     * Returns the value of the '<em><b>Ac Value</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Ac Value</em>' containment reference.
     * @see #setAcValue(AcValueType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_AcValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ac-value' namespace='##targetNamespace'"
     * @generated
     */
    AcValueType getAcValue();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAcValue <em>Ac
     * Value</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Ac Value</em>' containment reference.
     * @see #getAcValue()
     * @generated
     */
    void setAcValue(AcValueType value);

    /**
     * Returns the value of the '<em><b>Ac Value LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Ac Value LT</em>' containment reference.
     * @see #setAcValueLT(AcValueLTType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_AcValueLT()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ac-value-LT' namespace='##targetNamespace'"
     * @generated
     */
    AcValueLTType getAcValueLT();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAcValueLT <em>Ac
     * Value LT</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Ac Value LT</em>' containment reference.
     * @see #getAcValueLT()
     * @generated
     */
    void setAcValueLT(AcValueLTType value);

    /**
     * Returns the value of the '<em><b>Action Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Action Stmt</em>' containment reference.
     * @see #setActionStmt(ActionStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ActionStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='action-stmt' namespace='##targetNamespace'"
     * @generated
     */
    ActionStmtType getActionStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getActionStmt
     * <em>Action Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Action Stmt</em>' containment reference.
     * @see #getActionStmt()
     * @generated
     */
    void setActionStmt(ActionStmtType value);

    /**
     * Returns the value of the '<em><b>Allocate Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Allocate Stmt</em>' containment reference.
     * @see #setAllocateStmt(AllocateStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_AllocateStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='allocate-stmt' namespace='##targetNamespace'"
     * @generated
     */
    AllocateStmtType getAllocateStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAllocateStmt
     * <em>Allocate Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Allocate Stmt</em>' containment reference.
     * @see #getAllocateStmt()
     * @generated
     */
    void setAllocateStmt(AllocateStmtType value);

    /**
     * Returns the value of the '<em><b>Arg</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Arg</em>' containment reference.
     * @see #setArg(ArgType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_Arg()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='arg' namespace='##targetNamespace'"
     * @generated
     */
    ArgType getArg();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArg
     * <em>Arg</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Arg</em>' containment reference.
     * @see #getArg()
     * @generated
     */
    void setArg(ArgType value);

    /**
     * Returns the value of the '<em><b>Arg N</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Arg N</em>' containment reference.
     * @see #setArgN(ArgNType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ArgN()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='arg-N' namespace='##targetNamespace'"
     * @generated
     */
    ArgNType getArgN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArgN <em>Arg
     * N</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Arg N</em>' containment reference.
     * @see #getArgN()
     * @generated
     */
    void setArgN(ArgNType value);

    /**
     * Returns the value of the '<em><b>Arg Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Arg Spec</em>' containment reference.
     * @see #setArgSpec(ArgSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ArgSpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='arg-spec' namespace='##targetNamespace'"
     * @generated
     */
    ArgSpecType getArgSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArgSpec <em>Arg
     * Spec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Arg Spec</em>' containment reference.
     * @see #getArgSpec()
     * @generated
     */
    void setArgSpec(ArgSpecType value);

    /**
     * Returns the value of the '<em><b>Array Constructor E</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Array Constructor E</em>' containment reference.
     * @see #setArrayConstructorE(ArrayConstructorEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ArrayConstructorE()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='array-constructor-E'
     *        namespace='##targetNamespace'"
     * @generated
     */
    ArrayConstructorEType getArrayConstructorE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArrayConstructorE
     * <em>Array Constructor E</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Array Constructor E</em>' containment reference.
     * @see #getArrayConstructorE()
     * @generated
     */
    void setArrayConstructorE(ArrayConstructorEType value);

    /**
     * Returns the value of the '<em><b>Array R</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Array R</em>' containment reference.
     * @see #setArrayR(ArrayRType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ArrayR()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='array-R' namespace='##targetNamespace'"
     * @generated
     */
    ArrayRType getArrayR();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArrayR <em>Array
     * R</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Array R</em>' containment reference.
     * @see #getArrayR()
     * @generated
     */
    void setArrayR(ArrayRType value);

    /**
     * Returns the value of the '<em><b>Array Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Array Spec</em>' containment reference.
     * @see #setArraySpec(ArraySpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ArraySpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='array-spec' namespace='##targetNamespace'"
     * @generated
     */
    ArraySpecType getArraySpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArraySpec
     * <em>Array Spec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Array Spec</em>' containment reference.
     * @see #getArraySpec()
     * @generated
     */
    void setArraySpec(ArraySpecType value);

    /**
     * Returns the value of the '<em><b>Attribute</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Attribute</em>' containment reference.
     * @see #setAttribute(AttributeType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_Attribute()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='attribute' namespace='##targetNamespace'"
     * @generated
     */
    AttributeType getAttribute();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAttribute
     * <em>Attribute</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Attribute</em>' containment reference.
     * @see #getAttribute()
     * @generated
     */
    void setAttribute(AttributeType value);

    /**
     * Returns the value of the '<em><b>Attribute N</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Attribute N</em>' attribute.
     * @see #setAttributeN(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_AttributeN()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='attribute-N' namespace='##targetNamespace'"
     * @generated
     */
    String getAttributeN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAttributeN
     * <em>Attribute N</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Attribute N</em>' attribute.
     * @see #getAttributeN()
     * @generated
     */
    void setAttributeN(String value);

    /**
     * Returns the value of the '<em><b>C</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>C</em>' attribute.
     * @see #setC(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_C()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='C' namespace='##targetNamespace'"
     * @generated
     */
    String getC();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getC <em>C</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>C</em>' attribute.
     * @see #getC()
     * @generated
     */
    void setC(String value);

    /**
     * Returns the value of the '<em><b>Call Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Call Stmt</em>' containment reference.
     * @see #setCallStmt(CallStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_CallStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='call-stmt' namespace='##targetNamespace'"
     * @generated
     */
    CallStmtType getCallStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCallStmt <em>Call
     * Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Call Stmt</em>' containment reference.
     * @see #getCallStmt()
     * @generated
     */
    void setCallStmt(CallStmtType value);

    /**
     * Returns the value of the '<em><b>Case E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Case E</em>' containment reference.
     * @see #setCaseE(CaseEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_CaseE()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='case-E' namespace='##targetNamespace'"
     * @generated
     */
    CaseEType getCaseE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseE <em>Case
     * E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Case E</em>' containment reference.
     * @see #getCaseE()
     * @generated
     */
    void setCaseE(CaseEType value);

    /**
     * Returns the value of the '<em><b>Case Selector</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Case Selector</em>' containment reference.
     * @see #setCaseSelector(CaseSelectorType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_CaseSelector()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='case-selector' namespace='##targetNamespace'"
     * @generated
     */
    CaseSelectorType getCaseSelector();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseSelector
     * <em>Case Selector</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Case Selector</em>' containment reference.
     * @see #getCaseSelector()
     * @generated
     */
    void setCaseSelector(CaseSelectorType value);

    /**
     * Returns the value of the '<em><b>Case Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Case Stmt</em>' containment reference.
     * @see #setCaseStmt(CaseStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_CaseStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='case-stmt' namespace='##targetNamespace'"
     * @generated
     */
    CaseStmtType getCaseStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseStmt <em>Case
     * Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Case Stmt</em>' containment reference.
     * @see #getCaseStmt()
     * @generated
     */
    void setCaseStmt(CaseStmtType value);

    /**
     * Returns the value of the '<em><b>Case Value</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Case Value</em>' containment reference.
     * @see #setCaseValue(CaseValueType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_CaseValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='case-value' namespace='##targetNamespace'"
     * @generated
     */
    CaseValueType getCaseValue();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseValue
     * <em>Case Value</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Case Value</em>' containment reference.
     * @see #getCaseValue()
     * @generated
     */
    void setCaseValue(CaseValueType value);

    /**
     * Returns the value of the '<em><b>Case Value Range</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Case Value Range</em>' containment reference.
     * @see #setCaseValueRange(CaseValueRangeType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_CaseValueRange()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='case-value-range'
     *        namespace='##targetNamespace'"
     * @generated
     */
    CaseValueRangeType getCaseValueRange();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseValueRange
     * <em>Case Value Range</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Case Value Range</em>' containment reference.
     * @see #getCaseValueRange()
     * @generated
     */
    void setCaseValueRange(CaseValueRangeType value);

    /**
     * Returns the value of the '<em><b>Case Value Range LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Case Value Range LT</em>' containment reference.
     * @see #setCaseValueRangeLT(CaseValueRangeLTType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_CaseValueRangeLT()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='case-value-range-LT'
     *        namespace='##targetNamespace'"
     * @generated
     */
    CaseValueRangeLTType getCaseValueRangeLT();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseValueRangeLT
     * <em>Case Value Range LT</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Case Value Range LT</em>' containment reference.
     * @see #getCaseValueRangeLT()
     * @generated
     */
    void setCaseValueRangeLT(CaseValueRangeLTType value);

    /**
     * Returns the value of the '<em><b>Char Selector</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Char Selector</em>' containment reference.
     * @see #setCharSelector(CharSelectorType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_CharSelector()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='char-selector' namespace='##targetNamespace'"
     * @generated
     */
    CharSelectorType getCharSelector();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCharSelector
     * <em>Char Selector</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Char Selector</em>' containment reference.
     * @see #getCharSelector()
     * @generated
     */
    void setCharSelector(CharSelectorType value);

    /**
     * Returns the value of the '<em><b>Char Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Char Spec</em>' containment reference.
     * @see #setCharSpec(CharSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_CharSpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='char-spec' namespace='##targetNamespace'"
     * @generated
     */
    CharSpecType getCharSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCharSpec <em>Char
     * Spec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Char Spec</em>' containment reference.
     * @see #getCharSpec()
     * @generated
     */
    void setCharSpec(CharSpecType value);

    /**
     * Returns the value of the '<em><b>Close Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Close Spec</em>' containment reference.
     * @see #setCloseSpec(CloseSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_CloseSpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='close-spec' namespace='##targetNamespace'"
     * @generated
     */
    CloseSpecType getCloseSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCloseSpec
     * <em>Close Spec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Close Spec</em>' containment reference.
     * @see #getCloseSpec()
     * @generated
     */
    void setCloseSpec(CloseSpecType value);

    /**
     * Returns the value of the '<em><b>Close Spec Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Close Spec Spec</em>' containment reference.
     * @see #setCloseSpecSpec(CloseSpecSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_CloseSpecSpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='close-spec-spec' namespace='##targetNamespace'"
     * @generated
     */
    CloseSpecSpecType getCloseSpecSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCloseSpecSpec
     * <em>Close Spec Spec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Close Spec Spec</em>' containment reference.
     * @see #getCloseSpecSpec()
     * @generated
     */
    void setCloseSpecSpec(CloseSpecSpecType value);

    /**
     * Returns the value of the '<em><b>Close Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Close Stmt</em>' containment reference.
     * @see #setCloseStmt(CloseStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_CloseStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='close-stmt' namespace='##targetNamespace'"
     * @generated
     */
    CloseStmtType getCloseStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCloseStmt
     * <em>Close Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Close Stmt</em>' containment reference.
     * @see #getCloseStmt()
     * @generated
     */
    void setCloseStmt(CloseStmtType value);

    /**
     * Returns the value of the '<em><b>Cnt</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Cnt</em>' attribute.
     * @see #setCnt(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_Cnt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='cnt' namespace='##targetNamespace'"
     * @generated
     */
    String getCnt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCnt
     * <em>Cnt</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Cnt</em>' attribute.
     * @see #getCnt()
     * @generated
     */
    void setCnt(String value);

    /**
     * Returns the value of the '<em><b>Component Decl Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Component Decl Stmt</em>' containment reference.
     * @see #setComponentDeclStmt(ComponentDeclStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ComponentDeclStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='component-decl-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    ComponentDeclStmtType getComponentDeclStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getComponentDeclStmt
     * <em>Component Decl Stmt</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Component Decl Stmt</em>' containment reference.
     * @see #getComponentDeclStmt()
     * @generated
     */
    void setComponentDeclStmt(ComponentDeclStmtType value);

    /**
     * Returns the value of the '<em><b>Component R</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Component R</em>' containment reference.
     * @see #setComponentR(ComponentRType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ComponentR()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='component-R' namespace='##targetNamespace'"
     * @generated
     */
    ComponentRType getComponentR();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getComponentR
     * <em>Component R</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Component R</em>' containment reference.
     * @see #getComponentR()
     * @generated
     */
    void setComponentR(ComponentRType value);

    /**
     * Returns the value of the '<em><b>Condition E</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Condition E</em>' containment reference.
     * @see #setConditionE(ConditionEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ConditionE()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='condition-E' namespace='##targetNamespace'"
     * @generated
     */
    ConditionEType getConditionE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getConditionE
     * <em>Condition E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Condition E</em>' containment reference.
     * @see #getConditionE()
     * @generated
     */
    void setConditionE(ConditionEType value);

    /**
     * Returns the value of the '<em><b>Connect Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Connect Spec</em>' containment reference.
     * @see #setConnectSpec(ConnectSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ConnectSpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='connect-spec' namespace='##targetNamespace'"
     * @generated
     */
    ConnectSpecType getConnectSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getConnectSpec
     * <em>Connect Spec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Connect Spec</em>' containment reference.
     * @see #getConnectSpec()
     * @generated
     */
    void setConnectSpec(ConnectSpecType value);

    /**
     * Returns the value of the '<em><b>Connect Spec Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Connect Spec Spec</em>' containment reference.
     * @see #setConnectSpecSpec(ConnectSpecSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ConnectSpecSpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='connect-spec-spec'
     *        namespace='##targetNamespace'"
     * @generated
     */
    ConnectSpecSpecType getConnectSpecSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getConnectSpecSpec
     * <em>Connect Spec Spec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Connect Spec Spec</em>' containment reference.
     * @see #getConnectSpecSpec()
     * @generated
     */
    void setConnectSpecSpec(ConnectSpecSpecType value);

    /**
     * Returns the value of the '<em><b>Contains Stmt</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Contains Stmt</em>' attribute.
     * @see #setContainsStmt(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ContainsStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='contains-stmt' namespace='##targetNamespace'"
     * @generated
     */
    String getContainsStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getContainsStmt
     * <em>Contains Stmt</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Contains Stmt</em>' attribute.
     * @see #getContainsStmt()
     * @generated
     */
    void setContainsStmt(String value);

    /**
     * Returns the value of the '<em><b>Cpp</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Cpp</em>' attribute.
     * @see #setCpp(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_Cpp()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='cpp' namespace='##targetNamespace'"
     * @generated
     */
    String getCpp();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCpp
     * <em>Cpp</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Cpp</em>' attribute.
     * @see #getCpp()
     * @generated
     */
    void setCpp(String value);

    /**
     * Returns the value of the '<em><b>Ct</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Ct</em>' attribute.
     * @see #setCt(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_Ct()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='ct' namespace='##targetNamespace'"
     * @generated
     */
    String getCt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCt <em>Ct</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Ct</em>' attribute.
     * @see #getCt()
     * @generated
     */
    void setCt(String value);

    /**
     * Returns the value of the '<em><b>Cycle Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Cycle Stmt</em>' containment reference.
     * @see #setCycleStmt(CycleStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_CycleStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='cycle-stmt' namespace='##targetNamespace'"
     * @generated
     */
    CycleStmtType getCycleStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCycleStmt
     * <em>Cycle Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Cycle Stmt</em>' containment reference.
     * @see #getCycleStmt()
     * @generated
     */
    void setCycleStmt(CycleStmtType value);

    /**
     * Returns the value of the '<em><b>Deallocate Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Deallocate Stmt</em>' containment reference.
     * @see #setDeallocateStmt(DeallocateStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_DeallocateStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='deallocate-stmt' namespace='##targetNamespace'"
     * @generated
     */
    DeallocateStmtType getDeallocateStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getDeallocateStmt
     * <em>Deallocate Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Deallocate Stmt</em>' containment reference.
     * @see #getDeallocateStmt()
     * @generated
     */
    void setDeallocateStmt(DeallocateStmtType value);

    /**
     * Returns the value of the '<em><b>Derived TSpec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Derived TSpec</em>' containment reference.
     * @see #setDerivedTSpec(DerivedTSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_DerivedTSpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='derived-T-spec' namespace='##targetNamespace'"
     * @generated
     */
    DerivedTSpecType getDerivedTSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getDerivedTSpec
     * <em>Derived TSpec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Derived TSpec</em>' containment reference.
     * @see #getDerivedTSpec()
     * @generated
     */
    void setDerivedTSpec(DerivedTSpecType value);

    /**
     * Returns the value of the '<em><b>Do Stmt</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Do Stmt</em>' containment reference.
     * @see #setDoStmt(DoStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_DoStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='do-stmt' namespace='##targetNamespace'"
     * @generated
     */
    DoStmtType getDoStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getDoStmt <em>Do
     * Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Do Stmt</em>' containment reference.
     * @see #getDoStmt()
     * @generated
     */
    void setDoStmt(DoStmtType value);

    /**
     * Returns the value of the '<em><b>Do V</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Do V</em>' containment reference.
     * @see #setDoV(DoVType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_DoV()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='do-V' namespace='##targetNamespace'"
     * @generated
     */
    DoVType getDoV();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getDoV <em>Do
     * V</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Do V</em>' containment reference.
     * @see #getDoV()
     * @generated
     */
    void setDoV(DoVType value);

    /**
     * Returns the value of the '<em><b>Dummy Arg LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Dummy Arg LT</em>' containment reference.
     * @see #setDummyArgLT(DummyArgLTType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_DummyArgLT()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='dummy-arg-LT' namespace='##targetNamespace'"
     * @generated
     */
    DummyArgLTType getDummyArgLT();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getDummyArgLT
     * <em>Dummy Arg LT</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Dummy Arg LT</em>' containment reference.
     * @see #getDummyArgLT()
     * @generated
     */
    void setDummyArgLT(DummyArgLTType value);

    /**
     * Returns the value of the '<em><b>E1</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>E1</em>' containment reference.
     * @see #setE1(E1Type)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_E1()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='E-1' namespace='##targetNamespace'"
     * @generated
     */
    E1Type getE1();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getE1 <em>E1</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>E1</em>' containment reference.
     * @see #getE1()
     * @generated
     */
    void setE1(E1Type value);

    /**
     * Returns the value of the '<em><b>E2</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>E2</em>' containment reference.
     * @see #setE2(E2Type)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_E2()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='E-2' namespace='##targetNamespace'"
     * @generated
     */
    E2Type getE2();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getE2 <em>E2</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>E2</em>' containment reference.
     * @see #getE2()
     * @generated
     */
    void setE2(E2Type value);

    /**
     * Returns the value of the '<em><b>Element</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Element</em>' containment reference.
     * @see #setElement(ElementType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_Element()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='element' namespace='##targetNamespace'"
     * @generated
     */
    ElementType getElement();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getElement
     * <em>Element</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Element</em>' containment reference.
     * @see #getElement()
     * @generated
     */
    void setElement(ElementType value);

    /**
     * Returns the value of the '<em><b>Element LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Element LT</em>' containment reference.
     * @see #setElementLT(ElementLTType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ElementLT()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='element-LT' namespace='##targetNamespace'"
     * @generated
     */
    ElementLTType getElementLT();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getElementLT
     * <em>Element LT</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Element LT</em>' containment reference.
     * @see #getElementLT()
     * @generated
     */
    void setElementLT(ElementLTType value);

    /**
     * Returns the value of the '<em><b>Else If Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Else If Stmt</em>' containment reference.
     * @see #setElseIfStmt(ElseIfStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ElseIfStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='else-if-stmt' namespace='##targetNamespace'"
     * @generated
     */
    ElseIfStmtType getElseIfStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getElseIfStmt
     * <em>Else If Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Else If Stmt</em>' containment reference.
     * @see #getElseIfStmt()
     * @generated
     */
    void setElseIfStmt(ElseIfStmtType value);

    /**
     * Returns the value of the '<em><b>Else Stmt</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Else Stmt</em>' attribute.
     * @see #setElseStmt(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ElseStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='else-stmt' namespace='##targetNamespace'"
     * @generated
     */
    String getElseStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getElseStmt <em>Else
     * Stmt</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Else Stmt</em>' attribute.
     * @see #getElseStmt()
     * @generated
     */
    void setElseStmt(String value);

    /**
     * Returns the value of the '<em><b>Else Where Stmt</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Else Where Stmt</em>' attribute.
     * @see #setElseWhereStmt(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ElseWhereStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='else-where-stmt' namespace='##targetNamespace'"
     * @generated
     */
    String getElseWhereStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getElseWhereStmt
     * <em>Else Where Stmt</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Else Where Stmt</em>' attribute.
     * @see #getElseWhereStmt()
     * @generated
     */
    void setElseWhereStmt(String value);

    /**
     * Returns the value of the '<em><b>EN</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>EN</em>' containment reference.
     * @see #setEN(ENType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_EN()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='EN' namespace='##targetNamespace'"
     * @generated
     */
    ENType getEN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEN <em>EN</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>EN</em>' containment reference.
     * @see #getEN()
     * @generated
     */
    void setEN(ENType value);

    /**
     * Returns the value of the '<em><b>EN Decl</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>EN Decl</em>' containment reference.
     * @see #setENDecl(ENDeclType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ENDecl()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='EN-decl' namespace='##targetNamespace'"
     * @generated
     */
    ENDeclType getENDecl();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getENDecl <em>EN
     * Decl</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>EN Decl</em>' containment reference.
     * @see #getENDecl()
     * @generated
     */
    void setENDecl(ENDeclType value);

    /**
     * Returns the value of the '<em><b>EN Decl LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>EN Decl LT</em>' containment reference.
     * @see #setENDeclLT(ENDeclLTType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ENDeclLT()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='EN-decl-LT' namespace='##targetNamespace'"
     * @generated
     */
    ENDeclLTType getENDeclLT();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getENDeclLT <em>EN
     * Decl LT</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>EN Decl LT</em>' containment reference.
     * @see #getENDeclLT()
     * @generated
     */
    void setENDeclLT(ENDeclLTType value);

    /**
     * Returns the value of the '<em><b>ENLT</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>ENLT</em>' containment reference.
     * @see #setENLT(ENLTType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ENLT()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='EN-LT' namespace='##targetNamespace'"
     * @generated
     */
    ENLTType getENLT();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getENLT
     * <em>ENLT</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>ENLT</em>' containment reference.
     * @see #getENLT()
     * @generated
     */
    void setENLT(ENLTType value);

    /**
     * Returns the value of the '<em><b>ENN</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>ENN</em>' containment reference.
     * @see #setENN(ENNType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ENN()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='EN-N' namespace='##targetNamespace'"
     * @generated
     */
    ENNType getENN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getENN
     * <em>ENN</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>ENN</em>' containment reference.
     * @see #getENN()
     * @generated
     */
    void setENN(ENNType value);

    /**
     * Returns the value of the '<em><b>End Do Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Do Stmt</em>' containment reference.
     * @see #setEndDoStmt(EndDoStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_EndDoStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='end-do-stmt' namespace='##targetNamespace'"
     * @generated
     */
    EndDoStmtType getEndDoStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndDoStmt <em>End
     * Do Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>End Do Stmt</em>' containment reference.
     * @see #getEndDoStmt()
     * @generated
     */
    void setEndDoStmt(EndDoStmtType value);

    /**
     * Returns the value of the '<em><b>End Forall Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Forall Stmt</em>' containment reference.
     * @see #setEndForallStmt(EndForallStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_EndForallStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='end-forall-stmt' namespace='##targetNamespace'"
     * @generated
     */
    EndForallStmtType getEndForallStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndForallStmt
     * <em>End Forall Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>End Forall Stmt</em>' containment reference.
     * @see #getEndForallStmt()
     * @generated
     */
    void setEndForallStmt(EndForallStmtType value);

    /**
     * Returns the value of the '<em><b>End Function Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Function Stmt</em>' containment reference.
     * @see #setEndFunctionStmt(EndFunctionStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_EndFunctionStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='end-function-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    EndFunctionStmtType getEndFunctionStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndFunctionStmt
     * <em>End Function Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>End Function Stmt</em>' containment reference.
     * @see #getEndFunctionStmt()
     * @generated
     */
    void setEndFunctionStmt(EndFunctionStmtType value);

    /**
     * Returns the value of the '<em><b>End If Stmt</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>End If Stmt</em>' attribute.
     * @see #setEndIfStmt(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_EndIfStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='end-if-stmt' namespace='##targetNamespace'"
     * @generated
     */
    String getEndIfStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndIfStmt <em>End
     * If Stmt</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>End If Stmt</em>' attribute.
     * @see #getEndIfStmt()
     * @generated
     */
    void setEndIfStmt(String value);

    /**
     * Returns the value of the '<em><b>End Interface Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Interface Stmt</em>' containment reference.
     * @see #setEndInterfaceStmt(EndInterfaceStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_EndInterfaceStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='end-interface-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    EndInterfaceStmtType getEndInterfaceStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndInterfaceStmt
     * <em>End Interface Stmt</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>End Interface Stmt</em>' containment reference.
     * @see #getEndInterfaceStmt()
     * @generated
     */
    void setEndInterfaceStmt(EndInterfaceStmtType value);

    /**
     * Returns the value of the '<em><b>End Module Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Module Stmt</em>' containment reference.
     * @see #setEndModuleStmt(EndModuleStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_EndModuleStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='end-module-stmt' namespace='##targetNamespace'"
     * @generated
     */
    EndModuleStmtType getEndModuleStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndModuleStmt
     * <em>End Module Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>End Module Stmt</em>' containment reference.
     * @see #getEndModuleStmt()
     * @generated
     */
    void setEndModuleStmt(EndModuleStmtType value);

    /**
     * Returns the value of the '<em><b>End Program Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Program Stmt</em>' containment reference.
     * @see #setEndProgramStmt(EndProgramStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_EndProgramStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='end-program-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    EndProgramStmtType getEndProgramStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndProgramStmt
     * <em>End Program Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>End Program Stmt</em>' containment reference.
     * @see #getEndProgramStmt()
     * @generated
     */
    void setEndProgramStmt(EndProgramStmtType value);

    /**
     * Returns the value of the '<em><b>End Select Case Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Select Case Stmt</em>' containment reference.
     * @see #setEndSelectCaseStmt(EndSelectCaseStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_EndSelectCaseStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='end-select-case-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    EndSelectCaseStmtType getEndSelectCaseStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndSelectCaseStmt
     * <em>End Select Case Stmt</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>End Select Case Stmt</em>' containment reference.
     * @see #getEndSelectCaseStmt()
     * @generated
     */
    void setEndSelectCaseStmt(EndSelectCaseStmtType value);

    /**
     * Returns the value of the '<em><b>End Subroutine Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Subroutine Stmt</em>' containment reference.
     * @see #setEndSubroutineStmt(EndSubroutineStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_EndSubroutineStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='end-subroutine-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    EndSubroutineStmtType getEndSubroutineStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndSubroutineStmt
     * <em>End Subroutine Stmt</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>End Subroutine Stmt</em>' containment reference.
     * @see #getEndSubroutineStmt()
     * @generated
     */
    void setEndSubroutineStmt(EndSubroutineStmtType value);

    /**
     * Returns the value of the '<em><b>End TStmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End TStmt</em>' containment reference.
     * @see #setEndTStmt(EndTStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_EndTStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='end-T-stmt' namespace='##targetNamespace'"
     * @generated
     */
    EndTStmtType getEndTStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndTStmt <em>End
     * TStmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>End TStmt</em>' containment reference.
     * @see #getEndTStmt()
     * @generated
     */
    void setEndTStmt(EndTStmtType value);

    /**
     * Returns the value of the '<em><b>End Where Stmt</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Where Stmt</em>' attribute.
     * @see #setEndWhereStmt(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_EndWhereStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='end-where-stmt' namespace='##targetNamespace'"
     * @generated
     */
    String getEndWhereStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndWhereStmt
     * <em>End Where Stmt</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>End Where Stmt</em>' attribute.
     * @see #getEndWhereStmt()
     * @generated
     */
    void setEndWhereStmt(String value);

    /**
     * Returns the value of the '<em><b>Error</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Error</em>' containment reference.
     * @see #setError(ErrorType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_Error()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='error' namespace='##targetNamespace'"
     * @generated
     */
    ErrorType getError();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getError
     * <em>Error</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Error</em>' containment reference.
     * @see #getError()
     * @generated
     */
    void setError(ErrorType value);

    /**
     * Returns the value of the '<em><b>Exit Stmt</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Exit Stmt</em>' attribute.
     * @see #setExitStmt(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ExitStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='exit-stmt' namespace='##targetNamespace'"
     * @generated
     */
    String getExitStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getExitStmt <em>Exit
     * Stmt</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Exit Stmt</em>' attribute.
     * @see #getExitStmt()
     * @generated
     */
    void setExitStmt(String value);

    /**
     * Returns the value of the '<em><b>File</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>File</em>' containment reference.
     * @see #setFile(FileType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_File()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='file' namespace='##targetNamespace'"
     * @generated
     */
    FileType getFile();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getFile
     * <em>File</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>File</em>' containment reference.
     * @see #getFile()
     * @generated
     */
    void setFile(FileType value);

    /**
     * Returns the value of the '<em><b>Forall Construct Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Forall Construct Stmt</em>' containment reference.
     * @see #setForallConstructStmt(ForallConstructStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ForallConstructStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='forall-construct-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    ForallConstructStmtType getForallConstructStmt();

    /**
     * Sets the value of the
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getForallConstructStmt <em>Forall
     * Construct Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Forall Construct Stmt</em>' containment reference.
     * @see #getForallConstructStmt()
     * @generated
     */
    void setForallConstructStmt(ForallConstructStmtType value);

    /**
     * Returns the value of the '<em><b>Forall Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Forall Stmt</em>' containment reference.
     * @see #setForallStmt(ForallStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ForallStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='forall-stmt' namespace='##targetNamespace'"
     * @generated
     */
    ForallStmtType getForallStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getForallStmt
     * <em>Forall Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Forall Stmt</em>' containment reference.
     * @see #getForallStmt()
     * @generated
     */
    void setForallStmt(ForallStmtType value);

    /**
     * Returns the value of the '<em><b>Forall Triplet Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Forall Triplet Spec</em>' containment reference.
     * @see #setForallTripletSpec(ForallTripletSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ForallTripletSpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='forall-triplet-spec'
     *        namespace='##targetNamespace'"
     * @generated
     */
    ForallTripletSpecType getForallTripletSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getForallTripletSpec
     * <em>Forall Triplet Spec</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Forall Triplet Spec</em>' containment reference.
     * @see #getForallTripletSpec()
     * @generated
     */
    void setForallTripletSpec(ForallTripletSpecType value);

    /**
     * Returns the value of the '<em><b>Forall Triplet Spec LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Forall Triplet Spec LT</em>' containment reference.
     * @see #setForallTripletSpecLT(ForallTripletSpecLTType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ForallTripletSpecLT()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='forall-triplet-spec-LT'
     *        namespace='##targetNamespace'"
     * @generated
     */
    ForallTripletSpecLTType getForallTripletSpecLT();

    /**
     * Sets the value of the
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getForallTripletSpecLT <em>Forall Triplet
     * Spec LT</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Forall Triplet Spec LT</em>' containment reference.
     * @see #getForallTripletSpecLT()
     * @generated
     */
    void setForallTripletSpecLT(ForallTripletSpecLTType value);

    /**
     * Returns the value of the '<em><b>Function N</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Function N</em>' containment reference.
     * @see #setFunctionN(FunctionNType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_FunctionN()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='function-N' namespace='##targetNamespace'"
     * @generated
     */
    FunctionNType getFunctionN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getFunctionN
     * <em>Function N</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Function N</em>' containment reference.
     * @see #getFunctionN()
     * @generated
     */
    void setFunctionN(FunctionNType value);

    /**
     * Returns the value of the '<em><b>Function Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Function Stmt</em>' containment reference.
     * @see #setFunctionStmt(FunctionStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_FunctionStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='function-stmt' namespace='##targetNamespace'"
     * @generated
     */
    FunctionStmtType getFunctionStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getFunctionStmt
     * <em>Function Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Function Stmt</em>' containment reference.
     * @see #getFunctionStmt()
     * @generated
     */
    void setFunctionStmt(FunctionStmtType value);

    /**
     * Returns the value of the '<em><b>If Stmt</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>If Stmt</em>' containment reference.
     * @see #setIfStmt(IfStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_IfStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='if-stmt' namespace='##targetNamespace'"
     * @generated
     */
    IfStmtType getIfStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIfStmt <em>If
     * Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>If Stmt</em>' containment reference.
     * @see #getIfStmt()
     * @generated
     */
    void setIfStmt(IfStmtType value);

    /**
     * Returns the value of the '<em><b>If Then Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>If Then Stmt</em>' containment reference.
     * @see #setIfThenStmt(IfThenStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_IfThenStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='if-then-stmt' namespace='##targetNamespace'"
     * @generated
     */
    IfThenStmtType getIfThenStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIfThenStmt <em>If
     * Then Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>If Then Stmt</em>' containment reference.
     * @see #getIfThenStmt()
     * @generated
     */
    void setIfThenStmt(IfThenStmtType value);

    /**
     * Returns the value of the '<em><b>Implicit None Stmt</b></em>' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Implicit None Stmt</em>' attribute.
     * @see #setImplicitNoneStmt(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ImplicitNoneStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='implicit-none-stmt' namespace='##targetNamespace'"
     * @generated
     */
    String getImplicitNoneStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getImplicitNoneStmt
     * <em>Implicit None Stmt</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Implicit None Stmt</em>' attribute.
     * @see #getImplicitNoneStmt()
     * @generated
     */
    void setImplicitNoneStmt(String value);

    /**
     * Returns the value of the '<em><b>Init E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Init E</em>' containment reference.
     * @see #setInitE(InitEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_InitE()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='init-E' namespace='##targetNamespace'"
     * @generated
     */
    InitEType getInitE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getInitE <em>Init
     * E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Init E</em>' containment reference.
     * @see #getInitE()
     * @generated
     */
    void setInitE(InitEType value);

    /**
     * Returns the value of the '<em><b>Inquire Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Inquire Stmt</em>' containment reference.
     * @see #setInquireStmt(InquireStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_InquireStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='inquire-stmt' namespace='##targetNamespace'"
     * @generated
     */
    InquireStmtType getInquireStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getInquireStmt
     * <em>Inquire Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Inquire Stmt</em>' containment reference.
     * @see #getInquireStmt()
     * @generated
     */
    void setInquireStmt(InquireStmtType value);

    /**
     * Returns the value of the '<em><b>Inquiry Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Inquiry Spec</em>' containment reference.
     * @see #setInquirySpec(InquirySpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_InquirySpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='inquiry-spec' namespace='##targetNamespace'"
     * @generated
     */
    InquirySpecType getInquirySpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getInquirySpec
     * <em>Inquiry Spec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Inquiry Spec</em>' containment reference.
     * @see #getInquirySpec()
     * @generated
     */
    void setInquirySpec(InquirySpecType value);

    /**
     * Returns the value of the '<em><b>Inquiry Spec Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Inquiry Spec Spec</em>' containment reference.
     * @see #setInquirySpecSpec(InquirySpecSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_InquirySpecSpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='inquiry-spec-spec'
     *        namespace='##targetNamespace'"
     * @generated
     */
    InquirySpecSpecType getInquirySpecSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getInquirySpecSpec
     * <em>Inquiry Spec Spec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Inquiry Spec Spec</em>' containment reference.
     * @see #getInquirySpecSpec()
     * @generated
     */
    void setInquirySpecSpec(InquirySpecSpecType value);

    /**
     * Returns the value of the '<em><b>Intent Spec</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Intent Spec</em>' attribute.
     * @see #setIntentSpec(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_IntentSpec()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='intent-spec' namespace='##targetNamespace'"
     * @generated
     */
    String getIntentSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIntentSpec
     * <em>Intent Spec</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Intent Spec</em>' attribute.
     * @see #getIntentSpec()
     * @generated
     */
    void setIntentSpec(String value);

    /**
     * Returns the value of the '<em><b>Interface Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Interface Stmt</em>' containment reference.
     * @see #setInterfaceStmt(InterfaceStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_InterfaceStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='interface-stmt' namespace='##targetNamespace'"
     * @generated
     */
    InterfaceStmtType getInterfaceStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getInterfaceStmt
     * <em>Interface Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Interface Stmt</em>' containment reference.
     * @see #getInterfaceStmt()
     * @generated
     */
    void setInterfaceStmt(InterfaceStmtType value);

    /**
     * Returns the value of the '<em><b>Intrinsic TSpec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Intrinsic TSpec</em>' containment reference.
     * @see #setIntrinsicTSpec(IntrinsicTSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_IntrinsicTSpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='intrinsic-T-spec'
     *        namespace='##targetNamespace'"
     * @generated
     */
    IntrinsicTSpecType getIntrinsicTSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIntrinsicTSpec
     * <em>Intrinsic TSpec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Intrinsic TSpec</em>' containment reference.
     * @see #getIntrinsicTSpec()
     * @generated
     */
    void setIntrinsicTSpec(IntrinsicTSpecType value);

    /**
     * Returns the value of the '<em><b>Io Control</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Io Control</em>' containment reference.
     * @see #setIoControl(IoControlType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_IoControl()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='io-control' namespace='##targetNamespace'"
     * @generated
     */
    IoControlType getIoControl();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIoControl <em>Io
     * Control</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Io Control</em>' containment reference.
     * @see #getIoControl()
     * @generated
     */
    void setIoControl(IoControlType value);

    /**
     * Returns the value of the '<em><b>Io Control Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Io Control Spec</em>' containment reference.
     * @see #setIoControlSpec(IoControlSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_IoControlSpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='io-control-spec' namespace='##targetNamespace'"
     * @generated
     */
    IoControlSpecType getIoControlSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIoControlSpec
     * <em>Io Control Spec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Io Control Spec</em>' containment reference.
     * @see #getIoControlSpec()
     * @generated
     */
    void setIoControlSpec(IoControlSpecType value);

    /**
     * Returns the value of the '<em><b>Iterator</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Iterator</em>' containment reference.
     * @see #setIterator(IteratorType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_Iterator()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='iterator' namespace='##targetNamespace'"
     * @generated
     */
    IteratorType getIterator();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIterator
     * <em>Iterator</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Iterator</em>' containment reference.
     * @see #getIterator()
     * @generated
     */
    void setIterator(IteratorType value);

    /**
     * Returns the value of the '<em><b>Iterator Definition LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Iterator Definition LT</em>' containment reference.
     * @see #setIteratorDefinitionLT(IteratorDefinitionLTType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_IteratorDefinitionLT()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='iterator-definition-LT'
     *        namespace='##targetNamespace'"
     * @generated
     */
    IteratorDefinitionLTType getIteratorDefinitionLT();

    /**
     * Sets the value of the
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIteratorDefinitionLT <em>Iterator
     * Definition LT</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Iterator Definition LT</em>' containment reference.
     * @see #getIteratorDefinitionLT()
     * @generated
     */
    void setIteratorDefinitionLT(IteratorDefinitionLTType value);

    /**
     * Returns the value of the '<em><b>Iterator Element</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Iterator Element</em>' containment reference.
     * @see #setIteratorElement(IteratorElementType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_IteratorElement()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='iterator-element'
     *        namespace='##targetNamespace'"
     * @generated
     */
    IteratorElementType getIteratorElement();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIteratorElement
     * <em>Iterator Element</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Iterator Element</em>' containment reference.
     * @see #getIteratorElement()
     * @generated
     */
    void setIteratorElement(IteratorElementType value);

    /**
     * Returns the value of the '<em><b>K</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>K</em>' attribute.
     * @see #setK(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_K()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='k' namespace='##targetNamespace'"
     * @generated
     */
    String getK();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getK <em>K</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>K</em>' attribute.
     * @see #getK()
     * @generated
     */
    void setK(String value);

    /**
     * Returns the value of the '<em><b>KSelector</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>KSelector</em>' containment reference.
     * @see #setKSelector(KSelectorType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_KSelector()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='K-selector' namespace='##targetNamespace'"
     * @generated
     */
    KSelectorType getKSelector();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getKSelector
     * <em>KSelector</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>KSelector</em>' containment reference.
     * @see #getKSelector()
     * @generated
     */
    void setKSelector(KSelectorType value);

    /**
     * Returns the value of the '<em><b>KSpec</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>KSpec</em>' containment reference.
     * @see #setKSpec(KSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_KSpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='K-spec' namespace='##targetNamespace'"
     * @generated
     */
    KSpecType getKSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getKSpec
     * <em>KSpec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>KSpec</em>' containment reference.
     * @see #getKSpec()
     * @generated
     */
    void setKSpec(KSpecType value);

    /**
     * Returns the value of the '<em><b>L</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>L</em>' attribute.
     * @see #setL(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_L()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NMTOKEN" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='l' namespace='##targetNamespace'"
     * @generated
     */
    String getL();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getL <em>L</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>L</em>' attribute.
     * @see #getL()
     * @generated
     */
    void setL(String value);

    /**
     * Returns the value of the '<em><b>Label</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Label</em>' containment reference.
     * @see #setLabel(LabelType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_Label()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='label' namespace='##targetNamespace'"
     * @generated
     */
    LabelType getLabel();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getLabel
     * <em>Label</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Label</em>' containment reference.
     * @see #getLabel()
     * @generated
     */
    void setLabel(LabelType value);

    /**
     * Returns the value of the '<em><b>Literal E</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Literal E</em>' containment reference.
     * @see #setLiteralE(LiteralEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_LiteralE()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='literal-E' namespace='##targetNamespace'"
     * @generated
     */
    LiteralEType getLiteralE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getLiteralE
     * <em>Literal E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Literal E</em>' containment reference.
     * @see #getLiteralE()
     * @generated
     */
    void setLiteralE(LiteralEType value);

    /**
     * Returns the value of the '<em><b>Lower Bound</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Lower Bound</em>' containment reference.
     * @see #setLowerBound(LowerBoundType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_LowerBound()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='lower-bound' namespace='##targetNamespace'"
     * @generated
     */
    LowerBoundType getLowerBound();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getLowerBound
     * <em>Lower Bound</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Lower Bound</em>' containment reference.
     * @see #getLowerBound()
     * @generated
     */
    void setLowerBound(LowerBoundType value);

    /**
     * Returns the value of the '<em><b>Mask E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Mask E</em>' containment reference.
     * @see #setMaskE(MaskEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_MaskE()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='mask-E' namespace='##targetNamespace'"
     * @generated
     */
    MaskEType getMaskE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getMaskE <em>Mask
     * E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Mask E</em>' containment reference.
     * @see #getMaskE()
     * @generated
     */
    void setMaskE(MaskEType value);

    /**
     * Returns the value of the '<em><b>Module N</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Module N</em>' containment reference.
     * @see #setModuleN(ModuleNType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ModuleN()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='module-N' namespace='##targetNamespace'"
     * @generated
     */
    ModuleNType getModuleN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getModuleN
     * <em>Module N</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Module N</em>' containment reference.
     * @see #getModuleN()
     * @generated
     */
    void setModuleN(ModuleNType value);

    /**
     * Returns the value of the '<em><b>Module Procedure NLT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Module Procedure NLT</em>' containment reference.
     * @see #setModuleProcedureNLT(ModuleProcedureNLTType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ModuleProcedureNLT()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='module-procedure-N-LT'
     *        namespace='##targetNamespace'"
     * @generated
     */
    ModuleProcedureNLTType getModuleProcedureNLT();

    /**
     * Sets the value of the
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getModuleProcedureNLT <em>Module Procedure
     * NLT</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Module Procedure NLT</em>' containment reference.
     * @see #getModuleProcedureNLT()
     * @generated
     */
    void setModuleProcedureNLT(ModuleProcedureNLTType value);

    /**
     * Returns the value of the '<em><b>Module Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Module Stmt</em>' containment reference.
     * @see #setModuleStmt(ModuleStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ModuleStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='module-stmt' namespace='##targetNamespace'"
     * @generated
     */
    ModuleStmtType getModuleStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getModuleStmt
     * <em>Module Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Module Stmt</em>' containment reference.
     * @see #getModuleStmt()
     * @generated
     */
    void setModuleStmt(ModuleStmtType value);

    /**
     * Returns the value of the '<em><b>N</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>N</em>' attribute.
     * @see #setN(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_N()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='n' namespace='##targetNamespace'"
     * @generated
     */
    String getN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getN <em>N</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>N</em>' attribute.
     * @see #getN()
     * @generated
     */
    void setN(String value);

    /**
     * Returns the value of the '<em><b>N1</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>N1</em>' containment reference.
     * @see #setN1(NType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_N1()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='N' namespace='##targetNamespace'"
     * @generated
     */
    NType getN1();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getN1 <em>N1</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>N1</em>' containment reference.
     * @see #getN1()
     * @generated
     */
    void setN1(NType value);

    /**
     * Returns the value of the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Named E</em>' containment reference.
     * @see #setNamedE(NamedEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_NamedE()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='named-E' namespace='##targetNamespace'"
     * @generated
     */
    NamedEType getNamedE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamedE <em>Named
     * E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Named E</em>' containment reference.
     * @see #getNamedE()
     * @generated
     */
    void setNamedE(NamedEType value);

    /**
     * Returns the value of the '<em><b>Namelist Group N</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Namelist Group N</em>' containment reference.
     * @see #setNamelistGroupN(NamelistGroupNType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_NamelistGroupN()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='namelist-group-N'
     *        namespace='##targetNamespace'"
     * @generated
     */
    NamelistGroupNType getNamelistGroupN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistGroupN
     * <em>Namelist Group N</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Namelist Group N</em>' containment reference.
     * @see #getNamelistGroupN()
     * @generated
     */
    void setNamelistGroupN(NamelistGroupNType value);

    /**
     * Returns the value of the '<em><b>Namelist Group Obj</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Namelist Group Obj</em>' containment reference.
     * @see #setNamelistGroupObj(NamelistGroupObjType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_NamelistGroupObj()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='namelist-group-obj'
     *        namespace='##targetNamespace'"
     * @generated
     */
    NamelistGroupObjType getNamelistGroupObj();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistGroupObj
     * <em>Namelist Group Obj</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Namelist Group Obj</em>' containment reference.
     * @see #getNamelistGroupObj()
     * @generated
     */
    void setNamelistGroupObj(NamelistGroupObjType value);

    /**
     * Returns the value of the '<em><b>Namelist Group Obj LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Namelist Group Obj LT</em>' containment reference.
     * @see #setNamelistGroupObjLT(NamelistGroupObjLTType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_NamelistGroupObjLT()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='namelist-group-obj-LT'
     *        namespace='##targetNamespace'"
     * @generated
     */
    NamelistGroupObjLTType getNamelistGroupObjLT();

    /**
     * Sets the value of the
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistGroupObjLT <em>Namelist Group
     * Obj LT</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Namelist Group Obj LT</em>' containment reference.
     * @see #getNamelistGroupObjLT()
     * @generated
     */
    void setNamelistGroupObjLT(NamelistGroupObjLTType value);

    /**
     * Returns the value of the '<em><b>Namelist Group Obj N</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Namelist Group Obj N</em>' containment reference.
     * @see #setNamelistGroupObjN(NamelistGroupObjNType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_NamelistGroupObjN()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='namelist-group-obj-N'
     *        namespace='##targetNamespace'"
     * @generated
     */
    NamelistGroupObjNType getNamelistGroupObjN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistGroupObjN
     * <em>Namelist Group Obj N</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Namelist Group Obj N</em>' containment reference.
     * @see #getNamelistGroupObjN()
     * @generated
     */
    void setNamelistGroupObjN(NamelistGroupObjNType value);

    /**
     * Returns the value of the '<em><b>Namelist Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Namelist Stmt</em>' containment reference.
     * @see #setNamelistStmt(NamelistStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_NamelistStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='namelist-stmt' namespace='##targetNamespace'"
     * @generated
     */
    NamelistStmtType getNamelistStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistStmt
     * <em>Namelist Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Namelist Stmt</em>' containment reference.
     * @see #getNamelistStmt()
     * @generated
     */
    void setNamelistStmt(NamelistStmtType value);

    /**
     * Returns the value of the '<em><b>Nullify Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nullify Stmt</em>' containment reference.
     * @see #setNullifyStmt(NullifyStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_NullifyStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='nullify-stmt' namespace='##targetNamespace'"
     * @generated
     */
    NullifyStmtType getNullifyStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNullifyStmt
     * <em>Nullify Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Nullify Stmt</em>' containment reference.
     * @see #getNullifyStmt()
     * @generated
     */
    void setNullifyStmt(NullifyStmtType value);

    /**
     * Returns the value of the '<em><b>O</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>O</em>' attribute.
     * @see #setO(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_O()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='o' namespace='##targetNamespace'"
     * @generated
     */
    String getO();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getO <em>O</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>O</em>' attribute.
     * @see #getO()
     * @generated
     */
    void setO(String value);

    /**
     * Returns the value of the '<em><b>Object</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Object</em>' containment reference.
     * @see #setObject(ObjectType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_Object()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='object' namespace='##targetNamespace'"
     * @generated
     */
    ObjectType getObject();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getObject
     * <em>Object</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Object</em>' containment reference.
     * @see #getObject()
     * @generated
     */
    void setObject(ObjectType value);

    /**
     * Returns the value of the '<em><b>Op</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Op</em>' containment reference.
     * @see #setOp(OpType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_Op()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='op' namespace='##targetNamespace'"
     * @generated
     */
    OpType getOp();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getOp <em>Op</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Op</em>' containment reference.
     * @see #getOp()
     * @generated
     */
    void setOp(OpType value);

    /**
     * Returns the value of the '<em><b>Op E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Op E</em>' containment reference.
     * @see #setOpE(OpEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_OpE()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='op-E' namespace='##targetNamespace'"
     * @generated
     */
    OpEType getOpE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getOpE <em>Op
     * E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Op E</em>' containment reference.
     * @see #getOpE()
     * @generated
     */
    void setOpE(OpEType value);

    /**
     * Returns the value of the '<em><b>Open Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Open Stmt</em>' containment reference.
     * @see #setOpenStmt(OpenStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_OpenStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='open-stmt' namespace='##targetNamespace'"
     * @generated
     */
    OpenStmtType getOpenStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getOpenStmt <em>Open
     * Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Open Stmt</em>' containment reference.
     * @see #getOpenStmt()
     * @generated
     */
    void setOpenStmt(OpenStmtType value);

    /**
     * Returns the value of the '<em><b>Output Item</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Output Item</em>' containment reference.
     * @see #setOutputItem(OutputItemType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_OutputItem()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='output-item' namespace='##targetNamespace'"
     * @generated
     */
    OutputItemType getOutputItem();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getOutputItem
     * <em>Output Item</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Output Item</em>' containment reference.
     * @see #getOutputItem()
     * @generated
     */
    void setOutputItem(OutputItemType value);

    /**
     * Returns the value of the '<em><b>Output Item LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Output Item LT</em>' containment reference.
     * @see #setOutputItemLT(OutputItemLTType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_OutputItemLT()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='output-item-LT' namespace='##targetNamespace'"
     * @generated
     */
    OutputItemLTType getOutputItemLT();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getOutputItemLT
     * <em>Output Item LT</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Output Item LT</em>' containment reference.
     * @see #getOutputItemLT()
     * @generated
     */
    void setOutputItemLT(OutputItemLTType value);

    /**
     * Returns the value of the '<em><b>Parens E</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Parens E</em>' containment reference.
     * @see #setParensE(ParensEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ParensE()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='parens-E' namespace='##targetNamespace'"
     * @generated
     */
    ParensEType getParensE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getParensE
     * <em>Parens E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Parens E</em>' containment reference.
     * @see #getParensE()
     * @generated
     */
    void setParensE(ParensEType value);

    /**
     * Returns the value of the '<em><b>Parens R</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Parens R</em>' containment reference.
     * @see #setParensR(ParensRType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ParensR()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='parens-R' namespace='##targetNamespace'"
     * @generated
     */
    ParensRType getParensR();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getParensR
     * <em>Parens R</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Parens R</em>' containment reference.
     * @see #getParensR()
     * @generated
     */
    void setParensR(ParensRType value);

    /**
     * Returns the value of the '<em><b>Pointer AStmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Pointer AStmt</em>' containment reference.
     * @see #setPointerAStmt(PointerAStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_PointerAStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pointer-a-stmt' namespace='##targetNamespace'"
     * @generated
     */
    PointerAStmtType getPointerAStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getPointerAStmt
     * <em>Pointer AStmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Pointer AStmt</em>' containment reference.
     * @see #getPointerAStmt()
     * @generated
     */
    void setPointerAStmt(PointerAStmtType value);

    /**
     * Returns the value of the '<em><b>Pointer Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Pointer Stmt</em>' containment reference.
     * @see #setPointerStmt(PointerStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_PointerStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pointer-stmt' namespace='##targetNamespace'"
     * @generated
     */
    PointerStmtType getPointerStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getPointerStmt
     * <em>Pointer Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Pointer Stmt</em>' containment reference.
     * @see #getPointerStmt()
     * @generated
     */
    void setPointerStmt(PointerStmtType value);

    /**
     * Returns the value of the '<em><b>Prefix</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Prefix</em>' attribute.
     * @see #setPrefix(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_Prefix()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='prefix' namespace='##targetNamespace'"
     * @generated
     */
    String getPrefix();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getPrefix
     * <em>Prefix</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Prefix</em>' attribute.
     * @see #getPrefix()
     * @generated
     */
    void setPrefix(String value);

    /**
     * Returns the value of the '<em><b>Private Stmt</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Private Stmt</em>' attribute.
     * @see #setPrivateStmt(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_PrivateStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='private-stmt' namespace='##targetNamespace'"
     * @generated
     */
    String getPrivateStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getPrivateStmt
     * <em>Private Stmt</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Private Stmt</em>' attribute.
     * @see #getPrivateStmt()
     * @generated
     */
    void setPrivateStmt(String value);

    /**
     * Returns the value of the '<em><b>Procedure Designator</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Procedure Designator</em>' containment reference.
     * @see #setProcedureDesignator(ProcedureDesignatorType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ProcedureDesignator()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='procedure-designator'
     *        namespace='##targetNamespace'"
     * @generated
     */
    ProcedureDesignatorType getProcedureDesignator();

    /**
     * Sets the value of the
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getProcedureDesignator <em>Procedure
     * Designator</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Procedure Designator</em>' containment reference.
     * @see #getProcedureDesignator()
     * @generated
     */
    void setProcedureDesignator(ProcedureDesignatorType value);

    /**
     * Returns the value of the '<em><b>Procedure Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Procedure Stmt</em>' containment reference.
     * @see #setProcedureStmt(ProcedureStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ProcedureStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='procedure-stmt' namespace='##targetNamespace'"
     * @generated
     */
    ProcedureStmtType getProcedureStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getProcedureStmt
     * <em>Procedure Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Procedure Stmt</em>' containment reference.
     * @see #getProcedureStmt()
     * @generated
     */
    void setProcedureStmt(ProcedureStmtType value);

    /**
     * Returns the value of the '<em><b>Program N</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Program N</em>' containment reference.
     * @see #setProgramN(ProgramNType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ProgramN()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='program-N' namespace='##targetNamespace'"
     * @generated
     */
    ProgramNType getProgramN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getProgramN
     * <em>Program N</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Program N</em>' containment reference.
     * @see #getProgramN()
     * @generated
     */
    void setProgramN(ProgramNType value);

    /**
     * Returns the value of the '<em><b>Program Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Program Stmt</em>' containment reference.
     * @see #setProgramStmt(ProgramStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ProgramStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='program-stmt' namespace='##targetNamespace'"
     * @generated
     */
    ProgramStmtType getProgramStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getProgramStmt
     * <em>Program Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Program Stmt</em>' containment reference.
     * @see #getProgramStmt()
     * @generated
     */
    void setProgramStmt(ProgramStmtType value);

    /**
     * Returns the value of the '<em><b>Public Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Public Stmt</em>' containment reference.
     * @see #setPublicStmt(PublicStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_PublicStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='public-stmt' namespace='##targetNamespace'"
     * @generated
     */
    PublicStmtType getPublicStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getPublicStmt
     * <em>Public Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Public Stmt</em>' containment reference.
     * @see #getPublicStmt()
     * @generated
     */
    void setPublicStmt(PublicStmtType value);

    /**
     * Returns the value of the '<em><b>RLT</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>RLT</em>' containment reference.
     * @see #setRLT(RLTType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_RLT()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='R-LT' namespace='##targetNamespace'"
     * @generated
     */
    RLTType getRLT();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getRLT
     * <em>RLT</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>RLT</em>' containment reference.
     * @see #getRLT()
     * @generated
     */
    void setRLT(RLTType value);

    /**
     * Returns the value of the '<em><b>Read Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Read Stmt</em>' containment reference.
     * @see #setReadStmt(ReadStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ReadStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='read-stmt' namespace='##targetNamespace'"
     * @generated
     */
    ReadStmtType getReadStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getReadStmt <em>Read
     * Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Read Stmt</em>' containment reference.
     * @see #getReadStmt()
     * @generated
     */
    void setReadStmt(ReadStmtType value);

    /**
     * Returns the value of the '<em><b>Rename</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Rename</em>' containment reference.
     * @see #setRename(RenameType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_Rename()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='rename' namespace='##targetNamespace'"
     * @generated
     */
    RenameType getRename();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getRename
     * <em>Rename</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Rename</em>' containment reference.
     * @see #getRename()
     * @generated
     */
    void setRename(RenameType value);

    /**
     * Returns the value of the '<em><b>Rename LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Rename LT</em>' containment reference.
     * @see #setRenameLT(RenameLTType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_RenameLT()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='rename-LT' namespace='##targetNamespace'"
     * @generated
     */
    RenameLTType getRenameLT();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getRenameLT
     * <em>Rename LT</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Rename LT</em>' containment reference.
     * @see #getRenameLT()
     * @generated
     */
    void setRenameLT(RenameLTType value);

    /**
     * Returns the value of the '<em><b>Result Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Result Spec</em>' containment reference.
     * @see #setResultSpec(ResultSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ResultSpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='result-spec' namespace='##targetNamespace'"
     * @generated
     */
    ResultSpecType getResultSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getResultSpec
     * <em>Result Spec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Result Spec</em>' containment reference.
     * @see #getResultSpec()
     * @generated
     */
    void setResultSpec(ResultSpecType value);

    /**
     * Returns the value of the '<em><b>Return Stmt</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Return Stmt</em>' attribute.
     * @see #setReturnStmt(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ReturnStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='return-stmt' namespace='##targetNamespace'"
     * @generated
     */
    String getReturnStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getReturnStmt
     * <em>Return Stmt</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Return Stmt</em>' attribute.
     * @see #getReturnStmt()
     * @generated
     */
    void setReturnStmt(String value);

    /**
     * Returns the value of the '<em><b>S</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>S</em>' attribute.
     * @see #setS(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_S()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='S' namespace='##targetNamespace'"
     * @generated
     */
    String getS();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getS <em>S</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>S</em>' attribute.
     * @see #getS()
     * @generated
     */
    void setS(String value);

    /**
     * Returns the value of the '<em><b>Save Stmt</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Save Stmt</em>' attribute.
     * @see #setSaveStmt(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_SaveStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='save-stmt' namespace='##targetNamespace'"
     * @generated
     */
    String getSaveStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSaveStmt <em>Save
     * Stmt</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Save Stmt</em>' attribute.
     * @see #getSaveStmt()
     * @generated
     */
    void setSaveStmt(String value);

    /**
     * Returns the value of the '<em><b>Section Subscript</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Section Subscript</em>' containment reference.
     * @see #setSectionSubscript(SectionSubscriptType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_SectionSubscript()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='section-subscript'
     *        namespace='##targetNamespace'"
     * @generated
     */
    SectionSubscriptType getSectionSubscript();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSectionSubscript
     * <em>Section Subscript</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Section Subscript</em>' containment reference.
     * @see #getSectionSubscript()
     * @generated
     */
    void setSectionSubscript(SectionSubscriptType value);

    /**
     * Returns the value of the '<em><b>Section Subscript LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Section Subscript LT</em>' containment reference.
     * @see #setSectionSubscriptLT(SectionSubscriptLTType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_SectionSubscriptLT()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='section-subscript-LT'
     *        namespace='##targetNamespace'"
     * @generated
     */
    SectionSubscriptLTType getSectionSubscriptLT();

    /**
     * Sets the value of the
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSectionSubscriptLT <em>Section
     * Subscript LT</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Section Subscript LT</em>' containment reference.
     * @see #getSectionSubscriptLT()
     * @generated
     */
    void setSectionSubscriptLT(SectionSubscriptLTType value);

    /**
     * Returns the value of the '<em><b>Select Case Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Select Case Stmt</em>' containment reference.
     * @see #setSelectCaseStmt(SelectCaseStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_SelectCaseStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='select-case-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    SelectCaseStmtType getSelectCaseStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSelectCaseStmt
     * <em>Select Case Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Select Case Stmt</em>' containment reference.
     * @see #getSelectCaseStmt()
     * @generated
     */
    void setSelectCaseStmt(SelectCaseStmtType value);

    /**
     * Returns the value of the '<em><b>Shape Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Shape Spec</em>' containment reference.
     * @see #setShapeSpec(ShapeSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ShapeSpec()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='shape-spec' namespace='##targetNamespace'"
     * @generated
     */
    ShapeSpecType getShapeSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getShapeSpec
     * <em>Shape Spec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Shape Spec</em>' containment reference.
     * @see #getShapeSpec()
     * @generated
     */
    void setShapeSpec(ShapeSpecType value);

    /**
     * Returns the value of the '<em><b>Shape Spec LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Shape Spec LT</em>' containment reference.
     * @see #setShapeSpecLT(ShapeSpecLTType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_ShapeSpecLT()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='shape-spec-LT' namespace='##targetNamespace'"
     * @generated
     */
    ShapeSpecLTType getShapeSpecLT();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getShapeSpecLT
     * <em>Shape Spec LT</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Shape Spec LT</em>' containment reference.
     * @see #getShapeSpecLT()
     * @generated
     */
    void setShapeSpecLT(ShapeSpecLTType value);

    /**
     * Returns the value of the '<em><b>Star E</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Star E</em>' attribute.
     * @see #setStarE(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_StarE()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='star-E' namespace='##targetNamespace'"
     * @generated
     */
    String getStarE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getStarE <em>Star
     * E</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Star E</em>' attribute.
     * @see #getStarE()
     * @generated
     */
    void setStarE(String value);

    /**
     * Returns the value of the '<em><b>Stop Code</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Stop Code</em>' attribute.
     * @see #setStopCode(BigInteger)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_StopCode()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.Integer" upper="-2"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='stop-code' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getStopCode();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getStopCode <em>Stop
     * Code</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Stop Code</em>' attribute.
     * @see #getStopCode()
     * @generated
     */
    void setStopCode(BigInteger value);

    /**
     * Returns the value of the '<em><b>Stop Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Stop Stmt</em>' containment reference.
     * @see #setStopStmt(StopStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_StopStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='stop-stmt' namespace='##targetNamespace'"
     * @generated
     */
    StopStmtType getStopStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getStopStmt <em>Stop
     * Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Stop Stmt</em>' containment reference.
     * @see #getStopStmt()
     * @generated
     */
    void setStopStmt(StopStmtType value);

    /**
     * Returns the value of the '<em><b>String E</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>String E</em>' containment reference.
     * @see #setStringE(StringEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_StringE()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='string-E' namespace='##targetNamespace'"
     * @generated
     */
    StringEType getStringE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getStringE
     * <em>String E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>String E</em>' containment reference.
     * @see #getStringE()
     * @generated
     */
    void setStringE(StringEType value);

    /**
     * Returns the value of the '<em><b>Subroutine N</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Subroutine N</em>' containment reference.
     * @see #setSubroutineN(SubroutineNType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_SubroutineN()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='subroutine-N' namespace='##targetNamespace'"
     * @generated
     */
    SubroutineNType getSubroutineN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSubroutineN
     * <em>Subroutine N</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Subroutine N</em>' containment reference.
     * @see #getSubroutineN()
     * @generated
     */
    void setSubroutineN(SubroutineNType value);

    /**
     * Returns the value of the '<em><b>Subroutine Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Subroutine Stmt</em>' containment reference.
     * @see #setSubroutineStmt(SubroutineStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_SubroutineStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='subroutine-stmt' namespace='##targetNamespace'"
     * @generated
     */
    SubroutineStmtType getSubroutineStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSubroutineStmt
     * <em>Subroutine Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Subroutine Stmt</em>' containment reference.
     * @see #getSubroutineStmt()
     * @generated
     */
    void setSubroutineStmt(SubroutineStmtType value);

    /**
     * Returns the value of the '<em><b>TDecl Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>TDecl Stmt</em>' containment reference.
     * @see #setTDeclStmt(TDeclStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_TDeclStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='T-decl-stmt' namespace='##targetNamespace'"
     * @generated
     */
    TDeclStmtType getTDeclStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getTDeclStmt
     * <em>TDecl Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>TDecl Stmt</em>' containment reference.
     * @see #getTDeclStmt()
     * @generated
     */
    void setTDeclStmt(TDeclStmtType value);

    /**
     * Returns the value of the '<em><b>TN</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>TN</em>' containment reference.
     * @see #setTN(TNType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_TN()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='T-N' namespace='##targetNamespace'"
     * @generated
     */
    TNType getTN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getTN <em>TN</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>TN</em>' containment reference.
     * @see #getTN()
     * @generated
     */
    void setTN(TNType value);

    /**
     * Returns the value of the '<em><b>TStmt</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>TStmt</em>' containment reference.
     * @see #setTStmt(TStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_TStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='T-stmt' namespace='##targetNamespace'"
     * @generated
     */
    TStmtType getTStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getTStmt
     * <em>TStmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>TStmt</em>' containment reference.
     * @see #getTStmt()
     * @generated
     */
    void setTStmt(TStmtType value);

    /**
     * Returns the value of the '<em><b>Test E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Test E</em>' containment reference.
     * @see #setTestE(TestEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_TestE()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='test-E' namespace='##targetNamespace'"
     * @generated
     */
    TestEType getTestE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getTestE <em>Test
     * E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Test E</em>' containment reference.
     * @see #getTestE()
     * @generated
     */
    void setTestE(TestEType value);

    /**
     * Returns the value of the '<em><b>Upper Bound</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Upper Bound</em>' containment reference.
     * @see #setUpperBound(UpperBoundType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_UpperBound()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='upper-bound' namespace='##targetNamespace'"
     * @generated
     */
    UpperBoundType getUpperBound();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getUpperBound
     * <em>Upper Bound</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Upper Bound</em>' containment reference.
     * @see #getUpperBound()
     * @generated
     */
    void setUpperBound(UpperBoundType value);

    /**
     * Returns the value of the '<em><b>Use N</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Use N</em>' containment reference.
     * @see #setUseN(UseNType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_UseN()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='use-N' namespace='##targetNamespace'"
     * @generated
     */
    UseNType getUseN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getUseN <em>Use
     * N</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Use N</em>' containment reference.
     * @see #getUseN()
     * @generated
     */
    void setUseN(UseNType value);

    /**
     * Returns the value of the '<em><b>Use Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Use Stmt</em>' containment reference.
     * @see #setUseStmt(UseStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_UseStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='use-stmt' namespace='##targetNamespace'"
     * @generated
     */
    UseStmtType getUseStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getUseStmt <em>Use
     * Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Use Stmt</em>' containment reference.
     * @see #getUseStmt()
     * @generated
     */
    void setUseStmt(UseStmtType value);

    /**
     * Returns the value of the '<em><b>V</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>V</em>' containment reference.
     * @see #setV(VType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_V()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='V' namespace='##targetNamespace'"
     * @generated
     */
    VType getV();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getV <em>V</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>V</em>' containment reference.
     * @see #getV()
     * @generated
     */
    void setV(VType value);

    /**
     * Returns the value of the '<em><b>VN</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>VN</em>' containment reference.
     * @see #setVN(VNType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_VN()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='V-N' namespace='##targetNamespace'"
     * @generated
     */
    VNType getVN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getVN <em>VN</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>VN</em>' containment reference.
     * @see #getVN()
     * @generated
     */
    void setVN(VNType value);

    /**
     * Returns the value of the '<em><b>Where Construct Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Where Construct Stmt</em>' containment reference.
     * @see #setWhereConstructStmt(WhereConstructStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_WhereConstructStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='where-construct-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    WhereConstructStmtType getWhereConstructStmt();

    /**
     * Sets the value of the
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getWhereConstructStmt <em>Where Construct
     * Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Where Construct Stmt</em>' containment reference.
     * @see #getWhereConstructStmt()
     * @generated
     */
    void setWhereConstructStmt(WhereConstructStmtType value);

    /**
     * Returns the value of the '<em><b>Where Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Where Stmt</em>' containment reference.
     * @see #setWhereStmt(WhereStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_WhereStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='where-stmt' namespace='##targetNamespace'"
     * @generated
     */
    WhereStmtType getWhereStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getWhereStmt
     * <em>Where Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Where Stmt</em>' containment reference.
     * @see #getWhereStmt()
     * @generated
     */
    void setWhereStmt(WhereStmtType value);

    /**
     * Returns the value of the '<em><b>Write Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Write Stmt</em>' containment reference.
     * @see #setWriteStmt(WriteStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getDocumentRoot_WriteStmt()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='write-stmt' namespace='##targetNamespace'"
     * @generated
     */
    WriteStmtType getWriteStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getWriteStmt
     * <em>Write Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Write Stmt</em>' containment reference.
     * @see #getWriteStmt()
     * @generated
     */
    void setWriteStmt(WriteStmtType value);

} // DocumentRoot
