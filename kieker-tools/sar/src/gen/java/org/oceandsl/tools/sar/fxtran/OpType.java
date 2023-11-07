/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Op Type</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.OpType#getO <em>O</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getOpType()
 * @model extendedMetaData="name='op_._type' kind='elementOnly'"
 * @generated
 */
public interface OpType extends EObject {
    /**
     * Returns the value of the '<em><b>O</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>O</em>' attribute.
     * @see #setO(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getOpType_O()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='o' namespace='##targetNamespace'"
     * @generated
     */
    String getO();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.OpType#getO <em>O</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>O</em>' attribute.
     * @see #getO()
     * @generated
     */
    void setO(String value);

} // OpType
