/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>String EType</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.StringEType#getS <em>S</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getStringEType()
 * @model extendedMetaData="name='string-E_._type' kind='elementOnly'"
 * @generated
 */
public interface StringEType extends EObject {
    /**
     * Returns the value of the '<em><b>S</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>S</em>' attribute.
     * @see #setS(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getStringEType_S()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='S' namespace='##targetNamespace'"
     * @generated
     */
    String getS();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.StringEType#getS <em>S</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>S</em>' attribute.
     * @see #getS()
     * @generated
     */
    void setS(String value);

} // StringEType
