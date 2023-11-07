/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Intrinsic TSpec
 * Type</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType#getTN <em>TN</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType#getKSelector <em>KSelector</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType#getCharSelector <em>Char
 * Selector</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getIntrinsicTSpecType()
 * @model extendedMetaData="name='intrinsic-T-spec_._type' kind='elementOnly'"
 * @generated
 */
public interface IntrinsicTSpecType extends EObject {
    /**
     * Returns the value of the '<em><b>TN</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>TN</em>' containment reference.
     * @see #setTN(TNType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getIntrinsicTSpecType_TN()
     * @model containment="true" required="true" extendedMetaData="kind='element' name='T-N'
     *        namespace='##targetNamespace'"
     * @generated
     */
    TNType getTN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType#getTN
     * <em>TN</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>TN</em>' containment reference.
     * @see #getTN()
     * @generated
     */
    void setTN(TNType value);

    /**
     * Returns the value of the '<em><b>KSelector</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>KSelector</em>' containment reference.
     * @see #setKSelector(KSelectorType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getIntrinsicTSpecType_KSelector()
     * @model containment="true" extendedMetaData="kind='element' name='K-selector'
     *        namespace='##targetNamespace'"
     * @generated
     */
    KSelectorType getKSelector();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType#getKSelector
     * <em>KSelector</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>KSelector</em>' containment reference.
     * @see #getKSelector()
     * @generated
     */
    void setKSelector(KSelectorType value);

    /**
     * Returns the value of the '<em><b>Char Selector</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Char Selector</em>' containment reference.
     * @see #setCharSelector(CharSelectorType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getIntrinsicTSpecType_CharSelector()
     * @model containment="true" extendedMetaData="kind='element' name='char-selector'
     *        namespace='##targetNamespace'"
     * @generated
     */
    CharSelectorType getCharSelector();

    /**
     * Sets the value of the
     * '{@link org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType#getCharSelector <em>Char
     * Selector</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Char Selector</em>' containment reference.
     * @see #getCharSelector()
     * @generated
     */
    void setCharSelector(CharSelectorType value);

} // IntrinsicTSpecType
