/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Ac Value Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.AcValueType#getLiteralE <em>Literal E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.AcValueType#getNamedE <em>Named E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.AcValueType#getOpE <em>Op E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.AcValueType#getParensE <em>Parens E</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getAcValueType()
 * @model extendedMetaData="name='ac-value_._type' kind='elementOnly'"
 * @generated
 */
public interface AcValueType extends EObject {
    /**
     * Returns the value of the '<em><b>Literal E</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Literal E</em>' containment reference.
     * @see #setLiteralE(LiteralEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getAcValueType_LiteralE()
     * @model containment="true" extendedMetaData="kind='element' name='literal-E'
     *        namespace='##targetNamespace'"
     * @generated
     */
    LiteralEType getLiteralE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.AcValueType#getLiteralE
     * <em>Literal E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Literal E</em>' containment reference.
     * @see #getLiteralE()
     * @generated
     */
    void setLiteralE(LiteralEType value);

    /**
     * Returns the value of the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Named E</em>' containment reference.
     * @see #setNamedE(NamedEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getAcValueType_NamedE()
     * @model containment="true" extendedMetaData="kind='element' name='named-E'
     *        namespace='##targetNamespace'"
     * @generated
     */
    NamedEType getNamedE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.AcValueType#getNamedE <em>Named
     * E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Named E</em>' containment reference.
     * @see #getNamedE()
     * @generated
     */
    void setNamedE(NamedEType value);

    /**
     * Returns the value of the '<em><b>Op E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Op E</em>' containment reference.
     * @see #setOpE(OpEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getAcValueType_OpE()
     * @model containment="true" extendedMetaData="kind='element' name='op-E'
     *        namespace='##targetNamespace'"
     * @generated
     */
    OpEType getOpE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.AcValueType#getOpE <em>Op
     * E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Op E</em>' containment reference.
     * @see #getOpE()
     * @generated
     */
    void setOpE(OpEType value);

    /**
     * Returns the value of the '<em><b>Parens E</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Parens E</em>' containment reference.
     * @see #setParensE(ParensEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getAcValueType_ParensE()
     * @model containment="true" extendedMetaData="kind='element' name='parens-E'
     *        namespace='##targetNamespace'"
     * @generated
     */
    ParensEType getParensE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.AcValueType#getParensE <em>Parens
     * E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Parens E</em>' containment reference.
     * @see #getParensE()
     * @generated
     */
    void setParensE(ParensEType value);

} // AcValueType
