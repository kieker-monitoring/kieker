/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Pointer AStmt
 * Type</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.PointerAStmtType#getE1 <em>E1</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.PointerAStmtType#getA <em>A</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.PointerAStmtType#getE2 <em>E2</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getPointerAStmtType()
 * @model extendedMetaData="name='pointer-a-stmt_._type' kind='elementOnly'"
 * @generated
 */
public interface PointerAStmtType extends EObject {
    /**
     * Returns the value of the '<em><b>E1</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>E1</em>' containment reference.
     * @see #setE1(E1Type)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getPointerAStmtType_E1()
     * @model containment="true" required="true" extendedMetaData="kind='element' name='E-1'
     *        namespace='##targetNamespace'"
     * @generated
     */
    E1Type getE1();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.PointerAStmtType#getE1
     * <em>E1</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>E1</em>' containment reference.
     * @see #getE1()
     * @generated
     */
    void setE1(E1Type value);

    /**
     * Returns the value of the '<em><b>A</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>A</em>' attribute.
     * @see #setA(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getPointerAStmtType_A()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='a' namespace='##targetNamespace'"
     * @generated
     */
    String getA();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.PointerAStmtType#getA
     * <em>A</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>A</em>' attribute.
     * @see #getA()
     * @generated
     */
    void setA(String value);

    /**
     * Returns the value of the '<em><b>E2</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>E2</em>' containment reference.
     * @see #setE2(E2Type)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getPointerAStmtType_E2()
     * @model containment="true" required="true" extendedMetaData="kind='element' name='E-2'
     *        namespace='##targetNamespace'"
     * @generated
     */
    E2Type getE2();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.PointerAStmtType#getE2
     * <em>E2</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>E2</em>' containment reference.
     * @see #getE2()
     * @generated
     */
    void setE2(E2Type value);

} // PointerAStmtType
