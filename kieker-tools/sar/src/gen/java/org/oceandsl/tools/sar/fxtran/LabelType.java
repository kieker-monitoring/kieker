/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Label Type</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.LabelType#getError <em>Error</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getLabelType()
 * @model extendedMetaData="name='label_._type' kind='elementOnly'"
 * @generated
 */
public interface LabelType extends EObject {
    /**
     * Returns the value of the '<em><b>Error</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Error</em>' containment reference.
     * @see #setError(ErrorType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getLabelType_Error()
     * @model containment="true" required="true" extendedMetaData="kind='element' name='error'
     *        namespace='##targetNamespace'"
     * @generated
     */
    ErrorType getError();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.LabelType#getError
     * <em>Error</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Error</em>' containment reference.
     * @see #getError()
     * @generated
     */
    void setError(ErrorType value);

} // LabelType
