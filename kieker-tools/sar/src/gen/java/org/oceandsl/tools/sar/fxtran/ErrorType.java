/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Error Type</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ErrorType#getMsg <em>Msg</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getErrorType()
 * @model extendedMetaData="name='error_._type' kind='empty'"
 * @generated
 */
public interface ErrorType extends EObject {
    /**
     * Returns the value of the '<em><b>Msg</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Msg</em>' attribute.
     * @see #setMsg(Object)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getErrorType_Msg()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType" required="true"
     *        extendedMetaData="kind='attribute' name='msg'"
     * @generated
     */
    Object getMsg();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ErrorType#getMsg <em>Msg</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Msg</em>' attribute.
     * @see #getMsg()
     * @generated
     */
    void setMsg(Object value);

} // ErrorType
