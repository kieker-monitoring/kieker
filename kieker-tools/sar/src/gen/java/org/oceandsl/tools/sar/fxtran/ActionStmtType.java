/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Action Stmt Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getReturnStmt <em>Return Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getWhereStmt <em>Where Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getAStmt <em>AStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getAllocateStmt <em>Allocate
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getCallStmt <em>Call Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getDeallocateStmt <em>Deallocate
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getExitStmt <em>Exit Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getPointerAStmt <em>Pointer
 * AStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getCycleStmt <em>Cycle Stmt</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getActionStmtType()
 * @model extendedMetaData="name='action-stmt_._type' kind='elementOnly'"
 * @generated
 */
public interface ActionStmtType extends EObject {
    /**
     * Returns the value of the '<em><b>Return Stmt</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Return Stmt</em>' attribute.
     * @see #setReturnStmt(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getActionStmtType_ReturnStmt()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NCName" extendedMetaData="kind='element'
     *        name='return-stmt' namespace='##targetNamespace'"
     * @generated
     */
    String getReturnStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getReturnStmt
     * <em>Return Stmt</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Return Stmt</em>' attribute.
     * @see #getReturnStmt()
     * @generated
     */
    void setReturnStmt(String value);

    /**
     * Returns the value of the '<em><b>Where Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Where Stmt</em>' containment reference.
     * @see #setWhereStmt(WhereStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getActionStmtType_WhereStmt()
     * @model containment="true" extendedMetaData="kind='element' name='where-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    WhereStmtType getWhereStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getWhereStmt
     * <em>Where Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Where Stmt</em>' containment reference.
     * @see #getWhereStmt()
     * @generated
     */
    void setWhereStmt(WhereStmtType value);

    /**
     * Returns the value of the '<em><b>AStmt</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>AStmt</em>' containment reference.
     * @see #setAStmt(AStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getActionStmtType_AStmt()
     * @model containment="true" extendedMetaData="kind='element' name='a-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    AStmtType getAStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getAStmt
     * <em>AStmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>AStmt</em>' containment reference.
     * @see #getAStmt()
     * @generated
     */
    void setAStmt(AStmtType value);

    /**
     * Returns the value of the '<em><b>Allocate Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Allocate Stmt</em>' containment reference.
     * @see #setAllocateStmt(AllocateStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getActionStmtType_AllocateStmt()
     * @model containment="true" extendedMetaData="kind='element' name='allocate-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    AllocateStmtType getAllocateStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getAllocateStmt
     * <em>Allocate Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Allocate Stmt</em>' containment reference.
     * @see #getAllocateStmt()
     * @generated
     */
    void setAllocateStmt(AllocateStmtType value);

    /**
     * Returns the value of the '<em><b>Call Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Call Stmt</em>' containment reference.
     * @see #setCallStmt(CallStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getActionStmtType_CallStmt()
     * @model containment="true" extendedMetaData="kind='element' name='call-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    CallStmtType getCallStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getCallStmt
     * <em>Call Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Call Stmt</em>' containment reference.
     * @see #getCallStmt()
     * @generated
     */
    void setCallStmt(CallStmtType value);

    /**
     * Returns the value of the '<em><b>Deallocate Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Deallocate Stmt</em>' containment reference.
     * @see #setDeallocateStmt(DeallocateStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getActionStmtType_DeallocateStmt()
     * @model containment="true" extendedMetaData="kind='element' name='deallocate-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    DeallocateStmtType getDeallocateStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getDeallocateStmt
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
     * Returns the value of the '<em><b>Exit Stmt</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Exit Stmt</em>' attribute.
     * @see #setExitStmt(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getActionStmtType_ExitStmt()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NCName" extendedMetaData="kind='element'
     *        name='exit-stmt' namespace='##targetNamespace'"
     * @generated
     */
    String getExitStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getExitStmt
     * <em>Exit Stmt</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Exit Stmt</em>' attribute.
     * @see #getExitStmt()
     * @generated
     */
    void setExitStmt(String value);

    /**
     * Returns the value of the '<em><b>Pointer AStmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Pointer AStmt</em>' containment reference.
     * @see #setPointerAStmt(PointerAStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getActionStmtType_PointerAStmt()
     * @model containment="true" extendedMetaData="kind='element' name='pointer-a-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    PointerAStmtType getPointerAStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getPointerAStmt
     * <em>Pointer AStmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Pointer AStmt</em>' containment reference.
     * @see #getPointerAStmt()
     * @generated
     */
    void setPointerAStmt(PointerAStmtType value);

    /**
     * Returns the value of the '<em><b>Cycle Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Cycle Stmt</em>' containment reference.
     * @see #setCycleStmt(CycleStmtType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getActionStmtType_CycleStmt()
     * @model containment="true" extendedMetaData="kind='element' name='cycle-stmt'
     *        namespace='##targetNamespace'"
     * @generated
     */
    CycleStmtType getCycleStmt();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getCycleStmt
     * <em>Cycle Stmt</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Cycle Stmt</em>' containment reference.
     * @see #getCycleStmt()
     * @generated
     */
    void setCycleStmt(CycleStmtType value);

} // ActionStmtType
