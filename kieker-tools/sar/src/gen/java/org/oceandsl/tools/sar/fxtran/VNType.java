/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>VN Type</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.VNType#getVN <em>VN</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.VNType#getN <em>N</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getVNType()
 * @model extendedMetaData="name='V-N_._type' kind='elementOnly'"
 * @generated
 */
public interface VNType extends EObject {
    /**
     * Returns the value of the '<em><b>VN</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>VN</em>' containment reference.
     * @see #setVN(VNType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getVNType_VN()
     * @model containment="true" extendedMetaData="kind='element' name='V-N'
     *        namespace='##targetNamespace'"
     * @generated
     */
    VNType getVN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.VNType#getVN <em>VN</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>VN</em>' containment reference.
     * @see #getVN()
     * @generated
     */
    void setVN(VNType value);

    /**
     * Returns the value of the '<em><b>N</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>N</em>' attribute.
     * @see #setN(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getVNType_N()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NCName" extendedMetaData="kind='element'
     *        name='n' namespace='##targetNamespace'"
     * @generated
     */
    String getN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.VNType#getN <em>N</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>N</em>' attribute.
     * @see #getN()
     * @generated
     */
    void setN(String value);

} // VNType
