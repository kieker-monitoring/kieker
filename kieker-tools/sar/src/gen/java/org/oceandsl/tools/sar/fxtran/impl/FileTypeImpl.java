/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.oceandsl.tools.sar.fxtran.AStmtType;
import org.oceandsl.tools.sar.fxtran.AllocateStmtType;
import org.oceandsl.tools.sar.fxtran.CallStmtType;
import org.oceandsl.tools.sar.fxtran.CaseStmtType;
import org.oceandsl.tools.sar.fxtran.CloseStmtType;
import org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType;
import org.oceandsl.tools.sar.fxtran.DeallocateStmtType;
import org.oceandsl.tools.sar.fxtran.DoStmtType;
import org.oceandsl.tools.sar.fxtran.ElseIfStmtType;
import org.oceandsl.tools.sar.fxtran.EndDoStmtType;
import org.oceandsl.tools.sar.fxtran.EndForallStmtType;
import org.oceandsl.tools.sar.fxtran.EndFunctionStmtType;
import org.oceandsl.tools.sar.fxtran.EndInterfaceStmtType;
import org.oceandsl.tools.sar.fxtran.EndModuleStmtType;
import org.oceandsl.tools.sar.fxtran.EndProgramStmtType;
import org.oceandsl.tools.sar.fxtran.EndSelectCaseStmtType;
import org.oceandsl.tools.sar.fxtran.EndSubroutineStmtType;
import org.oceandsl.tools.sar.fxtran.EndTStmtType;
import org.oceandsl.tools.sar.fxtran.FileType;
import org.oceandsl.tools.sar.fxtran.ForallConstructStmtType;
import org.oceandsl.tools.sar.fxtran.ForallStmtType;
import org.oceandsl.tools.sar.fxtran.FunctionStmtType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.IfStmtType;
import org.oceandsl.tools.sar.fxtran.IfThenStmtType;
import org.oceandsl.tools.sar.fxtran.InquireStmtType;
import org.oceandsl.tools.sar.fxtran.InterfaceStmtType;
import org.oceandsl.tools.sar.fxtran.ModuleStmtType;
import org.oceandsl.tools.sar.fxtran.NamelistStmtType;
import org.oceandsl.tools.sar.fxtran.NullifyStmtType;
import org.oceandsl.tools.sar.fxtran.OpenStmtType;
import org.oceandsl.tools.sar.fxtran.PointerAStmtType;
import org.oceandsl.tools.sar.fxtran.PointerStmtType;
import org.oceandsl.tools.sar.fxtran.ProcedureStmtType;
import org.oceandsl.tools.sar.fxtran.ProgramStmtType;
import org.oceandsl.tools.sar.fxtran.PublicStmtType;
import org.oceandsl.tools.sar.fxtran.ReadStmtType;
import org.oceandsl.tools.sar.fxtran.SelectCaseStmtType;
import org.oceandsl.tools.sar.fxtran.StopStmtType;
import org.oceandsl.tools.sar.fxtran.SubroutineStmtType;
import org.oceandsl.tools.sar.fxtran.TDeclStmtType;
import org.oceandsl.tools.sar.fxtran.TStmtType;
import org.oceandsl.tools.sar.fxtran.UseStmtType;
import org.oceandsl.tools.sar.fxtran.WhereConstructStmtType;
import org.oceandsl.tools.sar.fxtran.WhereStmtType;
import org.oceandsl.tools.sar.fxtran.WriteStmtType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>File Type</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getC <em>C</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getAStmt <em>AStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getAllocateStmt <em>Allocate
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getCallStmt <em>Call Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getDeallocateStmt <em>Deallocate
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getExitStmt <em>Exit Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getPointerAStmt <em>Pointer
 * AStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getReturnStmt <em>Return
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getWhereStmt <em>Where Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getTDeclStmt <em>TDecl Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getTStmt <em>TStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getCaseStmt <em>Case Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getCloseStmt <em>Close Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getComponentDeclStmt <em>Component
 * Decl Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getContainsStmt <em>Contains
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getCpp <em>Cpp</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getDoStmt <em>Do Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getElseIfStmt <em>Else If
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getElseStmt <em>Else Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getElseWhereStmt <em>Else Where
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getEndTStmt <em>End TStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getEndDoStmt <em>End Do
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getEndForallStmt <em>End Forall
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getEndFunctionStmt <em>End Function
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getEndIfStmt <em>End If
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getEndInterfaceStmt <em>End Interface
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getEndSelectCaseStmt <em>End Select
 * Case Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getEndSubroutineStmt <em>End
 * Subroutine Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getEndWhereStmt <em>End Where
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getForallConstructStmt <em>Forall
 * Construct Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getForallStmt <em>Forall
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getFunctionStmt <em>Function
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getIfStmt <em>If Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getIfThenStmt <em>If Then
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getImplicitNoneStmt <em>Implicit None
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getInquireStmt <em>Inquire
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getInterfaceStmt <em>Interface
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getModuleStmt <em>Module
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getNamelistStmt <em>Namelist
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getNullifyStmt <em>Nullify
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getOpenStmt <em>Open Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getPointerStmt <em>Pointer
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getPrivateStmt <em>Private
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getProcedureStmt <em>Procedure
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getProgramStmt <em>Program
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getPublicStmt <em>Public
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getReadStmt <em>Read Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getSaveStmt <em>Save Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getSelectCaseStmt <em>Select Case
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getStopStmt <em>Stop Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getSubroutineStmt <em>Subroutine
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getUseStmt <em>Use Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getWhereConstructStmt <em>Where
 * Construct Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getWriteStmt <em>Write Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getEndModuleStmt <em>End Module
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getEndProgramStmt <em>End Program
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl#getName <em>Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FileTypeImpl extends MinimalEObjectImpl.Container implements FileType {
    /**
     * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap group;

    /**
     * The cached value of the '{@link #getEndModuleStmt() <em>End Module Stmt</em>}' containment
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getEndModuleStmt()
     * @generated
     * @ordered
     */
    protected EndModuleStmtType endModuleStmt;

    /**
     * The cached value of the '{@link #getEndProgramStmt() <em>End Program Stmt</em>}' containment
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getEndProgramStmt()
     * @generated
     * @ordered
     */
    protected EndProgramStmtType endProgramStmt;

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected FileTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getFileType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getGroup() {
        if (this.group == null) {
            this.group = new BasicFeatureMap(this, FxtranPackage.FILE_TYPE__GROUP);
        }
        return this.group;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getC() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_C());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<AStmtType> getAStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_AStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<AllocateStmtType> getAllocateStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_AllocateStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<CallStmtType> getCallStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_CallStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<DeallocateStmtType> getDeallocateStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_DeallocateStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getExitStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_ExitStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<PointerAStmtType> getPointerAStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_PointerAStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getReturnStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_ReturnStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<WhereStmtType> getWhereStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_WhereStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<TDeclStmtType> getTDeclStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_TDeclStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<TStmtType> getTStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_TStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<CaseStmtType> getCaseStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_CaseStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<CloseStmtType> getCloseStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_CloseStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ComponentDeclStmtType> getComponentDeclStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_ComponentDeclStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getContainsStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_ContainsStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getCpp() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_Cpp());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<DoStmtType> getDoStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_DoStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ElseIfStmtType> getElseIfStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_ElseIfStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getElseStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_ElseStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getElseWhereStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_ElseWhereStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<EndTStmtType> getEndTStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_EndTStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<EndDoStmtType> getEndDoStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_EndDoStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<EndForallStmtType> getEndForallStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_EndForallStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<EndFunctionStmtType> getEndFunctionStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_EndFunctionStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getEndIfStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_EndIfStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<EndInterfaceStmtType> getEndInterfaceStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_EndInterfaceStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<EndSelectCaseStmtType> getEndSelectCaseStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_EndSelectCaseStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<EndSubroutineStmtType> getEndSubroutineStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_EndSubroutineStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getEndWhereStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_EndWhereStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ForallConstructStmtType> getForallConstructStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_ForallConstructStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ForallStmtType> getForallStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_ForallStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<FunctionStmtType> getFunctionStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_FunctionStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<IfStmtType> getIfStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_IfStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<IfThenStmtType> getIfThenStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_IfThenStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getImplicitNoneStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_ImplicitNoneStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<InquireStmtType> getInquireStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_InquireStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<InterfaceStmtType> getInterfaceStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_InterfaceStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ModuleStmtType> getModuleStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_ModuleStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<NamelistStmtType> getNamelistStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_NamelistStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<NullifyStmtType> getNullifyStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_NullifyStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<OpenStmtType> getOpenStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_OpenStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<PointerStmtType> getPointerStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_PointerStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getPrivateStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_PrivateStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ProcedureStmtType> getProcedureStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_ProcedureStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ProgramStmtType> getProgramStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_ProgramStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<PublicStmtType> getPublicStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_PublicStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ReadStmtType> getReadStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_ReadStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getSaveStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_SaveStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<SelectCaseStmtType> getSelectCaseStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_SelectCaseStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<StopStmtType> getStopStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_StopStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<SubroutineStmtType> getSubroutineStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_SubroutineStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<UseStmtType> getUseStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_UseStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<WhereConstructStmtType> getWhereConstructStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_WhereConstructStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<WriteStmtType> getWriteStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFileType_WriteStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndModuleStmtType getEndModuleStmt() {
        return this.endModuleStmt;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetEndModuleStmt(final EndModuleStmtType newEndModuleStmt, NotificationChain msgs) {
        final EndModuleStmtType oldEndModuleStmt = this.endModuleStmt;
        this.endModuleStmt = newEndModuleStmt;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.FILE_TYPE__END_MODULE_STMT, oldEndModuleStmt, newEndModuleStmt);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setEndModuleStmt(final EndModuleStmtType newEndModuleStmt) {
        if (newEndModuleStmt != this.endModuleStmt) {
            NotificationChain msgs = null;
            if (this.endModuleStmt != null) {
                msgs = ((InternalEObject) this.endModuleStmt).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.FILE_TYPE__END_MODULE_STMT, null, msgs);
            }
            if (newEndModuleStmt != null) {
                msgs = ((InternalEObject) newEndModuleStmt).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.FILE_TYPE__END_MODULE_STMT, null, msgs);
            }
            msgs = this.basicSetEndModuleStmt(newEndModuleStmt, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.FILE_TYPE__END_MODULE_STMT,
                    newEndModuleStmt, newEndModuleStmt));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EndProgramStmtType getEndProgramStmt() {
        return this.endProgramStmt;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetEndProgramStmt(final EndProgramStmtType newEndProgramStmt,
            NotificationChain msgs) {
        final EndProgramStmtType oldEndProgramStmt = this.endProgramStmt;
        this.endProgramStmt = newEndProgramStmt;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.FILE_TYPE__END_PROGRAM_STMT, oldEndProgramStmt, newEndProgramStmt);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setEndProgramStmt(final EndProgramStmtType newEndProgramStmt) {
        if (newEndProgramStmt != this.endProgramStmt) {
            NotificationChain msgs = null;
            if (this.endProgramStmt != null) {
                msgs = ((InternalEObject) this.endProgramStmt).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.FILE_TYPE__END_PROGRAM_STMT, null, msgs);
            }
            if (newEndProgramStmt != null) {
                msgs = ((InternalEObject) newEndProgramStmt).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.FILE_TYPE__END_PROGRAM_STMT, null, msgs);
            }
            msgs = this.basicSetEndProgramStmt(newEndProgramStmt, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.FILE_TYPE__END_PROGRAM_STMT,
                    newEndProgramStmt, newEndProgramStmt));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setName(final String newName) {
        final String oldName = this.name;
        this.name = newName;
        if (this.eNotificationRequired()) {
            this.eNotify(
                    new ENotificationImpl(this, Notification.SET, FxtranPackage.FILE_TYPE__NAME, oldName, this.name));
        }
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
        case FxtranPackage.FILE_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__ASTMT:
            return ((InternalEList<?>) this.getAStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__ALLOCATE_STMT:
            return ((InternalEList<?>) this.getAllocateStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__CALL_STMT:
            return ((InternalEList<?>) this.getCallStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__DEALLOCATE_STMT:
            return ((InternalEList<?>) this.getDeallocateStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__POINTER_ASTMT:
            return ((InternalEList<?>) this.getPointerAStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__WHERE_STMT:
            return ((InternalEList<?>) this.getWhereStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__TDECL_STMT:
            return ((InternalEList<?>) this.getTDeclStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__TSTMT:
            return ((InternalEList<?>) this.getTStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__CASE_STMT:
            return ((InternalEList<?>) this.getCaseStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__CLOSE_STMT:
            return ((InternalEList<?>) this.getCloseStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__COMPONENT_DECL_STMT:
            return ((InternalEList<?>) this.getComponentDeclStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__DO_STMT:
            return ((InternalEList<?>) this.getDoStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__ELSE_IF_STMT:
            return ((InternalEList<?>) this.getElseIfStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__END_TSTMT:
            return ((InternalEList<?>) this.getEndTStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__END_DO_STMT:
            return ((InternalEList<?>) this.getEndDoStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__END_FORALL_STMT:
            return ((InternalEList<?>) this.getEndForallStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__END_FUNCTION_STMT:
            return ((InternalEList<?>) this.getEndFunctionStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__END_INTERFACE_STMT:
            return ((InternalEList<?>) this.getEndInterfaceStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__END_SELECT_CASE_STMT:
            return ((InternalEList<?>) this.getEndSelectCaseStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__END_SUBROUTINE_STMT:
            return ((InternalEList<?>) this.getEndSubroutineStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__FORALL_CONSTRUCT_STMT:
            return ((InternalEList<?>) this.getForallConstructStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__FORALL_STMT:
            return ((InternalEList<?>) this.getForallStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__FUNCTION_STMT:
            return ((InternalEList<?>) this.getFunctionStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__IF_STMT:
            return ((InternalEList<?>) this.getIfStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__IF_THEN_STMT:
            return ((InternalEList<?>) this.getIfThenStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__INQUIRE_STMT:
            return ((InternalEList<?>) this.getInquireStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__INTERFACE_STMT:
            return ((InternalEList<?>) this.getInterfaceStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__MODULE_STMT:
            return ((InternalEList<?>) this.getModuleStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__NAMELIST_STMT:
            return ((InternalEList<?>) this.getNamelistStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__NULLIFY_STMT:
            return ((InternalEList<?>) this.getNullifyStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__OPEN_STMT:
            return ((InternalEList<?>) this.getOpenStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__POINTER_STMT:
            return ((InternalEList<?>) this.getPointerStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__PROCEDURE_STMT:
            return ((InternalEList<?>) this.getProcedureStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__PROGRAM_STMT:
            return ((InternalEList<?>) this.getProgramStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__PUBLIC_STMT:
            return ((InternalEList<?>) this.getPublicStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__READ_STMT:
            return ((InternalEList<?>) this.getReadStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__SELECT_CASE_STMT:
            return ((InternalEList<?>) this.getSelectCaseStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__STOP_STMT:
            return ((InternalEList<?>) this.getStopStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__SUBROUTINE_STMT:
            return ((InternalEList<?>) this.getSubroutineStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__USE_STMT:
            return ((InternalEList<?>) this.getUseStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__WHERE_CONSTRUCT_STMT:
            return ((InternalEList<?>) this.getWhereConstructStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__WRITE_STMT:
            return ((InternalEList<?>) this.getWriteStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FILE_TYPE__END_MODULE_STMT:
            return this.basicSetEndModuleStmt(null, msgs);
        case FxtranPackage.FILE_TYPE__END_PROGRAM_STMT:
            return this.basicSetEndProgramStmt(null, msgs);
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
        case FxtranPackage.FILE_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.FILE_TYPE__C:
            return this.getC();
        case FxtranPackage.FILE_TYPE__ASTMT:
            return this.getAStmt();
        case FxtranPackage.FILE_TYPE__ALLOCATE_STMT:
            return this.getAllocateStmt();
        case FxtranPackage.FILE_TYPE__CALL_STMT:
            return this.getCallStmt();
        case FxtranPackage.FILE_TYPE__DEALLOCATE_STMT:
            return this.getDeallocateStmt();
        case FxtranPackage.FILE_TYPE__EXIT_STMT:
            return this.getExitStmt();
        case FxtranPackage.FILE_TYPE__POINTER_ASTMT:
            return this.getPointerAStmt();
        case FxtranPackage.FILE_TYPE__RETURN_STMT:
            return this.getReturnStmt();
        case FxtranPackage.FILE_TYPE__WHERE_STMT:
            return this.getWhereStmt();
        case FxtranPackage.FILE_TYPE__TDECL_STMT:
            return this.getTDeclStmt();
        case FxtranPackage.FILE_TYPE__TSTMT:
            return this.getTStmt();
        case FxtranPackage.FILE_TYPE__CASE_STMT:
            return this.getCaseStmt();
        case FxtranPackage.FILE_TYPE__CLOSE_STMT:
            return this.getCloseStmt();
        case FxtranPackage.FILE_TYPE__COMPONENT_DECL_STMT:
            return this.getComponentDeclStmt();
        case FxtranPackage.FILE_TYPE__CONTAINS_STMT:
            return this.getContainsStmt();
        case FxtranPackage.FILE_TYPE__CPP:
            return this.getCpp();
        case FxtranPackage.FILE_TYPE__DO_STMT:
            return this.getDoStmt();
        case FxtranPackage.FILE_TYPE__ELSE_IF_STMT:
            return this.getElseIfStmt();
        case FxtranPackage.FILE_TYPE__ELSE_STMT:
            return this.getElseStmt();
        case FxtranPackage.FILE_TYPE__ELSE_WHERE_STMT:
            return this.getElseWhereStmt();
        case FxtranPackage.FILE_TYPE__END_TSTMT:
            return this.getEndTStmt();
        case FxtranPackage.FILE_TYPE__END_DO_STMT:
            return this.getEndDoStmt();
        case FxtranPackage.FILE_TYPE__END_FORALL_STMT:
            return this.getEndForallStmt();
        case FxtranPackage.FILE_TYPE__END_FUNCTION_STMT:
            return this.getEndFunctionStmt();
        case FxtranPackage.FILE_TYPE__END_IF_STMT:
            return this.getEndIfStmt();
        case FxtranPackage.FILE_TYPE__END_INTERFACE_STMT:
            return this.getEndInterfaceStmt();
        case FxtranPackage.FILE_TYPE__END_SELECT_CASE_STMT:
            return this.getEndSelectCaseStmt();
        case FxtranPackage.FILE_TYPE__END_SUBROUTINE_STMT:
            return this.getEndSubroutineStmt();
        case FxtranPackage.FILE_TYPE__END_WHERE_STMT:
            return this.getEndWhereStmt();
        case FxtranPackage.FILE_TYPE__FORALL_CONSTRUCT_STMT:
            return this.getForallConstructStmt();
        case FxtranPackage.FILE_TYPE__FORALL_STMT:
            return this.getForallStmt();
        case FxtranPackage.FILE_TYPE__FUNCTION_STMT:
            return this.getFunctionStmt();
        case FxtranPackage.FILE_TYPE__IF_STMT:
            return this.getIfStmt();
        case FxtranPackage.FILE_TYPE__IF_THEN_STMT:
            return this.getIfThenStmt();
        case FxtranPackage.FILE_TYPE__IMPLICIT_NONE_STMT:
            return this.getImplicitNoneStmt();
        case FxtranPackage.FILE_TYPE__INQUIRE_STMT:
            return this.getInquireStmt();
        case FxtranPackage.FILE_TYPE__INTERFACE_STMT:
            return this.getInterfaceStmt();
        case FxtranPackage.FILE_TYPE__MODULE_STMT:
            return this.getModuleStmt();
        case FxtranPackage.FILE_TYPE__NAMELIST_STMT:
            return this.getNamelistStmt();
        case FxtranPackage.FILE_TYPE__NULLIFY_STMT:
            return this.getNullifyStmt();
        case FxtranPackage.FILE_TYPE__OPEN_STMT:
            return this.getOpenStmt();
        case FxtranPackage.FILE_TYPE__POINTER_STMT:
            return this.getPointerStmt();
        case FxtranPackage.FILE_TYPE__PRIVATE_STMT:
            return this.getPrivateStmt();
        case FxtranPackage.FILE_TYPE__PROCEDURE_STMT:
            return this.getProcedureStmt();
        case FxtranPackage.FILE_TYPE__PROGRAM_STMT:
            return this.getProgramStmt();
        case FxtranPackage.FILE_TYPE__PUBLIC_STMT:
            return this.getPublicStmt();
        case FxtranPackage.FILE_TYPE__READ_STMT:
            return this.getReadStmt();
        case FxtranPackage.FILE_TYPE__SAVE_STMT:
            return this.getSaveStmt();
        case FxtranPackage.FILE_TYPE__SELECT_CASE_STMT:
            return this.getSelectCaseStmt();
        case FxtranPackage.FILE_TYPE__STOP_STMT:
            return this.getStopStmt();
        case FxtranPackage.FILE_TYPE__SUBROUTINE_STMT:
            return this.getSubroutineStmt();
        case FxtranPackage.FILE_TYPE__USE_STMT:
            return this.getUseStmt();
        case FxtranPackage.FILE_TYPE__WHERE_CONSTRUCT_STMT:
            return this.getWhereConstructStmt();
        case FxtranPackage.FILE_TYPE__WRITE_STMT:
            return this.getWriteStmt();
        case FxtranPackage.FILE_TYPE__END_MODULE_STMT:
            return this.getEndModuleStmt();
        case FxtranPackage.FILE_TYPE__END_PROGRAM_STMT:
            return this.getEndProgramStmt();
        case FxtranPackage.FILE_TYPE__NAME:
            return this.getName();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(final int featureID, final Object newValue) {
        switch (featureID) {
        case FxtranPackage.FILE_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.FILE_TYPE__C:
            this.getC().clear();
            this.getC().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__ASTMT:
            this.getAStmt().clear();
            this.getAStmt().addAll((Collection<? extends AStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__ALLOCATE_STMT:
            this.getAllocateStmt().clear();
            this.getAllocateStmt().addAll((Collection<? extends AllocateStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__CALL_STMT:
            this.getCallStmt().clear();
            this.getCallStmt().addAll((Collection<? extends CallStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__DEALLOCATE_STMT:
            this.getDeallocateStmt().clear();
            this.getDeallocateStmt().addAll((Collection<? extends DeallocateStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__EXIT_STMT:
            this.getExitStmt().clear();
            this.getExitStmt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__POINTER_ASTMT:
            this.getPointerAStmt().clear();
            this.getPointerAStmt().addAll((Collection<? extends PointerAStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__RETURN_STMT:
            this.getReturnStmt().clear();
            this.getReturnStmt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__WHERE_STMT:
            this.getWhereStmt().clear();
            this.getWhereStmt().addAll((Collection<? extends WhereStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__TDECL_STMT:
            this.getTDeclStmt().clear();
            this.getTDeclStmt().addAll((Collection<? extends TDeclStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__TSTMT:
            this.getTStmt().clear();
            this.getTStmt().addAll((Collection<? extends TStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__CASE_STMT:
            this.getCaseStmt().clear();
            this.getCaseStmt().addAll((Collection<? extends CaseStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__CLOSE_STMT:
            this.getCloseStmt().clear();
            this.getCloseStmt().addAll((Collection<? extends CloseStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__COMPONENT_DECL_STMT:
            this.getComponentDeclStmt().clear();
            this.getComponentDeclStmt().addAll((Collection<? extends ComponentDeclStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__CONTAINS_STMT:
            this.getContainsStmt().clear();
            this.getContainsStmt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__CPP:
            this.getCpp().clear();
            this.getCpp().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__DO_STMT:
            this.getDoStmt().clear();
            this.getDoStmt().addAll((Collection<? extends DoStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__ELSE_IF_STMT:
            this.getElseIfStmt().clear();
            this.getElseIfStmt().addAll((Collection<? extends ElseIfStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__ELSE_STMT:
            this.getElseStmt().clear();
            this.getElseStmt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__ELSE_WHERE_STMT:
            this.getElseWhereStmt().clear();
            this.getElseWhereStmt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__END_TSTMT:
            this.getEndTStmt().clear();
            this.getEndTStmt().addAll((Collection<? extends EndTStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__END_DO_STMT:
            this.getEndDoStmt().clear();
            this.getEndDoStmt().addAll((Collection<? extends EndDoStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__END_FORALL_STMT:
            this.getEndForallStmt().clear();
            this.getEndForallStmt().addAll((Collection<? extends EndForallStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__END_FUNCTION_STMT:
            this.getEndFunctionStmt().clear();
            this.getEndFunctionStmt().addAll((Collection<? extends EndFunctionStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__END_IF_STMT:
            this.getEndIfStmt().clear();
            this.getEndIfStmt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__END_INTERFACE_STMT:
            this.getEndInterfaceStmt().clear();
            this.getEndInterfaceStmt().addAll((Collection<? extends EndInterfaceStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__END_SELECT_CASE_STMT:
            this.getEndSelectCaseStmt().clear();
            this.getEndSelectCaseStmt().addAll((Collection<? extends EndSelectCaseStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__END_SUBROUTINE_STMT:
            this.getEndSubroutineStmt().clear();
            this.getEndSubroutineStmt().addAll((Collection<? extends EndSubroutineStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__END_WHERE_STMT:
            this.getEndWhereStmt().clear();
            this.getEndWhereStmt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__FORALL_CONSTRUCT_STMT:
            this.getForallConstructStmt().clear();
            this.getForallConstructStmt().addAll((Collection<? extends ForallConstructStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__FORALL_STMT:
            this.getForallStmt().clear();
            this.getForallStmt().addAll((Collection<? extends ForallStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__FUNCTION_STMT:
            this.getFunctionStmt().clear();
            this.getFunctionStmt().addAll((Collection<? extends FunctionStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__IF_STMT:
            this.getIfStmt().clear();
            this.getIfStmt().addAll((Collection<? extends IfStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__IF_THEN_STMT:
            this.getIfThenStmt().clear();
            this.getIfThenStmt().addAll((Collection<? extends IfThenStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__IMPLICIT_NONE_STMT:
            this.getImplicitNoneStmt().clear();
            this.getImplicitNoneStmt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__INQUIRE_STMT:
            this.getInquireStmt().clear();
            this.getInquireStmt().addAll((Collection<? extends InquireStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__INTERFACE_STMT:
            this.getInterfaceStmt().clear();
            this.getInterfaceStmt().addAll((Collection<? extends InterfaceStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__MODULE_STMT:
            this.getModuleStmt().clear();
            this.getModuleStmt().addAll((Collection<? extends ModuleStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__NAMELIST_STMT:
            this.getNamelistStmt().clear();
            this.getNamelistStmt().addAll((Collection<? extends NamelistStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__NULLIFY_STMT:
            this.getNullifyStmt().clear();
            this.getNullifyStmt().addAll((Collection<? extends NullifyStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__OPEN_STMT:
            this.getOpenStmt().clear();
            this.getOpenStmt().addAll((Collection<? extends OpenStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__POINTER_STMT:
            this.getPointerStmt().clear();
            this.getPointerStmt().addAll((Collection<? extends PointerStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__PRIVATE_STMT:
            this.getPrivateStmt().clear();
            this.getPrivateStmt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__PROCEDURE_STMT:
            this.getProcedureStmt().clear();
            this.getProcedureStmt().addAll((Collection<? extends ProcedureStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__PROGRAM_STMT:
            this.getProgramStmt().clear();
            this.getProgramStmt().addAll((Collection<? extends ProgramStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__PUBLIC_STMT:
            this.getPublicStmt().clear();
            this.getPublicStmt().addAll((Collection<? extends PublicStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__READ_STMT:
            this.getReadStmt().clear();
            this.getReadStmt().addAll((Collection<? extends ReadStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__SAVE_STMT:
            this.getSaveStmt().clear();
            this.getSaveStmt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__SELECT_CASE_STMT:
            this.getSelectCaseStmt().clear();
            this.getSelectCaseStmt().addAll((Collection<? extends SelectCaseStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__STOP_STMT:
            this.getStopStmt().clear();
            this.getStopStmt().addAll((Collection<? extends StopStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__SUBROUTINE_STMT:
            this.getSubroutineStmt().clear();
            this.getSubroutineStmt().addAll((Collection<? extends SubroutineStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__USE_STMT:
            this.getUseStmt().clear();
            this.getUseStmt().addAll((Collection<? extends UseStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__WHERE_CONSTRUCT_STMT:
            this.getWhereConstructStmt().clear();
            this.getWhereConstructStmt().addAll((Collection<? extends WhereConstructStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__WRITE_STMT:
            this.getWriteStmt().clear();
            this.getWriteStmt().addAll((Collection<? extends WriteStmtType>) newValue);
            return;
        case FxtranPackage.FILE_TYPE__END_MODULE_STMT:
            this.setEndModuleStmt((EndModuleStmtType) newValue);
            return;
        case FxtranPackage.FILE_TYPE__END_PROGRAM_STMT:
            this.setEndProgramStmt((EndProgramStmtType) newValue);
            return;
        case FxtranPackage.FILE_TYPE__NAME:
            this.setName((String) newValue);
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
        case FxtranPackage.FILE_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.FILE_TYPE__C:
            this.getC().clear();
            return;
        case FxtranPackage.FILE_TYPE__ASTMT:
            this.getAStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__ALLOCATE_STMT:
            this.getAllocateStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__CALL_STMT:
            this.getCallStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__DEALLOCATE_STMT:
            this.getDeallocateStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__EXIT_STMT:
            this.getExitStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__POINTER_ASTMT:
            this.getPointerAStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__RETURN_STMT:
            this.getReturnStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__WHERE_STMT:
            this.getWhereStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__TDECL_STMT:
            this.getTDeclStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__TSTMT:
            this.getTStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__CASE_STMT:
            this.getCaseStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__CLOSE_STMT:
            this.getCloseStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__COMPONENT_DECL_STMT:
            this.getComponentDeclStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__CONTAINS_STMT:
            this.getContainsStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__CPP:
            this.getCpp().clear();
            return;
        case FxtranPackage.FILE_TYPE__DO_STMT:
            this.getDoStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__ELSE_IF_STMT:
            this.getElseIfStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__ELSE_STMT:
            this.getElseStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__ELSE_WHERE_STMT:
            this.getElseWhereStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__END_TSTMT:
            this.getEndTStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__END_DO_STMT:
            this.getEndDoStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__END_FORALL_STMT:
            this.getEndForallStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__END_FUNCTION_STMT:
            this.getEndFunctionStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__END_IF_STMT:
            this.getEndIfStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__END_INTERFACE_STMT:
            this.getEndInterfaceStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__END_SELECT_CASE_STMT:
            this.getEndSelectCaseStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__END_SUBROUTINE_STMT:
            this.getEndSubroutineStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__END_WHERE_STMT:
            this.getEndWhereStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__FORALL_CONSTRUCT_STMT:
            this.getForallConstructStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__FORALL_STMT:
            this.getForallStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__FUNCTION_STMT:
            this.getFunctionStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__IF_STMT:
            this.getIfStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__IF_THEN_STMT:
            this.getIfThenStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__IMPLICIT_NONE_STMT:
            this.getImplicitNoneStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__INQUIRE_STMT:
            this.getInquireStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__INTERFACE_STMT:
            this.getInterfaceStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__MODULE_STMT:
            this.getModuleStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__NAMELIST_STMT:
            this.getNamelistStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__NULLIFY_STMT:
            this.getNullifyStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__OPEN_STMT:
            this.getOpenStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__POINTER_STMT:
            this.getPointerStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__PRIVATE_STMT:
            this.getPrivateStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__PROCEDURE_STMT:
            this.getProcedureStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__PROGRAM_STMT:
            this.getProgramStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__PUBLIC_STMT:
            this.getPublicStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__READ_STMT:
            this.getReadStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__SAVE_STMT:
            this.getSaveStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__SELECT_CASE_STMT:
            this.getSelectCaseStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__STOP_STMT:
            this.getStopStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__SUBROUTINE_STMT:
            this.getSubroutineStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__USE_STMT:
            this.getUseStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__WHERE_CONSTRUCT_STMT:
            this.getWhereConstructStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__WRITE_STMT:
            this.getWriteStmt().clear();
            return;
        case FxtranPackage.FILE_TYPE__END_MODULE_STMT:
            this.setEndModuleStmt((EndModuleStmtType) null);
            return;
        case FxtranPackage.FILE_TYPE__END_PROGRAM_STMT:
            this.setEndProgramStmt((EndProgramStmtType) null);
            return;
        case FxtranPackage.FILE_TYPE__NAME:
            this.setName(NAME_EDEFAULT);
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
        case FxtranPackage.FILE_TYPE__GROUP:
            return this.group != null && !this.group.isEmpty();
        case FxtranPackage.FILE_TYPE__C:
            return !this.getC().isEmpty();
        case FxtranPackage.FILE_TYPE__ASTMT:
            return !this.getAStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__ALLOCATE_STMT:
            return !this.getAllocateStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__CALL_STMT:
            return !this.getCallStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__DEALLOCATE_STMT:
            return !this.getDeallocateStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__EXIT_STMT:
            return !this.getExitStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__POINTER_ASTMT:
            return !this.getPointerAStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__RETURN_STMT:
            return !this.getReturnStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__WHERE_STMT:
            return !this.getWhereStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__TDECL_STMT:
            return !this.getTDeclStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__TSTMT:
            return !this.getTStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__CASE_STMT:
            return !this.getCaseStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__CLOSE_STMT:
            return !this.getCloseStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__COMPONENT_DECL_STMT:
            return !this.getComponentDeclStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__CONTAINS_STMT:
            return !this.getContainsStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__CPP:
            return !this.getCpp().isEmpty();
        case FxtranPackage.FILE_TYPE__DO_STMT:
            return !this.getDoStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__ELSE_IF_STMT:
            return !this.getElseIfStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__ELSE_STMT:
            return !this.getElseStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__ELSE_WHERE_STMT:
            return !this.getElseWhereStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__END_TSTMT:
            return !this.getEndTStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__END_DO_STMT:
            return !this.getEndDoStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__END_FORALL_STMT:
            return !this.getEndForallStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__END_FUNCTION_STMT:
            return !this.getEndFunctionStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__END_IF_STMT:
            return !this.getEndIfStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__END_INTERFACE_STMT:
            return !this.getEndInterfaceStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__END_SELECT_CASE_STMT:
            return !this.getEndSelectCaseStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__END_SUBROUTINE_STMT:
            return !this.getEndSubroutineStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__END_WHERE_STMT:
            return !this.getEndWhereStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__FORALL_CONSTRUCT_STMT:
            return !this.getForallConstructStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__FORALL_STMT:
            return !this.getForallStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__FUNCTION_STMT:
            return !this.getFunctionStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__IF_STMT:
            return !this.getIfStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__IF_THEN_STMT:
            return !this.getIfThenStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__IMPLICIT_NONE_STMT:
            return !this.getImplicitNoneStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__INQUIRE_STMT:
            return !this.getInquireStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__INTERFACE_STMT:
            return !this.getInterfaceStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__MODULE_STMT:
            return !this.getModuleStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__NAMELIST_STMT:
            return !this.getNamelistStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__NULLIFY_STMT:
            return !this.getNullifyStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__OPEN_STMT:
            return !this.getOpenStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__POINTER_STMT:
            return !this.getPointerStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__PRIVATE_STMT:
            return !this.getPrivateStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__PROCEDURE_STMT:
            return !this.getProcedureStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__PROGRAM_STMT:
            return !this.getProgramStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__PUBLIC_STMT:
            return !this.getPublicStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__READ_STMT:
            return !this.getReadStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__SAVE_STMT:
            return !this.getSaveStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__SELECT_CASE_STMT:
            return !this.getSelectCaseStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__STOP_STMT:
            return !this.getStopStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__SUBROUTINE_STMT:
            return !this.getSubroutineStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__USE_STMT:
            return !this.getUseStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__WHERE_CONSTRUCT_STMT:
            return !this.getWhereConstructStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__WRITE_STMT:
            return !this.getWriteStmt().isEmpty();
        case FxtranPackage.FILE_TYPE__END_MODULE_STMT:
            return this.endModuleStmt != null;
        case FxtranPackage.FILE_TYPE__END_PROGRAM_STMT:
            return this.endProgramStmt != null;
        case FxtranPackage.FILE_TYPE__NAME:
            return NAME_EDEFAULT == null ? this.name != null : !NAME_EDEFAULT.equals(this.name);
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
        result.append(" (group: ");
        result.append(this.group);
        result.append(", name: ");
        result.append(this.name);
        result.append(')');
        return result.toString();
    }

} // FileTypeImpl
