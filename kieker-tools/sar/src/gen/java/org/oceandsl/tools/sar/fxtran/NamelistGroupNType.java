/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Namelist Group
 * NType</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.NamelistGroupNType#getN <em>N</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getNamelistGroupNType()
 * @model extendedMetaData="name='namelist-group-N_._type' kind='elementOnly'"
 * @generated
 */
public interface NamelistGroupNType extends EObject {
    /**
     * Returns the value of the '<em><b>N</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>N</em>' attribute.
     * @see #setN(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getNamelistGroupNType_N()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NCName" required="true"
     *        extendedMetaData="kind='element' name='n' namespace='##targetNamespace'"
     * @generated
     */
    String getN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupNType#getN
     * <em>N</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>N</em>' attribute.
     * @see #getN()
     * @generated
     */
    void setN(String value);

} // NamelistGroupNType
