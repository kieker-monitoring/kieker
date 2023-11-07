/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>TSpec Type</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.TSpecType#getDerivedTSpec <em>Derived TSpec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.TSpecType#getIntrinsicTSpec <em>Intrinsic
 * TSpec</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getTSpecType()
 * @model extendedMetaData="name='_T-spec__._type' kind='elementOnly'"
 * @generated
 */
public interface TSpecType extends EObject {
    /**
     * Returns the value of the '<em><b>Derived TSpec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Derived TSpec</em>' containment reference.
     * @see #setDerivedTSpec(DerivedTSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getTSpecType_DerivedTSpec()
     * @model containment="true" extendedMetaData="kind='element' name='derived-T-spec'
     *        namespace='##targetNamespace'"
     * @generated
     */
    DerivedTSpecType getDerivedTSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.TSpecType#getDerivedTSpec
     * <em>Derived TSpec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Derived TSpec</em>' containment reference.
     * @see #getDerivedTSpec()
     * @generated
     */
    void setDerivedTSpec(DerivedTSpecType value);

    /**
     * Returns the value of the '<em><b>Intrinsic TSpec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Intrinsic TSpec</em>' containment reference.
     * @see #setIntrinsicTSpec(IntrinsicTSpecType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getTSpecType_IntrinsicTSpec()
     * @model containment="true" extendedMetaData="kind='element' name='intrinsic-T-spec'
     *        namespace='##targetNamespace'"
     * @generated
     */
    IntrinsicTSpecType getIntrinsicTSpec();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.TSpecType#getIntrinsicTSpec
     * <em>Intrinsic TSpec</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Intrinsic TSpec</em>' containment reference.
     * @see #getIntrinsicTSpec()
     * @generated
     */
    void setIntrinsicTSpec(IntrinsicTSpecType value);

} // TSpecType
