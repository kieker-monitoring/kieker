/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import java.math.BigInteger;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.oceandsl.tools.sar.fxtran.*;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix
 * Map</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI
 * Schema Location</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getTSpec <em>TSpec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getA <em>A</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getAStmt <em>AStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getAcValue <em>Ac Value</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getAcValueLT <em>Ac Value
 * LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getActionStmt <em>Action
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getAllocateStmt <em>Allocate
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getArg <em>Arg</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getArgN <em>Arg N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getArgSpec <em>Arg Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getArrayConstructorE <em>Array
 * Constructor E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getArrayR <em>Array R</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getArraySpec <em>Array
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getAttribute
 * <em>Attribute</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getAttributeN <em>Attribute
 * N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getC <em>C</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getCallStmt <em>Call
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getCaseE <em>Case E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getCaseSelector <em>Case
 * Selector</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getCaseStmt <em>Case
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getCaseValue <em>Case
 * Value</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getCaseValueRange <em>Case Value
 * Range</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getCaseValueRangeLT <em>Case Value
 * Range LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getCharSelector <em>Char
 * Selector</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getCharSpec <em>Char
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getCloseSpec <em>Close
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getCloseSpecSpec <em>Close Spec
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getCloseStmt <em>Close
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getCnt <em>Cnt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getComponentDeclStmt <em>Component
 * Decl Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getComponentR <em>Component
 * R</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getConditionE <em>Condition
 * E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getConnectSpec <em>Connect
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getConnectSpecSpec <em>Connect
 * Spec Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getContainsStmt <em>Contains
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getCpp <em>Cpp</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getCt <em>Ct</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getCycleStmt <em>Cycle
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getDeallocateStmt <em>Deallocate
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getDerivedTSpec <em>Derived
 * TSpec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getDoStmt <em>Do Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getDoV <em>Do V</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getDummyArgLT <em>Dummy Arg
 * LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getE1 <em>E1</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getE2 <em>E2</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getElement <em>Element</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getElementLT <em>Element
 * LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getElseIfStmt <em>Else If
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getElseStmt <em>Else
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getElseWhereStmt <em>Else Where
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getEN <em>EN</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getENDecl <em>EN Decl</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getENDeclLT <em>EN Decl
 * LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getENLT <em>ENLT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getENN <em>ENN</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getEndDoStmt <em>End Do
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getEndForallStmt <em>End Forall
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getEndFunctionStmt <em>End
 * Function Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getEndIfStmt <em>End If
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getEndInterfaceStmt <em>End
 * Interface Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getEndModuleStmt <em>End Module
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getEndProgramStmt <em>End Program
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getEndSelectCaseStmt <em>End
 * Select Case Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getEndSubroutineStmt <em>End
 * Subroutine Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getEndTStmt <em>End
 * TStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getEndWhereStmt <em>End Where
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getError <em>Error</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getExitStmt <em>Exit
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getFile <em>File</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getForallConstructStmt <em>Forall
 * Construct Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getForallStmt <em>Forall
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getForallTripletSpec <em>Forall
 * Triplet Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getForallTripletSpecLT <em>Forall
 * Triplet Spec LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getFunctionN <em>Function
 * N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getFunctionStmt <em>Function
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getIfStmt <em>If Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getIfThenStmt <em>If Then
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getImplicitNoneStmt <em>Implicit
 * None Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getInitE <em>Init E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getInquireStmt <em>Inquire
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getInquirySpec <em>Inquiry
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getInquirySpecSpec <em>Inquiry
 * Spec Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getIntentSpec <em>Intent
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getInterfaceStmt <em>Interface
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getIntrinsicTSpec <em>Intrinsic
 * TSpec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getIoControl <em>Io
 * Control</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getIoControlSpec <em>Io Control
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getIterator
 * <em>Iterator</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getIteratorDefinitionLT
 * <em>Iterator Definition LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getIteratorElement <em>Iterator
 * Element</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getK <em>K</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getKSelector
 * <em>KSelector</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getKSpec <em>KSpec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getL <em>L</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getLabel <em>Label</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getLiteralE <em>Literal
 * E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getLowerBound <em>Lower
 * Bound</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getMaskE <em>Mask E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getModuleN <em>Module N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getModuleProcedureNLT <em>Module
 * Procedure NLT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getModuleStmt <em>Module
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getN <em>N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getN1 <em>N1</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getNamedE <em>Named E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getNamelistGroupN <em>Namelist
 * Group N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getNamelistGroupObj <em>Namelist
 * Group Obj</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getNamelistGroupObjLT <em>Namelist
 * Group Obj LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getNamelistGroupObjN <em>Namelist
 * Group Obj N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getNamelistStmt <em>Namelist
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getNullifyStmt <em>Nullify
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getO <em>O</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getObject <em>Object</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getOp <em>Op</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getOpE <em>Op E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getOpenStmt <em>Open
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getOutputItem <em>Output
 * Item</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getOutputItemLT <em>Output Item
 * LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getParensE <em>Parens E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getParensR <em>Parens R</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getPointerAStmt <em>Pointer
 * AStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getPointerStmt <em>Pointer
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getPrefix <em>Prefix</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getPrivateStmt <em>Private
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getProcedureDesignator
 * <em>Procedure Designator</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getProcedureStmt <em>Procedure
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getProgramN <em>Program
 * N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getProgramStmt <em>Program
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getPublicStmt <em>Public
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getRLT <em>RLT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getReadStmt <em>Read
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getRename <em>Rename</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getRenameLT <em>Rename
 * LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getResultSpec <em>Result
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getReturnStmt <em>Return
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getS <em>S</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getSaveStmt <em>Save
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getSectionSubscript <em>Section
 * Subscript</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getSectionSubscriptLT <em>Section
 * Subscript LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getSelectCaseStmt <em>Select Case
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getShapeSpec <em>Shape
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getShapeSpecLT <em>Shape Spec
 * LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getStarE <em>Star E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getStopCode <em>Stop
 * Code</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getStopStmt <em>Stop
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getStringE <em>String E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getSubroutineN <em>Subroutine
 * N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getSubroutineStmt <em>Subroutine
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getTDeclStmt <em>TDecl
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getTN <em>TN</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getTStmt <em>TStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getTestE <em>Test E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getUpperBound <em>Upper
 * Bound</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getUseN <em>Use N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getUseStmt <em>Use Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getV <em>V</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getVN <em>VN</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getWhereConstructStmt <em>Where
 * Construct Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getWhereStmt <em>Where
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl#getWriteStmt <em>Write
 * Stmt</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DocumentRootImpl extends MinimalEObjectImpl.Container implements DocumentRoot {
    /**
     * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getMixed()
     * @generated
     * @ordered
     */
    protected FeatureMap mixed;

    /**
     * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix Map</em>}' map. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getXMLNSPrefixMap()
     * @generated
     * @ordered
     */
    protected EMap<String, String> xMLNSPrefixMap;

    /**
     * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getXSISchemaLocation()
     * @generated
     * @ordered
     */
    protected EMap<String, String> xSISchemaLocation;

    /**
     * The default value of the '{@link #getA() <em>A</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getA()
     * @generated
     * @ordered
     */
    protected static final String A_EDEFAULT = null;

    /**
     * The default value of the '{@link #getAttributeN() <em>Attribute N</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getAttributeN()
     * @generated
     * @ordered
     */
    protected static final String ATTRIBUTE_N_EDEFAULT = null;

    /**
     * The default value of the '{@link #getC() <em>C</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getC()
     * @generated
     * @ordered
     */
    protected static final String C_EDEFAULT = null;

    /**
     * The default value of the '{@link #getCnt() <em>Cnt</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getCnt()
     * @generated
     * @ordered
     */
    protected static final String CNT_EDEFAULT = null;

    /**
     * The default value of the '{@link #getContainsStmt() <em>Contains Stmt</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getContainsStmt()
     * @generated
     * @ordered
     */
    protected static final String CONTAINS_STMT_EDEFAULT = null;

    /**
     * The default value of the '{@link #getCpp() <em>Cpp</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getCpp()
     * @generated
     * @ordered
     */
    protected static final String CPP_EDEFAULT = null;

    /**
     * The default value of the '{@link #getCt() <em>Ct</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getCt()
     * @generated
     * @ordered
     */
    protected static final String CT_EDEFAULT = null;

    /**
     * The default value of the '{@link #getElseStmt() <em>Else Stmt</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getElseStmt()
     * @generated
     * @ordered
     */
    protected static final String ELSE_STMT_EDEFAULT = null;

    /**
     * The default value of the '{@link #getElseWhereStmt() <em>Else Where Stmt</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getElseWhereStmt()
     * @generated
     * @ordered
     */
    protected static final String ELSE_WHERE_STMT_EDEFAULT = null;

    /**
     * The default value of the '{@link #getEndIfStmt() <em>End If Stmt</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getEndIfStmt()
     * @generated
     * @ordered
     */
    protected static final String END_IF_STMT_EDEFAULT = null;

    /**
     * The default value of the '{@link #getEndWhereStmt() <em>End Where Stmt</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getEndWhereStmt()
     * @generated
     * @ordered
     */
    protected static final String END_WHERE_STMT_EDEFAULT = null;

    /**
     * The default value of the '{@link #getExitStmt() <em>Exit Stmt</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getExitStmt()
     * @generated
     * @ordered
     */
    protected static final String EXIT_STMT_EDEFAULT = null;

    /**
     * The default value of the '{@link #getImplicitNoneStmt() <em>Implicit None Stmt</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getImplicitNoneStmt()
     * @generated
     * @ordered
     */
    protected static final String IMPLICIT_NONE_STMT_EDEFAULT = null;

    /**
     * The default value of the '{@link #getIntentSpec() <em>Intent Spec</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getIntentSpec()
     * @generated
     * @ordered
     */
    protected static final String INTENT_SPEC_EDEFAULT = null;

    /**
     * The default value of the '{@link #getK() <em>K</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getK()
     * @generated
     * @ordered
     */
    protected static final String K_EDEFAULT = null;

    /**
     * The default value of the '{@link #getL() <em>L</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getL()
     * @generated
     * @ordered
     */
    protected static final String L_EDEFAULT = null;

    /**
     * The default value of the '{@link #getN() <em>N</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getN()
     * @generated
     * @ordered
     */
    protected static final String N_EDEFAULT = null;

    /**
     * The default value of the '{@link #getO() <em>O</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getO()
     * @generated
     * @ordered
     */
    protected static final String O_EDEFAULT = null;

    /**
     * The default value of the '{@link #getPrefix() <em>Prefix</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getPrefix()
     * @generated
     * @ordered
     */
    protected static final String PREFIX_EDEFAULT = null;

    /**
     * The default value of the '{@link #getPrivateStmt() <em>Private Stmt</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getPrivateStmt()
     * @generated
     * @ordered
     */
    protected static final String PRIVATE_STMT_EDEFAULT = null;

    /**
     * The default value of the '{@link #getReturnStmt() <em>Return Stmt</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getReturnStmt()
     * @generated
     * @ordered
     */
    protected static final String RETURN_STMT_EDEFAULT = null;

    /**
     * The default value of the '{@link #getS() <em>S</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getS()
     * @generated
     * @ordered
     */
    protected static final String S_EDEFAULT = null;

    /**
     * The default value of the '{@link #getSaveStmt() <em>Save Stmt</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getSaveStmt()
     * @generated
     * @ordered
     */
    protected static final String SAVE_STMT_EDEFAULT = null;

    /**
     * The default value of the '{@link #getStarE() <em>Star E</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @see #getStarE()
     * @generated
     * @ordered
     */
    protected static final String STAR_E_EDEFAULT = null;

    /**
     * The default value of the '{@link #getStopCode() <em>Stop Code</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getStopCode()
     * @generated
     * @ordered
     */
    protected static final BigInteger STOP_CODE_EDEFAULT = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected DocumentRootImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getDocumentRoot();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.DOCUMENT_ROOT__MIXED);
        }
        return this.mixed;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EMap<String, String> getXMLNSPrefixMap() {
        if (this.xMLNSPrefixMap == null) {
            this.xMLNSPrefixMap = new EcoreEMap<>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY,
                    EStringToStringMapEntryImpl.class, this, FxtranPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        }
        return this.xMLNSPrefixMap;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EMap<String, String> getXSISchemaLocation() {
        if (this.xSISchemaLocation == null) {
            this.xSISchemaLocation = new EcoreEMap<>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY,
                    EStringToStringMapEntryImpl.class, this, FxtranPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        }
        return this.xSISchemaLocation;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TSpecType getTSpec() {
        return (TSpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_TSpec(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetTSpec(final TSpecType newTSpec, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_TSpec(),
                newTSpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setTSpec(final TSpecType newTSpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_TSpec(), newTSpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getA() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_A(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setA(final String newA) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_A(), newA);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public AStmtType getAStmt() {
        return (AStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_AStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetAStmt(final AStmtType newAStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_AStmt(),
                newAStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setAStmt(final AStmtType newAStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_AStmt(), newAStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public AcValueType getAcValue() {
        return (AcValueType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_AcValue(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetAcValue(final AcValueType newAcValue, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_AcValue(),
                newAcValue, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setAcValue(final AcValueType newAcValue) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_AcValue(), newAcValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public AcValueLTType getAcValueLT() {
        return (AcValueLTType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_AcValueLT(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetAcValueLT(final AcValueLTType newAcValueLT, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_AcValueLT(),
                newAcValueLT, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setAcValueLT(final AcValueLTType newAcValueLT) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_AcValueLT(), newAcValueLT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ActionStmtType getActionStmt() {
        return (ActionStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ActionStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetActionStmt(final ActionStmtType newActionStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ActionStmt(),
                newActionStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setActionStmt(final ActionStmtType newActionStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ActionStmt(),
                newActionStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public AllocateStmtType getAllocateStmt() {
        return (AllocateStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_AllocateStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetAllocateStmt(final AllocateStmtType newAllocateStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_AllocateStmt(),
                newAllocateStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setAllocateStmt(final AllocateStmtType newAllocateStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_AllocateStmt(),
                newAllocateStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ArgType getArg() {
        return (ArgType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_Arg(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetArg(final ArgType newArg, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_Arg(), newArg,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setArg(final ArgType newArg) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_Arg(), newArg);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ArgNType getArgN() {
        return (ArgNType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ArgN(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetArgN(final ArgNType newArgN, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ArgN(), newArgN,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setArgN(final ArgNType newArgN) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ArgN(), newArgN);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ArgSpecType getArgSpec() {
        return (ArgSpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ArgSpec(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetArgSpec(final ArgSpecType newArgSpec, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ArgSpec(),
                newArgSpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setArgSpec(final ArgSpecType newArgSpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ArgSpec(), newArgSpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ArrayConstructorEType getArrayConstructorE() {
        return (ArrayConstructorEType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ArrayConstructorE(),
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetArrayConstructorE(final ArrayConstructorEType newArrayConstructorE,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ArrayConstructorE(), newArrayConstructorE, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setArrayConstructorE(final ArrayConstructorEType newArrayConstructorE) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ArrayConstructorE(),
                newArrayConstructorE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ArrayRType getArrayR() {
        return (ArrayRType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ArrayR(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetArrayR(final ArrayRType newArrayR, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ArrayR(),
                newArrayR, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setArrayR(final ArrayRType newArrayR) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ArrayR(), newArrayR);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ArraySpecType getArraySpec() {
        return (ArraySpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ArraySpec(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetArraySpec(final ArraySpecType newArraySpec, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ArraySpec(),
                newArraySpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setArraySpec(final ArraySpecType newArraySpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ArraySpec(), newArraySpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public AttributeType getAttribute() {
        return (AttributeType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_Attribute(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetAttribute(final AttributeType newAttribute, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_Attribute(),
                newAttribute, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setAttribute(final AttributeType newAttribute) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_Attribute(), newAttribute);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getAttributeN() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_AttributeN(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setAttributeN(final String newAttributeN) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_AttributeN(),
                newAttributeN);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getC() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_C(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setC(final String newC) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_C(), newC);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CallStmtType getCallStmt() {
        return (CallStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_CallStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCallStmt(final CallStmtType newCallStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_CallStmt(),
                newCallStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCallStmt(final CallStmtType newCallStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_CallStmt(), newCallStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CaseEType getCaseE() {
        return (CaseEType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_CaseE(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCaseE(final CaseEType newCaseE, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_CaseE(),
                newCaseE, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCaseE(final CaseEType newCaseE) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_CaseE(), newCaseE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CaseSelectorType getCaseSelector() {
        return (CaseSelectorType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_CaseSelector(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCaseSelector(final CaseSelectorType newCaseSelector,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_CaseSelector(),
                newCaseSelector, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCaseSelector(final CaseSelectorType newCaseSelector) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_CaseSelector(),
                newCaseSelector);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CaseStmtType getCaseStmt() {
        return (CaseStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_CaseStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCaseStmt(final CaseStmtType newCaseStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_CaseStmt(),
                newCaseStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCaseStmt(final CaseStmtType newCaseStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_CaseStmt(), newCaseStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CaseValueType getCaseValue() {
        return (CaseValueType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_CaseValue(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCaseValue(final CaseValueType newCaseValue, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_CaseValue(),
                newCaseValue, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCaseValue(final CaseValueType newCaseValue) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_CaseValue(), newCaseValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CaseValueRangeType getCaseValueRange() {
        return (CaseValueRangeType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_CaseValueRange(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCaseValueRange(final CaseValueRangeType newCaseValueRange,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_CaseValueRange(), newCaseValueRange, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCaseValueRange(final CaseValueRangeType newCaseValueRange) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_CaseValueRange(),
                newCaseValueRange);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CaseValueRangeLTType getCaseValueRangeLT() {
        return (CaseValueRangeLTType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_CaseValueRangeLT(),
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCaseValueRangeLT(final CaseValueRangeLTType newCaseValueRangeLT,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_CaseValueRangeLT(), newCaseValueRangeLT, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCaseValueRangeLT(final CaseValueRangeLTType newCaseValueRangeLT) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_CaseValueRangeLT(),
                newCaseValueRangeLT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CharSelectorType getCharSelector() {
        return (CharSelectorType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_CharSelector(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCharSelector(final CharSelectorType newCharSelector,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_CharSelector(),
                newCharSelector, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCharSelector(final CharSelectorType newCharSelector) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_CharSelector(),
                newCharSelector);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CharSpecType getCharSpec() {
        return (CharSpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_CharSpec(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCharSpec(final CharSpecType newCharSpec, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_CharSpec(),
                newCharSpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCharSpec(final CharSpecType newCharSpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_CharSpec(), newCharSpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CloseSpecType getCloseSpec() {
        return (CloseSpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_CloseSpec(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCloseSpec(final CloseSpecType newCloseSpec, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_CloseSpec(),
                newCloseSpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCloseSpec(final CloseSpecType newCloseSpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_CloseSpec(), newCloseSpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CloseSpecSpecType getCloseSpecSpec() {
        return (CloseSpecSpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_CloseSpecSpec(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCloseSpecSpec(final CloseSpecSpecType newCloseSpecSpec,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_CloseSpecSpec(),
                newCloseSpecSpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCloseSpecSpec(final CloseSpecSpecType newCloseSpecSpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_CloseSpecSpec(),
                newCloseSpecSpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CloseStmtType getCloseStmt() {
        return (CloseStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_CloseStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCloseStmt(final CloseStmtType newCloseStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_CloseStmt(),
                newCloseStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCloseStmt(final CloseStmtType newCloseStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_CloseStmt(), newCloseStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getCnt() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_Cnt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCnt(final String newCnt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_Cnt(), newCnt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ComponentDeclStmtType getComponentDeclStmt() {
        return (ComponentDeclStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ComponentDeclStmt(),
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetComponentDeclStmt(final ComponentDeclStmtType newComponentDeclStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ComponentDeclStmt(), newComponentDeclStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setComponentDeclStmt(final ComponentDeclStmtType newComponentDeclStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ComponentDeclStmt(),
                newComponentDeclStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ComponentRType getComponentR() {
        return (ComponentRType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ComponentR(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetComponentR(final ComponentRType newComponentR, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ComponentR(),
                newComponentR, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setComponentR(final ComponentRType newComponentR) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ComponentR(),
                newComponentR);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ConditionEType getConditionE() {
        return (ConditionEType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ConditionE(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetConditionE(final ConditionEType newConditionE, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ConditionE(),
                newConditionE, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setConditionE(final ConditionEType newConditionE) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ConditionE(),
                newConditionE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ConnectSpecType getConnectSpec() {
        return (ConnectSpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ConnectSpec(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetConnectSpec(final ConnectSpecType newConnectSpec, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ConnectSpec(),
                newConnectSpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setConnectSpec(final ConnectSpecType newConnectSpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ConnectSpec(),
                newConnectSpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ConnectSpecSpecType getConnectSpecSpec() {
        return (ConnectSpecSpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ConnectSpecSpec(),
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetConnectSpecSpec(final ConnectSpecSpecType newConnectSpecSpec,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ConnectSpecSpec(), newConnectSpecSpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setConnectSpecSpec(final ConnectSpecSpecType newConnectSpecSpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ConnectSpecSpec(),
                newConnectSpecSpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getContainsStmt() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ContainsStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setContainsStmt(final String newContainsStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ContainsStmt(),
                newContainsStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getCpp() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_Cpp(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCpp(final String newCpp) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_Cpp(), newCpp);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getCt() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_Ct(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCt(final String newCt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_Ct(), newCt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CycleStmtType getCycleStmt() {
        return (CycleStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_CycleStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCycleStmt(final CycleStmtType newCycleStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_CycleStmt(),
                newCycleStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCycleStmt(final CycleStmtType newCycleStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_CycleStmt(), newCycleStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DeallocateStmtType getDeallocateStmt() {
        return (DeallocateStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_DeallocateStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetDeallocateStmt(final DeallocateStmtType newDeallocateStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_DeallocateStmt(), newDeallocateStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setDeallocateStmt(final DeallocateStmtType newDeallocateStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_DeallocateStmt(),
                newDeallocateStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DerivedTSpecType getDerivedTSpec() {
        return (DerivedTSpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_DerivedTSpec(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetDerivedTSpec(final DerivedTSpecType newDerivedTSpec,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_DerivedTSpec(),
                newDerivedTSpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setDerivedTSpec(final DerivedTSpecType newDerivedTSpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_DerivedTSpec(),
                newDerivedTSpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DoStmtType getDoStmt() {
        return (DoStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_DoStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetDoStmt(final DoStmtType newDoStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_DoStmt(),
                newDoStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setDoStmt(final DoStmtType newDoStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_DoStmt(), newDoStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DoVType getDoV() {
        return (DoVType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_DoV(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetDoV(final DoVType newDoV, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_DoV(), newDoV,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setDoV(final DoVType newDoV) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_DoV(), newDoV);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DummyArgLTType getDummyArgLT() {
        return (DummyArgLTType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_DummyArgLT(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetDummyArgLT(final DummyArgLTType newDummyArgLT, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_DummyArgLT(),
                newDummyArgLT, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setDummyArgLT(final DummyArgLTType newDummyArgLT) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_DummyArgLT(),
                newDummyArgLT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public E1Type getE1() {
        return (E1Type) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_E1(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetE1(final E1Type newE1, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_E1(), newE1,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setE1(final E1Type newE1) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_E1(), newE1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public E2Type getE2() {
        return (E2Type) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_E2(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetE2(final E2Type newE2, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_E2(), newE2,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setE2(final E2Type newE2) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_E2(), newE2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ElementType getElement() {
        return (ElementType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_Element(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetElement(final ElementType newElement, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_Element(),
                newElement, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setElement(final ElementType newElement) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_Element(), newElement);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ElementLTType getElementLT() {
        return (ElementLTType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ElementLT(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetElementLT(final ElementLTType newElementLT, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ElementLT(),
                newElementLT, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setElementLT(final ElementLTType newElementLT) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ElementLT(), newElementLT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ElseIfStmtType getElseIfStmt() {
        return (ElseIfStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ElseIfStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetElseIfStmt(final ElseIfStmtType newElseIfStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ElseIfStmt(),
                newElseIfStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setElseIfStmt(final ElseIfStmtType newElseIfStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ElseIfStmt(),
                newElseIfStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getElseStmt() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ElseStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setElseStmt(final String newElseStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ElseStmt(), newElseStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getElseWhereStmt() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ElseWhereStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setElseWhereStmt(final String newElseWhereStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ElseWhereStmt(),
                newElseWhereStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ENType getEN() {
        return (ENType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_EN(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetEN(final ENType newEN, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_EN(), newEN,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setEN(final ENType newEN) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_EN(), newEN);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ENDeclType getENDecl() {
        return (ENDeclType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ENDecl(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetENDecl(final ENDeclType newENDecl, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ENDecl(),
                newENDecl, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setENDecl(final ENDeclType newENDecl) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ENDecl(), newENDecl);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ENDeclLTType getENDeclLT() {
        return (ENDeclLTType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ENDeclLT(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetENDeclLT(final ENDeclLTType newENDeclLT, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ENDeclLT(),
                newENDeclLT, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setENDeclLT(final ENDeclLTType newENDeclLT) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ENDeclLT(), newENDeclLT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ENLTType getENLT() {
        return (ENLTType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ENLT(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetENLT(final ENLTType newENLT, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ENLT(), newENLT,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setENLT(final ENLTType newENLT) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ENLT(), newENLT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ENNType getENN() {
        return (ENNType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ENN(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetENN(final ENNType newENN, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ENN(), newENN,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setENN(final ENNType newENN) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ENN(), newENN);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndDoStmtType getEndDoStmt() {
        return (EndDoStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_EndDoStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetEndDoStmt(final EndDoStmtType newEndDoStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_EndDoStmt(),
                newEndDoStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setEndDoStmt(final EndDoStmtType newEndDoStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_EndDoStmt(), newEndDoStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndForallStmtType getEndForallStmt() {
        return (EndForallStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_EndForallStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetEndForallStmt(final EndForallStmtType newEndForallStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_EndForallStmt(),
                newEndForallStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setEndForallStmt(final EndForallStmtType newEndForallStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_EndForallStmt(),
                newEndForallStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndFunctionStmtType getEndFunctionStmt() {
        return (EndFunctionStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_EndFunctionStmt(),
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetEndFunctionStmt(final EndFunctionStmtType newEndFunctionStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_EndFunctionStmt(), newEndFunctionStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setEndFunctionStmt(final EndFunctionStmtType newEndFunctionStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_EndFunctionStmt(),
                newEndFunctionStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getEndIfStmt() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_EndIfStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setEndIfStmt(final String newEndIfStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_EndIfStmt(), newEndIfStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndInterfaceStmtType getEndInterfaceStmt() {
        return (EndInterfaceStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_EndInterfaceStmt(),
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetEndInterfaceStmt(final EndInterfaceStmtType newEndInterfaceStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_EndInterfaceStmt(), newEndInterfaceStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setEndInterfaceStmt(final EndInterfaceStmtType newEndInterfaceStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_EndInterfaceStmt(),
                newEndInterfaceStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndModuleStmtType getEndModuleStmt() {
        return (EndModuleStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_EndModuleStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetEndModuleStmt(final EndModuleStmtType newEndModuleStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_EndModuleStmt(),
                newEndModuleStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setEndModuleStmt(final EndModuleStmtType newEndModuleStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_EndModuleStmt(),
                newEndModuleStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndProgramStmtType getEndProgramStmt() {
        return (EndProgramStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_EndProgramStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetEndProgramStmt(final EndProgramStmtType newEndProgramStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_EndProgramStmt(), newEndProgramStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setEndProgramStmt(final EndProgramStmtType newEndProgramStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_EndProgramStmt(),
                newEndProgramStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndSelectCaseStmtType getEndSelectCaseStmt() {
        return (EndSelectCaseStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_EndSelectCaseStmt(),
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetEndSelectCaseStmt(final EndSelectCaseStmtType newEndSelectCaseStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_EndSelectCaseStmt(), newEndSelectCaseStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setEndSelectCaseStmt(final EndSelectCaseStmtType newEndSelectCaseStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_EndSelectCaseStmt(),
                newEndSelectCaseStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndSubroutineStmtType getEndSubroutineStmt() {
        return (EndSubroutineStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_EndSubroutineStmt(),
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetEndSubroutineStmt(final EndSubroutineStmtType newEndSubroutineStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_EndSubroutineStmt(), newEndSubroutineStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setEndSubroutineStmt(final EndSubroutineStmtType newEndSubroutineStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_EndSubroutineStmt(),
                newEndSubroutineStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndTStmtType getEndTStmt() {
        return (EndTStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_EndTStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetEndTStmt(final EndTStmtType newEndTStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_EndTStmt(),
                newEndTStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setEndTStmt(final EndTStmtType newEndTStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_EndTStmt(), newEndTStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getEndWhereStmt() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_EndWhereStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setEndWhereStmt(final String newEndWhereStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_EndWhereStmt(),
                newEndWhereStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ErrorType getError() {
        return (ErrorType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_Error(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetError(final ErrorType newError, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_Error(),
                newError, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setError(final ErrorType newError) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_Error(), newError);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getExitStmt() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ExitStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setExitStmt(final String newExitStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ExitStmt(), newExitStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FileType getFile() {
        return (FileType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_File(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetFile(final FileType newFile, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_File(), newFile,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setFile(final FileType newFile) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_File(), newFile);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ForallConstructStmtType getForallConstructStmt() {
        return (ForallConstructStmtType) this.getMixed()
                .get(FxtranPackage.eINSTANCE.getDocumentRoot_ForallConstructStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetForallConstructStmt(final ForallConstructStmtType newForallConstructStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ForallConstructStmt(), newForallConstructStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setForallConstructStmt(final ForallConstructStmtType newForallConstructStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ForallConstructStmt(),
                newForallConstructStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ForallStmtType getForallStmt() {
        return (ForallStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ForallStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetForallStmt(final ForallStmtType newForallStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ForallStmt(),
                newForallStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setForallStmt(final ForallStmtType newForallStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ForallStmt(),
                newForallStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ForallTripletSpecType getForallTripletSpec() {
        return (ForallTripletSpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ForallTripletSpec(),
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetForallTripletSpec(final ForallTripletSpecType newForallTripletSpec,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ForallTripletSpec(), newForallTripletSpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setForallTripletSpec(final ForallTripletSpecType newForallTripletSpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ForallTripletSpec(),
                newForallTripletSpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ForallTripletSpecLTType getForallTripletSpecLT() {
        return (ForallTripletSpecLTType) this.getMixed()
                .get(FxtranPackage.eINSTANCE.getDocumentRoot_ForallTripletSpecLT(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetForallTripletSpecLT(final ForallTripletSpecLTType newForallTripletSpecLT,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ForallTripletSpecLT(), newForallTripletSpecLT, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setForallTripletSpecLT(final ForallTripletSpecLTType newForallTripletSpecLT) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ForallTripletSpecLT(),
                newForallTripletSpecLT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FunctionNType getFunctionN() {
        return (FunctionNType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_FunctionN(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetFunctionN(final FunctionNType newFunctionN, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_FunctionN(),
                newFunctionN, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setFunctionN(final FunctionNType newFunctionN) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_FunctionN(), newFunctionN);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FunctionStmtType getFunctionStmt() {
        return (FunctionStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_FunctionStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetFunctionStmt(final FunctionStmtType newFunctionStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_FunctionStmt(),
                newFunctionStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setFunctionStmt(final FunctionStmtType newFunctionStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_FunctionStmt(),
                newFunctionStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IfStmtType getIfStmt() {
        return (IfStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_IfStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetIfStmt(final IfStmtType newIfStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_IfStmt(),
                newIfStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIfStmt(final IfStmtType newIfStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_IfStmt(), newIfStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IfThenStmtType getIfThenStmt() {
        return (IfThenStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_IfThenStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetIfThenStmt(final IfThenStmtType newIfThenStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_IfThenStmt(),
                newIfThenStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIfThenStmt(final IfThenStmtType newIfThenStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_IfThenStmt(),
                newIfThenStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getImplicitNoneStmt() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ImplicitNoneStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setImplicitNoneStmt(final String newImplicitNoneStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ImplicitNoneStmt(),
                newImplicitNoneStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public InitEType getInitE() {
        return (InitEType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_InitE(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetInitE(final InitEType newInitE, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_InitE(),
                newInitE, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setInitE(final InitEType newInitE) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_InitE(), newInitE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public InquireStmtType getInquireStmt() {
        return (InquireStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_InquireStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetInquireStmt(final InquireStmtType newInquireStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_InquireStmt(),
                newInquireStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setInquireStmt(final InquireStmtType newInquireStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_InquireStmt(),
                newInquireStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public InquirySpecType getInquirySpec() {
        return (InquirySpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_InquirySpec(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetInquirySpec(final InquirySpecType newInquirySpec, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_InquirySpec(),
                newInquirySpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setInquirySpec(final InquirySpecType newInquirySpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_InquirySpec(),
                newInquirySpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public InquirySpecSpecType getInquirySpecSpec() {
        return (InquirySpecSpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_InquirySpecSpec(),
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetInquirySpecSpec(final InquirySpecSpecType newInquirySpecSpec,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_InquirySpecSpec(), newInquirySpecSpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setInquirySpecSpec(final InquirySpecSpecType newInquirySpecSpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_InquirySpecSpec(),
                newInquirySpecSpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getIntentSpec() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_IntentSpec(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIntentSpec(final String newIntentSpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_IntentSpec(),
                newIntentSpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public InterfaceStmtType getInterfaceStmt() {
        return (InterfaceStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_InterfaceStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetInterfaceStmt(final InterfaceStmtType newInterfaceStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_InterfaceStmt(),
                newInterfaceStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setInterfaceStmt(final InterfaceStmtType newInterfaceStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_InterfaceStmt(),
                newInterfaceStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IntrinsicTSpecType getIntrinsicTSpec() {
        return (IntrinsicTSpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_IntrinsicTSpec(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetIntrinsicTSpec(final IntrinsicTSpecType newIntrinsicTSpec,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_IntrinsicTSpec(), newIntrinsicTSpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIntrinsicTSpec(final IntrinsicTSpecType newIntrinsicTSpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_IntrinsicTSpec(),
                newIntrinsicTSpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IoControlType getIoControl() {
        return (IoControlType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_IoControl(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetIoControl(final IoControlType newIoControl, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_IoControl(),
                newIoControl, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIoControl(final IoControlType newIoControl) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_IoControl(), newIoControl);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IoControlSpecType getIoControlSpec() {
        return (IoControlSpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_IoControlSpec(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetIoControlSpec(final IoControlSpecType newIoControlSpec,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_IoControlSpec(),
                newIoControlSpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIoControlSpec(final IoControlSpecType newIoControlSpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_IoControlSpec(),
                newIoControlSpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IteratorType getIterator() {
        return (IteratorType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_Iterator(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetIterator(final IteratorType newIterator, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_Iterator(),
                newIterator, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIterator(final IteratorType newIterator) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_Iterator(), newIterator);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IteratorDefinitionLTType getIteratorDefinitionLT() {
        return (IteratorDefinitionLTType) this.getMixed()
                .get(FxtranPackage.eINSTANCE.getDocumentRoot_IteratorDefinitionLT(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetIteratorDefinitionLT(final IteratorDefinitionLTType newIteratorDefinitionLT,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(
                FxtranPackage.eINSTANCE.getDocumentRoot_IteratorDefinitionLT(), newIteratorDefinitionLT, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIteratorDefinitionLT(final IteratorDefinitionLTType newIteratorDefinitionLT) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_IteratorDefinitionLT(),
                newIteratorDefinitionLT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IteratorElementType getIteratorElement() {
        return (IteratorElementType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_IteratorElement(),
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetIteratorElement(final IteratorElementType newIteratorElement,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_IteratorElement(), newIteratorElement, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIteratorElement(final IteratorElementType newIteratorElement) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_IteratorElement(),
                newIteratorElement);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getK() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_K(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setK(final String newK) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_K(), newK);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public KSelectorType getKSelector() {
        return (KSelectorType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_KSelector(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetKSelector(final KSelectorType newKSelector, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_KSelector(),
                newKSelector, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setKSelector(final KSelectorType newKSelector) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_KSelector(), newKSelector);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public KSpecType getKSpec() {
        return (KSpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_KSpec(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetKSpec(final KSpecType newKSpec, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_KSpec(),
                newKSpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setKSpec(final KSpecType newKSpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_KSpec(), newKSpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getL() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_L(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setL(final String newL) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_L(), newL);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public LabelType getLabel() {
        return (LabelType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_Label(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetLabel(final LabelType newLabel, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_Label(),
                newLabel, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setLabel(final LabelType newLabel) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_Label(), newLabel);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public LiteralEType getLiteralE() {
        return (LiteralEType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_LiteralE(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetLiteralE(final LiteralEType newLiteralE, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_LiteralE(),
                newLiteralE, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setLiteralE(final LiteralEType newLiteralE) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_LiteralE(), newLiteralE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public LowerBoundType getLowerBound() {
        return (LowerBoundType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_LowerBound(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetLowerBound(final LowerBoundType newLowerBound, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_LowerBound(),
                newLowerBound, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setLowerBound(final LowerBoundType newLowerBound) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_LowerBound(),
                newLowerBound);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public MaskEType getMaskE() {
        return (MaskEType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_MaskE(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetMaskE(final MaskEType newMaskE, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_MaskE(),
                newMaskE, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setMaskE(final MaskEType newMaskE) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_MaskE(), newMaskE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ModuleNType getModuleN() {
        return (ModuleNType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ModuleN(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetModuleN(final ModuleNType newModuleN, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ModuleN(),
                newModuleN, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setModuleN(final ModuleNType newModuleN) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ModuleN(), newModuleN);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ModuleProcedureNLTType getModuleProcedureNLT() {
        return (ModuleProcedureNLTType) this.getMixed()
                .get(FxtranPackage.eINSTANCE.getDocumentRoot_ModuleProcedureNLT(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetModuleProcedureNLT(final ModuleProcedureNLTType newModuleProcedureNLT,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ModuleProcedureNLT(), newModuleProcedureNLT, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setModuleProcedureNLT(final ModuleProcedureNLTType newModuleProcedureNLT) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ModuleProcedureNLT(),
                newModuleProcedureNLT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ModuleStmtType getModuleStmt() {
        return (ModuleStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ModuleStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetModuleStmt(final ModuleStmtType newModuleStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ModuleStmt(),
                newModuleStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setModuleStmt(final ModuleStmtType newModuleStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ModuleStmt(),
                newModuleStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getN() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_N(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setN(final String newN) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_N(), newN);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NType getN1() {
        return (NType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_N1(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetN1(final NType newN1, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_N1(), newN1,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setN1(final NType newN1) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_N1(), newN1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NamedEType getNamedE() {
        return (NamedEType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_NamedE(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetNamedE(final NamedEType newNamedE, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_NamedE(),
                newNamedE, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setNamedE(final NamedEType newNamedE) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_NamedE(), newNamedE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NamelistGroupNType getNamelistGroupN() {
        return (NamelistGroupNType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_NamelistGroupN(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetNamelistGroupN(final NamelistGroupNType newNamelistGroupN,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_NamelistGroupN(), newNamelistGroupN, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setNamelistGroupN(final NamelistGroupNType newNamelistGroupN) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_NamelistGroupN(),
                newNamelistGroupN);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NamelistGroupObjType getNamelistGroupObj() {
        return (NamelistGroupObjType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_NamelistGroupObj(),
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetNamelistGroupObj(final NamelistGroupObjType newNamelistGroupObj,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_NamelistGroupObj(), newNamelistGroupObj, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setNamelistGroupObj(final NamelistGroupObjType newNamelistGroupObj) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_NamelistGroupObj(),
                newNamelistGroupObj);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NamelistGroupObjLTType getNamelistGroupObjLT() {
        return (NamelistGroupObjLTType) this.getMixed()
                .get(FxtranPackage.eINSTANCE.getDocumentRoot_NamelistGroupObjLT(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetNamelistGroupObjLT(final NamelistGroupObjLTType newNamelistGroupObjLT,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_NamelistGroupObjLT(), newNamelistGroupObjLT, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setNamelistGroupObjLT(final NamelistGroupObjLTType newNamelistGroupObjLT) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_NamelistGroupObjLT(),
                newNamelistGroupObjLT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NamelistGroupObjNType getNamelistGroupObjN() {
        return (NamelistGroupObjNType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_NamelistGroupObjN(),
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetNamelistGroupObjN(final NamelistGroupObjNType newNamelistGroupObjN,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_NamelistGroupObjN(), newNamelistGroupObjN, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setNamelistGroupObjN(final NamelistGroupObjNType newNamelistGroupObjN) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_NamelistGroupObjN(),
                newNamelistGroupObjN);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NamelistStmtType getNamelistStmt() {
        return (NamelistStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_NamelistStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetNamelistStmt(final NamelistStmtType newNamelistStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_NamelistStmt(),
                newNamelistStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setNamelistStmt(final NamelistStmtType newNamelistStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_NamelistStmt(),
                newNamelistStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NullifyStmtType getNullifyStmt() {
        return (NullifyStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_NullifyStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetNullifyStmt(final NullifyStmtType newNullifyStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_NullifyStmt(),
                newNullifyStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setNullifyStmt(final NullifyStmtType newNullifyStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_NullifyStmt(),
                newNullifyStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getO() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_O(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setO(final String newO) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_O(), newO);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ObjectType getObject() {
        return (ObjectType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_Object(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetObject(final ObjectType newObject, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_Object(),
                newObject, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setObject(final ObjectType newObject) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_Object(), newObject);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OpType getOp() {
        return (OpType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_Op(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetOp(final OpType newOp, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_Op(), newOp,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setOp(final OpType newOp) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_Op(), newOp);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OpEType getOpE() {
        return (OpEType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_OpE(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetOpE(final OpEType newOpE, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_OpE(), newOpE,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setOpE(final OpEType newOpE) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_OpE(), newOpE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OpenStmtType getOpenStmt() {
        return (OpenStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_OpenStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetOpenStmt(final OpenStmtType newOpenStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_OpenStmt(),
                newOpenStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setOpenStmt(final OpenStmtType newOpenStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_OpenStmt(), newOpenStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OutputItemType getOutputItem() {
        return (OutputItemType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_OutputItem(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetOutputItem(final OutputItemType newOutputItem, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_OutputItem(),
                newOutputItem, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setOutputItem(final OutputItemType newOutputItem) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_OutputItem(),
                newOutputItem);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OutputItemLTType getOutputItemLT() {
        return (OutputItemLTType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_OutputItemLT(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetOutputItemLT(final OutputItemLTType newOutputItemLT,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_OutputItemLT(),
                newOutputItemLT, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setOutputItemLT(final OutputItemLTType newOutputItemLT) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_OutputItemLT(),
                newOutputItemLT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ParensEType getParensE() {
        return (ParensEType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ParensE(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetParensE(final ParensEType newParensE, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ParensE(),
                newParensE, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setParensE(final ParensEType newParensE) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ParensE(), newParensE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ParensRType getParensR() {
        return (ParensRType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ParensR(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetParensR(final ParensRType newParensR, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ParensR(),
                newParensR, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setParensR(final ParensRType newParensR) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ParensR(), newParensR);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PointerAStmtType getPointerAStmt() {
        return (PointerAStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_PointerAStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetPointerAStmt(final PointerAStmtType newPointerAStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_PointerAStmt(),
                newPointerAStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setPointerAStmt(final PointerAStmtType newPointerAStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_PointerAStmt(),
                newPointerAStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PointerStmtType getPointerStmt() {
        return (PointerStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_PointerStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetPointerStmt(final PointerStmtType newPointerStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_PointerStmt(),
                newPointerStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setPointerStmt(final PointerStmtType newPointerStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_PointerStmt(),
                newPointerStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getPrefix() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_Prefix(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setPrefix(final String newPrefix) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_Prefix(), newPrefix);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getPrivateStmt() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_PrivateStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setPrivateStmt(final String newPrivateStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_PrivateStmt(),
                newPrivateStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ProcedureDesignatorType getProcedureDesignator() {
        return (ProcedureDesignatorType) this.getMixed()
                .get(FxtranPackage.eINSTANCE.getDocumentRoot_ProcedureDesignator(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetProcedureDesignator(final ProcedureDesignatorType newProcedureDesignator,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ProcedureDesignator(), newProcedureDesignator, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setProcedureDesignator(final ProcedureDesignatorType newProcedureDesignator) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ProcedureDesignator(),
                newProcedureDesignator);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ProcedureStmtType getProcedureStmt() {
        return (ProcedureStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ProcedureStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetProcedureStmt(final ProcedureStmtType newProcedureStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ProcedureStmt(),
                newProcedureStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setProcedureStmt(final ProcedureStmtType newProcedureStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ProcedureStmt(),
                newProcedureStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ProgramNType getProgramN() {
        return (ProgramNType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ProgramN(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetProgramN(final ProgramNType newProgramN, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ProgramN(),
                newProgramN, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setProgramN(final ProgramNType newProgramN) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ProgramN(), newProgramN);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ProgramStmtType getProgramStmt() {
        return (ProgramStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ProgramStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetProgramStmt(final ProgramStmtType newProgramStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ProgramStmt(),
                newProgramStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setProgramStmt(final ProgramStmtType newProgramStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ProgramStmt(),
                newProgramStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PublicStmtType getPublicStmt() {
        return (PublicStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_PublicStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetPublicStmt(final PublicStmtType newPublicStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_PublicStmt(),
                newPublicStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setPublicStmt(final PublicStmtType newPublicStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_PublicStmt(),
                newPublicStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RLTType getRLT() {
        return (RLTType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_RLT(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetRLT(final RLTType newRLT, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_RLT(), newRLT,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setRLT(final RLTType newRLT) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_RLT(), newRLT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ReadStmtType getReadStmt() {
        return (ReadStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ReadStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetReadStmt(final ReadStmtType newReadStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ReadStmt(),
                newReadStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setReadStmt(final ReadStmtType newReadStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ReadStmt(), newReadStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RenameType getRename() {
        return (RenameType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_Rename(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetRename(final RenameType newRename, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_Rename(),
                newRename, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setRename(final RenameType newRename) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_Rename(), newRename);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RenameLTType getRenameLT() {
        return (RenameLTType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_RenameLT(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetRenameLT(final RenameLTType newRenameLT, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_RenameLT(),
                newRenameLT, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setRenameLT(final RenameLTType newRenameLT) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_RenameLT(), newRenameLT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ResultSpecType getResultSpec() {
        return (ResultSpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ResultSpec(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetResultSpec(final ResultSpecType newResultSpec, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ResultSpec(),
                newResultSpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setResultSpec(final ResultSpecType newResultSpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ResultSpec(),
                newResultSpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getReturnStmt() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ReturnStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setReturnStmt(final String newReturnStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ReturnStmt(),
                newReturnStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getS() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_S(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setS(final String newS) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_S(), newS);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getSaveStmt() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_SaveStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setSaveStmt(final String newSaveStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_SaveStmt(), newSaveStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SectionSubscriptType getSectionSubscript() {
        return (SectionSubscriptType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_SectionSubscript(),
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetSectionSubscript(final SectionSubscriptType newSectionSubscript,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_SectionSubscript(), newSectionSubscript, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setSectionSubscript(final SectionSubscriptType newSectionSubscript) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_SectionSubscript(),
                newSectionSubscript);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SectionSubscriptLTType getSectionSubscriptLT() {
        return (SectionSubscriptLTType) this.getMixed()
                .get(FxtranPackage.eINSTANCE.getDocumentRoot_SectionSubscriptLT(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetSectionSubscriptLT(final SectionSubscriptLTType newSectionSubscriptLT,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_SectionSubscriptLT(), newSectionSubscriptLT, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setSectionSubscriptLT(final SectionSubscriptLTType newSectionSubscriptLT) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_SectionSubscriptLT(),
                newSectionSubscriptLT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SelectCaseStmtType getSelectCaseStmt() {
        return (SelectCaseStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_SelectCaseStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetSelectCaseStmt(final SelectCaseStmtType newSelectCaseStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_SelectCaseStmt(), newSelectCaseStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setSelectCaseStmt(final SelectCaseStmtType newSelectCaseStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_SelectCaseStmt(),
                newSelectCaseStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ShapeSpecType getShapeSpec() {
        return (ShapeSpecType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ShapeSpec(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetShapeSpec(final ShapeSpecType newShapeSpec, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ShapeSpec(),
                newShapeSpec, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setShapeSpec(final ShapeSpecType newShapeSpec) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ShapeSpec(), newShapeSpec);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ShapeSpecLTType getShapeSpecLT() {
        return (ShapeSpecLTType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_ShapeSpecLT(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetShapeSpecLT(final ShapeSpecLTType newShapeSpecLT, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_ShapeSpecLT(),
                newShapeSpecLT, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setShapeSpecLT(final ShapeSpecLTType newShapeSpecLT) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_ShapeSpecLT(),
                newShapeSpecLT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getStarE() {
        return (String) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_StarE(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setStarE(final String newStarE) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_StarE(), newStarE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public BigInteger getStopCode() {
        return (BigInteger) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_StopCode(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setStopCode(final BigInteger newStopCode) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_StopCode(), newStopCode);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public StopStmtType getStopStmt() {
        return (StopStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_StopStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetStopStmt(final StopStmtType newStopStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_StopStmt(),
                newStopStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setStopStmt(final StopStmtType newStopStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_StopStmt(), newStopStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public StringEType getStringE() {
        return (StringEType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_StringE(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetStringE(final StringEType newStringE, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_StringE(),
                newStringE, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setStringE(final StringEType newStringE) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_StringE(), newStringE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SubroutineNType getSubroutineN() {
        return (SubroutineNType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_SubroutineN(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetSubroutineN(final SubroutineNType newSubroutineN, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_SubroutineN(),
                newSubroutineN, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setSubroutineN(final SubroutineNType newSubroutineN) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_SubroutineN(),
                newSubroutineN);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SubroutineStmtType getSubroutineStmt() {
        return (SubroutineStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_SubroutineStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetSubroutineStmt(final SubroutineStmtType newSubroutineStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_SubroutineStmt(), newSubroutineStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setSubroutineStmt(final SubroutineStmtType newSubroutineStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_SubroutineStmt(),
                newSubroutineStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TDeclStmtType getTDeclStmt() {
        return (TDeclStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_TDeclStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetTDeclStmt(final TDeclStmtType newTDeclStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_TDeclStmt(),
                newTDeclStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setTDeclStmt(final TDeclStmtType newTDeclStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_TDeclStmt(), newTDeclStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TNType getTN() {
        return (TNType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_TN(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetTN(final TNType newTN, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_TN(), newTN,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setTN(final TNType newTN) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_TN(), newTN);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TStmtType getTStmt() {
        return (TStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_TStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetTStmt(final TStmtType newTStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_TStmt(),
                newTStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setTStmt(final TStmtType newTStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_TStmt(), newTStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TestEType getTestE() {
        return (TestEType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_TestE(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetTestE(final TestEType newTestE, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_TestE(),
                newTestE, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setTestE(final TestEType newTestE) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_TestE(), newTestE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public UpperBoundType getUpperBound() {
        return (UpperBoundType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_UpperBound(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetUpperBound(final UpperBoundType newUpperBound, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_UpperBound(),
                newUpperBound, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setUpperBound(final UpperBoundType newUpperBound) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_UpperBound(),
                newUpperBound);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public UseNType getUseN() {
        return (UseNType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_UseN(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetUseN(final UseNType newUseN, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_UseN(), newUseN,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setUseN(final UseNType newUseN) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_UseN(), newUseN);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public UseStmtType getUseStmt() {
        return (UseStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_UseStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetUseStmt(final UseStmtType newUseStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_UseStmt(),
                newUseStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setUseStmt(final UseStmtType newUseStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_UseStmt(), newUseStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public VType getV() {
        return (VType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_V(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetV(final VType newV, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_V(), newV,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setV(final VType newV) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_V(), newV);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public VNType getVN() {
        return (VNType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_VN(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetVN(final VNType newVN, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_VN(), newVN,
                msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setVN(final VNType newVN) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_VN(), newVN);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public WhereConstructStmtType getWhereConstructStmt() {
        return (WhereConstructStmtType) this.getMixed()
                .get(FxtranPackage.eINSTANCE.getDocumentRoot_WhereConstructStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetWhereConstructStmt(final WhereConstructStmtType newWhereConstructStmt,
            final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed())
                .basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_WhereConstructStmt(), newWhereConstructStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setWhereConstructStmt(final WhereConstructStmtType newWhereConstructStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_WhereConstructStmt(),
                newWhereConstructStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public WhereStmtType getWhereStmt() {
        return (WhereStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_WhereStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetWhereStmt(final WhereStmtType newWhereStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_WhereStmt(),
                newWhereStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setWhereStmt(final WhereStmtType newWhereStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_WhereStmt(), newWhereStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public WriteStmtType getWriteStmt() {
        return (WriteStmtType) this.getMixed().get(FxtranPackage.eINSTANCE.getDocumentRoot_WriteStmt(), true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetWriteStmt(final WriteStmtType newWriteStmt, final NotificationChain msgs) {
        return ((FeatureMap.Internal) this.getMixed()).basicAdd(FxtranPackage.eINSTANCE.getDocumentRoot_WriteStmt(),
                newWriteStmt, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setWriteStmt(final WriteStmtType newWriteStmt) {
        ((FeatureMap.Internal) this.getMixed()).set(FxtranPackage.eINSTANCE.getDocumentRoot_WriteStmt(), newWriteStmt);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(final InternalEObject otherEnd, final int featureID,
            final NotificationChain msgs) {
        switch (featureID) {
        case FxtranPackage.DOCUMENT_ROOT__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
            return ((InternalEList<?>) this.getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
        case FxtranPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
            return ((InternalEList<?>) this.getXSISchemaLocation()).basicRemove(otherEnd, msgs);
        case FxtranPackage.DOCUMENT_ROOT__TSPEC:
            return this.basicSetTSpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ASTMT:
            return this.basicSetAStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__AC_VALUE:
            return this.basicSetAcValue(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__AC_VALUE_LT:
            return this.basicSetAcValueLT(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ACTION_STMT:
            return this.basicSetActionStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ALLOCATE_STMT:
            return this.basicSetAllocateStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ARG:
            return this.basicSetArg(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ARG_N:
            return this.basicSetArgN(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ARG_SPEC:
            return this.basicSetArgSpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ARRAY_CONSTRUCTOR_E:
            return this.basicSetArrayConstructorE(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ARRAY_R:
            return this.basicSetArrayR(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ARRAY_SPEC:
            return this.basicSetArraySpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ATTRIBUTE:
            return this.basicSetAttribute(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__CALL_STMT:
            return this.basicSetCallStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__CASE_E:
            return this.basicSetCaseE(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__CASE_SELECTOR:
            return this.basicSetCaseSelector(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__CASE_STMT:
            return this.basicSetCaseStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__CASE_VALUE:
            return this.basicSetCaseValue(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__CASE_VALUE_RANGE:
            return this.basicSetCaseValueRange(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__CASE_VALUE_RANGE_LT:
            return this.basicSetCaseValueRangeLT(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__CHAR_SELECTOR:
            return this.basicSetCharSelector(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__CHAR_SPEC:
            return this.basicSetCharSpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__CLOSE_SPEC:
            return this.basicSetCloseSpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__CLOSE_SPEC_SPEC:
            return this.basicSetCloseSpecSpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__CLOSE_STMT:
            return this.basicSetCloseStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__COMPONENT_DECL_STMT:
            return this.basicSetComponentDeclStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__COMPONENT_R:
            return this.basicSetComponentR(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__CONDITION_E:
            return this.basicSetConditionE(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__CONNECT_SPEC:
            return this.basicSetConnectSpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__CONNECT_SPEC_SPEC:
            return this.basicSetConnectSpecSpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__CYCLE_STMT:
            return this.basicSetCycleStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__DEALLOCATE_STMT:
            return this.basicSetDeallocateStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__DERIVED_TSPEC:
            return this.basicSetDerivedTSpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__DO_STMT:
            return this.basicSetDoStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__DO_V:
            return this.basicSetDoV(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__DUMMY_ARG_LT:
            return this.basicSetDummyArgLT(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__E1:
            return this.basicSetE1(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__E2:
            return this.basicSetE2(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ELEMENT:
            return this.basicSetElement(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ELEMENT_LT:
            return this.basicSetElementLT(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ELSE_IF_STMT:
            return this.basicSetElseIfStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__EN:
            return this.basicSetEN(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__EN_DECL:
            return this.basicSetENDecl(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__EN_DECL_LT:
            return this.basicSetENDeclLT(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ENLT:
            return this.basicSetENLT(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ENN:
            return this.basicSetENN(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__END_DO_STMT:
            return this.basicSetEndDoStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__END_FORALL_STMT:
            return this.basicSetEndForallStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__END_FUNCTION_STMT:
            return this.basicSetEndFunctionStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__END_INTERFACE_STMT:
            return this.basicSetEndInterfaceStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__END_MODULE_STMT:
            return this.basicSetEndModuleStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__END_PROGRAM_STMT:
            return this.basicSetEndProgramStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__END_SELECT_CASE_STMT:
            return this.basicSetEndSelectCaseStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__END_SUBROUTINE_STMT:
            return this.basicSetEndSubroutineStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__END_TSTMT:
            return this.basicSetEndTStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ERROR:
            return this.basicSetError(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__FILE:
            return this.basicSetFile(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__FORALL_CONSTRUCT_STMT:
            return this.basicSetForallConstructStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__FORALL_STMT:
            return this.basicSetForallStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__FORALL_TRIPLET_SPEC:
            return this.basicSetForallTripletSpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__FORALL_TRIPLET_SPEC_LT:
            return this.basicSetForallTripletSpecLT(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__FUNCTION_N:
            return this.basicSetFunctionN(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__FUNCTION_STMT:
            return this.basicSetFunctionStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__IF_STMT:
            return this.basicSetIfStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__IF_THEN_STMT:
            return this.basicSetIfThenStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__INIT_E:
            return this.basicSetInitE(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__INQUIRE_STMT:
            return this.basicSetInquireStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__INQUIRY_SPEC:
            return this.basicSetInquirySpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__INQUIRY_SPEC_SPEC:
            return this.basicSetInquirySpecSpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__INTERFACE_STMT:
            return this.basicSetInterfaceStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__INTRINSIC_TSPEC:
            return this.basicSetIntrinsicTSpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__IO_CONTROL:
            return this.basicSetIoControl(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__IO_CONTROL_SPEC:
            return this.basicSetIoControlSpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ITERATOR:
            return this.basicSetIterator(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ITERATOR_DEFINITION_LT:
            return this.basicSetIteratorDefinitionLT(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__ITERATOR_ELEMENT:
            return this.basicSetIteratorElement(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__KSELECTOR:
            return this.basicSetKSelector(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__KSPEC:
            return this.basicSetKSpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__LABEL:
            return this.basicSetLabel(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__LITERAL_E:
            return this.basicSetLiteralE(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__LOWER_BOUND:
            return this.basicSetLowerBound(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__MASK_E:
            return this.basicSetMaskE(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__MODULE_N:
            return this.basicSetModuleN(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__MODULE_PROCEDURE_NLT:
            return this.basicSetModuleProcedureNLT(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__MODULE_STMT:
            return this.basicSetModuleStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__N1:
            return this.basicSetN1(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__NAMED_E:
            return this.basicSetNamedE(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_N:
            return this.basicSetNamelistGroupN(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_OBJ:
            return this.basicSetNamelistGroupObj(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_OBJ_LT:
            return this.basicSetNamelistGroupObjLT(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_OBJ_N:
            return this.basicSetNamelistGroupObjN(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_STMT:
            return this.basicSetNamelistStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__NULLIFY_STMT:
            return this.basicSetNullifyStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__OBJECT:
            return this.basicSetObject(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__OP:
            return this.basicSetOp(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__OP_E:
            return this.basicSetOpE(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__OPEN_STMT:
            return this.basicSetOpenStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__OUTPUT_ITEM:
            return this.basicSetOutputItem(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__OUTPUT_ITEM_LT:
            return this.basicSetOutputItemLT(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__PARENS_E:
            return this.basicSetParensE(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__PARENS_R:
            return this.basicSetParensR(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__POINTER_ASTMT:
            return this.basicSetPointerAStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__POINTER_STMT:
            return this.basicSetPointerStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__PROCEDURE_DESIGNATOR:
            return this.basicSetProcedureDesignator(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__PROCEDURE_STMT:
            return this.basicSetProcedureStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__PROGRAM_N:
            return this.basicSetProgramN(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__PROGRAM_STMT:
            return this.basicSetProgramStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__PUBLIC_STMT:
            return this.basicSetPublicStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__RLT:
            return this.basicSetRLT(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__READ_STMT:
            return this.basicSetReadStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__RENAME:
            return this.basicSetRename(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__RENAME_LT:
            return this.basicSetRenameLT(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__RESULT_SPEC:
            return this.basicSetResultSpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__SECTION_SUBSCRIPT:
            return this.basicSetSectionSubscript(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__SECTION_SUBSCRIPT_LT:
            return this.basicSetSectionSubscriptLT(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__SELECT_CASE_STMT:
            return this.basicSetSelectCaseStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__SHAPE_SPEC:
            return this.basicSetShapeSpec(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__SHAPE_SPEC_LT:
            return this.basicSetShapeSpecLT(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__STOP_STMT:
            return this.basicSetStopStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__STRING_E:
            return this.basicSetStringE(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__SUBROUTINE_N:
            return this.basicSetSubroutineN(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__SUBROUTINE_STMT:
            return this.basicSetSubroutineStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__TDECL_STMT:
            return this.basicSetTDeclStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__TN:
            return this.basicSetTN(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__TSTMT:
            return this.basicSetTStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__TEST_E:
            return this.basicSetTestE(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__UPPER_BOUND:
            return this.basicSetUpperBound(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__USE_N:
            return this.basicSetUseN(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__USE_STMT:
            return this.basicSetUseStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__V:
            return this.basicSetV(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__VN:
            return this.basicSetVN(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__WHERE_CONSTRUCT_STMT:
            return this.basicSetWhereConstructStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__WHERE_STMT:
            return this.basicSetWhereStmt(null, msgs);
        case FxtranPackage.DOCUMENT_ROOT__WRITE_STMT:
            return this.basicSetWriteStmt(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
        switch (featureID) {
        case FxtranPackage.DOCUMENT_ROOT__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
            if (coreType) {
                return this.getXMLNSPrefixMap();
            } else {
                return this.getXMLNSPrefixMap().map();
            }
        case FxtranPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
            if (coreType) {
                return this.getXSISchemaLocation();
            } else {
                return this.getXSISchemaLocation().map();
            }
        case FxtranPackage.DOCUMENT_ROOT__TSPEC:
            return this.getTSpec();
        case FxtranPackage.DOCUMENT_ROOT__A:
            return this.getA();
        case FxtranPackage.DOCUMENT_ROOT__ASTMT:
            return this.getAStmt();
        case FxtranPackage.DOCUMENT_ROOT__AC_VALUE:
            return this.getAcValue();
        case FxtranPackage.DOCUMENT_ROOT__AC_VALUE_LT:
            return this.getAcValueLT();
        case FxtranPackage.DOCUMENT_ROOT__ACTION_STMT:
            return this.getActionStmt();
        case FxtranPackage.DOCUMENT_ROOT__ALLOCATE_STMT:
            return this.getAllocateStmt();
        case FxtranPackage.DOCUMENT_ROOT__ARG:
            return this.getArg();
        case FxtranPackage.DOCUMENT_ROOT__ARG_N:
            return this.getArgN();
        case FxtranPackage.DOCUMENT_ROOT__ARG_SPEC:
            return this.getArgSpec();
        case FxtranPackage.DOCUMENT_ROOT__ARRAY_CONSTRUCTOR_E:
            return this.getArrayConstructorE();
        case FxtranPackage.DOCUMENT_ROOT__ARRAY_R:
            return this.getArrayR();
        case FxtranPackage.DOCUMENT_ROOT__ARRAY_SPEC:
            return this.getArraySpec();
        case FxtranPackage.DOCUMENT_ROOT__ATTRIBUTE:
            return this.getAttribute();
        case FxtranPackage.DOCUMENT_ROOT__ATTRIBUTE_N:
            return this.getAttributeN();
        case FxtranPackage.DOCUMENT_ROOT__C:
            return this.getC();
        case FxtranPackage.DOCUMENT_ROOT__CALL_STMT:
            return this.getCallStmt();
        case FxtranPackage.DOCUMENT_ROOT__CASE_E:
            return this.getCaseE();
        case FxtranPackage.DOCUMENT_ROOT__CASE_SELECTOR:
            return this.getCaseSelector();
        case FxtranPackage.DOCUMENT_ROOT__CASE_STMT:
            return this.getCaseStmt();
        case FxtranPackage.DOCUMENT_ROOT__CASE_VALUE:
            return this.getCaseValue();
        case FxtranPackage.DOCUMENT_ROOT__CASE_VALUE_RANGE:
            return this.getCaseValueRange();
        case FxtranPackage.DOCUMENT_ROOT__CASE_VALUE_RANGE_LT:
            return this.getCaseValueRangeLT();
        case FxtranPackage.DOCUMENT_ROOT__CHAR_SELECTOR:
            return this.getCharSelector();
        case FxtranPackage.DOCUMENT_ROOT__CHAR_SPEC:
            return this.getCharSpec();
        case FxtranPackage.DOCUMENT_ROOT__CLOSE_SPEC:
            return this.getCloseSpec();
        case FxtranPackage.DOCUMENT_ROOT__CLOSE_SPEC_SPEC:
            return this.getCloseSpecSpec();
        case FxtranPackage.DOCUMENT_ROOT__CLOSE_STMT:
            return this.getCloseStmt();
        case FxtranPackage.DOCUMENT_ROOT__CNT:
            return this.getCnt();
        case FxtranPackage.DOCUMENT_ROOT__COMPONENT_DECL_STMT:
            return this.getComponentDeclStmt();
        case FxtranPackage.DOCUMENT_ROOT__COMPONENT_R:
            return this.getComponentR();
        case FxtranPackage.DOCUMENT_ROOT__CONDITION_E:
            return this.getConditionE();
        case FxtranPackage.DOCUMENT_ROOT__CONNECT_SPEC:
            return this.getConnectSpec();
        case FxtranPackage.DOCUMENT_ROOT__CONNECT_SPEC_SPEC:
            return this.getConnectSpecSpec();
        case FxtranPackage.DOCUMENT_ROOT__CONTAINS_STMT:
            return this.getContainsStmt();
        case FxtranPackage.DOCUMENT_ROOT__CPP:
            return this.getCpp();
        case FxtranPackage.DOCUMENT_ROOT__CT:
            return this.getCt();
        case FxtranPackage.DOCUMENT_ROOT__CYCLE_STMT:
            return this.getCycleStmt();
        case FxtranPackage.DOCUMENT_ROOT__DEALLOCATE_STMT:
            return this.getDeallocateStmt();
        case FxtranPackage.DOCUMENT_ROOT__DERIVED_TSPEC:
            return this.getDerivedTSpec();
        case FxtranPackage.DOCUMENT_ROOT__DO_STMT:
            return this.getDoStmt();
        case FxtranPackage.DOCUMENT_ROOT__DO_V:
            return this.getDoV();
        case FxtranPackage.DOCUMENT_ROOT__DUMMY_ARG_LT:
            return this.getDummyArgLT();
        case FxtranPackage.DOCUMENT_ROOT__E1:
            return this.getE1();
        case FxtranPackage.DOCUMENT_ROOT__E2:
            return this.getE2();
        case FxtranPackage.DOCUMENT_ROOT__ELEMENT:
            return this.getElement();
        case FxtranPackage.DOCUMENT_ROOT__ELEMENT_LT:
            return this.getElementLT();
        case FxtranPackage.DOCUMENT_ROOT__ELSE_IF_STMT:
            return this.getElseIfStmt();
        case FxtranPackage.DOCUMENT_ROOT__ELSE_STMT:
            return this.getElseStmt();
        case FxtranPackage.DOCUMENT_ROOT__ELSE_WHERE_STMT:
            return this.getElseWhereStmt();
        case FxtranPackage.DOCUMENT_ROOT__EN:
            return this.getEN();
        case FxtranPackage.DOCUMENT_ROOT__EN_DECL:
            return this.getENDecl();
        case FxtranPackage.DOCUMENT_ROOT__EN_DECL_LT:
            return this.getENDeclLT();
        case FxtranPackage.DOCUMENT_ROOT__ENLT:
            return this.getENLT();
        case FxtranPackage.DOCUMENT_ROOT__ENN:
            return this.getENN();
        case FxtranPackage.DOCUMENT_ROOT__END_DO_STMT:
            return this.getEndDoStmt();
        case FxtranPackage.DOCUMENT_ROOT__END_FORALL_STMT:
            return this.getEndForallStmt();
        case FxtranPackage.DOCUMENT_ROOT__END_FUNCTION_STMT:
            return this.getEndFunctionStmt();
        case FxtranPackage.DOCUMENT_ROOT__END_IF_STMT:
            return this.getEndIfStmt();
        case FxtranPackage.DOCUMENT_ROOT__END_INTERFACE_STMT:
            return this.getEndInterfaceStmt();
        case FxtranPackage.DOCUMENT_ROOT__END_MODULE_STMT:
            return this.getEndModuleStmt();
        case FxtranPackage.DOCUMENT_ROOT__END_PROGRAM_STMT:
            return this.getEndProgramStmt();
        case FxtranPackage.DOCUMENT_ROOT__END_SELECT_CASE_STMT:
            return this.getEndSelectCaseStmt();
        case FxtranPackage.DOCUMENT_ROOT__END_SUBROUTINE_STMT:
            return this.getEndSubroutineStmt();
        case FxtranPackage.DOCUMENT_ROOT__END_TSTMT:
            return this.getEndTStmt();
        case FxtranPackage.DOCUMENT_ROOT__END_WHERE_STMT:
            return this.getEndWhereStmt();
        case FxtranPackage.DOCUMENT_ROOT__ERROR:
            return this.getError();
        case FxtranPackage.DOCUMENT_ROOT__EXIT_STMT:
            return this.getExitStmt();
        case FxtranPackage.DOCUMENT_ROOT__FILE:
            return this.getFile();
        case FxtranPackage.DOCUMENT_ROOT__FORALL_CONSTRUCT_STMT:
            return this.getForallConstructStmt();
        case FxtranPackage.DOCUMENT_ROOT__FORALL_STMT:
            return this.getForallStmt();
        case FxtranPackage.DOCUMENT_ROOT__FORALL_TRIPLET_SPEC:
            return this.getForallTripletSpec();
        case FxtranPackage.DOCUMENT_ROOT__FORALL_TRIPLET_SPEC_LT:
            return this.getForallTripletSpecLT();
        case FxtranPackage.DOCUMENT_ROOT__FUNCTION_N:
            return this.getFunctionN();
        case FxtranPackage.DOCUMENT_ROOT__FUNCTION_STMT:
            return this.getFunctionStmt();
        case FxtranPackage.DOCUMENT_ROOT__IF_STMT:
            return this.getIfStmt();
        case FxtranPackage.DOCUMENT_ROOT__IF_THEN_STMT:
            return this.getIfThenStmt();
        case FxtranPackage.DOCUMENT_ROOT__IMPLICIT_NONE_STMT:
            return this.getImplicitNoneStmt();
        case FxtranPackage.DOCUMENT_ROOT__INIT_E:
            return this.getInitE();
        case FxtranPackage.DOCUMENT_ROOT__INQUIRE_STMT:
            return this.getInquireStmt();
        case FxtranPackage.DOCUMENT_ROOT__INQUIRY_SPEC:
            return this.getInquirySpec();
        case FxtranPackage.DOCUMENT_ROOT__INQUIRY_SPEC_SPEC:
            return this.getInquirySpecSpec();
        case FxtranPackage.DOCUMENT_ROOT__INTENT_SPEC:
            return this.getIntentSpec();
        case FxtranPackage.DOCUMENT_ROOT__INTERFACE_STMT:
            return this.getInterfaceStmt();
        case FxtranPackage.DOCUMENT_ROOT__INTRINSIC_TSPEC:
            return this.getIntrinsicTSpec();
        case FxtranPackage.DOCUMENT_ROOT__IO_CONTROL:
            return this.getIoControl();
        case FxtranPackage.DOCUMENT_ROOT__IO_CONTROL_SPEC:
            return this.getIoControlSpec();
        case FxtranPackage.DOCUMENT_ROOT__ITERATOR:
            return this.getIterator();
        case FxtranPackage.DOCUMENT_ROOT__ITERATOR_DEFINITION_LT:
            return this.getIteratorDefinitionLT();
        case FxtranPackage.DOCUMENT_ROOT__ITERATOR_ELEMENT:
            return this.getIteratorElement();
        case FxtranPackage.DOCUMENT_ROOT__K:
            return this.getK();
        case FxtranPackage.DOCUMENT_ROOT__KSELECTOR:
            return this.getKSelector();
        case FxtranPackage.DOCUMENT_ROOT__KSPEC:
            return this.getKSpec();
        case FxtranPackage.DOCUMENT_ROOT__L:
            return this.getL();
        case FxtranPackage.DOCUMENT_ROOT__LABEL:
            return this.getLabel();
        case FxtranPackage.DOCUMENT_ROOT__LITERAL_E:
            return this.getLiteralE();
        case FxtranPackage.DOCUMENT_ROOT__LOWER_BOUND:
            return this.getLowerBound();
        case FxtranPackage.DOCUMENT_ROOT__MASK_E:
            return this.getMaskE();
        case FxtranPackage.DOCUMENT_ROOT__MODULE_N:
            return this.getModuleN();
        case FxtranPackage.DOCUMENT_ROOT__MODULE_PROCEDURE_NLT:
            return this.getModuleProcedureNLT();
        case FxtranPackage.DOCUMENT_ROOT__MODULE_STMT:
            return this.getModuleStmt();
        case FxtranPackage.DOCUMENT_ROOT__N:
            return this.getN();
        case FxtranPackage.DOCUMENT_ROOT__N1:
            return this.getN1();
        case FxtranPackage.DOCUMENT_ROOT__NAMED_E:
            return this.getNamedE();
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_N:
            return this.getNamelistGroupN();
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_OBJ:
            return this.getNamelistGroupObj();
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_OBJ_LT:
            return this.getNamelistGroupObjLT();
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_OBJ_N:
            return this.getNamelistGroupObjN();
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_STMT:
            return this.getNamelistStmt();
        case FxtranPackage.DOCUMENT_ROOT__NULLIFY_STMT:
            return this.getNullifyStmt();
        case FxtranPackage.DOCUMENT_ROOT__O:
            return this.getO();
        case FxtranPackage.DOCUMENT_ROOT__OBJECT:
            return this.getObject();
        case FxtranPackage.DOCUMENT_ROOT__OP:
            return this.getOp();
        case FxtranPackage.DOCUMENT_ROOT__OP_E:
            return this.getOpE();
        case FxtranPackage.DOCUMENT_ROOT__OPEN_STMT:
            return this.getOpenStmt();
        case FxtranPackage.DOCUMENT_ROOT__OUTPUT_ITEM:
            return this.getOutputItem();
        case FxtranPackage.DOCUMENT_ROOT__OUTPUT_ITEM_LT:
            return this.getOutputItemLT();
        case FxtranPackage.DOCUMENT_ROOT__PARENS_E:
            return this.getParensE();
        case FxtranPackage.DOCUMENT_ROOT__PARENS_R:
            return this.getParensR();
        case FxtranPackage.DOCUMENT_ROOT__POINTER_ASTMT:
            return this.getPointerAStmt();
        case FxtranPackage.DOCUMENT_ROOT__POINTER_STMT:
            return this.getPointerStmt();
        case FxtranPackage.DOCUMENT_ROOT__PREFIX:
            return this.getPrefix();
        case FxtranPackage.DOCUMENT_ROOT__PRIVATE_STMT:
            return this.getPrivateStmt();
        case FxtranPackage.DOCUMENT_ROOT__PROCEDURE_DESIGNATOR:
            return this.getProcedureDesignator();
        case FxtranPackage.DOCUMENT_ROOT__PROCEDURE_STMT:
            return this.getProcedureStmt();
        case FxtranPackage.DOCUMENT_ROOT__PROGRAM_N:
            return this.getProgramN();
        case FxtranPackage.DOCUMENT_ROOT__PROGRAM_STMT:
            return this.getProgramStmt();
        case FxtranPackage.DOCUMENT_ROOT__PUBLIC_STMT:
            return this.getPublicStmt();
        case FxtranPackage.DOCUMENT_ROOT__RLT:
            return this.getRLT();
        case FxtranPackage.DOCUMENT_ROOT__READ_STMT:
            return this.getReadStmt();
        case FxtranPackage.DOCUMENT_ROOT__RENAME:
            return this.getRename();
        case FxtranPackage.DOCUMENT_ROOT__RENAME_LT:
            return this.getRenameLT();
        case FxtranPackage.DOCUMENT_ROOT__RESULT_SPEC:
            return this.getResultSpec();
        case FxtranPackage.DOCUMENT_ROOT__RETURN_STMT:
            return this.getReturnStmt();
        case FxtranPackage.DOCUMENT_ROOT__S:
            return this.getS();
        case FxtranPackage.DOCUMENT_ROOT__SAVE_STMT:
            return this.getSaveStmt();
        case FxtranPackage.DOCUMENT_ROOT__SECTION_SUBSCRIPT:
            return this.getSectionSubscript();
        case FxtranPackage.DOCUMENT_ROOT__SECTION_SUBSCRIPT_LT:
            return this.getSectionSubscriptLT();
        case FxtranPackage.DOCUMENT_ROOT__SELECT_CASE_STMT:
            return this.getSelectCaseStmt();
        case FxtranPackage.DOCUMENT_ROOT__SHAPE_SPEC:
            return this.getShapeSpec();
        case FxtranPackage.DOCUMENT_ROOT__SHAPE_SPEC_LT:
            return this.getShapeSpecLT();
        case FxtranPackage.DOCUMENT_ROOT__STAR_E:
            return this.getStarE();
        case FxtranPackage.DOCUMENT_ROOT__STOP_CODE:
            return this.getStopCode();
        case FxtranPackage.DOCUMENT_ROOT__STOP_STMT:
            return this.getStopStmt();
        case FxtranPackage.DOCUMENT_ROOT__STRING_E:
            return this.getStringE();
        case FxtranPackage.DOCUMENT_ROOT__SUBROUTINE_N:
            return this.getSubroutineN();
        case FxtranPackage.DOCUMENT_ROOT__SUBROUTINE_STMT:
            return this.getSubroutineStmt();
        case FxtranPackage.DOCUMENT_ROOT__TDECL_STMT:
            return this.getTDeclStmt();
        case FxtranPackage.DOCUMENT_ROOT__TN:
            return this.getTN();
        case FxtranPackage.DOCUMENT_ROOT__TSTMT:
            return this.getTStmt();
        case FxtranPackage.DOCUMENT_ROOT__TEST_E:
            return this.getTestE();
        case FxtranPackage.DOCUMENT_ROOT__UPPER_BOUND:
            return this.getUpperBound();
        case FxtranPackage.DOCUMENT_ROOT__USE_N:
            return this.getUseN();
        case FxtranPackage.DOCUMENT_ROOT__USE_STMT:
            return this.getUseStmt();
        case FxtranPackage.DOCUMENT_ROOT__V:
            return this.getV();
        case FxtranPackage.DOCUMENT_ROOT__VN:
            return this.getVN();
        case FxtranPackage.DOCUMENT_ROOT__WHERE_CONSTRUCT_STMT:
            return this.getWhereConstructStmt();
        case FxtranPackage.DOCUMENT_ROOT__WHERE_STMT:
            return this.getWhereStmt();
        case FxtranPackage.DOCUMENT_ROOT__WRITE_STMT:
            return this.getWriteStmt();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eSet(final int featureID, final Object newValue) {
        switch (featureID) {
        case FxtranPackage.DOCUMENT_ROOT__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
            ((EStructuralFeature.Setting) this.getXMLNSPrefixMap()).set(newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
            ((EStructuralFeature.Setting) this.getXSISchemaLocation()).set(newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__TSPEC:
            this.setTSpec((TSpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__A:
            this.setA((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ASTMT:
            this.setAStmt((AStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__AC_VALUE:
            this.setAcValue((AcValueType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__AC_VALUE_LT:
            this.setAcValueLT((AcValueLTType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ACTION_STMT:
            this.setActionStmt((ActionStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ALLOCATE_STMT:
            this.setAllocateStmt((AllocateStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ARG:
            this.setArg((ArgType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ARG_N:
            this.setArgN((ArgNType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ARG_SPEC:
            this.setArgSpec((ArgSpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ARRAY_CONSTRUCTOR_E:
            this.setArrayConstructorE((ArrayConstructorEType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ARRAY_R:
            this.setArrayR((ArrayRType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ARRAY_SPEC:
            this.setArraySpec((ArraySpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ATTRIBUTE:
            this.setAttribute((AttributeType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ATTRIBUTE_N:
            this.setAttributeN((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__C:
            this.setC((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CALL_STMT:
            this.setCallStmt((CallStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CASE_E:
            this.setCaseE((CaseEType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CASE_SELECTOR:
            this.setCaseSelector((CaseSelectorType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CASE_STMT:
            this.setCaseStmt((CaseStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CASE_VALUE:
            this.setCaseValue((CaseValueType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CASE_VALUE_RANGE:
            this.setCaseValueRange((CaseValueRangeType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CASE_VALUE_RANGE_LT:
            this.setCaseValueRangeLT((CaseValueRangeLTType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CHAR_SELECTOR:
            this.setCharSelector((CharSelectorType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CHAR_SPEC:
            this.setCharSpec((CharSpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CLOSE_SPEC:
            this.setCloseSpec((CloseSpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CLOSE_SPEC_SPEC:
            this.setCloseSpecSpec((CloseSpecSpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CLOSE_STMT:
            this.setCloseStmt((CloseStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CNT:
            this.setCnt((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__COMPONENT_DECL_STMT:
            this.setComponentDeclStmt((ComponentDeclStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__COMPONENT_R:
            this.setComponentR((ComponentRType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CONDITION_E:
            this.setConditionE((ConditionEType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CONNECT_SPEC:
            this.setConnectSpec((ConnectSpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CONNECT_SPEC_SPEC:
            this.setConnectSpecSpec((ConnectSpecSpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CONTAINS_STMT:
            this.setContainsStmt((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CPP:
            this.setCpp((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CT:
            this.setCt((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CYCLE_STMT:
            this.setCycleStmt((CycleStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__DEALLOCATE_STMT:
            this.setDeallocateStmt((DeallocateStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__DERIVED_TSPEC:
            this.setDerivedTSpec((DerivedTSpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__DO_STMT:
            this.setDoStmt((DoStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__DO_V:
            this.setDoV((DoVType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__DUMMY_ARG_LT:
            this.setDummyArgLT((DummyArgLTType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__E1:
            this.setE1((E1Type) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__E2:
            this.setE2((E2Type) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ELEMENT:
            this.setElement((ElementType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ELEMENT_LT:
            this.setElementLT((ElementLTType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ELSE_IF_STMT:
            this.setElseIfStmt((ElseIfStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ELSE_STMT:
            this.setElseStmt((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ELSE_WHERE_STMT:
            this.setElseWhereStmt((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__EN:
            this.setEN((ENType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__EN_DECL:
            this.setENDecl((ENDeclType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__EN_DECL_LT:
            this.setENDeclLT((ENDeclLTType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ENLT:
            this.setENLT((ENLTType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ENN:
            this.setENN((ENNType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_DO_STMT:
            this.setEndDoStmt((EndDoStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_FORALL_STMT:
            this.setEndForallStmt((EndForallStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_FUNCTION_STMT:
            this.setEndFunctionStmt((EndFunctionStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_IF_STMT:
            this.setEndIfStmt((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_INTERFACE_STMT:
            this.setEndInterfaceStmt((EndInterfaceStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_MODULE_STMT:
            this.setEndModuleStmt((EndModuleStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_PROGRAM_STMT:
            this.setEndProgramStmt((EndProgramStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_SELECT_CASE_STMT:
            this.setEndSelectCaseStmt((EndSelectCaseStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_SUBROUTINE_STMT:
            this.setEndSubroutineStmt((EndSubroutineStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_TSTMT:
            this.setEndTStmt((EndTStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_WHERE_STMT:
            this.setEndWhereStmt((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ERROR:
            this.setError((ErrorType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__EXIT_STMT:
            this.setExitStmt((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__FILE:
            this.setFile((FileType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__FORALL_CONSTRUCT_STMT:
            this.setForallConstructStmt((ForallConstructStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__FORALL_STMT:
            this.setForallStmt((ForallStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__FORALL_TRIPLET_SPEC:
            this.setForallTripletSpec((ForallTripletSpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__FORALL_TRIPLET_SPEC_LT:
            this.setForallTripletSpecLT((ForallTripletSpecLTType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__FUNCTION_N:
            this.setFunctionN((FunctionNType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__FUNCTION_STMT:
            this.setFunctionStmt((FunctionStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__IF_STMT:
            this.setIfStmt((IfStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__IF_THEN_STMT:
            this.setIfThenStmt((IfThenStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__IMPLICIT_NONE_STMT:
            this.setImplicitNoneStmt((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__INIT_E:
            this.setInitE((InitEType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__INQUIRE_STMT:
            this.setInquireStmt((InquireStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__INQUIRY_SPEC:
            this.setInquirySpec((InquirySpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__INQUIRY_SPEC_SPEC:
            this.setInquirySpecSpec((InquirySpecSpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__INTENT_SPEC:
            this.setIntentSpec((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__INTERFACE_STMT:
            this.setInterfaceStmt((InterfaceStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__INTRINSIC_TSPEC:
            this.setIntrinsicTSpec((IntrinsicTSpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__IO_CONTROL:
            this.setIoControl((IoControlType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__IO_CONTROL_SPEC:
            this.setIoControlSpec((IoControlSpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ITERATOR:
            this.setIterator((IteratorType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ITERATOR_DEFINITION_LT:
            this.setIteratorDefinitionLT((IteratorDefinitionLTType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ITERATOR_ELEMENT:
            this.setIteratorElement((IteratorElementType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__K:
            this.setK((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__KSELECTOR:
            this.setKSelector((KSelectorType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__KSPEC:
            this.setKSpec((KSpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__L:
            this.setL((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__LABEL:
            this.setLabel((LabelType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__LITERAL_E:
            this.setLiteralE((LiteralEType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__LOWER_BOUND:
            this.setLowerBound((LowerBoundType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__MASK_E:
            this.setMaskE((MaskEType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__MODULE_N:
            this.setModuleN((ModuleNType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__MODULE_PROCEDURE_NLT:
            this.setModuleProcedureNLT((ModuleProcedureNLTType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__MODULE_STMT:
            this.setModuleStmt((ModuleStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__N:
            this.setN((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__N1:
            this.setN1((NType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__NAMED_E:
            this.setNamedE((NamedEType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_N:
            this.setNamelistGroupN((NamelistGroupNType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_OBJ:
            this.setNamelistGroupObj((NamelistGroupObjType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_OBJ_LT:
            this.setNamelistGroupObjLT((NamelistGroupObjLTType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_OBJ_N:
            this.setNamelistGroupObjN((NamelistGroupObjNType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_STMT:
            this.setNamelistStmt((NamelistStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__NULLIFY_STMT:
            this.setNullifyStmt((NullifyStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__O:
            this.setO((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__OBJECT:
            this.setObject((ObjectType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__OP:
            this.setOp((OpType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__OP_E:
            this.setOpE((OpEType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__OPEN_STMT:
            this.setOpenStmt((OpenStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__OUTPUT_ITEM:
            this.setOutputItem((OutputItemType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__OUTPUT_ITEM_LT:
            this.setOutputItemLT((OutputItemLTType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PARENS_E:
            this.setParensE((ParensEType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PARENS_R:
            this.setParensR((ParensRType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__POINTER_ASTMT:
            this.setPointerAStmt((PointerAStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__POINTER_STMT:
            this.setPointerStmt((PointerStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PREFIX:
            this.setPrefix((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PRIVATE_STMT:
            this.setPrivateStmt((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PROCEDURE_DESIGNATOR:
            this.setProcedureDesignator((ProcedureDesignatorType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PROCEDURE_STMT:
            this.setProcedureStmt((ProcedureStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PROGRAM_N:
            this.setProgramN((ProgramNType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PROGRAM_STMT:
            this.setProgramStmt((ProgramStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PUBLIC_STMT:
            this.setPublicStmt((PublicStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__RLT:
            this.setRLT((RLTType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__READ_STMT:
            this.setReadStmt((ReadStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__RENAME:
            this.setRename((RenameType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__RENAME_LT:
            this.setRenameLT((RenameLTType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__RESULT_SPEC:
            this.setResultSpec((ResultSpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__RETURN_STMT:
            this.setReturnStmt((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__S:
            this.setS((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__SAVE_STMT:
            this.setSaveStmt((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__SECTION_SUBSCRIPT:
            this.setSectionSubscript((SectionSubscriptType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__SECTION_SUBSCRIPT_LT:
            this.setSectionSubscriptLT((SectionSubscriptLTType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__SELECT_CASE_STMT:
            this.setSelectCaseStmt((SelectCaseStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__SHAPE_SPEC:
            this.setShapeSpec((ShapeSpecType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__SHAPE_SPEC_LT:
            this.setShapeSpecLT((ShapeSpecLTType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__STAR_E:
            this.setStarE((String) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__STOP_CODE:
            this.setStopCode((BigInteger) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__STOP_STMT:
            this.setStopStmt((StopStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__STRING_E:
            this.setStringE((StringEType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__SUBROUTINE_N:
            this.setSubroutineN((SubroutineNType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__SUBROUTINE_STMT:
            this.setSubroutineStmt((SubroutineStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__TDECL_STMT:
            this.setTDeclStmt((TDeclStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__TN:
            this.setTN((TNType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__TSTMT:
            this.setTStmt((TStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__TEST_E:
            this.setTestE((TestEType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__UPPER_BOUND:
            this.setUpperBound((UpperBoundType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__USE_N:
            this.setUseN((UseNType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__USE_STMT:
            this.setUseStmt((UseStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__V:
            this.setV((VType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__VN:
            this.setVN((VNType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__WHERE_CONSTRUCT_STMT:
            this.setWhereConstructStmt((WhereConstructStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__WHERE_STMT:
            this.setWhereStmt((WhereStmtType) newValue);
            return;
        case FxtranPackage.DOCUMENT_ROOT__WRITE_STMT:
            this.setWriteStmt((WriteStmtType) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eUnset(final int featureID) {
        switch (featureID) {
        case FxtranPackage.DOCUMENT_ROOT__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
            this.getXMLNSPrefixMap().clear();
            return;
        case FxtranPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
            this.getXSISchemaLocation().clear();
            return;
        case FxtranPackage.DOCUMENT_ROOT__TSPEC:
            this.setTSpec((TSpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__A:
            this.setA(A_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ASTMT:
            this.setAStmt((AStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__AC_VALUE:
            this.setAcValue((AcValueType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__AC_VALUE_LT:
            this.setAcValueLT((AcValueLTType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ACTION_STMT:
            this.setActionStmt((ActionStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ALLOCATE_STMT:
            this.setAllocateStmt((AllocateStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ARG:
            this.setArg((ArgType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ARG_N:
            this.setArgN((ArgNType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ARG_SPEC:
            this.setArgSpec((ArgSpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ARRAY_CONSTRUCTOR_E:
            this.setArrayConstructorE((ArrayConstructorEType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ARRAY_R:
            this.setArrayR((ArrayRType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ARRAY_SPEC:
            this.setArraySpec((ArraySpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ATTRIBUTE:
            this.setAttribute((AttributeType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ATTRIBUTE_N:
            this.setAttributeN(ATTRIBUTE_N_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__C:
            this.setC(C_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CALL_STMT:
            this.setCallStmt((CallStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CASE_E:
            this.setCaseE((CaseEType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CASE_SELECTOR:
            this.setCaseSelector((CaseSelectorType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CASE_STMT:
            this.setCaseStmt((CaseStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CASE_VALUE:
            this.setCaseValue((CaseValueType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CASE_VALUE_RANGE:
            this.setCaseValueRange((CaseValueRangeType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CASE_VALUE_RANGE_LT:
            this.setCaseValueRangeLT((CaseValueRangeLTType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CHAR_SELECTOR:
            this.setCharSelector((CharSelectorType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CHAR_SPEC:
            this.setCharSpec((CharSpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CLOSE_SPEC:
            this.setCloseSpec((CloseSpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CLOSE_SPEC_SPEC:
            this.setCloseSpecSpec((CloseSpecSpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CLOSE_STMT:
            this.setCloseStmt((CloseStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CNT:
            this.setCnt(CNT_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__COMPONENT_DECL_STMT:
            this.setComponentDeclStmt((ComponentDeclStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__COMPONENT_R:
            this.setComponentR((ComponentRType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CONDITION_E:
            this.setConditionE((ConditionEType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CONNECT_SPEC:
            this.setConnectSpec((ConnectSpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CONNECT_SPEC_SPEC:
            this.setConnectSpecSpec((ConnectSpecSpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CONTAINS_STMT:
            this.setContainsStmt(CONTAINS_STMT_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CPP:
            this.setCpp(CPP_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CT:
            this.setCt(CT_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__CYCLE_STMT:
            this.setCycleStmt((CycleStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__DEALLOCATE_STMT:
            this.setDeallocateStmt((DeallocateStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__DERIVED_TSPEC:
            this.setDerivedTSpec((DerivedTSpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__DO_STMT:
            this.setDoStmt((DoStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__DO_V:
            this.setDoV((DoVType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__DUMMY_ARG_LT:
            this.setDummyArgLT((DummyArgLTType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__E1:
            this.setE1((E1Type) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__E2:
            this.setE2((E2Type) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ELEMENT:
            this.setElement((ElementType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ELEMENT_LT:
            this.setElementLT((ElementLTType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ELSE_IF_STMT:
            this.setElseIfStmt((ElseIfStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ELSE_STMT:
            this.setElseStmt(ELSE_STMT_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ELSE_WHERE_STMT:
            this.setElseWhereStmt(ELSE_WHERE_STMT_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__EN:
            this.setEN((ENType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__EN_DECL:
            this.setENDecl((ENDeclType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__EN_DECL_LT:
            this.setENDeclLT((ENDeclLTType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ENLT:
            this.setENLT((ENLTType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ENN:
            this.setENN((ENNType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_DO_STMT:
            this.setEndDoStmt((EndDoStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_FORALL_STMT:
            this.setEndForallStmt((EndForallStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_FUNCTION_STMT:
            this.setEndFunctionStmt((EndFunctionStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_IF_STMT:
            this.setEndIfStmt(END_IF_STMT_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_INTERFACE_STMT:
            this.setEndInterfaceStmt((EndInterfaceStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_MODULE_STMT:
            this.setEndModuleStmt((EndModuleStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_PROGRAM_STMT:
            this.setEndProgramStmt((EndProgramStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_SELECT_CASE_STMT:
            this.setEndSelectCaseStmt((EndSelectCaseStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_SUBROUTINE_STMT:
            this.setEndSubroutineStmt((EndSubroutineStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_TSTMT:
            this.setEndTStmt((EndTStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__END_WHERE_STMT:
            this.setEndWhereStmt(END_WHERE_STMT_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ERROR:
            this.setError((ErrorType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__EXIT_STMT:
            this.setExitStmt(EXIT_STMT_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__FILE:
            this.setFile((FileType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__FORALL_CONSTRUCT_STMT:
            this.setForallConstructStmt((ForallConstructStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__FORALL_STMT:
            this.setForallStmt((ForallStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__FORALL_TRIPLET_SPEC:
            this.setForallTripletSpec((ForallTripletSpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__FORALL_TRIPLET_SPEC_LT:
            this.setForallTripletSpecLT((ForallTripletSpecLTType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__FUNCTION_N:
            this.setFunctionN((FunctionNType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__FUNCTION_STMT:
            this.setFunctionStmt((FunctionStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__IF_STMT:
            this.setIfStmt((IfStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__IF_THEN_STMT:
            this.setIfThenStmt((IfThenStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__IMPLICIT_NONE_STMT:
            this.setImplicitNoneStmt(IMPLICIT_NONE_STMT_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__INIT_E:
            this.setInitE((InitEType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__INQUIRE_STMT:
            this.setInquireStmt((InquireStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__INQUIRY_SPEC:
            this.setInquirySpec((InquirySpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__INQUIRY_SPEC_SPEC:
            this.setInquirySpecSpec((InquirySpecSpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__INTENT_SPEC:
            this.setIntentSpec(INTENT_SPEC_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__INTERFACE_STMT:
            this.setInterfaceStmt((InterfaceStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__INTRINSIC_TSPEC:
            this.setIntrinsicTSpec((IntrinsicTSpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__IO_CONTROL:
            this.setIoControl((IoControlType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__IO_CONTROL_SPEC:
            this.setIoControlSpec((IoControlSpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ITERATOR:
            this.setIterator((IteratorType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ITERATOR_DEFINITION_LT:
            this.setIteratorDefinitionLT((IteratorDefinitionLTType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__ITERATOR_ELEMENT:
            this.setIteratorElement((IteratorElementType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__K:
            this.setK(K_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__KSELECTOR:
            this.setKSelector((KSelectorType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__KSPEC:
            this.setKSpec((KSpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__L:
            this.setL(L_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__LABEL:
            this.setLabel((LabelType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__LITERAL_E:
            this.setLiteralE((LiteralEType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__LOWER_BOUND:
            this.setLowerBound((LowerBoundType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__MASK_E:
            this.setMaskE((MaskEType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__MODULE_N:
            this.setModuleN((ModuleNType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__MODULE_PROCEDURE_NLT:
            this.setModuleProcedureNLT((ModuleProcedureNLTType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__MODULE_STMT:
            this.setModuleStmt((ModuleStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__N:
            this.setN(N_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__N1:
            this.setN1((NType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__NAMED_E:
            this.setNamedE((NamedEType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_N:
            this.setNamelistGroupN((NamelistGroupNType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_OBJ:
            this.setNamelistGroupObj((NamelistGroupObjType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_OBJ_LT:
            this.setNamelistGroupObjLT((NamelistGroupObjLTType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_OBJ_N:
            this.setNamelistGroupObjN((NamelistGroupObjNType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_STMT:
            this.setNamelistStmt((NamelistStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__NULLIFY_STMT:
            this.setNullifyStmt((NullifyStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__O:
            this.setO(O_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__OBJECT:
            this.setObject((ObjectType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__OP:
            this.setOp((OpType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__OP_E:
            this.setOpE((OpEType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__OPEN_STMT:
            this.setOpenStmt((OpenStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__OUTPUT_ITEM:
            this.setOutputItem((OutputItemType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__OUTPUT_ITEM_LT:
            this.setOutputItemLT((OutputItemLTType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PARENS_E:
            this.setParensE((ParensEType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PARENS_R:
            this.setParensR((ParensRType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__POINTER_ASTMT:
            this.setPointerAStmt((PointerAStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__POINTER_STMT:
            this.setPointerStmt((PointerStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PREFIX:
            this.setPrefix(PREFIX_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PRIVATE_STMT:
            this.setPrivateStmt(PRIVATE_STMT_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PROCEDURE_DESIGNATOR:
            this.setProcedureDesignator((ProcedureDesignatorType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PROCEDURE_STMT:
            this.setProcedureStmt((ProcedureStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PROGRAM_N:
            this.setProgramN((ProgramNType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PROGRAM_STMT:
            this.setProgramStmt((ProgramStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__PUBLIC_STMT:
            this.setPublicStmt((PublicStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__RLT:
            this.setRLT((RLTType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__READ_STMT:
            this.setReadStmt((ReadStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__RENAME:
            this.setRename((RenameType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__RENAME_LT:
            this.setRenameLT((RenameLTType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__RESULT_SPEC:
            this.setResultSpec((ResultSpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__RETURN_STMT:
            this.setReturnStmt(RETURN_STMT_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__S:
            this.setS(S_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__SAVE_STMT:
            this.setSaveStmt(SAVE_STMT_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__SECTION_SUBSCRIPT:
            this.setSectionSubscript((SectionSubscriptType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__SECTION_SUBSCRIPT_LT:
            this.setSectionSubscriptLT((SectionSubscriptLTType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__SELECT_CASE_STMT:
            this.setSelectCaseStmt((SelectCaseStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__SHAPE_SPEC:
            this.setShapeSpec((ShapeSpecType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__SHAPE_SPEC_LT:
            this.setShapeSpecLT((ShapeSpecLTType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__STAR_E:
            this.setStarE(STAR_E_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__STOP_CODE:
            this.setStopCode(STOP_CODE_EDEFAULT);
            return;
        case FxtranPackage.DOCUMENT_ROOT__STOP_STMT:
            this.setStopStmt((StopStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__STRING_E:
            this.setStringE((StringEType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__SUBROUTINE_N:
            this.setSubroutineN((SubroutineNType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__SUBROUTINE_STMT:
            this.setSubroutineStmt((SubroutineStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__TDECL_STMT:
            this.setTDeclStmt((TDeclStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__TN:
            this.setTN((TNType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__TSTMT:
            this.setTStmt((TStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__TEST_E:
            this.setTestE((TestEType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__UPPER_BOUND:
            this.setUpperBound((UpperBoundType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__USE_N:
            this.setUseN((UseNType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__USE_STMT:
            this.setUseStmt((UseStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__V:
            this.setV((VType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__VN:
            this.setVN((VNType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__WHERE_CONSTRUCT_STMT:
            this.setWhereConstructStmt((WhereConstructStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__WHERE_STMT:
            this.setWhereStmt((WhereStmtType) null);
            return;
        case FxtranPackage.DOCUMENT_ROOT__WRITE_STMT:
            this.setWriteStmt((WriteStmtType) null);
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(final int featureID) {
        switch (featureID) {
        case FxtranPackage.DOCUMENT_ROOT__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
            return this.xMLNSPrefixMap != null && !this.xMLNSPrefixMap.isEmpty();
        case FxtranPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
            return this.xSISchemaLocation != null && !this.xSISchemaLocation.isEmpty();
        case FxtranPackage.DOCUMENT_ROOT__TSPEC:
            return this.getTSpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__A:
            return A_EDEFAULT == null ? this.getA() != null : !A_EDEFAULT.equals(this.getA());
        case FxtranPackage.DOCUMENT_ROOT__ASTMT:
            return this.getAStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__AC_VALUE:
            return this.getAcValue() != null;
        case FxtranPackage.DOCUMENT_ROOT__AC_VALUE_LT:
            return this.getAcValueLT() != null;
        case FxtranPackage.DOCUMENT_ROOT__ACTION_STMT:
            return this.getActionStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__ALLOCATE_STMT:
            return this.getAllocateStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__ARG:
            return this.getArg() != null;
        case FxtranPackage.DOCUMENT_ROOT__ARG_N:
            return this.getArgN() != null;
        case FxtranPackage.DOCUMENT_ROOT__ARG_SPEC:
            return this.getArgSpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__ARRAY_CONSTRUCTOR_E:
            return this.getArrayConstructorE() != null;
        case FxtranPackage.DOCUMENT_ROOT__ARRAY_R:
            return this.getArrayR() != null;
        case FxtranPackage.DOCUMENT_ROOT__ARRAY_SPEC:
            return this.getArraySpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__ATTRIBUTE:
            return this.getAttribute() != null;
        case FxtranPackage.DOCUMENT_ROOT__ATTRIBUTE_N:
            return ATTRIBUTE_N_EDEFAULT == null ? this.getAttributeN() != null
                    : !ATTRIBUTE_N_EDEFAULT.equals(this.getAttributeN());
        case FxtranPackage.DOCUMENT_ROOT__C:
            return C_EDEFAULT == null ? this.getC() != null : !C_EDEFAULT.equals(this.getC());
        case FxtranPackage.DOCUMENT_ROOT__CALL_STMT:
            return this.getCallStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__CASE_E:
            return this.getCaseE() != null;
        case FxtranPackage.DOCUMENT_ROOT__CASE_SELECTOR:
            return this.getCaseSelector() != null;
        case FxtranPackage.DOCUMENT_ROOT__CASE_STMT:
            return this.getCaseStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__CASE_VALUE:
            return this.getCaseValue() != null;
        case FxtranPackage.DOCUMENT_ROOT__CASE_VALUE_RANGE:
            return this.getCaseValueRange() != null;
        case FxtranPackage.DOCUMENT_ROOT__CASE_VALUE_RANGE_LT:
            return this.getCaseValueRangeLT() != null;
        case FxtranPackage.DOCUMENT_ROOT__CHAR_SELECTOR:
            return this.getCharSelector() != null;
        case FxtranPackage.DOCUMENT_ROOT__CHAR_SPEC:
            return this.getCharSpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__CLOSE_SPEC:
            return this.getCloseSpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__CLOSE_SPEC_SPEC:
            return this.getCloseSpecSpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__CLOSE_STMT:
            return this.getCloseStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__CNT:
            return CNT_EDEFAULT == null ? this.getCnt() != null : !CNT_EDEFAULT.equals(this.getCnt());
        case FxtranPackage.DOCUMENT_ROOT__COMPONENT_DECL_STMT:
            return this.getComponentDeclStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__COMPONENT_R:
            return this.getComponentR() != null;
        case FxtranPackage.DOCUMENT_ROOT__CONDITION_E:
            return this.getConditionE() != null;
        case FxtranPackage.DOCUMENT_ROOT__CONNECT_SPEC:
            return this.getConnectSpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__CONNECT_SPEC_SPEC:
            return this.getConnectSpecSpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__CONTAINS_STMT:
            return CONTAINS_STMT_EDEFAULT == null ? this.getContainsStmt() != null
                    : !CONTAINS_STMT_EDEFAULT.equals(this.getContainsStmt());
        case FxtranPackage.DOCUMENT_ROOT__CPP:
            return CPP_EDEFAULT == null ? this.getCpp() != null : !CPP_EDEFAULT.equals(this.getCpp());
        case FxtranPackage.DOCUMENT_ROOT__CT:
            return CT_EDEFAULT == null ? this.getCt() != null : !CT_EDEFAULT.equals(this.getCt());
        case FxtranPackage.DOCUMENT_ROOT__CYCLE_STMT:
            return this.getCycleStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__DEALLOCATE_STMT:
            return this.getDeallocateStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__DERIVED_TSPEC:
            return this.getDerivedTSpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__DO_STMT:
            return this.getDoStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__DO_V:
            return this.getDoV() != null;
        case FxtranPackage.DOCUMENT_ROOT__DUMMY_ARG_LT:
            return this.getDummyArgLT() != null;
        case FxtranPackage.DOCUMENT_ROOT__E1:
            return this.getE1() != null;
        case FxtranPackage.DOCUMENT_ROOT__E2:
            return this.getE2() != null;
        case FxtranPackage.DOCUMENT_ROOT__ELEMENT:
            return this.getElement() != null;
        case FxtranPackage.DOCUMENT_ROOT__ELEMENT_LT:
            return this.getElementLT() != null;
        case FxtranPackage.DOCUMENT_ROOT__ELSE_IF_STMT:
            return this.getElseIfStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__ELSE_STMT:
            return ELSE_STMT_EDEFAULT == null ? this.getElseStmt() != null
                    : !ELSE_STMT_EDEFAULT.equals(this.getElseStmt());
        case FxtranPackage.DOCUMENT_ROOT__ELSE_WHERE_STMT:
            return ELSE_WHERE_STMT_EDEFAULT == null ? this.getElseWhereStmt() != null
                    : !ELSE_WHERE_STMT_EDEFAULT.equals(this.getElseWhereStmt());
        case FxtranPackage.DOCUMENT_ROOT__EN:
            return this.getEN() != null;
        case FxtranPackage.DOCUMENT_ROOT__EN_DECL:
            return this.getENDecl() != null;
        case FxtranPackage.DOCUMENT_ROOT__EN_DECL_LT:
            return this.getENDeclLT() != null;
        case FxtranPackage.DOCUMENT_ROOT__ENLT:
            return this.getENLT() != null;
        case FxtranPackage.DOCUMENT_ROOT__ENN:
            return this.getENN() != null;
        case FxtranPackage.DOCUMENT_ROOT__END_DO_STMT:
            return this.getEndDoStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__END_FORALL_STMT:
            return this.getEndForallStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__END_FUNCTION_STMT:
            return this.getEndFunctionStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__END_IF_STMT:
            return END_IF_STMT_EDEFAULT == null ? this.getEndIfStmt() != null
                    : !END_IF_STMT_EDEFAULT.equals(this.getEndIfStmt());
        case FxtranPackage.DOCUMENT_ROOT__END_INTERFACE_STMT:
            return this.getEndInterfaceStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__END_MODULE_STMT:
            return this.getEndModuleStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__END_PROGRAM_STMT:
            return this.getEndProgramStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__END_SELECT_CASE_STMT:
            return this.getEndSelectCaseStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__END_SUBROUTINE_STMT:
            return this.getEndSubroutineStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__END_TSTMT:
            return this.getEndTStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__END_WHERE_STMT:
            return END_WHERE_STMT_EDEFAULT == null ? this.getEndWhereStmt() != null
                    : !END_WHERE_STMT_EDEFAULT.equals(this.getEndWhereStmt());
        case FxtranPackage.DOCUMENT_ROOT__ERROR:
            return this.getError() != null;
        case FxtranPackage.DOCUMENT_ROOT__EXIT_STMT:
            return EXIT_STMT_EDEFAULT == null ? this.getExitStmt() != null
                    : !EXIT_STMT_EDEFAULT.equals(this.getExitStmt());
        case FxtranPackage.DOCUMENT_ROOT__FILE:
            return this.getFile() != null;
        case FxtranPackage.DOCUMENT_ROOT__FORALL_CONSTRUCT_STMT:
            return this.getForallConstructStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__FORALL_STMT:
            return this.getForallStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__FORALL_TRIPLET_SPEC:
            return this.getForallTripletSpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__FORALL_TRIPLET_SPEC_LT:
            return this.getForallTripletSpecLT() != null;
        case FxtranPackage.DOCUMENT_ROOT__FUNCTION_N:
            return this.getFunctionN() != null;
        case FxtranPackage.DOCUMENT_ROOT__FUNCTION_STMT:
            return this.getFunctionStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__IF_STMT:
            return this.getIfStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__IF_THEN_STMT:
            return this.getIfThenStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__IMPLICIT_NONE_STMT:
            return IMPLICIT_NONE_STMT_EDEFAULT == null ? this.getImplicitNoneStmt() != null
                    : !IMPLICIT_NONE_STMT_EDEFAULT.equals(this.getImplicitNoneStmt());
        case FxtranPackage.DOCUMENT_ROOT__INIT_E:
            return this.getInitE() != null;
        case FxtranPackage.DOCUMENT_ROOT__INQUIRE_STMT:
            return this.getInquireStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__INQUIRY_SPEC:
            return this.getInquirySpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__INQUIRY_SPEC_SPEC:
            return this.getInquirySpecSpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__INTENT_SPEC:
            return INTENT_SPEC_EDEFAULT == null ? this.getIntentSpec() != null
                    : !INTENT_SPEC_EDEFAULT.equals(this.getIntentSpec());
        case FxtranPackage.DOCUMENT_ROOT__INTERFACE_STMT:
            return this.getInterfaceStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__INTRINSIC_TSPEC:
            return this.getIntrinsicTSpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__IO_CONTROL:
            return this.getIoControl() != null;
        case FxtranPackage.DOCUMENT_ROOT__IO_CONTROL_SPEC:
            return this.getIoControlSpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__ITERATOR:
            return this.getIterator() != null;
        case FxtranPackage.DOCUMENT_ROOT__ITERATOR_DEFINITION_LT:
            return this.getIteratorDefinitionLT() != null;
        case FxtranPackage.DOCUMENT_ROOT__ITERATOR_ELEMENT:
            return this.getIteratorElement() != null;
        case FxtranPackage.DOCUMENT_ROOT__K:
            return K_EDEFAULT == null ? this.getK() != null : !K_EDEFAULT.equals(this.getK());
        case FxtranPackage.DOCUMENT_ROOT__KSELECTOR:
            return this.getKSelector() != null;
        case FxtranPackage.DOCUMENT_ROOT__KSPEC:
            return this.getKSpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__L:
            return L_EDEFAULT == null ? this.getL() != null : !L_EDEFAULT.equals(this.getL());
        case FxtranPackage.DOCUMENT_ROOT__LABEL:
            return this.getLabel() != null;
        case FxtranPackage.DOCUMENT_ROOT__LITERAL_E:
            return this.getLiteralE() != null;
        case FxtranPackage.DOCUMENT_ROOT__LOWER_BOUND:
            return this.getLowerBound() != null;
        case FxtranPackage.DOCUMENT_ROOT__MASK_E:
            return this.getMaskE() != null;
        case FxtranPackage.DOCUMENT_ROOT__MODULE_N:
            return this.getModuleN() != null;
        case FxtranPackage.DOCUMENT_ROOT__MODULE_PROCEDURE_NLT:
            return this.getModuleProcedureNLT() != null;
        case FxtranPackage.DOCUMENT_ROOT__MODULE_STMT:
            return this.getModuleStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__N:
            return N_EDEFAULT == null ? this.getN() != null : !N_EDEFAULT.equals(this.getN());
        case FxtranPackage.DOCUMENT_ROOT__N1:
            return this.getN1() != null;
        case FxtranPackage.DOCUMENT_ROOT__NAMED_E:
            return this.getNamedE() != null;
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_N:
            return this.getNamelistGroupN() != null;
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_OBJ:
            return this.getNamelistGroupObj() != null;
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_OBJ_LT:
            return this.getNamelistGroupObjLT() != null;
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_GROUP_OBJ_N:
            return this.getNamelistGroupObjN() != null;
        case FxtranPackage.DOCUMENT_ROOT__NAMELIST_STMT:
            return this.getNamelistStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__NULLIFY_STMT:
            return this.getNullifyStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__O:
            return O_EDEFAULT == null ? this.getO() != null : !O_EDEFAULT.equals(this.getO());
        case FxtranPackage.DOCUMENT_ROOT__OBJECT:
            return this.getObject() != null;
        case FxtranPackage.DOCUMENT_ROOT__OP:
            return this.getOp() != null;
        case FxtranPackage.DOCUMENT_ROOT__OP_E:
            return this.getOpE() != null;
        case FxtranPackage.DOCUMENT_ROOT__OPEN_STMT:
            return this.getOpenStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__OUTPUT_ITEM:
            return this.getOutputItem() != null;
        case FxtranPackage.DOCUMENT_ROOT__OUTPUT_ITEM_LT:
            return this.getOutputItemLT() != null;
        case FxtranPackage.DOCUMENT_ROOT__PARENS_E:
            return this.getParensE() != null;
        case FxtranPackage.DOCUMENT_ROOT__PARENS_R:
            return this.getParensR() != null;
        case FxtranPackage.DOCUMENT_ROOT__POINTER_ASTMT:
            return this.getPointerAStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__POINTER_STMT:
            return this.getPointerStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__PREFIX:
            return PREFIX_EDEFAULT == null ? this.getPrefix() != null : !PREFIX_EDEFAULT.equals(this.getPrefix());
        case FxtranPackage.DOCUMENT_ROOT__PRIVATE_STMT:
            return PRIVATE_STMT_EDEFAULT == null ? this.getPrivateStmt() != null
                    : !PRIVATE_STMT_EDEFAULT.equals(this.getPrivateStmt());
        case FxtranPackage.DOCUMENT_ROOT__PROCEDURE_DESIGNATOR:
            return this.getProcedureDesignator() != null;
        case FxtranPackage.DOCUMENT_ROOT__PROCEDURE_STMT:
            return this.getProcedureStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__PROGRAM_N:
            return this.getProgramN() != null;
        case FxtranPackage.DOCUMENT_ROOT__PROGRAM_STMT:
            return this.getProgramStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__PUBLIC_STMT:
            return this.getPublicStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__RLT:
            return this.getRLT() != null;
        case FxtranPackage.DOCUMENT_ROOT__READ_STMT:
            return this.getReadStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__RENAME:
            return this.getRename() != null;
        case FxtranPackage.DOCUMENT_ROOT__RENAME_LT:
            return this.getRenameLT() != null;
        case FxtranPackage.DOCUMENT_ROOT__RESULT_SPEC:
            return this.getResultSpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__RETURN_STMT:
            return RETURN_STMT_EDEFAULT == null ? this.getReturnStmt() != null
                    : !RETURN_STMT_EDEFAULT.equals(this.getReturnStmt());
        case FxtranPackage.DOCUMENT_ROOT__S:
            return S_EDEFAULT == null ? this.getS() != null : !S_EDEFAULT.equals(this.getS());
        case FxtranPackage.DOCUMENT_ROOT__SAVE_STMT:
            return SAVE_STMT_EDEFAULT == null ? this.getSaveStmt() != null
                    : !SAVE_STMT_EDEFAULT.equals(this.getSaveStmt());
        case FxtranPackage.DOCUMENT_ROOT__SECTION_SUBSCRIPT:
            return this.getSectionSubscript() != null;
        case FxtranPackage.DOCUMENT_ROOT__SECTION_SUBSCRIPT_LT:
            return this.getSectionSubscriptLT() != null;
        case FxtranPackage.DOCUMENT_ROOT__SELECT_CASE_STMT:
            return this.getSelectCaseStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__SHAPE_SPEC:
            return this.getShapeSpec() != null;
        case FxtranPackage.DOCUMENT_ROOT__SHAPE_SPEC_LT:
            return this.getShapeSpecLT() != null;
        case FxtranPackage.DOCUMENT_ROOT__STAR_E:
            return STAR_E_EDEFAULT == null ? this.getStarE() != null : !STAR_E_EDEFAULT.equals(this.getStarE());
        case FxtranPackage.DOCUMENT_ROOT__STOP_CODE:
            return STOP_CODE_EDEFAULT == null ? this.getStopCode() != null
                    : !STOP_CODE_EDEFAULT.equals(this.getStopCode());
        case FxtranPackage.DOCUMENT_ROOT__STOP_STMT:
            return this.getStopStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__STRING_E:
            return this.getStringE() != null;
        case FxtranPackage.DOCUMENT_ROOT__SUBROUTINE_N:
            return this.getSubroutineN() != null;
        case FxtranPackage.DOCUMENT_ROOT__SUBROUTINE_STMT:
            return this.getSubroutineStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__TDECL_STMT:
            return this.getTDeclStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__TN:
            return this.getTN() != null;
        case FxtranPackage.DOCUMENT_ROOT__TSTMT:
            return this.getTStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__TEST_E:
            return this.getTestE() != null;
        case FxtranPackage.DOCUMENT_ROOT__UPPER_BOUND:
            return this.getUpperBound() != null;
        case FxtranPackage.DOCUMENT_ROOT__USE_N:
            return this.getUseN() != null;
        case FxtranPackage.DOCUMENT_ROOT__USE_STMT:
            return this.getUseStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__V:
            return this.getV() != null;
        case FxtranPackage.DOCUMENT_ROOT__VN:
            return this.getVN() != null;
        case FxtranPackage.DOCUMENT_ROOT__WHERE_CONSTRUCT_STMT:
            return this.getWhereConstructStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__WHERE_STMT:
            return this.getWhereStmt() != null;
        case FxtranPackage.DOCUMENT_ROOT__WRITE_STMT:
            return this.getWriteStmt() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (this.eIsProxy()) {
            return super.toString();
        }

        final StringBuilder result = new StringBuilder(super.toString());
        result.append(" (mixed: ");
        result.append(this.mixed);
        result.append(')');
        return result.toString();
    }

} // DocumentRootImpl
