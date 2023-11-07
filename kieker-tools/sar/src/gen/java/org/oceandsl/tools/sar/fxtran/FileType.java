/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>File Type</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getC <em>C</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getAStmt <em>AStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getAllocateStmt <em>Allocate Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getCallStmt <em>Call Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getDeallocateStmt <em>Deallocate
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getExitStmt <em>Exit Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getPointerAStmt <em>Pointer AStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getReturnStmt <em>Return Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getWhereStmt <em>Where Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getTDeclStmt <em>TDecl Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getTStmt <em>TStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getCaseStmt <em>Case Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getCloseStmt <em>Close Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getComponentDeclStmt <em>Component Decl
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getContainsStmt <em>Contains Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getCpp <em>Cpp</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getDoStmt <em>Do Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getElseIfStmt <em>Else If Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getElseStmt <em>Else Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getElseWhereStmt <em>Else Where Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getEndTStmt <em>End TStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getEndDoStmt <em>End Do Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getEndForallStmt <em>End Forall Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getEndFunctionStmt <em>End Function
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getEndIfStmt <em>End If Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getEndInterfaceStmt <em>End Interface
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getEndSelectCaseStmt <em>End Select Case
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getEndSubroutineStmt <em>End Subroutine
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getEndWhereStmt <em>End Where Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getForallConstructStmt <em>Forall Construct
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getForallStmt <em>Forall Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getFunctionStmt <em>Function Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getIfStmt <em>If Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getIfThenStmt <em>If Then Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getImplicitNoneStmt <em>Implicit None
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getInquireStmt <em>Inquire Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getInterfaceStmt <em>Interface Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getModuleStmt <em>Module Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getNamelistStmt <em>Namelist Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getNullifyStmt <em>Nullify Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getOpenStmt <em>Open Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getPointerStmt <em>Pointer Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getPrivateStmt <em>Private Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getProcedureStmt <em>Procedure Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getProgramStmt <em>Program Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getPublicStmt <em>Public Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getReadStmt <em>Read Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getSaveStmt <em>Save Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getSelectCaseStmt <em>Select Case
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getStopStmt <em>Stop Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getSubroutineStmt <em>Subroutine
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getUseStmt <em>Use Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getWhereConstructStmt <em>Where Construct
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getWriteStmt <em>Write Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getEndModuleStmt <em>End Module Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getEndProgramStmt <em>End Program
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FileType#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType()
 * @model extendedMetaData="name='file_._type' kind='elementOnly'"
 * @generated
 */
public interface FileType extends EObject {
    /**
     * Returns the value of the '<em><b>Group</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Group</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:0'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>C</b></em>' attribute list. The list contents are of type
     * {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>C</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_C()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='C'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<String> getC();

    /**
     * Returns the value of the '<em><b>AStmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.AStmtType}. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>AStmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_AStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='a-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<AStmtType> getAStmt();

    /**
     * Returns the value of the '<em><b>Allocate Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.AllocateStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Allocate Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_AllocateStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='allocate-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<AllocateStmtType> getAllocateStmt();

    /**
     * Returns the value of the '<em><b>Call Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.CallStmtType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Call Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_CallStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='call-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<CallStmtType> getCallStmt();

    /**
     * Returns the value of the '<em><b>Deallocate Stmt</b></em>' containment reference list. The
     * list contents are of type {@link org.oceandsl.tools.sar.fxtran.DeallocateStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Deallocate Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_DeallocateStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='deallocate-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<DeallocateStmtType> getDeallocateStmt();

    /**
     * Returns the value of the '<em><b>Exit Stmt</b></em>' attribute list. The list contents are of
     * type {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Exit Stmt</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_ExitStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='exit-stmt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<String> getExitStmt();

    /**
     * Returns the value of the '<em><b>Pointer AStmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.PointerAStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Pointer AStmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_PointerAStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pointer-a-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<PointerAStmtType> getPointerAStmt();

    /**
     * Returns the value of the '<em><b>Return Stmt</b></em>' attribute list. The list contents are
     * of type {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Return Stmt</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_ReturnStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='return-stmt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<String> getReturnStmt();

    /**
     * Returns the value of the '<em><b>Where Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.WhereStmtType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Where Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_WhereStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='where-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<WhereStmtType> getWhereStmt();

    /**
     * Returns the value of the '<em><b>TDecl Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.TDeclStmtType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>TDecl Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_TDeclStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='T-decl-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<TDeclStmtType> getTDeclStmt();

    /**
     * Returns the value of the '<em><b>TStmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.TStmtType}. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>TStmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_TStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='T-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<TStmtType> getTStmt();

    /**
     * Returns the value of the '<em><b>Case Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.CaseStmtType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Case Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_CaseStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='case-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<CaseStmtType> getCaseStmt();

    /**
     * Returns the value of the '<em><b>Close Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.CloseStmtType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Close Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_CloseStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='close-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<CloseStmtType> getCloseStmt();

    /**
     * Returns the value of the '<em><b>Component Decl Stmt</b></em>' containment reference list.
     * The list contents are of type {@link org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType}.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Component Decl Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_ComponentDeclStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='component-decl-stmt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<ComponentDeclStmtType> getComponentDeclStmt();

    /**
     * Returns the value of the '<em><b>Contains Stmt</b></em>' attribute list. The list contents
     * are of type {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Contains Stmt</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_ContainsStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='contains-stmt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<String> getContainsStmt();

    /**
     * Returns the value of the '<em><b>Cpp</b></em>' attribute list. The list contents are of type
     * {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Cpp</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_Cpp()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='cpp'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<String> getCpp();

    /**
     * Returns the value of the '<em><b>Do Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.DoStmtType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Do Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_DoStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='do-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<DoStmtType> getDoStmt();

    /**
     * Returns the value of the '<em><b>Else If Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.ElseIfStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Else If Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_ElseIfStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='else-if-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<ElseIfStmtType> getElseIfStmt();

    /**
     * Returns the value of the '<em><b>Else Stmt</b></em>' attribute list. The list contents are of
     * type {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Else Stmt</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_ElseStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='else-stmt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<String> getElseStmt();

    /**
     * Returns the value of the '<em><b>Else Where Stmt</b></em>' attribute list. The list contents
     * are of type {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Else Where Stmt</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_ElseWhereStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='else-where-stmt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<String> getElseWhereStmt();

    /**
     * Returns the value of the '<em><b>End TStmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.EndTStmtType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End TStmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_EndTStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='end-T-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<EndTStmtType> getEndTStmt();

    /**
     * Returns the value of the '<em><b>End Do Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.EndDoStmtType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Do Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_EndDoStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='end-do-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<EndDoStmtType> getEndDoStmt();

    /**
     * Returns the value of the '<em><b>End Forall Stmt</b></em>' containment reference list. The
     * list contents are of type {@link org.oceandsl.tools.sar.fxtran.EndForallStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Forall Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_EndForallStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='end-forall-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<EndForallStmtType> getEndForallStmt();

    /**
     * Returns the value of the '<em><b>End Function Stmt</b></em>' containment reference list. The
     * list contents are of type {@link org.oceandsl.tools.sar.fxtran.EndFunctionStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Function Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_EndFunctionStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='end-function-stmt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<EndFunctionStmtType> getEndFunctionStmt();

    /**
     * Returns the value of the '<em><b>End If Stmt</b></em>' attribute list. The list contents are
     * of type {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End If Stmt</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_EndIfStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='end-if-stmt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<String> getEndIfStmt();

    /**
     * Returns the value of the '<em><b>End Interface Stmt</b></em>' containment reference list. The
     * list contents are of type {@link org.oceandsl.tools.sar.fxtran.EndInterfaceStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Interface Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_EndInterfaceStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='end-interface-stmt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<EndInterfaceStmtType> getEndInterfaceStmt();

    /**
     * Returns the value of the '<em><b>End Select Case Stmt</b></em>' containment reference list.
     * The list contents are of type {@link org.oceandsl.tools.sar.fxtran.EndSelectCaseStmtType}.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Select Case Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_EndSelectCaseStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='end-select-case-stmt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<EndSelectCaseStmtType> getEndSelectCaseStmt();

    /**
     * Returns the value of the '<em><b>End Subroutine Stmt</b></em>' containment reference list.
     * The list contents are of type {@link org.oceandsl.tools.sar.fxtran.EndSubroutineStmtType}.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Subroutine Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_EndSubroutineStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='end-subroutine-stmt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<EndSubroutineStmtType> getEndSubroutineStmt();

    /**
     * Returns the value of the '<em><b>End Where Stmt</b></em>' attribute list. The list contents
     * are of type {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Where Stmt</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_EndWhereStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='end-where-stmt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<String> getEndWhereStmt();

    /**
     * Returns the value of the '<em><b>Forall Construct Stmt</b></em>' containment reference list.
     * The list contents are of type {@link org.oceandsl.tools.sar.fxtran.ForallConstructStmtType}.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Forall Construct Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_ForallConstructStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='forall-construct-stmt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<ForallConstructStmtType> getForallConstructStmt();

    /**
     * Returns the value of the '<em><b>Forall Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.ForallStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Forall Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_ForallStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='forall-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<ForallStmtType> getForallStmt();

    /**
     * Returns the value of the '<em><b>Function Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.FunctionStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Function Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_FunctionStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='function-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<FunctionStmtType> getFunctionStmt();

    /**
     * Returns the value of the '<em><b>If Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.IfStmtType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>If Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_IfStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='if-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<IfStmtType> getIfStmt();

    /**
     * Returns the value of the '<em><b>If Then Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.IfThenStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>If Then Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_IfThenStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='if-then-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<IfThenStmtType> getIfThenStmt();

    /**
     * Returns the value of the '<em><b>Implicit None Stmt</b></em>' attribute list. The list
     * contents are of type {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Implicit None Stmt</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_ImplicitNoneStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element'
     *        name='implicit-none-stmt' namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<String> getImplicitNoneStmt();

    /**
     * Returns the value of the '<em><b>Inquire Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.InquireStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Inquire Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_InquireStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='inquire-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<InquireStmtType> getInquireStmt();

    /**
     * Returns the value of the '<em><b>Interface Stmt</b></em>' containment reference list. The
     * list contents are of type {@link org.oceandsl.tools.sar.fxtran.InterfaceStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Interface Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_InterfaceStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='interface-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<InterfaceStmtType> getInterfaceStmt();

    /**
     * Returns the value of the '<em><b>Module Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.ModuleStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Module Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_ModuleStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='module-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<ModuleStmtType> getModuleStmt();

    /**
     * Returns the value of the '<em><b>Namelist Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.NamelistStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Namelist Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_NamelistStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='namelist-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<NamelistStmtType> getNamelistStmt();

    /**
     * Returns the value of the '<em><b>Nullify Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.NullifyStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nullify Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_NullifyStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='nullify-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<NullifyStmtType> getNullifyStmt();

    /**
     * Returns the value of the '<em><b>Open Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.OpenStmtType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Open Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_OpenStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='open-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<OpenStmtType> getOpenStmt();

    /**
     * Returns the value of the '<em><b>Pointer Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.PointerStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Pointer Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_PointerStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pointer-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<PointerStmtType> getPointerStmt();

    /**
     * Returns the value of the '<em><b>Private Stmt</b></em>' attribute list. The list contents are
     * of type {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Private Stmt</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_PrivateStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='private-stmt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<String> getPrivateStmt();

    /**
     * Returns the value of the '<em><b>Procedure Stmt</b></em>' containment reference list. The
     * list contents are of type {@link org.oceandsl.tools.sar.fxtran.ProcedureStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Procedure Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_ProcedureStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='procedure-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<ProcedureStmtType> getProcedureStmt();

    /**
     * Returns the value of the '<em><b>Program Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.ProgramStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Program Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_ProgramStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='program-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<ProgramStmtType> getProgramStmt();

    /**
     * Returns the value of the '<em><b>Public Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.PublicStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Public Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_PublicStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='public-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<PublicStmtType> getPublicStmt();

    /**
     * Returns the value of the '<em><b>Read Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.ReadStmtType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Read Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_ReadStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='read-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<ReadStmtType> getReadStmt();

    /**
     * Returns the value of the '<em><b>Save Stmt</b></em>' attribute list. The list contents are of
     * type {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Save Stmt</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_SaveStmt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='save-stmt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<String> getSaveStmt();

    /**
     * Returns the value of the '<em><b>Select Case Stmt</b></em>' containment reference list. The
     * list contents are of type {@link org.oceandsl.tools.sar.fxtran.SelectCaseStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Select Case Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_SelectCaseStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='select-case-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<SelectCaseStmtType> getSelectCaseStmt();

    /**
     * Returns the value of the '<em><b>Stop Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.StopStmtType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Stop Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_StopStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='stop-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<StopStmtType> getStopStmt();

    /**
     * Returns the value of the '<em><b>Subroutine Stmt</b></em>' containment reference list. The
     * list contents are of type {@link org.oceandsl.tools.sar.fxtran.SubroutineStmtType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Subroutine Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_SubroutineStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='subroutine-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<SubroutineStmtType> getSubroutineStmt();

    /**
     * Returns the value of the '<em><b>Use Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.UseStmtType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Use Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_UseStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='use-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<UseStmtType> getUseStmt();

    /**
     * Returns the value of the '<em><b>Where Construct Stmt</b></em>' containment reference list.
     * The list contents are of type {@link org.oceandsl.tools.sar.fxtran.WhereConstructStmtType}.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Where Construct Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_WhereConstructStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='where-construct-stmt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<WhereConstructStmtType> getWhereConstructStmt();

    /**
     * Returns the value of the '<em><b>Write Stmt</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.WriteStmtType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Write Stmt</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_WriteStmt()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='write-stmt' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<WriteStmtType> getWriteStmt();

    /**
     * Returns the value of the '<em><b>End Module Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>End Module Stmt</em>' containment reference.
     * @see #setEndModuleStmt(EndModuleStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_EndModuleStmt()
     * @model containment="true" extendedMetaData="kind='element' name='end-module-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    EndModuleStmtType getEndModuleStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.FileType#getEndModuleStmt <em>End
     * Module Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
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
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_EndProgramStmt()
     * @model containment="true" extendedMetaData="kind='element' name='end-program-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    EndProgramStmtType getEndProgramStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.FileType#getEndProgramStmt
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
     * Returns the value of the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFileType_Name()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NCName" required="true"
     *        extendedMetaData="kind='attribute' name='name'"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.FileType#getName <em>Name</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

} // FileType
