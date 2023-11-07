/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Case EType</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.CaseEType#getNamedE <em>Named E</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCaseEType()
 * @model extendedMetaData="name='case-E_._type' kind='elementOnly'"
 * @generated
 */
public interface CaseEType extends EObject {
    /**
     * Returns the value of the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Named E</em>' containment reference.
     * @see #setNamedE(NamedEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCaseEType_NamedE()
     * @model containment="true" required="true" extendedMetaData="kind='element' name='named-E'
     *        namespace='##targetNamespace'"
     * @generated
     */
    NamedEType getNamedE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.CaseEType#getNamedE <em>Named
     * E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Named E</em>' containment reference.
     * @see #getNamedE()
     * @generated
     */
    void setNamedE(NamedEType value);

} // CaseEType
