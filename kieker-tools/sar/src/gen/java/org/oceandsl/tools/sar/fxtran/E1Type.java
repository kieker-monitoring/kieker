/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>E1 Type</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.E1Type#getNamedE <em>Named E</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getE1Type()
 * @model extendedMetaData="name='E-1_._type' kind='elementOnly'"
 * @generated
 */
public interface E1Type extends EObject {
    /**
     * Returns the value of the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Named E</em>' containment reference.
     * @see #setNamedE(NamedEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getE1Type_NamedE()
     * @model containment="true" required="true" extendedMetaData="kind='element' name='named-E'
     *        namespace='##targetNamespace'"
     * @generated
     */
    NamedEType getNamedE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.E1Type#getNamedE <em>Named
     * E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Named E</em>' containment reference.
     * @see #getNamedE()
     * @generated
     */
    void setNamedE(NamedEType value);

} // E1Type
