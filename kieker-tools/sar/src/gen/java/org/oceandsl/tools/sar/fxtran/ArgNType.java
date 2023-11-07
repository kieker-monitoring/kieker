/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Arg NType</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ArgNType#getN <em>N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ArgNType#getK <em>K</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ArgNType#getN1 <em>N1</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getArgNType()
 * @model extendedMetaData="name='arg-N_._type' kind='elementOnly'"
 * @generated
 */
public interface ArgNType extends EObject {
    /**
     * Returns the value of the '<em><b>N</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>N</em>' containment reference.
     * @see #setN(NType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getArgNType_N()
     * @model containment="true" extendedMetaData="kind='element' name='N'
     *        namespace='##targetNamespace'"
     * @generated
     */
    NType getN();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ArgNType#getN <em>N</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>N</em>' containment reference.
     * @see #getN()
     * @generated
     */
    void setN(NType value);

    /**
     * Returns the value of the '<em><b>K</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>K</em>' attribute.
     * @see #setK(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getArgNType_K()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NCName" extendedMetaData="kind='element'
     *        name='k' namespace='##targetNamespace'"
     * @generated
     */
    String getK();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ArgNType#getK <em>K</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>K</em>' attribute.
     * @see #getK()
     * @generated
     */
    void setK(String value);

    /**
     * Returns the value of the '<em><b>N1</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>N1</em>' attribute.
     * @see #setN1(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getArgNType_N1()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NCName" extendedMetaData="kind='attribute'
     *        name='n'"
     * @generated
     */
    String getN1();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ArgNType#getN1 <em>N1</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>N1</em>' attribute.
     * @see #getN1()
     * @generated
     */
    void setN1(String value);

} // ArgNType
