/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Case Value Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.CaseValueType#getLiteralE <em>Literal E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.CaseValueType#getStringE <em>String E</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCaseValueType()
 * @model extendedMetaData="name='case-value_._type' kind='elementOnly'"
 * @generated
 */
public interface CaseValueType extends EObject {
    /**
     * Returns the value of the '<em><b>Literal E</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Literal E</em>' containment reference.
     * @see #setLiteralE(LiteralEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCaseValueType_LiteralE()
     * @model containment="true" extendedMetaData="kind='element' name='literal-E'
     *        namespace='##targetNamespace'"
     * @generated
     */
    LiteralEType getLiteralE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.CaseValueType#getLiteralE
     * <em>Literal E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Literal E</em>' containment reference.
     * @see #getLiteralE()
     * @generated
     */
    void setLiteralE(LiteralEType value);

    /**
     * Returns the value of the '<em><b>String E</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>String E</em>' containment reference.
     * @see #setStringE(StringEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCaseValueType_StringE()
     * @model containment="true" extendedMetaData="kind='element' name='string-E'
     *        namespace='##targetNamespace'"
     * @generated
     */
    StringEType getStringE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.CaseValueType#getStringE
     * <em>String E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>String E</em>' containment reference.
     * @see #getStringE()
     * @generated
     */
    void setStringE(StringEType value);

} // CaseValueType
