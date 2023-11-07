/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Rename Type</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.RenameType#getUseN <em>Use N</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getRenameType()
 * @model extendedMetaData="name='rename_._type' kind='elementOnly'"
 * @generated
 */
public interface RenameType extends EObject {
    /**
     * Returns the value of the '<em><b>Use N</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Use N</em>' containment reference.
     * @see #setUseN(UseNType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getRenameType_UseN()
     * @model containment="true" required="true" extendedMetaData="kind='element' name='use-N'
     *        namespace='##targetNamespace'"
     * @generated
     */
    UseNType getUseN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.RenameType#getUseN <em>Use
     * N</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Use N</em>' containment reference.
     * @see #getUseN()
     * @generated
     */
    void setUseN(UseNType value);

} // RenameType
