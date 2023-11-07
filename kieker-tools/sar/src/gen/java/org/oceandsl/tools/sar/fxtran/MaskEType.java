/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Mask EType</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.MaskEType#getOpE <em>Op E</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getMaskEType()
 * @model extendedMetaData="name='mask-E_._type' kind='elementOnly'"
 * @generated
 */
public interface MaskEType extends EObject {
    /**
     * Returns the value of the '<em><b>Op E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Op E</em>' containment reference.
     * @see #setOpE(OpEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getMaskEType_OpE()
     * @model containment="true" required="true" extendedMetaData="kind='element' name='op-E'
     *        namespace='##targetNamespace'"
     * @generated
     */
    OpEType getOpE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.MaskEType#getOpE <em>Op E</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Op E</em>' containment reference.
     * @see #getOpE()
     * @generated
     */
    void setOpE(OpEType value);

} // MaskEType
